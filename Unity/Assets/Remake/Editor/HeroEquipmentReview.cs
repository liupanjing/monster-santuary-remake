using System;
using System.IO;
using System.Linq;
using UnityEditor;
using UnityEditor.SceneManagement;
using UnityEngine;
using MonsterRemake2D;

public class HeroReviewSpriteImport : AssetPostprocessor
{
    void OnPreprocessTexture()
    {
        if (!assetPath.StartsWith("Assets/Remake/ArtReview/HeroV4/")) return;
        var importer = (TextureImporter)assetImporter;
        importer.textureType = TextureImporterType.Sprite;
        importer.spriteImportMode = SpriteImportMode.Single;
        importer.spritePixelsPerUnit = 128;
        importer.filterMode = FilterMode.Bilinear;
        importer.textureCompression = TextureImporterCompression.Uncompressed;
        importer.alphaIsTransparency = true;
        importer.mipmapEnabled = false;
        var settings = new TextureImporterSettings();
        importer.ReadTextureSettings(settings);
        settings.spriteAlignment = (int)SpriteAlignment.Custom;
        settings.spritePivot = assetPath.EndsWith("pistol.png") ? new Vector2(.24f, .4f) : new Vector2(.5f, 0);
        importer.SetTextureSettings(settings);
    }
}

public static class HeroEquipmentReview
{
    const string Art = "Assets/Remake/ArtReview/HeroV4/";
    const string Definitions = "Assets/Remake/ArtReview/WeaponDefinitions/";
    const string Scene = "Assets/Remake/ArtReview/HeroEquipmentReview.unity";
    [Serializable] class SourceItem { public int index; public string name, description; }
    [Serializable] class WeaponEvidence { public SourceItem[] knownLocalWeapons; }

    static T Asset<T>(string path) where T : ScriptableObject
    {
        var asset = AssetDatabase.LoadAssetAtPath<T>(path);
        if (asset != null) return asset;
        asset = ScriptableObject.CreateInstance<T>();
        AssetDatabase.CreateAsset(asset, path);
        return asset;
    }

    static HandSocket2D Socket(float x, float y, bool mirror) => new HandSocket2D {
        position = new Vector2((x - 256) / 128, (512 - y) / 128),
        mirrorX = mirror, rotation = mirror ? -15 : 15, sortingOffset = -1
    };

