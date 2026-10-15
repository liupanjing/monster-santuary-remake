using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using MonsterRemake2D.Combat;
using UnityEngine;

namespace MonsterRemake2D
{
    public partial class EarthGame2D
    {
        AtbBattle combat;
        readonly Dictionary<string, Unit> combatSources = new Dictionary<string, Unit>();
        readonly Dictionary<string, SpriteRenderer> combatViews = new Dictionary<string, SpriteRenderer>();
        readonly HashSet<string> capturedThisBattle = new HashSet<string>();
        readonly List<string> combatLog = new List<string>();
        string pendingSkill, combatTarget;
        bool battleRewardsClaimed;
        float battleRefreshClock;
        int encounterSequence;
        string renderedReady;

        public void StartBattle(Unit[] enemies, bool boss)
        {
            if (mode == Mode.Battle) return;
            encounterSequence++;
            var book = Read<CombatBook>("atb-combat"); book.Validate();
            // The profile book is passed explicitly during construction, without running rules on a partial roster.
            combatSources.Clear(); combatViews.Clear(); capturedThisBattle.Clear(); combatLog.Clear();
            var actors = new List<Combatant>();
            actors.Add(BuildCombatant(state.hero, "hero", Side.Player, book, true));
            actors.Add(BuildCombatant(Pet, "pet", Side.Player, book));
            for (int i = 0; i < enemies.Length; i++) actors.Add(BuildCombatant(enemies[i], "enemy:" + i, Side.Enemy, book, false, boss));
            uint seed = Smoke ? 42u : unchecked((uint)(Environment.TickCount ^ encounterSequence));
            combat = new AtbBattle(book, actors, state.inventory.ToDictionary(s => s.id, s => s.count), seed);
            mode = Mode.Battle; bossBattle = boss; foes = enemies.ToList(); foeSprites.Clear();
            field.SetActive(false); battleScene = new GameObject("ATB battle · " + book.version);
            camera2d.rect = new Rect(0, 0, 1, 1); camera2d.orthographicSize = 4.3f; camera2d.transform.position = new Vector3(0, 0, -10);
            var bg = Draw("Original earth battle background", Resources.Load<Sprite>("Art/fbg1_0"), Vector3.zero, battleScene.transform, -150);
            var size = bg.sprite.bounds.size; bg.transform.position = new Vector3(0, -camera2d.orthographicSize, 0);
            bg.transform.localScale = new Vector3(camera2d.orthographicSize * 2 * (1280f / 800f) / size.x, camera2d.orthographicSize * 2 / size.y, 1);
            battleHeroMotion = NewHero("奥尼克 · 独立双枪战斗动作", battleScene.transform, new Vector3(-4.75f, -.65f, 0), HeroView2D.BattleFront, 20, .65f, true);
            battleHero = battleHeroMotion.rig.Body;
            battlePet = Draw("Active beast 2D", SpriteFor(Pet.id), new Vector3(-2.15f, -.65f, 0), battleScene.transform, 11, Mathf.Min(1.9f, 1.7f / SpriteFor(Pet.id).bounds.size.y)); battlePet.flipX = true;
            combatViews.Add("hero", battleHero); combatViews.Add("pet", battlePet);
            for (int i = 0; i < foes.Count; i++)
            {
                var sprite = SpriteFor(foes[i].id);
                var view = Draw("Enemy " + i, sprite, new Vector3(boss ? 3.5f : 1.8f + i * 1.65f, -.65f + (!boss && i == 1 ? .3f : 0), 0), battleScene.transform, 10 + i, Mathf.Min(boss ? 2.2f : 1.5f, (boss ? 2.1f : 1.8f) / sprite.bounds.size.y));
                foeSprites.Add(view); combatViews.Add("enemy:" + i, view);
            }
            pendingSkill = combatTarget = null; resolving = battleRewardsClaimed = false;
            log = "遭遇 " + string.Join("、", foes.Select(e => catalog.Find(e.id).name)) + "。行动条推进中。";
            combatLog.Add(log); Beep(165, .15f); BattleUI();
        }

