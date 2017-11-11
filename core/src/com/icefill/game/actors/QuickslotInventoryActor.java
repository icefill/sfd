package com.icefill.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.icefill.game.Assets;
import com.icefill.game.Global;

public class QuickslotInventoryActor extends Table {
	SlotActor[] shortcut;
	int BUTTON_HEIGHT=50;
	int BUTTON_WIDTH=50;
	
	public QuickslotInventoryActor()
	{
		DragAndDrop drag_and_drop= new DragAndDrop();
		shortcut= new SlotActor[2];
        //this.setBackground(Assets.getBackground());

        this.add(new Label("quick slots",new Label.LabelStyle(Assets.getFont(), Color.WHITE))).colspan(2).row();
		for (int i=0;i<2;i++)
		{
			shortcut[i]=(new SlotActor(-1,null,drag_and_drop,true));
			this.add(shortcut[i]).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(1);
			
		}
		
		
	}
	public void setSlot(SlotActor slot_parent,int i)
	{
		if (shortcut[i].quick_parent!=null)
		{
			shortcut[i].quick_parent.removeQuickSlot();
		}
		if (slot_parent.equip!=null)
		{
			shortcut[i].setSlot(slot_parent.equip);
		}
		shortcut[i].quick_parent=slot_parent;
		slot_parent.quick=shortcut[i];
		slot_parent.setColor(.5f,0f,0f,1f);	
	}
	public void setSlot(SlotActor slot_parent)
	{
		setSlot(slot_parent,findEmptyQuickSlot());
	}
	public void setSlotIfEmptyQuickSlotExist(SlotActor slot_parent)
	{
		int i=findEmptyQuickSlot();
		if (i>=0)
			setSlot(slot_parent,i);
	}
	public int findEmptyQuickSlot()
	{
		for (int i=0;i<2; i++)
		{
			if (shortcut[i].equip==null)
				return i;
		}
		return 0;
	}
	
	

}
