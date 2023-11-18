package com.terje.chesstacticstrainer_full;


import java.util.ArrayList;

import android.util.Log;



/*
| 1.c3 Nf6|2.d4 b6|3.Nf3 Bb7|4.g3 e5|5.dxe5 Ng4|6.Bg5 Be77.Bxe7 Qxe7|8.Bg2 Nxe5|9.O-O Nxf3+|10.exf3 O-O|11.Re1 Qf612.Nd2 d5|13.f4 Nd7|14.Nc4 Rad8|15.Qc2 dxc4|16.Bxb7 c617.Qe4 Nc5|18.Qxc6 Qf5|19.Qf3 Qc2|20.Bd5 Qxb2|21.Reb1 Qc222.Bxc4 Nd3|23.Rd1 Nxf2|24.Bb3 Nh3+|25.Kh1 Qf5|26.Kg2 h527.Rd5 Rxd5|28.Qxd5 Qg4|29.Qf3 Re8|30.Bc4 Rd8|31.Qxg4hxg4|32.Re1 Rd2+|33.Re2 Rd1|34.Re8+ Kh7|35.Bf1 Ra1|36.Re2b5|37.Rb2 g6|38.Rxb5 Rxa2+|39.Kh1 Ra1|40.Kg2 a5|41.c4 Kg742.c5 g5|43.fxg5 Ra2+|44.Kh1 Nxg5|45.Rb1 Rc2|46.Bd3 Rxc547.Rf1 Rc3|48.Bf5 Rf3|49.Kg2 a4|50.Rxf3 gxf3+|51.Kf2 a352.Bb1 Ne4+|0-1 |
1. d4 f5 2. c4 Nf6 3. g3 g6 4. Bg2 Bg7 5. Nf3 O-O 6. O-O d6 7. Nc3 Qe8 8. d5 Na6 9. Rb1 c5 10. dc6 bxc6 11. b4 Bd7 12. a3 Nc7 13. Bb2 Rb8 14. c5 dxc5 15. bxc5 Ng4 16. Ba1 Rxb1 17. Qxb1 e5 18. h3 Nh6 19. Rd1 e4 20. Ng5 Ne6 21. Qa2 Nf7 22. Rxd7 Nexg5 23. Rxa7 Ne6 24. Qc4 
*/

/**
* @author jan and trashed by terje
*
*/
public class GameAnalyzer {
	/*
	 private static final String DELIMITER = "|";
	 private static final String WhiteWin = "1-0";
	 private static final String Draw = "1/2-1/2";
	 private static final String BlackWin = "0-1";
	 */
 
     private static String[] splitRes = new String[2];

     
	public static MoveList createGame(String game) {	
			return createGameFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq",game);
	}
	
	public static MoveList createGameFromFen(String fen, String moves) {
		MoveList ml = new MoveList();
		ArrayList<String> moveA = GameAnalyzer.removeTrash(moves);
		if (moveA!=null) {
			ml.add(new GameState(GameState.convertFenToBoard(fen),whiteToMove));
			return createGameFromMoveList(ml,moveA);
		}
		else
			return null;
	}

	public static MoveList createGameFromMoveList(MoveList ml,ArrayList<String> moves) {

		GameState current;
		int i = 0;
		String curMove;
		
		
		String res = moves.get(moves.size()-1);
/*		System.out.println("Result in end was: "+res);
		if (res.equals("1-0"))
			gsc.setResult(GameResult.UNKNOWN,true);
		if (res.equals("0-1"))
			gsc.setResult(GameResult.UNKNOWN,false);
		if (res.equals("1/2-1/2"))
			gsc.setResult(GameResult.DRAW_UNKNOWN);
		if(gsc.getResult()!=null)
			moves.remove(moves.size()-1);
*/	
		//GameStateController gsc = new GameStateController(GameStateController.GAME_BROWSER);
		
		
		while (i < moves.size()) {
			curMove = moves.get(i).trim();
			System.out.println("Trying to make: " + curMove);
			if (curMove.equals("")) {
				i++;
				continue;
			}
			current = ml.getLast();
			Move m = convertToMove(current,curMove);
			if (m == null ) {
				System.out.println("failed to decode all moves");
				//gsc=null;
				return null;
			}
			else {
				current.classifyMove(m);
				current = new GameState(current,m);
				ml.add(current);
			}
			i++;
		}
		ml.resetMovePointer();
		return ml;
	}

    
	static boolean whiteToMove=true; 
     
