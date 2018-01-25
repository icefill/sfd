package com.icefill.game.actors.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.Team;
import com.icefill.game.actors.dungeon.DungeonGroup;
import com.icefill.game.actors.EquipActor;
import com.icefill.game.actors.ObjActor;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.actors.actionActors.CancelAction;
import com.icefill.game.sprites.NonObjSprites;

public class HUD extends Table {
    ObjActor selected;
    ObjActor on_cursor;
    TextureAtlas skinAtlas = Assets.getAsset(("ui/uiskin.atlas"), TextureAtlas.class);
    TextureRegion ability_count;
    //Table char_status_table;
    //Table game_info_table;
    Table bottom_table;
    Table center_table;
    Table top_table;
    Table top_left_table;

    CharSelectStatus status_table;
    CharSelectStatus status_table2;
    Table common_status_table;
    Table status_parent_table;
    Table status_parent_table2;
    Table status_parent_table3;
    Image icon_img;
    //Table information_table;
    //Label actions_label;
    Table ability_table;
    Table ability_table2;
    AbilityInfoTable ability_info;
    //Label ability_info_label;
    Table minimap_table;
    //DungeonGroup room;
    Label label_info;
    Label description;
    Label common_info;
    ActionContainer cancel_container;
    CancelAction cancel_action;
    //   Table inventory_table;
    //CommonInventoryActor inventory;
    Table quick_slot_table;
    TextureRegion mana_icon;
    NonObjSprites mana_sprites;

    com.icefill.game.actors.windows.BattleWinWindow battle_win_window;

    public HUD() {
        super();

        battle_win_window = new com.icefill.game.actors.windows.BattleWinWindow(Assets.getSkin());


        mana_icon = new TextureRegion(Assets.getAsset("sprite/icon.atlas", TextureAtlas.class).findRegion("ability_up"));
        icon_img = new Image();
        mana_sprites = (NonObjSprites) Assets.non_obj_sprites_map.get("mana_stone");
        ability_table = new Table(Assets.getSkin());
        ability_table2 = new Table(Assets.getSkin());
        common_status_table = new Table(Assets.getSkin());
        quick_slot_table = new Table(Assets.getSkin());
        ability_info = new AbilityInfoTable(Assets.getSkin());
        ability_info.setVisible(false);
        bottom_table = new Table(Assets.getSkin());
        center_table = new Table(Assets.getSkin());
        top_table = new Table(Assets.getSkin());
        top_left_table = new Table(Assets.getSkin());
        status_parent_table = new Table(Assets.getSkin());
        status_parent_table2 = new Table(Assets.getSkin());
        status_parent_table3 = new Table(Assets.getSkin());
        status_table = new CharSelectStatus(Assets.getSkin());
        status_table2 = new CharSelectStatus(Assets.getSkin());
        minimap_table = new Table(Assets.getSkin());
        this.setFillParent(true);
        label_info = new Label("Bummer", new Label.LabelStyle(Assets.getFont(), Color.BLACK));
        description = new Label("Bummer", new Label.LabelStyle(Assets.getFont(), Color.WHITE));
        common_info = new Label("Bummer", new Label.LabelStyle(Assets.getFont(), Color.BLACK));
        minimap_table.setBackground(Assets.getBackground());


        status_table.setBackground(Assets.getBackground());
        ability_info.setBackground(Assets.getBackground());

        status_parent_table.add(status_parent_table3).bottom();
        status_parent_table3.add(ability_table2).row();
        status_parent_table3.add(status_table);

        status_parent_table2.add(quick_slot_table);
        Table common_parent = new Table();
        common_parent.setBackground(Assets.getBackground());
        common_parent.add(common_info);
        top_table.add(top_left_table).expandX().top().left();
        ;
        top_left_table.add(common_parent).top();

        top_table.add(quick_slot_table).right().top();
        center_table.add(minimap_table).center();
        minimap_table.setVisible(false);
        status_parent_table.add(ability_table).left().pad(5);

        bottom_table.add(status_parent_table).expandX().left();
        this.add(top_table).fillX().top().row();
        this.add(center_table).expand().fillX().fillY().row();
        this.add(bottom_table).expandX().fillX().bottom().uniform();
    }


    public BattleWinWindow getBattleWinWindow() {
        return battle_win_window;
    }

    public void act(float delta) {
        super.act(delta);
        common_info.setText(Global.getPlayerTeam().getTeamResourceInfo());
    }

