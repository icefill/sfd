package com.icefill.game.actors;

import com.badlogic.gdx.Gdx;


import com.icefill.game.Constants;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.devices.DeviceActor;
import com.icefill.game.sprites.BasicSprites;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import org.jetbrains.annotations.NotNull;

public class BasicActor extends Group implements Constants {
    public int xx, yy;    //tile_coord of actor
    public float z = 0;
    protected DIR curr_dir = DIR.DL;
    public float elapsed_time = 0.0f;

    private int front_back = 0;
    protected boolean pause = false;
    protected boolean attached = false;
    protected BasicActor to_attach;
    protected float attach_anchor_x;
    protected float attach_anchor_y;
    protected float attach_anchor_z;
    protected float attach_anchor_rot;

    protected BasicActor self = this;

    protected boolean acting = false;
    private boolean active = false;
    public BasicSprites sprites;
    protected String sprites_name;
    ActionActor acting_action;

    public void setActingAction(ActionActor action) {
        this.acting_action = action;
    }

    public void releaseActingAction() {
        this.acting_action = null;
    }

    public void setFrontBack(int flag) {
        this.front_back = flag;
        //Global.getCurrentRoom().sortActors();
    }

    public Action setFrontBackAction(final int flag) {
        return Actions.run(new Runnable() {
            public void run() {
                setFrontBack(flag);
            }
        });
    }

    public int getFrontBack() {
        return front_back;
    }


    public void setPosition(float x, float y, float z) {
        this.setPosition(x, y);
        this.setZ(z);
    }

    public void setXX(int xx) {
        this.xx = xx;
    }

    ;

    public void setYY(int yy) {
        this.yy = yy;
    }

    ;

    public int getXX() {
        return xx;
    }

    ;

    public int getYY() {
        return yy;
    }

    ;

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public BasicActor() {
        // TODO Auto-generated constructor stub
    }

    public void attach(BasicActor to_attach) {
        this.to_attach = to_attach;
        attach_anchor_x = this.getX() - to_attach.getX();
        attach_anchor_y = this.getY() - to_attach.getY();
        attach_anchor_z = this.getZ() - to_attach.getZ();
        attach_anchor_rot = this.getRotation() - to_attach.getRotation();
        attached = true;

    }

    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        if (pause == false)
            elapsed_time += Gdx.graphics.getDeltaTime();
    }

    public void start() {
        self.acting = true;
        self.active = true;
        if (acting_action != null) {
            //	acting_action.addActingActor(this);
            acting_action.addActorCount();
        }
    }

    public void deActivate() {
        self.active = false;
        if (acting_action != null) {
            acting_action.subtractActorCount();
        }
    }

    public void end() {
        self.acting = false;
    }

    public void deActivateAndEnd() {
        deActivate();
        this.addAction(this.endActionSubAction());
    }

    public boolean isActive() {
        return active;
    }

    public boolean isActing() {
        return acting;
    }


    public Action startAction() {
        return Actions.run(new Runnable() {
            public void run() {
                self.start();
            }
        });
    }

    public Action deActivateAction() {
        return Actions.run(new Runnable() {
            public void run() {
                self.deActivate();
            }
        });
    }

    public Action deActivateAndEndAction() {
        return Actions.run(new Runnable() {
            public void run() {
                self.deActivateAndEnd();
            }
        });
    }

    public Action endActionSubAction() {
        return Actions.run(new Runnable() {
            public void run() {
                self.end();
            }
        });
    }

    public void hide() {

    }

    public Action pauseAnimationStart() {
        return Actions.run(new Runnable() {
            public void run() {
                self.pause = true;
            }
        });
    }

    public Action pauseAnimationEnd() {
        return Actions.run(new Runnable() {
            public void run() {
                self.pause = false;
            }
        });
    }

    public Action pauseAnimation(float time) {
        return Actions.sequence(
                pauseAnimationStart()
                , Actions.fadeIn(time)
                , pauseAnimationEnd()
        );
    }

    public Action addActionToOtherActor(final Actor to_add, final Action adding_action) {
        return Actions.run(new Runnable() {
            public void run() {
                adding_action.setActor(to_add);
                to_add.addAction(adding_action);
            }
        });
    }


    /*********************** Action related Direction  *******************************/
    public void setDirection(DIR dir) {
        if ((sprites != null && sprites.hasDirection()) || this instanceof DeviceActor) {
            if (!curr_dir.equals(dir)) curr_dir = dir;
        }
    }

    public DIR getDirection() {
        return curr_dir;
    }

     public DIR getDirectionToTarget(int target_xx, int target_yy) {
        int dxx = target_xx - getXX();
        int dyy = target_yy - getYY();
        if (dxx == 0 && dyy == 0)
            return curr_dir;
        else if (dyy > dxx) {
            if (dyy > -dxx) return DIR.DL;
            else return DIR.UL;
        } else if (dyy > -dxx) return DIR.DR;
        else return DIR.UR;
    }

    public FACING getTargetFacing(ObjActor target) {
        if (target.job != null && target.job.no_direction) return FACING.FRONT;
        DIR back = (getDirectionToTarget(target.getXX(), target.getYY()));
        DIR front = back.opposite();
        DIR left = front.turnLeft(1);
        DIR right = front.turnRight(1);
        DIR target_dir=target.getDirection();
        if (target_dir.equals(front)) return FACING.FRONT;
        else if (target_dir.equals(left)) return FACING.LEFT;
        if (target_dir.equals(right)) return FACING.RIGHT;
        if (target_dir.equals(back)) return FACING.BACK;
        else throw new RuntimeException("getTargetFacing function failed.");
    }

    public void setDirectionToTarget(float newx, float newy) {
        if (sprites.hasDirection()) {
            setDirection(getDirectionToTarget(newx, newy));
        }
    }

    public void setDirectionToTarget(int newxx, int newyy) {
        if (sprites.hasDirection()) {
            setDirection(getDirectionToTarget(newxx, newyy));
        }
    }

    public DIR getDirectionToTarget(float newx, float newy) {
        DIR dir;
        if (newx - getX() > 0) {
            if (newy - getY() > 0) {
                dir = DIR.UR;
            } else {
                dir = DIR.DR;
            }
        } else {
            if (newy - getY() > 0) {
                dir = DIR.UL;
            } else {
                dir = DIR.DL;
            }
        }
        return dir;
    }

    public Action headingAndMoveToAction(final float newx, final float newy, final float newz, float duration) {
        SequenceAction seq = new SequenceAction();
        seq.addAction(((ObjActor) self).setDirectionToCoordAction(newx, newy));
        seq.addAction(Actions.moveTo(newx, newy, duration));
        return seq;
    }

    public Action setDirectionAction(final DIR direction) {
        return Actions.run(new Runnable() {
            public void run() {
                self.setDirection(direction);
            }
        });
    }

    public Action setDirectionToCoordAction(final float x, final float y) {
        return Actions.run(new Runnable() {
            public void run() {
                self.setDirectionToTarget(x, y);
            }
        });
    }


}
