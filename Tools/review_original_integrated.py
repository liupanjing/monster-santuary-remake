"""Statically archive and compare a user-supplied JAR and DOCX references.

Does not execute game classes or alter Reference/OriginalGame. Parsed fields keep
the historical binary layout; instruction presence is not runtime reachability.
"""
from pathlib import Path
import argparse
import hashlib
import json
import re
import shutil
import struct
import xml.etree.ElementTree as ET
import zipfile

REPO = Path(__file__).resolve().parents[1]
W = "{http://schemas.openxmlformats.org/wordprocessingml/2006/main}"


class Reader:
    def __init__(self, data):
        self.data, self.pos = data, 0

    def take(self, count):
        value = self.data[self.pos:self.pos + count]
        if len(value) != count:
            raise ValueError(f"Truncated binary at {self.pos}, expected {count}")
        self.pos += count
        return value

    def number(self, fmt):
        return struct.unpack(">" + fmt, self.take(struct.calcsize(fmt)))[0]

    def utf(self):
        value = self.take(self.number("H")).replace(b"\xc0\x80", b"\x00")
        return value.decode("utf-8", "surrogatepass").encode(
            "utf-16", "surrogatepass").decode("utf-16")

    def finish(self):
        if self.pos != len(self.data):
            raise ValueError(f"Unparsed tail: {self.pos}/{len(self.data)}")


def sha(data):
    return hashlib.sha256(data).hexdigest()


def save(root, name, data):
    (root / name).write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n")


def archive(source, target):
    if target.exists():
        if sha(source.read_bytes()) != sha(target.read_bytes()):
            raise ValueError(f"Refusing to overwrite different source: {target}")
    else:
        shutil.copyfile(source, target)
    assert sha(source.read_bytes()) == sha(target.read_bytes())


def script(data):
    reader = Reader(data)
    result = []
    while True:
        offset = reader.pos
        opcode = reader.number("B")
        if opcode == 0:
            break
        result.append({"offset": offset, "opcode": opcode,
                       "depth": reader.number("b"),
                       "args": [reader.number("h") for _ in range(4)],
                       "text": [reader.utf(), reader.utf()]})
    reader.finish()
    return result


def map_record(name, data):
    r = Reader(data)
    header = [r.number("i") for _ in range(r.number("H"))]
    longs = [r.number("q"), r.number("q")]
    style, other = r.number("B"), r.number("B")
    width, height = r.number("H"), r.number("H")
    cells = [r.number("B"), r.number("B")]
    layers = [[r.number("h") for _ in range(width * height)] for _ in range(2)]
    attributes = list(r.take(width * height))
    return {"id": int(Path(name).stem), "header_ints": header,
            "header_longs": longs, "tileset_index": style, "other_byte": other,
            "width_tiles": width, "height_tiles": height,
            "file_cell_bytes": cells, "layers": layers, "attributes": attributes,
            "unparsed_tail_bytes": len(r.data) - r.pos,
            "parse_status": "exact" if r.pos == len(r.data) else "trailing_bytes"}


