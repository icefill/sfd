package com.icefill.game.actors.dungeon;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.icefill.game.*;
import com.icefill.game.actors.CursorActor;
import com.icefill.game.actors.windows.MinimapActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.screens.BasicScreen;

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
  DIR from_dir=DIR.DL;
  DIR to_dir=DIR.DL;

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


  public Team[] team_lists;

  //Destination
  int final_room[][];
  int initial_room[][];
  //position of current room initiate with -1
  public int room_xxx=-1;
  public int room_yyy=-1;
  public int room_zzz=-1;

  public AreaComputer area_computer;

  GFSM fsm;

  public MapActor current_map;
  private RoomGroup current_room;
  public RoomGroup rooms[][][];
  private ObjActor selected_obj;
  private ObjActor attacker;
  ActionActor selected_action;
  public BasicScreen screen;

  // MAna
  //public int mana=5;

  public DungeonGroup(DungeonSeed dungeon_factory,BasicScreen gameScreen)
  {
	  this.screen=gameScreen;
	  initialize(dungeon_factory);
  }

  private void initialize(DungeonSeed dungeon_seed)
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

    this.fsm = new GFSM(this);
    Global.gfs=fsm;
    Global.dungeon=this;

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

    //Set BoosroomDoor
    for (int zzz=0;zzz<dungeon_size[2];zzz++)
    {
    	setBossRoomDoor(boss_room[zzz][0],boss_room[zzz][1],zzz);
    }

    setRoom(dungeon_seed.initial_room[0][0],dungeon_seed.initial_room[0][1],0);

    addListener(new InputListener() {
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

	  area_computer.clearTargetList();
	  removeRoom();
	  // decide from door
	  if (room_xxx==-1) //initial
		  {from_dir=DIR.AB;to_dir=DIR.BL;}
	  else if (x-room_xxx>0) {from_dir=DIR.UL;to_dir=DIR.DR;}
	  else if (x-room_xxx<0) {from_dir=DIR.DR;to_dir=DIR.UL;}
	  else if (y-room_yyy>0) {from_dir=DIR.UR;to_dir=DIR.DL;}
	  else {from_dir=DIR.DL;to_dir=DIR.UR;}

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
	  cursor.setMapPosition(0, 0);
	  cursor.moveTo(Global.getStage().getWidth()*.5f, Global.getStage().getHeight()*.5f);
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
	public void revealNearRoom()
	{
		int xxx=this.getCurrentRoom().room_xx;
		int yyy=this.getCurrentRoom().room_yy;
		int zzz=this.getCurrentRoom().room_zz;
		for (int dir=0;dir<4;dir++) {
			RoomGroup room = getAdjacentRoom(xxx, yyy, zzz, DIR.toDIR(dir));
			if (room!=null) room.visited=true;
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
	  		//Global.showMessage("Please Wait until acting is over.",1);
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
	  if (this.getSelectedObj() != null) {
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
	public Team getTeam(int i) {
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
	public void setBossRoomDoor(int xxx,int yyy, int zzz) {
		RoomGroup room=rooms[xxx][yyy][zzz];
		if (room!=null) {
			for (int j=0;j<4;j++) {
				if (room.map.getDoor(j)!=null) {
					room.map.getDoor(j).setBossDoor();
				}
			}
		}
		for (int i=0;i<4;i++) {
			room=getAdjacentRoom(xxx,yyy,zzz,DIR.toDIR(i));
			if (room!=null)	{
				int j= (i+2) %4;
					if (room.map.getDoor(j)!=null)	{
						room.map.getDoor(j).setBossDoor();
					}
			}
		}
	}
	public RoomGroup getAdjacentRoom(int xxx,int yyy, int zzz,DIR dir) {
		switch (dir) {
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
			&&(0<=yyy && yyy< dungeon_size[1] )	) {
			return rooms[xxx][yyy][zzz];
		}
		else
			return null;
	}


		public void showTargetsAccuracyAndDamage(ActionContainer action) {
		if (((AbilityActor)action.action).splash_type!=3)
		for (AreaCell target_cell:area_computer.getTargetList()){
				ObjActor target=current_room.getObj(target_cell);
				if (target!= null) {
					StatusTuple status_change=((AbilityActor)action.action).getFirstStatusTuple();
					if (status_change!=null){
						String frb_string=getSelectedObj().getTargetFacing(target).toString();
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

	public void enableClick(boolean flag) {
		enable_click=flag;
		screen.clicked=false;
	}
	public Boolean isMouseClicked() {
		if (enable_click &&screen.clicked==true ) {
			screen.clicked=false;
			return true;
		}
		else {
			return false;
		}
	}

   public GFSM getGFSM() {
	   return fsm;
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
    public String file;
    public String job;
    public String control;
    public int level;
    public int team;
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