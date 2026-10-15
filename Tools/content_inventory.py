"""Build/query a source-complete inventory without granting gameplay or art approval.

Run from any directory: python3 /path/to/Tools/content_inventory.py [--check]
Only Docs/Content and Production/Content are written. Original evidence is read-only.
"""
from pathlib import Path
import argparse
import collections
import hashlib
import json
import re
import struct
import subprocess
from story_analysis import analyze

ROOT = Path(__file__).resolve().parents[1]
ORIGINAL = ROOT / 'Reference/OriginalGame'
DATA = ROOT / 'Unity/Assets/Remake/Resources/Data'
OUT = ROOT / 'Production/Content'
DOCS = ROOT / 'Docs/Content'
STATUSES = {
    'partial_adaptation': '部分适配，原版行为待恢复',
    'implemented_subset': '已接入当前章节子集',
    'imported_only': '仅导入资料，待实现',
    'restore_required': '待恢复与接入',
    'placeholder': '原表占位，不计入正式内容',
    'missing_source': '引用存在，源文件缺失',
    'expansion_proposed': '扩展计划，尚未实现',
    'offline_redesign': '平台功能需离线改编',
}


def read(path):
    return json.loads(path.read_text())


def relative(path):
    return str(path.relative_to(ROOT))


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def clean(text):
    return '' if text in ('=\x00', '0') else text.replace('\x00', '')


def cell(value):
    return str(value).replace('|', '\\|').replace('\n', ' ').replace('\r', '')


def table(headers, rows):
    return '\n'.join(['|' + '|'.join(headers) + '|', '|' + '|'.join(['---'] * len(headers)) + '|'] +
                     ['|' + '|'.join(cell(x) for x in row) + '|' for row in rows])


def reference(path, pointer):
    return {'path': relative(path), 'pointer': pointer}


def encounter_rows(path):
    if not path.exists():
        return []
    data = path.read_bytes()
    count = struct.unpack_from('>h', data)[0]
    end = 2 + count * 42
    assert count >= 0 and len(data) >= end and data[end:] in (b'', b'\x00'), f'MMP length/footer {path}'
    return [list(struct.unpack_from('>21h', data, 2 + i * 42)) for i in range(count)]


