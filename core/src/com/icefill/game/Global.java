package com.icefill.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.icefill.game.actors.*;
import com.icefill.game.actors.dungeon.GFSM;
import com.icefill.game.actors.windows.HUD;
import com.icefill.game.actors.windows.SlotButton;
import com.icefill.game.screens.BasicScreen;

public class Global {
	private static Stage stage;
	private static Stage ui_stage;
	private static ObjActor selected_obj;
	private static com.icefill.game.actors.dungeon.RoomGroup current_room;
	private static EquipActor selected_equip;
	private static SlotButton selected_slot;
	public static DragAndDrop drag_and_drop= new DragAndDrop();
	public static GFSM gfs;
	public static com.icefill.game.actors.dungeon.DungeonGroup dungeon;
	public static BasicScreen current_screen;
	private static TooltipTable tool_tip;
	private static com.icefill.game.actors.windows.ItemWindow item_window;
	private static com.icefill.game.actors.windows.ShopItemWindow shop_item_window;
	private static Label information_label ;
	private static int message_n=0;
	private static Team player_team;
	private static HUD hud;
	private static Vector3 reserved_camera_position;
	
	public static void setUIStage(Stage st)
	{
		ui_stage=st;
	}
	public static Stage getUIStage()
	{
		return ui_stage;
	}
	public static void setStage(Stage st)
	{
		stage=st;
	}
	public static Stage getStage()
	{
		return stage;
	}
	public static void setHUD(HUD h)
	{
		hud=h;
		ui_stage.addActor(h);
	}
	public static HUD getHUD()
	{
		return hud;
	}
	

	public static void initializeTooltip() {
		tool_tip=null;
		item_window=null;
	}
	/*
	public static void reserveCameraPosition(){
		if (reserved_camera_position==null)
		{
			reserved_camera_position= new Vector3();
		}
		reserved_camera_position.set(current_screen.camera.position);
	}
	public static void restoreCameraPosition()
	{
		if (reserved_camera_position!=null)
		{
			current_screen.camera.position.set(reserved_camera_position);
			//reserved_camera_position=null;
		}
	}

	public static void setCameraCenter()
	{
		//current_screen.camera.position.set(Global.getCurrentRoom().getMap().map_center_x, Global.getCurrentRoom().getMap().map_center_y, 0f);

	}
	*/
	public static void setCurrentRoom(com.icefill.game.actors.dungeon.RoomGroup room)
	{
		current_room=room;
	}
	public static com.icefill.game.actors.dungeon.RoomGroup getCurrentRoom()
	{
		return current_room;
	}
	
	public static ObjActor getSelectedObj()
	{
		return selected_obj;
	}
	public static void setSelectedObj(ObjActor s)
	{
		selected_obj=s;
	}
	
	public static DragAndDrop getDragAndDrop()
	{
		return drag_and_drop;
	}
	public static void setTooltip(String str) {
		if (tool_tip ==null) {
			tool_tip= new TooltipTable();
			ui_stage.addActor(tool_tip);
		}
		//tool_tip.setPosition(Global.current_screen.getScreenX(),Global.current_screen.getScreenY());
		tool_tip.setText(str);
	}
	/*
	*/
	public static void initializeInformationLabel()
	{
	    information_label = new Label("",Assets.getLabelStyle());
	    information_label.setPosition(ui_stage.getWidth()/2-100, ui_stage.getHeight()-30);
		information_label.setHeight(30);
		information_label.setFontScale(1.0f);
		ui_stage.addActor(information_label);

	}
	
	public static void setitemWindow(SlotButton slot) {
		if (item_window ==null) {
			item_window= new com.icefill.game.actors.windows.ItemWindow(Assets.getSkin());
		}
		ui_stage.addActor(item_window);
		if (slot.getEquip()!=null)
		item_window.showTable(slot,null);
		//tool_tip.setPosition(Global.current_screen.getScreenX(),Global.current_screen.getScreenY());
	}
	public static void setShopitemWindow(SlotButton slot) {
		if (shop_item_window ==null) {
			shop_item_window= new com.icefill.game.actors.windows.ShopItemWindow(Assets.getSkin());
		}
		ui_stage.addActor(shop_item_window);
		if (slot.getEquip()!=null)
			shop_item_window.showTable(slot,null);
		//tool_tip.setPosition(Global.current_screen.getScreenX(),Global.current_screen.getScreenY());
	}
	
