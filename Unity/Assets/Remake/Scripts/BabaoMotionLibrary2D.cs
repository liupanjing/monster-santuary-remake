using System;
using UnityEngine;

namespace MonsterRemake2D
{
    [Serializable]
    public class BabaoDirection2D
    {
        public int direction;
        public Sprite[] frames;
    }

    [CreateAssetMenu(menuName="Remake 2D/Babao motion")]
    public class BabaoMotionLibrary2D : ScriptableObject
    {
        public string approvedDesignVersion="v2";
        public bool designApproved=true;
        public bool motionApproved;
        public BabaoDirection2D[] directions;
        public int[] loop={0,1,2,3,2,1};
        public float idleRate=8,followRate=12,hoverHeight=.46f,bobAmplitude=.045f,followDistance=1.45f;

        public Sprite Frame(int direction,int index)
        {
            foreach(var entry in directions)
                if(entry.direction==direction)return entry.frames[loop[index%loop.Length]];
            throw new InvalidOperationException("Missing Babao direction "+direction);
        }
    }
}