    public Table getStatusTable() {
        return status_table;
    }

    public void setInventory(Team team) {
        quick_slot_table.clear();
        quick_slot_table.add(team.getQuickSlotInventory());

    }

    public void addObj(ObjActor selected) {
        this.selected = selected;
        status_table.setObj(selected);
    }

    public void releaseObj() {
        this.selected = null;
    }

    public void addCursorObj(ObjActor selected) {
        this.on_cursor = selected;
        status_table2.setObj(selected);
    }

    public void releaseCursorObj() {
        this.on_cursor = null;
    }

    public void addMap(MinimapActor minimap) {
        minimap_table.add(minimap).pad(25).row();
        minimap_table.add(minimap.getCloseButton());
    }

    public void showMap() {
        minimap_table.setVisible(true);
    }

    public void hideMap() {
        minimap_table.setVisible(false);
    }

    public Table getMinimap() {
        return minimap_table;
    }

    public void addMoveAbility(ObjActor selected_obj) {
        if (!selected_obj.getMoveAction().is_forbidden) {
            ability_table.add(selected_obj.getMoveAction().action);
            selected_obj.getMoveAction().addListener();
        }
    }

    public void showQuickSlot() {
        quick_slot_table.setVisible(true);
    }

    public void hideQuickSlot() {
        quick_slot_table.setVisible(false);
    }

    public boolean addAbilitySubMethod(ObjActor selected_obj, ActionContainer temp, boolean is_on_battle) {
        if (temp.action.getActionName().equals("Wait") && is_on_battle) {
            ability_table.add(temp.action);
            temp.addListener();
            return true;
        }

        if (selected_obj.isAvailableAction(temp)) {
            temp.action.setColor(1f, 1f, 1f, 1f);
            temp.addListener();
        } else temp.action.setColor(.3f, .3f, .3f, 1f);

        if ((temp.action.getType() == 0 && !(temp.action.getActionName().equals("Wait")))) {
            //ability_table2.add(temp.action);
            ability_table2.add(temp.action.getButton());
            return false;
        } else {
            if (is_on_battle &&
                    (!(temp.action instanceof AbilityActor) ||
                            ((AbilityActor) temp.action).checkWeaponType(selected_obj))
                    )
                ability_table.add(temp.action);
                return true;
        }
    }

    public void addAbility(ObjActor selected_obj, boolean is_on_battle) {

        if (selected_obj != null) {
            if (selected_obj.getActionList() != null) {
                if (is_on_battle) {
                    ability_table.setVisible(true);
                }
                ability_table2.setVisible(true);
                ability_table.clearChildren();
                ability_table2.clearChildren();

                for (ActionContainer temp : selected_obj.getActionList()) {

                    if (addAbilitySubMethod(selected_obj, temp, is_on_battle)) {

                        if (ability_table.getChildren().size%7==0) ability_table.row();
                    }
                }
                // itemm action
                if (selected_obj.getInventory() != null) {
                    for (int i = 1; i < 4; i++) {
                        EquipActor temp_equip = selected_obj.getInventory().getEquippingSlot(i).getEquip();
                        if (temp_equip != null && temp_equip.equip_action != null) {
                            ActionContainer temp = temp_equip.equip_action;

                            if (addAbilitySubMethod(selected_obj, temp, is_on_battle)) {
                                if (ability_table.getChildren().size%7==0) ability_table.row();
                            }
                        }
                    }
                }

            }


        }
    }

    public void addAbilityInfo(AbilityActor ability, int level) {
		/*
		status_parent_table.add(ability_info).expand().fillY().left();
		ability_info.setVisible(true);
		ability_info.setAbilityActor(ability);
		ability_info.setText(ability.toString(level));
		//ability_table.add(ability_info);
		 
		 */
    }

    public void removeAbilityInfo() {
        ability_info.setVisible(false);
        ability_info.remove();
        clearAbility();
    }

    public void clearAbility() {

        ability_table.clearChildren();
        ability_table2.clearChildren();
        ability_table2.setVisible(false);
        Global.getPlayerTeam().hideInventory();
        //ability_table.setVisible(false);
    }

    public void addCancle() {

        if (cancel_container == null) {
            cancel_container = new ActionContainer(new CancelAction(), 0);
            cancel_container.addListener();
        }
        if (Global.getSelectedObj() != null && Global.getSelectedObj().getSelectedAction() != null) {
            AbilityActor action = (AbilityActor) (Global.getSelectedObj().getSelectedAction().action);
            description.setText(action.getActionName() + ":\n" + action.getDescription());
            ability_table.add(action.getImage()).pad(3);
            ability_table.add(description).pad(3);
        }
        ability_table.add(cancel_container.action);
        ability_table.setVisible(true);
    }

