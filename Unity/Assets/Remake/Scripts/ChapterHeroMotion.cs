using System;
using UnityEngine;

namespace MonsterRemake2D
{
    public partial class EarthGame2D
    {
        HeroMotionLibrary2D heroMotions;
        HeroMotionPlayer2D fieldHeroMotion,battleHeroMotion;
        WeaponDefinition2D equippedVisual;
        void LoadHeroMotion()
        {
            heroMotions=Resources.Load<HeroMotionLibrary2D>("HeroMotion/Animations");
            if(heroMotions==null)throw new Exception("Hero animation library missing");
            equippedVisual=heroMotions.defaultWeapon;
        }

        HeroMotionPlayer2D NewHero(string name,Transform parent,Vector3 position,HeroView2D view,int order,float scale,bool mirror)
        {
            var root=new GameObject(name);root.transform.SetParent(parent,false);root.transform.position=position;
            root.transform.localScale=new Vector3(mirror?-scale:scale,scale,1);
            var player=root.AddComponent<HeroMotionPlayer2D>();player.Initialize(heroMotions,equippedVisual,view,true);
            player.rig.bodyOrder=order;player.rig.TryApplyFrame(player.rig.pose.Find(view),true,out _);
            return player;
        }

        void FieldFrame(bool moving,bool running)
        {
            if(fieldHeroMotion!=null)fieldHeroMotion.SampleField(direction,moving,running,Time.time);
        }

        void RestoreVisualEquipment()
        {
            if(state.visualWeaponId>=0&&TryGetWeapon(state.visualWeaponId,out var definition,out _))equippedVisual=definition;
        }

        bool TryGetWeapon(int id,out WeaponDefinition2D definition,out string reason)
        {
            definition=id<0?heroMotions.defaultWeapon:Resources.Load<WeaponDefinition2D>("Equipment/Weapons/Item"+id.ToString("000"));
            reason=null;
            if(definition==null){reason="未知武器。";return false;}
            if(definition.gripLayout==GripLayout2D.Unspecified||definition.requiredPoseKey!=heroMotions.battlePose.poseKey||definition.views==null||definition.Find(HeroView2D.BattleFront)?.sprite==null||definition.Find(HeroView2D.BattleBack)?.sprite==null||definition.Find(HeroView2D.BattleFront).scale<=0||definition.Find(HeroView2D.BattleBack).scale<=0){reason="该装备的图片或握持姿势尚未制作。";return false;}
            if(definition.showInField&&(definition.fieldPoseKey!=heroMotions.fieldPose.poseKey||heroMotions.field.AnyMissing(definition))){reason="该装备的跑图视角尚未制作。";return false;}
            return true;
        }

        // Presentation API for future inventory equipment selection. It does not
        // silently invent original weapon stats or add a second equipment slot.
        public bool TryEquipVisualWeapon(int originalItemId,out string reason)
        {
            if(!TryGetWeapon(originalItemId,out var next,out reason))return false;
            if(battleHeroMotion!=null&&mode==Mode.Battle&&!battleHeroMotion.TryEquip(next,out reason))return false;
            if(fieldHeroMotion!=null&&!fieldHeroMotion.TryEquip(next,out reason))return false;
            equippedVisual=next;state.visualWeaponId=originalItemId;Save();return true;
        }
    }

    static class HeroWeaponFieldValidation
    {
        public static bool AnyMissing(this FieldMotion2D[] fields,WeaponDefinition2D weapon)
        {
            foreach(var field in fields)if(weapon.Find(field.view)?.sprite==null||weapon.Find(field.view).scale<=0)return true;
            return false;
        }
    }
}
