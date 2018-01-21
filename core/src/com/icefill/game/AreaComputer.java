package com.icefill.game;

import java.util.LinkedList;

import com.badlogic.gdx.utils.Pool;
import com.icefill.game.actors.AreaCell;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.RoomGroup;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.utils.Randomizer;


public class AreaComputer {
	
	  private static LinkedList<AreaCell> area_list;
	  private static LinkedList<AreaCell> target_path;
	  private static LinkedList<AreaCell> target_list;
	  private static AreaCell target_center;
	  
	

	  public AreaComputer()
	  {
		  area_list= new LinkedList<AreaCell>();
		  target_path= new LinkedList<AreaCell>();
		  target_list= new LinkedList<AreaCell>();
		  target_center=null;
	  }
	  public LinkedList<AreaCell> computeArea(AbilityActor ability,AreaCell area_origin) {
		  switch (ability.targeting_type) {
		  	case 0:
		  		return computeWalkRange(ability.self_targeting, ability.center_contain, ability.targeting_min_range, ability.targeting_max_range, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block);
		  	case 1:
		  		return computeStraightRange(ability.self_targeting, ability.center_contain,ability.targeting_min_range , ability.targeting_max_range, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block,ability.wall_block);
		  	default:
		  		return computeWalkRange(ability.self_targeting, ability.center_contain, ability.targeting_min_range, ability.targeting_max_range, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block);
		  }
		  
	  }
	  public LinkedList<AreaCell> computeArea(AbilityActor ability,ObjActor caster) {
		  AreaCell area_origin=Global.getCurrentRoom().getMap().getCell(caster.getXX(), caster.getYY());
		  return computeArea(ability,area_origin);
	  }
	  
	  public LinkedList<AreaCell> computeTarget(AbilityActor ability,AreaCell target_origin,ObjActor caster) {
		  clearTargetList();
		  switch (ability.splash_type) {
		  	case 0:
		  		return computeWalkTarget(ability.self_targeting, ability.splash_center_contain, ability.splash_min_range, ability.splash_max_range, target_origin, Global.getCurrentRoom().getObj(target_origin), ability.splash_empty_contain, ability.splash_friendly_contain,ability.splash_enemy_contain, ability.splash_obj_block);
		  	case 2:
		  		return computeStraightFromCasterTarget(ability.self_targeting, ability.splash_center_contain, ability.splash_min_range, ability.splash_max_range, target_origin, caster, ability.splash_empty_contain, ability.splash_friendly_contain,ability.splash_enemy_contain, ability.splash_obj_block);
		  	case 3:
		  		return computeChainTarget(ability.self_targeting, ability.splash_center_contain, ability.splash_min_range, ability.splash_max_range, target_origin, caster, ability.splash_empty_contain, ability.splash_friendly_contain,ability.splash_enemy_contain, ability.splash_obj_block);
		  	default:
				return computeWalkTarget(ability.self_targeting, ability.splash_center_contain, ability.splash_min_range, ability.splash_max_range, target_origin, Global.getCurrentRoom().getObj(target_origin), ability.splash_empty_contain, ability.splash_friendly_contain,ability.splash_enemy_contain, ability.splash_obj_block);
		  }
	  }
	  public LinkedList<AreaCell> computeTarget(AbilityActor ability,ObjActor caster) {
		  AreaCell area_origin=Global.getCurrentRoom().getMap().getCell(caster.getXX(), caster.getYY());
		  return computeTarget(ability,area_origin,caster);
	  }
	 

