using System;
using UnityEngine;

namespace MonsterRemake2D
{
    public enum WeaponFamily2D { Firearm, Slingshot, MagicRanged, Crossbow, Whip }
    public enum GripLayout2D { Unspecified, DualOneHand, SingleOneHand, TwoHand }
    public enum HeroView2D { BattleFront, BattleBack, FieldDown, FieldUp, FieldLeft, FieldRight }

    [Serializable]
    public class WeaponView2D
    {
        public HeroView2D view;
        public Sprite sprite;
        [Tooltip("Weapon-local muzzle coordinates measured from the sprite's bottom-left.")]
        public Vector2 muzzleUV = new Vector2(.96f, .78f);
        public float scale = .75f;
    }

    [CreateAssetMenu(menuName = "Monster Remake/2D weapon")]
    public class WeaponDefinition2D : ScriptableObject
    {
        public int originalItemId = -1;
        public string displayName, originalDescription;
        public WeaponFamily2D family;
        public GripLayout2D gripLayout;
        public string requiredPoseKey;
        public bool previewOnly, artApproved, showInField;
        public WeaponView2D[] views = Array.Empty<WeaponView2D>();

        public WeaponView2D Find(HeroView2D view)
        {
            foreach (var entry in views)
                if (entry.view == view) return entry;
            return null;
        }
    }
}
