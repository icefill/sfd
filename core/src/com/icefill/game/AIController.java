package com.icefill.game;



import java.util.LinkedList;
import java.util.Stack;
import com.icefill.game.actors.AreaCell;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;


public class AIController {
		  LinkedList<ObjActor> ai_list;
		  AreaCell target;
		  AreaCell temp_target;
		  AreaComputer area_computer;
		  
		  
		  int phase;
		  
		  public AIController(AreaComputer ac) {
			  ai_list= new LinkedList<ObjActor>();
			  this.area_computer=ac;
		  }
		  
		  public void clearAI(){
			  ai_list.clear();
			  target=null;
		  }
		  
		  public float normalize(float value)
		  {
			  return (value)/(1.0f+value);
		  }
		  public float invNormalize(float value)
		  {
			  return 1f/(1.0f+value);
		  }
		  
		  public float expNormalize(float value)
		  {
			  float exp_value= (float)Math.exp(value);
			  return (exp_value)/(1.0f+exp_value);
		  }
		  
		  public LinkedList<ObjActor> getOpponentTeam(ObjActor obj)
		  {
			  return Global.dungeon.getTeamList(getOpponentTeamIndex(obj));
		  }
		  public LinkedList<ObjActor> getTeam(ObjActor obj)
		  {
			  return Global.dungeon.getTeamList(obj.getTeam());
		  }
		  public int getOpponentTeamIndex(ObjActor obj)
		  {
			  return (obj.getTeam()==0) ? 1 : 0;
		  }
		  
		  private int findNearstEnemy(LinkedList<ObjActor> opponent_team_list) {
			  int index_to_return=0;
			  int current_min_distance=100;
			  for (int i=0;i<opponent_team_list.size();i++) {
				  ObjActor temp=opponent_team_list.get(i);
				  int distance=temp.getOneDistance(Global.getSelectedObj()); 
				  if (distance<= current_min_distance) {
					  current_min_distance=distance;
					  index_to_return=i;
				  }	
				  
			  }
			  
			  return index_to_return;
			  
		  }
		  
		  private int computeMaxAttackRange()
		  {
				LinkedList<ActionContainer> action_list=Global.getSelectedObj().getActionList();
				int to_return=0;
				  for (ActionContainer action:action_list)
				  {
					  if (action.action instanceof AbilityActor 
							  && ((AbilityActor)action.action).ai_type.equals("ranged")
						  && ((AbilityActor)action.action).targeting_max_range>to_return)
						  {
							  to_return=((AbilityActor)action.action).targeting_max_range;
						  }
				  }
				return to_return;
		  }
		  
