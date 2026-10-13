"""Slice hand-drawn wing poses, register body landmarks, mirror the symmetric side."""
from pathlib import Path
import collections
import hashlib
import json
import statistics
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / 'Art/Companions/babao/v3'
OUTPUT = ROOT / 'Unity/Assets/Remake/Resources/BabaoMotion/Art'


def isolate(path, columns, rows):
    image = Image.open(path).convert('RGBA')
    width, height = image.size
    alpha = image.getchannel('A').tobytes()
    assert min(alpha) == 0 and max(alpha) > 240, 'Real alpha required'
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
        if len(pixels) >= 64:
            owned[votes.most_common(1)[0][0]].extend(pixels)
    frames = []
    for pixels in owned:
        mask = bytearray(width * height)
        for pixel in pixels:
            mask[pixel] = alpha[pixel]
        frame = image.copy(); frame.putalpha(Image.frombytes('L', (width, height), bytes(mask)))
        bounds = frame.getbbox(); assert bounds, path
        frames.append((frame.crop(bounds), bounds))
    return frames


def main():
    records = []; result = {}
    # Circle diameters measured from the stable core body, excluding tuft/wings.
    # One common scale for all drawings in an atlas, never per-frame bbox scaling.
    specifications = [('down', 'front', 3, 2, 280), ('left', 'left', 2, 2, 303),
                      ('up', 'back', 2, 2, 342)]
    for direction, source, columns, rows, diameter in specifications:
        path = REVIEW / 'source' / (source + '.png')
        frames = isolate(path, columns, rows)
        scale = 76 / diameter
        result[direction] = []
        folder = OUTPUT / direction; folder.mkdir(parents=True, exist_ok=True)
        for i, (frame, bounds) in enumerate(frames[:4]):
            a = frame.getchannel('A')
            centers = []
            for offset in (12, 16, 20):
                xs = [x for x in range(frame.width) if a.getpixel((x, frame.height - offset)) >= 200]
                assert xs
                centers.append((min(xs) + max(xs)) / 2)
            anchor_x = statistics.median(centers)
            anchor_y = frame.height - .43 * diameter
            resized = frame.resize((round(frame.width * scale), round(frame.height * scale)), Image.Resampling.LANCZOS)
            x = round(96 - anchor_x * scale); y = round(96 - anchor_y * scale)
            assert x >= 0 and y >= 0 and x + resized.width <= 192 and y + resized.height <= 192, (direction, i, x, y)
            canvas = Image.new('RGBA', (192, 192)); canvas.alpha_composite(resized, (x, y))
            output = folder / f'{i}.png'; canvas.save(output); result[direction].append(canvas)
            records.append({'direction': direction, 'index': i, 'atlasBounds': bounds,
                            'source': str(path.relative_to(ROOT)), 'bodyDiameter': diameter,
                            'anchor': [anchor_x, anchor_y], 'commonScale': scale,
                            'resource': str(output.relative_to(ROOT / 'Unity/Assets/Remake/Resources')).removesuffix('.png')})
    folder = OUTPUT / 'right'; folder.mkdir(exist_ok=True); result['right'] = []
    for i, frame in enumerate(result['left']):
        mirrored = frame.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
        mirrored.save(folder / f'{i}.png'); result['right'].append(mirrored)
        records.append({'direction': 'right', 'index': i, 'mirrorOf': 'left',
                        'resource': f'BabaoMotion/Art/right/{i}'})
    font = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 20)
    board = Image.new('RGB', (1020, 930), '#e9e6df'); draw = ImageDraw.Draw(board)
    draw.text((22, 14), '八宝 v3 · 四方向扇翅关键帧 · 正 / 侧 / 背独立绘制，右向镜像左向', font=font, fill='#253244')
    for row, (direction, label) in enumerate([('down', '下'), ('left', '左'), ('right', '右 · 镜像'), ('up', '上')]):
        draw.text((8, 68 + row * 210), label, font=font, fill='#253244')
        for col, frame in enumerate(result[direction]):
            board.paste(frame, (130 + col * 210, 54 + row * 210), frame)
    draw.text((22, 900), '4 张关键帧按 0→1→2→3→2→1 循环；共用缩放与身体中心。悬浮起伏由运行组件独立控制。', font=font, fill='#53545b')
    board.save(REVIEW / 'motion-contact.png')
    manifest = {'designApprovedVersion': 'v2', 'motionVersion': 'v3', 'status': 'motion_candidate',
                'tool': 'built-in image_gen', 'directions': ['down', 'left', 'right', 'up'],
                'canvas': [192, 192], 'PPU': 100, 'pivot': [.5, .5], 'bodyDiameterPixels': 76,
                'distinctNewDrawings': 12, 'runtimeSprites': 16, 'rightView': 'Mirror of symmetric left anatomy',
                'loop': [0, 1, 2, 3, 2, 1], 'idleSamplesPerSecond': 8, 'followSamplesPerSecond': 12,
                'registration': 'Common scale per atlas; lower-circle center and bottom landmark registered to a common body center; no per-frame resizing',
                'cutting': 'Alpha >=32 connected components >=64 pixels, assigned by majority cell; retained alpha preserved',
                'frontUnused': 'Source front frames4/5 have mistimed raised-wing phases; only first four used, no claim of six distinct cycle drawings',
                'frames': records,
                'sourceSHA256': {p.name: hashlib.sha256(p.read_bytes()).hexdigest() for p in (REVIEW / 'source').glob('*.png')},
                'prompts': [str(p.relative_to(REVIEW)) for p in sorted((REVIEW / 'prompts').glob('*.txt'))]}
    (REVIEW / 'motion-manifest.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
    print('Prepared 12 drawings + 4 mirrored sprites; transparent four-direction motion resources.')


if __name__ == '__main__':
    main()
