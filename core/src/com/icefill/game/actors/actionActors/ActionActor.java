package com.icefill.game.actors.actionActors;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.ObjActor;


public class ActionActor extends BasicActor {

	protected TextureRegion icon_texture;
	protected TextureRegion background;
	protected String action_name;
	protected String short_name;
	protected String description;
	protected int cool_time;
	protected int type;
	protected int ap;
	protected String action_type;
	protected TextButton button;
	protected boolean on_cursor;
	protected boolean subaction_running=false;
	private int actor_count=0;
	//private Image img;
	IconActor icon_actor;
	public boolean selected=false;
	protected Label tooltip;
	//LinkedList<BasicActor> actor_list;
	public ActionActor(){
		type=0;
		button= new TextButton("",Assets.getSkin());
		tooltip=  new Label("", new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
		//tooltip.setFontScale(0.5f);
		this.addActor(tooltip);
		tooltip.setPosition(0,170);
		tooltip.setVisible(false);
		//actor_list= new LinkedList<BasicActor>();
		background=  new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("scroll"));
	}
	public void initialize()
	{
		resetActorCount();
		subaction_running=false;



	}
	public void setButtonText(){
		if (short_name!=null) {
			button.setText(" "+short_name+" ");
		}
		else {
			button.setText(" "+short_name+" ");
		}
	}
	public int getAP()
	{
		return ap;
	}
	public boolean isSelected() {return selected;}

	public TextureRegion getIcon() {
		return icon_texture;
	}
	public TextButton getButton() {
		return button;
	}
	public String getDescription() {
		return description;
	}
	public void act(float delta){
		super.act(delta);
		this.setBounds(getX(), getY(),50,45);
	}
	
	public void draw(Batch batch, float delta){// For drawing icon
		batch.setColor(this.getColor());
		batch.draw(background, getX(), getY(),40,40);
		batch.draw(icon_texture, getX(), getY(),40,40);
		batch.setColor(1f,1f,1f,1f);
		if (short_name!=null)
		{
			Assets.getFont().setColor(Color.WHITE);
			Assets.getFont().draw(batch,short_name, getX(), getY()+48);
			//Assets.getFont().setColor(Color.WHITE);
		}
		//Assets.getFont().draw(batch, this.action_name, getX(), getY()+40);
		super.draw(batch, delta);
}
	
	public int execute(DungeonGroup room, ObjActor to_act,int level){
		return -1;
	}
	public int getType() {return type;}
	public String getActionType() {return action_type;}
	//public void addActingActor(BasicActor actor){actor_list.add(actor);}
	//public void releaseActingActor(BasicActor actor){actor_list.remove(actor);}
	public void addActorCount(){actor_count++; }
	public void subtractActorCount() {actor_count--; if (actor_count<0) actor_count=0;}
	public void resetActorCount(){actor_count=0;}
	public int getActorCount(){return actor_count;}
	public String getActionName() {return action_name;}
	
	/*
	public Image getIconImage()
	{
		if (img==null && icon_texture!=null)
		{
			img= new Image(icon_texture);
		}
		return img;
	}*/
	public Actor getIconActor()
	{
		if (icon_actor==null)
		{
			icon_actor = new IconActor(this);
		}
		return icon_actor;
		
	}
	public static class IconActor extends Actor {
		ActionActor action;
		
		public IconActor(ActionActor action)
		{
			this.action=action;
		}
		public void act(float delta)
		{
			super.act(delta);
			this.setBounds(getX(), getY(), 50, 50);
		}
		public void draw(Batch batch, float delta)
		{
			super.draw(batch,delta);
			batch.setColor(getColor());
			batch.draw(action.icon_texture,getX(),getY());
			if (action.short_name!=null)
			{
				Assets.getFont().draw(batch,"NEW Ability\n"+action.short_name, getX(), getY()+48);
				//Assets.getFont().draw(batch,action.short_name, getX(), getY()+40);
			}
			
			batch.setColor(Color.WHITE);
		}
	}
	public static class ActionContainer {
		public ActionActor action;
		public ActionContainer self;
		private InputListener listener;
		public int level;
		public int current_cool_time;
		public boolean is_forbidden;
		private boolean mana_free;
		public boolean isForbidden() {
			if (is_forbidden) return true;
			else if (current_cool_time>0) return true;
			else return false;
		}
		public void setManaFree()
		{
			mana_free=true;
		}
		public boolean isManaFree()
		{
			return mana_free;
		}
		public void setForbidden() {
		}
		public ActionContainer(){
		}
		public ActionContainer(final ActionActor action,int level) {
			this.action=action;
			self=this;
			this.level=level;
			this.listener=new InputListener() {
				public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor) {
					action.on_cursor=true;
				}
				public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor) {
					//tooltip.setVisible(false);
					action.on_cursor=false;
					if (!self.is_forbidden && current_cool_time<=0)
						action.setColor(1f,1f,1f,1f);
				}
				
			    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    	if (!self.is_forbidden && current_cool_time<=0) {
			    		action.selected=true;
						action.setColor(1f,1f,1f,1f);
			    	}
			    	Global.current_screen.clicked=true;
			    	return false;
			    			}
			    };
	
		}
		public int execute(DungeonGroup room, ObjActor to_act) {
			return action.execute(room, to_act, level);
		}
		public void addListener() {
			action.addListener(listener);
			action.button.addListener(listener);

		}
		public void addCoolTime() {
			this.current_cool_time=action.cool_time;
			if (this.current_cool_time>0) {
				action.setColor(.3f,.3f,.3f,1f);
			}
		}
		public void subCoolTime() {
			if (this.current_cool_time>0)
				this.current_cool_time--;
			if (this.current_cool_time==0) {
				if (action!=null)
				action.setColor(1f,1f,1f,1f);
			}
		}
		public void resetCoolTime() {
			this.current_cool_time=0;
			action.setColor(1f,1f,1f,1f);
		}
		
		public void removeListener() {

			action.removeListener(listener);
			action.button.removeListener(listener);
		}
	}

}
