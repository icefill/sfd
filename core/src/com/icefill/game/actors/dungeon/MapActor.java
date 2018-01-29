package com.icefill.game.actors.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.icefill.game.*;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.devices.*;
import com.icefill.game.sprites.NonObjSprites;

import java.util.LinkedList;

public class MapActor extends BasicActor implements Constants {
	int[]map_size;
	public float map_center_x;
	public float map_center_y;
	float map_origin_x;
	float map_origin_y;
	final static float TILE_WIDTH =68;
	final static float TIEL_HEIGHT =35;
	final static public float TILE_ANCHOR_X =37;
	final static public float TILE_ANCHOR_Y =40;
	final static public float TILE_Z =15;
	
	int height_modifier=15;
	float map_width;
	float map_height;

	private NonObjSprites area_selection;
	private AreaCell[] door_list;
	AreaCell[][] area_cell_array;
	

	Color area_color;
	
	LinkedList<AreaCell> area_list;
	
	
	public MapActor(RoomShapeType room_type, RoomGroup room, int[] map_size, OBJ[][] array,
					DungeonGroup.ItemPool item_pool, DungeonGroup.ItemPool equip_pool, DungeonGroup.ItemPool scroll_pool,
					int dungeon_level) {
		door_list= new AreaCell[6];
		map_center_x=Global.getStage().getWidth()*.5f;
		map_center_y=Global.getStage().getHeight()*.5f;
		setX(map_center_x);
		setY(map_center_y);
		area_selection= (NonObjSprites)Assets.non_obj_sprites_map.get("area_indicator");
		this.map_size=map_size;
		area_list = new LinkedList<AreaCell>();
		area_color= Color.BLUE;
		map_height=.5f* TIEL_HEIGHT *(map_size[1]+map_size[0]);
		map_width=.5f* TILE_WIDTH *(map_size[0]+map_size[1]);
		map_origin_y=getY()+0.5f*map_height;
		map_origin_x=getX();
		
		

		area_cell_array= new AreaCell[map_size[0]][map_size[1]];
		
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				area_cell_array[xx][yy]= new AreaCell(xx,yy,Assets.floor_map.get(room_type.floor_name),room);
				area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
				area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
				switch (array[xx][yy]) {
					case ANGEL 			: area_cell_array[xx][yy].device = new Angel(area_cell_array[xx][yy],room); break;
					case WALL			: {
						area_cell_array[xx][yy]= new AreaCell(xx,yy,Assets.floor_map.get(room_type.wall_name),room);
						area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
						area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
						area_cell_array[xx][yy].setWallIndex();
						break;}
					case EXPLOSIVE		: break;
					case OBSTACLE		: break;
					case FIRE_BOWL		: break;
					case DOOR			: {
						area_cell_array[xx][yy]= new AreaCell(xx,yy,Assets.floor_map.get(room_type.door_name),room);
						area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
						area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
						area_cell_array[xx][yy].setWallIndex();
						break;}
					case TRAP			: area_cell_array[xx][yy].device= new SpikeTrapActor(area_cell_array[xx][yy],room); break;
					case SHOP_CAT		: area_cell_array[xx][yy].device = new ShopDeviceActor(area_cell_array[xx][yy],room,dungeon_level,false);break;
					case SHRINE			: area_cell_array[xx][yy].device= new ShrineActor(area_cell_array[xx][yy],room); break;
					case MONSTER		: break;
					case BOSS_MONSTER	: break;
					case MAGIC_SCROLL	: area_cell_array[xx][yy].device = new ItemActor(new EquipActor(scroll_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);break;
					case ITEM			: area_cell_array[xx][yy].device = new ChestActor(new EquipActor(item_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);break;
					case WEAPON			: area_cell_array[xx][yy].device = new ChestActor(new EquipActor(equip_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);break;
					case RECRUIT_CAT	: area_cell_array[xx][yy].device = new MercShopDeviceActor(area_cell_array[xx][yy],room,dungeon_level); break;
					case UNDEST_OBS		: break;
					case DEST_OBS		: break;
					case DOWN_STAIR		: 	area_cell_array[xx][yy]= new AreaCell(xx,yy,Assets.floor_map.get(room_type.down_stair_name),room);
											area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
											area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
                                            area_cell_array[xx][yy].setFloorIndex();break;
					default: area_cell_array[xx][yy].setFloorIndex();break;
				}
				if (area_cell_array[xx][yy]!=null) room.addActor(area_cell_array[xx][yy]);
				//Set direction for door
				if (yy==0 && xx != map_size[0]-1) area_cell_array[xx][yy].setDirection(UR);
				else if (xx==0 && yy != map_size[1]-1) area_cell_array[xx][yy].setDirection(UL);
				else {
						room.addActor(area_cell_array[xx][yy]);
						if (xx==map_size[0]-1) area_cell_array[xx][yy].setDirection(DR);
						else if (yy==map_size[1]-1)area_cell_array[xx][yy].setDirection(DL);
				}

			}
		}

	}

	public AreaCell getDoor(int i)
	{
		return door_list[i];
	}
	public void openAllDoor(DungeonGroup dungeon)
	{
		for (AreaCell door_cell:door_list)
		{
			if (door_cell!=null)
			((DoorActor)(door_cell.device)).openDoor(dungeon, door_cell);
		}
	}
	public void openDoor(DungeonGroup dungeon,int direction)
	{
		if (door_list[direction]!=null)
			((DoorActor)(door_list[direction].device)).openDoor(dungeon, door_list[direction]);
	}
	public void closeDoor(DungeonGroup dungeon,int direction)
	{
		if (door_list[direction]!=null)
			((DoorActor)(door_list[direction].device)).closeDoor(dungeon, door_list[direction]);
	}
	public void closeAllDoor(DungeonGroup dungeon)
	{
		for (AreaCell door_cell:door_list)
		{
			if (door_cell!=null)
			((DoorActor)(door_cell.device)).closeDoor(dungeon, door_cell);
		}
	}
	
	public int getHeightModifier() {
		return height_modifier;
	}
	public void setAreaCellZ(int xx,int yy,int z) {	
		if (0<=z && z<height_modifier)
			area_cell_array[xx][yy].z=z;
	}
	public void draw(Batch batch, float delta){
		super.draw(batch, delta);
		drawFloors(batch,delta);
	
	}

	
	public void drawArea(Batch batch,float delta){
			AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				temp=area_cell_array[xx][yy];

				batch.setColor(area_color);
				if (temp.is_in_range && temp.isAnimationStarted())	{
					area_selection.drawAnimation(batch, temp.elapsed_time,0, 0, mapToScreenCoordX(temp.xx, temp.yy), mapToScreenCoordY(temp.xx, temp.yy)+temp.getZ(),false);
					temp.elapsed_time+=Gdx.graphics.getDeltaTime();
				}
				if (temp.is_target){
					batch.setColor(1f,0f,0f,0.5f);
					area_selection.drawAnimation(batch, 1, 0, 0, mapToScreenCoordX(temp.xx, temp.yy), mapToScreenCoordY(temp.xx, temp.yy)+temp.getZ(),false);
				}
				batch.setColor(1f,1f,1f,1f);
			}
		}

	}
	public void act(float delta) {
		super.act(delta);
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				area_cell_array[xx][yy].act(delta);
			}
		}
	}

	public void drawGlow(Batch batch, float delta) {
		batch.setBlendFunction(GL20.GL_DST_COLOR,GL20.GL_SRC_ALPHA);
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				area_cell_array[xx][yy].drawGlow(batch,delta);
			}
		}
 		
 		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


	}

	public void drawFloors(Batch batch, float delta) {
		float x;
		float y;
		AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				temp = area_cell_array[xx][yy];
				if ((xx!=(map_size[0]-1) && yy!=(map_size[1]-1)) || (temp.device instanceof DoorActor)) {
					x = mapToScreenCoordX(xx, yy) - TILE_ANCHOR_X;
					y = mapToScreenCoordY(xx, yy) - TILE_ANCHOR_Y;
					if (!((xx == map_size[0] - 1) || (yy == map_size[1] - 1))) {
						batch.draw((temp.floor.animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time, true), x, y + temp.getZ());
						if (temp.device != null)
							temp.device.drawDevice(batch, delta);
						//temp.drawWall(batch, delta);
					} else {
						batch.draw((temp.floor.animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time, true), x, y + temp.getZ());
					}

					if (temp.effect != null) {
						temp.drawEffect(batch, delta);
					}
				}
			}
		}
	}


	public AreaCell getCell(int xx,int yy) {
		if (isInFloor(xx,yy)) 
		return area_cell_array[xx][yy];
		
		
		
		else return null;
		
		
		
		
	}
	public AreaCell getCellNearCenter() {
		int xx=getCenterXX();
		int yy= getCenterYY();
		if (isInFloor(xx,yy)) 
		return area_cell_array[xx][yy];
		else return null;
	}
	public int getCenterXX() {
		return map_size[0]/2;
	}
	
	public int getCenterYY() {
		return map_size[1]/2;
		
	}
	
	public int screenTomapCoordX(float x,float y) {
		float xxx=((x-map_origin_x)/ TILE_WIDTH);
		float yyy=((map_origin_y-y)/ TIEL_HEIGHT);
		int xx=(int)((xxx+yyy)+0.5f);
		if (xx<=0) return 0;
		else if (xx>=map_size[0]) return map_size[0]-1;
		else return xx;
				
	}
	public int screenTomapCoordY(float x,float y) {
		float xxx=((x-map_origin_x)/ TILE_WIDTH);
		float yyy=((map_origin_y-y)/ TIEL_HEIGHT);
		int yy= (int)((-xxx+yyy)+0.5f);
		if (yy<=0) return 0;
		else if (yy>=map_size[1]) return map_size[1]-1;
		else return yy;		
	}
	public float mapToScreenCoordX(int xx,int yy) {
		return (float) (map_origin_x+0.5* TILE_WIDTH *(xx-yy));
	}
	public float mapToScreenCoordY(int xx,int yy) {
		return (float) (map_origin_y-0.5* TIEL_HEIGHT *(xx+yy));
	}
	
	public boolean isMovableAndNoDeviceFloor(int xx,int yy) {
		if (		(0<=xx) 
				&& (xx < map_size[0]) 
				&& (0<=yy) 
				&& (yy<map_size[1])
				&& !area_cell_array[xx][yy].isBlocked()
				&& (area_cell_array[xx][yy].device==null ||
				 area_cell_array[xx][yy].device.isPassable())
			)
			
			return true;
		else return false;
	}
	public boolean isMovableFloor(int xx,int yy) {
		if (		(0<=xx) 
				&& (xx < map_size[0]) 
				&& (0<=yy) 
				&& (yy<map_size[1])
				&& !area_cell_array[xx][yy].isBlocked()
			)
			
			return true;
		else return false;
	}

	public boolean isInFloor(int xx,int yy) {
		if ((0<=xx) 
		&& (xx < map_size[0]) 
		&& (0<=yy) 
		&& (yy<map_size[1])
		) return true;
		else return false;
	}
	
	public void addArea(LinkedList<AreaCell> to_add,Color color) {
		clearAreaList();
		area_color=color;
		area_list.addAll(to_add);
		ParallelAction area_action= new ParallelAction();

		int i=0;
	
		for (AreaCell temp:area_list){
			
			final AreaCell temp2=temp;
			area_action.addAction(Actions.run(new Runnable() {@Override public void run() {
				temp2.activate();
			}})
			);
			
		}
		this.addAction(area_action);
	}
	public void clearAreaList() {
		for (AreaCell temp:area_list) {
			temp.clear();
		}
		area_list.clear();
	}
	public void makeDoor(int direction,int to_xx,int to_yy,RoomGroup room) {
		int x=0,y=0;
		int xx=0,yy=0;
		switch (direction) {
		case DL:
			xx=x=map_size[0]/2;yy=y=map_size[1]-1;break;
		case DR:
			xx=x=map_size[0]-1;yy=y=map_size[1]/2;break;
		case UR:
			x=map_size[0]/2;y=0;
			xx=x;yy=-1;
			break;
		case UL:
			x=0;y=map_size[1]/2;
			xx=-1;yy=y;
			break;
		}
		area_cell_array[x][y].device=new DoorActor(direction,to_xx,to_yy,area_cell_array[x][y],room);
		area_cell_array[x][y].device.setPosition(this.mapToScreenCoordX(x, y),this.mapToScreenCoordY(x, y));
		area_cell_array[x][y].setUsualWall();
		//((DoorActor)(area_cell_array[x][y].device)).setDoorPosition(this.mapToScreenCoordX(xx, yy-1)-TILE_ANCHOR_X,this.mapToScreenCoordY(xx, yy-1)-TILE_ANCHOR_Y+16);
		door_list[direction]=(area_cell_array[x][y]);
	}
	public void makeVerticalDoor(int to_xx,int to_yy,int to_zz,RoomGroup room) {
		int x=0,y=0;
		int xx=0,yy=0;
		xx=map_size[0]/2;
		yy=map_size[1]/2;
		
		area_cell_array[xx][yy].device=new DoorActor(0,to_xx,to_yy,to_zz,area_cell_array[xx][yy],room);
		area_cell_array[xx][yy].device.setPosition(this.mapToScreenCoordX(xx, yy),this.mapToScreenCoordY(xx, yy));
		//((DoorActor)(area_cell_array[x][y].device)).setDoorPosition(this.mapToScreenCoordX(xx, yy-1)-TILE_ANCHOR_X,this.mapToScreenCoordY(xx, yy-1)-TILE_ANCHOR_Y+16);
		//door_list.add(area_cell_array[x][y]);
	}

	public String toString () {
		String to_return="";
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
					if (area_cell_array[xx][yy].is_in_range)
					to_return+="o";
					else to_return+="x";
			}
			to_return+="\n";
		}
		return to_return;
	}

	
}
