package com.icefill.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.icefill.game.actors.BasicActor;

public class TooltipTable extends Table {
	Label tooltip;
	BasicActor to_tooltip;
	public TooltipTable() {
		
		super();
		
		tooltip=  new Label("Bummer", new Label.LabelStyle(Assets.getFont(), Color.BLACK) );
		TextureAtlas skinAtlas = Assets.getAsset(("ui/uiskin.atlas"),TextureAtlas.class);
		//NinePatch background=new NinePatch(Assets.getAsset(("sprite/background.png"), Texture.class), 5, 5, 4, 4);
		NinePatch background = new NinePatch(Assets.getAsset(("sprite/message_background.png"), Texture.class), 15, 8, 8, 15);
		tooltip.setFontScale(1.0f);
		tooltip.setColor(1f,1f,1f,1f);
		tooltip.setWrap(true);
		
		//tooltip_table.setPosition(this.getX()-10,this.getY()+ 30);
		//this.addActor(tooltip);
		//this.setVisible(false);
		this.setBackground(new NinePatchDrawable(background));
		//this.setPosition(400, 200);
		
	}
	public void setText(String str) {
		tooltip.remove();
		tooltip.setText(str);
		this.add(tooltip).width(70);
		this.pack();
		//this.setSize(tooltip.getWidth(), tooltip.getHeight());
		this.setVisible(true);
	}
	public void relesseText(){
		this.setVisible(false);
	}
	public void act(float delta) {
		if (this.isVisible()) {
		    this.setPosition(Global.current_screen.stageToUICoordX(Global.current_screen.getScreenX(),Global.current_screen.getScreenY()),Global.current_screen.stageToUICoordY(Global.current_screen.getScreenX(),Global.current_screen.getScreenY()));
			//this.setPosition(cGdx.input.getX(),Gdx.input.getY());
		}
	}
}
