package com.icefill.game;

import com.badlogic.gdx.math.Vector2;
import com.icefill.game.utils.Randomizer;

public interface Constants {

    public static final int IDLE = 0;
    public static final int WALK = 1;
    public static final int ATTACK = 2;
    public static final int ATTACKED = 3;
    public static final int GUARD = 4;
    public static final int DEAD = 5;
    public static final int RAISE = 6;
    public static final int N_ANIMATION = 7;
    public static final String[] animation_name = {"idle", "walk", "attack", "attacked", "guard", "dead", "raise"};

    public static final int HEAD = 0;
    public static final int ARM1 = 1;
    public static final int ARM2 = 2;
    public static final int BODY = 3;
    public static final int N_BODYPARTS = 4;
    public static final String[] bodyparts_name = {"head", "arm1", "arm2", "body"};

    public enum FACING {
        FRONT(0),LEFT(1),BACK(2),RIGHT(3);
        final public int v ;
        FACING(int v) {this.v=v;}
    }

    public static final int EQUIPHEAD = 0;
    public static final int EQUIPBODY = 1;
    public static final int EQUIPW = 2;
    public static final int EQUIPS = 3;
    public static final String[] equip_name = {"equiophead", "equipbody", "equipl", "equipr"};
    public static final int N_EQUIP = 4;
    public static final int RADIATION_AREA = 0;
    public static final int STRAIGHTLINE_AREA = 1;
    public static final String[] targeting_area_name = {"radiation_area", "straightline_area"};
    public static final int PL_DONE = 0;
    public static final int PL_WAIT = 1;
    public static final int PL_ACTIVE = 2;
    public static final int PL_ATTACKED = 3;
    public static final int PL_DEAD = 4;
    public static final int N_PL_STATE = 6;
    public static final String[] pl_state_name = {"done", "wait", "active", "attacked", "dead"};

    public static final int GS_DUNGEON_INIT = 0;
    public static final int GS_DUNGEON_CLEAR = 1;
    public static final int GS_ROOM_INIT = 2;
    public static final int GS_BATTLE_INIT = 3;
    public static final int GS_BATTLE_END = 4;
    public static final int GS_TURN_INIT = 5;
    public static final int GS_TURN_END = 6;
    public static final int GS_PHASE_INIT = 7;
    public static final int GS_PHASE_END = 8;
    public static final int GS_CHOOSE_CHAR = 9;
    public static final int GS_CHOOSE_ACTION = 10;
    public static final int GS_SELECT_TARGET = 11;
    public static final int GS_INSPECTION_ACTING = 12;
    public static final int GS_PLAYER_ACTING = 13;
    public static final int GS_AI_ACTING = 14;
    public static final int GS_GAME_OVER = 15;
    public static final int GS_AI = 16;
    public static final int GS_DUNGEON_INSPECTION_INIT = 17;
    public static final String[] gs_state_name
            = {"DUNGEON_INIT", "DUNGEON_CLEAR", "ROOM_INIT", "BATTLE_INIT", "BATTLE_END", "TURN_INIT", "TURN_END", "PHASE_INIT", "PHASE_END", "CHOOSE_CHAR", "CHOOSE_ACTION",
            "SELECT_TARGET", "INSPECTION_ACTING", "PLAYER_ACTING", "AI_ACTING", "GAME_OVER", "AI", "DUNGEON_INSPECTION_INIT"};
    public static final int N_GAME_STATE = 18;

    public static final int CONTROLLED_PLAYER = 0;
    public static final int CONTROLLED_AI = 1;
    public static final String[] controlled_name = {"player", "AI"};

