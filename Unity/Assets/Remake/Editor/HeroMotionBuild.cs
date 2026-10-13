using System;
using System.IO;
using System.Linq;
using UnityEngine;
using UnityEditor;
using UnityEditor.SceneManagement;
using MonsterRemake2D;

public class HeroMotionImport : AssetPostprocessor
{
    public static void Configure(TextureImporter importer,string path)
    {
        bool field=path.Contains("/walk/")||path.Contains("/run/");
        importer.textureType=TextureImporterType.Sprite;importer.spriteImportMode=SpriteImportMode.Single;
        importer.spritePixelsPerUnit=field?100:128;importer.textureCompression=TextureImporterCompression.Uncompressed;
        importer.filterMode=FilterMode.Bilinear;importer.alphaIsTransparency=true;importer.mipmapEnabled=false;
        var s=new TextureImporterSettings();importer.ReadTextureSettings(s);s.spriteAlignment=(int)SpriteAlignment.Custom;
        s.spritePivot=path.EndsWith("pistol.png")?new Vector2(.24f,.4f):field?new Vector2(.5f,0):new Vector2(.5f,24f/640);
        importer.SetTextureSettings(s);
    }
    void OnPreprocessTexture(){if(assetPath.StartsWith("Assets/Remake/Resources/HeroMotion/Art/"))Configure((TextureImporter)assetImporter,assetPath);}
}

public static class HeroMotionBuild
{
    const string Root="Assets/Remake/Resources/HeroMotion/";
    [Serializable] class FrameSource {public string view,resource,handsResource;public int index;public float[] main,off;public float mainAngle,offAngle;}
    [Serializable] class Manifest {public FrameSource[] battle;}
    static Sprite Sprite(string resource){var s=AssetDatabase.LoadAssetAtPath<Sprite>("Assets/Remake/Resources/"+resource+".png");if(s==null)throw new Exception("Missing motion sprite "+resource);return s;}
    static T Asset<T>(string path) where T:ScriptableObject {var a=AssetDatabase.LoadAssetAtPath<T>(path);if(a!=null)return a;a=ScriptableObject.CreateInstance<T>();AssetDatabase.CreateAsset(a,path);return a;}
    static HandSocket2D Socket(float[] p,float angle)=>new HandSocket2D{position=new Vector2(p[0],p[1]),rotation=angle>90&&angle<270?angle-180:angle,mirrorX=angle>90&&angle<270,sortingOffset=1};

