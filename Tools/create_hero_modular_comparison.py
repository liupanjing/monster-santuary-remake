"""Layout original sprites beside the actual Unity modular-weapon preview."""
from pathlib import Path
import json
from PIL import Image, ImageDraw, ImageFont, ImageChops

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / 'Art/Reviews/51'
OUT = REVIEW / 'v4'
render = Image.open(OUT / 'unity-dual-pistol-preview.png').convert('RGB')
views = {}
for index, name in enumerate(['battle-front', 'battle-back']):
    cell = render.crop((index * render.width // 2, 0, (index + 1) * render.width // 2, render.height))
    backdrop = Image.new('RGB', cell.size, cell.getpixel((0, 0)))
    difference = ImageChops.difference(cell, backdrop).convert('L').point(lambda v: 255 if v >= 10 else 0)
    bounds = difference.getbbox()
    assert bounds, name
    views[name] = cell.crop(bounds)
for name in ['field-front', 'field-back']:
    image = Image.open(REVIEW / 'v3' / ('redraw-' + name + '.png')).convert('RGBA')
    views[name] = image.crop(image.getchannel('A').point(lambda a: 255 if a >= 8 else 0).getbbox())

font_path = '/System/Library/Fonts/STHeiti Medium.ttc'
title = ImageFont.truetype(font_path, 32)
heading = ImageFont.truetype(font_path, 22)
small = ImageFont.truetype(font_path, 18)
background = render.getpixel((0, 0))
board = Image.new('RGB', (1600, 1130), '#e9e6df')
draw = ImageDraw.Draw(board)
draw.text((40, 25), '奥尼克 · v4 双枪与独立武器图层 · 待确认', font=title, fill='#252833')
draw.text((40, 75), '上排新版来自 Unity 实际挂点渲染；枪为独立预览素材，后续每种枪单独替换。', font=small, fill='#53545b')
sources = {
    'battle-front': REVIEW / 's51_0.png', 'battle-back': REVIEW / 's51_1.png',
    'field-front': REVIEW / 'v3/original-field-front.png',
    'field-back': REVIEW / 'v3/original-field-back.png',
}
for row, kind in enumerate(['battle', 'field']):
    top, baseline = (115, 640) if row == 0 else (690, 1030)
    draw.rounded_rectangle((25, top, 1575, baseline + 32), radius=12, fill=background)
    draw.text((45, top + 16), '战斗精灵（身体 / 双枪分层）' if row == 0 else '跑图精灵（独立 Q 版，沿用待审稿）',
              font=heading, fill='#27465b')
    for column, label in enumerate(['原版 · 正面', '新版 · 正面', '原版 · 背面', '新版 · 背面']):
        draw.text((column * 400 + 80, top + 58), label, font=heading, fill='#252833')
    for pair, orientation in enumerate(['front', 'back']):
        name = kind + '-' + orientation
        original = Image.open(sources[name]).convert('RGBA')
        original = original.crop(original.getchannel('A').getbbox())
        original = original.resize((original.width * 9, original.height * 9), Image.Resampling.NEAREST)
        redraw = views[name]
        scale = original.height / redraw.height
        redraw = redraw.resize((round(redraw.width * scale), original.height), Image.Resampling.LANCZOS)
        for column, image in [(pair * 2, original), (pair * 2 + 1, redraw)]:
            position = (column * 400 + (400 - image.width) // 2, baseline - image.height)
            board.paste(image, position, image if image.mode == 'RGBA' else None)
draw.text((40, 1090), '当前只确认静态造型与模块结构；尚未制作动作，也未替换土之秘境中的旧占位角色。', font=small, fill='#53545b')
board.save(OUT / 'hero-original-vs-modular-v4.png')
print('Saved hero-original-vs-modular-v4.png')
