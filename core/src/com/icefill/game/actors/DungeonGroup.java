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
import com.badlogic.gdx.utils.OrderedMap;
import com.icefill.game.*;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.screens.BasicScreen;
import com.icefill.game.utils.NonRepeatRandomizer;

import java.util.ArrayList;
import java.util.LinkedList;

public class DungeonGroup extends Group  implements Constants
{
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
  
  //HUDActor popup_ui;

  // To Save ////////////////////////////////////////////////
  
  
  Team[] team_lists;
  //ObjActor leader;
  //private LinkedList<ObjActor> dead_player_list;
  //private LinkedList<ObjActor> dead_enemy_list;
   
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
  
  public DungeonGroup(String json_name, BasicScreen screen, Stage ui_stage)
  {
	  this.screen=screen;
	Json json = new Json();
	json.setElementType(DungeonSeed.class, "obj_list", ObjListElt.class);
    DungeonSeed dungeon_factory = (DungeonSeed)json.fromJson(DungeonSeed.class, Gdx.files.internal(json_name));
    //cancel_action= new CancelAction();
   initialize(dungeon_factory,screen,ui_stage);
  }
  
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
      //private boolean cameraOutOfLimit(Vector3 position){
    	 // int x_left_limit=WINDOW_WIDTH/2;
    	  
      //}

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
	  //System.out.println(pl_state_name[team_lists[0].get(0).obj_state]);
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

