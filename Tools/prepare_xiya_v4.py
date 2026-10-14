"""Segment authored with-sword masters into registered modular sprite layers.

All RGB comes from imagegen masters. Polygons assign existing pixels to parts;
they do not paint fingers, replace poses, or rotate a guessed sword onto a fist.
"""
from pathlib import Path
import argparse
import hashlib
import json
import statistics
from PIL import Image, ImageDraw, ImageFilter, ImageChops
from prepare_story_character_review import isolate
from prepare_story_motion import skin_parts

ROOT = Path(__file__).resolve().parents[1]
ART = ROOT / 'Art/NPCs/xiya/v4'
RES = ROOT / 'Unity/Assets/Remake/Resources/StoryMotionReview'
SIZE = (1024, 896)
ANCHOR = (600, 816)


def polygon(size, points):
    mask = Image.new('L', size)
    ImageDraw.Draw(mask).polygon([tuple(p) for p in points], fill=255)
    return mask


def layers(im, bounds, annotation):
    ox, oy = bounds[:2]
    local = lambda points: [(x-ox, y-oy) for x, y in points]
    weapon = polygon(im.size, local(annotation['weapon'])).filter(ImageFilter.MaxFilter(19))
    if annotation.get('bodyProtection'):
        weapon=ImageChops.multiply(weapon,ImageChops.invert(polygon(im.size,local(annotation['bodyProtection']))))
    roi = polygon(im.size, local(annotation['handRegion']))
    # Preserve skin and its adjacent ink; the annotated ROI excludes the cuff.
    skin = Image.new('L', im.size)
    pixels = skin.load()
    for part in skin_parts(im):
        for n in part['pixels']:
            x, y = n % im.width, n // im.width
            if roi.getpixel((x, y)): pixels[x, y] = 255
    ink = skin.filter(ImageFilter.MaxFilter(3))
    hand = ImageChops.multiply(ink, roi)
    front = ImageChops.multiply(hand, polygon(im.size, local(annotation['frontFingers'])))
    weapon = ImageChops.multiply(weapon, ImageChops.invert(hand))
    weapon = ImageChops.multiply(weapon, ImageChops.invert(front))
    body = ImageChops.invert(ImageChops.lighter(weapon, front))
    return body, weapon, front


def register(im, scale, root):
    w, h = round(im.width*scale), round(im.height*scale)
    offset = (round(ANCHOR[0]-root[0]*scale), round(ANCHOR[1]-root[1]*scale))
    assert offset[0]>=0 and offset[1]>=0 and offset[0]+w<=SIZE[0] and offset[1]+h<=SIZE[1], (offset, (w,h))
    result = Image.new('RGBA', SIZE)
    result.alpha_composite(im.resize((w,h), Image.Resampling.LANCZOS), offset)
    return result, offset, (w,h)


def registered_mask(mask, offset, resized):
    output = Image.new('L', SIZE)
    output.paste(mask.resize(resized, Image.Resampling.NEAREST), offset)
    return output


def save_layer(master, mask, path):
    # Binary assignment after resizing means the three images reconstruct exactly.
    out = master.copy()
    out.putalpha(ImageChops.multiply(master.getchannel('A'), mask))
    out.paste((0,0,0,0), mask=out.getchannel('A').point(lambda v:255 if v==0 else 0))
    path.parent.mkdir(parents=True, exist_ok=True)
    out.save(path)
    return out


