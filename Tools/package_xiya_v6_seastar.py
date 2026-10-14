"""Reassemble Seastar atlas frames and build review media; never synthesize poses."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import json, hashlib

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'Art/NPCs/xiya/v6'
FONT = '/System/Library/Fonts/STHeiti Medium.ttc'
LABELS = {'downcut': '向下竖劈 · 待审核', 'parry': '挥剑格挡 · 未通过质量检查'}

def main():
    manifest = {'provider': 'Seastar', 'humanApproved': False, 'formalGameIntegrated': False,
                'method': 'Restore exact atlas rectangles and spriteSourceSize offsets; preserve provider animation order and FPS. One fixed union crop per action for presentation, no per-frame rescaling, no new poses or interpolation.',
                'weaponLayers': 'Sword and body baked together in provider candidates; not modular production sprites.', 'actions': {}}
    font = ImageFont.truetype(FONT, 18)
    for action, label in LABELS.items():
        data = json.loads((OUT / f'seastar/{action}.json').read_text())
        atlas = Image.open(OUT / f'seastar/{action}-atlas.webp').convert('RGBA')
        sequence = data['animations'][action]
        meta = next(a for a in data['meta']['actions'] if a['id'] == action)
        assert len(sequence) == meta['frameCount']
        folder = OUT / 'frames' / action
        folder.mkdir(parents=True, exist_ok=True)
        frames = []
        for i, key in enumerate(sequence):
            record = data['frames'][key]
            rect, offset, size = (record[k] for k in ['frame', 'spriteSourceSize', 'sourceSize'])
            assert not record['rotated']
            assert 0 <= rect['x'] < rect['x'] + rect['w'] <= atlas.width
            assert 0 <= rect['y'] < rect['y'] + rect['h'] <= atlas.height
            assert 0 <= offset['x'] <= size['w'] - rect['w']
            assert 0 <= offset['y'] <= size['h'] - rect['h']
            frame = Image.new('RGBA', (size['w'], size['h']))
            region = atlas.crop((rect['x'], rect['y'], rect['x'] + rect['w'], rect['y'] + rect['h']))
            frame.paste(region, (offset['x'], offset['y']))
            frame.save(folder / f'{i:03d}.png')
            frames.append(frame)
        assert len({f.size for f in frames}) == 1
        boxes = [im.getbbox() for im in frames]
        crop = (max(0, min(b[0] for b in boxes)-25), max(0, min(b[1] for b in boxes)-25),
                min(frames[0].width, max(b[2] for b in boxes)+25), min(frames[0].height, max(b[3] for b in boxes)+25))
        # All preview frames use the same crop and scale, retaining body translations.
        preview = []
        for im in frames:
            view = Image.new('RGBA', (crop[2]-crop[0], crop[3]-crop[1]), '#e9e6df')
            view.alpha_composite(im.crop(crop))
            view = view.convert('RGB'); view.thumbnail((520, 520), Image.Resampling.LANCZOS)
            preview.append(view)
        palette_sheet = Image.new('RGB', (preview[0].width*8, preview[0].height))
        for n in range(8):
            palette_sheet.paste(preview[round(n*(len(preview)-1)/7)], (n*preview[0].width, 0))
        palette = palette_sheet.quantize(colors=256)
        gif = [im.quantize(palette=palette, dither=Image.Dither.NONE) for im in preview]
        gif[0].save(OUT/f'{action}-preview.gif', save_all=True, append_images=gif[1:], duration=round(1000/meta['fps']), loop=0, optimize=False, disposal=2)
        contact = Image.new('RGB', (1600, 1200), '#e9e6df'); draw = ImageDraw.Draw(contact)
        for n in range(12):
            i = round(n*(len(frames)-1)/11); im = frames[i].crop(crop); im.thumbnail((380,345))
            x, y = n%4*400 + (400-im.width)//2, n//4*400+5
            contact.paste(im, (x,y), im)
            draw.text((n%4*400+12, n//4*400+366), f'{action} · {i:02d} / {i/meta["fps"]:.2f}s', font=font, fill='#23312c')
        contact.save(OUT/f'{action}-contact.jpg')
        manifest['actions'][action] = {'label': label, 'frames': len(sequence), 'distinctAtlasEntries': len(set(sequence)),
            'fps': meta['fps'], 'durationSeconds': len(sequence)/meta['fps'], 'sourceCanvas': list(frames[0].size),
            'previewCrop': list(crop), 'alphaExtrema': list(atlas.getchannel('A').getextrema()),
            'status': 'pending_human_review' if action=='downcut' else 'quality_failed_review_candidate',
            'sequence': sequence, 'providerMetadata': f'seastar/{action}.json', 'atlas': f'seastar/{action}-atlas.webp',
            'originalVideo': f'seastar/{action}-source.mp4', 'gif': f'{action}-preview.gif',
            'knownIssues': ['Low follow-through has a long hold; inspect wrist and sword continuity'] if action=='downcut' else ['Recoil too weak', 'Brief sword trail remains', 'High guard becomes an outward swing rather than clear impact absorption'],
            'fileSHA256': {p.name: hashlib.sha256(p.read_bytes()).hexdigest() for p in (OUT/'seastar').glob(action+'*')}}
    (OUT/'animation-manifest.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2)+'\n')
    print(json.dumps({a:{k:v for k,v in m.items() if k in ['frames','distinctAtlasEntries','durationSeconds','alphaExtrema']} for a,m in manifest['actions'].items()}))

if __name__ == '__main__': main()
