"""Decode original cw/cw2 composites for Babao's visual review; preserve source bytes."""
from pathlib import Path
import hashlib
import json
import struct
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'Reference/OriginalGame/extracted'
OUT = ROOT / 'Art/Companions/babao/v1'


def decode(name, out=OUT):
    data = (SOURCE / f'{name}.bin').read_bytes()
    position = 4

    def read(fmt):
        nonlocal position
        values = struct.unpack_from('>' + fmt, data, position)
        position += struct.calcsize(fmt)
        return values[0] if len(values) == 1 else values

    rects = [read('4i') for _ in range(read('i'))]
    frames = []
    for _ in range(read('B')):
        position += 2
        frames.append([read('4H') for _ in range(read('B'))])
    animations = []
    for _ in range(read('B')):
        position += 1
        sequence = [read('2B') for _ in range(read('B'))]
        animations.append({'sequence': sequence, 'offset': [read('h') >> 8, read('h') >> 8]})
    atlas = Image.open(SOURCE / f'{name}.png').convert('RGBA')
    images = []
    for i, parts in enumerate(frames):
        canvas = Image.new('RGBA', (96, 96))
        for sprite, flags, x, y in parts:
            left, top, width, height = rects[sprite]
            part = atlas.crop((left, top, left + width, top + height))
            x = x if x < 32768 else x - 65536
            y = y if y < 32768 else y - 65536
            if flags & 1:
                part = part.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
                x -= width
            if flags & 2:
                part = part.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
                y -= height
            canvas.alpha_composite(part, (48 + x, 48 + y))
        canvas.save(out / 'original' / f'{name}-frame-{i:02}.png')
        images.append(canvas)
    return images, {'resource': name, 'rectangles': rects, 'frames': frames,
                    'animations': animations, 'decodedBytes': position,
                    'unusedTailBytes': len(data) - position}


def main():
    (OUT / 'original').mkdir(parents=True, exist_ok=True)
    font = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 22)
    records = []
    for resource in ('cw', 'cw2'):
        images, record = decode(resource)
        records.append(record)
        board = Image.new('RGB', (1200, ((len(images) + 5) // 6) * 220 + 80), '#292d36')
        draw = ImageDraw.Draw(board)
        draw.text((24, 20), f'原版 {resource} · 按 y.java 合成 · 最近邻放大', font=font, fill='white')
        for i, im in enumerate(images):
            crop = im.crop(im.getbbox())
            crop = crop.resize((crop.width * 8, crop.height * 8), Image.Resampling.NEAREST)
            x = (i % 6) * 200 + (200 - crop.width) // 2
            y = 80 + (i // 6) * 220 + (170 - crop.height) // 2
            board.paste(crop, (x, y), crop)
            draw.text((i % 6 * 200 + 18, 80 + i // 6 * 220 + 180), f'frame {i:02}', font=font, fill='white')
        board.save(OUT / f'original-{resource}-contact.png')
        if resource == 'cw':
            reference = Image.new('RGB', (960, 380), '#e9e6df')
            rd = ImageDraw.Draw(reference)
            for j, (frame, label) in enumerate(((4, '正面 · frame 04'), (7, '侧面 · frame 07'), (1, '背面 · frame 01'))):
                crop = images[frame].crop(images[frame].getbbox())
                crop = crop.resize((crop.width * 14, crop.height * 14), Image.Resampling.NEAREST)
                reference.paste(crop, (j * 320 + (320 - crop.width) // 2, 55), crop)
                rd.text((j * 320 + 28, 320), label, font=font, fill='#25252c')
            reference.save(OUT / 'original-three-views.png')
    (OUT / 'original-decoding.json').write_text(json.dumps({
        'formatEvidence': 'Reference/OriginalGame/decompiled/y.java',
        'sourceHashes': {f'{r}.{ext}': hashlib.sha256((SOURCE / f'{r}.{ext}').read_bytes()).hexdigest()
                         for r in ('cw', 'cw2') for ext in ('png', 'bin')},
        'resources': records,
    }, ensure_ascii=False, indent=2) + '\n')
    print([(r['resource'], len(r['frames']), r['animations']) for r in records])


if __name__ == '__main__':
    main()
