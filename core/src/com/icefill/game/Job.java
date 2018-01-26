package com.icefill.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.icefill.game.actors.actionActors.ObjActions;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.sprites.ObjSprites;
import com.icefill.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.Random;

public class Job
{
  public String job_name;
  public String description;
  public int jp_need;
  public boolean dangerous;
  public boolean no_direction;
  public int[] str_modifier;
  public int base_str;
  public int[] dex_modifier;
  public int base_dex;
  public int[] int_modifier;
  public int base_int;
  public int base_hp;
  public int[] hp_modifier;
  public int ability_count;
  public Color color;
  public int fire_level;
  public int lightning_level;
  public int holy_level;
  public int unholy_level;
  public int price;
  public float runaway_ratio;
  public String[] ability_name;
  public String[] attainable_ability;
  public String[] attainable_passive_ability; 
  public String[] learnable_magic_type;
  public String[] changeable_job;
  public String move_ability;
  public ArrayList<EquipmentForLevel> equipments_for_level;
 // public String[] item_name;
  public float glow[];
  public ObjSprites default_sprites;
  public String type;
  public String ai_type;
  public String dead_ability;
  public Job()
  {
  }

  public Job(Factory factory)
  {
	this.no_direction=factory.no_direction;  
	this.dangerous=factory.dangerous;
	this.learnable_magic_type=factory.learnable_magic_type;
    this.job_name = factory.job_name;
    this.jp_need=factory.jp_need;
    this.description = factory.description;
    this.str_modifier = factory.str_modifier;
    this.dex_modifier = factory.dex_modifier;
    this.int_modifier = factory.int_modifier;
    this.base_str= factory.base_str;
    this.base_dex=factory.base_dex;
    this.base_int=factory.base_int;
    this.base_hp=factory.base_hp;
    this.hp_modifier = factory.hp_modifier;
    this.runaway_ratio=factory.runaway_ratio;
    this.default_sprites = ((ObjSprites)Assets.obj_sprites_map.get(factory.default_sprites));
    this.equipments_for_level = factory.equipments_for_level;
    if (equipments_for_level!=null) {
        for (EquipmentForLevel equipmentsForLevel : equipments_for_level) {
            if (equipmentsForLevel!=null) {
                for (String[] equipments :equipmentsForLevel.equipment_names) {
                    if (equipments!=null) {
                        for (String equipment:equipments ) {
                            if (equipment!=null && !Gdx.files.internal("objs_data/equipment/" + equipment + ".json").exists()) throw new RuntimeException("job "+job_name+"'s equipment "+equipment+" not exist.");
                        }
                    }
                }

            }
        }
    }
   // this.item_name = factory.item_name;

    this.ability_name = factory.ability_name;
    if (ability_name!=null) {
        for (String name :ability_name) {
            if (Assets.actions_map.get(name)==null) throw new RuntimeException("job "+job_name+"'s ability "+name+" not exist.");
        }
    }
    this.attainable_ability= factory.attainable_ability;
      if (attainable_ability!=null) {
          for (String name :attainable_ability) {
              if (Assets.actions_map.get(name)==null) throw new RuntimeException("job "+job_name+"'s ability "+name+" not exist.");
          }
      }
    this.attainable_passive_ability= factory.attainable_passive_ability;
      if (attainable_passive_ability!=null) {
          for (String name :attainable_passive_ability) {
              if (Assets.actions_map.get(name)==null) throw new RuntimeException("job "+job_name+"'s ability "+name+" not exist.");
          }
      }
    this.move_ability=factory.move_ability;
      if (move_ability!=null) {
          if (Assets.actions_map.get(move_ability)==null) throw new RuntimeException("job "+job_name+"'s ability "+move_ability+" not exist.");
      }
    this.ability_count = factory.ability_count;
    this.dead_ability=factory.dead_ability;
      if (dead_ability!=null) {
          if ( ObjActions.getSubAction(dead_ability)==null) throw new RuntimeException("job "+job_name+"'s dead ability "+dead_ability+" not exist.");
      }
    this.changeable_job=factory.changeable_job;
      if (changeable_job!=null) {
          for (String job_name :changeable_job) {
              if (!Gdx.files.internal("objs_data/job/").exists()) throw new RuntimeException("job "+job_name+"'s ability "+job_name+" not exist.");
          }
      }
    this.type = factory.type;
    this.price=factory.price;
    if (factory.ai_type==null)
    {
    	ai_type="melee";
    }
    else this.ai_type = factory.ai_type; 

    this.fire_level=factory.fire_level;
    this.lightning_level=factory.lightning_level;
    this.holy_level=factory.holy_level;
    this.unholy_level=factory.unholy_level;

    this.glow=factory.glow;
    if (factory.color!=null) {
        this.color = new Color(factory.color[0], factory.color[1], factory.color[2], factory.color[3]);
    }
    else this.color=Color.WHITE;

  }
  public int getJPNeed(){
	  return jp_need;
  }
  public int getHPModifier() {
	  if (hp_modifier!=null) {
			return  com.icefill.game.utils.Randomizer.nextInt(hp_modifier[0],hp_modifier[1]);
		}
	  else return 0;
		
  }
   public int getSTRModifier() {
	  if (str_modifier!=null) {
			return  com.icefill.game.utils.Randomizer.nextInt(str_modifier[0],str_modifier[1]);
		}
	  else return 0;
		
  }
  public int getDEXModifier() {
	  if (dex_modifier!=null) {
			return  com.icefill.game.utils.Randomizer.nextInt(dex_modifier[0],dex_modifier[1]);
		}
	  else return 0;
		
  }
  public int getINTModifier() {
	  if (int_modifier!=null) {
			return  com.icefill.game.utils.Randomizer.nextInt(int_modifier[0],int_modifier[1]);
		}
	  else return 0;
		
  }
  public int getHPForLevel(int level) {
	  int hp= base_hp;
	  for (int i=1;i<level;i++) {
		  hp+=getHPModifier();
	  }
	  return hp;
  }
  public int getSTRForLevel(int level) {
	  int str= base_str;
	  for (int i=1;i<level;i++) {
		  str+=getSTRModifier();
	  }
	  return str;
  }
  public int getDEXForLevel(int level) {
	  int dex= base_dex;
	  for (int i=1;i<level;i++) {
		  dex+=getDEXModifier();
	  }
	  return dex;
  }
  public int getINTForLevel(int level) {
	  int intelligence= base_int;
	  for (int i=1;i<level;i++) {
		  intelligence+=getINTModifier();
	  }
	  return intelligence;
  }

