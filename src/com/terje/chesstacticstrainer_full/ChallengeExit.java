package com.terje.chesstacticstrainer_full;

import java.io.IOException;
import java.io.StringReader;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.LevelDescriptor;
import com.terje.chesstacticstrainer_full.SceneManager.SceneType;
import com.terje.chesstacticstrainer_full.Types.TacticResult;
import com.terje.chesstacticstrainer_full.WebService.WsCallback;

public class ChallengeExit extends BaseScene {
	private String descr;
	private int SW,SH,RW;
	private boolean good;
	@Override
	public void createScene() {
		SW = ActivityGame.CAMERA_WIDTH;
		SH = ActivityGame.CAMERA_HEIGHT;
		RW = 45;
		descr = "";

		setBackground(new Background(0.22f,0.32f,0.435f));

		TacticResult myResult = resourceManager.getTacticResult();
		LevelDescriptor ld = resourceManager.getCurrentSelectedLevel();

		int percentageGreen =(int) ((float)myResult.getGreens()/(float)ld.getNoOfProblems()*100);
		int percGBonus = (percentageGreen > 75)?50:(percentageGreen>50?25:0);
		int failBonus = -myResult.getFails()*50;
		int seqG = myResult.getGreenSequence();
		int greenSeqBonus = seqG>2?seqG*20:0;
		int woosBonus = myResult.getWoos()*50;
		int total = myResult.getScore()+percGBonus+failBonus+greenSeqBonus+woosBonus;		
		int currentHigh = resourceManager.getPlayerScore(ld.getLevel());
		LoopEntityModifier blinkModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeOutModifier(0.75f), new FadeInModifier(0.75f)));

		Text txt = new Text(SW/2,SH-SH/10, resourceManager.mediumWhiteFont, (ld.isRated?"Change: ":"Score: ")+myResult.getScore(), vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-1*RW, resourceManager.mediumWhiteFont, "Speedy: "+percentageGreen+"%"+(!ld.isRated?" (+"+percGBonus+")":""), vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-2*RW, resourceManager.mediumWhiteFont, "Faults: "+myResult.getFails()+(!ld.isRated?" ("+failBonus+")":""), vbom);
		if(myResult.getFails()>ld.getNoOfFailsAllowed()) {
			txt.setColor(Color.RED);
			txt.registerEntityModifier(blinkModifier);
		}
		attachChild(txt);	    
		txt = new Text(SW/2,SH-SH/10-3*RW, resourceManager.smallWhiteFont, "Longest sequence: "+myResult.getGreenSequence()+(!ld.isRated?" (+"+greenSeqBonus+")":""), vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/10-4*RW, resourceManager.smallWhiteFont, "Wooo's: "+myResult.getWoos()+(!ld.isRated?" (+"+woosBonus+")":""), vbom);
		attachChild(txt);
		txt = new Text(SW/2,SH-SH/8-6*RW, resourceManager.mediumWhiteFont,(!ld.isRated?"TOTAL: "+total+
				(total>currentHigh?" (PB!)":""):"New rating: "+resourceManager.getRating()),vbom);
		attachChild(txt);
		if (ld.isRated) {
			txt = new Text(SW/2,SH-SH/8-7*RW, resourceManager.smallWhiteFont,"RD: "+resourceManager.getRD(),vbom);
			attachChild(txt);
			if (myResult.getScore()>0)
				resourceManager.bravoSound.play();
		}
		if(!ld.isRated&&total>currentHigh) {
			txt.registerEntityModifier(blinkModifier);
			resourceManager.setPersonalBest(ld.getLevel(),total);
		}
		sendResult(ld,ld.isRated?(int)resourceManager.getRating():total);			
		if (!ld.isRated) {
			int result = calcResult(ld,total);
			if (result>0) {
				good = true;
				placeHead(result);
				resourceManager.setCleared(ld.getLevel());
				//If this was the last level of the scene, increase scene number.
				if (result>2)
					resourceManager.bravoSound.play();
			} 

			else {
				good = false;
				placeFail();
				resourceManager.awwSound.play();
			}
		}

		final ButtonSprite okButton = new  ButtonSprite(SW/2, SH/8-RW/2, resourceManager.ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if(pTouchEvent.isActionUp()) {
					unregisterTouchArea(this);
					Log.d("schack","Good is "+good+" playerscene is "+resourceManager.getHighestLevelCleared()+" istraininglocked: "+resourceManager.isTrainingLocked());
					if (good) {				
						if (resourceManager.getHighestLevelCleared()>2 && resourceManager.hasNotBeenAskedName() && resourceManager.isNetworkAvailable()) {
							SceneManager.getInstance().createScene(SceneType.SCENE_FIRST_TIME);		
						}
						else if (resourceManager.getHighestLevelCleared()>5 &&
									resourceManager.isTrainingLocked()) {
								activity.runOnUiThread(new Runnable() {
									public void run() {
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												activity);

										// set title
										alertDialogBuilder.setTitle("Congratulations!");

										// set dialog message
										alertDialogBuilder
										.setMessage("You have unlocked the Practice mode! You can find it under 'Practice' in the Main Menu!")
										.setCancelable(false)
										.setPositiveButton("Okay!",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												resourceManager.unlockTraining();
												SceneManager.getInstance().createScene(SceneType.SCENE_GAME);

											}
										});

										AlertDialog alertDialog = alertDialogBuilder.create();
										// show it
										alertDialog.show();
									}
								});
						


						} else {
							Log.d("schack","Should get here?");
							SceneManager.getInstance().createScene(SceneType.SCENE_GAME);
						}
						
					} else
						SceneManager.getInstance().createScene(SceneType.SCENE_GAME);


				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}


		};

		resourceManager.registerWoos(myResult.getWoos());
		resourceManager.registerLongestGreen(myResult.getGreenSequence());
		okButton.registerEntityModifier(FirstTimeScene.zoom(1f));
		registerTouchArea(okButton);
		attachChild(okButton);

	}


	private void placeFail() {
		Sprite failHead = new Sprite(SW/2,SH/2-2*RW,resourceManager.fail_region,vbom);
		failHead.registerEntityModifier(new ScaleModifier(1.5f,3f,1f,new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				Text txt = new Text(SW/2,SH/8+RW*2.5f, resourceManager.mediumWhiteFont, "Not passed", vbom);
				txt.setColor(Color.RED);
				attachChild(txt);		
			}
		} ,EaseBounceOut.getInstance()));
		attachChild(failHead);
	}

	private void placeHead(final int horses) {
		Sprite horseHead;
		int i = 3-horses;
		if (horses == 0) {
			Text txt = new Text(SW/2,SH/8+RW*2.5f, resourceManager.mediumWhiteFont, descr, vbom);
			attachChild(txt);		
		} else {
			horseHead = new Sprite(SW/4*(i+1),SH/2-2*RW,resourceManager.empty_horseHead_region,vbom);
			horseHead.registerEntityModifier(new ScaleModifier(1.5f,3f,1f,new IEntityModifierListener() {

				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}

				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					placeHead(horses-1);
				}
			} ,EaseBounceOut.getInstance()));
			attachChild(horseHead);
		}
	}




	private int calcResult(LevelDescriptor ld,int score) {
		int ret = 0;
		if (resourceManager.getTacticResult().getFails()>ld.getNoOfFailsAllowed()) {
			//do nothing..score will be 0.
		} else if (score>ld.getExcellentS()) {
			descr = "Excellent!";
			ret = 3;
		}
		else if (score>ld.getGoodS()) {
			descr = "Good!";
			ret =2;
		}
		else if (score>ld.getPoorS()) {
			descr = "Fair";
			ret = 1;
		}
		return ret;
	}

	private void sendResult(LevelDescriptor ld, int score) {
		if (resourceManager.isNetworkAvailable()) {
			final WsCallback afterinsert = new WsCallback() {

				@Override
				public void doSomething(String res) {
					Log.d("schack","Got "+res+"back");
					XmlPullParserFactory factory;
					try {
						factory = XmlPullParserFactory.newInstance();

						factory.setNamespaceAware(true);
						XmlPullParser xpp = factory.newPullParser();
						if (res!=null) {
						xpp.setInput( new StringReader (res) );
						int eventType = xpp.getEventType();
						String myPosition = null;
						while (eventType != XmlPullParser.END_DOCUMENT) {
							if(eventType == XmlPullParser.START_DOCUMENT) {
								System.out.println("Found start doc");
							} else if(eventType == XmlPullParser.START_TAG) {
								System.out.println("Found start tag "+xpp.getName());
							} else if(eventType == XmlPullParser.END_TAG) {
								System.out.println("End tag "+xpp.getName());
							} else if(eventType == XmlPullParser.TEXT) {
								System.out.println("Found value "+xpp.getText());
								myPosition = xpp.getText();
							}
							eventType = xpp.next();
						}
						if (myPosition !=null) {
							int myP = Integer.parseInt(myPosition);
							Log.d("schack","Parsed int value for my position: "+myP);
							Text txt = new Text(SW/2,SH-SH/10-5*RW, resourceManager.smallWhiteFont, (myP==0?"GLOBAL HIGHSCORE! AMAZING!":"World position: "+(myP+1)), vbom);			        	
							attachChild(txt);
						}
						}

					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
				} 
			};
			WebService ws = new WebService(afterinsert);
			String url = "uuid='"+resourceManager.getMyId()+"'&level="+ld.getLevel()+
					"&score="+score+(ld.isRated?"&rd="+resourceManager.getRD():
						("&total="+resourceManager.getTotalScore()+"&woos="+resourceManager.getTotalWoos()));
			Log.d("schack",url);
			ws.execute(url);
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().createScene(SceneType.SCENE_MENU);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_CHALLENGE_EXIT;
	}

	@Override
	public void disposeScene() {
		resourceManager.unloadChallengeExitResources();
	}



}
