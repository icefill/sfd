package com.icefill.game.actors.devices;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.icefill.game.Assets;
import com.icefill.game.actors.dungeon.AreaCell;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.dungeon.RoomGroup;

public class SpikeTrapActor extends DeviceActor {
	TextureRegion spike_hide;
	TextureRegion spike;
	final Sound hit_sound;
	boolean spike_out;
	
	public SpikeTrapActor (AreaCell cell,RoomGroup room) {
		super(room);
		harzardeous=true;
		TextureAtlas atlas= Assets.getAsset("sprite/trap1.atlas",TextureAtlas.class);
		spike_hide= atlas.findRegion("trap10000");
		spike= atlas.findRegion("trap10001");
		setX(cell.getX());
		setY(cell.getY());
		setZ(cell.getZ());
		setXX(cell.getXX());
		setYY(cell.getYY());
		
		hit_sound= Assets.getAsset("sound/hit.wav", Sound.class);
		passable=true;
		
		
	}
	public void setSpike(boolean out) {
		spike_out=out;
	}
	
	public void draw(Batch batch, float delta){
		super.draw(batch,delta);
		if (spike_out) {
			batch.draw(spike, getX()-48, getY()-20);
		}
	//	else {
	//	}
		
	}
	public void drawDevice(Batch batch, float delta){
		super.draw(batch, delta);
		batch.draw(spike_hide, getX()-48, getY()-20);
	}
	
	public void action(final DungeonGroup dungeon,final AreaCell target_cell) {
		
		final ObjActor target= dungeon.getCurrentRoom().getObj(target_cell);
		if (target!=null){//&& target.isPositionchanged()) {
			if (8>(int)(0.3f*target.status.total_status.HP))
			{
				target.inflictDamage(10,null);
			}	
			else {
				target.inflictDamageInRatio(.3f,null);
			}
			//hit_sound.play();

		}
		final Sound hit_sound= Assets.getAsset("sound/slash.wav", Sound.class);
		hit_sound.play();
		
		this.addAction(
				Actions.sequence(
				//dungeon.getGFSM().pauseGFSAction(),
				Actions.run(new Runnable() {public void run() {setSpike(true);}}),
				//Actions.run(new Runnable() {public void run() {if (target!=null) target.addAction(target.basicHitAction(dungeon, target));}}),
				Actions.delay(.5f),
				Actions.run(new Runnable() {public void run() {setSpike(false);}})
				//dungeon.getGFSM().reRunGFSAction()
				)
			);
		
	}

}
