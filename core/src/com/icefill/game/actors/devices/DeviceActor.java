package com.icefill.game.actors.devices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.RoomGroup;

public class DeviceActor extends BasicActor{
	//Boolean acting=false;
	protected boolean passable=true;
	protected boolean harzardeous;
	public DeviceActor()
	{}
	public DeviceActor(RoomGroup room)
	{
		room.addActor(this);
	}
	public void start() {
		acting=true;
	}
	public void end(){
		acting=false;
		elapsed_time=0;
		this.remove();
	}
	public void setDirection(DIR dir)
	{
		if (curr_dir != dir) curr_dir = dir;

	}
	public void drawDevice(Batch batch, float delta){
		super.draw(batch, delta);
		elapsed_time -= Gdx.graphics.getDeltaTime();
	//	if (sprites!=null)
	//		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, curr_dir, getX(), getY()+getZ());
	}
	
	//public void draw(Batch batch,float delta) {
		//this.drawDevice(batch, delta);
	//	super.draw(batch, delta);
	//	if (sprites!=null)
	//		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, curr_dir, getX(), getY()+getZ());
	//}
	public boolean isHarzardeous() {
		return harzardeous;
	}

	public boolean isPassable() {
		return passable;
	}
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {
		
	}
	public void activateDevice(DungeonGroup dungeon,AreaCell cell) {}


}