	  public LinkedList<AreaCell> computeInspectionArea(AreaCell area_origin){
		  return computeWalkRange(false, false, 0, 400, area_origin, Global.getCurrentRoom().getObj(area_origin), true, false, false, true);
	  }
	  public LinkedList<AreaCell> computeInspectionArea(ObjActor obj) {
		  return computeInspectionArea(obj,Global.getCurrentRoom().getCell(obj));
	  }
	  public LinkedList<AreaCell> computeInspectionArea(ObjActor obj,AreaCell area_origin) {
		  AbilityActor ability= (AbilityActor)obj.getMoveAction().action;
		  //AreaCell area_origin=Global.getCurrentRoom().getMap().getCell(obj.getXX(), obj.getYY());
		  switch (ability.targeting_type) {
		  	case 0:
		  		return computeWalkRange(ability.self_targeting, ability.center_contain, ability.targeting_min_range, 400, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block);
		  	case 1:
		  		return computeStraightRange(ability.self_targeting, ability.center_contain,ability.targeting_min_range , 400, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block,ability.wall_block);
		  	default:
		  		return computeWalkRange(ability.self_targeting, ability.center_contain, ability.targeting_min_range, 400, area_origin, Global.getCurrentRoom().getObj(area_origin), ability.target_empty_contain, ability.friendly_contain, ability.enemy_contain, ability.obj_block);
		  }
		  
		 // return computeInspectionArea(area_origin);
	  }
	  public AreaCell getTargetCenter() {
		  if (target_center==null) target_center=getTargetList().getFirst();
		  return target_center;
	  }
	  public void setTargetCenter(AreaCell temp) {
		  target_center=temp;
	  }
	  public LinkedList<AreaCell> computeStraightFromCasterTarget
	  (boolean self_targeting,boolean center_targeting,
			   int min_range,int max_range,
			   AreaCell area_origin,ObjActor caster,
			   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain, 
			   boolean obj_block) {
		  //Initialize some variables;
		  LinkedList<AreaCell> to_return = new LinkedList<AreaCell>();
		  AreaCell caster_cell=Global.getCurrentRoom().getMap().getCell(caster.getXX(), caster.getYY());
		  
		  LinkedList<AreaCell> temp_queue = new LinkedList<AreaCell>();
		//Decide whether contains center(origin)
		  AreaCell  cell_to_decide= area_origin;
		  int direction_xx= (cell_to_decide.getXX()- caster_cell.getXX());
		  if (direction_xx!=0)
			  direction_xx/=Math.abs(direction_xx);
		  int direction_yy= (cell_to_decide.getYY()- caster_cell.getYY());
		  if (direction_yy!=0)
			  direction_yy/=Math.abs(direction_yy);
		  int xx_to_decide=caster_cell.getXX();
		  int yy_to_decide=caster_cell.getYY();
		  if (xx_to_decide ==0 && yy_to_decide ==0) return to_return;
		  int i=0;
		  int range_counter=0;
		  while (true){
			  range_counter++;
			  xx_to_decide+=direction_xx;
			  yy_to_decide+=direction_yy;
			  if (Global.getCurrentRoom().getMap().isMovableAndNoDeviceFloor(xx_to_decide, yy_to_decide)) {
				  ObjActor obj_to_decide=Global.getCurrentRoom().getObj(xx_to_decide,yy_to_decide);
				  cell_to_decide=Global.getCurrentRoom().getMap().getCell(xx_to_decide, yy_to_decide);
				   if (obj_to_decide !=null)
				   {
					   if (obj_to_decide.getType().equals("wall")) break;
					   
					   if (obj_block)
					   {
						  break;
					   }
				  }
				   Boolean contain= isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain);
				   if (contain)
				   {
					  cell_to_decide.is_target=true;
					  to_return.add(cell_to_decide);
				   }
				  //if (area_origin.getXX()== xx_to_decide && area_origin.getYY()== yy_to_decide) break;
					  
			  }
			  else break;
			  if (range_counter>=max_range) break;
			  
		  }
		    return to_return;
		  
	  }
	  public LinkedList<AreaCell> computeChainTarget
	 (boolean self_targeting,boolean center_targeting,
			   int min_range,int max_range,
			   AreaCell area_origin,ObjActor caster,
			   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain, 
			   boolean obj_block) {
				  //Initialize some variables;
				  LinkedList<AreaCell> to_return = new LinkedList<AreaCell>();
				  if (max_range==0) {
					  to_return.add(area_origin)
					  ;area_origin.is_target=true;
					  return to_return;}
				  LinkedList<AreaCell> temp_list = new LinkedList<AreaCell>();
				  //int area_length = 2 * max_range + 1;
				  //boolean[][] checked = new boolean[area_length][area_length];
				  //int[][] neighbor_index = new int[4][2];
				  //neighbor_index[0][0] = -1; neighbor_index[0][1] = 0;
				 // neighbor_index[1][0] = 0; neighbor_index[1][1] = 1;
				 // neighbor_index[2][0] = 1; neighbor_index[2][1] = 0;
				 // neighbor_index[3][0] = 0; neighbor_index[3][1] = -1;
				  int index_xx=area_origin.getXX();
				  int index_yy=area_origin.getYY();
				  
				  //Decide whether contains center(origin)
				  AreaCell  cell_to_decide= area_origin;
				  ObjActor obj_to_decide=Global.getCurrentRoom().getObj(cell_to_decide);
				  if (center_targeting)// &&
					  //isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain))
				  {
					  cell_to_decide.is_target = true;
					  to_return.add(cell_to_decide);
					  
					  for (int i=0;i<max_range;i++) {
						  
						  for (int xx=-1;xx<2;xx++) {
							  for (int yy=-1;yy<2;yy++) {
								  int temp_xx=index_xx+xx;
								  int temp_yy=index_yy+yy;
								  
								  if (Global.getCurrentRoom().getMap().isInFloor(temp_xx,temp_yy) && (temp_xx!=index_xx || temp_yy!=index_yy)) {
									  AreaCell near_cell= Global.getCurrentRoom().getMap().getCell(temp_xx, temp_yy);
									  ObjActor near_obj= Global.getCurrentRoom().getObj(near_cell);
									  if (isTarget(near_obj,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain)
										  && (near_obj!=null) && !(near_obj.getType().equals("wall"))	  
											  ) {
										  temp_list.add(near_cell);
									  }
								  }
							  }
						  }
						  if (temp_list.isEmpty()) break;
						  else {
							  AreaCell next_cell=temp_list.get(Randomizer.nextInt(temp_list.size()));
							 to_return.add(next_cell);
							 index_xx=next_cell.getXX();
							 index_yy=next_cell.getYY();
						  }
						  temp_list.clear();
					  }
				  }
				  			  return to_return;
			  }

	   public LinkedList<AreaCell> computeWalkTarget
	  (boolean self_targeting,boolean center_targeting,
			   int min_range,int max_range,
			   AreaCell area_origin,ObjActor caster,
			   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain, 
			   boolean obj_block) {
				  //Initialize some variables;
				  LinkedList<AreaCell> to_return = new LinkedList<AreaCell>();
				  if (max_range==0) {
					  to_return.add(area_origin)
					  ;area_origin.is_target=true;
					  return to_return;}
				  LinkedList<AreaCell> temp_queue = new LinkedList<AreaCell>();
				  int area_length = 2 * max_range + 1;
				  boolean[][] checked = new boolean[area_length][area_length];
				  int[][] neighbor_index = new int[4][2];
				  neighbor_index[0][0] = -1; neighbor_index[0][1] = 0;
				  neighbor_index[1][0] = 0; neighbor_index[1][1] = 1;
				  neighbor_index[2][0] = 1; neighbor_index[2][1] = 0;
				  neighbor_index[3][0] = 0; neighbor_index[3][1] = -1;
				  int index_x=max_range;
				  int index_y=max_range;
				  
				  //Decide whether contains center(origin)
				  AreaCell  cell_to_decide= area_origin;
				  ObjActor obj_to_decide=Global.getCurrentRoom().getObj(cell_to_decide);
				  if (center_targeting &&
					  isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain))
				  {
					  cell_to_decide.is_target = true;
					  to_return.add(cell_to_decide);
				  }
				  checked[index_x][index_y]=true;
				  
				  while (true){
					  if (((obj_to_decide==null) || !obj_to_decide.getType().equals("wall")))
					  if (cell_to_decide.area_count < max_range) {
						  index_x = cell_to_decide.getXX() - area_origin.getXX() + max_range;
						  index_y = cell_to_decide.getYY() - area_origin.getYY() + max_range;
						  
						  //Decide whether put in temp_queue
						  
						  for (int i = 0; i < 4; i++) {
							  if (checked[(index_x + neighbor_index[i][0])][(index_y + neighbor_index[i][1])] == false) {
								  if (Global.getCurrentRoom().getMap().isMovableAndNoDeviceFloor(cell_to_decide.getXX() + neighbor_index[i][0], cell_to_decide.getYY() + neighbor_index[i][1])){
									  AreaCell temp_cell = Global.getCurrentRoom().getMap().getCell(cell_to_decide.getXX() + neighbor_index[i][0], cell_to_decide.getYY() + neighbor_index[i][1]);
									  ObjActor temp_obj=Global.getCurrentRoom().getObj(temp_cell);
									  if (((!obj_block) || (temp_obj == null)) ) {
										  temp_queue.add(temp_cell);
										  temp_cell.area_count=cell_to_decide.area_count+1;  
									  }
								  }
								  checked[(index_x + neighbor_index[i][0])][(index_y + neighbor_index[i][1])] = true;
							  }
						  }
						  
					  }
					//Decide is target
					  if (temp_queue.isEmpty()) {
						  break;
					  }
					  cell_to_decide = temp_queue.pollFirst();
					  obj_to_decide = Global.getCurrentRoom().getObj(cell_to_decide);
					  Boolean contain= isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain);
					  if (contain &&cell_to_decide.area_count >= min_range ) {
						  cell_to_decide.is_target = true;
						  to_return.add(cell_to_decide);
					  }
					  
			    }
				  return to_return;
			  }

	  public LinkedList<AreaCell> computeWalkRange
	  (boolean self_targeting,boolean center_targeting,
	   int min_range,int max_range,
	   AreaCell area_origin,ObjActor caster,
	   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain, 
	   boolean obj_block) {
		  //Initialize some variables;
		  LinkedList<AreaCell> to_return = new LinkedList<AreaCell>();
		  LinkedList<AreaCell> temp_queue = new LinkedList<AreaCell>();
		  int area_length = 2 * max_range + 1;
		  boolean[][] checked = new boolean[area_length][area_length];
		  int[][] neighbor_index = new int[4][2];
		  neighbor_index[0][0] = -1; neighbor_index[0][1] = 0;
		  neighbor_index[1][0] = 0; neighbor_index[1][1] = 1;
		  neighbor_index[2][0] = 1; neighbor_index[2][1] = 0;
		  neighbor_index[3][0] = 0; neighbor_index[3][1] = -1;
		  int index_x=max_range;
		  int index_y=max_range;
		  
		  
		  clearAreaList();
		  //Decide whether contains center(origin)
		  AreaCell  cell_to_decide= area_origin;
		  cell_to_decide.area_count=0;
		  ObjActor obj_to_decide=Global.getCurrentRoom().getObj(cell_to_decide);
		  if (center_targeting &&
			  isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain))
		  {
			  cell_to_decide.is_in_range = true;
			  to_return.add(cell_to_decide);
		  }
		  checked[index_x][index_y]=true;
		  
		  while (true){
			  if (cell_to_decide.area_count < max_range) {
				  index_x = cell_to_decide.getXX() - area_origin.getXX() + max_range;
				  index_y = cell_to_decide.getYY() - area_origin.getYY() + max_range;
				  
				  //Decide whether put in temp_queue
				  for (int i = 0; i < 4; i++) {
					  if (checked[(index_x + neighbor_index[i][0])][(index_y + neighbor_index[i][1])] == false) {
						  if (Global.getCurrentRoom().getMap().isMovableAndNoDeviceFloor(cell_to_decide.getXX() + neighbor_index[i][0], cell_to_decide.getYY() + neighbor_index[i][1])){
							  AreaCell temp_cell = Global.getCurrentRoom().getMap().getCell(cell_to_decide.getXX() + neighbor_index[i][0], cell_to_decide.getYY() + neighbor_index[i][1]);
							  ObjActor temp_obj=Global.getCurrentRoom().getObj(temp_cell);
							  if ((!obj_block) || (temp_obj == null || temp_obj.getTeam()== Global.getSelectedObj().getTeam())){
								  temp_queue.add(temp_cell);
								  temp_cell.parent = cell_to_decide;
								  temp_cell.area_count=cell_to_decide.area_count+1; 
							  }
						  }
						  checked[(index_x + neighbor_index[i][0])][(index_y + neighbor_index[i][1])] = true;
					  }
				  }
				  
			  }
			//Decide is target
			  if (temp_queue.isEmpty()) {
				  break;
			  }
			  cell_to_decide = temp_queue.pollFirst();
			  obj_to_decide = Global.getCurrentRoom().getObj(cell_to_decide);
			  Boolean contain= isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain);
			  if (contain && cell_to_decide.area_count >= min_range ) {
				  cell_to_decide.is_in_range = true;
				  to_return.add(cell_to_decide);
			  }
			  
	    }
		  area_origin.parent=null;
		  return to_return;
	  }
	  public LinkedList<AreaCell> computeStraightRange
	  (boolean self_targeting,boolean center_targeting,
	   int min_range,int max_range,
	   AreaCell area_origin,ObjActor caster,
	   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain, 
	   boolean obj_block,boolean wall_block) {
		  //Initialize some variables;
		  LinkedList<AreaCell> to_return = new LinkedList<AreaCell>();
		  LinkedList<AreaCell> temp_queue = new LinkedList<AreaCell>();
		  int[][] neighbor_index = new int[8][2];
		  neighbor_index[0][0] = -1; neighbor_index[0][1] = 0;
		  
		  neighbor_index[1][0] = 0; neighbor_index[1][1] = 1;
		  
		  neighbor_index[2][0] = 1; neighbor_index[2][1] = 0;
		  
		  neighbor_index[3][0] = 0; neighbor_index[3][1] = -1;
		  
		  neighbor_index[4][0] = -1; neighbor_index[4][1] = -1;
		  neighbor_index[5][0] = -1; neighbor_index[5][1] = 1;
		  neighbor_index[6][0] = 1; neighbor_index[6][1] = -1;
		  neighbor_index[7][0] = 1; neighbor_index[7][1] = 1;
		  
		  //Decide whether contains center(origin)
		  AreaCell  cell_to_decide= area_origin;
		  ObjActor obj_to_decide=Global.getCurrentRoom().getObj(cell_to_decide);
		  if (center_targeting &&
			  isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain)
			  && cell_to_decide.area_count >= min_range )
		  {
			  cell_to_decide.is_in_range = true;
			  to_return.add(cell_to_decide);
		  }
		  for (int i=0;i<4;i++) {
			    int adder_x=0,adder_y=0;
			    int index_x=area_origin.getXX();
			    int index_y=area_origin.getYY();
			    int range_counter=0;
			    while(true) {
			    	range_counter++;
			    	AreaCell parent=Global.getCurrentRoom().getMap().getCell(index_x,index_y);
			    	index_x=index_x+neighbor_index[i][0];
			    	index_y=index_y+neighbor_index[i][1];
			    	if (!Global.getCurrentRoom().getMap().isMovableAndNoDeviceFloor(index_x,index_y)) break	;
			    	
			    	cell_to_decide= Global.getCurrentRoom().getMap().getCell(index_x, index_y);
			    	cell_to_decide.area_count=parent.area_count+1;
			    	
			    	if (range_counter>max_range) break;
			    	obj_to_decide=Global.getCurrentRoom().getObj(cell_to_decide);
			    	
			    	Boolean contain= isTarget(obj_to_decide,self_targeting,caster,empty_area_contain,friendly_contain,enemy_contain);
			    	
			    	
		    		if (contain&&range_counter>= min_range ) {
		    			cell_to_decide.is_in_range=true;
		    			to_return.add(cell_to_decide);
		    		}
		    		if ((obj_to_decide != null)) {
		    			if (wall_block && obj_to_decide.getType().equals("wall")){
		    				break;
		    			}
		    			
			    	}
			    	
		    		if (obj_block &&(obj_to_decide != null)) {
		    			break;
		    			
			    	}
		    		
		    		
			    }
		  }
			      return to_return;
	  }
	  
	  
	  private boolean isTarget(ObjActor obj_to_decide,boolean self_targeting,ObjActor caster,
			   boolean empty_area_contain, boolean friendly_contain,boolean enemy_contain)
	  {
			  if (obj_to_decide == null) {// areaCell have no obj
				  if (empty_area_contain) {
					  return  true;
				  }
				  else return false;
			  }// areaCell have obj
			  else if (caster == null) return true;
			  else if ((obj_to_decide== caster) && self_targeting) {
				  return true;}
			  else if ( 
					  ((caster.getTeam() == obj_to_decide.getTeam()) && (friendly_contain)) || 
					  ((caster.getTeam() != obj_to_decide.getTeam()) && (enemy_contain))
					  ) {
				  return true;
			  }
			  else return false;
	  }
	  
		public void setAreaList(LinkedList<AreaCell> to_add) {
		 	if (area_list==null) area_list= new LinkedList<AreaCell>();
	    	else clearAreaList();
	 
			area_list.addAll(to_add);
			clearAreaCount();
		}
		public void setAreaListWithoutClearCount(LinkedList<AreaCell> to_add) {
		 	if (area_list==null) area_list= new LinkedList<AreaCell>();
	    	else clearAreaList();
	 
			area_list.addAll(to_add);
		}

		public void clearAreaList() {
			if (area_list!= null) {
			for (AreaCell temp:area_list) {
				temp.clear();
			}
			area_list.clear();
			}
		}
		public void clearAreaCount() {
			for (AreaCell temp:area_list) {
				temp.clearAreaCount();
			}
		}
		public boolean isInArea(int xx,int yy) {
			for (AreaCell temp:area_list) {
				if (xx== temp.getXX() && yy== temp.getYY())
					return true;
			}
			return false;
		}
		public LinkedList<AreaCell> getAreaList() {
			return area_list;
		}
		
		
	    public void setTargetPath(AreaCell target) {
	    	if (target_path==null) target_path= new LinkedList<AreaCell>();
	    	else target_path.clear();
	    	AreaCell temp = target;
	    	int i=0;
	    	do {
	    	    target_path.add(temp);
	    	    temp = temp.parent;
	    	    i++;
	    	    if (i>2000) break;
	    	}while (temp != null );
	    	target_path.pollLast();
	    }
	    public void setTargetPath(AbilityActor action,AreaCell target)
	    {
	    	setTargetList(computeTarget(action,target,Global.getSelectedObj()));
		    setTargetPath(getTargetList().getLast());
		    clearAreaList();
	    }
	    
	    public int getWalkDistance(AreaCell target) {
	    	if (target==null) return 0;
	    	if (area_list==null) return 0;
	    	AreaCell temp = target;
	    	int i=0;
	    	do {
	    	    target_path.add(temp);
	    	    temp = temp.parent;
	    	    i++;
	    	    if (i>2000) break;
	    	}while (temp != null );
	    	return i;
	    }
	    
	    public LinkedList<AreaCell>  getTargetPath() {
	    	return target_path;
	    }	
	   
		public void setTargetList(LinkedList<AreaCell> list_to_add) {
			/*
			if (target_list == null) target_list= new LinkedList<AreaCell>();
			else{target_list.clear();}
			
			target_list.addAll(list_to_add);
			*/
			target_list=list_to_add;
		}

		
		public void clearTargetList() {
			if (target_list != null) {
				for (AreaCell temp:target_list) {
					temp.clearTarget();
					ObjActor temp_target=Global.getCurrentRoom().getObj(temp);
					if (temp_target!=null)
						temp_target.removeTargetInfo();
				}
				target_list.clear();
			}
		}
		public LinkedList<AreaCell> getTargetList() {
			return target_list;
		}
		
		
		public boolean isTargetDone(RoomGroup obj_group) {
			boolean to_return=true;
			for (AreaCell temp:this.target_list) {
				if (obj_group.getObj(temp.getXX(), temp.getYY()) != null && obj_group.getObj(temp.getXX(), temp.getYY()).isActing() == true){
					to_return= false;
					break;
				}
				
			}
			return to_return;
		}
		
		public class CellListElt implements Pool.Poolable{
			private AreaCell cell;
			private CellListElt parent;
			
			public CellListElt(AreaCell cell)
			{
				this.cell=cell;
				parent=null;
			}
			public AreaCell cell(){
				return cell;
			}
			public CellListElt parent()
			{
				return parent;
			}
			@Override
			public void reset()
			{
				cell=null;
				parent=null;
			}
			
		}


}