def document(path):
    with zipfile.ZipFile(path) as z:
        if z.testzip() is not None:
            raise ValueError(f"DOCX CRC failure: {path}")
        members = {n.replace("\\", "/"): n for n in z.namelist()}
        root = ET.fromstring(z.read(members["word/document.xml"]))
        paragraphs = []
        for i, p in enumerate(root.iter(W + "p"), 1):
            text = "".join(e.text or "" for e in p.iter()
                           if e.tag in (W + "t", W + "delText"))
            if text:
                paragraphs.append({"paragraph": i, "text": text})
        ancillary = {}
        for name in members:
            if name.startswith("word/") and re.search(
                    r"(comments|footnotes|endnotes|header\d*|footer\d*)\.xml$", name):
                tree = ET.fromstring(z.read(members[name]))
                ancillary[name] = [e.text or "" for e in tree.iter(W + "t")]
        return {"file": path.name, "paragraphs": paragraphs,
                "table_count": sum(1 for _ in root.iter(W + "tbl")),
                "drawing_count": sum(1 for _ in root.iter(W + "drawing")),
                "tracked_insertions": sum(1 for _ in root.iter(W + "ins")),
                "tracked_deletions": sum(1 for _ in root.iter(W + "del")),
                "ancillary_text": ancillary}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input-dir", type=Path, required=True)
    parser.add_argument("--output-dir", type=Path, required=True)
    args = parser.parse_args()
    out = args.output_dir.resolve()
    out.mkdir(parents=True, exist_ok=True)
    source_dir = out / "source"
    source_dir.mkdir(exist_ok=True)
    filenames = ["怪兽仙境2_原版整合.jar", "怪兽仙境(1).docx", "怪兽仙境参考(1).docx"]
    sources = []
    for name in filenames:
        original = args.input_dir / name
        archive(original, source_dir / name)
        value = original.read_bytes()
        sources.append({"file": name, "bytes": len(value), "sha256": sha(value),
                        "provenance": "user_supplied; official completeness not authenticated"})
    save(out, "sources.json", sources)
    with zipfile.ZipFile(source_dir / filenames[0]) as z, zipfile.ZipFile(
            REPO / "Reference/OriginalGame/怪兽仙境2.jar") as old:
        bad = z.testzip()
        if bad is not None:
            raise ValueError(f"JAR CRC failure: {bad}")
        names, old_names = set(z.namelist()), set(old.namelist())
        scripts = {Path(n).name: script(z.read(n)) for n in sorted(names)
                   if n.startswith("script/") and n.endswith(".bin")}
        maps = [map_record(n, z.read(n)) for n in sorted(names)
                if re.fullmatch(r"maps/\d+\.bin", n)]
        changed = [{"file": n, "old_sha256": sha(old.read(n)),
                    "new_sha256": sha(z.read(n))} for n in sorted(names & old_names)
                   if z.read(n) != old.read(n)]
        unchanged = sorted(n for n in names & old_names if z.read(n) == old.read(n))
        save(out, "package-comparison.json", {"old_entries": len(old_names),
             "new_entries": len(names), "added": sorted(names - old_names),
             "removed": sorted(old_names - names), "changed": changed,
             "unchanged": unchanged,
             "new_manifest": z.read("META-INF/MANIFEST.MF").decode("utf-8", "replace")})
        save(out, "scripts.json", scripts)
        save(out, "maps.json", maps)
        map_names = [{"script": n, "offset": row["offset"], "name": row["text"][0]}
                     for n, rows in scripts.items() if re.fullmatch(r"m\d+\.bin", n)
                     for row in rows if row["opcode"] == 51]
        save(out, "map-names.json", map_names)
        calls = [{"script": n, "offset": row["offset"], "target": row["args"][0],
                  "present": f'{row["args"][0]}.bin' in scripts}
                 for n, rows in scripts.items() for row in rows
                 if row["opcode"] == 29 and row["args"][0] >= 0]
        save(out, "static-calls-op29.json", calls)
        text = []
        for n, rows in scripts.items():
            lines = [f'{r["offset"]} op{r["opcode"]}: ' + " / ".join(
                     t for t in r["text"] if t and t != "=\x00")
                     for r in rows if any(t and t != "=\x00" for t in r["text"])]
            if lines:
                text.extend([f"[{n}]", *lines, ""])
        (out / "script-text.txt").write_text("\n".join(text))
        summary = {"jar_bytes": (source_dir / filenames[0]).stat().st_size,
                   "entries": len(names), "script_files": len(scripts),
                   "script_instructions": sum(map(len, scripts.values())),
                   "dialogue_and_prompt_instructions": sum(
                       r["opcode"] in (1, 38, 39) and any(t and t != "=\x00" for t in r["text"])
                       for rows in scripts.values() for r in rows),
                   "map_files": len(maps),
                   "scene_files": sum(bool(re.fullmatch(r"scenes/\d+\.bin", n)) for n in names),
                   "png_files": sum(n.endswith(".png") for n in names),
                   "map_parse_issues": [{"id": m["id"], "width_tiles": m["width_tiles"],
                       "height_tiles": m["height_tiles"], "unparsed_tail_bytes": m["unparsed_tail_bytes"]}
                       for m in maps if m["parse_status"] != "exact"],
                   "missing_op29_targets": sorted({c["target"] for c in calls if not c["present"]}),
                   "script_added": sum(n.startswith("script/") for n in names - old_names),
                   "script_changed": [c["file"] for c in changed if c["file"].startswith("script/")],
                   "data_resources_byte_identical": [n for n in unchanged
                       if n.startswith(("mosters/", "skill", "item", "basic"))],
                   "validation": "CRC passed; every script consumed exactly; map tails reported; no game execution"}
        save(out, "review-summary.json", summary)
    docs = [document(source_dir / n) for n in filenames[1:]]
    save(out, "documents.json", docs)
    for doc in docs:
        (out / (Path(doc["file"]).stem + ".txt")).write_text("\n".join(
            f'P{p["paragraph"]}: {p["text"]}' for p in doc["paragraphs"]))
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    print("DOCX", [(d["file"], len(d["paragraphs"]), d["table_count"],
                     d["tracked_insertions"], d["tracked_deletions"]) for d in docs])


if __name__ == "__main__":
    main()
