package com.icefill.game.actors.dungeon;

import com.icefill.game.Constants;
import com.icefill.game.OBJ;
import com.icefill.game.RoomShapeType;
import com.icefill.game.utils.NonRepeatRandomizer;

import java.util.ArrayList;

public class RoomSeed {
      int[] room_size;
      int xxx,yyy;
      char[][] array;
    public boolean[] has_door;
    public boolean visited;
    public boolean initial_room=false;
    public RoomShapeType room_shape_type;
    int room_type;




    public RoomSeed(int room_xxx, int room_yyy){
        this.xxx=room_xxx;
        this.yyy=room_yyy;
        has_door= new boolean[4];
    }

    public RoomSeed(RoomShapeType room_type, int room_xxx, int room_yyy, int room_size_xx, int room_size_yy) {
        this(room_xxx,room_yyy);
        room_size= new int[2];
        room_size[0]=room_size_xx;
        room_size[1]=room_size_yy;
        this.room_shape_type=room_type;

        array= new char[room_size[0]][room_size[1]];
    }

    public void makeDownStair() {
        int middle_x=room_size[0]/2;
        int middle_y=room_size[1]/2;
        for (int dx=-1;dx<2;dx++){
            for (int dy=-1;dy<2;dy++){
                array[middle_x+dx][middle_y+dy]= OBJ.NOTHING.c;
            }
        }
        array[room_size[0]/2][room_size[1]/2-1]=OBJ.NOTHING.c;
        array[room_size[0]/2][room_size[1]/2]=OBJ.NOTHING.c;
    }
    public void makeUpStair() {
        int middle_x=room_size[0]/2;
        int middle_y=room_size[1]/2;
        for (int dx=-1;dx<2;dx++){
            for (int dy=-1;dy<2;dy++){
                array[middle_x+dx][middle_y+dy]=OBJ.NOTHING.c;
            }
        }
        array[room_size[0]/2][room_size[1]/2+1]=OBJ.NOTHING.c;
    }

    public void checkInitialRoom() {
        initial_room=true;
    }
    public int getRoomsizeX() {
        return room_size[0];
    }
    public int getRoomsizeY() {
        return room_size[1];
    }
    public int getRoomArea() {
        return (room_size[0]-1)*(room_size[1]-1);
    }


    public void makeWallAndDoor(){
        // Make wall
        for (int yy=0;yy<room_size[1];yy++) {
            for (int xx=0;xx<room_size[0];xx++) {
                if (xx==0) array[xx][yy]=OBJ.WALL.c;
                else if(xx==room_size[0]-1) array[xx][yy]=OBJ.WALL.c;
                else if( yy==0) array[xx][yy]=OBJ.WALL.c;
                else if (  yy==room_size[1]-1) {
                    array[xx][yy]=OBJ.WALL.c;
                }

            }
        }
        // Make door
        if (has_door[Constants.DL]) array[room_size[0]/2][room_size[1]-1]=OBJ.DOOR.c;
        if (has_door[Constants.DR]) array[room_size[0]-1][room_size[1]/2]=OBJ.DOOR.c;
        if (has_door[Constants.UR]) array[room_size[0]/2][0]=OBJ.DOOR.c;
        if (has_door[Constants.UL]) array[0][room_size[1]/2]=OBJ.DOOR.c;
    }

    public void createMonster(int monster_n) {
        int x_min=1;
        int x_max=room_size[0]-1;
        int y_min=1;
        int y_max=room_size[1]-1;

        if (has_door[0] || initial_room) y_max-=2;
        if (has_door[1]) x_max-=2;
        if (has_door[2]) y_min+=2;
        if (has_door[3]) x_min+=2;


        int monster_x;
        int monster_y;
        com.icefill.game.utils.NonRepeatRandomizer randomizer= new com.icefill.game.utils.NonRepeatRandomizer(x_min,x_max,y_min,y_max);
        for (int i=0;i<monster_n;i++) {
            int rn= randomizer.nextInt();
            monster_x=rn/10;
            monster_y=rn%10;
            array[monster_x][monster_y]=OBJ.MONSTER.c;
        }

        //Clearing



    }
    public void clearRoom() {
        for (int y=1;y<room_size[1]-1;y++){
            for (int x=1;x<room_size[0]-1;x++){
                array[x][y]=OBJ.NOTHING.c;
            }
        }
    }
    public void createBoss() {
            array[room_size[0]/2][room_size[1]/2]=OBJ.BOSS_MONSTER.c;
    }
    public void makeItemRoom() {
        clearRoom();
        int middle_x=(int)(room_size[0]/2);
        int middle_y=(int)(room_size[1]/2);
        array[middle_x][middle_y-1]=OBJ.FIRE_BOWL.c;
        array[middle_x][middle_y+1]=OBJ.FIRE_BOWL.c;
        array[middle_x][middle_y]=OBJ.WEAPON.c;
        array[middle_x-1][middle_y]=OBJ.ITEM.c;
    }

