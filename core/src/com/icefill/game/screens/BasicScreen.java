package com.icefill.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.icefill.game.Global;
import com.icefill.game.SigmaFiniteDungeon;

public class BasicScreen
  implements Screen
{
  public final Stage ui_stage;
  public final Stage stage;
  protected final SigmaFiniteDungeon game;
  protected float window_width;
  protected float window_height;
  protected float zoom = 1.0F;
  public boolean clicked=false;
  final protected float SCREEN_WIDTH=640*.9f,SCREEN_HEIGHT=450*.9f;
  final protected float UI_SCREEN_WIDTH=640*.75f,UI_SCREEN_HEIGHT=450*.75f;

    public boolean dragged=false;
  private BitmapFont font;
  private SpriteBatch batch;
  public Camera camera;
  public Camera ui_camera;
  private Skin skin;
  private Viewport viewport;
  private Viewport ui_viewport;
  InputMultiplexer inputMultiplexer;
  

  public BasicScreen(SigmaFiniteDungeon game)
  {
    this.game = game;

    this.stage = new Stage();
    
    this.ui_stage = new Stage();

    this.camera = new OrthographicCamera();
    this.ui_camera = new OrthographicCamera();
    this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, this.camera);
    this.ui_viewport = new StretchViewport(UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT, this.ui_camera);
    this.stage.setViewport(this.viewport);
    this.ui_stage.setViewport(this.ui_viewport);
    this.camera = this.stage.getCamera();
    this.ui_camera = this.ui_stage.getCamera();

    this.viewport.apply();
    this.ui_viewport.apply();

    Gdx.app.log(SigmaFiniteDungeon.LOG, "!!!" + ((OrthographicCamera)this.stage.getCamera()).zoom);

    this.inputMultiplexer = new InputMultiplexer();
    MapInputProcessor map_input_processor= new MapInputProcessor();
    this.inputMultiplexer.addProcessor(this.ui_stage);

    inputMultiplexer.addProcessor(map_input_processor);

    this.inputMultiplexer.addProcessor(this.stage);
    

    
        //this.zoom -= 0.2F;
    ((OrthographicCamera)this.camera).zoom = this.zoom;
    this.camera.update();
	Global.current_screen=this;

  }

  protected String getName()
  {
    return getClass().getSimpleName();
  }

  public BitmapFont getFont() {
    if (this.font == null)
    {
      this.font = new BitmapFont();
    }
    return this.font;
  }

  public SpriteBatch getBatch() {
    if (this.batch == null)
      this.batch = new SpriteBatch();
    return this.batch;
  }

  public void show()
  {
    Gdx.input.setInputProcessor(this.inputMultiplexer);
  }

  public void render(float delta)
  {
    if ((Gdx.input.isKeyPressed(8)) && (this.zoom > 0.5D))
    {
      this.zoom -= 0.01F;
      ((OrthographicCamera)this.camera).zoom = this.zoom;
      this.camera.update();
    }
    else if ((Gdx.input.isKeyPressed(9)) && (this.zoom < 1.5D))
    {
      this.zoom += 0.01F;
      ((OrthographicCamera)this.camera).zoom = this.zoom;
      this.camera.update();
    }
    this.stage.act(delta);
    this.ui_stage.act(delta);

    Gdx.gl.glClearColor(0.15F, 0.15F, 0.15F, 1.0F);
    Gdx.gl.glClear(16384);
    this.stage.draw();
    this.ui_stage.draw();
  }

  public void resize(int width, int height)
  {
    this.stage.getViewport().update(width, height, true);
    this.ui_stage.getViewport().update(width, height, true);
    this.window_height = height;
    this.window_width = width;

  }

  public void pause()
  {
  }

  public void resume()
  {
  }

  public void hide()
  
  {
    dispose();
  }

  public void dispose()
  {
  }
  public Stage getStage(){return stage;}
  public float getScreenX()
  {
    Vector3 worldPt = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0F);
    this.camera.unproject(worldPt);
    
    return worldPt.x;
  }
  public float getScreenY() {
    Vector3 worldPt = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0F);
    this.camera.unproject(worldPt);
    return worldPt.y;
  }
  public float stageToUICoordX(float stage_x, float stage_y) {
    Vector3 worldPt = new Vector3(stage_x, stage_y, 0.0F);
    this.camera.project(worldPt);
    this.ui_camera.unproject(worldPt);
    return worldPt.x;
  }
  public float stageToUICoordY(float stage_x, float stage_y) {
    Vector3 worldPt = new Vector3(stage_x, stage_y, 0.0F);
    this.camera.project(worldPt);
    worldPt.y = (Gdx.graphics.getHeight() - worldPt.y);
    this.ui_camera.unproject(worldPt);
    return worldPt.y;
  }
  
  public class MapInputProcessor implements InputProcessor {
	    Vector3 last_touch_down = new Vector3();
	    Vector3 last_camera = new Vector3();
	    
	    @Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			dragged=false;
	        last_touch_down.set( screenX, screenY, 0);
	        last_camera.set(camera.position);
	        
			// TODO Auto-generated method stub
			return false;
		}
	    

	    public boolean touchDragged(int x, int y, int pointer) {
	      //  dragged=moveCamera( x, y );
	        return true;
	    }
        /*
	    private boolean moveCamera( int touch_x, int touch_y ) {
	        Vector3 delta_position = getdeltaPosition( touch_x, touch_y );
	        float length=delta_position.len2();
	        stage.getCamera().position.set(delta_position.add(last_camera) );
	        if (length>30) return true;
	        else return false;
	    }
        */
	    private Vector3 getdeltaPosition( int x, int y ) {
	    	/*
	        Vector3 new_position = new Vector3(last_touch_down);
	        new_position.sub(x, y, 0);
	    	new_position.y = -new_position.y;
	    	*/
	    	Vector3 new_position= new Vector3(x,y,0);
	    	Vector3 last_touch_down2=new Vector3(last_touch_down);
	    	camera.unproject(new_position);
	    	camera.unproject(last_touch_down2);
	    	last_touch_down2.sub(new_position);
	    	//new_position.sub(last_touch_down2);
	        return last_touch_down2;
	    }
/*
	    private boolean cameraOutOfLimit( Vector3 position ) {
	        int x_left_limit = WINDOW_WIDHT / 2;
	        int x_right_limit = terrain.getWidth() - WINDOW_WIDTH / 2;
	        int y_bottom_limit = WINDOW_HEIGHT / 2;
	        int y_top_limit = terrain.getHeight() - WINDOW_HEIGHT / 2;

	        if( position.x < x_left_limit || position.x > x_right_limit )
	            return true;
	        else if( position.y < y_bottom_limit || position.y > y_top_limit )
	            return true;
	        else
	          return false;
	}
*/
		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			clicked=true;
			
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
  }
  public boolean isDragged() {
	  return dragged;
  }

}