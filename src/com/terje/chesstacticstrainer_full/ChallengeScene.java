package com.terje.chesstacticstrainer_full;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.LevelDescriptor;
import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.StageDescriptorFactory.StageDescriptor;
import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.StageDescriptorFactory.StageDescriptor.Type;
import com.terje.chesstacticstrainer_full.SceneManager.SceneType;
import com.terje.chesstacticstrainer_full.Types.TacticResult;

public class ChallengeScene extends BaseScene {

	private ChessBoard chessBoard;
	private Sprite happyFace,unhappyFace;
	//private List<TacticProblem> problems;
	//private TacticProblem problem;
	private RotationModifier happyModifier;
	private AlphaModifier unhappyModifier;

	private int currentC=0;
	private Text tacticScore;
	private int myScore,runningScore;
	private static final int BoardMargin = 150;
	private Sprite[] angryHeads;
	private int failC;
	private TacticResult myResult;
	private TempMeter_I meter; 
	private TempMeter timedMeter;
	private NoTempMeter untimedMeter;
	private final String smileyPos[] = { "pppppppppPPppPPpppPppPppPppppppPpPppppPppPPppPPpppPPPPpppppppppp",
			"ppppppppPPPPPPPPppppppppPPPPPPPPppppppppPPPPPPPPppppppppPPPPPPPP",
			"nNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnNnN",
	"xxxppxxxxxxppxxxxxxppxxxppppppppppppppppxxxppxxxxxxppxxxxxxppxxx" };
	private final static SimpleChessEngine ce = new SimpleChessEngine(3);
	private static StageDescriptor sd;
	private static StateType STATE=StateType.WAITING;
	private static MoveList ml=null;
	private static boolean PAUSED = false,alreadyQuitting = false, heartPlaying = false;
	private static int heartBeatLength = 10;
	private float tpy;
	private int tick=0;
	private int gtick=0;
	private int moveCount;
	private MoveYModifier drop,drop2;


