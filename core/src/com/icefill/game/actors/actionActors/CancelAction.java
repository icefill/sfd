package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;

public class CancelAction extends ActionActor {

	public CancelAction(){
		action_name="Cancel";
		short_name="cancel";
		type=0;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("cancel"));
	}
	
	
}
