# Unity 2D 工程

本工程承载完整怪兽仙境 Remake。继续使用 2D 俯视地图与角色精灵，扩展原有剧情、角色、怪兽和战斗系统，延续建筑、迷宫与宝箱设计。整体立项、系统边界与制作路线见 ../Docs/PROJECT.md、../Docs/SYSTEMS.md、../Docs/ROADMAP.md。

用 Unity Hub 打开本目录；编辑器版本见 ProjectSettings/ProjectVersion.txt。
打开 Assets/Remake/Scenes/Earth2D.unity 后点击 Play。

主角 v4 造型已获认可，v5 动作已接入且待视觉评价；50 只怪兽仍使用未通过美术审核的旧版占位。八宝 v3 动作与独立跟随已接入，新动作视觉认可另行记录。土之秘境考试阶段与交付条件来自 Resources/Data/earth-quest.json。项目规范见 ../AGENTS.md，逐角色清单见 ../Art/character-review.json。
当前阶段正在逐个角色对照原版重新制作，不能把工程能运行视为美术通过审核。

菜单 Remake 2D/Create and verify playable earth chapter 创建并检查场景。
Earth2DBuild.BuildMac 输出 ../Production/Build/MonsterWonderland2D.app。