        Combatant BuildCombatant(Unit source, string id, Side side, CombatBook book, bool hero = false, bool boss = false)
        {
            var definition = catalog.Find(source.id); var s = source.stats;
            var actor = new Combatant { Id = id, Name = definition.name, Species = source.id, Tier = definition.tier,
                Side = side, Hero = hero, Boss = boss, HP = source.hp, MaxHP = s.hp, MP = source.mp, MaxMP = s.mp,
                Attack = s.attack, Defense = s.defense, Magic = s.magic, Speed = s.speed, Element = s.element };
            actor.Skills.AddRange(new[] { "remake:attack", "remake:guard" });
            if (hero)
            {
                actor.Role = "枪手 · 压制 / 急救"; actor.Skills.AddRange(new[] { "remake:aim", "remake:suppress", "remake:heal" });
                actor.Skills.AddRange(Enumerable.Range(141, 5).Select(i => "remake:item-" + i));
                actor.Skills.AddRange(Enumerable.Range(121, 5).Select(i => "remake:capture-" + i)); actor.Skills.Add("remake:escape");
            }
            else if (boss)
            {
                actor.Role = "守护者 · 半血狂风阶段"; actor.Skills.AddRange(new[] { "remake:boss-strike", "remake:boss-venom", "remake:boss-wind", "remake:boss-storm" });
            }
            else { var profile = book.profiles.First(p => p.element == s.element); actor.Role = profile.role; actor.Skills.AddRange(profile.skills); }
            combatSources.Add(id, source); return actor;
        }

        void TickBattle()
        {
            if (combat == null || resolving || mode != Mode.Battle) return;
            ActionResult result;
            if (combat.Ready != null && combat.Ready.Side == Side.Enemy) result = combat.SubmitEnemy();
            else result = combat.Advance(Smoke ? .15 : Math.Min(.1, Time.deltaTime));
            if (result != null) { SyncCombat(result); StartCoroutine(PlayCombatResult(result)); return; }
            if (combat.Outcome != BattleOutcome.Ongoing) { FinishBattle(combat.Outcome == BattleOutcome.Victory, combat.Outcome == BattleOutcome.Defeat); return; }
            battleRefreshClock += Time.deltaTime;
            if (renderedReady != combat.Ready?.Id || combat.Ready == null && battleRefreshClock > .12f)
            { battleRefreshClock = 0; BattleUI(); }
        }

        IEnumerable<string> VisibleSkills(Combatant actor)
        {
            if (actor == null) return Array.Empty<string>();
            return actor.Skills.Where(id => !id.StartsWith("remake:item-") && !id.StartsWith("remake:capture-"))
                .Concat(actor.Skills.Where(id => id.StartsWith("remake:item-") || id.StartsWith("remake:capture-")).Where(id => combat.Inventory.TryGetValue(combat.Book.Find(id).itemId, out int n) && n > 0));
        }

