package com.icefill.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.icefill.game.screens.LoadingScreen;

public class SigmaFiniteDungeon extends Game
{
  public static final String LOG = SigmaFiniteDungeon.class.getSimpleName();
  private FPSLogger fpsLogger;

  public void create()
  {
    Gdx.app.log(LOG, "Creating game");
    this.fpsLogger = new FPSLogger();
    setScreen(new LoadingScreen(this));
  }

  public void resize(int width, int height)
  {
    super.resize(width, height);

    Gdx.app.log(LOG, "width:" + width + "   height" + height);
  }

  public void render() {
    super.render();
  }

  public void setScreen(Screen screen)
  {
    super.setScreen(screen);
  }
}