package com.icefill.game.actors;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.icefill.game.Global;

public class TargetInfoActor extends BasicActor {
	private Label target_info;
	ObjActor obj;

	public TargetInfoActor(ObjActor obj,Label.LabelStyle style)
	{
		this.obj=obj;
		
		target_info = new Label("Bummer",style);//new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
		//target_info.setPosition(-10, 34+getZ());
		//target_info.setFontScale(.25f);
		target_info.pack();
		//target_info.setColor(Color.BLACK);
		this.addActor(target_info);
		target_info.setVisible(false);
		this.setFrontBack(1);

	}
	public void addTargetInfo(String message) {
		target_info.setVisible(true);
		target_info.setText(message);
		this.setVisible(true);
		target_info.pack();
		Global.getCurrentRoom().addActor(this);
		this.setPosition(obj.getX()-10, obj.getY()+obj.getZ()+34);
	}
	
	public void removeTargetInfo()
	{
		target_info.setVisible(true);
		//target_info.clear();
		this.remove();
	}
	
}
