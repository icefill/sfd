package com.icefill.game.sprites;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.Assets;

public class NonObjSprites extends BasicSprites {
	
	Animation animation[];
	float rotation[];
	float rotation_anchor=0;
	boolean rotatable=false;

	
	public NonObjSprites(Factory factory,String atlas_path){
		this.anchor_x=factory.anchor_x;
		this.anchor_y=factory.anchor_y;
		TextureAtlas atlas=(TextureAtlas)Assets.getAsset(atlas_path, TextureAtlas.class);
		this.has_direction=factory.has_direction;
		readAnimationFromAtlas(atlas);
		height=animation[0].getKeyFrame(0).getRegionHeight();
		width=animation[0].getKeyFrame(0).getRegionHeight();
		this.rotation_anchor=factory.rotation_anchor;
		this.rotatable=factory.rotatable;
		if (factory.rotation != null) {
			rotation= factory.rotation;
		}
 	}
	public NonObjSprites(TextureRegion texture_region){
		this.has_direction=false;
		animation = new Animation[4];
		animation[0]=new Animation(1f,texture_region);
		animation[1]=animation[0];
		animation[2]=animation[0];
		animation[3]=animation[0];
		height=animation[0].getKeyFrame(0).getRegionHeight();
		width=animation[0].getKeyFrame(0).getRegionWidth();
		this.anchor_x=animation[0].getKeyFrame(0).getRegionWidth()*0.5f;
		this.anchor_y=animation[0].getKeyFrame(0).getRegionHeight()*0.5f;
		
		this.rotation_anchor=0;
	}
	public void setrotationAnchor(float degree) {
		this.rotation_anchor=degree;
	}
	public void drawAnimation(Batch batch,float elapsed_time,int anim,int direction,float x,float y) {
		if (!has_direction) direction=0;
		batch.draw(animation[direction].getKeyFrame(elapsed_time,true), x-anchor_x, y-anchor_y);
	}
	public void drawAnimation(Batch batch,float elapsed_time,int anim,int direction,float x,float y,boolean loop) {
		if (!has_direction) direction=0;
		batch.draw(animation[direction].getKeyFrame(elapsed_time,loop), x-anchor_x, y-anchor_y);
	}
	public void drawAnimation(Batch batch,float elapsed_time,int anim,int direction,float x,float y,float rotation,float scale_x,float scale_y) {
		if (!has_direction) direction=0;
		if (!rotatable || direction== DL|| direction==UL)
			batch.draw(animation[direction].getKeyFrame(elapsed_time,true), x-anchor_x, y-anchor_y,anchor_x,anchor_y,getWidth(),getHeight(),scale_x,scale_y,rotation-rotation_anchor);
		else
			batch.draw(animation[direction].getKeyFrame(elapsed_time,true), x-(width-anchor_x), y-(anchor_y),width-anchor_x,anchor_y,getWidth(),getHeight(),scale_x,scale_y,rotation-(180-rotation_anchor));
	}
	public void drawAnimationMiddleRotation(Batch batch,float elapsed_time,int anim,int direction,float x,float y,float rotation,float scale_x,float scale_y) {
		if (!has_direction) direction=0;
		batch.draw(animation[direction].getKeyFrame(elapsed_time,true), x-anchor_x, y-anchor_y,getWidth()/2,getHeight()/2,getWidth(),getHeight(),scale_x,scale_y,rotation-rotation_anchor);
	}

	public float getSpritesDuration(int animation,int direction) {
		if (!hasDirection()) direction=0;
		return this.animation[direction].getAnimationDuration()-this.animation[direction].getFrameDuration()+0.000001f;
	}
	public Animation getRepresentativeAnimations() {
		return animation[0];
	}
	private void readAnimationFromAtlas(TextureAtlas atlas) {
		int index=0;
		TextureRegion temp_region;
		Queue<TextureRegion> temp_frames = new LinkedList<TextureRegion>();
		if (has_direction) {
			animation= new Animation[4];
			if (is_symmetric) {
				for (int dir=0;dir<4;dir+=2){
					index=0;
					while(true) {
						String region_name=direction_name[dir]
								+String.format("%04d", index);
						temp_region=atlas.findRegion(region_name);
						if (temp_region == null)
							break;
						else {	
							temp_frames.add(temp_region);
							index++;
						}
					}//while
					if (index!=0)
					animation[dir]=new Animation(0.07f,temp_frames.toArray(new TextureRegion[index]));
					if (dir==UR && animation[dir]==null)
						animation[dir]=createFlippedAnimation(animation[DL]);
					temp_frames.clear();
					animation[dir+1]=createFlippedAnimation(animation[dir]);
				}
			} //is symmetric
			else {// is not symmetric
				for (int dir=0;dir<4;dir+=2){
					index=0;
					while(true) {
						String region_name=direction_name[dir]
								+String.format("%04d", index);
						temp_region=atlas.findRegion(region_name);
						if (temp_region == null)
							break;
						else {	
							temp_frames.add(temp_region);
							index++;
						}
					}//while
					animation[dir]=new Animation(0.07f,temp_frames.toArray(new TextureRegion[index]));
					temp_frames.clear();
					animation[dir+1]=createFlippedAnimation(animation[dir]);
				}
			} // is not symmetric
		} // has direction
		else { // has no direction
			animation = new Animation[1];
			index=0;
			while(true) {
				String region_name=direction_name[0]
						+String.format("%04d", index);
				temp_region=atlas.findRegion(region_name);
				if (temp_region == null)
					break;
				else {	
					temp_frames.add(temp_region);
					index++;
				}
			}//while
			animation[0]=new Animation(0.07f,temp_frames.toArray(new TextureRegion[index]));
		} // has no direction
	
	}
	  public float getDirectionRotation (int dir) {
		  return rotation[dir];
	  }
	  public boolean isSpritesEnd(float elapsed_time) {
		    return this.animation[0].isAnimationFinished(elapsed_time);
		  }

	public static class Factory {
    	public float anchor_x;
    	public float anchor_y;
    	public boolean has_direction;
    	public boolean is_symmetric;
    	public float rotation_anchor;
    	public float[] rotation;
    	public boolean rotatable;
    }
}
