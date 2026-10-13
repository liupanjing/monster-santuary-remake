using System;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
namespace MonsterRemake2D {
 public class ChapterUI {
  public RectTransform root,modal;public Camera camera;public Font font;public Text place,objective,resources,message,prompt;
  readonly Color ink=new Color(.065f,.08f,.085f,.97f),brown=new Color(.20f,.15f,.10f,.97f),gold=new Color(.85f,.68f,.37f),paper=new Color(.94f,.89f,.74f),muted=new Color(.66f,.68f,.61f);
  public ChapterUI(Camera world){
   font=Resources.Load<Font>("Fonts/NotoSansCJKsc-Regular");
   var o=new GameObject("2D interface",typeof(RectTransform),typeof(Canvas),typeof(CanvasScaler),typeof(GraphicRaycaster));o.layer=5;var canvas=o.GetComponent<Canvas>();
   camera=new GameObject("Interface camera").AddComponent<Camera>();camera.transform.position=new Vector3(0,0,-10);camera.orthographic=true;camera.cullingMask=1<<5;camera.clearFlags=CameraClearFlags.Depth;camera.depth=10;
   canvas.renderMode=RenderMode.ScreenSpaceCamera;canvas.worldCamera=camera;canvas.planeDistance=1;canvas.sortingOrder=100;
   var scaler=o.GetComponent<CanvasScaler>();scaler.uiScaleMode=CanvasScaler.ScaleMode.ScaleWithScreenSize;scaler.referenceResolution=new Vector2(1280,800);scaler.matchWidthOrHeight=.5f;root=o.GetComponent<RectTransform>();
   if(UnityEngine.Object.FindFirstObjectByType<EventSystem>()==null)new GameObject("UI input",typeof(EventSystem),typeof(StandaloneInputModule));
   Panel(root,0,0,1280,102,ink);Panel(root,0,102,1280,3,gold);Panel(root,0,695,1280,105,ink);Panel(root,0,695,1280,3,gold);
   place=Label(root,28,15,650,38,"怪兽仙境 · 土之秘境",27,true);objective=Label(root,29,56,850,30,"前往第三层，挑战秘境守护者。",17);
   resources=Label(root,880,22,372,65,"",17);resources.alignment=TextAnchor.UpperRight;
   message=Label(root,28,712,1020,43,"",19);prompt=Label(root,28,761,1000,24,"WASD / 方向键移动   E 调查   M 地图   Esc 队伍   F5 保存",14);prompt.color=muted;
  }
  public RectTransform Rect(RectTransform parent,string name,float x,float y,float w,float h){var o=new GameObject(name,typeof(RectTransform));o.layer=5;var r=o.GetComponent<RectTransform>();r.SetParent(parent,false);r.anchorMin=r.anchorMax=new Vector2(0,1);r.pivot=new Vector2(0,1);r.anchoredPosition=new Vector2(x,-y);r.sizeDelta=new Vector2(w,h);return r;}
  public RectTransform Panel(RectTransform parent,float x,float y,float w,float h,Color? color=null){var r=Rect(parent,"Panel",x,y,w,h);var i=r.gameObject.AddComponent<Image>();i.color=color??brown;i.raycastTarget=false;return r;}
  public Text Label(RectTransform parent,float x,float y,float w,float h,string body,int size=18,bool bold=false){var r=Rect(parent,"Label",x,y,w,h);var t=r.gameObject.AddComponent<Text>();t.font=font;t.text=body;t.fontSize=size;t.color=paper;t.fontStyle=bold?FontStyle.Bold:FontStyle.Normal;t.raycastTarget=false;t.horizontalOverflow=HorizontalWrapMode.Wrap;t.verticalOverflow=VerticalWrapMode.Truncate;return t;}
  public Button Button(RectTransform parent,float x,float y,float w,float h,string text,Action action,bool enabled=true){var r=Rect(parent,text,x,y,w,h);var i=r.gameObject.AddComponent<Image>();i.color=new Color(.28f,.23f,.16f,.98f);var b=r.gameObject.AddComponent<Button>();b.targetGraphic=i;b.onClick.AddListener(()=>action());b.interactable=enabled;var colors=b.colors;colors.normalColor=Color.white;colors.highlightedColor=new Color(1.15f,1.15f,1.15f);colors.pressedColor=new Color(.6f,.65f,.65f);colors.disabledColor=new Color(.5f,.5f,.5f,.55f);b.colors=colors;var l=Label(r,5,3,w-10,h-6,text,18);l.alignment=TextAnchor.MiddleCenter;return b;}
  public Image Picture(RectTransform parent,float x,float y,float w,float h,Sprite sprite){var r=Rect(parent,"Sprite picture",x,y,w,h);var i=r.gameObject.AddComponent<Image>();i.sprite=sprite;i.preserveAspect=true;i.raycastTarget=false;return i;}
  public RectTransform Modal(string heading,float w=950,float h=555){Clear();modal=Panel(root,(1280-w)/2,(800-h)/2,w,h,ink);var border=modal.gameObject.AddComponent<Outline>();border.effectColor=gold;border.effectDistance=new Vector2(2,-2);Label(modal,25,18,w-50,44,heading,26,true);return modal;}
  public void Clear(){if(modal!=null){UnityEngine.Object.Destroy(modal.gameObject);modal=null;}}
  public void Refresh(EarthSave s,EarthMap m,Items items,string msg,string near){place.text="怪兽仙境 · "+m.name;objective.text=s.bossDefeated?"已取得秘境碎片 · 返回入口交付认证物":"驯兽师考试 · 前往第三层，战胜秘境守护者";resources.text="金币  "+s.gold+"    封印球  "+(s.Count(121)+s.Count(122)+s.Count(123)+s.Count(124))+"\n已探索 "+s.visited.Count+" / 27 个区域";message.text=msg;prompt.text=string.IsNullOrEmpty(near)?"WASD / 方向键移动   E 调查   M 地图   Esc 队伍   F5 保存":near;}
 }
}
