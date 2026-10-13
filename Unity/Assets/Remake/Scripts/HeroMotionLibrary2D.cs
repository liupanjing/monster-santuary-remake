using System;
using UnityEngine;

namespace MonsterRemake2D
{
    public enum HeroBattleAction2D { Idle, Shoot, Hurt, Guard, Victory }
    [Serializable] public class FieldMotion2D {
        public HeroView2D view;
        public Sprite idle;
        public Sprite[] walk, run;
        public float walkFPS = 8, runFPS = 12;
    }
    [Serializable] public class HeroMotionFrame2D {
        public HeroPoseView2D pose;
        public float duration = .1f;
        public bool fireMain, fireOff;
    }
    [Serializable] public class BattleMotion2D {
        public HeroBattleAction2D action;
        public HeroView2D view;
        public bool loop;
        public HeroMotionFrame2D[] frames;
        public float Duration { get { float total=0; foreach(var f in frames) total+=f.duration; return total; } }
        public int FrameAt(float time) {
            float t=loop ? Mathf.Repeat(time, Duration) : Mathf.Clamp(time, 0, Mathf.Max(0,Duration-.00001f));
            for(int i=0;i<frames.Length;i++){t-=frames[i].duration;if(t<0)return i;}
            return frames.Length-1;
        }
    }
    [CreateAssetMenu(menuName="Monster Remake/Hero motion library")]
    public class HeroMotionLibrary2D : ScriptableObject {
        public HeroPoseDefinition2D fieldPose, battlePose;
        public WeaponDefinition2D defaultWeapon;
        public bool newFramesApproved;
        public FieldMotion2D[] field;
        public BattleMotion2D[] battle;
        public FieldMotion2D Field(HeroView2D view) { foreach(var m in field)if(m.view==view)return m;throw new ArgumentException("Missing field view "+view); }
        public BattleMotion2D Battle(HeroBattleAction2D action, HeroView2D view) {foreach(var m in battle)if(m.action==action&&m.view==view)return m;throw new ArgumentException("Missing battle action/view");}
        public static HeroView2D FieldView(int direction) => direction==0?HeroView2D.FieldDown:direction==1?HeroView2D.FieldLeft:direction==2?HeroView2D.FieldRight:HeroView2D.FieldUp;
    }
}
