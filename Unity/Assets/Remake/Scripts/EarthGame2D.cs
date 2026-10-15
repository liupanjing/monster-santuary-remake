using System;
using System.IO;
using System.Linq;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Tilemaps;
using UnityEngine.UI;
namespace MonsterRemake2D {
 public enum Mode {Explore,Dialogue,Battle,Menu,Map,Atlas,Complete}
 public partial class EarthGame2D:MonoBehaviour {
  public Catalog catalog;public World world;public Items items;public EarthSave state;public EarthMap map;public Mode mode;
  public ChapterUI ui;Camera camera2d;GameObject field,battleScene;SpriteRenderer heroSprite;Dictionary<int,Sprite> sprites=new Dictionary<int,Sprite>();Dictionary<string,SpriteRenderer> chestSprites=new Dictionary<string,SpriteRenderer>();Tile[] tiles;bool moving,resolving,bossBattle;int direction=0,stepCount,atlasPage,partyPage;string message="你和八宝带着豆猪抵达秘境入口。前往第三层接受守护者考验。",near="",log="";List<Unit> foes=new List<Unit>();List<SpriteRenderer> foeSprites=new List<SpriteRenderer>();SpriteRenderer battleHero,battlePet;Queue<KeyValuePair<string,string>> dialogues=new Queue<KeyValuePair<string,string>>();Action afterDialogue;AudioSource audio;
  bool Smoke=>Environment.GetCommandLineArgs().Contains("--2d-smoke");
  string SmokeOutput=>Environment.GetCommandLineArgs().FirstOrDefault(a=>a.StartsWith("--smoke-output="))?.Substring(15)??Application.persistentDataPath;
  string SavePath=>Path.Combine(Smoke?SmokeOutput:Application.persistentDataPath,"earth-2d-v2.json");
  Unit Pet=>state.pets[Mathf.Clamp(state.activePet,0,state.pets.Count-1)];
  Vector2Int Cell=>new Vector2Int(state.x,state.y);
  static Vector3 At(int x,int y)=>new Vector3(x+.5f,-y-.5f,0);
  void Start(){
   catalog=Read<Catalog>("catalog");world=Read<World>("earth-world");items=Read<Items>("items");
   foreach(var e in catalog.entries){var sprite=Resources.Load<Sprite>(e.spriteResource);if(sprite==null)throw new Exception("Missing newly drawn sprite: "+e.name);sprites[e.id]=sprite;}
   LoadHeroMotion();LoadBabao();LoadEarthQuest();
   var atlas=Resources.Load<Texture2D>("Art/tile1_0");tiles=new Tile[atlas.width/16];for(int i=0;i<tiles.Length;i++){var t=ScriptableObject.CreateInstance<Tile>();t.sprite=Sprite.Create(atlas,new Rect(i*16,0,16,16),new Vector2(.5f,.5f),16);tiles[i]=t;}
   camera2d=Camera.main;if(camera2d==null){camera2d=new GameObject("2D camera").AddComponent<Camera>();camera2d.tag="MainCamera";}camera2d.transform.position=new Vector3(10,-15,-10);camera2d.transform.rotation=Quaternion.identity;camera2d.orthographic=true;camera2d.orthographicSize=6;camera2d.backgroundColor=new Color(.055f,.052f,.10f);camera2d.clearFlags=CameraClearFlags.SolidColor;camera2d.cullingMask=~(1<<5);camera2d.gameObject.AddComponent<AudioListener>();audio=gameObject.AddComponent<AudioSource>();audio.volume=.18f;
   ui=new ChapterUI(camera2d);state=EarthSave.New(catalog);if(!Smoke)Load();RestoreVisualEquipment();state.hero.Refresh(catalog);foreach(var p in state.pets)p.Refresh(catalog);BuildMap(state.map);mode=Mode.Explore;UpdateUI();
   if(Smoke)StartCoroutine(SmokeScenario());else if(!File.Exists(SavePath))Dialogue(new[]{Pair("考官","考试的第一个内容是前往土之秘境第三层，找到并战胜秘境守护者。"),Pair("考官","我已经给你施加了通行标记。道路漫长，做好补给工作哦！"),Pair("奥尼克","就从这里开始。先看看周围的路，也不能漏掉宝箱。")},()=>{Notify("已进入土之秘境一层。入口传送石可休整和补给。");Save();});
  }
  T Read<T>(string key)=>JsonUtility.FromJson<T>(Resources.Load<TextAsset>("Data/"+key).text);
  public Sprite SpriteFor(int id)=>sprites[id];
  SpriteRenderer Draw(string name,Sprite s,Vector3 p,Transform parent,int order=60,float scale=1){var o=new GameObject(name);o.transform.SetParent(parent,false);o.transform.position=p;o.transform.localScale=Vector3.one*scale;var r=o.AddComponent<SpriteRenderer>();r.sprite=s;r.sortingOrder=order;return r;}
  void BuildMap(int id){
   if(field!=null)Destroy(field);field=new GameObject("Original earth map "+id);map=world.Find(id);state.map=id;if(!state.visited.Contains(id))state.visited.Add(id);chestSprites.Clear();
   var grid=new GameObject("Original 16px tile grid",typeof(Grid));grid.transform.SetParent(field.transform,false);
   for(int layer=0;layer<2;layer++){var o=new GameObject("Layer "+layer,typeof(Tilemap),typeof(TilemapRenderer));o.transform.SetParent(grid.transform,false);var tm=o.GetComponent<Tilemap>();o.GetComponent<TilemapRenderer>().sortingOrder=layer==0?-100:100;int[] data=layer==0?map.layer0:map.layer1;
    for(int y=0;y<map.height;y++)for(int x=0;x<map.width;x++){int n=data[y*map.width+x];if(n==-1)continue;int index=n&1023;if(index>=tiles.Length)continue;var cell=new Vector3Int(x,-y-1,0);tm.SetTile(cell,tiles[index]);if((n&0xc000)!=0){tm.SetTileFlags(cell,TileFlags.None);tm.SetTransformMatrix(cell,Matrix4x4.Scale(new Vector3((n&0x8000)!=0?-1:1,(n&0x4000)!=0?-1:1,1)));}}
   }
   foreach(var c in map.chests)if(!state.opened.Contains(c.key))chestSprites[c.key]=Draw(c.name,Resources.Load<Sprite>(c.big?"Art/bigbox":"Art/box"),At(c.x,c.y),field.transform,c.plane==1?160:60);
   foreach(var n in map.npcs){if(n.type=="guardian"){if(!state.bossDefeated)Draw("秘境守护者",SpriteFor(8),At(n.x,n.y),field.transform,160,1.25f);}else if(n.type.StartsWith("npc_s"))continue;else {var image=Resources.Load<Sprite>("Art/"+n.type);if(image!=null)Draw(n.type,image,At(n.x,n.y),field.transform,n.plane==1?160:60);}}
   if(id==9)Draw("入口传送石",SpriteFor(25),At(11,15),field.transform,155,.8f);
   var spawn=map.Nearest(Cell,state.plane);state.x=spawn.x;state.y=spawn.y;
   fieldHeroMotion=NewHero("奥尼克 · Q版四方向动作",field.transform,At(state.x,state.y),HeroMotionLibrary2D.FieldView(direction),state.plane==1?180:80,1,false);heroSprite=fieldHeroMotion.rig.Body;
   SpawnBabao();
   camera2d.rect=new Rect(0,.13f,1,.735f);camera2d.orthographicSize=6;FollowCamera(true);UpdateUI();
  }
  void Update(){
   if(state==null)return;
   if(mode==Mode.Dialogue){if(!Smoke&&(Input.GetKeyDown(KeyCode.Space)||Input.GetKeyDown(KeyCode.Return)||Input.GetKeyDown(KeyCode.E)))NextDialogue();return;}
   if(mode==Mode.Battle){TickBattle();if(!Smoke)BattleInput();return;}
   if(Smoke)return;
   if(Input.GetKeyDown(KeyCode.Escape)){if(mode==Mode.Explore)ShowMenu();else if(mode!=Mode.Battle&&mode!=Mode.Complete){ui.Clear();mode=Mode.Explore;}return;}
   if(mode!=Mode.Explore)return;
   if(Input.GetKeyDown(KeyCode.F5)){Save();Notify("已保存探索进度。");}
   if(Input.GetKeyDown(KeyCode.M)){ShowMap();return;}
   if(Input.GetKeyDown(KeyCode.E)&&!moving){Interact();return;}
   if(!moving){Vector2Int v=Vector2Int.zero;if(Input.GetKey(KeyCode.W)||Input.GetKey(KeyCode.UpArrow)){v=Vector2Int.down;direction=3;}else if(Input.GetKey(KeyCode.S)||Input.GetKey(KeyCode.DownArrow)){v=Vector2Int.up;direction=0;}else if(Input.GetKey(KeyCode.A)||Input.GetKey(KeyCode.LeftArrow)){v=Vector2Int.left;direction=1;}else if(Input.GetKey(KeyCode.D)||Input.GetKey(KeyCode.RightArrow)){v=Vector2Int.right;direction=2;}if(v!=Vector2Int.zero)StartCoroutine(Walk(v,Input.GetKey(KeyCode.LeftShift)?.09f:.15f));else FieldFrame(false,false);}
   near=NearbyPrompt();UpdateUI();
  }
  bool CanStep(Vector2Int cell,int plane)=>map.Walkable(cell,plane)&&!(map.id==75&&!state.bossDefeated&&cell.x==9&&cell.y==6);
  IEnumerator Walk(Vector2Int v,float duration){
   if(moving)yield break;var to=Cell+v;int plane=map.NextPlane(to,state.plane);if(!CanStep(to,plane)){FieldFrame(false,false);yield break;}
   moving=true;lastWalkWasRun=duration<=.1f;lastWalkPlane=plane;Vector3 from=fieldHeroMotion.transform.position,dest=At(to.x,to.y);float time=0;while(time<duration){time+=Time.deltaTime;float f=Mathf.Clamp01(time/duration);fieldHeroMotion.transform.position=Vector3.Lerp(from,dest,f);FieldFrame(true,lastWalkWasRun);yield return null;}
   state.x=to.x;state.y=to.y;state.plane=plane;fieldHeroMotion.transform.position=dest;fieldHeroMotion.rig.bodyOrder=plane==1?180:80;FieldFrame(false,false);moving=false;stepCount++;if(!Smoke&&stepCount>=32){stepCount=0;Ambush();}UpdateUI();
  }
  void LateUpdate(){if(state!=null&&mode!=Mode.Battle){TickBabao();FollowCamera(false);}}
  void FollowCamera(bool instant){if(heroSprite==null)return;float h=camera2d.orthographicSize,w=h*camera2d.aspect;var p=heroSprite.transform.position;float x=map.width<2*w?map.width/2f:Mathf.Clamp(p.x,w,map.width-w);float y=map.height<2*h?-map.height/2f:Mathf.Clamp(p.y,-map.height+h,-h);var target=new Vector3(x,y,-10);camera2d.transform.position=instant?target:Vector3.Lerp(camera2d.transform.position,target,1-Mathf.Exp(-Time.deltaTime*14));}
  bool Near(int x,int y,float distance=1.6f)=>(new Vector2(x-state.x,y-state.y)).magnitude<=distance;
  bool NearPortal(Portal p)=>p.Contains(Cell)||Near(p.x,p.y,1.2f);
  string NearbyPrompt(){
   if(map.id==9&&Near(10,15))return state.reported?"E · 查看考试完成记录":EarthQuestStage.id=="ready_to_report"?"E · 交付秘境碎片，完成考试":"E · 入口传送石：休整、补给和队伍";
   if(map.id==75&&!state.bossDefeated&&Near(9,6,2))return "E · 挑战秘境守护者";
   foreach(var c in map.chests)if(!state.opened.Contains(c.key)&&Near(c.x,c.y))return "E · 打开宝箱";
   foreach(var p in map.portals)if(NearPortal(p))return p.hidden?"E · 石壁后似乎有路":"E · 进入洞口";
   foreach(var n in map.npcs)if(n.type!="guardian"&&Near(n.x,n.y))return "E · 交谈";
   return "";
  }
  public void Interact(){
   if(mode!=Mode.Explore)return;
   if(map.id==9&&Near(10,15)){if(state.bossDefeated||state.reported)SubmitEarthQuest();else ShowCamp();return;}
   if(map.id==75&&!state.bossDefeated&&Near(9,6,2)){Dialogue(new[]{Pair("秘境守护者","我是土之秘境的守护者，也兼职考验你们这些新入职的驯兽师。"),Pair("秘境守护者","见习驯兽师参考者！你准备好接受考验了吗？"),Pair("奥尼克","我一定会战胜你。传奇猎人的孙子，也会是传奇！")},()=>StartBattle(new[]{new Unit(8,10,catalog)},true));return;}
   var chest=map.chests.Where(c=>!state.opened.Contains(c.key)&&Near(c.x,c.y)).OrderBy(c=>(new Vector2(c.x-state.x,c.y-state.y)).sqrMagnitude).FirstOrDefault();if(chest!=null){Claim(chest);return;}
   var portal=map.portals.FirstOrDefault(NearPortal);if(portal!=null){Travel(portal);return;}
   var npc=map.npcs.FirstOrDefault(n=>n.type!="guardian"&&Near(n.x,n.y));if(npc!=null){Dialogue(new[]{Pair(npc.sourceScript==3040?"速度的考验":"路上的驯兽师",npc.sourceScript==3040?"接下来就是极其危险的土之秘境第三层了。道路曲折，补给比蛮干更重要。":"高台与低处的路并不相同。楼梯可以上下，岩壁后的支路也值得探索。")},null);return;}
   Notify("靠近宝箱、洞口或传送石，再按 E 调查。");
  }
  public void Claim(Chest c){if(!state.Claim(c))return;if(chestSprites.TryGetValue(c.key,out var r)){Destroy(r.gameObject);chestSprites.Remove(c.key);}Notify(c.quantity>0?"打开宝箱：获得 "+c.name+" ×"+c.quantity:"旧箱中只剩灰尘。" );Beep(660,.12f);Save();}
  public void Travel(Portal p){if(p.outside){Notify("这里通往秘境之外。先完成考试，再回入口交付认证物。");return;}if(p.hidden){state.secretFound=true;Notify("发现岩壁后的侧洞。这里连接着另一段地脉。 ");}var m=world.Find(p.toMap);var a=m.arrivals.FirstOrDefault(x=>x.door==p.door);if(a==null)throw new Exception("Missing original arrival "+p.toMap+":"+p.door);state.x=a.x;state.y=a.y;state.plane=a.plane;BuildMap(p.toMap);Save();if(!p.hidden)Notify("进入 "+map.name+"。");}
  void Ambush(){
   var choices=map.encounters.Where(r=>r.species.Any(i=>i>=0)&&state.x*16>=r.bounds[0]&&state.x*16<=r.bounds[2]&&state.y*16>=r.bounds[1]&&state.y*16<=r.bounds[3]).ToArray();if(choices.Length==0)return;var region=choices[UnityEngine.Random.Range(0,choices.Length)];int count=UnityEngine.Random.Range(region.minCount,region.maxCount+1);var ids=region.species.Where(i=>i>=0&&i<50).ToArray();if(ids.Length==0)return;var group=new List<Unit>();for(int i=0;i<Mathf.Clamp(count,1,3);i++)group.Add(new Unit(ids[UnityEngine.Random.Range(0,ids.Length)],UnityEngine.Random.Range(region.minLevel,region.maxLevel+1),catalog));StartBattle(group.ToArray(),false);
  }
  void GainXP(Unit u,int amount){if(u.hp<=0)return;u.xp+=amount;while(u.xp>=u.level*15&&u.level<50){u.xp-=u.level*15;int hp=u.stats.hp,mp=u.stats.mp;u.level++;u.Refresh(catalog);u.hp+=u.stats.hp-hp;u.mp+=u.stats.mp-mp;}}
  void Rest(){state.hero.hp=state.hero.stats.hp;state.hero.mp=state.hero.stats.mp;foreach(var p in state.pets){p.hp=p.stats.hp;p.mp=p.stats.mp;}}
  void ShowCamp(){mode=Mode.Menu;var p=ui.Modal("入口传送石 · 补给与休整",850,440);ui.Label(p,25,85,800,55,"调整队伍、准备补给，再进入更深处。",19);ui.Button(p,25,155,245,45,"休整（20 金币）",()=>{if(state.gold>=20){state.gold-=20;Rest();Save();Notify("你和怪兽恢复了生命与魔力。");ui.Clear();mode=Mode.Explore;}else Notify("金币不足，可以先探索宝箱。 ");});ui.Button(p,295,155,245,45,"购买小血瓶（25 金币）",()=>Buy(141,25));ui.Button(p,565,155,245,45,"购买封印球1（50 金币）",()=>Buy(121,50));ui.Button(p,25,225,245,45,"队伍与行囊",ShowMenu);ui.Button(p,295,225,245,45,"怪兽图鉴",()=>ShowAtlas(0));ui.Button(p,565,225,245,45,"继续探索",CloseMenu);ui.Label(p,25,315,800,50,"通行标记已生效。第三层的守护者正等待着你。",18);}
  void Buy(int id,int gold){if(state.gold<gold){Notify("金币不足。");return;}state.gold-=gold;state.Add(id,1);Save();Notify("获得 "+items.Name(id)+"。");ShowCamp();}
  void ShowMenu(){mode=Mode.Menu;var p=ui.Modal("队伍与行囊",1040,570);ui.Label(p,25,75,530,32,"奥尼克  Lv."+state.hero.level+"  HP "+state.hero.hp+" / "+state.hero.stats.hp,19);partyPage=Mathf.Clamp(partyPage,0,(state.pets.Count-1)/7);for(int i=0;i<Mathf.Min(state.pets.Count-partyPage*7,7);i++){int n=i+partyPage*7;var u=state.pets[n];ui.Picture(p,25,115+i*47,43,43,SpriteFor(u.id));ui.Button(p,78,120+i*47,470,40,(state.activePet==n?"▶ ":"")+catalog.Find(u.id).name+"  Lv."+u.level+"   HP "+u.hp+" / "+u.stats.hp,()=>{if(TrySetActivePet(n))ShowMenu();});}ui.Button(p,25,450,185,32,"上一组",()=>{partyPage--;ShowMenu();},partyPage>0);ui.Button(p,225,450,185,32,"下一组",()=>{partyPage++;ShowMenu();},(partyPage+1)*7<state.pets.Count);ui.Label(p,430,454,120,30,(partyPage+1)+" / "+((state.pets.Count+6)/7),16);ui.Label(p,590,77,390,310,string.Join("\n",state.inventory.Where(s=>s.count>0).Take(11).Select(s=>items.Name(s.id)+"  ×"+s.count)),17);ui.Button(p,590,404,190,42,"怪兽图鉴",()=>ShowAtlas(0));ui.Button(p,790,404,205,42,"为怪兽补魔",GiveMagic);ui.Button(p,25,494,285,43,"保存并继续",()=>{Save();CloseMenu();});ui.Button(p,325,494,285,43,"退出游戏",()=>{Save();Application.Quit();});ui.Button(p,625,494,370,43,"重新开始",()=>{var b=ui.Modal("重新开始",620,270);ui.Label(b,25,82,560,40,"当前记录将备份，然后开始新的探索。",18);ui.Button(b,25,165,260,42,"开始新探索",()=>{if(File.Exists(SavePath))File.Copy(SavePath,SavePath+".backup",true);state=EarthSave.New(catalog);BuildMap(9);CloseMenu();Save();});ui.Button(b,320,165,260,42,"取消",ShowMenu);});}
  void GiveMagic(){int bottle=new[]{148,147,146,149,150}.FirstOrDefault(i=>state.Count(i)>0);if(bottle==0){Notify("魔瓶不足。");return;}state.Spend(bottle);Pet.mp=Mathf.Min(Pet.stats.mp,Pet.mp+(bottle==148?250:bottle==147?100:bottle==146?50:bottle==149?1000:2000));Save();Notify(catalog.Find(Pet.id).name+"恢复了魔力。");ShowMenu();}
  void CloseMenu(){ui.Clear();mode=Mode.Explore;UpdateUI();}
  void ShowAtlas(int page){mode=Mode.Atlas;atlasPage=page;var p=ui.Modal("怪兽图鉴 · 全新绘制",1130,600);var entries=catalog.entries.Where(e=>e.role=="monster").OrderBy(e=>e.element==5?9:e.element).ThenBy(e=>e.tier).ToArray();for(int i=0;i<10&&page*10+i<entries.Length;i++){var e=entries[page*10+i];float x=25+(i%5)*216,y=75+(i/5)*215;ui.Picture(p,x,y,190,170,SpriteFor(e.id));ui.Label(p,x,y+172,200,30,e.name+" · "+e.tier+"阶",17,true);}ui.Button(p,25,525,220,43,"上一页",()=>ShowAtlas(Mathf.Max(0,page-1)),page>0);ui.Label(p,290,535,200,30,(page+1)+" / 5",18);ui.Button(p,495,525,220,43,"下一页",()=>ShowAtlas(Mathf.Min(4,page+1)),page<4);ui.Button(p,870,525,230,43,"返回",ShowMenu);}
  void ShowMap(){mode=Mode.Map;var p=ui.Modal(map.name+" · 地脉地图",900,590);var t=new Texture2D(map.width,map.height,TextureFormat.RGBA32,false);t.filterMode=FilterMode.Point;for(int y=0;y<map.height;y++)for(int x=0;x<map.width;x++){int a=map.attributes[y*map.width+x];t.SetPixel(x,map.height-1-y,a==3?new Color(.15f,.13f,.1f):a==2?new Color(.44f,.42f,.29f):new Color(.72f,.62f,.40f));}foreach(var c in map.chests)if(!state.opened.Contains(c.key))t.SetPixel(c.x,map.height-1-c.y,new Color(1,.76f,.19f));foreach(var a in map.portals)if(!a.hidden||state.secretFound)t.SetPixel(a.x,map.height-1-a.y,new Color(.34f,.70f,.75f));t.SetPixel(state.x,map.height-1-state.y,new Color(.9f,.25f,.2f));t.Apply();var sprite=Sprite.Create(t,new Rect(0,0,t.width,t.height),new Vector2(.5f,.5f),1);ui.Picture(p,25,80,680,445,sprite);ui.Label(p,725,100,155,260,"红：当前位置\n金：宝箱\n青：洞口\n\n不同明暗表示上下层通路。\n楼梯可以改变所在层。",17);ui.Button(p,725,477,150,45,"继续探索",CloseMenu);}
  void ShowComplete(){mode=Mode.Complete;var p=ui.Modal("驯兽师考试 · 土之秘境通过",890,460);ui.Picture(p,30,90,160,230,heroMotions.Field(HeroView2D.FieldDown).idle);ui.Label(p,215,95,635,140,"奥尼克递上了秘境碎片。\n\n考官：你已经从土之秘境回来了？很好，第一阶段考试通过。",22);ui.Label(p,215,254,620,70,"探索区域 "+state.visited.Count+" / 27  ·  宝箱 "+state.opened.Count+" / 28\n收服怪兽 "+Mathf.Max(0,state.pets.Count-1)+"  ·  隐蔽侧洞 "+(state.secretFound?"已发现":"未发现"),18);ui.Button(p,25,365,250,48,"继续探索支路",CloseMenu);ui.Button(p,295,365,250,48,"查看怪兽图鉴",()=>ShowAtlas(0));ui.Button(p,565,365,285,48,"保存并退出",()=>{Save();Application.Quit();});}
  static KeyValuePair<string,string> Pair(string s,string b)=>new KeyValuePair<string,string>(s,b);
  void Dialogue(IEnumerable<KeyValuePair<string,string>> lines,Action finished){dialogues=new Queue<KeyValuePair<string,string>>(lines);afterDialogue=finished;mode=Mode.Dialogue;DrawDialogue();}
  void DrawDialogue(){if(dialogues.Count==0){ui.Clear();mode=Mode.Explore;var a=afterDialogue;afterDialogue=null;a?.Invoke();UpdateUI();return;}var line=dialogues.Peek();var p=ui.Modal(line.Key,980,230);ui.Picture(p,22,70,120,125,line.Key=="秘境守护者"?SpriteFor(8):heroMotions.Field(HeroView2D.FieldDown).idle);ui.Label(p,170,77,765,90,line.Value,21);ui.Button(p,760,170,190,40,"继续  Space",NextDialogue);}
  public void NextDialogue(){if(dialogues.Count>0)dialogues.Dequeue();DrawDialogue();}
  void Notify(string s){message=s;UpdateUI();}
  void UpdateUI(){if(ui!=null&&state!=null&&map!=null){ui.Refresh(state,map,items,message,near);if(earthQuest!=null)ui.objective.text=earthQuest.title+" · "+EarthQuestStage.message;}}
  void Beep(float hz,float seconds){if(Smoke)return;int n=(int)(22050*seconds);var c=AudioClip.Create("Retro cue",n,1,22050,false);var samples=new float[n];for(int i=0;i<n;i++)samples[i]=Mathf.Sign(Mathf.Sin(i*hz*Mathf.PI*2/22050))*.15f*(1f-(float)i/n);c.SetData(samples,0);audio.PlayOneShot(c);Destroy(c,seconds+1);}
  public void Save(){try{SaveFile.Write(SavePath,state);}catch(Exception e){Notify("保存失败："+e.Message);Debug.LogError(e);}}
  void Load(){try{if(!File.Exists(SavePath))return;var s=JsonUtility.FromJson<EarthSave>(File.ReadAllText(SavePath));if(s!=null&&s.Valid(catalog,world)){s.Upgrade();state=s;}else message="旧记录无法读取，已开始新的探索。";}catch(Exception e){message="存档无法读取，已开始新的探索。";Debug.LogWarning(e);}}
 }
}
