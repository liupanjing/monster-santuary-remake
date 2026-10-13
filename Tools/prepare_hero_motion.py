"""Cut generated body frames without repainting them; preserve shared scale/root."""
from pathlib import Path
import collections
import statistics
import json
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / 'Art/Reviews/51/v5'
OUTPUT = ROOT / 'Unity/Assets/Remake/Resources/HeroMotion/Art'
OUTPUT.mkdir(parents=True, exist_ok=True)


def isolate(source, columns, rows):
    image = Image.open(source).convert('RGBA')
    width, height = image.size
    alpha = image.getchannel('A').tobytes()
    seen = bytearray(width * height)
    owned = [[] for _ in range(columns * rows)]
    for seed in range(width * height):
        if seen[seed] or alpha[seed] < 32:
            continue
        queue = collections.deque([seed]); seen[seed] = 1
        pixels = []; votes = collections.Counter()
        while queue:
            pixel = queue.popleft(); pixels.append(pixel)
            x, y = pixel % width, pixel // width
            votes[min(rows - 1, y * rows // height) * columns + min(columns - 1, x * columns // width)] += 1
            for yy in range(max(0, y - 1), min(height, y + 2)):
                for xx in range(max(0, x - 1), min(width, x + 2)):
                    neighbor = yy * width + xx
                    if not seen[neighbor] and alpha[neighbor] >= 32:
                        seen[neighbor] = 1; queue.append(neighbor)
        if len(pixels) >= 32:
            owned[votes.most_common(1)[0][0]].extend(pixels)
    frames = []
    for i, pixels in enumerate(owned):
        mask = bytearray(width * height)
        for pixel in pixels:
            mask[pixel] = alpha[pixel]
        frame = image.copy(); frame.putalpha(Image.frombytes('L', (width, height), bytes(mask)))
        bounds = frame.getbbox(); assert bounds, (source, i)
        frames.append({'image': frame.crop(bounds), 'bounds': bounds, 'row': i // columns,
                       'column': i % columns, 'cell': [width / columns, height / rows]})
    return frames


def place(frame, scale, anchor_x, ground_y, size, margin=0):
    image = frame['image']
    image = image.resize((round(image.width * scale), round(image.height * scale)), Image.Resampling.LANCZOS)
    left, top, _, _ = frame['bounds']
    cw, ch = frame['cell']
    x = round(size / 2 + (left - frame['column'] * cw - anchor_x) * scale)
    y = round(size - 1 - margin + (top - frame['row'] * ch - ground_y) * scale)
    result = Image.new('RGBA', (size, size)); result.alpha_composite(image, (x, y))
    # Fail rather than silently cut a visible limb off the output canvas.
    assert x >= 0 and y >= 0 and x + image.width <= size and y + image.height <= size, (x,y,image.size,size)
    return result


walk = isolate(REVIEW / 'source/walk.png', 5, 4)
run = isolate(REVIEW / 'source/run.png', 4, 4)
walk_scale = 148 / statistics.median(walk[i * 5]['image'].height for i in range(4))
# Match the frontal head silhouette across the independently generated atlases.
def head_width(frame):
    image = frame['image']; alpha = image.getchannel('A')
    bounds = alpha.crop((0,0,image.width,round(image.height*.60))).getbbox()
    return bounds[2] - bounds[0]
run_scale = walk_scale * statistics.median(head_width(walk[i]) for i in range(5)) / statistics.median(head_width(run[i]) for i in range(4))
manifest = {'directions':['down','left','right','up'], 'fieldPPU':100, 'battlePPU':128,
            'field':[], 'battle':[], 'status':'motion_candidate',
            'scalePolicy':'shared scale per source atlas; common row root/ground, no per-frame stretching; run airborne lift retained'}
for family, frames, columns, scale in [('walk',walk,5,walk_scale), ('run',run,4,run_scale)]:
    folder = OUTPUT / family; folder.mkdir(exist_ok=True)
    for row in range(4):
        group = frames[row*columns:(row+1)*columns]
        cw,ch = group[0]['cell']
        ground = statistics.median(f['bounds'][3] - row*ch for f in (group if family=='walk' else [group[0],group[2]]))
        anchor = statistics.median((f['bounds'][0]+f['bounds'][2])/2-f['column']*cw for f in group)
        for column, frame in enumerate(group):
            image = place(frame,scale,anchor,ground,192)
            path = folder / f'{row}-{column}.png'; image.save(path)
            manifest['field'].append({'family':family,'direction':row,'index':column,
                                      'resource':str(path.relative_to(ROOT/'Unity/Assets/Remake/Resources')).removesuffix('.png'),
                                      'sourceBounds':list(frame['bounds']),'scale':scale,'root':[anchor,ground]})

# Coordinates below are calibrated in a 360x362.67 source cell and transformed
# with the same scale/root as the body, rather than body-independent estimates.
front = [((278,208),(112,205)), ((142,150),(101,136)), ((132,153),(80,143)),
         ((198,178),(63,146)), ((225,174),(88,153)), ((212,181),(102,179)),
         ((226,183),(104,153)), ((227,181),(128,193)), ((261,203),(126,205)),
         ((195,161),(153,152)), ((274,91),(98,92)), ((283,213),(115,212))]
back = [((281,201),(116,201)), ((258,130),(107,130)), ((130,112),(79,140)),
        ((131,112),(70,151)), ((146,126),(97,153)), ((277,171),(104,168)),
        ((280,191),(97,176)), ((284,181),(121,203)), ((278,201),(103,191)),
        ((231,119),(142,120)), ((268,93),(104,106)), ((273,202),(118,196))]

for view, calibration in [('front',front),('back',back)]:
    frames = isolate(REVIEW / ('source/battle-'+view+'.png'),4,3)
    reference_scale = 448 / statistics.median(frames[i]['image'].height for i in [0,1,2,3,4,5,8,9,10,11])
    folder = OUTPUT / ('battle-'+view); folder.mkdir(exist_ok=True)
    for row in range(3):
        group = frames[row*4:(row+1)*4]; cw,ch = group[0]['cell']
        ground = statistics.median(f['bounds'][3]-row*ch for f in group)
        anchor = cw / 2
        for column, frame in enumerate(group):
            index = row*4+column; image = place(frame,reference_scale,anchor,ground,640,24)
            image.save(folder/f'{index}.png')
            overlay = Image.new('RGBA',image.size); coords=[]
            for x,y in calibration[index]:
                x*=cw/360; y*=ch/(1088/3)
                ox=320+(x-anchor)*reference_scale; oy=615+(y-ground)*reference_scale
                radius=round(20*reference_scale)
                rect=(max(0,round(ox-radius)),max(0,round(oy-radius)),min(640,round(ox+radius)),min(640,round(oy+radius)))
                overlay.alpha_composite(image.crop(rect),(rect[0],rect[1]))
                coords.append([(ox-320)/128,(616-oy)/128])
            overlay.save(folder/f'{index}-hands.png')
            aiming=index in [1,2,3,4]
            angles=[180,180] if aiming else [15,165]
            if index==10:angles=[90,90]
            if index==9:angles=[90,90]
            manifest['battle'].append({'view':view,'index':index,'resource':f'HeroMotion/Art/battle-{view}/{index}',
                'handsResource':f'HeroMotion/Art/battle-{view}/{index}-hands','main':coords[0],'off':coords[1],
                'mainAngle':angles[0],'offAngle':angles[1], 'sourceBounds':list(frame['bounds']),
                'scale':reference_scale,'root':[anchor,ground]})

(REVIEW/'motion-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
for view in ['front','back']:
    # Approved v4 idle body gets transparent padding only, preserving its scale.
    source=Image.open(ROOT/f'Art/Reviews/51/v4/body-{view}.png').convert('RGBA')
    canvas=Image.new('RGBA',(640,640));canvas.alpha_composite(source,(64,104))
    canvas.save(OUTPUT/f'battle-{view}/idle.png')
Image.open(ROOT/'Art/Reviews/51/v4/pistol.png').save(OUTPUT/'pistol.png')
# Contact sheets for inspecting every cut and animation phase.
font=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',17)
for kind,entries,columns in [('field',manifest['field'],9),('battle',manifest['battle'],6)]:
    cellw,cellh=(150,180) if kind=='field' else (210,250)
    board=Image.new('RGB',(columns*cellw,((len(entries)+columns-1)//columns)*cellh),'#e9e6df');draw=ImageDraw.Draw(board)
    for i,entry in enumerate(entries):
        source=ROOT/'Unity/Assets/Remake/Resources'/(entry['resource']+'.png');image=Image.open(source).convert('RGBA')
        image.thumbnail((cellw-10,cellh-30),Image.Resampling.LANCZOS)
        x=i%columns*cellw;y=i//columns*cellh;board.paste(image,(x+(cellw-image.width)//2,y+cellh-30-image.height),image)
        label=(entry.get('family',entry.get('view',''))+' '+str(entry.get('direction',''))+':'+str(entry['index']))
        draw.text((x+5,y+cellh-25),label,font=font,fill='#252833')
    board.save(REVIEW/(kind+'-contact.png'))
print('Prepared',len(manifest['field']),'Q field frames and',len(manifest['battle']),'battle body frames; independent hand overlays.')
