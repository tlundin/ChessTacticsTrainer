package com.terje.chesstacticstrainer_full;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.terje.chesstacticstrainer_full.Types.Cord;


public class ChessPiece extends Sprite {

	final ChessBoard mother;
	float startX=0, startY=0;
	protected int intType;		
	private boolean dragStarted;
	private float boardTop;

	public static ChessPiece createPiece(int type,ChessBoard mother) {
		return new ChessPiece(0,0,
				getTexture(type),ResourceManager.getInstance().vbom,mother,type);		
	}


	public int getType() {
		return intType;
	}


	private ChessPiece(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, ChessBoard mother, int type) {
		super(pX, pY, pTextureRegion, vbom);
		this.mother = mother;
		intType = type;
		mother.mScene.registerTouchArea(this);
		boardTop = ActivityGame.CAMERA_HEIGHT-mother.fromTop;

	}

	float difx,dify;
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (mother.isDragAllowed()){
			switch(pSceneTouchEvent.getAction()) {	
			case TouchEvent.ACTION_DOWN:
				if (!dragStarted) {
					dragStarted = true;
					this.setScale(2.0f);		
					this.setAlpha(.5f);
					startX = this.getX();
					startY = this.getY();
					difx=startX-pSceneTouchEvent.getX();
					dify=startY-pSceneTouchEvent.getY();
					Log.d("schack","Drag start! ");
					mother.onPieceDragStart(this);
				}
				break;		
			case TouchEvent.ACTION_UP:	
				if (dragStarted) {		
					mother.allowDrag(false);
					mother.removeHighlight();
					this.setScale(1.0f);
					this.setAlpha(1.0f);		     
					int column = (int)(this.getX()/ChessBoard.Square_Width);
					int row = (int)((boardTop-this.getY())/ChessBoard.Square_Height);
					Log.d("schack","Drag end. Column, Row: ("+column+","+row+")");
					if (column >= 0 && column < 8 && row >= 0 && row < 8 ) {
						Cord start = new Cord((int)(startX/ChessBoard.Square_Width),(int)((ActivityGame.CAMERA_HEIGHT-startY-mother.fromTop)/ChessBoard.Square_Height));
						if (start.column == column && start.row == row) {
							Log.d("schack","still in start position");
							this.setPosition(startX, startY);
						} else {					
							//snap to grid
							float x = column*ChessBoard.Square_Width+ChessBoard.Square_Width/2;
							float y = boardTop-row*ChessBoard.Square_Height-ChessBoard.Square_Height/2;
							this.setPosition(x,y);
							Cord end = new Cord(column,row);
							if (!mother.whiteDown()) {
								turnCords(start,end);
								Log.d("schack","Turned cords. Start:"+start.column+" "+start.row+" END: "+end.column+" "+end.row);
							} else
								Log.d("schack"," NOT Turned cords. Start:"+start.column+" "+start.row+" END: "+end.column+" "+end.row);

							Move m = new Move(start, end);
							if(!mother.onPieceDragEnd(m)) {
								Log.d("schack","move was refused by listener");
								this.setPosition(startX, startY);
							}


						}
					} else {
						Log.d("schack","outside board...: (x,y): "+column+" "+row);
						this.setPosition(startX, startY);
					}
					dragStarted = false;
					mother.allowDrag(true);
				}
				break;	

			case TouchEvent.ACTION_MOVE:
				if (dragStarted) {
					this.setPosition(pSceneTouchEvent.getX() + difx, pSceneTouchEvent.getY()+dify);
					int column = (int)(this.getX()/ChessBoard.Square_Width);
					int row = (int)((boardTop-this.getY())/ChessBoard.Square_Height);
					if (column >= 0 && column < 8 && row >= 0 && row < 8)
						mother.highlightSquare(column,row);


				}
				break;	
			}
		} else
			Log.d("schack","drag was refused");
		return true;
		//return super
		//		.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	private void turnCords(Cord start, Cord end) {
		start.column = 7- start.column;
		start.row = 7-start.row;
		end.column = 7- end.column;
		end.row = 7- end.row;
	}


	public static ITextureRegion getTexture(int pieceType) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		ITextureRegion t=null;
		switch (pieceType) {
		case ChessConstants.B_PAWN:
			t = resourceManager.black_pawn_region;
			break;
		case ChessConstants.W_PAWN:
			t = resourceManager.white_pawn_region;
			break;
		case ChessConstants.B_KING:
			t = resourceManager.black_king_region;
			break;
		case ChessConstants.W_KING:
			t = resourceManager.white_king_region;
			break;
		case ChessConstants.B_QUEEN:
			t = resourceManager.black_queen_region;
			break;			
		case ChessConstants.W_QUEEN:
			t = resourceManager.white_queen_region;
			break;			
		case ChessConstants.B_BISHOP:
			t = resourceManager.black_bishop_region;
			break;
		case ChessConstants.W_BISHOP:
			t = resourceManager.white_bishop_region;
			break;
		case ChessConstants.B_KNIGHT:
			t = resourceManager.black_knight_region;
			break;
		case ChessConstants.W_KNIGHT:
			t = resourceManager.white_knight_region;
			break;
		case ChessConstants.B_ROOK:
			t = resourceManager.black_rook_region;
			break;
		case ChessConstants.W_ROOK:
			t = resourceManager.white_rook_region;
			break;	
		default:
			Log.e("schack","Did not recognize piece type "+pieceType);
			break;
		}
		return t;
	}

	public char convertToChar(int t) {
		char c = 'x';
		switch(t) {
		case ChessConstants.B_PAWN:
			c = 'p';
			break;
		case ChessConstants.W_PAWN:
			c = 'P';
			break;
		case ChessConstants.B_KING:
			c = 'k';
			break;
		case ChessConstants.W_KING:
			c='K';
			break;
		case ChessConstants.B_QUEEN:
			c='q';
			break;			
		case ChessConstants.W_QUEEN:
			c='Q';
			break;			
		case ChessConstants.B_BISHOP:
			c='b';
			break;
		case ChessConstants.W_BISHOP:
			c='B';
			break;
		case ChessConstants.B_KNIGHT:
			c='n';
			break;
		case ChessConstants.W_KNIGHT:
			c='N';
			break;
		case ChessConstants.B_ROOK:
			c='r';
			break;
		case ChessConstants.W_ROOK:
			c='R';
			break;	
		}		
		return c;
	}
}