def build():
    scripts = read(ORIGINAL / 'scripts.json')
    original_maps = read(ORIGINAL / 'maps.json')
    scenes = read(ROOT / 'Production/original-scenes.json')
    original_monsters = read(ORIGINAL / 'monsters.json')
    skills = read(ORIGINAL / 'skills.json')
    effects = read(ORIGINAL / 'basic-skills.json')
    items = read(ORIGINAL / 'items.json')
    calls = read(ORIGINAL / 'script-calls.json')
    world = {m['id']: m for m in read(DATA / 'earth-world.json')['maps']}
    catalog = {m['id']: m for m in read(DATA / 'catalog.json')['entries']}
    review_book = read(ROOT / 'Art/character-review.json')
    reviews = {m['id']: m for m in review_book['characters']}
    story_import = {s['id']: s for s in read(DATA / 'story.json')['sequences']}
    runtime_items = {i['id'] for i in read(DATA / 'items.json')['entries']}
    cs = ROOT / 'Unity/Assets/Remake/Scripts'
    game = (cs / 'EarthGame2D.cs').read_text() + (cs / 'ChapterAtbCombat.cs').read_text()
    rules = (cs / 'EarthData.cs').read_text()
    # These bindings are inspected implementations, not fuzzy dialogue matches.
    assert 'new Unit(8,10,catalog)' in game and 'state.Add(171, 1)' in game
    quest_code = (cs / 'ChapterQuest2D.cs').read_text()
    assert 'state.bossDefeated||state.reported)SubmitEarthQuest()' in game and 'npc.sourceScript==3040' in game
    assert 'save.reported = true' in quest_code and 'Data/earth-quest' in quest_code
    assert 'new AtbBattle(' in game and 'combat.Submit(new CombatCommand' in game
    assert 'class ChapterCombat' not in rules
    assert 'class OriginalGrowth' in rules and 'class OriginalElements' in rules
    adaptations = {
        'minit.bin': ('地图名/演员与守护者配置的子集；没有执行完整初始化分支', 'Tools/prepare_world.py', 'A06'),
        '2001.bin': ('仅土之秘境宝箱奖励子集；跨章奖励和条件分支未执行', 'Tools/prepare_world.py', 'A12'),
        '3032.bin': ('考试委托与入口交付为适配；首章阶段与交付条件已数据化；同盟场景、原奖励与后继事件未接入', relative(cs / 'ChapterQuest2D.cs'), 'A06'),
        '1044.bin': ('8号守护者、碎片171与少量对白；后半段西雅事件未接入，等级和战斗为适配', relative(cs / 'EarthGame2D.cs'), 'A06'),
        '3040.bin': ('NPC只显示简化提示；原速度考验的先手战斗、目标配置与条件未恢复', relative(cs / 'EarthGame2D.cs'), 'A06'),
    }
    names = collections.defaultdict(list)
    active = None
    for ins in scripts['minit.bin']:
        if ins['opcode'] == 59 and ins['depth'] == 0:
            active = ins['args'][2]
        if ins['opcode'] == 51 and active is not None:
            names[active].append({'name': clean(ins['text'][0]), 'offset': ins['offset'],
                                  'depth': ins['depth'], 'conditional': ins['depth'] > 1})

    scene_by_id = {s['id']: s for s in scenes}
    scene_refs = collections.defaultdict(list)
    map_edges = []
    chests = []
    item_candidates = collections.defaultdict(list)
    reward_map = reward_actor = None
    for ins in scripts['2001.bin']:
        if ins['opcode'] == 59 and ins['depth'] == 0:
            reward_map = ins['args'][2] if ins['args'][0:2] == [-1, 2] else None
            reward_actor = None
        if ins['opcode'] == 59 and ins['depth'] == 1 and ins['args'][0:2] == [-2, 2]:
            reward_actor = ins['args'][2]
        if ins['opcode'] == 14 and reward_map is not None and reward_actor is not None:
            item_candidates[(reward_map, reward_actor)].append({
                'instruction': f'script:2001.bin@{ins["offset"]}', 'rawArgs': ins['args'],
                'itemId': ins['args'][0], 'quantityParameter': ins['args'][1],
                'meaning': 'item construction within map/actor branch; later give/conditions must be reviewed'})
    all_encounters = {}
    for scene in scenes:
        mid = scene['id']
        for a in scene['actors']:
            params = a['params']
            if len(params) > 1 and params[1] >= 0:
                target = f'{params[1]}.bin'
                scene_refs[target].append({'map': mid, 'actor': a['index'], 'type': a['type'],
                                          'params': params, 'binding': 'actor params[1]; static reference, not chronological order'})
            if a['type'] == 'door_0' and len(params) >= 4:
                map_edges.append({'source': mid, 'actor': a['index'], 'target': params[3],
                                  'door': params[2], 'targetPresent': params[3] in scene_by_id})
            if a['type'] in ('box', 'bigbox'):
                key = f'{mid}:{a["index"]}'
                current = next((c for c in world.get(mid, {}).get('chests', []) if c['key'] == key), None)
                chests.append({'id': 'chest:' + key, 'map': mid, 'actor': a['index'], 'kind': a['type'],
                               'originalActor': a, 'runtime': current,
                               'originalItemCandidates': item_candidates[(mid, a['index'])],
                               'status': 'partial_adaptation' if current else 'restore_required',
                               'restore': '确认原奖励条件、交互范围和开箱表现；保留普通箱/大箱设计',
                               'task': 'A12' if current else 'A02'})
        all_encounters[mid] = encounter_rows(ORIGINAL / f'extracted/mmp/{mid}.bin')

    story = []
    instructions = []
    speaker_refs = collections.defaultdict(list)
    for name, rows in sorted(scripts.items(), key=lambda x: (not x[0][:-4].isdigit(), int(x[0][:-4]) if x[0][:-4].isdigit() else 0)):
        dialogue = [r for r in rows if r['opcode'] == 1]
        texts = [clean(t) for r in rows for t in r['text'] if clean(t)]
        a = adaptations.get(name)
        # Platform strings are tagged for review, never removed from the source inventory.
        platform = any(any(w in t for w in ['短信', 'wap.X6', 'wap.x6', '登陆X6', '彩票']) for t in texts)
        status = 'partial_adaptation' if a else 'offline_redesign' if platform else 'restore_required'
        row = {'id': 'script:' + name, 'file': name,
               'source': reference(ORIGINAL / 'scripts.json', name),
               'provenance': 'original_partial', 'status': status,
               'instructionCount': len(rows), 'dialogueCount': len(dialogue),
               'speakers': sorted({clean(r['text'][0]) for r in dialogue if clean(r['text'][0])}),
               'firstDialogue': dialogue[0]['text'] if dialogue else [],
               'lastDialogue': dialogue[-1]['text'] if dialogue else [],
               'sceneReferences': scene_refs[name],
               'calls': [r for r in calls if r['source'] == name],
               'importedDialogueLines': len(story_import.get(name, {}).get('lines', [])),
               'importedDataExecuted': False,
               'implementation': a[0] if a else '当前运行代码未执行该原脚本；存在场景引用不代表事件实现',
               'implementationEvidence': a[1] if a else None,
               'restore': '复核条件/变量/奖励/演出与后继分支，建立数据化事件；保留原台词来源',
               'extension': '平台功能离线改编，逐项决定保留玩法或替换' if platform else '人物动机/同伴对话/区域支线按章节另行设计',
               'task': a[2] if a else 'A02'}
        story.append(row)
        for ins in rows:
            uid = f'script:{name}@{ins["offset"]}'
            instructions.append({'id': uid, 'script': name, **ins,
                                 'status': 'restore_required',
                                 'note': '原指令未逐条映射；脚本的部分适配状态不可授予每条指令已实现'})
            if ins['opcode'] == 1:
                speaker = clean(ins['text'][0])
                if speaker and not re.fullmatch(r'-?\d+', speaker):
                    speaker_refs[speaker].append(uid)

    maps = []
    for m in original_maps:
        mid = m['id']; runtime = world.get(mid)
        variant_names = names[mid]
        label = ' / '.join(dict.fromkeys(x['name'] for x in variant_names)) or f'地图{mid}（名称待核实）'
        changed = []
        if runtime:
            assert runtime['layer0'] == m['layers'][0] and runtime['layer1'] == m['layers'][1]
            changed = [i for i, (a, b) in enumerate(zip(m['attributes'], runtime['attributes'])) if a != b]
        maps.append({'id': f'map:{mid}', 'originalId': mid, 'name': label,
                     'nameEvidence': variant_names, 'runtimeName': runtime['name'] if runtime else None,
                     'source': reference(ORIGINAL / 'maps.json', f'id={mid}'),
                     'sceneSource': reference(ROOT / 'Production/original-scenes.json', f'id={mid}'),
                     'provenance': 'remake_adaptation' if changed else 'original_partial',
                     'status': 'implemented_subset' if runtime else 'restore_required',
                     'dimensions': [m['width_tiles'], m['height_tiles']], 'tilesetIndex': m['tileset_index'],
                     'actorCount': len(scene_by_id[mid]['actors']),
                     'sceneScriptReferences': sorted(k for k, v in scene_refs.items() if any(r['map'] == mid for r in v)),
                     'portals': [e for e in map_edges if e['source'] == mid],
                     'encounterDefinitions': all_encounters[mid],
                     'encounterSourcePresent': (ORIGINAL / f'extracted/mmp/{mid}.bin').exists(),
                     'encounterSource': f'Reference/OriginalGame/extracted/mmp/{mid}.bin',
                     'chestIds': [c['id'] for c in chests if c['map'] == mid],
                     'runtimeCoverage': {'terrain': bool(runtime), 'heightAndDoors': bool(runtime),
                                         'fullOriginalEvents': False, 'changedCollisionCells': len(changed)},
                     'restore': '复核事件/机关/奖励与能力门槛' if runtime else '导出地形、图块资源、演员、门口与遭遇，接入剧情并验证返程',
                     'extension': '沿用建筑/迷宫/宝箱风格；新增支路与区域事件作为独立扩展',
                     'task': 'A12' if runtime else 'A02'})

    monsters = []
    encounter_maps = collections.defaultdict(set)
    runtime_encounter_maps = collections.defaultdict(set)
    for mid, rs in all_encounters.items():
        for r in rs:
            for species in r[10:13]:
                if species >= 0: encounter_maps[species].add(mid)
    for mid, m in world.items():
        for e in m['encounters']:
            for species in e['species']:
                if species >= 0: runtime_encounter_maps[species].add(mid)
    for m in original_monsters:
        mid = m['id']; c = catalog.get(mid); review = reviews.get(mid)
        role = c['role'] if c else 'placeholder'
        status = 'placeholder' if role == 'placeholder' else 'implemented_subset'
        monsters.append({'id': f'character:{mid}', 'originalId': mid, 'name': m['name'], 'role': role,
                         'source': reference(ORIGINAL / 'monsters.json', f'id={mid}'),
                         'provenance': 'original_partial', 'status': status,
                         'rawFields': m['numeric_fields_n_to_B'],
                         'element': c['element'] if c else None, 'tier': c['tier'] if c else None,
                         'originalEncounterMaps': sorted(encounter_maps[mid]),
                         'runtimeEncounterMaps': sorted(runtime_encounter_maps[mid]),
                         'guardianInEarth': mid == 8,
                         'runtimeCoverage': {'catalogAndStats': bool(c), 'genericCombat': bool(c),
                                             'originalSpeciesSkills': False,
                                             'originalSynthesisAndPersonality': False},
                         'artStatus': review['runtimeArtStatus'] if review else 'not_applicable',
                         'approvedDesignVersion': review['approvedVersion'] if review else None,
                         'reviewFile': review['reviewFile'] if review else None,
                         'restore': '逐只审核外形/动作，恢复专属技能、合成、性格与栖息地' if role == 'monster' else '完整主角成长、武器与技能；动作视觉验收' if role == 'player' else '保留原占位ID，不作为怪兽',
                         'extension': '按生态与战术职责扩展，新增怪兽使用独立ID',
                         'task': 'A12' if runtime_encounter_maps[mid] or mid in (8, 51) else 'A09'})

    skill_rows = []
    for s in skills:
        sid = s['index']; placeholder = sid == 0 and s['name'] == '0'
        anchors = []
        if sid in (9, 10, 11): anchors = ['重制行动条 remake:heal：独立目标急救；不等价于原治疗等级']
        skill_rows.append({'id': f'skill:{sid}', 'originalId': sid, 'name': s['name'],
                           'description': s['description'], 'rawFields': s['fields'],
                           'source': reference(ORIGINAL / 'skills.json', f'index={sid}'),
                           'provenance': 'original_partial', 'status': 'placeholder' if placeholder else 'restore_required',
                           'runtimeImplementedByOriginalId': False, 'analogousPrototypeBehavior': anchors,
                           'restore': '核对原字段的消耗/目标/效果/持续/学习条件；重制行动条技能不计为原技能恢复',
                           'extension': '重制行动条技能使用独立remake命名空间；继续核对原技能语义',
                           'task': 'A09' if 22 <= sid <= 38 or sid in (6, 9) else 'A01技能语义核对→A09'})
    effect_rows = [{'id': f'effect:{s["index"]}', 'originalId': s['index'], 'name': s['text'][0],
                    'text': s['text'], 'rawFields': s['fields'], 'provenance': 'original_partial',
                    'source': reference(ORIGINAL / 'basic-skills.json', f'index={s["index"]}'),
                    'status': 'restore_required', 'restore': '核对效果语义、持续、叠加、抵抗、触发和解除时序',
                    'task': 'A09'} for s in effects]
    used_items = {121, 122, 123, 124, 125, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 171}
    used_items.update(c['runtime']['item'] for c in chests if c['runtime'] and c['runtime']['item'] > 0)
    item_rows = []
    for i in items:
        iid = i['index']; present = 'name' in i
        item_rows.append({'id': f'item:{iid}', 'originalId': iid, 'name': i.get('name', f'远程物品标记{iid}'),
                          'originalRecord': i, 'source': reference(ORIGINAL / 'items.json', f'index={iid}'),
                          'provenance': 'original_partial',
                          'status': 'missing_source' if not present else 'partial_adaptation' if iid in used_items else 'imported_only',
                          'runtimeNameImported': iid in runtime_items,
                          'restore': '恢复完整属性/装备/使用条件；原奖励与适配经济分开' if present else '寻找定义；离线改编方案另行建档',
                          'task': 'A10' if iid <= 10 else 'A12' if iid in used_items else 'A01物品语义核对'})

    missing = []
    for target in sorted(set(c['target'] for c in calls if not c['present'])):
        missing.append({'id': f'missing:script:{target}.bin', 'kind': 'static_script_call',
                        'target': f'{target}.bin', 'status': 'missing_source',
                        'references': [c for c in calls if c['target'] == target], 'task': 'A02'})
    known_missing = {m['target'] for m in missing}
    for target, refs in sorted(scene_refs.items()):
        if target not in scripts and target not in known_missing:
            missing.append({'id': 'missing:scene-script:' + target, 'kind': 'scene_actor_script',
                            'target': target, 'status': 'missing_source', 'references': refs, 'task': 'A02'})
    for mid in range(86, 90):
        p = ORIGINAL / f'extracted/script/m{mid}.bin'
        if not p.exists():
            missing.append({'id': f'missing:init:m{mid}', 'kind': 'separate_scene_initialization',
                            'target': f'm{mid}.bin', 'status': 'missing_source',
                            'references': ['Reference/OriginalGame/解析说明.txt: 场景ID>=86单独初始化'], 'task': 'A02'})
        if not (ORIGINAL / f'extracted/mmp/{mid}.bin').exists():
            missing.append({'id': f'missing:mmp:{mid}', 'kind': 'encounter_definition',
                            'target': f'mmp/{mid}.bin', 'status': 'missing_source',
                            'references': [f'map:{mid}'], 'task': 'A02'})

    expansions = [
        ('story_companion', '八宝随行对话与人物关系支线', 'A02', ['script:1010.bin', 'script:1044.bin']),
        ('story_bridge', '缺失联网段落的离线剧情承接与完整终局', 'A02', [m['id'] for m in missing]),
        ('world_branches', '同风格迷宫支路、机关与探索奖励', 'A03/A12', ['map:45', 'map:77']),
        ('species_ecology', '怪兽生态差异与新增种类', 'A09', ['character:21']),
        ('battle_synergy', '主角与怪兽协同指令', 'A07/A09', ['skill:6']),
        ('boss_phases', '守护者可读提示与阶段机制', 'A07/A09', ['character:8']),
        ('weapon_tactics', '各枪型战术与独立外观', 'A10', [f'item:{i}' for i in range(11)]),
        ('synthesis_growth', '配方发现与技能继承的培养路线', 'A11', ['script:1006.bin', 'script:3055.bin', 'script:3056.bin']),
    ]
    expansion_rows = [{'id': 'expansion:' + k, 'name': n, 'status': 'expansion_proposed',
                       'provenance': 'remake_expansion', 'task': task, 'related': refs,
                       'source': reference(ROOT / 'Docs/PROJECT.md', '内容与系统范围'),
                       'acceptance': '先形成具体设计与首章验证；角色造型仍逐个审核'} for k, n, task, refs in expansions]
    inputs = [ORIGINAL / (n + '.json') for n in ['scripts', 'maps', 'monsters', 'skills', 'basic-skills', 'items', 'script-calls']]
    inputs += [ROOT / 'Production/original-scenes.json', DATA / 'earth-world.json', DATA / 'catalog.json',
               DATA / 'story.json', DATA / 'items.json', ROOT / 'Art/character-review.json',
               cs / 'EarthGame2D.cs', cs / 'EarthData.cs', ROOT / 'Tools/prepare_world.py', ROOT / 'Docs/PROJECT.md',
               cs / 'QuestRules2D.cs', cs / 'ChapterQuest2D.cs', DATA / 'earth-quest.json',
               ROOT / 'Tools/content_inventory.py', ROOT / 'Tools/story_analysis.py',
               ORIGINAL / 'decompiled/t.java', ORIGINAL / 'decompiled/d.java', ORIGINAL / 'decompiled/h.java']
    inputs += sorted((ORIGINAL / 'extracted/mmp').glob('*.bin'))
    inv = {'schemaVersion': 1, 'scope': '当前本地参考包的完整登记；不声称拥有缺失联网内容或完整原版语义',
           'gitHead': subprocess.check_output(['git', '-C', str(ROOT), 'rev-parse', 'HEAD'], text=True).strip(),
           'gitHeadMeaning': 'HEAD at inventory generation; sourceSHA256 identifies actual files including uncommitted changes',
           'sourceSHA256': {relative(p): digest(p) for p in inputs}, 'statusLabels': STATUSES,
           'scripts': story, 'instructions': instructions, 'maps': maps, 'chests': chests,
           'characters': monsters,
           'companions': [{'id': 'companion:' + c['id'], 'name': c['name'],
                'provenance': 'original_partial', 'status': 'implemented_subset' if c.get('runtimeIntegrated') else 'restore_required',
                'reviewRecord': c, 'source': reference(ROOT / 'Art/character-review.json', 'companions/' + c['id']),
                'restore': '跟随、剧情和能力分别验收；动作的技术通过不代替视觉认可', 'task': 'A04/A05'} for c in review_book.get('companions', [])],
           'speakers': [{'name': n, 'dialogueReferences': v,
                'identityStatus': 'speaker_label_only; title/shop/UI labels may not be distinct characters'} for n, v in sorted(speaker_refs.items())],
           'skills': skill_rows, 'effects': effect_rows, 'items': item_rows,
           'gaps': missing, 'expansions': expansion_rows}
    validate(inv)
    return inv


