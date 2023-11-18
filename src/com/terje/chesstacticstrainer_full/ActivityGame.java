package com.terje.chesstacticstrainer_full;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;

import android.util.Log;
import android.view.KeyEvent;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class ActivityGame extends LayoutGameActivity {
	
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onResumeGame()
	 */
	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		Log.d("Schack","onresumegame..");
		Music mMusic = ResourceManager.getInstance().mMusic;
		if(mMusic !=null && !mMusic.isPlaying())
			mMusic.play();
		

	}
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onPauseGame()
	 */
	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		Log.d("Schack","onpausegame..");
		Music mMusic = ResourceManager.getInstance().mMusic;
		if(mMusic !=null && !mMusic.isPlaying())
			mMusic.pause();
	}

	private ZoomCamera camera;
	public static final int CAMERA_WIDTH = 500;
	public static final int CAMERA_HEIGHT = 800;

	@Override
	public EngineOptions onCreateEngineOptions() {
		//camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		camera = new ZoomCamera(0f, 0f, (float)CAMERA_WIDTH, (float)CAMERA_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    //camera.setSurfaceSize(0, 0, 10000, 10000);
	    return engineOptions;
	}
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		 ResourceManager.getInstance().prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		 ResourceManager.getInstance().loadStaticResources();
		 pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		 SceneManager.getInstance().createScene(SceneType.SCENE_SPLASH,pOnCreateSceneCallback);		 
	}
	
	@Override
	public void onPopulateScene(Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		
	    		Music mMusic = ResourceManager.getInstance().mMusic;
	    		Log.e("Schack","playing music now..");
	    		if(mMusic !=null && !mMusic.isPlaying())
	    			mMusic.play();
	    		//register or create user.
	    		SceneManager.getInstance().createScene(SceneType.SCENE_MENU);
	    		pOnPopulateSceneCallback.onPopulateSceneFinished();
	    		//ResourceManager.getInstance().setRating(1500f);
	    		//ResourceManager.getInstance().setCleared(11);
		
	    		this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ResourceManager.getInstance().registerUser();
					}
				});

 
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	
	

	@Override
	protected synchronized void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		Music mMusic = ResourceManager.getInstance().mMusic;
		if(mMusic !=null && mMusic.isPlaying())
			mMusic.stop();
		super.onPause();
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	        System.exit(0);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	@Override
	  protected int getLayoutID() {
		/*
		int c = 0;
		c=PreferenceManager.getDefaultSharedPreferences(this).getInt("comcount", 0);
	    if (c>3) 
	    	return R.layout.game_layout;
	    else {
	    	PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("comcount", c+1).commit();
		    return R.layout.game_layout_noad;
	    }
	    */
		return R.layout.game_layout_noad;
	  }
	 
	  @Override
	  protected int getRenderSurfaceViewID() {
	    return R.id.SurfaceViewId;
	  }

}
