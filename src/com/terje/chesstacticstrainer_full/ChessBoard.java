package com.terje.chesstacticstrainer_full;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseQuartOut;

import android.util.Log;

public class ChessBoard extends Entity {


	private static boolean whiteDown=true;
	private static ChessPosition mPos=null;
	private static ChessPiece[][] mPieceArray;
	private static PieceBox mPieceBox;
	private static ResourceManager rm;
	public static Scene mScene;
	private final static float Piece_Width = 46;
	public final static float  Square_Width = 62.5f;
	public final static float	Square_Height = Square_Width;
	private static int dc = 0;
	private static Sprite dShadow,lShadow;
	public static int fromTop=0;
	private static boolean dragAllowed=false;


	public ChessBoard(Scene scene,int fromTop) {
		super();
		init(true,scene,fromTop);
	}

	public ChessBoard(boolean whiteDown, ChessPosition pos, Scene scene) {
		super();	
		init(whiteDown,scene,0);	
		erect(pos);
	}

	public void setupBoard(boolean whiteDown, ChessPosition pos) {
		Random r = new Random();
		ChessBoard.whiteDown = whiteDown;
		cleanBoard();
		Log.d("schack","DC after clean: "+dc);
		if (r.nextBoolean())
			erectFancy(pos);	
		else 
			erect(pos);
	}


	private void init(boolean whiteDown, Scene scene, int fromTop) {
		ChessBoard.whiteDown = whiteDown;
		mScene = scene;
		rm = ResourceManager.getInstance();
		mPieceBox = new PieceBox();	
		mPieceArray = new ChessPiece[8][8];
		Log.d("schack","whitedown is "+whiteDown);
//		this.setSize(rm.chessboard_region.getWidth(), rm.chessboard_region.getHeight());
		Log.d("schack","my size: "+this.getWidth()+" "+this.getHeight());
//		Sprite chessBSprite = new Sprite(0,0,rm.chessboard_region,rm.vbom);
	
		this.attachChild(new Sprite(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT- rm.chessboard_region.getHeight()/2-fromTop, rm.chessboard_region, rm.vbom));
		dShadow = new Sprite(ActivityGame.CAMERA_WIDTH,ActivityGame.CAMERA_HEIGHT/2,rm.d_highlight_square,rm.vbom);
		lShadow = new Sprite(0,0,rm.l_highlight_square,rm.vbom);
		dShadow.setVisible(false);
		lShadow.setVisible(false);
		attachChild(dShadow);
		attachChild(lShadow);
		
		this.fromTop = fromTop;
	}

