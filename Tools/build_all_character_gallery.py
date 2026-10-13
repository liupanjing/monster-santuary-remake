"""Publish a local review gallery and validate every completed static asset."""
from pathlib import Path
import html
import json
import hashlib
import os
from PIL import Image
from prepare_all_character_reviews import ROOT, OUT, write_json


def main():
    jobs=json.loads((OUT/'jobs.json').read_text())
    review=json.loads((ROOT/'Art/character-review.json').read_text())
    cards=[]; views=0
    for job in jobs:
        folder=ROOT/job['folder']
        assert (folder/'generation-manifest.json').is_file(), job['key']+' incomplete'
        manifest=json.loads((folder/'generation-manifest.json').read_text())
        manifest['prompts']=[str(p.relative_to(folder)) for p in sorted((folder/'prompts').glob('*.txt'))]
        manifest['promptSHA256']={str(p.relative_to(folder)):hashlib.sha256(p.read_bytes()).hexdigest() for p in sorted((folder/'prompts').glob('*.txt'))}
        manifest['sourceVariantsSHA256']={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in sorted((folder/'source').glob('*.png'))}
        if job.get('promptInterpretationNote'): manifest['promptInterpretationNote']=job['promptInterpretationNote']
        write_json(folder/'generation-manifest.json',manifest)
        assert manifest['approvedVersion'] is None and not manifest['runtimeIntegrated']
        assert manifest['sourceSHA256']==hashlib.sha256((folder/'source/design.png').read_bytes()).hexdigest()
        for path,digest in job['sourceSHA256'].items():
            assert hashlib.sha256((ROOT/path).read_bytes()).hexdigest()==digest
        for view in job['views']:
            p=folder/f'static/{view}.png'; im=Image.open(p)
            assert im.mode=='RGBA' and im.getchannel('A').getextrema()[0]==0 and im.getchannel('A').getextrema()[1]>240
            assert im.width>40 and im.height>40
            views+=1
        rel=lambda p: os.path.relpath(p,OUT)
        comparison=folder/'original-vs-redraw.png'
        job['comparison']=str(comparison.relative_to(ROOT))
        job['status']='draft_review'; job['approvedVersion']=None
        sources=job.get('sourceReferences',job['originals'])
        if job['kind']=='monster_field':
            row=next(r for r in review['characters'] if r['id']==job['id'])
            row.update(staticFieldArt=str((folder/'static').relative_to(ROOT)),fieldComparison=job['comparison'])
            review_path=ROOT/row['reviewFile'];text=review_path.read_text()
            marker='## 独立跑图静态候选'
            if marker in text: text=text.split(marker)[0].rstrip()+'\n'
            text+='\n'+marker+'\n\n'
        elif job['kind']=='monster':
            row=next(r for r in review['characters'] if r['id']==job['id'])
            row.update(status='draft_review',candidateVersion='v2',candidateComparison=job['comparison'],
                       staticArt=str((folder/'static').relative_to(ROOT)))
            review_path=ROOT/row['reviewFile']
            text=review_path.read_text()
            text=text.replace('状态：待原版审阅。现有重绘 v1 未通过审核，仅为运行占位。','状态：v2 静态重绘待审核。旧 v1 未通过，仅为运行占位。')
            marker='## 全角色静态重绘 v2'
            if marker in text:
                text=text.split(marker)[0].rstrip()+'\n'
            text+='\n'+marker+'\n\n'
        else:
            key=job['key']; rows=review.setdefault('npcs',[])
            row=next((r for r in rows if r['id']==key),None)
            data=dict(id=key,name=job['name'],role='dialogue_portrait_variant' if job['kind']=='portrait' else 'original_npc_visual_family',
                      status='draft_review',candidateVersion='v1',approvedVersion=None,approvedBy=None,
                      authorization='用户要求：现在继续把所有角色都做一遍，就按这个方法',
                      reviewFile=str((folder.parent/'REVIEW.md').relative_to(ROOT)),candidateComparison=job['comparison'],
                      originalReferences=sources,originalReferenceSHA256={Path(p).name:job['sourceSHA256'][p] for p in sources},
                      runtimeIntegrated=False,newAnimationFrames=0,staticArt=str((folder/'static').relative_to(ROOT)),
                      identityPolicy='Resource identity retained; shared appearances do not imply same story identity')
            if row: row.update(data)
            else: rows.append(data)
            review_path=ROOT/data['reviewFile']; text=f'# {job["name"]} · 独立静态造型审核\n\n'
            if job['kind']=='npc':
                text+=f'原版造型资源：`{job["resource"]}`，原场景使用 {job["originalSceneUses"]} 次。资源名保留，不擅自将共享造型绑定为单个剧情角色。\n\n'
            else:
                text+=f'头像身份由原版对话 opcode 1 的头像参数 {job["portraitId"]} 核实；只重绘头像，不能由头像推断未见的全身战斗立绘。\n\n'
        text+='状态：静态候选，未认可；未制作新动作或替换运行素材。\n\n'
        if job['silhouette']:
            text+='原包只有公共占位剪影；本版配色、器官和细节均为新设计，不是原画复原。\n\n'
        text+='## 源图约束 / 候选解释\n\n'+job['observedConstraints']+'\n\n'
        text+='低分辨率无法明确的表面材质、器官细节、立体转折与服装结构为候选解释，需逐角色审阅。原有配色变体分别保留。\n\n'
        prefix=folder.name+'/'
        text+=f'- [原版 / 新版对照]({prefix}original-vs-redraw.png)\n- [透明分视图]({prefix}static/)\n- [提示词]({prefix}prompts/design.txt)\n- [生成与源图记录]({prefix}generation-manifest.json)\n'
        review_path.write_text(text)
        title=html.escape(job['name']); key=html.escape(job['key']); kind='monster' if job['kind']=='monster_field' else job['kind']
        source_note='完整新设计 · 原包仅公共剪影' if job['silhouette'] else '原包依据重绘 · 静态候选'
        cards.append(f'''<article data-kind="{kind}" data-search="{key} {title}"><header><span>{key}</span><h2>{title}</h2><p>{source_note}</p></header><a href="{html.escape(rel(comparison))}" target="_blank"><img loading="lazy" src="{html.escape(rel(comparison))}" alt="{title} 原版与重绘对照"></a><footer><a href="{html.escape(rel(folder/'source/design.png'))}">透明原稿</a><a href="{html.escape(rel(review_path))}">审核档案</a><a href="{html.escape(rel(folder/'prompts/design.txt'))}">提示词</a></footer></article>''')
    existing=[('主角 · 已认可 v4','Art/Reviews/51/v4/hero-original-vs-modular-v4.png'),('八宝 · 已认可 v2','Art/Companions/babao/v2/babao-original-vs-redraw.png'),
              ('西雅 · v1 静态候选','Art/NPCs/xiya/v1/original-vs-redraw.png'),('七贝 · v1 静态候选','Art/Companions/qibei/v1/original-vs-redraw.png')]
    existing_html=''.join(f'<a href="{html.escape(os.path.relpath(ROOT/p,OUT))}">{html.escape(n)}</a>' for n,p in existing)
    page='''<!doctype html><html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>怪兽仙境 · 全角色静态审核</title><style>
*{box-sizing:border-box}body{margin:0;background:#e8e5dc;color:#25302b;font:16px system-ui,sans-serif}main{max-width:1700px;margin:auto;padding:32px}h1{font-size:38px;margin:8px 0}p{line-height:1.65}nav{display:flex;gap:12px;flex-wrap:wrap;margin:20px 0}nav a,button,input{border:1px solid #c9c7ba;background:#faf8f1;border-radius:8px;padding:10px 16px;color:#25302b}input{min-width:260px}button{cursor:pointer}button.active{background:#27584b;color:white}.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(min(100%,480px),1fr));gap:22px}article{border:1px solid #c8c4b6;border-radius:14px;overflow:hidden;background:#faf8f1;box-shadow:0 4px 18px #2430270a}header{padding:18px 20px 10px}header span{font:12px monospace;color:#727969}h2{margin:6px 0;font-size:25px}header p{margin:0;font-size:14px;color:#68736a}img{width:100%;display:block}footer{padding:12px 20px;display:flex;gap:16px;font-size:14px}a{color:#27584b}#count{margin:14px 0;color:#667467}.note{max-width:1000px;color:#667467}article[hidden]{display:none}
</style><main><span>MONSTER WONDERLAND / CHARACTER REVIEW</span><h1>全角色 · 原版与重绘</h1><p class="note">本轮 50 只怪兽、27 套剩余 NPC 造型或变体、4 张剧情头像，加上异翅巨蟒独立跑图版，共 82 项素材任务。头像与跑图变体可能属于同一剧情身份。每项保留源图、提示词、透明分视图与审核档案；造型确认后再制作动作与正式运行素材。头像和跑图造型保持独立比例。</p><nav>'''+existing_html+'''</nav><nav><input id="search" placeholder="搜索名字或原资源编号" aria-label="搜索角色"><button class="active" data-filter="all">全部</button><button data-filter="monster">怪兽</button><button data-filter="npc">NPC / 变体</button><button data-filter="portrait">独立头像</button></nav><div id="count"></div><section class="grid">'''+''.join(cards)+'''</section></main><script>
let filter='all';const cards=[...document.querySelectorAll('article')],search=document.querySelector('#search');function apply(){let n=0;const q=search.value.trim().toLowerCase();for(const c of cards){const visible=(filter==='all'||c.dataset.kind===filter)&&c.dataset.search.toLowerCase().includes(q);c.hidden=!visible;if(visible)n++}document.querySelector('#count').textContent=`显示 ${n} / ${cards.length} 项 · 点击对照图查看大图`}search.addEventListener('input',apply);for(const b of document.querySelectorAll('[data-filter]'))b.addEventListener('click',()=>{filter=b.dataset.filter;document.querySelectorAll('[data-filter]').forEach(x=>x.classList.toggle('active',x===b));apply()});apply();</script></html>'''
    (OUT/'index.html').write_text(page)
    review['currentReviewSubject']='all_characters_static';review['currentReviewSubjects']=[j['key'] for j in jobs]
    write_json(ROOT/'Art/character-review.json',review);write_json(OUT/'jobs.json',jobs)
    report=dict(result='pass',tasks=len(jobs),monsters=50,monsterFieldForms=1,npcVisualFamilies=27,portraits=4,transparentStaticViews=views,
                approvedNewDesigns=0,newAnimationFrames=0,runtimeReplacements=0,
                scope='Source hashes, alpha, coverage, independent output files; not human visual approval')
    write_json(OUT/'validation.json',report)
    (OUT/'README.md').write_text('# 全角色静态重绘审核\n\n用户本轮授权全部角色静态造型，延续独立素材与原版对照流程。\n\n'
              f'完成 {len(jobs)} 项任务，{views} 张透明分视图。50 只怪兽完整覆盖；NPC 按原包造型族和变体建档，多个剧情身份可能共用一个族；4 张独立头像不重复计为新的全身角色；另含异翅巨蟒独立跑图版。\n\n'
              '主角与八宝保留此前认可版本；西雅、七贝保留此前静态候选。本轮所有新稿为待审候选，四只特殊怪兽为新设计，未制作新动作、未替换运行资源。\n\n'
              '从 [本地画册](index.html) 搜索角色、筛选类别、打开独立对照图。生成工具：内置 image_gen；每项提示词与源图哈希见对应目录。\n')
    print(json.dumps(report,ensure_ascii=False))


if __name__=='__main__':
    main()
