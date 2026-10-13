"""Package the verified Babao player and cache-free Unity source snapshot."""
from pathlib import Path
import argparse
import hashlib
import json
import subprocess
import zipfile

ROOT = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser()
parser.add_argument('--player-path', type=Path, default=ROOT / 'Production/Build/MonsterWonderland2D.app')
options = parser.parse_args()
OUT = ROOT / 'Production/Deliveries'
OUT.mkdir(exist_ok=True)
REVIEW = ROOT / 'Art/Companions/babao/v3'
for marker, log in [('BABAO_MAC_BUILD_PASSED', 'babao-build.log'),
                    ('BABAO_MOTION_RUNTIME_PASSED', 'babao-preview.log'),
                    ('EARTH_2D_RUNTIME_PASSED', 'earth-babao-v3.log')]:
    assert marker in (ROOT / 'Production' / log).read_text(), marker
build_time = (ROOT / 'Production/babao-build.log').stat().st_mtime
for log in ('babao-preview.log', 'earth-babao-v3.log'):
    assert (ROOT / 'Production' / log).stat().st_mtime >= build_time, 'Runtime evidence predates current build'
app = options.player_path.resolve()
assert app.is_dir(), 'Verified player path required'
mac = OUT / '怪兽仙境2D-八宝跟随版-Mac.zip'
with zipfile.ZipFile(mac, 'w', zipfile.ZIP_DEFLATED) as archive:
    for path in sorted(app.rglob('*')):
        if path.is_file() and path.name != '.DS_Store':
            archive.write(path, 'MonsterWonderland2D.app/' + str(path.relative_to(app)))
    archive.write(REVIEW / 'README.md', '八宝动作说明.md')
    archive.write(REVIEW / 'babao-motion-preview.gif', '八宝动作预览.gif')
    archive.writestr('开始试玩.txt', '解压后打开 MonsterWonderland2D.app。\nWASD/方向键移动，Shift 奔跑，E 互动，M 地图，Esc 队伍，F5 存档。\n主角使用已有 Q 版跑图和模块化双枪战斗动作。八宝四方向悬浮扇翅，独立跟随，不随参战怪兽选择改变。\n新动作待视觉评价，其余怪兽仍为逐只待审占位。本包为 Mac 开发试玩版。\n')
source = OUT / '怪兽仙境2D-八宝跟随版-Unity源工程.zip'
# Export a reproducible commit, rather than collecting concurrently edited files.
revision = subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT, text=True).strip()
subprocess.run(['git', 'archive', '--format=zip', '--prefix=monster-sanctuary-remake/',
                '--output=' + str(source), revision], cwd=ROOT, check=True)
checks = {}
for path in (mac, source):
    with zipfile.ZipFile(path) as archive:
        assert archive.testzip() is None
    checks[path.name] = {'bytes': path.stat().st_size, 'sha256': hashlib.sha256(path.read_bytes()).hexdigest(), 'zipCRC': 'pass'}
(OUT / 'babao-integrity.json').write_text(json.dumps({'sourceCommit': revision, 'files': checks}, ensure_ascii=False, indent=2) + '\n')
print(json.dumps(checks, ensure_ascii=False))