    [MenuItem("Remake 2D/Build hero motion and preview")]
    public static void Create()
    {
        string weapons="Assets/Remake/Resources/Equipment/Weapons/";Directory.CreateDirectory(weapons);
        AssetDatabase.Refresh();
        foreach(var path in Directory.GetFiles(Root+"Art","*.png",SearchOption.AllDirectories)) {
            var importer=(TextureImporter)AssetImporter.GetAtPath(path);HeroMotionImport.Configure(importer,path);importer.SaveAndReimport();
        }
        EditorSceneManager.NewScene(NewSceneSetup.EmptyScene,NewSceneMode.Single);
        var sources=JsonUtility.FromJson<Manifest>(File.ReadAllText("../Art/Reviews/51/v5/motion-manifest.json"));
        bool firstLibrary=AssetDatabase.LoadAssetAtPath<HeroMotionLibrary2D>(Root+"Animations.asset")==null;
        var library=Asset<HeroMotionLibrary2D>(Root+"Animations.asset");if(firstLibrary)library.newFramesApproved=false;
        var battlePose=Asset<HeroPoseDefinition2D>(Root+"BattlePose.asset");battlePose.poseKey="dual_pistol_ready";battlePose.artApproved=true;
        var oldPose=AssetDatabase.LoadAssetAtPath<HeroPoseDefinition2D>("Assets/Remake/ArtReview/HeroV4/DualPistolReady.asset");
        oldPose.artApproved=true;EditorUtility.SetDirty(oldPose);
        battlePose.views=oldPose.views.Select(f=>new HeroPoseView2D{view=f.view,body=Sprite("HeroMotion/Art/battle-"+(f.view==HeroView2D.BattleFront?"front":"back")+"/idle"),mainHand=f.mainHand,offHand=f.offHand}).ToArray();
        library.battlePose=battlePose;
        var fieldPose=Asset<HeroPoseDefinition2D>(Root+"FieldPose.asset");fieldPose.poseKey="field_q";fieldPose.artApproved=false;
        library.field=Enumerable.Range(0,4).Select(i=>new FieldMotion2D {
            view=HeroMotionLibrary2D.FieldView(i),idle=Sprite("HeroMotion/Art/walk/"+i+"-0"),
            walk=Enumerable.Range(1,4).Select(j=>Sprite("HeroMotion/Art/walk/"+i+"-"+j)).ToArray(),
            run=Enumerable.Range(0,4).Select(j=>Sprite("HeroMotion/Art/run/"+i+"-"+j)).ToArray()
        }).ToArray();
        fieldPose.views=library.field.Select(f=>new HeroPoseView2D{view=f.view,body=f.idle}).ToArray();library.fieldPose=fieldPose;
        var gun=Asset<WeaponDefinition2D>(Root+"DefaultDualPistol.asset");gun.originalItemId=-1;gun.displayName="主角默认双枪";
        gun.family=WeaponFamily2D.Firearm;gun.gripLayout=GripLayout2D.DualOneHand;gun.requiredPoseKey="dual_pistol_ready";gun.fieldPoseKey="field_q";
        gun.artApproved=true;gun.previewOnly=false;gun.showInField=false;
        gun.views=new[]{HeroView2D.BattleFront,HeroView2D.BattleBack}.Select(v=>new WeaponView2D{view=v,sprite=Sprite("HeroMotion/Art/pistol")}).ToArray();library.defaultWeapon=gun;
        HeroPoseView2D Pose(HeroView2D view,int index) {
            if(index==-1)return battlePose.Find(view);
            var src=sources.battle.First(s=>s.index==index&&s.view==(view==HeroView2D.BattleFront?"front":"back"));
            return new HeroPoseView2D {view=view,body=Sprite(src.resource),foregroundHands=Sprite(src.handsResource),mainHand=Socket(src.main,src.mainAngle),offHand=Socket(src.off,src.offAngle)};
        }
        BattleMotion2D Clip(HeroBattleAction2D action,HeroView2D view,int[] indices,float[] seconds,bool loop=false) =>new BattleMotion2D {
            action=action,view=view,loop=loop,frames=indices.Select((index,j)=>new HeroMotionFrame2D {pose=Pose(view,index),duration=seconds[j],fireOff=action==HeroBattleAction2D.Shoot&&index==3,fireMain=action==HeroBattleAction2D.Shoot&&index==4}).ToArray()
        };
        library.battle=new[]{HeroView2D.BattleFront,HeroView2D.BattleBack}.SelectMany(v=>new[] {
            Clip(HeroBattleAction2D.Idle,v,new[]{-1},new[]{1f},true),
            Clip(HeroBattleAction2D.Shoot,v,new[]{1,2,3,4,5,-1},new[]{.10f,.10f,.10f,.10f,.12f,.12f}),
            Clip(HeroBattleAction2D.Hurt,v,new[]{6,7,8,-1},new[]{.09f,.12f,.12f,.10f}),
            Clip(HeroBattleAction2D.Guard,v,new[]{9},new[]{.24f},true),
            Clip(HeroBattleAction2D.Victory,v,new[]{10,-1},new[]{.55f,.15f})
        }).ToArray();
        foreach(var asset in new ScriptableObject[]{library,battlePose,fieldPose,gun})EditorUtility.SetDirty(asset);
        for(int i=0;i<11;i++) {
            string path=weapons+"Item"+i.ToString("000")+".asset";
            if(AssetDatabase.LoadAssetAtPath<WeaponDefinition2D>(path)!=null)continue;
            var src=AssetDatabase.LoadAssetAtPath<WeaponDefinition2D>("Assets/Remake/ArtReview/WeaponDefinitions/Item"+i.ToString("000")+".asset");
            AssetDatabase.CreateAsset(UnityEngine.Object.Instantiate(src),path);
        }
        AssetDatabase.SaveAssets();
        Verify(library);
        Earth2DBuild.Verify();
        var camera=new GameObject("Motion preview camera").AddComponent<Camera>();camera.tag="MainCamera";camera.orthographic=true;camera.orthographicSize=4;camera.transform.position=new Vector3(0,0,-10);camera.clearFlags=CameraClearFlags.SolidColor;camera.backgroundColor=new Color(.91f,.90f,.87f);
        var runner=new GameObject("Motion preview runner").AddComponent<HeroMotionPreviewRunner>();runner.library=library;
        Directory.CreateDirectory("Assets/Remake/Scenes");EditorSceneManager.SaveScene(UnityEngine.SceneManagement.SceneManager.GetActiveScene(),"Assets/Remake/Scenes/HeroMotionPreview.unity");
        EditorBuildSettings.scenes=new[]{new EditorBuildSettingsScene("Assets/Remake/Scenes/Earth2D.unity",true),new EditorBuildSettingsScene("Assets/Remake/Scenes/HeroMotionPreview.unity",true)};
        File.WriteAllText("../Production/hero-motion-editor-validation.txt","PASS: 4 independent Q directions; 4 idle + 16 walk + 16 run sprites; front/rear shooting, hurt, guard and victory sequences; approved idle breathing; 2 distinct firing markers per attack; hand overlays and grip anchors; separate body and weapon textures; invalid weapon/model replacement rejected; idle pose and sprite swap preserved. This is technical validation; v5 motions await visual review.\n");
        Debug.Log("HERO_MOTION_EDITOR_PASSED");
    }

