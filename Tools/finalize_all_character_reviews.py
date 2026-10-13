"""Coverage, independent review atlas and portable art-only review archive."""
from pathlib import Path
import json
import hashlib
import zipfile
import math
from PIL import Image, ImageDraw, ImageFont
from prepare_all_character_reviews import ROOT, OUT, SOURCE, fitted, FONT, write_json
from build_all_character_gallery import main as build_gallery


def main():
    build_gallery()
    jobs=json.loads((OUT/'jobs.json').read_text())
    npcs={j['resource'] for j in jobs if j['kind']=='npc'}
    accounted_npcs=npcs|{'npc0','npc_xiya','npc_s8_0'}
    assert accounted_npcs=={p.stem for p in SOURCE.glob('npc*.png')}
    monster_ids={j['id'] for j in jobs if j['kind']=='monster'}
    assert monster_ids==set(range(50))
    portrait_ids={j['portraitId'] for j in jobs if j['kind']=='portrait'}|{1,2,4,6}
    assert portrait_ids=={int(p.stem[4:]) for p in (SOURCE/'heads').glob('hpic*.png')}
    placeholders=[]
    for number in (-1,50):
        paths=[SOURCE/f'mpic/s{number}_{i}.png' for i in (0,1)]
        same=[hashlib.sha256(p.read_bytes()).hexdigest()==hashlib.sha256((SOURCE/f'mpic/s0_{i}.png').read_bytes()).hexdigest() for i,p in enumerate(paths)]
        assert all(same)
        placeholders.append(dict(id=number,kind='shared_placeholder_without_verified_character_identity',sameAsMonster0=all(same)))
    write_json(OUT/'source-coverage.json',dict(result='pass',collectibleMonsters=sorted(monster_ids),
               npcAtlases=len(accounted_npcs),npcResourcesAccounted=sorted(accounted_npcs),
               dialoguePortraitIdsAccounted=sorted(portrait_ids),unknownPlaceholderSlots=placeholders,
               existingDesigns=['hero v4','babao v2','xiya v1 candidate','qibei v1 candidate'],
               note='Coverage of known source visual families. Multiple dialogue identities may share one family; no claim that missing network-only characters were recovered.'))
    # Preserve machine-readable corrections without implying human approval.
    write_json(ROOT/'Art/Reviews/39/v2/visual-check.json',dict(status='assistant_static_check',
               correction='Rebuilt the tail fan after first draft had insufficient visible tips',
               visibleTailTipsFront=9,visibleTailTipsRear=9,humanApproved=False))
    for key in ('npc16','npc16_1','npc8'):
        j=next(j for j in jobs if j['key']==key)
        write_json(ROOT/j['folder']/'visual-check.json',dict(status='assistant_static_check',
                   correction='Removed brown areas misinterpreted from original ground shadow; retained near-black body',humanApproved=False))
    # Compact overview uses independent selected cutouts, not painted placeholders.
    cols=8;tilew=300;tileh=260
    overview=Image.new('RGB',(cols*tilew,math.ceil(len(jobs)/cols)*tileh+90),'#e9e6df')
    draw=ImageDraw.Draw(overview);title=ImageFont.truetype(FONT,30);font=ImageFont.truetype(FONT,19)
    draw.text((24,16),'怪兽仙境 · 全角色独立静态重绘 / 待审核',font=title,fill='#252833')
    draw.text((24,57),'50 怪兽 + 27 NPC 造型/变体 + 4 头像 + 1 独立跑图版；主角、八宝、西雅、七贝沿用现有档案',font=font,fill='#59665d')
    for i,j in enumerate(jobs):
        x=(i%cols)*tilew;y=90+(i//cols)*tileh
        draw.rounded_rectangle((x+5,y+5,x+tilew-5,y+tileh-5),radius=10,fill='#faf8f1')
        fitted(overview,Image.open(ROOT/j['folder']/f'static/{j["views"][0]}.png'),(x+24,y+15,tilew-48,190))
        draw.text((x+15,y+213),j['name'],font=font,fill='#252833')
        draw.text((x+15,y+238),j['key'],font=ImageFont.truetype(FONT,14),fill='#667467')
    overview.save(OUT/'overview.png')
    # Archive preserves the same relative layout, so the gallery works offline.
    selected=set()
    for j in jobs:
        folder=ROOT/j['folder'];selected.update(p for p in folder.rglob('*') if p.is_file())
        if j['kind'] in ('monster','monster_field'):
            root=ROOT/f'Art/Reviews/{j["id"]:02}'
            selected.add(root/'REVIEW.md');selected.update(root.glob('s*.png'))
        else: selected.add(folder.parent/'REVIEW.md')
        selected.update(ROOT/p for p in j['originals'])
        selected.update(ROOT/p for p in j.get('sourceReferences',j['originals']))
    for relative in ('Art/Reviews/51/v4','Art/Companions/babao/v2','Art/NPCs/xiya/v1','Art/Companions/qibei/v1'):
        folder=ROOT/relative;selected.update(p for p in folder.rglob('*') if p.is_file());selected.add(folder.parent/'REVIEW.md')
    selected.update(p for p in OUT.rglob('*') if p.is_file())
    selected.discard(OUT/'archive-integrity.json')
    selected.add(ROOT/'Art/character-review.json');selected.add(ROOT/'Docs/ART_REVIEW.md')
    archive=ROOT/'Production/Deliveries/怪兽仙境-全角色静态审核.zip';archive.parent.mkdir(parents=True,exist_ok=True)
    with zipfile.ZipFile(archive,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as z:
        for p in sorted(selected):
            if p.is_file() and '__pycache__' not in p.parts:
                z.write(p,'monster-sanctuary-character-review/'+str(p.relative_to(ROOT)))
    with zipfile.ZipFile(archive) as z: assert z.testzip() is None
    write_json(OUT/'archive-integrity.json',dict(archive=str(archive.relative_to(ROOT)),
               bytes=archive.stat().st_size,sha256=hashlib.sha256(archive.read_bytes()).hexdigest(),crc='pass',
               artifactKind='static_art_review',runtimeBuild=False))
    print(json.dumps(dict(tasks=len(jobs),coverage='pass',archive=str(archive),archiveBytes=archive.stat().st_size),ensure_ascii=False))


if __name__=='__main__': main()
