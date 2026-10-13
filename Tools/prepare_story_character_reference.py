"""Extract Xiya/Qibei original field frames, portrait identity and dialogue evidence."""
from pathlib import Path
import hashlib
import json
from PIL import Image, ImageDraw, ImageFont
from prepare_babao_reference import decode

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'Reference/OriginalGame/extracted'
FONT = '/System/Library/Fonts/STHeiti Medium.ttc'


def main():
    scripts = json.loads((ROOT / 'Reference/OriginalGame/scripts.json').read_text())
    scenes = json.loads((ROOT / 'Production/original-scenes.json').read_text())
    for key, name, resource, portrait_id, kind in [('xiya', '西雅', 'npc_xiya', 4, 'NPCs'),
                                                   ('qibei', '七贝', 'cw2', 6, 'Companions')]:
        out = ROOT / f'Art/{kind}/{key}/v1'
        (out / 'original').mkdir(parents=True, exist_ok=True)
        frames, record = decode(resource, out)
        portrait = Image.open(SOURCE / f'heads/hpic{portrait_id}.png').convert('RGBA')
        portrait.save(out / 'original/portrait.png')
        font = ImageFont.truetype(FONT, 22)
        board = Image.new('RGB', (1400, ((len(frames) + 6) // 7) * 190 + 80), '#292d36')
        draw = ImageDraw.Draw(board)
        draw.text((24, 20), f'{name} · 原版 {resource} 按 y.java 合成，最近邻放大', font=font, fill='white')
        for i, frame in enumerate(frames):
            im = frame.crop(frame.getbbox())
            im = im.resize((im.width * 6, im.height * 6), Image.Resampling.NEAREST)
            board.paste(im, (i % 7 * 200 + (200 - im.width) // 2, 70 + i // 7 * 190 + (145 - im.height) // 2), im)
            draw.text((i % 7 * 200 + 30, 70 + i // 7 * 190 + 150), f'frame {i:02}', font=font, fill='white')
        board.save(out / 'original-contact.png')
        reference = Image.new('RGB', (1200, 540), '#e9e6df'); draw = ImageDraw.Draw(reference)
        portrait_large = portrait.resize((portrait.width * 8, portrait.height * 8), Image.Resampling.NEAREST)
        reference.paste(portrait_large, ((400 - portrait_large.width) // 2, 80), portrait_large)
        draw.text((55, 420), f'对话头像 · hpic{portrait_id}', font=font, fill='#25252c')
        # Read first pose from actual direction animations instead of guessing a frame ID.
        directions = [('正面', 1), ('背面', 0)]
        for j, (label, animation) in enumerate(directions):
            lo, hi = record['animations'][animation]['sequence'][0]
            frame_id = (lo + (hi << 8)) & 1023
            im = frames[frame_id].crop(frames[frame_id].getbbox())
            im.save(out / 'original' / ('field-front.png' if j == 0 else 'field-back.png'))
            im = im.resize((im.width * 12, im.height * 12), Image.Resampling.NEAREST)
            reference.paste(im, ((j+1)*400 + (400-im.width)//2, 105), im)
            draw.text(((j+1)*400+60, 420), f'跑图{label} · anim {animation}', font=font, fill='#25252c')
        draw.text((24, 18), f'{name} · 原版身份/造型依据 · 不是高清原画', font=font, fill='#25252c')
        reference.save(out / 'original-reference.png')
        dialogue = [{'script': filename, **row} for filename, rows in scripts.items() for row in rows
                    if row['opcode'] == 1 and row.get('text', [None])[0] == name]
        assert dialogue and all(row['args'][0] == portrait_id for row in dialogue)
        originals = [f'{resource}.png', f'{resource}.bin', f'heads/hpic{portrait_id}.png']
        evidence = {'name': name, 'resource': resource, 'portrait': f'heads/hpic{portrait_id}.png',
                    'portraitEvidence': 'opcode1 args[0] passed by t.java to v.java heads/hpic<n>.png',
                    'dialogueCount': len(dialogue), 'dialogueExamples': dialogue[:3],
                    'decodedSprite': record,
                    'sceneExamples': [{'scene': s['id'], 'actors': [a for a in s['actors'] if a['type'] in ('npc_xiya', 'cw2')]}
                                      for s in scenes if s['id'] in (6, 9, 10)],
                    'originalReferences': ['Reference/OriginalGame/extracted/'+f for f in originals],
                    'sourceSHA256': {Path(f).name: hashlib.sha256((SOURCE/f).read_bytes()).hexdigest() for f in originals}}
        (out / 'reference-evidence.json').write_text(json.dumps(evidence, ensure_ascii=False, indent=2)+'\n')
        print(name, 'frames', len(frames), 'animations', len(record['animations']), 'portrait', portrait.size)


if __name__ == '__main__':
    main()
