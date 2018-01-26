package com.icefill.game;



import java.util.HashMap;




import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.icefill.game.actors.dungeon.Floor;
import com.icefill.game.actors.actionActors.*;
import com.icefill.game.actors.BasicActor;
import com.icefill.game.sprites.BasicSprites;
import com.icefill.game.sprites.NonObjSprites;
import com.icefill.game.sprites.ObjSprites;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Assets {
    private static AssetManager manager = new AssetManager();
    private static Skin skin;
    private static ImageButtonStyle image_button_style;
    private static BitmapFont font;
    private static BitmapFont rune_font;
    private static NinePatchDrawable background;
	private static Label.LabelStyle style; 
	private static Label.LabelStyle style_back; 
    public static HashMap<String, Floor> floor_map = new HashMap<String,Floor>();
    public static HashMap<String, BasicSprites> obj_sprites_map =new HashMap<String,BasicSprites>();
    public static HashMap<String, BasicSprites> non_obj_sprites_map =new HashMap<String,BasicSprites>();
	public static HashMap<String, Job> jobs_map= new HashMap<String, Job>();
	public static HashMap<String, BasicActor> effects_map= new HashMap<String, BasicActor>();
	public static HashMap<String, ActionActor> actions_map = new HashMap<String, ActionActor>();
	
	
	
    public static void queueLoading() {
    	
        manager.load("ui/uiskin.atlas", TextureAtlas.class);
        manager.load("sprite/icon.atlas",TextureAtlas.class);
        manager.load("sprite/area_indicator.atlas",TextureAtlas.class);
        manager.load("sprite/char_ui.atlas",TextureAtlas.class);
        manager.load("sprite/lightning.atlas",TextureAtlas.class);
        manager.load("sprite/chain.atlas",TextureAtlas.class);
        manager.load("sprite/trap1.atlas",TextureAtlas.class);
        manager.load("sprite/background.png",Texture.class);
		manager.load("sprite/background2.png",Texture.class);

		manager.load("sprite/message_background.png",Texture.class);
        manager.load("sprite/shadow.png",Texture.class);
        
        //Load Stuff data
        //Load Stuff data
        //manager.load("sprite/floors.atlas",TextureAtlas.class);
        manager.load("sprite/minimap.atlas",TextureAtlas.class);
        
        // Load sound
        manager.load("sound/cast_magic.wav",Sound.class);
        manager.load("sound/item.wav",Sound.class);
        manager.load("sound/door.wav",Sound.class);
        manager.load("sound/hit.wav",Sound.class);
        manager.load("sound/hit2.wav",Sound.class); 
        manager.load("sound/error.wav",Sound.class);
        manager.load("sound/select.wav",Sound.class);
        manager.load("sound/wait.wav",Sound.class);
        manager.load("sound/jump.wav",Sound.class);
        manager.load("sound/guard.wav",Sound.class); 
        manager.load("sound/fireball.wav",Sound.class);
        manager.load("sound/sound_explosion.wav",Sound.class);
        manager.load("sound/footstep.wav",Sound.class);
        manager.load("sound/FlameMagic.ogg",Sound.class);
        manager.load("sound/decay.ogg",Sound.class);
        manager.load("sound/slash.wav",Sound.class);
        manager.load("sound/shoot.ogg",Sound.class);
        manager.load("sound/error.wav",Sound.class); 
        manager.load("sound/miss.wav",Sound.class);
        manager.load("sound/levelup.wav",Sound.class);
        manager.load("sound/healing_sound.wav",Sound.class);
        manager.load("sound/lightning.wav",Sound.class);
        manager.load("music/wouldnt-it-be.mp3",Music.class);
        manager.finishLoading();
        
        Json floor_json = new Json();
    	ArrayList<String> non_obj_sprites_name_list = floor_json.fromJson(ArrayList.class,
                Gdx.files.internal("sprite/non_obj_sprites_list.json"));
    	
    	for (String non_obj_sprites_file_name:non_obj_sprites_name_list) {
    		String atlas_path="sprite/"+non_obj_sprites_file_name+".atlas";
    		manager.load(atlas_path,TextureAtlas.class);
    	}
    	ArrayList<String> obj_sprites_name_list = floor_json.fromJson(ArrayList.class,
                Gdx.files.internal("sprite/obj_sprites_list.json"));
    	for (String obj_sprites_file_name:obj_sprites_name_list) {
    		String atlas_path="sprite/"+obj_sprites_file_name+".atlas";
    		manager.load(atlas_path,TextureAtlas.class);
    	}
    	
    	manager.load("sprite/team_indicator.png",Texture.class);
    	manager.load("sprite/ability_indicator.png",Texture.class);
    	
        manager.finishLoading();

        LoadFloorMap();
        //LoadWallMap();
        LoadObjSprites(obj_sprites_name_list);
        LoadNonObjSprites(non_obj_sprites_name_list);
        LoadActionsMap();
		LoadJobsMap();
        LoadParticles();
    }
    public static Label.LabelStyle getLabelStyle()
	{
		if (style==null)
			style= new Label.LabelStyle(Assets.getFont(),Color.WHITE); 
		return style;
	}
    public static Label.LabelStyle getLabelStyleBackgrounded()
	{
		if (style_back==null)
		{
			style_back= new Label.LabelStyle(Assets.getFont(),Color.WHITE); 
			style_back.background=Assets.getBackground();
		}
		return style_back;
	}
    
    // getSkin
    public static Skin getSkin()
	{
		if (skin==null){
			skin= new Skin(Gdx.files.internal("ui/uiskin.json"),manager.get("ui/uiskin.atlas",TextureAtlas.class));	
		}
		return skin;
	}
    public static ImageButtonStyle getButtonStyle()
    {
    	
    	if (image_button_style== null)
    	{
    		Drawable drawable= new TextureRegionDrawable(new TextureRegion(getAsset(("sprite/background.png"), Texture.class)));
    		image_button_style= new ImageButtonStyle(drawable,drawable,drawable,drawable,drawable,drawable);
    	}
    	return image_button_style;
    }
    public static BitmapFont getFont()
    {
    	/*
    	if (font==null){
    		font= new BitmapFont(Gdx.files.internal("ui/silkscreen.fnt"));
    		font.getData().setScale(0.3f);
    	}
    	*/
    	return getSkin().getFont("default-font");
    }
    public static BitmapFont getRuneFont()
    {
    	
    	if (rune_font==null){
    		rune_font= new BitmapFont(Gdx.files.internal("ui/rune.fnt"));
       	}
    	
    	return rune_font;
    }
    
    public static BitmapFont getBigFont()
    {
    	
    	if (font==null){
    		font= new BitmapFont(Gdx.files.internal("ui/magic_school.fnt"));
    	}
    	
    	return font;
    }
    
    public static NinePatchDrawable getBackground() {
    	if (background==null) {
    		//NinePatch background_patch = new NinePatch(Assets.getAsset(("sprite/background.png"), Texture.class), 5, 5, 4, 4);
    		background= new NinePatchDrawable(new NinePatch(Assets.getAsset(("sprite/background2.png"), Texture.class), 15, 15, 15, 15));
    	}
    	return background;
    }
    //getAssets from manager
    public static <T> T getAsset(String filename,Class<T> type){
    	return manager.get(filename,type);
    }
    
    public static float getProgress(){
    	return manager.getProgress();
    }

    // This function gets called every render() and the AssetManager pauses the loading each frame
    // so we can still run menus and loading screens smoothly
    public static boolean update() {
        return manager.update();
    }
    
    public class NameTypeTuple {
    	String name;
    	String type;
    	public NameTypeTuple ( String name, String type) {
    		this.name=name;
    		this.type=type;
    	}
    }
    
    private static void LoadNonObjSprites(ArrayList<String> non_obj_sprites_name_list) {
    	Json floor_json = new Json();
    	NonObjSprites.Factory temp_factory;
    	for (String non_obj_sprites_file_name:non_obj_sprites_name_list) {
    		//read atlas
    		String atlas_path="sprite/"+non_obj_sprites_file_name+".atlas";
    		// read json

    		temp_factory= floor_json.fromJson(NonObjSprites.Factory.class
    				,Gdx.files.internal("sprite/"+ non_obj_sprites_file_name+".json"));
    		non_obj_sprites_map.put(non_obj_sprites_file_name,new NonObjSprites(temp_factory,atlas_path));
    	}	
    }
    private static void LoadObjSprites(ArrayList<String> obj_sprites_name_list) {
    	Json floor_json = new Json();
    	ObjSprites.Factory temp_factory;
    	for (String obj_sprites_file_name:obj_sprites_name_list) {
    		String atlas_path="sprite/"+obj_sprites_file_name+".atlas";
    		temp_factory= floor_json.fromJson(ObjSprites.Factory.class
    				,Gdx.files.internal("sprite/"+ obj_sprites_file_name+".json"));
    		obj_sprites_map.put(obj_sprites_file_name,new ObjSprites(temp_factory,atlas_path));
    	}	
    }
    
    private static void LoadFloorMap() {
    	Json floor_json = new Json();
    	ArrayList<String> floor_name_list = floor_json.fromJson(ArrayList.class,
                Gdx.files.internal("floors_data/floor_lists.json"));
    	//ArrayList<Floor.FloorFactory> floor_factory_list;
    	Floor.Factory temp_factory;
    	for (String floor_file_name:floor_name_list) {
    		temp_factory= floor_json.fromJson(Floor.Factory.class
    				,Gdx.files.internal("floors_data/"+ floor_file_name));
    		floor_map.put(temp_factory.name,new Floor(temp_factory));
    	}
    }

    private static void LoadJobsMap(){
    	Json job_json= new Json();
    	ArrayList<String> job_name_list= job_json.fromJson(ArrayList.class,
    			Gdx.files.internal("objs_data/job/job_list.json"));
    	Job.Factory temp_factory;
    	for (String job_file_name:job_name_list) {
    		temp_factory= job_json.fromJson(Job.Factory.class
    				,Gdx.files.internal("objs_data/job/"+ job_file_name));
    		jobs_map.put(temp_factory.job_name,new Job(temp_factory));
    	}
    }
    private static void LoadActionsMap(){
		ObjActions.initializeSubActionMap();

    	Json action_json= new Json();
    	ArrayList<String> action_name_list= action_json.fromJson(ArrayList.class,
    			Gdx.files.internal("objs_data/action/action_list.json"));
    	AbilityActor.Seed temp_seed;
    	for (String action_file_name:action_name_list) {
    		temp_seed = action_json.fromJson(AbilityActor.Seed.class
    				,Gdx.files.internal("objs_data/action/"+ action_file_name));
    		AbilityActor temp_ability=new AbilityActor(temp_seed);
    		actions_map.put(temp_seed.action_name,temp_ability);
    	}
    	actions_map.put("OpenInventory", new OpenInventoryAction());
		actions_map.put("OpenMap", new OpenMapAction());
		actions_map.put("Wait",new WaitAction());
    	actions_map.put("SetDirection",new SetDirectionAction());	
    	actions_map.put("AddAbility", new ObjInfoAbilityActor());
    }
    private static void LoadParticles(){
    	Json particle_json= new Json();
    	ArrayList<String> particles_name_list=particle_json.fromJson(ArrayList.class,
    			Gdx.files.internal("particles/particle_list.json"));
    	for (String particle_file_name:particles_name_list)
    	manager.load("particles/"+particle_file_name, ParticleEffect.class);
    }
}