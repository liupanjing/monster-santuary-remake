using System;
using System.Linq;
using UnityEngine;

namespace MonsterRemake2D
{
    public class EarthQuestFacts2D : IQuestFacts2D
    {
        readonly EarthSave save;
        public EarthQuestFacts2D(EarthSave state) { save = state ?? throw new ArgumentNullException(nameof(state)); }
        public bool Flag(string key)
        {
            switch (key)
            {
                case "earth.guardian_defeated": return save.bossDefeated;
                case "earth.exam_reported": return save.reported;
                default: throw new ArgumentException("Unknown earth fact: " + key);
            }
        }
        public int ItemCount(int id) => save.Count(id);
        public bool Visited(int map) => save.visited != null && save.visited.Contains(map);
    }

    public static class EarthQuestSubmission2D
    {
        // All checks precede mutations. Completion uses existing persisted fields, so old v2/v3
        // saves need no new task-state migration. A08 will add general cross-chapter storage.
        public static bool TrySubmit(QuestDefinition2D definition, EarthSave save, out string reason)
        {
            QuestRules2D.Validate(definition);
            if (save.reported) { reason = "考试已交付。"; return false; }
            if (!QuestRules2D.Matches(definition.submissionConditions, new EarthQuestFacts2D(save)))
            { reason = "战胜守护者并带回秘境碎片后，才能交付考试。"; return false; }
            if (save.Count(definition.submissionItemId) < definition.submissionItemCount)
            { reason = "交付物品不足。"; return false; }
            // Legacy completion retained the fragment. Keep that behavior until the original
            // turn-in reward/removal rules are verified, rather than inventing a new cost here.
            save.reported = true;
            reason = "土之秘境第一阶段考试通过。";
            return true;
        }
    }

    public partial class EarthGame2D
    {
        QuestDefinition2D earthQuest;
        void LoadEarthQuest()
        {
            var file = Resources.Load<TextAsset>("Data/earth-quest");
            if (file == null) throw new Exception("Earth quest data missing");
            var catalog = JsonUtility.FromJson<QuestCatalog2D>(file.text);
            earthQuest = catalog.quests.Single(q => q.id == "remake:earth-exam");
            QuestRules2D.Validate(earthQuest);
        }
        QuestStage2D EarthQuestStage => QuestRules2D.Stage(earthQuest, new EarthQuestFacts2D(state));
        bool SubmitEarthQuest()
        {
            if (state.reported) { UpdateUI(); ShowComplete(); return true; }
            if (!EarthQuestSubmission2D.TrySubmit(earthQuest, state, out var reason))
            { Notify(reason); return false; }
            Save(); UpdateUI(); ShowComplete(); return true;
        }

        void VerifyQuestCompletionRuntime()
        {
            if (EarthQuestStage.id != "completed" || ui.objective.text != earthQuest.title + " · " + EarthQuestStage.message)
                throw new Exception("Quest completion state/UI did not refresh");
            System.IO.File.WriteAllText(System.IO.Path.Combine(SmokeOutput, "quest-runtime-validation.txt"),
                "PASS: actual earth player completed guardian/fragment/turn-in sequence; data-defined completed stage and current objective text agree. Save compatibility and invalid/repeated submission are separately checked in editor validation.\n");
        }
    }
}
