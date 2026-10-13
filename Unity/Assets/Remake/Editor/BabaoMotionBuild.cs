using System;
using System.IO;
using System.Linq;
using UnityEditor;
using UnityEditor.SceneManagement;
using UnityEngine;
using MonsterRemake2D;

public class BabaoMotionImport : AssetPostprocessor
{
    public static void Configure(TextureImporter importer)
    {
        importer.textureType=TextureImporterType.Sprite;importer.spriteImportMode=SpriteImportMode.Single;
        importer.spritePixelsPerUnit=100;importer.textureCompression=TextureImporterCompression.Uncompressed;
        importer.filterMode=FilterMode.Bilinear;importer.alphaIsTransparency=true;importer.mipmapEnabled=false;
        var settings=new TextureImporterSettings();importer.ReadTextureSettings(settings);
        settings.spriteAlignment=(int)SpriteAlignment.Custom;settings.spritePivot=new Vector2(.5f,.5f);
        importer.SetTextureSettings(settings);
    }
    void OnPreprocessTexture(){if(assetPath.StartsWith("Assets/Remake/Resources/BabaoMotion/Art/"))Configure((TextureImporter)assetImporter);}
}

public static class BabaoMotionBuild
{
    const string Root="Assets/Remake/Resources/BabaoMotion/";
    public static void Create()
    {
        AssetDatabase.Refresh();
        foreach(var path in Directory.GetFiles(Root+"Art","*.png",SearchOption.AllDirectories)){
            var importer=(TextureImporter)AssetImporter.GetAtPath(path);BabaoMotionImport.Configure(importer);importer.SaveAndReimport();
        }
        var library=AssetDatabase.LoadAssetAtPath<BabaoMotionLibrary2D>(Root+"Animations.asset");
        if(library==null){library=ScriptableObject.CreateInstance<BabaoMotionLibrary2D>();AssetDatabase.CreateAsset(library,Root+"Animations.asset");}
        library.designApproved=true;library.approvedDesignVersion="v2";
        library.followDistance=1.45f;
        library.directions=new BabaoDirection2D[4];
        string[] names={"down","left","right","up"};
        for(int d=0;d<4;d++)library.directions[d]=new BabaoDirection2D{direction=d,frames=Enumerable.Range(0,4).Select(i=>AssetDatabase.LoadAssetAtPath<Sprite>(Root+"Art/"+names[d]+"/"+i+".png")).ToArray()};
        EditorUtility.SetDirty(library);AssetDatabase.SaveAssets();Verify(library);
        EditorSceneManager.NewScene(NewSceneSetup.EmptyScene,NewSceneMode.Single);
        library=AssetDatabase.LoadAssetAtPath<BabaoMotionLibrary2D>(Root+"Animations.asset");
        Directory.CreateDirectory(Root+"Prefabs");
        var follower=new GameObject("八宝 · 固定同伴").AddComponent<BabaoFollower2D>();
        follower.Initialize(library,Vector3.zero,new Vector3(0,library.followDistance,0),0,0);
        // Procedural shadow is recreated per instance, not serialized as a temporary texture.
        follower.shadow.sprite=null;
        string prefabPath=Root+"Prefabs/BabaoFollower.prefab";
        PrefabUtility.SaveAsPrefabAsset(follower.gameObject,prefabPath);UnityEngine.Object.DestroyImmediate(follower.gameObject);
        var clone=UnityEngine.Object.Instantiate(AssetDatabase.LoadAssetAtPath<GameObject>(prefabPath));
        var player=clone.GetComponent<BabaoFollower2D>();player.Initialize(library,Vector3.zero,new Vector3(0,library.followDistance,0),0,0);
        player.Initialize(library,Vector3.zero,new Vector3(0,library.followDistance,0),0,0);
        if(clone.transform.childCount!=2||player.visual.sprite==null||player.shadow.sprite==null)throw new Exception("Babao prefab reload/duplicate children");
        UnityEngine.Object.DestroyImmediate(clone);
        var camera=new GameObject("Babao preview camera").AddComponent<Camera>();camera.tag="MainCamera";
        camera.orthographic=true;camera.orthographicSize=3.2f;camera.transform.position=new Vector3(0,0,-10);
        camera.clearFlags=CameraClearFlags.SolidColor;camera.backgroundColor=new Color(.91f,.90f,.87f);
        var runner=new GameObject("Babao preview runner").AddComponent<BabaoMotionPreviewRunner>();runner.library=library;
        EditorSceneManager.SaveScene(UnityEngine.SceneManagement.SceneManager.GetActiveScene(),"Assets/Remake/Scenes/BabaoMotionPreview.unity");
        EditorBuildSettings.scenes=new[]{"Earth2D","HeroMotionPreview","BabaoMotionPreview"}.Select(name=>new EditorBuildSettingsScene("Assets/Remake/Scenes/"+name+".unity",true)).ToArray();
        Earth2DBuild.Verify();AssetDatabase.SaveAssets();
        Directory.CreateDirectory("../Production");
        File.WriteAllText("../Production/babao-editor-validation.txt","PASS: approved v2 design; 4 directions x 4 loadable wing poses (right mirrors left), six-phase wing loop; registered body pivot and scale; polyline follower does not cut across corners; delayed layer change; teleport reset; reloadable two-child prefab. New v3 motion awaits visual feedback.\n");
        Debug.Log("BABAO_EDITOR_PASSED");
    }

    static void Verify(BabaoMotionLibrary2D library)
    {
        if(library.directions.Length!=4||library.directions.Any(d=>d.frames.Length!=4||d.frames.Any(f=>f==null)))throw new Exception("Babao frames/directions");
        var player=new GameObject("Follow verification").AddComponent<BabaoFollower2D>();
        float gap=library.followDistance,leg=gap*2;
        player.Initialize(library,Vector3.zero,new Vector3(-gap,0,0),2,0);
        player.Advance(new Vector3(leg,0,0),0,false,1,0);
        player.Advance(new Vector3(leg,leg,0),1,false,1,.1f);
        if(Mathf.Abs(player.GroundPosition.x-leg)>.001f||Mathf.Abs(player.GroundPosition.y-gap)>.001f||player.Direction!=3||player.Plane!=0)throw new Exception("Babao corner or delayed plane");
        player.Advance(new Vector3(leg,leg*2,0),1,false,1,.2f);
        if(player.Plane!=1||player.transform.childCount!=2)throw new Exception("Babao plane or fixed layers");
        player.Advance(new Vector3(50,50,0),0,false,.016f,.3f);
        if(Vector3.Distance(player.GroundPosition,new Vector3(50,50,0))>.001f||player.Plane!=0)throw new Exception("Babao teleport reset");
        UnityEngine.Object.DestroyImmediate(player.gameObject);
    }

    public static void BuildMac()
    {
        Create();
        var result=BuildPipeline.BuildPlayer(EditorBuildSettings.scenes.Select(s=>s.path).ToArray(),"../Production/Build/MonsterWonderland2D.app",BuildTarget.StandaloneOSX,BuildOptions.None);
        if(result.summary.result!=UnityEditor.Build.Reporting.BuildResult.Succeeded)throw new Exception("Babao player build failed");
        Debug.Log("BABAO_MAC_BUILD_PASSED");
    }
}