        void BattleUI()
        {
            if (mode != Mode.Battle || combat == null) return;
            renderedReady = combat.Ready?.Id;
            ui.Clear(); var p = ui.Panel(ui.root, 16, 482, 1248, 210); ui.modal = p;
            var ready = combat.Ready?.Side == Side.Player && !resolving ? combat.Ready : null;
            string heading = resolving ? "行动结算中" : ready == null ? "行动条推进中" : ready.Name + "就绪 · 选择指令（时间暂停）";
            ui.Label(p, 16, 8, 800, 30, heading, 21, true);
            var panelSkill = pendingSkill == null ? null : combat.Book.Find(pendingSkill);
            ui.Label(p, 16, 42, 1180, 44, panelSkill == null ? (ready?.Role ?? "速度决定就绪时间；敌方下一次行动显示在右侧。") : panelSkill.description, 17);
            var skills = VisibleSkills(ready).ToArray();
            for (int i = 0; i < skills.Length; i++)
            {
                string id = skills[i]; var s = combat.Book.Find(id); string reason = combat.CanUse(ready, id);
                string label = (pendingSkill == id ? "▶ " : "") + s.name + (s.mpCost > 0 ? " · " + s.mpCost + "MP" : "");
                if (ready.Cooldown(id) > 0) label += " [" + ready.Cooldown(id) + "]";
                var button = ui.Button(p, 16 + i % 8 * 152, 88 + i / 8 * 48, 144, 42, label, () => PrepareBattleAction(id), reason == null);
                button.GetComponentInChildren<UnityEngine.UI.Text>().fontSize = 15;
            }
            ui.Label(p, 16, 185, 880, 24, pendingSkill == null ? "1 攻击　2 专属技能　3 防御　就绪后点击技能，再点击目标确认" : "请选择下方合法目标；Esc 取消指令。", 14);
            // Unit cards belong to the battle modal so they disappear together at battle end.
            for (int i = 0; i < combat.Units.Count; i++)
            {
                var u = combat.Units[i]; bool ally = u.Side == Side.Player;
                float x = ally ? 16 + i * 300 : 650 + (i - 2) * 202;
                float w = ally ? 286 : 190;
                var card = ui.Panel(p, x - 16, -364, w, ally ? 114 : 130, new Color(.06f, .09f, .12f, .92f));
                bool legal = ready != null && panelSkill != null && combat.Targets(ready, panelSkill).Contains(u);
                string name = u.Name + (u.Removed ? " · 已收服" : !u.Alive ? " · 倒下" : u.Boss && u.HP * 2 <= u.MaxHP ? " · 暴走" : "");
                if (legal) ui.Button(card, 4, 3, w - 8, 28, "▶ " + name + " · 确认目标", () => SubmitBattleAction(u.Id));
                else ui.Label(card, 8, 4, w - 16, 26, name, 17, true);
                ui.Label(card, 8, 33, w - 16, 24, "HP " + u.HP + "/" + u.MaxHP + "  MP " + u.MP, 15);
                var bar = ui.Panel(card, 8, 61, w - 16, 8, new Color(.15f, .18f, .20f));
                ui.Panel(bar, 0, 0, (w - 16) * Mathf.Clamp01((float)u.Gauge / 100), 8, u == combat.Ready ? new Color(1, .83f, .4f) : new Color(.27f, .7f, .8f));
                string statuses = (u.Guarding ? "防御 " : "") + (u.Shield > 0 ? "盾" + u.Shield + " " : "") + string.Join(" ", u.Statuses.Select(s => AtbBattle.StatusName(s.Id) + Math.Max(1, s.Remaining)));
                ui.Label(card, 8, 74, w - 16, 24, statuses, 14);
                if (!ally && u.Alive) ui.Label(card, 8, 100, w - 16, 28, combat.Book.Find(u.Intent).name + " → " + u.IntentTarget, 14);
            }
            ui.resources.text = "行动条战斗 · " + combat.Book.version + "\n战斗时间 " + combat.Time.ToString("0.0") + " 秒";
            ui.message.text = string.Join("\n", combatLog.Skip(Math.Max(0, combatLog.Count - 2)));
            ui.message.fontSize = 16; ui.prompt.text = "行动条满时独立下令 · 选择指令与播放动画期间暂停时间";
        }

        void PrepareBattleAction(string id)
        {
            var actor = combat?.Ready; if (resolving || actor == null || actor.Side != Side.Player) return;
            string error = combat.CanUse(actor, id); if (error != null) { log = error; combatLog.Add(error); BattleUI(); return; }
            pendingSkill = id; var skill = combat.Book.Find(id);
            if (skill.target == TargetRule.Self || skill.target == TargetRule.AllEnemies || skill.target == TargetRule.AllAllies)
                SubmitBattleAction(combat.Targets(actor, skill).First().Id);
            else BattleUI();
        }

