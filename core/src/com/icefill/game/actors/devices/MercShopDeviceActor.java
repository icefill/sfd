package com.icefill.game.actors.devices;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.AreaCell;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.MercShopWindow;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.ProjectileActor;
import com.icefill.game.actors.RoomGroup;
import com.icefill.game.actors.ShopInventoryActor;
import com.icefill.game.sprites.NonObjSprites;
import com.icefill.game.sprites.ObjSprites;

public class MercShopDeviceActor extends DeviceActor{
	Animation effect[];
	//TextureRegion shrine_deact;
	//TextureRegion shrine_act;
	boolean activated;
	MercShopWindow merc_shop;
	//ShopInventoryActor shop_inven;
	
	public MercShopDeviceActor() {
			passable=false;
			curr_dir=0;
			//TextureAtlas atlas= Assets.getAsset("sprite/shrine.atlas",TextureAtlas.class);
			//shrine_deact= atlas.findRegion("idle_dl0000");
			//shrine_act= atlas.findRegion("idle_ur0000");
			//shop_inven= new ShopInventoryActor(Assets.getSkin(),null);
			//Global.getUIStage().addActor(shop_inven);
			//shop_inven.setVisible(false);
			sprites= Assets.non_obj_sprites_map.get("shop");
			
			
	}
	public MercShopDeviceActor(AreaCell cell,RoomGroup room,int dungeon_level) {
		super(room);
		passable=false;
		curr_dir=0;
		//TextureAtlas atlas= Assets.getAsset("sprite/shrine.atlas",TextureAtlas.class);
		//shrine_deact= atlas.findRegion("idle_dl0000");
		//shrine_act= atlas.findRegion("idle_ur0000");
		setX(cell.getX());
		setY(cell.getY());
		setZ(cell.getZ());
		setXX(cell.getXX());
		setYY(cell.getYY());
		
		merc_shop= new MercShopWindow(dungeon_level,Global.dungeon.recruit_pool);
		//shop_inven= new ShopInventoryActor(Assets.getSkin(),null);
			//shop_inven.setHireItem(Global.dungeon);
		sprites= Assets.non_obj_sprites_map.get("merc");
		Global.getUIStage().addActor(merc_shop);
		merc_shop.setVisible(false);
		//sprites= Assets.non_obj_sprites_map.get("shop");
		
}

	public void activateDevice(DungeonGroup dungeon,AreaCell cell) {
		if (!activated)
			revive(dungeon);
	}	
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {
	
	}
	public void draw(Batch batch, float delta) {
		super.draw(batch,delta);
		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, curr_dir, getX(), getY()+getZ());
	}
	/*
	public void drawDevice(Batch batch, float delta){
		super.draw(batch,delta);
		if (activated)
			batch.draw(shrine_act, getX()-48, getY()-20+getZ());
		else
			batch.draw(shrine_deact, getX()-48, getY()-20+getZ());
	}
	*/
	public void revive(DungeonGroup dungeon) {
		merc_shop.setVisible(true);
		//Global.reserveCameraPosition();
		//Global.current_screen.camera.position.set(getX(),getY()+140,0);
		
	}

}
