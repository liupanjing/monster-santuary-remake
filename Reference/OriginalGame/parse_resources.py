"""Read the original resources without executing the game's classes.

Formats recovered from decompiled f.java, m.java and u.java.
Numeric fields retain their original order; names are not guessed.
"""
from pathlib import Path
import json
import struct
import re

ROOT = Path(__file__).resolve().parent
RES = ROOT / 'extracted'

class Reader:
    def __init__(self, data):
        self.data, self.pos = data, 0

    def take(self, n):
        b = self.data[self.pos:self.pos+n]
        if len(b) != n:
            raise ValueError(f'truncated at {self.pos}, expected {n}')
        self.pos += n
        return b

    def num(self, fmt):
        return struct.unpack('>' + fmt, self.take(struct.calcsize(fmt)))[0]

    def utf(self):
        b = self.take(self.num('H'))
        # Java modified UTF-8; normalize its null and UTF-16 surrogate encoding.
        return b.replace(b'\xc0\x80', b'\x00').decode('utf-8', 'surrogatepass').encode('utf-16', 'surrogatepass').decode('utf-16')

def save(name, value):
    (ROOT / name).write_text(json.dumps(value, ensure_ascii=False, indent=2))

scripts = {}
for path in sorted((RES / 'script').glob('*.bin')):
    r = Reader(path.read_bytes())
    rows = []
    while True:
        offset = r.pos
        op = r.num('B')
        if op == 0:
            break
        rows.append({'offset': offset, 'opcode': op, 'depth': r.num('b'),
                     'args': [r.num('h') for _ in range(4)],
                     'text': [r.utf(), r.utf()]})
    assert r.pos == len(r.data), (path.name, r.pos, len(r.data))
    scripts[path.name] = rows
save('scripts.json', scripts)
text = []
for name, rows in scripts.items():
    parts = [f'{row["offset"]}: ' + ' / '.join(s for s in row['text'] if s and s != '=\x00')
             for row in rows if row['opcode'] in (1, 38, 39)
             and any(s and s != '=\x00' for s in row['text'])]
    if parts:
        text.extend([f'[{name}]', *parts, ''])
(ROOT / 'dialogue.txt').write_text('\n'.join(text))

monsters = []
r = Reader((RES / 'mosters/mdata.bin').read_bytes())
while r.pos < len(r.data) - 1:
    monsters.append({'id': r.num('h'), 'name': r.utf(),
                     'numeric_fields_n_to_B': [r.num('h') for _ in range(15)]})
assert r.take(1) == b'\x00' and r.pos == len(r.data)
assert [m['id'] for m in monsters] == list(range(52))
save('monsters.json', monsters)

maps = []
for path in sorted((RES / 'maps').glob('*.bin'), key=lambda p: int(p.stem)):
    r = Reader(path.read_bytes())
    header = [r.num('i') for _ in range(r.num('H'))]
    longs = [r.num('q'), r.num('q')]
    style, other = r.num('B'), r.num('B')
    w, h = r.num('H'), r.num('H')
    cell = [r.num('B'), r.num('B')]
    layers = [[r.num('h') for _ in range(w*h)] for _ in range(2)]
    attributes = list(r.take(w*h))
    assert r.pos == len(r.data), path.name
    maps.append({'id': int(path.stem), 'width_tiles': w, 'height_tiles': h,
                 'tile_pixels_runtime': 16, 'tileset_index': style,
                 'other_byte': other, 'file_cell_bytes': cell,
                 'header_ints': header, 'header_longs': longs,
                 'layers': layers, 'attributes': attributes})
save('maps.json', maps)

# Static script call targets: t.x uses arg0; t.m uses arg3.
t = (ROOT / 'decompiled/t.java').read_text()
handlers = {int(op): handler for op, handler in
            re.findall(r'if \(by == (\d+)\) \{\s*this\.(\w+)\(f2, i2\)', t)}
call_ops = {op: (0 if handler == 'x' else 3) for op, handler in handlers.items()
            if handler in ('x', 'm')}
references = []
for name, rows in scripts.items():
    for row in rows:
        if row['opcode'] in call_ops:
            target = row['args'][call_ops[row['opcode']]]
            if target >= 0:
                references.append({'source': name, 'offset': row['offset'],
                                   'target': target,
                                   'present': f'{target}.bin' in scripts})
save('script-calls.json', references)
summary = {'script_files': len(scripts), 'script_records': sum(map(len, scripts.values())),
           'text_fields': sum(bool(s) for rows in scripts.values() for row in rows for s in row['text']),
           'dialogue_records_opcode_1': sum(row['opcode'] == 1 for rows in scripts.values() for row in rows),
           'monster_records': len(monsters), 'map_files': len(maps),
           'missing_static_script_targets': sorted({x['target'] for x in references if not x['present']})}
save('parsed-summary.json', summary)
print(json.dumps(summary, ensure_ascii=False, indent=2))
print('Monster examples:', [m['name'] for m in monsters[:16]])
