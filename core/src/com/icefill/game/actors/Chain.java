package com.icefill.game.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.icefill.game.Assets;

public class Chain extends Actor{
	Vector2 start_point;
	Vector2 destination_point;
	Vector2 current_end_point;
	Vector2 current_interval;
	float chain_interval=12.0f;
	float delta=0;
	float time=0;
	float elapsed=0;
	float length;
	float current_length;
	Vector2 extend_per_time;
	Vector2 chain_position;
	int chain_n;
	private boolean extending;
	private boolean attached;
	private BasicActor to_attach;
	TextureRegion chain_ring;
	TextureRegion chain_end;
	
	public Chain(float x, float y) {
		start_point= new Vector2();
		start_point.set(x,y);
		destination_point= new Vector2();
		current_end_point= new Vector2();
		current_interval= new Vector2();
		chain_position= new Vector2();
		TextureAtlas atlas= Assets.getAsset("sprite/chain.atlas",TextureAtlas.class);
		chain_ring= atlas.findRegion("chain_ring");
		chain_end= atlas.findRegion("chain_end");
	}
	
	public void extendChain(float x, float y,float time) {
		this.time=time;
		extending=true;
		elapsed=0;
		destination_point = new Vector2(x,y);
		Vector2 difference_vector=new Vector2(destination_point);
		setRotation(difference_vector.angle());
		difference_vector.sub(start_point);
		length= difference_vector.len();
		chain_n= (int)(length/chain_interval);
		current_end_point.set(start_point);
		extend_per_time= new Vector2(difference_vector);
		extend_per_time.scl(1f/time);
	}
	public Action extendAndAttachChainAction(final BasicActor target,final float time) {
		return Actions.sequence(
			Actions.run(new Runnable() {public void run() {
				extendChain(target.getX(),target.getY()+target.getZ()+15,time);
			}}),
			Actions.delay(time),
			Actions.run(new Runnable() {public void run() {
				attachChain(target);
			}}));
			
		
	}
	public Action shrinkChainAction(final float x, final float y,final float time) {
		return 	Actions.run(new Runnable() {public void run() {shrinkChain(time);}});
		
	}
	public void shrinkChain(float time) {
		if (chain_n!=0) {
		extending=true;
		elapsed=0;
		this.time=time;
		length=start_point.dst(current_end_point);
		destination_point.set(start_point);
		extend_per_time= new Vector2(start_point);
		extend_per_time.sub(current_end_point);
		extend_per_time.scl(1f/time);
		}
	}
	public void attachChain(BasicActor to_attach) {
		extending=false;
		elapsed=0;
		this.to_attach=to_attach;
		attached=true;
	}
	public void act(float delta) {
		if (chain_n!=0) {
		if (extending) {
			float delta_time=Gdx.graphics.getDeltaTime();
			elapsed+=delta_time;
			current_end_point.add(extend_per_time.x*delta_time,extend_per_time.y*delta_time);
			current_interval.set((current_end_point.x-start_point.x)/((float)chain_n),(current_end_point.y-start_point.y)/((float)chain_n));
			if (elapsed>=time) {
				extending=false;
				elapsed=0;
			}
		}
		else if (attached) {
			current_end_point.set(to_attach.getX(),to_attach.getY()+to_attach.getZ()+15);
			current_interval.set((current_end_point.x-start_point.x)/((float)chain_n),(current_end_point.y-start_point.y)/((float)chain_n));
		}
		setRotation(current_interval.angle());
		
		}
		// current_end= start_point+extend_per_time*
		
	}
	public void draw(Batch batch, float delta) {
		
			chain_position.set(start_point);
			for (int i=0;i<chain_n-1;i++) {
				//draw at point
				batch.draw(chain_ring, chain_position.x, chain_position.y, 5, 5, 10, 10,1f,1f, getRotation());
				chain_position.add(current_interval);
				//point+=current_interval
				
			}
			batch.draw(chain_end, chain_position.x, chain_position.y, 5, 5, 10, 10,1f,1f, getRotation());
			
	
		//batch.draw(chain_ring, start_point.x, start_point.y, 5, 5, 10, 10,1f,1f, 0);
			
	}
	
	
		
}
