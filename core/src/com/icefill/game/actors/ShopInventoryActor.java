package com.icefill.game.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;

public class ShopInventoryActor extends BasicInventoryActor{
	TextButton buy_button;
	public ShopInventoryActor(Skin skin, String[] ddd)
	{
		super("SHOP",skin,ddd);
		name="shop";
		for (int i=0;i<INVEN_SIZE;i++)
		{
			getSlot(i).setShopProduct();
		}
		setUnstackable();
		
		
		
		
	}
	public void setHireItem(DungeonGroup dungeon)
	{
		setSlot(new EquipActor("S#Hire novice"));
		setSlot(new EquipActor("S#Hire novice"));
		setSlot(new EquipActor("S#Hire novice bowman"));
		setSlot(new EquipActor("S#Hire novice bowman"));
		setSlot(new EquipActor("S#Hire apprentice mage"));
		
	}
	public void setShopItem(int level,DungeonGroup dungeon )
	{
		//setSlot(new EquipActor("S#Hire novice"));
		//setSlot(new EquipActor("S#Hire novice bowman"));
		//setSlot(new EquipActor("S#Hire apprentice mage"));
		
		setSlot(new EquipActor("healing_potion_10"));
		setSlot(new EquipActor("healing_potion_10"));
		setSlot(new EquipActor("throwing_axe"));
		
		for (int i=0;i<8;i++)
		{
			setSlot(new EquipActor(dungeon.equip_pool.getItem(level)));
		}
		for (int i=0;i<2;i++)
		{
			setSlot(new EquipActor(dungeon.item_pool.getItem(level)));
		}
		for (int i=0;i<2;i++)
		{
			setSlot(new EquipActor(dungeon.scroll_pool.getItem(level)));
		}
		
		/*
		setSlot(new EquipActor("armor1"));
		setSlot(new EquipActor("armor2"));
		setSlot(new EquipActor("shield1"));
		setSlot(new EquipActor("shield2"));
		setSlot(new EquipActor("bow1"));
		setSlot(new EquipActor("spear"));
		setSlot(new EquipActor("S#Fireball"));
		*/
	}

}