def validate(inv):
    assert len(inv['scripts']) == 163 and len(inv['instructions']) == 6714
    assert sum(x['dialogueCount'] for x in inv['scripts']) == 1483
    assert {m['originalId'] for m in inv['maps']} == set(range(90))
    assert sum(m['role'] == 'monster' for m in inv['characters']) == 50
    assert len(inv['characters']) == 52 and len(inv['skills']) == 96 and len(inv['effects']) == 37
    assert sum('name' in i['originalRecord'] for i in inv['items']) == 154
    assert sum('name' not in i['originalRecord'] for i in inv['items']) == 54
    assert len(inv['chests']) == 50
    for group in ['scripts', 'instructions', 'maps', 'chests', 'characters', 'companions', 'skills', 'effects', 'items', 'gaps', 'expansions']:
        rows = inv[group]; assert len({x['id'] for x in rows}) == len(rows), group
        assert all(x['status'] in STATUSES for x in rows), group
    assert all(not x['runtimeImplementedByOriginalId'] for x in inv['skills'])
    assert all(not x['runtimeCoverage']['fullOriginalEvents'] for x in inv['maps'])
    assert sum(x['status'] == 'implemented_subset' for x in inv['maps']) == 27
    assert sum(x['status'] == 'placeholder' for x in inv['skills']) == 1
    assert sum(x['approvedDesignVersion'] is not None for x in inv['characters']) == 1
    known_ids = {x['id'] for k in ['scripts', 'maps', 'characters', 'skills', 'items', 'gaps'] for x in inv[k]}
    assert all(r in known_ids for e in inv['expansions'] for r in e['related'])


