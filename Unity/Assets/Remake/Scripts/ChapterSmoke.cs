using System;
using System.IO;
using System.Linq;
using System.Collections;
using UnityEngine;
using MonsterRemake2D.Combat;
namespace MonsterRemake2D {
 [Serializable] public class RouteStep {public int map,x,y,plane;}
 [Serializable] public class ReplayRoute {public RouteStep[] steps;}
 public partial class EarthGame2D {
  IEnumerator SmokeScenario(){
   Directory.CreateDirectory(SmokeOutput);yield return new WaitForSeconds(.7f);RequireBabao("initial spawn");Capture("01-exploration");
   // Real grid movement, stair state changes and original door transitions, without random ambushes.
   var chestRoute=Read<ReplayRoute>("smoke-chest-route");yield return Replay(chestRoute);
   var c=map.chests.FirstOrDefault(ch=>Near(ch.x,ch.y)&&ch.source=="2001.bin");Require(c!=null,"Reach original chest");int count=state.Count(c.item);Claim(c);Claim(c);Require(state.Count(c.item)==count+c.quantity,"Original reward exactly once");Capture("02-chest");
   int petsBefore=state.pets.Count;int ballsBefore=state.Count(121);var caught=new Unit(22,3,catalog);caught.hp=1;
   StartBattle(new[]{caught,new Unit(21,2,catalog),new Unit(11,2,catalog)},false);yield return UntilPlayerReady();
   Require(combat.Ready.Id=="hero","Hero independently reaches readiness");Capture("03-battle");ClickBattle("封印球 1");ClickBattle("软泥怪 · 确认目标");yield return UntilRound();
   Require(state.pets.Count==petsBefore+1&&state.Count(121)==ballsBefore-1,"ATB capture spends one ball and removes stable enemy identity");
   int rounds=0;while(mode==Mode.Battle&&rounds++<80){yield return UntilPlayerReady();if(mode!=Mode.Battle)break;Choose("attack");yield return UntilRound();}Require(mode==Mode.Explore,"ATB capture battle completed");
   yield return CheckBabaoPartyAndBattle();
   // Start the chapter route at the entrance; keep the earned chest/party data.
   state.x=10;state.y=15;state.plane=0;BuildMap(9);Rest();var route=Read<ReplayRoute>("smoke-route");yield return Replay(route);Require(map.id==75,"Arrive at original guardian room");
   Capture("04-guardian-room");Interact();while(mode==Mode.Dialogue)NextDialogue();Require(mode==Mode.Battle&&foes[0].id==8,"Original species 8 guardian fight");yield return new WaitForSeconds(.3f);Capture("05-guardian-battle");rounds=0;
   while(mode==Mode.Battle&&rounds++<140){yield return UntilPlayerReady();if(mode!=Mode.Battle)break;
    var actor=combat.Ready;
    if(actor.Hero&&state.hero.hp<150&&state.Count(143)>0){pendingSkill="remake:item-143";SubmitBattleAction("hero");}
    else if(actor.Hero&&state.hero.hp<100&&combat.CanUse(actor,"remake:heal")==null){pendingSkill="remake:heal";SubmitBattleAction("hero");}
    else if(actor.Hero&&combat.CanUse(actor,"remake:suppress")==null)Choose("remake:suppress");
    else if(actor.Hero&&combat.CanUse(actor,"remake:aim")==null)Choose("remake:aim");
    else if(!actor.Hero&&combat.CanUse(actor,"remake:earth-shield")==null&&combat.Find("hero").Shield<25){pendingSkill="remake:earth-shield";SubmitBattleAction("hero");}
    else Choose(actor.Hero?"attack":"skill");yield return UntilRound();}
   while(mode==Mode.Dialogue)NextDialogue();Require(state.bossDefeated&&state.Count(171)==1,"Original secret fragment awarded");
   yield return Replay(Read<ReplayRoute>("smoke-return-route"));Require(map.id==9,"Return to entrance through original rooms");Interact();Require(state.reported&&mode==Mode.Complete,"Exam submission completed");yield return new WaitForSeconds(.3f);Capture("06-complete");
   Save();var copy=JsonUtility.FromJson<EarthSave>(File.ReadAllText(SavePath));Require(copy.Valid(catalog,world)&&copy.bossDefeated&&copy.reported&&copy.opened.Contains(c.key)&&copy.pets.Count==state.pets.Count,"Save survives complete chapter");
   VerifyQuestCompletionRuntime();ShowAtlas(2);yield return new WaitForSeconds(.2f);Capture("07-earth-atlas");ShowAtlas(4);yield return new WaitForSeconds(.2f);Capture("08-special-atlas");
   File.WriteAllText(Path.Combine(SmokeOutput,"runtime-validation.txt"),"PASS: remake-atb-v1 production runtime; independent readiness and stable targets; real UI seal capture consumes one ball; active monster switch independent of fixed Babao; retreat/defeat cleanup; new skill/guard/shield/status rules and modular dual-pistol presentation; original movement, height transitions, one-time chest, guardian species8 with phase skills, fragment171, return, exam submission and save reload.\nArt remains existing approved designs and unapproved motion/monster candidates; technical verification is not visual approval.\nOutward steps="+route.steps.Length+". Boss player actions="+rounds+".\nManual keyboard verification is separate.");Debug.Log("EARTH_2D_RUNTIME_PASSED");Application.Quit(0);
  }
  void ClickBattle(string text){var b=ui.modal.GetComponentsInChildren<UnityEngine.UI.Button>().FirstOrDefault(b=>b.name.Contains(text));Require(b!=null&&b.interactable,"UI battle button "+text);b.onClick.Invoke();}
  IEnumerator UntilRound(){float timeout=0;while(resolving){timeout+=Time.deltaTime;Require(timeout<35,"Battle round timeout");yield return null;}yield return null;}
  IEnumerator UntilPlayerReady(){float timeout=0;while(mode==Mode.Battle&&(resolving||combat.Ready==null||combat.Ready.Side!=Side.Player)){timeout+=Time.deltaTime;Require(timeout<45,"ATB readiness timeout");yield return null;}}
  IEnumerator Replay(ReplayRoute route){
   foreach(var next in route.steps.Skip(1)){
    if(next.map!=map.id){var portal=map.portals.FirstOrDefault(p=>p.toMap==next.map&&NearPortal(p));Require(portal!=null,"Original door reachable "+map.id+" -> "+next.map);Travel(portal);}
    else {var v=new Vector2Int(next.x-state.x,next.y-state.y);if(v==Vector2Int.zero)continue;Require(Mathf.Abs(v.x)+Mathf.Abs(v.y)==1,"Adjacent grid movement");direction=v.x<0?1:v.x>0?2:v.y<0?3:0;yield return Walk(v,.015f);}
    Require(state.x==next.x&&state.y==next.y&&state.plane==next.plane,"Movement and height state "+next.map+":"+next.x+","+next.y+","+next.plane);
    RequireBabao("route "+next.map+":"+next.x+","+next.y);
   }yield return new WaitForSeconds(.1f);
  }
  void Capture(string name){
   var rt=new RenderTexture(1280,800,24);var prior=camera2d.targetTexture;var priorUI=ui.camera.targetTexture;var active=RenderTexture.active;camera2d.targetTexture=rt;ui.camera.targetTexture=rt;var scaler=ui.root.GetComponent<UnityEngine.UI.CanvasScaler>();var scaleMode=scaler.uiScaleMode;scaler.uiScaleMode=UnityEngine.UI.CanvasScaler.ScaleMode.ConstantPixelSize;scaler.scaleFactor=1;Canvas.ForceUpdateCanvases();camera2d.Render();ui.camera.Render();RenderTexture.active=rt;var image=new Texture2D(1280,800,TextureFormat.RGB24,false);image.ReadPixels(new Rect(0,0,1280,800),0,0);image.Apply();File.WriteAllBytes(Path.Combine(SmokeOutput,name+".png"),image.EncodeToPNG());camera2d.targetTexture=prior;ui.camera.targetTexture=priorUI;scaler.uiScaleMode=scaleMode;RenderTexture.active=active;Destroy(image);Destroy(rt);
  }
  static void Require(bool value,string message){if(!value)throw new Exception("2D runtime verification failed: "+message);}
 }
}
