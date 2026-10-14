"""Restore provider frames without altering their pose, sequence or scale."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import json, hashlib

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT/'Art/NPCs/xiya/v7'

def main():
    data = json.loads((OUT/'seastar/cut.json').read_text())
    assert len(data['animations']) == 1
    action, seq = next(iter(data['animations'].items()))
    meta = next(a for a in data['meta']['actions'] if a['id'] == action)
    atlas = Image.open(OUT/'seastar/cut-atlas.webp').convert('RGBA')
    assert len(seq) == meta['frameCount']
    folder = OUT/'frames';folder.mkdir(exist_ok=True)
    ims=[]
    for i,key in enumerate(seq):
        record=data['frames'][key];r=record['frame'];s=record['spriteSourceSize'];ss=record['sourceSize']
        assert not record['rotated']
        assert 0 <= r['x'] < r['x']+r['w'] <= atlas.width
        assert 0 <= r['y'] < r['y']+r['h'] <= atlas.height
        assert 0 <= s['x'] <= ss['w']-r['w'] and 0 <= s['y'] <= ss['h']-r['h']
        im=Image.new('RGBA',(ss['w'],ss['h']))
        im.paste(atlas.crop((r['x'],r['y'],r['x']+r['w'],r['y']+r['h'])),(s['x'],s['y']))
        im.save(folder/f'{i:03d}.png');ims.append(im)
    assert len({im.size for im in ims}) == 1
    boxes=[im.getbbox() for im in ims]
    crop=[max(0,min(b[0] for b in boxes)-24),max(0,min(b[1] for b in boxes)-24),
          min(ims[0].width,max(b[2] for b in boxes)+24),min(ims[0].height,max(b[3] for b in boxes)+24)]
    views=[]
    for im in ims:
        view=Image.new('RGBA',(crop[2]-crop[0],crop[3]-crop[1]),'#e9e6df');view.alpha_composite(im.crop(crop))
        view=view.convert('RGB');view.thumbnail((520,520),Image.Resampling.LANCZOS);views.append(view)
    colors=Image.new('RGB',(views[0].width*8,views[0].height))
    for n in range(8):colors.paste(views[round(n*(len(views)-1)/7)],(n*views[0].width,0))
    palette=colors.quantize(colors=256)
    gif=[im.quantize(palette=palette,dither=Image.Dither.NONE) for im in views]
    durations=[round((i+1)*100/meta['fps'])*10-round(i*100/meta['fps'])*10 for i in range(len(gif))];durations[-1]+=500
    gif[0].save(OUT/'cut-preview.gif',save_all=True,append_images=gif[1:],duration=durations,loop=0,optimize=False,disposal=2)
    contact=Image.new('RGB',(1600,1200),'#e9e6df');draw=ImageDraw.Draw(contact)
    font=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',18)
    for n in range(12):
        i=round(n*(len(ims)-1)/11);im=ims[i].crop(crop);im.thumbnail((380,345))
        x,y=n%4*400+(400-im.width)//2,n//4*400+5;contact.paste(im,(x,y),im)
        draw.text((n%4*400+12,n//4*400+366),f'{i:02d} / {i/meta["fps"]:.2f}s',font=font,fill='#23312c')
    contact.save(OUT/'cut-contact.jpg')
    manifest={'provider':'Seastar','humanApproved':False,'formalGameIntegrated':False,
      'action':action,'sequence':seq,'frames':len(seq),'distinctAtlasEntries':len(set(seq)),
      'fps':meta['fps'],'durationSeconds':len(seq)/meta['fps'],'sourceCanvas':list(ims[0].size),
      'previewCrop':crop,'gifEndHoldMs':500,'alphaExtrema':list(atlas.getchannel('A').getextrema()),
      'method':'Provider frame order and offsets, one fixed crop for the entire action; no per-frame scaling, interpolation, pose synthesis or sword edits.',
      'fileSHA256':{p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in (OUT/'seastar').iterdir() if p.is_file()}}
    (OUT/'animation-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps({k:v for k,v in manifest.items() if k in ['action','frames','fps','durationSeconds','distinctAtlasEntries','alphaExtrema']}))

if __name__=='__main__':main()
