"""Cut static story-character proposals and make source comparisons; no repainting."""
from pathlib import Path
import collections
import hashlib
import json
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
FONT = '/System/Library/Fonts/STHeiti Medium.ttc'


def isolate(path, columns, rows):
    image = Image.open(path).convert('RGBA'); w, h = image.size
    alpha = image.getchannel('A').tobytes()
    assert min(alpha) == 0 and max(alpha) > 240, 'True alpha required'
    seen = bytearray(w * h); owned = [[] for _ in range(columns * rows)]
    for seed in range(w * h):
        if seen[seed] or alpha[seed] < 32:
            continue
        queue = collections.deque([seed]); seen[seed] = 1
        pixels = []; votes = collections.Counter()
        while queue:
            pixel = queue.popleft(); pixels.append(pixel)
            x, y = pixel % w, pixel // w
            votes[min(rows-1, y*rows//h)*columns+min(columns-1, x*columns//w)] += 1
            for yy in range(max(0, y-1), min(h, y+2)):
                for xx in range(max(0, x-1), min(w, x+2)):
                    neighbor = yy*w+xx
                    if not seen[neighbor] and alpha[neighbor] >= 32:
                        seen[neighbor] = 1; queue.append(neighbor)
        if len(pixels) >= 64:
            owned[votes.most_common(1)[0][0]].extend(pixels)
    frames = []
    for pixels in owned:
        mask = bytearray(w*h)
        for p in pixels:
            mask[p] = alpha[p]
        frame = image.copy(); frame.putalpha(Image.frombytes('L', (w,h), bytes(mask)))
        bounds = frame.getbbox(); assert bounds
        frames.append((frame.crop(bounds), bounds))
    return frames


def paste(board, image, center, baseline, height, pixel=False):
    image = image.convert('RGBA')
    scale = height / image.height
    image = image.resize((round(image.width*scale), height), Image.Resampling.NEAREST if pixel else Image.Resampling.LANCZOS)
    board.paste(image, (round(center-image.width/2), baseline-height), image)


def main():
    for key, name, kind, columns, rows, labels in [
        ('xiya', '西雅', 'NPCs', 3, 1, ['portrait','field-front','field-back']),
        ('qibei', '七贝', 'Companions', 2, 2, ['portrait','field-front','field-left','field-back'])]:
        out = ROOT / f'Art/{kind}/{key}/v1'
        frames = isolate(out / 'source/design.png', columns, rows)
        (out / 'static').mkdir(exist_ok=True)
        records = []
        for label, (frame, bounds) in zip(labels, frames):
            frame.save(out / 'static' / (label+'.png'))
            records.append({'view': label, 'bounds': bounds, 'size': list(frame.size)})
        title = ImageFont.truetype(FONT, 30); heading = ImageFont.truetype(FONT, 21); small = ImageFont.truetype(FONT, 18)
        board = Image.new('RGB', (1500, 1000), '#e9e6df'); draw = ImageDraw.Draw(board)
        draw.text((30, 20), f'{name} · 原版 / 2D 重绘 v1 · 静态候选', font=title, fill='#252833')
        draw.text((30, 68), '原图为包内像素资源并最近邻放大；新版为透明素材。未制作新版动作或接入运行。', font=small, fill='#53545b')
        draw.rounded_rectangle((20, 110, 1480, 520), radius=12, fill='#f8f6f1')
        draw.text((60, 125), '原版头像', font=heading, fill='#252833')
        draw.text((690, 125), '头像重绘 · 独立于跑图比例', font=heading, fill='#252833')
        paste(board, Image.open(out / 'original/portrait.png'), 290, 485, 300, True)
        paste(board, frames[0][0], 1020, 485, 310)
        draw.rounded_rectangle((20, 545, 1480, 955), radius=12, fill='#f8f6f1')
        qfront = frames[1][0]; qback = frames[-1][0]
        original_front = Image.open(out / 'original/field-front.png')
        original_back = Image.open(out / 'original/field-back.png')
        for center, label in [(180,'原版 · 跑图正面'),(505,'新版 · Q 正面'),(900,'原版 · 跑图背面'),(1225,'新版 · Q 背面')]:
            draw.text((center-95, 565), label, font=heading, fill='#252833')
        target = 265 if key == 'xiya' else 220
        paste(board, original_front, 180, 900, target, True)
        paste(board, original_back, 900, 900, target, True)
        # Front/rear use a COMMON scale from the source sheet; do not independently stretch.
        scale = target / max(qfront.height, qback.height)
        paste(board, qfront, 505, 900, round(qfront.height*scale))
        paste(board, qback, 1225, 900, round(qback.height*scale))
        if key == 'qibei':
            draw.text((30, 970), '保留两套原版表情：头像蓝眼/粉颊，跑图眯眼/黄绿脸颊；粉色双翼各视角一致，侧面单独提供透明图。', font=small, fill='#53545b')
        else:
            draw.text((30, 970), '保留紫发、蓝眼、绿色服装与头饰、金色胸扣、粉色腰带。头饰具体构造为像素资料的候选解释；武器另制。', font=small, fill='#53545b')
        board.save(out / 'original-vs-redraw.png')
        manifest = {'tool': 'built-in image_gen', 'status': 'static_design_candidate',
                    'approvedVersion': None, 'approvedBy': None, 'runtimeIntegrated': False, 'animationFrames': 0,
                    'views': records, 'cutPolicy': 'Alpha >=32 connected regions >=64 pixels assigned by majority cell; retained alpha preserved, no semantic repainting',
                    'sourceSHA256': {p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in (out/'source').glob('*.png')},
                    'originalEvidence': 'reference-evidence.json',
                    'prompts': [str(p.relative_to(out)) for p in sorted((out/'prompts').glob('*.txt'))]}
        (out / 'generation-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
        print(name, records)


if __name__ == '__main__':
    main()
