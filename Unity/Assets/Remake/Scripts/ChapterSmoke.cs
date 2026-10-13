using System;
using System.IO;
using System.Linq;
using System.Collections;
using UnityEngine;
namespace MonsterRemake2D {
 [Serializable] public class RouteStep {public int map,x,y,plane;}
 [Serializable] public class ReplayRoute {public RouteStep[] steps;}
 public partial class EarthGame2D {
  IEnumerator SmokeScenario(){
   Directory.CreateDirectory(SmokeOutput);yield return new WaitForSeconds(.7f);RequireBabao("initial spawn");Capture("01-exploration");
   // Real grid movement, stair state changes and original door transitions, without random ambushes.
   var chestRoute=Read<ReplayRoute>("smoke-chest-route");yield return Replay(chestRoute);
   var c=map.chests.FirstOrDefault(ch=>Near(ch.x,ch.y)&&ch.source=="2001.bin");Require(c!=null,"Reach original chest");int count=state.Count(c.item);Claim(c);Claim(c);Require(state.Count(c.item)==count+c.quantity,"Original reward exactly once");Capture("02-chest");
   UnityEngine.Random.InitState(42);int petsBefore=state.pets.Count;int ballsBefore=state.Count(121);var caught=new Unit(22,3,catalog);caught.hp=1;
   StartBattle(new[]{caught,new Unit(21,2,catalog),new Unit(11,2,catalog)},false);yield return new WaitForSeconds(.2f);Capture("03-battle");ClickBattle("封印球");ClickBattle("防御");yield return UntilRound();
   Require(state.pets.Count==petsBefore+1&&state.Count(121)==ballsBefore-1,"Multi-enemy capture spends one ball and preserves turn list");
   int rounds=0;while(mode==Mode.Battle&&rounds++<12){Choose("attack");if(phase==1)Choose("attack");yield return UntilRound();}Require(mode==Mode.Explore,"Capture battle completed");
   yield return CheckBabaoPartyAndBattle();
   // Start the chapter route at the entrance; keep the earned chest/party data.
   state.x=10;state.y=15;state.plane=0;BuildMap(9);Rest();var route=Read<ReplayRoute>("smoke-route");yield return Replay(route);Require(map.id==75,"Arrive at original guardian room");
   Capture("04-guardian-room");Interact();while(mode==Mode.Dialogue)NextDialogue();Require(mode==Mode.Battle&&foes[0].id==8,"Original species 8 guardian fight");yield return new WaitForSeconds(.3f);Capture("05-guardian-battle");rounds=0;
   while(mode==Mode.Battle&&rounds++<15){Choose(state.hero.hp<120&&state.Count(143)>0?"item":"attack");if(phase==1)Choose(Pet.mp>=8?"skill":"attack");yield return UntilRound();}
   while(mode==Mode.Dialogue)NextDialogue();Require(state.bossDefeated&&state.Count(171)==1,"Original secret fragment awarded");
   yield return Replay(Read<ReplayRoute>("smoke-return-route"));Require(map.id==9,"Return to entrance through original rooms");Interact();Require(state.reported&&mode==Mode.Complete,"Exam submission completed");yield return new WaitForSeconds(.3f);Capture("06-complete");
   Save();var copy=JsonUtility.FromJson<EarthSave>(File.ReadAllText(SavePath));Require(copy.Valid(catalog,world)&&copy.bossDefeated&&copy.reported&&copy.opened.Contains(c.key)&&copy.pets.Count==state.pets.Count,"Save survives complete chapter");
   VerifyQuestCompletionRuntime();ShowAtlas(2);yield return new WaitForSeconds(.2f);Capture("07-earth-atlas");ShowAtlas(4);yield return new WaitForSeconds(.2f);Capture("08-special-atlas");
   File.WriteAllText(Path.Combine(SmokeOutput,"runtime-validation.txt"),"PASS: 50 monster placeholders + approved v4 hero design with v5 motion candidate, approved v2 Babao design with v3 four-direction hover/flap/follow motion; real party UI selects captured slime and switches back without replacing Babao; selected slime appears in combat while Babao field object is hidden; retreat restores same companion instance; defeat return and save reload recreate correct Babao; 36 Q hero field frames and modular dual-pistol battle actions, original door and height transitions, one-time chest reward, three-enemy capture, guardian species8, fragment171, return and exam completion.\nOutward steps="+route.steps.Length+". Boss rounds="+rounds+".\nManual keyboard verification is separate; no automatic unlock was attempted.");Debug.Log("EARTH_2D_RUNTIME_PASSED");Application.Quit(0);
  }
  void ClickBattle(string text){var b=ui.modal.GetComponentsInChildren<UnityEngine.UI.Button>().FirstOrDefault(b=>b.name.Contains(text));Require(b!=null&&b.interactable,"UI battle button "+text);b.onClick.Invoke();}
  IEnumerator UntilRound(){float timeout=0;while(resolving){timeout+=Time.deltaTime;Require(timeout<35,"Battle round timeout");yield return null;}yield return null;}
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
