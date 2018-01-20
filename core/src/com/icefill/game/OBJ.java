package com.icefill.game;

public enum OBJ {
    WALL('w'),EXPLOSIVE('e'),OBSTACLE('o'),FIRE_BOWL('f'),DOOR('d'),TRAP('t')
    ,SHOP_CAT('C'),SHRINE('S'),MONSTER('m'),BOSS_MONSTER('b'),MAGIC_SCROLL('M'),ITEM('I'),WEAPON('W'),ANGEL('A'),RECRUIT_CAT('R')
    ,UNDEST_OBS('U'),DEST_OBS('D')
    ,NOTHING('x');
    final public char c;

    OBJ(char c) {
        this.c=c;
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
            default: return NOTHING;
        }
    }
}
