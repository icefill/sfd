package com.icefill.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.sprites.BasicSprites;
import com.icefill.game.sprites.NonObjSprites;

import java.util.HashMap;

public class CursorActor extends BasicActor
{
  private int xx;
  private int yy;
  private boolean map_position_changed;
  private boolean need_set_target;
  Action select_action;
  Label tooltip;
  Table tooltip_table;

  public CursorActor()
  {
    this.xx = 0; this.yy = 0;
    this.sprites = ((BasicSprites)Assets.non_obj_sprites_map.get("cursor"));
  }

  public void act(float delta) {
    super.act(delta);
    //tooltip_table.setPosition(this.getX(), this.getY());
  }

  public void doSelectAction() {
    addAction(Actions.sequence(Actions.moveBy(0.0F, -5.0F, 0.1F), Actions.moveBy(0.0F, 5.0F, 0.1F)));
  }
  public void draw(Batch batch, float delta) {
    super.draw(batch, delta);
    
    ((NonObjSprites)this.sprites).drawAnimation(batch, delta, 0, DIR.DL, getX(), getY() + getZ());
  }

  public boolean isMapPositionChanged() {
    return this.map_position_changed;
  }
  public boolean needSetTarget() {
	  if (need_set_target == true) {
		  need_set_target=false;
		  return true;
	  }
	  return false;
  }
  public void setMapPosition(int xx, int yy) {
    if ((this.xx == xx) && (this.yy == yy)) {
      this.map_position_changed = false;
    }
    else {
      this.xx = xx; this.yy = yy;
      this.map_position_changed = true;
      this.need_set_target=true;
    }
  }

  public int getXX() { return this.xx; }

  public int getYY() {
    return this.yy;
  }

  public void moveTo(float x, float y) {
    addAction(Actions.moveTo(x, y, 0.1F));
  }

}