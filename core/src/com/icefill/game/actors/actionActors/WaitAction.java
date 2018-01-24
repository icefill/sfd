package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;

public class WaitAction extends ActionActor {

	public WaitAction(){
		action_name="Wait";
		short_name="Wait";
		type=0;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("wait"));
	}
	public int execute(DungeonGroup room, ObjActor to_act,int level){
		to_act.obj_state=PL_DONE;
		to_act.status.setAP(0);
		return -1;
	}
	
}
