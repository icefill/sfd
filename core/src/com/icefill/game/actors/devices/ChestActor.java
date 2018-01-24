package com.icefill.game.actors.devices;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.dungeon.RoomGroup;
import com.icefill.game.sprites.NonObjSprites;

public class ChestActor extends DeviceActor{
	EquipActor equip;
//	float rotation;
	public ChestActor(EquipActor equip,AreaCell cell,RoomGroup room) {
		super(room);
			passable=false;
			curr_dir=0;
			this.equip=equip;
			setX(cell.getX());
			setY(cell.getY());
			setZ(cell.getZ());
			setXX(cell.getXX());
			setYY(cell.getYY());
			sprites=Assets.non_obj_sprites_map.get("chest_box");
			room.addActor(this);
			
	}
	
	
	public void drawDevice(Batch batch, float delta){
			//super.draw(batch,delta);
			//((NonObjSprites)sprites).drawAnimation(batch, delta, 0, 0, getX(), getY());
	}
	public void draw(Batch batch, float delta) {
		super.draw(batch,delta);
		((NonObjSprites)sprites).drawAnimation(batch, delta, 0, 0, getX(), getY()+getZ());
	}
	public void activateDevice(DungeonGroup dungeon,AreaCell cell) {
		cell.is_blocked=false;
		cell.device= new ItemActor(equip,cell,Global.getCurrentRoom());
		this.remove();
		//cell.device.addAction(Actions.rotateBy(360,.3f));
		
	}
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {

		//if (dungeon.getSelectedObj().getInventory().setSlot(equip)== true)
		//	dungeon.getCurrentRoom().getMap().getAreaCell(dungeon.getSelectedObj().getXX(),dungeon.getSelectedObj().getYY()).device=null;
	}


}
