package com.icefill.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.icefill.game.Assets;
import com.icefill.game.Constants;

public class CharUI extends Actor implements Constants
{
  private TextureRegion team_indicator;
  private TextureRegion leader_team_indicator;
  private TextureRegion ability_count;
  //private TextureRegion ability_count_de;
  
  //private TextureRegion selected_marker;
  //private TextureRegion team_indicator_deselect;
  private Animation selected_marker;
  private TextureRegion hp_background;
  private TextureRegion hp;
  private Color team_color;
  private ObjActor obj;

  public CharUI(int team,ObjActor obj)
  {
	  this.obj=obj;
    TextureAtlas char_ui_atlas = (TextureAtlas)Assets.getAsset("sprite/char_ui.atlas", TextureAtlas.class);
    this.team_indicator = new TextureRegion(char_ui_atlas.findRegion("team_indicator"));
    this.leader_team_indicator = new TextureRegion(char_ui_atlas.findRegion("leader_team_indicator"));
    //this.team_indicator_deselect=new TextureRegion(char_ui_atlas.findRegion("team_indicator_deselect"));
    this.ability_count = new TextureRegion(char_ui_atlas.findRegion("ac2"));
    //this.ability_count_de = new TextureRegion(char_ui_atlas.findRegion("ac2_de"));
    
    this.hp = new TextureRegion(char_ui_atlas.findRegion("hp"));
    this.hp_background = new TextureRegion(char_ui_atlas.findRegion("hp_back"));
    this.selected_marker= new Animation(.1f,char_ui_atlas.findRegion("selector0000")
    		,char_ui_atlas.findRegion("selector0001")
    		,char_ui_atlas.findRegion("selector0002")
    		,char_ui_atlas.findRegion("selector0003")
    		,char_ui_atlas.findRegion("selector0004")
    		,char_ui_atlas.findRegion("selector0005")
    		);

    switch (team) { case 0:
      this.team_color = Color.RED; break;
    case 1:
      this.team_color = Color.BLUE; break;
    default:
      this.team_color = Color.WHITE; }
      
  }

  public void draw_health(float elapsed_time,Batch batch, int current_hp, int hp_max, int current_ability_count, boolean selected)
  {
    if (selected)
    	batch.draw(selected_marker.getKeyFrame(elapsed_time,true), -18, 40+obj.getZ());
    	/*
      batch.draw(this.team_indicator, -7.0F, 42.0F, 0.0F, 0.0F, 
        8.0F, 
        8.0F, 1.5F, 1.5F, 0.0F);
        */
    //else
    batch.setColor(this.team_color);
    
    if (!obj.getType().equals("obstacle")&& !obj.getType().equals("wall")){
        
    if (obj.isLeader())
    	batch.draw(this.leader_team_indicator, -9.0F, 30.0F+obj.getZ());
    else
    	batch.draw(this.team_indicator, -9.0F, 30.0F+obj.getZ());
    
    for (int i = 0; i < current_ability_count; i++) {
      batch.draw(this.ability_count, i * 8, 34.0F+obj.getZ());
    }
    batch.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    /*
    if (current_ability_count>=0)
    for (int i = current_ability_count; i < 2; i++) {
        batch.draw(this.ability_count_de, i * 8+8, 32.0F);
      }
      */
    
    //batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    batch.draw(this.hp_background, 0.0F, 32.0F+obj.getZ(), 0.0F, 0.0F, 20.0F, 4.0F, 1.0F, 1.0F, 0.0F);
    Assets.getFont().draw(batch, Integer.toString(obj.status.getCurrentHP()), 20, 32+obj.getZ());
	 
    if (hp_max != 0  )
      batch.draw(this.hp, 1.0F, 33.0F+obj.getZ(), 0.0F, 0.0F, 18.0F, 2.0F, (float)(current_hp) / hp_max, 1.0F, 0.0F);
    }
  }
}