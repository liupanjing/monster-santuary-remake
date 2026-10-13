# 怪兽仙境 Remake

独立 Unity 2D 项目，面向电脑端。第一阶段交付土之秘境，保留原版俯视迷宫与主角带怪兽的回合战斗。

## 当前状态

已迁入上一版可运行原型及原版资源研究资料。用户指出上一版角色重绘明显偏离原画，因此 **主角和 50 只怪兽的 v1 重绘全部未通过审核**，工程中的这些图片暂时作为运行占位。角色定稿数为 0，不能把图片存在或自动通关成功等同于美术通过。

当前审核角色：主角奥尼克（ID 51）。已提交 [v3 持枪造型对照](Art/Reviews/51/v3/hero-original-vs-redraw-v3.png)，等待用户确认，尚未制作动作或替换工程资源。战斗精灵与跑图 Q 版精灵为两套独立比例；v2 将枪误读为刀，已撤回。

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
|`Docs/`|立项、制作流程、原型历史说明|

## 开始运行

1. 使用 Unity **6000.3.0f1** 打开 `Unity/`。
2. 打开 `Assets/Remake/Scenes/Earth2D.unity` 并点击 Play。
3. WASD / 方向键移动，E 互动，M 地图，Esc 队伍，F5 存档。

编辑器菜单 `Remake 2D/Create and verify playable earth chapter` 可创建并检查场景。调用 `Earth2DBuild.BuildMac` 输出 `Production/Build/MonsterWonderland2D.app`；生成缓存与构建包不进入 Git。

## 验证与边界

`Production/BaselineValidation/` 是迁移前原型的自动通关记录。迁移检查见 `Production/workspace-validation.json`，它验证资料完整性、审核状态与工程目录，不代表迁移后重新执行过 Unity 通关。地形及部分 NPC 仍使用原版素材，完整技能、合成和怪兽专属动作尚未完成。

原型有公开记录的地图 45 碰撞兼容调整、战斗平衡适配及一个当前正常路线不可达的奖励箱；详见 `Docs/PROTOTYPE_BASELINE.md`。角色审核完成前，先保持玩法原型可用，逐一替换角色。

[立项说明](Docs/PROJECT.md) · [逐角色制作流程](Docs/ART_REVIEW.md) · [主角原版审核](Art/Reviews/51/REVIEW.md)
