package com.terje.chesstacticstrainer_full;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;
import com.terje.chesstacticstrainer_full.WebService.WsCallback;


public class StatsScene extends BaseScene implements IOnMenuItemClickListener {
	private int SW,SH,RW;

	private boolean paid = false;

	private class LevelStats {

		int globalPosition;
		int highscore;
		int level;
		int myscore;
		String leaderName;

	}

	private List<LevelStats> levels = new ArrayList<LevelStats>();

	@Override
	public void createScene() {
		SW = ActivityGame.CAMERA_WIDTH;
		SH = ActivityGame.CAMERA_HEIGHT;
		RW = 45;

		paid = true;

		setBackground(new Background(0.22f,0.32f,0.435f));


		if (resourceManager.isNetworkAvailable()) {
			final WsCallback statsParser = new WsCallback() {
				@Override
				public void doSomething(String res) {
//					Log.e("schack","GOT "+res);
					res = res.replaceAll("(&[^\\s]+?;)", "");
					Log.e("schack","After "+res);
					XmlPullParserFactory factory;
					try {
						factory = XmlPullParserFactory.newInstance();

						factory.setNamespaceAware(true);
						XmlPullParser xpp = factory.newPullParser();

						xpp.setInput( new StringReader (res) );
						int eventType = xpp.getEventType();

						LevelStats curLev;
						String cVal = null;
						curLev = new LevelStats();
						while (eventType != XmlPullParser.END_DOCUMENT) {
							if(eventType == XmlPullParser.START_DOCUMENT) {
								System.out.println("Found start doc");
							} else if(eventType == XmlPullParser.START_TAG) {
								System.out.println("Found start tag "+xpp.getName());
							} else if(eventType == XmlPullParser.END_TAG) {
								System.out.println("End tag "+xpp.getName()+" Cval: "+cVal);
								if (cVal != null) {
									if (xpp.getName().equals("usersaboveme")) {
										curLev.globalPosition = Integer.parseInt(cVal)+1;
										Log.d("schack","Trying to add GLOBAL pos for level "+curLev.level+" i# "+levels.size()+" gp: "+curLev.globalPosition);
									}
									else if (xpp.getName().equals("highscore"))
										curLev.highscore= Integer.parseInt(cVal);
									else if (xpp.getName().equals("level"))
										curLev.level = Integer.parseInt(cVal);
									else if (xpp.getName().equals("myscoreonthislevel")) {
										curLev.myscore= Integer.parseInt(cVal);
									}
									else if (xpp.getName().equals("post")) {
										levels.add(curLev);
										curLev = new LevelStats();
									}
								}
							} else if(eventType == XmlPullParser.TEXT) {
								System.out.println("Found value "+xpp.getText());
								cVal = xpp.getText();
							}
							eventType = xpp.next();
						}

					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   

					int globalPos=0;int levelC=0;
					if (levels.size()>0) {
						for(LevelStats ld:levels) {
							Log.d("schack","GLOBAL POS FOR LEVEL "+ld.level+" is "+ld.globalPosition);
							if (ld.myscore>0) {
								globalPos+=ld.globalPosition;
								levelC++;
							}
						}

						Text txt = new Text(SW/2,SH-SH/10-2*RW, resourceManager.mediumWhiteFont, "World position: "+(levelC>0?globalPos/levelC:"?"),vbom);
						attachChild(txt); 

					}

				}
			};
			WebService ws = new WebService(statsParser);
			ws.execute("uuid='"+resourceManager.getMyId()+"'&stats=");

		}

		//int highestLevel = resourceManager.getHighestLevelCleared();
		int trickiestSolved = resourceManager.getTrickiestSolved();
		int totalSolved = resourceManager.getTotalSolved();
		int totalScore = resourceManager.getTotalScore();
		int totalWoos = resourceManager.getTotalWoos();
		int longestGreen = resourceManager.getLongestGreen();	

		int rating = (int)resourceManager.getRating();

		//int currentHigh = resourceManager.getPlayerScore(ld.getLevel());
		LoopEntityModifier blinkModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeOutModifier(0.5f), new FadeInModifier(0.5f)));

		String userName = resourceManager.getPlayerName();
		Text txt = new Text(SW/2,SH-SH/10, resourceManager.mediumWhiteFont, "Stats for "+(userName.equals("")?"*unknown*":userName), vbom);
		attachChild(txt);
		txt.registerEntityModifier(blinkModifier);
		int cursor = 3;
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "Total score: "+totalScore, vbom);
		attachChild(txt);    
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "Your rating: "+(resourceManager.isTrainingLocked()?"****":rating), vbom);
		attachChild(txt);    
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "#Problems solved: "+totalSolved, vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "Longest green sequence: "+longestGreen, vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "#Wooo's: "+totalWoos, vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-cursor++*RW, resourceManager.smallWhiteFont, "Hardest solved: "+(paid?trickiestSolved:"Hidden"),vbom);
		attachChild(txt);
		//attachButtons();



		ButtonSprite okButton = new  ButtonSprite(SW/2, SH/8, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				StatsScene.this.unregisterTouchArea(this);
				StatsScene.this.onBackKeyPressed();

				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};


		registerTouchArea(okButton);

		attachChild(okButton);


	}

	private MenuScene menuChildScene;
	private final int MENU_PROFILE = 0;
	private final int MENU_SCORE_PER_LEVEL = 2;
	private final int MENU_RATING = 3;

	private void attachButtons() {


		final IMenuItem profileMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PROFILE, resourceManager.profileB_region, vbom), 1.2f, 1);
		final IMenuItem scorePerLevelMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SCORE_PER_LEVEL, resourceManager.score_per_levelB_region, vbom), 1.2f, 1);
		final IMenuItem ratingListMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RATING, resourceManager.rating_listB_region, vbom), 1.2f, 1);

		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);
		menuChildScene.addMenuItem(profileMenuItem);
		menuChildScene.addMenuItem(scorePerLevelMenuItem);
		menuChildScene.addMenuItem(ratingListMenuItem);
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		profileMenuItem.setPosition(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2-100);
		scorePerLevelMenuItem.setPosition(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2-180);
		ratingListMenuItem.setPosition(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2-260);
		//optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 120);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
		{
		case MENU_PROFILE:
			break;
		case MENU_SCORE_PER_LEVEL:
			break;
		case MENU_RATING:
			break;
		}
		return true;
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().createScene(SceneType.SCENE_MENU);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_STATS;
	}

	@Override
	public void disposeScene() {
		resourceManager.unloadStatsResources();
	}



}
