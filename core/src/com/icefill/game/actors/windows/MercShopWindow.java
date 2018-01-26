package com.icefill.game.actors.windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.dungeon.DungeonGroup.MonsterPool;

public class MercShopWindow extends BasicWindow {

	MercButton buttons[];
	public MercShopWindow(int level,MonsterPool merc_pool)
	{
		super(Assets.getSkin(),false);
		table.add("RECRUIT").colspan(5).pad(5).row();
		
		buttons= new MercButton[5];
		for (int i=0;i<3;i++)
		{
			buttons[i]=new MercButton(merc_pool.getMonster(level));
			table.add(buttons[i]).size(100,80).pad(5);
		}
		table.row();
		for (int i=0;i<3;i++)
		{
			TextButton button=new TextButton("Info", Assets.getSkin(), "default");
			table.add(button).size(100,20).pad(5);
			final ObjActor obj= buttons[i].obj;
			button.addListener(new InputListener() {
				public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
					//on_cursor=true;
				}
				public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
					//on_cursor=false;
				}
				
			    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    	obj.getSkillWindow().chooseAndAddAbilities(Global.dungeon);return true;
			    	}
			    });
		}
		table.row();
		TextButton closeButton = new TextButton("CLOSE",Assets.getSkin());
		closeButton.addListener(new HidingClickListener(this));
		table.add(closeButton).colspan(5);
	}
	
	public class HidingClickListener extends ClickListener{
		private Actor actor;
		
		public HidingClickListener(Actor actor) {
			this.actor=actor;
		}
		
		public void clicked(InputEvent event, float x, float y) {
			//hideTable();
			actor.setVisible(false);
			//Global.restoreCameraPosition();
		}
	}
	

}
