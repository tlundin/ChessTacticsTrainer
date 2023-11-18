/**
 * 
 */
package com.terje.chesstacticstrainer_full;

/**
 * @author tlundin
 * The bitboardstore generates and keeps a set of bitboards (masks) that 
 * are used to find checks and possible moves in a position.
 * new comment
 */
public final class BitBoardStore {

	/**
	 * Generate all possible moves on a board for all pieces. Needs to be done once.
	 */
	private final int BISHOP 	= 0;
	private final int KNIGHT 	= 1;
	private final int W_PAWN	= 2;
	private final int QUEEN 	= 3;
	private final int KING 		= 4;
	private final int ROOK	 	= 5;
	private final int B_PAWN 	= 6;
	private long[][][] bitBoards = new long[8][8][7];
	private int[] 	  typeToIndex;
	public final static BitBoardStore bb = new BitBoardStore();
	
	private BitBoardStore() {
		// For each square on the board, generate possible moves for each piece type.
	  for (int x=0;x<8;x++) {
		for (int y=0;y<8;y++) {
			bitBoards[x][y][BISHOP] = bishopMoves(0,x,y);
			
			bitBoards[x][y][KNIGHT] = knightMoves(0,x,y);
			
			bitBoards[x][y][W_PAWN] = whitePawnMoves(0,x,y);
			
			bitBoards[x][y][B_PAWN] = blackPawnMoves(0,x,y);
			
			long board 				= rookMoves(0,x,y);
			
			bitBoards[x][y][ROOK]   = board;
			
			bitBoards[x][y][QUEEN]  = bishopMoves(board,x,y);
			
			bitBoards[x][y][KING]	= kingMoves(0,x,y);
					
		}
	  }
	  //allow king to go two steps if castling.
	  bitBoards[4][7][KING] = setBitAt(bitBoards[4][7][KING],6,7);
	  bitBoards[4][7][KING] = setBitAt(bitBoards[4][7][KING],2,7);
	  bitBoards[4][0][KING] = setBitAt(bitBoards[4][0][KING],6,0);
	  bitBoards[4][0][KING] = setBitAt(bitBoards[4][0][KING],2,0);
	  typeToIndex = this.buildIndexFromType();
	}

	long[] getMoves(int x,int y) {
		return bitBoards[x][y];
	}
	
	long getMoves(int column, int row, int pieceType) {
		return bitBoards[column][row][typeToIndex[pieceType]];
	}

/*Helper function that sets a bit at a certain position
 * 
 */
	
private int[] buildIndexFromType() {
	int[] ret = new int[15];
	ret[ChessConstants.B_BISHOP] = BISHOP;
	ret[ChessConstants.W_BISHOP] = BISHOP;
	ret[ChessConstants.B_KNIGHT] = KNIGHT;
	ret[ChessConstants.W_KNIGHT] = KNIGHT;
	ret[ChessConstants.B_ROOK]	= ROOK;
	ret[ChessConstants.W_ROOK] 	= ROOK;
	ret[ChessConstants.B_QUEEN] = QUEEN;
	ret[ChessConstants.W_QUEEN] = QUEEN;
	ret[ChessConstants.B_KING]	= KING;
	ret[ChessConstants.W_KING] 	= KING;
	ret[ChessConstants.W_PAWN] 	= W_PAWN;
	ret[ChessConstants.B_PAWN] 	= B_PAWN;
	
	return ret;
	
}
	
public static void test() {
	Long x = 0l;
	x = bb.setBitAt(x, 0, 0);
	System.out.println("TEST: BIT AT 0,0");
	System.out.println(Long.toBinaryString(x));
	x = 0l;
	x = bb.setBitAt(x, 7, 0);
	System.out.println("TEST: BIT AT 7,0");
	System.out.println(Long.toBinaryString(x));
	x = 0l;
	x = bb.setBitAt(x, 0, 7);
	System.out.println("TEST: BIT AT 7,7");
	System.out.println(Long.toBinaryString(x));
	x = 0l;
	x = bb.setBitAt(x, 7, 7);
	System.out.println("TEST: BIT AT 7,7");
	System.out.println(Long.toBinaryString(x));
	
	System.out.println("KING MOVE 6,0 -> 7,0: "+Long.toBinaryString(bb.setBitAt(0, 7, 0) & bb.getMoves(6, 0,ChessConstants.B_KING)));
}

public long setBitAt(long bb, int r, int c) {
	  r=7-r;
	  c=7-c;
	  long m = 1;
	  m = m<<(r+8*c);
	  return (bb|m);
	  
}




public static boolean isPieceAt(long bb, int c,int r) {
	r = 7-r;
	c= 7-c;
	return (bb & (1L << r*8+c)) != 0;
}

public static boolean inBoard(int z) {
	  return (z>=0&&z<8);
}


