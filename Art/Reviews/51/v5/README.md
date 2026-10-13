# 奥尼克 v5：跑图与双枪战斗动作

用户于 2026-10-13 认可 v4 双枪造型，并授权制作动作。基线认可与本轮动作视觉验收分开记录；本轮是已接入试玩的动作初版，不自动标记为用户已认可的新动作。

## 动作范围

|场景|方向 / 视角|资源与播放|
|---|---|---|
|Q 版跑图|下、左、右、上|4 张待机图，16 张行走图，16 张独立奔跑图|
|行走|每方向 4 张|8 张图/秒，左右接触与经过循环|
|奔跑|每方向 4 张|12 张图/秒，跨步与收腿腾空，Shift 实际移动更快|
|战斗待机|正面、背面|沿用已认可的 v4 身体，轻微程序呼吸；不是额外手绘帧|
|双枪射击|正面、背面|举枪 → 瞄准 → 两次后坐力 → 放下 → 待机；两次开火只结算一次攻击伤害|
|受击|正面、背面|冲击 → 收缩 → 恢复；回到待机或原防御姿势|
|防御、胜利|正面、背面|对应独立身体姿势与持续时间，枪械仍单独挂载|

战斗共生成并切出 24 张身体姿势图，其中本轮动作实际使用 20 张；待机继续用 2 张已认可 v4 身体图，未用新生成的近似待机图替换基线。手部遮挡图从相同身体帧技术切出，没有把枪合成进身体图片。

当前治疗、血瓶、封印球和撤退沿用原型逻辑；本轮没有宣称为这些指令另绘专属动作，也没有新增七种原版枪的型号图片。

## 查看

- [完整实际 Unity 动作预览](hero-motion-preview.gif)
- [Q 版四方向预览](field-motion-preview.gif)
- [双枪战斗预览](battle-motion-preview.gif)
- [逐帧身体检查](battle-contact.png)、[挂枪后的逐帧检查](battle-with-weapons-contact.png)
- 在 Unity 打开 `Assets/Remake/Scenes/HeroMotionPreview.unity` 后 Play，可循环查看。
- 在 `Assets/Remake/Scenes/Earth2D.unity` Play 可试玩土之秘境，WASD 移动，Shift 奔跑，E 互动。

GIF 由实际构建程序的 96 张渲染采样编码，平均 24 个采样/秒；它不是 96 张手绘帧，也不是游戏实际 FPS 测量。源图绘制与游戏每秒更新、连续位移及 GIF 采样分别记录。

## 枪械与动作模块

身体精灵、手部挂点/遮挡、武器精灵、枪口和效果分别管理。`HeroMotionLibrary2D` 存动作序列，`HeroMotionPlayer2D` 播放身体帧；`HeroEquipmentRig2D` 使用当前装备图片和每帧手部挂点，不把武器画死在动作图中。

装备图形接口为 `EarthGame2D.TryEquipVisualWeapon(originalItemId, out reason)`。原版 ID 对应 `Resources/Equipment/Weapons/ItemNNN.asset`；当前 11 种武器是数据蓝图，没有未制作的型号替代图。缺失图片或姿势不兼容时拒绝换装。后续补全图片与对应姿势即可接入，不需要重绘通用双枪身体；长枪、弩、鞭等仍需要各自姿势。

此接口只负责视觉与存档，不虚构原版武器属性或新增第二个数值装备槽。默认双枪使用已认可的中性枪图，原版物品 ID 为 -1。存档保存视觉装备 ID；v2 记录迁为 v3 时保留剧情、金币、队伍、宝箱和坐标，并将缺失的视觉装备字段置为默认双枪。存档文件名保留以兼容旧记录。

资源 Prefab：`Resources/HeroMotion/Prefabs/HeroFieldAnimated.prefab` 和 `HeroBattleAnimated.prefab`。依赖均在当前 Unity 工程内，两个角色形态有不同身体资源与 PPU。

## 验证

已通过当前 Unity 编译、动作/事件结构检查、动画中换枪、不兼容握持拒绝、旧存档迁移检查、实际构建程序动作回放，以及土之秘境完整自动通关。手工键盘试玩与用户对新动作的视觉认可尚未记录。

源图、切片根位置、共同缩放、手部挂点与提示词见 `source/`、`prompts/`、[motion-manifest.json](motion-manifest.json)。绘制使用内置 imagegen；技术切片、透明处理和 GIF 编码没有生成新姿势或插值帧。
