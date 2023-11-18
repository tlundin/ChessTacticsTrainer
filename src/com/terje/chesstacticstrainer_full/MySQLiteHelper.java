package com.terje.chesstacticstrainer_full;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.terje.chesstacticstrainer_full.Types.MatePuzzle;
import com.terje.chesstacticstrainer_full.Types.TacticProblem;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	private static final String DB_NAME = "chess.db";
	private static String DB_PATH=null;

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public MySQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext=context;
		DB_PATH = "/data/data/"+context.getPackageName()+"/databases/";
	}


	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {		  
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		String myPath = DB_PATH + DB_NAME;
		File dbFile = myContext.getDatabasePath(myPath);
		return (dbFile.exists());	  
	}



	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while((length = myInput.read(buffer))>0) {
			myOutput.write(buffer,0,length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase !=null ) 
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public List<TacticProblem> getTacticProblems(int size, int minRating, int maxRating) {
		//openDataBase();
		List<TacticProblem> ret = new ArrayList<TacticProblem>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * from tactic WHERE rating >"+minRating+" AND rating <"+maxRating+" ORDER BY RANDOM() LIMIT "+size, null);
		c.moveToFirst();
		int rd, rating;
		String board,moves,tmp;
		boolean whiteToMove;
		while(!c.isAfterLast()) {
			rd = c.getInt(1);
			rating = c.getInt(2);
			board = c.getString(3);
			moves = c.getString(4);
			tmp = c.getString(5);
			whiteToMove=false;
			if (tmp!=null && tmp.equals("w"))
				whiteToMove = true;
			ret.add(new TacticProblem(rd,rating,board,moves,whiteToMove));

			c.moveToNext();
		}
		return ret;
	}


	public TacticProblem getTacticProblem(int min, int max) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * from tactic WHERE rating >"+min+" AND rating <"+max+" ORDER BY RANDOM() LIMIT 1", null);
		c.moveToFirst();
		int rd, rating;
		String board,moves,tmp;
		boolean whiteToMove;
		rd = c.getInt(1);
		rating = c.getInt(2);
		board = c.getString(3);
		moves = c.getString(4);
		tmp = c.getString(5);
		whiteToMove=false;
		if (tmp!=null && tmp.equals("w"))
			whiteToMove = true;
		return new TacticProblem(rd,rating,board,moves,whiteToMove);

	}

	public MatePuzzle getMatePuzzle(int nMoves) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * from mate WHERE matein ="+nMoves+" ORDER BY RANDOM() LIMIT 1", null);
		c.moveToFirst();
		String moves,fen;
		boolean whiteToMove;
		moves = c.getString(2);
		fen = c.getString(3);
		
		//whiteToMove=true;
		//moves = "1. Qg7";
		//fen = "2N2B2/r1p2p2/1P4RQ/3kpP1N/2p3R1/1n6/r1n1P2K/1B6 w - - 0 1]";

		
		if (moves!=null && moves.length()>0 && fen !=null) {
			whiteToMove = !(moves.startsWith("1.."));
			return new MatePuzzle(moves,fen,whiteToMove);
		}
		return null;
		

	}

} 
