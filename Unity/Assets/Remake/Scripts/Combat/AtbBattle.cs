using System;
using System.Collections.Generic;
using System.Linq;

namespace MonsterRemake2D.Combat
{
    // Pure C# rules: no scene, coroutine, save file, frame clock or Unity random dependency.
    public class AtbBattle
    {
        public readonly CombatBook Book;
        public readonly List<Combatant> Units;
        public readonly Dictionary<int, int> Inventory;
        public readonly List<CombatCommand> History = new List<CombatCommand>();
        readonly CombatRandom random;
        public Combatant Ready { get; private set; }
        public BattleOutcome Outcome { get; private set; }
        public double Time { get; private set; }
        public bool BossFight => Units.Any(u => u.Boss);

        public AtbBattle(CombatBook book, IEnumerable<Combatant> units, IDictionary<int, int> inventory, uint seed)
        {
            book.Validate(); Book = book; Units = units.ToList(); Inventory = new Dictionary<int, int>(inventory); random = new CombatRandom(seed);
            if (Units.Select(u => u.Id).Distinct().Count() != Units.Count || Units.Count(u => u.Hero) != 1
                || !Units.Any(u => u.Side == Side.Enemy) || Units.Any(u => u.MaxHP <= 0 || u.Speed <= 0 || u.HP < 0 || u.HP > u.MaxHP))
                throw new ArgumentException("Invalid battle participants");
            foreach (var u in Units)
            {
                if (u.Skills.Any(id => Book.Find(id) == null)) throw new ArgumentException("Unknown combatant skill");
                u.Guarding = false; u.Gauge = 0; u.Shield = 0; u.Statuses.Clear(); u.Cooldowns.Clear(); u.Actions = 0;
            }
            CheckOutcome(); RefreshIntents();
        }

        public static bool KnownStatus(string id) => new[] { "burn", "poison", "stun", "slow", "haste", "vulnerable", "power", "silence" }.Contains(id);
        public double Rate(Combatant u) => (12 + Math.Sqrt(Math.Max(1, u.Speed)) * 2.4) * (u.Has("slow") ? .65 : 1) * (u.Has("haste") ? 1.35 : 1);
        public Combatant Find(string id) => Units.FirstOrDefault(u => u.Id == id);
        public IEnumerable<Combatant> Targets(Combatant actor, SkillDefinition skill)
        {
            return Units.Where(u => u.Alive && (skill.target == TargetRule.Self ? u == actor
                : skill.target == TargetRule.Ally || skill.target == TargetRule.AllAllies ? u.Side == actor.Side : u.Side != actor.Side));
        }

        public string CanUse(Combatant actor, string skillId)
        {
            var s = Book.Find(skillId);
            if (actor == null || !actor.Alive) return "角色无法行动";
            if (s == null || !actor.Skills.Contains(skillId)) return "没有该技能";
            if (actor.MP < s.mpCost) return "魔力不足";
            if (actor.Cooldown(skillId) > 0) return "冷却 " + actor.Cooldown(skillId) + " 次行动";
            if (s.mpCost > 0 && actor.Has("silence")) return "沉默中";
            if (s.itemId > 0 && (!Inventory.TryGetValue(s.itemId, out int n) || n <= 0)) return "物品不足";
            if (s.effects.Any(e => e.kind == EffectKind.Capture) && BossFight) return "守护者不可收服";
            if (s.effects.Any(e => e.kind == EffectKind.Escape) && BossFight) return "守护战不可撤退";
            if (!Targets(actor, s).Any()) return "没有合法目标";
            return null;
        }

