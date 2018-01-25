package com.icefill.game.actors.actionActors;


import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.*;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.utils.Randomizer;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.effects.EffectActor;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.StatusTuple;

public class AbilityActor extends ActionActor {
	
	public String action_type;
	public String ai_type;
	public int action_level;
	private String area_type;
	public boolean target_empty_contain;
	public boolean friendly_contain;
	public boolean enemy_contain;
	public boolean obj_block;
	public boolean friendly_pass;
	public boolean wall_block;
	public boolean exp_flag;
	public boolean need_hire;
	public boolean do_not_execute_avoid_motion;
	public int targeting_min_range;
	public int targeting_max_range;
	public int targeting_type;
	public int splash_min_range;
	public int splash_max_range;
	public int splash_type;
	public int required_level;
	public int mana_cost;
	public int price;
	public boolean self_targeting;
	public boolean splash_empty_contain;
	public boolean splash_friendly_contain;
	public boolean splash_enemy_contain;
	public boolean splash_obj_block;
	public boolean center_contain;
	public boolean splash_center_contain;
	public boolean passive_action;
	private ArrayList<Motion> motions;
	private Boolean do_hit_motions=false;
	private Motion current_motion;
	private Image img;
	EffectActor reserved_effect=null;
	AbilityActor self= this;
	ArrayList<String> weapon_type;
	//private int motions_index=0;
	
	private int motion_index=0;
	private int motion_state=0;
	private int sub_motion_index=0;
	
