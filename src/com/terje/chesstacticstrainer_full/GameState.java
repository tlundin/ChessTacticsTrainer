package com.terje.chesstacticstrainer_full;

import java.util.ArrayList;
import java.util.List;

import com.terje.chesstacticstrainer_full.Types.Cord;



//STILL TO DO: CASTLING LEFT RIGHT.

public class GameState {

	private ChessPosition pos; 
	public int fiftyDrawCounter;
	public String whiteOkToCastle, blackOkToCastle;
	public boolean whiteToMove;
	private Move theMoveThatLeadToThisPosition = null;
	private Position whiteKingPos, blackKingPos, checkPiecePos;
	private final static BitBoardStore bb = BitBoardStore.bb;
	private Position enPassantSquare = null;

	// this is only for the initial first position;

	public GameState(GameState gs, Move m) {
		copyCurrentPos(gs);
		applyMove(m);
	}


	public GameState(final int[][] position, boolean wtm) {
		pos = new ChessPosition(position);
		whiteOkToCastle = "KQ";
		blackOkToCastle = "kq";
		// finds the kings and changes castling status:
		findKings();
		theMoveThatLeadToThisPosition = null;
		whiteToMove = wtm;
	}

	private void copyCurrentPos(GameState cp) {
		pos = new ChessPosition(cp.getPosition().getBoard());
		whiteToMove = !cp.whiteToMove;
		whiteOkToCastle = cp.whiteOkToCastle;
		blackOkToCastle = cp.blackOkToCastle;
		// the king pos is the same if not changed later on by move.
		blackKingPos = cp.blackKingPos;
		whiteKingPos = cp.whiteKingPos;
		checkPiecePos = null;
	}

	public void setMove(Move m) {
		theMoveThatLeadToThisPosition = m;
	}

	/*
	 * public String findDiff(String FEN) { String myFen = calcFen(); if (myFen ==
	 * null || FEN == null) { System.out.println("couldn't calc fen in
	 * SetMove!"); return null; } //Prepare by replacing all numbers with " "
	 * myFen = replaceNums(myFen);
	 * 
	 * int i=0; char m,n; while (i<myFen.length()) { m = myFen.charAt(i); n =
	 * FEN.charAt(i); if (m!=n) { System.out.println("Found diff at: "+i); if
	 * ("rnbqkp".indexOf(n)>=0) System.out.println("black piece moved!"); } } }
	 */
	public void setKingPosition(boolean color, Position p) {
		if (color)
			whiteKingPos = p;
		else
			blackKingPos = p;
	}

	public Move getMove() {
		return theMoveThatLeadToThisPosition;
	}

	public ChessPosition getPosition() {
		return pos;
	}

	public void forbidCastling(Move m) {

		if (ChessConstants.isRook(m.pieceId)) {
			if (ChessConstants.isWhite(m.pieceId)) {
				if (m.getFromX() == 7 && m.getFromY() == 7) {
					if (whiteOkToCastle.equals("KQ"))
						whiteOkToCastle = "Q";
					else if (whiteOkToCastle.equals("K"))
						whiteOkToCastle = "-";

				} else if (m.getFromX() == 0 && m.getFromY() == 7) {
					if (whiteOkToCastle.equals("KQ"))
						whiteOkToCastle = "K";
					else if (whiteOkToCastle.equals("Q"))
						whiteOkToCastle = "-";
				}
			} else if (m.getFromX() == 7 && m.getFromY() == 0) {
				if (blackOkToCastle.equals("kq"))
					blackOkToCastle = "q";
				else if (blackOkToCastle.equals("k"))
					blackOkToCastle = "-";
			} else if (m.getFromX() == 0 && m.getFromY() == 0) {
				if (blackOkToCastle.equals("kq"))
					blackOkToCastle = "k";
				else if (blackOkToCastle.equals("q"))
					blackOkToCastle = "-";
			}
		} else if (ChessConstants.isWhite(m.pieceId))
			whiteOkToCastle = "-";
		else
			blackOkToCastle = "-";
	}

	public Position getKingPosition(boolean color) {
		return color ? whiteKingPos : blackKingPos;
	}

	/*
	 * public boolean isOkToCastle(boolean color) { return
	 * color?whiteOkToCastle:blackOkToCastle; }
	 */

