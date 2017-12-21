package com.icefill.game.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.icefill.game.AIController;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.Global;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.devices.DeviceActor;
import com.icefill.game.screens.GameScreen;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Byungpil on 2017-01-21.
 */
public class GFSM {
    private DungeonGroup dungeonGroup;
    LinkedList<Integer> states_stack;
    int seq = 0;
    int sub_seq = 0;
    public int current_team = 0;
    int click_counter = 0;
    int initial_team = 1;
    int clicked_before_xx = -1;
    int clicked_before_yy = -1;
    AreaCell cell_to_examine;
    boolean pause_gfs_flag = false;
    boolean battle_end_flag = false;
    int acting_state;
    int INSPECTION = 0;
    int BATTLE_PLAYER = 1;
    int BATTLE_AI = 2;
    int pause_index = 0;
    AIController ai_controller;

    public GFSM(DungeonGroup dungeonGroup) {
        this.dungeonGroup = dungeonGroup;
        this.states_stack = new LinkedList<Integer>();
        ai_controller = new AIController(dungeonGroup.area_computer);
        pushState(Constants.GS_ROOM_INIT);
    }

    public void pauseGFS() {
        pause_index++;
    }

    public int getSeq() {
        return seq;
    }

    public void reRunGFS() {
        pause_index--;
    }

    public int getActingState() {
        return acting_state;
    }

    public Action pauseGFSAction() {
        return (Actions.run(new Runnable() {
            public void run() {
                pauseGFS();
            }
        }));
    }

    public Action reRunGFSAction() {
        return (Actions.run(new Runnable() {
            public void run() {
                reRunGFS();
            }
        }));
    }

    public void act(float delta) {
        if (pause_index <= 0) {
            switch (((Integer) this.states_stack.getFirst()).intValue()) {
                // Turn init : 
                case Constants.GS_DUNGEON_INIT:
                    break;
                case Constants.GS_DUNGEON_CLEAR:
                    break;
                case Constants.GS_ROOM_INIT:
                    gs_room_init();
                    break;
                case Constants.GS_BATTLE_INIT:
                    gs_battle_init();
                    break;
                case Constants.GS_BATTLE_END:
                    gs_battle_end();
                    break;
                case Constants.GS_TURN_INIT:
                    gs_turn_init();
                    break;
                case Constants.GS_TURN_END:
                    gs_turn_end();
                    break;
                case Constants.GS_PHASE_INIT:
                    gs_phase_init();
                    break;
                case Constants.GS_PHASE_END:
                    gs_phase_end();
                    break;

                case Constants.GS_CHOOSE_CHAR:
                    gs_choose_char();
                    break;
                case Constants.GS_CHOOSE_ACTION:
                    gs_choose_action();
                    break;
                case Constants.GS_SELECT_TARGET:
                    gs_select_target();
                    break;

                case Constants.GS_PLAYER_ACTING:
                    gs_player_acting();
                    break;
                //case GS_AI_ACTING: gs_ai_acting();break;

                case Constants.GS_GAME_OVER:
                    break;
                case Constants.GS_AI:
                    gs_ai();
                    break;
                case Constants.GS_DUNGEON_INSPECTION_INIT:
                    gs_dungeon_inspection_init();
                    break;

            }
        }
    }


    private void popState() {
        states_stack.pollFirst();
        this.seq = 0;
    }

    private void popAll() {
        states_stack.clear();
    }

    public void pushState(int state) {
        this.seq = 0;
        this.sub_seq = 0;
        this.states_stack.push(Integer.valueOf(state));
        System.out.println(Constants.gs_state_name[state]);
    }

    public String getCurrentStateName() {
        return DungeonGroup.gs_state_name[((Integer) states_stack.getFirst()).intValue()];
    }

    public int getCurrentState() {
        return states_stack.getFirst();
    }

    //************* AI STATE *********************************************************/
    private void gs_ai() {
        AreaCell target = ai_controller.chooseObjAndActionAndTarget();
        if (Global.getSelectedObj() != null) {
            if (target != null && Global.getSelectedObj().getSelectedAction() != null) {
                dungeonGroup.area_computer.setAreaList(dungeonGroup.area_computer.computeArea((AbilityActor) (Global.getSelectedObj().getSelectedAction().action), Global.getSelectedObj()));
                //area_computer.computeTarget(ability, caster)
                dungeonGroup.area_computer.setTargetPath((AbilityActor) (Global.getSelectedObj().getSelectedAction().action), target);
            } else {
                Global.getSelectedObj().selectAction(null);
            }
        }
        pushState(Constants.GS_PLAYER_ACTING);

    }

