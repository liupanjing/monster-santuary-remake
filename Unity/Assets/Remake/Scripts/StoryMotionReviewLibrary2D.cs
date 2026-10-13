using System;
using System.Linq;
using UnityEngine;

namespace MonsterRemake2D
{
    [Serializable] public class ReviewGrip2D { public float x,y,angle; }
    [Serializable] public class ReviewMovie2D
    {
        public string key;
        public Sprite[] frames,hands;
        public ReviewGrip2D[] grips;
        public int[] loop;
        public float fps;
        public float[] durations;
        public int Index(float time,bool repeat=true)
        {
            time=Mathf.Max(0,time);
            if(durations!=null&&durations.Length==frames.Length){
                float total=durations.Sum();
                float remaining=repeat?Mathf.Repeat(time,total):Mathf.Min(time,total-.0001f);
                for(int i=0;i<durations.Length;i++){if(remaining<durations[i])return i;remaining-=durations[i];}
                return frames.Length-1;
            }
            int phase=Mathf.FloorToInt(time*fps);
            return loop[repeat?phase%loop.Length:Mathf.Min(phase,loop.Length-1)];
        }
        public int Phase(float phase)=>loop[Mathf.Max(0,Mathf.FloorToInt(phase))%loop.Length];
    }
    [Serializable] public class ReviewCharacter2D
    {
        public string id;
        public Sprite portrait,originalField;
        public ReviewMovie2D[] actions;
        public ReviewMovie2D Find(string key)
        {
            var action=actions.FirstOrDefault(a=>a.key==key);
            if(action==null)throw new InvalidOperationException(id+" review action missing: "+key);
            return action;
        }
    }
    public class StoryMotionReviewLibrary2D : ScriptableObject
    {
        public bool humanApproved;
        public ReviewCharacter2D[] actors;
        public Sprite sword;
        public ReviewCharacter2D Actor(string id)=>actors.First(a=>a.id==id);
    }
}
