package com.icefill.game.actors.effects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.icefill.game.actors.BasicActor;

public class ProjectileActor extends EffectActor {
	EffectActor hit_effect;
	BasicActor to_attach;
	private float speed=7f;
	public ProjectileActor(String particle_file_name,String sprite_name,float glowing_distance,Color glow_color) {
		super(particle_file_name,sprite_name,0f,glowing_distance,glow_color);
		continuous=true;
		attached=false;
	}	
	public ProjectileActor (String particle_file_name,String sprite_name,float after_delay) {
		super(particle_file_name,sprite_name,after_delay,false);
		continuous=true;
		attached=false;
	}
	public ProjectileActor (String particle_file_name,String sprite_name,float after_delay,boolean continuous) {
		super(particle_file_name,sprite_name,after_delay,false);
		this.continuous=continuous;
		attached=false;
		continuously_show=true;
	}
	
	public ProjectileActor (BasicActor obj){
		super(obj.sprites);
		this.after_delay=0.5f;
		continuous=true;
	}
	
	public void start() {
		super.start();
		if (pe!=null) {
			pe.reset();
			pe.start();
		}
		
	}
	/*
	public void deActivate(){
		super.deActivate();
		this.addAction(endAction());
	}
*/
	/*
	public void deActivateAndEnd() {
		super.deActivate();
		this.addAction(endAction());
	}
	*/
	public float getSpeed(){ return speed;}
	
	public SequenceAction endActionSubAction() {
		SequenceAction seq= new SequenceAction();
		if (glowing) {
		 
		}
		else {
			//this.setVisible(false);
			seq.addAction(Actions.delay(.3f));
			seq.addAction(Actions.run(new Runnable() {public void run() {self.setVisible(false);}}));
			//seq.addAction(Actions.delay(.2f));
		}
		seq.addAction(Actions.run(new Runnable() {public void run() {end();}}));
		return seq;
	}
	
}
