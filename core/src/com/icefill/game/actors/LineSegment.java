package com.icefill.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.icefill.game.Assets;

public class LineSegment extends Actor{
	Vector2 start_point;
	Vector2 end_point;
	Vector2 vec;
	
	float length;
	float thickness;
	float pixel_thickness;
	
	TextureRegion middle;
	TextureRegion left;
	TextureRegion right;
	public LineSegment(Vector2 start_point,Vector2 end_point, float thickness) {
		this.start_point=start_point;
		this.end_point=end_point;
		this.thickness=thickness;
		this.vec= new Vector2(end_point);
		vec.sub(start_point);
		length= vec.len();
		setRotation(vec.angle());
		TextureAtlas atlas= Assets.getAsset("sprite/lightning.atlas",TextureAtlas.class);
		middle= atlas.findRegion("lightning_m");
		left= atlas.findRegion("lightning_l");
		right= atlas.findRegion("lightning_r");
		setX((start_point.x+end_point.x)*0.5f);
		setY((start_point.y+end_point.y)*0.5f);
		
	}
	public void draw(Batch batch, float delta) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_SRC_ALPHA);
		batch.setColor(.5f,.5f,1f,1f);
		
		batch.draw(middle, getX()-thickness/20f-15, getY()-thickness/10f-15, 15, 15, 30, 30, length/30f, thickness/10f, getRotation());
		batch.draw(left, start_point.x-thickness/20f-15, start_point.y-thickness/10f-15, 15, 15, 15, 30, thickness/10f, thickness/10f, getRotation());
		batch.draw(right, end_point.x-thickness/20f, end_point.y-thickness/10-15f, 0, 15, 15, 30, thickness/10f, thickness/10f, getRotation());
	
		batch.setColor(1f,1f,1f,1f);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	public void draw(Batch batch, float delta,float r,float g,float b,float a) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
		batch.setColor(r,g,b,a);
		
		batch.draw(middle, getX()-thickness/20f-15, getY()-thickness/10f-15, 15, 15, 30, 30, length/30f, thickness/10f, getRotation());
		batch.draw(left, start_point.x-thickness/20f-15, start_point.y-thickness/10f-15, 15, 15, 15, 30, thickness/10f, thickness/10f, getRotation());
		batch.draw(right, end_point.x-thickness/20f, end_point.y-thickness/10-15f, 0, 15, 15, 30, thickness/10f, thickness/10f, getRotation());
	
		batch.setColor(1f,1f,1f,1f);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	public void changeLineSizeAction(float size,float time) {
	}
	// start point end point, thickness
	// draw
}