    enum DIR {
        DL(0),DR(1),UR(2),UL(3),AB(4),BL(5);
        final public int v;
        DIR() {this.v=0;}
        DIR(int v) {
            this.v=v;
        }
        public static DIR getRandomDIR(){
            return toDIR(Randomizer.nextInt(4));
        }
        public String toString(){
            switch (this) {
                case DL: return "dl";
                case DR: return "dr";
                case UR: return "ur";
                case UL: return "ul";
                case AB: return "ab";
                case BL: return "bl";
                default: throw new RuntimeException("Enum Dir function toString is ruined!");
            }
        }
        public DIR opposite() {
            switch (this) {
                case DL: return UR;
                case DR: return UL;
                case UR: return DL;
                case UL: return DR;
                case AB: return BL;
                case BL: return AB;
                default: return DL;
            }
        }
        public DIR turnLeft(int times) {
            if (this.equals(AB) || this.equals(AB) ) return this;
            return toDIR((this.v+times)%4);
        }
        public DIR turnRight(int times) {
            if (this.equals(AB) || this.equals(AB) ) return this;
            return toDIR((this.v-(times%4)+4)%4);
        }
        public static DIR toDIR(int i) {
            switch (i) {
                case 0: return DL;
                case 1: return DR;
                case 2: return UR;
                case 3: return UL;
                case 4: return AB;
                case 5: return BL;
                default: throw new RuntimeException("Only 0,1,2,3 can be changed to enum DIR but arg:"+i);
            }
        }
        public Vector2 toScreenVector() {
            Vector2 to_return;
            switch (this) {
                case DL:
                    to_return = new Vector2(-2, -1);
                    break;
                case DR:
                    to_return = new Vector2(2, -1);
                    break;
                case UR:
                    to_return = new Vector2(2, 1);
                    break;
                case UL:
                    to_return = new Vector2(-2, 1);
                    break;
                default:
                    to_return = new Vector2(0, 0);
                    break;
            }
            to_return.nor();
            return to_return;
        }

        public Vector2 toMapVector() {
            Vector2 to_return = new Vector2();
            switch (this) {
                case DL:
                    to_return.x = 0;
                    to_return.y = 1;
                    break;
                case DR:
                    to_return.x = 1;
                    to_return.y = 0;
                    break;
                case UR:
                    to_return.x = 0;
                    to_return.y = -1;
                    break;
                case UL:
                    to_return.x = -1;
                    to_return.y = 0;
                    break;
            }
            return to_return;
        }


    }

    enum OBJ {
        WALL('w'),DOWN_STAIR('↓'),EXPLOSIVE('e'),OBSTACLE('o'),FIRE_BOWL('f'),DOOR('d'),TRAP('t')
        ,SHOP_CAT('C'),SHRINE('S'),MONSTER('m'),BOSS_MONSTER('b'),MAGIC_SCROLL('M'),ITEM('I'),WEAPON('W'),ANGEL('A'),RECRUIT_CAT('R')
        ,UNDEST_OBS('U'),DEST_OBS('D')
        ,NOTHING('x');
        final public char c;

        OBJ(char c) {
            this.c=c;
        }

        public static boolean checkBlocked(OBJ obj) {
            if (obj.equals(WALL) || obj.equals(DOWN_STAIR) || obj.equals(OBSTACLE) || obj.equals(SHOP_CAT) || obj.equals(SHRINE) ||obj.equals(ANGEL) || obj.equals(RECRUIT_CAT) ||obj.equals(UNDEST_OBS)) return true;
            else return false;
        }
        public static OBJ toEnum(char ch) {
            switch (ch) {
                case 'w': return WALL;
                case 'e': return EXPLOSIVE;
                case 'o': return OBSTACLE;
                case 'f': return FIRE_BOWL;
                case 'd': return DOOR;
                case 't': return TRAP;
                case 'C': return SHOP_CAT;
                case 'S': return SHRINE;
                case 'm': return MONSTER;
                case 'b': return BOSS_MONSTER;
                case 'M': return MAGIC_SCROLL;
                case 'I': return ITEM;
                case 'W': return WEAPON;
                case 'A': return ANGEL;
                case 'R': return RECRUIT_CAT;
                case 'U': return UNDEST_OBS;
                case 'D': return DEST_OBS;
                case '↓': return DOWN_STAIR;
                default: return NOTHING;
            }
        }
    }
}