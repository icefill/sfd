package com.icefill.game.actors;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.icefill.game.Job;
import com.icefill.game.utils.Randomizer;
import com.icefill.game.actors.ObjActor.Status;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;

public class LevelUpWindow extends BasicWindow {
	ObjActor obj;
	ImageButtonStyle style;
	//Table table;
	//int pause_id;
	//public int new_ability_point;
	LevelUpWindow window;
	DungeonGroup dungeon;
	//Table status_table;
	Table ability_table;
	Table outer_table;
	Table left_table;
	Table right_table;
	Label label;
	TextButton button;
	Label stat_up;
	//int incre=0;
	
	ActionContainer ability_to_add;
	AbilityInfoTable ability_info_table;
	
	public LevelUpWindow(final ObjActor obj_to_level_up,Skin skin){
		super(skin,false);
		stat_up= new Label("",Assets.getSkin());
		outer_table= new Table();
		ability_table = new Table(Assets.getSkin());
		left_table= new Table();
		left_table.setBackground(Assets.getBackground());
		right_table= new Table(Assets.getSkin());
		right_table.setBackground(Assets.getBackground());
		obj = obj_to_level_up;
		window=this;
		label = new Label("", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.BLACK) );
		label.setFontScale(1f);
	    button = new TextButton("LEARN", Assets.getSkin(), "default");
		button.setVisible(false);
	    button.addListener(new ClickListener() {
	      public void clicked(InputEvent event, float x, float y) {
	    	//if (window.getAbilityToAdd()!=null&& new_ability_point>0 && ((AbilityActor)(window.getAbilityToAdd().action)).required_level+window.getAbilityToAdd().level<=obj.level) {	    		
	    	  	obj.addAbility((AbilityActor)getAbilityToAdd().action);
	    	  	window.hideTable();
					window.getDungeon().setVisible(true);
	      }
	    });
	    TextButton close_button = new TextButton("CLOSE", Assets.getSkin(), "default");
	    close_button.addListener(new ClickListener() {
	      public void clicked(InputEvent event, float x, float y) {
	  					window.hideTable();
	  					window.getDungeon().setVisible(true);
	      }
	    });
	    
