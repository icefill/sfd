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
        implements Constants {
    public GameScreen(SigmaFiniteDungeon game) {
        super(game);
        Global.setStage(this.stage);
        Global.setUIStage(this.ui_stage);
        Global.setHUD(new HUD());

    }

    public void GameOver() {
        game.setScreen(new GameOverScreen(game));
    }

    public void WinTheGame() {
        game.setScreen(new WinScreen(game));
    }


}