	private long bishopMoves(long b, int x, int y) {
		  
		int xb=0,yb=0;
		  /*Do down->up diagonal*/
		  /*Find out what y is when x=0; if outside board, 
		   y is bigger than x. Then y must be 0 and x = y-x.*/
		  xb=0; 
		  yb=y-x; 
		  if (yb<0) {
			  xb = x-y; 
			  yb = 0;
		  }
		  int i;
		  for(i=xb;i<8;i++) {
			  if (yb>7) break;
			 b = setBitAt(b,i,yb);
			 yb++;
		  }	  
		  /*Do up->down diagonal. Same as above*/
		  xb=0;
		  yb=x+y;
		  
		  if (yb>7) {
			  xb=yb-7;
			  yb=7;
		  }
		  for(i=xb;i<8;i++) {
			  if (yb<0) break;
			 b = setBitAt(b,i,yb);
			 yb--;
		  }
		  return b;
	}

	private long rookMoves(long b, int x, int y) {
		//rooks are not very difficult...paint a cross on the board.
		for (int i = 0;i<8;i++) {
			b = setBitAt(b,x,i);
			b = setBitAt(b,i,y);
		}
		return b;
	}
	
	
	private long knightMoves(long b,int x, int y) {
		 
		 final int[] xx = new int[] {x-1,x+1,x-2,x-2,x-1,x+1,x+2,x+2};
		 final int[] yy = new int[] {y+2,y+2,y+1,y-1,y-2,y-2,y-1,y+1};

		 for(int i=0;i<8;i++) {
			 if (inBoard(xx[i])&& inBoard(yy[i]))
					 b = setBitAt(b,xx[i],yy[i]);
		 }
		 return b;
	  }
	  
	  private long blackPawnMoves(long b, int x, int y) {
		 
		 //pawns can not exist on first & last row.
		 if(y==0||y==7)
		  return 0;
		 int m=(y==1)?4:3;
		 final int[] xx = new int[] {x+1,x-1,x,x};
		 final int[] yy = new int[] {y+1,y+1,y+1,y+2};
		 for(int i=0;i<m;i++) {
				 if (inBoard(xx[i])&& inBoard(yy[i]))
				    b = setBitAt(b,xx[i],yy[i]);		  
		 }
		 return b;
	  }
	  //this can also be implemented by flipping the white pawn moves.
	  private long whitePawnMoves(long b, int x, int y) {
			 
			 //pawns can not exist on first & last row.
			 if(y==0||y==7)
			  return 0;
			 int m=(y==6)?4:3;
			 final int[] xx = new int[] {x+1,x-1,x,x};
			 final int[] yy = new int[] {y-1,y-1,y-1,y-2};
			 for(int i=0;i<m;i++) {
					 if (inBoard(xx[i])&& inBoard(yy[i]))
					    b = setBitAt(b,xx[i],yy[i]);		  
			 }
			return b;
		  }
	  
	 private long kingMoves(long b,int x, int y) {
		 final int[] xx = new int[] {x+1,x-1,x+1,x,x-1,x+1,x,x-1};
		 final int[] yy = new int[] {y,y,y+1,y+1,y+1,y-1,y-1,y-1};
		 for (int i=0;i<8;i++) {
			 if (inBoard(xx[i]) && inBoard (yy[i]))
			 	b = setBitAt(b,xx[i],yy[i]);
		 }
		 
		 return b;
	 }
	 
	 public void printBoard(int x, int y, int type) {
		 long board = bitBoards[x][y][typeToIndex[type]];
		 System.out.println(Long.toBinaryString(board));
		 
	 }
	 
	  public long pawnChecks(int x, int y, boolean white) {
		  //pawns can not exist on first & last row.
		  if(y==0||y==7)
			  return 0;
		  long b = 0;
		  int f = white?-1:1;
		  final int[] xx = new int[] {x+1,x-1};
		  final int[] yy = new int[] {y+f,y+f};
		  for(int i=0;i<2;i++) {
			  if (inBoard(xx[i])&& inBoard(yy[i]))
				  b = setBitAt(b,xx[i],yy[i]);		  
		  }
		  return b;
	  }		    

}
	