import pathlib,json,struct
B=pathlib.Path(__file__).resolve().parents[1];O=B/'Reference/OriginalGame';E=O/'extracted'
class R:
 def __init__(self,p):self.data=p.read_bytes();self.pos=0
 def n(self,fmt):v=struct.unpack_from('>'+fmt,self.data,self.pos)[0];self.pos+=struct.calcsize(fmt);return v
 def utf(self):n=self.n('H');d=self.data[self.pos:self.pos+n];self.pos+=n;return d.decode('utf8').replace('\x00','')
r=R(E/'storm.bin');names=[r.utf()for _ in range(r.n('H'))];r=R(E/'O0.binz');tiles=[r.utf()for _ in range(r.n('H'))]
scenes=[]
for p in sorted((E/'scenes').glob('*.bin'),key=lambda p:int(p.stem)):
 r=R(p);head=[r.n('i')for _ in range(r.n('H'))];actors=[]
 for i in range(r.n('H')):
  length=r.n('h');kind=r.n('B');anim=r.n('B');flags=r.n('B');skip=r.n('B');xy=[r.n('h')for _ in range(6)];params=[r.n('h')for _ in range((length-16)//2)]
  actors.append({'index':i,'type':names[kind],'anim':anim,'flags':flags,'bounds':xy,'params':params})
 scenes.append({'id':int(p.stem),'header':head,'actors':actors,'unusedTailBytes':len(r.data)-r.pos})
(B/'Production/original-scenes.json').write_text(json.dumps(scenes,ensure_ascii=False,indent=2))
ms=json.loads((O/'maps.json').read_text())
for mid in [5,7,8,9,61,62,63,64,65,66,72,74,75,76,77,78,79,80,81]:
 m=ms[mid];s=scenes[mid];print('MAP',mid,m['width_tiles'],m['height_tiles'],tiles[m['tileset_index']]);print([(a['index'],a['type'],a['bounds'][:2],a['params'])for a in s['actors']if a['type'].startswith(('door','box','bigbox','npc','s27'))])
