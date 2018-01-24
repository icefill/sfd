package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.icefill.game.Assets;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.actors.dungeon.DungeonGroup;


public class LevelupAbilityButton extends TextButton {
	final AbilityActor ability;
	final String to_change;
	//protected TextureRegion icon_texture;
	//protected String action_name;
	protected boolean on_cursor;
	final TextButton self;
	public AbilityActor getAbility() {
		return ability;
	}
	public LevelupAbilityButton(AbilityActor ability, final DungeonGroup dungeon, final LevelUpWindow window){
		super("", Assets.getSkin(), "default");
		self=this;
		this.ability=ability;
		this.to_change=null;
		//this.setSize(40, 40);
		//this.setBounds(0, 0, 40, 40);
		this.addListener(new InputListener() {
			public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor)
			{
				on_cursor=true;
			}
			public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor)
			{
				on_cursor=false;
			}
			
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	window.setAbilityToAdd(new ActionContainer(getAbility(),0));
		    	
				//window.getObj().action_list.add(new ActionContainer(getAbility(),0));
				return true;
		    	}
		    });

	}
	public void act(float delta){
		super.act(delta);
		//this.setBounds(getX(), getY(),40,40);
	}
	public void draw(Batch batch, float delta){// For drawing icon
		super.draw(batch, delta);
		//if(((AbilityActor)ability).required_level>obj.level) {
		//	batch.setColor(.3f,.2f,.2f,1f);
		//}
		//else {
			if (on_cursor)
				batch.setColor(0f,0f,1f,1f);
		//}
		if (ability==null)
		{
			
			Assets.getFont().draw(batch,to_change, getX(), getY()+15);
		}
		else
		{
			batch.draw(ability.getIcon(), getX(), getY(),40,40);
			Assets.getFont().draw(batch, " "+ability.required_level, getX(), getY());
			batch.setColor(1f,1f,1f,1f);
		}
		//Assets.getFont().draw(batch,ability.getActionName(), getX(), getY()+120);
	}
	
}
