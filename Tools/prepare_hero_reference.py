"""Decode original npc0.bin composites for reference only; no new animation art."""
from pathlib import Path
import json
import struct
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'Reference/OriginalGame/extracted'
REVIEW = ROOT / 'Art/Reviews/51'
OUT = REVIEW / 'v2'
OUT.mkdir(exist_ok=True)
data = (SOURCE / 'npc0.bin').read_bytes()
position = 4


def read(fmt):
    global position
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
atlas = Image.open(SOURCE / 'npc0.png').convert('RGBA')


def signed(value):
    return value if value < 32768 else value - 65536


def render(animation, name):
    lo, hi = animations[animation]['sequence'][0]
    index = (lo + (hi << 8)) & 1023
    canvas = Image.new('RGBA', (96, 96))
    for sprite, flags, x, y in frames[index]:
        left, top, width, height = rects[sprite]
        image = atlas.crop((left, top, left + width, top + height))
        x, y = signed(x), signed(y)
        if flags & 1:
            image = image.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
            x -= width
        if flags & 2:
            image = image.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
            y -= height
        canvas.alpha_composite(image, (48 + x, 48 + y))
    bounds = canvas.getbbox()
    cropped = canvas.crop(bounds)
    cropped.save(OUT / name)
    return {'animation': animation, 'frame': index, 'bounds': list(bounds),
            'size': list(cropped.size), 'name': name}


# e.java direction map: up=4, down=5, left=6, right=7.
records = [render(5, 'original-field-front.png'), render(4, 'original-field-back.png')]
font = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 22)
board = Image.new('RGB', (1200, 840), '#e9e6df')
draw = ImageDraw.Draw(board)
sources = [
    (REVIEW / 's51_0.png', '战斗正面 · s51_0'),
    (REVIEW / 's51_1.png', '战斗背面 · s51_1'),
    (OUT / 'original-field-front.png', '跑图正面 · npc0 animation 5'),
    (OUT / 'original-field-back.png', '跑图背面 · npc0 animation 4'),
]
for i, (source, label) in enumerate(sources):
    image = Image.open(source).convert('RGBA')
    scale = 7 if i < 2 else 10
    image = image.resize((image.width * scale, image.height * scale), Image.Resampling.NEAREST)
    x = i % 2 * 600 + (600 - image.width) // 2
    y = i // 2 * 420 + 45
    board.paste(image, (x, y), image)
    draw.text((i % 2 * 600 + 25, i // 2 * 420 + 370), label, font=font, fill='#25252c')
board.save(OUT / 'original-two-forms-reference.png')
(OUT / 'field-source-decoding.json').write_text(json.dumps({
    'sourcePNG': 'Reference/OriginalGame/extracted/npc0.png',
    'sourceBIN': 'Reference/OriginalGame/extracted/npc0.bin',
    'formatEvidence': 'Reference/OriginalGame/decompiled/y.java',
    'actorEvidence': 'e.java lines 260–273; original-scenes.json actor 0 is npc0',
    'decodedBytes': position, 'unusedTailBytes': len(data) - position,
    'tailPolicy': 'y.java reads declared counts and does not consume the remaining tail',
    'references': records,
}, ensure_ascii=False, indent=2) + '\n')
print(records)
