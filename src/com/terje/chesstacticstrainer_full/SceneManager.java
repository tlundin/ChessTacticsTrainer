package com.terje.chesstacticstrainer_full;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import android.util.Log;

public class SceneManager
{
	//---------------------------------------------
	// SCENES
	//---------------------------------------------

	
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private static SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private static BaseScene currentScene;

	private static Engine engine = ResourceManager.getInstance().engine;

	private static LoadingScene loadingScene;


	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING, 
		SCENE_FIRST_TIME,
		SCENE_TACTICS, 
		SCENE_CHALLENGE_ENTRY,
		SCENE_CHALLENGE_EXIT,
		SCENE_STATS,
		SCENE_ENDING
	}


	//Load and unload splash scene in the beginning
/*
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourceManager.getInstance().loadSplashScreenResources();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

*/

	private SceneManager() {
		loadingScene = new LoadingScene();
	}

	public void createScene(SceneType sceneType, OnCreateSceneCallback pOnCreateSceneCallback) {
		setScene(sceneType);
		pOnCreateSceneCallback.onCreateSceneFinished(currentScene);
		
	}
	
	public void createScene(final SceneType sceneType) {
		if (currentScene!=null) {
			Log.d("schack","Current SCENETYPE before dispose: "+currentScene.getSceneType());					
			currentScene.disposeScene();
		}
		engine.setScene(loadingScene);		
		engine.registerUpdateHandler(new TimerHandler(.1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				setScene(sceneType);
			}
		}));
	}


	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public void setScene(BaseScene scene)
	{
		currentScene = scene;
		currentSceneType = scene.getSceneType();
		engine.setScene(scene);
	}

	
	private void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
		case SCENE_MENU:
			ResourceManager.getInstance().loadMenuResources();
			setScene(new MainMenuScene());
			break;
		case SCENE_GAME:
			if (ResourceManager.getInstance().noMoreLevels())
				setScene(SceneType.SCENE_ENDING);
			else {
				ResourceManager.getInstance().loadGameResources();
				setScene(new GameScene());
			}
			break;
		case SCENE_SPLASH:
			ResourceManager.getInstance().loadSplashScreenResources();
			setScene(new SplashScene());
			break;
		case SCENE_FIRST_TIME:
			ResourceManager.getInstance().loadFirstTimeResources();		
			setScene(new FirstTimeScene());   
			break;
		case SCENE_TACTICS:
			setScene(new ChallengeScene());
			break;		
		case SCENE_CHALLENGE_ENTRY:
			ResourceManager.getInstance().loadTacticsResources();	
			setScene(new ChallengeEntry());
			break;
		case SCENE_CHALLENGE_EXIT:
			ResourceManager.getInstance().loadChallengeExitResources();	
			setScene(new ChallengeExit());
			break;
		case SCENE_STATS:
			ResourceManager.getInstance().loadStatsResources();
			setScene(new StatsScene());
			break;
		case SCENE_ENDING:
			ResourceManager.getInstance().loadEndResources();
			setScene(new EndingScene());
		default:
			break;
		}
	}
	

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static SceneManager getInstance()
	{
		return INSTANCE;
	}

	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}

	public BaseScene getCurrentScene()
	{
		return currentScene;
	}

	
	/*
	public void loadMenuSceneFromFirstTime(final Engine mEngine)
	{
		setScene(loadingScene);
		firstTimeScene.disposeScene();
		ResourceManager.getInstance().unloadFirstSceneTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuTextures();
				SceneManager.getInstance().setScene(menuScene);
			}
		}));
	}
	*/

}