    public static ArrayList<String> removeTrash(String game) {

        int i = 1;
        int lastPos = 0;
        ArrayList<String> retVal = new ArrayList<String>();
        System.out.println("Moves to analyze: "+game);
        whiteToMove=!(game.startsWith("1..."));
        
        if (!whiteToMove) {
        	Log.d("schack","FOUND BLACK MOVE STARTTT");
        	game = game.replaceFirst("1...", "1.");
        	Log.d("schack", "Game after replace: "+game);
        }
        
        while (true) {
            String toRemove = String.valueOf(i) + ".";
            int pos = game.indexOf(toRemove, lastPos);
            if (pos == -1) {
                	if ((twoMoves(game.substring(lastPos, game.length()-1)))){
                		retVal.add(splitRes[0]);
                		retVal.add(splitRes[1]);
                		System.out.println("Last: Adding "+splitRes[0]+" and "+splitRes[1]);
                	}
                	else  {
                		retVal.add(game.substring(lastPos).trim());
                		System.out.println("Last: Adding only "+game.substring(lastPos).trim());
                	}
                	return retVal;
                    
                } 
 /*              int zeroOne = game.lastIndexOf(BlackWin);
                    if (zeroOne > lastPos) {
                    	retVal.add(BlackWin);
                    } else {
                        int oneZero = game.lastIndexOf(WhiteWin);
                        if (oneZero > lastPos) {
                        	retVal.add(WhiteWin);
                        } else {
                            int fiftyFifty = game.lastIndexOf(Draw);
                            if (fiftyFifty > lastPos) {
                            	retVal.add(Draw);
                            } else {
                            	retVal.add(game.substring(lastPos));
                            }
                        }
                   }
                break;
            }
 */
            if (pos > 0) {
            	if (twoMoves(game.substring(lastPos,pos))) {
            		retVal.add(splitRes[0]);
            		retVal.add(splitRes[1]);
            	}
            	else {
            		System.out.println("Something wrong at move "+i+". String: "+game.substring(lastPos,pos));
            		return null;	
            		}
            }
            lastPos = pos + toRemove.length();
            i++;
        }
    }
    
	private static boolean twoMoves(String toSplit) {
		final String[] normalSplit = toSplit.trim().split(" ");
		int length = toSplit.length();
		if ((normalSplit).length==2) {
			splitRes[0]=normalSplit[0];
			splitRes[1]=normalSplit[1].replace("|","");

			return true;
		} 
			//split failed. Try more advanced.	
		if (normalSplit.length==1) {
			splitRes[0]=normalSplit[0];
			splitRes[1]="";
			return true;
		}
		return false;
		/*
				for (int j = 1; j < length-2; j++) {
					char c = toSplit.charAt(j);
					if (Character.isDigit(c)) {
						System.out.println("Found split potential at char: "+c);
						char nextChar = (j+1<length)?toSplit.charAt(j+1):'.';
					if (nextChar=='.') {
						System.out.println("At end of line!");
						return false;
					}
					//Check special cases
					//'+' means split should be move one step.
					if (nextChar=='+')
						j++;
					char nextnextChar = (j+2<length)?toSplit.charAt(j+2):'.';
					if (nextChar=='.') {
						System.out.println("At end of line!");
						return false;
					}
					//'e' might be as in "ep" meaning en passant. Then skip two.
					else if ((nextChar=='e'&&nextnextChar=='p')||((c=='8'||c=='1')&&(nextChar=='='))) {
						System.out.println("enpassant or promote in split: "+toSplit);
						j+=2;
						if((j+3)<length&&toSplit.charAt(j+3)=='+')
							j++;
					}
					System.out.println("SPLIT! Sub1: "+toSplit.substring(0,j+1)+" Sub2: "+toSplit.substring(j+1));
					splitRes[0] = toSplit.substring(0,j+1);
					splitRes[1] = toSplit.substring(j+1).replace("|", "");
					return true;
					}
				}
			
		}
		return false;
		*/
	}
	