        void SubmitBattleAction(string targetId)
        {
            if (pendingSkill == null || combat?.Ready == null || resolving) return;
            try
            {
                var result = combat.Submit(new CombatCommand(combat.Ready.Id, pendingSkill, targetId));
                combatTarget = targetId; pendingSkill = null; SyncCombat(result); StartCoroutine(PlayCombatResult(result));
            }
            catch (InvalidOperationException e) { log = e.Message; combatLog.Add(log); BattleUI(); }
        }

        // Smoke/API shorthand submits one ready actor; it never queues another actor's order.
        public void Choose(string action)
        {
            var actor = combat?.Ready; if (mode != Mode.Battle || resolving || actor == null || actor.Side != Side.Player) return;
            string id = ShortcutSkill(actor, action);
            if (id == null) return;
            string error = combat.CanUse(actor, id); if (error != null) { log = error; combatLog.Add(error); BattleUI(); return; }
            pendingSkill = id; var legal = combat.Targets(actor, combat.Book.Find(id)).ToArray();
            string selected = legal.Any(u => u.Id == combatTarget) ? combatTarget : legal[0].Id;
            SubmitBattleAction(selected);
        }

        string ShortcutSkill(Combatant actor, string action)
        {
            if (action.StartsWith("remake:")) return action;
            if (action == "attack" || action == "guard" || action == "heal" || action == "escape") return "remake:" + action;
            if (action == "skill") return actor.Skills.FirstOrDefault(id => id != "remake:attack" && id != "remake:guard" && combat.CanUse(actor, id) == null);
            if (action == "capture") return actor.Skills.Where(id => id.StartsWith("remake:capture-")).Reverse().FirstOrDefault(id => combat.CanUse(actor, id) == null);
            if (action == "item") return actor.Skills.Where(id => id.StartsWith("remake:item-")).FirstOrDefault(id => combat.CanUse(actor, id) == null);
            return null;
        }

        void BattleInput()
        {
            if (Input.GetKeyDown(KeyCode.Escape)) { pendingSkill = null; BattleUI(); return; }
            if (combat?.Ready == null || combat.Ready.Side != Side.Player || resolving) return;
            if (Input.GetKeyDown(KeyCode.Alpha1)) PrepareBattleAction("remake:attack");
            if (Input.GetKeyDown(KeyCode.Alpha2)) { var id = ShortcutSkill(combat.Ready, "skill"); if (id != null) PrepareBattleAction(id); }
            if (Input.GetKeyDown(KeyCode.Alpha3)) PrepareBattleAction("remake:guard");
        }

        void SyncCombat(ActionResult result)
        {
            foreach (var u in combat.Units) { var source = combatSources[u.Id]; source.hp = u.HP; source.mp = u.MP; source.guarding = false; }
            foreach (var stack in combat.Inventory)
            {
                var saved = state.inventory.FirstOrDefault(s => s.id == stack.Key); if (saved != null) saved.count = stack.Value;
            }
            foreach (var e in result.Events.Where(e => e.Kind == "capture" && e.Amount == 1))
                if (capturedThisBattle.Add(e.Target)) state.pets.Add(combatSources[e.Target]);
        }

