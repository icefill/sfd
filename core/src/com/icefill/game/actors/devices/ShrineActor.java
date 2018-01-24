package com.icefill.game.actors.devices;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.utils.Randomizer;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.actionActors.Function;
import com.icefill.game.actors.dungeon.RoomGroup;
import com.icefill.game.actors.actionActors.ObjActions;
import com.icefill.game.sprites.NonObjSprites;

public class ShrineActor extends DeviceActor{
	private float door_x=0;
	private float door_y=0;
	Animation effect[];
	TextureRegion shrine_deact;
	TextureRegion shrine_act;
	boolean activated;
	Rune rune;
	Function function;
	static ArrayList<Function> shrine_effect_list;

	public ShrineActor() {
			passable=false;
			curr_dir=0;
			TextureAtlas atlas= Assets.getAsset("sprite/shrine.atlas",TextureAtlas.class);
			shrine_deact= atlas.findRegion("idle_dl0000");
			shrine_act= atlas.findRegion("idle_dl0000");
			
			rune= new Rune("B");
			//rune.setPosition(getX(),getY());
			this.addActor(rune);
			rune.setPosition(16, 70);
	}
	public ShrineActor(AreaCell cell, RoomGroup room) {
		super(room);
		passable=false;
		curr_dir=0;
	    
		TextureAtlas atlas= Assets.getAsset("sprite/shrine.atlas",TextureAtlas.class);
		shrine_deact= atlas.findRegion("idle_dl0000");
		shrine_act= atlas.findRegion("idle_dl0000");
		if (shrine_effect_list ==null)
			initializeShrine();
		int n=Randomizer.nextInt(shrine_effect_list.size());
		function= shrine_effect_list.get(n);
		
		setX(cell.getX());
		setY(cell.getY());
		setZ(cell.getZ());
		setXX(cell.getXX());
		setYY(cell.getYY());
		rune= new Rune(Character.toString((char)(65+n)));
		this.addActor(rune);
		rune.setPosition(16, 70);
}
	public static void initializeShrine()
	{
		shrine_effect_list= new ArrayList<Function>(); 
	    shrine_effect_list.add(ObjActions.getSubAction("show_map"));
	    shrine_effect_list.add(ObjActions.getSubAction("double_gold"));
	    shrine_effect_list.add(ObjActions.getSubAction("half_gold"));
	    shrine_effect_list.add(ObjActions.getSubAction("half_hp"));
	    shrine_effect_list.add(ObjActions.getSubAction("full_hp"));
	    Collections.shuffle(shrine_effect_list);

	}
	public static void shuffleShrine()
	{
		if (shrine_effect_list==null)
		{
			initializeShrine();
		}
		else
		{
		    Collections.shuffle(shrine_effect_list);
		}
	}

	/*
	public float getDoorX(){
		return door_x;
	}
	public float getDoorY(){
		return door_y;
	}
	
	public void setDoorPosition(float x,float y)
	{
		door_x=x;
		door_y=y;
	}
	*/
	public void activateDevice(DungeonGroup dungeon,AreaCell cell) {
		if (!activated)
		{
			function.execute(dungeon, Global.getSelectedObj(), null, null);
			//revive(dungeon);
			rune.remove();
		}
	}	
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {
	
	}
	public void draw(Batch batch, float delta) {
		if (activated)
			batch.draw(shrine_act, getX()-48, getY()-20+getZ());
		else
			batch.draw(shrine_deact, getX()-48, getY()-20+getZ());
		super.draw(batch,delta);
		
 		//rune.draw(batch, delta);
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
	public class Rune extends BasicActor
	{
		NonObjSprites glow= (NonObjSprites)Assets.non_obj_sprites_map.get("glow");
		String word;
		public Rune(String word)
		{
			super();
			SequenceAction action= new SequenceAction();
			RepeatAction repeat= new RepeatAction();
			this.word=word;
			
			action.addAction(Actions.moveBy(0, 5,2f));
			action.addAction(Actions.moveBy(0,-5,2f));
			
			repeat.setAction(action);
			repeat.setCount(RepeatAction.FOREVER);
			this.addAction(repeat);
		}
		public void draw(Batch batch,float delta)
		{
			super.draw(batch,delta);
			//elapsed_time-=delta;
			batch.setBlendFunction(GL20.GL_DST_COLOR,GL20.GL_SRC_ALPHA);
	 		glow.drawAnimation(batch, elapsed_time, 1, 0, getX()-20,getY()
				,0,1f,1f);
	 		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Assets.getRuneFont().draw(batch, word, getX()-26, getY()+10);
			
		}
		
	}

}
