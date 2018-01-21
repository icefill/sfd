package com.icefill.game.actors;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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

public class ObjInfoWindow extends BasicWindow {
	ObjActor obj;
	ImageButtonStyle style;
	//Table table;
	int pause_id;
	public int new_ability_point;
	ObjInfoWindow window;
	DungeonGroup dungeon;
	Table status_table;
	Table ability_table;
	Table left_table;
	Table left_up_table;
	Table left_down_table;
	PotraitTable potrait_table;
	
	Table job_table;
	Table job_table2;
	Table right_table;
	//Table table;
	Label label;
	Label job_label;
	Label job_label2;
	Label job_label3;
	
	TextButton button;
	
	TextButton current_job_button;
	TextButton[] job_button;
	
	
	
	ActionContainer ability_to_add;
	AbilityInfoTable ability_info_table;
	JobInfoTable job_info_table;
	public ObjInfoWindow(ObjActor obj_to_level_up,Skin skin){
		super(skin,true);
		TextureAtlas skinAtlas = Assets.getAsset(("ui/uiskin.atlas"),TextureAtlas.class);
		status_table = new Table();
		ability_table = new Table();
		left_table= new Table();
		left_up_table=new Table();
		job_table=new Table();
		job_table2=new Table();
		
		job_info_table= new JobInfoTable();
		right_table= new Table();
		obj = obj_to_level_up;
		
		window=this;
		
		label = new Label("", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.WHITE) );
	    job_label= new Label("JOBS", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.WHITE) );
	    job_label2= new Label("CURRENT: ", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.BLACK) );
	    job_label3= new Label("CHANGEABLE: ", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.BLACK) );
		
	    job_table.add(job_label).pad(7).colspan(2).row();
		current_job_button= new TextButton("LEARN", Assets.getSkin(), "default");
		current_job_button.addListener(new ClickListener() {
  	      public void clicked(InputEvent event, float x, float y) {
  	      	window.setJobInfo(window.getObj().getJob());
  	      }
  	    });
		job_table.add(job_label2).pad(5);
		
		job_table.add(current_job_button).pad(5).row();
    	job_table.add(job_label3).pad(5).top();
    	job_table.add(job_table2).pad(5).row();
		job_button= new TextButton[5];
	    for (int i=0;i<4;i++)
	    {
	    	final int n=i;
	    	job_button[i]= new TextButton("LEARN", Assets.getSkin(), "default");
	    	job_button[i].addListener(new ClickListener() {
	  	      public void clicked(InputEvent event, float x, float y) {
	  	    	window.setJobInfo(Assets.jobs_map.get(window.getObj().getJob().changeable_job[n]));  
	  	      	window.getObj();
	  	      }
	  	    });
	    	job_table2.add(job_button[i]).left().pad(5).row();
	    	job_button[i].setVisible(false);
	    	
	    }
	    
	    
	    TextButton close_button = new TextButton("CLOSE", Assets.getSkin(), "default");
	    close_button.addListener(new ClickListener() {
	      public void clicked(InputEvent event, float x, float y) {
	    	//if (window.getAbilityToAdd()!=null) { 
	      	//window.getObj().addAbility(window.getAbilityToAdd());
	  					//window.getObj().attain_ability_list.remove(getAbility());
	  					window.hideTable();
	  					window.getDungeon().setVisible(true);
	  					//if (windofow.getObj().status.current_ap>0)
	  					//	dungeon.popup_ui.addAbility(obj, true);
	    	//}
	      }
	    });
	    //left_up_table.add("SKILLS").row();
	    potrait_table= new PotraitTable(obj_to_level_up);
	    left_up_table.add(potrait_table).height(70).row();
	    left_up_table.add(status_table).row();
		left_up_table.add(ability_table).row();
		table.add(left_table);
		ability_info_table = new AbilityInfoTable();
		//left_up_table.setBackground(Assets.getBackground());
		//job_table.setBackground(Assets.getBackground());
		right_table.setBackground(Assets.getBackground());
		right_table.add(ability_info_table);
		ability_info_table.setVisible(false);
		table.add(right_table).size(250,250).row();
	    left_up_table.add(button).pad(5f).row();
	    //left_up_table.add(close_button);
	    left_table.add(left_up_table).size(150,150).pad(15).row();
	    left_table.add(job_table).size(150,100).pad(15);
		status_table.add(label);
		//obj_to_level_up.addActor(this);
		//this.add(table);
		this.setVisible(false);
		setJob(obj);
		setJobInfo(obj.job);
		table.row();
		table.add(close_button).colspan(2);
		//table.setDebug(true,true);
		
	}
	public void setAbilityToAdd(ActionContainer ability) {
		right_table.clear();
		right_table.add(ability_info_table);
		ability_to_add=ability;
		ability_info_table.setAbilityActor((AbilityActor)ability.action,obj);
		ability_info_table.setVisible(true);
		if (ability_to_add.level>obj.level || new_ability_point==0)
			button.setVisible(false);
		else
			button.setVisible(true);
	}
	public void setJob(ObjActor obj)
	{
		if (obj!=null)
		{
			current_job_button.setText(obj.job.job_name);
			if (obj.job.changeable_job!=null)
			{
				int job_size=obj.job.changeable_job.length;
				for (int i=0;i<4;i++)
				{
					if (i<job_size)
					{
						job_button[i].setVisible(true);
						job_button[i].setText(obj.job.changeable_job[i]);
					}
					else
					{
						job_button[i].setVisible(false);
						
					}
				}
			}
			else
			{
				for (int i=0;i<4;i++)
				{
					job_button[i].setVisible(false);
					
				}
			}
		}
	}
	public void setJobInfo(Job job) {
		right_table.clear();
		right_table.add(job_info_table);
		
		job_info_table.setVisible(true);
		job_info_table.setJobInfo(job);
		
	}
	
	public ActionContainer getAbilityToAdd() {
		return ability_to_add;
	}
	public DungeonGroup getDungeon() {
		return dungeon;
	}
	public void levelUp(final DungeonGroup dungeon) {
		statUp();
		obj.inventory.renewStatus();
		
		this.dungeon=dungeon;
		if (obj.attain_ability_list !=null 
				&& !obj.attain_ability_list.isEmpty()) {
			new_ability_point+=1;
			dungeon.setVisible(true);
			
		}
		//obj.status.current_hp=obj.status.total_status.HP;
		if (obj.getSkillWindow()!=null)
			obj.getSkillWindow().chooseAndAddAbilities(dungeon);
		
		//this.setVisible(true);
	}
	public void changeAttainAbilities()
	{
		
	}
	public void statUp() {
		Job job= obj.getJob();
		Status base_status=obj.status.base_status;
		if (job.hp_modifier!=null) {
			int hp_modifier=Randomizer.nextInt(job.hp_modifier[0],job.hp_modifier[1]);
			base_status.HP+=hp_modifier;
			if (hp_modifier>0)
				obj.showStatusChange("+HP: +"+hp_modifier+"\n",0);
		}
		if (job.str_modifier!=null) {
			int str_modifier=Randomizer.nextInt(job.str_modifier[0],job.str_modifier[1]);
			base_status.STR+=str_modifier;
			if (str_modifier>0)
				obj.showStatusChange("+STR: +"+str_modifier+"\n",0);
		}
	
		if (job.dex_modifier!=null) {
			int dex_modifier=Randomizer.nextInt(job.dex_modifier[0],job.dex_modifier[1]);
			base_status.DEX+=dex_modifier;
			if (dex_modifier>0)
				obj.showStatusChange("+DEX: +"+dex_modifier+"\n",0);
		}
	
		if (job.int_modifier!=null) {
			int int_modifier=Randomizer.nextInt(job.int_modifier[0],job.int_modifier[1]);
			base_status.INT+=int_modifier;
			if (int_modifier>0)
				obj.showStatusChange("+INT: +"+int_modifier+"\n",0);
		}
		obj.status.setStatus(obj.getInventory(), obj.turn_effect_list);
	}
	public ObjActor getObj() {
		return obj;
	}
	public void chooseAndAddAbilities(final DungeonGroup dungeon) {
		// Show stat change
		Global.getUIStage().addActor(this);
		this.setJobInfo(obj.job);
		//this.setFillParent(true);
		this.dungeon=dungeon;
		Random rn= new Random();
		Status base_status=obj.status.base_status;
		//Job job= obj.getJob();
		//String temp=obj.getJob().job_name+"  /   LEVEL:"+obj.level+"  /POINT:"+new_ability_point+"\n\n";

		label.setText("Abilities");
		
		obj.status.setStatus(obj.inventory,obj.turn_effect_list);
		
		//this.add(label).fillX().row();
		//Show new Ability
		ability_table.clearChildren();
		for (ActionContainer temp_container:obj.ability_list) {
			ActionActor temp_ability=temp_container.action;
			//((AbilityActor)temp_ability);
			if (temp_ability!=null && temp_ability instanceof AbilityActor)
			{
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(temp_ability),dungeon);
			ability_table.add(button);
			}
		}
		ability_table.row();
		if (obj.passive_action_list!=null)
		for (ActionActor temp_ability:obj.passive_action_list) {
			//((AbilityActor)temp_ability);
			if (temp_ability!=null && temp_ability instanceof AbilityActor)
			{
			LevelupAbilityButton button= new LevelupAbilityButton((AbilityActor)(temp_ability),dungeon);
			ability_table.add(button);
			}
		}
		setJob(obj);
		
		this.setVisible(true);
		
	}
	public void hideTable() {
		ability_table.clearChildren();
      //  Global.setCameraCenter();
        this.setVisible(false);
	}
	public class LevelupAbilityButton extends BasicActor {
		final AbilityActor ability;
		//protected TextureRegion icon_texture;
		//protected String action_name;
		protected boolean on_cursor;
		public AbilityActor getAbility() {
			return ability;
		}
		public LevelupAbilityButton(AbilityActor ability,final DungeonGroup dungeon){
			this.ability=ability;
			this.setBounds(0, 0, 32, 40);
			this.addListener(new InputListener() {
				public void enter(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
					on_cursor=true;
				}
				public void exit(InputEvent event,float x, float y, int pointer,Actor fromActor)
				{
					on_cursor=false;
				}
				
			    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    	window.setAbilityToAdd(new ActionContainer(getAbility(),0));
					//window.getObj().action_list.add(new ActionContainer(getAbility(),0));
					return true;
			    	}
			    });

		}
		
		public void act(float delta){
			super.act(delta);
			this.setBounds(getX(), getY(),32,40);
		}
		public void draw(Batch batch, float delta){// For drawing icon
			if(((AbilityActor)ability).required_level>obj.level) {
				batch.setColor(.3f,.2f,.2f,1f);
			}
			else {
				if (on_cursor)
					batch.setColor(0f,0f,1f,1f);
			}
			batch.draw(ability.getIcon(), getX(), getY(),32,40);
			
			Assets.getFont().draw(batch, " "+ability.required_level, getX(), getY());
			batch.setColor(1f,1f,1f,1f);
			//Assets.getFont().draw(batch,ability.getActionName(), getX(), getY()+120);
		}
		
	}
	public class JobTable extends Table {
		private Button current_job_button;
		
		public JobTable()
		{
			super();
			current_job_button= new Button();
			button = new TextButton("Bummer", Assets.getSkin(), "default");
			button.setVisible(false);
		    button.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		    	if (window.getAbilityToAdd()!=null&& new_ability_point>0 && ((AbilityActor)(window.getAbilityToAdd().action)).required_level+window.getAbilityToAdd().level<=obj.level) { 
		      	//window.getObj().addAbility((AbilityActor)window.getAbilityToAdd().action);
		      	//new_ability_point--;
		      	//String temp=obj.getJob().job_name+"CHOOSE NEW ABILITY."+new_ability_point+"\n\n";
				//label.setText(temp);
			
		  					//window.getObj().attain_ability_list.remove(getAbility());
		  					//window.hideTable();
		  					//window.getDungeon().setVisible(false);
		  					//if (window.getObj().status.current_ap>0)
		  					//	dungeon.popup_ui.addAbility(obj, true);
		    	}
		      }
		    });
		}
		/*
		public void setJob(ObjActor obj)
		{
			if (obj!=null)
			{
				
			}
		}
		*/
	}
	public class JobInfoTable extends Table {
		private Label job_info_label;
		private TextButton change_job;
		private Job info_job;
		
		public void setJobInfo(final Job job)
		{
			
			if (job_info_label==null)
			{
				job_info_label= new Label("Bummer",new Label.LabelStyle(Assets.getFont(), Color.WHITE));
				this.add(job_info_label).pad(5).row();
				button = new TextButton("CHANGE JOB", Assets.getSkin(), "default");
				button.setVisible(false);
			    button.addListener(new ClickListener() {
			      public void clicked(InputEvent event, float x, float y) {
			    	if (Global.getSelectedObj().level>=info_job.getJPNeed())
			    	{
			    		Global.getSelectedObj().setJob(info_job, false);
			    		//Global.getPlayerTeam().decreaseJP(info_job.getJPNeed());
			    		obj.getSkillWindow().chooseAndAddAbilities(dungeon);
			    	}
			    	else
			    	{
			    		Global.showMessage("NOT ENOUGH LEVEL",1);
			    	}
					  }
			    });
			    this.add(button);
			    
			
			}
			if (job !=null)
			{
				info_job=job;
				String job_info;
				job_info="JOB INFORMATION\n\n"+
						"*NAME: "+job.job_name+"\n\n "
								+job.description+"\n\n"
								+"*REQUIRED LEVEL TO CHANGE JOB: "+job.getJPNeed()+"\n\n";
				if (job.ability_name!=null)
				job_info+="*Base Skill: \n "+getString(job.ability_name)+"\n\n";
				if (job.attainable_ability!=null)
				job_info+="*Attainable Skill: \n "+getString(job.attainable_ability)+"\n\n";
				if (job.attainable_passive_ability!=null)
				job_info+="*Attainable Passive: \n "+getString(job.attainable_passive_ability)+"\n\n";
				job_info+="*magic level\n fire:"+job.fire_level
						+"  lightning: "+job.lightning_level
						+"\n holy: "+job.holy_level
						+ "  unholy: "+job.unholy_level;
				job_info_label.setColor(Color.WHITE);
				job_info_label.setText(job_info);
				if (obj.job==job)
			    	button.setVisible(false);
			    else
			    	button.setVisible(true);
			}
			
		}
	}
	public String getString(String[] list)
	{
		String to_return="";
		int i=0;
		for (String temp_str:list)
		{
			i++;
			to_return+="["+temp_str+"] ";
			if (i%2==0)
				to_return+="\n";
		}
		return to_return;
	}
	public String getString(List<String> list)
	{
		String to_return="";
		int i=0;
		for (String temp_str:list)
		{
			i++;
			to_return+="["+temp_str+"] ";
			if (i%2==0)
				to_return+="\n";
		}
		return to_return;
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
			//middle_table.pack();
			this.add(draw_range_actor).size(150,100);
			//bottom_table.setSize(150, 100);
			
			this.pack();
		}
		public void setAbilityActor (AbilityActor ability,ObjActor obj) {
			this.ability=ability;
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
