package com.terje.chesstacticstrainer_full;

import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.LevelDescriptor;
import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener
{
	protected static final float ZoomFactor = 1f;
	private static Sprite piece;
	private static boolean zoomDone = false;
	private static SurfaceScrollDetector myScrollDetector;



	//scaling 
	private final int BG_ZOOM_LVL_1 = 1;
	private PinchZoomDetector myPinchZoomDetector;
	
	private int[][] ladderStepLocations;

	//flag to block press before zooming done.
	@Override
	public void createScene()
	{
		ladderStepLocations = resourceManager.ladderStepLocations;
		createBackground();

		//Decide if we should show some bg information.
		int levelOffset = resourceManager.firstSceneId-1;
		attachSprites(resourceManager.getHighestLevelCleared()-levelOffset,levelOffset);

	
		createHUD();

		myScrollDetector = new SurfaceScrollDetector(this);
		myScrollDetector.setEnabled(true);

		myPinchZoomDetector = new PinchZoomDetector(this);
		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionDownEnabled(true);

		engine.setTouchController(new MultiTouchController());
		setOnAreaTouchTraversalFrontToBack();

		//		camera.setBoundsEnabled(true);
		//		camera.setBounds(0, 0, resourceManager.game_background_region.getWidth()/2+ActivityGame.CAMERA_WIDTH/2, 
		//				resourceManager.game_background_region.getHeight()/2+ActivityGame.CAMERA_HEIGHT/2);

		
		if (clearedKingLevel(resourceManager.getHighestLevelCleared()-levelOffset)&&!resourceManager.kingHasFallen()) {
			Log.d("schack","Down with the king!!");
			camera.setCenter(king.getX(),king.getY());
			glowBall.setVisible(false);
			piece.setVisible(false);
			king.animate(150);
			engine.registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() 
			{
				public void onTimePassed(final TimerHandler pTimerHandler) 
				{downWithTheKing();}
			}));
		} else {
			camera.setZoomFactor(ZoomFactor);
			camera.setCenter(piece.getX(),piece.getY());
			
			zoomDone = true;
		}
		
		if (resourceManager.isFirstTimeGameEntry()) 
			showPopup("WELCOME BRAVE HERO!\n\nYou have arrived at the\nfirst floating island\nof Kirdavia!\nEach island is controlled\nby an evil king.\nThe purpose of the game\nis to outsmart each\nking in chess. Fight\nyour way to the top of\nthe tower!"
					+ " The faster you\nare, the better score\nyou will get.", new OkCb() {
						@Override
						public void onOkClicked() {
							Log.d("schack","Camera x y before change"+camera.getCenterX()+" "+camera.getCenterY());
							camera.setCenter(piece.getX(),piece.getY());
							camera.setZoomFactor(.75f);

							camera.registerUpdateHandler(new IUpdateHandler() {

								@Override
								public void reset() {
									// TODO Auto-generated method stub

								}

								@Override
								public void onUpdate(float pSecondsElapsed) {
									if (camera.getZoomFactor()==ZoomFactor) {
										zoomDone=true;
										camera.unregisterUpdateHandler(this);
										Log.d("schack","zoom ready. Deregistering updatehandler");
									}

								}
							});
						}
					});
			Log.d("schack","End of create scene");

	}
	final Random rr = new Random();
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		
		super.onManagedUpdate(pSecondsElapsed);
		
		
			if (rr.nextInt(250)==33 && !resourceManager.kingHasFallen())
			{
				

				if (rr.nextBoolean())
					resourceManager.grr.play();
				else
					resourceManager.grr2.play();
			}
			
	}
	
	
	
	

	private void downWithTheKing() {
		king.clearEntityModifiers();

		king.registerEntityModifier(new MoveYModifier(8f, king.getY(), -1500,new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				resourceManager.falling.play();
				engine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
				{
					public void onTimePassed(final TimerHandler pTimerHandler) 
					{
						if (resourceManager.firstTimeKiller()) 
							showPopup("CONGRATULATIONS!!\n\nYou have ridden the\nfirst floating island\nof its evil king!\nYou can now travel to\nthe next island.\nAs you continue, the\ndifficulty level will\nincrease, but so will\nyour skills!",new OkCb() {

								@Override
								public void onOkClicked() {
									resourceManager.setKingHasFallen();
									engine.runOnUpdateThread(new Runnable() {

										@Override
										public void run() {
											levelUp();
										}

									});
								}});
						else
							levelUp();
					}
				}));
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			}

			public void levelUp() {
				glowBall.detachSelf();
				Log.d("schack","king now fallen so I am drawing the levelup!");
				Sprite levelUp= new Sprite(0,0,resourceManager.levelUp,vbom);
				levelUp.setPosition(resourceManager.game_background_region.getWidth()/2+ladderStepLocations[ladderStepLocations.length-1][0],
						resourceManager.game_background_region.getHeight()/2+ladderStepLocations[ladderStepLocations.length-1][1]+levelUp.getHeight()+75);
				attachChild(levelUp);
				addGroovyFloat(levelUp);
				levelUp.registerEntityModifier(new AlphaModifier(1.0f, 1.0f, 2.0f) );
				attachChild(glowBall);
				glowBall.setVisible(true);
				piece.setVisible(true);
				zoomDone = true;
			}
		}));

	}

	int c = 0;

	private ITextureRegion oneTwoThree(int level) {

		int score = resourceManager.getPlayerScore(level);
		Log.d("schack","Score for level "+level+" is "+score);
		LevelDescriptor ld = LevelDescriptorFactory.createLevelDescriptor(level);

		if (ld.getPoorS()>score)
			return resourceManager.pf;
		if (ld.getGoodS()>score)
			return resourceManager.pf_1;
		if (ld.getExcellentS()>score)
			return resourceManager.pf_2;
		else
			return resourceManager.pf_3;

	}


	private boolean clearedKingLevel(int highestLevelCleared) {
		return (highestLevelCleared == resourceManager.ladderStepLocations.length-2);

	}


	AnimatedSprite king;
	Sprite glowBall;
	private void attachSprites(final int highestLevelCleared,final int levelOffset) {


		final Sprite[] levels = new Sprite[ladderStepLocations.length-2];
		Sprite levelUp;

		piece =  new Sprite(0,0, resourceManager.game_piece_region, vbom);
		piece.setX(getLevelX(0));	
		piece.setY(getLevelY(0)+piece.getHeight()/4);
		if (resourceManager.mirrorPiece)
			piece.setFlippedHorizontal(true);

		if (resourceManager.kingHasFallen()){
			Log.d("schack","king HAS fallen so I am drawing the levelup!");
			levelUp= new Sprite(0,0,resourceManager.levelUp,vbom);
			levelUp.setPosition(resourceManager.game_background_region.getWidth()/2+ladderStepLocations[ladderStepLocations.length-1][0],
					resourceManager.game_background_region.getHeight()/2+ladderStepLocations[ladderStepLocations.length-1][1]);
			attachChild(levelUp);
			levelUp.registerEntityModifier(new AlphaModifier(1.0f, 1.0f, 2.0f) );
			addGroovyFloat(levelUp);
		}
		glowBall = new Sprite(0,0,resourceManager.glowBall,vbom) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if(pSceneTouchEvent.isActionDown()) {
					Log.d("schack","Level offset: "+levelOffset);
					levelPressed(highestLevelCleared+1+levelOffset);
				}
				return true;
			}

		};

		registerTouchArea(glowBall);
		//starts on 2 since first slot in array is the initial horse position.
		c=1;
		while (c<=levels.length) {
			levels[c-1] = new Sprite(0, 0, 
					(c>highestLevelCleared)?resourceManager.pf:oneTwoThree(c+levelOffset), vbom) {
				final int myId = c+levelOffset;
				
				@Override

				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

					if(pSceneTouchEvent.isActionUp()) {
						Log.d("schack","Pressed "+myId);
						Log.d("schack","Level offset: "+levelOffset);
						levelPressed(myId);
					}
					return true;
				}

			};


			levels[c-1].setPosition(resourceManager.game_background_region.getWidth()/2+ladderStepLocations[c][0], 
					resourceManager.game_background_region.getHeight()/2+ladderStepLocations[c][1]);

			levels[c-1].setScale(.75f/BG_ZOOM_LVL_1);

			attachChild(levels[c-1]);
			addGroovyFloat(levels[c-1]);
			registerTouchArea(levels[c-1]);

			if(c==highestLevelCleared) {
				piece.setX(levels[c-1].getX());	
				piece.setY(levels[c-1].getY()+piece.getHeight()/4);
			}
			c++;
		}

		glowBall.setPosition(resourceManager.game_background_region.getWidth()/2+ladderStepLocations[highestLevelCleared+1][0],
				resourceManager.game_background_region.getHeight()/2+ladderStepLocations[highestLevelCleared+1][1]+glowBall.getHeight()/4);


		glowBall.setScale(.5f/BG_ZOOM_LVL_1);


		piece.setScale(.5f/BG_ZOOM_LVL_1);
		glowBall.registerEntityModifier (new LoopEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(1.5f),
						new AlphaModifier(.5f, 1f, .5f),
						new AlphaModifier(.25f, .5f, 1f))));


		attachChild(glowBall);
		addGroovyFloat(glowBall);
		attachChild(piece);
		addGroovyFloat(piece);

		if (!resourceManager.kingHasFallen()) {
			Log.d("schack","king has not fallen so I am drawing the king");
			king = new AnimatedSprite(0,0,resourceManager.king,vbom);
			king.setPosition(resourceManager.game_background_region.getWidth()/2+ladderStepLocations[ladderStepLocations.length-1][0],
					resourceManager.game_background_region.getHeight()/2+ladderStepLocations[ladderStepLocations.length-1][1]);
			attachChild(king);
			addGroovyFloat(king);	
			king.animate(2500);		
		}

		setTouchAreaBindingOnActionDownEnabled(true);



		final Sprite[] clouds = new Sprite[3];

		for (int i = 0; i<3; i++) {
			final Random r = new Random();
			float heightF = r.nextFloat()*1800;
			heightF = heightF - 900;
			clouds[i] = new Sprite(-500,ActivityGame.CAMERA_HEIGHT/2+heightF,resourceManager.cloud_region,vbom);
			clouds[i].setAlpha(.5f);
			clouds[i].setScale(r.nextFloat()*1.5f);
			moveCloud = getCloud(clouds[i]);
			attachChild(clouds[i]);
			clouds[i].registerEntityModifier(moveCloud);
			moveCloud.setAutoUnregisterWhenFinished(true);

		}


	}






	public float getLevelX(int levelId) {
		Log.d("schack","getLeveLX levelId: "+levelId);
		return resourceManager.game_background_region.getWidth()/2+ladderStepLocations[levelId][0];
	}

	public float getLevelY(int levelId) {
		return resourceManager.game_background_region.getHeight()/2+ladderStepLocations[levelId][1];
	}


	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().createScene(SceneType.SCENE_MENU);
	}


	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene()
	{
		Log.d("schack","Dispose scene called on game");
		camera.setHUD(null);
		camera.setZoomFactor(1f);
		camera.setCenter(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2);
		ResourceManager.getInstance().unloadGameResources();
		this.unregisterTouchAreas(new ITouchAreaMatcher() {

			@Override
			public boolean matches(ITouchArea pItem) {
				return true;
			}
		});


	}


	MoveXModifier moveCloud;
	private void createBackground()
	{
		Sprite castleSprite = new Sprite(ActivityGame.CAMERA_WIDTH/2+resourceManager.castleOffsetX,ActivityGame.CAMERA_HEIGHT/2+resourceManager.castleOffsetY, resourceManager.castle_region, vbom);
		Sprite bg = new Sprite(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2, resourceManager.game_background_region, vbom);
		bg.setScale(4.0f);
		attachChild(bg);


		final Random r = new Random();
		final Sprite[] clouds = new Sprite[3];

		for (int i = 0; i<3; i++) {
			float heightF = r.nextFloat()*500;
			heightF = heightF - 250;
			clouds[i] = new Sprite(-500,ActivityGame.CAMERA_HEIGHT/2+heightF,resourceManager.cloud_region,vbom);
			clouds[i].setAlpha(.5f);
			clouds[i].setScale(r.nextFloat());
			moveCloud = getCloud(clouds[i]);
			attachChild(clouds[i]);
			clouds[i].registerEntityModifier(moveCloud);
			moveCloud.setAutoUnregisterWhenFinished(true);
		}



		attachChild(castleSprite);
		//castleSprite.registerEntityModifier(pEntityModifier);
		//setBackground(new SpriteBackground(tower));
		addGroovyFloat(castleSprite);



	}


	protected MoveXModifier getCloud(final Sprite c) {
		final Random r = new Random();

		return new MoveXModifier( r.nextFloat()*50,-c.getWidth(), 2000+c.getWidth(), new IEntityModifierListener() {
			final Sprite s = c;
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				float heightF = r.nextFloat()*1800;
				heightF = heightF - 900;
				s.setY(ActivityGame.CAMERA_HEIGHT/2+heightF);
				MoveXModifier m = getCloud(s);
				m.setAutoUnregisterWhenFinished(true);
				s.registerEntityModifier(m);
			}
		});


	}



	private void addGroovyFloat(Sprite s) {
		final float pToY = s.getY();

		final float pointList[] = {
				pToY,
				pToY-10,
				pToY-20,
				pToY,
		};

		final int controlPointC = pointList.length;
		float tension = 0f;

		CardinalSplineMoveModifierConfig config;	

		CardinalSplineMoveModifier updown;


		config = new CardinalSplineMoveModifierConfig(controlPointC,tension);	
		updown = new CardinalSplineMoveModifier(6.5f,config);
		LoopEntityModifier ley = new LoopEntityModifier(updown);
		for (int i = 0;i<controlPointC;i++) 
			config.setControlPoint(i,s.getX(),pointList[i]);
		s.registerEntityModifier(ley);

	}

	boolean pressed = false;
	private final static int END_OF_FREE = 12;
	protected void levelPressed(final int levelId) {
		final int highestLevelCleared = resourceManager.getHighestLevelCleared();
		if (!zoomDone || pressed || levelId > (highestLevelCleared+1))
			Log.d("schack","Pressed or zoom not done or clicked level outside allowed range: "+levelId);
		else {
			pressed=true;
			resourceManager.gallop.play();
			piece.unregisterEntityModifiers(new IEntityModifierMatcher() {

				@Override
				public boolean matches(IModifier<IEntity> pItem) {
					return true;
				}
			});

			engine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					Log.d("Schack","Pressed level "+levelId);
					final int nLevelId = levelId-resourceManager.firstSceneId+1;
					MoveModifier m = new MoveModifier(1f,piece.getX(),piece.getY(),getLevelX(nLevelId),getLevelY(nLevelId)+piece.getHeight()/4,new IEntityModifierListener() {

						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
							if (levelId == END_OF_FREE && !resourceManager.isPaid()) {
								showPopup("Sorry, but this is\nthe end of the free.\ngame. To go further,\nplease purchase the \nfull version.",new OkCb() {

									@Override
									public void onOkClicked() {
										engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												SceneManager.getInstance().createScene(SceneManager.SceneType.SCENE_MENU);
											}

										});
									}});
							}
								
							if (nLevelId ==(resourceManager.ladderStepLocations.length-1)) {
								Log.d("schack","Glowball level up pressed");
								resourceManager.increasePlayerScene();
								resourceManager.setCurrentSelectedLevel(LevelDescriptorFactory.createLevelDescriptor(1));
								SceneManager.getInstance().createScene(SceneManager.SceneType.SCENE_GAME);
								} else 
								//IF a message popup is shown, we will wait for a while with creating next scene.
								SceneManager.getInstance().createScene(SceneType.SCENE_CHALLENGE_ENTRY);
							resourceManager.setCurrentSelectedLevel(LevelDescriptorFactory.createLevelDescriptor(levelId));
						}
					});
					piece.registerEntityModifier(m);
					m.setAutoUnregisterWhenFinished(true);
					pressed = false;
				}
			});

		}


	}


	private boolean showMessage(int level) {
		/*if (level == ladderStepLocations.length && resourceManager.getPlayerScene() == 1) {
			showKingPopup();
			return true;
		}
		 */
		return false;	
	}

	/*
	private void showKingPopup() {		
		Scene childScene = new Scene();
		childScene.setBackgroundEnabled(false);
		childScene.setPosition(0,0);
		final Sprite popup = new Sprite(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2, 
				resourceManager.game_intro_popup_region, vbom);
		Text popupText = new Text(0, 0, resourceManager.smallRedFont, "END GAME\nYou are about to face\nthe black king.\nWin by reaching mate\nwithin the move and time\nlimits. Winning will take\nyou to the next stage!\n"
				,new TextOptions(HorizontalAlign.CENTER), vbom);
		;
		popup.attachChild(popupText);
		popupText.setPosition(popup.getWidth()/2,popupText.getHeight());
		final ButtonSprite okButton = new ButtonSprite(0, 0, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				if(pTouchEvent.isActionDown()) {
					unregisterTouchArea(this);
					popup.detachChildren();
					popup.detachSelf();
					SceneManager.getInstance().createScene(SceneType.SCENE_CHALLENGE_ENTRY);
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		childScene.attachChild(popup);
		popup.attachChild(okButton);
		okButton.setPosition(popup.getWidth()/2,60);
		okButton.setScale(.60f);
		okButton.registerEntityModifier(FirstTimeScene.zoom(.60f));
		registerTouchArea(okButton);
		setChildScene(childScene);
	}
	 */

	private interface OkCb {

		public void onOkClicked();
	}

	private void showPopup(String text, final OkCb okPressedCb) {		
		Scene childScene = new Scene();
		childScene.setBackgroundEnabled(false);
		childScene.setPosition(0,0);
		final Sprite popup = new Sprite(camera.getCenterX(),camera.getCenterY(), 
				resourceManager.game_intro_popup_region, vbom);
		Text popupText = new Text(0, 0, resourceManager.smallRedFont, text,new TextOptions(HorizontalAlign.CENTER), vbom);
		;
		popup.attachChild(popupText);
		final int BORDER = 25;
		popupText.setPosition(popup.getWidth()/2,popup.getHeight()-BORDER-popupText.getHeight()/2);
		final ButtonSprite okButton = new ButtonSprite(0, 0, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				if(pTouchEvent.isActionUp()) {
					unregisterTouchArea(this);
					popup.detachChildren();
					popup.detachSelf();
					if (okPressedCb!=null)
						okPressedCb.onOkClicked();
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		childScene.attachChild(popup);
		popup.attachChild(okButton);	
		okButton.setPosition(popup.getWidth()/2,60);
		okButton.setScale(.60f);
		okButton.registerEntityModifier(FirstTimeScene.zoom(.60f));
		registerTouchArea(okButton);
		setChildScene(childScene);
	}


	private HUD gameHUD;
	private Text scoreText;
	private float mPinchZoomStartedCameraZoomFactor;

	private void createHUD()
	{
		gameHUD = new HUD();

		// CREATE SCORE TEXT
		scoreText = new Text(20, ActivityGame.CAMERA_HEIGHT-80, resourceManager.font, "Total score: 0000000000000000000"+resourceManager.getTotalScore(), new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setText("Total score: "+resourceManager.getTotalScore());
		scoreText.setAnchorCenter(0, 0);    
		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		final float zoomFactor = camera.getZoomFactor();
		float x = -pDistanceX / zoomFactor;
		float y = pDistanceY/zoomFactor;
		if (!((x<0 && camera.getCenterX()>=(-500*zoomFactor))||(x>0 && camera.getCenterX()<=(1000*zoomFactor))))
			x = 0;

		if(!((y>0&&camera.getCenterY()<1000*zoomFactor)||(y<0&&camera.getCenterY()>-800*zoomFactor+600)))
			y = 0;       
		camera.offsetCenter(x, y);
		//scoreText.setText("cy:"+(int)camera.getCenterY()+" Z:"+camera.getZoomFactor());

	}


	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		checkBoundaries();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(myPinchZoomDetector != null) {
			myPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(myPinchZoomDetector.isZooming()) {
				myScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					myScrollDetector.setEnabled(true);
				}
				myScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			myScrollDetector.onTouchEvent(pSceneTouchEvent);
		}




		return true;
	}



	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		mPinchZoomStartedCameraZoomFactor = camera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		final float newZ = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
		if (newZ>0.2f && newZ<1.2f) {
			camera.setZoomFactor(newZ);
			checkBoundaries();
		}
	}

	private void checkBoundaries() {
		final float zoomFactor = camera.getZoomFactor();
		if(camera.getCenterX()<(-500f*zoomFactor))
			camera.setCenter(-500f*zoomFactor, camera.getCenterY());
		else if (camera.getCenterX()>(1000f*zoomFactor))
			camera.setCenter(1000f*zoomFactor, camera.getCenterY());

		if(camera.getCenterY()>1000f*zoomFactor)
			camera.setCenter(camera.getCenterX(), 1000f*zoomFactor);
		else if (camera.getCenterY()<(-800f*zoomFactor+600f))
			camera.setCenter(camera.getCenterX(), -800f*zoomFactor+600f);       
		
	}



	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		final float newZ = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
		if (newZ>0.25f && newZ<=1f)
			camera.setZoomFactor(newZ);
		else {
			if (newZ<=0.25f)
				camera.setZoomFactor(0.25f);
			else
				camera.setZoomFactor(1f);
		}
	}



}