def render(inv):
    documents = {}
    prefix = '由 `Tools/content_inventory.py` 生成。手工开发状态应修改生成器中的显式实现映射，不能通过名称相似或文件存在授予已实现状态。完整原始参数与每条指令见 [JSON清单](../../Production/Content/content-inventory.json)。\n\n'
    documents['STORY.md'] = '# 原版剧情与事件清单\n\n' + prefix + '按原文件登记；顺序为文件排序，不代表剧情先后。`story.json` 的台词导入未被当前运行代码执行，不能视为剧情已实现。原指令6714条均保留独立ID与偏移，部分脚本适配不授予所有指令已实现状态。\n\n' + table(
        ['文件', '指令/对话', '人物/标签', '首句摘要', '状态', '实现与恢复差距', '任务'],
        [(s['file'], f'{s["instructionCount"]}/{s["dialogueCount"]}', '、'.join(s['speakers']),
          ' / '.join(s['firstDialogue'])[:110], STATUSES[s['status']], s['implementation'], s['task']) for s in inv['scripts']]) + '\n'
    documents['MAPS.md'] = '# 原版地图、场景与宝箱清单\n\n' + prefix + '保留所有90张地图与场景。地图78的两种名称来自条件分支，不自动合并为一处地点。已接入表示地形/层级/门口子集；事件、机关与全部奖励仍待恢复。包内共50个箱体演员，当前土之秘境使用其中28个。\n\n' + table(
        ['地图ID', '名称/条件名称', '尺寸', '演员/出口/箱', '状态', '碰撞改动格', '任务'],
        [(m['originalId'], m['name'], '×'.join(map(str, m['dimensions'])),
          f'{m["actorCount"]}/{len(m["portals"])}/{len(m["chestIds"])}', STATUSES[m['status']], m['runtimeCoverage']['changedCollisionCells'], m['task']) for m in inv['maps']]) + '\n\n## 宝箱逐项\n\n' + table(
        ['身份', '类型', '状态', '原奖励候选（条件待核实）', '当前奖励', '待处理'],
        [(c['id'], c['kind'], STATUSES[c['status']],
          '; '.join(f'物品{a["itemId"]}×参数{a["quantityParameter"]} @{a["instruction"]}' for a in c['originalItemCandidates']) or '未找到对应物品构造指令',
          f'物品{c["runtime"]["item"]}×{c["runtime"]["quantity"]}（{c["runtime"]["source"]}）' if c['runtime'] else '未接入；原奖励条件待核实', c['restore']) for c in inv['chests']]) + '\n'
    documents['MONSTERS.md'] = '# 原版怪兽与主角清单\n\n' + prefix + '52条原表记录分为50只怪兽、主角51与占位50。八宝不在可收服怪兽表内。图鉴/通用战斗/成长可用不表示专属技能、合成、性格或正式美术已完成。遭遇ID来自原mmp，仍需核对未接入地图的运行语义。\n\n' + table(
        ['ID', '名称', '角色', '元素/阶', '当前遭遇地图', '玩法状态', '美术状态/认可造型', '待恢复'],
        [(m['originalId'], m['name'], m['role'], f'{m["element"]}/{m["tier"]}', ','.join(map(str, m['runtimeEncounterMaps'])) or ('守护者75' if m['guardianInEarth'] else '无当前野外遭遇'), STATUSES[m['status']], f'{m["artStatus"]} / {m["approvedDesignVersion"] or "无"}', m['restore']) for m in inv['characters']]) + '\n'
    documents['PEOPLE.md'] = '# 固定同伴与原版对话角色线索\n\n' + prefix + '固定同伴独立于50只怪兽。下面的对话发言标签还包含职业名、提示和UI标签，不能把每个标签都算成一个独立NPC；精确角色身份与场景演员绑定在A02中继续核对。\n\n' + table(
        ['身份', '名称', '当前清单状态', '认可造型', '待恢复'],
        [(c['id'], c['name'], STATUSES[c['status']], c['reviewRecord'].get('approvedVersion'), c['restore']) for c in inv['companions']]) + '\n\n## 全部非数字发言标签\n\n' + table(
        ['标签', '对话指令数', '原脚本文件', '示例指令身份'],
        [(s['name'], len(s['dialogueReferences']), ', '.join(sorted({r.split('@')[0].removeprefix('script:') for r in s['dialogueReferences']})), s['dialogueReferences'][0]) for s in inv['speakers']]) + '\n'
    documents['SKILLS.md'] = '# 原版技能与基础效果清单\n\n' + prefix + '96条技能记录中ID0为占位，实际有名技能95条。当前已替换为独立行动条系统，技能使用remake命名空间；旧通用skill伤害与固定80治疗已删除。95条原技能仍没有按原技能ID执行的实现；原描述作为证据保留，不把重制技能与新数值标为原版恢复。\n\n' + table(
        ['ID', '技能', '原版描述', '状态', '任务'], [(s['originalId'], s['name'], s['description'], STATUSES[s['status']], s['task']) for s in inv['skills']]) + '\n\n## 37条基础效果\n\n' + table(
        ['ID', '原效果文本', '状态', '恢复工作'], [(s['originalId'], ' / '.join(s['text']), STATUSES[s['status']], s['restore']) for s in inv['effects']]) + '\n'
    documents['ITEMS.md'] = '# 物品补充清单\n\n' + prefix + '208条表记录包含154条本地定义与54条远程标记。名称导入不表示属性/装备/使用功能恢复。当前原武器型号仅有数据蓝图，默认双枪不代表任一原型号。\n\n' + table(
        ['ID', '名称', '状态', '名称已导入', '原描述', '任务'], [(i['originalId'], i['name'], STATUSES[i['status']], i['runtimeNameImported'], i['originalRecord'].get('description', '定义缺失'), i['task']) for i in inv['items']]) + '\n'
    documents['GAPS.md'] = '# 缺失原版资料与恢复缺口\n\n' + prefix + '静态脚本调用、场景演员脚本引用与高编号场景初始化分别统计。静态引用不证明分支必然执行；以下不是原版全部缺失资源清单。远程物品标记另见物品表。\n\n' + table(
        ['缺失目标', '引用类型', '引用数量', '处理'], [(g['target'], g['kind'], len(g['references']), '调查来源与条件；无法取得时另建离线改编，不伪称原剧情恢复') for g in inv['gaps']]) + '\n\n已知玩法缺口：地图45兼容碰撞；77:8奖励箱当前普通路线不可交互；土之秘境6个箱子奖励未恢复；速度/力量/智慧考验未恢复；完整序章、合成、性格、装备数值、原技能/状态、跨章与终局未接入。\n'
    documents['EXPANSIONS.md'] = '# 扩展内容候选清单\n\n' + prefix + '承接已确认立项范围，以下是制作候选，尚未实现或认可具体美术。先恢复可核实内容，扩展使用独立ID与来源标签。\n\n' + table(
        ['ID', '扩展', '关联原内容/缺口', '任务'], [(e['id'], e['name'], ', '.join(e['related']), e['task']) for e in inv['expansions']]) + '\n'
    counts = {k: len(inv[k]) for k in ['scripts', 'instructions', 'maps', 'chests', 'characters', 'companions', 'skills', 'effects', 'items', 'gaps', 'expansions']}
    documents['README.md'] = '# 怪兽仙境 · 原版完整内容清单\n\n完整指当前本地参考包逐项登记，并包含已知缺失引用；不声称已经取得完整原作后续联网资料。\n\n' + table(
        ['类别', '条目', '当前实现情况'], [('剧情脚本 / 指令', f'{counts["scripts"]} / {counts["instructions"]}', '5个脚本部分适配，其余恢复/离线改编；1483条对话指令全部登记'),
        ('地图 / 场景', '90 / 90', '27张地形与通路接入，63张待接入；全部原事件待映射'),
        ('宝箱演员', counts['chests'], '当前章节28个，22个有奖励资料，6个待恢复奖励'),
        ('怪兽 / 主角 / 占位', '50 / 1 / 1', '51个正式身份进入图鉴/通用规则；50怪兽正式美术与专属玩法未完成'),
        ('技能 / 占位', '95 / 1', '按原技能ID实现0；通用技能攻击不计原技能'),
        ('基础效果', counts['effects'], '37条待核对并实现'),
        ('本地物品 / 远程标记', '154 / 54', '154名称导入；部分奖励/道具适配，完整属性待恢复'),
        ('已知缺失资源目标', counts['gaps'], '脚本/场景初始化/遭遇资料分别登记；调查后恢复或离线改编'),
        ('扩展候选', counts['expansions'], '立项范围内分批设计，未实现')]) + '\n\n' + '\n'.join([
        '- [剧情与事件](STORY.md)', '- [地图、场景与宝箱](MAPS.md)', '- [怪兽与主角](MONSTERS.md)', '- [固定同伴与对话角色](PEOPLE.md)',
        '- [技能与基础效果](SKILLS.md)', '- [物品](ITEMS.md)', '- [缺失资料](GAPS.md)', '- [扩展候选](EXPANSIONS.md)',
        '- [下一批开发与事件链](DEVELOPMENT.md)', '- [原版故事梳理](../Story/ORIGINAL_STORY.md)', '- [角色关系](../Story/CHARACTERS.md)', '- [重制扩写大纲](../Story/REMAKE_OUTLINE.md)', '- [机器可读完整清单](../../Production/Content/content-inventory.json)',
        '- [静态剧情依赖图数据](../../Production/Content/story-dependencies.json)']) + '\n\n## 更新与查询\n\n```sh\npython3 Tools/content_inventory.py\npython3 Tools/content_inventory.py --check\npython3 Tools/content_inventory.py --category maps --status restore_required\npython3 Tools/content_inventory.py --category skills --search 地震\n```\n\n生成器解析真实原资料和当前运行数据，所有条目保留稳定ID、来源、实现/恢复/扩展与任务字段。源哈希与Git提交保存在JSON；存在未提交代码时只证明本次静态快照，不作为该提交的运行通关证明。\n'
    graph = {'scope': 'static dependencies; NOT a verified chronological plot or runtime unlock graph',
             'scripts': [s['file'] for s in inv['scripts']],
             'callEdges': [c for s in inv['scripts'] for c in s['calls']],
             'sceneBindings': [{'script': s['file'], **r} for s in inv['scripts'] for r in s['sceneReferences']],
             'missingTargets': inv['gaps'], 'mapDoorEdges': [p for m in inv['maps'] for p in m['portals']]}
    by_script = collections.defaultdict(list)
    for instruction in inv['instructions']:
        by_script[instruction['script']].append(instruction)
    events = analyze([{'file': s['file'], 'instructions': by_script[s['file']]} for s in inv['scripts']])
    report = {'result': 'pass', 'scope': 'source completeness, IDs, current static implementation bindings; not Unity runtime',
              'gitHead': inv['gitHead'], 'counts': counts,
              'sourceSHA256': inv['sourceSHA256']}
    outputs = {OUT / 'content-inventory.json': json.dumps(inv, ensure_ascii=False, indent=2) + '\n',
               OUT / 'story-dependencies.json': json.dumps(graph, ensure_ascii=False, indent=2) + '\n',
               OUT / 'original-event-candidates.json': json.dumps(events, ensure_ascii=False, indent=2) + '\n',
               OUT / 'inventory-validation.json': json.dumps(report, ensure_ascii=False, indent=2) + '\n'}
    outputs.update({DOCS / n: t for n, t in documents.items()})
    return outputs, report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true', help='Rebuild in memory; fail if committed output is stale')
    parser.add_argument('--category', choices=['scripts', 'instructions', 'maps', 'chests', 'characters', 'companions', 'skills', 'effects', 'items', 'gaps', 'expansions'])
    parser.add_argument('--status', choices=STATUSES)
    parser.add_argument('--search', default='')
    args = parser.parse_args()
    inv = build()
    if args.category:
        rows = [r for r in inv[args.category] if (not args.status or r['status'] == args.status) and
                args.search.casefold() in json.dumps(r, ensure_ascii=False).casefold()]
        print(json.dumps(rows, ensure_ascii=False, indent=2)); return
    outputs, report = render(inv)
    if args.check:
        stale = []
        for p, t in outputs.items():
            if not p.exists():
                stale.append(relative(p)); continue
            actual = p.read_text()
            if p.suffix == '.json':
                a, b = json.loads(actual), json.loads(t)
                # A commit alone does not change source content. Retain the original
                # generation HEAD in saved evidence while checking every source hash.
                a.pop('gitHead', None); b.pop('gitHead', None)
                equal = a == b
            else:
                equal = actual == t
            if not equal: stale.append(relative(p))
        if stale: raise SystemExit('STALE: ' + ', '.join(stale))
    else:
        for p, t in outputs.items():
            p.parent.mkdir(parents=True, exist_ok=True); p.write_text(t)
    print(json.dumps({'result': 'pass', 'checkOnly': args.check, 'counts': report['counts']}, ensure_ascii=False))


if __name__ == '__main__':
    main()
