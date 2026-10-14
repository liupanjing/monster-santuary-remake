"""Technical crop/registration and pencil-test packaging; never draws character poses."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageOps
import json, hashlib
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'Art/NPCs/xiya/v5'
DURATIONS=[80,70,90,100,50,40,40,50,60,90,90,120]
PHASES=['戒备','引剑','转肩','蓄势','转胯出剑','挥斩过渡','切过目标','延续剑路','收势','减速回收','回防过渡','恢复戒备']

def main():
    src=OUT/'source/continuous-slash-rough.png'
    sheet=Image.open(src).convert('RGB');w,h=sheet.size
    assert w>1000 and h>700
    folder=OUT/'frames';folder.mkdir(exist_ok=True)
    frames=[];records=[]
    # Use actual source dimensions and a shared nominal cell canvas. Never fit
    # each pose's body bounding box to a different scale.
    cellw,cellh=round(w/4),round(h/3)
    cw,ch=cellw+80,cellh+80
    anchors=[(106,355),(102,355),(94,355),(104,357),(88,348),(98,348),(102,349),(108,350),(106,335),(102,335),(90,335),(100,336)]
    target=(140,400)
    for i in range(12):
        c,r=i%4,i//4
        box=[round(c*w/4),round(r*h/3),round((c+1)*w/4),round((r+1)*h/3)]
        # Cell 08's final few empty ground pixels contain the tip of the
        # next row's sword; stop below its own boots to exclude that neighbor.
        if i==5:box[2]=690
        if i==6:box[0]=690
        if i==9:box[2]=700
        if i==10:box[0]=700
        if i==7:box[3]=box[1]+354
        im=sheet.crop(box);canvas=Image.new('RGB',(cw,ch),'white')
        source_anchor=(anchors[i][0]+round(c*w/4)-box[0],anchors[i][1]+round(r*h/3)-box[1])
        offset=(target[0]-source_anchor[0],target[1]-source_anchor[1])
        canvas.paste(im,offset);canvas.save(folder/f'{i:02d}.png')
        frames.append(canvas)
        records.append({'index':i,'phase':PHASES[i],'sourceBounds':box,'frontBootAnchor':source_anchor,'registrationOffset':offset,'durationMs':DURATIONS[i],'frameSHA256':hashlib.sha256((folder/f'{i:02d}.png').read_bytes()).hexdigest()})
    f=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',22)
    contact=Image.new('RGB',(cw*4,(ch+44)*3),'#f3f1ec');d=ImageDraw.Draw(contact)
    for i,im in enumerate(frames):
        x=(i%4)*cw;y=(i//4)*(ch+44);contact.paste(im,(x,y));d.text((x+10,y+ch+9),f'{i+1:02d} · {PHASES[i]}',font=f,fill='#3b5444')
    contact.save(OUT/'slash-rough-contact.png')
    # Same source drawings, no optical-flow tweening, morphs or crossfades.
    preview=[]
    for i,im in enumerate(frames):
        b=Image.new('RGB',(cw,ch+80),'#f3f1ec');b.paste(im,(0,40));d=ImageDraw.Draw(b);d.text((12,8),'西雅 · 连续挥剑草稿',font=f,fill='#3b5444');d.text((12,ch+48),f'{i+1:02d} · {PHASES[i]}',font=f,fill='#3b5444');preview.append(b)
    pal=preview[0].quantize(colors=240);preview=[im.quantize(palette=pal,dither=Image.Dither.NONE) for im in preview]
    preview[0].save(OUT/'slash-pencil-test.gif',save_all=True,append_images=preview[1:],duration=DURATIONS[:-1]+[DURATIONS[-1]+400],loop=0,disposal=2,optimize=False)
    manifest={'status':'design_and_pencil_test_candidate','humanApproved':False,'formalGameIntegrated':False,'tool':'built-in image_gen','design':'source/knight-design.png','sheet':'source/continuous-slash-rough.png','sourceSize':[w,h],'canvas':[cw,ch],'commonFrontBootAnchor':target,'uniqueDrawings':12,'actionDurationMs':sum(DURATIONS),'previewEndHoldMs':400,'method':'One sequential rough drawing sheet; explicit crop rectangles with two extended blade margins and neighbor-exclusion margins; common canvas and manually marked front-boot contact registration; no per-pose rescaling, old-pose reuse, optical-flow or crossfade','draftScope':'single battle view; not finished animation or Unity sprite import','frames':records,'sourceSHA256':{str(p.relative_to(OUT)):hashlib.sha256(p.read_bytes()).hexdigest() for p in (OUT/'source').glob('*.png')}}
    (OUT/'draft-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps({'sourceSize':[w,h],'canvas':[cw,ch],'drawings':12,'artApproved':False}))

if __name__=='__main__':main()
