using System;
using System.Collections.Generic;
using System.Linq;

namespace MonsterRemake2D.Combat
{
    public enum Side { Player, Enemy }
    public enum TargetRule { Enemy, Ally, Self, AllEnemies, AllAllies }
    public enum EffectKind { Damage, Heal, Shield, Status, Guard, Capture, Escape, RestoreMP }
    public enum DamageKind { Physical, Magic, Direct }
    public enum BattleOutcome { Ongoing, Victory, Defeat, Escaped }

    [Serializable] public class SkillEffect
    {
        public EffectKind kind;
        public DamageKind damageKind;
        public int power, duration;
        public float scale, chance = 1;
        public string status, bonusStatus;
        public int bonusPower;
        public bool consumeBonus;
    }

    [Serializable] public class SkillDefinition
    {
        public string id, name, description, provenance;
        public TargetRule target;
        public int mpCost, cooldown, itemId;
        public float recovery;
        public int element = -1;
        public SkillEffect[] effects;
    }

    [Serializable] public class CombatProfile
    {
        public int element;
        public string role;
        public string[] skills;
    }

    [Serializable] public class CombatBook
    {
        public string version;
        public SkillDefinition[] skills;
        public CombatProfile[] profiles;
        public SkillDefinition Find(string id) => skills.FirstOrDefault(s => s.id == id);

        public void Validate()
        {
            if (skills == null || profiles == null || skills.Length == 0) throw new ArgumentException("Empty combat book");
            if (skills.Select(s => s.id).Distinct().Count() != skills.Length) throw new ArgumentException("Duplicate skill ID");
            foreach (var s in skills)
            {
                if (string.IsNullOrEmpty(s.id) || string.IsNullOrEmpty(s.name) || string.IsNullOrEmpty(s.provenance)
                    || s.mpCost < 0 || s.cooldown < 0 || s.recovery < 0 || s.recovery > 100 || s.effects == null || s.effects.Length == 0)
                    throw new ArgumentException("Invalid skill: " + s.id);
                foreach (var e in s.effects)
                    if (e.chance < 0 || e.chance > 1 || e.duration < 0 || e.power < 0 || e.scale < 0
                        || e.kind == EffectKind.Status && !AtbBattle.KnownStatus(e.status))
                        throw new ArgumentException("Invalid effect: " + s.id);
            }
            foreach (var p in profiles)
                if (p.skills == null || p.skills.Any(id => Find(id) == null)) throw new ArgumentException("Unknown profile skill");
        }
    }

    public class CombatStatus
    {
        public string Id;
        public int Remaining, Power;
    }

    public class Combatant
    {
        // Encounter-local identity, independent of species IDs and mutable list indices.
        public string Id, Name, Role, Intent, IntentTarget;
        public int Species, Tier, HP, MaxHP, MP, MaxMP, Attack, Defense, Magic, Speed, Element, Shield;
        public Side Side;
        public bool Hero, Boss, Removed, Guarding;
        public double Gauge;
        public int Actions;
        public readonly List<string> Skills = new List<string>();
        public readonly List<CombatStatus> Statuses = new List<CombatStatus>();
        public readonly Dictionary<string, int> Cooldowns = new Dictionary<string, int>();
        public bool Alive => HP > 0 && !Removed;
        public bool Has(string id) => Statuses.Any(s => s.Id == id);
        public int Cooldown(string id) => Cooldowns.TryGetValue(id, out int value) ? value : 0;
    }

    public class CombatEvent
    {
        public string Actor, Target, Skill, Kind, Text;
        public int Amount;
    }

    public class ActionResult
    {
        public readonly List<CombatEvent> Events = new List<CombatEvent>();
        public BattleOutcome Outcome;
    }

    public class CombatCommand
    {
        public string Actor, Skill, Target;
        public CombatCommand(string actor, string skill, string target) { Actor = actor; Skill = skill; Target = target; }
    }

    // A specified PRNG keeps replay independent of Unity frames and System.Random versions.
    public class CombatRandom
    {
        uint state;
        public CombatRandom(uint seed) { state = seed == 0 ? 1u : seed; }
        public double Next() { state ^= state << 13; state ^= state >> 17; state ^= state << 5; return state / 4294967296.0; }
    }
}
