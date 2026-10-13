"""Prepare independently traceable static proposals for every original visual family."""
from pathlib import Path
import json
import hashlib
import argparse
from PIL import Image, ImageDraw, ImageFont
from prepare_babao_reference import decode
from prepare_story_character_review import isolate

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'Reference/OriginalGame/extracted'
OUT = ROOT / 'Art/AllCharacters/2026-10-13'
FONT = '/System/Library/Fonts/STHeiti Medium.ttc'


def write_json(path, value):
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n')


def fitted(board, im, bounds, pixel=False):
    im = im.convert('RGBA')
    box = im.getbbox()
    if box:
        im = im.crop(box)
    x, y, w, h = bounds
    scale = min(w/im.width, h/im.height)
    im = im.resize((max(1, round(im.width*scale)), max(1, round(im.height*scale))),
                   Image.Resampling.NEAREST if pixel else Image.Resampling.LANCZOS)
    board.paste(im, (x+(w-im.width)//2, y+(h-im.height)//2), im)


def setup():
    OUT.mkdir(parents=True, exist_ok=True)
    review = json.loads((ROOT/'Art/character-review.json').read_text())
    scenes = json.loads((ROOT/'Production/original-scenes.json').read_text())
    scripts = json.loads((ROOT/'Reference/OriginalGame/scripts.json').read_text())
    jobs = []
    for row in review['characters']:
        if row['role'] != 'monster':
            continue
        key = f'monster-{row["id"]:02}'
        folder = ROOT / f'Art/Reviews/{row["id"]:02}/v2'
        originals = [SOURCE/f'mpic/s{row["id"]}_{i}.png' for i in (0,1)]
        jobs.append(dict(key=key, name=row['name'], kind='monster', id=row['id'], tier=row['tier'],
                         folder=str(folder.relative_to(ROOT)), originals=[str(p.relative_to(ROOT)) for p in originals],
                         views=['battle-front','battle-back'], silhouette=row['originalReferenceKind']=='silhouette_only'))
    # Preserve resource identity where dialogue-to-actor identity is not proven.
    for path in sorted(SOURCE.glob('npc*.png')):
        resource = path.stem
        if resource in ('npc0','npc_xiya','npc_s8_0'):
            continue
        folder = ROOT / f'Art/NPCs/{resource}/v1'
        (folder/'original').mkdir(parents=True, exist_ok=True)
        frames, record = decode(resource, folder)
        write_json(folder/'original/decoding.json', record)
        poses = [('field-front',1),('field-back',0)] if len(record['animations'])>=2 and resource not in ('npc1','npc3','npc4') else [('field-pose',0)]
        for label, anim in poses:
            if record['animations']:
                seq = record['animations'][anim]['sequence'][0]
                frame = frames[(seq[0]+(seq[1]<<8)) & 1023]
            else:
                frame = frames[0]
            frame.crop(frame.getbbox()).save(folder/f'original/{label}.png')
        matches = [{'scene':s['id'], 'actor':a} for s in scenes for a in s['actors'] if a['type']==resource]
        write_json(folder/'original/scene-usage.json', matches)
        jobs.append(dict(key=resource, name='克莱尔' if resource=='npc_klaier' else resource,
                         kind='npc', folder=str(folder.relative_to(ROOT)), resource=resource,
                         originals=[str((folder/f'original/{v}.png').relative_to(ROOT)) for v,_ in poses],
                         sourceReferences=[str((SOURCE/f'{resource}.{ext}').relative_to(ROOT)) for ext in ['png','bin']],
                         views=[v for v,_ in poses], silhouette=False, originalSceneUses=len(matches)))
    # The original field boss has its own compact composite, independently drawn
    # rather than scaling down its battle body.
    resource='npc_s8_0'; folder=ROOT/'Art/Reviews/08/v2-field'
    (folder/'original').mkdir(parents=True,exist_ok=True)
    frames,record=decode(resource,folder); write_json(folder/'original/decoding.json',record)
    for label,anim in [('field-front',1),('field-back',0)]:
        seq=record['animations'][anim]['sequence'][0]
        frame=frames[(seq[0]+(seq[1]<<8))&1023]
        frame.crop(frame.getbbox()).save(folder/f'original/{label}.png')
    jobs.append(dict(key='monster-08-field',name='异翅巨蟒·跑图版',kind='monster_field',id=8,
                     folder=str(folder.relative_to(ROOT)),resource=resource,
                     originals=[str((folder/f'original/{v}.png').relative_to(ROOT)) for v in ['field-front','field-back']],
                     sourceReferences=[str((SOURCE/f'{resource}.{ext}').relative_to(ROOT)) for ext in ['png','bin']],
                     views=['field-front','field-back'],silhouette=False,
                     observedConstraints='Original compact overworld serpent: broad small face, vertical purple neck and lavender belly, mint-green back, turquoise horizontal bat wings, little coiled base. Independent field body from npc_s8_0; no arms or legs.'))
    # Portrait-only sources remain distinct; never guess a body from the portrait.
    for number, name in [(3,'许愿池精灵'),(5,'耐法瑞'),(9,'芙蕾尔'),(10,'克莱尔')]:
        folder = ROOT/f'Art/NPCs/portrait-{number}/v1'
        dialogues = [{'script':script, **r} for script,rows in scripts.items() for r in rows
                     if r['opcode']==1 and r['args'][0]==number]
        folder.mkdir(parents=True,exist_ok=True)
        write_json(folder/'dialogue-evidence.json', dialogues)
        jobs.append(dict(key=f'portrait-{number}', name=name+'·头像', kind='portrait',
                         folder=str(folder.relative_to(ROOT)), originals=[f'Reference/OriginalGame/extracted/heads/hpic{number}.png'],
                         views=['portrait'], silhouette=False, portraitId=number))
    for job in jobs:
        folder = ROOT/job['folder']
        for sub in ('source','static','prompts'):
            (folder/sub).mkdir(parents=True,exist_ok=True)
        board = Image.new('RGB',(1000,600),'#e9e6df')
        draw = ImageDraw.Draw(board); font = ImageFont.truetype(FONT,24)
        draw.text((24,16),job['name']+' · 原包参考（最近邻放大）',font=font,fill='#252833')
        for i,p in enumerate(job['originals']):
            n=len(job['originals']); width=1000//n
            fitted(board,Image.open(ROOT/p),(i*width+30,70,width-60,450),True)
            draw.text((i*width+30,540),job['views'][i],font=font,fill='#252833')
        board.save(folder/'original-reference.png')
        job['reference']=str((folder/'original-reference.png').relative_to(ROOT))
        sources=job.get('sourceReferences',job['originals'])
        job['sourceSHA256']={p:hashlib.sha256((ROOT/p).read_bytes()).hexdigest() for p in sources}
    write_json(OUT/'jobs.json',jobs)
    for start in range(0,len(jobs),10):
        group=jobs[start:start+10]
        board=Image.new('RGB',(1600, len(group)*190),'#e9e6df'); draw=ImageDraw.Draw(board)
        font=ImageFont.truetype(FONT,22)
        for i,j in enumerate(group):
            draw.text((15,i*190+15),j['key']+' '+j['name'],font=font,fill='#252833')
            for k,p in enumerate(j['originals']):
                fitted(board,Image.open(ROOT/p),(430+k*560,i*190+10,500,170),True)
        board.save(OUT/f'original-group-{start//10:02}.png')
    print(json.dumps({'jobs':len(jobs),'monsters':50,'npcTemplates':sum(j['kind']=='npc' for j in jobs),
                      'portraits':4,'list':str(OUT/'jobs.json')},ensure_ascii=False))


def finish(key):
    jobs=json.loads((OUT/'jobs.json').read_text()); job=next(j for j in jobs if j['key']==key)
    folder=ROOT/job['folder']; source=folder/'source/design.png'
    frames=isolate(source,len(job['views']),1)
    board=Image.new('RGB',(1400,750),'#e9e6df'); draw=ImageDraw.Draw(board)
    font=ImageFont.truetype(FONT,24); small=ImageFont.truetype(FONT,19)
    draw.text((24,16),job['name']+' · 原版 / 2D 独立重绘 · 静态候选',font=font,fill='#252833')
    for i,(view,(frame,bounds)) in enumerate(zip(job['views'],frames)):
        frame.save(folder/f'static/{view}.png')
        n=len(frames); width=1400//n; x=i*width
        draw.text((x+20,60),view+' · 原版',font=small,fill='#252833')
        fitted(board,Image.open(ROOT/job['originals'][i]),(x+30,100,width//2-55,525),True)
        draw.text((x+width//2,60),'重绘',font=small,fill='#252833')
        fitted(board,frame,(x+width//2+10,100,width//2-40,525))
    note='原包只有相同占位剪影；本版是完整新设计，配色与细节不是原画复原。' if job['silhouette'] else '以原包轮廓、配色和身体比例为依据；高清细节是候选解释，待逐角色确认。'
    draw.text((24,660),note,font=small,fill='#53545b')
    draw.text((24,700),'透明素材独立保存；本轮不制作动作、不替换已发布运行资源。',font=small,fill='#53545b')
    board.save(folder/'original-vs-redraw.png')
    write_json(folder/'generation-manifest.json',dict(tool='built-in image_gen',status='static_design_candidate',
               approvedVersion=None,approvedBy=None,runtimeIntegrated=False,animationFrames=0,
               key=key,views=[{'name':v,'bounds':list(b),'size':list(f.size)} for v,(f,b) in zip(job['views'],frames)],
               originalReferenceSHA256=job['sourceSHA256'],sourceSHA256=hashlib.sha256(source.read_bytes()).hexdigest(),
               prompt='prompts/design.txt',prompts=[str(p.relative_to(folder)) for p in sorted((folder/'prompts').glob('*.txt'))],
               reference=job['reference'],silhouetteOnly=job['silhouette']))
    print(key,'saved',len(frames),'static views')


def contact(start):
    jobs=json.loads((OUT/'jobs.json').read_text())[start:start+10]
    board=Image.new('RGB',(1800,max(1,(len(jobs)+1)//2)*320),'#e9e6df'); draw=ImageDraw.Draw(board)
    font=ImageFont.truetype(FONT,22)
    for i,j in enumerate(jobs):
        x=(i%2)*900; y=(i//2)*320
        labels='原 / 新正面 / 新背面' if len(j['views'])==2 else '原 / 新单姿态'
        draw.text((x+16,y+12),j['key']+' '+j['name']+' · '+labels,font=font,fill='#252833')
        fitted(board,Image.open(ROOT/j['originals'][0]),(x+15,y+60,180,220),True)
        for k,v in enumerate(j['views']):
            p=ROOT/j['folder']/f'static/{v}.png'
            if p.exists():
                fitted(board,Image.open(p),(x+220+k*325,y+55,300,250))
    board.save(OUT/f'new-group-{start//10:02}.png')
    print(OUT/f'new-group-{start//10:02}.png')


if __name__=='__main__':
    p=argparse.ArgumentParser(); p.add_argument('--finish'); p.add_argument('--contact',type=int); a=p.parse_args()
    if a.finish: finish(a.finish)
    elif a.contact is not None: contact(a.contact)
    else: setup()
