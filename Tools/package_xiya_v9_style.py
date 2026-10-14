"""Lay out existing Seastar 2D drawings for style review, without pose synthesis."""
from pathlib import Path
import hashlib
import json
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'Art/NPCs/xiya/v9'
FONT = '/System/Library/Fonts/STHeiti Medium.ttc'


def font(size):
    return ImageFont.truetype(FONT, size)


def fit(board, path, rect):
    im = Image.open(path).convert('RGBA')
    bounds = im.getbbox()
    assert bounds, f'Empty artwork: {path}'
    im = im.crop(bounds)
    x, y, w, h = rect
    im.thumbnail((w, h), Image.Resampling.LANCZOS)
    board.alpha_composite(im, (int(x+(w-im.width)/2), int(y+h-im.height)))


def main():
    # Independent static illustrations: layout sizing does not establish a
    # shared animation canvas or claim motion continuity.
    board = Image.new('RGBA', (1500, 920), '#eeece3')
    draw = ImageDraw.Draw(board)
    draw.text((35, 25), '西雅 · v9 二维画风纠正', fill='#273a32', font=font(33))
    draw.text((35, 74), '旧版已退回；新图是造型候选，三个姿态没有拼接成动画。', fill='#526351', font=font(20))
    draw.text((35, 132), '旧稿：三维渲染观感（退回）', fill='#70454c', font=font(21))
    draw.text((535, 132), 'v9：二维线条、平涂、硬边阴影', fill='#335947', font=font(21))
    draw.text((1060, 132), '原版像素：头像与Q版', fill='#335947', font=font(21))
    fit(board, ROOT/'Art/NPCs/xiya/v8/frames/000.png', (35, 180, 450, 665))
    fit(board, OUT/'seastar/ready.png', (540, 180, 450, 665))
    # Original pixel evidence uses nearest-neighbor display rather than smoothing.
    original = Image.open(ROOT/'Art/NPCs/xiya/v1/original/portrait.png').convert('RGBA')
    original.thumbnail((330,330))
    ratio = min(330/original.width, 330/original.height)
    original = original.resize((int(original.width*ratio),int(original.height*ratio)), Image.Resampling.NEAREST)
    board.paste('#eeece3', (1050,180,1480,540))
    board.alpha_composite(original, (int(1245-original.width/2),200))
    original = Image.open(ROOT/'Art/NPCs/xiya/v1/original/field-front.png').convert('RGBA')
    original.thumbnail((230,230))
    ratio = min(230/original.width,230/original.height)
    original = original.resize((int(original.width*ratio),int(original.height*ratio)),Image.Resampling.NEAREST)
    board.alpha_composite(original, (int(1245-original.width/2),590))
    draw.text((1040, 842), '原包没有可确认的战斗全身图', fill='#526351', font=font(20))
    draw.text((35, 883), 'Seastar 重绘 · 身体/剑独立保存 · 未导入正式 Unity 角色', fill='#526351', font=font(20))
    old = Image.open(ROOT/'Art/NPCs/xiya/v8/frames/000.png').convert('RGBA')
    old.crop(old.getbbox()).save(OUT/'v8-rejected-static-crop.png')
    board.convert('RGB').save(OUT/'style-comparison.png')

    poses = Image.new('RGBA', (1650, 720), '#eeece3')
    d = ImageDraw.Draw(poses)
    d.text((25, 18), '西雅 v9 · 三个静态动作姿态（不是连续动画）', fill='#273a32', font=font(29))
    for i, (name, label) in enumerate([('key-high','高位举剑'),('key-cut','挥出经过'),('key-low','低位收势')]):
        d.text((25+i*550, 78), label, fill='#335947', font=font(23))
        fit(poses, OUT/f'seastar/{name}.webp', (25+i*550, 130, 500, 555))
    poses.convert('RGB').save(OUT/'key-poses.png')

    provenance = json.loads((OUT/'seastar-provenance.json').read_text())
    provenance['localFiles'] = {}
    for p in sorted((OUT/'seastar').glob('*')):
        if p.is_file():
            im = Image.open(p).convert('RGBA')
            assert im.getbbox(), p
            provenance['localFiles'][p.name] = dict(sha256=hashlib.sha256(p.read_bytes()).hexdigest(), size=list(im.size), alphaExtrema=list(im.getchannel('A').getextrema()))
    provenance['presentation'] = 'Static layout only; per-panel fitting is not an animation normalization or pose synthesis.'
    (OUT/'seastar-provenance.json').write_text(json.dumps(provenance, ensure_ascii=False, indent=2)+'\n')
    print('Packaged static style comparison and three independent keys; no animation created.')


if __name__ == '__main__':
    main()