	public AbilityActor()
	{}
	public AbilityActor(Factory factory){
		type=1;
		ap=1;
		action_type= factory.action_type;
		ai_type=factory.ai_type;
		if (ai_type==null)
			ai_type="melee";
		required_level=factory.required_level;
		action_name=factory.action_name;
		description=factory.description;
		action_type=factory.action_type;
		action_level=factory.action_level;
		short_name=factory.short_name;
		this.do_not_execute_avoid_motion=factory.do_not_execute_avoid_motion;
		center_contain=factory.center_contain;
		splash_center_contain=factory.splash_center_contain;
		target_empty_contain=factory.target_empty_contain;
		friendly_contain=factory.friendly_contain;
		enemy_contain=factory.enemy_contain;
	    obj_block=factory.obj_block;
	    friendly_pass=factory.friendly_pass;
	    wall_block=factory.wall_block;
		targeting_min_range=factory.targeting_min_range;
		targeting_max_range=factory.targeting_max_range;
		targeting_type=factory.targeting_type;
		splash_min_range=factory.splash_min_range;
		splash_max_range=factory.splash_max_range;
		splash_type=factory.splash_type;
		self_targeting=factory.self_targeting;
		splash_empty_contain=factory.splash_empty_contain;
		splash_friendly_contain=factory.friendly_contain;
		splash_enemy_contain=factory.splash_enemy_contain;
		splash_obj_block=factory.splash_obj_block;
		need_hire=factory.need_hire;
		price=factory.price;
		this.weapon_type=factory.weapon_type;
		this.cool_time=factory.cool_time;
		this.mana_cost=factory.mana_cost;
		//this.status_change_list=factory.status_change_list;
		tooltip.setText(this.toString());
		motions= new ArrayList<Motion>();
		for ( SubMotionName motion_names:factory.motions) {
			Motion temp_motion= new Motion(motion_names);
			if (temp_motion.status_change !=null) {
				temp_motion.status_change.setStatusType();
			}
			motions.add(new Motion(motion_names));
			
		}
		
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion(factory.icon_name));//new Texture("sprite/icon_attack.png");
		
	}
	public String getShortName()
	{
		return short_name;
	}
	//public StatusTuple getStatusTuple() {
	//	return motions.get(0).status_change;
	//}
	//public void addActorCount(){actor_count++; }
	//public void subtractActorCount() {actor_count--;}
	
	public void selectTarget(){}
	public void doAction(){}
	public boolean isSelected() {return selected;}
	public String getActionType() {
		return action_type;
	}
	public TextureRegion getIcon() {
		return icon_texture;
	}
	public Image getImage()
	{
		if (img== null)
		{
			img= new Image(icon_texture);
		}
		return img;
	}
	public int getTargetingRange(){ return targeting_max_range;}
	public int getTargetingType() { return targeting_type;}
	public int getSplashRange(){ return splash_max_range;}
	public int getSplashType() { return splash_type;}
	public void act(float delta){
		super.act(delta);
		
	}
	public void reserveEffect(EffectActor effect)
	{
		reserved_effect=effect;
	}
	public void initialize()
	{
		super.initialize();
		motion_index=0;
		motion_state=0;
		sub_motion_index=0;
	}
	public int MotionExecute(DungeonGroup room, ObjActor to_act,int level) {
		current_motion=motions.get(motion_index);
		
		ArrayList<Function> sub_motions;
		
		switch (motion_state) {
		case 0: sub_motions=current_motion.pre_motions;break;
		case 1: sub_motions=current_motion.motions; break;
		default: sub_motions=current_motion.motions;break; // Should not happen
		}
		if (do_hit_motions) {
			boolean hit=false;
			for (int i=room.area_computer.getTargetList().size()-1;i>=0;i--) {
				AreaCell temp_area=room.area_computer.getTargetList().get(i);
				if (this.hitOrMiss(room, to_act, temp_area,level) && current_motion.hit_motions!= null) {
					//current_motion.hit_motion.execute(room,to_act,this);
					hit=true;
				}
			}
			
			do_hit_motions=false;
		}		
		if (sub_motions.size()>sub_motion_index) {// have motions to execute
			if (!subaction_running) {
				sub_motions.get(sub_motion_index).execute(room,to_act,this,null);
				subaction_running=true;

			}
			else if (getActorCount()==0) { //All actors are deactivated
				sub_motion_index++;	
				subaction_running=false;
			}
		}
		else {
			  //All actions are done
				switch (motion_state) {
					case 0://pre_motion_done --> check hit_or_miss
						//actor_list.clear();
						resetActorCount();
						sub_motion_index=0;
						// calculate hit or miss and add action
						
						if (current_motion.status_change !=null){
							do_hit_motions=true;
						}
						 motion_state=1;
						return 0;
					case 1:
						if ((!to_act.isActing()) && room.area_computer.isTargetDone(room.getCurrentRoom())) {
						motion_state=0;
						//actor_list.clear();
						resetActorCount();
						sub_motion_index=0;
						return -1;
						}
						else return 1;
				//motion_done--> if hit -> state=2 else miss motion

				}
			
			return 0;
		}
		//else ==1: motion
		// else hit motion
		return 0;
	}

	public int executePassive(DungeonGroup room, ObjActor to_act,int level){
		motions.get(0).motions.get(0).execute(room, to_act, null, null);
		return -1;
	}

	public int execute(DungeonGroup room, ObjActor to_act,int level){
		if (motions.size()>motion_index) {
			// have motion to execute
			if (MotionExecute(room,to_act,level) == -1) {// check motion done
				motion_index++;
			}
			return 0;
		}
		else {// motions all done;
			motion_index=0;
			if (exp_flag==true)
			{
				exp_flag=false;
				to_act.gainExperience(3,null, room,false);
			}
			return -1;
		}
		
	}
	public boolean hitOrMiss (DungeonGroup room,ObjActor to_act,AreaCell target_area,int level) {
		ObjActor target= room.getCurrentRoom().getObj(target_area);
		if (target!=null)
		{
			//Random rn= new Random();
			if (current_motion.status_change.status_type.equals("healing"))
			{
				this.hit(room, to_act, target_area,level);
				exp_flag=true;
				return true;
			}
			else if (current_motion.status_change.status_type.equals("mana_change"))
			{
				this.hit(room, to_act, target_area,level);
				return true;
			}
			else if ( Randomizer.nextFloat()*100f < calculateHitRate(current_motion.status_change,level,to_act,target))
			{
				int block= calculateBlockRate(to_act,target);
				if (Randomizer.nextFloat()*100f < block)
				{	//Guard
					target.showStatusChange("BLOCK", 0);
					target.addAction(target.GuardAction(to_act, room));
					if (reserved_effect!=null)
					{
						reserved_effect.end();
						reserved_effect=null;
					}
					return false;
				}
				else 
				{
					
				// Hit
					exp_flag=true;
					if(to_act.getFrontRearOrBack(target)==2)
						to_act.showMessage("BACKSTAB!!!");
					this.hit(room, to_act, target_area,level);
					
					return true;
				}
			}
			else if (!this.do_not_execute_avoid_motion)
			{
				// Dodge
				target.showStatusChange("DODGE", 0);
				this.dodge(room, to_act, target_area);
				if (reserved_effect!=null)
				{
					reserved_effect.end();
					reserved_effect=null;
				}
				return false;
			}
		}
		return false;
	}

	public int calculateBlockRate(ObjActor to_act,ObjActor target)
	{
		Motion motion= current_motion;
		if (motion==null && motions!=null) motion=motions.get(0);
		
		if (motion!=null &&
				(
						motion.status_change.status_type.equals("physical_weapon1_damage") ||
						motion.status_change.status_type.equals("physical_weapon2_damage")||
						motion.status_change.status_type.equals("physical_damage")
				)
			)
		{
			if (target.getInventory()!=null && target.getInventory().getEquip(3)!=null)
			{
				int block=target.getInventory().getEquip(3).getStatus().BLOCK;
				int frb=to_act.getFrontRearOrBack(target);
				if (frb==0)
				{
					return  block;
				}
				else if (frb==2)
				{
					return 0;
				}
				else
				{
					return (int)(block*0.5);
				}
			
			
			}
		}
		
		return 0;
	}

	public int statusChange(DungeonGroup room,ObjActor to_act,ObjActor target,int level) {
		StatusTuple temp=current_motion.status_change;
		if (current_motion.status_change.status_type.equals("mana_change")) {
			Global.getPlayerTeam().increaseMana((int)current_motion.status_change.getAmount(level));
			return 0;
		}
		
		if (target != null){
			int damage=calculateDamage(temp,level,room,to_act,target);
			
			if (damage>0)
			{
				if (calculateCriticalRate(temp,level,to_act,target)>Randomizer.nextFloat()*100f)
				{
					damage=(int)(damage*1.5);
					ObjActions.getSubAction("screen_shake").execute(Global.dungeon,null,null,null);
					ObjActions.getSubAction("move_back").execute(Global.dungeon,to_act,null,Global.dungeon.getCurrentRoom().getCell(target));
					target.showStatusChange("CRITICAL!!!\nDAMAGE: "+Integer.toString(damage), 0);
					
				}
				else {
					target.showStatusChange("DAMAGE: "+Integer.toString(damage), 0);
				}
				target.inflictDamage(damage,to_act);
				
			}
			else if (damage<0){
				target.showStatusChange("HEALED: "+Integer.toString(-damage), 0);
				target.status.heal(-damage);
			}
			
			
			if (temp.getDuration(level)>0) {
				StatusTuple.TURNEFFECT temp_effect= new StatusTuple.TURNEFFECT(temp.turn_effect,temp.getEffect(level),damage, to_act,current_motion.end_motions,reserved_effect);
				target.addTurnEffect(temp_effect);
				reserved_effect=null;
			}
			return damage;
		}
		return 0;
	}
	public static int calculateDamage(StatusTuple temp,int level,DungeonGroup room,ObjActor to_act,ObjActor target)
	{
		int damage=0;
		damage= Randomizer.nextInt(calculateMinDamage(temp,level,room,to_act,target),calculateMaxDamage(temp,level,room,to_act,target));
		return damage;

	}
	public static int calculateMaxDamage(StatusTuple temp,int level,DungeonGroup room,ObjActor to_act,ObjActor target)
	{
		int damage=0;
		if (target != null&& temp.status_type!=null){
					if (temp.status_type.equals("physical_damage")) {
						damage=calculatePhysicalDamage(temp.getAmount(level),to_act,target);
										}
					else if (temp.status_type.equals("magic_damage")) {
						damage=calculateMagicDamage(temp.getAmount(level),to_act,target);
					} 
					else if (temp.status_type.equals("physical_weapon1_damage")) {
						damage=calculateMaxPhysicalWeaponDamage(temp.getAmount(level),to_act,target,2);
					}
					else if (temp.status_type.equals("physical_weapon2_damage")) {
						damage=calculateMaxPhysicalWeaponDamage(temp.getAmount(level),to_act,target,3);
					}
					
					else if (temp.status_type.equals("healing")) {
						damage=-(int)temp.getAmount(level);
					}
					return damage;
				}
		else return 0;
	}
	public static int calculateMinDamage(StatusTuple temp,int level,DungeonGroup room,ObjActor to_act,ObjActor target)
	{
		int damage=0;
		if (target != null&& temp.status_type!=null){
					if (temp.status_type.equals("physical_damage")) {
						damage=calculatePhysicalDamage(temp.getAmount(level),to_act,target);
										}
					else if (temp.status_type.equals("magic_damage")) {
						damage=calculateMagicDamage(temp.getAmount(level),to_act,target);
					} 
					else if (temp.status_type.equals("physical_weapon1_damage")) {
						damage=calculateMinPhysicalWeaponDamage(temp.getAmount(level),to_act,target,2);
					}
					else if (temp.status_type.equals("physical_weapon2_damage")) {
						damage=calculateMinPhysicalWeaponDamage(temp.getAmount(level),to_act,target,3);
					}
					
					else if (temp.status_type.equals("healing")) {
						damage=-(int)temp.getAmount(level);
					}
					return damage;
				}
		else return 0;
	}
	
	public static int calculatePhysicalDamage(float amount,ObjActor obj,ObjActor target)
	{
		int damage= (int)(amount-target.status.total_status.DEFENSE);
		if (damage<=0) damage=0;
		return damage;
	}
	
	public static int calculatePhysicalWeaponDamage(float amount,ObjActor obj,ObjActor target,int weapon_n) {
		int equip_n=2;
		int damage= Randomizer.nextInt(calculateMinPhysicalWeaponDamage(amount,obj,target,weapon_n)
				,calculateMaxPhysicalWeaponDamage(amount,obj,target,weapon_n));
		if (damage<=0) damage=0;
		return damage;
	}
	public static int calculateMaxPhysicalWeaponDamage(float amount,ObjActor obj,ObjActor target,int weapon_n){
		int equip_n=2;
		int damage=0;
		if (obj.getInventory().getEquip(weapon_n)!=null){
			
		damage= (int)(
							((float)amount/100.0f)
							*obj.getInventory().getEquip(weapon_n).getStatus().MAX_ATTACK
							*(1.0f+((float)obj.status.total_status.STR)/30)
						)
						-target.status.total_status.DEFENSE;
		}
		if (damage<=0) damage=0;
		return damage;
	}
	public static int calculateMinPhysicalWeaponDamage(float amount,ObjActor obj,ObjActor target,int weapon_n){
		int equip_n=2;
		
		int damage=0;
		if (obj.getInventory().getEquip(weapon_n)!=null){
		damage= (int)(
							((float)amount/100.0f)
							*obj.getInventory().getEquip(weapon_n).getStatus().MIN_ATTACK
							*(1.0f+((float)obj.status.total_status.STR)/30)
						)
						-target.status.total_status.DEFENSE;
		}
		if (damage<=0) damage=0;
		return damage;
	}
	public static int calculateMagicDamage(float amount,ObjActor obj,ObjActor target) {
		int damage= (int)(amount*(1+((float)obj.status.total_status.INT)/30));
		return damage;
	}
	public StatusTuple getStatusTuple() {
		return current_motion.status_change;
	}
	public StatusTuple getFirstStatusTuple() {
		if (motions.size() !=0)
		return motions.get(0).status_change;
		else
			return null;
	}
	public static float calculateCriticalRate(StatusTuple ability_status,int level,ObjActor obj,ObjActor target)
	{
		float hit_rate;
		if (ability_status.relative)
		{
			if (ability_status.base_weapon_type.equals("ARM1"))
			{
				hit_rate=ability_status.getCritical(level)*obj.getInventory().getEquip(2).getStatus().CRITICAL/100;
			}
			else if (ability_status.base_weapon_type.equals("ARM2"))
			{
				hit_rate=ability_status.getCritical(level)*obj.getInventory().getEquip(3).getStatus().CRITICAL/100;
			}
			else if (ability_status.base_weapon_type.equals("HEAD"))
			{
				hit_rate=ability_status.getCritical(level)*obj.getInventory().getEquip(0).getStatus().CRITICAL/100;
			}
			else if (ability_status.base_weapon_type.equals("BODY"))
			{
				hit_rate=ability_status.getCritical(level)*obj.getInventory().getEquip(1).getStatus().CRITICAL/100;
			}
			else
			{
				//Should not happen
				hit_rate=0;
			}
		}

	else hit_rate=ability_status.getCritical(level);
	int frb=obj.getFrontRearOrBack(target);
	switch (frb)
	{
	case 0:
		break;
	case 1:
	case 3:
		//hit_rate=(int)(hit_rate*1.2);
		break;
	case 2:
		hit_rate=hit_rate+25;
		break;
			
	}
	return hit_rate;
		
	}
	public static float calculateHitRate(StatusTuple ability_status,int level,ObjActor obj,ObjActor target) {
		float hit_rate;
		if (target.getType().equals("wall") || target.getType().equals("obstacle") )
			hit_rate=100;
		else if (ability_status.relative)
			{
				if (ability_status.base_weapon_type.equals("ARM1"))
				{
					hit_rate=ability_status.getAccuracy(level)*obj.getInventory().getEquip(2).getStatus().ACCURACY/100-target.status.total_status.DODGE;
				}
				else if (ability_status.base_weapon_type.equals("ARM2"))
				{
					hit_rate=ability_status.getAccuracy(level)*obj.getInventory().getEquip(3).getStatus().ACCURACY/100-target.status.total_status.DODGE;
				}
				else if (ability_status.base_weapon_type.equals("HEAD"))
				{
					hit_rate=ability_status.getAccuracy(level)*obj.getInventory().getEquip(0).getStatus().ACCURACY/100-target.status.total_status.DODGE;
				}
				else if (ability_status.base_weapon_type.equals("BODY"))
				{
					hit_rate=ability_status.getAccuracy(level)*obj.getInventory().getEquip(1).getStatus().ACCURACY/100-target.status.total_status.DODGE;
				}
				else
				{
					//Should not happen
					hit_rate=0;
				}
			}
		else hit_rate=ability_status.getAccuracy(level)-target.status.total_status.DODGE;
		
		if (hit_rate==0) hit_rate=3f;
		int frb=obj.getFrontRearOrBack(target);
		switch (frb)
		{
		case 0:	break;
		case 1:
		case 3:
			hit_rate=(int)(hit_rate*1.2);
			break;
		case 2:
			hit_rate=(int)(hit_rate*1.5);
			break;
				
		}
		return hit_rate;
	}
	public void hit(final DungeonGroup room,final ObjActor to_act,AreaCell target_area,int level) {
		ObjActor target=room.getCurrentRoom().getObj(target_area.getXX(),target_area.getYY());

		if (target!= null) {
			
				Sound hit_sound= Assets.getAsset("sound/hit.wav", Sound.class);
			if (current_motion.hit_motions !=null) {
				for(Function temp_function:current_motion.hit_motions)
					temp_function.execute(room,to_act,this,target_area);
			}
			SequenceAction seq= new SequenceAction();
			
			if (statusChange(room, to_act, target,level)>0) {
				//seq.addAction(target.basicHitAction(room,to_act));
				//hit_sound.play();
				//ObjActions.screenShakeAction.execute(room, to_act, this,null);
			}
			else
				seq.addAction(target.basicBuffAction(room,to_act));
			target.addAction(seq);
			target.checkDeadandExecuteDead(room);
			//target.setActingAction(this);	
		}
	}
	public void dodge(final DungeonGroup room,final ObjActor to_act,AreaCell target_area) {
		ObjActor target=room.getCurrentRoom().getObj(target_area.getXX(),target_area.getYY());
		if (target!= null) {
			SequenceAction seq= new SequenceAction();
			int direction=to_act.getDirectionToTarget(target.getXX(), target.getYY());
			Sound hit_sound= Assets.getAsset("sound/slash.wav", Sound.class);
			hit_sound.play(1f,1f,1f);

			seq.addAction(target.setDirectionAction(target.getOppositeDirection(direction)));
			seq.addAction(target.DodgeAction(to_act.getDirectionToTarget(target.getXX(),target.getYY()),room)
								);

			target.clearActions();
			target.addAction(seq);
		}
		
	} 
	public boolean checkWeaponType(ObjActor obj) {
		if (weapon_type == null)
			return true;
		else {
			if (obj.getInventory()!=null) {
				EquipActor equip1=obj.getInventory().getEquip(2);
				EquipActor equip2=obj.getInventory().getEquip(3);
				for (String equip_type:weapon_type) {
					if ((equip1!= null &&equip1.getTypeForAbility().equals(equip_type))||
						 (equip2!=null && equip2.getTypeForAbility().equals(equip_type))
						 ) {
						return true;
					}
				}
			}
			return false;
		}
	}
	public String toString(int level) {
		String to_return="name: "+this.action_name+"\n\n"+
						 this.description+"\n\n"+
						 "SKILL LEVEL :"+level+"\n"+
						 "TYPE        : "+this.action_type+"\n"+
						 "RANGE       : "+this.targeting_min_range+"~"+this.targeting_max_range+"\n"+
						 "SPLASH      : "+this.splash_min_range+"~"+this.splash_max_range+"\n\n"+
						 getDamagesInfo(level);
		return to_return;
	}
	public String toString2(int level) {
		String to_return="*name: "+this.action_name+"\n\n"+
				 "  "+this.description+"\n\n"
				 +"*TYPE        : "+this.action_type+"(LVL:"+this.action_level+")\n\n"
				 +"*REQUIRED_LEVEL:"+this.required_level+"\n\n";
				 //+ "REQUIRED LEVEL:"+(this.required_level)+"\n";
				if (level-1>=0) {
					to_return+=//"CURRENT LEVEL :"+(level-1)+"\n\n"+
							 //"RANGE       : "+this.targeting_min_range+"~"+this.targeting_max_range+"\n"+
							 //"SPLASH      : "+this.splash_min_range+"~"+this.splash_max_range+"\n\n"+
							 getDamagesInfo(level-1)+"\n";
				}
			/*
				to_return+="\n\nNEXT LEVEL :"+(level)+"\n\n"+
						 //"RANGE       : "+this.targeting_min_range+"~"+this.targeting_max_range+"\n"+
						 //"SPLASH      : "+this.splash_min_range+"~"+this.splash_max_range+"\n\n"+
						 getDamagesInfo(level);
			 */
		return to_return;
	}
	public String getDamagesInfo(int level) {
		String to_return="EFFECT: \n";
		if (motions!=null) {
			int i=1;
			for (Motion motion: motions) {
				if (motion.status_change!=null) {
					to_return+=
						"#"+i+motion.status_change.status_type+": "+motion.status_change.getAmount(level)+"("+motion.status_change.getAccuracy(level)+")\n";
				}
			}
		}
		return to_return;
	}
	public void draw(Batch batch, float delta){// For drawing icon
	super.draw(batch, delta);
	
	if (mana_cost>0) {
		Assets.getFont().setColor(Color.BLUE);
		Assets.getFont().draw(batch, "M:"+mana_cost, getX()+20, getY()+5);
		Assets.getFont().setColor(Color.WHITE);
	}
	if (cool_time>0) {
		Assets.getFont().setColor(Color.RED);
		Assets.getFont().draw(batch, "CT:"+cool_time, getX(), getY()+40);
		Assets.getFont().setColor(Color.WHITE);
	}
}
	public static class Factory {
		public String action_name;
		public String icon_name;
		public String description;
		public String action_type;
		public String ai_type;
		
		public int action_level;
		public String short_name;
		public boolean target_empty_contain;
		public boolean friendly_contain;
		public boolean center_contain;
		public boolean enemy_contain;
		public boolean obj_block;
		public boolean friendly_pass;
		public boolean wall_block;
		public boolean need_hire;
		public boolean do_not_execute_avoid_motion;
		public int targeting_min_range;
		public int targeting_max_range;
		public int targeting_type;
		public int splash_min_range;
		public int splash_max_range;
		public int splash_type;
		public int cool_time;
		public int mana_cost;
		public int required_level;
		public int price;
		public boolean self_targeting;
		public boolean splash_empty_contain;
		public boolean splash_friendly_contain;
		public boolean splash_center_contain;
		public boolean splash_enemy_contain;
		public boolean splash_obj_block;
		public ArrayList<String> weapon_type;
		//public ArrayList<String> motions_name;
		//public ArrayList<StatusTuple> status_change_list;
		public ArrayList<SubMotionName> motions;
	}
	public static class SubMotionName {
		public ArrayList<String> pre_motions;
		public ArrayList<String> motions;
		public ArrayList<String> hit_motions;
		public ArrayList<String> end_motions;
		public StatusTuple status_change;
	}
	
	public static class Motion {
		public ArrayList<Function> pre_motions;
		public ArrayList<Function> motions;
		public ArrayList<Function> hit_motions;
		public ArrayList<Function> end_motions;
		public StatusTuple status_change;
		public Motion (SubMotionName names) {
			pre_motions= new ArrayList<Function>();
			motions= new ArrayList<Function>();
			hit_motions= new ArrayList<Function>();
			end_motions= new ArrayList<Function>();
			if (names.hit_motions!=null)
			for (String temp_name:names.hit_motions) {
				hit_motions.add(ObjActions.getSubAction(temp_name));
			}
			
			this.status_change=names.status_change;
			if (names.pre_motions != null)
			for (String temp_name:names.pre_motions) {
				pre_motions.add(ObjActions.getSubAction(temp_name));
			}
			
			if (names.motions!=null)
			for (String temp_name:names.motions) {
				motions.add(ObjActions.getSubAction(temp_name));
			}
			if (names.end_motions!=null) {
				for (String temp_name:names.end_motions) {
					end_motions.add(ObjActions.getSubAction(temp_name));
				}
					
			}
		}
	}
	

}
