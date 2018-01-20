package com.icefill.game.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.icefill.game.Assets;
import com.icefill.game.actors.devices.DeviceActor;
import com.icefill.game.sprites.NonObjSprites;

public class AreaCell extends BasicActor{
	//public float elapsed_time;
	public int offset_modifier_x;
	public boolean area_animation_started=false;
	public boolean is_in_range=false;
	public boolean is_target=false;
	public boolean is_blocked=false;
	public boolean transparent=false;
	
	public int wall_direction;
	public Floor floor;
	public int floor_index;
	public int wall_index;
	public int area_count;
	public AreaCell parent;
	public DeviceActor device;
	public ParticleEffect effect;
	private NonObjSprites glow;
	public AreaCell(int xx,int yy,Floor  floor,RoomGroup room) {
		area_count=0;
		this.floor=floor;
		if (floor.isWall())
		{
			is_blocked=true;
			room.addActor(this);
		}
		this.xx=xx;
		this.yy=yy;
		elapsed_time=0f;
		if (floor.particle_name!=null) {
			effect= new ParticleEffect();
			effect.load(Gdx.files.internal(floor.particle_name),Gdx.files.internal("particles/"));
			effect.start();
			glow = (NonObjSprites)Assets.non_obj_sprites_map.get("glow");
		}
	}

	public void setDirection(int direction) {
		super.setDirection(direction);
		wall_direction=direction;
		if (floor.wall_animation !=null && getDirection()<2)
		{
				transparent=true;
		}
	}
	public void setUsualWall(){
		wall_index=0;
	}
	public void activate()
	{
		area_animation_started=true;
	}
	public void clearAreaCount() {
		area_count=0;
	}

	public void setWallIndex(){
		wall_index=floor.getRndWallIndex();
	}
	
	public void setFloorIndex(){
		floor_index=floor.getRndFloorIndex();
	}
	
	public void draw(Batch batch, float delta) {
		super.draw(batch, delta);
		
		if (floor.wall_animation!=null)
			drawWall(batch,delta);
	}
	public void setBossDoor()
	{
		wall_index=1;
	}
	public void drawWall(Batch batch, float delta) {
		if (floor.wall_animation!=null && is_blocked)
		{
			if (transparent)
			{
				batch.setColor(1f,1f,1f,.6f);
			}
			batch.draw((floor.wall_animation.get(wall_index)[getDirection()]).getKeyFrame(elapsed_time,true), getX()-35,getY()+getZ()-15);
			batch.setColor(1f,1f,1f,1f);
		}
	}
	
	public void drawWallTransparently(Batch batch, float delta) {
		if (floor.wall_animation!=null && is_blocked)
		{
			batch.setColor(1f,1f,1f,.7f);
			batch.draw((floor.wall_animation.get(wall_index)[wall_direction]).getKeyFrame(elapsed_time,true), getX()-35,getY()+getZ()-15);
			batch.setColor(1f,1f,1f,1f);
		}
	}
	
	public void clear() {
		elapsed_time=0f;
		area_animation_started=false;
		parent=null;
		is_in_range=false;
		is_target=false;
		area_count=0;
	}
	public void clearTarget() {
		is_target=false;
		clearAreaCount();
	}
	public boolean isAnimationStarted(){
		return area_animation_started;
	}
	/*
	public void start() {    
		effect.reset();
		effect.start();

		}
	public void end(){
		effect.reset();
	}
	*/
	public void drawEffect(Batch batch, float delta){
				batch.setBlendFunction(GL20.GL_DST_COLOR,GL20.GL_SRC_ALPHA);
				//batch.setColor(0.2f,0.2f,0.2f,1f);
				effect.draw(batch);
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	public void drawGlow(Batch batch,float delta) {
		if (glow!=null)
		glow.drawAnimation(batch, elapsed_time, 1, 0, getX()+offset_modifier_x*floor.particle_offset_x
				, getY()+floor.particle_offset_y+getZ()//+temp_light.zz
				,0,1f,1f);
		
	}
	public void setX(float x) {
		super.setX(x);
		if (getDirection()==DL ||getDirection()==UL)
			offset_modifier_x=1;	
		else
			offset_modifier_x=-1;
		
		if (effect!=null)		
		effect.setPosition(getX()+offset_modifier_x*floor.particle_offset_x,getY()+floor.particle_offset_y+getZ());
	}
	public void setY(float y) {
		super.setY(y);
		if (effect!=null)
		effect.setPosition(getX()+offset_modifier_x*floor.particle_offset_x,getY()+floor.particle_offset_y+getZ());
	}
	
	public void act(float delta){
		super.act(delta);
		if (pause== false)
			elapsed_time += Gdx.graphics.getDeltaTime();
	
		if (effect!=null) {
		//effect.setPosition(getX()+offset_modifier_x*floor.particle_offset_x,getY()+floor.particle_offset_y+getZ());
		effect.update(delta);
		}
		if (device!= null) 
			device.act(delta);
	}
	
	public void execute(DungeonGroup dungeon) {
		if (device!=null)
		{
			device.activateDevice(dungeon,this);
		}
	}
	
	
	public boolean isBlocked() {
		return is_blocked;
	}
	public boolean isDangerous()
	{
		if (device==null ) return false;
		else if (device.isHarzardeous()) return true;
		else return false;
	}
	public String toString() {
		return "("+xx+","+yy+")";
	}
	

}