	private void erectFancy(ChessPosition pos) {
		int tot = 0;
		mPos = pos;
		final int OUTSIDE_D = 200;
		Random r = new Random();
		MoveModifier e;
		Log.e("schack","Attaching pieces");
		ChessPiece piece;
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				if (mPos.isEmpty(column,row))
					continue;
				piece = mPieceBox.getPiece(mPos.get(column, row));
				RealCord end = project(column,row);
				RealCord start = new RealCord();
				if(r.nextBoolean())
					if(column<4) 
						start.x = ActivityGame.CAMERA_WIDTH+OUTSIDE_D+column*Square_Width;
					else
						start.x = -OUTSIDE_D-column*Square_Width;
				else
					start.x=r.nextInt(ActivityGame.CAMERA_WIDTH);
				if(r.nextBoolean())
					if(row<4) 
						start.y = ActivityGame.CAMERA_HEIGHT+OUTSIDE_D+column*Square_Height;
					else
						start.y = -OUTSIDE_D-column*Square_Height;
				else
					start.y=r.nextInt(ActivityGame.CAMERA_HEIGHT);				
				e = new MoveModifier(r.nextFloat()*2+1.0f,start.x,start.y,end.x,end.y,EaseQuartOut.getInstance());
				e.setAutoUnregisterWhenFinished(true);
				piece.registerEntityModifier(e);
				attachChild(piece);
				mPieceArray[column][row]=piece;
			}
		}
		Log.d("schack","Added in total "+tot+" pieces");
	}

	private void erect(ChessPosition pos) {
		mPos = pos;
		Log.e("schack","Attaching pieces");
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				addPiece(column,row);
			}
		}
	}
	
	public void blinkColor(ChessConstants.Color c) {
		ChessPiece p;
		SequenceEntityModifier shake = new SequenceEntityModifier(
				new RotationByModifier(.10f, 15),
				new RotationByModifier(.20f, -30),
				new RotationByModifier(.10f, 15));
		shake.setAutoUnregisterWhenFinished(true);
		
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				p = mPieceArray[column][row];
				if (p!=null) 
					if (ChessConstants.isWhite(p.getType())==(c.equals(ChessConstants.Color.white)))
						p.registerEntityModifier(shake.deepCopy()); 
			}
		}
	}

	private void cleanBoard() {
		ChessPiece p;
		int tot = 0;
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				p = mPieceArray[column][row];
				if (p!=null) {
					mPieceBox.returnPiece(p);
					mPieceArray[column][row]=null;
					tot++;
				}			
			}
		}
		Log.d("schack","Removed in total "+tot+" pieces");
	}

	private void addPiece(int column, int row) {
		if (mPos.isEmpty(column,row))
			return;
		final ChessPiece piece = mPieceBox.getPiece(mPos.get(column, row));
		RealCord rc = project(column,row);
		piece.setPosition(rc.x,rc.y);
/*		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {

				if (!piece.hasParent()) {
					ChessBoard.this.attachChild(piece);
					Log.d("schack","DC: "+(++dc));
				}
				else
					Log.e("schack","piece "+ChessConstants.pieceNames[piece.getType()]+" was attached");
			}
		});	
*/
		attachChild(piece);
		mPieceArray[column][row]=piece;
		
	}


	private void removePiece(int column, int row) {
		if (mPieceArray[column][row]!=null)
			mPieceBox.returnPiece(mPieceArray[column][row]);
		else
			Log.e("schack","missing piece at "+column+","+row+" in removePiece");
		mPieceArray[column][row]=null;
		mPos.put(column, row, ChessConstants.B_EMPTY);
	}


	private int turn(int w) {
		return !whiteDown?7-w:w;
	}

	public RealCord project(int column,int row) {
		RealCord rc = new RealCord();
		rc.x = turn(column)*Square_Width+Square_Width/2;
		rc.y = ActivityGame.CAMERA_HEIGHT-turn(row)*Square_Height-Square_Height/2-fromTop-5;
		return rc;
	}


	public class RealCord {
		public float x,y;
	}



	public class PieceBox { 
		Map<Integer, Set<ChessPiece>> piecebox;
		private final char[] types = {'p','P','b','B','q','Q','k','K','r','R','n','N'};
		private final int[] amounts = {8,8,3,3,3,3,3,3,3,3,3,3}; 

		public PieceBox() {
			piecebox = new HashMap<Integer, Set<ChessPiece>>();
			Set<ChessPiece> s;	
			for (int i=0;i<types.length;i++) {

				s = createSet(ChessConstants.convertToInt(types[i]),amounts[i]);
				piecebox.put(ChessConstants.convertToInt(types[i]),s);
			}
		}

		private Set<ChessPiece> createSet(int type, int n) {
			Set<ChessPiece> s = new HashSet<ChessPiece>();

			for (int i = 0;i<n;i++) 
				s.add(ChessPiece.createPiece(type,ChessBoard.this));
			return s;
		}
		
		

		public ChessPiece getPiece(int type) {
			Set<ChessPiece> s = piecebox.get(type);
			ChessPiece p;
			if (true) {
//			if (s.isEmpty()) {
				//create new. 
				p=ChessPiece.createPiece(type,ChessBoard.this);
			}
			else {
				//TODO: find a solution here...
				p = s.iterator().next();
				s.remove(p);
				
			}
			return p;
		}
		
		
		public void returnPiece(final ChessPiece p) {
			ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					if (!p.hasParent()) {
						Log.e("schack","Piece "+ChessConstants.pieceNames[p.getType()]+ " is not yet attached!");
					}
					//Log.d("schack","DC: "+--dc);
					Log.d("schack","Detatching piece: "+ChessConstants.pieceNames[p.getType()]);
					p.detachSelf();
					mScene.unregisterTouchArea(p);
					//Do not add to box until detached.
					//Set<ChessPiece> s = piecebox.get(p.getType());
					//s.add(p);		
					//Log.d("schack","Box of "+ChessConstants.pieceNames[p.getIntType()]+" now has "+s.size()+" members");
				}
			});


		}
	}

	static IEntityModifier mover;

	public void makeMove(final Move m,final MoveCallBack_I done) {

		float moveDuration = .75f;
		final ChessPiece p = mPieceArray[m.getFromX()][m.getFromY()];
		RealCord from,to;
		if (p==null) {
			Log.d("schack", "Piece was null with Move from: "+m.getFromX()+","+m.getFromY()+" to: "+m.getToX()+","+m.getToY());
			Log.d("schack", "piece was: "+ChessConstants.pieceNames[m.pieceId]);
			return;
		}
		Log.d("schack","Piece: "+ChessConstants.pieceNames[p.getType()]);
		IEntityModifierListener listener = new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				Log.d("schack","On mod started..");
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				Log.d("schack","Made move..");
				registerMove(m);
				
				animateSideEffects(m,new MoveCallBack_I() {
					@Override
					public void onMoveDone() {
						Log.d("schack","Sideffects done..");
						done.onMoveDone();
					}
				});
	
			}

		};
			from = project (m.from.column,m.from.row);
			to = project (m.to.column,m.to.row);
			if (ChessConstants.isKnight(p.getType()))
			{
				RealCord inb;
				CardinalSplineMoveModifierConfig conf = new CardinalSplineMoveModifierConfig(4,0f);

				inb = (Math.abs(m.from.column-m.to.column)>Math.abs(m.from.row-m.to.row))?
						project(m.to.column,m.from.row):project(m.from.column,m.to.row);
						conf.setControlPoint(0, from.x, from.y);
						conf.setControlPoint(1, from.x, from.y);
						conf.setControlPoint(2, inb.x, inb.y);
						conf.setControlPoint(3, to.x, to.y);	

						mover = new CardinalSplineMoveModifier(moveDuration,conf,listener);
						
						detachChild(p);
						attachChild(p);
			} else 
				mover = new MoveModifier(moveDuration,from.x,from.y,to.x, to.y,listener,
						org.andengine.util.modifier.ease.EaseSineOut.getInstance());

			p.registerEntityModifier(mover);
			Log.d("schack","Registered mover");
			final ChessPiece targetP = mPieceArray[m.to.column][m.to.row];
			if (targetP!=null) 
				p.registerUpdateHandler(new IUpdateHandler() {			
					@Override
					public void reset() {
					}				
					@Override
					public void onUpdate(float pSecondsElapsed) {
						if (p.collidesWith(targetP)) {
							mPieceBox.returnPiece(targetP);	
							p.unregisterUpdateHandler(this);
						}
					}
				});
				
			
			mover.setAutoUnregisterWhenFinished(true);
	}
	

	public void animateSideEffects(Move m,final MoveCallBack_I done) {
		switch (m.moveType) {
		case Move.normalF:
		case Move.twoStepPawnF:
			Log.d("schack", "normal Move");
			done.onMoveDone();
			break;
		case Move.enPassantF:
			Log.d("schack", "enpassant Move");
			removePiece(m.getToX(), m.getFromY());
			done.onMoveDone();
			break;
		case Move.pawnPromoteF:
			Log.d("schack","Promote move");
			if(m.promotePiece==ChessConstants.B_EMPTY) {
				createPromotePieceDialog(m,new MoveCallBack_I(){

					@Override
					public void onMoveDone() {
						Log.d("schack","Promote move on movedone1");
						done.onMoveDone();
					}});
			}
			else {
				removePiece(m.getToX(),m.getToY());
				mPos.put(m.getToX(), m.getToY(), m.promotePiece);
				addPiece(m.getToX(),m.getToY());
				
				Log.d("schack","Promote move on movedone2");
				done.onMoveDone();
			}
			break;
		case Move.exchangeF:
			System.out.println("exchange");
			Move mCastling = new Move();
			if (m.getToX() > m.getFromX())
				mCastling.set(7, m.getToY(), 5, m.getToY());
			else
				mCastling.set(0, m.getToY(), 3, m.getToY());
				makeMove(mCastling,new MoveCallBack_I() {
					
					@Override
					public void onMoveDone() {
						done.onMoveDone();
					}
				});
			break;
		}		
		
	}
	
	final int[] bPieces = {ChessConstants.B_BISHOP,ChessConstants.B_QUEEN,ChessConstants.B_ROOK,ChessConstants.B_KNIGHT};
	final int[] wPieces = {ChessConstants.W_BISHOP,ChessConstants.W_QUEEN,ChessConstants.W_ROOK,ChessConstants.W_KNIGHT};
	Rectangle promoteRect;
	final Sprite[] promoPieces = new Sprite[bPieces.length];
	float promoteW=0;
	float scale=2;

	private void createPromotePieceDialog(final Move m, final MoveCallBack_I done) {
		

    	final LoopEntityModifier blinkModifier = new LoopEntityModifier(
	    	    new SequenceEntityModifier(new FadeOutModifier(0.5f), new FadeInModifier(0.5f)));

		
		promoteW = ResourceManager.getInstance().black_queen_region.getWidth()*scale*bPieces.length;
		int borderW = 20;
//		Rectangle r = new Rectangle(ActivityGame.CAMERA_WIDTH/2-w/2-borderW,ActivityGame.CAMERA_HEIGHT/2+promoPieces[0].getHeight()/2+borderW,
//				ActivityGame.CAMERA_WIDTH/2+w/2+borderW,ActivityGame.CAMERA_HEIGHT/2-promoPieces[0].getHeight()/2-borderW,ResourceManager.getInstance().vbom);
		for(int i=0;i<bPieces.length;i++) {
			final int mType = ChessConstants.isWhite(m.pieceId)?wPieces[i]:bPieces[i];
			promoPieces[i] = new Sprite(0,0,ChessPiece.getTexture(mType),ResourceManager.getInstance().vbom) {			
				final int myType = mType;
				boolean pressed = false;
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (!pressed) {
					pressed = true;
					m.promotePiece=myType;
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							Log.d("schack","gets here (run)..");
							ChessBoard.this.detachChild(promoteRect);
							promoteRect.unregisterEntityModifier(blinkModifier);
							for(int i=0;i<bPieces.length;i++) 								
								ChessBoard.this.detachChild(promoPieces[i]);
							
							done.onMoveDone();						
						}
					});
					Log.d("schack","gets here (ontouch)..");
					for(int i=0;i<bPieces.length;i++) 
						mScene.unregisterTouchArea(promoPieces[i]);
					removePiece(m.getToX(),m.getToY());
					mPos.put(m.to.column, m.to.row, m.promotePiece);
					addPiece(m.to.column,m.to.row);
					}
					return true;
					
				}
			};
			promoPieces[i].setScale(scale);
			promoPieces[i].setPosition(ActivityGame.CAMERA_WIDTH/2-promoteW/2+promoPieces[0].getWidth()*scale/2+i*promoPieces[i].getWidth()*scale, ActivityGame.CAMERA_HEIGHT/2);
			mScene.registerTouchArea(promoPieces[i]);
		}
		promoteRect = new Rectangle(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2,
		promoteW+borderW,promoPieces[0].getHeight()*scale+borderW,ResourceManager.getInstance().vbom);
		promoteRect.setColor(0.22f,0.32f,0.435f);
		promoteRect.registerEntityModifier(blinkModifier);
		attachChild(promoteRect);
		for(int i=0;i<bPieces.length;i++) 
			attachChild(promoPieces[i]);
			
	}

	private void registerMove(Move m) {
		Log.d("schack","register move called for "+ChessConstants.pieceNames[m.pieceId]);
		mPieceArray[m.to.column][m.to.row]=mPieceArray[m.from.column][m.from.row];
		Log.d("schack","Position "+m.to.column+","+m.to.row+" now has "+mPieceArray[m.to.column][m.to.row]);
		mPieceArray[m.from.column][m.from.row]=null;
	}

	public void registerDragPieceListener(DragCallBack_I dragCB) {
		Log.d("schack","Registering Drag Listener");
		this.dragCB = dragCB;
	}


	public void allowDrag(boolean b) {
		dragAllowed = b;
	}
	
	DragCallBack_I dragCB = null;

	public void onPieceDragStart(ChessPiece chessPieceSprite) {
		if (dragCB !=null)
			dragCB.onDragStart(chessPieceSprite);
	}

	public boolean onPieceDragEnd(Move m) {

		//If a listener refuses the move, cancel the move.
		if (dragCB!= null) {
			if(!dragCB.onDragEnd(m))
				return false;
		}
		final ChessPiece targetP = mPieceArray[m.to.column][m.to.row];
		if (targetP!=null) 
			mPieceBox.returnPiece(targetP);	
			
		this.registerMove(m);
		return true;
	}
	
	public boolean whiteDown() {
		return whiteDown;
	}

	private int currC=-1,currR=-1;
	boolean lShadowisA=false,dShadowisA=false;

	public void highlightSquare(int column, int row) {
		if (column == currC && row == currR)
			return;
		currC = column;
		currR = row;
		float x = column*ChessBoard.Square_Width+ChessBoard.Square_Width/2;
		float y = ActivityGame.CAMERA_HEIGHT-row*Square_Height-Square_Height/2-fromTop;

		if (!((column%2==0&&row%2==0) || (column%2!=0&&row%2!=0))) {
			detachLShadow();
			if (!dShadowisA) {
			dShadow.setVisible(true);
			dShadowisA = true;
			}
			dShadow.setPosition(x, y);
		} else {
			detachDShadow();
			if (!lShadowisA) {
			lShadow.setVisible(true);
			lShadowisA = true;
			}
			lShadow.setPosition(x, y);
		}
		
	}
	
	public void removeHighlight() {
		detachLShadow();
		detachDShadow();
	}


	private void detachDShadow() {
		if (dShadowisA) {
			dShadow.setVisible(false);
			dShadowisA=false;
		}
	}

	private void detachLShadow() {
		if (lShadowisA) {
			lShadow.setVisible(false);
			lShadowisA=false;
		}
	}
	
	public boolean isDragAllowed() {
		return dragAllowed;
	}





}
