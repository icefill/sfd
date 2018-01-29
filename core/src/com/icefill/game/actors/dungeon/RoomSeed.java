package com.icefill.game.actors.dungeon;

import com.badlogic.gdx.utils.ObjectSet;
import com.icefill.game.Constants;
import com.icefill.game.RoomShapeType;
import com.icefill.game.utils.NonRepeatRandomizer;

import java.util.LinkedList;

import static com.icefill.game.Constants.DL;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

public class RoomSeed {
    int[] room_size;
    int xxx, yyy;
    OBJ[][] array;
    public boolean[] has_door;
    public int[][] door_position;

    public boolean visited;
    public boolean initial_room = false;
    public RoomShapeType room_shape_type;
    int room_type;


    public RoomSeed(int room_xxx, int room_yyy) {
        this.xxx = room_xxx;
        this.yyy = room_yyy;
        has_door = new boolean[4];
        door_position = new int[4][2];
    }

    public RoomSeed(RoomShapeType room_type, int room_xxx, int room_yyy, int room_size_xx, int room_size_yy) {
        this(room_xxx, room_yyy);
        room_size = new int[2];
        room_size[0] = room_size_xx;
        room_size[1] = room_size_yy;
        this.room_shape_type = room_type;
        makeRoomArea(room_size_xx, room_size_yy);

    }

    public void makeRoomArea(int room_size_xx, int room_size_yy) {
        room_size[0] = room_size_xx;
        room_size[1] = room_size_yy;
        array = new OBJ[room_size_xx][room_size_yy];
        array = new OBJ[room_size[0]][room_size[1]];
        for (int xx = 0; xx < room_size_xx; xx++) {
            for (int yy = 0; yy < room_size_yy; yy++) {
                array[xx][yy] = OBJ.NOTHING;
            }
        }
    }

    public void makeFinalRoom() {
        int middle_x = room_size[0] / 2;
        int middle_y = room_size[1] / 2;
        clearRoom();
        array[room_size[0] / 2][room_size[1] / 2 - 1] = OBJ.NOTHING;
        array[room_size[0] / 2][room_size[1] / 2] = OBJ.DOWN_STAIR;
    }

