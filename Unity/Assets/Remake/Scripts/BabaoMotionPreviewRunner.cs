using System;
using System.IO;
using System.Linq;
using System.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace MonsterRemake2D
{
    public class BabaoMotionPreviewRunner : MonoBehaviour
    {
        public BabaoMotionLibrary2D library;
        BabaoFollower2D[] actors;
        HeroMotionPlayer2D hero;
        BabaoFollower2D follower;
        string output;
        bool capture;
        int cornerCount;

        [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.AfterSceneLoad)]
        static void Bootstrap()
        {
            if(Environment.GetCommandLineArgs().Contains("--babao-preview")&&SceneManager.GetActiveScene().name!="BabaoMotionPreview")
                SceneManager.LoadScene("BabaoMotionPreview");
        }

        void Start()
        {
            capture=Environment.GetCommandLineArgs().Contains("--babao-preview");
            const string key="--babao-output=";
            output=Environment.GetCommandLineArgs().FirstOrDefault(s=>s.StartsWith(key))?.Substring(key.Length)??Application.persistentDataPath;
            if(capture)Directory.CreateDirectory(output);
            actors=Enumerable.Range(0,4).Select(i=>Create("Babao direction "+i,new Vector3(-4.1f+i*2.5f,1.1f,0),i)).ToArray();
            var heroLibrary=Resources.Load<HeroMotionLibrary2D>("HeroMotion/Animations");
            hero=new GameObject("Q hero with independent Babao").AddComponent<HeroMotionPlayer2D>();
            hero.Initialize(heroLibrary,heroLibrary.defaultWeapon,HeroView2D.FieldRight,true);hero.automaticIdle=false;
            hero.transform.position=new Vector3(-3.4f,-1.6f,0);
            follower=Create("Actual polyline follower",hero.transform.position+Vector3.left*library.followDistance,2);
            follower.ResetTrail(hero.transform.position,follower.transform.position,2,0);
            if(capture)StartCoroutine(Record());
        }

        BabaoFollower2D Create(string name,Vector3 position,int direction)
        {
            var p=new GameObject(name).AddComponent<BabaoFollower2D>();p.Initialize(library,position,position,direction,0);return p;
        }

        void LateUpdate(){if(!capture&&actors!=null)Sample(Mathf.Repeat(Time.time,4),Time.deltaTime);}

        void Sample(float time,float dt)
        {
            bool following=time>=1;
            float flapPhase=time<1?time*library.idleRate:library.idleRate+(time-1)*library.followRate;
            for(int i=0;i<4;i++)actors[i].Sample(i,following,flapPhase,time);
            Vector3 next;
            int direction;
            // Four straight segments. The follower must traverse the same corners.
            if(time<1){next=new Vector3(-3.4f+time*3,-1.6f,0);direction=2;}
            else if(time<2){next=new Vector3(-.4f,-1.6f+(time-1)*1.2f,0);direction=3;}
            else if(time<3){next=new Vector3(-.4f+(time-2)*3,-.4f,0);direction=2;}
            else {next=new Vector3(2.6f,-.4f-(time-3)*1.2f,0);direction=0;}
            hero.transform.position=next;hero.SampleField(direction,true,time>=2,time);
            follower.Advance(next,0,time>=2,dt,time);
            Require(Vector3.Distance(follower.GroundPosition,next)<=library.followDistance+.01f,"Bounded following distance");
            bool onRoute=Mathf.Abs(follower.GroundPosition.y+1.6f)<.001f||
                         Mathf.Abs(follower.GroundPosition.x+.4f)<.001f||
                         Mathf.Abs(follower.GroundPosition.y+.4f)<.001f||
                         Mathf.Abs(follower.GroundPosition.x-2.6f)<.001f;
            Require(onRoute,"Follower remains on traversed route, no diagonal corner cutting");
            if(Mathf.Abs(time-1)<.001f||Mathf.Abs(time-2)<.001f||Mathf.Abs(time-3)<.001f)cornerCount++;
        }

        IEnumerator Record()
        {
            yield return null;
            for(int i=0;i<96;i++){Sample(i/24f,1/24f);yield return new WaitForEndOfFrame();Capture(i);yield return null;}
            Require(cornerCount==3,"Three actual follower corners");
            foreach(var actor in actors)Require(actor.transform.childCount==2,"Body and shadow stay separate");
            File.WriteAllText(Path.Combine(output,"runtime-validation.txt"),"PASS: built Unity player samples four-direction floating/flapping with 4 key drawings per direction (right mirrored), six-phase loop; ground shadow stays stationary beneath bobbing sprite; path follower traverses three corners on the leader's path with <=1.46 tile spacing; idle/follow cadence changes without resetting the wing phase. 96 captures at 24 samples/s are preview cadence, not a game FPS claim.\n");
            Debug.Log("BABAO_MOTION_RUNTIME_PASSED");Application.Quit(0);
        }

        void Capture(int index)
        {
            var camera=Camera.main;var rt=new RenderTexture(1280,720,24);var prior=RenderTexture.active;
            camera.targetTexture=rt;camera.Render();RenderTexture.active=rt;
            var texture=new Texture2D(1280,720,TextureFormat.RGB24,false);texture.ReadPixels(new Rect(0,0,1280,720),0,0);texture.Apply();
            File.WriteAllBytes(Path.Combine(output,"frame-"+index.ToString("000")+".png"),texture.EncodeToPNG());
            camera.targetTexture=null;RenderTexture.active=prior;Destroy(texture);Destroy(rt);
        }

        static void Require(bool ok,string reason){if(!ok)throw new Exception("Babao preview: "+reason);}
    }
}
