Shader "MonsterRemake/ReviewLayerComposite"
{
 Properties { [PerRendererData] _MainTex("Body",2D)="white"{} _WeaponTex("Weapon",2D)="black"{} _FingerTex("Fingers",2D)="black"{} _ShowWeapon("Show weapon",Float)=1 }
 SubShader {
  Tags {"Queue"="Transparent" "RenderType"="Transparent" "CanUseSpriteAtlas"="False"}
  Cull Off Lighting Off ZWrite Off Blend One OneMinusSrcAlpha
  Pass {
   CGPROGRAM
   #pragma vertex vert
   #pragma fragment frag
   #include "UnityCG.cginc"
   struct Input {float4 vertex:POSITION; float2 uv:TEXCOORD0; float4 color:COLOR;};
   struct Output {float4 vertex:SV_POSITION;float2 uv:TEXCOORD0;float4 color:COLOR;};
   sampler2D _MainTex,_WeaponTex,_FingerTex;float _ShowWeapon;
   Output vert(Input v){Output o;o.vertex=UnityObjectToClipPos(v.vertex);o.uv=v.uv;o.color=v.color;return o;}
   // The three registered source masks are disjoint. Sum their premultiplied
   // coverage in one pass so bilinear edges do not expose the background twice.
   fixed4 frag(Output i):SV_Target {
    fixed4 a=tex2D(_MainTex,i.uv), b=tex2D(_WeaponTex,i.uv), c=tex2D(_FingerTex,i.uv);
    b.a*=_ShowWeapon;
    fixed alpha=saturate(a.a+b.a+c.a)*i.color.a;
    return fixed4((a.rgb*a.a+b.rgb*b.a+c.rgb*c.a)*i.color.rgb*i.color.a,alpha);
   }
   ENDCG
  }
 }
}