	  //  left_table.add(status_table).size(240,200).row();
		left_table.add(ability_table);
		left_table.pack();
		table.add("LEVEL UP! CHOOSE A NEW ABILITY").pad(5).row();
		table.add(outer_table).row();
		outer_table.add(left_table).size(240,220);
		outer_table.add(right_table).size(200,220);
		//outer_table.pack();
		ability_info_table = new AbilityInfoTable();
		//right_table.pack();
		ability_info_table.setVisible(false);
		//left_table.add(button).pad(15f).row();
	    table.add(close_button);
	//	status_table.add(label);
		//obj_to_level_up.addActor(this);
		//this.add(table);
		outer_table.pack();
		this.setVisible(false);
		
	}
	
	
	
	public void setAbilityToAdd(ActionContainer ability) {
		ability_to_add=ability;
		right_table.clear();
		right_table.add(ability_info_table);
		ability_info_table.setAbilityActor((AbilityActor)ability.action,obj);
		ability_info_table.setVisible(true);
		ability_info_table.add(button);
		
		if (ability_to_add.level>obj.level)// || new_ability_point==0)
			button.setVisible(false);
		else
			button.setVisible(true);
			
	}
	public boolean addRandomAbility() {
		if (obj.attain_ability_list ==null)
		{
			
		}
		return false;
	}
	public ActionContainer getAbilityToAdd() {
		return ability_to_add;
	}
	public DungeonGroup getDungeon() {
		return dungeon;
	}
	public void levelUp(final DungeonGroup dungeon) {
		right_table.clear();
		stat_up.setText("STATUS CHANGE\n\n"+statUp());
		right_table.add(stat_up);

		obj.inventory.renewStatus();
		
		this.dungeon=dungeon;
		if (obj.attain_ability_list !=null 
				&& !obj.attain_ability_list.isEmpty()) {
			//new_ability_point+=1;
			dungeon.setVisible(true);
			
		}
		//obj.status.current_hp=obj.status.total_status.HP;
		
		
		if (obj.level% 2 ==0)
		{
			if (obj.isLeader())
				this.chooseAndAddAbilities(dungeon);
			else
			{
				AbilityActor ability=(AbilityActor)obj.getRandomAbilityToLearn();
				if (ability!=null)
				obj.addAbility(ability);
			}
		}
		//this.setVisible(true);
	}
	public TextButton getLearnButton()
	{
		return button;
	}
	public String statUp() {
		Job job= obj.getJob();
		String str="";
		Status base_status=obj.status.base_status;
		if (job.hp_modifier!=null) {
			int hp_modifier=Randomizer.nextInt(job.hp_modifier[0],job.hp_modifier[1]);
			base_status.HP+=hp_modifier;
			if (hp_modifier>0)
				str+="+HP: +"+hp_modifier+"\n";
				//obj.showStatusChange("+HP: +"+hp_modifier+"\n",0);
		}
		if (job.str_modifier!=null) {
			int str_modifier=Randomizer.nextInt(job.str_modifier[0],job.str_modifier[1]);
			base_status.STR+=str_modifier;
			if (str_modifier>0)
				str+="+STR: +"+str_modifier+"\n";
			//	obj.showStatusChange("+STR: +"+str_modifier+"\n",0);
		}
	
		if (job.dex_modifier!=null) {
			int dex_modifier=Randomizer.nextInt(job.dex_modifier[0],job.dex_modifier[1]);
			base_status.DEX+=dex_modifier;
			if (dex_modifier>0)
				str+="+DEX: +"+dex_modifier+"\n";
			//	obj.showStatusChange("+DEX: +"+dex_modifier+"\n",0);
		}
	
		if (job.int_modifier!=null) {
			int int_modifier=Randomizer.nextInt(job.int_modifier[0],job.int_modifier[1]);
			base_status.INT+=int_modifier;
			if (int_modifier>0)
				str+="+INT: +"+int_modifier+"\n";
			//	obj.showStatusChange("+INT: +"+int_modifier+"\n",0);
		}
		obj.status.setStatus(obj.getInventory(), obj.turn_effect_list);
		return str;
	}
	public ObjActor getObj() {
		return obj;
	}
	public void chooseAndAddAbilities(final DungeonGroup dungeon) {
		// Show stat change
		Global.getUIStage().addActor(this);
		this.setFillParent(true);
		this.dungeon=dungeon;
		Status base_status=obj.status.base_status;
		Job job= obj.getJob();
		String temp=obj.getJob().job_name+"ABILITIES";
		/*
		if (job.hp_modifier!=null) {
			int hp_modifier=Randomizer.nextInt(job.hp_modifier[0],job.hp_modifier[1]);
			base_status.HP+=hp_modifier;
			if (hp_modifier>0)
				temp+="HP: +"+hp_modifier+"\n";
		}
		if (job.str_modifier!=null) {
			int str_modifier=Randomizer.nextInt(job.str_modifier[0],job.str_modifier[1]);
			base_status.STR+=str_modifier;
			if (str_modifier>0)
				temp+="STR: +"+str_modifier+"\n";
		}
	
		if (job.dex_modifier!=null) {
			int dex_modifier=Randomizer.nextInt(job.dex_modifier[0],job.dex_modifier[1]);
			base_status.DEX+=dex_modifier;
			if (dex_modifier>0)
				temp+="DEX: +"+dex_modifier+"\n";
		}
	
		if (job.int_modifier!=null) {
			int int_modifier=Randomizer.nextInt(job.int_modifier[0],job.int_modifier[1]);
			base_status.INT+=int_modifier;
			if (int_modifier>0)
				temp+="INT: +"+int_modifier+"\n";
		}
	*/
		
		label.setText(temp);
		
		obj.status.setStatus(obj.inventory,obj.turn_effect_list);
		
		//this.add(label).fillX().row();
		//Show new Ability
		ability_table.clearChildren();
		int i=0;
		ability_table.add("ABILITIES").pad(5).colspan(4).row();
		if (obj.attain_ability_list!=null)
		for (ActionActor temp_ability:obj.attain_ability_list) {
			//((AbilityActor)temp_ability);
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(temp_ability),dungeon,window);
			ability_table.add(button).size(40,40).pad(5).left();
			i++;
			if (i%4==0)
				ability_table.row();
			
		}
		if (obj.attain_passive_ability_list!=null)
		for (ActionActor temp_ability:obj.attain_passive_ability_list) {
			//((AbilityActor)temp_ability);
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(temp_ability),dungeon,window);
			ability_table.add(button).size(40,40).pad(5).left();
			i++;
			if (i%4==0)
				ability_table.row();
		}
		
		//LevelupAbilityButton hire_button= new LevelupAbilityButton("max hire +1",dungeon,window);
		//ability_table.add(hire_button);
		//this.pack();
		/*
		if (obj.attain_ability_list.size()==1) {
			
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(obj.attain_ability_list.getFirst()),dungeon);
			this.add(button);
		}
		else
		{
			int index= rn.nextInt(obj.attain_ability_list.size());
			//int index2= rn.nextInt(obj.attain_ability_list.size());
			
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(obj.attain_ability_list.get(index)),dungeon);
			this.add(button);
		}
		*/
		this.setVisible(true);
		//randomly choose abilities among ~~
		//add abilieties to this
	}
	public void hideTable() {
		ability_table.clearChildren();
		this.setVisible(false);
	}
	public class AbilityInfoTable extends Table {
		DungeonGroup dungeon;
		AbilityActor ability;
		DrawRangeActor draw_range_actor;
		//Table top_table;
		//Table middle_table;
		//Table bottom_table;
		Image ability_icon;
		Label ability_info_label;
		public AbilityInfoTable () {
			super(Assets.getSkin());
			//this.setDebug(true,true);
			//this.setDebug(true,true);
			//top_table= new Table();
			//middle_table= new Table();
			//bottom_table= new Table();
			//this.add(top_table).pad(5).row();
			//this.add(middle_table).pad(5).row();
			//this.add(bottom_table).pad(5).row();
			ability_info_label=new Label("Bummer",new Label.LabelStyle(Assets.getFont(), Color.BLACK));
			//ability_info_label.setBounds(getX(), getY(), 150, 100);
			draw_range_actor= new DrawRangeActor();
			//ability_info_label.setWrap(true);
			this.add(ability_info_label).row();
			this.add(draw_range_actor).size(150,100);
			//bottom_table.setSize(150, 100);
			
			this.pack();
		}
		public void setAbilityActor (AbilityActor ability,ObjActor obj) {
			this.ability=ability;
			this.clear();
			this.add(ability_info_label).row();
			this.add(draw_range_actor).size(150,100);
			this.add(ability_icon).row();
			//ability_info_label.setWrap(true);
			//this.add(ability_info_label).width(150).height(250);
			this.pack();
			
			
			//ability_info_label.setWidth(100);
			int level=-1;
			LinkedList<ActionContainer> action_list=obj.getActionList();
			for (ActionContainer action:action_list) {
				if (action.action !=null &&action.action.equals(ability))
					level=action.level;
				draw_range_actor.setAbility((AbilityActor)ability);
			}
			setText(ability.toString2(level+1));
		}
		public void draw(Batch batch, float delta){
			super.draw(batch,delta);
			if (ability!=null) {
			//batch.draw(ability.getIcon(),getX()+45,getY()+270);
			
			//drawRange(bottom_table.getX()+450,bottom_table.getY()+100,ability,batch,delta);
			}
		}
		public void setText(String string) {
			ability_info_label.setText(string);
		
		}
		
			
	}
	public class DrawRangeActor extends Table {
		Image ability_icon;
		TextureRegion room_texture;
		int tile_width;
		int tile_height;
		int min_range;
		int max_range;
		boolean self_targeting;
		int min_splash;
		int max_splash;
		int splash_position_x=0;
		int splash_position_y=max_range;
		int range=8;
		
		private AbilityActor ability;
		public DrawRangeActor() {
			//this.setBounds(0, 0, 150, 100);
			TextureAtlas atlas= Assets.getAsset("sprite/minimap.atlas", TextureAtlas.class);
			room_texture=atlas.findRegion("room");
			tile_width=12;
			tile_height=6;
			
		}
		public void setAbility (AbilityActor ability) {
			this.ability=ability;
			min_range=ability.targeting_min_range;
			max_range=ability.targeting_max_range;
			self_targeting=ability.self_targeting;
			min_splash= ability.splash_min_range;
			max_splash= ability.splash_max_range;
			splash_position_x=0;
			splash_position_y=max_range;
			range=8;
			
		}
		
		public float mapToScreenCoordX(float x,int xxx,int yyy) {
			return (float) (getX()+getWidth()*0.5+0.5*tile_width*(xxx-yyy));
		}
		public float mapToScreenCoordY(float y,int xxx,int yyy) {
			return (float) (getY()+getHeight()*0.75-0.5*tile_height*(xxx+yyy));
		}
		public void act(Batch batch,float delta) {
			//this.setBounds(getX(), getY(), 150, 100);
		}
		public void draw(Batch batch,float delta) {
			super.draw(batch, delta);
			switch (ability.targeting_type) {
			case 0:
			for (int xx= -range;xx<range+1;xx++) {
				for (int yy= -range;yy<range+1;yy++) {
						int distance= Math.abs(xx)+Math.abs(yy);
						if (min_range<=distance && distance<=max_range)
							batch.setColor(Color.BLUE);
						else
							batch.setColor(Color.BLACK);
						if (Math.abs(xx)+Math.abs(yy)<=range)
								batch.draw(room_texture, mapToScreenCoordX(0,xx,yy)-(float)(0.5*tile_width),mapToScreenCoordY(0,xx,yy)-(float)(0.5*tile_height));
								batch.setColor(Color.WHITE);
				}
				
			}
			break;
			
			case 1:
				for (int xx= -range;xx<range+1;xx++) {
					for (int yy= -range;yy<range+1;yy++) {
						//int distance= Math.abs(xx)+Math.abs(yy);
						
						if ((xx==0 && Math.abs(yy)<=max_range && min_range<=Math.abs(yy)) ||
										(yy==0 && Math.abs(xx)<=max_range && min_range<=Math.abs(xx))
										)
										batch.setColor(Color.BLUE);
									else
										batch.setColor(Color.BLACK);
									if (Math.abs(xx)+Math.abs(yy)<= range)
									batch.draw(room_texture, mapToScreenCoordX(0,xx,yy)-(float)(0.5*tile_width),mapToScreenCoordY(0,xx,yy)-(float)(0.5*tile_height));
									batch.setColor(Color.WHITE);
					}
					
				}
			break;		
			}
			switch (ability.splash_type) {
			case 0:
				for (int xx=-max_splash;xx<max_splash+1;xx++) {
					for (int yy=max_range-max_splash;yy<max_range+max_splash+1;yy++) {
						if (Math.abs(xx-splash_position_x)+Math.abs(yy-splash_position_y)<=max_splash) {
							batch.setColor(Color.RED);
							batch.draw(room_texture, mapToScreenCoordX(0,xx,yy)-(float)(0.5*tile_width),mapToScreenCoordY(0,xx,yy)-(float)(0.5*tile_height));
						}
					}
				}
				break;
			case 2:
					for (int yy=min_range;yy<max_range+1;yy++) {
						//if (Math.abs(-splash_position_x)+Math.abs(yy-splash_position_y)<=max_splash) {
							batch.setColor(Color.RED);
							batch.draw(room_texture, mapToScreenCoordX(0,0,yy)-(float)(0.5*tile_width),mapToScreenCoordY(0,0,yy)-(float)(0.5*tile_height));
						//}
					}
				break;
			}
		}

		
	}

	
}
