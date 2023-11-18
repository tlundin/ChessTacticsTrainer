package com.terje.chesstacticstrainer_full;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class SplashScene extends BaseScene {

	private Sprite splash;
	
	@Override
	public void createScene() {
		splash = new Sprite(0, 0, resourceManager.splash_region, vbom)
		{
		    @Override
		    protected void preDraw(GLState pGLState, Camera pCamera) 
		    {
		       super.preDraw(pGLState, pCamera);
		       pGLState.enableDither();
		    }
		};
		        
		splash.setScale(1.5f);
		splash.setPosition(resourceManager.splash_region.getWidth()-resourceManager.splash_region.getWidth()/4,ActivityGame.CAMERA_HEIGHT/2);
	   // setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		setBackground(new Background(0.22f,0.32f,0.435f));
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		ResourceManager.getInstance().unloadSplashScreen();
		splash.detachSelf();
		    splash.dispose();
		    this.detachSelf();
		    this.dispose();
	}

}
