package com.icefill.game.actors.devices;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.RoomGroup;
import com.icefill.game.extendedActions.ExtendedActions;
import com.icefill.game.sprites.NonObjSprites;

public class GoldActor extends DeviceActor {
    EquipActor equip;
    Texture shadow;
    int amount;

    //	float rotation;
    public GoldActor(int amount, AreaCell cell, RoomGroup room) {
        super(room);
        sprites = Assets.non_obj_sprites_map.get("gold");
        this.amount=amount;
        curr_dir = DIR.DL;
        setX(cell.getX());
        setY(cell.getY());
        setZ(0);
        SequenceAction seq = new SequenceAction();
        seq.addAction(ExtendedActions.moveTo3D(getX(), getY(), getZ() + 5, .3f));
        seq.addAction(ExtendedActions.moveTo3D(getX(), getY(), getZ(), .3f));
        this.addAction(Actions.sequence(
                ExtendedActions.moveToParabolic(getX(), getY(), getZ(), .3f),
                Actions.forever(seq)
                )
        );

        harzardeous = false;
        shadow = Assets.getAsset("sprite/shadow.png", Texture.class);
        //Global.gfs.pushState(10);

    }


    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        batch.setColor(1f, 1f, 1f, 0.6f);
        batch.draw(shadow, getX() - 16, getY() - 8);
        batch.setColor(1f, 1f, 1f, 1f);
        ((NonObjSprites) sprites).drawAnimationMiddleRotation(batch, elapsed_time, 0, DIR.DL, getX(), getY() + getZ(), getRotation(), 1f, 1f);

    }

    public void action(final DungeonGroup dungeon, final AreaCell target_cell) {
        final AreaCell cell = dungeon.getCurrentRoom().getCell(dungeon.getSelectedObj());
        if (dungeon.getCurrentRoom().getObj(target_cell) != null) {
            this.addAction(Actions.sequence(ExtendedActions.moveTo3D(getX(), getY(), getZ() + 15f, .7f),
                    Actions.run(new Runnable() {
                        public void run() {
                            cell.device = null;
                            if (Global.getSelectedObj()!=null)
                                Global.dungeon.getTeam(Global.getSelectedObj().getTeam()).increaseGold(amount);
                            self.remove();
                        }
                    }))
            );
            Sound hit_sound = Assets.getAsset("sound/item.wav", Sound.class);
            hit_sound.play();


        }
        //dungeon.getCurrentRoom().getMap().getAreaCell(dungeon.getSelectedObj().getXX(),dungeon.getSelectedObj().getYY()).device=null;
    }


}
