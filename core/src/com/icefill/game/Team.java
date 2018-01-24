package com.icefill.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.icefill.game.actors.windows.CommonInventoryWindow;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.windows.QuickslotInventory;
import com.icefill.game.actors.dungeon.RoomGroup;


public class Team extends LinkedList<ObjActor>{
	private int mana;
	private int initial_mana=0;
	private int delta_mana=1;
	private int max_mana=3;
	private int gold=200;
	private int jp;
	private int max_hire=3;
	CommonInventoryWindow inventory;
	QuickslotInventory quick_slot;
	//private LinkedList<ObjActor> team_list;
	private ObjActor leader;
	
	public Team()
	{
		super();
	}
	public Team(ArrayList<String> team_member_names,boolean first_member_is_leader)
	{
		super();
		  //initialize_players
		for (String temp:team_member_names) {
			  //current_room.setActor(obj);
			  this.addPlayer(temp);
		}
		if (first_member_is_leader)
			setLeader(this.get(0));
		inventory= new CommonInventoryWindow(Assets.getSkin(),null);
		inventory.addToUIStage();
		//Global.getUIStage().addActor(inventory);
		quick_slot= new QuickslotInventory();
		quick_slot.setSlot(inventory.getSlot(0));
		quick_slot.setSlot(inventory.getSlot(1));
		inventory.setVisible(false);
			
	}
	public CommonInventoryWindow getInventory()
	{
		return inventory;
	}
	public void eliminateTeam()
	{
		for (ObjActor team_member:this)
		{
			//if(team_member.status.current_hp>0)
			//{
				if (!team_member.isDead())
					team_member.inflictDamageInRatio(1f,null);
				//team_member.checkDeadandExecuteDead(Global.dungeon);
			//}
		}
	}
	public void showInventory()
	{
		inventory.showTable();
	}
	public void hideInventory()
	{
		inventory.hideTable();
	}
	public QuickslotInventory getQuickSlotInventory()
	{
		return quick_slot;
	}
	public void setLeader(ObjActor obj)
	{
		this.leader=obj;
		obj.setLeader();
	}
	public ObjActor getLeader()
	{
		return this.leader;
	}
	
	  public ObjActor addPlayer(String player_name) {
		  ObjActor obj=new ObjActor(player_name,Global.getDragAndDrop());
		  this.add(obj);
		  Global.getUIStage().addActor(obj.getInventory());
		  return obj;
	  }
	  public ObjActor addPlayer(ObjActor obj) {
		  this.add(obj);
		  Global.getUIStage().addActor(obj.getInventory());
		  return obj;
	  }
	  public void addPlayerandSet(String player_name,RoomGroup room) {
		  room.setObj(addPlayer(player_name));		  
	  }
	public void increasMaxHire(int amount)
	{
		max_hire+=amount;
	}
	public int getMaxHire()
	{
		return max_hire;
	}
	public int getMaxMana()
	{
		return max_mana;
	}
	
	public void increaseMana(int amount) {
		mana=mana+amount;
		if (mana>max_mana) mana=max_mana;
	}
	public void increaseMaxMana(int amount) {
		max_mana=max_mana+amount;
	}
	
	public int getMana() {
		return mana;
	}
	public int getGold() {
		return gold;
	}
	public void increaseGold(int amount)
	{
		gold= gold+amount;
	}
	public void decreaseGold(int amount)
	{
		gold= gold-amount;
		if (gold<0) gold=0;
	}
	public int getJP()
	{
		return jp;
	}
	public void increaseJP(int amount)
	{
		jp+=amount;
	}
	public void decreaseJP(int amount)
	{
		jp-=amount;
		if (jp<0) jp=0;
	}
	
	
	public void decreaseMana(int amount) {
		mana=mana-amount;
		if (mana<0) mana=0;
	}
	public int getDeltaMana()
	{
		return delta_mana;
	}
	public void setManaZero() {
		mana=0;
	}
	public void setManaToInitial()
	{
		mana=initial_mana;
	}
	public String getTeamResourceInfo()
	{
		return "mana: "+mana+"/"+max_mana+
				"\nhire: "+(size()-1)+"/"+max_hire+
				"\ngold: "+gold;
	}
	
}
