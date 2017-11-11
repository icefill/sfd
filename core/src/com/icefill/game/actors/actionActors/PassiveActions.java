package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.icefill.game.actors.AreaCell;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.Chain;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.EffectActor;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.Function;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.PassiveFunction;
import com.icefill.game.actors.ProjectileActor;
import com.icefill.game.extendedActions.ExtendedActions;
import com.icefill.game.sprites.BasicSprites;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.Randomizer;
import com.icefill.game.actors.LineSegment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

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