        // Advance exactly to the next readiness boundary. Ties retain roster order.
        // Any result is returned immediately so presentation can pause before advancing again.
        public ActionResult Advance(double seconds)
        {
            if (seconds < 0 || double.IsNaN(seconds) || double.IsInfinity(seconds)) throw new ArgumentOutOfRangeException(nameof(seconds));
            if (Outcome != BattleOutcome.Ongoing || Ready != null) return null;
            var live = Units.Where(u => u.Alive).ToArray();
            double wait = live.Min(u => Math.Max(0, (100 - u.Gauge) / Rate(u)));
            double elapsed = Math.Min(seconds, wait); Time += elapsed;
            foreach (var u in live) u.Gauge = Math.Min(100, u.Gauge + elapsed * Rate(u));
            if (seconds + 1e-9 < wait) return null;
            Ready = live.First(u => u.Gauge >= 100 - 1e-7);
            var result = new ActionResult(); var actor = Ready;
            actor.Guarding = false;
            foreach (string id in actor.Cooldowns.Keys.ToArray()) actor.Cooldowns[id] = Math.Max(0, actor.Cooldowns[id] - 1);
            bool stunned = actor.Has("stun");
            foreach (var status in actor.Statuses.ToArray())
            {
                if (status.Id == "burn" || status.Id == "poison")
                    Hurt(actor, actor, Math.Max(1, status.Power), "status:" + status.Id, result, true);
                status.Remaining--;
            }
            CheckOutcome();
            if (!actor.Alive || Outcome != BattleOutcome.Ongoing) Ready = null;
            else if (stunned)
            {
                Add(result, actor, actor, "status:stun", "skip", 0, actor.Name + "眩晕，错过行动");
                EndAction(actor, 0);
            }
            // Deliver periodic effects before asking for a command or performing enemy AI.
            if (result.Events.Count > 0 || Outcome != BattleOutcome.Ongoing) { result.Outcome = Outcome; return result; }
            return null;
        }

        public ActionResult Submit(CombatCommand command)
        {
            if (command == null || Ready == null || command.Actor != Ready.Id || Outcome != BattleOutcome.Ongoing)
                throw new InvalidOperationException("Actor is not ready");
            var actor = Ready; var skill = Book.Find(command.Skill); string error = CanUse(actor, command.Skill);
            if (error != null) throw new InvalidOperationException(error);
            var legal = Targets(actor, skill).ToArray();
            bool group = skill.target == TargetRule.AllEnemies || skill.target == TargetRule.AllAllies;
            var selected = skill.target == TargetRule.Self ? actor : Find(command.Target);
            if (!group && !legal.Contains(selected)) throw new InvalidOperationException("目标已失效，请重新选择");
            var targets = group ? legal : new[] { selected };
            var result = new ActionResult();
            // Validate the whole command before spending anything.
            actor.MP -= skill.mpCost;
            if (skill.itemId > 0) Inventory[skill.itemId]--;
            actor.Cooldowns[skill.id] = skill.cooldown + 1;
            History.Add(new CombatCommand(actor.Id, skill.id, selected?.Id));
            Add(result, actor, selected, skill.id, "action", 0, actor.Name + " · " + skill.name);
            foreach (var target in targets)
            {
                foreach (var effect in skill.effects)
                {
                    if (!target.Alive || Outcome != BattleOutcome.Ongoing) break;
                    if (effect.chance < 1 && random.Next() >= effect.chance) continue;
                    Apply(actor, target, skill, effect, result);
                }
            }
            EndAction(actor, skill.recovery); CheckOutcome(); result.Outcome = Outcome;
            return result;
        }

