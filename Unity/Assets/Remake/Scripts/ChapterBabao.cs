using System;
using UnityEngine;

namespace MonsterRemake2D
{
    public partial class EarthGame2D
    {
        BabaoMotionLibrary2D babaoMotions;
        GameObject babaoPrefab;
        BabaoFollower2D babao;

        void LoadBabao()
        {
            babaoMotions=Resources.Load<BabaoMotionLibrary2D>("BabaoMotion/Animations");
            babaoPrefab=Resources.Load<GameObject>("BabaoMotion/Prefabs/BabaoFollower");
            if(babaoMotions==null||babaoPrefab==null)throw new Exception("Babao animations/prefab missing");
        }

        void SpawnBabao()
        {
            Vector3 leader=fieldHeroMotion.transform.position;
            var facing=BabaoFollower2D.Facing(direction);
            Vector3 spawn=leader;
            // Prefer behind the hero; a blocked doorway may require a side spawn.
            var sideways=new Vector3(-facing.y,facing.x,0);
            foreach(var offset in new[]{-facing,sideways,-sideways,facing}){
                var neighbor=Cell+new Vector2Int(Mathf.RoundToInt(offset.x),-Mathf.RoundToInt(offset.y));
                if(CanStep(neighbor,state.plane)){spawn=leader+offset*babaoMotions.followDistance;break;}
            }
            var instance=Instantiate(babaoPrefab,field.transform);instance.name="八宝 · 固定随行灵兽";
            babao=instance.GetComponent<BabaoFollower2D>();
            babao.Initialize(babaoMotions,leader,spawn,direction,state.plane);
        }

        void TickBabao()
        {
            if(babao!=null&&babao.gameObject.activeInHierarchy&&fieldHeroMotion!=null)
                babao.Advance(fieldHeroMotion.transform.position,moving?lastWalkPlane:state.plane,moving&&lastWalkWasRun,Time.deltaTime,Time.time);
        }

        bool lastWalkWasRun;
        int lastWalkPlane;
        public bool TrySetActivePet(int index)
        {
            if(state==null||mode==Mode.Battle||index<0||index>=state.pets.Count)return false;
            state.activePet=index;Save();return true;
        }
    }
}
