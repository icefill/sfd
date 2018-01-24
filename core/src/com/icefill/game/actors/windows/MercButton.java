package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.ObjActor;

public class MercButton extends Button {
	ObjActor obj;
	int price;
	
	public MercButton(DungeonGroup.ObjListElt temp_obj)
	{
		super (Assets.getSkin());
		obj=new ObjActor(-1,-1,0,temp_obj.level,Assets.jobs_map.get(temp_obj.job),0);
		price=(int)(obj.job.price*(1.0f+ (obj.level-1)/5));
		//this.add(obj.getJob().job_name);
		this.sizeBy(50,70);
		this.addListener(new InputListener() {
			public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor)
			{
				//on_cursor=true;
			}
			public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor)
			{
				//on_cursor=false;
			}
			
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	if (Global.dungeon.getTeamList(0).getGold()>=price)
		    	{
		    		if (Global.getPlayerTeam().getMaxHire()>Global.getPlayerTeam().size()-1)
		    		{
		    			Global.getCurrentRoom().setObj(obj);
		    			Global.getPlayerTeam().add(obj);
		    			Global.getPlayerTeam().decreaseGold(price);
		    			//obj.team=0;
		    			obj=null;
		    		}
		    		else
		    		{
		    			Global.showBigMessage("Not enough hire point");
		    			return true;
		    		}
		    	}
		    	else
		    	{
		    		Global.showBigMessage("Not enough gold");
		    	}
				return true;
		    	}
		    });
	}
	public void draw(Batch batch,float delta)
	{
		super.draw(batch, delta);
		if (obj!=null)
		{
			obj.draw(batch, delta,this.getX()+30,this.getY()+20);
			//Assets.getFont().draw(batch,obj.job.job_name, getX(), getY()+10);
			batch.setColor(Color.YELLOW);
			Assets.getFont().draw(batch,"$"+price, getX()+35, getY()+50);
			batch.setColor(Color.WHITE);	
		}
		else
			Assets.getFont().draw(batch,"SOLD", getX()+15, getY()+30);
	}
	
}
