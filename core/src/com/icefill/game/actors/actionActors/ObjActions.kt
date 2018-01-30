package com.icefill.game.actors.actionActors

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.icefill.game.*
import com.icefill.game.actors.*
import com.icefill.game.actors.devices.SpikeTrapActor
import com.icefill.game.actors.dungeon.AreaCell
import com.icefill.game.actors.effects.EffectActor
import com.icefill.game.actors.effects.Lightning
import com.icefill.game.actors.effects.LineSegment
import com.icefill.game.actors.effects.ProjectileActor
import com.icefill.game.extendedActions.ExtendedActions
import com.icefill.game.utils.Randomizer
import java.util.*

/**
 * Created by Byungpil on 2017-01-19.
 */

class ObjActions : Constants {
    companion object {
        var subaction_map = HashMap<String, Function>()
        @JvmStatic
        fun getSubAction(str: String): Function? = subaction_map[str]

        @JvmStatic
        fun initializeSubActionMap() {
            subaction_map.put("basic_attack", BasicAttackSubAction)
            subaction_map.put("spear_attack", SpearAttackSubAction)
            subaction_map.put("basic_cast", BasicCastSubAction)
            subaction_map.put("basic_right_attack", BasicRightAttackSubAction)
            subaction_map.put("rotate", RotatingAction)
            subaction_map.put("basic_shoot_arrow", BasicShootArrowSubAction)
            subaction_map.put("basic_hit", BasicAttackEffectSubAction)
            subaction_map.put("rotating_hit", RotatingAttackEffectSubAction)
            subaction_map.put("shield_bash", ShieldBashAction)
            subaction_map.put("push", PushAction)
            subaction_map.put("push_half", PushHalfAction)
            subaction_map.put("attract", AttractAction)

            subaction_map.put("set_direction", SetDirectionSubAction)

            subaction_map.put("move_back", MoveBackSubAction)
            subaction_map.put("basic_attack_effect", BasicSlashAttackEffectSubAction)
            subaction_map.put("blood_effect", BloodEffectSubAction)
            subaction_map.put("acid_hit", AcidHitEffectSubAction)
            subaction_map.put("release_acid", ReleaseAcidEffectSubAction)

            subaction_map.put("fire_hit", FireHitEffectSubAction)
            subaction_map.put("lightning_hit", LightningHitEffectSubAction)
            subaction_map.put("wooden_hit", WoodenHitEffectSubAction)

            subaction_map.put("charge", ChargeAction)

            subaction_map.put("show_selected_item", showSelectedItemAction)
            subaction_map.put("throw_selected_item", throwSelectedItemAction)
            subaction_map.put("throw_dagger", throwDagger)
            subaction_map.put("throw_bomb", throwBombAction)
            subaction_map.put("throw_stone", throwStoneAction)
            subaction_map.put("throw_web", throwWebAction)


            subaction_map.put("throw_selected_item_straight", throwSelectedItemStraightAction)
            subaction_map.put("throw_weapon_straight", throwWeaponStraightAction)

            subaction_map.put("explode", Explode)

            subaction_map.put("move", objMoveToSkill)
            subaction_map.put("leap", objLeapSkill)
            subaction_map.put("fly", objFlyMoveSkill)
            subaction_map.put("teleport", objTeleportSkill)
            subaction_map.put("cast_defend", DefendAction)
            subaction_map.put("defend_release", DefendReleaseAction)

            subaction_map.put("charge_mana", ChargeManaSubAction)
            subaction_map.put("cast_fireball", CastFireBallSubAction)
            subaction_map.put("shoot_fire_cannon", ShootFireCannonSubAction)

            subaction_map.put("cast_acidball", CastAcidBallSubAction)
            subaction_map.put("cast_burn", CastBurnSubAction)
            subaction_map.put("cast_lightning", CastLightningSubAction)
            subaction_map.put("cast_energy_ray", CastEnergyRaySubAction)


            subaction_map.put("cast_meteor", CastMeteorSubAction)
            subaction_map.put("shoot_arrow", ShootArrowSubAction)
            subaction_map.put("shoot_multiple_arrow", ShootMultipleArrowSubAction)

            subaction_map.put("explosion_effect", ExplosionSubAction)
            subaction_map.put("sequencial_explosion_effect", SequencialExplosionSubAction)
            subaction_map.put("chain_lightning_effect", ChainLightningSubAction)

            subaction_map.put("acid_explosion_effect", AcidExplosionSubAction)
            subaction_map.put("cast_decay", CastDecaySubAction)
            subaction_map.put("cast_heal", CastHealSubAction)
            subaction_map.put("cast_ice_wall", CastIceWall)

            subaction_map.put("zoom_in", zoomInSubAction)
            subaction_map.put("zoom_out", zoomOutSubAction)
            subaction_map.put("screen_shake", screenShakeAction)

            subaction_map.put("camera_to_center", cameraToCenterAction)
            subaction_map.put("camera_to_actor", cameraToActorAction)
            subaction_map.put("camera_to_target", cameraToTargetAction)
            subaction_map.put("dark", darkAction)
            subaction_map.put("restore_dark", restoreDarkAction)
            subaction_map.put("delay", delaySubAction)
            subaction_map.put("throw_chain", ThrowChainAction)
            subaction_map.put("move_foward", MoveFowardSubAction)
            subaction_map.put("move_scatter", MoveScatterSubAction)
            subaction_map.put("revive", ReviveAction)
            subaction_map.put("undead_revive", UndeadRevive)

            subaction_map.put("add_novice", AddNoviceAction)
            subaction_map.put("add_novice2", AddNovice2Action)
            subaction_map.put("summon jelly", SummonJelly)
            subaction_map.put("summon skeleton", SummonSkeleton)


            subaction_map.put("add_apprentice_mage", AddMageAction)
            subaction_map.put("max_hire_up", MaxHireUpSubAction)
            subaction_map.put("max_mana_up", MaxManaUpSubAction)

            subaction_map.put("show_map", ShowMapSubAction)
            subaction_map.put("double_gold", DoubleGoldSubAction)
            subaction_map.put("half_gold", HalfGoldSubAction)
            subaction_map.put("half_hp", HalfHPSubAction)
            subaction_map.put("full_hp", FullHPSubAction)
            subaction_map.put("steal_gold", StealGoldSubAction)
        }

        val ShowMapSubAction: Function = Function { room, to_act, action, current_target ->
            Global.showBigMessage("MAP REVEALED")
            val prj = ProjectileActor("particles/bless.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 50f
            room.currentRoom.addActor(prj)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.5f), prj.deActivateAndEndAction()
                    )
            )
            room.revealFloorMap()
            -1
        }
        val DoubleGoldSubAction: Function = Function { room, to_act, action, current_target ->
            Global.showBigMessage("You feel richer")
            val prj = ProjectileActor("particles/bless.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 50f
            room.currentRoom.addActor(prj)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.5f), prj.deActivateAndEndAction()
                    )
            )
            Global.getPlayerTeam().increaseGold(150)
            -1
        }
        val HalfGoldSubAction: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().increaseGold(-Global.getPlayerTeam().gold / 2)
            Global.showBigMessage("GOLD HALF")
            val prj = ProjectileActor("particles/curse.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 50f
            room.currentRoom.addActor(prj)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.5f), prj.deActivateAndEndAction()

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )
            -1
        }
        val darkAction: Function = Function { room, to_act, action, current_target ->
            -1
        }
        val restoreDarkAction: Function = Function { room, to_act, action, current_target ->
            -1
        }
        val HalfHPSubAction: Function = Function { room, to_act, action, current_target ->
            //for (ObjActor temp:Global.getPlayerTeam())
            //{

            to_act.inflictDamage((to_act.status.currentHP * .5f).toInt(), null)
            val prj = ProjectileActor("particles/curse.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 50f
            room.currentRoom.addActor(prj)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.5f), prj.deActivateAndEndAction()
                    )
            )
            //}
            Global.showBigMessage("It hurts")
            -1
        }
        val FullHPSubAction: Function = Function { room, to_act, action, current_target ->
            Global.showBigMessage("YOU FEEL REFRESHED")
            //for (ObjActor temp:Global.getPlayerTeam())
            //{
            val prj = ProjectileActor("particles/bless.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 50f
            room.currentRoom.addActor(prj)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.5f), prj.deActivateAndEndAction()

                    )
            )
            to_act.status.fullHP()
            //}
            -1
        }
        val StealGoldSubAction: Function = Function { dungeon, to_act, action, current_target ->
            val targetCell = dungeon.area_computer.targetList.first
            val target=dungeon.currentRoom.getObj(targetCell)

            if (!(target.isWall || target.isObstacle)) {
                var amount=0
                repeat(target.level+1){it -> amount+=Randomizer.nextInt(10,14)}
                dungeon.getTeam(to_act.team).increaseGold(amount)
                Global.showBigMessage("Stole "+amount+"G")
            }

            to_act.addAction(
                    Actions.sequence(
                            to_act.startAction(), Actions.delay(.8f), to_act.deActivateAndEndAction()
                    )
            )

            -1
        }
        val ChargeManaSubAction: Function = Function { room, to_act, action, current_target ->
            val effect = EffectActor(null, "charge_mana", 0f, 0f, null)
            effect.setPosition(to_act.x, to_act.y)
            effect.z = 8f
            effect.setActingAction(action)
            room.currentRoom.addActor(effect)
            effect.start()
            val cast_sound = Assets.getAsset<Sound>("sound/cast_magic.wav", Sound::class.java)
            cast_sound.play()
            -1
        }

        val MaxHireUpSubAction: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().increasMaxHire(1)
            -1
        }
        val MaxManaUpSubAction: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().increaseMaxMana(2)
            -1
        }
        val AddNoviceAction: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().addPlayerandSet("novice_1", room.currentRoom)
            Global.gfs.pushState(10)
            -1
        }
        val AddNovice2Action: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().addPlayerandSet("novice_2", room.currentRoom)
            Global.gfs.pushState(10)
            -1
        }

        val AddMageAction: Function = Function { room, to_act, action, current_target ->
            Global.getPlayerTeam().addPlayerandSet("apprentice_mage", room.currentRoom)
            Global.gfs.pushState(10)
            -1
        }

        val SummonJelly: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val obj = ObjActor(-1, -1, to_act.team, 2, Assets.jobs_map["jelly"], 1)

            obj.setSummonedObj()
            if (to_act.team >= 0)
                room.getTeam(to_act.team).addPlayer(obj)
            room.currentRoom.setObj(target, obj)
            var x = room.currentRoom.map.mapToScreenCoordX(to_act.xx, to_act.yy)
            var y = room.currentRoom.map.mapToScreenCoordY(to_act.xx, to_act.yy)
            obj.setPosition(x, y)
            x = room.currentRoom.map.mapToScreenCoordX(target.xx, target.yy)
            y = room.currentRoom.map.mapToScreenCoordY(target.xx, target.yy)
            obj.addAction(ExtendedActions.moveToParabolic(x, y, obj.z, .3f))
            val seq = SequenceAction(to_act.startAction(), Actions.delay(1f), to_act.deActivateAndEndAction());
            to_act.addAction(seq);
            -1
        }
        val SummonSkeleton: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val obj = ObjActor(-1, -1, to_act.team, 2, Assets.jobs_map["skeleton"], 1)
            obj.setSummonedObj()
            if (to_act.team >= 0)
                room.getTeam(to_act.team).addPlayer(obj)
            room.currentRoom.setObj(target, obj)
            obj.setActingAction(action)
            obj.start()
            obj.addAction(Actions.sequence(obj.setAnimationAction(Constants.DEAD), obj.animationAction(Constants.RAISE, 1, 0f), obj.setAnimationAction(Constants.IDLE), obj.deActivateAndEndAction()
            ))
            -1
        }

        val ReviveAction: Function = Function { room, to_act, action, current_target ->
            if (room.currentRoom.getObj(to_act.xx, to_act.yy) == null) {
                to_act.status.setHPInRatio(.5f)
                to_act.reviveAction(room)
                room.currentRoom.setObj(to_act.xx, to_act.yy, to_act)
            } else {
                val cell = room.currentRoom.getCell(to_act.xx, to_act.yy)
                for (dir in 0..3) {
                    val temp_obj = room.currentRoom.getNearObj(cell, dir)
                    var temp_cell: AreaCell? = null
                    if (temp_obj == null) {
                        temp_cell = room.currentRoom.getNearCell(cell, dir)
                        to_act.status.setHPInRatio(.5f)
                        to_act.reviveAction(room)

                        room.currentRoom.setObj(temp_cell!!.xx, temp_cell!!.yy, to_act)
                        break
                    }
                }
            }
            -1
        }

        val UndeadRevive: Function = Function { room, to_act, action, current_target ->
            val revive_undead = StatusTuple.TURNEFFECT("revive", "etc", 3, to_act, subaction_map.get("revive"), "UNDEAD REVIVE")
            to_act.addTurnEffect(revive_undead)
            -1
        }

        val AttractAction: Function = Function { room, to_act, action, current_target ->
            val target_obj = room.currentRoom.getObj(current_target)
            if (target_obj != null)
                current_target.direction = current_target.getDirectionToTarget(to_act.xx, to_act.yy)
            -1
        }

        val delaySubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            //seq.addAction(to_act.startAction());
            seq.addAction(room.gfsm.pauseGFSAction())
            seq.addAction(Actions.delay(.7f))
            seq.addAction(room.gfsm.reRunGFSAction())
            //seq.addAction(to_act.deActivateAction());
            //seq.addAction(to_act.endActionSubAction());
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }


        val zoomInSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            //seq.addAction(to_act.startAction());
            //seq.addAction(Actions.run(()-> {room.zoom(-0.03f);}));
            seq.addAction(Actions.run { room.zoom(-0.03f) })
            seq.addAction(Actions.run { room.zoom(-0.03f) })
            seq.addAction(Actions.run { room.zoom(-0.03f) })
            seq.addAction(Actions.run { room.zoom(-0.03f) })
            seq.addAction(Actions.run { room.zoom(-0.03f) })
            //seq.addAction(Actions.delay(1f));
            //seq.addAction(to_act.deActivateAction());
            //seq.addAction(to_act.endActionSubAction());
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }
        val zoomOutSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            //seq.addAction(to_act.startAction());
            seq.addAction(Actions.run { room.zoom(+0.03f) })
            seq.addAction(Actions.run { room.zoom(+0.03f) })
            seq.addAction(Actions.run { room.zoom(+0.03f) })
            seq.addAction(Actions.run { room.zoom(+0.03f) })
            seq.addAction(Actions.run { room.zoom(+0.03f) })
            seq.addAction(Actions.run { room.zoom(+0.03f) })

            //seq.addAction(Actions.delay(0.3f));
            //seq.addAction(to_act.deActivateAction());
            //seq.addAction(to_act.endActionSubAction());
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }
        val screenShakeAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            //seq.addAction(to_act.startAction());
            seq.addAction(Actions.run { room.camera.translate(0f, 4f) })
            seq.addAction(Actions.delay(.04f))
            seq.addAction(Actions.run { room.camera.translate(0f, -8f) })
            seq.addAction(Actions.delay(.04f))
            seq.addAction(Actions.run { room.camera.translate(0f, +4f) })

            seq.addAction(Actions.delay(0.3f))
            room.addAction(seq)
            -1
        }
        val cameraToActorAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            seq.addAction(
                    Actions.run { room.screen.camera.position.set(to_act.x, to_act.y, 0f) }
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }
        val cameraToTargetAction: Function = Function { room, to_act, action, current_target ->
            val target_center = room.area_computer.targetList.first
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            seq.addAction(
                    Actions.run { room.screen.camera.position.set(target_center.x, target_center.y, 0f) }
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }
        val cameraToCenterAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            seq.addAction(
                    Actions.run { room.screen.camera.position.set(room.currentMap.map_center_x, room.currentMap.map_center_y, 0f) })
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            //to_act.setActingAction(action);
            -1
        }
        val objMoveToSkill: Function = Function { room, to_act, action, current_target ->
            val move_stack = room.area_computer.targetPath
            //move_stack.pollLast();
            var temp: AreaCell? = null
            //AreaCell before_cell=null;
            //AreaCell before=null;
            val line: LineSegment
            val seq = SequenceAction()

            seq.addAction(to_act.startAction())
            seq.addAction(to_act.changeAnimationSubAction(Constants.WALK))
            do {
                val before_cell = temp
                temp = move_stack.removeAt(move_stack.size - 1)
                if (temp != null) {
                    //	seq.addAction(to_act.changeAnimationSubAction(WALK));
                    val temp_obj = room.currentRoom.getObj(temp)
                    if (temp_obj != null) {
                        seq.addAction(Actions.run {
                            temp_obj.addAction((temp_obj as ObjActor).DodgeAction(to_act.getDirectionToTarget(temp_obj.xx, temp_obj.yy), room))
                            if (before_cell != null && before_cell.device != null) {
                                before_cell.device.activateDevice(Global.dungeon, before_cell)
                            }
                        })
                    }

                    seq.addAction(
                            Actions.parallel(
                                    to_act.headingAndMoveToAction(
                                            room.currentMap.mapToScreenCoordX(temp.xx, temp.yy), room.currentMap.mapToScreenCoordY(temp.xx, temp.yy), temp.z, 0.25f),
                                    Actions.sequence(
                                            Actions.delay(.2f),
                                            Actions.run {
                                                if (before_cell != null && before_cell.device != null && before_cell.device is SpikeTrapActor)
                                                    before_cell.device.action(Global.dungeon, before_cell)
                                            }
                                            //Actions.sequence(
                                            //		Actions.delay(0.1f),
                                            //,to_act.changeMapPositionSubAction(room.getCurrentRoom(),temp.getXX(),temp.getYY())
                                            //		)
                                    )
                            )
                    )
                }


                //before=temp;
            } while (!move_stack.isEmpty())
            if (temp != null)
                seq.addAction(to_act.changeMapPositionSubAction(room.currentRoom, temp.xx, temp.yy))
            seq.addAction(to_act.changeAnimationSubAction(to_act.idleAnimation))
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }
        val objLeapSkill: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val move_x = room.currentMap.mapToScreenCoordX(target.xx, target.yy)
            val move_y = room.currentMap.mapToScreenCoordY(target.xx, target.yy)
            //move_stack.pollLast();
            val temp: AreaCell
            val jump_sound = Assets.getAsset("sound/jump.wav", Sound::class.java)
            val hit_sound = Assets.getAsset("sound/hit2.wav", Sound::class.java)
            jump_sound.play()
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            //seq.addAction(to_act.changeAnimationSubAction(WALK));
            seq.addAction(to_act.setDirectionToCoordAction(move_x, move_y))
            seq.addAction(ExtendedActions.moveToParabolic(move_x, move_y, target.z, to_act.lineDistance(target) / 3f))
            seq.addAction(to_act.changeMapPositionSubAction(room.currentRoom, target.xx, target.yy))
            seq.addAction(Actions.run { hit_sound.play() })
            seq.addAction(to_act.changeAnimationSubAction(to_act.idleAnimation))
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }
        val objFlyMoveSkill: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val move_x = room.currentMap.mapToScreenCoordX(target.xx, target.yy)
            val move_y = room.currentMap.mapToScreenCoordY(target.xx, target.yy)
            //move_stack.pollLast();
            val temp: AreaCell
            //Sound jump_sound= Assets.getAsset("sound/jump.wav", Sound.class);
            //final Sound hit_sound= Assets.getAsset("sound/hit2.wav", Sound.class);
            //jump_sound.play();
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            //seq.addAction(to_act.changeAnimationSubAction(WALK));
            seq.addAction(to_act.setDirectionToCoordAction(move_x, move_y))
            seq.addAction(ExtendedActions.moveTo3D(to_act.x, to_act.y, to_act.z + 50, .5f))
            seq.addAction(ExtendedActions.moveTo3D(move_x, move_y, to_act.z + 50, to_act.lineDistance(target) / 3))
            seq.addAction(ExtendedActions.moveTo3D(move_x, move_y, 0f, .5f))
            seq.addAction(to_act.changeMapPositionSubAction(room.currentRoom, target.xx, target.yy))
            //seq.addAction(Actions.run(new Runnable() {public void run() {hit_sound.play();}}));
            seq.addAction(to_act.changeAnimationSubAction(to_act.idleAnimation))
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }

        val objTeleportSkill: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val move_x = room.currentMap.mapToScreenCoordX(target.xx, target.yy)
            val move_y = room.currentMap.mapToScreenCoordY(target.xx, target.yy)
            //move_stack.pollLast();
            val temp: AreaCell
            //Sound jump_sound= Assets.getAsset("sound/jump.wav", Sound.class);
            val hit_sound = Assets.getAsset("sound/hit.wav", Sound::class.java)
            //jump_sound.play();
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())
            //seq.addAction(to_act.changeAnimationSubAction(WALK));
            seq.addAction(to_act.setDirectionToCoordAction(move_x, move_y))
            //seq.addAction(ExtendedActions.moveToParabolic(move_x, move_y, target.getZ(),to_act.lineDistance(target)/5f));
            seq.addAction(Actions.run { to_act.setPosition(move_x, move_y) })
            seq.addAction(to_act.changeMapPositionSubAction(room.currentRoom, target.xx, target.yy))
            seq.addAction(Actions.run { hit_sound.play() })
            seq.addAction(to_act.changeAnimationSubAction(to_act.idleAnimation))
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }

        val RotatingAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val seq = SequenceAction()
            //seq.addAction(to_act.startAction());
            seq.addAction(to_act.setDirectionAction(Constants.DL))
            seq.addAction(Actions.delay(.02f))
            seq.addAction(to_act.setDirectionAction(Constants.DR))
            seq.addAction(Actions.delay(.02f))
            seq.addAction(to_act.setDirectionAction(Constants.UR))
            seq.addAction(Actions.delay(.02f))
            seq.addAction(to_act.setDirectionAction(Constants.UL))
            seq.addAction(Actions.delay(.02f))
            //seq.addAction(to_act.deActivateAction());
            //seq.addAction(to_act.endActionSubAction());
            to_act.addAction(seq)
            //to_act.setActingAction(action);

            -1
        }
        val ShieldBashAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val seq = SequenceAction()
            val direction_guard = to_act.getDirectionToTarget(first_cell.xx, first_cell.yy)

            seq.addAction(to_act.setDirectionAction(direction_guard))

            val vector_direction = BasicActor.directionToScreenVector(direction_guard).scl(25f)
            seq.addAction(to_act.startAction())
            seq.addAction(Actions.parallel(
                    to_act.animationAction(Constants.GUARD, 1, .8f), Actions.sequence(
                    //Actions.delay(.5f),
                    ExtendedActions.moveToParabolic(to_act.x + vector_direction.x, to_act.y + vector_direction.y, to_act.z, 0.1f),
                    Actions.delay(.2f),
                    to_act.setAnimationAction(Constants.WALK),
                    Actions.moveBy(-vector_direction.x, -vector_direction.y, 0.2f),
                    to_act.setAnimationAction(to_act.idleAnimation)

            )
            )
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            -1
        }
        val PushAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val seq = SequenceAction()
            val direction_guard = to_act.getDirectionToTarget(first_cell.xx, first_cell.yy)
            val sound = Assets.getAsset("sound/slash.wav", Sound::class.java)
            sound.play()
            seq.addAction(to_act.setDirectionAction(direction_guard))
            val vector_direction = BasicActor.directionToScreenVector(direction_guard).scl(15f)
            seq.addAction(to_act.startAction())
            seq.addAction(Actions.parallel(
                    //to_act.animationAction(GUARD, 1,.5f)
                    Actions.sequence(
                            ExtendedActions.moveToParabolic(to_act.x + vector_direction.x, to_act.y + vector_direction.y, to_act.z, 0.1f),
                            Actions.delay(.2f),
                            to_act.setAnimationAction(Constants.WALK),
                            Actions.moveBy(-vector_direction.x, -vector_direction.y, 0.2f),
                            to_act.setAnimationAction(to_act.idleAnimation),
                            Actions.delay(.5f)
                    )
            )
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            -1
        }
        val PushHalfAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val seq = SequenceAction()
            val direction_guard = to_act.getDirectionToTarget(first_cell.xx, first_cell.yy)
            val sound = Assets.getAsset("sound/slash.wav", Sound::class.java)
            sound.play()
            seq.addAction(to_act.setDirectionAction(direction_guard))
            val vector_direction = BasicActor.directionToScreenVector(direction_guard).scl(15f)
            seq.addAction(to_act.startAction())
            seq.addAction(Actions.parallel(
                    //to_act.animationAction(GUARD, 1,.5f)
                    Actions.sequence(
                            ExtendedActions.moveToParabolic(to_act.x + vector_direction.x, to_act.y + vector_direction.y, to_act.z, 0.1f),
                            Actions.delay(.2f),
                            to_act.setAnimationAction(Constants.WALK),
                            Actions.moveBy(-vector_direction.x, -vector_direction.y, 0.2f),
                            to_act.setAnimationAction(to_act.idleAnimation),
                            Actions.delay(.5f)
                    )
            )
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            -1
        }
        val ChargeAction: Function = Function { room, to_act, action, current_target ->
            val last_cell = room.area_computer.targetList.last
            val seq = SequenceAction()
            val direction_guard = to_act.getDirectionToTarget(last_cell.xx, last_cell.yy)
            to_act.setActingAction(action)

            seq.addAction(to_act.setDirectionAction(direction_guard))

            val vector_direction = BasicActor.directionToScreenVector(direction_guard).scl(25f)
            seq.addAction(to_act.startAction())
            seq.addAction(Actions.parallel(
                    to_act.animationAction(Constants.GUARD, 1, .5f), Actions.sequence(
                    ExtendedActions.moveToParabolic(last_cell.x, last_cell.y, last_cell.z, 0.15f),
                    to_act.changeMapPositionSubAction(room.currentRoom, last_cell.xx, last_cell.yy),
                    Actions.delay(.2f),
                    //to_act.setAnimationAction(WALK),
                    //Actions.moveBy(-vector_direction.x, -vector_direction.y, 0.2f),
                    to_act.setAnimationAction(to_act.idleAnimation)

            )
            )
            )
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)

            -1
        }

        val DefendAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val seq = SequenceAction()
            val direction_guard = to_act.getDirectionToTarget(first_cell.xx, first_cell.yy)

            //seq.addAction(to_act.setDirectionAction(direction_guard));

            val vector_direction = BasicActor.directionToScreenVector(direction_guard).scl(15f)
            seq.addAction(Actions.parallel(
                    to_act.changeIdleAnimationSubAction(Constants.GUARD), to_act.changeAnimationSubAction(Constants.GUARD)
            )
            )
            to_act.addAction(Actions.after(seq))
            to_act.status.setAP(0)
            -1
        }
        val DefendReleaseAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            seq.addAction(to_act.changeIdleAnimationSubAction(Constants.IDLE))
            seq.addAction(to_act.changeAnimationSubAction(Constants.IDLE)
                    //to_act.changeAnimationSubAction(GUARD)
            )
            to_act.addAction(Actions.after(seq))
            -1
        }


        val BasicAttackSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            val first_target = room.area_computer.targetList.first
            val direction = to_act.getDirectionToTarget(first_target.xx, first_target.yy)
            seq.addAction(to_act.startAction())
            seq.addAction(to_act.setDirectionAction(direction))
            //seq.addAction(Actions.delay(.3f));
            //seq.addAction(Actions.delay(.3f));
            val multiplier: Int
            val from: Float
            val to: Float
            if (to_act.inventory != null && to_act.inventory.getEquip(2) != null) {

                val rotation_before = to_act.inventory.getEquip(2).rotation
            }

            if (direction == Constants.DL || direction == Constants.UL) {
                from = 0f
                to = 215f
                multiplier = 1
            } else {
                from = 180f
                to = -45f
                multiplier = -1
            }
            if (to_act.inventory.getEquip(2) != null) {
                val hit_sound = Assets.getAsset("sound/slash.wav", Sound::class.java)

                seq.addAction(
                        Actions.sequence(
                                //Actions.delay(.2f),
                                to_act.inventory.getEquip(2).rotateAnimation(from, .2f),
                                Actions.delay(.2f),
                                //to_act.getInventory().getEquip(2).rotateByAnimation(360f,.4f),
                                //Actions.delay(.5f),
                                Actions.parallel(
                                        // dash toward enemy
                                        Actions.run { hit_sound.play(0.2f, 2f, 1f) },
                                        to_act.pulledAction(direction), to_act.animationAction(Constants.ATTACK, 1, 0f), to_act.inventory.getEquip(2).rotateAnimation(from, to, .15f), to_act.deActivateAndEndAction()

                                )// attack animation
                                // rotate weapon
                                , to_act.inventory.getEquip(2).setRotationDirectionAction(direction)


                        )
                )
            } else {
                seq.addAction(
                        Actions.sequence(
                                //Actions.delay(.2f),
                                //to_act.getInventory().getEquip(2).rotateAnimation(from,.1f),
                                Actions.delay(.2f),
                                //to_act.getInventory().getEquip(2).rotateByAnimation(360f,.4f),
                                //Actions.delay(.5f),
                                Actions.parallel(
                                        // dash toward enemy
                                        to_act.pulledAction(direction), to_act.animationAction(Constants.ATTACK, 1, 0f), to_act.deActivateAndEndAction()

                                )// attack animation
                                // rotate weapon
                                //,to_act.getInventory().getEquip(2).rotateAnimation(from, to,.1f)

                                //,to_act.getInventory().getEquip(2).setRotationDirectionAction(direction)


                        )
                )
            }
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }
        val SpearAttackSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            if (room.area_computer.targetList.isEmpty()) return@Function -1
            val first_target = room.area_computer.targetList.first

            val direction = to_act.getDirectionToTarget(first_target.xx, first_target.yy)
            seq.addAction(to_act.startAction())
            seq.addAction(to_act.setDirectionAction(direction))
            //seq.addAction(Actions.delay(.3f));
            //seq.addAction(Actions.delay(.3f));
            //int multiplier;
            //final float from;
            //final float to;
            val spear_dir: Float
            if (to_act.inventory != null && to_act.inventory.getEquip(2) != null) {

                val rotation_before = to_act.inventory.getEquip(2).rotation
            }
            when (direction) {
                Constants.DL -> spear_dir = 200.5f
                Constants.DR -> spear_dir = 337.5f
                Constants.UR -> spear_dir = 22.5f
                Constants.UL -> spear_dir = 157.5f
                else -> spear_dir = 200.5f
            }
            if (to_act.inventory.getEquip(2) != null) {
                seq.addAction(
                        Actions.sequence(
                                //Actions.delay(.2f),
                                to_act.inventory.getEquip(2).rotateAnimation(spear_dir, .2f),
                                Actions.delay(.2f),
                                //to_act.getInventory().getEquip(2).rotateByAnimation(360f,.4f),
                                //Actions.delay(.5f),
                                Actions.parallel(
                                        // dash toward enemy
                                        to_act.pulledAction(direction, 2.8f), to_act.animationAction(Constants.ATTACK, 1, 0f), to_act.deActivateAndEndAction()
                                )// attack animation
                                // rotate weapon
                                , to_act.inventory.getEquip(2).setRotationDirectionAction(direction)


                        )
                )
            }
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }

        val BasicCastSubAction: Function = Function { room, to_act, action, current_target ->
            if (to_act.inventory != null && to_act.inventory.getEquip(2) != null) {
                val seq = SequenceAction()
                //final Sound cast_sound= Assets.getAsset("sound/cast_magic.wav", Sound.class);
                val first_target = room.area_computer.targetList.first

                val direction = to_act.getDirectionToTarget(first_target)

                seq.addAction(to_act.startAction())
                seq.addAction(to_act.setDirectionAction(direction))
                //seq.addAction(Actions.delay(.3f));
                //seq.addAction(Actions.delay(.3f));
                val multiplier: Int
                val from: Float
                val to: Float
                val rotation_before = to_act.inventory.getEquip(2).rotation

                if (direction == Constants.DL || direction == Constants.UL) {
                    from = 45f
                    to = 215f
                    multiplier = 1
                } else {
                    from = 135f
                    to = -45f
                    multiplier = -1
                }
                if (to_act.inventory.getEquip(2) != null)
                    seq.addAction(
                            Actions.sequence(
                                    to_act.inventory.getEquip(2).rotateAnimation(from, .1f),
                                    Actions.delay(.1f),
                                    //Actions.run(new Runnable() {public void run() {cast_sound.play();}}),
                                    //to_act.getInventory().getEquip(2).rotateByAnimation(360f,.4f),
                                    Actions.delay(.3f),
                                    Actions.parallel(
                                            // dash toward enemy
                                            to_act.pulledAction(direction), to_act.animationAction(Constants.ATTACK, 1, 0f), to_act.inventory.getEquip(2).rotateAnimation(from, from + 350, .1f), to_act.deActivateAndEndAction()
                                    )// attack animation
                                    // rotate weapon
                                    , to_act.inventory.getEquip(2).setRotationDirectionAction(direction)


                            )
                    )
                else {
                    to_act.deActivateAndEnd()
                }
                to_act.addAction(seq)
                to_act.setActingAction(action)
            }
            -1
        }
        val SetDirectionSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            val first_target = room.area_computer.targetList.first
            val direction = to_act.getDirectionToTarget(first_target.xx, first_target.yy)
            //seq.addAction(to_act.startAction());
            to_act.direction = direction
            -1
        }

        val ThrowChainAction: Function = Function { room, to_act, action, current_target_area ->
            val target = room.area_computer.targetList.first
            val target_obj = room.currentRoom.getObj(target)
            if (target_obj != null) {
                val target_center = room.area_computer.targetCenter
                val direction = to_act.getDirectionToTarget(target.xx, target.yy)
                val dir_vector = BasicActor.directionToMapVector(direction)
                val target_x = to_act.xx + dir_vector.x.toInt()
                val target_y = to_act.yy + dir_vector.y.toInt()
                val chain = Chain(to_act.x, to_act.y + to_act.z + 7f)
                room.addActor(chain)
                val seq = SequenceAction()
                seq.addAction(Actions.sequence(//target.animationAction(ATTACKED,1,0.0f)
                        to_act.startAction(),
                        chain.extendAndAttachChainAction(room.currentRoom.getObj(target), .2f),
                        to_act.deActivateAction(),
                        Actions.delay(.7f),

                        to_act.endActionSubAction()
                ))
                seq.addAction(Actions.run { chain.remove() })

                to_act.addAction(seq)
                to_act.setActingAction(action)

                //}
            }
            -1
        }
        val MoveFowardSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell target=room.area_computer.getTargetList().getFirst();
            val target_center = room.area_computer.targetCenter
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)
            if (target != null && target.type != "wall") {
                val direction: Int
                if (current_target_area == target_center) {
                    direction = to_act.getDirectionToTarget(target.xx, target.yy)
                } else {
                    direction = target_center.getDirectionToTarget(target.xx, target.yy)
                }
                val dir_vector = BasicActor.directionToMapVector(direction)
                val target_x = to_act.xx + dir_vector.x.toInt()
                val target_y = to_act.yy + dir_vector.y.toInt()
                val i = room.area_computer.targetList.indexOf(current_target_area)
                val cell = room.currentMap.getCell(target_x, target_y)
                val seq = SequenceAction()
                room.currentRoom.changeActorPosition(target, cell.xx, cell.yy)
                seq.addAction(Actions.sequence(//target.animationAction(ATTACKED,1,0.0f)

                        target.startAction(),
                        //target.changeMapPositionSubAction(room.getCurrentRoom(),cell.getXX(),cell.getYY()),
                        ExtendedActions.moveTo3D(cell.x, cell.y, cell.z, .2f),
                        //	Actions.run(new Runnable() {public void run() {
                        //		room.getCurrentRoom().changeActorPosition(target, cell.getXX(), cell.getYY());

                        //	}}),
                        //Actions.delay(1f),
                        target.deActivateAndEndAction()
                ))

                target.addAction(seq)
                target.setActingAction(action)
                room.area_computer.targetList[i] = room.currentMap.getCell(target_x, target_y)
            }
            -1
        }
        val MoveScatterSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell target=room.area_computer.getTargetList().getFirst();
            val target_center = room.area_computer.targetCenter
            //for (int i=room.area_computer.getTargetList().size()-1;i>=0;i--){
            val target: ObjActor?
            if (current_target_area != null)
                target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)
            else
                target = null
            if (target != null && target.type != "wall") {
                val direction: Int
                if (current_target_area == target_center) {

                    val seq = SequenceAction()
                    seq.addAction(Actions.sequence(//target.animationAction(ATTACKED,1,0.0f)
                            target.startAction(),
                            //target.changeMapPositionSubAction(room.getCurrentRoom(),cell.getXX(),cell.getYY()),
                            ExtendedActions.moveToParabolic(target.x, target.y, target.z, .2f),
                            //Actions.delay(1f),
                            target.deActivateAndEndAction()))

                    target.addAction(seq)
                    target.setActingAction(action)
                    return@Function -1

                } else {
                    direction = target_center.getDirectionToTarget(target.xx, target.yy)
                }
                val dir_vector = BasicActor.directionToMapVector(direction)
                val target_x = current_target_area!!.xx + dir_vector.x.toInt()
                val target_y = current_target_area.yy + dir_vector.y.toInt()
                val i = room.area_computer.targetList.indexOf(current_target_area)
                if (room.currentMap.isInFloor(target_x, target_y)) {
                    val cell = room.currentMap.getCell(target_x, target_y)
                    if (room.currentRoom.getObj(cell) == null && !cell.is_blocked) {
                        room.currentRoom.changeActorPosition(target, cell.xx, cell.yy)

                        val seq = SequenceAction()
                        seq.addAction(Actions.sequence(//target.animationAction(ATTACKED,1,0.0f)
                                target.startAction(),
                                //target.changeMapPositionSubAction(room.getCurrentRoom(),cell.getXX(),cell.getYY()),
                                ExtendedActions.moveToParabolic(cell.x, cell.y, cell.z, .2f),
                                //Actions.delay(1f),
                                target.deActivateAndEndAction()))

                        target.addAction(seq)
                        target.setActingAction(action)
                        room.area_computer.targetList[i] = room.currentMap.getCell(target_x, target_y)
                    } else {
                        val blocking_obj = room.currentRoom.getObj(cell)
                        val hit_sound = Assets.getAsset("sound/hit2.wav", Sound::class.java)
                        val seq = SequenceAction()
                        seq.addAction(Actions.sequence(//target.animationAction(ATTACKED,1,0.0f)
                                target.startAction(),
                                //target.changeMapPositionSubAction(room.getCurrentRoom(),cell.getXX(),cell.getYY()),
                                ExtendedActions.moveToParabolic(cell.x, cell.y, cell.z, .2f),
                                Actions.run {
                                    if (target.type != "wall")
                                        target.inflictDamage(3, null)
                                    blocking_obj?.inflictDamage(3, null)
                                })
                        )
                        if (blocking_obj != null) {
                            seq.addAction(
                                    Actions.parallel(
                                            Actions.run { hit_sound.play() },
                                            //target.basicHitAction(room, blocking_obj),
                                            //blocking_obj.basicHitAction(room, to_act),
                                            ExtendedActions.moveTo3D(current_target_area.x, current_target_area.y, current_target_area.z, .2f)
                                    )
                            )
                        } else {
                            seq.addAction(
                                    Actions.parallel(
                                            Actions.run { hit_sound.play() },
                                            //target.basicHitAction(room, blocking_obj),
                                            ExtendedActions.moveTo3D(current_target_area.x, current_target_area.y, current_target_area.z, .2f)
                                    )
                            )

                        }
                        seq.addAction(
                                Actions.sequence(
                                        ExtendedActions.moveToParabolic(current_target_area.x, current_target_area.y, current_target_area.z, .1f),
                                        //Actions.delay(1f),
                                        target.deActivateAndEndAction()
                                ))

                        target.addAction(seq)
                        target.setActingAction(action)

                    }
                }
            }
            //}
            -1
        }

        val MoveBackSubAction: Function = Function { room, to_act, action, current_target_area ->
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)
            if (target != null && target.type != "wall") {
                val direction = to_act.getDirectionToTarget(target.xx, target.yy)
                val dir_vector = BasicActor.directionToMapVector(direction)
                val target_x = current_target_area.xx + dir_vector.x.toInt()
                val target_y = current_target_area.yy + dir_vector.y.toInt()

                val i = room.area_computer.targetList.indexOf(current_target_area)

                if (room.currentMap.isInFloor(target_x, target_y)) {
                    val cell = room.currentMap.getCell(target_x, target_y)
                    val seq = SequenceAction()
                    val par = ParallelAction()
                    seq.addAction(target.startAction())
                    val hit_sound = Assets.getAsset("sound/hit2.wav", Sound::class.java)
                    seq.addAction(Actions.parallel(
                            ExtendedActions.moveTo3D(cell.x, cell.y, cell.z, .2f),
                            Actions.run { hit_sound.play() }
                    ))
                    if (room.currentRoom.getObj(cell) == null && !cell.is_blocked) {
                        room.currentRoom.changeActorPosition(target, cell.xx, cell.yy)
                        room.area_computer.targetList[i] = room.currentMap.getCell(target_x, target_y)

                    } else { // exist an obj to knocked back
                        val blocking_obj = room.currentRoom.getObj(cell)
                        seq.addAction(Actions.run {
                            if (target.type != "wall")
                                target.inflictDamage(3, null)
                            blocking_obj?.inflictDamage(3, null)
                        })
                        par.addAction(Actions.run { hit_sound.play() })
                        //par.addAction(target.basicHitAction(room, blocking_obj));
                        par.addAction(ExtendedActions.moveTo3D(current_target_area.x, current_target_area.y, current_target_area.z, .2f))
                        //if (blocking_obj!=null)
                        //{
                        //	par.addAction(blocking_obj.basicHitAction(room, to_act));
                        //}
                        seq.addAction(par)
                        seq.addAction(Actions.delay(.5f))
                    }
                    seq.addAction(target.deActivateAndEndAction())
                    target.addAction(seq)
                    target.setActingAction(action)

                }
            }
            -1
        }

        val BasicRightAttackSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            val first_target = room.area_computer.targetList.first
            val direction = to_act.getDirectionToTarget(first_target.xx, first_target.yy)
            seq.addAction(to_act.startAction())
            seq.addAction(to_act.setDirectionAction(direction))
            val multiplier: Int
            val from: Float
            val to: Float
            //final float rotation_before= to_act.getInventory().getEquip(3).getRotation();

            if (direction == Constants.DL || direction == Constants.UL) {
                from = 45f
                to = 215f
                multiplier = 1
            } else {
                from = 135f
                to = -45f
                multiplier = -1
            }
            if (to_act.inventory.getEquip(2) != null)
                seq.addAction(
                        Actions.sequence(
                                //Actions.delay(.2f),
                                to_act.inventory.getEquip(3).rotateAnimation(from, .2f),
                                Actions.delay(.2f),
                                //to_act.getInventory().getEquip(2).rotateByAnimation(360f,.4f),
                                //Actions.delay(.5f),
                                Actions.parallel(
                                        // dash toward enemy
                                        to_act.pulledAction(direction), to_act.animationAction(Constants.ATTACK, 1, 0f), to_act.inventory.getEquip(3).rotateAnimation(from, to, .15f), to_act.deActivateAndEndAction()
                                )// attack animation
                                // rotate weapon
                                , to_act.inventory.getEquip(3).setRotationDirectionAction(direction)
                        )
                )
            else {
                to_act.deActivateAndEnd()
            }
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }

        val BasicShootArrowSubAction: Function = Function { room, to_act, action, current_target ->
            val seq = SequenceAction()
            val first_target = room.area_computer.targetList.first
            val direction = to_act.getDirectionToTarget(first_target.xx, first_target.yy)
            seq.addAction(to_act.startAction())
            seq.addAction(to_act.setDirectionAction(direction))
            val from: Float
            when (direction) {
                Constants.DL -> from = 170f
                Constants.DR -> from = -27f
                Constants.UR -> from = 27f
                Constants.UL -> from = 190f
                else -> from = 0f
            }
            seq.addAction(
                    Actions.sequence(
                            Actions.parallel(
                                    // rotate weapon
                                    Actions.sequence(
                                            to_act.inventory.getEquip(3).rotateAnimation(from, .1f), //setRotationAction(from),
                                            Actions.delay(.1f), to_act.deActivateAction(), Actions.delay(1f)
                                    )

                            ), to_act.inventory.getEquip(3).setRotationDirectionAction(direction), to_act.endActionSubAction()
                    )
            )
            to_act.addAction(seq)
            to_act.setActingAction(action)
            -1
        }
        val CastBurnSubAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val prj = ProjectileActor("particles/burn.json", null, 450f, Color(.8f, .0f, .0f, 1f))
            prj.setPosition(room.currentMap.mapToScreenCoordX(first_cell.xx, first_cell.yy),
                    room.currentMap.mapToScreenCoordY(first_cell.xx, first_cell.yy)
            )
            prj.z = 5f
            room.currentRoom.addActor(prj)
            prj.setActingAction(action)
            val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
            hit_sound.play()
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.8f), prj.deActivateAndEndAction()

                    )
            )

            -1
        }
        val CastEnergyRaySubAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.last
            val line = LineSegment(Vector2(to_act.x, to_act.y + to_act.z + 15f), Vector2(first_cell.x, first_cell.y + first_cell.z + 15f), 7f)
            room.addActor(line)
            val prj = ProjectileActor("particles/lightning_ball.json", null, 200f, Color(.3f, .0f, .0f, 1f))
            prj.setPosition(room.currentMap.mapToScreenCoordX(first_cell.xx, first_cell.yy),
                    room.currentMap.mapToScreenCoordY(first_cell.xx, first_cell.yy)
            )
            prj.z = 5f
            room.addActor(prj)
            prj.setActingAction(action)
            val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
            hit_sound.play()
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(.8f), Actions.run { line.remove() }, prj.deActivateAndEndAction()

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )

            -1
        }
        val CastLightningSubAction: Function = Function { room, to_act, action, current_target ->
            val first_cell = room.area_computer.targetList.first
            val line = Lightning(Vector2(first_cell.x, first_cell.y + to_act.z), Vector2(first_cell.x, 1000f), 3f)
            room.addActor(line)
            //final ProjectileActor prj= new ProjectileActor("particles/lightning_ball.json",null,200,new Color(.3f,.0f,.0f,1f));
            //prj.setPosition(room.getCurrentMap().mapToScreenCoordX(first_cell.getXX(),first_cell.getYY()),
            //		room.getCurrentMap().mapToScreenCoordY(first_cell.getXX(),first_cell.getYY())
            //		);
            //prj.setZ(5);
            //room.addActor(prj);
            //prj.setActingAction(action);
            val hit_sound = Assets.getAsset("sound/lightning.wav", Sound::class.java)
            hit_sound.play()
            line.fadeoutLightning()
            line.addAction(
                    Actions.sequence(
                            Actions.delay(.8f), Actions.run { line.remove() }

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )
            this.screenShakeAction.execute(Global.dungeon, null, null, null)
            -1
        }
        val CastDecaySubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            var glowing = true
            val prj2 = ProjectileActor(null, null, 250f, Color(.0f, .3f, .0f, 1f))
            glowing = false
            prj2.setPosition(to_act.x, to_act.y)
            prj2.z = 0f
            val hit_sound = Assets.getAsset("sound/decay.ogg", Sound::class.java)
            hit_sound.play()

            room.currentRoom.addActor(prj2)

            prj2.setActingAction(action)

            prj2.addAction(
                    Actions.sequence(
                            prj2.startAction(), Actions.delay(2f), prj2.deActivateAndEndAction()

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )
            for (target in room.area_computer.targetList) {

                val prj = ProjectileActor("particles/particle8.json", null, 150f, Color(.0f, .3f, .0f, 1f))
                glowing = false
                prj.setPosition(to_act.x, to_act.y)
                prj.z = 15f
                room.currentRoom.addActor(prj)
                prj.setActingAction(action)

                prj.addAction(
                        Actions.sequence(
                                prj.startAction(), Actions.moveTo(room.currentMap.mapToScreenCoordX(target.xx, target.yy), room.currentMap.mapToScreenCoordY(target.xx, target.yy) + 20, 1f), Actions.delay(1f), prj.deActivateAndEndAction()

                                //,prj.endActionSubAction()
                                //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                        )
                )
            }
            -1
        }


        val CastFireBallSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val prj = ProjectileActor("particles/fireball.json", null, 1f, Color(.8f, .0f, .0f, .5f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 10f
            room.currentRoom.addActor(prj)
            prj.setActingAction(action)
            val hit_sound = Assets.getAsset("sound/FlameMagic.ogg", Sound::class.java)
            hit_sound.play(.4f)
            //prj.start();
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.moveTo(room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy), to_act.lineDistance(first_target).toFloat() / prj.speed * 1.2f), prj.deActivateAndEndAction()
                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )

            -1
        }
        val CastAcidBallSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val prj = ProjectileActor("particles/particle8.json", null, 1f, Color(.0f, .3f, .0f, .5f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 10f
            val hit_sound = Assets.getAsset("sound/FlameMagic.ogg", Sound::class.java)
            hit_sound.play()

            room.currentRoom.addActor(prj)
            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.moveTo(room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy), to_act.lineDistance(first_target) / prj.speed * 3f), prj.deActivateAndEndAction()

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )//,Actions.delay(.8f)
            )

            -1
        }
        val ShootFireCannonSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val prj = ProjectileActor("particles/fireball.json", null, 400f, Color(.8f, .0f, .0f, 1f))
            prj.setPosition(to_act.x, to_act.y)
            prj.z = 10f
            room.currentRoom.addActor(prj)
            prj.setActingAction(action)
            val hit_sound = Assets.getAsset("sound/FlameMagic.ogg", Sound::class.java)
            hit_sound.play(.4f)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), ExtendedActions.moveToParabolic(room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy), 0f, to_act.lineDistance(first_target) / prj.speed * 1.2f), Actions.delay(.1f), prj.deActivateAndEndAction()
                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )

            -1
        }

        val ShootArrowSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val first_target_x = room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy)
            val first_target_y = room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy)
            val prj = ProjectileActor(null, "arrow", 0f, null)
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 10
            prj.isVisible = false
            room.currentRoom.addActor(prj)

            val hit_sound = Assets.getAsset("sound/slash.wav", Sound::class.java)
            hit_sound.play()

            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    ExtendedActions.moveToParabolicWithDirection(
                                            room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy) - 5f + 10f * Randomizer.nextFloat(), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy) - 5f + 10f * Randomizer.nextFloat(), first_target.z + 10f * Randomizer.nextFloat() + 5f, 4f / prj.speed),
                                    Actions.sequence(
                                            Actions.delay(.05f),
                                            Actions.run { prj.isVisible = true }

                                    ),
                                    Actions.sequence(
                                            Actions.delay(4f / prj.speed - .15f), prj.setDirectionAction(Constants.UR), prj.deActivateAndEndAction()

                                    )
                            )
                    )
            )

            -1
        }
        val ShootMultipleArrowSubAction: Function = Function { room, to_act, action, current_target ->
            for (first_target in room.area_computer.targetList) {
                //final AreaCell first_target=room.area_computer.getTargetList().getFirst();
                val first_target_x = room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy)
                val first_target_y = room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy)
                val prj = ProjectileActor(null, "arrow", 0f, null)
                prj.setPosition(to_act.x, to_act.y)
                prj.z = 20f
                room.currentRoom.addActor(prj)

                val hit_sound = Assets.getAsset("sound/shoot.ogg", Sound::class.java)
                hit_sound.play()

                prj.setActingAction(action)
                val degree: Float
                val arrow_vector = Vector2(first_target_x - to_act.x, first_target_y - to_act.y)

                prj.rotation = arrow_vector.angle()
                prj.addAction(
                        Actions.sequence(
                                prj.startAction(),
                                Actions.parallel(
                                        ExtendedActions.moveToParabolic(room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy), first_target.z + 15, to_act.lineDistance(first_target) / prj.speed),
                                        Actions.sequence(
                                                Actions.delay(to_act.lineDistance(first_target) / prj.speed - .24f), Actions.sequence(prj.deActivateAndEndAction()))
                                )
                                //,prj.endActionSubAction()
                                //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                        )
                )
            }
            -1
        }


        val CastHealSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val target_obj = room.currentRoom.getObj(first_target)
            val hit_sound = Assets.getAsset("sound/healing_sound.wav", Sound::class.java)
            hit_sound.play(.4f)

            val prj = ProjectileActor("particles/particle_heal.json", null, 100f, Color(.0f, .0f, .3f, 1f))
            prj.setPosition(room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy), room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy))
            prj.z = 40f
            room.currentRoom.addActor(prj)
            prj.setActingAction(action)

            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.delay(1f), prj.deActivateAndEndAction()

                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )
            //	((AbilityActor)action).statusChange(room, to_act, target_obj);

            -1
        }
        val CastMeteorSubAction: Function = Function { room, to_act, action, current_target ->
            val first_target = room.area_computer.targetList.first
            val prj = ProjectileActor("particles/particle_meteor.json", null, 150f, Color(.3f, .0f, .0f, 1f))
            prj.frontBack = 1
            prj.setPosition(to_act.x, to_act.y + 300)
            prj.z = 15f
            room.currentRoom.addActor(prj)
            prj.setActingAction(action)
            val hit_sound = Assets.getAsset("sound/FlameMagic.ogg", Sound::class.java)
            hit_sound.play()

            prj.addAction(
                    Actions.sequence(
                            prj.startAction(), Actions.moveTo(
                            room.currentMap.mapToScreenCoordX(first_target.xx, first_target.yy).toFloat(), (room.currentMap.mapToScreenCoordY(first_target.xx, first_target.yy) + 20).toFloat(), to_act.lineDistance(first_target) * 2f / prj.speed), Actions.delay(.1f), prj.deActivateAndEndAction(), Actions.delay(.3f)
                            //,prj.endActionSubAction()
                            //,Actions.run(new Runnable() {public void run() {prj.remove();}})
                    )
            )
            -1
        }
        val CastIceWall: Function = Function { room, to_act, action, current_target ->
            val target = room.area_computer.targetList.first
            val temp = ObjActor(target.xx, target.yy, -1, 1, Assets.jobs_map["ice_wall"], -1)
            room.currentRoom.setObj(target.xx, target.yy, temp)
            temp.x = target.x
            temp.y = target.y
            temp.z = target.z
            room.currentRoom.addActor(temp)
            -1
        }


        val BasicSlashAttackEffectSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            for (target_area in room.area_computer.targetList) {
                val target = room.currentRoom.getObj(target_area.xx, target_area.yy)

                if (target != null) {

                    val effect = EffectActor(null, "slash", 0f, 0f, null)
                    effect.setPosition(target.x, target.y)
                    effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                    effect.z = 15f
                    effect.setActingAction(action)
                    room.currentRoom.addActor(effect)
                    effect.start()

                }
            }

            -1
        }
        val RotatingAttackEffectSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //ObjActor target=room.getCurrentRoom().getActor(target_area.getXX(),target_area.getYY());
            val hit_sound = Assets.getAsset("sound/slash.ogg", Sound::class.java)
            hit_sound.play(0.2f, 2f, 1f)

            //if (target !=null) {

            val effect = EffectActor(null, "rotating_slash", 0f, 0f, null)
            effect.setPosition(to_act.x, to_act.y)
            //effect.setDirection(to_act.getDirectionToTarget(to_act.getXX(),to_act.getYY()));
            effect.z = 15f
            effect.setActingAction(action)
            room.currentRoom.addActor(effect)
            effect.start()

            //}

            -1
        }
        val BloodEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)

            if (target != null) {
                val effect = EffectActor(null, "hit_effect", 2f, 1f, Color(.2f, .0f, .0f, 1f))

                //final EffectActor effect= new EffectActor("particles/particle4.json",null,2f,100,new Color(.2f,.0f,.0f,1f));
                effect.setPosition(target.x, target.y)
                effect.setScale(1.7f)
                effect.rotation = Randomizer.nextFloat() * 360
                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 12f
                //effect.setActingAction(action);
                //target.addActor(effect);
                room.currentRoom.addActor(effect)
                effect.start()

            }
            //}

            -1
        }
        val AcidHitEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)

            if (target != null) {
                target.color = Color.GREEN
                val effect = EffectActor("particles/particle9.json", null, 2f, 0f, null)
                effect.setPosition(target.x, target.y)
                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 15f
                //effect.setActingAction(action);
                room.currentRoom.addActor(effect)
                effect.start()

            }
            //}

            -1
        }
        val ReleaseAcidEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            //ObjActor target=room.getCurrentRoom().getActor(current_target_area.getXX(),current_target_area.getYY());

            if (to_act != null) {
                to_act.color = Color.WHITE

            }
            //}

            -1
        }

        val FireHitEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)

            if (target != null) {

                val effect = EffectActor("particles/fire_hit.json", null, .5f, 1f, Color(.4f, .0f, .0f, 1f))
                effect.setPosition(target.x, target.y)

                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 15f
                //effect.setActingAction(action);
                room.currentRoom.addActor(effect)
                effect.start()

            }
            //}

            -1
        }
        val LightningHitEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)

            if (target != null) {

                val effect = EffectActor("particles/lightning_hit.json", null, .5f, 1f, Color(.0f, .0f, .2f, 1f))
                effect.setPosition(target.x, target.y)
                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 15f
                //effect.setActingAction(action);
                room.currentRoom.addActor(effect)
                effect.start()

            }
            //}

            -1
        }
        val WoodenHitEffectSubAction: Function = Function { room, to_act, action, current_target_area ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            //for (AreaCell target_area:room.area_computer.getTargetList()){
            val target = room.currentRoom.getObj(current_target_area.xx, current_target_area.yy)

            val effect = EffectActor("particles/wooden_particle.json", null, 2f, 0f, null)
            effect.setPosition(current_target_area.x, current_target_area.y)
            //effect.setDirection(to_act.getDirectionToTarget(target.getXX(),target.getYY()));
            effect.z = 15f
            //effect.setActingAction(action);
            room.currentRoom.addActor(effect)
            effect.start()


            //}

            -1
        }

        val BasicAttackEffectSubAction: Function = Function { room, to_act, action, current_target ->
            for (target_area in room.area_computer.targetList) {
                val target = room.currentRoom.getObj(target_area.xx, target_area.yy)
                if (target != null) {
                    val effect = EffectActor(null, "slash", 1f, 0f, null)
                    effect.setPosition(target.x, target.y)
                    effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                    effect.z = 15f
                    effect.setActingAction(action)

                    room.currentRoom.addActor(effect)
                    effect.start()

                }
            }

            -1
        }
        val showSelectedItemAction: Function = Function { room, to_act, action, current_target ->
            val prj = ProjectileActor(Global.getSelectedEquip())
            val first_target = room.area_computer.targetList.first
            prj.setPosition(first_target.x, first_target.y)
            prj.z = first_target.z + 40
            room.currentRoom.addActor(prj)


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    ExtendedActions.moveToParabolic(first_target.x, first_target.y, first_target.z + 40, .3f),
                                    Actions.sequence(
                                            Actions.delay(to_act.lineDistance(first_target) / prj.speed), prj.setDirectionAction(Constants.UR), prj.deActivateAndEndAction()

                                    )
                            )
                    )
            )

            -1

            /*
         EquipActor equip= Global.selected_equip;
         final Image image= new Image(equip.getInventoryImage());
         SequenceAction seq= new SequenceAction();
         to_act.setActingAction(action);
         seq.addAction(to_act.startAction());
         seq.addAction(Actions.run(new Runnable() {public void run() {
                     to_act.addActor(image);image.setPosition(-20, 30);
                     image.addAction(ExtendedActions.moveBy(0, 5,.3f));}}));
         seq.addAction(Actions.delay(.6f));
         seq.addAction(Actions.run(new Runnable() {public void run() {image.remove();}}));
         seq.addAction(to_act.deActivateAction());
         seq.addAction(to_act.endActionSubAction());
         to_act.addAction(seq);
         */
            //return -1;
        }
        val throwSelectedItemAction: Function = Function { room, to_act, action, current_target ->
            val equip = Global.getSelectedEquip()

            val prj = ProjectileActor(equip)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            val dir = to_act.getDirectionToTarget(first_target)
            val arrow_vector = Vector2(first_target.x - to_act.x, first_target.y - to_act.y)
            when (dir) {
                Constants.DL, Constants.UL -> prj.rotation = arrow_vector.angle() + 30
                Constants.DR, Constants.UR -> prj.rotation = arrow_vector.angle() - 30
            }
            //prj.setRotation(degrees);
            prj.z = to_act.z + 30
            room.currentRoom.addActor(prj)


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    Actions.rotateBy(720f, .5f),
                                    ExtendedActions.moveToParabolic(first_target.x, first_target.y, first_target.z + 10, .5f),
                                    Actions.sequence(
                                            Actions.delay(.5f), prj.deActivateAndEndAction(), Actions.delay(.5f)
                                            /*	,Actions.run(
                                                 new Runnable() {
                                                     public void run() {
                                                          Global.current_room.setItem(first_target.getXX(),first_target.getYY(),equip,false);
                                                     }
                                                 }

                                         )*/
                                    )//prj.setDirectionAction(UR)
                            )
                    )
            )

            -1
        }
        val throwStoneAction: Function = Function { room, to_act, action, current_target ->
            val prj = ProjectileActor(null, "stone", 0f)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 20
            room.currentRoom.addActor(prj)


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    Actions.rotateBy(720f, .5f),
                                    ExtendedActions.moveToParabolic(first_target.x, first_target.y, first_target.z + 10, .5f),
                                    Actions.sequence(
                                            Actions.delay(.5f), prj.deActivateAndEndAction()//, Actions.delay(.5f)
                                            /*	,Actions.run(
                                                 new Runnable() {
                                                     public void run() {
                                                          Global.current_room.setItem(first_target.getXX(),first_target.getYY(),equip,false);
                                                     }
                                                 }

                                         )*/
                                    )//prj.setDirectionAction(UR)
                            )
                    )
            )

            -1
        }
        val throwWebAction: Function = Function { room, to_act, action, current_target ->
            val prj = ProjectileActor(null, "web", 0f, false)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z
            room.currentRoom.addActor(prj)
            (to_act.selectedAction.action as AbilityActor).reserveEffect(prj)
            val hit_sound = Assets.getAsset("sound/slash.wav", Sound::class.java)
            hit_sound.play()
            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    ExtendedActions.moveToParabolic(first_target.x, first_target.y, first_target.z + 3, .3f), Actions.sequence(
                                    Actions.delay(.3f), prj.deActivateAction(), prj.setFrontBackAction(-1)
                            )
                            )
                    )
            )

            -1
        }
        val throwBombAction: Function = Function { room, to_act, action, current_target ->
            val equip = Global.getSelectedEquip()

            val prj = ProjectileActor(null, "bomb", .5f)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 20
            room.currentRoom.addActor(prj)


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    Actions.rotateBy(720f, .5f),
                                    ExtendedActions.moveToParabolic(first_target.x, first_target.y, first_target.z + 10, .5f),
                                    Actions.sequence(
                                            Actions.delay(.5f), prj.deActivateAndEndAction(), Actions.delay(.5f)
                                            /*	,Actions.run(
                                                 new Runnable() {
                                                     public void run() {
                                                          Global.current_room.setItem(first_target.getXX(),first_target.getYY(),equip,false);
                                                     }
                                                 }

                                         )*/
                                    )//prj.setDirectionAction(UR)
                            )
                    )
            )

            -1
        }

        val throwWeaponStraightAction: Function = Function { room, to_act, action, current_target ->
            val equip = to_act.inventory.getEquip(2)
            to_act.inventory.setEquip(2, null)
            val prj = ProjectileActor(equip)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 20
            room.currentRoom.addActor(prj)
            val arrow_vector = Vector2(first_target.x - to_act.x, first_target.y - to_act.y)

            prj.rotation = arrow_vector.angle()


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.sequence(
                                    ExtendedActions.moveTo3D(first_target.x, first_target.y, first_target.z + 10, .3f),
                                    ExtendedActions.moveTo3D(to_act.x, to_act.y, first_target.z + 10, .3f),
                                    Actions.sequence(
                                            Actions.delay(.5f), prj.deActivateAndEndAction(), Actions.run { to_act.inventory.setEquip(2, equip) }

                                    )//prj.setDirectionAction(UR)
                                    //,Actions.delay(.5f)
                            )
                    )
            )

            -1
        }
        val throwSelectedItemStraightAction: Function = Function { room, to_act, action, current_target ->
            val equip = Global.getSelectedEquip()
            val prj = ProjectileActor(equip)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 20
            room.currentRoom.addActor(prj)
            val arrow_vector = Vector2(first_target.x - to_act.x, first_target.y - to_act.y)

            prj.rotation = arrow_vector.angle()


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    ExtendedActions.moveTo3D(first_target.x, first_target.y, first_target.z + 10, to_act.lineDistance(first_target) / 10f),
                                    Actions.sequence(
                                            Actions.delay(to_act.lineDistance(first_target) / 10f - .05f), prj.deActivateAndEndAction()
                                            /*,Actions.delay(.5f)
                                         ,Actions.run(
                                                 new Runnable() {
                                                     public void run() {
                                                          Global.current_room.setItem(first_target.getXX(),first_target.getYY(),equip,false);
                                                     }
                                                 }

                                         )*/

                                    )//prj.setDirectionAction(UR)
                            )
                    )
            )

            -1
        }
        val throwDagger: Function = Function { room, to_act, action, current_target ->
            val prj = ProjectileActor(null, "throwing_dagger", .3f)
            val first_target = room.area_computer.targetList.first
            prj.setPosition(to_act.x, to_act.y)
            prj.z = to_act.z + 20
            room.currentRoom.addActor(prj)
            val arrow_vector = Vector2(first_target.x - to_act.x, first_target.y - to_act.y)

            prj.rotation = arrow_vector.angle()


            prj.setActingAction(action)
            prj.addAction(
                    Actions.sequence(
                            prj.startAction(),
                            Actions.parallel(
                                    ExtendedActions.moveTo3D(first_target.x, first_target.y, first_target.z + 10, to_act.lineDistance(first_target) / 10f),
                                    Actions.sequence(
                                            Actions.delay(to_act.lineDistance(first_target) / 10f - .05f), prj.deActivateAndEndAction()
                                            /*,Actions.delay(.5f)
                                         ,Actions.run(
                                                 new Runnable() {
                                                     public void run() {
                                                          Global.current_room.setItem(first_target.getXX(),first_target.getYY(),equip,false);
                                                     }
                                                 }

                                         )*/

                                    )//prj.setDirectionAction(UR)
                            )
                    )
            )

            -1
        }

        val ExplosionSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            for (target in room.area_computer.targetList) {
                //ObjActor target=room.current_room.getActor(target_area.getXX(),target_area.getYY());
                //if (target !=null) {
                val effect = EffectActor("particles/particle_explode.json", null, .5f, 1f, Color(.1f, .0f, .0f, 1f))
                effect.setPosition(room.currentMap.mapToScreenCoordX(target.xx, target.yy),
                        room.currentMap.mapToScreenCoordY(target.xx, target.yy))
                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 15f
                effect.setActingAction(action)
                effect.start()
                effect.deActivate()
                val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
                hit_sound.play()
                room.currentRoom.addActor(effect)
                effect.start()
                effect.deActivate()

                //}
            }

            -1
        }

        val ChainLightningSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            val seq = SequenceAction()
            val line_list = LinkedList<Lightning>()
            val proj_list = LinkedList<ProjectileActor>()
            seq.addAction(to_act.startAction())
            val line = Lightning(Vector2(to_act.x, to_act.y + to_act.z + 15f), Vector2(room.area_computer.targetList.first.x, room.area_computer.targetList.first.y + room.area_computer.targetList.first.z), 3f)
            room.addActor(line)
            line_list.add(line)
            //ProjectileActor prj0= new ProjectileActor("particles/lightning_ball.json",null,200,new Color(.3f,.0f,.0f,1f));
            //prj0.setPosition(room.area_computer.getTargetList().getFirst().getX(),room.area_computer.getTargetList().getFirst().getY());
            //prj0.setZ(room.area_computer.getTargetList().getFirst().getZ());
            //room.addActor(prj0);
            //proj_list.add(prj0);
            val hit_sound = Assets.getAsset("sound/lightning.wav", Sound::class.java)
            hit_sound.play()
            this.screenShakeAction.execute(Global.dungeon, null, null, null)


            for (i in 0..room.area_computer.targetList.size - 1) {
                val target = room.area_computer.targetList[i]
                val before: AreaCell?
                val line2: Lightning?
                val prj: ProjectileActor?
                if (i != 0) {
                    before = room.area_computer.targetList[i - 1]
                    line2 = Lightning(Vector2(target.x, target.y + target.z), Vector2(before!!.x, before.y + before.z), 3f)
                    line_list.add(line2)
                    //prj= new ProjectileActor("particles/lightning_ball.json",null,200,new Color(.3f,.0f,.0f,1f));
                    //prj.setPosition(target.getX(),target.getY());
                    //prj.setZ(target.getZ());
                    //proj_list.add(prj);
                } else {
                    before = null
                    line2 = null
                    prj = null
                }


                seq.addAction(
                        Actions.run {
                            if (before != null) {
                                room.addActor(line2!!)
                                //if (prj!=null)
                                //room.addActor(prj);
                                //prj.start();
                                //Sound hit_sound= Assets.getAsset("sound/lightning.wav", Sound.class);
                                hit_sound.play()
                                this.screenShakeAction.execute(Global.dungeon, null, null, null)

                            }
                        }
                )
                seq.addAction(Actions.delay(.08f))

                //ObjActor target=room.current_room.getActor(target_area.getXX(),target_area.getYY());
                //if (target !=null) {

                //}
            }
            seq.addAction(Actions.delay(.5f))

            val line_list_ptr = line_list
            val prj_list_ptr = proj_list

            seq.addAction(
                    Actions.run {
                        for (temp in line_list_ptr) {
                            temp.fadeoutLightning()
                            //temp.remove()	;

                        }
                        for (proj in prj_list_ptr) {
                            //proj.deActivate();
                            proj.end()
                        }
                    }
            )

            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)


            -1
        }

        val SequencialExplosionSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            val seq = SequenceAction()
            seq.addAction(to_act.startAction())

            for (target in room.area_computer.targetList) {
                seq.addAction(
                        Actions.run {
                            //if (before!=null) {
                            //LineSegment line= new LineSegment(new Vector2(temp.getX(),temp.getY()),new Vector2(before.getX(),before.getY()),3);
                            //	room.addActor(line);
                            //}
                            val effect = EffectActor("particles/particle_explode.json", null, .5f, 1f, Color(.1f, .0f, .0f, 1f))
                            effect.setPosition(room.currentMap.mapToScreenCoordX(target.xx, target.yy),
                                    room.currentMap.mapToScreenCoordY(target.xx, target.yy))
                            effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                            effect.z = 15f
                            effect.setActingAction(action)
                            effect.start()
                            val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
                            hit_sound.play()
                            room.currentRoom.addActor(effect)
                            effect.start()
                        }
                )
                seq.addAction(Actions.delay(.3f))

                //ObjActor target=room.current_room.getActor(target_area.getXX(),target_area.getYY());
                //if (target !=null) {

                //}
            }
            seq.addAction(to_act.deActivateAndEndAction())
            to_act.addAction(seq)
            to_act.setActingAction(action)


            -1
        }

        val AcidExplosionSubAction: Function = Function { room, to_act, action, current_target ->
            //final AreaCell first_target=to_act.getTargetList().getFirst();
            for (target in room.area_computer.targetList) {
                //ObjActor target=room.current_room.getActor(target_area.getXX(),target_area.getYY());
                //if (target !=null) {
                val effect = EffectActor("particles/acid_explosion.json", null, .5f, 1f, Color(.1f, .0f, .0f, 1f))
                effect.setPosition(room.currentMap.mapToScreenCoordX(target.xx, target.yy),
                        room.currentMap.mapToScreenCoordY(target.xx, target.yy))
                effect.direction = to_act.getDirectionToTarget(target.xx, target.yy)
                effect.z = 15f
                effect.setActingAction(action)
                effect.start()
                effect.deActivate()
                val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
                hit_sound.play()
                room.currentRoom.addActor(effect)
                effect.start()
                effect.deActivate()


                //}
            }

            -1
        }

        val Explode: Function = Function { room, to_act, action, current_target ->
            val center_xx = to_act.xx
            val center_yy = to_act.yy
            //subaction_map["wooden_hit"]?.execute(room, to_act, null, room.currentMap.getCell(center_xx, center_yy))
            for (dxx in -1..1) {
                for (dyy in -1..1) {
                    val xx = center_xx + dxx
                    val yy = center_yy + dyy
                    if (room.currentMap.isMovableAndNoDeviceFloor(xx, yy)) {
                        val effect = EffectActor("particles/particle_explode.json", null, .5f, 1f, Color(.1f, .0f, .0f, 1f))
                        effect.setPosition(room.currentMap.mapToScreenCoordX(xx, yy),
                                room.currentMap.mapToScreenCoordY(xx, yy))
                        effect.direction = to_act.getDirectionToTarget(xx, yy)
                        effect.z = 15f
                        effect.start()
                        val hit_sound = Assets.getAsset("sound/sound_explosion.wav", Sound::class.java)
                        hit_sound.play()
                        room.currentRoom.addActor(effect)
                        val target = room.currentRoom.getObj(xx, yy)
                        if (target != null) {
                            val seq = SequenceAction()
                            val direction = to_act.getDirectionToTarget(target.xx, target.yy)

                            var multiplier = 1

                            if (direction == Constants.DR || direction == Constants.UR) multiplier = -1
                            val pause_id = 0
                            //seq.addAction(target.basicHitAction(room, to_act));


                            target.addAction(seq)
                            if (target.type != "wall")
                                if (target.status.total_status.HP * .3f > 8)
                                    target.inflictDamageInRatio(.3f, null)
                                else
                                    target.inflictDamage(8, null)


                        }
                        effect.start()

                    }

                }
            }

            this.screenShakeAction.execute(room, to_act, null, null)

            -1
        }

    }
}
