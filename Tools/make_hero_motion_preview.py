"""Encode actual Unity captures as labeled GIFs; no generated motion/interpolation."""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont
import shutil,json

ROOT=Path(__file__).resolve().parents[1]
SOURCE=ROOT/'Production/Runtime/hero-motion-preview'
OUT=ROOT/'Art/Reviews/51/v5'
paths=sorted(SOURCE.glob('frame-*.png'))
assert len(paths)==96
font=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',20)
small=ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc',17)
frames=[]
for i,path in enumerate(paths):
    image=Image.open(path).convert('RGB');draw=ImageDraw.Draw(image)
    t=i/24
    field='待机'if t<.25 else'行走 · 8 帧/秒'if t<2 else'奔跑 · 12 帧/秒'
    battle='呼吸待机'if t<.3 else'双枪射击 / 后坐力'if t<1.55 else'受击'if t<2.05 else'防御'if t<2.55 else'胜利'if t<3.25 else'回到待机'
    draw.text((55,25),'Q 版跑图 · '+field,font=font,fill='#253244')
    draw.text((630,25),'双枪战斗 · '+battle,font=font,fill='#253244')
    draw.text((110,285),'下',font=small,fill='#53545b');draw.text((310,285),'左',font=small,fill='#53545b')
    draw.text((110,550),'右',font=small,fill='#53545b');draw.text((310,550),'上',font=small,fill='#53545b')
    draw.text((715,600),'正面视角',font=small,fill='#53545b');draw.text((1040,600),'背面视角',font=small,fill='#53545b')
    draw.text((55,672),'实际 Unity 播放采样；身体、枪械、手部遮挡、枪口效果独立。',font=small,fill='#53545b')
    frames.append(image)
# A shared palette reduces quantization color flicker between frames.
palette_sheet=Image.new('RGB',(320*8,180*3))
for i,index in enumerate(range(0,96,4)):
    thumb=frames[index].resize((320,180),Image.Resampling.LANCZOS)
    palette_sheet.paste(thumb,(i%8*320,i//8*180))
palette=palette_sheet.quantize(colors=256)
quantized=[frame.quantize(palette=palette,dither=Image.Dither.NONE)for frame in frames]
timing=([40]*5+[50])*16
quantized[0].save(OUT/'hero-motion-preview.gif',save_all=True,append_images=quantized[1:],duration=timing,loop=0,disposal=2)
for name,box in [('field',(25,0,490,620)),('battle',(575,0,1280,650))]:
    images=[frame.crop(box).quantize(palette=palette,dither=Image.Dither.NONE)for frame in frames]
    images[0].save(OUT/(name+'-motion-preview.gif'),save_all=True,append_images=images[1:],duration=timing,loop=0,disposal=2)
shutil.copy2(SOURCE/'runtime-validation.txt',OUT/'runtime-validation.txt')
(OUT/'preview-cadence.json').write_text(json.dumps({'samples':96,'sampleCadence':24,'source':'Built Unity player, HeroMotionPreview scene','sourceFrames':36,'battleSourceActionFramesUsed':20,'approvedBattleIdleViews':2,'movementDrawingCadence':{'walk':8,'run':12},'gifTiming':'GIF 10ms units use five 40ms frames plus one 50ms frame, averaging 24 samples/s; no interpolated drawings or native game FPS claims'},ensure_ascii=False,indent=2)+'\n')
print('Encoded actual Unity field/battle/full previews.')
