package com.icefill.game.actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Constants;
import com.icefill.game.utils.Randomizer;

public class Floor extends BasicActor
  implements Constants
{
		public String name;
	  public String description;
	  public int n_of_tiles;
	  public int n_of_wall_tiles;
	  public int light_radius;
	  public String particle_name;
	  public int particle_offset_x;
	  public int particle_offset_y;
	  
	  public ArrayList<Animation[]> animation;
	  public ArrayList<Animation[]> wall_animation;
	  
	  public boolean is_wall;

  public Floor()
  {
  }

  public Floor(Factory factory)
  {
    this.name = factory.name;
    this.n_of_tiles=factory.n_of_tiles;
    this.n_of_wall_tiles=factory.n_of_wall_tiles;
    TextureAtlas atlas = new TextureAtlas(factory.atlas);
    light_radius=factory.light_radius;
    		//(TextureAtlas)Assets.getAsset(factory.atlas, TextureAtlas.class);
    this.description = factory.description;
    animation = new ArrayList<Animation[]>();
    animationFromTextureAtlas(atlas,animation,n_of_tiles);
    
    is_wall=factory.is_wall;
    if (factory.wall_atlas!= null) {
    	wall_animation=new ArrayList<Animation[]>();
    	TextureAtlas wall_atlas= new TextureAtlas(factory.wall_atlas);
    	animationFromTextureAtlas(wall_atlas,wall_animation,n_of_wall_tiles);
    }
    particle_name=factory.particle_name;
    particle_offset_x=factory.particle_offset_x;
    particle_offset_y=factory.particle_offset_y;
    //atlas.dispose();
  }
  private void animationFromTextureAtlas(TextureAtlas atlas,ArrayList<Animation[]> anim,int n_of_tiles) {
	  int index=0;
		
		TextureRegion temp_region;
		Queue<TextureRegion> temp_frames = new LinkedList<TextureRegion>();
		
		for (int i=0;i<n_of_tiles;i++) {
			Animation[] temp_animation= new Animation[4];
			for (int dir=0;dir<4;dir++){
				index=0;
				while(true) {
					String region_name=i+direction_name[dir]
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
					temp_animation[dir]=new Animation(0.07f,temp_frames.toArray(new TextureRegion[index]));
				temp_frames.clear();
			}
			if (temp_animation[DR]==null)
				temp_animation[DR]=temp_animation[DL];
			if (temp_animation[UR]==null)
				temp_animation[UR]=temp_animation[DL];
			if (temp_animation[UL]==null)
				temp_animation[UL]=temp_animation[UR];
			anim.add(temp_animation);
		}
  }
   public boolean isWall() {
	  return is_wall;
  }
  public int getRndWallIndex()
  {
	  int rn=Randomizer.nextInt(n_of_wall_tiles+2);
	  if (rn<3)
		  return 0;
	  else return rn-2;
  }
  public int getRndFloorIndex()
  {
	  int rn=Randomizer.nextInt(n_of_tiles+13);
	  if (rn<14)
		  return 0;
	  else return rn-13;
  }
  public static class Factory
  {
	 
    public String name;
    public String atlas;
    public String wall_atlas;
    public int n_of_tiles;
    public int n_of_wall_tiles;
    //public String atlas_index;
    public String description;
    public String particle_name;
    public int light_radius;
    public int particle_offset_x;
    public int particle_offset_y;
    public boolean is_wall;
  }
}