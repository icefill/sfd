package com.icefill.game.actors.devices;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.Assets;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.RoomGroup;
import com.icefill.game.sprites.NonObjSprites;

public class DoorActor extends DeviceActor{
	private float door_x=0;
	private float door_y=0;
	private int move_position[];
	AreaCell cell;
	Animation effect[];
	
	public DoorActor(DIR direction,int move_position_x,int move_position_y,int move_position_z,AreaCell cell,RoomGroup room) {
		super(room);
			curr_dir=direction;
			sprites= Assets.non_obj_sprites_map.get("down_marker");
			this.cell=cell;
			this.setFrontBack(1);
			move_position = new int[3];
			move_position[0]=move_position_x;
			move_position[1]=move_position_y;
			move_position[2]=move_position_z;
	}
	public DoorActor(DIR direction,int move_position_x,int move_position_y,AreaCell cell,RoomGroup room) {
		super(room);
		this.cell=cell;
		this.setFrontBack(1);
		curr_dir=direction;
		sprites= Assets.non_obj_sprites_map.get("exit_marker");
		//this.openDoor(Global.dungeon, cell);
		move_position = new int[3];
		move_position[0]=move_position_x;
		move_position[1]=move_position_y;
		move_position[2]=-1;
	}

	public float getDoorX(){
		return door_x;
	}
	public float getDoorY(){
		return door_y;
	}
	public void setDoorPosition(float x,float y)
	{
		door_x=x;
		door_y=y;
	}
	
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {
	if (move_position[2]<0) {
			dungeon.setRoom(move_position[0], move_position[1]);
		}
		else {
			dungeon.setRoom(move_position[0], move_position[1],move_position[2]);
		}
	}
	public void draw(Batch batch, float delta) {
		super.draw(batch,delta);
		if (!cell.is_blocked)
		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, curr_dir, getX(), getY()+getZ());
	}
	public void openDoor(DungeonGroup dungeon,AreaCell cell) {
		// Open door animation
		if (cell.is_blocked)
		{
			//cell.setFrontBack(1);
			cell.is_blocked=false;
			cell.wall_direction=cell.wall_direction.turnLeft(1);
		}
		//if (device!=null)
			//device.activateDevice(dungeon);
	}
	public void closeDoor(DungeonGroup dungeon,AreaCell cell) {
		// Open door animation
		if (!cell.is_blocked)
		{
			//cell.setFrontBack(0);
			cell.is_blocked=true;
			cell.wall_direction=cell.wall_direction.turnLeft(1);
		}
		//if (device!=null)
			//device.activateDevice(dungeon);
	}
	public void activateDevice(DungeonGroup dungeon,AreaCell cell) {
		cell.is_blocked=false;
		//if (wall_direction==1) {
				//is_blocked=false;
				cell.wall_direction=cell.wall_direction.turnLeft(1);
				Sound hit_sound= Assets.getAsset("sound/door.wav", Sound.class);
				hit_sound.play();
				//}
				
		//cell.device.addAction(Actions.rotateBy(360,.3f));
		
	}
	


}
