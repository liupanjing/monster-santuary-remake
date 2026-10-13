using System;
using System.Collections.Generic;

namespace MonsterRemake2D
{
    [Serializable] public class QuestCondition2D
    {
        public string kind, key;
        public int value, minimum;
        public bool expected;
    }
    [Serializable] public class QuestStage2D
    {
        public string id, message;
        public QuestCondition2D[] conditions;
    }
    [Serializable] public class QuestDefinition2D
    {
        public string id, title, provenance, sourceNote;
        public QuestStage2D[] stages;
        public QuestCondition2D[] submissionConditions;
        public int submissionItemId, submissionItemCount;
    }
    [Serializable] public class QuestCatalog2D { public QuestDefinition2D[] quests; }

    public interface IQuestFacts2D
    {
        bool Flag(string key);
        int ItemCount(int id);
        bool Visited(int map);
    }

    // Rules read facts; Unity presentation and legacy save mapping live outside this module.
    public static class QuestRules2D
    {
        public static bool Matches(QuestCondition2D[] conditions, IQuestFacts2D facts)
        {
            if (conditions == null || facts == null) throw new ArgumentException("Missing quest conditions/facts");
            foreach (var c in conditions)
            {
                if (c == null) throw new ArgumentException("Missing condition");
                bool result;
                switch (c.kind)
                {
                    case "flag": result = facts.Flag(c.key) == c.expected; break;
                    case "item_count": result = facts.ItemCount(c.value) >= c.minimum; break;
                    case "visited_map": result = facts.Visited(c.value) == c.expected; break;
                    case "always": result = true; break;
                    default: throw new ArgumentException("Unknown quest condition: " + c.kind);
                }
                if (!result) return false;
            }
            return true;
        }

        public static QuestStage2D Stage(QuestDefinition2D definition, IQuestFacts2D facts)
        {
            if (definition?.stages == null) throw new ArgumentException("Missing quest definition");
            foreach (var stage in definition.stages)
                if (Matches(stage.conditions, facts)) return stage;
            throw new InvalidOperationException("No matching stage: " + definition.id);
        }

        public static void Validate(QuestDefinition2D definition)
        {
            if (definition == null || string.IsNullOrWhiteSpace(definition.id) || definition.stages == null ||
                definition.stages.Length == 0 || definition.submissionConditions == null || definition.submissionConditions.Length == 0 ||
                definition.submissionItemId < 0 || definition.submissionItemCount <= 0)
                throw new ArgumentException("Invalid quest definition");
            var ids = new HashSet<string>();
            foreach (var s in definition.stages)
            {
                if (s == null || string.IsNullOrWhiteSpace(s.id) || !ids.Add(s.id) || s.conditions == null || s.conditions.Length == 0)
                    throw new ArgumentException("Invalid/duplicate stage");
                foreach (var c in s.conditions) ValidateCondition(c);
            }
            foreach (var c in definition.submissionConditions) ValidateCondition(c);
        }

        static void ValidateCondition(QuestCondition2D c)
        {
            if (c == null) throw new ArgumentException("Null condition");
            switch (c.kind)
            {
                case "flag": if (string.IsNullOrWhiteSpace(c.key)) throw new ArgumentException("Missing flag"); break;
                case "item_count": if (c.value < 0 || c.minimum <= 0) throw new ArgumentException("Invalid item/count"); break;
                case "visited_map": if (c.value < 0) throw new ArgumentException("Invalid map"); break;
                case "always": break;
                default: throw new ArgumentException("Unknown condition: " + c.kind);
            }
        }
    }
}
