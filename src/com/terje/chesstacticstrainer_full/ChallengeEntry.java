package com.terje.chesstacticstrainer_full;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.LevelDescriptor;
import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class ChallengeEntry extends BaseScene {

	private int SW,SH,RW;
	@Override
	public void createScene() {
		SW = ActivityGame.CAMERA_WIDTH;
		SH = ActivityGame.CAMERA_HEIGHT;
		RW = 45;
		Sprite[] h_face = new Sprite[6];
		LevelDescriptor ld = resourceManager.getCurrentSelectedLevel();

		float W=32,H=64;

		float xs[] = {SW/4,SW/4,SW/4,SW/4+W,SW/4+W,SW/4+2*W};
		float ys[] = {SH/2,SH/2-H,SH/2-H*2,SH/2-H,SH/2-H*2,SH/2-H*2};
		for(int i=0;i<h_face.length;i++) {
			h_face[i] = new Sprite(xs[i],ys[i],resourceManager.empty_horseHead_region2,vbom);
			h_face[i].setScale(.5f);
		}
		Sprite bgSprite = new Sprite(SW/2,SH/2, resourceManager.tactics_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, bgSprite));
		setBackground(background);
		Text txt = new Text(SW/2,SH-SH/8, resourceManager.font, ld.isRated?"RATED":"LEVEL "+ld.getLevel(), vbom);
		txt.setColor(Color.BLUE);
		attachChild(txt);
		if (ld.getLevelDescription().length()>0) {
			txt = new Text(SW/2,SH-SH/8-RW, resourceManager.mediumFont, "\""+ld.getLevelDescription()+"\"", vbom);
			txt.setColor(Color.YELLOW);
			attachChild(txt);
		}
		String f = ld.getNoOfFailsAllowed()<LevelDescriptor.INFINITY?Integer.toString(ld.getNoOfFailsAllowed()):"\u221E";
		txt = new Text(SW/2,SH-SH/8-RW*2, resourceManager.mediumFont, "Faults allowed: "+f, vbom);
		txt.setColor(Color.BLUE);

		attachChild(txt);
		txt = new Text(SW/2,SH-SH/8-RW*3, resourceManager.mediumFont, "Problems: "+ld.getNoOfProblems(), vbom);
		txt.setColor(Color.BLUE);

		attachChild(txt);
		txt = new Text(SW/2,SH-SH/8-RW*4, resourceManager.mediumFont, "Your best: "+resourceManager.getPlayerScore(ld.getLevel()), vbom);
		txt.setColor(Color.BLUE);

		attachChild(txt);
		txt = new Text(SW/2,SH/2+RW, resourceManager.mediumWhiteFont, "Target(s): ", vbom);
		//txt.setColor(Color.BLACK);
		attachChild(txt);

		if (!ld.isRated) {
			txt = new Text(SW/2+100,SH/2, resourceManager.smallWhiteFont, ld.getPoorS()+" points", vbom);
			//txt.setColor(Color.BLACK);
			attachChild(txt);
			txt = new Text(SW/2+100,SH/2-H, resourceManager.smallWhiteFont, ld.getGoodS()+" points", vbom);
			//txt.setColor(Color.BLACK);
			attachChild(txt);
			txt = new Text(SW/2+100,SH/2-H*2, resourceManager.smallWhiteFont, ld.getExcellentS()+" points", vbom);
			//txt.setColor(Color.BLACK);
			attachChild(txt);
			for(int i=0;i<h_face.length;i++) 
				attachChild(h_face[i]);
		} else {
			txt = new Text(SW/2,SH/2, resourceManager.smallWhiteFont, "\nIncrease your rating.\n\nClimb the rating ladder.", vbom);
			attachChild(txt);
		}

		ButtonSprite okButton = new  ButtonSprite(SW/2, SH/8, resourceManager.ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if(pTouchEvent.isActionUp()) {
					unregisterTouchArea(this);
					//Save the current rating.
					resourceManager.setOldRating();
					SceneManager.getInstance().createScene(SceneType.SCENE_TACTICS);
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		okButton.registerEntityModifier(FirstTimeScene.zoom(1f));
		registerTouchArea(okButton);
		attachChild(okButton);


	}

	@Override
	public void onBackKeyPressed() {
				SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_CHALLENGE_ENTRY;
	}

	@Override
	public void disposeScene() {
		
		resourceManager.unloadTacticsEntryResources();
		engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
            	detachChildren();
        		detachSelf();
        	}
		});
	}
}
