package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Output;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.HUDActor;
import com.icefill.game.actors.ItemWindow;
import com.icefill.game.actors.DungeonGroup.DungeonSeed;
import com.icefill.game.actors.DungeonGroup.ObjListElt;
import com.icefill.game.actors.Floor;
import com.icefill.game.actors.devices.DeviceActor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class GameScreen extends BasicScreen
  implements Constants
{
//	Kryo kryo= new Kryo();
//	Output output;
	
  public GameScreen(SigmaFiniteDungeon game)
  {
    super(game);
    Global.setStage(this.stage);
    Global.setUIStage(this.ui_stage);
    
    Global.setHUD(new HUDActor());
    
    ArrayList<String> floor_list = new ArrayList<String>();
    floor_list.add("floor1");
    floor_list.add("down_stair_floor");
    
    
    ArrayList<String> wall_list= new ArrayList<String>(); 
    wall_list.add(null);
    wall_list.add("wall1");
    wall_list.add("light_wall1");
    wall_list.add("door_wall1");
    wall_list.add("up_stair_floor");
    
    //ArrayList wall_list= new ArrayList();
    //wall_list.add(arg0)
    
    ArrayList<String> char_list = new ArrayList<String>();
    char_list.add("proto");
    //char_list.add("novice_2");
    //char_list.add("novice_1");
    
    //char_list.add("novice_2");
    
    //char_list.add("jerry");
    
    //char_list.add("felix"); 
    //char_list.add("tom");
    //char_list.add("teresa");
    
    //char_list.add("engi");
    //char_list.add("magic_user1");
    //char_list.add("spike");
    //char_list.add("skeleton_warrior");
    
    DungeonGroup.MonsterPool recruit_pool= new DungeonGroup.MonsterPool();
    
    DungeonGroup.MonsterPoolElt temp = new DungeonGroup.MonsterPoolElt(1,1);
    
    temp.add(new ObjListElt(null,"novice",1,1));
    temp.add(new ObjListElt(null,"novice_bow",1,1));
    temp.add(new ObjListElt(null,"novice",1,1));
    temp.add(new ObjListElt(null,"novice_bow",1,1));
    temp.add(new ObjListElt(null,"apprentice_mage",1,1));
    
    //temp.add(new ObjListElt(null,"skeleton_mage",1,1));
    
    recruit_pool.addMonsterPoolElt(temp);
    
    temp = new DungeonGroup.MonsterPoolElt(2,2);
    temp.add(new ObjListElt(null,"novice",2,1));
    temp.add(new ObjListElt(null,"novice_bow",2,1));
    temp.add(new ObjListElt(null,"novice",2,1));
    temp.add(new ObjListElt(null,"novice_bow",2,1));
    temp.add(new ObjListElt(null,"apprentice_mage",2,1));
    recruit_pool.addMonsterPoolElt(temp);
    
    temp = new DungeonGroup.MonsterPoolElt(3,3);
    temp.add(new ObjListElt(null,"black_mage",1,1));
    temp.add(new ObjListElt(null,"knight",1,1));
    temp.add(new ObjListElt(null,"cleric",1,1));
    temp.add(new ObjListElt(null,"archer",1,1));
    temp.add(new ObjListElt(null,"spearman",1,1));
    recruit_pool.addMonsterPoolElt(temp);

    temp = new DungeonGroup.MonsterPoolElt(4,4);
    temp.add(new ObjListElt(null,"black_mage",2,1));
    temp.add(new ObjListElt(null,"knight",2,1));
    temp.add(new ObjListElt(null,"cleric",2,1));
    temp.add(new ObjListElt(null,"archer",2,1));
    temp.add(new ObjListElt(null,"spearman",2,1));
    recruit_pool.addMonsterPoolElt(temp);
    
    temp = new DungeonGroup.MonsterPoolElt(5,8);
    temp.add(new ObjListElt(null,"black_mage",2,1));
    temp.add(new ObjListElt(null,"knight",3,1));
    temp.add(new ObjListElt(null,"cleric",2,1));
    temp.add(new ObjListElt(null,"archer",3,1));
    temp.add(new ObjListElt(null,"spearman",2,1));
    recruit_pool.addMonsterPoolElt(temp);
    
    DungeonGroup.MonsterPool monster_pool= new DungeonGroup.MonsterPool();
    temp = new DungeonGroup.MonsterPoolElt(1,1);
      temp.add(new ObjListElt(null,"jelly",1,1));
    temp.add(new ObjListElt(null,"wolf",1,1));
    temp.add(new ObjListElt(null,"bat",1,1));
    temp.add(new ObjListElt(null,"zombie",1,1));
    temp.add(new ObjListElt(null,"jelly",1,1));
    temp.add(new ObjListElt(null,"wolf",1,1));
    temp.add(new ObjListElt(null,"bat",1,1));
    temp.add(new ObjListElt(null,"zombie",1,1));
    temp.add(new ObjListElt(null,"bandit",1,1));
    temp.add(new ObjListElt(null,"spider",1,1));
    temp.add(new ObjListElt(null,"goblin",1,1));
    temp.add(new ObjListElt(null,"skeleton_archer",1,1));
    
    monster_pool.addMonsterPoolElt(temp);
    
    temp = new DungeonGroup.MonsterPoolElt(2,2);
    //temp.add(new ObjListElt(null,"bandit_archer",1,1));
    
    temp.add(new ObjListElt(null,"zombie",3,1));
    temp.add(new ObjListElt(null,"bandit",2,1));
    temp.add(new ObjListElt(null,"goblin",1,1));
    temp.add(new ObjListElt(null,"goblin",2,1));
    temp.add(new ObjListElt(null,"bandit",2,1));
    temp.add(new ObjListElt(null,"goblin",1,1));
    temp.add(new ObjListElt(null,"goblin",2,1));
    temp.add(new ObjListElt(null,"spider",3,1));
    temp.add(new ObjListElt(null,"bandit_archer",1,1));
    
    temp.add(new ObjListElt(null,"skeleton_archer",2,1));
    //temp.add(new ObjListElt(null,"bat",3,1));
    
    
    monster_pool.addMonsterPoolElt(temp);
    
    temp = new DungeonGroup.MonsterPoolElt(3,3);
    temp.add(new ObjListElt(null,"goblin",3,1));
    temp.add(new ObjListElt(null,"goblin",3,1));
    
    temp.add(new ObjListElt(null,"skeleton",1,1));
    temp.add(new ObjListElt(null,"skeleton",2,1));
    temp.add(new ObjListElt(null,"bandit",3,1));
    temp.add(new ObjListElt(null,"bandit",3,1));
    
    temp.add(new ObjListElt(null,"bandit",3,1));
    
    temp.add(new ObjListElt(null,"goblin_shaman",1,1)); 
    temp.add(new ObjListElt(null,"lizard_man",1,1));
    temp.add(new ObjListElt(null,"lizard_man",1,1));
    temp.add(new ObjListElt(null,"bandit_archer",2,1));
    
    
    monster_pool.addMonsterPoolElt(temp);
    temp = new DungeonGroup.MonsterPoolElt(4,4);
    temp.add(new ObjListElt(null,"skeleton",3,1));
    temp.add(new ObjListElt(null,"goblin_shaman",1,1)); 
    temp.add(new ObjListElt(null,"goblin",4,1));
    temp.add(new ObjListElt(null,"goblin",4,1));
    
    temp.add(new ObjListElt(null,"goblin",4,1));
    
    temp.add(new ObjListElt(null,"skeleton",3,1));
    temp.add(new ObjListElt(null,"skeleton",4,1));
    temp.add(new ObjListElt(null,"skeleton_archer",3,1));
    temp.add(new ObjListElt(null,"lizard_man",4,1));
    temp.add(new ObjListElt(null,"lizard_man",3,1));
    temp.add(new ObjListElt(null,"bandit",4,1));
    temp.add(new ObjListElt(null,"bandit_archer",4,1));
    temp.add(new ObjListElt(null,"bandit",4,1));temp.add(new ObjListElt(null,"bandit",4,1));
    
    temp.add(new ObjListElt(null,"goblin_shaman",2,1)); 
    
    
    monster_pool.addMonsterPoolElt(temp);
    temp = new DungeonGroup.MonsterPoolElt(5,5);
    temp.add(new ObjListElt(null,"skeleton",5,1));
    temp.add(new ObjListElt(null,"skeleton",5,1));
    temp.add(new ObjListElt(null,"lizard_man",5,1));
    temp.add(new ObjListElt(null,"lizard_man",5,1));
    temp.add(new ObjListElt(null,"lizard_man",5,1));
    temp.add(new ObjListElt(null,"skeleton",4,1));
    temp.add(new ObjListElt(null,"skeleton",4,1));
    temp.add(new ObjListElt(null,"goblin_shaman",5,1)); 
    temp.add(new ObjListElt(null,"skeleton_mage",1,1));
    temp.add(new ObjListElt(null,"skeleton_mage",1,1));
    temp.add(new ObjListElt(null,"skeleton_archer",5,1));
    temp.add(new ObjListElt(null,"bandit_archer",5,1));
    temp.add(new ObjListElt(null,"spider",7,1));
    
    temp.add(new ObjListElt(null,"spider",7,1));
    monster_pool.addMonsterPoolElt(temp);
    
    DungeonGroup.MonsterPool boss_pool= new DungeonGroup.MonsterPool();
    temp = new DungeonGroup.MonsterPoolElt(1,1);
    temp.add(new ObjListElt(null,"king jelly",3,1));
    temp.add(new ObjListElt(null,"goblin_leader",3,1));
    
    boss_pool.addMonsterPoolElt(temp);
    temp = new DungeonGroup.MonsterPoolElt(2,2);
    temp.add(new ObjListElt(null,"cyclops",3,1));
    temp.add(new ObjListElt(null,"goblin_witch",3,1));
    
    boss_pool.addMonsterPoolElt(temp);
    temp = new DungeonGroup.MonsterPoolElt(3,3);
    temp.add(new ObjListElt(null,"cyclops",5,1));
    temp.add(new ObjListElt(null,"goblin_witch",5,1));
    
    boss_pool.addMonsterPoolElt(temp);
    temp = new DungeonGroup.MonsterPoolElt(4,10);
    temp.add(new ObjListElt(null,"lich",9,1));
    boss_pool.addMonsterPoolElt(temp);
    
    
    DungeonGroup.ItemPool scroll_pool= new DungeonGroup.ItemPool();
    
    DungeonGroup.ItemPoolElt temp_elt= new DungeonGroup.ItemPoolElt(1,2);
    temp_elt.add("S#Fireball");
    temp_elt.add("S#Energy ray");
    temp_elt.add("S#Decay");
    temp_elt.add("S#Lightning");
    temp_elt.add("S#Explosion");
    temp_elt.add("S#Explosion");
    temp_elt.add("S#Burn");
    temp_elt.add("S#Burn");
    temp_elt.add("S#Heal"); 
    temp_elt.add("S#Summon jelly");
    
    scroll_pool.addItemPoolElt(temp_elt);
    
    temp_elt=new DungeonGroup.ItemPoolElt(3,4);
    temp_elt.add("S#Fireball");
    temp_elt.add("S#Acidball");
    temp_elt.add("S#Fireball");
    temp_elt.add("S#Acidball");
    temp_elt.add("S#Decay");
    temp_elt.add("S#Area Heal");
    temp_elt.add("S#Energy ray");
    temp_elt.add("S#Chain lightning");
    temp_elt.add("S#Summon skeleton");
    scroll_pool.addItemPoolElt(temp_elt);
    
    temp_elt=new DungeonGroup.ItemPoolElt(4,5);
    temp_elt.add("S#Chain lightning");
    temp_elt.add("S#Summon skeleton");
    temp_elt.add("S#Energy ray");
    temp_elt.add("S#Decay");
    temp_elt.add("S#Meteor");
    
    scroll_pool.addItemPoolElt(temp_elt);
    DungeonGroup.ItemPool equip_pool= new DungeonGroup.ItemPool();
    
    temp_elt= new DungeonGroup.ItemPoolElt(1,1);
    temp_elt.add("axe0");
    temp_elt.add("staff1");
    temp_elt.add("short_sword2");
    temp_elt.add("leather_armor2");
    temp_elt.add("robe1");
    temp_elt.add("shield1");
    temp_elt.add("bow0");
    equip_pool.addItemPoolElt(temp_elt);
    
    temp_elt= new DungeonGroup.ItemPoolElt(1,3);
    temp_elt.add("axe1");
    temp_elt.add("sabre1");
    temp_elt.add("staff2");
    temp_elt.add("leather_armor3");
    temp_elt.add("shield2");
    temp_elt.add("spear"); 
    temp_elt.add("bow1");
    temp_elt.add("robe2");
    
    equip_pool.addItemPoolElt(temp_elt);
    
    temp_elt=new DungeonGroup.ItemPoolElt(3,4);
    temp_elt.add("armor1");
    temp_elt.add("bow2");
    temp_elt.add("axe2");
    temp_elt.add("spear"); 
    temp_elt.add("mace1"); 
    temp_elt.add("long_sword1");
    temp_elt.add("shield3");
    equip_pool.addItemPoolElt(temp_elt);
    
    temp_elt=new DungeonGroup.ItemPoolElt(4,5);
    temp_elt.add("armor2");
    temp_elt.add("bow3");
    temp_elt.add("axe3");
    temp_elt.add("long_sword2");
    temp_elt.add("shield4");
    temp_elt.add("mace2");
    temp_elt.add("robe3");
    
    temp_elt=new DungeonGroup.ItemPoolElt(5,6);
    temp_elt.add("armor3");
    temp_elt.add("axe4");
    temp_elt.add("long_sword3");
    temp_elt.add("mace3");
    
    temp_elt.add("shield5");
    temp_elt.add("mace3");
    
    temp_elt.add("robe4");
    
    equip_pool.addItemPoolElt(temp_elt);
    
    
    DungeonGroup.ItemPool item_pool= new DungeonGroup.ItemPool();
    temp_elt= new DungeonGroup.ItemPoolElt(1,2);
    
    
    temp_elt.add("mana_stone");
    temp_elt.add("mana_stone");
    temp_elt.add("bomb");
    temp_elt.add("bomb");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("mana_stone");
    temp_elt.add("S#Ice wall");
    temp_elt.add("S#Teleport");
    temp_elt.add("S#Throw web");
    temp_elt.add("S#Throw web");
    temp_elt.add("str_up_potion");
    temp_elt.add("def_up_potion");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_axe");
    
    item_pool.addItemPoolElt(temp_elt);
    temp_elt=new DungeonGroup.ItemPoolElt(3,4);
    temp_elt.add("mana_stone");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_axe");
    temp_elt.add("S#Ice wall");
    temp_elt.add("S#Throw web");
    temp_elt.add("S#Throw web");
    temp_elt.add("S#Teleport");
    temp_elt.add("bomb");
    temp_elt.add("bomb");
    temp_elt.add("mana_stone");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("str_up_potion");
    temp_elt.add("def_up_potion");
    
    item_pool.addItemPoolElt(temp_elt);
    temp_elt=new DungeonGroup.ItemPoolElt(5,6);
    temp_elt.add("mana_stone");
    temp_elt.add("S#Ice wall");
    temp_elt.add("S#Throw web");
    temp_elt.add("S#Throw web");
    
    temp_elt.add("mana_stone");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("healing_potion_10");
    temp_elt.add("throwing_dagger");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_dagger");
    temp_elt.add("throwing_axe");
    temp_elt.add("throwing_axe");
    temp_elt.add("str_up_potion");
    temp_elt.add("def_up_potion");
    
    
    item_pool.addItemPoolElt(temp_elt);
    
    
    
    
    
    
    //ArrayList<DungeonGroup.ObjListElt> obj_list = new ArrayList<DungeonGroup.ObjListElt>();
    //obj_list.add(null);
    //obj_list.add(new DungeonGroup.ObjListElt(null, "skeleton", 2, 1));
    //obj_list.add(new DungeonGroup.ObjListElt(null, "skeleton_mage", 1, 1));
    //obj_list.add(new DungeonGroup.ObjListElt(null, "skeleton_mage", 3, 1));
    //obj_list.add(new DungeonGroup.ObjListElt(null, "skeleton_archer", 2, 1));
    
    //obj_list.add(new DungeonGroup.ObjListElt(null, "skeleton_mage_boss", 12, 1));
    //ArrayList<DeviceActor> device_list = new ArrayList<DeviceActor>();
    
    ArrayList obs_list = new ArrayList();
    obs_list.add(null);
   // obs_list.add(new DungeonGroup.ObjListElt(-1, null, "wall", "wall1", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "knight_statue", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    //obs_list.add(new DungeonGroup.ObjListElt(null, "hermit_statue", 1, -1));
    //obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle1", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null,"candle_stand", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle2", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle2", 1, -1));
    //obs_list.add(new DungeonGroup.ObjListElt(null, "shrine", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "wall2", 5, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle2", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle2", 1, -1));
    obs_list.add(new DungeonGroup.ObjListElt(null, "obstacle1", 1, -1));
    
    
    DungeonGroup.DungeonSeed df = new DungeonGroup.DungeonSeed(char_list,recruit_pool, monster_pool,boss_pool,item_pool,equip_pool,scroll_pool,obs_list, floor_list,wall_list);
    DungeonGroup dungeon = new DungeonGroup(df, this, this.ui_stage);
    /*
    try {
		output = new Output(new FileOutputStream("file.bin"));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		output=null;
	}
    kryo.writeObject(output,dungeon);*/
    //DungeonGroup dungeon= new DungeonGroup("sample_map.json",this,ui_stage);
    this.stage.addActor(dungeon);
    stage.setScrollFocus(dungeon);
    dungeon.setBounds(0F,0F, 1280.0F, 960.0F);
  }
  public void saveState() {
	//  kryo.writeObject(output,df);
  }

  
  public void GameOver() {
	  game.setScreen(new GameOverScreen(game));
  }
  public void WinTheGame() {
	  game.setScreen(new WinScreen(game));
  }
  
}