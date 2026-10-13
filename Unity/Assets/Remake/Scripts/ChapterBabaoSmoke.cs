using System.Collections;
using System.Linq;
using UnityEngine;

namespace MonsterRemake2D
{
    public partial class EarthGame2D
    {
        void RequireBabao(string context)
        {
            Require(babao!=null&&babao.library==babaoMotions&&babao.library.approvedDesignVersion=="v2","Fixed Babao library: "+context);
            Require(babaoMotions.directions.Any(d=>d.frames.Contains(babao.visual.sprite)),"Babao has companion art, not active beast art: "+context);
            Require(babao.transform.childCount==2,"Babao fixed body/shadow layers: "+context);
            Require(Vector3.Distance(babao.GroundPosition,fieldHeroMotion.transform.position)<babaoMotions.followDistance+.25f,"Babao follows within bounded distance: "+context);
        }

        IEnumerator CheckBabaoPartyAndBattle()
        {
            RequireBabao("after capture");
            int originalObject=babao.GetInstanceID();
            int capturedIndex=state.pets.Count-1;
            ShowMenu();ClickParty(capturedIndex);yield return null;
            Require(state.activePet==capturedIndex&&Pet.id==22,"Real party UI selects captured monster");
            Require(babao.GetInstanceID()==originalObject,"Party switch preserves independent Babao instance");
            RequireBabao("switched to slime");Capture("09-party-babao");CloseMenu();
            StartBattle(new[]{new Unit(21,2,catalog)},false);yield return null;
            Require(battlePet.sprite==SpriteFor(22),"Battle renders selected slime, not Babao");
            Require(!babao.gameObject.activeInHierarchy&&!TrySetActivePet(0),"Battle hides field companion and rejects party change");
            Capture("10-switched-battle");Choose("escape");if(phase==1)Choose("guard");yield return UntilRound();
            Require(mode==Mode.Explore&&babao.GetInstanceID()==originalObject,"Retreat restores same Babao object");RequireBabao("retreat");
            ShowMenu();ClickParty(0);CloseMenu();yield return null;
            Require(Pet.id==21&&babao.GetInstanceID()==originalObject,"Switch back to boar does not replace Babao");
            // Exercise the defeat cleanup branch without adding a long unrelated combat replay.
            StartBattle(new[]{new Unit(21,2,catalog)},false);state.hero.hp=0;FinishBattle(false,true);yield return null;
            Require(map.id==9&&state.hero.hp>0&&mode==Mode.Explore,"Defeat restores entrance and party");RequireBabao("defeat return");
            Save();int petIndex=state.activePet;Load();state.hero.Refresh(catalog);foreach(var pet in state.pets)pet.Refresh(catalog);BuildMap(state.map);yield return null;
            Require(state.activePet==petIndex&&state.pets.Count>1,"Save reload preserves selected combat monster");RequireBabao("save reload");
            Capture("11-babao-return");
        }

        void ClickParty(int index)
        {
            string name=catalog.Find(state.pets[index].id).name;
            var button=ui.modal.GetComponentsInChildren<UnityEngine.UI.Button>().FirstOrDefault(b=>b.name.Contains(name)&&b.name.Contains("Lv."));
            Require(button!=null&&button.interactable,"Real party button "+name);button.onClick.Invoke();
        }
    }
}
