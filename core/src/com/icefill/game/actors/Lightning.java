package com.icefill.game.actors;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.icefill.game.Assets;
import com.icefill.game.Randomizer;

public class Lightning extends Actor{
	Vector2 start_point;
	Vector2 end_point;
	Vector2 tangent;
	float thickness;
	float length;
	float alpha=1;
	boolean fade_out;
	ArrayList<LineSegment> segments;
	FloatAction float_action;
	
	public Lightning(Vector2 start_point,Vector2 end_point, float thickness) {
		this.start_point=start_point;
		this.end_point=end_point;
		this.thickness=thickness;
		this.tangent= new Vector2(end_point).sub(start_point);
		this.length=tangent.len();
		CreateLightning();
	}
	public void CreateLightning()
	{
		segments= new ArrayList<LineSegment>();
		Vector2 normal= new Vector2(tangent.y,-tangent.x).nor();
		int n=(int)(length/12);
		ArrayList<Float> pos= new ArrayList<Float>();
		pos.add(0f);
		for (int i=0;i<n;i++)
		{
			pos.add(Randomizer.nextFloat());
		}
		n++;
		Collections.sort(pos);
		float sway=200;
		float jagg=1/sway;
		float prev_dis=0;
		Vector2 pt=null;
		Vector2 prev_pt= start_point;
		for (int i=1;i<n;i++)
		{
			float scale = (length*jagg)*(pos.get(i)-pos.get(i-1));
			float envelope=pos.get(i)>.95f ? 20*(1-pos.get(i)):1;
			float dis= Randomizer.nextFloat()*2*sway-sway;
			dis-=(dis-prev_dis)*(1-scale);
			dis*=envelope;
			pt= new Vector2(start_point).add(new Vector2(tangent).scl(pos.get(i))).add(new Vector2(normal).scl(dis));
			segments.add(new LineSegment(prev_pt,pt,thickness));
			prev_pt=pt;
			prev_dis=dis;
		}
		segments.add(new LineSegment(pt,end_point,thickness));
		//FloatAction float_action= new FloatAction(1,0);
		//this.addAction(float_action);
		
	}
	public void draw(Batch batch, float delta) {
		for(LineSegment seg:segments)
		{
			seg.draw(batch, delta,.6f,.6f,1f,alpha);
			
		}
	}
	public void fadeoutLightning()
	{
		fade_out=true;
	}
	public void act(float delta){
		if (fade_out)
		{
			alpha-=0.03f;
			if (alpha<=0) {alpha=0; this.remove();return;}
		}
	}
	
	public void changeLineSizeAction(float size,float time) {
	}
	// start point end point, thickness
	// draw
}
