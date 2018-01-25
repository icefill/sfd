package com.icefill.game.actors.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.RoomShapeType;
import com.icefill.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.LinkedList;


public class DungeonSeed {
    int[][] initial_room;
    int[] dungeon_size;
    int dungeon_area;
    int[][] final_room;
    int[][] boss_room;
    float monster_room_ratio;

    int[] shop_n_in_floor;
    int[] healing_room_n_in_floor;
    int[] hire_room_n_in_floor;
    int[] shrine_in_floor;
    int[] treasure_room_in_floor;
    int[] scroll_room_in_floor;

    ArrayList<String> char_name_list;
    OrderedMap<String, RoomShapeType> room_shape_types;
    DungeonGroup.MonsterPool recruit_pool;
    DungeonGroup.MonsterPool monster_pool;
    DungeonGroup.MonsterPool boss_pool;
    DungeonGroup.ItemPool item_pool;
    DungeonGroup.ItemPool equip_pool;
    DungeonGroup.ItemPool scroll_pool;
    ArrayList<DungeonGroup.ObjListElt> undest_obs_info_list;
    ArrayList<DungeonGroup.ObjListElt> dest_obs_info_list;
    ArrayList<DungeonGroup.ObjListElt> explosive_name_list;
    RoomSeed[][][] room_array;
    LinkedList<RoomSeed> stack;

    public DungeonSeed(String dungeonPropertiesName) {
        readDungeonProperties(dungeonPropertiesName);
        constructDungeonSeed();
    }

    public void readDungeonProperties(String dungeonPropertiesName) {
        JsonReader json = new JsonReader();
        JsonValue dungeon_prop = json.parse(Gdx.files.internal(dungeonPropertiesName));

        JsonValue dungeonSize = dungeon_prop.get("dungeon_size");
        dungeon_size = new int[]{dungeonSize.get("xxx").asInt(), dungeonSize.get("xxx").asInt(), dungeonSize.get("xxx").asInt()};
        room_array = new RoomSeed[dungeon_size[0]][dungeon_size[1]][dungeon_size[2]];

        monster_room_ratio = dungeon_prop.get("monster_room_ratio").asInt();

        shop_n_in_floor = dungeon_prop.get("shop_n_in_floor").asIntArray();
        healing_room_n_in_floor = dungeon_prop.get("healing_room_n_in_floor").asIntArray();
        hire_room_n_in_floor = dungeon_prop.get("hire_room_n_in_floor").asIntArray();
        shrine_in_floor = dungeon_prop.get("shrine_in_floor").asIntArray();
        treasure_room_in_floor = dungeon_prop.get("treasure_room_in_floor").asIntArray();
        scroll_room_in_floor = dungeon_prop.get("scroll_room_in_floor").asIntArray();

        room_shape_types = new OrderedMap<String, RoomShapeType>();
        for (JsonValue jsonValue : dungeon_prop.get("room_type")) {
            String room_type_name = jsonValue.get("name").asString();
            String floor_name = jsonValue.get("floor_name").asString();
            String stair_name = jsonValue.get("down_stair_name").asString();
            String wall_name = jsonValue.get("wall_name").asString();
            String door_name = jsonValue.get("door_name").asString();
            String fire_bowl_name = jsonValue.get("fire_bowl_name").asString();

            if (Assets.floor_map.get(floor_name) == null)
                throw new RuntimeException("Wrong floor name:" + floor_name + " in room_type:" + room_type_name);
            if (Assets.floor_map.get(stair_name) == null)
                throw new RuntimeException("Wrong stair name:" + floor_name + " in room_type:" + room_type_name);
            if (Assets.floor_map.get(wall_name) == null)
                throw new RuntimeException("Wrong wall name:" + wall_name + " in room_type:" + room_type_name);
            if (Assets.floor_map.get(door_name) == null)
                throw new RuntimeException("Wrong door name:" + door_name + " in room_type:" + room_type_name);
            if (Assets.jobs_map.get(fire_bowl_name) == null)
                throw new RuntimeException("Wrong fire bowl name:" + fire_bowl_name + " in room_type:" + room_type_name);
            room_shape_types.put(room_type_name, new RoomShapeType(floor_name, stair_name, wall_name, door_name, fire_bowl_name));
        }


        char_name_list = new ArrayList<String>();
        for (JsonValue nameValue : dungeon_prop.get("initial_characters_name")) {
            String name = nameValue.asString();
            if (!Gdx.files.internal("objs_data/chars/" + name + ".json").exists())
                throw new RuntimeException("Character" + name + "does not exsit!");
            else char_name_list.add(name);
        }


        recruit_pool = new DungeonGroup.MonsterPool();
        for (JsonValue poolNameValue : dungeon_prop.get("mercernaries")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (Assets.jobs_map.get(name) == null) throw new RuntimeException("merc " + name + " does not exsit.");
                else {
                    int n = eltValue.get("mul").asInt();
                    for (int i = 0; i < n; i++)
                        pool_elt.add(new DungeonGroup.ObjListElt(null, name, eltValue.get("level").asInt(), 1));
                    inserted = true;
                }
            }
            if (inserted) recruit_pool.addMonsterPoolElt(pool_elt);

        }

