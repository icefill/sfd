package com.icefill.game.actors;

import com.badlogic.gdx.Gdx;


import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.devices.DeviceActor;
import com.icefill.game.extendedActions.ExtendedActions;
import com.icefill.game.sprites.BasicSprites;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class BasicActor extends Group implements Constants{
	public int xx,yy;	//tile_coord of actor
	public float z=0;
	protected int curr_dir=0;
	public float elapsed_time=0.0f;

	private int front_back=0;
	protected boolean pause=false;
	protected boolean attached=false;
	protected BasicActor to_attach;
	protected float attach_anchor_x;
	protected float attach_anchor_y;
	protected float attach_anchor_z;
	protected float attach_anchor_rot;
	
	protected BasicActor self=this;
	
	protected boolean acting=false;
	private boolean active=false;
	public BasicSprites sprites;
	protected String sprites_name;
	ActionActor acting_action;
	protected int pause_id;
	
	public void setActingAction(ActionActor action) {
		this.acting_action= action;
	}
	public void releaseActingAction(){
		this.acting_action=null;
	}
	public void setPauseID(int id) {
		this.pause_id=id;
	}
	public int getPauseID() {
		return pause_id;
	}
	public void setFrontBack(int flag) {
		this.front_back=flag;
		//Global.getCurrentRoom().sortActors();
	}
	public Action setFrontBackAction(final int flag)
	{
		return Actions.run(new Runnable() {public void run() {setFrontBack(flag);}});
	}
	public int getFrontBack()
	{
		return front_back;
	}
	
	
	public void setPosition(float x, float y, float z) {
		this.setPosition(x,y);
		this.setZ(z);
	}
	public void setXX(int xx) {this.xx=xx;};
	public void setYY(int yy) {this.yy=yy;};
	public int getXX() {return xx;};
	public int getYY() {return yy;};

	public float getZ(){
		return z;
	}
	public void setZ(float z){
		this.z=z;
	}
	public BasicActor() {
		// TODO Auto-generated constructor stub
	}
	public void attach(BasicActor to_attach) {
		this.to_attach=to_attach;
		attach_anchor_x=this.getX()-to_attach.getX();
		attach_anchor_y=this.getY()-to_attach.getY();
		attach_anchor_z=this.getZ()-to_attach.getZ();
		attach_anchor_rot=this.getRotation()-to_attach.getRotation();
		attached=true;
		
	}

	public void draw(Batch batch, float delta){
		super.draw(batch, delta);
		if (pause== false)
			elapsed_time += Gdx.graphics.getDeltaTime();
	}
	
	public void start() {self.acting=true;self.active=true;
	if (acting_action!= null)
	{
	//	acting_action.addActingActor(this);
		acting_action.addActorCount();
	}}
	public void deActivate() {
	self.active=false;
	if (acting_action!=null)
		{
		acting_action.subtractActorCount();
		}
	}
	public void end() {self.acting=false;}
	public void deActivateAndEnd() {deActivate();this.addAction(this.endActionSubAction());}
	public boolean isActive() {return active;}
	public boolean isActing() {return acting;}
	
	
	
	public Action startAction() {
		return Actions.run(new Runnable() {public void run() {self.start();}});
	}
	
	public Action deActivateAction() {
		return Actions.run(new Runnable() {public void run() {self.deActivate();}});
	}
	
	public Action deActivateAndEndAction() {
		return Actions.run(new Runnable() {public void run() {self.deActivateAndEnd();}});
	}
	
	public Action endActionSubAction () {
		return Actions.run(new Runnable() {public void run() {self.end();}});
	}
	public void hide() {
		
	}
	
	public Action pauseAnimationStart() {
		return Actions.run(new Runnable() {public void run() {self.pause=true;}} );
	}
	public Action pauseAnimationEnd() {
		return Actions.run(new Runnable() {public void run() {self.pause=false;}});
	}
	public Action pauseAnimation(float time) {
		return Actions.sequence(
				 pauseAnimationStart()
				,Actions.fadeIn(time)
				,pauseAnimationEnd()
				);
	}

	public Action addActionToOtherActor(final Actor to_add,final Action adding_action) {
		return Actions.run(new Runnable() {public void run() {
			adding_action.setActor(to_add);
			to_add.addAction(adding_action);
			}});
	}
	

	/*********************** Action related Direction  *******************************/
	public void setDirection(int dir)
	{
		if ((sprites!=null && sprites.hasDirection()) || this instanceof DeviceActor) {
			if (0 <= dir && dir <= 3) {
				if (curr_dir != dir) curr_dir = dir;
			}
		}
	}
public int getDirection(){return curr_dir;}
	
	public int getDirectionToTarget(int target_xx,int target_yy) {
		int dxx=target_xx-getXX();
		int dyy=target_yy-getYY();
		if (dxx==0 && dyy==0)
			return curr_dir;
		else if (dyy>dxx) { if (dyy > -dxx ) return DL;
					else return UL;}
		else if (dyy> -dxx) return DR;
		else return UR;
	}
	public int getFrontRearOrBack(ObjActor target)
	{
		if (target.job!=null && target.job.no_direction) return 0;
		int back=getDirectionToTarget(target.getXX(),target.getYY());
		int front= (back+2) % 4;
		int left= (back+1) % 4;
		int right= (back+3) % 4;
		
		int target_direction=target.getDirection();
		if (target_direction==front)
		{
			return 0;
		}
		else if (target_direction==left)
		{
			return 1;
		}
		else if (target_direction==right)
		{
			return 3;
		}
		else
		{
			return 2;
		}
		
	}
	public String getFrontRearOrBackString(ObjActor target)
	{
		int frb=getFrontRearOrBack(target);
		switch (frb)
		{
		case 0:
			return "FRONT";
		case 1:
		case 3:
			return "SIDE";
		case 2:
			return "BACK";
		default:
			return "??";
		}
		
	}
	public void setDirectionToTarget(float newx, float newy) {
		if (sprites.hasDirection()) {
			setDirection(getDirectionToTarget(newx,newy));
		}
	}
	public void setDirectionToTarget(int newxx,int newyy) {
		if (sprites.hasDirection()) {
			setDirection(getDirectionToTarget(newxx,newyy));
		}
	}
	public int getDirectionToTarget(float newx,float newy) {
		int dir;
		if (newx-getX() > 0) {
			if (newy-getY()>0) {dir=UR; }
			else {dir=DR; }
		}
		else {
			if (newy-getY()>0) {dir=UL; }
			else {dir=DL;}
		}
	return dir;
	}
	public static int getOppositeDirection(int direction) {
		switch (direction) {
		case DL: return UR;
		case DR: return UL;
		case UR: return DL;
		case UL: return DR;
		default : return DL;
		}
	}
	public static int getRightDirection(int direction) {
		switch (direction) {
		case DL: return DR;
		case DR: return UR;
		case UR: return UL;
		case UL: return DL;
		default : return DL;
		}
	}
	public static Vector2 directionToScreenVector(int direction) {
		Vector2 to_return;
		switch (direction) {
		case DL: to_return= new Vector2(-2,-1);break;
		case DR:to_return= new Vector2(2,-1);break;
		case UR:to_return= new Vector2(2,1);break;
		case UL:to_return= new Vector2(-2,1);break;
		default: to_return= new Vector2(0,0);break;
		}
		to_return.nor();
		return to_return;
	}
	public static Vector2 directionToMapVector(int direction) {
		Vector2 to_return= new Vector2();
		switch (direction) {
		case DL:
			to_return.x=0;
			to_return.y=1;
			break;
		case DR:
			to_return.x=1;
			to_return.y=0;
			break;
		case UR:
			to_return.x=0;
			to_return.y=-1;
			break;
		case UL:
			to_return.x=-1;
			to_return.y=0;
			break;
		}
		return to_return;
	}

	public Action headingAndMoveToAction(final float newx,final float newy,final float newz,float duration) {
		SequenceAction seq= new SequenceAction();
		seq.addAction(((ObjActor)self).setDirectionToCoordAction (newx,newy));
		seq.addAction(Actions.moveTo(newx,newy,duration));
		return seq;
	}

	public Action setDirectionAction (final int direction) {
		return Actions.run(new Runnable() {public void run() {self.setDirection(direction);}});
	}
	public Action setDirectionToCoordAction(final float x,final float y) {
		return Actions.run(new Runnable() {public void run() {self.setDirectionToTarget(x, y);}});
	}


	
}
