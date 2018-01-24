package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;

public class ObjInfoAbilityActor extends ActionActor {

	public ObjInfoAbilityActor(){
		action_name="Add Ability";
		short_name="ABIL";
		setButtonText();
		type=0;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("ability_up"));
	}
	public int execute(DungeonGroup room, ObjActor to_act,int level){
		if (to_act.getSkillWindow()!=null)
			to_act.getSkillWindow().chooseAndAddAbilities(room);
		//to_act.obj_state=PL_DONE;
		//to_act.status.current_ap=0;
		return -1;
	}
	
}