    [MenuItem("Remake 2D/Create modular hero art review")]
    public static void CreateAndVerify()
    {
        var approvedPose = AssetDatabase.LoadAssetAtPath<HeroPoseDefinition2D>(Art + "DualPistolReady.asset");
        Require(approvedPose == null || !approvedPose.artApproved,
            "This review pose is already approved; create a new version instead of overwriting it");
        Directory.CreateDirectory(Definitions);
        AssetDatabase.Refresh();
        // Also configure imports explicitly for a newly cloned project, where PNGs
        // can have been imported before this postprocessor finished compiling.
        foreach (var name in new[] { "body-front.png", "body-back.png", "pistol.png" })
        {
            var importer = (TextureImporter)AssetImporter.GetAtPath(Art + name);
            importer.textureType = TextureImporterType.Sprite;
            importer.spriteImportMode = SpriteImportMode.Single;
            importer.spritePixelsPerUnit = 128;
            importer.filterMode = FilterMode.Bilinear;
            importer.textureCompression = TextureImporterCompression.Uncompressed;
            importer.mipmapEnabled = false;
            var settings = new TextureImporterSettings();
            importer.ReadTextureSettings(settings);
            settings.spriteAlignment = (int)SpriteAlignment.Custom;
            settings.spritePivot = name == "pistol.png" ? new Vector2(.24f, .4f) : new Vector2(.5f, 0);
            importer.SetTextureSettings(settings);
            importer.SaveAndReimport();
        }
        // NewScene unloads unused assets; open it before keeping pose sprites in
        // local managed references, otherwise those references can be unloaded.
        EditorSceneManager.NewScene(NewSceneSetup.EmptyScene, NewSceneMode.Single);
        var pose = Asset<HeroPoseDefinition2D>(Art + "DualPistolReady.asset");
        pose.poseKey = "dual_pistol_ready";
        pose.artApproved = false;
        pose.views = new[] {
            new HeroPoseView2D { view = HeroView2D.BattleFront,
                body = AssetDatabase.LoadAssetAtPath<Sprite>(Art + "body-front.png"),
                mainHand = Socket(345, 296, false), offHand = Socket(158, 285, true) },
            new HeroPoseView2D { view = HeroView2D.BattleBack,
                body = AssetDatabase.LoadAssetAtPath<Sprite>(Art + "body-back.png"),
                mainHand = Socket(365, 300, false), offHand = Socket(153, 299, true) }
        };
        var preview = Asset<WeaponDefinition2D>(Definitions + "NeutralPreviewPistol.asset");
        preview.originalItemId = -1; preview.displayName = "双枪姿势用中性手枪（待审核）";
        preview.previewOnly = true; preview.artApproved = false; preview.showInField = false;
        preview.family = WeaponFamily2D.Firearm; preview.gripLayout = GripLayout2D.DualOneHand;
        preview.requiredPoseKey = pose.poseKey;
        var pistol = AssetDatabase.LoadAssetAtPath<Sprite>(Art + "pistol.png");
        preview.views = new[] {
            new WeaponView2D { view = HeroView2D.BattleFront, sprite = pistol },
            new WeaponView2D { view = HeroView2D.BattleBack, sprite = pistol }
        };
        var evidence = JsonUtility.FromJson<WeaponEvidence>(File.ReadAllText("../Art/Reviews/51/weapon-evidence.json"));
        foreach (var source in evidence.knownLocalWeapons)
        {
            string path = Definitions + "Item" + source.index.ToString("000") + ".asset";
            bool newlyCreated = AssetDatabase.LoadAssetAtPath<WeaponDefinition2D>(path) == null;
            var definition = Asset<WeaponDefinition2D>(path);
            definition.originalItemId = source.index; definition.displayName = source.name;
            definition.originalDescription = source.description;
            definition.family = source.index == 0 ? WeaponFamily2D.Slingshot :
                source.index == 8 ? WeaponFamily2D.MagicRanged :
                source.index == 9 ? WeaponFamily2D.Crossbow :
                source.index == 10 ? WeaponFamily2D.Whip : WeaponFamily2D.Firearm;
            // Seed once. Later per-item models, grip adjustments and approval
            // records must survive rerunning this review setup command.
            if (newlyCreated)
            {
                definition.gripLayout = GripLayout2D.Unspecified; definition.requiredPoseKey = "";
                definition.artApproved = false; definition.previewOnly = false;
                definition.views = Array.Empty<WeaponView2D>();
            }
            EditorUtility.SetDirty(definition);
        }
        EditorUtility.SetDirty(pose); EditorUtility.SetDirty(preview); AssetDatabase.SaveAssets();
        var camera = new GameObject("Review camera").AddComponent<Camera>();
        camera.orthographic = true; camera.orthographicSize = 2.5f;
        camera.clearFlags = CameraClearFlags.SolidColor; camera.backgroundColor = new Color(.91f,.90f,.87f);
        camera.transform.position = new Vector3(0, 1.85f, -10);
        var front = Rig("Battle front (draft)", new Vector3(-2.5f, 0, 0), pose, preview, HeroView2D.BattleFront);
        Rig("Battle rear (draft)", new Vector3(2.5f, 0, 0), pose, preview, HeroView2D.BattleBack);
        PrefabUtility.SaveAsPrefabAsset(front.gameObject, Art + "HeroDualPistolReview.prefab");
        EditorSceneManager.SaveScene(UnityEngine.SceneManagement.SceneManager.GetActiveScene(), Scene);
        Verify(front, pose, preview);
        Capture(camera, "../Art/Reviews/51/v4/unity-dual-pistol-preview.png");
        File.WriteAllText("../Production/hero-equipment-validation.txt",
            "PASS: Unity compile; body and weapon sprites are separate; front/rear dual sockets; sprite replacement preserves body; muzzle changes follow weapon definition; repeated equips reuse layers; unsupported pose and missing art rejected atomically; single/dual visibility and unequip; production draft gate; prefab reload without duplicate layers.\n11 per-item definitions are unassigned art blueprints, not finished gun models. No new animation or gameplay art replacement.\n");
        Debug.Log("HERO_EQUIPMENT_MODULE_PASSED");
    }

