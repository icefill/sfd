package com.icefill.game.utils;

import java.util.List;

public class StaticFunctions {

    public static<T> T chooseAmong(List<T> list) {
        return list.get(Randomizer.nextInt(list.size()));
    }
}