	private LevelDescriptor ld;
	private String stateMessage="";
	@Override
	public void createScene() {
		myResult = new TacticResult();
		ld = resourceManager.getCurrentSelectedLevel();

		setBackground(new Background(0.22f,0.32f,0.435f));
		chessBoard = new ChessBoard(this,BoardMargin);

		tacticScore = new Text(0, ActivityGame.CAMERA_HEIGHT-40, resourceManager.mediumWhiteFont,"Points:"+"00000"+"\n               ", vbom);
		tacticScore.setX(tacticScore.getWidth()/2+20);

		tpy = ActivityGame.CAMERA_HEIGHT-BoardMargin+resourceManager.tactics_termoPause.getHeight()/2;
		pc_thinking = new Sprite(ActivityGame.CAMERA_WIDTH/2,tpy,resourceManager.tactics_thinking,vbom);
		pc_thinking.setVisible(false);

		drop = new MoveYModifier(.5f, ActivityGame.CAMERA_HEIGHT+100, ActivityGame.CAMERA_HEIGHT/2-16);
		drop2 = new MoveYModifier(.25f, ActivityGame.CAMERA_HEIGHT+100, ActivityGame.CAMERA_HEIGHT/2+100);

		timedMeter = new TempMeter();
		timedMeter.setVisible(false);
		untimedMeter = new NoTempMeter();
		attachChild(tacticScore);
		attachChild(timedMeter);
		attachChild(chessBoard);
		attachChild(pc_thinking);


		tacticScore.registerEntityModifier(FirstTimeScene.zoom(1.0f));

		happyFace = new Sprite(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2,ResourceManager.getInstance().happyface,vbom);
		unhappyFace = new Sprite(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2,ResourceManager.getInstance().unhappyface,vbom);
		happyFace.setScale(2.0f);
		unhappyFace.setScale(2.0f);

		angryHeads = new Sprite[3];
		for (int i = 0; i<angryHeads.length;i++) {
			angryHeads[i]=new Sprite(ActivityGame.CAMERA_WIDTH-180+60*i,ActivityGame.CAMERA_HEIGHT-40,ResourceManager.getInstance().unhappyface,vbom);
			angryHeads[i].setScale(.784f);
			angryHeads[i].setVisible(false);
			attachChild(angryHeads[i]);
		}
		failC = 0;

		setTouchAreaBindingOnActionDownEnabled(true);

		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				unhappyModifier = new AlphaModifier(1.5f,1.0f,0.0f) {
					@Override
					protected void onModifierStarted(IEntity pItem)
					{
						super.onModifierStarted(pItem);

					}

					@Override
					protected void onModifierFinished(IEntity pItem)
					{
						super.onModifierFinished(pItem);
						unhappyFace.detachSelf();
						STATE = StateType.NEXT;
					}					
				};
				happyModifier = new RotationModifier(.76f, 0, 360)
				{
					@Override
					protected void onModifierStarted(IEntity pItem)
					{
						super.onModifierStarted(pItem);
					}

					@Override
					protected void onModifierFinished(IEntity pItem)
					{
						super.onModifierFinished(pItem);
						happyFace.detachSelf();

						STATE = StateType.NEXT;

					}
				};	
				happyModifier.setAutoUnregisterWhenFinished(true);
				unhappyModifier.setAutoUnregisterWhenFinished(true);

				currentC  = 0;
				STATE = StateType.INITIAL;
				Log.d("schack","started");
			}} );	

		if (ld.isRated)
			runningScore=myScore=(int) resourceManager.getRating();
		else {
			myScore = 0;
			runningScore = 0;
		}


		chessBoard.registerDragPieceListener(new DragCallBack_I() {

			@Override
			public void onDragStart(Sprite s) {

			}

			@Override
			public boolean onDragEnd(final Move m) {
				resourceManager.movePieceSound.play();
				Log.d("schack","user tries to move from ("+m.from.column+","+m.from.row+") to ("+m.to.column+","+m.to.row+")");											
				//NEW CODE
				GameState gs = ml.getCurrentPosition();
				gs.classifyMove(m);
				final GameState newState = gs.makeMove(m);
				if (newState == null)
					return false;
				STATE = StateType.WAITING;
				chessBoard.animateSideEffects(m,new MoveCallBack_I() {
					@Override
					public void onMoveDone() {
						Log.d("schack","Sideeffects done");
						//If promote, include promotepiece on board.
						if (m.moveType==Move.pawnPromoteF)
							newState.applyMove(m);
						//If there is a next move, user should do this move.
						moveCount++;
						if (ml.hasNext()) {
							ml.goForward();
							Log.d("schack","Found next in dragend, so moving forward");
							ml.getCurrentPosition().getPosition().print();			
							Move mm = ml.getCurrentPosition().getMove();
							if (!mm.equals(m)) {
								Log.d("schack","correct move was ("+mm.from.column+","+mm.from.row+") to ("+mm.to.column+","+mm.to.row+")");
								stateMessage = "Correct move was: "+mm.getShortNoFancy();
								STATE = StateType.WRONG_MOVE;
							} else 
								//move is correct. Move forward or go to next problem.
							{

								if (ml.hasNext()) {
									ml.goForward();
									STATE = StateType.COMPUTER_TURN;
									meter.score();



								} else {
									//last move in list
									Log.d("schack","This seems to be the last move");
									//go to next problem.
									if (sd.getType()==Type.Tactics) {
										STATE = StateType.CORRECT_MOVE;
									} else {
										Log.d("schack","Reached end of movelist. Let computer take over..");
										STATE = StateType.PONDER;
									}
								}
							}
						} else {
							//check if type Tactic. If so, something is wrong.
							if (sd.getType()==Type.Tactics) {
								Log.e("schack","Reached end of movelist, but user made a move!");
								STATE = StateType.NEXT;
							} else {
								//if not, just add the new gameState.
								ml.add(newState);
								//Check for end conditions.


								Log.d("schack","STATE set to Ponder");
								STATE = StateType.PONDER;


							}
						}			

					}
				});	
				//Wait for sideeffects, if any.





				return true;
			}
		});

	}


	private enum StateType {
		USER_TURN,
		COMPUTER_TURN,
		INITIAL,
		WAITING,
		NEXT,
		WRONG_MOVE,
		TIME_OUT,
		PONDER, 
		CORRECT_MOVE, 
		MOVE_COUNT_EXCEEDED
	}
	/*
	private final static int USER_TURN = 1;
	private final static int COMPUTER_TURN = 2;
	private final static int INITIAL = 3;
	private final static int WAITING = 4;
	private final static int NEXT = 5;
	private final static int WRONG_MOVE = 6;
	private final static int TIME_OUT = 7;
	private final static int PONDER = 8;
	 */


	/* (non-Javadoc)
	 * @see org.andengine.entity.scene.Scene#onManagedUpdate(float)
	 */


	final static Random r = new Random();

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		super.onManagedUpdate(pSecondsElapsed);

		if (PAUSED)
			return;

		switch (STATE) {

		case WAITING:
			break;

		case INITIAL: 

			STATE = StateType.WAITING;		
			chessBoard.allowDrag(false);
			alreadyQuitting = false;
			moveCount = 0;
			sd = ld.getStageDescriptor(currentC+1);
			ml = sd.getMoveList();
			String newbieMsg = resourceManager.getInitialMessage(sd.getType());
			if (newbieMsg !=null) {
				showMessage(newbieMsg);
				break;
			}

			if (meter!=null)
				meter.hide();

			if (sd.isTimed()) {
				Log.d("schack","This is timed...showing meter");
				meter = timedMeter;
				timedMeter.setVisible(true);
			}
			else
				meter = untimedMeter;

			meter.init();
			tick = 0;
			GameState gs = ml.getCurrentPosition();
			//reverse board if tactics.
			chessBoard.setupBoard(sd.getType().equals(Type.Tactics)?!gs.whiteToMove:sd.playerStarts==gs.whiteToMove,gs.getPosition());

			final Text popupTxt = new Text(0,0 , resourceManager.tacticPopupFont, sd.getProblemStatement()+
					"\nProlems Remaining: "+(ld.getNoOfProblems()-currentC), vbom);
			final Rectangle popbg = new Rectangle(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT+100,
					popupTxt.getWidth()+20,popupTxt.getHeight()+20,vbom);
			popupTxt.setPosition(popbg.getWidth()/2,popbg.getHeight()/2);
			popbg.setColor(0.22f,0.32f,0.435f);
			popbg.attachChild(popupTxt);
			//Display initial board.
			//Rectangle bg = new Rectangle(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2+20,)
			this.attachChild(popbg);		
			drop.reset();
			popbg.registerEntityModifier(drop);

			resourceManager.wooshSound.play();
			engine.registerUpdateHandler(new TimerHandler(2.5f, new ITimerCallback() 
			{
				public void onTimePassed(final TimerHandler pTimerHandler) 
				{					
					popbg.detachChildren();
					popbg.detachSelf();
					chessBoard.blinkColor(chessBoard.whiteDown()?ChessConstants.Color.white:ChessConstants.Color.black);
				}
			}));
			engine.registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() 
			{
				public void onTimePassed(final TimerHandler pTimerHandler) 
				{
					engine.unregisterUpdateHandler(pTimerHandler);
					ChallengeScene.this.detachChild(popupTxt);

					if (sd.playerStarts) {
						STATE = StateType.USER_TURN;
						chessBoard.allowDrag(true);
					}
					else {						
						ml.goForward();
						STATE = StateType.COMPUTER_TURN;
					}					
				}
			}));


			break;

		case PONDER:
			chessBoard.allowDrag(false);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					int result = ml.getCurrentPosition().checkForEndConditions();
					if (result!=GameResult.NORMAL) {
						if (result == GameResult.MATE) {
							Log.d("schack","Player won!");
							STATE = StateType.CORRECT_MOVE;
						}
						else {
							stateMessage = "STALE MATE";
							STATE = StateType.WRONG_MOVE;
						}

					} else {
						//If no moves left and no mate, game over!!!
						if (moveCount==sd.getMoveLimit()) {
							stateMessage = "OUT OF MOVES!";
							STATE = StateType.MOVE_COUNT_EXCEEDED;
						}
						else {

							GameState newState = ce.findBestMove(ml.getCurrentPosition());
							if (newState==null) {
								Log.d("schack","Computer found no move in ponder!!");
								STATE = StateType.NEXT;
							} else {
								ml.add(newState);
								Log.d("schack","State is now computer turn");
								STATE = StateType.COMPUTER_TURN;
							}
						}
					}
					if (STATE != StateType.COMPUTER_TURN)
						meter.showTemp();

				}});
			Log.d("schack","pondering move..");
			Log.d("schack","Drag is allowed? "+chessBoard.isDragAllowed());
			meter.showComputerThinking();
			STATE = StateType.WAITING;
			t.start();
			break;

		case COMPUTER_TURN:
			final GameState newState = ml.getCurrentPosition();
			final Move moveThatLeadHere = newState.getMove();		
			//Check if there is a move already. If not, it is likely beg. of game
			if (moveThatLeadHere == null) {
				STATE = StateType.PONDER;
				Log.d("schack","State: ponder");

			} else {

				Log.d("schack","State: waiting");
				STATE = StateType.WAITING;
				chessBoard.makeMove(moveThatLeadHere,new MoveCallBack_I() {			
					@Override
					public void onMoveDone() {
						meter.showTemp();
						Log.d("schack","move done. Now waiting for player input");
						int result = newState.checkForEndConditions();
						if (result!=GameResult.NORMAL) {
							if (result == GameResult.MATE) {
								Log.d("schack","Computer won!");
								stateMessage = "CHECK MATE";
								STATE = StateType.WRONG_MOVE;
							}
							else {
								Log.d("schack","Stale mate!");
								stateMessage = "STALE MATE";
								STATE = StateType.WRONG_MOVE;
							}

						}
						else if(newState.checkIfDraw(!newState.whiteToMove)) {
							Log.d("schack","NO WAY TO WIN");
							stateMessage = "NO PATH TO VICTORY";
							STATE = StateType.WRONG_MOVE;
						} else {
							STATE = StateType.USER_TURN;
							if (sd.getType().equals(Type.Tactics))
								tick = 0;
							chessBoard.allowDrag(true);
						}
					}
				});		
			}
			break;

		case USER_TURN:
			if (tick%60==0)
				meter.advance(tick/60);
			tick++;

			break;

		case NEXT:
			currentC++;
			if(failC>ld.getNoOfFailsAllowed()||currentC==ld.getNoOfProblems()) {

				//needed?
				chessBoard.setupBoard(true,new ChessPosition(smileyPos[r.nextInt(smileyPos.length)]));
				engine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
				{
					public void onTimePassed(final TimerHandler pTimerHandler) 
					{
						meter.greenSequence();
						myResult.setScore(myScore);

						Log.e("schack","GREEN SEQUENCE "+myResult.getGreenSequence());
						Log.e("schack","GREENS "+myResult.getGreens());
						resourceManager.setTacticResult(myResult);
						engine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().createScene(SceneType.SCENE_CHALLENGE_EXIT);
					}
				}));
				STATE = StateType.WAITING;
			} else
				STATE = StateType.INITIAL;		
			break;

		case TIME_OUT:
			stateMessage = "OUT OF TIME";
			STATE = StateType.WRONG_MOVE;
			break;

		case MOVE_COUNT_EXCEEDED:
			stateMessage = "OUT OF MOVES";
			STATE = StateType.WRONG_MOVE;
			break;
		case WRONG_MOVE:
			if (!alreadyQuitting) {
				alreadyQuitting = true;
				meter.onfail();
				if (stateMessage.length()>0) {
					final Text popupTxt2 = new Text(0,0 , resourceManager.tacticPopupFont, stateMessage, vbom);
					final Rectangle popbg2 = new Rectangle(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT+100,
							popupTxt2.getWidth()+20,popupTxt2.getHeight()+20,vbom);
					popupTxt2.setPosition(popbg2.getWidth()/2,popbg2.getHeight()/2);
					popbg2.setColor(0.22f,0.32f,0.435f);
					popbg2.attachChild(popupTxt2);
					//Display initial board.
					//Rectangle bg = new Rectangle(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2+20,)
					this.attachChild(popbg2);		
					drop2.reset();
					popbg2.registerEntityModifier(drop2);
					engine.registerUpdateHandler(new TimerHandler(1.5f, new ITimerCallback() 
					{
						public void onTimePassed(final TimerHandler pTimerHandler) 
						{					
							popbg2.detachChildren();
							popbg2.detachSelf();

						}
					}));
					stateMessage = "";
				}

				chessBoard.attachChild(unhappyFace);
				unhappyFace.registerEntityModifier(unhappyModifier);		
				unhappyModifier.reset();
				if (failC<3)
					angryHeads[failC].setVisible(true);
				failC++;
				STATE = StateType.WAITING;

			}
			break;
		case CORRECT_MOVE:
			chessBoard.attachChild(happyFace);
			happyFace.registerEntityModifier(happyModifier);	
			happyModifier.reset();
			STATE = StateType.WAITING;
			meter.score();
			//Any special bonus for completing this level?
			if (!ld.isRated)
				myScore += sd.bonus(meter.getBonusFactor());
			else
				meter.registerWin();
			resourceManager.registerSolved(sd.getProblemDifficulty());

			break;
		default:
			break;

		}
		if(runningScore!=myScore) {
			if (myScore-runningScore>100)
				runningScore+=(runningScore<myScore?5:-5);
			else
				runningScore+=(runningScore<myScore?1:-1);
		}

		if (gtick++%5==0)
			tacticScore.setText("Points: "+ runningScore+(sd!=null&&sd.hasMoveLimit()?"\nMoves Left: "+(sd.getMoveLimit()-moveCount):""));

	}

	private void showMessage(String initialMessage) {
		Scene childScene = new Scene();
		childScene.setBackgroundEnabled(false);
		childScene.setPosition(0,0);
		final Sprite popup = new Sprite(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2, 
				resourceManager.game_intro_popup_region, vbom);
		Text popupText = new Text(0, 0, resourceManager.smallRedFont, initialMessage,new TextOptions(HorizontalAlign.CENTER), vbom);
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
					STATE = StateType.INITIAL;
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

	private class NoTempMeter implements TempMeter_I {

		@Override
		public void init() {
			showTemp();		
		}

		@Override
		public void showComputerThinking() {
			pc_thinking.setVisible(true);
		}

		@Override
		public void showTemp() {
			pc_thinking.setVisible(false);
		}

		@Override
		public void advance(int i) {

		}

		@Override
		public void greenSequence() {
			lastGreen = false;
			if (myResult.getGreenSequence()<greenSequence)
				myResult.setGreenSequence(greenSequence);
		}

		@Override
		public void onfail() {
			resourceManager.booSound.play();
			myResult.setFails(myResult.getFails()+1);;
			if (lastGreen) 
				greenSequence();

		}

		@Override
		public void score() {
			myResult.setGreens(myResult.getGreens()+1);
			lastGreen=true;
			greenSequence++;						
			myScore+=50;
		}

		@Override
		public int getBonusFactor() {
			return moveCount;
		}

		@Override
		public void registerWin() {
			// TODO Auto-generated method stub

		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub

		}

	}

	private Sprite pc_thinking;
	private boolean lastGreen=false;
	private int greenSequence=0;
	public Rater rater=new Rater();

	private class Rater {

		ArrayList<Rating> winners=new ArrayList<Rating>(), losers=new ArrayList<Rating>();

		public void recalculate(boolean won, int oppRating) {
			Rating player = new Rating(resourceManager.getRating(),resourceManager.getRD());
			if (won)
				losers.add(new Rating(oppRating,1f));
			else
				winners.add(new Rating(oppRating,1f));

			ArrayList<Rating> all = new ArrayList<Rating>();
			all.addAll(winners);
			all.add(player);
			all.addAll(losers);
			Rating.calculateRatings(all);
			resourceManager.setRating((float)player.getRating());
			resourceManager.setRD((float)player.getRatingDeviation());

			Log.d("schack","recalculated rating to "+resourceManager.getRating()+" RD: "+resourceManager.getRD());
		}


	}

	private class TempMeter extends Entity implements TempMeter_I {
		private TiledSprite slidingFace;
		private Sprite tactics_termoPause,paused;
		private MoveXModifier sliderM;
		private MoveModifier scoreM;

		private float greenStart = resourceManager.happyface.getWidth()/2+15;
		private float greenEnd = 200;
		private float yellowEnd = 355;
		private float orangeEnd = 415;
		private float redEnd = 480;

		private int initial = 3;
		private int timeLeft = -1;
		private int initialTime;
		private int speedBonus = 0;
		private boolean stopClock,pauseAllowed;

		public TempMeter() {
			Log.d("schack","TEMP METER CREATED");

			paused = new Sprite(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT+200,resourceManager.paused_region,vbom) {
				@Override
				public boolean onAreaTouched(
						TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					unregisterTouchArea(paused);
					paused.registerEntityModifier(new MoveYModifier(1.5f, ActivityGame.CAMERA_HEIGHT/2-16, ActivityGame.CAMERA_HEIGHT+100,new IEntityModifierListener() {

						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
							resourceManager.wooshSound.play();

						}

						@Override
						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
							chessBoard.setVisible(true);
							PAUSED = false;						
							registerTouchArea(tactics_termoPause);


						}
					}));
					return super
							.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}

			};

			tactics_termoPause = new Sprite(0,tpy,
					resourceManager.tactics_termoPause,vbom) {
				@Override
				public boolean onAreaTouched(
						TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					unregisterTouchArea(tactics_termoPause);
					paused.registerEntityModifier(new MoveYModifier(.5f, ActivityGame.CAMERA_HEIGHT+100, ActivityGame.CAMERA_HEIGHT/2-16, new IEntityModifierListener() {

						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						}

						@Override
						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
							registerTouchArea(paused);
						}
					}));
					resourceManager.wooshSound.play();

					chessBoard.setVisible(false);
					PAUSED = true;
					slidingFace.unregisterEntityModifier(sliderM);
					sliderM = null;
					return super
							.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};

			tactics_termoPause.setVisible(true);
			slidingFace = new TiledSprite(-500,tactics_termoPause.getY(),resourceManager.tiledfaces,vbom);
			tactics_termoPause.setWidth(ActivityGame.CAMERA_WIDTH);
			tactics_termoPause.setX(ActivityGame.CAMERA_WIDTH/2);	
			slidingFace.setVisible(false);


			attachChild(tactics_termoPause);
			attachChild(slidingFace);
			attachChild(paused);
		}

		public void showComputerThinking() {
			tactics_termoPause.setVisible(false);
			slidingFace.setVisible(false);
			pc_thinking.setVisible(true);
			stopClock = true;
			slidingFace.unregisterEntityModifier(sliderM);
			sliderM = null;
		}

		public void showTemp() {
			pc_thinking.setVisible(false);
			tactics_termoPause.setVisible(true);			
			stopClock = false;
		}




		public void init() {
			Log.d("schack","Meter init");
			stopClock = false;
			initialTime = initial+sd.getTimeG()+sd.getTimeY()+sd.getTimeR();
			pc_thinking.setVisible(false);			
			resetFace();
			showTemp();
			timeLeft = initialTime;
			sliderM = null;
			speedBonus = 0;
		}

		private void resetFace() {
			Log.d("schack","resetFace called.");
			slidingFace.setVisible(false);
			slidingFace.setCurrentTileIndex(0);
			slidingFace.clearEntityModifiers();
			slidingFace.setPosition(greenStart,tactics_termoPause.getY());
		}

		public void score() {
			Log.d("schack","Scoring...");
			stopTick();

			slidingFace.unregisterEntityModifier(sliderM);
			sliderM = null;
			if (!ld.isRated) {
				scoreM = new MoveModifier(.5f,slidingFace.getX(),slidingFace.getY(),tacticScore.getX(),tacticScore.getY(), new IEntityModifierListener() {


					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						int tot = ml.size()/2;
						myScore+=(timeLeft*2)/tot;
						resourceManager.correctMove.play();
					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						Log.d("schack","scoreM modifier done.");
						engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								resetFace();	
							}
						});

					}
				});
				scoreM.setAutoUnregisterWhenFinished(true);
				slidingFace.registerEntityModifier(scoreM);
			} else {
				resourceManager.correctMove.play();
				resetFace();					
			}

			int timeSpent = initialTime - timeLeft;

			//TODO Is 2 a good time??
			if (timeSpent < 2) {
				resourceManager.wowSound.play();
				myResult.setWoos(myResult.getWoos()+1);
				speedBonus = 200;
			}
			if (timeSpent < sd.getTimeG()) {
				myResult.setGreens(myResult.getGreens()+1);
				lastGreen=true;
				greenSequence++;
				speedBonus = 100;
			} else {
				greenSequence();
				if(timeSpent < sd.getTimeY()) {
					speedBonus = 50;
					myResult.setYellows(myResult.getYellows()+1);		
				}
				else
					myResult.setReds(myResult.getReds()+1);;
			}


		}
		public void onfail() {
			stopTick();
			resourceManager.booSound.play();
			myResult.setFails(myResult.getFails()+1);;
			if (lastGreen) 
				greenSequence();
			slidingFace.setVisible(false);
			speedBonus = 0;
			if (ld.isRated) {
				rater.recalculate(false, sd.getProblemDifficulty());
				myScore = (int)resourceManager.getRating();
				Log.d("schack","Rating equal to "+resourceManager.getRating());
			}
		}


		public void greenSequence() {
			lastGreen = false;
			if (myResult.getGreenSequence()<greenSequence)
				myResult.setGreenSequence(greenSequence);
		}

		private int getTimeSpent() {
			return initialTime - timeLeft;
		}

		public void advance(int tick) {
			if (stopClock)
				return;
			if (!pauseAllowed) {
				registerTouchArea(tactics_termoPause);
				pauseAllowed = true;
			}
			if (tick>=initial) {
				if (sliderM==null||sliderM.isFinished()) {
					Log.e("schack","SliderM was null or finished. Removing and creating new.");
					if (tick<initial+sd.getTimeG()) {
						sliderM = new MoveXModifier((initial+sd.getTimeG())-tick,slidingFace.getX(),greenEnd);
						slidingFace.setCurrentTileIndex(0);
						Log.d("schack","1");
					}
					else if (tick<initial+sd.getTimeG()+sd.getTimeY()/2) {
						sliderM = new MoveXModifier((initial+sd.getTimeG()+sd.getTimeY()/2)-tick,slidingFace.getX(),yellowEnd);
						slidingFace.setCurrentTileIndex(1);	
						Log.d("schack","2");
					}
					else if (tick<initial+sd.getTimeG()+sd.getTimeY()) {
						sliderM = new MoveXModifier((initial+sd.getTimeG()+sd.getTimeY())-tick,slidingFace.getX(),orangeEnd);
						slidingFace.setCurrentTileIndex(2);	
						Log.d("schack","3");
					}
					else {
						Log.d("schack","4");
						sliderM = new MoveXModifier((initial+sd.getTimeG()+sd.getTimeY()+sd.getTimeR())-tick,slidingFace.getX(),redEnd,
								new IEntityModifierListener() {

							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								Log.d("schack","Modifier slider end started!");
							}

							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										STATE = StateType.TIME_OUT;
										Log.d("schack","TimeOUT!!!!!");							
									}
								});

							}
						});
						slidingFace.setCurrentTileIndex(3);	
					}
					sliderM.setAutoUnregisterWhenFinished(true);
					sliderM.reset();
					slidingFace.registerEntityModifier(sliderM);

				} else
					Log.d("schack","Slider is "+((sliderM==null)?"NULL":"Not finished"));
				if (!slidingFace.isVisible())
					slidingFace.setVisible(true);
			}

			timeLeft--;
			Log.d("schack", "time left: "+timeLeft+" heartbeatlength: "+heartBeatLength);
			if (timeLeft == heartBeatLength) {
				resourceManager.heartSound.play();
				Log.d("schack","Playing hearbeat!");
				heartPlaying = true;
			}
		}

		private void stopTick() {
			if (heartPlaying) {
				heartPlaying = false;
				resourceManager.heartSound.stop();
			}
			pauseAllowed = false;
			unregisterTouchArea(tactics_termoPause);
			stopClock = true;
		}

		@Override
		public int getBonusFactor() {
			return getTimeSpent();
		}

		@Override
		public void registerWin() {
			rater.recalculate(true,sd.getProblemDifficulty()+speedBonus);
			myScore = (int)resourceManager.getRating();

		}

		@Override
		public void hide() {
			tactics_termoPause.setVisible(false);
		}

	}

	@Override
	public void onBackKeyPressed() {
		if (STATE== StateType.WAITING)
			quit();
		if (STATE!=StateType.INITIAL) {
			if (STATE == StateType.PONDER)
				ce.cancel();
			quit();
		}
	}

	private void quit() {
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				SceneManager.getInstance().createScene(SceneType.SCENE_MENU);					
			}
		});
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_TACTICS;
	}

	@Override
	public void disposeScene() {
		STATE = StateType.WAITING;
		ResourceManager.getInstance().unloadTacticsResources();
		unregisterEntityModifiers(new IEntityModifierMatcher() {

			@Override
			public boolean matches(IModifier<IEntity> pItem) {
				return true;
			}
		});
		unregisterTouchAreas(new ITouchAreaMatcher() {

			@Override
			public boolean matches(ITouchArea pItem) {
				return true;
			}
		});

		this.detachChildren();
		detachSelf();

	}




}