    static HeroEquipmentRig2D Rig(string name, Vector3 position, HeroPoseDefinition2D pose,
        WeaponDefinition2D weapon, HeroView2D view)
    {
        var rig = new GameObject(name).AddComponent<HeroEquipmentRig2D>();
        rig.transform.position = position; rig.allowDraftArt = true;
        Require(rig.TryEquip(pose, weapon, view, out string reason), reason);
        return rig;
    }

    static void Verify(HeroEquipmentRig2D rig, HeroPoseDefinition2D pose, WeaponDefinition2D preview)
    {
        var originalBody = rig.Body.sprite;
        var swapped = UnityEngine.Object.Instantiate(preview);
        var texture = preview.views[0].sprite.texture;
        var source = preview.views[0].sprite;
        var otherSprite = Sprite.Create(texture, source.rect, source.pivot / source.rect.size, source.pixelsPerUnit);
        swapped.views[0].sprite = otherSprite;
        swapped.views[0].muzzleUV = new Vector2(.88f, .6f);
        Vector3 oldMuzzle = rig.MainMuzzle.position;
        for (int i = 0; i < 3; i++) Require(rig.TryEquip(pose, swapped, rig.view, out _), "swap");
        Require(rig.Body.sprite == originalBody && rig.MainWeapon.sprite == otherSprite &&
            rig.OffWeapon.sprite == otherSprite, "Independent weapon sprite swap");
        Require(rig.MainMuzzle.position != oldMuzzle && rig.transform.childCount == 4, "Muzzle and layer reuse");
        swapped.requiredPoseKey = "rifle_ready";
        Require(!rig.TryEquip(pose, swapped, rig.view, out _) && rig.MainWeapon.sprite == otherSprite,
            "Reject incompatible hand pose without partial changes");
        var missing = ScriptableObject.CreateInstance<WeaponDefinition2D>();
        missing.family = WeaponFamily2D.Crossbow;
        Require(!rig.TryEquip(pose, missing, rig.view, out _) && rig.Body.sprite == originalBody, "Missing crossbow art");
        swapped.requiredPoseKey = pose.poseKey; swapped.gripLayout = GripLayout2D.SingleOneHand;
        Require(rig.TryEquip(pose, swapped, rig.view, out _) && !rig.OffWeapon.gameObject.activeSelf, "Single hand layout");
        Require(rig.TryEquip(pose, null, rig.view, out _) && !rig.MainWeapon.gameObject.activeSelf, "Unequip");
        rig.allowDraftArt = false;
        Require(!rig.TryEquip(pose, preview, rig.view, out _), "Draft art approval gate");
        rig.allowDraftArt = true; Require(rig.TryEquip(pose, preview, rig.view, out _), "Restore preview");
        var prefab = AssetDatabase.LoadAssetAtPath<GameObject>(Art + "HeroDualPistolReview.prefab");
        var instance = UnityEngine.Object.Instantiate(prefab).GetComponent<HeroEquipmentRig2D>();
        Require(instance.TryEquip(pose, preview, HeroView2D.BattleBack, out _) &&
            instance.transform.childCount == 4, "Prefab reload reuses layers");
        UnityEngine.Object.DestroyImmediate(instance.gameObject);
        UnityEngine.Object.DestroyImmediate(missing);
        UnityEngine.Object.DestroyImmediate(swapped); UnityEngine.Object.DestroyImmediate(otherSprite);
    }

    static void Capture(Camera camera, string path)
    {
        var rt = new RenderTexture(1400, 800, 24);
        camera.targetTexture = rt; camera.Render();
        var prior = RenderTexture.active; RenderTexture.active = rt;
        var pixels = new Texture2D(1400, 800, TextureFormat.RGB24, false);
        pixels.ReadPixels(new Rect(0,0,1400,800), 0, 0); pixels.Apply();
        File.WriteAllBytes(path, pixels.EncodeToPNG());
        camera.targetTexture = null; RenderTexture.active = prior;
        UnityEngine.Object.DestroyImmediate(pixels); UnityEngine.Object.DestroyImmediate(rt);
    }

    static void Require(bool ok, string message)
    {
        if (!ok) throw new Exception("Hero equipment: " + message);
    }
}
