package com.icefill.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.icefill.game.Assets;
import com.icefill.game.Global;
import com.icefill.game.sprites.NonObjSprites;

public class MinimapActor extends Actor {
	TextureRegion room_texture[];
	//TextureRegion path_dl_ur_texture;
	//TextureRegion path_dr_ul_texture;
	TextureRegion path[];
	TextButton button;
	Label minimap_info;
	DungeonGroup dungeon;
	RoomGroup rooms[][][];
	int tile_width;
	int tile_height;
	float dx;
	float dy;
	
	//NonObjSprites mana_sprites;
    
	
	
	
	public MinimapActor (DungeonGroup dungeon) {
		//mana_sprites= (NonObjSprites)Assets.non_obj_sprites_map.get("mana_stone");
		button= new TextButton("CLOSE",Assets.getSkin());
		button.addListener(
				new InputListener() {
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
						Global.getHUD().hideMap();
						return false;
					}
				}
		);
		minimap_info = new Label("", new Label.LabelStyle(Assets.getFont(), Color.BLACK) );
		this.dungeon=dungeon;
		this.rooms=dungeon.rooms;
		tile_width=16;
		tile_height=16;
		dx=0.25f*tile_width;
		dy=0.25f*tile_width;
		room_texture= new TextureRegion[8];
		path= new TextureRegion[4];
		
		TextureAtlas atlas= Assets.getAsset("sprite/minimap.atlas", TextureAtlas.class);
		room_texture[0]=atlas.findRegion("empty");
		room_texture[1]=atlas.findRegion("boss");
		room_texture[2]=atlas.findRegion("chest");
		room_texture[3]=atlas.findRegion("down_stair");
		room_texture[4]=atlas.findRegion("enemy");
		room_texture[5]=atlas.findRegion("wing");
		room_texture[6]=atlas.findRegion("unknown");
		room_texture[7]=atlas.findRegion("current");
		path[0]=atlas.findRegion("dl0000");
		path[1]=atlas.findRegion("dr0000");
		path[2]=atlas.findRegion("ur0000");
		path[3]=atlas.findRegion("ul0000");
		
		//path_dl_ur_texture=atlas.findRegion("door_dl_ur");
		//path_dr_ul_texture=atlas.findRegion("door_dr_ul");
		
		setHeight((float)(tile_height*(dungeon.dungeon_size[1]+dungeon.dungeon_size[0]))*0.5f);
		setWidth((float)(tile_width*(dungeon.dungeon_size[1]+dungeon.dungeon_size[0]))*0.5f);
	}
	public TextButton getCloseButton(){
		return button;
	}
	public void draw(Batch batch, float delta) {
		super.draw(batch, delta);
		for (int yyy=0;yyy<dungeon.dungeon_size[1];yyy++){
			for (int xxx=0;xxx<dungeon.dungeon_size[0];xxx++){
				float x= mapToScreenCoordX(xxx,yyy)-(float)(0.5*tile_width);
				float y=mapToScreenCoordY(xxx,yyy)-(float)(0.5*tile_height);
				if ((rooms[xxx][yyy][dungeon.room_zzz]).isVisited()) 
				{
					
					if (rooms[xxx][yyy][dungeon.room_zzz].equals(Global.getCurrentRoom()))
					{
						batch.setColor(Color.RED);
						//batch.draw(room_texture[7],x,y);
					}
					for (int i=0;i<4;i++)
					{
						if (rooms[xxx][yyy][dungeon.room_zzz].has_door[i])
							batch.draw(path[i],x,y);
					}
					batch.draw(room_texture[rooms[xxx][yyy][dungeon.room_zzz].room_type],x,y);
					
				}
				
				else
				{
					batch.draw(room_texture[6], x,y);
				}
				
				batch.setColor(Color.WHITE);
			}	
		}
		Assets.getFont().draw(batch, "X:"+dungeon.room_xxx
				+" Y:"+dungeon.room_yyy+"  Z:"+dungeon.room_zzz
				, getX(), getY());
		//for (int i=0;i<Global.getMana();i++)
		//	mana_sprites.drawAnimation(batch, delta, 0, 0, 20+getX()+i*10, getY()-30);
	}
	public void act(float delta) {
		super.act(delta);
		setBounds(getX(),getY(),getWidth(),getHeight());
	}
	public float mapToScreenCoordX(int xxx,int yyy) {
		return (float) (getX()+getWidth()*0.5+0.5*tile_width*(xxx-yyy));
	}
	public float mapToScreenCoordY(int xxx,int yyy) {
		return (float) (getY()+getHeight()-0.5*tile_height*(xxx+yyy));
	}
	
	
}