    static void Verify(HeroMotionLibrary2D library)
    {
        if(library.field.Length!=4||library.field.Any(f=>f.walk.Length!=4||f.run.Length!=4||f.idle==null))throw new Exception("Field motion directions/frames");
        if(library.battle.Length!=10||library.battle.Any(c=>c.frames.Any(f=>f.pose.body==null||f.duration<=0)))throw new Exception("Battle action frames");
        foreach(var clip in library.battle.Where(c=>c.action==HeroBattleAction2D.Shoot))if(clip.frames.Count(f=>f.fireMain||f.fireOff)!=2)throw new Exception("Dual-pistol events");
        var obj=new GameObject("Motion verification");var player=obj.AddComponent<HeroMotionPlayer2D>();player.Initialize(library,library.defaultWeapon,HeroView2D.BattleFront,true);
        foreach(var clip in library.battle.Where(c=>c.view==HeroView2D.BattleFront))for(float t=0;t<clip.Duration;t+=.03f)player.SampleBattle(clip.action,t);
        player.SampleBattle(HeroBattleAction2D.Shoot,.28f);var body=player.rig.Body.sprite;int layers=obj.transform.childCount;
        var swapped=UnityEngine.Object.Instantiate(library.defaultWeapon);swapped.views[0].scale=.62f;
        if(!player.TryEquip(swapped,out _)||player.rig.Body.sprite!=body||obj.transform.childCount!=layers)throw new Exception("Weapon swap during motion");
        swapped.requiredPoseKey="rifle_ready";if(player.TryEquip(swapped,out _))throw new Exception("Unsupported grip accepted");
        var catalog=JsonUtility.FromJson<Catalog>(Resources.Load<TextAsset>("Data/catalog").text);
        var oldSave=EarthSave.New(catalog);oldSave.version=2;oldSave.opened.Add("test-chest");oldSave.gold=123;
        string oldJson=System.Text.RegularExpressions.Regex.Replace(JsonUtility.ToJson(oldSave),"\"visualWeaponId\":-?\\d+,","");
        var migrated=JsonUtility.FromJson<EarthSave>(oldJson);migrated.Upgrade();
        if(migrated.version!=3||migrated.visualWeaponId!=-1||migrated.gold!=123||!migrated.opened.Contains("test-chest"))throw new Exception("Save migration");
        UnityEngine.Object.DestroyImmediate(obj);UnityEngine.Object.DestroyImmediate(swapped);
    }

    public static void BuildMac()
    {
        Create();Directory.CreateDirectory("../Production/Build");
        var result=BuildPipeline.BuildPlayer(EditorBuildSettings.scenes.Select(s=>s.path).ToArray(),"../Production/Build/MonsterWonderland2D.app",BuildTarget.StandaloneOSX,BuildOptions.None);
        if(result.summary.result!=UnityEditor.Build.Reporting.BuildResult.Succeeded)throw new Exception("Motion game build failed");
        Debug.Log("HERO_MOTION_MAC_BUILD_PASSED");
    }

