using System;
using System.IO;
using System.Linq;
using System.Collections;
using UnityEngine;

namespace MonsterRemake2D
{
    public class StoryMotionReviewRunner : MonoBehaviour
    {
        public StoryMotionReviewLibrary2D library;
        StoryMotionReviewActor2D[] xiya,qibei;
        SpriteRenderer portrait,originalField;
        StoryReviewTrail2D trail=new StoryReviewTrail2D();
        readonly string[] directions={"down","left","right","up"};
        bool recording,paused,sword=true;
        int mode=0;
        float clock,phase;
        string output;
        Font font;
        string[] labels={"西雅 · 战斗待机 / 斩击 / 受击","西雅 · Q 版四方向待机 / 行走","七贝 · 战斗立绘悬浮 / 扇翅","七贝 · Q 版四方向悬浮 / 扇翅","西雅与七贝 · 沿路径跟随"};

        void Start()
        {
            recording=Environment.GetCommandLineArgs().Contains("--story-motion-record");
            const string option="--story-motion-output=";
            output=Environment.GetCommandLineArgs().FirstOrDefault(a=>a.StartsWith(option))?.Substring(option.Length)??Application.persistentDataPath;
            xiya=Enumerable.Range(0,4).Select(i=>Create("xiya",i)).ToArray();qibei=Enumerable.Range(0,4).Select(i=>Create("qibei",i)).ToArray();
            portrait=new GameObject("Original portrait evidence").AddComponent<SpriteRenderer>();
            originalField=new GameObject("Original field evidence").AddComponent<SpriteRenderer>();
            portrait.sortingOrder=0;originalField.sortingOrder=0;
            font=Font.CreateDynamicFontFromOSFont(new[]{"PingFang SC","Microsoft YaHei","Arial Unicode MS"},18);
            Reset();
            if(recording){Directory.CreateDirectory(output);StartCoroutine(Record());}
        }
        StoryMotionReviewActor2D Create(string id,int i)
        {
            var prefab=Resources.Load<GameObject>("StoryMotionReview/Prefabs/"+id);
            var actor=Instantiate(prefab).GetComponent<StoryMotionReviewActor2D>();actor.name=id+" review "+i;actor.Initialize(library,id);return actor;
        }
        void Reset(){clock=0;phase=0;trail.Reset(new Vector3(-4.95f,-1.8f),new Vector3(-3.5f,-1.8f));}
        void Update()
        {
            if(recording)return;
            for(int i=0;i<5;i++)if(Input.GetKeyDown((KeyCode)((int)KeyCode.Alpha1+i))){mode=i;Reset();}
            if(Input.GetKeyDown(KeyCode.Space))paused=!paused;
            if(Input.GetKeyDown(KeyCode.RightArrow)&&paused)clock=Mathf.Min(3.9375f,clock+1/16f);
            if(Input.GetKeyDown(KeyCode.LeftArrow)&&paused){clock=Mathf.Max(0,clock-1/16f);ResetTrailForClock();}
            if(!paused){float old=clock;clock+=Time.deltaTime;if(clock>=4){Reset();}else if(mode==4&&clock<old)Reset();}
            Sample(mode,clock,paused?0:Time.deltaTime);
        }
        void ResetTrailForClock()
        {
            float target=clock;Reset();for(float t=0;t<=target;t+=1/64f)Sample(4,t,1/64f);clock=target;
        }
        public void Sample(int view,float time,float dt)
        {
            Camera.main.orthographicSize=view==0&&library.weaponModule!=null?4.4f:3.9f;
            foreach(var a in xiya)a.gameObject.SetActive(false);foreach(var a in qibei)a.gameObject.SetActive(false);
            bool evidence=view<4;
            portrait.enabled=evidence;originalField.enabled=evidence;
            var owner=library.Actor(view<2?"xiya":"qibei");
            portrait.sprite=owner.portrait;originalField.sprite=owner.originalField;
            portrait.transform.position=new Vector3(-4.4f,1.0f,0);portrait.transform.localScale=Vector3.one*5;
            originalField.transform.position=new Vector3(-4.4f,-1.4f,0);originalField.transform.localScale=Vector3.one*4;
            if(view==0){
                var a=xiya[0];a.gameObject.SetActive(true);a.transform.position=new Vector3(.8f,-3.0f,0);a.showSword=sword;
                if(time<1.2f)a.Sample("battle-idle",time);
                else if(time<2.05f)a.Sample("battle-slash",time-1.2f,false);
                else if(time<2.45f)a.Sample("battle-hurt",time-2.05f,false);
                else a.Sample("battle-idle",time-2.45f);
                if(a.equippedWeapon!=null){
                    Require(!sword||a.weapon.sprite==a.equippedWeapon.Frame(a.Action,a.FrameIndex),"Authored equipment pose selected");
                    Require(!a.weapon.enabled||a.weapon.transform.localPosition==a.body.transform.localPosition&&a.weapon.transform.localRotation==Quaternion.identity,"Sword keeps authored contact and perspective");
                }else Require(!a.weapon.enabled||Vector3.Distance(a.weapon.transform.localPosition,new Vector3(owner.Find(a.Action).grips[a.FrameIndex].x,owner.Find(a.Action).grips[a.FrameIndex].y,0))<.001f,"Sword attached to same right-hand grip");
            }else if(view==1){
                for(int d=0;d<4;d++){
                    var a=xiya[d];a.gameObject.SetActive(true);a.transform.position=new Vector3(-1.9f+d*2.25f,-1.1f,0);
                    bool walk=time>=.5f&&time<3.5f;a.Sample("field-"+(walk?"walk-":"idle-")+directions[d],Mathf.Max(0,time-.5f));
                }
            }else if(view==2){
                var a=qibei[0];a.gameObject.SetActive(true);a.transform.position=new Vector3(1,.1f,0);a.Sample("battle-hover",time);
            }else if(view==3){
                for(int d=0;d<4;d++){
                    var a=qibei[d];a.gameObject.SetActive(true);a.transform.position=new Vector3(-1.9f+d*2.25f,-.5f,0);a.Sample("field-flap-"+directions[d],time);
                }
            }else{
                var leader=xiya[0];var follower=qibei[0];leader.gameObject.SetActive(true);follower.gameObject.SetActive(true);
                Vector3 p;int dir;
                if(time<1){p=new Vector3(-3.5f+time*4.5f,-1.8f,0);dir=2;}
                else if(time<2){p=new Vector3(1,-1.8f+(time-1)*2.2f,0);dir=3;}
                else if(time<3){p=new Vector3(1-(time-2)*4.5f,.4f,0);dir=1;}
                else {p=new Vector3(-3.5f,.4f-(time-3)*2.2f,0);dir=0;}
                leader.transform.position=p;leader.Sample("field-walk-"+directions[dir],time);
                trail.Advance(p);follower.transform.position=trail.Position;phase+=Mathf.Max(0,dt)*12;
                follower.Sample("field-flap-"+directions[trail.Direction],time,true,phase);
                Require(Vector3.Distance(p,trail.Position)<=StoryReviewTrail2D.Gap+.01f,"Follower bounded spacing");
                bool route=Mathf.Abs(trail.Position.y+1.8f)<.001f||Mathf.Abs(trail.Position.x-1)<.001f||Mathf.Abs(trail.Position.y-.4f)<.001f||Mathf.Abs(trail.Position.x+3.5f)<.001f;
                Require(route,"Follower follows traversed corners, no diagonal cut");
            }
        }
        IEnumerator Record()
        {
            yield return null;
            for(int m=0;m<5;m++){
                mode=m;Reset();string dir=Path.Combine(output,"mode-"+m);Directory.CreateDirectory(dir);
                for(int i=0;i<64;i++){Sample(m,i/16f,1/16f);yield return new WaitForEndOfFrame();Capture(dir,i);yield return null;}
            }
            if(library.weaponModule!=null){
                foreach(var actor in xiya)actor.gameObject.SetActive(false);
                foreach(var actor in qibei)actor.gameObject.SetActive(false);
                portrait.enabled=false;originalField.enabled=false;
                var a=xiya[0];a.gameObject.SetActive(true);a.transform.position=new Vector3(0,-3,0);
                var camera=Camera.main;camera.orthographicSize=4.4f;var savedPosition=camera.transform.position;float savedSize=camera.orthographicSize;
                var masterMaterial=new Material(Shader.Find("Sprites/Default"));
                foreach(var movie in library.Actor("xiya").actions.Where(m=>m.weaponFrames.Length>0)){
                    for(int i=0;i<movie.frames.Length;i++){
                        float t=movie.durations!=null&&movie.durations.Length==movie.frames.Length?movie.durations.Take(i).Sum()+.0001f:(i+.01f)/movie.fps;
                        a.showSword=true;a.Sample(movie.key,t,false);
                        Require(a.FrameIndex==i&&a.weapon.sprite==movie.weaponFrames[i],"Every authored sword pose slot: "+movie.key+" frame "+i+" actual "+a.FrameIndex+"");
                        string dir=Path.Combine(output,"poses",movie.key);Directory.CreateDirectory(dir);Capture(dir,i);
                        var grip=movie.grips[i];camera.transform.position=a.transform.position+new Vector3(grip.x,grip.y,-10);camera.orthographicSize=.75f;
                        dir=Path.Combine(output,"grips",movie.key);Directory.CreateDirectory(dir);Capture(dir,i);
                        // Compare the module's rendered contact to its complete with-sword master.
                        a.body.sprite=movie.masterFrames[i];a.body.sharedMaterial=masterMaterial;a.body.SetPropertyBlock(null);a.weapon.enabled=false;a.hand.enabled=false;
                        dir=Path.Combine(output,"master-grips",movie.key);Directory.CreateDirectory(dir);Capture(dir,i);
                        camera.transform.position=savedPosition;camera.orthographicSize=savedSize;
                        a.showSword=false;a.Sample(movie.key,t,false);
                        Require(!a.weapon.enabled&&a.body.sprite==movie.frames[i]&&a.hand.sprite==movie.hands[i],"Hide equipment without changing body/finger poses");
                        dir=Path.Combine(output,"without-sword",movie.key);Directory.CreateDirectory(dir);Capture(dir,i);
                    }
                }
                a.showSword=true;Destroy(masterMaterial);
            }
            File.WriteAllText(Path.Combine(output,"runtime-validation.txt"),"PASS: actual Unity player renders independent Xiya battle and Q field forms, authored modular sword through nine unique idle/slash/hurt drawings and eleven pose slots; every slot recorded full-body, grip, master grip and with weapon hidden, four field directions with six walking drawings each, Qibei battle and Q face palettes, four wing directions (right mirrored), and Qibei follows Xiya around three corners on the actual leader path. 5 modes x 64 captures at 16 samples/s; not a claim of 16 new drawings per second, gameplay integration or human art approval.\n");
            Debug.Log("STORY_MOTION_REVIEW_RUNTIME_PASSED");Application.Quit(0);
        }
        void Capture(string dir,int index)
        {
            var camera=Camera.main;var rt=new RenderTexture(1280,720,24);var prior=RenderTexture.active;
            camera.targetTexture=rt;camera.Render();RenderTexture.active=rt;
            var tex=new Texture2D(1280,720,TextureFormat.RGB24,false);tex.ReadPixels(new Rect(0,0,1280,720),0,0);tex.Apply();
            File.WriteAllBytes(Path.Combine(dir,"frame-"+index.ToString("000")+".png"),tex.EncodeToPNG());camera.targetTexture=null;RenderTexture.active=prior;Destroy(tex);Destroy(rt);
        }
        void OnGUI()
        {
            if(recording)return;
            var style=new GUIStyle(GUI.skin.button){font=font,fontSize=16};
            for(int i=0;i<labels.Length;i++)if(GUI.Button(new Rect(12+i*210,12,205,42),labels[i],style)){mode=i;Reset();}
            if(GUI.Button(new Rect(12,65,130,34),paused?"继续播放":"暂停",style))paused=!paused;
            if(GUI.Button(new Rect(150,65,130,34),"下一帧",style)){paused=true;clock=Mathf.Min(3.9375f,clock+1/16f);}
            if(GUI.Button(new Rect(290,65,155,34),sword?"隐藏剑看身体":"显示独立剑",style))sword=!sword;
            GUI.Label(new Rect(12,105,1000,34),"按 1–5 切换；空格暂停；左右键逐帧。造型与动作候选，待逐角色评价。",new GUIStyle(GUI.skin.label){font=font,fontSize=16});
        }
        static void Require(bool ok,string reason){if(!ok)throw new Exception("Story motion review: "+reason);}
    }
}