		  private float computeAttackWeight(AreaCell temp,int attack_range)
		  {	  
			  float to_return=Float.MAX_VALUE;
			  for (ObjActor obj:getOpponentTeam(Global.getSelectedObj()))
			  {
				 to_return= Math.min(obj.getOneDistance(temp),to_return);
			  }
			  
			  return invNormalize(Math.abs(to_return-attack_range));
		  }
		  private float computeAssistWeight(ObjActor selected_obj,AreaCell temp)
		  {	  
			  float to_return=0;
			  
			  for (ObjActor obj:getTeam(selected_obj))
			  {
				  if (obj!=selected_obj && obj.status.getCurrentHP()>0)
					  to_return+=obj.getOneDistance(temp);
			  }
			  
			  float to_return2=Float.MAX_VALUE;
			  for (ObjActor obj:getOpponentTeam(Global.getSelectedObj()))
			  {
				 to_return2= Math.min(obj.getOneDistance(temp),to_return2);
			  }
			  
			  
			  return .5f*(invNormalize(Math.abs(to_return))+invNormalize(Math.abs(to_return2-3)));
		  }
		  private AreaCell backtrackingUntilInRange(AreaCell enemy_cell,int max_range)
		  {
  		  		while(  enemy_cell!=null &&
  		  				(
  		  						(enemy_cell.area_count>max_range)
  		  						|| Global.getCurrentRoom().getObj(enemy_cell)!=null
  		  						|| enemy_cell.isDangerous()
  		  				) 
  		  			)
  		  		{
  		  			enemy_cell=enemy_cell.parent;
  		  		}
	  		  		
	  		  	
	  		  	return enemy_cell;
		  }
		  private AreaCell getMeleeMovePosition(ObjActor selected_obj,AreaCell area_origin){
			  
			  if (!selected_obj.isAvailableAction(selected_obj.getMoveAction()))
			  	{
			  		return null;
			  	}
			  	AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  	
			  	selected_obj.selectAction(selected_obj.getMoveAction());
			  	AreaCell enemy_cell=null;
	  		  	int opponent_team_index=getOpponentTeamIndex(selected_obj);
	  		  	
			  	area_computer.setAreaListWithoutClearCount(area_computer.computeInspectionArea(selected_obj,area_origin));
	  		  	
	  		  	//Find approachable position such that have enemy near itself
	  		  	//The reason using reversed for loop is last entry in the list is nearst cell to the origin 
	  		  	for (int i=area_computer.getAreaList().size()-1;i>=0;i--)	
	  		    {
	  		  		AreaCell temp_cell= area_computer.getAreaList().get(i);
	  		  		for (int dir=0;dir<4;dir++) {
	  		  			ObjActor temp_obj= Global.getCurrentRoom().getNearObj(temp_cell,dir);
	  		  			if (temp_obj!=null && temp_obj.getTeam()==opponent_team_index) {
	  		  				if (!temp_cell.isDangerous())
	  		  				{
	  		  					enemy_cell=temp_cell;
	  		  					break;
	  		  				}
	  		  			}
	  		  		}
	  		  		
	  		  	}
	  		  	
	  		  	// If found one, find movable position(Should be in maximum move range) by backtracking.
	  		  enemy_cell=backtrackingUntilInRange(enemy_cell,action.targeting_max_range);

	  		  area_computer.clearAreaCount();
	  		  area_computer.clearAreaList();
	  		  return enemy_cell;
		  }

