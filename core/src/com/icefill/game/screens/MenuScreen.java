package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;

public class MenuScreen extends BasicScreen
  implements Constants
{
  SpriteBatch batch;
  Texture img;
  Texture img3;
  Label label;
  Image img2;
  Table table;

  public MenuScreen(final SigmaFiniteDungeon game)
  {
    super(game);

    //Label label = new Label("VER 1.09 ",Assets.getSkin());

    TextButton start_button = new TextButton("Start", Assets.getSkin(), "default");
    TextButton tutorial_button = new TextButton("HOW TO PLAY?", Assets.getSkin(), "default");
    TextButton exit_button = new TextButton("EXIT", Assets.getSkin(), "default");

    start_button.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
    	Global.initializeTooltip();
       // game.setScreen(new GameScreen(game));
        game.setScreen(new DungeonMakingScreen(game));
        
      }
    });
    tutorial_button.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        Global.initializeTooltip();
        game.setScreen(new TutorialScreen(game));

      }
    });
    exit_button.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();

      }
    });
    Gdx.input.setInputProcessor(ui_stage);
    Image image= new Image(new Texture("sprite/TITLE.png"));
    this.table = new Table(Assets.getSkin());

    this.table.setFillParent(true);
    Label label = new Label("VER 1.09", new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
    //label.setFontScale(1);
    //this.table.add("GAME OVER").center();
    this.table.add(image).center().row();
    table.add(label).bottom().right();
    this.table.row();



    Gdx.input.setInputProcessor(stage);
    table.row();
    this.table.add(start_button).size(120.0F, 30.0F).uniform().spaceBottom(10.0F).spaceTop(30f).row();
    this.table.add(tutorial_button).size(120.0F, 30.0F).uniform().spaceBottom(10.0F).spaceTop(10f).row();
    this.table.add(exit_button).size(120.0F, 30.0F).uniform().spaceBottom(10.0F).spaceTop(10f);

    this.ui_stage.addActor(this.table);
  }
  public void render(float alpha) {
     // Gdx.gl.glClearColor(1f,1f,1f,1f);
     // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      super.render(alpha);

  }
}