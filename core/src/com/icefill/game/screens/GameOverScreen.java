package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.SigmaFiniteDungeon;

public class GameOverScreen extends BasicScreen
  implements Constants
{
  Table table;

  public GameOverScreen(final SigmaFiniteDungeon game)
  {
    super(game);
    TextButton button = new TextButton("Accept", Assets.getSkin(), "default");
    button.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new MenuScreen(game));
      }
    });
    Gdx.input.setInputProcessor(this.ui_stage);
    this.table = new Table(Assets.getSkin());
    this.table.setFillParent(true);
    Label label = new Label("GAME OVER", new Label.LabelStyle(Assets.getBigFont(), Color.WHITE) );
    label.setFontScale(1);
    table.add(label).center();
    this.table.row();

    Gdx.input.setInputProcessor(this.stage);

    this.table.add(button).size(120.0F, 50.0F).uniform().spaceBottom(10.0F);

    this.ui_stage.addActor(this.table);
  }
}