		  private AreaCell getRangedMovePosition(ObjActor selected_obj){
			  if (!selected_obj.isAvailableAction(selected_obj.getMoveAction()))
			  	{
			  		return null;
			  	}
			  
			  	AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  	selected_obj.selectAction(selected_obj.getMoveAction());
			  	
			  	
			  	
			  	int max_attack_range= Math.min(computeMaxAttackRange(),4);

			  	area_computer.setAreaListWithoutClearCount(area_computer.computeArea(action,selected_obj));
	  		  	AreaCell enemy_cell=Global.getCurrentRoom().getCell(selected_obj);
	  		    float enemy_weight=computeAttackWeight(enemy_cell,max_attack_range);
	  		    
	  		  	LinkedList<AreaCell> area_list= area_computer.getAreaList();
	  		  	for (AreaCell temp:area_list)
	  		  	{
	  		  		float temp_weight=computeAttackWeight(temp,max_attack_range);
	  		  		if (temp_weight>enemy_weight)
	  		  		{
	  		  			enemy_cell=temp;
	  		  			enemy_weight=temp_weight;
	  		  		}		
	  		  	}
	  		  	
	  		  //enemy_cell=backtrackingUntilInRange(enemy_cell,action.targeting_max_range);

	  		  	
	  		  area_computer.clearAreaCount();
	  		  area_computer.clearAreaList();
	  		  return enemy_cell;
		  }
		  private AreaCell getRunawayMovePosition(ObjActor selected_obj)
		  {
			  if (!selected_obj.isAvailableAction(selected_obj.getMoveAction()))
			  	{
			  		return null;
			  	}

			  	AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  	selected_obj.selectAction(selected_obj.getMoveAction());
			  	Global.showBigMessage(selected_obj.getJob().job_name+" is fleeing");
			  	
			  	area_computer.setAreaListWithoutClearCount(area_computer.computeArea(action,selected_obj));
	  		  	AreaCell enemy_cell=Global.getCurrentRoom().getCell(selected_obj);
	  		    float enemy_weight=computeAttackWeight(enemy_cell,5);
	  		    
	  		  	LinkedList<AreaCell> area_list= area_computer.getAreaList();
	  		  	for (AreaCell temp:area_list)
	  		  	{
	  		  		float temp_weight=computeAttackWeight(temp,4);
	  		  		if (temp_weight>enemy_weight)
	  		  		{
	  		  			enemy_cell=temp;
	  		  			enemy_weight=temp_weight;
	  		  		}		
	  		  	}
	  		  	
	  		  //enemy_cell=backtrackingUntilInRange(enemy_cell,action.targeting_max_range);

	  		  	
	  		  area_computer.clearAreaCount();
	  		  area_computer.clearAreaList();
	  		  return enemy_cell;

		  }
		  private AreaCell getAssistMovePosition(ObjActor selected_obj){
			  if (!selected_obj.isAvailableAction(selected_obj.getMoveAction()))
			  	{
			  		return null;
			  	}
			  
			  	AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  	selected_obj.selectAction(selected_obj.getMoveAction());
			  	
			  	area_computer.setAreaListWithoutClearCount(area_computer.computeArea(action,selected_obj));
	  		  	AreaCell enemy_cell=Global.getCurrentRoom().getCell(selected_obj);
	  		    float enemy_weight=computeAssistWeight(selected_obj,enemy_cell);
	  		    
	  		  	LinkedList<AreaCell> area_list= area_computer.getAreaList();
	  		  	for (AreaCell temp:area_list)
	  		  	{
	  		  		float temp_weight=computeAssistWeight(selected_obj,temp);
	  		  		if (temp_weight>enemy_weight)
	  		  		{
	  		  			enemy_cell=temp;
	  		  			enemy_weight=temp_weight;
	  		  		}		
	  		  	}
	  		  	
	  		  //enemy_cell=backtrackingUntilInRange(enemy_cell,action.targeting_max_range);

	  		  	
	  		  area_computer.clearAreaCount();
	  		  area_computer.clearAreaList();
	  		  return enemy_cell;
		  }		  
		  
