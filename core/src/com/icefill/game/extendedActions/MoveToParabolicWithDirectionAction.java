package com.icefill.game.extendedActions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.icefill.game.actors.BasicActor;

public class MoveToParabolicWithDirectionAction extends MoveToParabolicAction {

	private Vector2 direction_start;
	private Vector2 direction_end;
	private Vector2 direction;
	protected void begin(){
		super.begin();
		float z_modifier= this.getDuration()*v0;
		direction= new Vector2();
		direction_start= new Vector2(endX-startX,endY-startY+z_modifier);
		direction_end= new Vector2(endX-startX,endY-startY-z_modifier);
		((BasicActor)target).setRotation(direction_start.angle());
		
		//v0=(float)((endZ-startZ)/this.getDuration()+g*0.5*this.getDuration());

	}
	protected void update (float percent) {
		super.update(percent);
		direction.set(direction_end);
		direction.sub(direction_start);
		direction.scl(percent);
		direction.add(direction_start);
		((BasicActor)target).setRotation(direction.angle());
		
		//float time=this.getDuration()*percent;
		//((BasicActor)target).setZ((float)(startZ+v0*time-0.5*g*time*time));
	}
	
	
}
