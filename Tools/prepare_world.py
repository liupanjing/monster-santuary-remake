import json,pathlib,struct,shutil,collections
B=pathlib.Path(__file__).resolve().parents[1];O=B/'Reference/OriginalGame';P=B/'Production';U=B/'Unity';D=U/'Assets/Remake/Resources/Data';A=U/'Assets/Remake/Resources/Art';A.mkdir(exist_ok=True)
scenes={x['id']:x for x in json.loads((P/'original-scenes.json').read_text())};maps={x['id']:x for x in json.loads((O/'maps.json').read_text())};scripts=json.loads((O/'scripts.json').read_text());items={x['index']:x for x in json.loads((O/'items.json').read_text())}
active=None;names={}
for r in scripts['minit.bin']:
 if r['opcode']==59 and r['depth']==0:active=r['args'][2]
 if r['opcode']==51 and r['depth']==1 and '土之秘境' in r['text'][0]:names[active]=r['text'][0]
names[78]='土之秘境3层'
mid=None;aid=None;reward={}
for r in scripts['2001.bin']:
 if r['opcode']==59 and r['depth']==0:mid=r['args'][2];aid=None
 if r['opcode']==59 and r['depth']==1 and r['args'][0]==-2:aid=r['args'][2]
 if r['opcode']==14 and mid in names:reward[(mid,aid)]=(r['args'][0],r['args'][1])