    public boolean isCanceled() {
        return cancel_container.action.selected;
    }

    public void clearCancel() {
        cancel_container.action.remove();
        cancel_container.action.selected = false;
    }

    public void removeAbility(ObjActor selected_obj) {
        for (ActionContainer temp : selected_obj.getActionList()) {
            temp.action.remove();
            temp.removeListener();
            temp.action.selected = false;
        }
        if (selected_obj.getInventory() != null) {
            for (int i = 1; i < 4; i++) {
                EquipActor temp_equip = selected_obj.getInventory().getEquippingSlot(i).getEquip();
                if (temp_equip != null && temp_equip.equip_action != null) {
                    ActionContainer temp = temp_equip.equip_action;
                    temp.action.remove();
                    temp.removeListener();
                    temp.action.selected = false;
                }
            }
        }
        ability_table.setVisible(false);
        ability_table2.setVisible(false);
        Global.getPlayerTeam().hideInventory();
        //ability_table.clearChildren();
    }

    public void draw(Batch batch, float delta) {
        super.draw(batch, delta);
        //label_info.setText(room.toString());
        //game_info_table.draw(batch, 1);

        //Assets.getFont().draw(batch, "mana: "+Global.mana, 200, 700);

    }


    public class CharSelectStatus extends Table {
        private ObjActor selected_obj;
        private Label label;
        AbilityCountTable ability_count_table;

        public CharSelectStatus(Skin skin) {
            super(skin);
            ability_count_table = new AbilityCountTable(Assets.getSkin());

            label = new Label("", new Label.LabelStyle(Assets.getFont(), Color.BLACK));
            this.add(label).size(80, 40).row();
            this.add(ability_count_table).size(80, 12);
        }

        public void draw(Batch batch, float delta) {
            super.draw(batch, delta);
            if (selected_obj != null)
                selected_obj.draw(batch, delta, this.getX() + 80, this.getY() + 20);
        }

        public void setObj(ObjActor obj) {
            this.selected_obj = obj;
            ability_count_table.setObj(obj);
            if (obj != null)
                this.label.setText(obj.getSelectedString());
        }
    }

    public class AbilityInfoTable extends Table {
        TextureRegion room_texture;
        DungeonGroup dungeon;
        AbilityActor ability;
        Table top_table;
        Table middle_table;
        Table bottom_table;
        Image ability_icon;
        Label ability_info_label;
        int tile_width;
        int tile_height;

        public AbilityInfoTable(Skin skin) {
            super(skin);
            //this.setDebug(true,true);
            top_table = new Table(skin);
            middle_table = new Table(skin);
            bottom_table = new Table(skin);
            this.add(top_table);
            this.add(middle_table);
            this.add(bottom_table).row();
            ability_info_label = new Label("Bummer", new Label.LabelStyle(Assets.getFont(), Color.WHITE));

            tile_width = 12;
            tile_height = 6;
            TextureAtlas atlas = Assets.getAsset("sprite/minimap.atlas", TextureAtlas.class);
            room_texture = atlas.findRegion("room");

            bottom_table.add(ability_info_label);
        }

        public void setAbilityActor(AbilityActor ability) {
            this.ability = ability;
            this.add(ability_icon).row();
            this.add(ability_info_label);
        }

        public void draw(Batch batch, float delta) {
            super.draw(batch, delta);
            if (ability != null) {
                batch.draw(ability.getIcon(), getX() + 45, getY() + 270);

                drawRange(getX(), getY() - 200, ability, batch, delta);
            }
        }

        public void setText(String string) {
            ability_info_label.setText(string);
        }

