package com.icefill.game.actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.Constants;

public class Wall extends BasicActor
  implements Constants
{
  public String name;
  public String description;
  public int n_of_tiles;
  public ArrayList<Animation[]> animation;
  
  public Wall()
  {
	  
  }

  public Wall(Factory factory)
  {
    this.name = factory.name;
    this.n_of_tiles=factory.n_of_tiles;
    TextureAtlas atlas = new TextureAtlas(factory.atlas);
    		//(TextureAtlas)Assets.getAsset(factory.atlas, TextureAtlas.class);
    this.description = factory.description;

    animationFromTextureAtlas(atlas);
    atlas.dispose();
  }
  
  private void animationFromTextureAtlas(TextureAtlas atlas) {
		int index=0;
		animation= new ArrayList<Animation[]>();
		
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
				temp_animation[UL]=temp_animation[UR];
			animation.add(temp_animation);
		}
  }

  public static class Factory
  {
    public String name;
    public String atlas;
    public int n_of_tiles;
    public String description;
  }
}