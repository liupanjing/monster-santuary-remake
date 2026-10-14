"""Package actual Unity camera captures for individual art review; no art approval."""
from pathlib import Path
import argparse
import hashlib
import json
import shutil
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'Art/Reviews/story-motion-v2'
FONT = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 24)
SMALL = ImageFont.truetype('/System/Library/Fonts/STHeiti Medium.ttc', 18)
MODES = [
    ('西雅 · 战斗', '待机 → 蓄力斩击 → 受击 → 恢复', 'Art/NPCs/xiya/v2', 'battle'),
    ('西雅 · Q 版跑图', '四方向：下 / 左 / 右 / 上；每方向 6 张行走关键帧', 'Art/NPCs/xiya/v2', 'field'),
    ('七贝 · 战斗', '粉白脸 / 蓝眼 / 粉颊；悬浮与双翼扇动', 'Art/Companions/qibei/v2', 'battle'),
    ('七贝 · Q 版跑图', '下 / 左 / 右 / 上；右向镜像左向；每方向 4 张翼形', 'Art/Companions/qibei/v2', 'field'),
    ('西雅与七贝 · 路径跟随', '七贝沿西雅走过的路径转弯；固定随行距离', 'Art/Reviews/story-motion-v2', 'follow'),
]


def labelled(im, mode, index):
    title, sub, _, _ = MODES[mode]
    # Fixed crops only; every frame keeps the exact Unity camera scale.
    if mode in (1, 3):
        im = im.crop((100, 160, 1220, 570))
    im = im.copy()
    im.thumbnail((1000, 562), Image.Resampling.LANCZOS)
    board = Image.new('RGB', (im.width, im.height + 110), '#e8e6df')
    board.paste(im, (0, 70))
    draw = ImageDraw.Draw(board)
    draw.text((20, 12), title + '  ·  动作候选 v2', fill='#252a28', font=FONT)
    draw.text((20, 43), sub, fill='#565d56', font=SMALL)
    draw.text((20, im.height + 77), 'Unity 实际录制 · 原版资料在左 · 16 次采样/秒', fill='#565d56', font=SMALL)
    draw.text((im.width - 155, im.height + 77), f'{index / 16:.2f}s / 4s', fill='#565d56', font=SMALL)
    return board


