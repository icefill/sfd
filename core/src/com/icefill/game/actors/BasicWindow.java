package com.icefill.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;

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
		     }
		      
		    });
	    table.setTouchable(Touchable.enabled);
	    this.setTouchable(Touchable.enabled);
	    
	    
	    //dungeon.getUiStage().addActor(this);
	    this.add(table).fill().expand();
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
	public void addToUIStage() {
		Table table=new Table(Assets.getSkin());
		table.setFillParent(true);
		Global.getUIStage().addActor(table);
		table.add(this).fill().expand();
	}
	public void hideTable() {
		this.setVisible(false);
		
	}
		
}
