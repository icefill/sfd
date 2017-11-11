package com.icefill.game.actors;


import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.icefill.game.Assets;
import com.icefill.game.sprites.NonObjSprites;

public class TorchActor extends BasicActor{
	ParticleEffect pe;
	
	public TorchActor() {
		pe= new ParticleEffect();
		pe.load(Gdx.files.internal("particles/particle3.json"),Gdx.files.internal("particles/"));

	}
	public void start() {    
		pe.reset();
		pe.start();
		
	}

	public void end(){
			pe.reset();
	}
	public void draw(Batch batch, float delta){
		super.draw(batch, delta);
				batch.setBlendFunction(GL20.GL_DST_COLOR,GL20.GL_SRC_ALPHA);
				//batch.setColor(0.2f,0.2f,0.2f,1f);
				pe.draw(batch);
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	public void act(float delta){
		super.act(delta);
		pe.setPosition(getX(),getY()+getZ());
		pe.update(delta);
	}
}
