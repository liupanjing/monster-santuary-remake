using UnityEngine;

namespace MonsterRemake2D
{
    // Body/hand frames and equipment are independent; no gun is baked into art.
    public class HeroEquipmentRig2D : MonoBehaviour
    {
        public HeroPoseDefinition2D pose;
        public WeaponDefinition2D weapon;
        public HeroView2D view;
        public bool allowDraftArt;
        public int bodyOrder = 10;
        public SpriteRenderer Body { get; private set; }
        public SpriteRenderer MainWeapon { get; private set; }
        public SpriteRenderer OffWeapon { get; private set; }
        public Transform MainMuzzle { get; private set; }
        public Transform OffMuzzle { get; private set; }
        SpriteRenderer foreground;

        void Start()
        {
            EnsureLayers();
            if (pose != null && !TryEquip(pose, weapon, view, out string reason))
                Debug.LogWarning(reason, this);
        }

        SpriteRenderer Layer(string name)
        {
            var existing = transform.Find(name);
            if (existing != null) return existing.GetComponent<SpriteRenderer>();
            var child = new GameObject(name);
            child.transform.SetParent(transform, false);
            return child.AddComponent<SpriteRenderer>();
        }

        void EnsureLayers()
        {
            if (Body != null) return;
            Body = Layer("Body (no weapons)");
            MainWeapon = Layer("Main weapon");
            OffWeapon = Layer("Off weapon");
            foreground = Layer("Optional hand occlusion");
            MainMuzzle = Muzzle(MainWeapon.transform);
            OffMuzzle = Muzzle(OffWeapon.transform);
        }

        static Transform Muzzle(Transform parent)
        {
            var result = parent.Find("Muzzle");
            if (result != null) return result;
            result = new GameObject("Muzzle").transform;
            result.SetParent(parent, false);
            return result;
        }

        public bool TryEquip(HeroPoseDefinition2D nextPose, WeaponDefinition2D nextWeapon,
            HeroView2D nextView, out string reason)
        {
            var frame = nextPose != null ? nextPose.Find(nextView) : null;
            return ApplyChecked(nextPose, nextWeapon, frame, nextPose != null && nextPose.artApproved, out reason);
        }

        public bool TryApplyFrame(HeroPoseView2D frame, bool frameApproved, out string reason)
        {
            return ApplyChecked(pose, weapon, frame, frameApproved, out reason);
        }

        bool ApplyChecked(HeroPoseDefinition2D nextPose, WeaponDefinition2D nextWeapon,
            HeroPoseView2D frame, bool approved, out string reason)
        {
            reason = null;
            if (nextPose == null || frame == null || frame.body == null) { reason = "Missing hero view/pose"; return false; }
            var nextView = frame.view;
            bool field = (int)nextView >= (int)HeroView2D.FieldDown;
            bool displayWeapon = nextWeapon != null && (!field || nextWeapon.showInField);
            if (!allowDraftArt && (!approved || displayWeapon && !nextWeapon.artApproved))
            { reason = "Art must be approved for production; drafts require the review rig"; return false; }
            WeaponView2D art = displayWeapon ? nextWeapon.Find(nextView) : null;
            if (displayWeapon && (art == null || art.sprite == null || art.scale <= 0))
            { reason = "Weapon view is not drawn yet"; return false; }
            if (displayWeapon && (nextWeapon.gripLayout == GripLayout2D.Unspecified ||
                (field ? nextWeapon.fieldPoseKey : nextWeapon.requiredPoseKey) != nextPose.poseKey))
            { reason = "Weapon requires a different hand/body pose"; return false; }
            if (displayWeapon && nextWeapon.previewOnly && !allowDraftArt)
            { reason = "Neutral preview weapon cannot stand in for an original item"; return false; }
            // All checks happen before changing the current body or equipped visuals.
            EnsureLayers();
            pose = nextPose; weapon = nextWeapon; view = nextView;
            Body.sprite = frame.body; Body.sortingOrder = bodyOrder;
            foreground.sprite = frame.foregroundHands; foreground.sortingOrder = bodyOrder + 2;
            Apply(MainWeapon, MainMuzzle, frame.mainHand, art);
            Apply(OffWeapon, OffMuzzle, frame.offHand,
                displayWeapon && nextWeapon.gripLayout == GripLayout2D.DualOneHand ? art : null);
            return true;
        }

        public void Tint(Color color)
        {
            EnsureLayers(); Body.color = color; MainWeapon.color = color;
            OffWeapon.color = color; foreground.color = color;
        }

        public void Breathe(float amount)
        {
            Body.transform.localScale=foreground.transform.localScale=new Vector3(1,1+amount,1);
            foreach(var renderer in new[]{MainWeapon,OffWeapon}) {
                var p=renderer.transform.localPosition;p.y*=1+amount;renderer.transform.localPosition=p;
            }
        }

        void Apply(SpriteRenderer renderer, Transform muzzle, HandSocket2D socket, WeaponView2D art)
        {
            bool enabled = art != null;
            renderer.gameObject.SetActive(enabled);
            if (!enabled) { renderer.sprite = null; return; }
            renderer.sprite = art.sprite;
            renderer.sortingOrder = bodyOrder + socket.sortingOffset;
            renderer.transform.localPosition = socket.position;
            renderer.transform.localRotation = Quaternion.Euler(0, 0, socket.rotation);
            renderer.transform.localScale = new Vector3(socket.mirrorX ? -art.scale : art.scale, art.scale, 1);
            Vector2 pixel = Vector2.Scale(art.muzzleUV, art.sprite.rect.size) - art.sprite.pivot;
            muzzle.localPosition = pixel / art.sprite.pixelsPerUnit;
        }
    }
}