def nearest(m,px,py,plane=0):
 w=m['width_tiles'];h=m['height_tiles'];ground=[(i%w,i//w)for i,v in enumerate(m['attributes'])if v in ((0,1)if plane==0 else (0,2))]
 return min(ground,key=lambda p:(p[0]+.5-px/16)**2+(p[1]+.5-py/16)**2)
def cell(m,a):return nearest(m,a['bounds'][0],a['bounds'][1]-4,a['params'][0] if a['params'] else 0)
world=[]
for mid in sorted(names):
 m=maps[mid];s=scenes[mid];w=m['width_tiles'];h=m['height_tiles'];
 # Compatibility overlay for map45: local collision data and visible floor geometry disagree.
 # Preserve original data in OriginalEvidence; export an explicit reversible overlay only.
 if mid==45:
  prior=list(m['attributes']);floorTiles={0,9,10,11,12,13,14,15,16,17,18,19,20,23,25,27,28,29,30,35,44}
  effective=[m['layers'][1][i] if m['layers'][1][i]!=-1 else m['layers'][0][i] for i in range(w*h)]
  m['attributes']=[0 if a in (3,255)and((v&1023)in floorTiles or(m['layers'][0][i]&1023)in floorTiles)else a for i,(a,v)in enumerate(zip(prior,effective))]
  (P/'map45-compatibility-overlay.json').write_text(json.dumps({'map':45,'reason':'Parsed local collision and rendered floor geometry do not align; playable 2D compatibility overlay. Exact original collision fidelity not claimed.','original':prior,'overlay':m['attributes'],'changedCells':sum(a!=b for a,b in zip(prior,m['attributes'])),'floorTileIDs':sorted(floorTiles)},indent=2))
 level=1 if '1层' in names[mid] else 2 if '2层' in names[mid] else 3
 ports=[];arr=[];chests=[];npcs=[];triggers=[]
 for a in s['actors']:
  x,y=cell(m,a);params=a['params'];kind=a['type'];idx=a['index'];key=f'{mid}:{idx}'
  if kind in ('layer0','layer1'):triggers.append({'rect':a['bounds'][2:],'plane':int(kind[-1])})
  if kind=='door_0' and len(params)>=4:
   dst=params[3];link=params[2]
   cells=[]
   l,t,r,b=a['bounds'][2:]
   for yy in range(h):
    for xx in range(w):
     if m['attributes'][yy*w+xx]in(0,1,2) and xx*16+16>=l and xx*16<=r and yy*16+16>=t and yy*16<=b:cells.extend([xx,yy])
   ports.append({'cells':cells,'rect':a['bounds'][2:],'key':key,'x':x,'y':y,'toMap':dst,'door':link,'outside':dst not in names,'hidden':mid==65 and dst==68,'plane':params[0]})
  if kind=='door_1' and len(params)>=4:arr.append({'x':x,'y':y,'door':params[2],'plane':params[0]})
  if kind in ('box','bigbox'):
   item,qty=reward.get((mid,idx),(0,0));chests.append({'key':key,'x':x,'y':y,'item':item,'quantity':qty,'name':items.get(item,{}).get('name','空宝箱'),'source':'2001.bin' if (mid,idx)in reward else 'unresolved','big':kind=='bigbox','plane':params[0]})
  if kind.startswith('npc') and idx>1 and 0<=a['bounds'][0]<w*16 and 0<=a['bounds'][1]<h*16:
   npcs.append({'key':key,'x':x,'y':y,'sourceScript':params[1] if len(params)>1 else -1,'type':kind,'plane':params[0]})
 if mid==75:npcs.append({'key':'guardian','x':9,'y':6,'sourceScript':1044,'type':'guardian','species':8})
 # Three floors retain original encounter definitions (mmp bytes fields follow h.java).
 path=O/'extracted/mmp'/f'{mid}.bin';data=path.read_bytes();count=struct.unpack_from('>h',data)[0];regions=[]
 for i in range(count):
  f=list(struct.unpack_from('>21h',data,2+i*42));regions.append({'bounds':f[:4],'minCount':f[4],'maxCount':f[5],'threshold':f[6],'minLevel':f[8],'maxLevel':f[9],'species':f[10:13],'drops':f[13:17],'rates':f[17:21]})
 world.append({'id':mid,'name':names[mid],'floor':level,'width':w,'height':h,'layer0':m['layers'][0],'layer1':m['layers'][1],'attributes':m['attributes'],'portals':ports,'arrivals':arr,'chests':chests,'npcs':npcs,'encounters':regions,'layerTriggers':triggers,'source':'original maps/scenes/mmp/minit/2001 resources'})
(D/'earth-world.json').write_text(json.dumps({'maps':world},ensure_ascii=False,indent=2))
(D/'items.json').write_text(json.dumps({'entries':[{'id':x['index'],'name':x.get('name','未恢复物品'),'description':x.get('description','')}for x in items.values() if 'name'in x]},ensure_ascii=False,indent=2))
# Recover essential original dialogue; omit in-script platform payment prompts.
lines=[]
for n in ['3032.bin','1044.bin']:
 ds=[{'speaker':r['text'][0],'body':r['text'][1]}for r in scripts[n]if r['opcode']==1]
 if n=='3032.bin':ds=[d for d in ds if '第三层找到' in d['body']or '施加一个标记' in d['body']]
 lines.append({'id':n,'lines':ds})
(D/'story.json').write_text(json.dumps({'sequences':lines},ensure_ascii=False,indent=2))
for n in ['tile1_0.png','box.png','bigbox.png','fbg1_0.png','fbg1_1.png']:shutil.copy2(O/'extracted'/n,A/n)
# The original NPC sprites remain source material, separate from all freshly drawn monsters.
for n in ['npc_fighter1','npc16','npc_xiya','npc_wolfman']:
 data=(O/'extracted'/(n+'.bin')).read_bytes();rect=struct.unpack_from('>4i',data,8)
 from PIL import Image
 im=Image.open(O/'extracted'/(n+'.png')).convert('RGBA');x,y,ww,hh=rect;im.crop((x,y,x+ww,y+hh)).save(A/(n+'.png'))
# Summarize source fidelity instead of inventing raw fields.
report={'mapCount':len(world),'floors':3,'portals':sum(len(m['portals'])for m in world),'chests':sum(len(m['chests'])for m in world),'resolvedChestRewards':sum(c['source']=='2001.bin'for m in world for c in m['chests']),'guardian':{'map':75,'positionPixels':[144,96],'species':8,'source':'minit opcode3 and 1044 opcode9'},'walkableAtGround':[0,1],'walkableAtUpper':[0,2],'blockedAtBoth':[3,255]}
(P/'world-source-report.json').write_text(json.dumps(report,ensure_ascii=False,indent=2));print(report)
# Portal graph determines a real original route; runtime will test cell-level walking as well.
q=collections.deque([(9,[9])]);seen=set()
while q:
 mid,path=q.popleft()
 if mid in seen:continue
 seen.add(mid)
 if mid==75:print('Original portal route to guardian:',path);break
 for p in next(x for x in world if x['id']==mid)['portals']:
  if not p['outside']:q.append((p['toMap'],path+[p['toMap']]))