        void Apply(Combatant actor, Combatant target, SkillDefinition skill, SkillEffect effect, ActionResult result)
        {
            int bonus = !string.IsNullOrEmpty(effect.bonusStatus) && target.Has(effect.bonusStatus) ? effect.bonusPower : 0;
            switch (effect.kind)
            {
                case EffectKind.Damage:
                    int offense = effect.damageKind == DamageKind.Magic ? actor.Magic : actor.Attack;
                    double baseDamage = effect.power + offense * effect.scale + bonus;
                    if (actor.Has("power")) baseDamage *= 1.25;
                    if (effect.damageKind != DamageKind.Direct) baseDamage *= 100.0 / (100 + target.Defense * (effect.damageKind == DamageKind.Magic ? .65 : 1));
                    int element = skill.element < 0 ? actor.Element : skill.element;
                    if (element != 5 && target.Element != 5)
                        baseDamage *= (element + 1) % 5 == target.Element ? 1.25 : (target.Element + 1) % 5 == element ? .75 : 1;
                    if (target.Has("vulnerable")) baseDamage *= 1.25;
                    if (target.Guarding) baseDamage *= .5;
                    Hurt(actor, target, Math.Max(1, (int)Math.Round(baseDamage, MidpointRounding.AwayFromZero)), skill.id, result, false);
                    if (bonus > 0 && effect.consumeBonus) target.Statuses.RemoveAll(s => s.Id == effect.bonusStatus);
                    break;
                case EffectKind.Heal:
                    int heal = Math.Min(target.MaxHP - target.HP, effect.power + (int)(actor.Magic * effect.scale)); target.HP += heal;
                    Add(result, actor, target, skill.id, "heal", heal, target.Name + "回复 " + heal + " HP"); break;
                case EffectKind.RestoreMP:
                    int mp = Math.Min(target.MaxMP - target.MP, effect.power); target.MP += mp;
                    Add(result, actor, target, skill.id, "mp", mp, target.Name + "回复 " + mp + " MP"); break;
                case EffectKind.Shield:
                    target.Shield = Math.Max(target.Shield, effect.power + (int)(actor.Magic * effect.scale));
                    Add(result, actor, target, skill.id, "shield", target.Shield, target.Name + "获得护盾 " + target.Shield); break;
                case EffectKind.Guard:
                    target.Guarding = true; Add(result, actor, target, skill.id, "guard", 0, target.Name + "防御至下次行动，伤害减半"); break;
                case EffectKind.Status:
                    var status = target.Statuses.FirstOrDefault(s => s.Id == effect.status);
                    if (status == null) { status = new CombatStatus { Id = effect.status }; target.Statuses.Add(status); }
                    // Boss control lasts at most one readiness, without hiding immunity rolls.
                    int duration = target.Boss && effect.status == "stun" ? Math.Min(1, effect.duration) : effect.duration;
                    status.Remaining = Math.Max(status.Remaining, duration); status.Power = Math.Max(status.Power, effect.power);
                    Add(result, actor, target, skill.id, "status", duration, target.Name + " · " + StatusName(effect.status) + " " + duration + " 次行动"); break;
                case EffectKind.Capture:
                    double chance = CaptureChance(target, skill.itemId - 120);
                    if (random.Next() < chance)
                    {
                        target.Removed = true;
                        Add(result, actor, target, skill.id, "capture", 1, target.Name + "被收服，加入队伍");
                    }
                    else Add(result, actor, target, skill.id, "capture", 0, target.Name + "挣脱封印（成功率 " + (int)(chance * 100) + "%）");
                    break;
                case EffectKind.Escape: Outcome = BattleOutcome.Escaped; Add(result, actor, target, skill.id, "escape", 0, "脱离战斗"); break;
            }
        }

        public static double CaptureChance(Combatant target, int ballTier)
        {
            double wounded = 1 - (double)target.HP / target.MaxHP;
            return Math.Max(.05, Math.Min(.95, .12 + wounded * wounded * .65 + .18 * Math.Min(1, (double)ballTier / Math.Max(1, target.Tier))));
        }

        void Hurt(Combatant actor, Combatant target, int amount, string skill, ActionResult result, bool direct)
        {
            int absorbed = direct ? 0 : Math.Min(target.Shield, amount); target.Shield -= absorbed;
            int damage = Math.Min(target.HP, amount - absorbed); target.HP -= damage;
            Add(result, actor, target, skill, "damage", damage, target.Name + "受到 " + damage + " 伤害" + (absorbed > 0 ? "（护盾吸收 " + absorbed + "）" : ""));
            if (!target.Alive) Add(result, actor, target, skill, "down", 0, target.Name + "倒下");
        }

