package com.icefill.game;

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
    public static final int DL = 0;
    public static final int DR = 1;
    public static final int UR = 2;
    public static final int UL = 3;
    public static final int N_DIRECTION = 4;
    public static final String[] direction_name = {"dl", "dr", "ur", "ul"};
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
}