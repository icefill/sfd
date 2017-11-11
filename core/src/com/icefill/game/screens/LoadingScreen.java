package com.icefill.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.icefill.game.Assets;
import com.icefill.game.SigmaFiniteDungeon;

public class LoadingScreen extends BasicScreen
{
  public boolean animationDone = false;

  public LoadingScreen(SigmaFiniteDungeon game) { super(game); }

  public void render(float delta)
  {
    super.render(delta);
    getBatch().begin();
    Assets.getBigFont().draw(getBatch(), "Loading:" + Assets.getProgress()*100+"%", 100, 230);
    getBatch().end();
    //Gdx.app.log(SigmaFiniteDungeon.LOG, String.format("%02f", new Object[] { Float.valueOf(Assets.getProgress() * 100.0F) }));

    if ((Assets.update()) && 
      (this.animationDone)) {
      Assets.getSkin();
      this.game.setScreen(new MenuScreen(this.game));
    }
  }

  public void show()
  {
    this.animationDone = true;

    Assets.queueLoading();
  }
}