    public void makeScrollRoom() {
        clearRoom();
        int middle_x=(int)(room_size[0]/2);
        int middle_y=(int)(room_size[1]/2);
        array[middle_x][middle_y-1]=OBJ.FIRE_BOWL.c;
        array[middle_x][middle_y+1]=OBJ.FIRE_BOWL.c;
        array[middle_x][middle_y]=OBJ.MAGIC_SCROLL.c;

    }
    public void makeInitialRoom() {
        clearRoom();
        int middle_x=(int)((room_size[0]-1)/2);
        int middle_y=(int)((room_size[1]-1)/2);
        array[middle_x-1][middle_y]=OBJ.RECRUIT_CAT.c;
        array[middle_x+1][middle_y]=OBJ.SHOP_CAT.c;

    }
    public void makeAngelRoom() {
        clearRoom();
        int middle_x=(int)((room_size[0]-1)/2);
        int middle_y=(int)((room_size[1]-1)/2);
        //obj_index_array[middle_x][middle_y]=-8;
        //obj_index_array[1][1]=-4;
        //obj_index_array[1][2]=-4;
        //obj_index_array[1][3]=-4;
        //obj_index_array[middle_x][middle_y+1]=-7;
        //obj_index_array[middle_x][middle_y]=-6;
        array[middle_x][middle_y]=OBJ.ANGEL.c;

        //obj_index_array[middle_x-1][middle_y]=-7;
        //obj_index_array[middle_x+1][middle_y]=-7;
        //device_index_array[middle_x-1][middle_y]=2;
        //device_index_array[middle_x+1][middle_y]=2;
        //device_index_array[middle_x][middle_y+1]=5;
        //device_index_array[middle_x][middle_y-1]=4;


    }
    public void makeShopRoom() {
        int middle_x=(int)((room_size[0]-1)/2);
        int middle_y=(int)((room_size[1]-1)/2);
        clearRoom();
        array[middle_x][middle_y]=OBJ.SHOP_CAT.c;
    }
    public void makemercRoom() {
        int middle_x=(int)((room_size[0]-1)/2);
        int middle_y=(int)((room_size[1]-1)/2);
        clearRoom();
        array[middle_x][middle_y]=OBJ.RECRUIT_CAT.c;

    }
    public void createObstacles(int n_in_room) {
        int trap_n= com.icefill.game.utils.Randomizer.nextInt(3);
        int x_min=1;
        int x_max=room_size[0]-1;
        int y_min=1;
        int y_max=room_size[1]-1;
        if (has_door[0] || initial_room) y_max--;
        if (has_door[1]) x_max--;
        if (has_door[2]) y_min++;
        if (has_door[3]) x_min++;
        com.icefill.game.utils.NonRepeatRandomizer randomizer= new NonRepeatRandomizer(x_min,x_max,y_min,y_max);

        for (int i=0;i<n_in_room;i++) {
            int rn=randomizer.nextInt();
            int monster_x=rn/10;
            int monster_y=rn%10;
            if (com.icefill.game.utils.Randomizer.hitInRatio(.8f))
                array[monster_x][monster_y]=OBJ.UNDEST_OBS.c;
            else
                array[monster_x][monster_y]=OBJ.DEST_OBS.c;
        }
        for (int i=0;i<trap_n;i++) {
            int rn= randomizer.nextInt();
            int trap_x=rn/10;
            int trap_y=rn%10;
            array[trap_x][trap_y]=OBJ.TRAP.c;
        }
        int explosive_n= com.icefill.game.utils.Randomizer.nextInt(2);
        for (int i=0;i<explosive_n;i++) {
            int rn=randomizer.nextInt();
            array[rn/10][rn%10]=OBJ.EXPLOSIVE.c;
        }
        if (has_door[0] && has_door[2]&& !has_door[1]&& !has_door[3]) {
            int xx= com.icefill.game.utils.Randomizer.nextInt(1,room_size[0]-2);//rn.nextInt(room_size[0]-2)+1;
            for (int yy=0;yy<room_size[1];yy++){
                array[xx][yy]=OBJ.NOTHING.c;
            }
        }
        else if (!has_door[0] && !has_door[2]&& has_door[1]&& has_door[3]) {
            int yy= com.icefill.game.utils.Randomizer.nextInt(1,room_size[1]-2);//rn.nextInt(room_size[1]-2)+1;
            for (int xx=0;xx<room_size[0];xx++){
                array[xx][yy]=OBJ.NOTHING.c;
            }
        }


    }
    public void makeHealingRoom() {
        int middle_x=(int)(room_size[0]/2);
        int middle_y=(int)(room_size[1]/2);
        clearRoom();

        //obj_index_array[middle_x][middle_y]=-8;
        if (middle_x-2>1)
            array[middle_x-2][middle_y]=OBJ.FIRE_BOWL.c;
        if (middle_x+2<room_size[0]-2)
            array[middle_x+2][middle_y]=OBJ.FIRE_BOWL.c;
        if (middle_y-2>1)
            array[middle_x][middle_y-2]=OBJ.FIRE_BOWL.c;
        if (middle_y+2<room_size[1]-2)
            array[middle_x][middle_y+2]=OBJ.FIRE_BOWL.c;

        array[middle_x][middle_y]=OBJ.ANGEL.c;

    }
    public String toString() {
        String to_return="{\n";
        to_return+="room_size:"+room_size[0]+","+room_size[1]+"\n";
          to_return+="obj_index:\n";
          to_return+="room_n:"+this.xxx+","+this.yyy+"\n";
          String arr="";
          for (int yy=0;yy<room_size[1];yy++){
              for (int xx=0;xx<room_size[0];xx++){
                  arr+=" "+array[xx][yy];
              }
              arr+="\n";
          }
          arr+="\n\n";
        return to_return+array;
    }
}
