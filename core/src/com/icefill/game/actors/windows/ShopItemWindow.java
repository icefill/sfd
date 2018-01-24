package com.icefill.game.actors.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.actionActors.AbilityActor;

public class ShopItemWindow extends ItemWindow {
	TextButton buy_button;
	TextButton clseed_button;
	public ShopItemWindow(Skin skin)
	{
		super(skin);
		buy_button=new TextButton("BUY", Assets.getSkin(), "default");
	    buy_button.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		    	  if (Global.getPlayerTeam().getGold()>= slot.getPrice())
		    	  {
		    		  Global.getPlayerTeam().decreaseGold(slot.getPrice());
		    		  if (slot.getEquip().equip_action!=null && slot.getEquip().equip_action.action.getActionType().equals("passive"))
		    		  {
		    			  if (((AbilityActor)slot.getEquip().equip_action.action).need_hire && Global.getPlayerTeam().getMaxHire()<=Global.dungeon.team_lists[0].size())
		    			  {
		    				  Global.showMessage("Need More Hire Point",1);
		    				  return;
		    			  }
		    			  ((AbilityActor)slot.getEquip().equip_action.action).executePassive(Global.dungeon, Global.getSelectedObj(), 0);
		    		  }
		    		  else
		    		  {
		    			  Global.getPlayerTeam().getInventory().setSlot(slot.equip);
		    		  }
		    			  slot.setSlot(null);
		    			  window.hideTable();
		    		  
		    	  }
		    	  else
		    	  {
		    		  Global.showMessage("NOT ENOUGH GOLD", 1);
		    	  }
		      }
		      
		    });
		close_button = new TextButton("CLOSE",skin);
		close_button.addListener(
				new ClickListener()
				{
					public void clicked(InputEvent event, float x, float y)
					{
						window.hideTable();
					}

				}
		);


	}
	
	public void setButton(SlotButton button) {
		button_table.add(buy_button).pad(5f);
		button_table.add(close_button).pad(5f);

	}

	
	
}
