package com.icefill.game.extendedActions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ExtendedActions extends Actions{
	static public MoveToParabolicAction moveToParabolic (float x, float y,float z,float duration) { 
		MoveToParabolicAction action = action(MoveToParabolicAction.class); 
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		action.setG(2000);
		 		return action; 
 
		 	} 
	
	static public MoveToParabolicWithDirectionAction moveToParabolicWithDirection (float x, float y,float z,float duration) { 
		MoveToParabolicWithDirectionAction action = action(MoveToParabolicWithDirectionAction.class); 
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		action.setG(2000);
		 		return action; 
 
		 	} 

	static public MoveTo3DAction moveTo3D (float x, float y,float z,float duration) { 
		MoveTo3DAction action = action(MoveTo3DAction.class); 
		 		action.setPosition(x, y,z); 
		 		action.setDuration(duration); 
		 		action.setInterpolation(null);
		 		return action; 
 
		 	} 
	
		 

}