def main(partial=False):
    specs = json.loads((ART/'segmentation.json').read_text())
    exports, records = {}, []
    for source in specs['sources']:
        if partial and not (ART/source['file']).exists(): continue
        frames = isolate(ART/source['file'], source['columns'], source['rows'])
        prepared = []
        for (im,bounds), annotation in zip(frames, source['parts']):
            masks = layers(im,bounds,annotation)
            body_im=im.copy();body_im.putalpha(ImageChops.multiply(im.getchannel('A'),ImageChops.invert(masks[1])))
            body_bounds=body_im.getbbox();assert body_bounds
            prepared.append((im,bounds,annotation,masks,body_bounds))
        scale=584/statistics.median(b[4][3]-b[4][1] for b in prepared)
        for index,(im,bounds,ann,masks,body_bounds) in enumerate(prepared):
            if source['key']=='slash-a' and index==0:
                continue  # Original lift had a pommel/cuff merge; replaced by lift-master.
            alpha=im.getchannel('A');xs=[]
            # Both boots determine the horizontal ground root even when one foot
            # is higher in a recoil/lunge. Bottom-row centroids would teleport hips.
            for y in range(int(body_bounds[1]+(body_bounds[3]-body_bounds[1])*.65),body_bounds[3]):
                for x in range(body_bounds[0],body_bounds[2]):
                    r,g,b,a=im.getpixel((x,y))
                    if a>=128 and g>45 and g>r*1.04 and g>b*1.10 and not masks[1].getpixel((x,y)):
                        xs.append(x)
            assert xs, 'Both boot silhouettes are required for ground registration'
            root=((min(xs)+max(xs))/2,body_bounds[3])
            master,offset,resized=register(im,scale,root)
            bm,wm,hm=[registered_mask(m,offset,resized) for m in masks]
            # Assign only pixels of the master, keeping every pose's actual contact.
            wm=wm.point(lambda v:255 if v>=128 else 0);hm=hm.point(lambda v:255 if v>=128 else 0)
            wm=ImageChops.multiply(wm,ImageChops.invert(hm));bm=ImageChops.invert(ImageChops.lighter(wm,hm))
            relative=f'StoryMotionReview/xiya/v4/{source["key"]}/{index}'
            folder=ROOT/'Unity/Assets/Remake/Resources'/relative
            body=save_layer(master,bm,folder.with_name(folder.name+'-body').with_suffix('.png'))
            sword=save_layer(master,wm,folder.with_name(folder.name+'-weapon').with_suffix('.png'))
            fingers=save_layer(master,hm,folder.with_name(folder.name+'-fingers').with_suffix('.png'))
            master.save(folder.with_name(folder.name+'-master').with_suffix('.png'))
            result=Image.alpha_composite(Image.alpha_composite(body,sword),fingers)
            assert result.tobytes()==master.tobytes(), 'Registered layers must reconstruct master exactly'
            grip=ann['grip'];native=(offset[0]+(grip[0]-bounds[0])*scale,offset[1]+(grip[1]-bounds[1])*scale)
            exports[(source['key'],index)]={
                'frame':relative+'-body','weapon':relative+'-weapon','hand':relative+'-fingers','master':relative+'-master',
                'grip':{'x':(native[0]-ANCHOR[0])/100,'y':(ANCHOR[1]-native[1])/100,'angle':0},
            }
            records.append({'source':source['file'],'key':source['key'],'index':index,'atlasBounds':bounds,'bodyBounds':body_bounds,'scalePerSheet':scale,'footRoot':root,'offset':offset,'annotation':ann,'registeredGripPixel':native,'recomposition':'exact RGBA match'})
    if not partial:
        manifest_path=RES/'manifest.json';manifest=json.loads(manifest_path.read_text())
        actor=next(a for a in manifest['actors'] if a['id']=='xiya')
        routes={
            'battle-idle':[('ready',0)],
            'battle-slash':[('ready',0),('lift',0)]+[('slash-a',i)for i in (1,2,3)]+[('slash-b',i)for i in range(2)]+[('ready',0)],
            'battle-hurt':[('hurt',0),('hurt',1)],
        }
        for key,route in routes.items():
            movie=next(m for m in actor['actions'] if m['key']==key)
            movie.update(frames=[exports[p]['frame']for p in route],hands=[exports[p]['hand']for p in route],weapons=[exports[p]['weapon']for p in route],masters=[exports[p]['master']for p in route],grips=[exports[p]['grip']for p in route],loop=list(range(len(route))),fps=8,pivotY=1-ANCHOR[1]/SIZE[1])
            if key=='battle-slash':movie['durations']=[.10,.12,.14,.08,.06,.10,.13,.12]
        manifest['equipmentId']='xiya-neutral-straight-sword-v4'
        manifest_path.write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
    report={'version':'v4','status':'motion_review_candidate','humanApproved':False,'formalGameIntegrated':False,'method':'Complete with-sword masters segmented into body/rear palm, authored weapon pose, front fingers; no empty-hand redraw and no live sword-angle guessing','weaponImages':'Visible weapon segments preserve authored hand occlusion; replacement items need their own images for the same pose slots','canvas':SIZE,'footAnchor':ANCHOR,'pixelsPerUnit':100,'records':records,'sourceSHA256':{p.name:hashlib.sha256(p.read_bytes()).hexdigest()for p in (ART/'source').glob('*-master.png')},'partial':partial}
    (ART/'module-manifest.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
    print('Registered',len(exports),'unique source poses; exact pixel reconstruction; partial',partial)


if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--partial',action='store_true');main(parser.parse_args().partial)