        IEnumerator PlayCombatResult(ActionResult result)
        {
            resolving = true;
            var action = result.Events.FirstOrDefault(e => e.Kind == "action");
            if (action != null)
            {
                log = action.Text; combatLog.Add(log); BattleUI();
                if (action.Actor == "hero" && result.Events.Any(e => e.Kind == "damage"))
                {
                    var hit = result.Events.First(e => e.Kind == "damage");
                    yield return battleHeroMotion.Play(HeroBattleAction2D.Shoot, combatViews[hit.Target].transform);
                }
                else if (action.Actor == "hero" && action.Skill == "remake:guard") yield return battleHeroMotion.Play(HeroBattleAction2D.Guard);
                else if (result.Events.Any(e => e.Kind == "damage"))
                {
                    var hit = result.Events.First(e => e.Kind == "damage");
                    yield return Lunge(combatViews[action.Actor], combatViews[hit.Target], combat.Find(action.Actor).Side == Side.Player ? 1 : -1);
                }
            }
            foreach (var e in result.Events.Where(e => e.Kind != "action"))
            {
                log = e.Text; combatLog.Add(log);
                if (combatViews.TryGetValue(e.Target ?? "", out var view))
                {
                    if (e.Kind == "damage" && e.Amount > 0)
                    {
                        view.color = new Color(1, .4f, .35f); yield return new WaitForSeconds(Smoke ? .01f : .16f); view.color = Color.white;
                    }
                    if (e.Kind == "capture" && e.Amount == 1) view.gameObject.SetActive(false);
                }
                BattleUI();
            }
            foreach (var u in combat.Units) if (!u.Alive && !u.Removed) combatViews[u.Id].color = new Color(.35f, .35f, .35f, .4f);
            if (result.Events.Any(e => e.Kind == "damage" && e.Target == "hero" && e.Amount > 0) && combat.Find("hero").Alive)
                yield return battleHeroMotion.Play(HeroBattleAction2D.Hurt);
            if (combat.Outcome == BattleOutcome.Victory) yield return battleHeroMotion.Play(HeroBattleAction2D.Victory);
            resolving = false;
            if (combat.Outcome != BattleOutcome.Ongoing) FinishBattle(combat.Outcome == BattleOutcome.Victory, combat.Outcome == BattleOutcome.Defeat);
            else BattleUI();
        }

        IEnumerator Lunge(SpriteRenderer a, SpriteRenderer b, int sign)
        {
            Vector3 p = a.transform.position; float t = 0;
            while (t < .18f) { t += Time.deltaTime; a.transform.position = p + Vector3.right * (Mathf.Sin(t / .18f * Mathf.PI) * .3f * sign); yield return null; }
            a.transform.position = p;
        }

        void FinishBattle(bool won, bool defeated)
        {
            if (mode != Mode.Battle || battleRewardsClaimed) return;
            if (Smoke) System.IO.File.WriteAllText(System.IO.Path.Combine(SmokeOutput, "battle-" + encounterSequence + "-trace.txt"),
                "Result: " + (won ? "victory" : defeated ? "defeat" : "escape") + "\n" + string.Join("\n", combatLog));
            battleRewardsClaimed = true; resolving = false; pendingSkill = null;
            state.hero.guarding = false; foreach (var pet in state.pets) pet.guarding = false;
            ui.Clear(); Destroy(battleScene); field.SetActive(true); mode = Mode.Explore;
            camera2d.rect = new Rect(0, .13f, 1, .735f); camera2d.orthographicSize = 6; ui.message.fontSize = 19;
            if (defeated)
            {
                state.gold = Mathf.Max(0, state.gold - 10); Rest(); state.x = 10; state.y = 15; state.plane = 0; BuildMap(9);
                Notify("队伍撤回入口休整。损失 10 金币，已领取的宝箱仍保留。");
            }
            else if (won)
            {
                state.gold += 20 + map.floor * 10; GainXP(state.hero, 30 + map.floor * 15); GainXP(Pet, 30 + map.floor * 15);
                if (bossBattle)
                {
                    state.bossDefeated = true; state.Add(171, 1); BuildMap(map.id);
                    Dialogue(new[] { Pair("秘境守护者", "在你身上，我又看到了耐法瑞的影子呢。"), Pair("秘境守护者", "后面的路还很长，那么，就让我成为你传奇的第一步吧。"), Pair("提示", "获得秘境碎片。返回入口交付认证物。") }, null);
                }
                else Notify("战斗胜利。获得金币和经验。");
            }
            else Notify("成功脱离战斗。");
            FollowCamera(true); Save(); UpdateUI();
        }
    }
}
