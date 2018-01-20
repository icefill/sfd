package com.icefill.game;

public class RoomShapeType {

    public String floor_name;
    public String down_stair_name;
    public String wall_name;
    public String door_name;
    public String fire_bowl_name;

    public RoomShapeType(String floor_name, String down_stair_name,String wall_name,String door_name,String fire_bowl_name) {
        this.floor_name=floor_name;
        this.down_stair_name=down_stair_name;
        this.wall_name=wall_name;
        this.door_name=door_name;
        this.fire_bowl_name=fire_bowl_name;
    }
}