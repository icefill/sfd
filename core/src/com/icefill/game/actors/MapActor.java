package com.icefill.game.actors;

import java.lang.reflect.Array;
















import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.Randomizer;
import com.icefill.game.actors.devices.Angel;
import com.icefill.game.actors.devices.ChestActor;
import com.icefill.game.actors.devices.DoorActor;
import com.icefill.game.actors.devices.ItemActor;
import com.icefill.game.actors.devices.MercShopDeviceActor;
import com.icefill.game.actors.devices.ShopDeviceActor;
import com.icefill.game.actors.devices.SpikeTrapActor;
import com.icefill.game.actors.devices.TrapActor;
import com.icefill.game.sprites.NonObjSprites;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MapActor extends BasicActor implements Constants {
	int[]map_size;
	public float map_center_x=300f;
	public float map_center_y=170f;
	float map_origin_x;
	float map_origin_y;
	float tile_width=68;
	float tile_height=35;
	public float tile_anchor_x=37;
	public float tile_anchor_y=40;
	float tile_z=15;
	
	int height_modifier=15;
	float map_width;
	float map_height;
	private NonObjSprites area_selection;
	private NonObjSprites wall_tile;
	private NonObjSprites door;
	private AreaCell[] door_list;
	private ArrayList<DungeonGroup.LightInformation> lights;
	AreaCell[][] area_cell_array;
	
	LinkedList<Floor> floor_list;
	LinkedList<Floor> wall_list;
	
	Color area_color;
	
	LinkedList<AreaCell> area_list;
	
	
	public MapActor(RoomGroup room,int[] map_size,ArrayList<String> floor_name_list,ArrayList<String> wall_name_list,int[][] floor_index_array,int[][] device_index_array,
			DungeonGroup.ItemPool item_pool,DungeonGroup.ItemPool equip_pool,DungeonGroup.ItemPool scroll_pool,
			int dungeon_level) {
		door_list= new AreaCell[6];
		map_center_x=Global.getStage().getWidth()*.5f;
		map_center_y=Global.getStage().getHeight()*.5f;
		setX(map_center_x);
		setY(map_center_y);
		//wall_tile=(NonObjSprites)Assets.non_obj_sprites_map.get("wall_tile");
		door=(NonObjSprites)Assets.non_obj_sprites_map.get("door");
		area_selection= (NonObjSprites)Assets.non_obj_sprites_map.get("area_indicator");
		this.map_size=map_size;
		area_list = new LinkedList<AreaCell>();
		area_color= Color.BLUE;
		map_height=(float)((.5f*tile_height*(map_size[1]+map_size[0])));
		map_width=(float)((.5f*tile_width*(map_size[0]+map_size[1])));
		map_origin_y=getY()+0.5f*map_height;
		map_origin_x=getX();
		
		
		floor_list=new LinkedList<Floor>();
		
		//int i=0;
		for (String floor_name: floor_name_list){
			floor_list.add(Assets.floor_map.get(floor_name));
			//i++;
		}
		wall_list=new LinkedList<Floor>();
		for (String wall_name: wall_name_list){
			wall_list.add(Assets.floor_map.get(wall_name));
			//i++;
		}
		lights= new ArrayList<DungeonGroup.LightInformation>();
		area_cell_array= new AreaCell[map_size[0]][map_size[1]];
		
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				if (floor_index_array[xx][yy]>=0) {
					area_cell_array[xx][yy]= new AreaCell(xx,yy,floor_list.get(floor_index_array[xx][yy]),room);
					area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
					area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
					//area_cell_array[xx][yy].setZ((int)((Randomizer.nextInt(2)*height_modifier)/2f));
					// Add Device
					if (device_index_array[xx][yy] !=0) {
						switch (device_index_array[xx][yy]) {
						case 1:
							area_cell_array[xx][yy].device= new TrapActor(area_cell_array[xx][yy],room);
							break;
						case 2:
							area_cell_array[xx][yy].device = new ChestActor(new EquipActor(item_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);
							break;
						case 3:
							area_cell_array[xx][yy].device = new SpikeTrapActor(area_cell_array[xx][yy],room);
							break;
						case 4:
							area_cell_array[xx][yy].device = new ItemActor(new EquipActor(scroll_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);
							break;
						case 5:
							area_cell_array[xx][yy].device = new ChestActor(new EquipActor(equip_pool.getItem(dungeon_level)),area_cell_array[xx][yy],room);
							break;
						case 6:
							area_cell_array[xx][yy].device = new ShopDeviceActor(area_cell_array[xx][yy],room,dungeon_level,false);
							
							break;	
						case 7:
							area_cell_array[xx][yy].device = new Angel(area_cell_array[xx][yy],room);
							
							break;
						case 8:
							area_cell_array[xx][yy].device = new MercShopDeviceActor(area_cell_array[xx][yy],room,dungeon_level);
							
							break;	
						}
						//room.addActor(area_cell_array[xx][yy].device);
						area_cell_array[xx][yy].setZ(0);
					}
					else
					{
						area_cell_array[xx][yy].setFloorIndex();
						
						//area_cell_array[xx][yy].setFloorIndex();
					}
				}
				else {
					
					int index=0;// rn.nextInt(wall_list.get(-floor_index_array[xx][yy]).n_of_tiles);
					area_cell_array[xx][yy]= new AreaCell(xx,yy,wall_list.get(-floor_index_array[xx][yy]),room);
					//area_cell_array[xx][yy].setZ((int)((Randomizer.nextInt(2)*height_modifier)/2f));
									
					if (area_cell_array[xx][yy].floor.light_radius>0){
						
						lights.add(new DungeonGroup.LightInformation(xx,yy,20,7));
					}
				    
					if (yy==0 && xx != map_size[0]-1) area_cell_array[xx][yy].setDirection(UR);
					else if (xx==0 && yy != map_size[1]-1) area_cell_array[xx][yy].setDirection(UL);
					else {
						//area_cell_array[xx][yy].setZ((int)((Randomizer.nextInt(2)*height_modifier)/2f));
						//area_cell_array[xx][yy].setZ((int)(height_modifier)/2);
						room.addActor(area_cell_array[xx][yy]);
						if (xx==map_size[0]-1) area_cell_array[xx][yy].setDirection(DR);
						else if (yy==map_size[1]-1)area_cell_array[xx][yy].setDirection(DL);
					}
					area_cell_array[xx][yy].setWallIndex();
					//area_cell_array[xx][yy].setFloorIndex();
					
					area_cell_array[xx][yy].setX(this.mapToScreenCoordX(xx, yy));
					area_cell_array[xx][yy].setY(this.mapToScreenCoordY(xx, yy));
					
					
				}
				//this.addActor(area_cell_array[xx][yy]);
				
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
		//elapsed_time += Gdx.graphics.getDeltaTime();
		//drawWall(batch,delta);
		//drawDoor(batch,delta);
		drawFloors(batch,delta);
	
	}
	public void drawDoor(Batch batch,float delta){
		for (AreaCell temp:door_list) {
			//if (temp.device.curr_dir == UL ||temp.device.curr_dir == UR )
			//door.drawAnimation(batch, delta, 0, temp.device.curr_dir, ((DoorActor)(temp.device)).getDoorX(),((DoorActor)temp.device).getDoorY() );
		}
	}
	
	public void drawArea(Batch batch,float delta){
		float x;
		float y;
		AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				x=mapToScreenCoordX(xx,yy)-tile_anchor_x;
				y=mapToScreenCoordY(xx,yy)-tile_anchor_y;
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
	public void drawWall(Batch batch, float delta){
		float x;
		float y;
		AreaCell temp;
		batch.setColor(.2f,.22f,.2f,1f);
		int xx=map_size[0]-1;
		for (int yy=1;yy<map_size[1]-1;yy++) {
				x=mapToScreenCoordX(xx,yy)-tile_anchor_x;
				y=mapToScreenCoordY(xx,yy)-tile_anchor_y;
				temp=area_cell_array[xx][yy];
				if (temp.floor.wall_animation!=null) 
				batch.draw((temp.floor.wall_animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time,true), x,y+temp.getZ());
		}
		int yy=map_size[1]-1;
		for (xx=1;xx<map_size[0]-0;xx++) {
				x=mapToScreenCoordX(xx,yy)-tile_anchor_x;
				y=mapToScreenCoordY(xx,yy)-tile_anchor_y;
				temp=area_cell_array[xx][yy];
				if (temp.floor.wall_animation!=null) 
				batch.draw((temp.floor.wall_animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time,true), x,y+temp.getZ());
		}
		batch.setColor(1f,1f,1f,1f);
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
	public void drawOuterWall(Batch batch, float delta)
	{
		AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++)
		{
			temp=area_cell_array[map_size[0]-1][yy];
			if (temp.device==null)
			{
				temp.drawWall(batch, delta);
			}
			else
			{
				temp.drawWallTransparently(batch, delta);
			}
		}
		for (int xx=0;xx<map_size[0];xx++)
		{
			temp=area_cell_array[xx][map_size[1]-1];
			if (temp.device==null)
			{
				temp.drawWall(batch, delta);
			}
			else
			{
				temp.drawWallTransparently(batch, delta);
			}
		}
	}
	public void drawFloors(Batch batch, float delta) {
		float x;
		float y;
		AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				x=mapToScreenCoordX(xx,yy)-tile_anchor_x;
				y=mapToScreenCoordY(xx,yy)-tile_anchor_y;
				temp=area_cell_array[xx][yy];
				if (!((xx==map_size[0]-1) || (yy ==map_size[1]-1))) {
					batch.draw((temp.floor.animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time,true), x,y+temp.getZ());
					if (temp.device!=null)
						temp.device.drawDevice(batch, delta);
					//temp.drawWall(batch, delta);
				}
				else
				{
					batch.draw((temp.floor.animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time,true), x,y+temp.getZ());
				}
				
				/*
				if (temp.floor.wall_animation!=null) {
					batch.draw((temp.floor.wall_animation.get(temp.floor_index)[temp.getDirection()]).getKeyFrame(elapsed_time,true), x,y+temp.getZ());
				}
				*/
				if (temp.effect!= null) {
					temp.drawEffect(batch,delta);
				}
				//batch.draw(floor_list.get(temp.floor_index).curr_animation.getKeyFrame(elapsed_time,true)
				//		, x,y+temp.getZ());

			}
		}
	}
	/*
	public void drawDevice(Batch batch,float delta) {
		float x_coord;
		float y_coord;
		AreaCell temp;
		for (int yy=0;yy<map_size[1];yy++) {
			for (int xx=0;xx<map_size[0];xx++) {
				x_coord=mapToScreenCoordX(xx,yy);
				y_coord=mapToScreenCoordY(xx,yy);
				temp=area_cell_array[xx][yy];		
				if (temp.device !=null) { 
					temp.device.drawDevice(batch, delta);					
				}
			}
		}
	}
*/
	public Floor getFloor(int xx,int yy) {
		if (isInFloor(xx,yy))
		return floor_list.get(area_cell_array[xx][yy].floor_index);
		else return null;
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
		float xxx=((x-map_origin_x)/tile_width); 
		float yyy=((map_origin_y-y)/tile_height);
		int xx=(int)((xxx+yyy)+0.5f);
		if (xx<=0) return 0;
		else if (xx>=map_size[0]) return map_size[0]-1;
		else return xx;
				
	}
	public int screenTomapCoordY(float x,float y) {
		float xxx=((x-map_origin_x)/tile_width); 
		float yyy=((map_origin_y-y)/tile_height);
		int yy= (int)((-xxx+yyy)+0.5f);
		if (yy<=0) return 0;
		else if (yy>=map_size[1]) return map_size[1]-1;
		else return yy;		
	}
	public float mapToScreenCoordX(int xx,int yy) {
		return (float) (map_origin_x+0.5*tile_width*(xx-yy));
	}
	public float mapToScreenCoordY(int xx,int yy) {
		return (float) (map_origin_y-0.5*tile_height*(xx+yy));
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
		//((DoorActor)(area_cell_array[x][y].device)).setDoorPosition(this.mapToScreenCoordX(xx, yy-1)-tile_anchor_x,this.mapToScreenCoordY(xx, yy-1)-tile_anchor_y+16);
		door_list[direction]=(area_cell_array[x][y]);
	}
	public void makeVerticalDoor(int to_xx,int to_yy,int to_zz,RoomGroup room) {
		int x=0,y=0;
		int xx=0,yy=0;
		xx=map_size[0]/2;
		yy=map_size[1]/2;
		
		area_cell_array[xx][yy].device=new DoorActor(0,to_xx,to_yy,to_zz,area_cell_array[xx][yy],room);
		area_cell_array[xx][yy].device.setPosition(this.mapToScreenCoordX(xx, yy),this.mapToScreenCoordY(xx, yy));
		//((DoorActor)(area_cell_array[x][y].device)).setDoorPosition(this.mapToScreenCoordX(xx, yy-1)-tile_anchor_x,this.mapToScreenCoordY(xx, yy-1)-tile_anchor_y+16);
		//door_list.add(area_cell_array[x][y]);
	}
	
	public ArrayList<DungeonGroup.LightInformation> getLightsInformation(){
		return lights;
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
