package com.icefill.game.actors.actionActors;

import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.dungeon.DungeonGroup;

public abstract interface PassiveFunction
{
  public abstract int activatePassiveAction(com.icefill.game.actors.dungeon.DungeonGroup paramDungeonGroup, ObjActor paramObjActor);
  public abstract int deActivatePassiveAction(DungeonGroup paramDungeonGroup, ObjActor paramObjActor);
}