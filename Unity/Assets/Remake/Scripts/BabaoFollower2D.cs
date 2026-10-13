using System;
using System.Collections.Generic;
using UnityEngine;

namespace MonsterRemake2D
{
    // Fixed story companion. Never reads inventory, activePet, or monster artwork.
    public class BabaoFollower2D : MonoBehaviour
    {
        struct Point
        {
            public Vector3 position;
            public int plane;
            public Point(Vector3 position,int plane){this.position=position;this.plane=plane;}
        }

        public BabaoMotionLibrary2D library;
        public SpriteRenderer visual,shadow;
        readonly List<Point> trail=new List<Point>();
        Vector3 lastLeader;
        float phase;
        Texture2D shadowTexture;
        Sprite shadowSprite;
        public int Direction {get;private set;}
        public int Plane {get;private set;}
        public bool Following {get;private set;}
        public Vector3 GroundPosition=>transform.position;
        public float TrailLength {get;private set;}

        public static Vector3 Facing(int direction)=>direction==1?Vector3.left:direction==2?Vector3.right:direction==3?Vector3.up:Vector3.down;

        public void Initialize(BabaoMotionLibrary2D definition,Vector3 leader,Vector3 spawn,int direction,int plane)
        {
            if(definition==null||!definition.designApproved)throw new InvalidOperationException("Babao approved design missing");
            library=definition;
            if(visual==null)visual=Child("八宝 · 悬浮扇翅");
            if(shadow==null)shadow=Child("八宝 · 地面影子");
            if(shadow.sprite==null)CreateShadow();
            ResetTrail(leader,spawn,direction,plane);
        }

        SpriteRenderer Child(string childName)
        {
            var child=transform.Find(childName);
            if(child==null){child=new GameObject(childName).transform;child.SetParent(transform,false);}
            var renderer=child.GetComponent<SpriteRenderer>();
            return renderer==null?child.gameObject.AddComponent<SpriteRenderer>():renderer;
        }

        void CreateShadow()
        {
            shadowTexture=new Texture2D(32,16,TextureFormat.RGBA32,false);
            shadowTexture.filterMode=FilterMode.Bilinear;
            for(int y=0;y<16;y++)for(int x=0;x<32;x++){
                float r=Mathf.Pow((x-15.5f)/15.5f,2)+Mathf.Pow((y-7.5f)/7.5f,2);
                shadowTexture.SetPixel(x,y,new Color(.06f,.055f,.08f,Mathf.Clamp01(1-r)*.24f));
            }
            shadowTexture.Apply();shadowSprite=Sprite.Create(shadowTexture,new Rect(0,0,32,16),new Vector2(.5f,.5f),64);
            shadow.sprite=shadowSprite;shadow.transform.localPosition=new Vector3(0,.01f,0);
        }

        public void ResetTrail(Vector3 leader,Vector3 spawn,int direction,int plane)
        {
            trail.Clear();trail.Add(new Point(spawn,plane));
            if(Vector3.Distance(spawn,leader)>.0001f)trail.Add(new Point(leader,plane));
            transform.position=spawn;lastLeader=leader;Direction=direction;Plane=plane;phase=0;Following=false;
            TrailLength=Vector3.Distance(spawn,leader);Sample(direction,false,0,0);
        }

        public void Advance(Vector3 leader,int leaderPlane,bool running,float deltaTime,float time)
        {
            if(library==null)return;
            float distance=Vector3.Distance(lastLeader,leader);
            if(distance>4){ResetTrail(leader,leader,Direction,leaderPlane);return;}
            if(distance>.0001f){trail.Add(new Point(leader,leaderPlane));TrailLength+=distance;}
            else if(trail.Count>0){var last=trail[trail.Count-1];last.plane=leaderPlane;trail[trail.Count-1]=last;}
            lastLeader=leader;
            float excess=Mathf.Max(0,TrailLength-library.followDistance);
            float budget=Mathf.Min(excess,distance+Mathf.Max(0,deltaTime)*3);
            Following=budget>.0001f;
            while(budget>.00001f&&trail.Count>1){
                var a=trail[0];var b=trail[1];var delta=b.position-a.position;float length=delta.magnitude;
                if(length<.00001f){Plane=b.plane;trail.RemoveAt(0);continue;}
                float step=Mathf.Min(budget,length);var unit=delta/length;
                Direction=Mathf.Abs(unit.x)>Mathf.Abs(unit.y)?(unit.x<0?1:2):(unit.y>0?3:0);
                a.position+=unit*step;Plane=a.plane;transform.position=a.position;
                budget-=step;TrailLength=Mathf.Max(0,TrailLength-step);
                if(step>=length-.00001f){Plane=b.plane;trail.RemoveAt(0);}
                else trail[0]=a;
            }
            phase+=Mathf.Max(0,deltaTime)*(Following?library.followRate*(running?1.15f:1):library.idleRate);
            Sample(Direction,Following,phase,time);
        }

        // Explicit sampling is also used by the actual Unity preview capture.
        public void Sample(int direction,bool following,float framePhase,float time)
        {
            Direction=direction;Following=following;
            visual.sprite=library.Frame(direction,Mathf.Max(0,Mathf.FloorToInt(framePhase)));
            float bob=Mathf.Sin(time*Mathf.PI*2*.9f)*library.bobAmplitude;
            visual.transform.localPosition=new Vector3(0,library.hoverHeight+bob,0);
            visual.sortingOrder=(Plane==1?180:80)-4;
            shadow.sortingOrder=(Plane==1?180:80)-6;
        }

        void OnDestroy()
        {
            if(Application.isPlaying){
                if(shadowSprite!=null)Destroy(shadowSprite);
                if(shadowTexture!=null)Destroy(shadowTexture);
            }else{
                if(shadowSprite!=null)DestroyImmediate(shadowSprite);
                if(shadowTexture!=null)DestroyImmediate(shadowTexture);
            }
        }
    }
}
