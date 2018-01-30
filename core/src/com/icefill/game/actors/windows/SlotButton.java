package com.icefill.game.actors.windows;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.utils.Randomizer;

public class SlotButton extends ImageButton {
    protected EquipActor equip;
    //protected Label tooltip;
    protected Label label;
    boolean instant_use;
    boolean shop_product;
    //protected Table tooltip_table;
    //boolean is_showing_tooltip=false;
    Vector2 position = new Vector2();
    ObjActor obj = null;
    int type;
    int price=0;
    final int BUTTON_WIDTH = 50;
    final int BUTTON_HEIGHT = 50;
    SlotButton couple;
    SlotButton quick;
    SlotButton quick_parent;
    TextureRegion background = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("scroll"));


    public SlotButton(int type, Label label, DragAndDrop drag_and_drop, boolean instant_use) {
        super(new ImageButtonStyle(Assets.getSkin().get(ButtonStyle.class)));
        this.type = type;
        setToolTip();
        this.label = label;
        this.setBackground(Assets.getBackground());

        this.setSkin(Assets.getSkin());
        this.setBackground(Assets.getBackground());
        if (drag_and_drop != null) {
            drag_and_drop.addSource(new SlotSource(this));
            drag_and_drop.addTarget(new SlotTarget(this));
        }

        this.instant_use = instant_use;
        this.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    }


    public SlotButton(int type, EquipActor equip, Label label, DragAndDrop drag_and_drop, boolean instant_use) {
        super(equip.getButtonStyle());
        //this.setBackground(Assets.getBackground());
        this.setSkin(Assets.getSkin());
        this.setBackground(Assets.getBackground());

        this.equip = equip;
        this.price= (int)(equip.price* Randomizer.nextFloat(.9f,1.2f));
        this.type = type;
        this.label = label;
        if (drag_and_drop != null) {

            drag_and_drop.addSource(new SlotSource(this));
            drag_and_drop.addTarget(new SlotTarget(this));
            setToolTip();

        }
        this.instant_use = instant_use;
        this.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public void setCouple(SlotButton slot) {
        this.couple = slot;
    }

    public int getPrice() {
        return price;
    }

    public SlotButton getCouple() {
        return couple;
    }

    public void setShopProduct() {
        shop_product = true;
    }

    public void setObj(ObjActor obj) {
        this.obj = obj;
    }

    public ImageButtonStyle getEmptyButtonStyle() {
        ImageButtonStyle style = new ImageButtonStyle(Assets.getSkin().get(ButtonStyle.class));
        return style;
    }

    public boolean use() {
        if (equip != null) {
            if (equip.equip_action != null) {
                Global.setSelectedSlot(this);
                Global.getSelectedObj().selectAction(equip.equip_action);
                Global.gfs.pushState(11); //GS_SELECT_TARGET
                return true;

            }
        }
        return false;
    }

    public void setToolTip() {

        this.addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (equip != null) {//&& !is_showing_tooltip) {
                    Global.setTooltip(equip.toString());
                    //is_showing_tooltip=true;
                    //tooltip_table.setVisible(true);
                }
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (equip != null) {
					/*if (instant_use)
					{
						use();
						
					}
					else*/
                    {
                        //Global.setTooltip(equip.toString());

                        //Global.setitemWindow(getSlot());
                    }

                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (equip != null) {
                    Global.releaseTooltip();
                    if (instant_use) {

                        use();

                    } else if (shop_product) {
                        //Global.releaseTooltip();
                        Global.setShopitemWindow(getSlot());
                    } else {
                        //Global.releaseTooltip();
                        Global.setitemWindow(getSlot());
                    }

                }
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Global.releaseTooltip();
                //is_showing_tooltip=false;
                //tooltip_table.setVisible(false);
            }

        });

    }

    public int getType() {
        return type;
    }

    public SlotButton getSlot() {
        return this;
    }

    public boolean isEmpty() {
        if (equip == null)
            return true;
        else return false;
    }

    public void setNumber(int n) {
        equip.n = n;
    }

    public int getNumber() {
        return equip.n;
    }

    public void addNumber() {
        equip.n++;
    }

    public void addNumber(int n) {
        equip.n += n;
    }

    public void subNumber() {
        equip.n--;
		/*
		if (getNumber()<=0)
		{
	  		setSlot(null);
	  		if (couple!=null)
	  		{
	  			couple.setSlot(null);
	  		}
		}
		*/
    }

    public void setSlot(EquipActor equip) {

        if (equip != null) {
            this.localToStageCoordinates(position);

            if (label != null)
                label.setText(equip.toString());
            this.setStyle(equip.getButtonStyle());
        } else {
            this.setStyle(getEmptyButtonStyle());
        }
        this.equip = equip;
        if (equip!=null)
            this.price= (int)(equip.price* Randomizer.nextFloat(.9f,1.2f));
        if (obj != null) {
            obj.status.setStatus(obj.inventory, obj.turn_effect_list);
            obj.inventory.renewStatus();
        }
    }
	/*
	public void setSlot(EquipActor equip,int n) {
		setSlot(equip);
		setNumber(n);
	}
	*/

    public void exchangeSlot(SlotButton another_slot, ObjActor obj) {
        EquipActor temp_equip = this.equip;
        //int temp_n=this.equip.n;
        this.setSlot(another_slot.getEquip());
        another_slot.setSlot(temp_equip);
    }

    public EquipActor getEquip() {
        return equip;
    }

    public void removeQuickSlot() {
        if (this.quick != null) {
            quick.setSlot(null);
            setColor(1f, 1f, 1f, 1f);
            quick.quick_parent = null;
            quick = null;

        }
    }

    public void addQuickSlot(SlotButton quick) {

    }

    public void draw(Batch batch, float delta) {// For drawing icon
        super.draw(batch, delta);
        if (equip != null) {
            if (equip.short_name != null) {
                Assets.getFont().setColor(Color.BLACK);
                Assets.getFont().draw(batch, equip.short_name, getX() + 3, getY() + 45);
                Assets.getFont().setColor(Color.WHITE);
            }
            if (equip.n > 1) {
                Assets.getFont().setColor(Color.BLACK);
                Assets.getFont().draw(batch, Integer.toString(equip.n), getX() + 40, getY() + 10);
                Assets.getFont().setColor(Color.WHITE);
            }
            if (shop_product) {
                Assets.getFont().setColor(Color.BLACK);
                Assets.getFont().draw(batch, "$" + getPrice(), getX() + 10, getY() + 10);
                Assets.getFont().setColor(Color.WHITE);
            }
        }
    }

    public void act(float delta) {
        super.act(delta);
        if (equip != null && equip.n <= 0) {
            setSlot(null);
            if (quick != null) {
                removeQuickSlot();
            }
        }
    }

    public class SlotSource extends Source {
        private SlotButton source_slot;


        public SlotSource(SlotButton actor) {
            super(actor);
            this.source_slot = actor;
        }

        public Payload dragStart(InputEvent event, float x, float y, int pointer) {
            if (source_slot.getEquip() == null)
                return null;
            else {
                Payload payload = new Payload();
                Actor drag_actor = new Image(source_slot.getEquip().getSprites().getRepresentativeAnimations().getKeyFrame(0));
                payload.setObject(source_slot);
                payload.setDragActor(drag_actor);
                payload.setValidDragActor(drag_actor);
                payload.setInvalidDragActor(drag_actor);
                return payload;
            }

        }

        public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
            SlotButton payload_slot = (SlotButton) payload.getObject();
            if (target != null) {
                SlotButton target_slot = ((SlotButton) target.getActor());
                if ((target_slot.getType() < 0 && target_slot.getEquip() == null)
                        || (target_slot.getType() == payload_slot.getEquip().getType())
                        || (target_slot.getType() == 3 && payload_slot.getEquip().getType() == 2)
                        )
                    payload_slot.exchangeSlot(target_slot, obj);

            } else { //ThrowAway Item
                EquipActor temp = payload_slot.getEquip();
                //payload_slot.setSlot(null);

            }

        }


    }

    public class SlotTarget extends Target {
        private SlotButton target_slot;

        public SlotTarget(SlotButton actor) {
            super(actor);
            target_slot = actor;
            getActor().setColor(Color.WHITE);
        }

        public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
            SlotButton payload_slot = (SlotButton) payload.getObject();
            getActor().setColor(Color.WHITE);
            return true;
        }

        public void drop(Source source, Payload payload, float x, float y, int pointer) {

        }

        public void reset(Source source, Payload payload) {
            getActor().setColor(Color.WHITE);
        }
    }
}
