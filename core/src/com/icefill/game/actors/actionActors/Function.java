package com.icefill.game.actors.actionActors;

import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;

public interface Function
{
    int execute(DungeonGroup paramDungeonGroup, ObjActor paramObjActor, ActionActor paramActionActor, AreaCell current_target);
}