    public void makeUpStair() {
        int middle_x = room_size[0] / 2;
        int middle_y = room_size[1] / 2;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                array[middle_x + dx][middle_y + dy] = OBJ.NOTHING;
            }
        }
        array[room_size[0] / 2][room_size[1] / 2 + 1] = OBJ.NOTHING;
    }

    public void checkInitialRoom() {
        initial_room = true;
    }

    public int getRoomsizeX() {
        return room_size[0];
    }

    public int getRoomsizeY() {
        return room_size[1];
    }

    public int getRoomArea() {
        return (room_size[0] - 1) * (room_size[1] - 1);
    }


    public void makeWalls() {
        // Make wall
        if (array != null) {
            for (int yy = 0; yy < room_size[1]; yy++) {
                for (int xx = 0; xx < room_size[0]; xx++) {
                    if (xx == 0) array[xx][yy] = OBJ.WALL;
                    else if (xx == room_size[0] - 1) array[xx][yy] = OBJ.WALL;
                    else if (yy == 0) array[xx][yy] = OBJ.WALL;
                    else if (yy == room_size[1] - 1) {
                        array[xx][yy] = OBJ.WALL;
                    }

                }
            }
        } else throw new RuntimeException("Roomseed Array not initialized");
    }

    //RoomArray should been initialized before using this method

    public boolean checkBlockedMonsterPosition(int xx, int yy) {
        boolean[] reach_to_door = new boolean[4];
        for (int i = 0; i < 4; i++) reach_to_door[i] = false;

        ObjectSet<Integer> lookup_set = new ObjectSet<Integer>();
        LinkedList<Integer> queue = new LinkedList<Integer>();
        Integer current = index(xx, yy);
        queue.add(current);
        while (!queue.isEmpty()) {
            current = queue.pollFirst();
            if (lookup_set.contains(current)) continue;
            else {
                lookup_set.add(current);
                int currentX = current % room_size[0];
                int currentY = current / room_size[0];

                int nearX = currentX + 1;
                int nearY = currentY;
                checkBlockedSubmethod(nearX, nearY, lookup_set, queue, reach_to_door);

                nearX = currentX;
                nearY = currentY + 1;
                checkBlockedSubmethod(nearX, nearY, lookup_set, queue, reach_to_door);

                nearX = currentX - 1;
                nearY = currentY;
                checkBlockedSubmethod(nearX, nearY, lookup_set, queue, reach_to_door);

                nearX = currentX;
                nearY = currentY - 1;
                checkBlockedSubmethod(nearX, nearY, lookup_set, queue, reach_to_door);

            }
        }
        for (int dir = 0; dir < 4; dir++)
            if (has_door[dir] && reach_to_door[dir] == false) return true;
        return false;
    }

    public int index(int xx, int yy) {
        return xx + room_size[0] * yy;
    }

    public void checkBlockedSubmethod(int nearX, int nearY, ObjectSet<Integer> lookup_set, LinkedList<Integer> queue, boolean[] reach_to_door) {
        if (0 <= nearX && nearX < room_size[0] && 0 <= nearY && nearY < room_size[1]) {
            if (!(OBJ.checkBlocked(array[nearX][nearY]))) {
                queue.add(index(nearX, nearY));
                for (int dir = 0; dir < 4; dir++)
                    if (has_door[dir] == true && nearX == door_position[dir][0] && nearY == door_position[dir][1])
                        reach_to_door[dir] = true;
            }
        }
    }

    public void makeDoors() {
        if (array != null) {
            if (has_door[Constants.DL]) {
                door_position[Constants.DL][0] = room_size[0] / 2;
                door_position[Constants.DL][1] = room_size[1] - 1;
                array[door_position[DL][0]][door_position[DL][1]] = OBJ.DOOR;
            }
            if (has_door[Constants.DR]) {
                door_position[Constants.DR][0] = room_size[0] - 1;
                door_position[Constants.DR][1] = room_size[1] / 2;
                array[door_position[Constants.DR][0]][door_position[Constants.DR][1]] = OBJ.DOOR;
            }
            if (has_door[Constants.UR]) {
                door_position[Constants.UR][0] = room_size[0] / 2;
                door_position[Constants.UR][1] = 0;
                array[door_position[Constants.UR][0]][door_position[Constants.UR][1]] = OBJ.DOOR;
            }
            if (has_door[Constants.UL]) {
                door_position[Constants.UL][0] = 0;
                door_position[Constants.UL][1] = room_size[1] / 2;
                array[door_position[Constants.UL][0]][door_position[Constants.UL][1]] = OBJ.DOOR;
            }
        } else throw new RuntimeException("Roomseed Array not initialized");
    }

    //Should be called after create obstacles
    public void createMonster(int monster_n) {
        int x_min = 1;
        int x_max = room_size[0] - 1;
        int y_min = 1;
        int y_max = room_size[1] - 1;

        if (has_door[0] || initial_room) y_max -= 2;
        if (has_door[1]) x_max -= 2;
        if (has_door[2]) y_min += 2;
        if (has_door[3]) x_min += 2;

        int monster_x = -1;
        int monster_y = -1;
        com.icefill.game.utils.NonRepeatRandomizer randomizer = new com.icefill.game.utils.NonRepeatRandomizer(x_min, x_max, y_min, y_max);
        for (int i = 0; i < monster_n; i++) {
            int rn;// = randomizer.nextInt();
            for (int n = 0; n < 20; n++) {
                rn = randomizer.nextInt();
                monster_x = rn / 10;
                monster_y = rn % 10;
                if (!checkBlockedMonsterPosition(monster_x, monster_y)) {
                    array[monster_x][monster_y] = OBJ.MONSTER;
                    break;
                }
            }
            array[monster_x][monster_y] = OBJ.MONSTER;
        }

        //Clearing


    }

    public void clearRoom() {
        for (int y = 1; y < room_size[1] - 1; y++) {
            for (int x = 1; x < room_size[0] - 1; x++) {
                array[x][y] = OBJ.NOTHING;
            }
        }
    }

    public void createBoss() {
        array[room_size[0] / 2][room_size[1] / 2] = OBJ.BOSS_MONSTER;
    }

    public void makeItemRoom() {
        clearRoom();
        int middle_x = (int) (room_size[0] / 2);
        int middle_y = (int) (room_size[1] / 2);
        array[middle_x - 2][middle_y - 1] = OBJ.FIRE_BOWL;
        array[middle_x + 1][middle_y - 1] = OBJ.FIRE_BOWL;
        array[middle_x - 2][middle_y + 1] = OBJ.FIRE_BOWL;
        array[middle_x + 1][middle_y + 1] = OBJ.FIRE_BOWL;
        array[middle_x][middle_y] = OBJ.WEAPON;
        array[middle_x - 1][middle_y] = OBJ.ITEM;
    }

    public void makeScrollRoom() {
        clearRoom();
        int middle_x = (int) (room_size[0] / 2);
        int middle_y = (int) (room_size[1] / 2);
        array[middle_x][middle_y - 1] = OBJ.FIRE_BOWL;
        array[middle_x][middle_y + 1] = OBJ.FIRE_BOWL;
        array[middle_x][middle_y] = OBJ.MAGIC_SCROLL;

    }

    public void makeInitialRoom() {
        clearRoom();
        int middle_x = (int) ((room_size[0] - 1) / 2);
        int middle_y = (int) ((room_size[1] - 1) / 2);
        array[middle_x - 1][middle_y - 1] = OBJ.FIRE_BOWL;
        array[middle_x + 1][middle_y - 1] = OBJ.FIRE_BOWL;
        array[middle_x - 1][middle_y] = OBJ.RECRUIT_CAT;
        array[middle_x + 1][middle_y] = OBJ.SHOP_CAT;

    }

    public void makeHealingRoom() {
        clearRoom();
        int middle_x = (int) ((room_size[0] - 1) / 2);
        int middle_y = (int) ((room_size[1] - 1) / 2);
        if (middle_x - 2 > 1)
            array[middle_x - 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_x + 2 < room_size[0] - 2)
            array[middle_x + 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_y - 2 > 1)
            array[middle_x][middle_y - 2] = OBJ.FIRE_BOWL;
        if (middle_y + 2 < room_size[1] - 2)
            array[middle_x][middle_y + 2] = OBJ.FIRE_BOWL;

        array[middle_x][middle_y] = OBJ.ANGEL;
    }

    public void makeShopRoom() {
        int middle_x = (int) ((room_size[0] - 1) / 2);
        int middle_y = (int) ((room_size[1] - 1) / 2);
        clearRoom();
        if (middle_x - 2 > 1)
            array[middle_x - 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_x + 2 < room_size[0] - 2)
            array[middle_x + 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_y - 2 > 1)
            array[middle_x][middle_y - 2] = OBJ.FIRE_BOWL;
        if (middle_y + 2 < room_size[1] - 2)
            array[middle_x][middle_y + 2] = OBJ.FIRE_BOWL;

        array[middle_x][middle_y] = OBJ.SHOP_CAT;
    }

    public void makemercRoom() {
        int middle_x = (int) ((room_size[0] - 1) / 2);
        int middle_y = (int) ((room_size[1] - 1) / 2);
        clearRoom();
        array[middle_x][middle_y] = OBJ.RECRUIT_CAT;

    }

    public void createObstacles(int n_in_room,float destructable_obs_ratio,int trap_n) {
        int x_min = 1;
        int x_max = room_size[0] - 1;
        int y_min = 1;
        int y_max = room_size[1] - 1;
        if (has_door[0] || initial_room) y_max--;
        if (has_door[1]) x_max--;
        if (has_door[2]) y_min++;
        if (has_door[3]) x_min++;
        com.icefill.game.utils.NonRepeatRandomizer randomizer = new NonRepeatRandomizer(x_min, x_max, y_min, y_max);

        for (int i = 0; i < n_in_room; i++) {
            int rn = randomizer.nextInt();
            int monster_x = rn / 10;
            int monster_y = rn % 10;
            if (com.icefill.game.utils.Randomizer.hitInRatio(destructable_obs_ratio))
                array[monster_x][monster_y] = OBJ.DEST_OBS;
            else
                array[monster_x][monster_y] = OBJ.UNDEST_OBS;
        }
        for (int i = 0; i < trap_n; i++) {
            int rn = randomizer.nextInt();
            int trap_x = rn / 10;
            int trap_y = rn % 10;
            array[trap_x][trap_y] = OBJ.TRAP;
        }
        int explosive_n = com.icefill.game.utils.Randomizer.nextInt(2);
        for (int i = 0; i < explosive_n; i++) {
            int rn = randomizer.nextInt();
            array[rn / 10][rn % 10] = OBJ.EXPLOSIVE;
        }
        if (has_door[0] && has_door[2] && !has_door[1] && !has_door[3]) {
            int xx = com.icefill.game.utils.Randomizer.nextInt(1, room_size[0] - 2);//rn.nextInt(room_size[0]-2)+1;
            for (int yy = 0; yy < room_size[1]; yy++) {
                array[xx][yy] = OBJ.NOTHING;
            }
        } else if (!has_door[0] && !has_door[2] && has_door[1] && has_door[3]) {
            int yy = com.icefill.game.utils.Randomizer.nextInt(1, room_size[1] - 2);//rn.nextInt(room_size[1]-2)+1;
            for (int xx = 0; xx < room_size[0]; xx++) {
                array[xx][yy] = OBJ.NOTHING;
            }
        }


    }

    public void makeShrineRoom() {
        int middle_x = (int) (room_size[0] / 2);
        int middle_y = (int) (room_size[1] / 2);
        clearRoom();

        //obj_index_array[middle_x][middle_y]=-8;
        if (middle_x - 2 > 1)
            array[middle_x - 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_x + 2 < room_size[0] - 2)
            array[middle_x + 2][middle_y] = OBJ.FIRE_BOWL;
        if (middle_y - 2 > 1)
            array[middle_x][middle_y - 2] = OBJ.FIRE_BOWL;
        if (middle_y + 2 < room_size[1] - 2)
            array[middle_x][middle_y + 2] = OBJ.FIRE_BOWL;

        array[middle_x][middle_y] = OBJ.SHRINE;

    }

    public String toString() {
        String to_return = "{\n";
        to_return += "room_size:" + room_size[0] + "," + room_size[1] + "\n";
        to_return += "obj_index:\n";
        to_return += "room_n:" + this.xxx + "," + this.yyy + "\n";
        String arr = "";
        for (int yy = 0; yy < room_size[1]; yy++) {
            for (int xx = 0; xx < room_size[0]; xx++) {
                arr += " " + array[xx][yy];
            }
            arr += "\n";
        }
        arr += "\n\n";
        return to_return + array;
    }
}
