package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;

public class PotraitTable extends Table {
	ObjActor obj;
	
	public PotraitTable(ObjActor obj)
	{
		super (Assets.getSkin());
		this.obj=obj;
	}
	public void draw(Batch batch,float delta)
	{
		super.draw(batch, delta);
		if (obj!=null)
		{
			obj.draw(batch, delta,this.getX()+this.getWidth()*.5f,this.getY()+20);
			Assets.getFont().draw(batch,obj.job.job_name, getX()-40f, getY()+60);
		}
	}
	
}
