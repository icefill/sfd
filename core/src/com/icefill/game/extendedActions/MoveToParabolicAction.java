package com.icefill.game.extendedActions;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.icefill.game.actors.BasicActor;

public class MoveToParabolicAction extends MoveTo3DAction {

	protected float v0;
	private float g;

	protected void begin(){
		super.begin();
		v0=(float)((endZ-startZ)/this.getDuration()+g*0.5*this.getDuration());

	}
	public void setG(float g) {
		this.g=g;
	}
	protected void update (float percent) {
		super.update(percent);
		float time=this.getDuration()*percent;
		((BasicActor)target).setZ((float)(startZ+v0*time-0.5*g*time*time));
	}
	
	
}