		  private AreaCell getDumbMovePosition(ObjActor selected_obj,AbilityActor move_action,int type) {
			  if (!selected_obj.isAvailableAction(selected_obj.getMoveAction()))
			  	{
			  		return null;
			  	}
			  
			  AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  selected_obj.selectAction(selected_obj.getMoveAction());	
			  LinkedList<ObjActor>opponent_team=getOpponentTeam(selected_obj);
			  ObjActor opponent;
			  int index=0;
			  if (type==0) findNearstEnemy(opponent_team);
			  else index= (int)(Math.random()*opponent_team.size());
			  opponent= opponent_team.get(index);
			  int initial_distance=opponent.getOneDistance(selected_obj);
			  LinkedList<AreaCell> temp_list= area_computer.computeArea(move_action,selected_obj);
			  int min_distance=Integer.MAX_VALUE;
			  int distance=0;
			  AreaCell to_return=null; 
			  for (AreaCell temp_cell :temp_list) {
					  distance= opponent.getOneDistance(temp_cell);
					  if (temp_cell.isDangerous()) distance+=4;
					  if (distance<=min_distance) {
						  min_distance=distance;
						  to_return=temp_cell;
					  }
			  }
			  area_computer.clearAreaList();
			  if (type==0&&initial_distance<=min_distance && Randomizer.hitInRatio(.5f)) return null;
				  else  return to_return;
		  }
		  
		  
		  private AreaCell findObjToDestory() {
			  Stack<AreaCell> obs_stack= new Stack<AreaCell>();
			  target=null;
			  ObjActor selected_obj=Global.getSelectedObj();
			  AbilityActor action=(AbilityActor)selected_obj.getMoveAction().action;
			  Global.getSelectedObj().selectAction(Global.getSelectedObj().getMoveAction());
			  
			  // Check if obstacle to destroy is near obj
		  	  for (int dir=0;dir<4;dir++)
		  	  	{
  		  			ObjActor temp_obj= Global.getCurrentRoom().getNearObj(selected_obj,dir);
  		  			AreaCell temp_cell= Global.getCurrentRoom().getNearCell(selected_obj,dir);
  		  			if (temp_obj!=null && temp_obj.isObstacle()&& !temp_obj.isDangerous()) {
  		  				if (getMeleeMovePosition(selected_obj,temp_cell)!=null)
  		  				{
  		  					target=Global.getCurrentRoom().getCell(selected_obj);
  		  					
  		  					return temp_cell;
  		  				}
  		  				
  		  			}
  		  		}
		  	  
		  	  
			  area_computer.setAreaListWithoutClearCount(area_computer.computeInspectionArea(Global.getSelectedObj()));	
	  		  	
	  		  	
	  		  	//Find approachable position such that have obstacle near itself
	  		  	
  		  	for (AreaCell temp: area_computer.getAreaList())
  		  	{
  		  		for (int dir=0;dir<4;dir++) {
  		  			ObjActor temp_obj= Global.getCurrentRoom().getNearObj(temp,dir);
  		  			if (temp_obj!=null && temp_obj.isObstacle()) {
	  		  				if (!temp.isDangerous())
	  		  				{
	  		  					obs_stack.push(temp);
	  		  					continue;
	  		  				}
	  		  			}
	  		  		}
	  		  		
	  		 }
  		  	area_computer.clearAreaCount();
  		  	area_computer.clearAreaList();
  		  
	  		 while (!obs_stack.isEmpty())
	  		  	{
	  		  		AreaCell temp=obs_stack.pop();
	  		  		for (int dir=0;dir<4;dir++) {
	  		  			ObjActor temp_obj= Global.getCurrentRoom().getNearObj(temp,dir);
	  		  			AreaCell temp_cell= Global.getCurrentRoom().getNearCell(temp,dir);
	  		  			if (temp_obj!=null && temp_obj.isObstacle()) {
	  		  				if (getMeleeMovePosition(selected_obj,temp_cell)!=null)
	  		  				{
	  		  					area_computer.setAreaListWithoutClearCount(area_computer.computeInspectionArea(Global.getSelectedObj())); 		  	
	  		  					temp=backtrackingUntilInRange(temp,action.targeting_max_range);
	  		  					/*
	  		  					if (target !=null && target.equals(temp))
	  		  					{
	  		  						Global.showBigMessage("BackTraking failed");
	  		  					}
	  		  					*/
	  		  					target=temp;
	  		  					area_computer.clearAreaCount();
	  		  					area_computer.clearAreaList();
	  		    		  
	  		  					return temp_cell;
	  		  				}
	  		  				
	  		  			}
	  		  		}
	  		  	}

	  		  	target=null;
				  return null;
		  }

		  public void addObj(ObjActor obj) {
			  ai_list.add(obj);
		  }
		  
		  public int selectObjtoAct()
		  {
			  if (Global.getSelectedObj()==null || Global.getSelectedObj().status.getCurrentAP()<=0) {
					ObjActor next_ai= ai_list.pollLast();
					Global.dungeon.selectObj(next_ai);
					if (next_ai==null) return -1;
				  	//action_list= Global.getSelectedObj().getActionList();
				  }
			  return 0;
				  
		  }