	public static void releaseTooltip () {
		if (tool_tip!=null)
		tool_tip.relesseText();
	}
	public static void renewItemWindow() {
		if (item_window!=null)
		item_window.renewSlot();
	}
	public static void renewShopItemWindow() {
		if (shop_item_window!=null)
		shop_item_window.renewSlot();
	}
	public static void setPlayerTeam(Team team)
	{
		player_team=team;
		hud.setInventory(team);

	}
	public static Team getPlayerTeam()
	{
		return player_team;
	}
	public static void showMessage(String message,int type) {
		final Label status_change_label = new Label("",Assets.getLabelStyle());//new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
		status_change_label.setSize(200, 30);
		status_change_label.setFontScale(1.0f);
		status_change_label.setText(message);
		status_change_label.pack();
		status_change_label.setPosition((ui_stage.getWidth()-status_change_label.getWidth())/2,ui_stage.getHeight()-status_change_label.getHeight()-10);
		status_change_label.setVisible(true);
		ui_stage.addActor(status_change_label);
		if (type==1)
		{
			Sound sound= Assets.getAsset("sound/error.wav", Sound.class);
			sound.play();
			status_change_label.setPosition((ui_stage.getWidth()-status_change_label.getWidth())/2
					,(ui_stage.getHeight()-status_change_label.getHeight())/2);
			status_change_label.addAction(
					
					Actions.sequence(
							Actions.fadeIn(.1f),
							Actions.delay(1f),		
							Actions.fadeOut(.4f),
							
							Actions.removeActor()
					)
					
				);

		}
		else
			{
			message_n++;
			
			status_change_label.addAction(
										
										Actions.sequence(
												Actions.fadeIn(.1f),
												Actions.parallel(
														Actions.moveTo((ui_stage.getWidth()-status_change_label.getWidth())/2,ui_stage.getHeight()-35*(message_n+1),message_n*.3f)),
												Actions.run(new Runnable() {public void run() {message_n--;}}),
												Actions.delay(1f),		
												Actions.fadeOut(.4f),
												
												Actions.removeActor()
										)
										
									);
			}
	}
	public static void showInformation(String message) {
		//hideInformation();
		if (information_label==null)
		{
			initializeInformationLabel();
		}
		information_label.setText(message);
		information_label.pack();
		information_label.setHeight(30);
		information_label.setPosition(ui_stage.getWidth()/2-information_label.getWidth()*0.5f, -2);
		information_label.setVisible(true);
		/*
		information_label.addAction(
										
										Actions.sequence(
												//Actions.moveTo(200,100,.3f),
												Actions.fadeIn(.3f)
												
										)
										
									);*/
	}
	public static void showBigMessage(String message) 
	{
	    final Label status_change_label = new Label("",Assets.getLabelStyleBackgrounded());//new Label.LabelStyle(Assets.getFont(), Color.WHITE) );
		status_change_label.setSize(300, 60);
		status_change_label.setFontScale(1.0f);
		status_change_label.setText(message);
		status_change_label.pack();
		status_change_label.setVisible(true);
		ui_stage.addActor(status_change_label);
		Sound sound= Assets.getAsset("sound/error.wav", Sound.class);
		sound.play();
		status_change_label.setPosition((ui_stage.getWidth()-status_change_label.getWidth())/2,ui_stage.getHeight()/2-status_change_label.getHeight()/2);
		status_change_label.addAction(
					
					Actions.sequence(
							//Actions.moveTo(200,100,.3f),
							Global.gfs.pauseGFSAction(),
							Actions.fadeIn(.2f),
							//Actions.parallel(
									//Actions.delay(1f),
							//Actions.run(new Runnable() {public void run() {message_n--;}}),
							Actions.delay(1.2f),
							Actions.fadeOut(.1f),
							Global.gfs.reRunGFSAction(),
							Actions.removeActor()
					)
					
				);


	}

	public void hideInformation()
	{
		information_label.addAction(
				
				Actions.sequence(
						//Actions.moveTo(200,100,.3f),
						Actions.fadeOut(.1f)
						
				));

		information_label.setVisible(false);
	}
	public static void setSelectedSlot(SlotButton slot)
	{
		selected_slot=slot;
	}
	public static void releaseSelectedSlot()
	{
		selected_slot=null;
	}
	public static EquipActor getSelectedEquip()
	{
		return selected_slot.getEquip();
	}
	public static boolean decreaseNumberOfSelectedEquip()
	{
		if (selected_slot!=null)
		{
			if (selected_slot.getEquip().isDisposable())
			{
  		  		selected_slot.subNumber();
  		  		
			}
			return true;
		}
		return false;
	}
	

}
