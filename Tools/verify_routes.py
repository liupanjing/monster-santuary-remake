import json,pathlib,collections
B=pathlib.Path(__file__).resolve().parents[1];P=B/'Production';D=B/'Unity/Assets/Remake/Resources/Data';maps={m['id']:m for m in json.loads((D/'earth-world.json').read_text())['maps']}
def step(m,x,y,plane):
 xx=x*16+8;yy=y*16+8
 for t in m['layerTriggers']:
  a,b,c,d=t['rect']
  if xx+8>=a and xx-8<=c and yy+8>=b and yy-8<=d:plane=t['plane']
 return plane
def search(start):
 q=collections.deque([start]);parents={start:None};dist={start:0}
 while q:
  mid,x,y,z=q.popleft();m=maps[mid];nexts=[]
  for xx,yy in[(x+1,y),(x-1,y),(x,y+1),(x,y-1)]:
   zz=step(m,xx,yy,z)
   if 0<=xx<m['width'] and 0<=yy<m['height'] and m['attributes'][yy*m['width']+xx]in((0,1)if zz==0 else(0,2)):nexts.append((mid,xx,yy,zz))
  for p in m['portals']:
   if ((x,y)in list(zip(p['cells'][::2],p['cells'][1::2])) or p['x']==x and p['y']==y) and not p['outside']:
    a=next((a for a in maps[p['toMap']]['arrivals']if a['door']==p['door']),None)
    if a:nexts.append((p['toMap'],a['x'],a['y'],a['plane']))
  for n in nexts:
   if n not in parents:parents[n]=(mid,x,y,z);dist[n]=dist[(mid,x,y,z)]+1;q.append(n)
 return parents,dist
start=(9,10,15,0);parents,dist=search(start)
goal=(75,9,8,0);assert goal in parents,'Original maze route to guardian is disconnected'
def path_to(n,parents=parents):
 r=[]
 while n is not None:r.append({'map':n[0],'x':n[1],'y':n[2],'plane':n[3]});n=parents[n]
 return list(reversed(r))
path=path_to(goal);order=[]
for p in path:
 if not order or order[-1]!=p['map']:order.append(p['map'])
# Save deterministic routes for the actual runtime replay, not teleport-only assertions.
chestGoals=[(mid,c['x'],c['y'],c['plane'])for mid,m in maps.items()for c in m['chests']if c['source']=='2001.bin' and(mid,c['x'],c['y'],c['plane'])in parents]
chestGoal=min(chestGoals,key=lambda p:dist[p]);report={'statesReached':len(parents),'mapsReached':sorted(set(n[0]for n in parents)),'guardianSteps':len(path),'guardianRooms':order,'reachableResolvedChests':len(chestGoals),'resolvedChestCount':sum(c['source']=='2001.bin'for m in maps.values()for c in m['chests']),'interactableResolvedChests':sum(any(n[0]==mid and(n[1]-c['x'])**2+(n[2]-c['y'])**2<=1.6**2 for n in parents) for mid,m in maps.items()for c in m['chests']if c['source']=='2001.bin')}
(D/'smoke-route.json').write_text(json.dumps({'steps':path},indent=2));(D/'smoke-chest-route.json').write_text(json.dumps({'steps':path_to(chestGoal)},indent=2));(P/'route-validation.json').write_text(json.dumps(report,indent=2));print(report)

back,_=search(goal);assert start in back,'Return to entry disconnected'
(D/'smoke-return-route.json').write_text(json.dumps({'steps':path_to(start,back)},indent=2))
print('Return route steps',len(path_to(start,back)))