	/**************************************************************************************************
													SEED CLASS
	**************************************************************************************************/
  public static class DungeonSeed
  { 
	int[][] initial_room;
	int[] dungeon_size;
	int dungeon_area;
	int[][] final_room;
	int[][] boss_room;
    ArrayList<String> char_name_list;
    OrderedMap<String,RoomShapeType> room_shape_types;
    MonsterPool recruit_pool;
    MonsterPool monster_pool;
    MonsterPool boss_pool;
    ItemPool item_pool;
    ItemPool equip_pool;
    ItemPool scroll_pool;
    ArrayList<ObjListElt> undest_obs_info_list;
	ArrayList<ObjListElt> dest_obs_info_list;
	  ArrayList<ObjListElt> explosive_name_list;
    RoomSeed[][][]room_array;
    LinkedList<RoomSeed> stack;
    public DungeonSeed(){}
    public DungeonSeed(OrderedMap<String,RoomShapeType> room_type, ArrayList<String> char_name_list, MonsterPool recruit_pool, MonsterPool monster_pool, MonsterPool boss_pool
			, ItemPool item_pool, ItemPool equip_pool, ItemPool scroll_pool, ArrayList<ObjListElt>explosive_name_list,ArrayList<ObjListElt>undest_obs_info_list, ArrayList<ObjListElt>dest_obs_info_list) {
    	this.char_name_list =char_name_list;
    	//this.obj_list=obj_list;
    	this.recruit_pool=recruit_pool;
    	this.monster_pool=monster_pool;
    	this.boss_pool=boss_pool;
    	this.item_pool= item_pool;
    	this.equip_pool= equip_pool;
    	this.scroll_pool=scroll_pool;
    	this.explosive_name_list=explosive_name_list;
    	this.undest_obs_info_list =undest_obs_info_list;
		this.dest_obs_info_list =dest_obs_info_list;
    	this.room_shape_types=room_type;
    	int obj_n=1;
    	dungeon_size= new int[3];
    	
    	
    	dungeon_size[0]=4;
    	dungeon_size[1]=4;
    	dungeon_size[2]=5;
    	//room_type= new int[dungeon_size[0]][dungeon_size[1]][dungeon_size[2]];
    	chooseInitialAndFinalRooms();
    	boss_room= new int[dungeon_size[2]][2];

    	stack= new LinkedList<RoomSeed>();
    	
    	
    	room_array= new RoomSeed[dungeon_size[0]][dungeon_size[1]][dungeon_size[2]];
    	
    	dungeon_area=dungeon_size[0]*dungeon_size[1];
    	
    	float monster_room_ratio=0.33f;
    	
    	int room_n= (dungeon_size[0]*dungeon_size[1]);
    	
    	//Initialize rooms
    	
    	for (int xxx=0;xxx<dungeon_size[0];xxx++) {
    		for (int yyy=0;yyy<dungeon_size[1];yyy++) {
    			for (int zzz=0;zzz<dungeon_size[2];zzz++) {
    				//Randomize room size
    				int room_size_x= com.icefill.game.utils.Randomizer.nextInt((int)(8+zzz*.35f),9+(int)(zzz*.5f));
    				int room_size_y= com.icefill.game.utils.Randomizer.nextInt((int)(8+zzz*.35f),9+(int)(zzz*.5f));
    				room_array[xxx][yyy][zzz]= new RoomSeed(room_type.get("basic"),xxx,yyy,room_size_x,room_size_y);

    				if (xxx==initial_room[zzz][0] && yyy==initial_room[zzz][1])
    					room_array[xxx][yyy][zzz].checkInitialRoom();
    	    	
    	    	//Set Floor
    			}
    		}      	
    	}
    	
    	makeDungeonRoute();
    	
    	
    	//Make Obstacle and monsters
    	
    	
    	for (int zzz=0;zzz<dungeon_size[2];zzz++) {
    		for (int xxx=0;xxx<dungeon_size[0];xxx++) {
    			for (int yyy=0;yyy<dungeon_size[1];yyy++) {
    				if ( //room_array[xxx][yyy][zzz].initial_room
    						(xxx==initial_room[zzz][0] && yyy==initial_room[zzz][1])
    						
    						) {
    					room_array[xxx][yyy][zzz].makeWallAndDoor();
    					if (zzz!=0) {
    						room_array[xxx][yyy][zzz].makeAngelRoom();
    					//room_array[xxx][yyy][zzz].makeUpStair();
    					}
    					else {
    						room_array[xxx][yyy][zzz].makeInitialRoom();
    						 					}
    					// Do some stuff
    				}
    				else if (xxx==final_room[zzz][0] && yyy==final_room[zzz][1]){
    					room_array[xxx][yyy][zzz].makeWallAndDoor();
    					if (zzz!=dungeon_size[2]) {
    							
    						room_array[xxx][yyy][zzz].makeDownStair();
    						room_array[xxx][yyy][zzz].room_type=3;
    					}
    				}
    				else {
    					int room_area=room_array[xxx][yyy][zzz].getRoomArea();
    			
    					//Create Obstacles
    					int max_obs=(int)(room_area*0.2f);
    					int min_obs=(int)(room_area*0.15f);
    					int obs_n_in_room=0;
    					obs_n_in_room= com.icefill.game.utils.Randomizer.nextInt(min_obs,max_obs);
						room_array[xxx][yyy][zzz].createObstacles(obs_n_in_room);
						room_array[xxx][yyy][zzz].makeWallAndDoor();
    				}
    				
    	        
    			}
    		}
    		
    		
    	}
    	
    	
    	com.icefill.game.utils.NonRepeatRandomizer randomizer= new com.icefill.game.utils.NonRepeatRandomizer(dungeon_size[0],dungeon_size[1]);
    	int rn;
    	for (int i=0;i<dungeon_size[2];i++) {
    		randomizer.reset();
    		int p=0;
    		//Create Monsters
    		for (int k=0;k<dungeon_size[0]*dungeon_size[1]*monster_room_ratio;k++) {
        		
    			for (int j=0;j<10;j++) {
    				
    				rn=randomizer.nextInt();
    				int xx= rn/10;
    				int yy= rn%10;
    				if (
    						(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    						(xx!=final_room[i][0] || yy!=final_room[i][1]) &&
        					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    						) {
    					int monster_n= com.icefill.game.utils.Randomizer.nextInt((int)((i)*.5f+3),(int)((i)*.5f+4));//(int)(room_array[xx][yy][i].getRoomArea()*0.022f+(i+1));
    					room_array[xx][yy][i].createMonster(obj_n,monster_n);
    					room_array[xx][yy][i].room_type=4;
    					System.out.println("monster room #"+p+":"+rn);
    					p++;
    					break;
    				}
    			}
    		}
   		for (int j=0;j<10;j++) {
    			rn=randomizer.nextInt();
    			int xx= rn/10;
    			int yy= rn%10;
    			if (
    					(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    					(xx!=final_room[i][0] || yy!=final_room[i][1]) &&
    					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    				) {
    			room_array[xx][yy][i].makeHealingRoom();
    			room_array[xx][yy][i].room_type=5;
    			System.out.println("healingroom"+rn);
    			break;
    			}
    		}
   		for (int j=0;j<10;j++) {
			rn=randomizer.nextInt();
			int xx= rn/10;
			int yy= rn%10;
			if (
					(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
					(xx!=final_room[i][0] || yy!=final_room[i][1]) &&
					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
				) {
			room_array[xx][yy][i].makeHealingRoom();
			room_array[xx][yy][i].room_type=5;
			System.out.println("healingroom"+rn);
			break;
			}
		}
	
    		for (int k=0;k<1;k++) {
    			for (int j=0;j<10;j++) {
    				rn=randomizer.nextInt();
        			int xx= rn/10;
        			int yy= rn%10;
        			if (
    						(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    						(xx!=final_room[i][0] || yy!=final_room[i][1]) &&
        					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    						) {
    					room_array[xx][yy][i].makeItemRoom();
    					room_array[xx][yy][i].room_type=2;
    					System.out.println("itemroom"+rn);
    					break;
    				}
    			}
    		}

     		for (int k=0;k<1;k++) {
    			for (int j=0;j<10;j++) {
    				rn=randomizer.nextInt();
        			int xx= rn/10;
        			int yy= rn%10;
        			if (
    						(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    						(xx!=final_room[i][0] || yy!=final_room[i][1]) &&
        					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    						) {
    					room_array[xx][yy][i].makeShopRoom();
    					room_array[xx][yy][i].room_type=2;
    					System.out.println("shoproom"+rn);
    					break;
    				}
    			}
    		}
    		for (int k=0;k<1;k++) {
    			for (int j=0;j<10;j++) {
    				rn=randomizer.nextInt();
        			int xx= rn/10;
        			int yy= rn%10;
        			if (
    						(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    						(xx!=final_room[i][0] || yy!=final_room[i][1])&&
        					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    						) {
        				System.out.println("scrollroom"+rn);
    					room_array[xx][yy][i].makeScrollRoom();
    					room_array[xx][yy][i].room_type=2;
    					break;
    				}
    			}
    		}
    		for (int k=0;k<1;k++) {
    			for (int j=0;j<10;j++) {
    				rn=randomizer.nextInt();
        			int xx= rn/10;
        			int yy= rn%10;
        			if (
    						(xx!=initial_room[i][0] || yy!=initial_room[i][1]) &&
    						(xx!=final_room[i][0] || yy!=final_room[i][1])&&
        					(xx!=boss_room[i][0]|| yy!=boss_room[i][1])
    						) {
        				System.out.println("scrollroom"+rn);
    					room_array[xx][yy][i].makemercRoom();
    					room_array[xx][yy][i].room_type=2;
    					break;
    				}
    			}
    		}
    		int monster_n= com.icefill.game.utils.Randomizer.nextInt((int)((i)*.5f+4),(int)((i)*.5f+4));
    		room_array[boss_room[i][0]][boss_room[i][1]][i].createMonster(obj_n,monster_n);
    		room_array[boss_room[i][0]][boss_room[i][1]][i].createBoss();
    		room_array[boss_room[i][0]][boss_room[i][1]][i].room_type=1;
    		
    	}
 
    	
    }

    public void clearVisited(int zzz) {
    	for (int xx=0;xx<dungeon_size[0];xx++){
    		for (int yy=0;yy<dungeon_size[1];yy++){
        		if (room_array[xx][yy][zzz]!=null)
        			room_array[xx][yy][zzz].visited=false;
        	}	
    	}
    }
    
    public void makeDungeonRoute() {
    	
    	  	
    	
    	for (int zzz=0;zzz<dungeon_size[2];zzz++) {
    	do {
    		stack.clear();
    		int current_xxx=initial_room[zzz][0];
	    	int current_yyy=initial_room[zzz][1];
    		stack.add(room_array[current_xxx][current_yyy][zzz]);
        	stack.getLast().visited=true;
        	LinkedList<Integer> possible_door= new LinkedList<Integer>();
        	RoomSeed back_room=stack.getLast();
        	int iter=0;
        	
        	while
        			(current_xxx != final_room[zzz][0] || current_yyy != final_room[zzz][1]
        					) {
    		
        		int temp_xxx=0;
        		int temp_yyy=0;
        		
        		RoomSeed temp_room;
    		
    		
        		temp_xxx=current_xxx;temp_yyy=current_yyy+1;	
        		if (  
        				(0<=temp_xxx  && temp_xxx<dungeon_size[0])
        				&&  (0<=temp_yyy  && temp_yyy<dungeon_size[1])
        		) {
    				temp_room= room_array[temp_xxx][temp_yyy][zzz];
    				if (
    						!(temp_room.visited)
    						&&  !back_room.equals(temp_room)
    				) {
    						possible_door.add(DL);
    				}
    			}
        		temp_xxx=current_xxx+1;temp_yyy=current_yyy;
        		if (  
        				(0<=temp_xxx  && temp_xxx<dungeon_size[0])
        				&&  (0<=temp_yyy  && temp_yyy<dungeon_size[1])
    		    ) {
    				temp_room= room_array[temp_xxx][temp_yyy][zzz];
    				if (
    						!(temp_room.visited)
    						&&  !back_room.equals(temp_room)
    				) {
    					possible_door.add(DR);
    				}
    			}
        		temp_xxx=current_xxx;temp_yyy=current_yyy-1;
        		if (  
    				(0<=temp_xxx  && temp_xxx<dungeon_size[0])
    				&&  (0<=temp_yyy  && temp_yyy<dungeon_size[1])
    		    ) {
    				temp_room= room_array[temp_xxx][temp_yyy][zzz];
    				if (
    						!(temp_room.visited)
    						&&  !back_room.equals(temp_room)
    					) {
    						possible_door.add(UR);
    				}
    			}
        		temp_xxx=current_xxx-1;temp_yyy=current_yyy;
        		if (  
    				(0<=temp_xxx  && temp_xxx<dungeon_size[0])
    				&&  (0<=temp_yyy  && temp_yyy<dungeon_size[1])
    		    ) {
    				temp_room= room_array[temp_xxx][temp_yyy][zzz];
    				if (
    						!(temp_room.visited)
    						&&  !back_room.equals(temp_room)
    				) {
    						possible_door.add(UL);
    				}
    			}
        		
        		// has no possible door -> backtrack 
        		if (possible_door.size() ==0) {
        			RoomSeed temp=stack.pollLast();
        			//temp.visited=false;
        			current_xxx=stack.getLast().xxx;
        			current_yyy=stack.getLast().yyy;
        			back_room=temp;
        		}
        		else {
        			int next_direction=0;
        			// choose next direction
        			if (possible_door.size() ==1) {
        				next_direction=possible_door.getLast();
        			}
        			else {
        				//Make door direction
        				next_direction=possible_door.get(com.icefill.game.utils.Randomizer.nextInt(possible_door.size()));
        			}
    			
        			switch (next_direction) {
    					case DL: current_yyy=current_yyy+1;break;
    					case DR: current_xxx=current_xxx+1;break;
    					case UR: current_yyy=current_yyy-1;break;
    					case UL: current_xxx=current_xxx-1;break;
        			}
        			stack.add(room_array[current_xxx][current_yyy][zzz]);
        			room_array[current_xxx][current_yyy][zzz].visited=true;
        		}
        		possible_door.clear();
        		iter++;
    		
    		// if has at list one , randomly choose 1
    		// if has not, backtrack 
        	} // while
    	clearVisited(zzz);
    	
    	} while (stack.size()==0);
    	//System.out.println("Result!");
    	
    	/*
    	for (RoomFactory temp:stack){
    		System.out.print("("+temp.xxx+","+temp.yyy+")");
    	}
    	System.out.println();
    	*/
    	
    	// make door
    	for (int i=0;i<stack.size()-1;i++) {
    		int dx= stack.get(i+1).xxx-stack.get(i).xxx;
    		int dy= stack.get(i+1).yyy-stack.get(i).yyy;
    		if (dx>0) {
    			stack.get(i+1).has_door[UL]=true;
    			stack.get(i).has_door[DR]=true;
    		}
    		else if (dx<0) {
    			stack.get(i+1).has_door[DR]=true;
    			stack.get(i).has_door[UL]=true;
    		}
  
    		else if (dy>0) {
    			stack.get(i+1).has_door[UR]=true;	
    			stack.get(i).has_door[DL]=true;
    		}
    		else {
    			stack.get(i+1).has_door[DL]=true;	
    			stack.get(i).has_door[UR]=true;
    		}
    	}
    	
    	//check boss_room
    	
    	boss_room[zzz][0]=stack.get(stack.size()-2).xxx;
    	boss_room[zzz][1]=stack.get(stack.size()-2).yyy;

    	for (RoomSeed temp:stack) {
    		temp.visited=true;
    	}
    	
    	// Make door to not visited room
    	for (int xx=0;xx<dungeon_size[0];xx++) {
    		for (int yy=0;yy<dungeon_size[1];yy++) {
    			if (!(room_array[xx][yy][zzz].visited)){
    				System.out.println("not visited"+xx+","+yy);
    				for (int i=0;i<4;i++) {
    					if (com.icefill.game.utils.Randomizer.hitInRatio(0.5f)) {
    					switch (i) {
    					case DL:
    						if (yy+1<dungeon_size[1] && !isFinalRoom(xx,yy+1,zzz)) {
    							room_array[xx][yy][zzz].has_door[i]=true;
    						room_array[xx][yy+1][zzz].has_door[UR]=true;
    						}
    						break;
    					case DR:
    						if (xx+1<dungeon_size[0] && !isFinalRoom(xx+1,yy,zzz)) {
    							room_array[xx][yy][zzz].has_door[i]=true;
    						room_array[xx+1][yy][zzz].has_door[UL]=true;
    						}
    						break;
    					case UR:
    						if (0<=yy-1 && !isFinalRoom(xx,yy-1,zzz)) {
    							room_array[xx][yy][zzz].has_door[i]=true;
    						room_array[xx][yy-1][zzz].has_door[DL]=true;
    						}
    						break;
    					case UL:
    						if (0<=xx-1 && !isFinalRoom(xx-1,yy,zzz)) {
    							room_array[xx][yy][zzz].has_door[i]=true;
    						room_array[xx-1][yy][zzz].has_door[DR]=true;
    						}
    						break;
    					}	
    				
    				}
        		}
    			}	
        	}
        	
    	}//for
    	}
    	showDungeonStatus();
    }
    public boolean isBossRoom(int xxx,int yyy, int zzz)
    {
    	if (boss_room[zzz][0]==xxx && boss_room[zzz][1]==yyy)
    		return true;
    	return false;
    }
    public boolean isFinalRoom(int xxx,int yyy, int zzz)
    {
    	if (final_room[zzz][0]==xxx && final_room[zzz][1]==yyy)
    		return true;
    	return false;
    }
    
    public void chooseInitialAndFinalRooms() {
    	int minimum_length=(dungeon_size[0]+1)/2+(dungeon_size[1]+1)/2-1;
    	initial_room= new int[dungeon_size[2]][2];
    	final_room= new int[dungeon_size[2]][2];
    	//initial_room[0][0]=0;
    	//initial_room[0][1]=0;
    	//final_room[0][0]=dungeon_size[0]-1;
    	//final_room[0][1]=dungeon_size[1]-1;
    	
    	for (int i=0;i<dungeon_size[2];i++)
    	{
    		
    		if (i %2 ==0)
    		{
    			initial_room[i][0]=0;
        		initial_room[i][1]=0;
    			final_room[i][0]=dungeon_size[0]-1;
    	    	final_room[i][1]=dungeon_size[1]-1;
    	    	
    		}
    		else
    		{
    			initial_room[i][0]=dungeon_size[0]-1;
    	    	initial_room[i][1]=dungeon_size[1]-1;
    	    	final_room[i][0]=0;
    	    	final_room[i][1]=0;
    	    	
    		}
    	}
       }
    public void showDungeonStatus() {
    	for (int i=0;i<dungeon_size[2];i++){
    		System.out.println("initail_room"+i+":"+initial_room[i][0]+","+initial_room[i][1]);
    		System.out.println("final_room"+i+":"+final_room[i][0]+","+final_room[i][1]);
    	}
    	return ;
    }
    public String toString(){
  	  String to_return="";
  	  to_return+=("Dungeon_size:"+dungeon_size[0]+","+dungeon_size[1]+"\n");
  	  to_return+=("Initial room:"+ initial_room[0]+","+initial_room[1]+"\n");
  	  to_return+=("charn:"+ char_name_list.size()+"\n");
  	  //to_return+=("objn:"+obj_list.size()+"\n");
  	to_return+=("obsn:"+ (undest_obs_info_list.size()+dest_obs_info_list.size())+"\n");
  	  for (int x=0;x<dungeon_size[0];x++)  {
  	 	  for (int y=0;y<dungeon_size[1];y++)  {
  	  		  to_return+=room_array[x][y].toString()+"\n";
  	  	  } 		  
  	  }
  	   return to_return;
  	      
    }
  }
 
  public static class RoomSeed{
	  	int[] room_size;
	  	ArrayList<LightInformation> lights;
	  	int xxx,yyy;
	  	char[][] array;
	    public boolean[] has_door;
	    public boolean visited;
	    public boolean initial_room=false;
	    public RoomShapeType room_shape_type;
	    int room_type;




	    public RoomSeed(int room_xxx,int room_yyy){
			this.xxx=room_xxx;
			this.yyy=room_yyy;
			has_door= new boolean[4];
		}

	    public RoomSeed(RoomShapeType room_type,int room_xxx,int room_yyy,int room_size_xx,int room_size_yy) {
	    	this(room_xxx,room_yyy);
	    	room_size= new int[2];
	    	room_size[0]=room_size_xx;
	    	room_size[1]=room_size_yy;
			this.room_shape_type=room_type;

	    	array= new char[room_size[0]][room_size[1]];
	    }

	    public void makeDownStair() {
	    	int middle_x=room_size[0]/2;
	    	int middle_y=room_size[1]/2;
	    	for (int dx=-1;dx<2;dx++){
	    		for (int dy=-1;dy<2;dy++){
	    			array[middle_x+dx][middle_y+dy]= OBJ.NOTHING.c;
	    		}
	    	}
	    	array[room_size[0]/2][room_size[1]/2-1]=OBJ.NOTHING.c;
	    	array[room_size[0]/2][room_size[1]/2]=OBJ.NOTHING.c;
	    }
	    public void makeUpStair() {
	    	int middle_x=room_size[0]/2;
	    	int middle_y=room_size[1]/2;
	    	for (int dx=-1;dx<2;dx++){
	    		for (int dy=-1;dy<2;dy++){
	    			array[middle_x+dx][middle_y+dy]=OBJ.NOTHING.c;
	    		}
	    	}
	    	array[room_size[0]/2][room_size[1]/2+1]=OBJ.NOTHING.c;
	    }
	    
	    public void checkInitialRoom() {
	    	initial_room=true;
	    }
	    public int getRoomsizeX() {
	    	return room_size[0];
	    }
	    public int getRoomsizeY() {
	    	return room_size[1];
	    }
	    public int getRoomArea() {
	    	return (room_size[0]-1)*(room_size[1]-1);
	    }


	    public void makeWallAndDoor(){
	    	// Make wall
	    	for (int yy=0;yy<room_size[1];yy++) {
	    		for (int xx=0;xx<room_size[0];xx++) {
	    			if (xx==0) array[xx][yy]=OBJ.WALL.c;
	    			else if(xx==room_size[0]-1) array[xx][yy]=OBJ.WALL.c;
	    			else if( yy==0) array[xx][yy]=OBJ.WALL.c;
	    			else if (  yy==room_size[1]-1) {
	    				array[xx][yy]=OBJ.WALL.c;
	    			}

		    	}	
	    	}
	    	// Make door
	    	if (has_door[DL]) array[room_size[0]/2][room_size[1]-1]=OBJ.DOOR.c;
	    	if (has_door[DR]) array[room_size[0]-1][room_size[1]/2]=OBJ.DOOR.c;
	    	if (has_door[UR]) array[room_size[0]/2][0]=OBJ.DOOR.c;
	    	if (has_door[UL]) array[0][room_size[1]/2]=OBJ.DOOR.c;
	    }

	    public void createMonster(int obj_n,int monster_n) {
	    	int x_min=1;
	    	int x_max=room_size[0]-1;
	    	int y_min=1;
	    	int y_max=room_size[1]-1;

	    	if (has_door[0] || initial_room) y_max-=2;
	    	if (has_door[1]) x_max-=2;
	    	if (has_door[2]) y_min+=2; 
	    	if (has_door[3]) x_min+=2;
	    	

	    	int monster_x;
	    	int monster_y;
	    	com.icefill.game.utils.NonRepeatRandomizer randomizer= new com.icefill.game.utils.NonRepeatRandomizer(x_min,x_max,y_min,y_max);
	    	for (int i=0;i<monster_n;i++) {
	    		int rn= randomizer.nextInt();
	    		monster_x=rn/10;
	    		monster_y=rn%10;
	    		array[monster_x][monster_y]=OBJ.MONSTER.c;
	    	}
	    	
	    	//Clearing 
	    	
	    	
	    	
	    }
	    public void clearRoom() {
			for (int y=1;y<room_size[1]-1;y++){
				for (int x=1;x<room_size[0]-1;x++){
					array[x][y]=OBJ.NOTHING.c;
				}
			}
		}
	    public void createBoss() {
	    		array[room_size[0]/2][room_size[1]/2]=OBJ.BOSS_MONSTER.c;
	    }
	    public void makeItemRoom() {
	    	clearRoom();
	    	int middle_x=(int)(room_size[0]/2);
	    	int middle_y=(int)(room_size[1]/2);
	    	array[middle_x][middle_y-1]=OBJ.FIRE_BOWL.c;
	    	array[middle_x][middle_y+1]=OBJ.FIRE_BOWL.c;
	    	array[middle_x][middle_y]=OBJ.WEAPON.c;
	    	array[middle_x-1][middle_y]=OBJ.ITEM.c;
	    }

	    public void makeScrollRoom() {
	    	clearRoom();
	    	int middle_x=(int)(room_size[0]/2);
	    	int middle_y=(int)(room_size[1]/2);
			array[middle_x][middle_y-1]=OBJ.FIRE_BOWL.c;
			array[middle_x][middle_y+1]=OBJ.FIRE_BOWL.c;
			array[middle_x][middle_y]=OBJ.MAGIC_SCROLL.c;
	    	
	    }
	    public void makeInitialRoom() {
			clearRoom();
			int middle_x=(int)((room_size[0]-1)/2);
	    	int middle_y=(int)((room_size[1]-1)/2);
	    	array[middle_x][middle_y]=OBJ.RECRUIT_CAT.c;

	    	
	    }
	    public void makeAngelRoom() {
			clearRoom();
			int middle_x=(int)((room_size[0]-1)/2);
	    	int middle_y=(int)((room_size[1]-1)/2);
	    	//obj_index_array[middle_x][middle_y]=-8;
	    	//obj_index_array[1][1]=-4;
	    	//obj_index_array[1][2]=-4;
	    	//obj_index_array[1][3]=-4;
	    	//obj_index_array[middle_x][middle_y+1]=-7;
	    	//obj_index_array[middle_x][middle_y]=-6;
	    	array[middle_x][middle_y]=OBJ.ANGEL.c;
	    	
	    	//obj_index_array[middle_x-1][middle_y]=-7;
	    	//obj_index_array[middle_x+1][middle_y]=-7;
	    	//device_index_array[middle_x-1][middle_y]=2;
	    	//device_index_array[middle_x+1][middle_y]=2;
	    	//device_index_array[middle_x][middle_y+1]=5;
	    	//device_index_array[middle_x][middle_y-1]=4;
	    	
	    	
	    }
	    public void makeShopRoom() {
	    	int middle_x=(int)((room_size[0]-1)/2);
	    	int middle_y=(int)((room_size[1]-1)/2);
			clearRoom();
	    	array[middle_x][middle_y]=OBJ.SHOP_CAT.c;
	    }
	    public void makemercRoom() {
	    	int middle_x=(int)((room_size[0]-1)/2);
	    	int middle_y=(int)((room_size[1]-1)/2);
			clearRoom();
			array[middle_x][middle_y]=OBJ.RECRUIT_CAT.c;

	    }
	    public void createObstacles(int n_in_room) {
	    	int trap_n= com.icefill.game.utils.Randomizer.nextInt(3);
	    	int x_min=1;
	    	int x_max=room_size[0]-1;
	    	int y_min=1;
	    	int y_max=room_size[1]-1;
	    	if (has_door[0] || initial_room) y_max--;
	    	if (has_door[1]) x_max--;
	    	if (has_door[2]) y_min++; 
	    	if (has_door[3]) x_min++;
	    	com.icefill.game.utils.NonRepeatRandomizer randomizer= new NonRepeatRandomizer(x_min,x_max,y_min,y_max);

	    	for (int i=0;i<n_in_room;i++) {
	    		int rn=randomizer.nextInt();
	    		int monster_x=rn/10;
	    		int monster_y=rn%10;
	    		if (com.icefill.game.utils.Randomizer.hitInRatio(.8f))
	    			array[monster_x][monster_y]=OBJ.UNDEST_OBS.c;
	    		else
					array[monster_x][monster_y]=OBJ.DEST_OBS.c;
	    	}
	    	for (int i=0;i<trap_n;i++) {
	    		int rn= randomizer.nextInt();
	    		int trap_x=rn/10;
	    		int trap_y=rn%10;
	    		array[trap_x][trap_y]=OBJ.TRAP.c;
	    	}
	    	int explosive_n= com.icefill.game.utils.Randomizer.nextInt(2);
	    	for (int i=0;i<explosive_n;i++) {
	    		int rn=randomizer.nextInt();
	    		array[rn/10][rn%10]=OBJ.EXPLOSIVE.c;
	    	}
	    	if (has_door[0] && has_door[2]&& !has_door[1]&& !has_door[3]) {
	    		int xx= com.icefill.game.utils.Randomizer.nextInt(1,room_size[0]-2);//rn.nextInt(room_size[0]-2)+1;
	    		for (int yy=0;yy<room_size[1];yy++){
	    			array[xx][yy]=OBJ.NOTHING.c;
	    		}
	    	}
	    	else if (!has_door[0] && !has_door[2]&& has_door[1]&& has_door[3]) {
	    		int yy= com.icefill.game.utils.Randomizer.nextInt(1,room_size[1]-2);//rn.nextInt(room_size[1]-2)+1;
	    		for (int xx=0;xx<room_size[0];xx++){
					array[xx][yy]=OBJ.NOTHING.c;
	    		}
	    	}

	    	
	    }
	    public void makeHealingRoom() {
	    	int middle_x=(int)(room_size[0]/2);
	    	int middle_y=(int)(room_size[1]/2);
	    	clearRoom();
	    	
	    	//obj_index_array[middle_x][middle_y]=-8;
	    	if (middle_x-2>1)
	    		array[middle_x-2][middle_y]=OBJ.FIRE_BOWL.c;
	    	if (middle_x+2<room_size[0]-2)
	    		array[middle_x+2][middle_y]=OBJ.FIRE_BOWL.c;
	    	if (middle_y-2>1)
	    		array[middle_x][middle_y-2]=OBJ.FIRE_BOWL.c;
	    	if (middle_y+2<room_size[1]-2)
	    		array[middle_x][middle_y+2]=OBJ.FIRE_BOWL.c;
	    
	    	array[middle_x][middle_y]=OBJ.ANGEL.c;
	    	
	    }
	    public String toString() {
	    	String to_return="{\n";
	    	to_return+="room_size:"+room_size[0]+","+room_size[1]+"\n";
		  	to_return+="obj_index:\n";
		  	to_return+="room_n:"+this.xxx+","+this.yyy+"\n";
		  	String arr="";
		  	for (int yy=0;yy<room_size[1];yy++){
		  		for (int xx=0;xx<room_size[0];xx++){
			  		arr+=" "+array[xx][yy];
			  	}
			  	arr+="\n";
		  	}
		  	arr+="\n\n";
		    return to_return+array;
	    }
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