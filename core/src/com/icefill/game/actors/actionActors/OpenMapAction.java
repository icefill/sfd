package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;

public class OpenMapAction extends ActionActor {
	boolean begin=false;
	public OpenMapAction(){
		action_name="Map";
		short_name="MAP";
		setButtonText();
		type=0;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("inventory"));
	}
	public int execute(DungeonGroup room, ObjActor to_act,int level){

		if (Global.gfs.getSeq()==0) {
			Global.getHUD().showMap();
		}
		
		else {
			if (!Global.getHUD().getMinimap().isVisible()) {
				//begin=false;
				return -1;
			}
		}
		
		return 0;
	}
	
}
