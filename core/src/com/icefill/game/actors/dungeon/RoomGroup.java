package com.icefill.game.actors.dungeon;


import java.util.Comparator;

import com.icefill.game.*;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Sort;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.devices.*;
import com.icefill.game.extendedActions.ExtendedActions;
import com.icefill.game.sprites.NonObjSprites;
import com.icefill.game.utils.Randomizer;

import static com.icefill.game.utils.StaticFunctions.chooseAmong;

public class RoomGroup extends Group implements Constants {
    //position of room in dungeon
    public int room_xx;
    public int room_yy;
    public int room_zz;
    int dungeon_level;
    public int room_type;
    int room_size[];
    public boolean[] has_door;
    ObjActor[][] obj_array;
    Sort sort = new Sort();
    Team enemy_list;
    Team player_list;
    MapActor map;
    NonObjSprites glow;
    NonObjSprites light_emitter;
    boolean visited;
    public boolean boss_room;
    AComparator comp = new AComparator();


    public RoomGroup(RoomShapeType roomShapeType, int room_xx, int room_yy, int room_zz, DungeonSeed dungeon_seed) {
        this.has_door = dungeon_seed.room_array[room_xx][room_yy][room_zz].has_door;
        this.room_type = dungeon_seed.room_array[room_xx][room_yy][room_zz].room_type;
        this.room_xx = room_xx;
        this.room_yy = room_yy;
        this.room_zz = room_zz;
        this.dungeon_level = room_zz + 1;
        this.map = new MapActor(roomShapeType,
                this,
                dungeon_seed.room_array[room_xx][room_yy][room_zz].room_size
                , dungeon_seed.room_array[room_xx][room_yy][room_zz].array
                , dungeon_seed.item_pool, dungeon_seed.equip_pool, dungeon_seed.scroll_pool
                , dungeon_level
        );

        //make obj list

        enemy_list = new Team();
        int controlled;
        this.room_size = dungeon_seed.room_array[room_xx][room_yy][room_zz].room_size;
        RoomSeed roomSeed = dungeon_seed.room_array[room_xx][room_yy][room_zz];
        obj_array = new ObjActor[room_size[0]][room_size[1]];

        for (int yy = 0; yy < room_size[1]; yy++) {
            for (int xx = 0; xx < room_size[0]; xx++) {
                float x = map.mapToScreenCoordX(xx, yy);
                float y = map.mapToScreenCoordY(xx, yy);
                ObjActor temp;

                switch (OBJ.toEnum(roomSeed.array[xx][yy])) {
                    case ANGEL:
                        break;
                    case WALL: {
                        break;
                    }
                    case EXPLOSIVE: {
                        temp = new ObjActor(xx, yy, -1, 1, Assets.jobs_map.get(chooseAmong(dungeon_seed.explosive_name_list).job), CONTROLLED_AI);
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        this.addActor(temp);
                        break;
                    }
                    case OBSTACLE: {
                        DungeonGroup.ObjListElt elt;
                        if (Randomizer.hitInRatio(.3f))
                            elt = chooseAmong(dungeon_seed.undest_obs_info_list);
                        else
                            elt = chooseAmong(dungeon_seed.dest_obs_info_list);
                        temp = new ObjActor(xx, yy, elt.team, elt.level, Assets.jobs_map.get(elt.job), CONTROLLED_AI);
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        this.addActor(temp);
                        break;
                    }
                    case FIRE_BOWL: {
                        temp = new ObjActor(xx, yy, -1, 1, Assets.jobs_map.get(roomShapeType.fire_bowl_name), CONTROLLED_AI);
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        this.addActor(temp);
                        break;
                    }
                    case DOOR:
                        break;
                    case TRAP:
                        break;
                    case SHOP_CAT:
                        break;
                    case SHRINE:
                        break;
                    case MONSTER: {
                        DungeonGroup.ObjListElt temp_obj;
                        controlled = CONTROLLED_AI;
                        temp_obj = dungeon_seed.monster_pool.getMonster(dungeon_level);
                        temp = new ObjActor(xx, yy, temp_obj.team, temp_obj.level, Assets.jobs_map.get(temp_obj.job), controlled);
                        temp.setDirection(com.icefill.game.utils.Randomizer.nextInt(0, 3));
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        enemy_list.add(temp);
                        this.addActor(temp);
                        break;
                    }
                    case BOSS_MONSTER: {
                        DungeonGroup.ObjListElt temp_obj;
                        controlled = CONTROLLED_AI;
                        temp_obj = dungeon_seed.boss_pool.getMonster(dungeon_level);
                        temp = new ObjActor(xx, yy, temp_obj.team, temp_obj.level, Assets.jobs_map.get(temp_obj.job), controlled);
                        enemy_list.add(temp);
                        enemy_list.setLeader(temp);
                        boss_room = true;
                        temp.setDirection(com.icefill.game.utils.Randomizer.nextInt(0, 3));
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        //put objActor in team_list
                        this.addActor(temp);
                        break;
                    }
                    case MAGIC_SCROLL:
                        break;
                    case ITEM:
                        break;
                    case WEAPON:
                        break;
                    case RECRUIT_CAT:
                        break;
                    case UNDEST_OBS: {
                        DungeonGroup.ObjListElt elt = chooseAmong(dungeon_seed.undest_obs_info_list);
                        temp = new ObjActor(xx, yy, elt.team, elt.level, Assets.jobs_map.get(elt.job), CONTROLLED_AI);
                        obj_array[xx][yy] = temp;
                        temp.setDirection(Randomizer.nextInt(4));
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        this.addActor(temp);
                        break;
                    }
                    case DEST_OBS: {
                        DungeonGroup.ObjListElt elt = chooseAmong(dungeon_seed.dest_obs_info_list);
                        temp = new ObjActor(xx, yy, elt.team, elt.level, Assets.jobs_map.get(elt.job), CONTROLLED_AI);
                        obj_array[xx][yy] = temp;
                        temp.setX(x);
                        temp.setY(y + map.getCell(xx, yy).getZ());
                        this.addActor(temp);
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        glow = (NonObjSprites) Assets.non_obj_sprites_map.get("glow");
        light_emitter = (NonObjSprites) Assets.non_obj_sprites_map.get("light_emitter");
        //map.getAreaCell(1, 1).device=new ItemActor(new EquipActor((AbilityActor)Assets.actions_map.get("Fireball")),map.getAreaCell(1, 1));

        if (has_door[UL])
            map.makeDoor(UL, room_xx - 1, room_yy, this);
        if (has_door[DR])
            map.makeDoor(DR, room_xx + 1, room_yy, this);
        if (has_door[UR])
            map.makeDoor(UR, room_xx, room_yy - 1, this);
        if (has_door[DL])
            map.makeDoor(DL, room_xx, room_yy + 1, this);


    }

    public void setPlayerToRoom(LinkedList<ObjActor> player_list, int from_dir) {
        int init_pos_x = 0;
        int init_pos_y = 0;
        int adder_x = 0;
        int adder_y = 0;
        switch (from_dir) {
            case DL:
                init_pos_x = map.map_size[0] / 2 - 2;
                init_pos_y = map.map_size[1] - 2;
                adder_x = 1;
                break;
            case DR:
                init_pos_x = map.map_size[0] - 2;
                init_pos_y = map.map_size[1] / 2 - 2;
                adder_y = 1;
                break;
            case UR:
                init_pos_x = map.map_size[0] / 2 - 2;
                ;
                init_pos_y = 1;
                adder_x = 1;
                break;
            case UL:
                init_pos_x = 1;
                init_pos_y = map.map_size[1] / 2 - 2;
                ;
                adder_y = 1;
                break;
            default:
                init_pos_x = map.map_size[0] / 2;
                init_pos_y = map.map_size[1] - 3;
                adder_x = 1;
                from_dir = DL;
                break;
        }
        int i = 0;
        for (ObjActor temp : player_list) {
            temp.remove();
            this.setObj(init_pos_x + i * adder_x, init_pos_y + i * adder_y, temp);
            temp.setDirection(from_dir);
            i++;
        }

    }

    public void setRoom(Team player_list, int from_dir, int to_dir) {
        this.player_list = player_list;
        setPlayerToRoom(player_list, from_dir);

        if (from_dir < 4) {
            for (ObjActor temp : player_list) {
                temp.setDirection(to_dir);
            }
            map.openDoor(Global.dungeon, from_dir);
        }

    }

    public void removeRoom() {
        this.remove();
        for (ObjActor temp : player_list) {
            if (temp != null)
                removeActor(temp.getXX(), temp.getYY());
        }
    }

    public void setObj(int xx, int yy, ObjActor to_set) {
        if (to_set != null) {
            float x = map.mapToScreenCoordX(xx, yy);
            float y = map.mapToScreenCoordY(xx, yy);
            obj_array[xx][yy] = to_set;
            to_set.setXX(xx);
            to_set.setYY(yy);
            to_set.setX(x);
            to_set.setY(y + map.getCell(xx, yy).getZ());
            this.addActor(to_set);
        }
    }

    public boolean setObj(AreaCell cell, ObjActor to_set) {
        if (getObj(cell) == null) {
            setObj(cell.getXX(), cell.getYY(), to_set);
            return true;
        }
        return false;
    }

    public boolean setObj(ObjActor to_set) {
        int xx = map.getCenterXX();
        int yy = map.getCenterYY();

        if (to_set != null) {
            if (map.getCell(xx, yy).device == null
                    && getObj(xx, yy) == null
                    ) {
                setObj(xx, yy, to_set);
                return true;
            } else {
                for (int dxx = -1; dxx < 2; dxx++) {
                    for (int dyy = -1; dyy < 2; dyy++) {
                        if (
                                map.isInFloor(xx + dxx, yy + dyy)
                                        && map.getCell(xx + dxx, yy + dyy).device == null
                                        && !(map.getCell(xx + dxx, yy + dyy).is_blocked)
                                        && getObj(xx + dxx, yy + dyy) == null
                                ) {
                            setObj(xx + dxx, yy + dyy, to_set);

                            return true;
                        }
                    }
                }
            }

            {
                for (xx = 1; xx < map.map_size[0] - 1; xx++) {
                    for (yy = 1; yy < map.map_size[1] - 1; yy++) {
                        if (map.getCell(xx, yy).device == null
                                && !(map.getCell(xx, yy).is_blocked)
                                && getObj(xx, yy) == null) {
                            setObj(xx, yy, to_set);
                            return true;
                        }
                    }
                }
            }
        }


        return false;
    }

    public boolean setItem(EquipActor item) {
        if (item != null) {
            for (int xx = 1; xx < map.map_size[0] - 1; xx++) {
                for (int yy = 1; yy < map.map_size[1] - 1; yy++) {
                    if (map.getCell(xx, yy).device == null) {
                        map.getCell(xx, yy).device = new ItemActor(item, map.getCell(xx, yy), this);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean setItem(int xx, int yy, EquipActor item, Boolean in_a_chest) {
        DeviceActor device = null;
        if (item != null) {
            if (map.getCell(xx, yy).device == null
                    && getObj(xx, yy) == null
                    ) {
                if (in_a_chest) {
                    device = new ChestActor(item, map.getCell(xx, yy), this);
                    map.getCell(xx, yy).device = device;
                } else {
                    device = new ItemActor(item, map.getCell(xx, yy), this);
                    map.getCell(xx, yy).device = device;
                }

            } else {
                boolean done = false;
                for (int dxx = -1; dxx < 2; dxx++) {
                    for (int dyy = -1; dyy < 2; dyy++) {
                        if (
                                map.isInFloor(xx + dxx, yy + dyy)
                                        && map.getCell(xx + dxx, yy + dyy).device == null
                                        && !(map.getCell(xx + dxx, yy + dyy).is_blocked)
                                        && getObj(xx + dxx, yy + dyy) == null
                                ) {
                            if (in_a_chest) {
                                device = new ChestActor(item, map.getCell(xx + dxx, yy + dyy), this);
                                map.getCell(xx + dxx, yy + dyy).device = device;
                            } else {
                                device = new ItemActor(item, map.getCell(xx + dxx, yy + dyy), this);
                                map.getCell(xx + dxx, yy + dyy).device = device;
                            }
                            done = true;
                            break;
                            //return true;
                        }
                    }
                    if (done) break;
                }
            }
            if (device != null) {
                device.addAction(ExtendedActions.moveToParabolic(device.getX(), device.getY(), device.getZ(), .4f));
                return true;
            }
        }
        return false;
    }

    public ObjActor getObj(int xx, int yy) {
        return obj_array[xx][yy];
    }

    public ObjActor getObj(AreaCell cell) {
        if (cell != null)
            return obj_array[cell.xx][cell.yy];
        else return null;
    }

    public ObjActor getNearObj(ObjActor obj, int dir) {
        if (obj != null) {
            return getObj(getNearCell(obj.getXX(), obj.getYY(), dir));
        }
        return null;
    }

    public ObjActor getNearObj(AreaCell cell, int dir) {
        if (cell != null) {
            return getObj(getNearCell(cell.getXX(), cell.getYY(), dir));
        }
        return null;
    }

    public AreaCell getCell(int xx, int yy) {
        return map.getCell(xx, yy);
    }

    public AreaCell getCell(ObjActor obj) {
        return map.getCell(obj.getXX(), obj.getYY());
    }

    public AreaCell getNearCell(int xx, int yy, int direction) {
        switch (direction) {
            case 0:
                yy++;
                break;
            case 1:
                xx++;
                break;
            case 2:
                yy--;
                break;
            case 3:
                xx--;
                break;
        }
        return map.getCell(xx, yy);
    }

    public AreaCell getNearCell(ObjActor obj, int direction) {
        return getNearCell(obj.getXX(), obj.getYY(), direction);
    }

    public AreaCell getNearCell(AreaCell cell, int direction) {
        return getNearCell(cell.getXX(), cell.getYY(), direction);
    }

    public MapActor getMap() {
        return this.map;
    }

    public void removeActor(int xx, int yy) {
        obj_array[xx][yy] = null;
    }

    public void changeActorPosition(ObjActor temp_obj, int xx, int yy) {
        obj_array[temp_obj.getXX()][temp_obj.getYY()] = null;
        obj_array[xx][yy] = temp_obj;
        temp_obj.setXX(xx);
        temp_obj.setYY(yy);
        if (temp_obj != null) temp_obj.setPositionChanged();
    }

    public void exchangeActorsPosition(ObjActor temp_obj, BasicActor temp_obj2) {

    }

    public Team getEnemyList() {
        return this.enemy_list;
    }

    public void draw(Batch batch, float delta) {
        map.drawArea(batch, delta);
        super.draw(batch, delta);
    }

    public AreaCell getNearEmptyCell(int xx_origin, int yy_origin) {
        int xx, yy;
        AreaCell to_return = null;
        xx = xx_origin - 1;
        yy = yy_origin;
        if (map.isMovableFloor(xx, yy) && getObj(xx, yy) != null) {
            if (to_return == null) {
                to_return = map.getCell(xx, yy);
            } else if (map.getCell(xx, yy) == null) {
                to_return = map.getCell(xx, yy);

            }
        }
        return to_return;
    }

    public void drawGlow(Batch batch, float delta) {

    }

    public void act(float delta) {
        super.act(delta);
        sortActors();
    }

    public void sortActors() {
        sort.sort(this.getChildren(), comp);
    }

    public class AComparator implements Comparator<Actor> {
        public int compare(Actor arg0, Actor arg1) {
            if ((((BasicActor) arg0).getFrontBack()) < (((BasicActor) arg1).getFrontBack())) {
                return -1;
            } else if ((((BasicActor) arg0).getFrontBack()) > (((BasicActor) arg1).getFrontBack())) {
                return 1;
            } else if ((arg0).getY() > (arg1).getY()) {
                return -1;
            } else if ((arg0).getY() == (arg1).getY()) {
                if (((BasicActor) arg0).getZ() < ((BasicActor) arg1).getZ()) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return 1;
            }
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

    public boolean checkEnemyAnnihilated() {
        boolean team_annihilated = true;
        for (ObjActor obj : enemy_list) {
            if (obj.obj_state != PL_DEAD) {
                return false;
            }
        }
        return true;
    }
}

