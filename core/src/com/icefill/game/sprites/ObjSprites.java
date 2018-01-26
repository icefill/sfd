package com.icefill.game.sprites;


import java.util.LinkedList;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.icefill.game.Assets;
import com.icefill.game.actors.windows.PersonalInventory;

import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ObjSprites extends BasicSprites {
    boolean body_equippable;
    //Sound foot1= Assets.getAsset("sound/footstep.wav", Sound.class);
    boolean equippable;
    int shield_dir = 0;
    boolean sound_run;
    Texture shadow;
    public Animation[][][] animation;
    boolean has_animation[];
    int head_coord[][][][];
    int body_coord[][][][];
    int arm2_coord[][][][];
    int arm1_coord[][][][];
    float height;
    float width;

    public ObjSprites(Factory factory, String atlas_path) {
        this.anchor_x = factory.anchor_x;
        this.anchor_y = factory.anchor_y;
        this.equippable = factory.equippable;
        this.body_equippable = factory.body_equippable;
        this.has_direction = factory.has_direction;
        this.head_coord = factory.head_coord;
        this.body_coord = factory.body_coord;
        this.arm2_coord = factory.arm2_coord;
        this.arm1_coord = factory.arm1_coord;
        TextureAtlas atlas = (TextureAtlas) Assets.getAsset(atlas_path, TextureAtlas.class);
        shadow = Assets.getAsset("sprite/shadow.png", Texture.class);

        readAnimationFromAtlas(atlas);
        //makeDeadAnimation();

        height = animation[0][0][0].getKeyFrame(0).getRegionHeight();
        width = animation[0][0][0].getKeyFrame(0).getRegionHeight();
    }

    public boolean hasAnimation(int ani) {
        return has_animation[ani];
    }

    public void setAnimationReverse(int state) {
        int n_body;
        if (body_equippable) {
            n_body = 4;
        } else {
            n_body = 1;
        }
        for (int dir = 0; dir < 4; dir++) {
            for (int body_parts = 0; body_parts < n_body; body_parts++) {
                if (animation[state][dir][body_parts] != null) ;
                animation[state][dir][body_parts].setPlayMode(PlayMode.REVERSED);
            }
        }
    }

    public void setAnimationNormal(int state) {
        Animation.PlayMode to_set;
        if (state == DEAD) to_set = PlayMode.NORMAL;
        else to_set = PlayMode.LOOP;


        for (int dir = 0; dir < 4; dir++) {
            for (int body_parts = 0; body_parts < 4; body_parts++) {
                if (animation[state][dir][body_parts] != null) ;
                animation[state][dir][body_parts].setPlayMode(to_set);
            }
        }


    }

    public Texture getShadow() {
        return shadow;
    }

    public boolean isEquippable() {
        return equippable;
    }

    public float getSpritesDuration(int anim, int direction) {
        if (!has_direction)
            direction = 0;
        return animation[anim][direction][HEAD].getAnimationDuration();
    }

    public void draw(Batch batch, float delta) {

    }

    public void drawAnimation(Batch batch, float elapsed_time, int anim, int direction, float x, float y, float origin_x, float origin_y, float rotation, float scalex, float scaley, PersonalInventory inventory, Color color) {
        if (!equippable) {

            if (anim != IDLE && anim != DEAD)
                anim = IDLE;
            batch.setColor(color);
            if ((anim == DEAD || anim == RAISE) && animation[anim][0][0] != null)
                batch.draw(animation[anim][direction][0].getKeyFrame(elapsed_time, false), x - anchor_x, y - anchor_y);
            else {
                batch.draw(animation[anim][direction][0].getKeyFrame(elapsed_time, true), x - anchor_x, y - anchor_y);
            }
            batch.setColor(1f, 1f, 1f, 1f);
        } else if ((anim == DEAD || anim == RAISE) && animation[anim][0][0] != null) {
            //batch.setColor(color);
            //if (!body_equippable)
            batch.draw(animation[anim][direction][0].getKeyFrame(elapsed_time, false), x - anchor_x, y - anchor_y);
            //batch.setColor(color);
        } else if (body_equippable) {
            int index = animation[anim][direction][HEAD].getKeyFrameIndex(elapsed_time);
            if (inventory.getEquip(2) != null && (inventory.getEquip(2).getType() != 6)) {
                inventory.getEquip(2).setPosition(x + this.arm2_coord[anim][direction][index][0] - anchor_x, y + this.arm2_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(2).drawRotatableAnimation(batch, elapsed_time, anim, direction);
            }
            if ((anim == GUARD) && (inventory.getEquip(3) != null) && (direction == UL || direction == UR)) {

                inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, direction);
            }


            batch.setColor(color);
            batch.draw(animation[anim][direction][BODY].getKeyFrame(elapsed_time, true), x - anchor_x, y - anchor_y);
            batch.setColor(1f, 1f, 1f, 1f);

            if (inventory.getEquip(1) != null) {
                inventory.getEquip(1).setPosition(x + this.body_coord[anim][direction][index][0] - anchor_x, y + this.body_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(1).drawAnimation(batch, elapsed_time, anim, direction);

            }
            batch.setColor(color);
            batch.draw(animation[anim][direction][HEAD].getKeyFrame(elapsed_time, true), x - anchor_x, y - anchor_y);
            batch.setColor(1f, 1f, 1f, 1f);
            if (inventory.getEquip(0) != null) {
                inventory.getEquip(0).setPosition(x + this.head_coord[anim][direction][index][0] - anchor_x, y + this.head_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(0).drawAnimation(batch, elapsed_time, anim, direction);

            }
            if (anim == GUARD) {
                if (inventory.getEquip(3) != null && (direction == DL || direction == DR)) {
                    inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                    inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, direction);

                }
            } else {
                if (inventory.getEquip(3) != null) {
                    if (inventory.getEquip(3).getType() == 3) {
                        inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                        switch (direction) {

                            case DL:
                            case UR:
                                shield_dir = DR;
                                break;
                            default:
                                shield_dir = DL;
                                break;
                        }
                        inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, shield_dir);
                    } else if (inventory.getEquip(3).getType() != 6) {
                        inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                        inventory.getEquip(3).drawRotatableAnimation(batch, elapsed_time, anim, direction);
                    }

                }
            }

        } else {
            int index;
            index = animation[anim][direction][0].getKeyFrameIndex(elapsed_time);
	         if (inventory.getEquip(2) != null && (inventory.getEquip(2).getType() != 6)) {
                inventory.getEquip(2).setPosition(x + this.arm2_coord[anim][direction][index][0] - anchor_x, y + this.arm2_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(2).drawRotatableAnimation(batch, elapsed_time, anim, direction);
            }

            if ((anim == GUARD) && (inventory.getEquip(3) != null) && (direction == UL || direction == UR)) {

                inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, direction);
            }

            batch.setColor(color);
            batch.draw(animation[anim][direction][0].getKeyFrame(elapsed_time, true), x - anchor_x, y - anchor_y);
            batch.setColor(1f, 1f, 1f, 1f);
            if (inventory.getEquip(0) != null) {
                inventory.getEquip(0).setPosition(x + this.head_coord[anim][direction][index][0] - anchor_x, y + this.head_coord[anim][direction][index][1] - anchor_y);
                inventory.getEquip(0).drawAnimation(batch, elapsed_time, anim, direction);

            }

            if (anim == GUARD) {
                if (inventory.getEquip(3) != null && (direction == DL || direction == DR)) {
                    inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                    inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, direction);

                }
            } else {
                if (inventory.getEquip(3) != null) {
                    if (inventory.getEquip(3).getType() == 3) {
                        inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                        switch (direction) {

                            case DL:
                            case UR:
                                shield_dir = DR;
                                break;
                            default:
                                shield_dir = DL;
                                break;
                        }
                        inventory.getEquip(3).drawAnimation(batch, elapsed_time, anim, shield_dir);
                    } else if ((inventory.getEquip(3).getType() != 6)) {
                        inventory.getEquip(3).setPosition(x + this.arm1_coord[anim][direction][index][0] - anchor_x, y + this.arm1_coord[anim][direction][index][1] - anchor_y);
                        inventory.getEquip(3).drawRotatableAnimation(batch, elapsed_time, anim, direction);
                    }

                }
            }

        }

        batch.setColor(1f, 1f, 1f, 1f);
    }

    public Animation getRepresentativeAnimations() {
        return animation[0][0][0];
    }

    public void readAnimationFromAtlas(TextureAtlas atlas) {
        Queue<TextureRegion> temp_frames = new LinkedList<TextureRegion>();
        TextureRegion temp_region;
        int n_bodyparts;
        int direction_adder;
        String region_name;
        if (body_equippable) {
            n_bodyparts = N_BODYPARTS;
            animation = new Animation[N_ANIMATION][N_DIRECTION][N_BODYPARTS];
        } else {
            n_bodyparts = 1;
            animation = new Animation[N_ANIMATION][N_DIRECTION][1];
        }
        has_animation = new boolean[N_ANIMATION];
        if (is_symmetric) direction_adder = 2;
        else direction_adder = 1;
        int index = 0;
        for (int ani = 0; ani < N_ANIMATION; ani++) {
            for (int dir = 0; dir < N_DIRECTION; dir += direction_adder) {
                for (int bodyparts = 0; bodyparts < n_bodyparts; bodyparts++) {
                    index = 0;
                    while (true) {
                        if (body_equippable)
                            region_name = animation_name[ani] + "_"
                                    + direction_name[dir] + "_"
                                    + bodyparts_name[bodyparts]
                                    + String.format("%04d", index);
                        else {
                            region_name = animation_name[ani] + "_"
                                    + direction_name[dir]
                                    + String.format("%04d", index);
                        }
                        temp_region = atlas.findRegion(region_name);
                        if (temp_region == null) break;
                        else {
                            has_animation[ani] = true;
                            temp_frames.add(temp_region);
                            index++;
                        }
                    }//while
                    animation[ani][dir][bodyparts] = new Animation(0.1f, temp_frames.toArray(new TextureRegion[index]));
                    if (ani == DEAD || ani == RAISE) animation[ani][dir][bodyparts].setPlayMode(PlayMode.NORMAL);
                    else
                        animation[ani][dir][bodyparts].setPlayMode(PlayMode.LOOP);
                    if (ani == ATTACK || ani == ATTACKED) {
                        animation[ani][dir][bodyparts] = animation[WALK][dir][bodyparts];
                    }
                    temp_frames.clear();
                    if (is_symmetric) {
                        animation[ani][dir + 1][bodyparts] = createFlippedAnimation(animation[ani][dir][bodyparts]);
                        if (ani == DEAD || ani == RAISE)
                            animation[ani][dir + 1][bodyparts].setPlayMode(PlayMode.NORMAL);
                        else
                            animation[ani][dir + 1][bodyparts].setPlayMode(PlayMode.LOOP);
                    }

                }//bodyparts
            }//dir
        }//ani

    }

    public static class Factory {
        public float anchor_x;
        public float anchor_y;
        public boolean has_direction;
        public boolean is_symmetric;
        public boolean equippable;
        public boolean body_equippable;
        public int[][][][] head_coord;
        public int[][][][] body_coord;
        public int[][][][] arm2_coord;
        public int[][][][] arm1_coord;
    }
}
