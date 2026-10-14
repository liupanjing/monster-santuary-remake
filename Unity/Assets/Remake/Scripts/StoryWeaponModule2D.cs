using System;
using System.Linq;
using UnityEngine;

namespace MonsterRemake2D
{
    [Serializable]
    public class StoryWeaponPose2D
    {
        public string action;
        public Sprite[] frames;
    }

    // Each equipment item owns its images for the body's authored pose slots.
    // Registration keeps the drawn perspective and contact with the fingers.
    public class StoryWeaponModule2D : ScriptableObject
    {
        public string equipmentId;
        public bool humanApproved;
        public StoryWeaponPose2D[] poses;

        public Sprite Frame(string action, int index)
        {
            var pose = poses?.FirstOrDefault(p => p.action == action);
            return pose != null && index >= 0 && index < pose.frames.Length
                ? pose.frames[index] : null;
        }
    }
}
