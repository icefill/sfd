package com.icefill.game.screens;

//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Output;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.DungeonSeed;
import com.icefill.game.actors.windows.HUD;


public class GameScreen extends BasicScreen
  implements Constants
{
  public GameScreen(SigmaFiniteDungeon game)
  {
    super(game);
    Global.setStage(this.stage);
    Global.setUIStage(this.ui_stage);
    Global.setHUD(new HUD());

    DungeonSeed dungeonSeed = new DungeonSeed("dungeon_prop.dat");
    DungeonGroup dungeon = new DungeonGroup(dungeonSeed, this, this.ui_stage);
    this.stage.addActor(dungeon);
    stage.setScrollFocus(dungeon);
    dungeon.setBounds(0F,0F, 1280.0F, 960.0F);
  }

  public void GameOver() {
	  game.setScreen(new GameOverScreen(game));
  }
  public void WinTheGame() {
	  game.setScreen(new WinScreen(game));
  }


}