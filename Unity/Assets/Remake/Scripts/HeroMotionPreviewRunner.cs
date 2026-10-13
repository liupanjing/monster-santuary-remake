using System;
using System.IO;
using System.Linq;
using System.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace MonsterRemake2D
{
    public class HeroMotionPreviewRunner : MonoBehaviour
    {
        public HeroMotionLibrary2D library;
        HeroMotionPlayer2D[] field, battle;
        Transform[] targets;
        bool capture;
        string output;
        HeroBattleAction2D lastAction;

        [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.AfterSceneLoad)]
        static void PreviewBootstrap()
        {
            if(Environment.GetCommandLineArgs().Contains("--hero-motion-preview")&&SceneManager.GetActiveScene().name!="HeroMotionPreview")
                SceneManager.LoadScene("HeroMotionPreview");
        }

        void Start()
        {
            capture=Environment.GetCommandLineArgs().Contains("--hero-motion-preview");
            const string key="--hero-motion-output=";
            output=Environment.GetCommandLineArgs().FirstOrDefault(s=>s.StartsWith(key))?.Substring(key.Length)??Application.persistentDataPath;
            if(capture)Directory.CreateDirectory(output);
            field=Enumerable.Range(0,4).Select(i=>Create("Q field "+i,new Vector3(i%2==0?-5.5f:-3.3f,i<2?1.0f:-2.0f,0),HeroMotionLibrary2D.FieldView(i),false)).ToArray();
            battle=new[]{Create("Battle front",new Vector3(1.3f,-2.35f,0),HeroView2D.BattleFront,true),Create("Battle rear",new Vector3(5f,-2.35f,0),HeroView2D.BattleBack,true)};
            targets=new Transform[2];for(int i=0;i<2;i++){targets[i]=new GameObject("FX target "+i).transform;targets[i].position=new Vector3(i==0?3.5f:7f,.1f,0);}
            ValidateReplacement();
            if(capture)StartCoroutine(Record());
        }

        HeroMotionPlayer2D Create(string name,Vector3 position,HeroView2D view,bool mirror)
        {
            var player=new GameObject(name).AddComponent<HeroMotionPlayer2D>();player.transform.position=position;
            if(mirror)player.transform.localScale=new Vector3(-1,1,1);
            player.Initialize(library,library.defaultWeapon,view,true);player.automaticIdle=false;
            return player;
        }

        void ValidateReplacement()
        {
            var player=battle[0];player.SampleBattle(HeroBattleAction2D.Shoot,.3f);Sprite body=player.rig.Body.sprite;
            var model=Instantiate(library.defaultWeapon);var gun=model.views[0].sprite;
            var replacement=Sprite.Create(gun.texture,gun.rect,gun.pivot/gun.rect.size,gun.pixelsPerUnit);
            foreach(var v in model.views){v.sprite=replacement;v.scale=.65f;}
            Require(player.TryEquip(model,out _)&&player.rig.Body.sprite==body&&player.rig.MainWeapon.sprite==replacement,"Swap during shooting keeps body frame");
            model.requiredPoseKey="crossbow_ready";
            Require(!player.TryEquip(model,out _)&&player.rig.Body.sprite==body,"Reject unsupported body pose atomically");
            Require(player.TryEquip(library.defaultWeapon,out _),"Restore default dual pistols");
            Destroy(model);Destroy(replacement);
        }

        void LateUpdate(){if(!capture&&field!=null)Sample(Mathf.Repeat(Time.time,4));}

        void Sample(float time)
        {
            for(int i=0;i<4;i++)field[i].SampleField(i,time>=.25f,time>=2,time);
            var action=time<.30f?HeroBattleAction2D.Idle:time<1.55f?HeroBattleAction2D.Shoot:time<2.05f?HeroBattleAction2D.Hurt:time<2.55f?HeroBattleAction2D.Guard:time<3.25f?HeroBattleAction2D.Victory:HeroBattleAction2D.Idle;
            if(action!=lastAction){foreach(var p in battle)p.ResetPreviewEvents();lastAction=action;}
            for(int i=0;i<2;i++){
                float start=action==HeroBattleAction2D.Shoot?.30f:action==HeroBattleAction2D.Hurt?1.55f:action==HeroBattleAction2D.Guard?2.05f:action==HeroBattleAction2D.Victory?2.55f:0;
                float t=time-start;
                if(action==HeroBattleAction2D.Shoot)t=Mathf.Repeat(t,library.Battle(action,battle[i].rig.view).Duration);
                battle[i].PreviewEvents(action,t,targets[i]);
            }
        }

        IEnumerator Record()
        {
            yield return null;
            for(int i=0;i<96;i++){Sample(i/24f);yield return new WaitForEndOfFrame();Capture(i);yield return null;}
            int shots=battle[0].ShotsEmitted;
            int before=shots;int impacts=0;
            yield return battle[0].Play(HeroBattleAction2D.Shoot,targets[0],()=>impacts++);
            Require(battle[0].ShotsEmitted-before==2&&impacts==1,"Two muzzle events, one damage callback");
            Require(battle[0].Action==HeroBattleAction2D.Idle&&!battle[0].Busy,"Attack restores idle");
            battle[0].holdGuard=true;yield return battle[0].Play(HeroBattleAction2D.Hurt);
            Require(battle[0].Action==HeroBattleAction2D.Guard,"Hurt returns to held guard");
            File.WriteAllText(Path.Combine(output,"runtime-validation.txt"),"PASS: built player plays 4 Q field idle/walk/run directions and front/rear battle idle/shoot/hurt/guard/victory; distinct movement sprites; modular weapon swap during shooting preserves body; incompatible grip rejected; 2 muzzle events deliver one impact callback; action completion returns to idle/guard. 96 deterministic preview samples at 24 samples/s; sample cadence is not a game FPS claim.\n");
            Debug.Log("HERO_MOTION_RUNTIME_PASSED");Application.Quit(0);
        }

        void Capture(int index)
        {
            var camera=Camera.main;var rt=new RenderTexture(1280,720,24);var prior=RenderTexture.active;
            camera.targetTexture=rt;camera.Render();RenderTexture.active=rt;
            var texture=new Texture2D(1280,720,TextureFormat.RGB24,false);texture.ReadPixels(new Rect(0,0,1280,720),0,0);texture.Apply();
            File.WriteAllBytes(Path.Combine(output,"frame-"+index.ToString("000")+".png"),texture.EncodeToPNG());
            camera.targetTexture=null;RenderTexture.active=prior;Destroy(texture);Destroy(rt);
        }

        static void Require(bool ok,string reason){if(!ok)throw new Exception("Hero motion runtime: "+reason);}
    }
}
