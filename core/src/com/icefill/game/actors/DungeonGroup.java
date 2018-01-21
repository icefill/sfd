package com.icefill.game.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Json;
import com.icefill.game.*;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.screens.BasicScreen;
import com.icefill.game.utils.NonRepeatRandomizer;

import java.util.ArrayList;

public class DungeonGroup extends Group  implements Constants {
  // number of rooms 
  public int dungeon_size[];
  
  public ItemPool item_pool;
  
  public ItemPool equip_pool;
  public ItemPool scroll_pool;
  
  //public MonsterPool monster_pool;
  public MonsterPool recruit_pool;
  
  int clicked_xx;
  int clicked_yy;
  
  int selected_xx;
  int selected_yy;
  
  int boss_room[][];
  
  
  AreaCell selected_cell;
  
  //When move room position, 
  int from_dir=0;
  int to_dir=0;
  
  // for cancel
  boolean cancel_flag=false;
  boolean enable_click=true;
  
  CursorActor cursor;
   
  //Music
  //Music music_wib;
  //Music music_anod;
  //Music music_battle;
  //UI
  

  // To Save ////////////////////////////////////////////////
  
  
  Team[] team_lists;

  //Destination
  int final_room[][];
  int initial_room[][];
  //position of current room initiate with -1
  int room_xxx=-1;
  int room_yyy=-1;
  int room_zzz=-1;
  
  public AreaComputer area_computer;
  
  GFSM fsm;
  
  MapActor current_map;
  private RoomGroup current_room;
  RoomGroup rooms[][][];
  //LinkedList<ObjActor> transparent_list;
  //NonObjSprites glow;
  private ObjActor selected_obj;
  private ObjActor attacker;
  ActionActor selected_action;
  public final BasicScreen screen;
  //boolean mouse_clicked = false;
  //private Color ambient_color;
  
  // MAna
  //public int mana=5;

  public DungeonGroup(DungeonSeed dungeon_factory,BasicScreen screen,Stage ui_stage)
  {
	  this.screen=screen;
	  initialize(dungeon_factory,screen,ui_stage);
  }
  
  private void initialize(DungeonSeed dungeon_seed,BasicScreen screen,Stage ui_stage)
  {
	  area_computer= new AreaComputer();
	  //To do :: dispose
	 // music_wib=Gdx.audio.newMusic(Gdx.files.internal("music/wouldnt-it-be.mp3"));
	 //music_anod = Gdx.audio.newMusic(Gdx.files.internal("music/anod_erik_skiff.mp3"));
	 //music_battle=Gdx.audio.newMusic(Gdx.files.internal("music/jumpshot_erik_skiff.mp3"));
	 //music_battle.setLooping(true);
	 //music_anod.setLooping(true);
	 //music_wib.setLooping(true);
	 //music_wib.setVolume(.5f);
	 // music_wib.play();
	 //music_anod.play();

	this.boss_room=dungeon_seed.boss_room;
	this.final_room=dungeon_seed.final_room;
	this.initial_room=dungeon_seed.initial_room;
	
    //this.Global.dungeon = this;
    this.fsm = new GFSM(this);
    Global.gfs=fsm;
    Global.dungeon=this;
    //this.transparent_list = new LinkedList();
    //this.ui_stage = ui_stage;
    //popup_ui= new HUDActor();
    //ui_stage.addActor(popup_ui);
    //this.screen = screen;
    this.cursor = new CursorActor();
    
  
    //Initialize team lists
  team_lists= new Team[2];
  team_lists[0]= new Team(dungeon_seed.char_name_list,true);
  Global.setPlayerTeam(team_lists[0]);
 
    this.dungeon_size=dungeon_seed.dungeon_size;
    rooms= new RoomGroup[dungeon_size[0]][dungeon_size[1]][dungeon_size[2]];
    
    this.item_pool=dungeon_seed.item_pool;
    this.equip_pool=dungeon_seed.equip_pool;
    this.scroll_pool=dungeon_seed.scroll_pool;
    this.recruit_pool=dungeon_seed.recruit_pool;
    
    // shrine initialize
    
    for (int z=0;z<dungeon_size[2];z++) {
    for (int y=0;y<dungeon_size[1];y++) {
    	for (int x=0;x<dungeon_size[0];x++) {
    		rooms[x][y][z]= new RoomGroup(dungeon_seed.room_shape_types.get("basic"),x,y,z,dungeon_seed);
    		
    	}
    }
    }
    
    for (int zzz=0;zzz<dungeon_size[2]-1;zzz++) {
    	rooms[final_room[zzz][0]][final_room[zzz][1]][zzz].map.makeVerticalDoor(final_room[zzz][0], final_room[zzz][1], zzz+1,rooms[final_room[zzz][0]][final_room[zzz][1]][zzz]);
    }
    /*
    for (int zzz=1;zzz<dungeon_size[2];zzz++) {
    	rooms[initial_room[zzz][0]][initial_room[zzz][1]][zzz].map.makeVerticalDoor(initial_room[zzz][0], initial_room[zzz][1], zzz-1,rooms[final_room[zzz][0]][final_room[zzz][1]][zzz]);
    }
    */
    
    //Set BoosroomDoor
    for (int zzz=0;zzz<dungeon_size[2];zzz++)
    {
    	setBossRoomDoor(boss_room[zzz][0],boss_room[zzz][1],zzz);
    }
    
    setRoom(dungeon_seed.initial_room[0][0],dungeon_seed.initial_room[0][1],0);
    
    addListener(new InputListener()
    {
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
       return true;
      }

      public void touchUp(InputEvent event, float x, float y,
    		  int pointer, int button) {
    	  	if (!isDragged()) {
    		   setMousePosition(x,y);
    		   
    		   DungeonGroup.this.clicked_xx = DungeonGroup.this.current_map.screenTomapCoordX(x, y);
    	       DungeonGroup.this.clicked_yy = DungeonGroup.this.current_map.screenTomapCoordY(x, y);
    	       DungeonGroup.this.cursor.doSelectAction();
    	       DungeonGroup.this.screen.clicked=true;
    	  	   //DungeonGroup.this.mouse_clicked = true;
    	  	}
      }
      public boolean mouseMoved(InputEvent event, float x, float y) {
    	  setMousePosition(x,y);
        	return true;
    	    
      }
      public boolean scrolled(InputEvent event, float x, float y, int amount)
      {
    	if (amount>0)  
    	  DungeonGroup.this.zoom(0.01F);
    	else
    		DungeonGroup.this.zoom(-0.01F);
        return false;
      }

    });
    
