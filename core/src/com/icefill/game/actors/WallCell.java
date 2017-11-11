package com.icefill.game.actors;


public class WallCell extends BasicActor{
	public float elapsed_time;
	public Wall wall;
	public int wall_index;
	
	public WallCell(int xx,int yy,int floor_index) {
		this.xx=xx;
		this.yy=yy;
		elapsed_time=0f;
	}
	public String toString() {
		return "("+xx+","+yy+")";
	}

}
