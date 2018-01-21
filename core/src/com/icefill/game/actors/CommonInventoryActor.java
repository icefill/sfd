package com.icefill.game.actors;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.InventoryActor.HidingClickListener;
import com.icefill.game.sprites.ObjSprites;

public class CommonInventoryActor extends BasicInventoryActor {
	Table obj_inven_table;

	CommonInventoryActor inventory;
	public CommonInventoryActor(Skin skin,String[] item_name){
		super("COMMON INVENTORY",skin,item_name);
		obj_inven_table=new Table(skin);
		table.add(obj_inven_table);
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("throwing_axe"));
	}
	public void addObjInventory(InventoryActor inven) {
		if(this.inven!=null) this.inven.remove();
		obj_inven_table.add(inven).row();

		this.inven=inven;
		this.pack();
	}

}
