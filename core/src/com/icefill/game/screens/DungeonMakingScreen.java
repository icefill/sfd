package com.icefill.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.icefill.game.Assets;
import com.icefill.game.SigmaFiniteDungeon;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.dungeon.DungeonSeed;
import com.icefill.game.sprites.ObjSprites;

public class DungeonMakingScreen extends BasicScreen {
    public float elapsed_time = 0f;
    TextureAtlas atlas;
    private Animation anim;
    boolean creating_done = false;
    public GameScreen gameScreen;
    TempThread tempThread = new TempThread();
    DungeonGroup dungeon;
    float size_x = 0;
    float size_y = 0;
    float position_x = 0;
    float position_y = 0;

    public DungeonMakingScreen(SigmaFiniteDungeon game) {
        super(game);
        gameScreen = new GameScreen(game);
        size_x = Gdx.graphics.getWidth() * .7f;
        size_y = Gdx.graphics.getHeight() * .25f;
        position_x = Gdx.graphics.getWidth() * .15f;
        position_y = Gdx.graphics.getHeight() * .5f;
        //anim = ((ObjSprites)Assets.obj_sprites_map.get("spider")).animation[0][0][0];
        TextureAtlas atlas = new TextureAtlas("dungeon_loading.atlas");
        anim = new Animation(1 / 8f, atlas.findRegion("dl0000"), atlas.findRegion("dl0001"), atlas.findRegion("dl0002"), atlas.findRegion("dl0003"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        tempThread.start();

    }


    public void render(float delta) {
        elapsed_time+=Gdx.graphics.getDeltaTime();
        super.render(delta);
        getBatch().begin();
        getBatch().draw(anim.getKeyFrame(elapsed_time),position_x,position_y,size_x,size_y);
        getBatch().end();


        if (creating_done) {
            gameScreen.stage.addActor(dungeon);
            gameScreen.stage.setScrollFocus(dungeon);
            dungeon.setBounds(0F, 0F, 1280.0F, 960.0F);
            this.game.setScreen(gameScreen);
        }
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        size_x = width * .7f;
        size_y = height * .35f;
        position_x = width * .15f;
        position_y = height * .3f;
    }


    class TempThread extends Thread {
        public void run() {
            DungeonSeed dungeonSeed = new DungeonSeed("dungeon_prop.dat");
            dungeon = new DungeonGroup(dungeonSeed, gameScreen);
            creating_done = true;
        }
    }


}