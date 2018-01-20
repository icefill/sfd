package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.icefill.game.Assets;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.InventoryActor;
import com.icefill.game.sprites.NonObjSprites;
import com.icefill.game.sprites.ObjSprites;

public class DungeonMakingScreen extends BasicScreen
{
  public boolean animationDone = false;
  public float elapsed_time=0f;
  private Animation anim;
  boolean creating_done=false;
  public GameScreen gameScreen;
  TempThread tempThread= new TempThread();

  public DungeonMakingScreen(SigmaFiniteDungeon game) {
    super(game);
    anim = ((ObjSprites)Assets.obj_sprites_map.get("spider")).animation[0][0][0];
    tempThread.run();
  }



  public void render(float delta)
  {
    elapsed_time+= Gdx.graphics.getDeltaTime();
    super.render(delta);
    getBatch().begin();
    getBatch().draw(anim.getKeyFrame(elapsed_time),150,100);
    Assets.getBigFont().draw(getBatch(), "Creating Dungeon:", 150, 230);
    getBatch().end();
    if (creating_done) this.game.setScreen(gameScreen);
  }
  class TempThread extends Thread {
     public void run() {
       gameScreen= new GameScreen(game);
       creating_done=true;
     }
  }

}