    //************************DUNGEON_INIT_STATE **************************************
    private void gs_dungeon_init() {

    }

    private void gs_room_init() {
        if (!(dungeonGroup.team_lists[0].isEmpty())) {
            for (Iterator<ObjActor> itr = dungeonGroup.team_lists[0].iterator(); itr.hasNext(); ) {
                ObjActor temp = itr.next();
                temp.resetCoolTimes();
                //temp.addCoolTimes();
                //temp.doTurnEffect(Global.dungeon);
            }
            dungeonGroup.team_lists[0].setManaToInitial();
            dungeonGroup.team_lists[1].increaseMana(15);

        }
        //deSelectObj();
        if (Global.dungeon.checkWinorGameOver(0) == 1)
            pushState(Constants.GS_DUNGEON_INSPECTION_INIT);
        else
            pushState(Constants.GS_BATTLE_INIT);
    }

    private void gs_battle_init() {
        //initial_team=Randomizer.nextInt(2);
        initial_team = 0;
        if (Global.getCurrentRoom().boss_room) {
            Global.showBigMessage("BOSS BATTLE!!!");
        }
        //else
        //	Global.showBigMessage("ENEMY INCOUNTER!!!");
        final Sound sound = Assets.getAsset("sound/slash.wav", Sound.class);

        pushState(Constants.GS_TURN_INIT);
        pauseGFS();
        SequenceAction action = new SequenceAction();
        action.addAction(Actions.delay(.2f));
        for (final ObjActor temp : Global.getCurrentRoom().getEnemyList()) {
            if (temp.obj_state != Constants.PL_DEAD) {
                action.addAction(Actions.sequence(
                        //Actions.moveTo(200,100,.3f),
                        temp.setDirectionAction(dungeonGroup.from_dir),
                        Actions.run(new Runnable() {
                            public void run() {
                                sound.play();
                                if (temp.isLeader())
                                    temp.showMessage("I am the boss!!");
                                else
                                    temp.showMessage("!!!");
                            }
                        }),
                        Actions.delay(.4f)
                ));
            }
            //temp.showMessage("!!!");
            //temp.setDirection(from_dir);
        }

        action.addAction(Actions.run(new Runnable() {
            public void run() {
                Global.getCurrentRoom().map.closeAllDoor(Global.dungeon);
                Global.showBigMessage("ENEMY INCOUNTER!!!");
            }
        }));
        action.addAction(Actions.delay(.4f));
        action.addAction(this.reRunGFSAction());
        Global.getCurrentRoom().addAction(action);
        //music_anod.pause();
        //music_battle.setPosition(0f);
        //music_battle.play();
        // Choose first team
    }

    private void gs_battle_end() {
        Global.showBigMessage("BATTLE END");
        dungeonGroup.team_lists[0].setManaZero();
        Global.dungeon.releaseAttacker();
        for (ObjActor obj : dungeonGroup.team_lists[0]) {
            obj.initializeRunawayCount();
            if (obj.isSummoned()) {
                obj.inflictDamageInRatio(1f, null);
            }
            obj.clearTurnEffect();

        }

        for (ObjActor obj : dungeonGroup.team_lists[1]) {
            obj.clearTurnEffect();
            obj.initializeRunawayCount();
        }
        //Global.getCurrentRoom().map.openAllDoor(Global.dungeon);

        Global.getHUD().getBattleWinWindow().battleWin(Global.dungeon);
        //dead_enemy_list.clear();

        Global.dungeon.deSelectObj();
        //	Global.dungeon.selectObj(Global.dungeon.team_lists[0].get(0));
        //	music_battle.stop();
        //	music_anod.setPosition(0f);
        //	music_anod.play();
        pushState(Constants.GS_DUNGEON_INSPECTION_INIT);
    }

    private void gs_turn_init() {
        current_team = initial_team;
        pushState(Constants.GS_PHASE_INIT);
    }

    private void gs_turn_end() {
        dungeonGroup.team_lists[0].increaseMana(dungeonGroup.team_lists[0].getDeltaMana());

        // status damage/heal...
        pushState(Constants.GS_TURN_INIT);
    }

