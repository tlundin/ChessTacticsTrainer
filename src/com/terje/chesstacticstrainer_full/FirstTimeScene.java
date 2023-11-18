package com.terje.chesstacticstrainer_full;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;
import com.terje.chesstacticstrainer_full.WebService.WsCallback;

public class FirstTimeScene extends BaseScene implements IOnMenuItemClickListener,EntryListener {

	InputText it;
	Rectangle rect;
	Text header,bread,countrySelectHeader;
	ButtonSprite okButton;
	
	ArrayList<ButtonSprite> countryList;
	final String[] country = ResourceManager.getInstance().countries;


	@Override
	public void createScene() {
		createBackground();

		okButton = new  ButtonSprite(0, 0, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				ITextureRegion[] cTextures;
				if(pTouchEvent.isActionUp()) {
					rect.detachChildren();
					unregisterTouchArea(okButton);
					cTextures = ResourceManager.getInstance().countryTextures;
					int i = 0,j=1,c=0,margX = 70,margY = -100;
					int w=120,h=150;
					MenuScene menuChildScene = new MenuScene(camera);
					IMenuItem playMenuItem;
					for(ITextureRegion texture:cTextures) {
						playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(c++, texture, vbom), .30f, .27f);
						Log.d("schack","Adding "+country[c-1]+" at position x:"+i*w+" y:"+j*h);
						menuChildScene.addMenuItem(playMenuItem);
						// playMenuItem.setPosition();
						playMenuItem.registerEntityModifier(new MoveModifier(.75f, i*w+margX,-100,i*w+margX, j*h+margY));

						i++;
						if (i==4) {
							i=0;
							j++;
						}

					}
					menuChildScene.setPosition(0, 0);
					menuChildScene.buildAnimations();
					menuChildScene.setBackgroundEnabled(false);
					menuChildScene.setOnMenuItemClickListener(FirstTimeScene.this);
					setChildScene(menuChildScene);

					rect.attachChild(countrySelectHeader);
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		//final LoopEntityModifier loop = new LoopEntityModifier(zoom);

		okButton.registerEntityModifier(zoom(1f));
		registerTouchArea(okButton);

		rect = resourceManager.first_time_bg_rect; 
		header = new Text(0, 0, resourceManager.font, "Congratulations!", vbom);
;
		bread = new Text(0, 0, resourceManager.smallWhiteFont, "You have earned the rights\nto select a name and a\ncountry for yourself!\n"
				+ " This will show up in the\nranking and highscore lists.",new TextOptions(HorizontalAlign.CENTER), vbom);
		countrySelectHeader = new Text(0, 0, resourceManager.font, "Select your country!", vbom);

		rect.setAlpha(.15f);
		header.setPosition(rect.getWidth()/2,rect.getHeight()-35);
		bread.setPosition(rect.getWidth()/2,rect.getHeight()-200);
		countrySelectHeader.setPosition(rect.getWidth()/2,rect.getHeight()-50);
		okButton.setPosition(rect.getWidth()/2,rect.getHeight()-bread.getHeight()-header.getHeight()-200);
		rect.attachChild(bread);
		rect.attachChild(header);
		rect.attachChild(okButton);
		attachChild(rect);

	}

	public static LoopEntityModifier zoom(final float fromScale) {
		
		final float toScale = fromScale + .03f;
		final float sDuration = .4f;
		ScaleModifier scaleup = new ScaleModifier(sDuration,fromScale,toScale);
		ScaleModifier scaledown = new ScaleModifier(sDuration,toScale,fromScale);
		DelayModifier sDelay = new DelayModifier(.5f);
		return new LoopEntityModifier(new SequenceEntityModifier(scaleup,sDelay,scaledown));
	}
	
	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().createScene(SceneType.SCENE_MENU);
	}


	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_FIRST_TIME;
	}

	@Override
	public void disposeScene() {
		
		
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				Log.d("schack","dispose called");
				ResourceManager.getInstance().unloadFirstTimeResources();
				detachChildren();
				detachSelf();
				rect=null;
				it=null;
				header=bread=countrySelectHeader=null;
						
			}});
	}

	private void createBackground()
	{
		setBackground(new Background(ResourceManager.mBackgroundColor));
	}



	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		final IEntityModifierListener listener = new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				//Now start normal hang loop for both.
				askForName();
			}};			

			countryCode = country[pMenuItem.getID()];
			Log.d("schack","User selected the following country: "+countryCode);
			pMenuScene.detachChildren();
			pMenuItem.registerEntityModifier(new MoveModifier(.65f, pMenuItem.getX(),pMenuItem.getY(),ActivityGame.CAMERA_WIDTH/2-pMenuItem.getWidth()/2,ActivityGame.CAMERA_HEIGHT-50,listener));
			rect.attachChild(pMenuItem);
			countrySelectHeader.setText("");

			return false;

	}

	private void askForName() {
		clearChildScene();
		countrySelectHeader.setY(ActivityGame.CAMERA_HEIGHT-120);
		countrySelectHeader.setText("Enter a name!");
		final float fromScale = .75f;
		final float toScale = .77f;
		final float sDuration = .4f;
		ScaleModifier scaleup = new ScaleModifier(sDuration,fromScale,toScale);
		ScaleModifier scaledown = new ScaleModifier(sDuration,toScale,fromScale);
		DelayModifier sDelay = new DelayModifier(.5f);

		SequenceEntityModifier zoom = new SequenceEntityModifier(scaleup,sDelay,scaledown);
		//Now ask for a name
		ITextureRegion texture = ResourceManager.getInstance().inputfield_region;
		it = new InputText(rect.getWidth()/2,ActivityGame.CAMERA_HEIGHT-texture.getHeight()-100, "Name", "Enter a nickname", texture, ResourceManager.getInstance().font, 35, (int) (texture.getHeight()-60), vbom,activity,(EntryListener)this);
		it.setScale(fromScale);
		it.registerEntityModifier(zoom);
		rect.attachChild(it);
		registerTouchArea(it);


	}

	private String countryCode;
	private String playerName="";
	
	@Override
	public void okPressed() {
		unregisterTouchArea(it);
		try {
			playerName = URLEncoder.encode(it.getText(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebService rt = new WebService(new WsCallback() {

			@Override
			public void doSomething(String res) {

				
				//allow user to change name if he wants to.
				registerTouchArea(it);
				String  OK_RESULT = "1";
				if (res.equals(OK_RESULT)) {
				} else 
					Log.e("Schack","DB Insert returnerade "+res);

			}});
		Log.d("schack","Calling updateuser with "+"updateuser="+playerName+"&country="+countryCode+"&uuid="+resourceManager.getMyId());
		rt.execute("updateuser="+playerName+"&country="+countryCode+"&uuid="+resourceManager.getMyId());

		okButton = new  ButtonSprite(0, 0, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pTouchEvent.isActionDown()) {
					unregisterTouchArea(it);
					ResourceManager.getInstance().setPlayerName(playerName);
					resourceManager.userHasBeenAskedForName();
					SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		okButton.registerEntityModifier(zoom(1f));
		registerTouchArea(okButton);
		okButton.setPosition(rect.getWidth()/2,rect.getHeight()-bread.getHeight()-header.getHeight()-200);
		rect.attachChild(okButton);


	}

		@Override
		public void cancelPressed() {
			// TODO Auto-generated method stub

		}

}
