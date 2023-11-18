package com.terje.chesstacticstrainer_full;

import android.util.Log;

public class ChessPosition {

	private int[][] pos;
	
	private boolean wtm = true;

	public ChessPosition(String sBoard) {
		pos = new int[8][8];
		Log.d("schack","chessboard to draw: ");
		for(int column=0; column<8;column++)
			Log.d("schack",sBoard.substring(column*8, column*8+8));
		if (sBoard.length() != 64) {
			Log.e("chess","the board is "+sBoard.length()+" long. Should be 64");
		}
		for(int row=0; row<8;row++) {
			for(int column=0;column<8;column++) {
				char ch = sBoard.charAt(row*8+column);
				pos[column][row] = ChessConstants.convertToInt(ch);
			}
		}			
	}
	
	public ChessPosition() {
		pos = new int[8][8];
	}
	
	public ChessPosition(int[][] pos) {
		this.pos = new int[8][8];
		for(int i = 0;i< 8;i++) {
			this.pos[i] = pos[i].clone();
		}
	}

	public boolean isEmpty(int column, int row) {
		return(ChessConstants.isEmpty(pos[column][row]));		
	}

	public int get(int column, int row) {
		return pos[column][row];
	}
	
	public int[][] getBoard() {
		return pos;
	}

	public void put(int column, int row, int piece) {
		pos[column][row] = piece;
	}

	public void print() {
		for(int row = 0;row<8;row++) {
			for(int column = 0;column<8;column++) {
			System.out.print(ChessConstants.pieceCharNames[get(column,row)]);
			}
			System.out.println();
		}
		
	}
}