    //************* PHASE INIT STATE **************************************************/
    private void gs_phase_init() {
        //for (int i=0;i<2;i++) {
        int i = current_team;
        if (!(dungeonGroup.team_lists[i].isEmpty())) {
            for (Iterator<ObjActor> itr = dungeonGroup.team_lists[i].iterator(); itr.hasNext(); ) {
                ObjActor temp = itr.next();
                temp.subCoolTimes();
                temp.doTurnEffect(Global.dungeon);
                temp.decreaseRunawayCount();
                /*
                if (temp.checkDeadandExecuteDead(Global.dungeon))
                {
                      if (checkBattleOverandChangeState()) return;
                    
                }
                */
            }
        }
        //}

        // If there's no ememy,=> inspection state
        dungeonGroup.deSelectObj();
        //if (Global.dungeon.checkWinorGameOver(0)==1)
        //	pushState(GS_DUNGEON_INSPECTION_INIT);
        //else {// There's an enemy 
        dungeonGroup.setSelectedObj(null);
        boolean have_ai = false;
        ai_controller.clearAI();
        //Initialize team

        if (current_team == 0) {
            Global.showBigMessage("PLYAER PHASE");
        } else {
            Global.showBigMessage("ENEMY PHASE");
        }

        for (ObjActor obj : dungeonGroup.team_lists[current_team]) {
            if (obj.obj_state != Constants.PL_DEAD) {
                obj.obj_state = Constants.PL_WAIT;
                if (obj.getControlled() == Constants.CONTROLLED_AI) {
                    ai_controller.addObj(obj);
                    have_ai = true;
                }
                obj.status.setAP();
            }
        }
        if (have_ai) {
            pushState(Constants.GS_AI);
            acting_state = BATTLE_AI;
        } else {
/*
        if (checkBattleOverandChangeState()) return;
        */
            pushState(Constants.GS_CHOOSE_CHAR);
            acting_state = BATTLE_PLAYER;
        }
    }

