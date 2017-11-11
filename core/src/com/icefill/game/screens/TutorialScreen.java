package com.icefill.game.screens;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.DungeonGroup.DungeonSeed;
import com.icefill.game.actors.DungeonGroup.ObjListElt;
import com.icefill.game.actors.HUDActor;

import java.util.ArrayList;

public class TutorialScreen extends BasicScreen
  implements Constants
{
//	Kryo kryo= new Kryo();
//	Output output;
Table img_table;
    Image images[];
    int index=0;
  public TutorialScreen(final SigmaFiniteDungeon game)
  {
    super(game);
    Table parent_table= new Table(Assets.getSkin());
    //parent_table.setDebug(true,true);
     img_table= new Table(Assets.getSkin());
      this.ui_stage.addActor(parent_table);
      parent_table.setFillParent(true);
      parent_table.add(img_table).size(UI_SCREEN_WIDTH*.95f,UI_SCREEN_HEIGHT*.95f).colspan(3).row();
      img_table.setFillParent(true);
      images = new Image[8];
      for (int i=0;i<8;i++) {
          images[i]=new Image(new Texture("tutorial/tuto_"+(i+1)+".png"));
          //image[i].setFillParent(true);
      }
      img_table.add(images[0]).size(UI_SCREEN_WIDTH*.9f,UI_SCREEN_HEIGHT*.9f);
      TextButton prev_button = new TextButton("<-", Assets.getSkin(), "default");
      TextButton next_button = new TextButton("->", Assets.getSkin(), "default");
      TextButton exit_button = new TextButton("BACK TO MENU", Assets.getSkin(), "default");
      parent_table.add(prev_button).expand().fill();
      parent_table.add(exit_button).expand().fill();
      parent_table.add(next_button).expand().fill();
      prev_button.addListener(new ClickListener() {
          public void clicked(InputEvent event, float x, float y) {
              index=(index+7) % 8;
              setImage(index);
          }
      });
      next_button.addListener(new ClickListener() {
          public void clicked(InputEvent event, float x, float y) {
              index=(index+1) % 8;
              setImage(index);
          }
      });

      exit_button.addListener(new ClickListener() {
          public void clicked(InputEvent event, float x, float y) {
              game.setScreen(new MenuScreen(game));

          }
      });
      this.setImage(index);

  }
  public void setImage(int i) {
      img_table.clearChildren();
      img_table.add(images[i]).size(UI_SCREEN_WIDTH*.9f,UI_SCREEN_HEIGHT*.9f);

  }


}