        void EndAction(Combatant actor, float recovery)
        {
            actor.Statuses.RemoveAll(s => s.Remaining <= 0);
            actor.Actions++; actor.Gauge = -recovery; Ready = null; CheckOutcome(); RefreshIntents();
        }

        void CheckOutcome()
        {
            if (Outcome != BattleOutcome.Ongoing) return;
            if (!Units.First(u => u.Hero).Alive) Outcome = BattleOutcome.Defeat;
            else if (!Units.Any(u => u.Side == Side.Enemy && u.Alive)) Outcome = BattleOutcome.Victory;
        }

        public string EnemySkill(Combatant actor)
        {
            var usable = actor.Skills.Where(id => CanUse(actor, id) == null).ToArray();
            if (actor.Boss)
            {
                if (actor.HP * 2 <= actor.MaxHP && usable.Contains("remake:boss-storm") && actor.Actions % 2 == 1) return "remake:boss-storm";
                if (usable.Contains("remake:boss-venom") && actor.Actions % 3 == 1) return "remake:boss-venom";
                if (usable.Contains("remake:boss-wind") && actor.Actions % 3 == 2) return "remake:boss-wind";
                return usable.Contains("remake:boss-strike") ? "remake:boss-strike" : "remake:attack";
            }
            if (usable.Contains("remake:water-heal") && Targets(actor, Book.Find("remake:water-heal")).Any(u => u.HP * 2 < u.MaxHP)) return "remake:water-heal";
            var specialist = usable.Where(id => id != "remake:attack" && id != "remake:guard").ToArray();
            return actor.Actions % 2 == 1 && specialist.Length > 0 ? specialist[actor.Actions / 2 % specialist.Length] : "remake:attack";
        }

        public ActionResult SubmitEnemy()
        {
            if (Ready == null || Ready.Side != Side.Enemy) return null;
            var actor = Ready; string id = EnemySkill(actor); var skill = Book.Find(id);
            // Support chooses the ally with the lowest health ratio; offense alternates targets.
            var target = EnemyTarget(actor, skill);
            return Submit(new CombatCommand(actor.Id, id, target.Id));
        }

        Combatant EnemyTarget(Combatant actor, SkillDefinition skill)
        {
            var targets = Targets(actor, skill).ToArray();
            return skill.target == TargetRule.Ally ? targets.OrderBy(u => (double)u.HP / u.MaxHP).First()
                : targets[actor.Actions % targets.Length];
        }

        void RefreshIntents()
        {
            if (Outcome != BattleOutcome.Ongoing) return;
            foreach (var u in Units.Where(u => u.Side == Side.Enemy && u.Alive))
            {
                // Next readiness decrements cooldowns; forecast using that same boundary.
                foreach (var id in u.Cooldowns.Keys.ToArray()) u.Cooldowns[id]--;
                u.Intent = EnemySkill(u);
                var skill = Book.Find(u.Intent);
                u.IntentTarget = skill.target == TargetRule.AllEnemies || skill.target == TargetRule.AllAllies ? "全体" : EnemyTarget(u, skill).Name;
                foreach (var id in u.Cooldowns.Keys.ToArray()) u.Cooldowns[id]++;
            }
        }

        static void Add(ActionResult r, Combatant actor, Combatant target, string skill, string kind, int amount, string text)
            => r.Events.Add(new CombatEvent { Actor = actor.Id, Target = target?.Id, Skill = skill, Kind = kind, Amount = amount, Text = text });

        public static string StatusName(string id)
        {
            switch (id) { case "burn": return "灼烧"; case "poison": return "中毒"; case "stun": return "眩晕"; case "slow": return "迟缓";
                case "haste": return "加速"; case "vulnerable": return "破绽"; case "power": return "强化"; case "silence": return "沉默"; default: return id; }
        }
    }
}
