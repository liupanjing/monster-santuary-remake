"""Compose unchanged original pixels and generated redraw views for approval."""
from pathlib import Path
import argparse
import json
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / 'Art/Reviews/51'
parser = argparse.ArgumentParser()
parser.add_argument('--version', default='v3')
args = parser.parse_args()
OUT = REVIEW / args.version
sheet = Image.open(OUT / 'hero-redraw-sheet.png').convert('RGBA')
width, height = sheet.size
# The generation layout has longer battle figures. Cut in the empty gutter,
# not at the geometric midpoint, which would include battle boot fragments.
split_y = round(height * .61)
figures = {}
for index, name in enumerate(['battle-front', 'battle-back', 'field-front', 'field-back']):
    top, bottom = (0, split_y) if index < 2 else (split_y, height)
    left, right = (0, width // 2) if index % 2 == 0 else (width // 2, width)
    cell = sheet.crop((left, top, right, bottom))
    # Ignore invisible alpha 1..7 specks when measuring occupied bounds.
    # Original generated alpha inside the selected crop remains unchanged.
    bounds = cell.getchannel('A').point(lambda a: 255 if a >= 8 else 0).getbbox()
    assert bounds, name
    figures[name] = cell.crop(bounds)
    figures[name].save(OUT / ('redraw-' + name + '.png'))

font_path = '/System/Library/Fonts/STHeiti Medium.ttc'
title = ImageFont.truetype(font_path, 34)
heading = ImageFont.truetype(font_path, 24)
small = ImageFont.truetype(font_path, 19)
board = Image.new('RGB', (1600, 1130), '#e9e6df')
draw = ImageDraw.Draw(board)
draw.text((40, 25), '奥尼克 · 原版 / 2D 重绘对照 · ' + args.version + ' 待确认', font=title, fill='#252833')
draw.text((40, 75), '两套独立精灵：战斗对照原版身体比例；跑图使用原版 Q 版比例。', font=small, fill='#53545b')
originals = {
    'battle-front': REVIEW / 's51_0.png', 'battle-back': REVIEW / 's51_1.png',
    'field-front': OUT / 'original-field-front.png', 'field-back': OUT / 'original-field-back.png',
}
labels = ['原版 · 正面', '重绘 · 正面', '原版 · 背面', '重绘 · 背面']
for row, kind in enumerate(['battle', 'field']):
    top = 115 if row == 0 else 690
    baseline = 640 if row == 0 else 1030
    draw.rounded_rectangle((25, top, 1575, baseline + 32), radius=12, fill='#f7f5ef')
    draw.text((45, top + 16), '战斗精灵' if row == 0 else '跑图精灵（Q 版）', font=heading, fill='#27465b')
    for column, label in enumerate(labels):
        draw.text((column * 400 + 80, top + 58), label, font=heading, fill='#252833')
    for pair, orientation in enumerate(['front', 'back']):
        name = kind + '-' + orientation
        image = Image.open(originals[name]).convert('RGBA')
        image = image.crop(image.getchannel('A').getbbox())
        original = image.resize((image.width * 9, image.height * 9), Image.Resampling.NEAREST)
        redraw = figures[name]
        factor = original.height / redraw.height
        redraw = redraw.resize((round(redraw.width * factor), original.height), Image.Resampling.LANCZOS)
        for column, view in [(pair * 2, original), (pair * 2 + 1, redraw)]:
            board.paste(view, (column * 400 + (400 - view.width) // 2, baseline - view.height), view)
draw.text((40, 1090), '原图：s51_0 / s51_1 与 npc0 图集合成静态帧。新版为造型审核稿，尚未制作动作或替换游戏资源。', font=small, fill='#53545b')
board.save(OUT / ('hero-original-vs-redraw-' + args.version + '.png'))
manifest_path = OUT / 'generation-manifest.json'
manifest = json.loads(manifest_path.read_text())
manifest['sheetSlicing'] = {'verticalGutter': split_y, 'method': 'empty-gutter cuts; no figure repaint'}
manifest['redrawnViewSizes'] = {name: list(image.size) for name, image in figures.items()}
manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
print('Saved comparison; four complete isolated views for', args.version)
