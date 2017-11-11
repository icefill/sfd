package com.icefill.game.actors;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;



import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.sprites.BasicSprites;
import com.icefill.game.sprites.NonObjSprites;

public class EffectActor extends BasicActor implements Constants {
	protected NonObjSprites glow;
	protected boolean continuous=false;
	protected boolean continuously_show=false;
	
	float after_delay=0;
	//protected PointLight sprite_light;
	protected ParticleEffect pe;
	boolean rotating;
	float end_time=0f;
	boolean end_flag=false;
	protected boolean glowing=false;
	protected Color glow_color;
	protected float glow_size=2f;
	protected float glowing_distance=200;
	public EffectActor(String particle_file_name,String sprite_name,float after_delay,float glowing_distance,Color glow_color) {
			curr_dir=DL;
			this.glowing_distance=glowing_distance;
			after_delay=this.after_delay;
			if (sprite_name!=null)
				sprites= Assets.non_obj_sprites_map.get(sprite_name);
			if (particle_file_name!=null) {
				pe=new ParticleEffect();
				pe.load(Gdx.files.internal(particle_file_name),Gdx.files.internal("particles/"));
			}
			if (glowing_distance>0) {
				glowing =true;
				if (glowing_distance>=100)
					glowing_distance/=400;
			}
			if (glowing) {
				this.glowing_distance=glowing_distance;
				this.glow_color=glow_color;
				glow = (NonObjSprites)Assets.non_obj_sprites_map.get("glow");
				
				//glow_size=glowing_distance/300f;
			    //this.sprite_light.setColor(.4F, 0.4F, 0.4F, 1.0F);
				}
	}
	
	public EffectActor(BasicSprites sprites) {
		curr_dir=DL;
		//after_delay=this.after_delay;
		this.sprites= sprites;
		
	}
	public EffectActor(String particle_file_name,String sprite_name,float after_delay,boolean GLOW_FLAG) {
		curr_dir=DL;
		after_delay=this.after_delay;
		if (sprite_name!=null)
			sprites= Assets.non_obj_sprites_map.get(sprite_name);
		if (particle_file_name!=null) {
			pe=new ParticleEffect();
			pe.load(Gdx.files.internal(particle_file_name),Gdx.files.internal("particles/"));
		}
		this.glowing=GLOW_FLAG;
		if (glowing) {
			glow = (NonObjSprites)Assets.non_obj_sprites_map.get("glow");
			
			}
	}	
	
	public void start() {
		super.start();
		if (pe!=null)
		pe.start();
	}
	public void end(){
		super.end();
		elapsed_time=0;
		this.remove();
		if (pe!=null) {
			pe.dispose();
			pe.reset();
		}
		//if (sprite_light!=null) {
		//	sprite_light.remove();
		//}
		//this.remove();
	}
	public void setSpritesRotation(float degree) {
		setRotation(degree);
	}
	public void draw(Batch batch, float delta){
		super.draw(batch, delta);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
		
			if (pe!= null) {
				pe.draw(batch);
			}
			if (glowing) {
				batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
				
				batch.setColor(glow_color);
				glow.drawAnimation(batch, elapsed_time, 0,0,getX(), getY()+getZ(), 0, glowing_distance,glowing_distance);
				batch.setColor(1f,1f,1f,1f);
			}
		
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			if (sprites!=null) {
				if (attached) {
					this.setPosition(to_attach.getX()+attach_anchor_x,to_attach.getY()+attach_anchor_y,to_attach.getZ()+attach_anchor_z);
					this.setRotation(to_attach.getRotation()+this.attach_anchor_rot);
						//current_interval.set((current_end_point.x-start_point.x)/((float)chain_n),(current_end_point.y-start_point.y)/((float)chain_n));
				}
				if (continuous) {
					((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0,curr_dir, getX(), getY()+getZ(),getRotation(),getScaleX(),getScaleY());
				}
				else if ( !((NonObjSprites)sprites).isSpritesEnd(elapsed_time))
				{
					((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0,curr_dir, getX(), getY()+getZ(),getRotation(),getScaleX(),getScaleY());
					end_time=elapsed_time;
				}
				else if (continuously_show)
				{
					
					((NonObjSprites)sprites).drawAnimation(batch, end_time, 0,curr_dir, getX(), getY()+getZ(),getRotation(),getScaleX(),getScaleY());
				}
				
				
			}
	}
	public void act(float delta){
		super.act(delta);
		//if (sprite_light!=null)
		//	sprite_light.setPosition(getX(), getY());
		if (pe!=null) {
			pe.setPosition(getX(), getY()+getZ());
			pe.update(delta);
		}
		
		
			if (!continuous
				&& (pe ==null || pe.isComplete())
				&& (sprites ==null || ((NonObjSprites)sprites).isSpritesEnd(elapsed_time)&& !continuously_show)
					){
				this.addAction(Actions.sequence(
						Actions.delay(after_delay),
						this.deActivateAndEndAction()
						//this.endActionSubAction()
					));

			}
			
			
			
	}
	public static class Factory{
		public String sprites_name;
		public String particle_effect_name;
	}

}
