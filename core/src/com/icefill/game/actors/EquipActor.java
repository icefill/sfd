package com.icefill.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.icefill.game.Assets;
import com.icefill.game.Constants;
import com.icefill.game.utils.Randomizer;
import com.icefill.game.actors.actionActors.AbilityActor;
import com.icefill.game.actors.actionActors.ActionActor.ActionContainer;
import com.icefill.game.sprites.NonObjSprites;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class EquipActor extends BasicActor implements Constants {
	float direction_rotation[];
	DIR direction_before=DIR.AB;
	public int type=0;
	public int price;
	public int n=1;
	public String name;
	public String short_name;
	public String type_for_ability;
	public String gear_type;
	ImageButtonStyle style;
	public EquipStatus status;
	private boolean disposable;
	public boolean two_handed;
	public ActionContainer equip_action;
	boolean has_background;
	//PlaceHolderEquipActor place_holder;
	
	Color body_color;
	public EquipActor() {
		// TODO Auto-generated constructor stub
	}
	
    public EquipActor(String json_name) {
    	this.setHeight(30);
    	this.setWidth(30);
    	if (json_name.startsWith("S#")) {
    		AbilityActor ability=(AbilityActor)Assets.actions_map.get(json_name.substring(2));
    		equip_action=new ActionContainer(ability,1);
    		this.name= ability.getActionName();
    		this.short_name=ability.getShortName();
        	this.status=null;
        	this.setDisposable(true);
        	this.sprites=new NonObjSprites(ability.getIcon());
        	has_background=true;
        	this.price=ability.price;
        	direction_rotation=new float[4];
        	//if (temp_factory.body_color !=null)
        	//	body_color= new Color(temp_factory.body_color[0],temp_factory.body_color[1],temp_factory.body_color[2],temp_factory.body_color[3]);
        	body_color= Color.BLACK;
        	this.type=4;
        	type_for_ability=new String("scroll");
        	
    	}
    	else {
    	Json equip_json = new Json();
    	EquipActor.Factory temp_factory;
    	String name;
    	int level;
			if (json_name.startsWith("L#")) {
				name=json_name.substring(3);
				level=Integer.parseInt(json_name.substring(2,3));
			}
			else
			{
				name=json_name;
				level=0;
			}
    	temp_factory= equip_json.fromJson(EquipActor.Factory.class
    				,Gdx.files.internal("objs_data/equipment/"+ name+".json"));
    	if (temp_factory.equip_action!=null)
    		equip_action=new ActionContainer(Assets.actions_map.get(temp_factory.equip_action),level);
    	if (temp_factory.n>0)
    		this.n=temp_factory.n;
    	//else
    		//equip_action=new ActionContainer(Assets.actions_map.get("Throw item"),0);
    	this.name= temp_factory.name;
    	this.short_name=temp_factory.short_name;
    	if (temp_factory.status!=null)
    	{
    		this.status=temp_factory.status.getStatus();
    	}
    	this.setDisposable(temp_factory.disposable);
    	this.sprites=Assets.non_obj_sprites_map.get(temp_factory.sprites_name);
    	direction_rotation=temp_factory.direction_rotation;
    	if (temp_factory.body_color !=null)
    		body_color= new Color(temp_factory.body_color[0],temp_factory.body_color[1],temp_factory.body_color[2],temp_factory.body_color[3]);
    	else body_color= Color.BLACK;
    	this.type=temp_factory.item_type;
    	type_for_ability=temp_factory.type;
    	this.gear_type=temp_factory.type;
    	this.two_handed=temp_factory.two_handed;
    	this.price=temp_factory.price;
    	//if (two_handed)
    	//{
    	//	place_holder= new PlaceHolderEquipActor(this);
    	//}
    	}
    	if (equip_action!=null)
    	{
    		equip_action.setManaFree();
    	}
    	createButtonStyle();
    	
    }
    public EquipActor(AbilityActor ability,int level) {
    	
    	//Json floor_json = new Json();
    	//EquipActor.Factory temp_factory;
    	//String atlas_path="sprite/"+json_name+".atlas";
    	
    	//temp_factory= floor_json.fromJson(EquipActor.Factory.class
    	//			,Gdx.files.internal("objs_data/equipment/"+ json_name+".json"));
    	equip_action=new ActionContainer(ability,level);
    	//else
    		//equip_action=new ActionContainer(Assets.actions_map.get("Throw item"),0);
    	this.name= ability.getActionName();
    	this.name=ability.getShortName();
    	this.status=null;
    	this.setDisposable(true);
    	this.sprites=new NonObjSprites(ability.getIcon());
    	direction_rotation=new float[4];
    	//if (temp_factory.body_color !=null)
    	//	body_color= new Color(temp_factory.body_color[0],temp_factory.body_color[1],temp_factory.body_color[2],temp_factory.body_color[3]);
    	body_color= Color.BLACK;
    	this.type=4;
    	type_for_ability=new String("scroll");
    	createButtonStyle();
	
	}
    /*
	public EquipActor(NonObjSprites sprites) {
		this.sprites=sprites;
		direction_rotation= new float[4];	
	}*/
    public boolean hasBackground()
    {
    	return has_background;
    }
	public String getName(){
		return name;
	}
	//public PlaceHolderEquipActor getPlaceHolder() {
	//	return place_holder;
	//}
	public Color getBodyColor() {return body_color;}
	public EquipActor(NonObjSprites sprites,int dl,int dr,int ur,int ul) {
		this.sprites=sprites;
		direction_rotation= new float[4];
		direction_rotation[DIR.DL.v]=dl;
		direction_rotation[DIR.DR.v]=dr;
		direction_rotation[DIR.UL.v]=ul;
		direction_rotation[DIR.UR.v]=ur;
		
	}
	public String getGearType()
	{
		return gear_type;
	}
	public NonObjSprites getSprites() {
		return (NonObjSprites)sprites;
	}
	
	public void drawAnimation(Batch batch, float elapsed_time,int animation,DIR direction){
			if (direction != direction_before) {
				setRotation(direction_rotation[direction.v]);
				direction_before=direction;
			}
			((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, direction,getX(), getY()); 

	}
	public void drawRotatableAnimation(Batch batch, float elapsed_time,int animation,DIR direction){
		if (direction != direction_before) {
			setRotation(direction_rotation[direction.v]);
			direction_before=direction;
		}
		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, direction,getX(), getY(),this.getRotation(), 1, 1); 

}
	public void drawEquip(Batch batch, float delta) {
		((NonObjSprites)sprites).drawAnimation(batch,elapsed_time, 0, DIR.DL, sprites.getAnchorPointX()+5, sprites.getAnchorPointY()+5);
	}
/*
	public void drawAnimation(Batch batch, float elapsed_time,int animation,int direction,float rotation,float x,float y){
		((NonObjSprites)sprites).drawAnimation(batch, elapsed_time, 0, direction, x, y, rotation, 1, 1);
		
}*/
	public int getType() {return type;}
	public String getTypeForAbility() {return type_for_ability;}
	public EquipStatus getStatus() {return status;}
	public void setRotationDirection(DIR direction){
		setRotation(direction_rotation[direction.v]);
		direction_before=direction;
	}
	public Action setRotationDirectionAction(final DIR direction) {
		return Actions.run(new Runnable() {public void run(){self.addAction(Actions.rotateTo(direction_rotation[direction.v]));}});
	}
	public Action setRotationAction(final float degrees){
		return Actions.run(new Runnable() {public void run(){self.addAction(Actions.rotateTo(degrees));}});
		//return Actions.run(new Runnable() {public void run() {Actions.rotateTo(degrees);}});//self.setRotation(degrees);}} );
	}
	

	
	public Action rotateAnimation(final float from,final float to,final float duration) {
		//setRotation(from);
		final SequenceAction action =new SequenceAction();
		action.addAction(Actions.rotateTo(from));
		action.addAction(Actions.rotateTo(to, duration));
		action.addAction(Actions.delay(.6f));
		return Actions.run(new Runnable() {public void run(){self.addAction(action);}});
	}
	public Action rotateAnimation(final float to,final float duration) {
		final SequenceAction action =new SequenceAction();
		action.addAction(Actions.rotateTo(to, duration));
		//action.addAction(Actions.delay(.6f));
		return Actions.run(new Runnable() {public void run(){self.addAction(action);}});
	}
	public Action rotateByAnimation(final float amount,final float duration) {
		final SequenceAction action =new SequenceAction();
		action.addAction(Actions.rotateBy(amount, duration));
		return Actions.run(new Runnable() {public void run(){self.addAction(action);}});
	}
	

	public static class Factory {
		String name;
		String short_name;
		String type;
		String sprites_name;
		String equip_action;
		boolean disposable;
		float[] direction_rotation;
		float[] body_color;
		int item_type;
		int price;
		int n;
		boolean two_handed;
		EquipStatusSeed status;
	}
	public static class EquipStatusSeed {
		public int STR_MODIFIER;
		public int DEX_MODIFIER;
		public int INT_MODIFIER;
		public int HP_MODIFIER;
		public int DODGE_MODIFIER;
		public int MIN_ATTACK;
		public int MAX_ATTACK;
		public int CRITICAL;
		public int CRITICAL_H;
		public int DEFENSE;
		public int ACCURACY;
		public int BLOCK;
		public int STR_MODIFIER_H;
		public int DEX_MODIFIER_H;
		public int INT_MODIFIER_H;
		public int HP_MODIFIER_H;
		public int DODGE_MODIFIER_H;
		public int MIN_ATTACK_H;
		public int MAX_ATTACK_H;
		public int DEFENSE_H;
		public int ACCURACY_H;
		public int BLOCK_H;
		public EquipStatus getStatus()
		{
			EquipStatus equip_status= new EquipStatus();
			equip_status.STR_MODIFIER=STR_MODIFIER;
			equip_status.DEX_MODIFIER=DEX_MODIFIER;
			equip_status.INT_MODIFIER=INT_MODIFIER;
			equip_status.HP_MODIFIER=HP_MODIFIER;
			equip_status.DODGE_MODIFIER=DODGE_MODIFIER;
			equip_status.MIN_ATTACK=MIN_ATTACK;
			equip_status.MAX_ATTACK=MAX_ATTACK;
			equip_status.CRITICAL=CRITICAL;
			equip_status.DEFENSE=DEFENSE;
			equip_status.ACCURACY=ACCURACY;
			equip_status.BLOCK=BLOCK;
			
			
			if (STR_MODIFIER_H!=0)
			{
				equip_status.STR_MODIFIER=Randomizer.nextInt(STR_MODIFIER,STR_MODIFIER_H);
			}
			if (DEX_MODIFIER_H!=0)
			{
				equip_status.DEX_MODIFIER=Randomizer.nextInt(DEX_MODIFIER,DEX_MODIFIER_H);
			}
			if (INT_MODIFIER_H!=0)
			{
				equip_status.INT_MODIFIER=Randomizer.nextInt(INT_MODIFIER,INT_MODIFIER_H);
			}
			if (HP_MODIFIER_H!=0)
			{
				equip_status.HP_MODIFIER=Randomizer.nextInt(HP_MODIFIER,HP_MODIFIER_H);
			}
			if (DODGE_MODIFIER_H!=0)
			{
				equip_status.DODGE_MODIFIER=Randomizer.nextInt(DODGE_MODIFIER,DODGE_MODIFIER_H);
			}
			if (MIN_ATTACK_H!=0)
			{
				equip_status.MIN_ATTACK=Randomizer.nextInt(MIN_ATTACK,MIN_ATTACK_H);
			}
			if (MAX_ATTACK_H!=0)
			{
				equip_status.MAX_ATTACK=Randomizer.nextInt(MAX_ATTACK,MAX_ATTACK_H);
				if (equip_status.MIN_ATTACK>equip_status.MAX_ATTACK)
				{
					equip_status.MIN_ATTACK=equip_status.MAX_ATTACK;
				}
			}
			if (CRITICAL_H!=0)
			{
				equip_status.CRITICAL=Randomizer.nextInt(CRITICAL,CRITICAL_H);
			}
			
			if (DEFENSE_H!=0)
			{
				equip_status.DEFENSE=Randomizer.nextInt(DEFENSE,DEFENSE_H);
			}if (ACCURACY_H!=0)
			{
				equip_status.ACCURACY=Randomizer.nextInt(ACCURACY,ACCURACY_H);
			}if (BLOCK_H!=0)
			{
				equip_status.BLOCK=Randomizer.nextInt(BLOCK,BLOCK_H);
			}
			
			
			return equip_status;
		}
	}
	public static class EquipStatus {
		public int STR_MODIFIER;
		public int DEX_MODIFIER;
		public int INT_MODIFIER;
		public int HP_MODIFIER;
		public int DODGE_MODIFIER;
		public int CRITICAL_MODIFIER;
		public int MIN_ATTACK;
		public int MAX_ATTACK;
		public int CRITICAL;
		
		public int DEFENSE;
		public int ACCURACY;
		public int BLOCK;
		
		
	}
	public TextureRegionDrawable getInventoryImage(){
		return new TextureRegionDrawable(sprites.getRepresentativeAnimations().getKeyFrame(0));
	}
	protected void createButtonStyle() {
		style = new ImageButtonStyle(Assets.getSkin().get(ButtonStyle.class));
		//Texture texture= new Texture(sprites.getRepresentativeAnimations().getKeyFrame(0).getTexture());
		if (sprites!=null) {
		style.imageUp=new TextureRegionDrawable(sprites.getRepresentativeAnimations().getKeyFrame(0));
		style.imageDown=new TextureRegionDrawable(sprites.getRepresentativeAnimations().getKeyFrame(0));
		}
	}
	public ImageButtonStyle getButtonStyle() {
		return style;
	}
	public String toString() {
		String to_return=name+"\n\n";
		if (status!=null) {
			if (status.MIN_ATTACK>0) to_return+="ATK:"+status.MIN_ATTACK+"";
			if (status.MAX_ATTACK>0) to_return+="~"+status.MAX_ATTACK+"\n";
			else to_return+="\n";
			if (status.ACCURACY>0) to_return+="ACC:"+status.ACCURACY+"\n";
			if (status.CRITICAL>0) to_return+="CRIT:"+status.CRITICAL+"\n";
			if (status.STR_MODIFIER>0) to_return+="STR:"+status.STR_MODIFIER+"\n";
			if (status.DEX_MODIFIER>0) to_return+="DEX:"+status.DEX_MODIFIER+"\n";
			if (status.INT_MODIFIER>0) to_return+="INT:"+status.INT_MODIFIER+"\n";
			if (status.HP_MODIFIER>0) to_return+="HP:"+status.HP_MODIFIER+"\n";
			if (status.DODGE_MODIFIER>0) to_return+="STR:"+status.DODGE_MODIFIER+"\n";
			if (status.DEFENSE>0) to_return+="DEF:"+status.DEFENSE+"\n";
			if (status.BLOCK>0) to_return+="BLK:"+status.BLOCK+"\n";
			if (equip_action!=null) to_return+="SKILL:"+equip_action.action.getActionName()+"\n";
			if (price>0)to_return+="PRICE:"+price+"\n";
		}
		return to_return;
	}

	public boolean isDisposable() {
		return disposable;
	}

	public void setDisposable(boolean disposable) {
		this.disposable = disposable;
	}
}