        monster_pool = new DungeonGroup.MonsterPool();
        for (JsonValue poolNameValue : dungeon_prop.get("monsters")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (Assets.jobs_map.get(name) == null)
                    throw new RuntimeException("monster " + name + " does not exsit.");
                else {
                    int n = eltValue.get("mul").asInt();
                    for (int i = 0; i < n; i++)
                        pool_elt.add(new DungeonGroup.ObjListElt(null, name, eltValue.get("level").asInt(), 1));
                    inserted = true;
                }
            }
            if (inserted) monster_pool.addMonsterPoolElt(pool_elt);

        }

        boss_pool = new DungeonGroup.MonsterPool();
        for (JsonValue poolNameValue : dungeon_prop.get("bosses")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.MonsterPoolElt pool_elt = new DungeonGroup.MonsterPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (Assets.jobs_map.get(name) == null) throw new RuntimeException("Boss " + name + " does not exsit.");
                else {
                    int n = eltValue.get("mul").asInt();
                    for (int i = 0; i < n; i++)
                        pool_elt.add(new DungeonGroup.ObjListElt(null, name, eltValue.get("level").asInt(), 1));
                    inserted = true;
                }
            }
            if (inserted) boss_pool.addMonsterPoolElt(pool_elt);
        }

        scroll_pool = new DungeonGroup.ItemPool();
        for (JsonValue poolNameValue : dungeon_prop.get("scrolls_in_chest")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (name.startsWith("S#")) {
                    if (Assets.actions_map.get(name.substring(2)) == null)
                        throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++) pool_elt.add(name);
                        inserted = true;
                    }
                } else {
                    if (!Gdx.files.internal("objs_data/equipment/" + name + ".json").exists())
                        throw new RuntimeException("skill " + name + " does not exsit.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++)
                            pool_elt.add(name);
                        inserted = true;
                    }
                }
            }
            if (inserted) scroll_pool.addItemPoolElt(pool_elt);
        }

        equip_pool = new DungeonGroup.ItemPool();
        for (JsonValue poolNameValue : dungeon_prop.get("weapons_in_chest")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (name.startsWith("S#")) {
                    if (Assets.actions_map.get(name.substring(2)) == null)
                        throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++) pool_elt.add(name);
                        inserted = true;
                    }
                } else {
                    if (!Gdx.files.internal("objs_data/equipment/" + name + ".json").exists())
                        throw new RuntimeException("skill " + name + " does not exist.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++)
                            pool_elt.add(name);
                        inserted = true;
                    }
                }
            }
            if (inserted) equip_pool.addItemPoolElt(pool_elt);
        }
        item_pool = new DungeonGroup.ItemPool();
        for (JsonValue poolNameValue : dungeon_prop.get("items")) {
            Integer min = poolNameValue.get("min_floor").asInt();
            Integer max = poolNameValue.get("max_floor").asInt();
            JsonValue namesValue = poolNameValue.get("names");
            DungeonGroup.ItemPoolElt pool_elt = new DungeonGroup.ItemPoolElt(min, max);
            Boolean inserted = false;
            for (JsonValue eltValue : namesValue) {
                String name = eltValue.get("name").asString();
                if (name.startsWith("S#")) {
                    if (Assets.actions_map.get(name.substring(2)) == null)
                        throw new RuntimeException("skill " + name.substring(2) + " does not exsit.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++) pool_elt.add(name);
                        inserted = true;
                    }
                } else {
                    if (!Gdx.files.internal("objs_data/equipment/" + name + ".json").exists())
                        throw new RuntimeException("item" + name.substring(2) + " does not exsit.");
                    else {
                        int n = eltValue.get("mul").asInt();
                        for (int i = 0; i < n; i++)
                            pool_elt.add(name);
                        inserted = true;
                    }
                }
            }
            if (inserted) item_pool.addItemPoolElt(pool_elt);
        }

        explosive_name_list = new ArrayList();
        for (JsonValue obs : dungeon_prop.get("explosive_obs_list")) {
            String name = obs.get("name").asString();
            if (Assets.jobs_map.get(name) != null) {
                int n = obs.get("mul").asInt();
                for (int i = 0; i < n; i++)
                    explosive_name_list.add(new DungeonGroup.ObjListElt(null, name, 1, -1));
            } else throw new RuntimeException("explosive " + name + " does not exsit.");
        }

        dest_obs_info_list = new ArrayList();
        for (JsonValue obs : dungeon_prop.get("destructible_obs_list")) {
            String name = obs.get("name").asString();
            if (Assets.jobs_map.get(name) != null) {
                int n = obs.get("mul").asInt();
                for (int i = 0; i < n; i++)
                    dest_obs_info_list.add(new DungeonGroup.ObjListElt(null, name, 1, -1));
            } else throw new RuntimeException("obstacle " + name + " does not exsit.");
        }
        undest_obs_info_list = new ArrayList();
        for (JsonValue obs : dungeon_prop.get("undestructible_obs_list")) {
            String name = obs.get("name").asString();
            if (Assets.jobs_map.get(name) != null) {
                int n = obs.get("mul").asInt();
                for (int i = 0; i < n; i++)
                    undest_obs_info_list.add(new DungeonGroup.ObjListElt(null, name, 5, -1));
            } else throw new RuntimeException("obstacle " + name + " does not exsit.");
        }
    }

    public void constructDungeonSeed() {
        chooseInitialAndFinalRooms();
        boss_room = new int[dungeon_size[2]][2];

        stack = new LinkedList<RoomSeed>();


        room_array = new RoomSeed[dungeon_size[0]][dungeon_size[1]][dungeon_size[2]];

        dungeon_area = dungeon_size[0] * dungeon_size[1];

        float monster_room_ratio = 0.33f;


        //Initialize rooms

        for (int xxx = 0; xxx < dungeon_size[0]; xxx++) {
            for (int yyy = 0; yyy < dungeon_size[1]; yyy++) {
                for (int zzz = 0; zzz < dungeon_size[2]; zzz++) {
                    //Randomize room size
                    int room_size_x = com.icefill.game.utils.Randomizer.nextInt((int) (8 + zzz * .35f), 9 + (int) (zzz * .5f));
                    int room_size_y = com.icefill.game.utils.Randomizer.nextInt((int) (8 + zzz * .35f), 9 + (int) (zzz * .5f));
                    room_array[xxx][yyy][zzz] = new RoomSeed(room_shape_types.get("basic"), xxx, yyy, room_size_x, room_size_y);

                    if (xxx == initial_room[zzz][0] && yyy == initial_room[zzz][1])
                        room_array[xxx][yyy][zzz].checkInitialRoom();

                    //Set Floor
                }
            }
        }

        makeDungeonRoute();


        //Make Obstacle and monsters

        for (int zzz = 0; zzz < dungeon_size[2]; zzz++) {
            for (int xxx = 0; xxx < dungeon_size[0]; xxx++) {
                for (int yyy = 0; yyy < dungeon_size[1]; yyy++) {
                    if ( //room_array[xxx][yyy][zzz].initial_room
                            (xxx == initial_room[zzz][0] && yyy == initial_room[zzz][1])

                            ) {
                        room_array[xxx][yyy][zzz].makeWallAndDoor();
                        if (zzz != 0) {
                            room_array[xxx][yyy][zzz].makeHealingRoom();
                            //room_array[xxx][yyy][zzz].makeUpStair();
                        } else {
                            room_array[xxx][yyy][zzz].makeInitialRoom();
                        }
                        // Do some stuff
                    } else if (xxx == final_room[zzz][0] && yyy == final_room[zzz][1]) {
                        room_array[xxx][yyy][zzz].makeWallAndDoor();
                        if (zzz != dungeon_size[2]) {

                            room_array[xxx][yyy][zzz].makeFinalRoom();
                            room_array[xxx][yyy][zzz].room_type = 3;
                        }
                    } else {
                        int room_area = room_array[xxx][yyy][zzz].getRoomArea();

                        //Create Obstacles
                        int max_obs = (int) (room_area * 0.2f);
                        int min_obs = (int) (room_area * 0.15f);
                        int obs_n_in_room = 0;
                        obs_n_in_room = com.icefill.game.utils.Randomizer.nextInt(min_obs, max_obs);
                        room_array[xxx][yyy][zzz].createObstacles(obs_n_in_room);
                        room_array[xxx][yyy][zzz].makeWallAndDoor();
                    }


                }
            }


        }


        com.icefill.game.utils.NonRepeatRandomizer randomizer = new com.icefill.game.utils.NonRepeatRandomizer(dungeon_size[0], dungeon_size[1]);
        int rn;

        for (int i = 0; i < dungeon_size[2]; i++) {
            randomizer.reset();
            int p = 0;
            //Create Monsters
            for (int k = 0; k < dungeon_size[0] * dungeon_size[1] * monster_room_ratio; k++) {
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        int monster_n = com.icefill.game.utils.Randomizer.nextInt((int) ((i) * .5f + 3), (int) ((i) * .5f + 4));//(int)(room_array[xx][yy][i].getRoomArea()*0.022f+(i+1));
                        room_array[xx][yy][i].createMonster(monster_n);
                        room_array[xx][yy][i].room_type = 4;
                        p++;
                        break;
                    }
                }
            }
            int n_room = Randomizer.nextInt(healing_room_n_in_floor[0], healing_room_n_in_floor[1]);
            for (int k = 0; k < n_room; k++)
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makeHealingRoom();
                        room_array[xx][yy][i].room_type = 5;
                        break;
                    }
                }
            n_room = Randomizer.nextInt(shrine_in_floor[0], shrine_in_floor[1]);
            for (int k = 0; k < n_room; k++)
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makeShrineRoom();
                        room_array[xx][yy][i].room_type = 5;
                        break;
                    }
                }

            n_room= Randomizer.nextInt(treasure_room_in_floor[0], treasure_room_in_floor[1]);
            for (int k = 0; k < n_room; k++) {
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makeItemRoom();
                        room_array[xx][yy][i].room_type = 2;
                        break;
                    }
                }
            }
            n_room= Randomizer.nextInt(shop_n_in_floor[0], shop_n_in_floor[1]);
            for (int k = 0; k < n_room; k++) {
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makeShopRoom();
                        room_array[xx][yy][i].room_type = 2;
                        break;
                    }
                }
            }
            n_room= Randomizer.nextInt(scroll_room_in_floor[0], scroll_room_in_floor[1]);
            for (int k = 0; k < n_room; k++) {
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makeScrollRoom();
                        room_array[xx][yy][i].room_type = 2;
                        break;
                    }
                }
            }
            n_room= Randomizer.nextInt(hire_room_n_in_floor[0], hire_room_n_in_floor[1]);
            for (int k = 0; k < n_room; k++) {
                for (int j = 0; j < 10; j++) {
                    rn = randomizer.nextInt();
                    int xx = rn / 10;
                    int yy = rn % 10;
                    if (
                            (xx != initial_room[i][0] || yy != initial_room[i][1]) &&
                                    (xx != final_room[i][0] || yy != final_room[i][1]) &&
                                    (xx != boss_room[i][0] || yy != boss_room[i][1])
                            ) {
                        room_array[xx][yy][i].makemercRoom();
                        room_array[xx][yy][i].room_type = 2;
                        break;
                    }
                }
            }
            int monster_n = com.icefill.game.utils.Randomizer.nextInt((int) ((i) * .5f + 4), (int) ((i) * .5f + 4));
            room_array[boss_room[i][0]][boss_room[i][1]][i].createMonster(monster_n);
            room_array[boss_room[i][0]][boss_room[i][1]][i].createBoss();
            room_array[boss_room[i][0]][boss_room[i][1]][i].room_type = 1;

        }


    }

    public void clearVisited(int zzz) {
        for (int xx = 0; xx < dungeon_size[0]; xx++) {
            for (int yy = 0; yy < dungeon_size[1]; yy++) {
                if (room_array[xx][yy][zzz] != null)
                    room_array[xx][yy][zzz].visited = false;
            }
        }
    }

    public void makeDungeonRoute() {
        for (int zzz = 0; zzz < dungeon_size[2]; zzz++) {
            do {
                stack.clear();
                int current_xxx = initial_room[zzz][0];
                int current_yyy = initial_room[zzz][1];
                stack.add(room_array[current_xxx][current_yyy][zzz]);
                stack.getLast().visited = true;
                LinkedList<Integer> possible_door = new LinkedList<Integer>();
                RoomSeed back_room = stack.getLast();
                int iter = 0;

                while
                        (current_xxx != final_room[zzz][0] || current_yyy != final_room[zzz][1]
                        ) {

                    int temp_xxx = 0;
                    int temp_yyy = 0;

                    RoomSeed temp_room;


                    temp_xxx = current_xxx;
                    temp_yyy = current_yyy + 1;
                    if (
                            (0 <= temp_xxx && temp_xxx < dungeon_size[0])
                                    && (0 <= temp_yyy && temp_yyy < dungeon_size[1])
                            ) {
                        temp_room = room_array[temp_xxx][temp_yyy][zzz];
                        if (
                                !(temp_room.visited)
                                        && !back_room.equals(temp_room)
                                ) {
                            possible_door.add(Constants.DL);
                        }
                    }
                    temp_xxx = current_xxx + 1;
                    temp_yyy = current_yyy;
                    if (
                            (0 <= temp_xxx && temp_xxx < dungeon_size[0])
                                    && (0 <= temp_yyy && temp_yyy < dungeon_size[1])
                            ) {
                        temp_room = room_array[temp_xxx][temp_yyy][zzz];
                        if (
                                !(temp_room.visited)
                                        && !back_room.equals(temp_room)
                                ) {
                            possible_door.add(Constants.DR);
                        }
                    }
                    temp_xxx = current_xxx;
                    temp_yyy = current_yyy - 1;
                    if (
                            (0 <= temp_xxx && temp_xxx < dungeon_size[0])
                                    && (0 <= temp_yyy && temp_yyy < dungeon_size[1])
                            ) {
                        temp_room = room_array[temp_xxx][temp_yyy][zzz];
                        if (
                                !(temp_room.visited)
                                        && !back_room.equals(temp_room)
                                ) {
                            possible_door.add(Constants.UR);
                        }
                    }
                    temp_xxx = current_xxx - 1;
                    temp_yyy = current_yyy;
                    if (
                            (0 <= temp_xxx && temp_xxx < dungeon_size[0])
                                    && (0 <= temp_yyy && temp_yyy < dungeon_size[1])
                            ) {
                        temp_room = room_array[temp_xxx][temp_yyy][zzz];
                        if (
                                !(temp_room.visited)
                                        && !back_room.equals(temp_room)
                                ) {
                            possible_door.add(Constants.UL);
                        }
                    }

                    // has no possible door -> backtrack
                    if (possible_door.size() == 0) {
                        RoomSeed temp = stack.pollLast();
                        //temp.visited=false;
                        current_xxx = stack.getLast().xxx;
                        current_yyy = stack.getLast().yyy;
                        back_room = temp;
                    } else {
                        int next_direction = 0;
                        // choose next direction
                        if (possible_door.size() == 1) {
                            next_direction = possible_door.getLast();
                        } else {
                            //Make door direction
                            next_direction = possible_door.get(com.icefill.game.utils.Randomizer.nextInt(possible_door.size()));
                        }

                        switch (next_direction) {
                            case Constants.DL:
                                current_yyy = current_yyy + 1;
                                break;
                            case Constants.DR:
                                current_xxx = current_xxx + 1;
                                break;
                            case Constants.UR:
                                current_yyy = current_yyy - 1;
                                break;
                            case Constants.UL:
                                current_xxx = current_xxx - 1;
                                break;
                        }
                        stack.add(room_array[current_xxx][current_yyy][zzz]);
                        room_array[current_xxx][current_yyy][zzz].visited = true;
                    }
                    possible_door.clear();
                    iter++;

                    // if has at list one , randomly choose 1
                    // if has not, backtrack
                } // while
                clearVisited(zzz);

            } while (stack.size() == 0);

            // make door
            for (int i = 0; i < stack.size() - 1; i++) {
                int dx = stack.get(i + 1).xxx - stack.get(i).xxx;
                int dy = stack.get(i + 1).yyy - stack.get(i).yyy;
                if (dx > 0) {
                    stack.get(i + 1).has_door[Constants.UL] = true;
                    stack.get(i).has_door[Constants.DR] = true;
                } else if (dx < 0) {
                    stack.get(i + 1).has_door[Constants.DR] = true;
                    stack.get(i).has_door[Constants.UL] = true;
                } else if (dy > 0) {
                    stack.get(i + 1).has_door[Constants.UR] = true;
                    stack.get(i).has_door[Constants.DL] = true;
                } else {
                    stack.get(i + 1).has_door[Constants.DL] = true;
                    stack.get(i).has_door[Constants.UR] = true;
                }
            }

            //check boss_room

            boss_room[zzz][0] = stack.get(stack.size() - 2).xxx;
            boss_room[zzz][1] = stack.get(stack.size() - 2).yyy;

            for (RoomSeed temp : stack) {
                temp.visited = true;
            }

            // Make door to not visited room
            for (int xx = 0; xx < dungeon_size[0]; xx++) {
                for (int yy = 0; yy < dungeon_size[1]; yy++) {
                    if (!(room_array[xx][yy][zzz].visited)) {
                        for (int i = 0; i < 4; i++) {
                            if (com.icefill.game.utils.Randomizer.hitInRatio(0.5f)) {
                                switch (i) {
                                    case Constants.DL:
                                        if (yy + 1 < dungeon_size[1] && !isFinalRoom(xx, yy + 1, zzz)) {
                                            room_array[xx][yy][zzz].has_door[i] = true;
                                            room_array[xx][yy + 1][zzz].has_door[Constants.UR] = true;
                                        }
                                        break;
                                    case Constants.DR:
                                        if (xx + 1 < dungeon_size[0] && !isFinalRoom(xx + 1, yy, zzz)) {
                                            room_array[xx][yy][zzz].has_door[i] = true;
                                            room_array[xx + 1][yy][zzz].has_door[Constants.UL] = true;
                                        }
                                        break;
                                    case Constants.UR:
                                        if (0 <= yy - 1 && !isFinalRoom(xx, yy - 1, zzz)) {
                                            room_array[xx][yy][zzz].has_door[i] = true;
                                            room_array[xx][yy - 1][zzz].has_door[Constants.DL] = true;
                                        }
                                        break;
                                    case Constants.UL:
                                        if (0 <= xx - 1 && !isFinalRoom(xx - 1, yy, zzz)) {
                                            room_array[xx][yy][zzz].has_door[i] = true;
                                            room_array[xx - 1][yy][zzz].has_door[Constants.DR] = true;
                                        }
                                        break;
                                }

                            }
                        }
                    }
                }

            }//for
        }
        showDungeonStatus();
    }

    public boolean isBossRoom(int xxx, int yyy, int zzz) {
        if (boss_room[zzz][0] == xxx && boss_room[zzz][1] == yyy)
            return true;
        return false;
    }

    public boolean isFinalRoom(int xxx, int yyy, int zzz) {
        if (final_room[zzz][0] == xxx && final_room[zzz][1] == yyy)
            return true;
        return false;
    }

    public void chooseInitialAndFinalRooms() {
        int minimum_length = (dungeon_size[0] + 1) / 2 + (dungeon_size[1] + 1) / 2 - 1;
        initial_room = new int[dungeon_size[2]][2];
        final_room = new int[dungeon_size[2]][2];
        //initial_room[0][0]=0;
        //initial_room[0][1]=0;
        //final_room[0][0]=dungeon_size[0]-1;
        //final_room[0][1]=dungeon_size[1]-1;

        for (int i = 0; i < dungeon_size[2]; i++) {

            if (i % 2 == 0) {
                initial_room[i][0] = 0;
                initial_room[i][1] = 0;
                final_room[i][0] = dungeon_size[0] - 1;
                final_room[i][1] = dungeon_size[1] - 1;

            } else {
                initial_room[i][0] = dungeon_size[0] - 1;
                initial_room[i][1] = dungeon_size[1] - 1;
                final_room[i][0] = 0;
                final_room[i][1] = 0;

            }
        }
    }

    public void showDungeonStatus() {
        for (int i = 0; i < dungeon_size[2]; i++) {
        }
        return;
    }

    public String toString() {
        String to_return = "";
        to_return += ("Dungeon_size:" + dungeon_size[0] + "," + dungeon_size[1] + "\n");
        to_return += ("Initial room:" + initial_room[0] + "," + initial_room[1] + "\n");
        to_return += ("charn:" + char_name_list.size() + "\n");
        //to_return+=("objn:"+obj_list.size()+"\n");
        to_return += ("obsn:" + (undest_obs_info_list.size() + dest_obs_info_list.size()) + "\n");
        for (int x = 0; x < dungeon_size[0]; x++) {
            for (int y = 0; y < dungeon_size[1]; y++) {
                to_return += room_array[x][y].toString() + "\n";
            }
        }
        return to_return;

    }
}
