package com.icefill.game.actors.windows;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.utils.Randomizer;

public class PersonalInventory extends Table {
	ArrayList<SlotButton> equipping_slots;
	ArrayList<Label> equipping_label;
	ArrayList<SlotButton> inventory_slots;
	DragAndDrop drag_and_drop;
	//Table window;
	Table equip_table;
	//Table inven_table;
	Label status_label;
	int BUTTON_HEIGHT=50;
	int BUTTON_WIDTH=50;
	ObjActor obj;
	PersonalInventory inventory;
	public PersonalInventory(Skin skin, String[] equipment_name, String[] item_name , ObjActor obj, DragAndDrop drag_and_drop){
		super(skin);
		this.obj=obj;
		inventory=this;
		//window= new Table(skin);
		
		status_label=  new Label("", new Label.LabelStyle(Assets.getFont(), Color.BLACK) );
		equip_table= new Table(skin);
		//inven_table=new Table(skin);
		//right_table2= new Table(skin);
		
		this.drag_and_drop=drag_and_drop;
		setInventory(equipment_name,item_name);


		status_label.setText(obj.toString());
		this.add(equip_table).pad(8).colspan(2).row();
		this.add(status_label);

		equipping_slots.get(2).setCouple(equipping_slots.get(3));
		equipping_slots.get(3).setCouple(equipping_slots.get(2));
		
 		setVisible(false);

	}
	public void renewStatus() {
		status_label.setText(obj.toString());
	}
	public SlotButton getEquippingSlot(int i) {
		return equipping_slots.get(i);
	}
	public void setInventory(String[] equipment_name,String[] inventory_name){
    		equipping_slots = new ArrayList<SlotButton>();
    		equipping_label=new ArrayList<Label>();
    		if (equipment_name !=null) {
    			equip_table.add("Equipment").colspan(3).row();
        		
    		for (int i=0;i<4;i++)
    		{
    			Label label=  new Label("", new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
        		label.setFontScale(1.0f);
    			label.setColor(1f,1f,1f,1f);
    			equipping_label.add(label);
    			if (equipment_name[i] !=null) {
    				EquipActor equip=new EquipActor(equipment_name[i]);
    				if (i<4) {
    					equipping_slots.add(new SlotButton(i,equip,label,drag_and_drop,false));
    					equipping_slots.get(i).setObj(obj);
    				}
    				else {
    					equipping_slots.add(new SlotButton(4,equip,label,drag_and_drop,false));
    					equipping_slots.get(i).setObj(obj);
    				}
    				label.setText(equip.toString());
    				
    			}
    			else {
    				if (i<4 && i!=1) {
    				equipping_slots.add(new SlotButton(i,label,drag_and_drop,false));
    				equipping_slots.get(i).setObj(obj);
    				}
    				else 
    				{
    					equipping_slots.add(new SlotButton(4,label,drag_and_drop,false));
    					equipping_slots.get(i).setObj(obj);
    				}
    			
    			}
    			if (i!=0)
    			equip_table.add(equipping_slots.get(i)).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(5);
    		}
    		equipping_slots.get(1).add("ARMOR");
    		equipping_slots.get(2).add("L ARM");
    		equipping_slots.get(3).add("R ARM");
    		this.row();
    		}

	}
	public void draw(Batch batch,float delta) {
		super.draw(batch,delta);

	}
	public void act(float delta) {
		for (SlotButton temp:equipping_slots) {
			if (temp.getEquip()!=null)
			temp.getEquip().act(delta);
		}
	}
	public EquipActor getEquip(int i){
		if (i>=0)
			return equipping_slots.get(i).getEquip();
		else
			return equipping_slots.get(-i).getEquip();
	}
	public ObjActor.Status getTotalEquipStatus() {
		ObjActor.Status total_status= new ObjActor.Status();
		for (SlotButton temp_equip:equipping_slots) {
			if (temp_equip.getEquip()!=null)
			total_status.addStatus(temp_equip.getEquip().getStatus());
		} 
		return total_status;
	}
	public int getEmptySlot() {
		int i=0;
		for (SlotButton temp:inventory_slots){
			if (temp.isEmpty()){
				return i;
			}
			i++;
		}
		return -1;
	}
	public void setSlot(int i,EquipActor equip) {
		inventory_slots.get(i).setSlot(equip);
	}
	public void setEquip(SlotButton slot)
	{
		if(slot!=null && slot.equip!=null) {
			 int type= slot.equip.type;
  			 if (type!=-1) {
  			  
  			  type=Math.abs(type);
  			  
  			  //slot : to equip
  			  EquipActor equip=slot.equip;
  			  EquipActor equip_existing=getEquip(type);
  			  if (equip.equals(equip_existing)) return;
			  
  			  SlotButton equip_slot=getEquippingSlot(type);
  			  SlotButton couple_slot=equip_slot.getCouple();
  			  slot.setSlot(null);
  			  if (couple_slot !=null)
  			  {
  				  EquipActor couple_equip=couple_slot.getEquip();
  				  // equipping two handed -> drop couple slot
  				  // if equip_slot's couple is two_handed -> unequipping 
  				  if ((equip.two_handed && couple_equip!=null) ||
  						  (couple_equip!=null && couple_equip.two_handed))
  						  
  				  {
  					  if (couple_equip!=null)
  					  {
  						  couple_slot.setSlot(null);
  						  
  						  if (!Global.getPlayerTeam().getInventory().setSlot(couple_equip))
  						  {
  							  Global.getCurrentRoom().setItem(Global.getSelectedObj().getXX(),Global.getSelectedObj().getYY(),couple_equip,false);
  						  }
  						  //couple_slot.subNumber();
  					  }
  				  }
  				  
  			  }
  			  
  			  
  			  setEquip(type, equip);
  			  //slot.subNumber();
  			  if (equip_existing !=null)
  			  {
  				  
  				if (!Global.getPlayerTeam().getInventory().setSlot(equip_existing))
				{
  					Global.getCurrentRoom().setItem(Global.getSelectedObj().getXX(),Global.getSelectedObj().getYY(),equip_existing,false);
  					
				}
  				
  			  }
  			  
  			  
  			  
  		  }
  	  }
  	
	}
	public void setEquip(int i,EquipActor equip) {
		if (i>=0)
			equipping_slots.get(i).setSlot(equip);
		else 
			equipping_slots.get(-i).setSlot(equip);
		//if (equip.two_handed)
		//{
		//	int other_slot_n= ((i+1) % 2)+2;
		//	equipping_slots.get(other_slot_n).setSlot(equip.getPlaceHolder());
		//}
	}
	public boolean setSlot(EquipActor equip) {
		switch (equip.type) {
		case 0:
		case 1:
		case 2:
		case 3:
			if (equipping_slots.get(equip.type).getEquip() == null) {
				equipping_slots.get(equip.type).setSlot(equip);
				return true;
			}
			break;
		case 4:
		case 5:
			if (equipping_slots.get(4).getEquip() == null) {
				equipping_slots.get(4).setSlot(equip);
				return true;
			}
			else if (equipping_slots.get(5).getEquip() == null) {
				equipping_slots.get(5).setSlot(equip);
				return true;
			}
			break;
		default:
			break;
		}
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
	public EquipActor popEquip(int i) {
		EquipActor to_return= equipping_slots.get(i).getEquip();
		equipping_slots.get(i).setSlot(null);
		return to_return;
	}
	
	public EquipActor getRandomEquip() {
		int index= Randomizer.nextInt(1, 3);
		
		return popEquip(index);
		
	}
	public class HidingClickListener extends ClickListener{
		private Actor actor;
		
		public HidingClickListener(Actor actor) {
			this.actor=actor;
		}
		
		public void clicked(InputEvent event, float x, float y) {
			actor.setVisible(false);
		}
	}
}
