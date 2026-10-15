# 怪兽仙境 Remake

在 Unity 中重写完整怪兽仙境的电脑端单机 RPG。用户已明确继续 **2D 俯视地图与角色精灵**；剧情、角色、怪兽、战斗和培养系统在原有基础上扩展，建筑风格、迷宫样式与宝箱设计继续沿用原版。

最终目标是从正式开场到结局的完整游戏。土之秘境作为首个完整验证章节，现有可玩原型继续保留。

[整体立项书](Docs/PROJECT.md) · [系统设计基线](Docs/SYSTEMS.md) · [制作路线与任务清单](Docs/ROADMAP.md)

[原版完整内容清单](Docs/Content/README.md)已逐项登记剧情、地图、怪兽、技能、效果、物品与缺失资料，明确当前实现、待恢复和扩展候选。使用 `python3 Tools/content_inventory.py --check` 检查清单是否与当前代码/资料一致。

剧情规划：[原版故事梳理](Docs/Story/ORIGINAL_STORY.md) · [主要角色与关系](Docs/Story/CHARACTERS.md) · [重制扩写大纲 v1](Docs/Story/REMAKE_OUTLINE.md)。原版证据、缺失段落与新增故事分别标注，后续开发先按剧情事件和人物出场顺序细化。

## 当前状态

已迁入可运行原型与原版资料。v1 重绘曾被指出偏离原画；目前主角 v4 双枪造型已获用户认可，v5 动作初版已替换主角运行资源。50 只怪兽的 v1 图片仍为待审占位。原 51 个角色清单中已有 1 个认可造型，固定同伴八宝另行统计；新动作的技术通过不代替用户视觉认可。

主角奥尼克（ID 51）的 [v5 实际动作预览与说明](Art/Reviews/51/v5/README.md)包含 Q 版四方向待机/行走/奔跑，以及独立双枪战斗动作，已接入土之秘境。新动作仍待用户看效果。

固定随行灵兽八宝的 v2 造型已认可，v3 动作与独立跟随已接入，并有技术运行记录；新动作视觉评价另行记录，详见 [八宝档案](Art/Companions/babao/REVIEW.md)。50 只可收服怪兽与八宝分开管理。

土之秘境考试已开始使用数据化任务阶段和交付条件，顶部目标随进度更新；实际程序通关与旧存档兼容检查见 [任务验证记录](Production/Content/quest-verification.json)。完整原事件与跨章任务仍按路线继续恢复。

土之秘境战斗现已改为等待型行动条制：速度推动独立行动条，就绪后选择指令和目标。规则、重制技能与当前验收范围见 [行动条战斗说明](Docs/ATB_COMBAT.md)，验证记录见 [Production/Combat](Production/Combat/validation.json)。

新增的原版整合包已补齐雷、风、火、水与返乡主线文本，原始 JAR、两份网友 Word 及静态核对结果保存在 [Reference/OriginalIntegrated20261014](Reference/OriginalIntegrated20261014/)。剧情梳理已按新证据更新；新章节尚未接入试玩，内容清单生成器当前仍以旧包为基线。

目前只有土之秘境可玩子集。完整剧情、任务系统、技能与状态、装备玩法、培养合成、其余章节和终局按整体立项逐步实现，不能视为已经完成。

## 目录

|目录|用途|
|---|---|
|`Unity/`|Unity 工程；用 Unity Hub 打开此目录|
|`Reference/OriginalGame/`|原版 JAR、提取资源、反编译和解析资料|
|`Reference/original-input-sha256.json`|原始资料校验值|
|`Art/Reviews/`|每个角色独立的原版图和审核记录|
|`Art/character-review.json`|51 个角色的审核状态与顺序|
|`Art/Archive/rejected-v1/`|未通过的旧重绘和提示词，供比较追溯|
|`Production/`|数据恢复记录、路线检查和历史运行验证|
|`Tools/`|使用仓库相对路径的数据恢复与检查脚本|
|`Docs/`|整体立项、系统设计、制作路线、审核流程与原型历史说明|

## 开始运行

1. 使用 Unity **6000.3.0f1** 打开 `Unity/`。
2. 打开 `Assets/Remake/Scenes/Earth2D.unity` 并点击 Play。
3. WASD / 方向键移动，E 互动，M 地图，Esc 队伍，F5 存档。

编辑器菜单 `Remake 2D/Create and verify playable earth chapter` 可创建并检查场景。调用 `Earth2DBuild.BuildMac` 输出 `Production/Build/MonsterWonderland2D.app`；生成缓存与构建包不进入 Git。

当前完整动作版推荐使用 `HeroMotionBuild.BuildMac`，包含土之秘境与 `HeroMotionPreview` 两个场景。动作资源可从 `Resources/HeroMotion/Prefabs/` 的两个 Prefab 复用；动作定义见 `HeroMotionLibrary2D`，武器替换接口见 `ChapterHeroMotion.cs`。

## 验证与边界

`Production/BaselineValidation/` 是迁移前原型记录；当前 v5 已重新执行完整章节与动作运行验证，说明见 `Art/Reviews/51/v5/README.md`。`Production/workspace-validation.json` 只验证资料完整性与审核建档。地形及部分 NPC 仍使用原版素材，完整技能、合成和怪兽专属动作尚未完成。

原型有公开记录的地图 45 碰撞兼容调整、战斗平衡适配及一个当前正常路线不可达的奖励箱；详见 `Docs/PROTOTYPE_BASELINE.md`。角色审核完成前，先保持玩法原型可用，逐一替换角色。

[立项说明](Docs/PROJECT.md) · [逐角色制作流程](Docs/ART_REVIEW.md) · [主角原版审核](Art/Reviews/51/REVIEW.md)

## 武器模块

独立预览场景为 `Unity/Assets/Remake/ArtReview/HeroEquipmentReview.unity`。身体姿势、手部挂点与武器模型分别由 `HeroPoseDefinition2D`、`HeroEquipmentRig2D`、`WeaponDefinition2D` 管理，11 个原版武器数据资源已经建档，实际型号图片尚待逐个制作。

编辑器菜单 `Remake 2D/Create modular hero art review` 创建预览并验证换装与挂点。验证结果见 `Production/hero-equipment-validation.txt`，这项验证已在当前工程执行。[四种特殊武器分类及模块化边界](Docs/WEAPON_TYPES_AND_MODULARITY.md)。
