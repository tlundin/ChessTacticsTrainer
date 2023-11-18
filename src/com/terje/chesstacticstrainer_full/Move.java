package com.terje.chesstacticstrainer_full;

import com.terje.chesstacticstrainer_full.Types.Cord;

//Different kinds of moves

public class Move extends BasicMove {
	
	public static final int normalF = 0;
	public static final int enPassantF = 1;
	public static final int exchangeF = 2;
	public static final int pawnPromoteF = 3;
	public static final int twoStepPawnF = 4;
	
	
	public int pieceId;
	public int moveType=normalF;
	public int promotePiece=ChessConstants.B_EMPTY;
	private boolean slag,check=false;
	public boolean disx=false,disy=false;
	private final static String[] xs = {"a","b","c","d","e","f","g","h"};
	private final static String[] ys = {"8","7","6","5","4","3","2","1"};
	
	public Move() {
		
	}
	
	public Move(Cord from, Cord to) {
		super(from,to);
	}
	public Move(int fromX, int fromY, int toX, int toY) {
		super(new Cord(fromX,fromY),new Cord(toX,toY));
	}
	
	
	
	public void set(int x,int y,int xx, int yy) {
		from = new Cord(x,y);
		to = new Cord(xx,yy);
	}
	
	
	public void set(int id,int moveType,boolean _slag) {
		pieceId = id;
		this.moveType = moveType;
		slag = _slag;
		if (slag && ChessConstants.isPawn(pieceId))
			disx=true;
	}
	/*
	public void set(int x,int y,int xx, int yy,int id,int moveType) {
		set(x,y,xx,yy);
		set(id,moveType,false);
	}
	*/
	
	public String getNotation() {
		if(Move.exchangeF==moveType) 
			return getFromX()<getToX()?"O-O":"O-O-O";
		else
			return getShortNotation();
	}
	
	public String getLongNotation() {
		return printPiece(pieceId)+xs[getFromX()]+ys[getFromY()]+
		(slag?"x":"-")+xs[getToX()]+ys[getToY()]+(promotePiece==ChessConstants.B_EMPTY?"":"="+printPiece(promotePiece))+(check?"+":"");
	}
	
	public String getShortNotation() {
		return printPiece(pieceId)+(disx?xs[getFromX()]:"")+(disy?ys[getFromY()]:"")+(slag?"x":"")+xs[getToX()]+ys[getToY()]+(promotePiece==ChessConstants.B_EMPTY?"":"="+printPiece(promotePiece))+(check?"+":"");
	}
	//ChessConstants.pieceShortName[pieceId]
	public String getRawNotation() {
		return(xs[getFromX()]+ys[getFromY()]+xs[getToX()]+ys[getToY()]+(promotePiece==ChessConstants.B_EMPTY?"":"="+ChessConstants.pieceShortName[promotePiece]));	
	}
	
	public String getShortNoFancy() {
		return ChessConstants.pieceShortName[pieceId]+(disx?xs[getFromX()]:"")+(disy?ys[getFromY()]:"")+(slag?"x":"")+xs[getToX()]+ys[getToY()]+
		(promotePiece==ChessConstants.B_EMPTY?"":"="+ChessConstants.pieceShortName[promotePiece])+(check?"+":"");
	}
	
	private static String printPiece(int pieceId) {
		if (ChessConstants.isPawn(pieceId)||ChessConstants.isEmpty(pieceId))
				return "";
			return ChessConstants.pieceShortName[pieceId];
		//return new String("&lt;img border=&quot;0&quot; src=&quot;"+notationBase+ChessConstants.pieceNotationPicName[pieceId]+"&quot; alt=&quot;"+ChessConstants.pieceShortName[pieceId]+"&quot; /&gt;");
	}
	
	public void setCheck() {
		check=true;
	}
	
	public boolean isInCheck() {
		return check;
	}
	
	public boolean isInSlag() {
		return slag;
	}
	
	public void setSlag() {
		slag = true;
	}
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}
}