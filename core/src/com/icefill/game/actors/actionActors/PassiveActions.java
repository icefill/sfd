package com.icefill.game.actors.actionActors;

import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.Constants;
import com.icefill.game.Global;

import java.util.HashMap;

public class PassiveActions implements Constants {
	 public static HashMap<String, PassiveFunction> subaction_map =new HashMap<String,PassiveFunction>();
	  
	public PassiveActions() {
		// TODO Auto-generated constructor stub
		
	}
	public static void initializeSubActionMap() {
		subaction_map.put("increase max hire",IncreaseMaxHire);
		subaction_map.put("increase ap",IncreaseAP);
	}
	
	public static PassiveFunction getSubAction(String name) {
		return subaction_map.get(name);
	}
	public static final PassiveFunction IncreaseMaxHire = new PassiveFunction() {
		public int activatePassiveAction (final DungeonGroup room,final ObjActor to_act) {
			Global.getPlayerTeam().increasMaxHire(1);
			return -1;	
		}
		public int deActivatePassiveAction (final DungeonGroup room,final ObjActor to_act) {
			Global.getPlayerTeam().increasMaxHire(-1);
			return -1;	
		}
	};
	public static final PassiveFunction IncreaseAP = new PassiveFunction() {
		public int activatePassiveAction (final DungeonGroup room,final ObjActor to_act) {
			to_act.status.base_status.DEFENSE+=2;
			return -1;	
		}
		public int deActivatePassiveAction (final DungeonGroup room,final ObjActor to_act) {
			to_act.status.base_status.DEFENSE-=2;
			return -1;	
		}
	};

}
