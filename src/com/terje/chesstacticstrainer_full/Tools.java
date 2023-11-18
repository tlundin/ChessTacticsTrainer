package com.terje.chesstacticstrainer_full;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.terje.chesstacticstrainer_full.Types.Cord;

public class Tools {

	
	
	public static class PersistenceHelper {
		public static final String UNDEFINED = "";
		SharedPreferences sp;
		public PersistenceHelper(Context ctx) {
			sp = PreferenceManager.getDefaultSharedPreferences(ctx);
			if (ctx == null)
				Log.e("Strand","Context null in getdefaultsharedpreferences!");
		}

		public String getString(String key) {
			return sp.getString(key,UNDEFINED);
		}
		
		public float getFloat(String key) {
			return sp.getFloat(key, 0);
		}
		public int getInt(String key) {
			return sp.getInt(key, 0);
		}
		
		public void put(String key,int value) {
			sp.edit().putInt(key, value).commit();
		}
		public void put(String key,float value) {
			sp.edit().putFloat(key, value).commit();
		}

		public void put(String key, String value) {
			sp.edit().putString(key,value).commit();
		}

	}

	
	
	
	public static String getCountry() {
		Locale locale = Locale.getDefault();
		 return locale.getCountry();
	}


	
	
	public static int[][] translateBoard(String sBoard) {
		int[][] result = new int[8][8];	
		if (sBoard.length() != 64) {
			System.out.println("the board is not correct in length");
		}
		for(int i=0; i<8;i++) {
			for(int j=0;j<8;j++) {
				char ch = sBoard.charAt(i*8+j);
				result[j][i] = ChessConstants.convertToInt(ch);
			}
		}
		return result;
	}
	
	
	public static MoveList addMoves(String moves,MoveList ml) {
		String[] token = new String[20];
		boolean notDone = true;
		int start=0,end=0,i=0,from=0,to=0;
		Log.d("schack","Creating movelist from "+moves);
		tokenizer:
			while (notDone) {
				int moveEnd = moves.indexOf(',',start);
				while(notDone) {
					end = moves.indexOf(':',start);
					if (end == -1) 
						break tokenizer;
					token[i]= moves.substring(start, end);
					if (i == 1||i==3) {
						try {
							if (token[i].charAt(1)=='x') {
								from = Integer.parseInt(token[i-1]);
							}
							else {	
								to = Integer.parseInt(token[i-1]);
							}
						} catch (Exception e) {e.printStackTrace();}
					}	
					i++;
					start = end+1;
					if (start==moveEnd) {
						i=0;
						start++;
						break;
					}

				}
				Cord fromC = new Cord(from);
				Cord toC = new Cord(to);				
				Move m = new Move(fromC,toC);	
				ml.getCurrentPosition().classifyMove(m);
				if (m.moveType == Move.pawnPromoteF)
					m.promotePiece = ChessConstants.getQueen(ChessConstants.isWhite(m.pieceId));
				ml.add(new GameState(ml.getCurrentPosition(),m));
				//create new empty gamestate;

			}
		return ml;
	}
	

}
