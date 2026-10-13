using System;
using System.Collections;
using UnityEngine;

namespace MonsterRemake2D
{
    [RequireComponent(typeof(HeroEquipmentRig2D))]
    public class HeroMotionPlayer2D : MonoBehaviour
    {
        public HeroMotionLibrary2D library;
        public HeroEquipmentRig2D rig;
        public bool holdGuard;
        public bool automaticIdle = true;
        public bool Busy {get;private set;}
        public int ShotsEmitted {get;private set;}
        public HeroBattleAction2D Action {get;private set;}
        float clock;
        int lastEventFrame=-1;
        Material effectMaterial;

        public void Initialize(HeroMotionLibrary2D motions, WeaponDefinition2D weapon, HeroView2D view, bool draft)
        {
            library=motions;rig=GetComponent<HeroEquipmentRig2D>();rig.allowDraftArt=draft;
            bool field=(int)view>=(int)HeroView2D.FieldDown;
            if(!rig.TryEquip(field?motions.fieldPose:motions.battlePose,weapon,view,out string reason))throw new Exception(reason);
            Action=HeroBattleAction2D.Idle;
        }

        void Update()
        {
            if(!automaticIdle||library==null||rig==null||Busy||(int)rig.view>=(int)HeroView2D.FieldDown)return;
            clock+=Time.deltaTime;
            SampleBattle(holdGuard?HeroBattleAction2D.Guard:HeroBattleAction2D.Idle,clock);
        }

        public void SampleField(int direction,bool moving,bool running,float time)
        {
            var view=HeroMotionLibrary2D.FieldView(direction);var motion=library.Field(view);
            var baseFrame=library.fieldPose.Find(view);
            Sprite body=moving?(running?motion.run:motion.walk)[(int)(time*(running?motion.runFPS:motion.walkFPS))%4]:motion.idle;
            var frame=new HeroPoseView2D { view=view,body=body,mainHand=baseFrame.mainHand,offHand=baseFrame.offHand };
            if(!rig.TryApplyFrame(frame,library.newFramesApproved,out string reason))throw new Exception(reason);
        }

        public int SampleBattle(HeroBattleAction2D action,float time)
        {
            clock=time;Action=action;var clip=library.Battle(action,rig.view);int index=clip.FrameAt(time);
            if(!rig.TryApplyFrame(clip.frames[index].pose, action==HeroBattleAction2D.Idle||library.newFramesApproved,out string reason))throw new Exception(reason);
            // Idle breath is a small transform of the approved body, not invented extra frames.
            rig.Breathe(action==HeroBattleAction2D.Idle?Mathf.Sin(time*Mathf.PI*1.4f)*.008f:0);
            return index;
        }

        public bool TryEquip(WeaponDefinition2D weapon,out string reason)
        {
            if(!rig.TryEquip(rig.pose,weapon,rig.view,out reason))return false;
            if((int)rig.view<(int)HeroView2D.FieldDown)SampleBattle(Action,clock);
            return true;
        }

        public IEnumerator Play(HeroBattleAction2D action,Transform target=null,System.Action impact=null)
        {
            Busy=true;clock=0;lastEventFrame=-1;
            var clip=library.Battle(action,rig.view);bool appliedImpact=false;
            int eventIndex=0;float eventStart=0;
            while(clock<clip.Duration)
            {
                int frame=SampleBattle(action,clock);
                while(eventIndex<clip.frames.Length&&clock>=eventStart)
                {
                    var pose=clip.frames[eventIndex];
                    if(pose.fireMain)EmitShot(rig.MainMuzzle,target);
                    if(pose.fireOff)EmitShot(rig.OffMuzzle,target);
                    if((pose.fireMain||pose.fireOff)&&!appliedImpact){impact?.Invoke();appliedImpact=true;}
                    eventStart+=pose.duration;eventIndex++;
                }
                clock+=Time.deltaTime;yield return null;
            }
            // Event delivery survives a long frame that skips a visual key pose.
            for(;eventIndex<clip.frames.Length;eventIndex++) {
                var f=clip.frames[eventIndex];if(f.fireMain)EmitShot(rig.MainMuzzle,target);if(f.fireOff)EmitShot(rig.OffMuzzle,target);
                if((f.fireMain||f.fireOff)&&!appliedImpact){impact?.Invoke();appliedImpact=true;}
            }
            if(!appliedImpact)impact?.Invoke();
            Busy=false;clock=0;rig.Tint(Color.white);
            SampleBattle(holdGuard?HeroBattleAction2D.Guard:HeroBattleAction2D.Idle,0);
        }

        public void PreviewEvents(HeroBattleAction2D action,float time,Transform target)
        {
            int frame=SampleBattle(action,time);
            if(frame!=lastEventFrame){var f=library.Battle(action,rig.view).frames[frame];if(f.fireMain)EmitShot(rig.MainMuzzle,target);if(f.fireOff)EmitShot(rig.OffMuzzle,target);lastEventFrame=frame;}
        }

        public void ResetPreviewEvents(){lastEventFrame=-1;}

        void EmitShot(Transform muzzle,Transform target)
        {
            if(muzzle==null||!muzzle.gameObject.activeInHierarchy)return;
            ShotsEmitted++;Vector3 end=target!=null?target.position+Vector3.up*.5f:muzzle.position+muzzle.right*3;
            StartCoroutine(ShotFX(muzzle.position,end));
        }

        IEnumerator ShotFX(Vector3 origin,Vector3 end)
        {
            if(effectMaterial==null)effectMaterial=new Material(Shader.Find("Sprites/Default"));
            var root=new GameObject("Independent muzzle flash and tracer");root.transform.SetParent(transform,false);
            var trail=root.AddComponent<LineRenderer>();trail.material=effectMaterial;trail.sortingOrder=200;trail.positionCount=2;trail.useWorldSpace=true;
            trail.SetPosition(0,origin);trail.SetPosition(1,end);trail.startWidth=.025f;trail.endWidth=.005f;
            var flash=new GameObject("Flash").AddComponent<LineRenderer>();flash.transform.SetParent(root.transform,false);flash.material=effectMaterial;
            flash.sortingOrder=201;flash.positionCount=5;flash.useWorldSpace=true;flash.startWidth=.04f;flash.endWidth=.02f;
            for(int i=0;i<5;i++)flash.SetPosition(i,origin+new Vector3(i%2==0?-.09f:.09f,(i-2)*.055f,0));
            float elapsed=0;while(elapsed<.12f){elapsed+=Time.deltaTime;float alpha=1-elapsed/.12f;trail.startColor=new Color(1,.86f,.35f,alpha);trail.endColor=new Color(1,.92f,.62f,alpha);flash.startColor=flash.endColor=new Color(1,.83f,.25f,alpha);yield return null;}
            Destroy(root);
        }

        void OnDestroy(){if(effectMaterial!=null)Destroy(effectMaterial);}
    }
}