	public static Move convertToMove(GameState gs, String s) {
		
		boolean white = gs.whiteToMove;
		Move m = new Move();
		int fromX,fromY,toX,toY;
		int l = s.length();
		int promotePiece, column, row;
		// check for corruption
		if (s == null || l < 2) {
			return null;
		}
		// check for check
		if (s.charAt(l - 1) == '+') {
			s = s.substring(0, l - 1);
			l--;
			System.out.println("CHECK!");
			//ensure check is true
			m.setCheck();
		} else if (s.charAt(l - 1) == '#') {
			s = s.substring(0, l - 1);
			l--;
			System.out.println("MATE!");
		}

		// check for corruption
		if (l < 2)
			return null;
		// check for short castling
		if ("o-o".equals(s) || "O-O".equals(s) || "0-0".equals(s)) {
			if (white) {
				fromY = 7;
				toY = 7;
			} else {
				fromY = 0;
				toY = 0;
			}
			toX = 6;
			fromX = 4;
			m.set(fromX,fromY,toX,toY);
			return m;
		}
		// check for long castling
		if ("o-o-o".equals(s) || "O-O-O".equals(s) || "0-0-0".equals(s)) {
			if (white) {
				fromY = 7;
				toY = 7;
			} else {
				fromY = 0;
				toY = 0;
			}
			toX = 2;
			fromX = 4;
			m.set(fromX,fromY,toX,toY);
			return m;
		}
		// check for promotion
		char lastChar = s.charAt(l - 1);
		if (!Character.isDigit(lastChar)) {
			if ((promotePiece = getPiece(lastChar, white)) != -1) {
				System.out.println("Promotion!");
				int discard = 1;
				if (s.charAt(l - 2) == '=')
					discard++;
				s = s.substring(0, l - discard);
				l -= discard;
				lastChar = s.charAt(l - 1);
				m.promotePiece = promotePiece;
			} else {
				System.out.println("last char was not digit and not = before");
				return null;
			}
		}

		// Get To or From
		if (l > 1 && Character.isDigit(lastChar)) {
			column = checkLetter(s.charAt(l - 2));
			row = 8 - Character.digit(lastChar, 10);
			l -= 2;
		} else {
			System.out.println("last xx was not A..H,0..9 or string too short");
			return null;
		}
		// if no more chars, this is it. It was a pawn move.
		if (l == 0)
			return findMove(gs,m, ChessConstants.getPawn(white), column, -1, column,
					row);
		// if only one char remaining, it was a normal piece move, short
		// notation
		if (l == 1) {
			int pieceType = getPiece(s.charAt(0), white);
			if (pieceType == -1) {
				System.out.println("Couldnt find piece for "+s+". Will instead try if its a pawn capturing");
				pieceType = checkLetter(s.charAt(0));
				if (pieceType == -1) {
					System.out.println("...failed. This is corrupt");
					return null;
				} else
					return findMove(gs,m, ChessConstants.getPawn(white), pieceType, -1, column, row);
			}
			return findMove(gs,m, pieceType, -1, -1, column,
					row);
		}
		lastChar = (s.charAt(l - 1));
		if (lastChar == 'x' || lastChar == 'X' || lastChar == '-') {
			l--;
			lastChar = (s.charAt(l - 1));
		}
		if (l == 1) {
			/*System.out.println("Short notation capture, no special: " + s + " "
					+ s.charAt(l));*/
			int piece = getPiece(s.charAt(0), white);
			if (piece == -1)
				return findMove(gs,m, ChessConstants.getPawn(white), checkLetter(s
						.charAt(0)), -1, column, row);
			else
				return findMove(gs,m, piece, -1, -1, column, row);
			// now it is certain its long notation.
		}
		if (l == 3) {
			/*System.out.println("long notation, piece move");*/
			return findMove(gs,m, getPiece(s.charAt(0), white), checkLetter(s
					.charAt(1)), 8 - Character.digit(s.charAt(2), 10), column,
					row);
		}
		// either pawn or special Short notation
		if (l == 2) {
			/*System.out.println("pawnlong or short special");*/
			int piece = getPiece(s.charAt(0), white);
			if (piece != -1) {
				// special. Contains either row or column info.
				int tc = -1, tr = -1;
				if (Character.isDigit(s.charAt(1)))
					tr = 8 - Character.digit(s.charAt(1), 10);
				else
					tc = checkLetter(s.charAt(1));
				return findMove(gs,m, piece, tc, tr, column, row);
			} else
				return findMove(gs,m, ChessConstants.getPawn(white), checkLetter(s
						.charAt(0)), 8 - Character.digit(s.charAt(1), 10),
						column, row);
		}
		System.out.println("I get here. That is bad");
		return null;
	}
	