    //addListener(new MyGestureDetector());
    
    Global.getHUD().addMap(new MinimapActor(this));
    
    //this.addActor(battle_win_window); 
	
  }

  public ObjActor getAttacker() {
	  return attacker;
  }
  public void releaseAttacker() { attacker=null;}

	public MapActor getCurrentMap() {
	  return current_map;
  }
  public boolean isDragged () {
	  return screen.dragged;
  }
  /*
  public Color getAmbientColor() {
	  return ambient_color;
  }
  */
  public void removeRoom() {
	  if (this.cursor != null)
		  this.cursor.remove();
	  
	  if (this.current_map !=null)
		  this.current_map.remove();
	  
	  if (this.getCurrentRoom() != null) {
		  getCurrentRoom().removeRoom();  
	  }
  }
  public void setRoom(int xxx,int yyy) {
	  setRoom(xxx,yyy,room_zzz);
  }
  public void setRoom(int x, int y,int z){
	  //Global.getStage().getRoot().getColor().a = 1;
	  //SequenceAction sequenceAction = new SequenceAction();
	  //  sequenceAction.addAction(Actions.fadeOut(0.5f));

	  //  stage.getRoot().addAction(sequenceAction);

	  
	  area_computer.clearTargetList();
	  removeRoom();
	  // decide from door
	  if (room_xxx==-1) //initial
		  {from_dir=4;to_dir=5;}
	  else if (x-room_xxx>0) {from_dir=UL;to_dir=DR;}
	  else if (x-room_xxx<0) {from_dir=DR;to_dir=UL;}
	  else if (y-room_yyy>0) {from_dir=UR;to_dir=DL;}
	  else {from_dir=DL;to_dir=UR;}
	  
	  room_xxx=x;
	  room_yyy=y;
	  room_zzz=z;
	  
	  //Add map and room	  
	  this.addActor(rooms[x][y][z].getMap());
	  this.addActor(rooms[x][y][z]);
	  this.current_map=rooms[x][y][z].getMap();
	  
	  this.setCurrentRoom(rooms[x][y][z]);
	  getCurrentRoom().addActor(cursor);
	  this.getCurrentRoom().setVisited();
	  team_lists[1]=getCurrentRoom().getEnemyList();
	  
	  getCurrentRoom().setRoom(team_lists[0],from_dir,to_dir);
	  //Global.current_room=getCurrentRoom();
	  cursor.setMapPosition(0, 0);
	  cursor.moveTo(Global.getStage().getWidth()*.5f, Global.getStage().getHeight()*.5f);
	  //this.removeActor(battle_win_window);
	 // screen.camera.position.set(0,0,0);
	  //this.addActor(battle_win_window);
	  Global.getStage().getRoot().addAction(Actions.sequence(Actions.fadeOut(.0f)
			  ,Actions.fadeIn(0.5f)));

  }
  public void revealFloorMap()
  {
	  int zz=this.getCurrentRoom().room_zz;
	  for (int yy=0;yy<dungeon_size[1];yy++)
	  {
		  for (int xx=0;xx<dungeon_size[0];xx++)
		  {
			  rooms[xx][yy][zz].visited=true;
		  }  
	  }
  }
  
  public void zoom(float amount)
  {
    if ((0.5D < ((OrthographicCamera)this.screen.camera).zoom) && 
      (((OrthographicCamera)this.screen.camera).zoom < 1.5D))
    {
      ((OrthographicCamera)this.screen.camera).zoom += amount;
    }
  }

  
  public void draw(Batch batch, float delta) { //this.elapsed_time += Gdx.graphics.getDeltaTime();
    super.draw(batch, delta);
  }
  public String toString() {
	  String to_return= "Clicked:(" + this.clicked_xx + "," + this.clicked_yy + ")\n"+
	  		 "Cursor:(" + this.cursor.getXX() + "," + this.cursor.getYY() + ")\n"+
	  		 "GameState:" + this.fsm.getCurrentStateName() +"\n"+
	  		 "CurrentTeam:" + this.fsm.current_team+"\n"
	  		 +
	  		 "xxx: "+room_xxx+"\nyyy: "+room_yyy+"\nzzz: "+room_zzz+"\n";
	  return to_return;
  }
  
  public void act(float delta)
  {
    super.act(delta);
    if (cursor.isMapPositionChanged()) {
    	ObjActor obj=this.getCurrentRoom().getObj(cursor.getXX(),cursor.getYY());
    	if (obj!=null) {Global.getHUD().addCursorObj(obj);}
    }
    this.fsm.act(delta);
	  if (isMouseClicked()&& fsm.getCurrentState()==GS_PLAYER_ACTING)
      {
	  		Global.showMessage("Please Wait until acting is over.",1);
      }
    
  }

  
  public void selectObj(ObjActor to_select) {
    if (getSelectedObj() == to_select) {
      return;
    }
    Global.setSelectedObj(to_select);
    attacker= to_select;
    deSelectObj();
    setSelectedObj(to_select);
    Global.getHUD().addObj(getSelectedObj());
    if (to_select!=null) {
    Sound select_sound=Assets.getAsset("sound/select.wav", Sound.class);
    select_sound.play();
    getSelectedObj().selectActor();
    Global.renewItemWindow();
    }

  }
  
  public void deSelectObj() { 
	  if (this.getSelectedObj() != null)
  {
	  getSelectedObj().getInventory().setVisible(false);
	  Global.getHUD().removeAbility(getSelectedObj());
    this.getSelectedObj().deSelectActor();
    for (ActionContainer temp : this.getSelectedObj().getActionList()) {
      temp.action.remove();
    }
    this.setSelectedObj(null);
  }
}
  public void setMousePosition(float x,float y) {
	  cursor.setMapPosition(current_map.screenTomapCoordX(x,y), 
    	      current_map.screenTomapCoordY(x, y));
	    if (cursor.isMapPositionChanged()) {
	    	  Global.getHUD().releaseObj();
	      cursor.moveTo(current_map.mapToScreenCoordX(cursor.getXX(), cursor.getYY()), current_map.mapToScreenCoordY(cursor.getXX(), cursor.getYY()) + current_map.getCell(cursor.getXX(), cursor.getYY()).getZ()+5);
	      if (getCurrentRoom().getObj(cursor.getXX(), cursor.getYY()) == null) {
	          cursor.setZ(0.0F);
	      }
	        else {
	         // cursor.setZ(32.0F);
	          //Global.getHUD().addObj(getCurrentRoom().getActor(cursor.getXX(), cursor.getYY()));
	        }
	        
	   //   releaseOpaqueWall();
	   //   checkOpaqueWall(1);
	    }

  }

  public boolean checkTeamAnnihilated(int i) {
	  boolean team_annihilated=true;
	   for (ObjActor obj:team_lists[i]) {
			  if (obj.obj_state!=PL_DEAD) {
				  return false;
			  }
		  }
		  return true;
  }
  
  
  // team(team_number) is win -> return 1
  //                      lose -> reurn -1
  //                      else -> return 0
  public int checkWinorGameOver (int team_number) {
	   if (team_lists[0].getLeader() !=null &&
			  team_lists[0].getLeader().obj_state==PL_DEAD) {
		  team_lists[0].eliminateTeam();
		  return -1;}
	  if (team_lists[1].getLeader() !=null && 
			  team_lists[1].getLeader().obj_state==PL_DEAD) {
		  	team_lists[1].eliminateTeam();
		  return 1;
		  }
	  else if (checkTeamAnnihilated(1)) { return 1;}
	  else return 0;
  }

  
  public boolean isTeamEnd(int team_number) {
	  for (ObjActor temp_actor : team_lists[team_number]) {
          if (temp_actor.obj_state==PL_WAIT) {
            return false;
          }
        }
      return true;  
  }
  
  public boolean checkDungeonClear() {
	  
	  for (int xx=0;xx<dungeon_size[0];xx++){
		  for (int yy=0;yy<dungeon_size[1];yy++){
			  for (int zz=0;zz<dungeon_size[2];zz++){
			  if (!rooms[xx][yy][zz].checkEnemyAnnihilated())
				  return false;
			  }
		  }  
	  }
	  return true;
  }
  
  
	
	public OrthographicCamera getCamera() {
		return (OrthographicCamera)screen.camera;
	}

	public void setCancel() {
		cancel_flag=true;
	}
	public boolean isCanceled() {
		if (cancel_flag== true) {
			cancel_flag=false;
			return true;
		}
		return false;
			
	}
	public Team getTeamList(int i) {
		return team_lists[i];
	}

  public ObjActor getSelectedObj() {
		return selected_obj;
	}

	public void setSelectedObj(ObjActor selected_obj) {
		this.selected_obj = selected_obj;
		Global.setSelectedObj(selected_obj);
	}
	public RoomGroup getCurrentRoom() {
		return current_room;
	}

	public void setCurrentRoom(RoomGroup current_room) {
		this.current_room = current_room;
		Global.setCurrentRoom(current_room);
	}
	public void setBossRoomDoor(int xxx,int yyy, int zzz)
	{
		RoomGroup room=rooms[xxx][yyy][zzz];
		if (room!=null)
		{
			for (int j=0;j<4;j++)
			{
				if (room.map.getDoor(j)!=null)
				{
					room.map.getDoor(j).setBossDoor();
				}
			}
		}
		for (int i=0;i<4;i++)
		{
			room=getAdjacentRoom(xxx,yyy,zzz,i);
			if (room!=null)
			{
				int j= (i+2) %4;
					if (room.map.getDoor(j)!=null)
					{
						room.map.getDoor(j).setBossDoor();
					}
			}
		}
	}
	public RoomGroup getAdjacentRoom(int xxx,int yyy, int zzz,int dir)
	{
		switch (dir)
		{
		case DL:
			yyy++;
			break;
		case DR:
			xxx++;
			break;
		case UR:
			yyy--;
			break;
		case UL:
			xxx--;
			break;
		}
		if ((0<=xxx && xxx< dungeon_size[0] )
			&&(0<=yyy && yyy< dungeon_size[1] )	)
		{
			return rooms[xxx][yyy][zzz];
		}
		else
			return null;
	}
	
		
		public void showTargetsAccuracyAndDamage(ActionContainer action) {
		if (((AbilityActor)action.action).splash_type!=3)
		for (AreaCell target_cell:area_computer.getTargetList()){
				ObjActor target=current_room.getObj(target_cell);
				if (target!= null){
					StatusTuple status_change=((AbilityActor)action.action).getFirstStatusTuple();
					if (status_change!=null){
						String frb_string=getSelectedObj().getFrontRearOrBackString(target);
						int hit_rate=(int)((AbilityActor)action.action).calculateHitRate(status_change,action.level, getSelectedObj(), target);
						int crit=(int)((AbilityActor)action.action).calculateCriticalRate(status_change,action.level, getSelectedObj(), target);
						int min_damage=((AbilityActor)action.action).calculateMinDamage(status_change,action.level,Global.dungeon,getSelectedObj(),target);
						int max_damage=((AbilityActor)action.action).calculateMaxDamage(status_change,action.level,Global.dungeon,getSelectedObj(),target);
						int block_rate=((AbilityActor)action.action).calculateBlockRate(getSelectedObj(), target);
						target.addTargetInfo(frb_string+" attack\nAR:"+(hit_rate)+"(BLK:"+block_rate+")%\ndmg:"+min_damage+"~"+max_damage+"/"+target.status.getCurrentHP()+"\nCRIT:"+crit);
					}
				}
			}

	}

		public void enableClick(boolean flag)
	{
		enable_click=flag;
		screen.clicked=false;
	}
	public Boolean isMouseClicked() {
		
		if (enable_click &&screen.clicked==true ) {
			screen.clicked=false;
			return true;
		}
		else
		{
			return false;
		}
	}

   public GFSM getGFSM() {
	   return fsm;
   }

	public static class LightInformation{
	  int xx;
	  int yy;
	  int zz;
	  int direction;
	  float radius;
	  public LightInformation(){}
	  public LightInformation (int xx,int yy, int zz,float radius) {
		  this.xx=xx;
		  this.yy=yy;
		  this.zz=zz;
		  this.radius=radius;
		  if (xx==-1) direction=1;
		  else direction=0;
	  }
  }
  public static class MonsterPool {
	  ArrayList<MonsterPoolElt> monster_list_list;
	  
	  public MonsterPool() {
		  monster_list_list= new ArrayList<MonsterPoolElt>();
	  }
	  public void addMonsterPoolElt(MonsterPoolElt temp) {
		  monster_list_list.add(temp);
	  }
	  public ObjListElt getMonster(int dungeon_level){
		  ArrayList<MonsterPoolElt> temp_list_list= new ArrayList<MonsterPoolElt>();
		  for (MonsterPoolElt temp_list:monster_list_list) {
			  if ( (temp_list.min_level<= dungeon_level)&&
				   (temp_list.max_level == 0 || temp_list.max_level>=dungeon_level)
				   )
				  temp_list_list.add(temp_list);
		  }
		  return temp_list_list.get(com.icefill.game.utils.Randomizer.nextInt(temp_list_list.size())).getRandomObjElt();
	  }
  }
  public static class MonsterPoolElt {
	  int min_level;
	  int max_level;
	  ArrayList<ObjListElt> monster_list;
	  public MonsterPoolElt(){}
	  public MonsterPoolElt(int min_level,int max_level) {
		  this.min_level=min_level;
		  this.max_level=max_level;
		  monster_list= new ArrayList<ObjListElt>();
	  }
	  public void add(ObjListElt obj_elt) {
		  monster_list.add(obj_elt);
	  }
	  public ObjListElt getRandomObjElt() {
		return monster_list.get(com.icefill.game.utils.Randomizer.nextInt(monster_list.size()));
	  }
  }
  public static class ItemPool {
	  ArrayList<ItemPoolElt> item_list_list;
	  
	  public ItemPool() {
		  item_list_list= new ArrayList<ItemPoolElt>();
	  }
	  public void addItemPoolElt(ItemPoolElt temp) {
		  item_list_list.add(temp);
	  }
	  public String getItem(int dungeon_level){
		  ArrayList<ItemPoolElt> temp_list_list= new ArrayList<ItemPoolElt>();
		  for (ItemPoolElt temp_list:item_list_list) {
			  if ( (temp_list.min_level<= dungeon_level)&&
				   (temp_list.max_level == 0 || temp_list.max_level>=dungeon_level)
				   )
				  temp_list_list.add(temp_list);
		  }
		  return temp_list_list.get(com.icefill.game.utils.Randomizer.nextInt(temp_list_list.size())).getRandomItem();
	  }
  }
  public static class ItemPoolElt {
	  int min_level;
	  int max_level;
	  ArrayList<String> item_list;
	  public ItemPoolElt(){}
	  public ItemPoolElt(int min_level,int max_level) {
		  this.min_level=min_level;
		  this.max_level=max_level;
		  item_list= new ArrayList<String>();
	  }
	  public void add(String obj_elt) {
		  item_list.add(obj_elt);
	  }
	  public String getRandomItem() {
		return item_list.get(com.icefill.game.utils.Randomizer.nextInt(item_list.size()));
	  }
  }

  public static class ObjListElt
  {
    String file;
    String job;
    String control;
    int level;
    int team;
    String direction;
    public ObjListElt(){}
    public ObjListElt(String file, String job,int level,int team){
    	this.file=file;
    	this.job=job;
    	this.level=level;
    	this.team=team;
    }
    
  }
}