package com.icefill.game.actors.actionActors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.actors.CommonInventoryActor;
import com.icefill.game.actors.DungeonGroup;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.sprites.NonObjSprites;

public class SetDirectionAction extends ActionActor {
	boolean begin=false;
	SetDirectionButton dl;
	SetDirectionButton dr;
	SetDirectionButton ur;
	SetDirectionButton ul;
	int direction_to_change;
	public SetDirectionAction(){
		action_name="SetDirection";
		short_name="SetDir";
		direction_to_change=-1;
		type=1;
		ap=1;
		icon_texture = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("move"));
		dl = new SetDirectionButton(0,this);
		dr = new SetDirectionButton(1,this);
		ur = new SetDirectionButton(2,this);
		ul = new SetDirectionButton(3,this);
		
		
		
	}
	public void notifyDirection(int direction)
	{
		this.direction_to_change=direction;
	}
	public int execute(DungeonGroup room, ObjActor to_act,int level){
		
		if (Global.gfs.getSeq()==0) {
			room.enableClick(false);
			float x=room.getSelectedObj().getX();
			float y=room.getSelectedObj().getY();
			float dx= 30;
			float dy= 15;
			dl.setPosition(x-dx-20, y-dy-15);
			dl.setBound();
			
			dr.setPosition(x+dx-20, y-dy-15);
			dr.setBound();
			
			ur.setPosition(x+dx-20, y+dy-15);
			ur.setBound();
			
			ul.setPosition(x-dx-20, y+dy-15);
			ul.setBound();
			
			room.getStage().addActor(dl);
			room.getStage().addActor(dr);
			room.getStage().addActor(ur);
			room.getStage().addActor(ul);
			
			//room.getHUD().getInventory().addObjInventory(to_act.getInventory());
			//room.getHUD().getInventory().setVisible(true);
			//to_act.getInventory().setVisible(true);
		
		//room.getHUD().add(to_act.getInventory());
		//begin = true;
		}
		else {
			if (direction_to_change!=-1) {		    	
				room.getSelectedObj().setDirection(direction_to_change);
				//begin=false;
				direction_to_change=-1;
				dl.remove();
				dr.remove();
				ul.remove();
				ur.remove();
				room.enableClick(true);
				return -1;
			}
		}
		return 0;
	}
	
	public class SetDirectionButton extends BasicActor{
		float height;
		float width;
		final SetDirectionAction action;
		public SetDirectionButton(int direction,SetDirectionAction action)
		{
			//this.setDebug(true);
			sprites= Assets.non_obj_sprites_map.get("exit_marker");
			this.height= sprites.getHeight();
			this.height= sprites.getWidth();
			this.setDirection(direction);
			this.action=action;
			InputListener input_listener=
					new InputListener() {
				public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
				}
				public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
				}
				
			    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    	Global.dungeon.screen.clicked=false;
			    	
			    	((SetDirectionButton)self).action.notifyDirection(self.getDirection());
			    	return true;
			    			}
			    };
			this.addListener(input_listener);
			
		
		}
		public void setBound()
		{
			this.setBounds(getX(), getY(),40,30);
		}
		public void act(float delta){
			super.act(delta);
			
			
		}
		public void draw(Batch batch, float delta) {
		    super.draw(batch, delta);
		    ((NonObjSprites)this.sprites).drawAnimation(batch, delta, 0, getDirection(), getX()+20, getY()+15 + getZ());
		  }
		
	}
	
}
