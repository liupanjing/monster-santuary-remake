"""Check repository migration and explicit art status; never grants visual approval."""
from pathlib import Path
import ast
import hashlib
import json

ROOT = Path(__file__).resolve().parents[1]


def check(ok, message):
    if not ok:
        raise AssertionError(message)


checksums = json.loads((ROOT / 'Reference/original-input-sha256.json').read_text())
for relative, digest in checksums.items():
    path = ROOT / relative
    check(path.is_file(), f'Missing original input: {relative}')
    check(hashlib.sha256(path.read_bytes()).hexdigest() == digest,
          f'Changed original input: {relative}')

review = json.loads((ROOT / 'Art/character-review.json').read_text())
catalog = json.loads((ROOT / 'Unity/Assets/Remake/Resources/Data/catalog.json').read_text())
entries = {row['id']: row for row in catalog['entries']}
rows = review['characters']
check(len(rows) == 51 and set(entries) == {row['id'] for row in rows}, '51 unique roles')
check(sum(row['role'] == 'monster' for row in rows) == 50, '50 monsters')
check(review['currentCharacter'] in entries, 'Current review character exists')
approved = [row for row in rows if row['status'] == 'approved']
check(review['approvedCount'] == len(approved), 'Approved count matches records')
for row in rows:
    check((ROOT / row['reviewFile']).is_file(), f'Review file for {row["id"]}')
    check(bool(row['originalReferences']), f'Original reference for {row["id"]}')
    for relative in row['originalReferences']:
        path = ROOT / relative
        check(path.is_file(), f'Missing reference: {relative}')
        check(hashlib.sha256(path.read_bytes()).hexdigest()
              == row['originalReferenceSHA256'][path.name], f'Reference checksum: {relative}')
    if row['status'] in ['approved', 'design_approved', 'runtime_review']:
        check(row['approvedVersion'] and row['approvedBy'], 'Approval requires version and evidence')
    else:
        check(row['approvedVersion'] is None, 'Pending role cannot claim an approved version')
    entry = entries[row['id']]
    check(entry['artPolicy'] == 'original_reference_review', 'Current reference-based policy')
    check(entry['assetStatus'] == row['runtimeArtStatus'], 'Runtime/review art status agree')
    path = ROOT / 'Unity/Assets/Remake/Resources' / (entry['spriteResource'] + '.png')
    check(path.is_file() and path.with_suffix('.png.meta').is_file(), 'Sprite and Unity metadata')

for i in range(12):
    check((ROOT / f'Unity/Assets/Remake/Resources/Art/Hero/f{i}.png').is_file(), 'Hero frame exists')
check((ROOT / 'Unity/Assets/Remake/Scenes/Earth2D.unity').is_file(), 'Playable scene exists')
for path in (ROOT / 'Tools').glob('*.py'):
    source = path.read_text()
    ast.parse(source, filename=str(path))
    check('/.codex/' + 'visualizations/' not in source, 'No old temporary path in active tools')
check('../remake-2d-production' not in
      (ROOT / 'Unity/Assets/Remake/Editor/Earth2DBuild.cs').read_text(), 'Build output migrated')

report = {
    'result': 'pass', 'originalInputsChecked': len(checksums),
    'characters': len(rows), 'monsters': 50, 'approvedCharacters': len(approved),
    'approvedDesigns': sum(bool(row['approvedVersion']) for row in rows),
    'currentCharacter': review['currentCharacter'], 'spriteMetadata': 'pass',
    'toolsSyntax': 'pass', 'oldActiveToolPaths': 'none',
    'scope': 'Workspace integrity and review bookkeeping; not art approval or Unity runtime replay',
}
(ROOT / 'Production/workspace-validation.json').write_text(
    json.dumps(report, ensure_ascii=False, indent=2) + '\n')
print(json.dumps(report, ensure_ascii=False))
