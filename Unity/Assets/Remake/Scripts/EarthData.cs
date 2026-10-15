using System;
using System.IO;
using System.Linq;
using System.Collections.Generic;
using UnityEngine;
namespace MonsterRemake2D {
 [Serializable] public class Character {public int id,element,tier;public string name,role,artPolicy,assetStatus,spriteResource,designBrief; public int[] rawOriginalFields;}
 [Serializable] public class Catalog {public Character[] entries;public Character Find(int id)=>entries.First(e=>e.id==id);}
 [Serializable] public class Portal {public string key;public int x,y,toMap,door,plane;public bool outside,hidden;public int[] cells,rect;public bool Contains(Vector2Int p){for(int i=0;i+1<(cells?.Length??0);i+=2)if(cells[i]==p.x&&cells[i+1]==p.y)return true;return p.x==x&&p.y==y;}}
 [Serializable] public class Arrival {public int x,y,door,plane;}
 [Serializable] public class Chest {public string key,name,source;public int x,y,item,quantity,plane;public bool big;}
 [Serializable] public class Npc {public string key,type;public int x,y,sourceScript,species,plane;}
 [Serializable] public class Encounter {public int[] bounds,species,drops,rates;public int minCount,maxCount,minLevel,maxLevel,threshold;}
 [Serializable] public class HeightTrigger {public int[] rect;public int plane;}
 [Serializable] public class EarthMap {public int id,floor,width,height;public string name,source;public int[] layer0,layer1,attributes;public Portal[] portals;public Arrival[] arrivals;public Chest[] chests;public Npc[] npcs;public Encounter[] encounters;public HeightTrigger[] layerTriggers;
  public bool Walkable(Vector2Int p,int plane=0){if(p.x<0||p.y<0||p.x>=width||p.y>=height)return false;int a=attributes[p.y*width+p.x];return a==0||a==1&&plane==0||a==2&&plane==1;}
  public int NextPlane(Vector2Int p,int plane){float x=p.x*16+8,y=p.y*16+8;foreach(var t in layerTriggers??Array.Empty<HeightTrigger>())if(x+8>=t.rect[0]&&x-8<=t.rect[2]&&y+8>=t.rect[1]&&y-8<=t.rect[3])plane=t.plane;return plane;}
  public Vector2Int Nearest(Vector2Int target,int plane=0){Vector2Int best=Vector2Int.zero;float d=float.MaxValue;for(int y=0;y<height;y++)for(int x=0;x<width;x++){var p=new Vector2Int(x,y);float n=(p-target).sqrMagnitude;if(Walkable(p,plane)&&n<d){d=n;best=p;}}return best;}
  public List<Vector3Int> Route(Vector3Int start,Vector3Int end,HashSet<Vector2Int> blocked=null){var q=new Queue<Vector3Int>();var parent=new Dictionary<Vector3Int,Vector3Int>();q.Enqueue(start);parent[start]=start;while(q.Count>0){var p=q.Dequeue();if(p==end){var path=new List<Vector3Int>{p};while(p!=start){p=parent[p];path.Add(p);}path.Reverse();return path;}foreach(var v in new[]{Vector2Int.up,Vector2Int.down,Vector2Int.left,Vector2Int.right}){var cell=new Vector2Int(p.x,p.y)+v;int height=NextPlane(cell,p.z);var n=new Vector3Int(cell.x,cell.y,height);if(Walkable(cell,height)&&!parent.ContainsKey(n)&&(blocked==null||!blocked.Contains(cell))){parent[n]=p;q.Enqueue(n);}}}return null;}

 }
 [Serializable] public class World {public EarthMap[] maps;public EarthMap Find(int id)=>maps.First(m=>m.id==id);}
 [Serializable] public class Item {public int id;public string name,description;}
 [Serializable] public class Items {public Item[] entries;public string Name(int id)=>entries.FirstOrDefault(x=>x.id==id)?.name??"物品"+id;}
 [Serializable] public class ItemStack {public int id,count; public ItemStack(int i,int n){id=i;count=n;}}
 [Serializable] public class Unit {public int id,level,hp,mp,xp;public bool guarding;[NonSerialized] public Stats stats;
  public Unit(int i,int l,Catalog c){id=i;level=l;Refresh(c);hp=stats.hp;mp=stats.mp;}
  public void Refresh(Catalog c){stats=OriginalGrowth.Calculate(c.Find(id).rawOriginalFields,level);hp=Mathf.Clamp(hp,0,stats.hp);mp=Mathf.Clamp(mp,0,stats.mp);}
 }
 public struct Stats {public int hp,mp,attack,defense,speed,magic,element;}
 public static class OriginalGrowth {
  // m.java: ((base + growth * level / 5) * level / 50), with Java integer truncation.
  public static Stats Calculate(int[] f,int l){int V(int b,int g)=>Mathf.Max(1,(f[b]+f[g]*l/5)*l/50);return new Stats{element=f[0],attack=V(1,2),defense=V(3,4),speed=V(5,6),hp=V(7,8),mp=V(9,10),magic=V(11,12)};}
 }
 [Serializable] public class EarthSave {public int version=3,visualWeaponId=-1,map=9,x=10,y=15,plane=0,gold=100,activePet=0;public bool bossDefeated,reported,secretFound;public Unit hero;public List<Unit> pets=new List<Unit>();public List<ItemStack> inventory=new List<ItemStack>();public List<string> opened=new List<string>();public List<int> visited=new List<int>();
  public int Count(int id)=>inventory.FirstOrDefault(x=>x.id==id)?.count??0;
  public void Upgrade(){if(version==2){version=3;visualWeaponId=-1;}}
  public void Add(int id,int n){var s=inventory.FirstOrDefault(x=>x.id==id);if(s==null)inventory.Add(new ItemStack(id,n));else s.count+=n;}
  public bool Spend(int id){var s=inventory.FirstOrDefault(x=>x.id==id);if(s==null||s.count<=0)return false;s.count--;return true;}
  public bool Claim(Chest c){if(opened.Contains(c.key))return false;opened.Add(c.key);if(c.item>0&&c.quantity>0)Add(c.item,c.quantity);return true;}
  public static EarthSave New(Catalog c){var s=new EarthSave{hero=new Unit(51,10,c)};s.pets.Add(new Unit(21,10,c));s.Add(141,6);s.Add(143,2);s.Add(146,3);s.Add(121,8);return s;}
  public bool Valid(Catalog c,World w){return (version==2||version==3)&&hero!=null&&hero.id==51&&hero.level>0&&pets!=null&&pets.Count>0&&pets.All(p=>p.id!=51&&c.entries.Any(e=>e.id==p.id))&&inventory!=null&&opened!=null&&visited!=null&&w.maps.Any(m=>m.id==map);}
 }
 public static class OriginalElements {
  // w.java.a(attackerElement, defenderElement, damage): wind -> thunder -> earth -> water -> fire -> wind.
  public static int Scale(int attacker,int defender,int damage){if(attacker==5||defender==5)return damage;int factor=(attacker+1)%5==defender?1250:(defender+1)%5==attacker?750:1000;return damage*factor/1000;}
 }
 public static class SaveFile {
  public static void Write(string path,EarthSave s){Directory.CreateDirectory(Path.GetDirectoryName(path));string tmp=path+".new";File.WriteAllText(tmp,JsonUtility.ToJson(s,true));if(File.Exists(path))File.Replace(tmp,path,null);else File.Move(tmp,path);}
 }
}
