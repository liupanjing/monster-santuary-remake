using System;
using System.IO;
using System.Linq;
using UnityEditor;
using UnityEditor.SceneManagement;
using UnityEngine;
using MonsterRemake2D;

public class StoryReviewImport : AssetPostprocessor
{
    public static void Configure(TextureImporter importer)
    {
        importer.textureType=TextureImporterType.Sprite;importer.spriteImportMode=SpriteImportMode.Single;
        importer.spritePixelsPerUnit=100;importer.textureCompression=TextureImporterCompression.Uncompressed;
        importer.filterMode=importer.assetPath.Contains("/original/")?FilterMode.Point:FilterMode.Bilinear;
        importer.alphaIsTransparency=true;importer.mipmapEnabled=false;importer.maxTextureSize=2048;
        float pivot=importer.assetPath.Contains("/original/")||importer.assetPath.Contains("/qibei/")?.5f:
                    importer.assetPath.EndsWith("/sword.png")?.21f:importer.assetPath.Contains("/battle-")?.0625f:.140625f;
        var settings=new TextureImporterSettings();importer.ReadTextureSettings(settings);
        settings.spriteAlignment=(int)SpriteAlignment.Custom;settings.spritePivot=new Vector2(.5f,pivot);importer.SetTextureSettings(settings);
    }
    void OnPreprocessTexture(){if(assetPath.StartsWith("Assets/Remake/Resources/StoryMotionReview/"))Configure((TextureImporter)assetImporter);}
}
public static class StoryMotionReviewBuild
{
    const string Root="Assets/Remake/Resources/StoryMotionReview/";
    [Serializable] class Data {public Actor[] actors;}
    [Serializable] class Actor {public string id,portrait,originalField;public Movie[] actions;}
    [Serializable] class Movie {public string key;public float fps;public int[] loop;public string[] frames,hands;public ReviewGrip2D[] grips;}
    public static void Create()
    {
        AssetDatabase.Refresh();
        foreach(var path in Directory.GetFiles(Root,"*.png",SearchOption.AllDirectories)){
            var imp=(TextureImporter)AssetImporter.GetAtPath(path);StoryReviewImport.Configure(imp);imp.SaveAndReimport();
        }
        var source=JsonUtility.FromJson<Data>(File.ReadAllText(Root+"manifest.json"));
        var library=AssetDatabase.LoadAssetAtPath<StoryMotionReviewLibrary2D>(Root+"Definitions.asset");
        if(library==null){library=ScriptableObject.CreateInstance<StoryMotionReviewLibrary2D>();AssetDatabase.CreateAsset(library,Root+"Definitions.asset");}
        library.humanApproved=false;library.sword=Load("StoryMotionReview/xiya/sword");
        library.actors=source.actors.Select(a=>new ReviewCharacter2D{id=a.id,portrait=Load(a.portrait),originalField=Load(a.originalField),actions=a.actions.Select(m=>new ReviewMovie2D{key=m.key,fps=m.key=="battle-idle"?3:m.fps,loop=m.loop,frames=m.frames.Select(Load).ToArray(),hands=m.hands.Select(Load).ToArray(),grips=m.grips,durations=m.key=="battle-slash"?new[]{.14f,.24f,.09f,.08f,.12f,.18f}:null}).ToArray()}).ToArray();
        Verify(library);EditorUtility.SetDirty(library);AssetDatabase.SaveAssets();
        EditorSceneManager.NewScene(NewSceneSetup.EmptyScene,NewSceneMode.Single);
        library=AssetDatabase.LoadAssetAtPath<StoryMotionReviewLibrary2D>(Root+"Definitions.asset");
        Directory.CreateDirectory(Root+"Prefabs");
        foreach(var actor in library.actors){
            var player=new GameObject(actor.id+" · 动作审核候选").AddComponent<StoryMotionReviewActor2D>();player.Initialize(library,actor.id);
            player.Sample(actor.id=="xiya"?"battle-idle":"battle-hover",0);player.shadow.sprite=null;
            string path=Root+"Prefabs/"+actor.id+".prefab";PrefabUtility.SaveAsPrefabAsset(player.gameObject,path);UnityEngine.Object.DestroyImmediate(player.gameObject);
            var reload=UnityEngine.Object.Instantiate(AssetDatabase.LoadAssetAtPath<GameObject>(path));var component=reload.GetComponent<StoryMotionReviewActor2D>();
            component.Initialize(library,actor.id);component.Initialize(library,actor.id);if(reload.transform.childCount!=4)throw new Exception("Duplicate review layers");UnityEngine.Object.DestroyImmediate(reload);
        }
        var camera=new GameObject("Review camera").AddComponent<Camera>();camera.tag="MainCamera";camera.orthographic=true;camera.orthographicSize=3.9f;
        camera.transform.position=new Vector3(0,0,-10);camera.clearFlags=CameraClearFlags.SolidColor;camera.backgroundColor=new Color(.91f,.90f,.87f);
        new GameObject("Independent story motion review").AddComponent<StoryMotionReviewRunner>().library=library;
        EditorSceneManager.SaveScene(UnityEngine.SceneManagement.SceneManager.GetActiveScene(),"Assets/Remake/Scenes/StoryMotionReview.unity");
        Debug.Log("STORY_MOTION_REVIEW_EDITOR_PASSED");
    }
    static Sprite Load(string relative){var sprite=AssetDatabase.LoadAssetAtPath<Sprite>("Assets/Remake/Resources/"+relative+".png");if(sprite==null)throw new Exception("Review sprite missing "+relative);return sprite;}
    static void Verify(StoryMotionReviewLibrary2D library)
    {
        foreach(var actor in library.actors){
            foreach(var m in actor.actions){if(m.frames.Length==0||m.frames.Any(s=>s==null)||m.loop.Any(i=>i<0||i>=m.frames.Length))throw new Exception("Invalid movie "+m.key);}
            foreach(string d in new[]{"down","left","right","up"}){
                var m=actor.Find(actor.id=="xiya"?"field-walk-"+d:"field-flap-"+d);if(m.frames.Length!=(actor.id=="xiya"?6:4))throw new Exception("Direction/frame count");
                if(actor.id=="xiya")actor.Find("field-idle-"+d);
            }
        }
        foreach(string key in new[]{"battle-idle","battle-slash","battle-hurt"}){
            var m=library.Actor("xiya").Find(key);if(m.frames.Length!=m.hands.Length||m.frames.Length!=m.grips.Length)throw new Exception("Sword hand/grip mapping");
        }
        var trail=new StoryReviewTrail2D();trail.Reset(new Vector3(-1.45f,0),Vector3.zero);trail.Advance(new Vector3(3,0));trail.Advance(new Vector3(3,3));
        if(Vector3.Distance(trail.Position,new Vector3(3,1.55f))>.001f||trail.Direction!=3)throw new Exception("Review follower corner traversal");
        if(library.humanApproved)throw new Exception("Technical validation must not grant human art approval");
    }
    public static void BuildMac()
    {
        Create();var result=BuildPipeline.BuildPlayer(new[]{"Assets/Remake/Scenes/StoryMotionReview.unity"},"../Production/Build/StoryMotionReview.app",BuildTarget.StandaloneOSX,BuildOptions.None);
        if(result.summary.result!=UnityEditor.Build.Reporting.BuildResult.Succeeded)throw new Exception("Review player build failed");
        Debug.Log("STORY_MOTION_REVIEW_MAC_BUILD_PASSED");
    }
}
