package com.icefill.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.esotericsoftware.kryo.KryoSerializable;
import com.icefill.game.Constants;

public class BasicSprites
  implements Constants
{
  protected boolean has_direction = false;
  protected boolean is_symmetric = true;
  protected float anchor_x = 0.0F;
  protected float anchor_y = 0.0F;
  protected float height = 0.0F;
  protected float width = 0.0F;
  protected String name = "";

  public boolean hasDirection()
  {
    return this.has_direction;
  }
  public float getAnchorPointX() {
    return this.anchor_x;
  }
  public float getAnchorPointY() {
    return this.anchor_y;
  }
  public float getHeight() { return this.height; } 
  public float getWidth() { return this.width; }

  public float getSpritesDuration(int animation, int direction) {
    return 0.0F;
  }
  public Animation getRepresentativeAnimations() {
    return null;
  }

  public Animation createFlippedAnimation(Animation to_flip) {
    float frame_duration = to_flip.getFrameDuration();
    float ani_duration = to_flip.getAnimationDuration();
    float fr = 0.0F;

    TextureRegion[] temp = new TextureRegion[(int)(ani_duration / frame_duration)];
    for (int i = 0; i < (int)(ani_duration / frame_duration); i++) {
      temp[i] = new TextureRegion(to_flip.getKeyFrame(fr));
      temp[i].flip(true, false);
      fr += frame_duration;
    }
    Animation to_return = new Animation(frame_duration, temp);
    return to_return;
  }
}