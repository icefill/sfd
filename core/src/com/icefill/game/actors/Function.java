package com.icefill.game.actors;

import com.icefill.game.actors.actionActors.ActionActor;

public interface Function
{
    int execute(DungeonGroup paramDungeonGroup, ObjActor paramObjActor, ActionActor paramActionActor,AreaCell current_target);
}