	public boolean inStaleMate() {
		int cp;
		if (hasEscapeRoute(whiteToMove))
			return false;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cp = pos.get(i, j);
				// skip check if empty,if king, or if wrong color
				if (cp == ChessConstants.B_EMPTY
						|| (ChessConstants.isWhite(cp) != whiteToMove)
						|| (ChessConstants.isKing(cp)))
					continue;
				if (pieceCanMove(i, j, cp))
					return false;
			}
		}

		return true;

	}

	public boolean inMate(boolean color) {
		if (!hasEscapeRoute(color))
			return !canBlock(color);
		else
			return false;
	}

	private final static int[] xx = new int[] { +1, -1, +1, 0, -1, +1, 0, -1 };
	private final static int[] yy = new int[] { 0, 0, +1, +1, +1, -1, -1, -1 };

	private boolean hasEscapeRoute(boolean color) {
		Position k = new Position();
		k.set(getKingPosition(color).x, getKingPosition(color).y);
		//		 System.out.println("king pos is: "+k.x+" "+k.y);
		for (int i = 0; i < 8; i++) {
			k.set(getKingPosition(color).x + xx[i], getKingPosition(color).y
					+ yy[i]);
			if (!bb.inBoard(k.x) || !bb.inBoard(k.y))
				continue;
			if (kingDistanceOk(getKingPosition(!color), k)) {
				if (!inCheck(color, k)) {
					if (pos.get(k.x, k.y) == ChessConstants.B_EMPTY) {
						//						System.out.println("escape:" + k.x + " " + k.y
						//								+ "empty+nocheck...");
						return true;
					} else {
						//						System.out.println("noescape:" + k.x + " " + k.y
						//								+ " not empty...");
						if (ChessConstants.isWhite(pos.get(k.x, k.y)) != color) {
							//							System.out.println("zzzescape: "+k.x+" "+k.y);
							return true;
						}
						//						else
						//							System.out.println("noescape: " + k.x + " " + k.y
						//									+ " has same color");
					}
				}					
				//					else
				//					System.out.println("noescape:" + k.x + " " + k.y
				//							+ " in check");
			} 
			//			else
			//				System.out.println("noescape:" + k.x + " " + k.y
			//						+ " too close to king");

		}
		return false;
	}

	private boolean canBlock(boolean color) {
		int cx = checkPiecePos.x;
		int cy = checkPiecePos.y;
		int kx = getKingPosition(color).x;
		int ky = getKingPosition(color).y;
		int cp = pos.get(cx, cy);
		System.out.println("checked from:" + cx + "," + cy);
		// create a bitboard containing all squares from checking piece until
		// king
		long checkMask = bb.setBitAt(0, cx, cy);
		// if knight or pawn, this is enough. Otherwise, take also all squares
		// between checking piece and king.
		if (!ChessConstants.isKnight(cp) && !ChessConstants.isPawn(cp)) {
			Position v = getVector(cx, cy, kx, ky);
			int x = cx, y = cy;
			while (x != kx || y != ky) {
				checkMask = bb.setBitAt(checkMask, x, y);
				x = x + v.x;
				y = y + v.y;
				System.out.println("x: y:"+x+" "+y);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cp = pos.get(i,j);
				// skip check if empty,if king, or if wrong color
				if (cp == ChessConstants.B_EMPTY
						|| (ChessConstants.isWhite(cp) != color)
						|| (ChessConstants.isKing(cp)))
					continue;
				long hits = checkMask & bb.getMoves(i, j, cp);
				//if a piece can move into mask, and if this piece is not pinned, then it can block.
				if (hits != 0 && pieceIsFree(cx, cy, i, j, color)) {
					System.out.println(ChessConstants.pieceNames[cp]+" at " + i + "," + j
							+ " can go between");
					// if its a knight we are done.
					if (ChessConstants.isKnight(cp)) {  //|| ChessConstants.isPawn(cp)
						System.out
						.println("Done...its a knight.");
						return true;
					}
					// check for pieces in between.
					int ii = 63;
					long mask = 1;
					int jj = 0;
					int[] store = new int[5];
					while (hits > 0) {
						if ((hits & mask) != 0)
							store[jj++] = ii;
						hits = hits >> 1;
		ii--;
					}
					int x, y;
					for (ii = 0; ii < jj; ii++) {
						y = store[ii] / 8;
						x = store[ii] - 8 * y;
						if (freeWay(i, j, x, y)) {
							//final check. If this is a pawn check that its not a x move
							if (ChessConstants.isPawn(cp)) {
								if ((i!=x && (y!=cy || ChessConstants.isEmpty(pos.get(x, y)))) || (i==x&&y==cy)) {
									System.out.println("...or no. Pawn cannot move in between.");
									System.out.println("Pawn cannot take checking piece by moving straight forward.");
									System.out.println("Pawn cannot go between by X:ing to a square in between if the square is empty.");
								} else {
									System.out.println("Pawn can either go between or can capture checking piece");
									System.out.println("I,J,X,Y: "+i+" "+j+" "+x+" "+y+" ");
									return true;
								}
							} else {
								System.out
								.println("...and has freeway to go between");
								return true;
							}
						}
					}

				}
			}
		}

		return false;
	}

	private boolean pieceIsFree(int x, int y, int xx, int yy, boolean color) {
		int temp1 = pos.get(x, y);
		int temp2 = pos.get(xx, yy);
		boolean ret;
		pos.put(x,y,ChessConstants.B_EMPTY);
		pos.put(xx,yy,ChessConstants.B_EMPTY);
		if (inCheck(color)) {
			//System.out.println("Piece was found to be bound!");
			ret = false;
		} else
			ret = true;
		pos.put(x,y,temp1);
		pos.put(xx,yy,temp2);
		checkPiecePos.x = x;
		checkPiecePos.y = y;
		return ret;
	}

	public boolean inCheck(boolean color) {
		Position kingP = getKingPosition(color);
		if (inCheck(color, kingP)) {
			//			System.out.println("Terje2: changing checkpiecepos");
			checkPiecePos = new Position(tmpChk.x, tmpChk.y);
			return true;
		}
		return false;
	}

	// tmpChk is used to temporary keep the position of the piece that is
	// checking.
	// if the king is checked the value is stored to checkPiecePos.
	private final Position tmpChk = new Position();

	public boolean inCheck(boolean color, Position kingP) {
		// remove king so that he does not block the view.
		Position p = getKingPosition(color);
		pos.put(p.x,p.y,ChessConstants.B_EMPTY);
		boolean result = analyzeBoard(color, kingP);
		// put the king back
		pos.put(p.x,p.y,ChessConstants.getKing(color));
		return result;
	}

	private boolean analyzeBoard(boolean color, Position kingP) {
		int px, py, v;
		int knight = ChessConstants.getKnight(!color);
		int pawn = ChessConstants.getPawn(!color);
		if (checkDiagonals(!color, kingP) || checkHorizontals(!color, kingP))
			return true;
		for (int i = 0; i < 8; i++) {
			px = knV[i][0] + kingP.x;
			py = knV[i][1] + kingP.y;
			if (inCheck(px, py, knight)) {
				return true;
			}
		}
		v = color ? -1 : +1;
		px = kingP.x + 1;
		py = kingP.y + v;
		if (inCheck(px, py, pawn)) {
			return true;
		}
		px = kingP.x - 1;
		if (inCheck(px, py, pawn)) {
			return true;
		}
		return false;
	}

	private boolean inCheck(int px, int py, int pType) {
		if (bb.inBoard(px) && bb.inBoard(py) && pos.get(px, py) == pType) {
			//			System.out.println("Knight or pawn check from" + px + "," + py);
			tmpChk.set(px, py);
			return true;
		}
		return false;
	}

	private boolean checkDiagonals(boolean color, Position p) {
		for (int i = 0; i < 4; i++)
			if (checkDiagonalVectors(i, color, p))
				return true;
		return false;
	}

	private boolean checkDiagonalVectors(int i, boolean color, Position p) {
		int queen = ChessConstants.getQueen(color);
		int bishop = ChessConstants.getBishop(color);
		int cx, cy;
		boolean tst;
		cx = p.x;
		cy = p.y;
		tst = true;
		do {
			cx += vectorsD[i][0];
			cy += vectorsD[i][1];
			if (bb.inBoard(cx) && bb.inBoard(cy)) {
				if (pos.get(cx, cy) != ChessConstants.B_EMPTY)
					if (pos.get(cx, cy) == bishop || pos.get(cx, cy) == queen) {
						tmpChk.set(cx, cy);
						//							System.out.println("Found "
						//									+ ChessConstants.pieceNames[pos.get(cx, cy)]
						//									+ " in checkdiagonal: " + cx + "," + cy);
						return true;
					} else
						tst = false;
			} else
				tst = false;
		} while (tst);
		return false;
	}

	private boolean checkHorizontals(boolean color, Position p) {
		for (int i = 0; i < 4; i++)
			if (checkHorizontalVectors(i, color, p))
				return true;
		return false;
	}

	private boolean checkHorizontalVectors(int i, boolean color, Position p) {

		int queen = ChessConstants.getQueen(color);
		int rook = ChessConstants.getRook(color);
		int cx, cy;
		boolean tst;
		cx = p.x;
		cy = p.y;
		tst = true;
		do {
			cx += vectorsH[i][0];
			cy += vectorsH[i][1];
			if (bb.inBoard(cx) && bb.inBoard(cy)) {
				if (pos.get(cx, cy) != ChessConstants.B_EMPTY)
					if (pos.get(cx, cy) == rook || pos.get(cx, cy) == queen) {
						tmpChk.set(cx, cy);
						//						System.out.println("Found "
						//								+ ChessConstants.pieceNames[pos.get(cx, cy)]
						//								+ " in checkHorizontal: " + cx + "," + cy);
						return true;
					} else
						tst = false;
			} else
				tst = false;
		} while (tst);
		return false;
	}

	public Move classifyMove(int fromX, int fromY, int toX, int toY) {
		Move m = new Move(fromX, fromY, toX, toY);
		//		System.out.println("Classifying move from to: ("+fromX+","+fromY+") ("+toX+","+toY+")");
		return classifyMove(m);
	}

	public Move classifyMove(Move m) {
		int movingPieceId = pos.get(m.getFromX(),m.getFromY());
		int targetId = pos.get(m.getToX(),m.getToY());
		int type = Move.normalF;

		if (ChessConstants.isPawn(movingPieceId)) {
			//Log.d("schack","found pawn on "+m.getFromX()+","+m.getFromY());
			if ((m.getToX() != m.getFromX()) && (ChessConstants.isEmpty(targetId)))
				type = Move.enPassantF;
			else if ((m.getToY() == 7) || (m.getToY() == 0)) {
				type = Move.pawnPromoteF;
			} else if (Math.abs(m.getFromY()-m.getToY()) == 2)
				type = Move.twoStepPawnF;
		} else if (ChessConstants.isKing(movingPieceId)
				&& (Math.abs(m.getToX()-m.getFromX()) == 2))
			type = Move.exchangeF;
		m.set(movingPieceId, type, targetId != ChessConstants.B_EMPTY);

		return m;

	}


	public GameState makeMove(Move m) {


		//Log.d("schack","Trying to make "+m.getShortNoFancy());
		int movingPieceId = m.pieceId;
		//		System.out.println("Piece is a "
		//				+ ChessConstants.pieceNames[movingPieceId]);
		int targetId = pos.get(m.getToX(),m.getToY());

		// System.out.println("trying move FROM: ("+m.getFromX()+"x"+m.getFromY()+") TO:
		// ("+m.getToX()+"x"+m.getToY()+")");
		// check that player does not move opponents piece. Irrelevant. Only own
		// pieces draggable.
		if (!checkTurn(movingPieceId)) {
			System.out.println("illegal: turn error.WhiteToMove="
					+ whiteToMove);
			return null;
		}


		// check if target is reachable by piece
		if ((bb.setBitAt(0l, m.getToX(), m.getToY()) & bb.getMoves(m.getFromX(), m.getFromY(),
				movingPieceId)) == 0) {
			System.out.println("illegal: "
					+ ChessConstants.pieceNames[movingPieceId]
							+ " cannot move like that");
			//			System.out.println("fromx fromy tox toy:"+m.getFromX()+" "+m.getFromY()+" "+m.getToX()+" "+m.getToY());
			//			bb.printBoard(m.getFromX(), m.getFromY(), movingPieceId);
			return null;
		}

		// check if target has the same color as piece.
		if (!ChessConstants.isEmpty(targetId)
				&& ChessConstants.sameColor(movingPieceId, targetId)) {
			//			System.out.println("illegal: cannot take own piece: "+ChessConstants.pieceNames[targetId]);

			return null;
		}

		switch (m.moveType) {

		case Move.normalF:
		case Move.twoStepPawnF:

			// check if something in between
			if (!ChessConstants.isKnight(movingPieceId))
				if (!freeWay(m.getFromX(), m.getFromY(), m.getToX(),
						m.getToY())) {
					//					System.out.println("illegal: not a clear path");
					//					this.getPosition().print();
					return null;
				}
			// If pawn: check if enPassant should be set.
			if (ChessConstants.isPawn(movingPieceId)) {
				// check if pawn is moved forward.
				if (m.getFromX() == m.getToX()) {
					// exit if target is occupied. pawns cannot take forward.
					if (!ChessConstants.isEmpty(targetId)) {
						//System.out.println("illegal: pawn cannot take forward");
						return null;
					}
				}
			}

			break;

		case Move.enPassantF:
			Move previousMove = getMove();
			if (previousMove == null
					|| previousMove.moveType != Move.twoStepPawnF
					|| previousMove.getToX() != m.getToX()
					|| Math.abs(previousMove.getToY()-m.getToY()) != 1) {
				//System.out.println("illegal enpassant");
				return null;
			}
			break;

		case Move.exchangeF:
			if ((m.getFromY() != m.getToY())
					|| (whiteToMove ? (m.getFromY() != 7 || whiteOkToCastle
					.equals("-"))
					: (m.getFromY() != 0 || blackOkToCastle
					.equals("-")))
					|| (inCheck(whiteToMove,
							new Position(m.getFromX(), m.getFromY())))
							|| (m.getToX() > m.getFromX() ? !freeWay(4,
									m.getFromY(), 7, m.getToY())
									|| inCheck(
											whiteToMove, new Position(
													m.getFromX() + 1, m.getFromY()))
													|| !ChessConstants
													.isRook(pos.get(7, m.getFromY()))
													|| (whiteToMove ? whiteOkToCastle
															.indexOf("K") < 0
															: blackOkToCastle
															.indexOf("k") < 0)
															: !freeWay(4, m.getFromY(), 0,
																	m.getToY())
																	|| inCheck(
																			whiteToMove,
																			new Position(m.getFromX() - 1, m.getFromY()))
																			|| !ChessConstants
																			.isRook(pos.get(0, m.getFromY()))
																			|| (whiteToMove ? whiteOkToCastle
																					.indexOf("Q") < 0
																					: blackOkToCastle
																					.indexOf("q") < 0))) {
				//System.out.println("illegal castling");
				return null;
			}
			break;

		case Move.pawnPromoteF:
			if (m.getFromX() == m.getToX() && (targetId != ChessConstants.B_EMPTY))
				// exit if target is occupied. pawns cannot take forward.
			{
				System.out.println("illegal promote. Piece in the way");
				return null;
			}
			break;
		}
		//		Log.d("schack","OK: "+m.getShortNoFancy());
		GameState newState = new GameState(this,m);
		if(newState.checkIfLegal())
			return newState;
		else {
			//			System.out.println("Illegal move was: "+m.getLongNotation());
			return null;
		}
	}

	public boolean checkIfLegal() {

		// get king position for the player currently moving.
		if (inCheck(!whiteToMove)) {
			//	System.out.println("Illegal: "+(!whiteToMove?"White ":"Black ") +"king at ("+getKingPosition(!whiteToMove).x+","+getKingPosition(!whiteToMove).y+") is in check from "+checkPiecePos.x+","+checkPiecePos.y);
			//	System.out.println("Checking piece is a "+ChessConstants.pieceNames[pos.get(checkPiecePos.x, checkPiecePos.y)]);
			return false;
		}
		// check that kings are not too close
		if (!kingDistanceOk(getKingPosition(true),getKingPosition(false))) {
			//	System.out.println("Illegal: Kings too close");
			return false;
		}
		//The position is legal, return true;
		return true;
	}

	public boolean checkIfDraw(boolean playerIsWhite) {
		int piece,val=0;
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				piece=pos.get(i, j);
				if (ChessConstants.isEmpty(piece) || (ChessConstants.isWhite(piece)==playerIsWhite)||ChessConstants.isKing(piece))
					continue;
				else
					if(ChessConstants.isBishop(piece) || ChessConstants.isKnight(piece))
						val += 1;
					else
						val += 2;
				if (val >1)
					return false;
			}
		}
		return true;
	}
	public int checkForEndConditions() {
		Move m = getMove();
		if (inCheck(whiteToMove)) {
			// Check if player is mate.
			if (inMate(whiteToMove)) {
				System.out.println("MATE");
				return GameResult.MATE;
			} else {
				//System.out.println("Opponent is in CHECK!");
				m.setCheck();
			}
		}
		else
			if (inStaleMate()) {
				System.out.println("STALEMATE");
				return GameResult.STALEMATE;
			}
		return GameResult.NORMAL;
	}

	public void applyMove(Move m) {
		// use current position as template to create a new position
		// make move
		if (m==null)
			return;
		if (!ChessConstants.isEmpty(pos.get(m.getToX(),m.getToY())))
			m.setSlag();
		pos.put(m.getToX(),m.getToY(),pos.get(m.getFromX(),m.getFromY()));
		pos.put(m.getFromX(),m.getFromY(),ChessConstants.B_EMPTY);
		//		System.out.println("Making move: "+m.getLongNotation());
		// make sideeffects
		if (ChessConstants.isKing(m.pieceId))
			setKingPosition(!whiteToMove, new Position(m.getToX(), m.getToY()));
		switch (m.moveType) {
		case Move.normalF:
			if(!ChessConstants.isKing(m.pieceId)&&!ChessConstants.isPawn(m.pieceId))
				disambiguate(m);
			break;
		case Move.twoStepPawnF:
			enPassantSquare = new Position(m.getFromX(), ChessConstants
					.isWhite(m.pieceId) ? 3 : 6);
			break;
		case Move.enPassantF:
			pos.put(m.getToX(),m.getFromY(),ChessConstants.B_EMPTY);
			m.setSlag();m.disx=true;
			break;
		case Move.pawnPromoteF:
			if (m.promotePiece != ChessConstants.B_EMPTY)
				pos.put(m.getToX(),m.getToY(),m.promotePiece);
			//Log.d("schack","Promote piece move applied");
			if (m.isInSlag())
				m.disx=true;
			break;
		case Move.exchangeF:
			int rook = (whiteToMove) ? ChessConstants.B_ROOK
					: ChessConstants.W_ROOK;
			if (m.getToX() > m.getFromX()) {
				pos.put(5,m.getToY(),rook);
				pos.put(7,m.getToY(),ChessConstants.B_EMPTY);
			} else {
				pos.put(3,m.getToY(),rook);
				pos.put(0,m.getToY(),ChessConstants.B_EMPTY);
			}
			break;
		}

		theMoveThatLeadToThisPosition = m;
		if ((!whiteToMove && !whiteOkToCastle.equals("-") || whiteToMove
				&& !blackOkToCastle.equals("-"))
				&& (ChessConstants.isKing(m.pieceId) || ChessConstants
						.isRook(m.pieceId))) {
			//System.out.println("king or rook moved.");
			forbidCastling(m);
			//System.out.println("wok: " + whiteOkToCastle + " bok: "
			//		+ blackOkToCastle);

		}
	}

	private final static int[][] knV = { { -1, 2 }, { 1, 2 }, { -2, 1 },
		{ -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 }, { 2, 1 } };
	private final static int[][] vectorsD = { { -1, -1 }, { 1, 1 }, { -1, 1 },
		{ 1, -1 } };
	private final static int[][] vectorsH = { { -1, 0 }, { 1, 0 }, { 0, 1 },
		{ 0, -1 } };


	//this function will add flags for x,y if disambiguation is needed in shortnotation.
	private void disambiguate(Move m) {
		int x, y;
		final Position p=new Position(m.getToX(),m.getToY());
		final Position bv = getVector(m.getToX(),m.getToY(),m.getFromX(),m.getFromY());
		if (ChessConstants.isKnight(m.pieceId)) {
			for (int i = 0; i < 8; i++) {
				x = knV[i][0] + m.getToX();
				y = knV[i][1] + m.getToY();
				if ( bb.inBoard(y)&& bb.inBoard(x)&&pos.get(x, y) == m.pieceId
						&& !((x == m.getFromX()) && (y == m.getFromY())) 
						&& ChessConstants.sameColor(pos.get(x, y), m.pieceId))
					addNotation(m,x,y);

			}
		}
		if (ChessConstants.isBishop(m.pieceId)
				|| ChessConstants.isQueen(m.pieceId)) {

			for(int i=0;i<4;i++) {		
				if (bv.x==vectorsD[i][0]&&bv.y==vectorsD[i][1])
					continue;
				if (checkDiagonalVectors(i,!this.whiteToMove,p)&&pos.get(tmpChk.x,tmpChk.y)==m.pieceId) {
					addNotation(m,tmpChk.x,tmpChk.y);	
				}		
			}
		}	
		if (ChessConstants.isRook(m.pieceId)||
				ChessConstants.isQueen(m.pieceId)) {
			for(int i=0;i<4;i++) {		
				if (bv.x==vectorsH[i][0]&&bv.y==vectorsH[i][1])
					continue;
				if (checkHorizontalVectors(i,!this.whiteToMove,p)&&pos.get(tmpChk.x,tmpChk.y)==m.pieceId) {
					addNotation(m,tmpChk.x,tmpChk.y);	
				}		
			}

		}

	}

	private void addNotation(Move m, int x,int y) {
		if (x == m.getFromX())
			m.disy = true;
		else
			m.disx = true;
	}


	public boolean kingDistanceOk(Position k1, Position k2) {
		return ((Math.abs(k1.x - k2.x) > 1) || (Math.abs(k1.y - k2.y) > 1));
	}

	private void findKings() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int piece = pos.get(i, j);
				if (piece == ChessConstants.B_KING)
					blackKingPos = new Position(i, j);
				else if (piece == ChessConstants.W_KING)
					whiteKingPos = new Position(i, j);
			}
		}
		//Log.d("schack","White king is at "+whiteKingPos.x+","+whiteKingPos.y);
	}

	private boolean checkIfOk(int x, int y) {
		if (!(bb.inBoard(x) && bb.inBoard(y)))
			return false;
		if (!ChessConstants.isEmpty(pos.get(x, y))
				&& ChessConstants.isWhite(pos.get(x, y)) == whiteToMove) {
			//System.out.println("Cannot move there. Same piece color on target square");
			return false;
		}
		// check in the end if piece is bound. If so return false
		int piece = pos.get(x, y);
		pos.put(x, y, ChessConstants.getPawn(whiteToMove));
		if (inCheck(whiteToMove)) {
			//System.out.println(whiteToMove ? "white" : "black"
			//		+ " cannot move piece to " + x + " " + y
			//		+ " because its locked");
			return false;
		}
		pos.put(x, y, piece);
		return true;
	}

	private boolean pieceCanMove(int x, int y, int piece) {
		boolean ret = true;
		// make a bitmask with all own pieces.
		if (ChessConstants.isPawn(piece)) {
			//Find all moves possible from bitboard.
			List<Move>moves = new ArrayList<Move>();
			addMoves(moves,x,y,BitBoardStore.bb.getMoves(x, y, piece));
			//try make each. if none works, end.
			GameState nextState = null;
			for (Move m:moves) {
				//gs.classifyMove(m);
				nextState = makeMove(m);
				if(nextState != null) 
					return true;
			}

		}
		else if (ChessConstants.isKnight(piece)) {
			for (int i = 0; i < 8; i++)
				if (checkIfOk(x + knV[i][0], y + knV[i][1]))
					return true;
		}
		else {
			if (ChessConstants.isBishop(piece) || ChessConstants.isQueen(piece))

				for (int i = 0; i < 4; i++)
					if (checkIfOk(x + vectorsD[i][0], y + vectorsD[i][1]))
						return true;

			if (ChessConstants.isQueen(piece) || ChessConstants.isRook(piece))
				for (int i = 0; i < 4; i++)
					if (checkIfOk(x + vectorsH[i][0], y + vectorsH[i][1]))
						return true;
		}
		return false;
	}

	public boolean freeWay(int xc, int yc, int xx, int yy) {
		Position v = getVector(xc, yc, xx, yy);
		int x = xc;
		int y = yc;
		while (bb.inBoard(x) && bb.inBoard(y)) {
			x += v.x;
			y += v.y;
			if ((x == xx) && (y == yy))
				return true;
			// if(x>7||x<0||y>7||y<0)
			// System.out.println("bugger");
			if (pos.get(x, y) != ChessConstants.B_EMPTY)
				return false;
		}
		//System.out.println("out of bounds");
		return false;
	}

	Position getVector(int cx, int cy, int kx, int ky) {
		int dx = 0;
		int dy = 0;
		if (cx < kx)
			dx = 1;
		if (cx > kx)
			dx = -1;
		if (cy < ky)
			dy = 1;
		if (cy > ky)
			dy = -1;
		return new Position(dx, dy);
	}

	private static final String f[] = { "K", "Q", "B", "N", "R", "P", "k", "q",
		"b", "n", "r", "p" };
	private static final String s[] = { "A", "B", "C", "D", "E", "F", "G", "H" };

	private static int getPieceRep(char c) {
		for (int i = 0; i < f.length; i++) {
			if (c == f[i].charAt(0))
				return i + 1;
		}
		return -1;
	}

	public static int[][] convertFenToBoard(String FEN) {
		//This only looks at the first part of the FEN, i.e. the board part!
		int i = 0, j = 0, p = 0;
		int[][] ret = new int[8][8];
		char c;
		while (i < 64) {
			c = FEN.charAt(p);
			if (!Character.isDigit(c)) {
				if (c == '/')
					j++;
				else {
					int piece = getPieceRep(c);
					if (piece == -1) {
						System.out
						.println("Misformed fen. This is not a piece: "
								+ c + " p is " + p + " i j " + i + " "
								+ j);
						throw new IllegalArgumentException("FEN MISFORMED");
					} else {
						ret[i % 8][j] = piece;
						i++;
					}
				}
			} else {
				int num = c - 48;
				for (int jj = 0; jj < num; jj++, i++)
					ret[i % 8][j] = ChessConstants.B_EMPTY;
			}
			p++;
		}
		return ret;
	}

	public String convertBoardToFen() {
		String ret = "";
		int empty = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pos.get(j, i) != ChessConstants.B_EMPTY) {
					if (empty != 0) {
						ret += Integer.toString(empty);
						empty = 0;
					}
					ret += f[pos.get(j, i) - 1];
				} else
					empty++;
			}
			if (empty != 0) {
				ret += Integer.toString(empty);
				empty = 0;
			}
			ret += (i == 7) ? "" : "/";
		}
		ret += whiteToMove ? " w " : " b ";

		boolean wok = whiteOkToCastle.equals("-"), bok = blackOkToCastle
				.equals("-");

		if (wok && bok)
			ret += "-";
		else {
			ret += (wok ? "" : whiteOkToCastle);
			ret += (bok ? "" : blackOkToCastle);
		}
		if (enPassantSquare != null)
			ret += " " + s[enPassantSquare.x]
					+ Integer.toString(enPassantSquare.y);
		return ret;
	}

	private boolean checkTurn(int pieceType) {
		return whiteToMove == ChessConstants.isWhite(pieceType);
	}


	public int getNoOfKingEscapeRoutes(boolean color) {
		int escapeRoutes = 0;
		Position k = new Position();
		k.set(getKingPosition(color).x, getKingPosition(color).y);
		// System.out.println("king pos is: "+k.x+" "+k.y);
		for (int i = 0; i < 8; i++) {
			k.set(getKingPosition(color).x + xx[i], getKingPosition(color).y
					+ yy[i]);
			if (!bb.inBoard(k.x) || !bb.inBoard(k.y))
				continue;
			if (kingDistanceOk(getKingPosition(!color), k)) {
				if (!inCheck(color, k)) {
					if (pos.get(k.x, k.y) == ChessConstants.B_EMPTY) {
						//							System.out.println("escape:" + k.x + " " + k.y
						//									+ "empty+nocheck...");
						escapeRoutes++;
					} else {
						//							System.out.println("noescape:" + k.x + " " + k.y
						//									+ " not empty...");
						if (ChessConstants.isWhite(pos.get(k.x, k.y)) != color)
							escapeRoutes++;
						//							else
						//								System.out.println("noescape: " + k.x + " " + k.y
						//										+ " has same color");
					}
				}					
				//						else
				//						System.out.println("noescape:" + k.x + " " + k.y
				//								+ " in check");
			} 
			//				else
			//					System.out.println("noescape:" + k.x + " " + k.y
			//							+ " too close to king");

		}
		//Log.d("schack","found "+escapeRoutes+" escapeRoutes");
		return escapeRoutes;
	}	
	
	
	public void addMoves(List<Move> result,int fromc,int fromr, long moves) {
		for(int row=0; row<8;row++) {
			for(int column=0;column<8;column++) {
				if(BitBoardStore.isPieceAt(moves, column,row)) {
					result.add(new Move(new Cord(fromc,fromr),new Cord(column,row)));
				}
			}
		}
	}
}
