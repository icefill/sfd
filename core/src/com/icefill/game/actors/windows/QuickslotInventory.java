package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.icefill.game.Assets;

public class QuickslotInventory extends Table {
	SlotButton[] shortcut;
	int BUTTON_HEIGHT=50;
	int BUTTON_WIDTH=50;
	
	public QuickslotInventory()
	{
		DragAndDrop drag_and_drop= new DragAndDrop();
		shortcut= new SlotButton[2];
        //this.setBackground(Assets.getBackground());

        this.add(new Label("quick slots",new Label.LabelStyle(Assets.getFont(), Color.WHITE))).colspan(2).row();
		for (int i=0;i<2;i++)
		{
			shortcut[i]=(new SlotButton(-1,null,drag_and_drop,true));
			this.add(shortcut[i]).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(1);
			
		}
		
		
	}
	public void setSlot(SlotButton slot_parent, int i)
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
	public void setSlot(SlotButton slot_parent)
	{
		setSlot(slot_parent,findEmptyQuickSlot());
	}
	public void setSlotIfEmptyQuickSlotExist(SlotButton slot_parent)
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
