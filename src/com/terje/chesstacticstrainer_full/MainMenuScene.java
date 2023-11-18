package com.terje.chesstacticstrainer_full;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.util.modifier.IModifier;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener  {

	final static float SH = ActivityGame.CAMERA_HEIGHT;
	final static float SW = ActivityGame.CAMERA_WIDTH;

	final static float baseX = SW/2;
	//final static float sub_baseX = SW/2;
	final static float baseY = SH-130;
	final static float startPosY = SH+500;//-resourceManager.chesstactics_region.getHeight();
	final static float startPosX = -500;//-resourceManager.challenge_region.getWidth();
	Sprite header;
	Sprite subheader;

	final float pDuration = 1.2f;
	final float pFromY = startPosY;
	final static float pToY = SH-50;
	final float sDuration = 3f;
	final float fromScale = 1f;
	final float toScale = 1.05f;
	final float tension = 0f;
	final static float pointList[] = {
		pToY,
		pToY-10,
		pToY-20,
		pToY,
	};

	final static int controlPointC = pointList.length;
	MoveYModifier my;
	ScaleModifier scaleup;
	ScaleModifier scaledown;
	DelayModifier sdelay;
	SequenceEntityModifier zoom,zoom2;
	CardinalSplineMoveModifierConfig config;	
	CardinalSplineMoveModifier updown;
	ParallelEntityModifier pey;
	LoopEntityModifier ley;
	DelayModifier ldelay;
	IEntityModifierListener listener;			
	MoveXModifier mx;
	SequenceEntityModifier smx;
	private CardinalSplineMoveModifierConfig config2;
	private CardinalSplineMoveModifier updown2;
	private LoopEntityModifier sley;
	private Sprite bgSprite;


	private void createBackground()
	{

		header = new Sprite(baseX,startPosY, resourceManager.chesstactics_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		subheader = new Sprite(startPosX,baseY, resourceManager.challenge_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		bgSprite = new Sprite(SW/2,SH/2, resourceManager.background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};		

		attachChild(header);
		attachChild(subheader);
		if (resourceManager.isMaster())
			attachChild(new Text(SW/2,SH/2+150,resourceManager.font,"MASTER MODE",vbom));
		//setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, bgSprite));
		setBackground(background);

		my = new MoveYModifier(pDuration,pFromY, pToY,  org.andengine.util.modifier.ease.EaseBounceIn.getInstance());
		scaleup = new ScaleModifier(sDuration,fromScale,toScale);
		scaledown = new ScaleModifier(sDuration,toScale,fromScale);
		sdelay = new DelayModifier(.5f);
		//		zoom = new SequenceEntityModifier(scaleup,sdelay,scaledown);
		config = new CardinalSplineMoveModifierConfig(controlPointC,tension);	
		config2 = new CardinalSplineMoveModifierConfig(controlPointC,tension);	
		updown = new CardinalSplineMoveModifier(6.5f,config);
		updown2 = new CardinalSplineMoveModifier(6.5f,config2);
		//		pey = new ParallelEntityModifier(zoom, updown);
		//		sey = new ParallelEntityModifier(zoom, updown2);
		ley = new LoopEntityModifier(updown);
		sley = new LoopEntityModifier(updown2);
		ldelay = new DelayModifier(1f);
		listener = new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				//Now start normal hang loop for both.
				unregisterAndPlay();
			}};			
			mx = new MoveXModifier(pDuration,startPosX,baseX,listener,org.andengine.util.modifier.ease.EaseExponentialIn.getInstance());
			smx = new  SequenceEntityModifier(ldelay,mx);

			float dif = baseY - pToY;
			for (int i = 0;i<controlPointC;i++) {
				config.setControlPoint(i,baseX,pointList[i]);
				config2.setControlPoint(i,baseX,pointList[i]+dif);
			}

			header.registerEntityModifier(my);
			subheader.registerEntityModifier(smx);

	}


	private void unregisterAndPlay() {
		//header.unregisterEntityModifier(my);
		//subheader.unregisterEntityModifier(smx);
		subheader.registerEntityModifier(sley);
		header.registerEntityModifier(ley);
		createMenuChildScene();
	}

	@Override
	public void createScene() {
		createBackground();
		Music mMusic = resourceManager.mMusic;
		if(mMusic !=null && !mMusic.isPlaying())
			mMusic.play();
		//register user.
		
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		Log.d("schack","Unload called here");
		resourceManager.unloadMenuResources();
		unregisterEntityModifiers(new IEntityModifierMatcher() {

			@Override
			public boolean matches(IModifier<IEntity> pItem) {
				return true;
			}
		});
		this.detachChildren();
		detachSelf();
	}

	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
//	private final int MENU_OPTIONS = 1;
	private final int MENU_STATS = 2;
	private final int MENU_PRACTISE = 3;
	
	private void createMenuChildScene()
	{
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourceManager.playB_region, vbom), 1.2f, 1);
		final IMenuItem practiceMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PRACTISE, resourceManager.practiceB_region, vbom), 1.2f, 1);
		final IMenuItem statsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_STATS, resourceManager.statsB_region, vbom), 1.2f, 1);

		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);
		menuChildScene.addMenuItem(playMenuItem);
		if (!resourceManager.isTrainingLocked())
			menuChildScene.addMenuItem(practiceMenuItem);
		menuChildScene.addMenuItem(statsMenuItem);
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() -100);
		practiceMenuItem.setPosition(practiceMenuItem.getX(), practiceMenuItem.getY() - 110);
		statsMenuItem.setPosition(statsMenuItem.getX(), statsMenuItem.getY() - 120);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
		{
		case MENU_PLAY:
			resourceManager.incrementUseCount();

			if (resourceManager.getVisitCount()>5 && resourceManager.hasNotBeenAskedToRate()) {
				Log.d("schack","Will ask the user to Rate the App now");

				
				activity.runOnUiThread(new Runnable() {
					  public void run() {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									activity);

							// set title
							alertDialogBuilder.setTitle("Please help us!");

							// set dialog message
							alertDialogBuilder
							.setMessage("Could you spare a moment to rate this App? For problems, contact me directly: terje_sverje@yahoo.se. Thanks!")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									Uri uri = Uri.parse("market://details?id=" + MainMenuScene.this.activity.getPackageName());
									Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
									try {
										MainMenuScene.this.activity.startActivity(goToMarket);
									} catch (ActivityNotFoundException e) {
									  Toast.makeText(MainMenuScene.this.activity, "Couldn't contact the market", Toast.LENGTH_LONG).show();
									}
									//Don't ask the user again.
									resourceManager.userHasBeenAskedToRate();
								}
							})
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
									SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
									if (resourceManager.getVisitCount()>8) {
										resourceManager.userHasBeenAskedToRate();
										
									}
								}
							});
						  AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
							alertDialog.show();
					  }
					});
				// create alert dialog
				
				
			} 
				else 
					SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
					//SceneManager.getInstance().createScene(SceneType.SCENE_ENDING);
					//SceneManager.getInstance().createScene(SceneType.SCENE_FIRST_TIME);
			return true;
			
		case MENU_STATS:
			SceneManager.getInstance().createScene(SceneType.SCENE_STATS);
			return true;
			
		case MENU_PRACTISE:
			//ResourceManager.getInstance().setCurrentSelectedLevel(LevelDescriptorFactory.createLevelDescriptor(LevelDescriptorFactory.TRAINING_LD));
			ResourceManager.getInstance().setCurrentSelectedLevel(LevelDescriptorFactory.createLevelDescriptor(LevelDescriptorFactory.TRAINING_LD));
			SceneManager.getInstance().createScene(SceneType.SCENE_CHALLENGE_ENTRY);
			return true;
				
		default:
			return false;
		}
	}

	/*
	if (ResourceManager.getInstance().getPlayerName()!=PersistenceHelper.UNDEFINED)
		SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
		else 
			SceneManager.getInstance().createScene(SceneType.SCENE_FIRST_TIME);
	return true;
	 */
	/*
	SimpleChessEngine s = new SimpleChessEngine();
	GameState gs = new GameState(ChessConstants.INITIAL_BOARD,true);
	s.findBestMove(gs);
	 */



}