	private static Move findMove(GameState gs, Move m, int piece, int frX, int frY, int toX, int toY) {
		
		/*System.out.println("frx fry tox toy: " + frX + " " + frY + " " + toX
				+ " " + toY);*/
		if (frX != -1 && frY != -1) {
			m.set(frX, frY, toX, toY);
			if (inBoard(frX) && inBoard(frY) && inBoard(toX) && inBoard(toY))
				return m;
			else {
				System.out.println("Cords out of board in findmove");
				return null;
			}
		}
		int x = (frX == -1) ? 0 : frX, y = (frY == -1) ? 0 : frY;
		if (!(inBoard(x) && inBoard(y) && inBoard(toX) && inBoard(toY)))
			return null;
		boolean wrongPiece = false;
		int fromX=0,fromY = 0;
		while (true) {
			//System.out.println("X Y: " + x + " " + y);
			if (piece != gs.getPosition().get(x, y) || wrongPiece) {
				wrongPiece = false;
				if (frX == -1) {
					x++;
					if (x > 7) {
						x = 0;
						if (frY != -1 || y == 7) {
							System.out
									.println("Couldnt find FROM piece in findMove X");
							return null;
						} else
							y++;
					}
				} else {
					y++;
					if (y > 7) {
						System.out
								.println("Couldnt find FROM piece in findMove Y");
						return null;
					}
				}
			} else {
				if ((BitBoardStore.bb.setBitAt(0, toX, toY) & BitBoardStore.bb.getMoves(x, y, piece)) != 0) {

					if ((ChessConstants.isPawn(piece) && toX != x && gs.getPosition().get(toX, toY) == ChessConstants.B_EMPTY)) {
						Move previousMove = gs.getMove();
						if (previousMove == null
								|| previousMove.moveType != Move.twoStepPawnF
								|| previousMove.getToX() != toX
								|| Math.abs(previousMove.getToY() - toY) != 1) {
							System.out.println("Enpassant impossible");
							wrongPiece = true;
							continue;
						}
					}
					if (ChessConstants.isKnight(piece)||
							ChessConstants.isKing(piece)||
							gs.freeWay(x, y, toX, toY)) {
						fromX = x;
						fromY = y;
						break;
					}
				}
				wrongPiece = true;
			}
		}
		m.set(fromX, fromY, toX,toY);

		if (inBoard(fromX) && inBoard(fromY) && inBoard(toX)
				&& inBoard(toY)) {
			/*System.out.println("found piece. My pos nr is: "
					+ this.getLastPos().getPosNr());*/
			return m;
		} else {
			System.out.println("Cords out of board in findmove");
			return null;
		}

	}
	
