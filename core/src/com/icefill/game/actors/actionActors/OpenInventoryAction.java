package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.CommonInventoryActor;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.ObjActor;

public class OpenInventoryAction extends ActionActor {
	boolean begin=false;
	public OpenInventoryAction(){
		action_name="Inven";
		short_name="INVEN";
		setButtonText();
		type=0;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("inventory"));
	}
	public int execute(DungeonGroup room, ObjActor to_act,int level){

		if (Global.gfs.getSeq()==0) {
			Global.getPlayerTeam().getInventory().addObjInventory(to_act.getInventory());
			Global.getPlayerTeam().getInventory().setVisible(true);
			Global.getPlayerTeam().showInventory();
			to_act.getInventory().setVisible(true);
		
		//room.getHUD().add(to_act.getInventory());
		//begin = true;
		}
		else {
			if (!Global.getPlayerTeam().getInventory().isVisible()) {
				//begin=false;
				return -1;
			}
		}
		
		return 0;
	}
	
}
