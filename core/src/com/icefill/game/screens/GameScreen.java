package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Output;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.HUDActor;
import com.icefill.game.RoomShapeType;
import com.icefill.game.actors.DungeonGroup.ObjListElt;

import java.util.ArrayList;


public class GameScreen extends BasicScreen
  implements Constants
{
  public GameScreen(SigmaFiniteDungeon game)
  {
    super(game);
    Global.setStage(this.stage);
    Global.setUIStage(this.ui_stage);
    Global.setHUD(new HUDActor());

    JsonReader json=new JsonReader();
    JsonValue dungeon_prop=json.parse(Gdx.files.internal("dungeon_prop.dat"));

    OrderedMap<String,RoomShapeType> room_type= new OrderedMap<String,RoomShapeType>();

    //Parse room type

    for (JsonValue jsonValue : dungeon_prop.get("room_type")){
        String room_type_name=jsonValue.get("name").asString();
        String floor_name=jsonValue.get("floor_name").asString();
        String stair_name=jsonValue.get("down_stair_name").asString();
        String wall_name=jsonValue.get("wall_name").asString();
        String door_name=jsonValue.get("door_name").asString();
        String fire_bowl_name=jsonValue.get("fire_bowl_name").asString();

        if (Assets.floor_map.get(floor_name)==null) throw new RuntimeException("Wrong floor name:"+floor_name+" in room_type:"+room_type_name);
        if (Assets.floor_map.get(stair_name)==null) throw new RuntimeException("Wrong stair name:"+floor_name+" in room_type:"+room_type_name);
        if (Assets.floor_map.get(wall_name)==null) throw new RuntimeException("Wrong wall name:"+wall_name+" in room_type:"+room_type_name);
        if (Assets.floor_map.get(door_name)==null) throw new RuntimeException("Wrong door name:"+door_name+" in room_type:"+room_type_name);
        if (Assets.jobs_map.get(fire_bowl_name)==null) throw new RuntimeException("Wrong fire bowl name:"+fire_bowl_name+" in room_type:"+room_type_name);
        room_type.put(room_type_name,new RoomShapeType(floor_name,stair_name,wall_name,door_name,fire_bowl_name));
    }

    String shrine_name=dungeon_prop.get("shrine").asString();
    if (Assets.jobs_map.get(shrine_name)==null) throw new RuntimeException("Wrong shrine name(Should be in jobs dir):"+shrine_name);

    ArrayList<String> char_name_list = new ArrayList<String>();
    for (JsonValue nameValue :dungeon_prop.get("initial_characters_name")){
        String name=nameValue.asString();
        if (!Gdx.files.internal("objs_data/chars/"+ name+".json").exists()) throw new RuntimeException("Character"+ name +"does not exsit!");
        else char_name_list.add(name);
    }


    DungeonGroup.MonsterPool recruit_pool= new DungeonGroup.MonsterPool();
    for (JsonValue poolNameValue :dungeon_prop.get("mercernaries")){
        Integer min=poolNameValue.get("min_floor").asInt();
        Integer max=poolNameValue.get("max_floor").asInt();
        JsonValue namesValue=poolNameValue.get("names");
        DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min,max);
        Boolean inserted=false;
        for (JsonValue eltValue :namesValue) {
            String name=eltValue.get("name").asString();
            if (Assets.jobs_map.get(name)==null) throw new RuntimeException("merc "+ name+" does not exsit." );
            else{
                int n=eltValue.get("mul").asInt();
                for (int i=0;i<n;i++)
                    pool_elt.add(new ObjListElt(null,name,eltValue.get("level").asInt(),1));
                inserted=true;
            }
        }
        if (inserted) recruit_pool.addMonsterPoolElt(pool_elt);

    }

      DungeonGroup.MonsterPool monster_pool= new DungeonGroup.MonsterPool();
      for (JsonValue poolNameValue :dungeon_prop.get("monsters")){
          Integer min=poolNameValue.get("min_floor").asInt();
          Integer max=poolNameValue.get("max_floor").asInt();
          JsonValue namesValue=poolNameValue.get("names");
          DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min,max);
          Boolean inserted=false;
          for (JsonValue eltValue :namesValue) {
              String name=eltValue.get("name").asString();
              if (Assets.jobs_map.get(name)==null) throw new RuntimeException("monster "+ name+" does not exsit." );
              else{
                  int n=eltValue.get("mul").asInt();
                  for (int i=0;i<n;i++)
                      pool_elt.add(new ObjListElt(null,name,eltValue.get("level").asInt(),1));
                  inserted=true;
              }
          }
          if (inserted) monster_pool.addMonsterPoolElt(pool_elt);

      }

      DungeonGroup.MonsterPool boss_pool= new DungeonGroup.MonsterPool();
      for (JsonValue poolNameValue :dungeon_prop.get("bosses")){
          Integer min=poolNameValue.get("min_floor").asInt();
          Integer max=poolNameValue.get("max_floor").asInt();
          JsonValue namesValue=poolNameValue.get("names");
          DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min,max);
          Boolean inserted=false;
          for (JsonValue eltValue :namesValue) {
              String name=eltValue.get("name").asString();
              if (Assets.jobs_map.get(name)==null) throw new RuntimeException("Boss "+ name+" does not exsit." );
              else{
                  int n=eltValue.get("mul").asInt();
                  for (int i=0;i<n;i++)
                      pool_elt.add(new ObjListElt(null,name,eltValue.get("level").asInt(),1));
                  inserted=true;
              }
          }
          if (inserted) boss_pool.addMonsterPoolElt(pool_elt);
      }

      DungeonGroup.ItemPool scroll_pool= new DungeonGroup.ItemPool();
      for (JsonValue poolNameValue :dungeon_prop.get("scrolls_in_chest")){
          Integer min=poolNameValue.get("min_floor").asInt();
          Integer max=poolNameValue.get("max_floor").asInt();
          JsonValue namesValue=poolNameValue.get("names");
          DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min,max);
          Boolean inserted=false;
          for (JsonValue eltValue :namesValue) {
              String name=eltValue.get("name").asString();
              if (name.startsWith("S#")) {
                  if (Assets.actions_map.get(name.substring(2)) == null)
                      throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++) pool_elt.add(name);
                      inserted=true;
                  }
              } else{
                  if (!Gdx.files.internal("objs_data/equipment/"+ name+".json").exists()) throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++)
                          pool_elt.add(name);
                      inserted = true;
                  }
              }
          }
          if (inserted) scroll_pool.addItemPoolElt(pool_elt);
      }

      DungeonGroup.ItemPool equip_pool= new DungeonGroup.ItemPool();
      for (JsonValue poolNameValue :dungeon_prop.get("weapons_in_chest")){
          Integer min=poolNameValue.get("min_floor").asInt();
          Integer max=poolNameValue.get("max_floor").asInt();
          JsonValue namesValue=poolNameValue.get("names");
          DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min,max);
          Boolean inserted=false;
          for (JsonValue eltValue :namesValue) {
              String name=eltValue.get("name").asString();
              if (name.startsWith("S#")) {
                  if (Assets.actions_map.get(name.substring(2)) == null)
                      throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++) pool_elt.add(name);
                      inserted=true;
                  }
              } else{
                  if (!Gdx.files.internal("objs_data/equipment/"+ name+".json").exists()) throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++)
                          pool_elt.add(name);
                      inserted = true;
                  }
              }
          }
          if (inserted) equip_pool.addItemPoolElt(pool_elt);
      }
      DungeonGroup.ItemPool item_pool= new DungeonGroup.ItemPool();
      for (JsonValue poolNameValue :dungeon_prop.get("items")){
          Integer min=poolNameValue.get("min_floor").asInt();
          Integer max=poolNameValue.get("max_floor").asInt();
          JsonValue namesValue=poolNameValue.get("names");
          DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min,max);
          Boolean inserted=false;
          for (JsonValue eltValue :namesValue) {
              String name=eltValue.get("name").asString();
              if (name.startsWith("S#")) {
                  if (Assets.actions_map.get(name.substring(2)) == null)
                      throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++) pool_elt.add(name);
                      inserted=true;
                  }
              } else{
                  if (!Gdx.files.internal("objs_data/equipment/"+ name+".json").exists()) throw new RuntimeException("item" + name.substring(2) + " does not exsit.");
                  else {
                      int n = eltValue.get("mul").asInt();
                      for (int i = 0; i < n; i++)
                          pool_elt.add(name);
                      inserted = true;
                  }
              }
          }
          if (inserted) item_pool.addItemPoolElt(pool_elt);
      }

    ArrayList explosive_obs_list = new ArrayList();
    for (JsonValue obs :dungeon_prop.get("explosive_obs_list")){
        String name=obs.get("name").asString();
        if (Assets.jobs_map.get(name)!=null) explosive_obs_list.add(new DungeonGroup.ObjListElt(null,name,1,-1));
        else throw new RuntimeException("explosive "+ name+" does not exsit." );
    }

    ArrayList dest_obs_list = new ArrayList();
    for (JsonValue obs :dungeon_prop.get("destructible_obs_list")){
        String name=obs.get("name").asString();
        if (Assets.jobs_map.get(name)!=null) dest_obs_list.add(new DungeonGroup.ObjListElt(null,name,1,-1));
        else throw new RuntimeException("obstacle "+ name+" does not exsit." );
    }
    ArrayList undest_obs_list = new ArrayList();
      for (JsonValue obs :dungeon_prop.get("undestructible_obs_list")){
          String name=obs.get("name").asString();
          if (Assets.jobs_map.get(name)!=null) undest_obs_list.add(new DungeonGroup.ObjListElt(null,name,5,-1));
          else throw new RuntimeException("obstacle "+ name+" does not exsit." );
      }

    DungeonGroup.DungeonSeed dungeonSeed = new DungeonGroup.DungeonSeed(room_type,char_name_list,recruit_pool, monster_pool,boss_pool,item_pool,equip_pool,scroll_pool,explosive_obs_list,undest_obs_list,dest_obs_list);
    DungeonGroup dungeon = new DungeonGroup(dungeonSeed, this, this.ui_stage);
    this.stage.addActor(dungeon);
    stage.setScrollFocus(dungeon);
    dungeon.setBounds(0F,0F, 1280.0F, 960.0F);
  }

  public void GameOver() {
	  game.setScreen(new GameOverScreen(game));
  }
  public void WinTheGame() {
	  game.setScreen(new WinScreen(game));
  }


}