    public boolean checkBattleOverandChangeState() {
        switch (Global.dungeon.checkWinorGameOver(0)) {
            case -1:
                Global.dungeon.addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            public void run() {
                                Global.showBigMessage("game over...");
                                Global.gfs.pauseGFS();
                            }
                        }),
                        Actions.delay(3f),
                        Actions.run(new Runnable() {
                            public void run() {
                                Global.gfs.reRunGFS();
                                ((GameScreen) dungeonGroup.screen).GameOver();
                                return;
                            }
                        })
                ));
                return true;
            case 0:
                //pushState(GS_PHASE_INIT);
                return false;
            case 1:
                // /If you're in final room and win the battle -> game win
                if (false) {//room_xx== final_room[0]&& room_yy==final_room[1]) {
                    ((GameScreen) dungeonGroup.screen).WinTheGame();
                } else {//Else, just change to inspection mode
                    if (acting_state == INSPECTION) {
                        popAll();
                        pushState(Constants.GS_DUNGEON_INSPECTION_INIT);
                        if (dungeonGroup.getSelectedObj() != null) {
                            AreaCell selected_cell = dungeonGroup.current_map.getCell(dungeonGroup.getSelectedObj().getXX(), dungeonGroup.getSelectedObj().getYY());
                            if (selected_cell.device != null) { // IF there is a device on current tile
                                ((DeviceActor) (selected_cell.device)).action(Global.dungeon, selected_cell);
                                pushState(Constants.GS_ROOM_INIT);
                                return false;
                            }

                        }

                    } else {
                        popAll();
                        pushState(Constants.GS_BATTLE_END);
                        return true;
                    }
                }
                break;

        }
        return false;
    }

    private void gs_phase_end() {
        current_team = (current_team + 1) % 2;
        if (current_team == initial_team) {
            pushState(Constants.GS_TURN_END);
        } else {
            pushState(Constants.GS_PHASE_INIT);
        }
    }

    //***************** GAME OVER STATE **********************************************/
    private void gs_game_over() {
    }
    //****************** DUNGEON_INSPECTION STATE *****************************/

    // default -> leader
    // other char select--> change leader(select)
    // click area -> move
    // click door -> move to next room
    private void gs_dungeon_inspection_init() {
        acting_state = INSPECTION;
        pushState(Constants.GS_CHOOSE_ACTION);
        //return;

    }


    //***************** CHOOSE CHAR STATE ********************************************/
    private void gs_choose_char() {
        switch (seq) {
            case 0:
                Global.showInformation("CHOOSE CHAR TO ACT");
                seq++;

            default:
                if (dungeonGroup.getSelectedObj() == null) {
        
        /*
        for (ObjActor temp:getTeamList(0))
        {
            if (temp.status.current_ap>0)
            {
                temp.obj_state=PL_WAIT;
                selectObj(temp);
                pushState(GS_CHOOSE_ACTION);
                
                break;
            }
        }
        */
                }
                if (dungeonGroup.isMouseClicked() && dungeonGroup.current_map.isInFloor(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy)) {
                    if ((dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy) != null)
                            &&
                            (
                                    (dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy).obj_state == Constants.PL_WAIT)
                                            || ((acting_state == INSPECTION) && current_team == dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy).getTeam())
                            )
                            ) {
                        dungeonGroup.area_computer.clearAreaList();
                        dungeonGroup.current_map.clearAreaList();
                        dungeonGroup.selectObj(dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy));
                        pushState(Constants.GS_CHOOSE_ACTION);
                    } else {
                        Global.showMessage("There is no Char on this tile", 1);
                    }
                }

                break;
        }
    }

    // ***********  CHOOSE ACTION STATE *************************************************/
    private void gs_choose_action() {
        switch (seq) {
            case 0:

                if (acting_state == INSPECTION) {
                    Global.showInformation("Touch to move or activate devices.");
                    // Select if not selected anyone
                    if (dungeonGroup.getSelectedObj() == null) {
                        {
                            dungeonGroup.selectObj(dungeonGroup.team_lists[0].getFirst());
                        }
                    }

                    // show abilities of selected obj
                    if (dungeonGroup.fsm.getActingState() == 0)
                        Global.getHUD().addAbility(dungeonGroup.getSelectedObj(), false);
                    else
                        Global.getHUD().addAbility(dungeonGroup.getSelectedObj(), true);
                    dungeonGroup.area_computer.clearAreaList();
                    dungeonGroup.getSelectedObj().selectAction(dungeonGroup.getSelectedObj().getMoveAction());


                    // area_computer.compute paths

                    dungeonGroup.area_computer.setAreaList(dungeonGroup.area_computer.computeInspectionArea(dungeonGroup.getSelectedObj()));
                    //current_map.addArea(getAreaList(),new Color(0f,0f,1f,0.4f));
                    dungeonGroup.area_computer.setTargetList(dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.getSelectedObj()));

                    dungeonGroup.cursor.setVisible(true);

                } else {
                    Global.showInformation("TOUCH BLUE AREA TO MOVE OR SELECT ACTION");

                    Global.getHUD().addAbility(dungeonGroup.getSelectedObj(), true);

                    if (dungeonGroup.getSelectedObj() != null) {
                        dungeonGroup.getSelectedObj().selectAction(dungeonGroup.getSelectedObj().getMoveAction());
                        if (dungeonGroup.getSelectedObj().isAvailableAction(dungeonGroup.getSelectedObj().getMoveAction())) {
                            dungeonGroup.area_computer.setAreaList(dungeonGroup.area_computer.computeArea((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.getSelectedObj()));
                            dungeonGroup.current_map.addArea(dungeonGroup.area_computer.getAreaList(), new Color(0f, 0f, 1f, 0.4f));
                            dungeonGroup.area_computer.setTargetList(dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.getSelectedObj()));
                        } else dungeonGroup.area_computer.clearTargetList();
                    }
                }
                Global.getHUD().showQuickSlot();
                this.seq += 1;
                break;
            default:

                if (dungeonGroup.isMouseClicked() && dungeonGroup.current_map.isInFloor(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy)) {

                    dungeonGroup.selected_cell = dungeonGroup.current_map.getCell(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy);
                    dungeonGroup.selected_xx = dungeonGroup.clicked_xx;
                    dungeonGroup.selected_yy = dungeonGroup.clicked_yy;

                    if (dungeonGroup.getSelectedObj().isActionSelected()) {

                        dungeonGroup.area_computer.clearAreaList();
                        dungeonGroup.current_map.clearAreaList();
                        Global.getHUD().removeAbility(dungeonGroup.getSelectedObj());
                        Global.getHUD().hideQuickSlot();
                        //showMessage(getSelectedObj().getSelectedAction().action.getActionName(),0);
                        if (dungeonGroup.getSelectedObj().getSelectedAction().action instanceof AbilityActor) {

                            pushState(Constants.GS_SELECT_TARGET);
                        } else {// instant action
                            Global.getHUD().clearAbility();
                            Global.getHUD().hideQuickSlot();
                            pushState(Constants.GS_PLAYER_ACTING);
                        }
                    }// Clicked Other player -> select that player
                    else if ((dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy) != null)
                            &&
                            (
                                    (dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy).obj_state == Constants.PL_WAIT)
                                            || ((acting_state == INSPECTION) && current_team == dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy).getTeam())
                            )
                            ) {
                        dungeonGroup.area_computer.clearAreaList();
                        dungeonGroup.current_map.clearAreaList();
                        dungeonGroup.selectObj(dungeonGroup.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy));
                        pushState(Constants.GS_CHOOSE_ACTION);
                    }
                    // Move
                    else if (dungeonGroup.area_computer.isInArea(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy)) {
                        dungeonGroup.area_computer.setTargetList(
                                dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.current_map.getCell(dungeonGroup.cursor.getXX(), dungeonGroup.cursor.getYY()), dungeonGroup.getSelectedObj())
                        );
                        Global.getHUD().removeAbility(dungeonGroup.getSelectedObj());
                        Global.getHUD().hideQuickSlot();
                        dungeonGroup.area_computer.setTargetPath(dungeonGroup.area_computer.getTargetList().getLast());
                        dungeonGroup.area_computer.clearAreaList();
                        dungeonGroup.current_map.clearAreaList();
                        pushState(Constants.GS_PLAYER_ACTING);
                        break;

                    } else if (acting_state == INSPECTION && dungeonGroup.selected_cell != null && dungeonGroup.selected_cell.device != null) {
                        //selected actor is near
                        //is door
                        // open door


                        if (dungeonGroup.area_computer.isInArea(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy)) {
                            dungeonGroup.area_computer.setTargetList(
                                    dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.current_map.getCell(dungeonGroup.cursor.getXX(), dungeonGroup.cursor.getYY()), dungeonGroup.getSelectedObj())
                            );
                            Global.getHUD().removeAbility(dungeonGroup.getSelectedObj());
                            Global.getHUD().hideQuickSlot();
                            dungeonGroup.area_computer.setTargetPath(dungeonGroup.area_computer.getTargetList().getLast());
                            dungeonGroup.area_computer.clearAreaList();
                            dungeonGroup.current_map.clearAreaList();
                            pushState(Constants.GS_PLAYER_ACTING);
                            break;

                        } else {
                            AreaCell near_empty_cell = null;
                            int min_distance = 2000;
                            for (int dir = 0; dir < 4; dir++) {
                                AreaCell temp_cell = Global.getCurrentRoom().getNearCell(dungeonGroup.selected_cell, dir);
                                if (temp_cell == Global.getCurrentRoom().getCell(Global.getSelectedObj())) {
                                    Global.getSelectedObj().setDirectionToTarget(dungeonGroup.selected_cell.getXX(), dungeonGroup.selected_cell.getYY());
                                    dungeonGroup.selected_cell.execute(Global.dungeon);

                                    pushState(Constants.GS_CHOOSE_ACTION);
                                    return;
                                }
                                if (temp_cell != null && temp_cell.device == null && !temp_cell.is_blocked && Global.getCurrentRoom().getObj(temp_cell) == null) {
                                    int current_distance = dungeonGroup.area_computer.getWalkDistance(temp_cell);
                                    if (current_distance < min_distance) {
                                        near_empty_cell = temp_cell;
                                        min_distance = current_distance;
                                    }
                                }
                            }
                            if (near_empty_cell != null) {
                                cell_to_examine = dungeonGroup.selected_cell;
                                dungeonGroup.area_computer.setTargetList(
                                        dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getMoveAction().action, dungeonGroup.current_map.getCell(near_empty_cell.getXX(), near_empty_cell.getYY()), dungeonGroup.getSelectedObj())
                                );
                                Global.getHUD().removeAbility(dungeonGroup.getSelectedObj());
                                Global.getHUD().hideQuickSlot();
                                dungeonGroup.area_computer.setTargetPath(dungeonGroup.area_computer.getTargetList().getLast());
                                dungeonGroup.area_computer.clearAreaList();
                                dungeonGroup.current_map.clearAreaList();
                                pushState(Constants.GS_PLAYER_ACTING);
                            } else {
                                Global.showMessage("Cannot Move near device.", 1);
                                pushState(Constants.GS_CHOOSE_ACTION);
                            }
                        }

                        //	  }
                    } else {
                        //Global.showMessage("Invalid TOUCH", 1);
                    }


                }
                break;
        }
    }


    //******************* SELECT TARGET STATE *******************************/
    private void gs_select_target() {
        ActionActor.ActionContainer action = (dungeonGroup.getSelectedObj().getSelectedAction());
        Global.getHUD().addAbilityInfo((AbilityActor) action.action, action.level);
        //getSelectedObj().addTargetInfo(action.action.getActionName());
        switch (seq) {
            case 0:
                Global.showInformation("Touch BLUE AREA TO SELECT TARGET(S).");

                dungeonGroup.area_computer.clearAreaList();
                dungeonGroup.current_map.clearAreaList();
                Global.getHUD().removeAbility(dungeonGroup.getSelectedObj());
                Global.getHUD().hideQuickSlot();
                click_counter = 0;
                clicked_before_xx = -1;
                clicked_before_yy = -1;
                dungeonGroup.area_computer.setAreaList(dungeonGroup.area_computer.computeArea((AbilityActor) action.action, dungeonGroup.getSelectedObj()));
                dungeonGroup.current_map.addArea(dungeonGroup.area_computer.getAreaList(), new Color(0f, 0f, 1f, 0.4f));
                Global.getHUD().addCancle();
                dungeonGroup.area_computer.setTargetList(dungeonGroup.area_computer.computeTarget((AbilityActor) action.action, dungeonGroup.getSelectedObj()));
                this.seq += 1;
                break;
            default:
                if ((dungeonGroup.cursor.needSetTarget()) && dungeonGroup.area_computer.isInArea(dungeonGroup.cursor.getXX(), dungeonGroup.cursor.getYY())) { //show target area
                    dungeonGroup.area_computer.setTargetList(dungeonGroup.area_computer.computeTarget((AbilityActor) dungeonGroup.getSelectedObj().getSelectedAction().action, dungeonGroup.current_map.getCell(dungeonGroup.cursor.getXX(), dungeonGroup.cursor.getYY()), dungeonGroup.getSelectedObj()));

                    // Show accuracy and damage
                    dungeonGroup.showTargetsAccuracyAndDamage(action);
                }
                if (dungeonGroup.isMouseClicked()) {
                    // Clicked Cancel Button
                    if (Global.getHUD().isCanceled()) {
                        Global.getHUD().removeAbilityInfo();
                        dungeonGroup.current_map.clearAreaList();
                        dungeonGroup.area_computer.clearAreaList();
                        Global.getHUD().clearCancel();
                        Global.releaseSelectedSlot();
                        pushState(Constants.GS_CHOOSE_ACTION);
                        break;
                    }
                    // Clicked Area
                    if (dungeonGroup.area_computer.isInArea(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy)) {
                        if (((AbilityActor) action.action).getActionType().equals("move")
                                && Global.dungeon.getCurrentRoom().getObj(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy) != null) {
                        } else if (clicked_before_xx == dungeonGroup.clicked_xx && clicked_before_yy == dungeonGroup.clicked_yy) {
                            dungeonGroup.area_computer.setTargetCenter(Global.dungeon.getCurrentMap().getCell(dungeonGroup.clicked_xx, dungeonGroup.clicked_yy));
                            Global.getHUD().removeAbilityInfo();
                            dungeonGroup.area_computer.setTargetList(
                                    dungeonGroup.area_computer.computeTarget((AbilityActor) action.action, dungeonGroup.current_map.getCell(dungeonGroup.cursor.getXX(), dungeonGroup.cursor.getYY()), dungeonGroup.getSelectedObj())
                            );
                            if (!dungeonGroup.area_computer.getTargetList().isEmpty())
                                dungeonGroup.area_computer.setTargetPath(dungeonGroup.area_computer.getTargetList().getLast());
                            dungeonGroup.area_computer.clearAreaList();
                            dungeonGroup.current_map.clearAreaList();
                            Global.getHUD().clearCancel();
                            Global.getHUD().addCursorObj(Global.dungeon.getCurrentRoom().getObj(dungeonGroup.area_computer.getTargetCenter()));
                            pushState(Constants.GS_PLAYER_ACTING);
                        } else {

                            clicked_before_xx = dungeonGroup.clicked_xx;
                            clicked_before_yy = dungeonGroup.clicked_yy;
                            Global.showInformation("Touch Once more to EXECUTE ACTION");
                        }
                    } else {
                        //Global.showMessage("Invalid TOUCH", 1);
                    }

                }
                break;
        }
    }

    private void gs_counter_acting() {
        switch (this.seq) {
            case 0:
                AreaCell counter_cell = dungeonGroup.area_computer.getTargetList().pollFirst();

                // choose attacked
                // has counter
        }
    }

    /************************ ACTING STATE ****************************************/
    private void gs_player_acting() {
        Global.showInformation("ACTING");
        switch (this.seq) {
            case 0:
                //showMessage(getSelectedObj().getSelectedAction().getActionName(),0);
                if (dungeonGroup.getSelectedObj() != null && dungeonGroup.getSelectedObj().getSelectedAction() != null) {
                    dungeonGroup.getSelectedObj().getSelectedAction().action.initialize();
                    sub_seq = dungeonGroup.getSelectedObj().getSelectedAction().execute(Global.dungeon, dungeonGroup.getSelectedObj());
                } else {
                    sub_seq = -1;
                }
                this.seq += 1;
                break;
            default:

                if (sub_seq != -1) { //Still acting -> excute action
                    if (dungeonGroup.getSelectedObj() != null && dungeonGroup.getSelectedObj().getSelectedAction() != null)
                        sub_seq = dungeonGroup.getSelectedObj().getSelectedAction().execute(Global.dungeon, dungeonGroup.getSelectedObj());
                } else { //Acting done.
                    Global.decreaseNumberOfSelectedEquip();
                    Global.releaseSelectedSlot();
                    if (cell_to_examine != null) {
                        Global.getSelectedObj().setDirectionToTarget(cell_to_examine.getXX(), cell_to_examine.getYY());
                        cell_to_examine.execute(Global.dungeon);
                        cell_to_examine = null;

                    }

                    if (dungeonGroup.getSelectedObj() != null && dungeonGroup.getSelectedObj().getSelectedAction() != null)
                        dungeonGroup.getSelectedObj().getSelectedAction().addCoolTime();
                    if (dungeonGroup.getSelectedObj() != null && dungeonGroup.getSelectedObj().getSelectedAction() != null && dungeonGroup.getSelectedObj().getSelectedAction().action != null)
                        if (dungeonGroup.getSelectedObj().getSelectedAction().action instanceof AbilityActor && dungeonGroup.getSelectedObj().getTeam() == 0) {

                            if (!dungeonGroup.getSelectedObj().getSelectedAction().isManaFree())
                                dungeonGroup.team_lists[0].decreaseMana(((AbilityActor) dungeonGroup.getSelectedObj().getSelectedAction().action).mana_cost);
                        }
                    if (dungeonGroup.getSelectedObj() != null)
                        dungeonGroup.getSelectedObj().removeTargetInfo();
                    sub_seq = 0;
                    if (acting_state != INSPECTION) {

                        if (dungeonGroup.getSelectedObj() != null) {
                            AreaCell selected_cell = dungeonGroup.current_map.getCell(dungeonGroup.getSelectedObj().getXX(), dungeonGroup.getSelectedObj().getYY());
                            if (selected_cell.device != null) { // IF there is a device on current tile
                                ((DeviceActor) (selected_cell.device)).action(Global.dungeon, selected_cell);
                            }

                        }
                        for (AreaCell target_cell : dungeonGroup.area_computer.getTargetList()) {
                            ObjActor target = Global.dungeon.getCurrentRoom().getObj(target_cell);
                            if (target != null) {
                                if (target_cell.device != null && !target.equals(Global.getSelectedObj())) {
                                    // IF there is a device on current tile
                                    ((DeviceActor) (target_cell.device)).action(Global.dungeon, target_cell);
                                }

                            }
                        }

                    }
                    // decrease AP
                    if (dungeonGroup.getSelectedObj() != null)
                        if (dungeonGroup.getSelectedObj().getSelectedAction() == null || dungeonGroup.getSelectedObj().getSelectedAction().action.getType() != 0)
                            dungeonGroup.getSelectedObj().status.decreaseAP();
                    ;

                    switch (Global.dungeon.checkWinorGameOver(0)) {
                        case -1:
                            Global.dungeon.addAction(Actions.sequence(
                                    Actions.run(new Runnable() {
                                        public void run() {
                                            Global.gfs.pauseGFS();
                                            Global.showBigMessage("game over...");
                                        }
                                    }),
                                    Actions.delay(3f),
                                    Actions.run(new Runnable() {
                                        public void run() {
                                            ((GameScreen) dungeonGroup.screen).GameOver();
                                            Global.gfs.reRunGFS();
                                            return;
                                        }
                                    })
                            ));

                        case 0:
                            if (acting_state == BATTLE_PLAYER) {
                                if (dungeonGroup.getSelectedObj().status.getCurrentAP() > 0) { //AP remain
                                    pushState(Constants.GS_CHOOSE_ACTION);
                                } else {// AP =0
                                    /*if (getSelectedObj().status.current_ap==0)
                                    {
                                        if (getSelectedObj().getTeam()==0)
                                        {
                                            getSelectedObj().selectAction(new ActionContainer(Assets.actions_map.get("Setdierction"),0));
                                            pushState(GS_PLAYER_ACTING);
                                        }
                                    } 
                                    else*/
                                    {
                                        if (dungeonGroup.getSelectedObj().obj_state != Constants.PL_DEAD) {
                                            dungeonGroup.getSelectedObj().obj_state = Constants.PL_DONE;
                                            dungeonGroup.getSelectedObj().showMessage("WAIT");
                                            Sound select_sound = Assets.getAsset("sound/wait.wav", Sound.class);
                                            select_sound.play();
                                            Global.dungeon.addAction(
                                                    Actions.sequence(
                                                            this.pauseGFSAction(),
                                                            Actions.delay(.3f),
                                                            this.reRunGFSAction()
                                                    )
                                            );
                                        }
                                        dungeonGroup.deSelectObj();
                                        //check team_end
                                        if (dungeonGroup.isTeamEnd(current_team)) {
                                            pushState(Constants.GS_PHASE_END);
                                        } else {
                                            pushState(Constants.GS_CHOOSE_CHAR);
                                        }
                                    }
                                }
                            } else if (acting_state == BATTLE_AI) {
                                if (dungeonGroup.getSelectedObj() == null) {
                                    // All AI Done
                                    acting_state = BATTLE_PLAYER;
                                    pushState(Constants.GS_CHOOSE_CHAR);
                                    return;
                                }
                                if (dungeonGroup.getSelectedObj().status.getCurrentAP() <= 0) {
                                    if (dungeonGroup.getSelectedObj().obj_state != Constants.PL_DEAD) {
                                        dungeonGroup.getSelectedObj().obj_state = Constants.PL_DONE;
                                    }
                                }
                                if (dungeonGroup.isTeamEnd(current_team)) {
                                    pushState(Constants.GS_PHASE_END);
                                } else {
                                    pushState(Constants.GS_AI);
                                }
                                return;
                            }

                            break;
                        case 1:

                            // /If you're in final room and win the battle -> game win
                            if (dungeonGroup.room_zzz == dungeonGroup.dungeon_size[2] - 1 && dungeonGroup.room_xxx == dungeonGroup.boss_room[dungeonGroup.dungeon_size[2] - 1][0] && dungeonGroup.room_yyy == dungeonGroup.boss_room[dungeonGroup.dungeon_size[2] - 1][1]) {
                                ((GameScreen) dungeonGroup.screen).WinTheGame();
                            } else {//Else, just change to inspection mode

                                if (acting_state == INSPECTION) {
                                    popAll();
                                    pushState(Constants.GS_DUNGEON_INSPECTION_INIT);
                                    if (dungeonGroup.getSelectedObj() != null) {
                                        AreaCell selected_cell = dungeonGroup.current_map.getCell(dungeonGroup.getSelectedObj().getXX(), dungeonGroup.getSelectedObj().getYY());
                                        if (selected_cell.device != null) { // IF there is a device on current tile
                                            ((DeviceActor) (selected_cell.device)).action(Global.dungeon, selected_cell);
                                            pushState(Constants.GS_ROOM_INIT);
                                            return;
                                        }

                                    }
                                    for (AreaCell target_cell : dungeonGroup.area_computer.getTargetList()) {
                                        ObjActor target = Global.dungeon.getCurrentRoom().getObj(target_cell);
                                        if (target != null) {
                                            if (target_cell.device != null) { // IF there is a device on current tile
                                                ((DeviceActor) (target_cell.device)).action(Global.dungeon, target_cell);
                                                pushState(Constants.GS_ROOM_INIT);
                                                return;
                                            }

                                        }
                                    }
                    /*
                          AreaCell temp=current_map.getAreaCell(getSelectedObj().getXX(),getSelectedObj().getYY());
                  
                          if (temp.device!=null) { // IF there is a device on current tile
                              ((DeviceActor)(temp.device)).action(Global.dungeon,temp);
                              pushState(GS_ROOM_INIT);
                          }
                          return;
                          */
                                } else {
                                    popAll();
                                    pushState(Constants.GS_BATTLE_END);
                                    return;
                                }
                            }
                            break;
                    }
                }
                //clearTargetList();

                break;
        }

    }


}
