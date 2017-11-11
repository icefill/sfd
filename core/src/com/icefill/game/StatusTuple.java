package com.icefill.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.icefill.game.actors.EffectActor;
import com.icefill.game.actors.Function;
import com.icefill.game.actors.ObjActor;


public class StatusTuple {
	public String type;
	public String base_weapon_type;
	public boolean relative;
	public String turn_effect;
	public ArrayList<EFFECT> effect;
	public String status_type;
	public float getAmount(int level) {
		if (effect==null) return 0;
		else if (effect.size()>level) {
			return effect.get(level).amount;
		}
		else return effect.get(effect.size()-1).amount;
	}
	public float getAccuracy(int level) {
		if (effect==null) return 0;
		else if (effect.size()>level) {
			return effect.get(level).accuracy;
		}
		else return effect.get(effect.size()-1).accuracy;
	}
	public float getCritical(int level) {
		if (effect==null) return 0;
		else if (effect.size()>level) {
			return effect.get(level).critical;
		}
		else return effect.get(effect.size()-1).critical;
	}
	public void setStatusType() {
		if (effect!=null && !effect.isEmpty()) {
			for (EFFECT temp_effect:effect) {
				temp_effect.status_type=this.status_type;
			}
		}
	}
	public int getDuration(int level) {
		if (effect==null) return 0;
		else if (effect.size()>level) {
			return effect.get(level).duration;
		}
		else return effect.get(effect.size()-1).duration;
	}
	public EFFECT getEffect(int level) {
		if (effect!=null) {
			if (level>=effect.size())
				level=effect.size()-1;
			
			return effect.get(level);
		}
		else return null;
	}
	//poison
	//dont move
	//dont act
	//weak
	//
	public static class EFFECT {
		public String status_type;
		public int duration;
		public int odd_of_release;
		public int amount;
		public int accuracy;
		public int critical;
		public int range_modifier;
		public ObjActor.Status initial_status_change;
		public LinkedList<String> restricted_ability_type;
		public EFFECT(){}
	}
	public static class TURNEFFECT {
		public String turn_effect_name;
		public String status_type;
		public String release_message;
		public ObjActor caster;
		public EffectActor effect;
		public ArrayList<Function> end_motions;
		public int duration;
		public int current_turn;
		public int odd_of_release;
		public ObjActor.Status initial_status_change;
		public int amount;
		public LinkedList<String> restricted_ability_type;
		public TURNEFFECT(){}
		public TURNEFFECT(String turn_effect_name,EFFECT original,int amount,ObjActor caster,ArrayList<Function>end_motions,EffectActor effect) {
			this.status_type=original.status_type;
			this.duration=original.duration;
			this.initial_status_change= original.initial_status_change;
			this.current_turn=0;
			this.caster=caster;
			this.end_motions=end_motions;
			this.odd_of_release=original.odd_of_release;
			this.restricted_ability_type=original.restricted_ability_type;
			this.amount= amount;
			this.turn_effect_name=turn_effect_name;
			this.effect=effect;
		}
		public TURNEFFECT(String turn_effect_name,String status_type,int duration,ObjActor caster,Function end_motions,String release_message) {
			this.status_type=status_type;
			this.duration=duration;
			this.initial_status_change=null;
			this.current_turn=0;
			this.caster=caster;
			this.end_motions=new ArrayList<Function>();
			this.end_motions.add(end_motions);
			this.amount= 0;
			this.release_message=release_message;
			this.turn_effect_name=turn_effect_name;
		}
		
	}

}
