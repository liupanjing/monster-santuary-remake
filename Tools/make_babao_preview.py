"""Encode the built Unity player's captures; no synthesized/interpolated motion."""
from pathlib import Path
import json
import shutil
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'Production/Runtime/babao-preview'
CHAPTER = ROOT / 'Production/Runtime/earth-babao-v3'
OUT = ROOT / 'Art/Companions/babao/v3'
build_time = (ROOT / 'Production/babao-build.log').stat().st_mtime
for log, marker in [('babao-preview.log', 'BABAO_MOTION_RUNTIME_PASSED'), ('earth-babao-v3.log', 'EARTH_2D_RUNTIME_PASSED')]:
    path = ROOT / 'Production' / log
    assert marker in path.read_text() and path.stat().st_mtime >= build_time, 'Require current successful runtime'
paths = sorted(SOURCE.glob('frame-*.png'))
assert len(paths) == 96
assert all(path.stat().st_mtime >= build_time for path in paths), 'Reject stale captures'
font = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 20)
small = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 17)
frames = []
for i, path in enumerate(paths):
    image = Image.open(path).convert('RGB'); draw = ImageDraw.Draw(image)
    draw.text((28, 20), '八宝 · 四方向悬浮 / 扇翅 · '+('待机' if i < 24 else '跟随时加快扇翅'), font=font, fill='#253244')
    for j, label in enumerate(('下', '左', '右 · 镜像', '上')):
        draw.text((125 + j * 300, 280), label, font=small, fill='#53545b')
    draw.text((28, 340), 'Q 版主角 + 独立八宝 · 沿同一路径过三个转角', font=font, fill='#253244')
    draw.text((28, 685), '实际 Unity 预览场景采样；圆身、翅膀帧、悬浮高度和地面影子分开控制。', font=small, fill='#53545b')
    frames.append(image)
sheet = Image.new('RGB', (320 * 8, 180 * 3))
for i, index in enumerate(range(0, 96, 4)):
    sheet.paste(frames[index].resize((320, 180), Image.Resampling.LANCZOS), (i % 8 * 320, i // 8 * 180))
palette = sheet.quantize(colors=256)
images = [frame.quantize(palette=palette, dither=Image.Dither.NONE) for frame in frames]
images[0].save(OUT / 'babao-motion-preview.gif', save_all=True, append_images=images[1:], duration=([40] * 5 + [50]) * 16, loop=0, disposal=2)
contact = Image.new('RGB', (1280, 360), '#e9e6df')
for i, index in enumerate((0, 12, 24, 36, 48, 60, 72, 84)):
    contact.paste(frames[index].resize((320, 180), Image.Resampling.LANCZOS), (i % 4 * 320, i // 4 * 180))
contact.save(OUT / 'runtime-contact.png')
for folder, name in [(SOURCE, 'motion-runtime.txt'), (CHAPTER, 'chapter-runtime.txt')]:
    shutil.copy2(folder / 'runtime-validation.txt', OUT / name)
for name in ('01-exploration', '09-party-babao', '10-switched-battle', '11-babao-return'):
    shutil.copy2(CHAPTER / (name + '.png'), OUT / (name + '.png'))
(OUT / 'preview-cadence.json').write_text(json.dumps({'source': 'Built Unity BabaoMotionPreview scene, not chapter gameplay footage',
    'samples': 96, 'samplesPerSecond': 24, 'drawingLoop': [0,1,2,3,2,1],
    'uniqueNewDrawings': 12, 'rightMirrorsLeft': True,
    'idlePhaseRate': 8, 'followPhaseRate': 12, 'interpolation': 'none',
    'chapterEvidence': ['01-exploration.png', '09-party-babao.png', '10-switched-battle.png', '11-babao-return.png']}, ensure_ascii=False, indent=2)+'\n')
print('Saved actual Unity Babao GIF and chapter evidence.')
