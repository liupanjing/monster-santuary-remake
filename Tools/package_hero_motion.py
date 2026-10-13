"""Package the already-tested player and cache-free repository snapshot."""
from pathlib import Path
import zipfile
import hashlib
import json

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'Production/Deliveries'
OUT.mkdir(exist_ok=True)


def tree(archive,path,prefix):
    for file in sorted(path.rglob('*')):
        if file.is_file() and file.name!='.DS_Store':
            archive.write(file,prefix+'/'+str(file.relative_to(path)))


app=ROOT/'Production/Build/MonsterWonderland2D.app'
assert app.exists()
mac=OUT/'怪兽仙境2D-主角动作版-Mac.zip'
with zipfile.ZipFile(mac,'w',zipfile.ZIP_DEFLATED)as archive:
    tree(archive,app,'MonsterWonderland2D.app')
    archive.write(ROOT/'Art/Reviews/51/v5/README.md','主角动作说明.md')
    archive.write(ROOT/'Art/Reviews/51/v5/hero-motion-preview.gif','主角动作预览.gif')
    archive.writestr('开始试玩.txt','解压后打开 MonsterWonderland2D.app。\nWASD/方向键移动，Shift 奔跑，E 互动，M 地图，Esc 队伍，F5 存档。\n本包为主角 v5 动作开发版：Q 版跑图与双枪战斗使用不同资源，武器独立挂载。\n其他怪兽仍为待逐只审核的造型占位。\n')
source=OUT/'怪兽仙境2D-主角动作版-Unity源工程.zip'
exclude={'.git','Library','Logs','Temp','Obj','UserSettings','__pycache__','Build','Runtime','Deliveries'}
with zipfile.ZipFile(source,'w',zipfile.ZIP_DEFLATED)as archive:
    for file in sorted(ROOT.rglob('*')):
        if not file.is_file():continue
        relative=file.relative_to(ROOT)
        if any(part in exclude for part in relative.parts)or file.suffix in ['.log','.pyc']or file.name=='.DS_Store':continue
        archive.write(file,'monster-sanctuary-remake/'+str(relative))
checks={}
for file in [mac,source]:
    with zipfile.ZipFile(file)as archive:assert archive.testzip()is None
    checks[file.name]={'bytes':file.stat().st_size,'sha256':hashlib.sha256(file.read_bytes()).hexdigest(),'zipCRC':'pass'}
(OUT/'integrity.json').write_text(json.dumps(checks,ensure_ascii=False,indent=2)+'\n')
print(json.dumps({name:row['bytes']for name,row in checks.items()},ensure_ascii=False))