        public void drawRange(float x, float y, AbilityActor ability, Batch batch, float delta) {
            int min_range = ability.targeting_min_range;
            int max_range = ability.targeting_max_range;
            boolean self_targeting = ability.self_targeting;
            int min_splash = ability.splash_min_range;
            int max_splash = ability.splash_max_range;
            int splash_position_x = 0;
            int splash_position_y = max_range;
            int range = 8;
            switch (ability.targeting_type) {
                case 0:
                    for (int xx = -range; xx < range + 1; xx++) {
                        for (int yy = -range; yy < range + 1; yy++) {
                            int distance = Math.abs(xx) + Math.abs(yy);
                            if (min_range <= distance && distance <= max_range)
                                batch.setColor(Color.BLUE);
                            else
                                batch.setColor(Color.BLACK);
                            if (Math.abs(xx) + Math.abs(yy) <= range)
                                batch.draw(room_texture, mapToScreenCoordX(x, xx, yy) - (float) (0.5 * tile_width), mapToScreenCoordY(y, xx, yy) - (float) (0.5 * tile_height));
                            batch.setColor(Color.WHITE);
                        }

                    }
                    break;

                case 1:
                    for (int xx = -range; xx < range + 1; xx++) {
                        for (int yy = -range; yy < range + 1; yy++) {
                            //int distance= Math.abs(xx)+Math.abs(yy);

                            if ((xx == 0 && Math.abs(yy) <= max_range && min_range <= Math.abs(yy)) ||
                                    (yy == 0 && Math.abs(xx) <= max_range && min_range <= Math.abs(xx))
                                    )
                                batch.setColor(Color.BLUE);
                            else
                                batch.setColor(Color.BLACK);
                            if (Math.abs(xx) + Math.abs(yy) <= range)
                                batch.draw(room_texture, mapToScreenCoordX(x, xx, yy) - (float) (0.5 * tile_width), mapToScreenCoordY(y, xx, yy) - (float) (0.5 * tile_height));
                            batch.setColor(Color.WHITE);
                        }

                    }
                    break;
            }
            switch (ability.splash_type) {
                case 0:
                    for (int xx = -max_splash; xx < max_splash + 1; xx++) {
                        for (int yy = max_range - max_splash; yy < max_range + max_splash + 1; yy++) {
                            if (Math.abs(xx - splash_position_x) + Math.abs(yy - splash_position_y) <= max_splash) {
                                batch.setColor(Color.RED);
                                batch.draw(room_texture, mapToScreenCoordX(x, xx, yy) - (float) (0.5 * tile_width), mapToScreenCoordY(y, xx, yy) - (float) (0.5 * tile_height));
                            }
                        }
                    }
                    break;
                case 2:
                    for (int yy = min_range; yy < max_range + 1; yy++) {
                        //if (Math.abs(-splash_position_x)+Math.abs(yy-splash_position_y)<=max_splash) {
                        batch.setColor(Color.RED);
                        batch.draw(room_texture, mapToScreenCoordX(x, 0, yy) - (float) (0.5 * tile_width), mapToScreenCoordY(y, 0, yy) - (float) (0.5 * tile_height));
                        //}
                    }
                    break;
            }
        }

        public float mapToScreenCoordX(float x, int xxx, int yyy) {
            return (float) (x + getWidth() * 0.5 + 0.5 * tile_width * (xxx - yyy));
        }

        public float mapToScreenCoordY(float y, int xxx, int yyy) {
            return (float) (y + getHeight() * 0.75 - 0.5 * tile_height * (xxx + yyy));
        }


    }

    public class AbilityCountTable extends Table {
        public ObjActor obj;
        TextureAtlas char_ui_atlas = Assets.getAsset("sprite/char_ui.atlas", TextureAtlas.class);
        TextureRegion ability_count = new TextureRegion(char_ui_atlas.findRegion("ability_count"));
        TextureRegion mana_count = new TextureRegion(char_ui_atlas.findRegion("mana_count"));
        int mana;

        public AbilityCountTable(Skin skin) {
            super(skin);
            //this.setDebug(true);

        }

        public void setObj(ObjActor obj) {
            this.obj = obj;
        }

        public void act(Batch batch, float delta) {
            //	this.setBounds(getX(), getY(), 120, 30);
        }

        public void draw(Batch batch, float delta) {
            super.draw(batch, delta);
            Assets.getFont().draw(batch, "AP:", getX(), getY() + 9);
            if (obj != null) {
                int count = obj.status.getCurrentAP();
                for (int i = 0; i < count; i++) {
                    batch.draw(this.ability_count, getX() + i * 18 + 16, getY() + 2);
                }
            }
			/*
			mana=Global.getMana();
			Assets.getFont().draw(batch, "MP:", getX()-18+70, getY()+9);	
			for (int i = 0; i < mana; i++) {
		  	      batch.draw(this.mana_count,getX()+ i * 18+70,getY()+2);
		  	    }
		  		*/
        }
    }


}