  public EquipmentForLevel getEquipmentForLevel(int level) {
  	if (equipments_for_level==null) return null;
  	ArrayList<EquipmentForLevel> equip_list=new ArrayList<EquipmentForLevel>();
  	for (EquipmentForLevel temp:equipments_for_level) {
  		if (temp.min_level<=level &&(temp.max_level>=level || temp.max_level==0)){
  			equip_list.add(temp);
  		}
  	}
  	if (equip_list.isEmpty()) {
  		equip_list.add(equipments_for_level.get(0));
  	}
  	
  	return equip_list.get(Randomizer.nextInt(equip_list.size()));
  }

  public static class Factory
  {
    String job_name;
    String type;
    String ai_type;
    
    String description;
    String default_sprites;
    int jp_need;
    boolean dangerous;
    boolean no_direction;
    int[] str_modifier;
    int transparent;
    float[] color;
    int base_str;
    int[] dex_modifier;
    int base_dex;
    int base_int;
    int[] int_modifier;
    int[] hp_modifier;
    int base_hp;
    int price;
    int fire_level;
    int lightning_level;
    int holy_level;
    int unholy_level;
    int ability_count;
    float runaway_ratio;
    float glow[];
    
    ArrayList<EquipmentForLevel> equipments_for_level;
    //String[] item_name;
    String[] ability_name;
    String[] attainable_ability;
    String[] attainable_passive_ability;
    String[] learnable_magic_type;
    String[] changeable_job;
    ArrayList<String> equippable_gear;
    
    String move_ability;
    String dead_ability;
  }
  public static class EquipmentForLevel {
	  int min_level;
	  int max_level;
	  ArrayList<String[]> equipment_names;
	  
	  public String[] chooseEquipmentSet() {
		  if (equipment_names.size()>1) {
			  Random rn= new Random();
			  int index= rn.nextInt(equipment_names.size());
			  return equipment_names.get(index);
		  }
		  else {
			  return equipment_names.get(0);
		  }
	  }
	  }
  }