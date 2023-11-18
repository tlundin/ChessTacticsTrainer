package com.terje.chesstacticstrainer_full;

import java.io.Serializable;

public class GameResult implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameResult() {
	}
	
	public GameResult(int result, int reason) {
		this.result = result;
		this.lossReason = reason;
	}
	
	public static final int BLACK_WIN = 0;
	public static final int WHITE_WIN = 1;
	public static final int DRAW = 2;
	public static final int NO_WINNER = 3;
	
	public static final int TIME_LOSS=0;
	public static final int RESIGN=1;
	public static final int MATE=2;
	public static final int DISCONNECTION=3;
	public static final int INSUFFICIENT_MATERIAL_DRAW=4;
	public static final int STALEMATE = 5;
	public static final int DRAW_BY_AGREEMENT = 6;
	public static final int DRAW_BY_50_MOVE_RULE = 7; 
	public static final int UNKNOWN=8;
	public static final int DRAW_UNKNOWN=9;
	public static final int FIRST_MOVE_EXIT=10;	
	public static final int NORMAL=-1;
	
	private  int result;
	private  int lossReason;
	
	public boolean draw() {
		return (result == DRAW);
	}
	
	public int getReason() {
		return lossReason;
	}
	
	public int getResult() {
		return result;
	}
	
	public String resultString() {
		return resultString(result);
	}
	
	public static String resultString(int res) {
		switch (res) {
		case WHITE_WIN:
			return "1-0";
		case BLACK_WIN:
			return "0-1";
		case DRAW:
			return "1/2-1/2";
		case NO_WINNER:
			return "?-?";
		}
		return "";
	}
	
	public static String lossReason(int reason) {
		switch(reason) {
			case TIME_LOSS:
				return "lost on time";
			case RESIGN:
				return "resigned";
			case MATE:
				return "check-mate";
			case DISCONNECTION:
				return "disconnected";
			case INSUFFICIENT_MATERIAL_DRAW:
				return "insufficient material draw";
			case DRAW_BY_AGREEMENT:
				return "players agreed on draw";
			case UNKNOWN:
				return "unknown";
			case DRAW_UNKNOWN:
				return "unknown";
				
		}
				
		return "";
	}
	
	public String lossReason() {
		return lossReason(lossReason);
	}
}
