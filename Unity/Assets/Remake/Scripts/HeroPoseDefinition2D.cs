using System;
using UnityEngine;

namespace MonsterRemake2D
{
    [Serializable]
    public class HandSocket2D
    {
        public Vector2 position;
        public float rotation;
        public bool mirrorX;
        public int sortingOffset = -1;
    }

    [Serializable]
    public class HeroPoseView2D
    {
        public HeroView2D view;
        public Sprite body;
        public HandSocket2D mainHand = new HandSocket2D();
        public HandSocket2D offHand = new HandSocket2D();
        public Sprite foregroundHands;
    }

    [CreateAssetMenu(menuName = "Monster Remake/2D hero pose")]
    public class HeroPoseDefinition2D : ScriptableObject
    {
        public string poseKey;
        public bool artApproved;
        public HeroPoseView2D[] views = Array.Empty<HeroPoseView2D>();

        public HeroPoseView2D Find(HeroView2D view)
        {
            foreach (var entry in views)
                if (entry.view == view) return entry;
            return null;
        }
    }
}
