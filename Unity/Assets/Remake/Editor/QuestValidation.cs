using System;
using System.IO;
using System.Linq;
using UnityEditor;
using UnityEditor.Build.Reporting;
using UnityEngine;
using MonsterRemake2D;

public static class QuestValidation
{
    static void Require(bool value, string text) { if (!value) throw new Exception("Quest validation: " + text); }
    [MenuItem("Remake 2D/Verify earth quest rules")]
    public static void Run()
    {
        var def = JsonUtility.FromJson<QuestCatalog2D>(Resources.Load<TextAsset>("Data/earth-quest").text).quests.Single();
        QuestRules2D.Validate(def);
        var catalog = JsonUtility.FromJson<Catalog>(Resources.Load<TextAsset>("Data/catalog").text);
        var world = JsonUtility.FromJson<World>(Resources.Load<TextAsset>("Data/earth-world").text);
        var save = EarthSave.New(catalog);
        string Stage() => QuestRules2D.Stage(def, new EarthQuestFacts2D(save)).id;
        Require(Stage() == "explore", "New save exploration stage");
        Require(!EarthQuestSubmission2D.TrySubmit(def, save, out _) && !save.reported, "Premature turn-in rejected");
        save.Add(171, 1);
        Require(!EarthQuestSubmission2D.TrySubmit(def, save, out _) && save.Count(171) == 1, "Fragment without boss rejected atomically");
        save.Spend(171); save.visited.Add(75);
        Require(Stage() == "challenge_guardian", "Guardian area visited");
        save.bossDefeated = true;
        Require(Stage() == "fragment_missing", "Missing quest item is explicit");
        Require(!EarthQuestSubmission2D.TrySubmit(def, save, out _) && !save.reported, "Boss alone cannot finish exam");
        save.Add(171, 1);
        Require(Stage() == "ready_to_report", "Boss and fragment ready");
        Require(EarthQuestSubmission2D.TrySubmit(def, save, out _) && save.reported && Stage() == "completed", "Valid turn-in completes");
        int gold = save.gold, items = save.Count(171), pets = save.pets.Count;
        Require(!EarthQuestSubmission2D.TrySubmit(def, save, out _) && save.gold == gold && save.Count(171) == items && save.pets.Count == pets,
            "Repeated submission has no effects");
        var copy = JsonUtility.FromJson<EarthSave>(JsonUtility.ToJson(save));
        Require(copy.Valid(catalog, world) && QuestRules2D.Stage(def, new EarthQuestFacts2D(copy)).id == "completed", "Saved completion restores");
        copy.inventory.RemoveAll(i => i.id == 171);
        Require(QuestRules2D.Stage(def, new EarthQuestFacts2D(copy)).id == "completed", "Legacy completed save needs no fragment");
        copy.version = 2; copy.Upgrade();
        Require(copy.version == 3 && copy.reported && QuestRules2D.Stage(def, new EarthQuestFacts2D(copy)).id == "completed", "v2 upgrade keeps quest completion");
        var malformed = JsonUtility.FromJson<QuestDefinition2D>(JsonUtility.ToJson(def));
        malformed.stages[0].conditions[0].kind = "unknown";
        bool rejected = false;
        try { QuestRules2D.Validate(malformed); } catch (ArgumentException) { rejected = true; }
        Require(rejected, "Malformed condition rejected");
        Earth2DBuild.Verify();
        Directory.CreateDirectory("../Production/Content");
        File.WriteAllText("../Production/Content/quest-editor-validation.txt",
            "PASS: Unity compilation; data-defined earth quest stages; premature/fragment-only/boss-only submission rejected; valid and repeated turn-in; saved and legacy completed state; v2-to-v3 compatibility; malformed condition; existing map/catalog/growth/save/element regression. Static editor validation, not manual gameplay or original script fidelity.\n");
        Debug.Log("EARTH_QUEST_EDITOR_PASSED");
    }

    public static void BuildMac()
    {
        Run();
        Directory.CreateDirectory("../Production/Build");
        var report = BuildPipeline.BuildPlayer(new BuildPlayerOptions {
            scenes = new[] { "Assets/Remake/Scenes/Earth2D.unity" },
            locationPathName = "../Production/Build/QuestValidation.app",
            target = BuildTarget.StandaloneOSX, options = BuildOptions.None
        });
        Require(report.summary.result == BuildResult.Succeeded, "Quest player build");
        Debug.Log("EARTH_QUEST_BUILD_PASSED");
    }
}
