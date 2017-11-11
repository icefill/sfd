package com.icefill.game.actors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.Job;
import com.icefill.game.Randomizer;
import com.icefill.game.actors.ObjActor.Status;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.screens.GameScreen;

public class BasicWindow extends Table {
	BasicWindow window;
	//DungeonGroup dungeon;
	//Label item_name;
	Table table;
	private Boolean close;
	
	public BasicWindow() {
		close=false;
	}
	public BasicWindow(Skin skin,boolean auto_close){
		table= new Table(Assets.getSkin());
		table.center();
		this.close=auto_close;
		//NinePatch background = new NinePatch(Assets.getAsset(("sprite/background.png"),Texture.class), 5, 5, 4, 4);
	    table.setBackground(Assets.getBackground());
		
		TextureAtlas skinAtlas = Assets.getAsset(("ui/uiskin.atlas"),TextureAtlas.class);
		window=this;
	    this.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		    	 // window.hideTable();
		  		 if (table.getX()<x && x<table.getX()+table.getWidth() &&
		  			table.getY()<y && y<table.getY()+table.getHeight() 	 )
		  		 {}
		  		 else {
		  			 if (close)
		  			 window.hideTable();
		  		 }
		  		 
		      }
		      
		    });
	    table.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		  		 //System.out.println("Table clicked");
		      }
		      
		    });
	    table.setTouchable(Touchable.enabled);
	    this.setTouchable(Touchable.enabled);
	    
	    
	    //dungeon.getUiStage().addActor(this);
	    this.add(table);
	    this.setFillParent(true);
	    this.center();
	    //table.pack();
		  
		//this.setVisible(false);
		
	}

	//public DungeonGroup getDungeon() {
	//	return dungeon;
	//}
		public void showTable() {
		//this.mp_button.setText("MANA:"+getDungeon().getDeadEnemyList().size());
		this.setVisible(true);
	}
	public void hideTable() {
		this.setVisible(false);
		
	}
		
}
