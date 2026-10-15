using System;
using System.Collections.Generic;
using System.Linq;
using MonsterRemake2D.Combat;
using UnityEditor;
using UnityEngine;

// Also compiled as an independent console suite by Tools/test_atb_combat.py.
public static class AtbCombatValidation
{
    static int checks;
    static void Require(bool value, string context) { checks++; if (!value) throw new Exception("ATB rules: " + context); }
    static CombatBook book;

    [MenuItem("Remake 2D/Validate new ATB combat rules")]
    public static void Run()
    {
        Validate(JsonUtility.FromJson<CombatBook>(Resources.Load<TextAsset>("Data/atb-combat").text));
        Debug.Log("ATB_RULES_PASSED: " + checks);
    }

    public static int Validate(CombatBook definitions)
    {
        book = definitions; checks = 0; book.Validate();
        ReadinessAndPause(); CommandValidation(); TargetIdentity(); GuardAndShield(); StatusLifetime(); SkillCombo();
        CaptureAndRemoval(); EnemyIntentAndPhase(); Outcomes(); DeterministicReplay();
        return checks;
    }

    static Combatant Unit(string id, Side side, int speed = 40, bool hero = false)
    {
        var u = new Combatant { Id = id, Name = id, Side = side, Hero = hero, MaxHP = 1000, HP = 1000,
            MaxMP = 500, MP = 500, Attack = 100, Defense = 40, Magic = 80, Speed = speed, Element = 5, Tier = 1 };
        u.Skills.AddRange(book.skills.Select(s => s.id)); return u;
    }
    static AtbBattle Battle(uint seed = 42)
        => new AtbBattle(book, new[] { Unit("hero", Side.Player, 80, true), Unit("pet", Side.Player, 40), Unit("enemy:0", Side.Enemy, 20), Unit("enemy:1", Side.Enemy, 10) }, new Dictionary<int, int> { { 121, 3 }, { 143, 1 } }, seed);
    static void Ready(AtbBattle b, string id)
    {
        if (b.Ready != null && b.Ready.Id != id) throw new Exception("Pending actor differs");
        foreach (var u in b.Units) u.Gauge = u.Id == id ? 100 : 0;
        b.Advance(0); Require(b.Ready != null && b.Ready.Id == id, "readiness boundary " + id);
    }
    static ActionResult Use(AtbBattle b, string actor, string skill, string target)
    {
        Ready(b, actor); return b.Submit(new CombatCommand(actor, "remake:" + skill, target));
    }
    static void Reject(Action action, string context)
    {
        bool failed = false; try { action(); } catch (InvalidOperationException) { failed = true; } Require(failed, context);
    }
    static void ReadinessAndPause()
    {
        var b = Battle(); b.Advance(100); Require(b.Ready.Id == "hero", "fastest becomes ready first");
        double clock = b.Time, gauge = b.Find("pet").Gauge; b.Advance(20);
        Require(b.Time == clock && b.Find("pet").Gauge == gauge, "planning pauses all clocks");
        b.Submit(new CombatCommand("hero", "remake:aim", "enemy:0")); Require(b.Find("hero").Gauge == -25, "heavy skill recovery debt");
        var tie = new AtbBattle(book, new[] { Unit("hero", Side.Player, 40, true), Unit("pet", Side.Player, 40), Unit("e", Side.Enemy, 40) }, new Dictionary<int,int>(), 1);
        tie.Advance(100); Require(tie.Ready.Id == "hero", "deterministic tie order");
    }
    static void CommandValidation()
    {
        var b = Battle(); Ready(b, "hero"); int hp = b.Find("enemy:0").HP, mp = b.Find("hero").MP;
        Reject(() => b.Submit(new CombatCommand("pet", "remake:attack", "enemy:0")), "unready actor rejected");
        Reject(() => b.Submit(new CombatCommand("hero", "remake:heal", "enemy:0")), "hostile heal rejected");
        Reject(() => b.Submit(new CombatCommand("hero", "remake:attack", "missing")), "stale target rejected");
        b.Find("hero").MP = 0;
        Reject(() => b.Submit(new CombatCommand("hero", "remake:aim", "enemy:0")), "insufficient MP rejected");
        Require(b.Find("enemy:0").HP == hp && b.History.Count == 0, "invalid commands have no side effects");
        b.Find("hero").MP = mp; b.Submit(new CombatCommand("hero", "remake:aim", "enemy:0")); Ready(b, "hero");
        Reject(() => b.Submit(new CombatCommand("hero", "remake:aim", "enemy:0")), "cooldown cannot be bypassed");
        b.Submit(new CombatCommand("hero", "remake:guard", "hero")); Ready(b, "hero");
        Require(b.CanUse(b.Ready, "remake:aim") == null, "cooldown expires after required intervening action");
    }
    static void TargetIdentity()
    {
        var b = Battle(); Use(b, "hero", "attack", "enemy:0"); int first = b.Find("enemy:0").HP;
        Require(b.History.Count == 1 && first == 929, "one submitted attack produces exactly one damage result");
        Use(b, "pet", "attack", "enemy:1");
        Require(b.Find("enemy:0").HP == first && b.History[0].Target == "enemy:0" && b.History[1].Target == "enemy:1", "actors retain independent targets");
        b.Find("pet").HP = 300; int hero = b.Find("hero").HP; Use(b, "hero", "heal", "pet");
        Require(b.Find("pet").HP > 300 && b.Find("hero").HP == hero, "explicit ally heal target");
        b.Find("enemy:1").HP = 0; Ready(b, "hero");
        Reject(() => b.Submit(new CombatCommand("hero", "remake:attack", "enemy:1")), "dead target rejected without retargeting");
    }
    static void GuardAndShield()
    {
        var baseline = Battle(); var hit = Use(baseline, "enemy:0", "attack", "hero");
        int ordinary = hit.Events.First(e => e.Kind == "damage").Amount;
        var guarded = Battle(); Use(guarded, "hero", "guard", "hero");
        hit = Use(guarded, "enemy:0", "attack", "hero");
        Require(hit.Events.First(e => e.Kind == "damage").Amount < ordinary, "guard reduces incoming damage");
        Ready(guarded, "hero"); Require(!guarded.Ready.Guarding, "guard expires at next own readiness");
        var shield = Battle(); Use(shield, "pet", "earth-shield", "hero");
        int strength = shield.Find("hero").Shield, health = shield.Find("hero").HP;
        hit = Use(shield, "enemy:0", "attack", "hero");
        Require(shield.Find("hero").HP == health && shield.Find("hero").Shield == strength - ordinary, "shield absorbs without spending HP");
        Use(shield, "pet", "guard", "pet"); Use(shield, "pet", "earth-shield", "hero");
        Require(shield.Find("hero").Shield == strength, "reapplying shield refreshes instead of stacking");
        var second = new AtbBattle(book, shield.Units, shield.Inventory, 1);
        Require(second.Units.All(u => !u.Guarding && u.Shield == 0 && u.Statuses.Count == 0), "battle effects never leak into next encounter");
    }
    static void StatusLifetime()
    {
        var b = Battle(); Use(b, "hero", "suppress", "enemy:0"); var enemy = b.Find("enemy:0");
        Require(enemy.Has("slow") && b.Rate(enemy) < b.Rate(b.Find("enemy:1")), "slow affects action bar rate");
        Use(b, "enemy:0", "guard", "enemy:0"); Require(enemy.Has("slow"), "slow persists one action");
        Use(b, "enemy:0", "guard", "enemy:0"); Require(!enemy.Has("slow"), "slow expires after two actions");
        Use(b, "hero", "fire-burn", "enemy:0"); int hp = enemy.HP;
        foreach (var u in b.Units) u.Gauge = u == enemy ? 100 : 0;
        var periodic = b.Advance(0); Require(periodic.Events.Any(e => e.Kind == "damage") && enemy.HP == hp - 10, "burn ticks at affected readiness");
        b.Submit(new CombatCommand(enemy.Id, "remake:guard", enemy.Id));
        enemy.Statuses.Add(new CombatStatus { Id = "stun", Remaining = 1 }); foreach (var u in b.Units) u.Gauge = u == enemy ? 100 : 0;
        var skipped = b.Advance(0); Require(skipped.Events.Any(e => e.Kind == "skip") && b.Ready == null && !enemy.Has("stun"), "stun skips exactly one readiness");
        var h = b.Find("hero"); h.Statuses.Add(new CombatStatus { Id = "silence", Remaining = 1 }); Ready(b, "hero");
        Require(b.CanUse(h, "remake:heal") == "沉默中", "silence remains active during affected action");
        b.Submit(new CombatCommand(h.Id, "remake:guard", h.Id)); Require(!h.Has("silence"), "silence expires at action end");
    }
    static void SkillCombo()
    {
        var a = Battle(); var b = Battle(); Use(a, "hero", "fire-burn", "enemy:0");
        var boosted = Use(a, "hero", "fire-detonate", "enemy:0"); var normal = Use(b, "hero", "fire-detonate", "enemy:0");
        Require(boosted.Events.First(e => e.Kind == "damage").Amount > normal.Events.First(e => e.Kind == "damage").Amount && !a.Find("enemy:0").Has("burn"), "detonate consumes burn for bonus damage");
    }
    static void CaptureAndRemoval()
    {
        var b = Battle(1); var enemy = b.Find("enemy:0"); double full = AtbBattle.CaptureChance(enemy, 1); enemy.HP = 1;
        Require(AtbBattle.CaptureChance(enemy, 1) > full, "weakening improves capture chance");
        var r = Use(b, "hero", "capture-121", enemy.Id);
        Require(r.Events.Any(e => e.Kind == "capture" && e.Amount == 1) && enemy.Removed && b.Inventory[121] == 2, "capture removes one identity and spends one ball");
        Ready(b, "pet"); Reject(() => b.Submit(new CombatCommand("pet", "remake:attack", enemy.Id)), "captured enemy cannot be targeted");
        b.Submit(new CombatCommand("pet", "remake:attack", "enemy:1")); Require(b.Find("enemy:1").HP < 1000, "remaining enemy identity survives removal");
        var boss = Battle(); boss.Find("enemy:0").Boss = true; Ready(boss, "hero");
        Reject(() => boss.Submit(new CombatCommand("hero", "remake:capture-121", "enemy:0")), "boss capture forbidden");
        Reject(() => boss.Submit(new CombatCommand("hero", "remake:escape", "hero")), "boss retreat forbidden");
        Require(boss.Inventory[121] == 3, "forbidden capture does not spend ball");
    }
    static void EnemyIntentAndPhase()
    {
        var b = Battle(); var boss = b.Find("enemy:0"); boss.Boss = true; boss.HP = 400; boss.Actions = 1;
        Require(b.EnemySkill(boss) == "remake:boss-storm", "half-health phase chooses storm");
        Ready(b, boss.Id); var r = b.SubmitEnemy();
        Require(r.Events.Count(e => e.Kind == "damage") == 2 && b.Find("hero").Has("slow") && b.Find("pet").Has("slow"), "boss AOE hits both live allies");
        Require(boss.Gauge == -35 && boss.Intent != null && boss.IntentTarget != null, "enemy intent, target and recovery updated");
        boss.HP = boss.MaxHP; boss.Actions = 5; boss.Cooldowns.Clear();
        Require(b.EnemySkill(boss) != "remake:boss-storm", "phase skill never leaks into above-half-health fallback");
    }
    static void Outcomes()
    {
        var b = Battle(); b.Find("enemy:0").HP = b.Find("enemy:1").HP = 1;
        var r = Use(b, "hero", "boss-storm", "enemy:0"); Require(r.Outcome == BattleOutcome.Victory, "AOE victory evaluated once");
        Reject(() => b.Submit(new CombatCommand("hero", "remake:attack", "enemy:0")), "finished battle rejects repeat actions");
        b = Battle(); b.Find("hero").HP = 1; r = Use(b, "enemy:0", "attack", "hero"); Require(r.Outcome == BattleOutcome.Defeat, "hero death ends combat");
        b = Battle(); b.Find("hero").HP = 1; b.Find("pet").HP = 0; r = Use(b, "enemy:0", "attack", "hero");
        Require(r.Outcome == BattleOutcome.Defeat, "last ally death ends without forecasting an empty target list");
        b = Battle(); b.Find("pet").HP = 1; r = Use(b, "enemy:0", "attack", "pet"); Require(r.Outcome == BattleOutcome.Ongoing, "pet death does not end combat");
        b = Battle(); r = Use(b, "hero", "escape", "hero"); Require(r.Outcome == BattleOutcome.Escaped, "ordinary retreat ends combat");
        b = Battle(); b.Find("pet").HP = 1; Use(b, "hero", "item-143", "pet");
        Require(b.Find("pet").HP == 401 && b.Inventory[143] == 0, "item heals chosen ally and spends exactly once");
    }
    static string Replay(double step)
    {
        var b = Battle(123); var trace = new List<string>(); int safety = 0;
        while (b.Outcome == BattleOutcome.Ongoing && safety++ < 100000)
        {
            ActionResult r = b.Advance(step);
            if (b.Ready != null) r = b.Ready.Side == Side.Enemy ? b.SubmitEnemy()
                : b.Submit(new CombatCommand(b.Ready.Id, "remake:attack", b.Units.First(u => u.Side == Side.Enemy && u.Alive).Id));
            if (r != null) trace.AddRange(r.Events.Select(e => e.Actor + ":" + e.Target + ":" + e.Kind + ":" + e.Amount));
        }
        Require(safety < 100000 && b.Outcome != BattleOutcome.Ongoing, "replay terminates");
        return string.Join("|", trace) + "|" + string.Join(",", b.Units.Select(u => u.HP));
    }
    static void DeterministicReplay() => Require(Replay(.01) == Replay(.17), "fixed-seed outcome and action trace independent of frame step");
}
