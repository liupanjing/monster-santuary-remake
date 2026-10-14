"""Package layered Seastar output for review; artwork is not generated here."""
from pathlib import Path
from PIL import Image
import json, hashlib
import package_xiya_v7_cut as atlas_review

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT/'Art/NPCs/xiya/v8'

def main():
    # Reuse the verified atlas decoder, fixed union crop and GIF packaging.
    atlas_review.OUT = OUT
    atlas_review.main()
    manifest = json.loads((OUT/'animation-manifest.json').read_text())
    tracking = json.loads((OUT/'seastar/grips.json').read_text())
    data = json.loads((OUT/'seastar/body.json').read_text())
    atlas = Image.open(OUT/'seastar/body-atlas.webp').convert('RGBA')
    seq = next(iter(data['animations'].values()))
    assert len(seq) == manifest['frames'] == len(tracking['frames'])
    offset = tracking['bodyOffset']; size = tracking['compositeCanvas']
    assert size == manifest['sourceCanvas']
    folder = OUT/'frames/body'; folder.mkdir(parents=True,exist_ok=True)
    images=[]
    for i,key in enumerate(seq):
        record=data['frames'][key];r=record['frame'];s=record['spriteSourceSize']
        assert not record['rotated']
        assert 0 <= r['x'] < r['x']+r['w'] <= atlas.width
        assert 0 <= r['y'] < r['y']+r['h'] <= atlas.height
        x,y=s['x']+offset[0],s['y']+offset[1]
        assert 0 <= x <= size[0]-r['w'] and 0 <= y <= size[1]-r['h']
        im=Image.new('RGBA',tuple(size))
        im.paste(atlas.crop((r['x'],r['y'],r['x']+r['w'],r['y']+r['h'])),(x,y))
        im.save(folder/f'{i:03d}.png');images.append(im)
    scales={round(row['swordScale'],8) for row in tracking['frames']}
    assert len(scales)==1
    angles=[row['angleDegreesUnwrapped'] for row in tracking['frames']]
    assert all(abs(b-a)<180 for a,b in zip(angles,angles[1:]))
    manifest.update(method='Decode provider composite and unarmed-body atlases with original frame order and offsets. One fixed review crop. Seastar generated body motion; provider composited one rigid sword with tracked grip positions and original hand occlusion. No local pose synthesis or artwork edits.',
        weaponLayers='Independent Seastar sword image and per-frame grip/angle/hand-occlusion records.',
        bodySequence=seq,bodyOffset=offset,rigidSwordScale=next(iter(scales)),
        bodyMetadata='seastar/body.json',bodyAtlas='seastar/body-atlas.webp',gripData='seastar/grips.json',swordImage='seastar/sword.webp',
        swordAngleUnwrapped=angles,humanApproved=False,formalGameIntegrated=False)
    reference=Image.open(ROOT/'Art/References/External2D/FantasyWarrior/source/Fantasy Warrior/Sprites/Attack3.png').convert('RGBA')
    cell=reference.width//8
    boxes=[reference.crop((i*cell,0,(i+1)*cell,reference.height)).getbbox() for i in range(8)]
    phase_map=[0,0,0,1,1,1,2,2,2,2,2,2,3,3,4,4,5,5,6,6,7,7]
    assert len(phase_map)==manifest['frames']
    manifest.update(status='motion_candidate_pending_human_review',sourceProviderVersion='v20',noSwordGlow=True,
        bodyVideo='seastar/draft1/body-source.mp4',
        assessment='全帧无剑光；高位尾柄沿护腕的明显重叠已修正。右手高位蓄力、身体前转和腰腿高度斜向收势可见。早期准备帧仍有源视频的手部运动软化，保留供视觉审核。',
        referenceSheet='../../../References/External2D/FantasyWarrior/source/Fantasy%20Warrior/Sprites/Attack3.png',
        referenceFrames=8,referenceCell=[cell,reference.height],
        referenceCrop=[max(0,min(b[0] for b in boxes)-2),max(0,min(b[1] for b in boxes)-2),min(cell,max(b[2] for b in boxes)+2),min(reference.height,max(b[3] for b in boxes)+2)],
        referencePhaseMap=phase_map,referenceMirroredForReview=True,
        referenceTiming='Approximate stage correspondence for inspection, not source author original game timing')
    manifest['fileSHA256']={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in (OUT/'seastar').iterdir() if p.is_file()}
    (OUT/'animation-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps({'bodyFrames':len(images),'singleSwordScale':next(iter(scales)),'noAngleWrapJumps':True,'humanApproved':False}))

if __name__=='__main__':main()
