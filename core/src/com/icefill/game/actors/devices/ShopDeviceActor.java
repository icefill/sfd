package com.icefill.game.actors.devices;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.RoomGroup;
import com.icefill.game.actors.windows.ShopInventoryWindow;
import com.icefill.game.sprites.NonObjSprites;

public class ShopDeviceActor extends DeviceActor {
    Animation effect[];
    //TextureRegion shrine_deact;
    //TextureRegion shrine_act;
    boolean activated;
    ShopInventoryWindow shop_inven;

    public ShopDeviceActor() {
        passable = false;
        curr_dir = DIR.DL;
        shop_inven = new ShopInventoryWindow(Assets.getSkin(), null);
        Global.getUIStage().addActor(shop_inven);
        shop_inven.setVisible(false);
        sprites = Assets.non_obj_sprites_map.get("shop");


    }

    public ShopDeviceActor(AreaCell cell, RoomGroup room, int dungeon_level) {
        super(room);
        passable = false;
        curr_dir = DIR.DL;
        setX(cell.getX());
        setY(cell.getY());
        setZ(cell.getZ());
        setXX(cell.getXX());
        setYY(cell.getYY());

        shop_inven = new ShopInventoryWindow(Assets.getSkin(), null);
        shop_inven.setShopItem(dungeon_level, Global.dungeon);
        sprites = Assets.non_obj_sprites_map.get("shop");

        Global.getUIStage().addActor(shop_inven);
        shop_inven.setVisible(false);
        //sprites= Assets.non_obj_sprites_map.get("shop");

    }

    public void activateDevice(DungeonGroup dungeon, AreaCell cell) {
        if (!activated)
            revive(dungeon);
    }

    public void action(final DungeonGroup dungeon, final AreaCell target_cell) {

    }

    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        ((NonObjSprites) sprites).drawAnimation(batch, elapsed_time, 0, curr_dir, getX(), getY() + getZ());
    }

    public void revive(DungeonGroup dungeon) {
        shop_inven.setVisible(true);
    }

}