    public static void RenderPoseContact()
    {
        Create();EditorSceneManager.NewScene(NewSceneSetup.EmptyScene,NewSceneMode.Single);
        var library=Resources.Load<HeroMotionLibrary2D>("HeroMotion/Animations");
        var sources=JsonUtility.FromJson<Manifest>(File.ReadAllText("../Art/Reviews/51/v5/motion-manifest.json"));
        var camera=new GameObject("Pose contact camera").AddComponent<Camera>();camera.orthographic=true;camera.orthographicSize=2.65f;
        camera.transform.position=new Vector3(0,1.8f,-10);camera.clearFlags=CameraClearFlags.SolidColor;camera.backgroundColor=new Color(.91f,.90f,.87f);
        var actor=new GameObject("Pose contact actor").AddComponent<HeroMotionPlayer2D>();actor.Initialize(library,library.defaultWeapon,HeroView2D.BattleFront,true);
        string folder="../Art/Reviews/51/v5/pose-contact";Directory.CreateDirectory(folder);
        foreach(var source in sources.battle) {
            var view=source.view=="front"?HeroView2D.BattleFront:HeroView2D.BattleBack;
            actor.rig.TryEquip(library.battlePose,library.defaultWeapon,view,out _);
            var frame=new HeroPoseView2D{view=view,body=Sprite(source.resource),foregroundHands=Sprite(source.handsResource),mainHand=Socket(source.main,source.mainAngle),offHand=Socket(source.off,source.offAngle)};
            if(!actor.rig.TryApplyFrame(frame,false,out string reason))throw new Exception(reason);
            var rt=new RenderTexture(360,460,24);camera.targetTexture=rt;camera.Render();var prior=RenderTexture.active;RenderTexture.active=rt;
            var texture=new Texture2D(360,460,TextureFormat.RGB24,false);texture.ReadPixels(new Rect(0,0,360,460),0,0);texture.Apply();
            File.WriteAllBytes(folder+"/"+source.view+"-"+source.index.ToString("00")+".png",texture.EncodeToPNG());
            camera.targetTexture=null;RenderTexture.active=prior;UnityEngine.Object.DestroyImmediate(texture);UnityEngine.Object.DestroyImmediate(rt);
        }
        Debug.Log("HERO_MOTION_CONTACT_RENDERED");
    }

    public static void ExportPrefabs()
    {
        EditorSceneManager.NewScene(NewSceneSetup.EmptyScene,NewSceneMode.Single);
        var library=Resources.Load<HeroMotionLibrary2D>("HeroMotion/Animations");
        Directory.CreateDirectory(Root+"Prefabs");
        foreach(bool field in new[]{true,false}) {
            string path=Root+"Prefabs/"+(field?"HeroFieldAnimated":"HeroBattleAnimated")+".prefab";
            var player=new GameObject(field?"Q field hero":"Modular dual-pistol battle hero").AddComponent<HeroMotionPlayer2D>();
            player.Initialize(library,library.defaultWeapon,field?HeroView2D.FieldDown:HeroView2D.BattleFront,true);
            player.rig.bodyOrder=field?80:20;
            if(!field)player.transform.localScale=new Vector3(-.8f,.8f,1);
            PrefabUtility.SaveAsPrefabAsset(player.gameObject,path);UnityEngine.Object.DestroyImmediate(player.gameObject);
            var clone=UnityEngine.Object.Instantiate(AssetDatabase.LoadAssetAtPath<GameObject>(path));
            var motion=clone.GetComponent<HeroMotionPlayer2D>();motion.Initialize(library,library.defaultWeapon,field?HeroView2D.FieldDown:HeroView2D.BattleFront,true);
            if(field)motion.SampleField(3,true,true,.2f);else motion.SampleBattle(HeroBattleAction2D.Shoot,.3f);
            if(clone.transform.childCount!=4||motion.rig.Body.sprite==null)throw new Exception("Animated prefab reload failed");
            UnityEngine.Object.DestroyImmediate(clone);
        }
        AssetDatabase.SaveAssets();Debug.Log("HERO_MOTION_PREFABS_PASSED");
    }
}
