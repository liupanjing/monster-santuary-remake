"""Package actual Unity captures of the Xiya sword-grip revision, without approval."""
from pathlib import Path
import json, shutil, hashlib
from PIL import Image, ImageDraw, ImageFont, ImageChops
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'Art/NPCs/xiya/v4'
SOURCE=ROOT/'Production/Runtime/xiya-v4-final'
FONT=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',22)
NAMES=['预备','提剑','蓄力','出剑','斜斩','收势','回收','预备']

def main():
    assert 'PASS:' in (SOURCE/'runtime-validation.txt').read_text()
    files=list(SOURCE.rglob('*.png')); assert len(files)==364,len(files)
    report={'humanApproved':False,'capture':'Unity Camera.Render','rawFrames':len(files),'uniqueBattleDrawings':9,'battlePoseSlots':11,'source':str(SOURCE.relative_to(ROOT)),'rawSHA256':{},'gripComparison':[]}
    for p in files:
        rel=p.relative_to(SOURCE);dest=OUT/'captures'/rel;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,dest)
        web=OUT/'web'/rel.with_suffix('.webp');web.parent.mkdir(parents=True,exist_ok=True)
        Image.open(p).convert('RGB').save(web,lossless=True)
        report['rawSHA256'][str(rel)]=hashlib.sha256(p.read_bytes()).hexdigest()
    shutil.copy2(SOURCE/'runtime-validation.txt',OUT/'runtime-validation.txt')
    for p in sorted((SOURCE/'grips').rglob('*.png')):
        master=SOURCE/'master-grips'/p.relative_to(SOURCE/'grips')
        diff=ImageChops.difference(Image.open(p),Image.open(master))
        stat=diff.getbbox(); report['gripComparison'].append({'pose':str(p.relative_to(SOURCE/'grips')),'differentBounds':stat})
    # Every sheet uses a common crop and scale; no per-pose zooming.
    board=Image.new('RGB',(1280,820),'#e8e6df');draw=ImageDraw.Draw(board)
    hands=Image.new('RGB',(1600,940),'#e8e6df');hd=ImageDraw.Draw(hands)
    for i in range(8):
        p=SOURCE/f'poses/battle-slash/frame-{i:03d}.png'
        im=Image.open(p).crop((150,0,1030,710));im.thumbnail((320,365))
        x=i%4*320;y=i//4*410;board.paste(im,(x,y));draw.text((x+12,y+367),f'{i+1} · {NAMES[i]}',font=FONT,fill='#273b30')
        im=Image.open(SOURCE/f'grips/battle-slash/frame-{i:03d}.png').crop((400,140,880,600));im.thumbnail((390,420))
        x=i%4*400;y=i//4*470;hands.paste(im,(x,y));hd.text((x+12,y+425),f'{i+1} · {NAMES[i]}',font=FONT,fill='#273b30')
    board.save(OUT/'battle-unity-contact.png');hands.save(OUT/'grip-unity-contact.png')
    frames=[]
    for i in range(64):
        im=Image.open(SOURCE/f'mode-0/frame-{i:03d}.png').convert('RGB');im.thumbnail((960,540))
        b=Image.new('RGB',(960,595),'#e8e6df');b.paste(im,(0,55));d=ImageDraw.Draw(b);d.text((20,15),'西雅 v4 · Unity 实际播放 · 待机 / 斩击 / 受击',font=FONT,fill='#273b30');frames.append(b)
    pal=frames[24].quantize(colors=240);frames=[im.quantize(palette=pal,dither=Image.Dither.NONE) for im in frames]
    frames[0].save(OUT/'battle-unity-preview.gif',save_all=True,append_images=frames[1:],duration=[60,70,60,60]*16,loop=0,disposal=2,optimize=False)
    (OUT/'runtime-capture-manifest.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
    print('Packaged',len(files),'actual Unity renders; nine unique battle drawings; humanApproved=false')

if __name__=='__main__':main()
