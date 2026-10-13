"""Cut static Babao proposals and lay out references; no painted edits or motion."""
from pathlib import Path
import collections
import hashlib
import json
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'Art/Companions/babao/v1'


def main():
    source = OUT / 'source/design.png'
    atlas = Image.open(source).convert('RGBA')
    w, h = atlas.size
    alpha = atlas.getchannel('A').tobytes()
    seen = bytearray(w * h)
    owned = [[] for _ in range(3)]
    for seed in range(w * h):
        if seen[seed] or alpha[seed] < 32:
            continue
        queue = collections.deque([seed])
        seen[seed] = 1
        pixels = []
        votes = collections.Counter()
        while queue:
            pixel = queue.popleft()
            pixels.append(pixel)
            x, y = pixel % w, pixel // w
            votes[min(2, x * 3 // w)] += 1
            for yy in range(max(0, y - 1), min(h, y + 2)):
                for xx in range(max(0, x - 1), min(w, x + 2)):
                    neighbor = yy * w + xx
                    if not seen[neighbor] and alpha[neighbor] >= 32:
                        seen[neighbor] = 1
                        queue.append(neighbor)
        if len(pixels) >= 64:
            owned[votes.most_common(1)[0][0]].extend(pixels)
    frames = []
    records = []
    (OUT / 'static').mkdir(exist_ok=True)
    for i, name in enumerate(('front', 'left', 'back')):
        mask = bytearray(w * h)
        for pixel in owned[i]:
            mask[pixel] = alpha[pixel]
        image = atlas.copy()
        image.putalpha(Image.frombytes('L', (w, h), bytes(mask)))
        bounds = image.getbbox()
        assert bounds and len(owned[i]) > 10000, name
        image = image.crop(bounds)
        path = OUT / 'static' / f'{name}.png'
        image.save(path)
        frames.append(image)
        records.append({'view': name, 'atlasBounds': bounds, 'opaquePixels': len(owned[i]),
                        'output': str(path.relative_to(ROOT))})
    font_path = '/System/Library/Fonts/STHeiti Medium.ttc'
    title = ImageFont.truetype(font_path, 32)
    label = ImageFont.truetype(font_path, 22)
    small = ImageFont.truetype(font_path, 18)
    board = Image.new('RGB', (1500, 960), '#e9e6df')
    draw = ImageDraw.Draw(board)
    draw.text((32, 22), '八宝 · 原版 / 2D 重绘 v1 · 静态造型待审', font=title, fill='#252833')
    draw.text((32, 72), '原版来自 cw.png + cw.bin 合成；新版为透明手绘候选。尚未制作新版动作或接入试玩。', font=small, fill='#53545b')
    for i, (frame, name) in enumerate(zip((4, 7, 1), ('正面', '侧面', '背面'))):
        base = i * 500
        draw.rounded_rectangle((base + 15, 114, base + 485, 623), radius=12, fill='#f8f6f1')
        draw.text((base + 195, 130), name, font=label, fill='#252833')
        original = Image.open(OUT / 'original' / f'cw-frame-{frame:02}.png').convert('RGBA')
        original = original.crop(original.getbbox())
        original = original.resize((original.width * 11, original.height * 11), Image.Resampling.NEAREST)
        board.paste(original, (base + (500 - original.width) // 2, 180), original)
        draw.text((base + 145, 364), '原版 · 最近邻放大', font=small, fill='#53545b')
        im = frames[i]
        scale = 180 / max(f.height for f in frames)
        im = im.resize((round(im.width * scale), round(im.height * scale)), Image.Resampling.LANCZOS)
        board.paste(im, (base + (500 - im.width) // 2, 585 - im.height), im)
        draw.text((base + 155, 592), '新版 · 共用缩放', font=small, fill='#53545b')
    draw.text((40, 660), '与当前 Q 版主角同框 · 静态排版，非 Unity 运行截图', font=label, fill='#252833')
    hero = Image.open(ROOT / 'Unity/Assets/Remake/Resources/HeroMotion/Art/walk/1-0.png').convert('RGBA')
    hero = hero.crop(hero.getbbox())
    hero = hero.resize((round(hero.width * 210 / hero.height), 210), Image.Resampling.LANCZOS)
    board.paste(hero, (660, 902 - hero.height), hero)
    companion = frames[0]
    # Original cw front includes a shadow; this ratio is a rough scale proposal.
    companion_h = round(210 * 15 / 23)
    companion = companion.resize((round(companion.width * companion_h / companion.height), companion_h), Image.Resampling.LANCZOS)
    board.paste(companion, (470, 882 - companion.height), companion)
    draw.text((40, 923), '保留青蓝圆身、卷曲突起、眯眼与粉颊。侧面翅膀/背部折线为低像素资料的重绘解释，比例待游戏镜头确认。', font=small, fill='#53545b')
    board.save(OUT / 'babao-original-vs-redraw.png')
    manifest = {'tool': 'built-in image_gen', 'status': 'static_design_candidate',
                'approvedVersion': None, 'approvedBy': None, 'animationFrames': 0,
                'runtimeIntegrated': False, 'views': records,
                'cutPolicy': 'Shared atlas scale; alpha >= 32 connected regions >= 64 pixels, preserving retained alpha; no repainting',
                'sourceSHA256': hashlib.sha256(source.read_bytes()).hexdigest(),
                'prompts': ['prompts/design.txt', 'prompts/fidelity-correction.txt'],
                'references': ['original-three-views.png', 'Unity/Assets/Remake/Resources/HeroMotion/Art/walk/1-0.png'],
                'limits': ['Wing shape and back folded line interpreted from small original sprites',
                           'Same-screen example is a static montage, not gameplay verification',
                           'cw2 alternate magenta palette found; story purpose unconfirmed, not adopted']}
    (OUT / 'generation-manifest.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps({'views': records, 'trueTransparency': atlas.getextrema()[3], 'comparison': str(OUT / 'babao-original-vs-redraw.png')}))


if __name__ == '__main__':
    main()
