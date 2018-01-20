package com.icefill.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.utils.NonRepeatRandomizer;
import com.icefill.game.utils.Randomizer;

public class BattleWinWindow extends BasicWindow{
	
	ObjActor obj;
	ImageButtonStyle style;
	//Table table;
	BattleWinWindow window;
	DungeonGroup dungeon;
	Label label;
	//Table table;
	int sum;
	TextButton exp_button;
	TextButton revive_button;
	TextButton hp_button;
	TextButton mp_button;
	TextButton jp_button;
	
	TextButton item_button;
	TextButton magic_item_button;

	TextButton hero_button;
	TextButton hero_button2;
	BattleWinWindow self=this;
	String hero1;
	String hero2;
	String magic_item;
	NonRepeatRandomizer rn= new NonRepeatRandomizer(5);
    
	
	public String getHero(int i)
	{
		if (i==0)
			return hero1;
		else
			return hero2;
	}
	public String getMagicItem()
	{
		return magic_item;
	}
	public BattleWinWindow(Skin skin){
		super(skin,false);
		//table= new Table(skin);
		TextureAtlas skinAtlas = Assets.getAsset(("ui/uiskin.atlas"),TextureAtlas.class);
		
		//NinePatch background = new NinePatch(Assets.getAsset(("sprite/background.png"),Texture.class), 5, 5, 4, 4);
		
		//this.setBackground(new NinePatchDrawable(background));
		
		window=this;
		label = new Label("CHOOSE TREAT!", new Label.LabelStyle(Assets.getSkin().getFont("default-font"), Color.WHITE) );
	
		
	    label.setFontScale(1.2f);
		//this.setFillParent(true);
		//this.center();
		
		
		exp_button = new TextButton("Experience:", Assets.getSkin(), "default");
	    exp_button.addListener(new ClickListener() {
	      public void clicked(InputEvent event, float x, float y) {
	    	   
	      	 //if (!getDungeon().getTeamList(0).isEmpty())
	      	 // sum/=getDungeon().getTeamList(0).size();
	      	 for (ObjActor obj: getDungeon().getTeamList(0)) {
	     		
	     		obj.gainExperience(sum, getDungeon());
	     	}
	     	//getDungeon().getDeadEnemyList().clear();	      	  
	  					window.hideTable();
	  					window.getDungeon().setVisible(true);
	  		//EquipActor item= new EquipActor()
	  		//getDungeon().getCurrentRoom().setItem(item)
	      }
	      
	    });
	    jp_button = new TextButton("JP 3:", Assets.getSkin(), "default");
	    jp_button.addListener(new ClickListener() {
	      public void clicked(InputEvent event, float x, float y) {
	    	  if (sum!=0)
	    		  Global.getPlayerTeam().increaseJP(sum/5);
	    	//getDungeon().getDeadEnemyList().clear();	      	  
	    	window.hideTable();
	  		window.getDungeon().setVisible(true);

	      }
	      
	    });
	    
	    hero_button = new TextButton("NEW HERO: ", Assets.getSkin(), "default");
	    hero_button.addListener(new ClickListener() {
	    	  public void clicked(InputEvent event, float x, float y) {
	    		  	Global.getPlayerTeam().addPlayerandSet(self.getHero(0),Global.getCurrentRoom());
	    		    		    
	    		    Global.gfs.pushState(10);
	    			//getDungeon().getDeadEnemyList().clear();	      	  
	    	  		
	    			window.hideTable();
	    	  } 		      
	   });
	   hero_button.setVisible(false); 		    
	    		    
	   hero_button2 = new TextButton("NEW HERO: ", Assets.getSkin(), "default");
	    hero_button2.addListener(new ClickListener() {
	    	  public void clicked(InputEvent event, float x, float y) {
	    		  Global.getPlayerTeam().addPlayerandSet(self.getHero(1),Global.getCurrentRoom());
	    		    	
	    		    		    
	    		    Global.gfs.pushState(10);
	    			//getDungeon().getDeadEnemyList().clear();	      	  
	    	  		
	    			window.hideTable();
	    	  } 		      
	   });
	   hero_button.setVisible(false);
	   magic_item_button = new TextButton("MAGIC ITEM: ", Assets.getSkin(), "default");
	   magic_item_button.addListener(new ClickListener() {
	    	  public void clicked(InputEvent event, float x, float y) {
	    		  int xx= dungeon.getCurrentMap().getCenterXX();
	  				int yy= dungeon.getCurrentMap().getCenterYY();
	  				AreaCell temp=dungeon.getCurrentRoom().getMap().getCell(xx, yy);
	  				//ItemActor item= new ItemActor(relic,null);
	  				dungeon.getCurrentRoom().setItem(xx,yy, new EquipActor(self.getMagicItem()),true);
	    		  		
	    		    		    
	    		    Global.gfs.pushState(10);
	    			//getDungeon().getDeadEnemyList().clear();	      	  
	    	  		
	    			window.hideTable();
	    	  } 		      
	   });
	   magic_item_button.setVisible(false);
	   
	    item_button = new TextButton("ITEM", Assets.getSkin(), "default");
	    item_button.addListener(new ClickListener() {
	    	 public void clicked(InputEvent event, float x, float y) {
	 	    	
	    EquipActor relic;
	    int i=Randomizer.nextInt(10);

	    if (i<5) relic= new EquipActor(dungeon.item_pool.getItem(dungeon.room_zzz+1));
	    else if (i<8) relic= new EquipActor(dungeon.equip_pool.getItem(dungeon.room_zzz+1));
	    else relic= new EquipActor(dungeon.scroll_pool.getItem(dungeon.room_zzz+1));
		if (relic!=null) {
			int xx= dungeon.getCurrentMap().getCenterXX();
			int yy= dungeon.getCurrentMap().getCenterYY();
			dungeon.getCurrentRoom().setItem(xx,yy, relic,true);
		}
		Global.gfs.pushState(10);
		//getDungeon().getDeadEnemyList().clear();	      	  
  		
		window.hideTable();
	      }
	    });
	    hp_button=new TextButton("Replenish\nHealth", Assets.getSkin(), "default");
	    hp_button.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		    	 // getDungeon().getDeadEnemyList().clear();	      	  
			  		
		    	  for (ObjActor temp_player:getDungeon().getTeamList(0)) {
		  			temp_player.status.healInRatio(3.0f/5);
		  			ProjectileActor prj= new ProjectileActor("particles/particle_heal.json",null,100,new Color(.0f,.0f,.3f,1f));
		  			prj.setPosition(temp_player.getX(), temp_player.getY());
		  			prj.setZ(50);
		  			getDungeon().getCurrentRoom().addActor(prj);
		  			prj.addAction(
		  					Actions.sequence(
		  							prj.startAction()
		  							,Actions.delay(.5f)
		  							,prj.deActivateAndEndAction()
		  							
		  							//,prj.endActionSubAction()
		  							//,Actions.run(new Runnable() {public void run() {prj.remove();}})
		  					)
		  			);
		  		}
		  		
		  					//window.getObj().attain_ability_list.remove(getAbility());
		  					window.hideTable();
		  					window.getDungeon().setVisible(true);
		  					//if (window.getObj().status.current_ap>0)
		  					//	dungeon.popup_ui.addAbility(obj, true);
		      }
		      
		    });
	    revive_button=new TextButton("Rivive", Assets.getSkin(), "default");
	    revive_button.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {

		  		window.hideTable();
					window.getDungeon().setVisible(true);
					
		  		
      }
		      
		    });
	    
	    mp_button=new TextButton("Mana:", Assets.getSkin(), "default");
	    mp_button.addListener(new ClickListener() {
		      public void clicked(InputEvent event, float x, float y) {
		    	  //Global.getPlayerTeam().increaseMana(getDungeon().getDeadEnemyList().size());
		    	  //getDungeon().getDeadEnemyList().clear();	      	  
			  	  window.hideTable();
		  		  window.getDungeon().setVisible(true);
		  					//if (window.getObj().status.current_ap>0)
		  					//	dungeon.popup_ui.addAbility(obj, true);
		      }
		      
		    });
	    
	   // table= new Table();
	   // table.center();
	    table.add(label).pad(4).colspan(4).row();
	    table.add(exp_button).pad(4).size(70,70);
	    table.add(item_button).pad(4).size(70,70);
	    //table.add(jp_button).pad(4).size(70,70);
	    //table.add(magic_item_button).pad(4).size(70,70);
	    table.add(hp_button).pad(4).size(70,70);
	    
	    //table.add(hp_button).pad(10).row();
	    //table.add(mp_button).pad(10).row();
	    //table.add(revive_button).row();
	    //table.setBackground(Assets.getBackground());
		
	    Global.getUIStage().addActor(this);
	   // window.add(table);
	    //this.setFillParent(true);
	    //this.center();
	    table.pack();
		  
		this.setVisible(false);
		
	}

	public DungeonGroup getDungeon() {
		return dungeon;
	}
	public String getHeroName(int i)
	{
    	switch(i){
    		case 0:
    			return "spike";
    		case 1:
    			return "magic_user1";
    			
    		case 2:
    			return "jerry";
    		case 3:
    			return "felix";
     		case 4:
    			return "teresa";
    		default:
    			return "spike";
    	}

	}
	public String getHeroJob(int i)
	{
    	switch(i){
    		case 0:
    			return "knight";
    		case 1:
    			return "wizard";
    			
    		case 2:
    			return "ranger";
    		case 3:
    			return "spearman";
     		case 4:
    			return "cleric";
    		default:
    			return "knight";
    	}

	}
	public String getMagicItemName(int i)
	{
    	switch(i){
    		case 0:
    			return "protection_shield";
    		case 1:
    			return "healing_staff";
    			
    		case 2:
    			return "flame_tongue";
    		case 3:
    			return "venom_staff";
    		default:
    			return "healing_staff";
    	}

	}

	public void battleWin(final DungeonGroup dungeon) {
		this.dungeon=dungeon;
		//this.mp_button.setText("MANA:"+getDungeon().getDeadEnemyList().size());
		if (Global.getCurrentRoom().boss_room && Global.getCurrentRoom().room_zz%2 ==1) {
			rn.reset();
			int rn1=rn.nextInt();
			int rn2=rn.nextInt();
			
			hero1=getHeroName(rn1);
		    hero_button.setVisible(true);
		    hero_button.setText("NEW HERO:"+getHeroJob(rn1));
		    hero2=getHeroName(rn2);
		    hero_button2.setVisible(true);
		    hero_button2.setText("NEW HERO:"+getHeroJob(rn2));
		    magic_item_button.setVisible(false);
		}
		else if (Global.getCurrentRoom().boss_room && Global.getCurrentRoom().room_zz%2 ==0)
			{
			magic_item_button.setVisible(true);
			magic_item=getMagicItemName(Randomizer.nextInt(3));
			magic_item_button.setText("MAGIC ITEM:"+magic_item);
			hero_button.setVisible(false);
			hero_button2.setVisible(false);
			
			}
		else
		{
			hero_button.setVisible(false);
			hero_button2.setVisible(false);
			
			magic_item_button.setVisible(false);
		}
		
		sum=getDungeon().getTeamList(1).size();
		Global.getPlayerTeam().increaseGold(sum*10);
		
		
      	sum+=Global.dungeon.getTeamList(0).size();
      	
    	sum*=5;
    	if (sum!=0)
    	jp_button.setText("JP: "+sum/5);
      	exp_button.setText("EXP: "+sum);
      	jp_button.setVisible(true);
		this.setVisible(true);
	}

	public ObjActor getObj() {
		return obj;
	}
	public void hideTable() {
		this.setVisible(false);
		for (ObjActor obj:Global.getPlayerTeam())
		{
			if (!obj.isDead())
			{
				obj.checkLevelUp(Global.dungeon);
			}
		}
	}
		
}