		  public boolean findMeleeActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("melee")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeWeightandsetTarget(((AbilityActor)temp_action.action),-3,1,0);
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else {
				  target=null;
				  return false;
			  }
		  }
		  public boolean findRangedActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  temp_target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("ranged")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeWeightandsetTarget(((AbilityActor)temp_action.action),-3,1,0);
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else 
			  {
				  target=null;
				  return false;
			  }
		  }
		  public boolean findBuffActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("buff")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeWeightandsetTarget(((AbilityActor)temp_action.action),1,-3,0);
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else
			  {
				  target=null;
				  return false;
			  }
		  }
		  public boolean findNuffActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("nuff")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeWeightandsetTarget(((AbilityActor)temp_action.action),-3,1,0);
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else {
				  target=null;
				  return false;
			  }
		  }
		  public boolean findSummonActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("summon")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeWeightandsetTarget(((AbilityActor)temp_action.action),0,0,1);
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else {
				  target=null;
				  return false;
			  }
		  }
		  public boolean findHealingActionAndTarget(ObjActor selected_obj)
		  {
			  target=null;
			  float return_weight=0;
			  ActionContainer return_action=null;
			  for (ActionContainer temp_action:selected_obj.getActionList()) {
				  if (temp_action.action instanceof AbilityActor
						  && ((AbilityActor)temp_action.action).ai_type.equals("healing")
						  && selected_obj.isAvailableAction(temp_action)
						  )
				  {
					  float current_weight=computeHealingWeightandsetTarget(((AbilityActor)temp_action.action));
				  	  if (current_weight > return_weight)
				  	  {
				  		  return_weight= current_weight;
				  		  target=temp_target;
				  		  return_action=temp_action;
				  	  }
				  }
			  }
			  
			  if (return_weight>0.5f) {
				  selected_obj.selectAction(return_action);
				  return true;
			  }
			  else {
				  target=null;
				  return false;
			  }
		  }
		  private float computeWeightandsetTarget(AbilityActor action,float friendly_weight,float enemy_weight,float empty_weight) {
			  LinkedList<AreaCell> temp=area_computer.computeArea(action,Global.getSelectedObj());
			  area_computer.setAreaList(temp);
			  float return_weight=0;
			  float current_weight=0;
			  for (AreaCell temp_cell:area_computer.getAreaList()) {
					  current_weight=0;
					  area_computer.setTargetList(area_computer.computeTarget(action,temp_cell,Global.getSelectedObj()));
					  for (AreaCell temp_cell_splash:area_computer.getTargetList()) {
						  ObjActor actor=Global.getCurrentRoom().getObj(temp_cell_splash);
						  if (temp_cell_splash.is_target)
						  {
							  if( actor!= null)
							  {
								  if (actor.getTeam() == Global.getSelectedObj().getTeam())
									  current_weight+=friendly_weight;
								  else if (actor.getTeam()!=-1)
									  current_weight+=enemy_weight;
							  }
							  else // No actor on cell
							  {
								  current_weight+=empty_weight;  
							  }
						  }
						  else {
							  //
						  }
					  }
					  area_computer.clearTargetList();
					  current_weight=expNormalize(current_weight);
					  if (current_weight> return_weight)
					  {
						  return_weight=current_weight;
						  temp_target=temp_cell;
					  }
					  
				  
			  }
			  area_computer.clearAreaList();
			  
			  
			  return return_weight;
		  } 
		  private float computeHealingWeightandsetTarget(AbilityActor action) {
			  LinkedList<AreaCell> temp=area_computer.computeArea(action,Global.getSelectedObj());
			  area_computer.setAreaList(temp);
			  float return_weight=0;
			  float current_weight=0;
			  for (AreaCell temp_cell:area_computer.getAreaList()) {
					  current_weight=0;
					  area_computer.setTargetList(area_computer.computeTarget(action,temp_cell,Global.getSelectedObj()));
					  for (AreaCell temp_cell_splash:area_computer.getTargetList()) {
						  ObjActor actor=Global.getCurrentRoom().getObj(temp_cell_splash);
						  if (temp_cell_splash.is_target &&
								  actor!= null
									  ) {
							  if (actor.getTeam() == Global.getSelectedObj().getTeam())
								  current_weight+=1-(actor.status.getCurrentHPRatio());
							  else if (actor.getTeam()!=-1)
								  current_weight+=(-1.5);
						  }
						  else {
							  current_weight+=0;
						  }
					  }
					  current_weight=expNormalize(current_weight);
					  if (current_weight> return_weight)
					  {
						  return_weight=current_weight;
						  temp_target=temp_cell;
					  }
				  
			  }
			  area_computer.clearAreaList();
			  
			  return return_weight;
		  } 
		  public AreaCell meleeAI(ObjActor selected_obj)
		  {
			  if (findMeleeActionAndTarget(selected_obj)) return target;
			  else {   
				  target=getMeleeMovePosition(selected_obj,Global.getCurrentRoom().getCell(selected_obj));
				  if (target==null)
				  	{// no target, no move postion
				  			AreaCell obs_cell=findObjToDestory();
				  			if (obs_cell!=null)
				  			{
					  			if (target.equals(Global.getCurrentRoom().getCell(selected_obj)))
					  			{
									selected_obj.selectAction(selected_obj.getActionList().getFirst());
				  					target=obs_cell;
				  				}
					  			else if (target==null ||Randomizer.hitInRatio(.3f))
						  		{
						  				target=getDumbMovePosition(selected_obj,(AbilityActor)selected_obj.getMoveAction().action,1);
						  			
						  		}
				  				
					  		}
					  		
					  	
				  	}
				  
				  if (target==null)
				  {
					  if (findRangedActionAndTarget(selected_obj)) return target;
					  else if (findNuffActionAndTarget(selected_obj)) return target;
					  else if (findBuffActionAndTarget(selected_obj)) return target;
					  else if (findSummonActionAndTarget(selected_obj)) return target;
				  }
			  }
			  return target;
		  }
		  public AreaCell rangedAI(ObjActor selected_obj)
		  {
			  target=getRangedMovePosition(selected_obj);
			  if (target==null)
			  {
				  if (findRangedActionAndTarget(selected_obj)) return target;
				  else if (findMeleeActionAndTarget(selected_obj)) return target;
				  else if (findNuffActionAndTarget(selected_obj)) return target;
				  else if (findBuffActionAndTarget(selected_obj)) return target;
				  else if (findSummonActionAndTarget(selected_obj)) return target;
			  }
			  return target;
		  }
		  
		  public AreaCell assistAI(ObjActor selected_obj)
		  {
			
			 target=getAssistMovePosition(selected_obj); 
			 if (target==null)
			 {
				 if (findMeleeActionAndTarget(selected_obj)) return target;
				  else if (findHealingActionAndTarget(selected_obj)) return target;
				 else if (findRangedActionAndTarget(selected_obj)) return target;
				 else if (findSummonActionAndTarget(selected_obj)) return target;
				 else if (findBuffActionAndTarget(selected_obj)) return target;
				 else if (findNuffActionAndTarget(selected_obj)) return target;
			 }
			 //else
			//{
				
			//}
		
			return target;  
		  }

		  public AreaCell chooseObjAndActionAndTarget() {
			
			  // Select Obj
			  if (selectObjtoAct()==-1 ) return null;
			  ObjActor selected_obj=Global.getSelectedObj();
			  target=null;
			  if (selected_obj.isDead())
			  {
				  return null;
			  }
			  if (selected_obj.isFleeing())
			  {
				  target=getRunawayMovePosition(selected_obj);
				  if (target!=null) return target;
			  }
			  
			  
			  if (selected_obj.isAssistUnit())
			  {
				  assistAI(selected_obj);
			  }
			  else if (selected_obj.isRangedUnit())
			  {
				  rangedAI(selected_obj); 
			  }
			  else 
			  {
				  meleeAI(selected_obj);
			  }
			   
			  
			  
			  return target;	
			  
		  }
		  
		  
	  
	  
}