def main(source):
    assert 'PASS:' in (source / 'runtime-validation.txt').read_text()
    OUT.mkdir(parents=True, exist_ok=True)
    report = {'humanApproved': False, 'source': str(source.relative_to(ROOT)), 'captureType': 'Unity Camera.Render', 'fps': 16, 'modes': [], 'rawSHA256': {}}
    for mode, (title, subtitle, relative, kind) in enumerate(MODES):
        paths = sorted((source / f'mode-{mode}').glob('frame-*.png'))
        assert len(paths) == 64
        dst = OUT / f'frames/mode-{mode}'
        dst.mkdir(parents=True, exist_ok=True)
        web = OUT / f'web/mode-{mode}'
        web.mkdir(parents=True, exist_ok=True)
        rendered = []
        for index, path in enumerate(paths):
            shutil.copy2(path, dst / path.name)
            Image.open(path).convert('RGB').save(web / (path.stem + '.webp'), lossless=True)
            report['rawSHA256'][str((dst / path.name).relative_to(OUT))] = hashlib.sha256(path.read_bytes()).hexdigest()
            rendered.append(labelled(Image.open(path).convert('RGB'), mode, index))
        folder = ROOT / relative
        gif = folder / f'{kind}-unity-preview.gif'
        # GIF timing has 10 ms resolution: alternate 60/70/60/60 = 250 ms.
        durations = [60, 70, 60, 60] * 16
        pal = rendered[0].quantize(colors=240)
        frames = [im.quantize(palette=pal, dither=Image.Dither.NONE) for im in rendered]
        frames[0].save(gif, save_all=True, append_images=frames[1:], duration=durations, loop=0, optimize=False, disposal=2)
        selected = [0, 20, 24, 26, 28, 30, 34, 38] if mode == 0 else [0, 4, 12, 20, 28, 36, 44, 60]
        contact = Image.new('RGB', (1000, 4 * 360), '#e8e6df')
        d = ImageDraw.Draw(contact)
        for j, index in enumerate(selected):
            im = Image.open(paths[index]).convert('RGB')
            im.thumbnail((500, 320), Image.Resampling.LANCZOS)
            x, y = j % 2 * 500, j // 2 * 360
            contact.paste(im, (x, y))
            d.text((x + 15, y + 320), f'{title} · {index / 16:.2f}s', fill='#252a28', font=SMALL)
        contact_path = folder / f'{kind}-unity-contact.png'
        contact.save(contact_path)
        report['modes'].append({'mode': mode, 'title': title, 'subtitle': subtitle, 'frames': 64, 'gif': str(gif.relative_to(ROOT)), 'contact': str(contact_path.relative_to(ROOT))})
    shutil.copy2(source / 'runtime-validation.txt', OUT / 'runtime-validation.txt')
    (OUT / 'runtime-capture-manifest.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    (OUT / 'index.html').write_text(HTML, encoding='utf-8')
    print('Packaged 320 actual Unity frames, five previews, and review page:', OUT)


HTML = '''<!doctype html>
<html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>西雅与七贝 · 逐角色动作审核</title>
<style>
*{box-sizing:border-box}body{margin:0;background:#ebe9e2;color:#242d28;font:16px/1.65 system-ui,-apple-system,"PingFang SC",sans-serif}main{max-width:1280px;margin:auto;padding:34px 32px}.eyebrow{letter-spacing:3px;font-size:12px;color:#64756a}h1{font-size:34px;margin:8px 0 18px}p{margin:10px 0}.tabs{display:flex;gap:8px;flex-wrap:wrap;margin:18px 0}button,select{font:inherit;border:1px solid #bdc5ba;border-radius:7px;padding:9px 17px;background:#f8f7f1;color:#263c30;cursor:pointer}button.active{background:#315941;color:#fff;border-color:#315941}.top{display:flex;align-items:center;justify-content:space-between;gap:20px}.badge{font-size:13px;background:#dfd0b6;color:#655039;padding:6px 12px;border-radius:30px}.stage{border:1px solid #c9cec3;border-radius:12px;overflow:hidden;background:#e8e6df}canvas{display:block;width:100%;height:auto}#caption{padding:13px 20px;border-top:1px solid #c9cec3;color:#526357;font-size:14px}.controls{display:flex;gap:10px;align-items:center;flex-wrap:wrap;padding:14px 0}input[type=range]{flex:1;min-width:150px;accent-color:#315941}output{min-width:130px;font-variant-numeric:tabular-nums;color:#516458}.note{border-left:3px solid #738967;padding:5px 17px;margin-top:20px;color:#566254}.details{display:grid;grid-template-columns:1fr 1fr;gap:18px;margin-top:24px}.details>div{background:#f3f2eb;border-radius:8px;padding:18px}.details b{display:block}a{color:#315941}.footer{font-size:13px;color:#69736b;margin-top:22px}@media(max-width:700px){main{padding:20px 14px}h1{font-size:27px}.details{grid-template-columns:1fr}.top{align-items:start}.badge{white-space:nowrap}}
</style>
<main><div class="eyebrow">怪兽仙境 REMAKE / CHARACTER REVIEW 02</div><div class="top"><h1 id="title">西雅 · 战斗动作</h1><span class="badge">候选 v2 · 待你评价</span></div>
<div class="tabs" id="roles"><button data-role="0" class="active">01 西雅</button><button data-role="1">02 七贝</button><button data-role="2">两人跟随</button></div>
<div class="tabs" id="forms"><button data-form="0" class="active">战斗立绘</button><button data-form="1">Q 版跑图</button><select id="chapter" aria-label="西雅战斗阶段"><option value="0">完整动作</option><option value="0.25">待机</option><option value="1.2">斩击</option><option value="2.05">受击</option></select></div>
<div class="stage"><canvas width="1280" height="720" id="stage" aria-label="Unity 实际动作预览"></canvas><div id="caption"></div></div>
<div class="controls"><button id="play">暂停</button><button id="previous" aria-label="上一帧">← 上一帧</button><button id="next">下一帧 →</button><input id="timeline" type="range" min="0" max="63" value="0" aria-label="动作帧"><output id="time">0.00s / 4.00s</output><select id="speed" aria-label="播放速度"><option value="1">正常速度</option><option value="0.5">半速</option></select></div>
<div class="note" id="note"></div><div class="details"><div><b>本轮怎么看</b>先看比例、配色与轮廓，再暂停检查挥剑时的持握、行走的左右脚和翅膀的上/下拍。左右方向键逐帧，空格暂停。</div><div><b>素材与证据</b><a id="contact" href="../../NPCs/xiya/v2/battle-unity-contact.png">动作关键帧</a> · <a id="original" href="../../NPCs/xiya/v1/original-vs-redraw.png">原版 / v1 静态对照</a><br><a href="runtime-capture-manifest.json">Unity 录制帧与校验记录</a> · <a href="runtime-validation.txt">运行检查回执</a></div></div>
<p class="footer">这些画面来自独立 Unity 审核场景。16 次采样/秒是录制频率；西雅每个跑图方向 6 张绘制关键帧，七贝每方向 4 张翼形。美术认可单独记录。</p></main>
<script>
const titles=['西雅 · 战斗动作','西雅 · Q 版跑图','七贝 · 战斗立绘','七贝 · Q 版跑图','西雅与七贝 · 路径跟随'];
const captions=['原版头像与跑图在左；战斗补画在右。待机 → 蓄力斩击 → 受击 → 恢复。','原版资料在左；新版从左到右：下、左、右、上。前后 0.5 秒待机，中间 3 秒行走。','原版资料在左；新版战斗形态：粉白脸、蓝眼、粉颊，悬浮和双翼扇动。','原版资料在左；新版从左到右：下、左、右、上。右向镜像左向。','七贝跟随西雅走过的路线，转弯时继续沿原路移动。'];
const notes=['这版握剑已被指出穿插与手型不自然，待重画；当前画面保留作问题对照。完整战斗身体与剑型仍属于参考补画。','跑图使用独立 Q 版身体；左右方向分别绘制，保留长紫发、绿帽、绿衣、粉腰带。此处优先看四向轮廓与步态。','七贝按原版头像保留粉白脸、蓝眼和粉颊。这里先审悬浮与扇翅；尚未扩展攻击或法术动作。','跑图保留原版黑色眯眼和黄绿脸颊。各方向都具有一对双瓣翅膀；侧面远翼会被身体遮挡。','七贝是西雅的固定随行灵兽。此场景用于检查跟随、朝向和转弯；尚未接入正式土之秘境剧情。'];
let role=0,form=0,mode=0,frame=0,playing=true,last=0,images=[],revision=0;
const $=id=>document.getElementById(id),ctx=$('stage').getContext('2d');
function draw(){const im=images[frame];ctx.fillStyle='#e8e6df';ctx.fillRect(0,0,1280,720);if(im&&im.complete&&im.naturalWidth){if(mode===1||mode===3)ctx.drawImage(im,100,160,1120,410,0,80,1280,469);else ctx.drawImage(im,0,0)}else{ctx.fillStyle='#526357';ctx.font='22px sans-serif';ctx.fillText('载入 Unity 录制帧…',40,80)}if(mode===1||mode===3){ctx.fillStyle='#526357';ctx.font='20px sans-serif';['下','左','右','上'].forEach((s,i)=>ctx.fillText(s,(466+i*207.7-100)*1280/1120-8,588))} $('timeline').value=frame;$('time').textContent=(frame/16).toFixed(2)+'s / 4.00s';$('play').textContent=playing?'暂停':'继续播放'}
async function loadFrames(own,selectedMode,pictures){let cursor=0;async function worker(){while(own===revision&&cursor<64){const index=cursor++,im=pictures[index];await new Promise(resolve=>{im.onload=()=>{if(own===revision&&index===frame)draw();resolve()};im.onerror=resolve;im.src='web/mode-'+selectedMode+'/frame-'+String(index).padStart(3,'0')+'.webp'})}}await Promise.all(Array.from({length:4},worker))}function select(){images.forEach(im=>{im.onload=null;im.onerror=null;im.removeAttribute('src')});mode=role===2?4:role*2+form;frame=0;last=performance.now();const own=++revision;images=Array.from({length:64},()=>new Image);loadFrames(own,mode,images);document.querySelector('.badge').textContent=mode===0?'v2 握剑待重画':'候选 v2 · 待你评价';$('title').textContent=titles[mode];$('caption').textContent=captions[mode];$('note').textContent=notes[mode];$('forms').style.display=role===2?'none':'flex';$('chapter').style.display=mode===0?'inline-block':'none';$('chapter').value='0';document.querySelectorAll('[data-role]').forEach(b=>b.classList.toggle('active',+b.dataset.role===role));document.querySelectorAll('[data-form]').forEach(b=>b.classList.toggle('active',+b.dataset.form===form));const base=role===0?'../../NPCs/xiya':role===1?'../../Companions/qibei':'.';$('contact').href=mode===4?'follow-unity-contact.png':base+'/v2/'+(form===0?'battle':'field')+'-unity-contact.png';$('original').href=role===1?'../../Companions/qibei/v1/original-vs-redraw.png':'../../NPCs/xiya/v1/original-vs-redraw.png';draw()}
function step(n){playing=false;frame=(frame+n+64)%64;draw()}document.querySelectorAll('[data-role]').forEach(b=>b.onclick=()=>{role=+b.dataset.role;select()});document.querySelectorAll('[data-form]').forEach(b=>b.onclick=()=>{form=+b.dataset.form;select()});$('play').onclick=()=>{playing=!playing;last=performance.now();draw()};$('next').onclick=()=>step(1);$('previous').onclick=()=>step(-1);$('timeline').oninput=()=>{playing=false;frame=+$('timeline').value;draw()};$('chapter').onchange=()=>{playing=false;frame=Math.round(+$('chapter').value*16);draw()};document.addEventListener('keydown',e=>{if(['Space','ArrowRight','ArrowLeft'].includes(e.code)){e.preventDefault();if(e.code==='Space')$('play').click();else step(e.code==='ArrowRight'?1:-1)}});function tick(t){if(playing&&t-last>=62.5/+$('speed').value){const count=Math.floor((t-last)/(62.5/+$('speed').value));frame=(frame+count)%64;last=t;draw()}requestAnimationFrame(tick)}select();requestAnimationFrame(tick);
</script></html>'''

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('captures', type=Path)
    main(parser.parse_args().captures.resolve())
