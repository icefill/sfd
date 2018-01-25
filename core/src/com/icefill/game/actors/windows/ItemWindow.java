package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.AbilityActor;

public class ItemWindow extends BasicWindow {
    //ObjActor obj;
    ImageButtonStyle style;
    SlotButton slot;
    TextButton use_button;
    TextButton equip_button;
    TextButton dual_equip_button;
    TextButton un_equip_button;
    TextButton drop_button;
    TextButton memorize_button;
    TextButton add_to_shortcut_button;
    TextButton delete_shortcut_button;
    TextButton close_button;
    Table description_parent_table;
    protected ItemDescriptionTable description_table;
    protected ItemDescriptionTable current_description_table;
    Table button_table;

    public ItemWindow(Skin skin) {
        super(skin, true);
        description_parent_table = new Table();
        description_table = new ItemDescriptionTable("ITEM TO CHANGE");
        current_description_table = new ItemDescriptionTable("CURRENT ITEM");

        button_table = new Table();
        add_to_shortcut_button = new TextButton("ADD TO QUICKSLOT", Assets.getSkin(), "default");
        add_to_shortcut_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (slot != null && slot.equip != null) {
                    Global.getPlayerTeam().getQuickSlotInventory().setSlot(slot);
                    window.hideTable();
                }

            }

        });
        delete_shortcut_button = new TextButton("REMOVE QUICKSLOT", Assets.getSkin(), "default");
        delete_shortcut_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (slot != null && slot.equip != null && slot.quick != null) {
                    slot.removeQuickSlot();
                    window.hideTable();
                }

            }

        });

        use_button = new TextButton("USE", Assets.getSkin(), "default");
        use_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                slot.use();
                window.hideTable();

            }

        });
        equip_button = new TextButton("EQUIP", Assets.getSkin(), "default");
        equip_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                Global.getSelectedObj().getInventory().setEquip(slot);
                window.hideTable();

            }

        });
        memorize_button = new TextButton("MEMORIZE", Assets.getSkin(), "default");
        memorize_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (slot != null && slot.equip != null) {
                    if (slot.equip.equip_action != null) {
                        if (Global.getSelectedObj().isLearnable((AbilityActor) slot.equip.equip_action.action)) {
                            Global.showBigMessage(((AbilityActor)slot.equip.equip_action.action).getActionName()+" "+slot.equip.equip_action.level+ " memorized.");
                            Global.getSelectedObj().addAbility((AbilityActor) slot.equip.equip_action.action);
                            slot.subNumber();

                        }

                    }
                }
                window.hideTable();

            }

        });

        un_equip_button = new TextButton("UNEQUIP", Assets.getSkin(), "default");
        un_equip_button.addListener(new ClickListener() {
                                        public void clicked(InputEvent event, float x, float y) {
                                            if (slot != null && slot.equip != null) {
                                                EquipActor equip_existing = slot.equip;
                                                if (Global.getPlayerTeam().getInventory().setSlot(equip_existing)) {
                                                    slot.setSlot(null);

                                                } else {
                                                    if (Global.getCurrentRoom().setItem(Global.getSelectedObj().getXX(), Global.getSelectedObj().getYY(), equip_existing, false)) {
                                                        slot.setSlot(null);
                                                    }
                                                }

                                            }
                                            window.hideTable();

                                        }

                                    }
        );
        drop_button = new TextButton("DROP", Assets.getSkin(), "default");
        drop_button.addListener(new ClickListener() {
                                    public void clicked(InputEvent event, float x, float y) {
                                        if (slot != null && slot.equip != null) {
                                            EquipActor equip_existing = slot.equip;
                                            if (Global.getCurrentRoom().setItem(Global.getSelectedObj().getXX(), Global.getSelectedObj().getYY(), equip_existing, false)) {
                                                if (slot.couple != null) {
                                                    slot.couple.setSlot(null);
                                                }
                                                slot.setSlot(null);
                                            }
                                            window.hideTable();
                                        }
                                    }

                                }
        );
        close_button = new TextButton("CLOSE", skin);
        close_button.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        window.hideTable();
                    }

                }
        );

        table.add(description_parent_table).pad(5f).row();
        table.add(button_table);
        //
	    /*
	    table.add(use_button).pad(1);
	    table.add(equip_button).pad(1);
	    table.add(un_equip_button).pad(1).row();
	    table.add(memorize_button).pad(1);
	    table.add(add_to_shortcut_button).pad(1);
	    table.add(delete_shortcut_button).pad(1);
	    */
        //dungeon.getUiStage().addActor(this);
        //this.add(table);
        //this.setFillParent(true);
        //this.center();
        //table.pack();

        //this.setVisible(false);

    }

    public void setButton(SlotButton slot) {
        button_table.clear();
        if (slot.equip.type < 4
                ) {
            if (Global.getSelectedObj().getInventory().getEquip(slot.equip.type) == null ||
                    !Global.getSelectedObj().getInventory().getEquip(slot.equip.type).equals(slot.equip))
                button_table.add(equip_button).pad(5);

        }
        if (slot.type == -1) {
            button_table.add(drop_button).pad(5);
        } else {
            button_table.add(un_equip_button).pad(5);

        }
        if (slot.quick == null) {
            if (slot.equip.isDisposable())
                button_table.add(add_to_shortcut_button).pad(5);
        } else {
            button_table.add(delete_shortcut_button).pad(5);
        }
        if (slot.equip.status == null) {
            button_table.add(use_button);
        }

        if (slot.equip.equip_action != null &&
                (
                        slot.equip.equip_action.action.getActionType().equals("fire_magic")
                                || slot.equip.equip_action.action.getActionType().equals("lightning_magic")
                                || slot.equip.equip_action.action.getActionType().equals("holy_magic")
                                || slot.equip.equip_action.action.getActionType().equals("unholy_magic")
                )
                ) {
            //if (Global.getSelectedObj().isLearnable((AbilityActor)slot.equip.equip_action.action)){
            button_table.add(memorize_button).pad(5);
            // }

        }
        button_table.add(close_button).pad(5);

    }

    public void setDescription(SlotButton slot) {
        description_parent_table.clear();
        int equip_n = Math.abs(slot.equip.getType());
        if (1 <= equip_n && equip_n <= 3) {
            EquipActor current_equip = Global.getSelectedObj().getInventory().getEquip(equip_n);
            if (current_equip != null && !current_equip.equals(slot.equip)) {

                current_description_table.setDescription(current_equip, true);
                description_parent_table.add(current_description_table).height(280);
            }
            description_table.setDescription(slot, true);
        }
        description_table.setDescription(slot, false);
        description_parent_table.add(description_table).height(280);
    }

    public void setSlot(SlotButton slot) {
        if (slot.equip != null) {
            setButton(slot);
            setDescription(slot);

            this.slot = slot;
        }
    }

    public void renewSlot() {
        setSlot(slot);
    }

    public void showTable(SlotButton slot, ObjActor obj) {
        setSlot(slot);
        //this.mp_button.setText("MANA:"+getDungeon().getDeadEnemyList().size());
        this.setVisible(true);
    }

    public class ItemDescriptionTable extends Table {
        Image item_image;
        Label description;
        Label name_label;

        public ItemDescriptionTable(String name) {
            super();
            this.setBackground(Assets.getBackground());
            item_image = new Image();

            name_label = new Label(name, new Label.LabelStyle(Assets.getSkin().getFont("font_slkscr_8pt"), Color.WHITE));
            description = new Label("blah blah blah...", new Label.LabelStyle(Assets.getSkin().getFont("font_slkscr_8pt"), Color.BLACK));
            description.setWrap(true);
            this.add(name_label).pad(5).row();
            this.add(item_image).pad(5).colspan(4).row();
            this.add(description).width(120).pad(2).colspan(4).row();

        }

        public void setDescription(EquipActor equip, Boolean has_alternative) {
            item_image.setDrawable(equip.getInventoryImage());
            if (equip.status == null) {
                description.setText(((AbilityActor) (equip.equip_action.action)).toString2(equip.equip_action.level));

            } else {
                description.setText(equip.toString());
            }
            description.pack();
            //if (has_alternative)
            //{
            name_label.setVisible(true);
            //}
            //else
            //	name_label.setVisible(false);
        }

        public void setDescription(SlotButton slot, Boolean has_alternative) {
            setDescription(slot.equip, has_alternative);
        }

    }

}
