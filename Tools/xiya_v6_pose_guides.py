"""Draw technical joint constraints as imagegen inputs, not character artwork."""
from pathlib import Path
from PIL import Image,ImageDraw
import json
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'Art/NPCs/xiya/v6/guides'
# Each tuple: pelvis, upper chest, head, right elbow, right fist, sword tip,
# front knee, rear knee. Feet are the pivot anchors; knees/hips/head move.
CUT=[
[(250,330),(250,240),(250,195),(215,266),(190,255),(205,130),(224,379),(302,382)],
[(269,337),(283,247),(288,204),(300,220),(277,181),(330,65),(237,386),(315,383)],
[(279,343),(300,252),(306,209),(339,198),(309,150),(340,28),(237,387),(322,390)],
[(259,344),(255,252),(251,207),(292,218),(247,166),(239,41),(214,390),(292,392)],
[(244,352),(220,267),(209,226),(190,251),(151,253),(27,251),(189,391),(282,399)],
[(238,360),(207,278),(190,238),(166,293),(141,329),(141,454),(184,396),(274,403)],
[(238,363),(203,283),(183,244),(157,297),(132,338),(132,463),(181,398),(274,403)],
[(242,355),(219,270),(207,229),(185,298),(162,301),(52,361),(195,392),(278,395)],
[(247,341),(238,251),(232,207),(199,270),(184,258),(101,164),(218,382),(294,384)],
[(250,330),(250,240),(250,195),(215,266),(190,255),(205,130),(224,379),(302,382)]]
BLOCK=[
[(250,330),(250,240),(250,195),(215,266),(190,255),(205,130),(224,379),(302,382)],
[(259,338),(272,249),(278,208),(243,227),(212,213),(123,123),(221,387),(306,389)],
[(261,345),(266,258),(272,215),(230,224),(192,193),(68,207),(214,391),(307,395)],
[(267,351),(278,266),(289,224),(238,227),(204,191),(80,207),(211,395),(313,399)],
[(283,357),(307,276),(326,239),(282,232),(248,203),(124,217),(223,398),(320,403)],
[(279,354),(296,271),(312,233),(272,231),(235,201),(111,215),(219,396),(317,401)],
[(265,351),(272,263),(281,222),(231,251),(187,234),(64,212),(213,393),(303,398)],
[(254,343),(254,254),(254,210),(213,270),(185,252),(118,146),(217,387),(299,390)],
[(250,334),(249,244),(249,200),(216,269),(190,257),(205,132),(222,382),(301,384)],
[(250,330),(250,240),(250,195),(215,266),(190,255),(205,130),(224,379),(302,382)]]
for name,poses in [('downcut',CUT),('parry',BLOCK)]:
 board=Image.new('RGB',(2500,1000),'white');draw=ImageDraw.Draw(board)
 for i,pose in enumerate(poses):
  ox=(i%5)*500;oy=(i//5)*500
  def p(v):return (v[0]+ox,v[1]+oy)
  hip,chest,head,elbow,fist,tip,k1,k2=pose
  front=(205,438);rear=(333,438)
  # Head and shoulder chain are explicitly coupled to the moving chest.
  neck=(chest[0]+(head[0]-chest[0])*.30,chest[1]-22)
  upper=(chest[0]+14,chest[1]-3);far=(chest[0]-14,chest[1]+3)
  left_elbow=(chest[0]+38,chest[1]+38);left_fist=(hip[0]+23,hip[1]-30)
  for line,col in [([hip,chest,neck,head],'#476d9d'),([upper,elbow,fist],'#bc6b44'),([far,left_elbow,left_fist],'#82a0b9'),([hip,k1,front],'#476d9d'),([hip,k2,rear],'#82a0b9')]:draw.line([p(v) for v in line],fill=col,width=6)
  draw.line([p((chest[0]-28,chest[1])),p((chest[0]+28,chest[1]))],fill='#476d9d',width=6)
  draw.line([p((hip[0]-20,hip[1])),p((hip[0]+20,hip[1]))],fill='#476d9d',width=6)
  draw.ellipse([head[0]+ox-20,head[1]+oy-25,head[0]+ox+20,head[1]+oy+25],outline='#476d9d',width=4)
  draw.line([p(fist),p(tip)],fill='#222a30',width=6)
  dx,dy=tip[0]-fist[0],tip[1]-fist[1];L=(dx*dx+dy*dy)**.5;guard=(fist[0]+dx/L*9,fist[1]+dy/L*9)
  draw.line([p((guard[0]-dy/L*13,guard[1]+dx/L*13)),p((guard[0]+dy/L*13,guard[1]-dx/L*13))],fill='#222a30',width=4)
  for x,y in [hip,chest,elbow,fist,k1,k2]:draw.ellipse([x+ox-5,y+oy-5,x+ox+5,y+oy+5],fill='#d29b59')
  for v in [front,rear]:draw.line([p((v[0]-15,v[1])),p((v[0]+17,v[1]))],fill='#476d9d',width=5)
 board.save(OUT/(name+'-joint-guide.png'))
 (OUT/(name+'-joints.json')).write_text(json.dumps({'purpose':'technical joint guide, not character art','grid':[5,2],'cell':[500,500],'physicalSwordLengthApproximately':125,'frontFoot':[205,438],'rearToe':[333,438],'poses':poses},indent=2)+'\n')
print('Technical guides prepared; not game sprites')