	private static int getPiece(final char c, boolean isWhite) {
		int pp = -1;
		switch (c) {
		case 'Q':
			// case 'q':
			pp = ChessConstants.getQueen(isWhite);
			break;
		case 'R':
			// case 'r':
			pp = ChessConstants.getRook(isWhite);
			break;
		case 'N':
			// case 'n':
			pp = ChessConstants.getKnight(isWhite);
			break;
		case 'B':
			// case 'b':
			pp = ChessConstants.getBishop(isWhite);
			break;
		case 'K':
			pp = ChessConstants.getKing(isWhite);
		}
		return pp;
	}

	private static int checkLetter(char c) {
		if (c >= 'a' && c <= 'h')
			return (c - 97);
		if (c >= 'A' && c <= 'H')
			return (c - 65);
		return -1;
	}
	
	public static boolean inBoard(int z) {
		  return (z>=0&&z<8);
	}
	
	public static Move calcMoveFromFEN(int[][] board, String fen) {
		int[][] newBoard= GameState.convertFenToBoard(fen);
		return findDiff(board,newBoard);
	}
	
	private static Move findDiff(int[][] curPos, int[][] newPos) {
		final Position p1 = new Position();
		final Position p2 = new Position();
		final Position p3 = new Position();
		final Position p4 = new Position();
		final Position p[] = {p1,p2,p3,p4};
		int i=0;
		for (int x = 0; x<8;x++) {
			for(int y=0;y<8;y++) {				
				if (curPos[x][y]!=newPos[x][y]) {
					if (i>3) {
						System.out.println("more than 4 changes in the board in finddiff. impossible");
						System.out.println("x: y: "+x+" "+y);
						return null;
					}
					p[i].x=x;
					p[i].y=y;
					i++;
				}
						
			}
		}
		Move ret = new Move();
		if (i==4) {
			System.out.println("rockad");
			if (p1.x == 0) {
				System.out.println("0-0-0");
				ret.set(4, p1.y, 2,p1.y);
			}
			else {
				System.out.println("0-0");
				ret.set(4,p1.y,6,p1.y);
			}
			return ret;
				
		}
		if (i==3) {
			System.out.println("En passant!");
			for (int r=0;r<3;r++) {
				if (newPos[p[r].x][p[r].y] != ChessConstants.B_EMPTY) {
					System.out.println("Found TO square!");
					for (int rr=0;rr<3;rr++) {
						if(newPos[p[rr].x][p[rr].y]==ChessConstants.B_EMPTY && p[rr].x != p[r].x) {
							System.out.println("Found FROM square!");
							ret.set(p[rr].x, p[rr].y, p[r].x,p[r].y);
							return ret;
						}
					}
				}
			}
			System.out.println("Didn't find From and To in en passant.");
			return null;
		}
		else if (i!=2) {
			System.out.println("something wrong in finddiff");
			return null;
		}
		
		if (newPos[p1.x][p1.y] == ChessConstants.B_EMPTY) 
			ret.set(p1.x,p1.y,p2.x,p2.y);
		else
			ret.set(p2.x,p2.y,p1.x,p1.y);
		return ret;
		
	}


/*package com.seriouschess.client.chessControl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * [Event "Miscellaneous Game"] ["Site "?"] [Date "1620.??.??"] [EventDate "?"]
 * [Round "35"] [Result "1-0"] [White "Gioachino Greco"] [Black "NN"] [ECO
 * "A02"] [WhiteElo "?"] [BlackElo "?"] [PlyCount "19"]
 * 
 * 1.f4 e5 2.fxe5 Qh4+ 3.g3 Qe4 4.Nf3 Nc6 5.Nc3 Qf5 6.e4 Qe6 7.d4 Qe7 8.Bg5 Qb4
 * 9.a3 Qxb2 10.Na4 1-0
 */

	
	
	
	
/**
 * @author jan
 * 
 *
	
	private static Connection conn;

	private static PreparedStatement selectStmt;

	private static PreparedStatement checkFenStmt;

	private static PreparedStatement insertFenStmt;

	private static PreparedStatement insertGafStmt;

	private static PreparedStatement checkLastIdStmt;

	//private static final String DELIMITER = "|";

	private static final String selectGameStr = "select id, game from games_archive_new where id between ? and ?";

	private static final String insertFenStr = "insert into fen(fen) values (?)";

	private static final String insertGafStr = "insert into games_archive_fen values(?, ?, ?)";

	private static final String checkFenStr = "select id from fen where fen = ?";

	private static final String checkLastIdStr = "select last_insert_id()";


	private static void insertDb(String calcFen, long id, int seqNo)
		throws SQLException {
	Long fenId = null;
	checkFenStmt = conn.prepareStatement(checkFenStr);
	checkFenStmt.setString(1, calcFen);
	ResultSet rs = checkFenStmt.executeQuery();
	if (rs.next()) {
		fenId = rs.getLong(1);
		rs.close();
	} else {
		insertFenStmt = conn.prepareStatement(insertFenStr);
		insertFenStmt.setString(1, calcFen);
		insertFenStmt.executeUpdate();
		insertFenStmt.close();
		checkLastIdStmt = conn.prepareStatement(checkLastIdStr);
		rs = checkFenStmt.executeQuery();
		rs.next();
		fenId = rs.getLong(1);
		rs.close();
		checkLastIdStmt.close();
	}
	checkFenStmt.close();
	insertGafStmt = conn.prepareStatement(insertGafStr);
	insertGafStmt.setLong(1, id);
	insertGafStmt.setInt(2, seqNo);
	insertGafStmt.setLong(3, fenId);
	insertGafStmt.executeUpdate();
	insertGafStmt.close();
	}

	

	public static void analyseGames(long start, long end) throws IOException,
	InstantiationException, IllegalAccessException,
	ClassNotFoundException, SQLException  { 
	
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager
				.getConnection("jdbc:mysql://193.96.173.140:3306/chess?"
						+ "user=terje&password=%26amanda");
		selectStmt = conn.prepareStatement(selectGameStr);
		selectStmt.setLong(1, start);
		selectStmt.setLong(2, end);
		ResultSet rs = selectStmt.executeQuery();
		while (rs.next()) {
			long id = rs.getLong(1);
			String game = rs.getString(2);
			analyzeGame(id, game);
		}
		rs.close();
		selectStmt.close();
		conn.close();
	}
	final static GameStateController gsc = new GameStateController(GameStateController.GAME_ANALYZER,null);
	
	public static void analyzeGame(long id, String game) throws SQLException {
		System.out.println("Analyzing game " + id);
		gsc.setupStartingBoard();
		if (createGameFromMoveList(gsc,game)) {
			int i = 1;
			for (GameState gs : gsc.getMoveList().moveList) {
				insertDb(gs.convertBoardToFen(), id, i++);
			}
		}
	}



	private static String removeTrash(String game) {
		String aux = game.replace(DELIMITER, " ");
		int i = 1;
		int lastPos = 0;
		StringBuilder retVal = new StringBuilder();
		while (true) {
			String toRemove = String.valueOf(i) + ".";
			int pos = aux.indexOf(toRemove, lastPos);
			if (pos == -1) {
				int lastBlank = aux.lastIndexOf(' ');
				if (lastBlank > lastPos) {
					retVal.append(aux.substring(lastPos, lastBlank));
				} else {
					int zeroOne = aux.lastIndexOf("0-1");
					if (zeroOne > lastPos) {
						retVal.append(aux.substring(lastPos, zeroOne));
					} else {
						int oneZero = aux.lastIndexOf("1-0");
						if (oneZero > lastPos) {
							retVal.append(aux.substring(lastPos, oneZero));
						} else {
							int fiftyFifty = aux.lastIndexOf("1/2-1/2");
							if (fiftyFifty > lastPos) {
								retVal.append(aux
										.substring(lastPos, fiftyFifty));
							} else {
								retVal.append(aux.substring(lastPos));
							}
						}
					}
				}
				break;
			}
			if (pos > 0) {
				retVal.append(aux.substring(lastPos, pos));
			}
			lastPos = pos + toRemove.length();
			i++;
		}
		return retVal.toString();
	}
}
*/
	
}



