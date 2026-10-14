using System;
using System.Collections.Generic;
using UnityEngine;

namespace MonsterRemake2D
{
    // Candidate-only player. No references to party selection, quests or saved games.
    public class StoryMotionReviewActor2D : MonoBehaviour
    {
        public StoryMotionReviewLibrary2D library;
        public string characterId;
        public SpriteRenderer body,weapon,hand,shadow;
        public bool showSword=true;
        public StoryWeaponModule2D equippedWeapon;
        public int FrameIndex {get;private set;}
        public string Action {get;private set;}
        Material defaultMaterial,compositeMaterial;
        MaterialPropertyBlock compositeProperties;
        Texture2D shadowTexture;
        Sprite shadowSprite;

        public void Initialize(StoryMotionReviewLibrary2D definition,string id)
        {
            library=definition;characterId=id;
            if(characterId=="xiya" && equippedWeapon==null)equippedWeapon=library.weaponModule;
            body=Layer("Body",10);weapon=Layer("Independent sword",11);hand=Layer("Foreground right hand",12);shadow=Layer("Ground shadow",8);
            if(defaultMaterial==null)defaultMaterial=body.sharedMaterial;
            if(shadow.sprite==null)CreateShadow();
        }
        SpriteRenderer Layer(string name,int order)
        {
            var t=transform.Find(name);
            if(t==null){t=new GameObject(name).transform;t.SetParent(transform,false);}
            var r=t.GetComponent<SpriteRenderer>();
            if(r==null)r=t.gameObject.AddComponent<SpriteRenderer>();
            r.sortingOrder=order;return r;
        }
        void CreateShadow()
        {
            shadowTexture=new Texture2D(32,16,TextureFormat.RGBA32,false);shadowTexture.filterMode=FilterMode.Bilinear;
            for(int y=0;y<16;y++)for(int x=0;x<32;x++){
                float q=Mathf.Pow((x-15.5f)/15.5f,2)+Mathf.Pow((y-7.5f)/7.5f,2);
                shadowTexture.SetPixel(x,y,new Color(.15f,.1f,.13f,Mathf.Clamp01(1-q)*.17f));
            }
            shadowTexture.Apply();shadowSprite=Sprite.Create(shadowTexture,new Rect(0,0,32,16),new Vector2(.5f,.5f),64);
            shadow.sprite=shadowSprite;
        }
        public void Sample(string action,float time,bool repeat=true,float phase=-1)
        {
            var movie=library.Actor(characterId).Find(action);
            int index=phase<0?movie.Index(time,repeat):movie.Phase(phase);
            Action=action;FrameIndex=index;body.sprite=movie.frames[index];
            bool battle=action.StartsWith("battle-");
            bool floating=characterId=="qibei";
            body.transform.localPosition=new Vector3(0,floating?(battle?0:.46f)+Mathf.Sin(time*Mathf.PI*2)*.045f:0,0);
            hand.transform.localPosition=body.transform.localPosition;
            hand.enabled=movie.hands!=null&&movie.hands.Length>index;
            if(hand.enabled)hand.sprite=movie.hands[index];
            var authoredWeapon=equippedWeapon!=null?equippedWeapon.Frame(action,index):null;
            weapon.enabled=showSword&&characterId=="xiya"&&battle&&(authoredWeapon!=null||library.sword!=null);
            if(weapon.enabled){
                if(equippedWeapon!=null){
                    // Missing pose slots are explicit; do not rotate an unrelated sword image.
                    weapon.enabled=authoredWeapon!=null;
                    weapon.sprite=authoredWeapon;
                    weapon.transform.localPosition=body.transform.localPosition;
                    weapon.transform.localRotation=Quaternion.identity;
                }else{
                    var grip=movie.grips[index];weapon.sprite=library.sword;
                    weapon.transform.localPosition=new Vector3(grip.x,grip.y,0);
                    weapon.transform.localRotation=Quaternion.Euler(0,0,grip.angle);
                }
            }
            bool compose=battle&&equippedWeapon!=null&&hand.enabled;
            weapon.forceRenderingOff=compose;hand.forceRenderingOff=compose;
            if(compose){
                if(compositeMaterial==null){
                    var shader=Resources.Load<Shader>("StoryMotionReview/LayerComposite");
                    if(shader==null)throw new InvalidOperationException("Review composite shader missing");
                    compositeMaterial=new Material(shader);compositeProperties=new MaterialPropertyBlock();
                }
                body.sharedMaterial=compositeMaterial;
                compositeProperties.SetTexture("_WeaponTex",authoredWeapon!=null?authoredWeapon.texture:Texture2D.blackTexture);
                compositeProperties.SetTexture("_FingerTex",hand.sprite.texture);
                compositeProperties.SetFloat("_ShowWeapon",showSword&&authoredWeapon!=null?1:0);
                body.SetPropertyBlock(compositeProperties);
            }else if(compositeMaterial!=null){body.sharedMaterial=defaultMaterial;body.SetPropertyBlock(null);}
            shadow.enabled=!battle;
            int order=battle?10:100-Mathf.RoundToInt(transform.position.y*20);
            body.sortingOrder=order;weapon.sortingOrder=order+1;hand.sortingOrder=order+2;shadow.sortingOrder=order-2;
        }
        public void SetWeaponModule(StoryWeaponModule2D module){equippedWeapon=module;}
        void OnDestroy()
        {
            if(compositeMaterial!=null){if(Application.isPlaying)Destroy(compositeMaterial);else DestroyImmediate(compositeMaterial);}
            if(Application.isPlaying){if(shadowSprite!=null)Destroy(shadowSprite);if(shadowTexture!=null)Destroy(shadowTexture);}
            else {if(shadowSprite!=null)DestroyImmediate(shadowSprite);if(shadowTexture!=null)DestroyImmediate(shadowTexture);}
        }
    }

    // Same arc-length trail principle as the fixed Babao follower, with no approval
    // or gameplay dependencies. This permits visual review before art acceptance.
    public class StoryReviewTrail2D
    {
        readonly List<Vector3> points=new List<Vector3>();
        Vector3 leader;
        public Vector3 Position {get;private set;}
        public int Direction {get;private set;}
        public float Length {get;private set;}
        public const float Gap=1.45f;
        public void Reset(Vector3 start,Vector3 next)
        {
            points.Clear();points.Add(start);points.Add(next);Position=start;leader=next;
            Length=Vector3.Distance(start,next);Direction=2;
        }
        public void Advance(Vector3 next)
        {
            float distance=Vector3.Distance(leader,next);
            if(distance>4){Reset(next,next);return;}
            if(distance>.00001f){points.Add(next);Length+=distance;}
            leader=next;float budget=Mathf.Max(0,Length-Gap);
            while(budget>.00001f&&points.Count>1){
                var a=points[0];var b=points[1];var delta=b-a;float length=delta.magnitude;
                if(length<.00001f){points.RemoveAt(0);continue;}
                float step=Mathf.Min(length,budget);var unit=delta/length;
                Direction=Mathf.Abs(unit.x)>Mathf.Abs(unit.y)?(unit.x<0?1:2):(unit.y>0?3:0);
                Position=a+unit*step;Length=Mathf.Max(0,Length-step);budget-=step;
                if(step>=length-.00001f)points.RemoveAt(0);else points[0]=Position;
            }
        }
    }
}
