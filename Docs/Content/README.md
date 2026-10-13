# 怪兽仙境 · 原版完整内容清单

完整指当前本地参考包逐项登记，并包含已知缺失引用；不声称已经取得完整原作后续联网资料。

|类别|条目|当前实现情况|
|---|---|---|
|剧情脚本 / 指令|163 / 6714|5个脚本部分适配，其余恢复/离线改编；1483条对话指令全部登记|
|地图 / 场景|90 / 90|27张地形与通路接入，63张待接入；全部原事件待映射|
|宝箱演员|50|当前章节28个，22个有奖励资料，6个待恢复奖励|
|怪兽 / 主角 / 占位|50 / 1 / 1|51个正式身份进入图鉴/通用规则；50怪兽正式美术与专属玩法未完成|
|技能 / 占位|95 / 1|按原技能ID实现0；通用技能攻击不计原技能|
|基础效果|37|37条待核对并实现|
|本地物品 / 远程标记|154 / 54|154名称导入；部分奖励/道具适配，完整属性待恢复|
|已知缺失资源目标|32|脚本/场景初始化/遭遇资料分别登记；调查后恢复或离线改编|
|扩展候选|8|立项范围内分批设计，未实现|

- [剧情与事件](STORY.md)
- [地图、场景与宝箱](MAPS.md)
- [怪兽与主角](MONSTERS.md)
- [固定同伴与对话角色](PEOPLE.md)
- [技能与基础效果](SKILLS.md)
- [物品](ITEMS.md)
- [缺失资料](GAPS.md)
- [扩展候选](EXPANSIONS.md)
- [下一批开发与事件链](DEVELOPMENT.md)
- [原版故事梳理](../Story/ORIGINAL_STORY.md)
- [角色关系](../Story/CHARACTERS.md)
- [重制扩写大纲](../Story/REMAKE_OUTLINE.md)
- [机器可读完整清单](../../Production/Content/content-inventory.json)
- [静态剧情依赖图数据](../../Production/Content/story-dependencies.json)

## 更新与查询

```sh
python3 Tools/content_inventory.py
python3 Tools/content_inventory.py --check
python3 Tools/content_inventory.py --category maps --status restore_required
python3 Tools/content_inventory.py --category skills --search 地震
```

生成器解析真实原资料和当前运行数据，所有条目保留稳定ID、来源、实现/恢复/扩展与任务字段。源哈希与Git提交保存在JSON；存在未提交代码时只证明本次静态快照，不作为该提交的运行通关证明。
