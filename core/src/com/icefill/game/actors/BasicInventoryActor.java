package com.icefill.game.actors;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.InventoryActor.HidingClickListener;
import com.icefill.game.sprites.ObjSprites;

public class BasicInventoryActor extends BasicWindow {
	ArrayList<SlotActor> inventory_slots;
	//Window window;
	Table inven_table;
	//Table obj_inven_table;
	Table close_button_table;
	Label status_label;
	private boolean un_stackable;
	InventoryActor inven;
	int BUTTON_HEIGHT=50;
	int BUTTON_WIDTH=50;
	int INVEN_SIZE=15;
	String name;
	BasicInventoryActor inventory;
	public BasicInventoryActor(String name,Skin skin,String[] item_name){
		super(skin,false);
		this.name=name;
		inventory=this;
		//window= new Window("Inventory",skin);
		status_label=  new Label("", new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
		inven_table=new Table(skin);
		//window.setMovable(false);
		//drag_and_drop= new DragAndDrop();
		defaults().space(8);
		setInventory(item_name);
		table.add(inven_table);
		window.row();
		TextButton closeButton = new TextButton("CLOSE",skin);
		closeButton.addListener(new HidingClickListener(this));
		inven_table.add(closeButton).colspan(5);
	}
	public void setUnstackable()
	{
		un_stackable=true;
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
	public void hideTable() {
		//Global.setCameraCenter();

		this.setVisible(false);
		//Global.restoreCameraPosition();
	}
	public SlotActor getSlot(int i)
	{
		if (i<=inventory_slots.size())
		{
			return inventory_slots.get(i);
		}
		else return null;
	}
	public Table getInventoryTable()
	{
		return inven_table;
	}
	public void setInventory(String[] inventory_name){
        		
    		if (name!=null)
    			inven_table.add(name).colspan(4);
    		inventory_slots = new ArrayList<SlotActor>();
    		inven_table.row();
    		//if (inventory_name !=null)
        		for (int i=0;i<INVEN_SIZE;i++)
        		{
        			//if (inventory_name[i] !=null) {
        			//	inventory_slots.add(new SlotActor(-1,new EquipActor(inventory_name[i]),null,drag_and_drop));
        			//}
        			//else
        				inventory_slots.add(new SlotActor(-1,null,Global.getDragAndDrop(),false));
        			inven_table.add(inventory_slots.get(i)).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(2);
        			
        			if (i % 5 ==4)
        				inven_table.row();
        		}
        		//Label label=  new Label("DUMP", new Label.LabelStyle(Assets.getFont(), Color.BLACK) );
        		//inventory_slots.add(new DumpSlot(label,drag_and_drop));
        		//inven_table.add(new DumpSlot(label,drag_and_drop)).size(BUTTON_WIDTH,BUTTON_HEIGHT);;
    		//else
    		//	inven_table.add("NULL");
        
		//pack();
	}
	public void draw(Batch batch,float delta) {
		super.draw(batch,delta);
		//((ObjSprites)obj.sprites).drawAnimation(batch, 0, 0, 0, 700, 350, 0, 0, 0, 10, 10, this, this.getColor());
		
	}

	public int getEmptySlot() {
		int i=0;
		for (SlotActor temp:inventory_slots){
			if (temp.isEmpty()){
				return i;
			}
			i++;
		}
		return -1;
	}
	public boolean isFull() {
		if (getEmptySlot()==-1)
			return true;
		else return false;
	}
	public void setSlot(int i,EquipActor equip) {
		inventory_slots.get(i).setSlot(equip);
	}
	public boolean setSlot(EquipActor equip) {
		//int size= inventory_slots.size();
		// Check already existing item
		if (!un_stackable &&equip.isDisposable())
		{
			for (int i=0;i<INVEN_SIZE;i++)
			{
				if (inventory_slots.get(i).equip!=null
					&& inventory_slots.get(i).equip.getName().equals(equip.getName())){
					inventory_slots.get(i).addNumber(equip.n);
					return true;
				}
			
			}
		}
		// if not, find empty slot.
		int empty_index=getEmptySlot();
		if (empty_index!= -1) {
			setSlot(empty_index,equip);
			return true;
		}
		return false;
	}
	public EquipActor popSlot(int i) {
		EquipActor to_return= inventory_slots.get(i).getEquip();
		inventory_slots.get(i).setSlot(null);
		return to_return;
	}
	

}
