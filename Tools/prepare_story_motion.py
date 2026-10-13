"""Technical slicing/registration of independently drawn story motion candidates."""
from pathlib import Path
import argparse
import collections
import hashlib
import json
import math
import statistics
from PIL import Image, ImageDraw, ImageFont, ImageFilter
from prepare_story_character_review import isolate

ROOT=Path(__file__).resolve().parents[1]
RES=ROOT/'Unity/Assets/Remake/Resources/StoryMotionReview'
XI=ROOT/'Art/NPCs/xiya/v2'
QI=ROOT/'Art/Companions/qibei/v2'
FONT='/System/Library/Fonts/STHeiti Medium.ttc'


def write(path,data):
    path.parent.mkdir(parents=True,exist_ok=True)
    path.write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n')


def skin_parts(im):
    w,h=im.size;px=list(im.get_flattened_data());seen=bytearray(w*h);parts=[]
    def skin(i):
        r,g,b,a=px[i];return a>=128 and r>120 and g>65 and b>35 and r>g+7 and g>b+5 and r>b+20
    for i in range(w*h):
        if seen[i] or not skin(i): continue
        queue=collections.deque([i]);seen[i]=1;owned=[]
        while queue:
            n=queue.popleft();owned.append(n);x,y=n%w,n//w
            for yy in range(max(0,y-1),min(h,y+2)):
                for xx in range(max(0,x-1),min(w,x+2)):
                    p=yy*w+xx
                    if not seen[p] and skin(p): seen[p]=1;queue.append(p)
        if len(owned)>=30:
            xs=[n%w for n in owned];ys=[n//w for n in owned]
            parts.append(dict(pixels=owned,bounds=(min(xs),min(ys),max(xs)+1,max(ys)+1),area=len(owned)))
    return sorted(parts,key=lambda p:p['area'],reverse=True)


def circle(im):
    a=im.getchannel('A');w,h=im.size;points=[];centers=[]
    for y in range(int(h*.84),max(int(h*.84)+1,int(h*.95))):
        xs=[x for x in range(w) if a.getpixel((x,y))>=128]
        if len(xs)>10:
            half=(max(xs)-min(xs)+1)/2;points.append((y,half*half+y*y));centers.append((max(xs)+min(xs))/2)
    assert len(points)>3
    mean_y=statistics.mean(y for y,z in points);mean_z=statistics.mean(z for y,z in points)
    slope=sum((y-mean_y)*(z-mean_z) for y,z in points)/sum((y-mean_y)**2 for y,z in points)
    cy=slope/2;rr=mean_z-slope*mean_y+cy*cy
    radius=math.sqrt(max(0,rr))
    method='lower-circle-fit'
    if not (.18*h<radius<.65*h and .15*h<cy<.95*h):
        radius=h*.40;cy=h-radius;method='fallback-lower-body'
    return statistics.median(centers),cy,2*radius,method


def field_anchor(im):
    a=im.getchannel('A');centers=[]
    for frac in (.20,.25,.30):
        y=int(im.height*frac);xs=[x for x in range(im.width) if a.getpixel((x,y))>=128]
        if xs: centers.append((min(xs)+max(xs))/2)
    return statistics.median(centers),im.height


def canvas(im,scale,anchor,size,target):
    out=Image.new('RGBA',size)
    resized=im.resize((max(1,round(im.width*scale)),max(1,round(im.height*scale))),Image.Resampling.LANCZOS)
    x=round(target[0]-anchor[0]*scale);y=round(target[1]-anchor[1]*scale)
    assert x>=0 and y>=0 and x+resized.width<=size[0] and y+resized.height<=size[1],(size,x,y,resized.size)
    out.alpha_composite(resized,(x,y));return out,(x,y)


def save(im,relative):
    p=RES/(relative+'.png');p.parent.mkdir(parents=True,exist_ok=True);im.save(p)
    return 'StoryMotionReview/'+relative


def contact(folder,key,frames):
    cols=min(4,len(frames));w=cols*280;h=((len(frames)+cols-1)//cols)*280+55
    board=Image.new('RGB',(w,h),'#e9e6df');draw=ImageDraw.Draw(board);font=ImageFont.truetype(FONT,18)
    draw.text((12,12),key+' · 独立绘制关键帧 / 共用缩放',font=font,fill='#253244')
    for i,frame in enumerate(frames):
        factor=min(240/frame.width,230/frame.height)
        im=frame.resize((round(frame.width*factor),round(frame.height*factor)),Image.Resampling.LANCZOS)
        x=(i%cols)*280+(280-im.width)//2;y=55+(i//cols)*280
        board.paste(im,(x,y),im);draw.text(((i%cols)*280+12,y+240),str(i),font=font,fill='#253244')
    board.save(folder/(key+'-contact.png'))
    # This early review is a source-frame preview, not a Unity capture.
    preview=[]
    loop=list(range(len(frames))) if 'walk' in key or 'battle-' in key and 'hover' not in key else [0,1,2,3,2,1] if len(frames)==4 else list(range(len(frames)))
    for i in loop:
        b=Image.new('RGB',(420,420),'#e9e6df');im=frames[i]
        factor=min(380/im.width,370/im.height);im=im.resize((round(im.width*factor),round(im.height*factor)),Image.Resampling.LANCZOS)
        b.paste(im,((420-im.width)//2,(420-im.height)//2),im);preview.append(b)
    preview[0].save(folder/(key+'-source-preview.gif'),save_all=True,append_images=preview[1:],duration=125,loop=0)


def main(partial=False):
    jobs=json.loads((XI/'motion-jobs.json').read_text());actors={'xiya':[],'qibei':[]};records={'xiya':[],'qibei':[]}
    base=isolate(XI/'source/battle-body.png',1,1)[0][0]
    base_face=skin_parts(base)[0]['bounds'];target_face=(base_face[3]-base_face[1])*576/base.height
    for job in jobs:
        path=ROOT/job['source']
        if not path.exists():
            if partial: continue
            raise AssertionError('Missing '+str(path))
        frames=isolate(path,job['columns'],job['rows']);person='xiya' if job['folder'].startswith('Art/NPCs') else 'qibei';folder=XI if person=='xiya' else QI
        kind=job['kind'];key=job['key'];rendered=[];refs=[];hands=[];grips=[];annotations=[]
        if kind=='sword':
            im=frames[0][0];scale=300/im.height;out=Image.new('RGBA',(128,384));small=im.resize((round(im.width*scale),round(im.height*scale)),Image.Resampling.LANCZOS)
            out.alpha_composite(small,((128-small.width)//2,30));save(out,'xiya/sword');im.save(folder/'static/sword.png');continue
        if kind.startswith('qibei'):
            body=[circle(im) for im,bounds in frames];scale=(260 if kind=='qibei_battle' else 76)/statistics.median(a[2] for a in body)
            size=(640,640) if kind=='qibei_battle' else (192,192);target=(size[0]/2,size[1]/2)
        elif kind.startswith('xiya_field'):
            scale=150/statistics.median(im.height for im,b in frames);size=(256,256);target=(128,220)
        else:
            faces=[skin_parts(im)[0] for im,b in frames];scale=target_face/statistics.median(p['bounds'][3]-p['bounds'][1] for p in faces)
            size=(640,768);target=(320,720)
        for i,(im,bounds) in enumerate(frames):
            hand=None
            if kind.startswith('qibei'):
                cx,cy,diameter,method=body[i];anchor=(cx,cy)
            elif kind.startswith('xiya_field'):
                anchor=field_anchor(im)
            else:
                a=im.getchannel('A');xs=[]
                for y in range(int(im.height*.86),im.height):
                    xs.extend(x for x in range(im.width) if a.getpixel((x,y))>=128)
                anchor=((min(xs)+max(xs))/2,im.height)
                parts=skin_parts(im);face=parts[0];candidates=[p for p in parts[1:] if p['area']>80]
                # Right fist is viewer-left except impact/follow-through across the body.
                if kind=='xiya_battle_slash' and i in (3,4): hand=max(candidates,key=lambda p:(p['bounds'][1]+p['bounds'][3])/2)
                elif kind=='xiya_battle_slash' and i in (1,2): hand=min(candidates,key=lambda p:p['bounds'][1])
                else: hand=min(candidates,key=lambda p:p['bounds'][0])
            out,offset=canvas(im,scale,anchor,size,target);rendered.append(out)
            refs.append(save(out,person+'/'+key+'/'+str(i)))
            record=dict(index=i,atlasBounds=list(bounds),anchor=list(anchor),commonScale=scale,canvas=list(size),offset=list(offset))
            if kind.startswith('qibei'): record.update(bodyDiameter=body[i][2],bodyFit=body[i][3])
            if hand:
                x0,y0,x1,y1=hand['bounds'];hx=(x0+x1)/2;hy=(y0+y1)/2
                mask=Image.new('L',im.size)
                pix=mask.load()
                for n in hand['pixels']: pix[n%im.width,n//im.width]=255
                mask=mask.filter(ImageFilter.MaxFilter(7))
                alpha=im.getchannel('A');mask=Image.frombytes('L',im.size,bytes(min(a,m) for a,m in zip(alpha.tobytes(),mask.tobytes())))
                foreground=im.copy();foreground.putalpha(mask)
                hand_out,_=canvas(foreground,scale,anchor,size,target);hands.append(save(hand_out,person+'/'+key+'/'+str(i)+'-hand'))
                angle=[25,120,55,-75,-120,25][i] if kind=='xiya_battle_slash' else 25
                grip=dict(x=(offset[0]+hx*scale-size[0]/2)/100,y=(720-offset[1]-hy*scale)/100,angle=angle)
                grips.append(grip);record.update(grip=grip,sourceHandBounds=list(hand['bounds']))
                marker=out.copy();d=ImageDraw.Draw(marker);gx=offset[0]+hx*scale;gy=offset[1]+hy*scale;d.ellipse((gx-6,gy-6,gx+6,gy+6),outline='red',width=2);annotations.append(marker)
            records[person].append(dict(key=key,**record))
        contact(folder,key,rendered)
        if annotations: contact(folder,key+'-grips',annotations)
        if kind=='xiya_field_idle':
            for i,direction in enumerate(('left','right')): actors[person].append(dict(key='field-idle-'+direction,fps=1,loop=[0],frames=[refs[i]],hands=[],grips=[],pivotY=1-220/256))
        else:
            actors[person].append(dict(key=key,fps=8 if 'walk' in key else 9 if kind.startswith('qibei') else 6,
                loop=[0,1,2,3,2,1] if kind.startswith('qibei') else list(range(len(refs))),frames=refs,hands=hands,grips=grips,
                pivotY=.5 if kind.startswith('qibei') else 1-target[1]/size[1]))
    # Exact existing front/back idle bodies, separate from large battle proportions.
    for direction,filename in [('down','field-front'),('up','field-back')]:
        im=Image.open(ROOT/f'Art/NPCs/xiya/v1/static/{filename}.png').convert('RGBA');out,_=canvas(im,150/im.height,field_anchor(im),(256,256),(128,220))
        ref=save(out,'xiya/field-idle-'+direction+'/0');actors['xiya'].append(dict(key='field-idle-'+direction,fps=1,loop=[0],frames=[ref],hands=[],grips=[],pivotY=1-220/256))
    # Qibei is symmetric and has no sided equipment; right explicitly mirrors left.
    left=next((a for a in actors['qibei'] if a['key']=='field-flap-left'),None)
    if left:
        refs=[]
        for i,relative in enumerate(left['frames']):
            im=Image.open(ROOT/'Unity/Assets/Remake/Resources'/f'{relative}.png');refs.append(save(im.transpose(Image.Transpose.FLIP_LEFT_RIGHT),'qibei/field-flap-right/'+str(i)))
        actors['qibei'].append(dict(key='field-flap-right',fps=9,loop=[0,1,2,3,2,1],frames=refs,hands=[],grips=[],pivotY=.5,mirrorOf='field-flap-left'))
    for person,head in [('xiya',4),('qibei',6)]:
        im=Image.open(ROOT/f'Reference/OriginalGame/extracted/heads/hpic{head}.png').convert('RGBA');save(im,'original/'+person+'-portrait')
        im=Image.open(ROOT/(f'Art/NPCs/xiya/v1/original/field-front.png' if person=='xiya' else 'Art/Companions/qibei/v1/original/field-front.png')).convert('RGBA');save(im,'original/'+person+'-field')
        write((XI if person=='xiya' else QI)/'motion-manifest.json',dict(tool='built-in image_gen',status='motion_review_candidate',
            userAuthorization='一个一个立绘来review，要有动作那种；西雅和七贝；战斗立绘和Q跑图两套一起',
            approvedVersion=None,runtimeIntegrated=False,reviewSceneOnly=True,handDrawnKeys=records[person],
            actions=actors[person],registration='One common scale per sheet; fixed ground anchor for Xiya; lower-core circle registration for Qibei. No per-frame rescaling.',
            rightMirror=person=='qibei',sourceSHA256={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in (XI if person=='xiya' else QI).joinpath('source').glob('*.png')},
            prompts=[str(p.relative_to(XI if person=='xiya' else QI)) for p in (XI if person=='xiya' else QI).joinpath('prompts').glob('*.txt')]))
    write(RES/'manifest.json',dict(actors=[dict(id=p,actions=actions,portrait='StoryMotionReview/original/'+p+'-portrait',originalField='StoryMotionReview/original/'+p+'-field') for p,actions in actors.items()]))
    print('Prepared',[(p,len(v)) for p,v in actors.items()],'partial',partial)


if __name__=='__main__':
    p=argparse.ArgumentParser();p.add_argument('--partial',action='store_true');args=p.parse_args();main(args.partial)
