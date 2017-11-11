package com.icefill.game.actors;

import com.icefill.game.actors.actionActors.ActionActor;

public abstract interface PassiveFunction
{
  public abstract int activatePassiveAction(DungeonGroup paramDungeonGroup, ObjActor paramObjActor);
  public abstract int deActivatePassiveAction(DungeonGroup paramDungeonGroup, ObjActor paramObjActor);
}