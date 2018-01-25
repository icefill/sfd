package com.icefill.game.actors.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.icefill.game.actors.EquipActor;

public class CommonInventoryWindow extends BasicInventoryWindow {
	Table common_table;
	Table obj_inven_table;

	CommonInventoryWindow inventory;
	public CommonInventoryWindow(Skin skin, String[] item_name){
		super("COMMON INVENTORY",skin,item_name);
		obj_inven_table=new Table(skin);
		table.add(obj_inven_table);
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("healing_potion_10"));
		this.setSlot(new EquipActor("throwing_axe"));
	}
	public void addObjInventory(PersonalInventory inven) {
		if(this.inven!=null) this.inven.remove();
		obj_inven_table.add(inven).row();

		this.inven=inven;
		this.pack();
	}

}
