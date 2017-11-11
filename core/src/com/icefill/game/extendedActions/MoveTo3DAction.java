package com.icefill.game.extendedActions;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.icefill.game.actors.BasicActor;

public class MoveTo3DAction extends MoveToAction {
	protected float startZ;
	protected float endZ;
	protected float startX;
	protected float startY;
	protected float endX;
	protected float endY;

	protected void begin(){
		startX=((BasicActor)target).getX();
		startY=((BasicActor)target).getY();
		startZ=((BasicActor)target).getZ();
		super.begin();
	}
	protected void setPosition(float x,float y, float z) {
		super.setPosition(x, y);
		this.endX=x;
		this.endY=y;
		this.endZ=z;
	}

	protected void update (float percent) {
		super.update(percent);
		((BasicActor)target).setZ(startZ + (endZ - startZ) * percent);
	}
	
	
}
