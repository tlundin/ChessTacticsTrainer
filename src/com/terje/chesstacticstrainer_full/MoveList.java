package com.terje.chesstacticstrainer_full;

import java.util.LinkedList;

import android.util.Log;

public class MoveList extends LinkedList<GameState> {

	private static final long serialVersionUID = 1L;

	private int currentPosition=-1;
		
	
	public void resetMovePointer() {
		currentPosition = 0;
	}

	public boolean goForward() {
	
		if (currentPosition< this.size()-1) {
			currentPosition++;
			return true;
		} else
			return false;
		
	}

	public boolean goBackward() {
		if (currentPosition > 0) {
			currentPosition--;
			return true;
		} else
			return false;
	}
	
	public boolean hasNext(){
		return (currentPosition< this.size()-1);
	}

	public GameState next() {
		if (goForward())
			return getCurrentPosition();
		else
			return null;
	}
	public boolean setCurrentPosition(int posNr) {
		System.out.println("Set position:" + posNr);
		if (!inRange(posNr)) {
			System.out.println("WRONG! posNr out of bound in setCurrentPosition: "+posNr);
			return false;
		} else {
			// noOfMoves = moveIndex;
			currentPosition = posNr;
			return true;
		}
	}
	
	
	public int getCurrentPosNr() {
		return currentPosition;
	}
	
	
	
	public GameState getCurrentPosition() {
		Move m = get(currentPosition).getMove();
		Log.d("schack","Getcurrentposition, move that lead here: "+(m==null?"NULL":m.getShortNoFancy()));
		Log.d("schack","Getcurrentposition, white to move: "+get(currentPosition).whiteToMove);
		return this.isEmpty()?null:get(currentPosition);
	}
	
	
	public GameState getPosition(int posNr) {
		if (!inRange(posNr)) {
			System.out.println("posindex outside in getPosition: " + posNr);
			// currentPosition = null;
			return null;
		}
		return get(posNr);
	}
	
	@Override
	public boolean add(GameState gs) {
		super.add(gs);
		currentPosition = this.size()-1;
		Log.d("schack","Current position after ADD: "+currentPosition+". ");
		return true;
	}
	
	public void storePositionAtMovePointer(GameState gs) {
		if (inRange(currentPosition+1)) {
			currentPosition++;
			while (this.size() > currentPosition) {
			System.out.println("removing move...");
			remove(currentPosition);
			}
		}
		add(gs);			
	}
	
	private boolean inRange(int posNr) {
		return (posNr < size() && posNr >= 0);

	}
	
	@Override
	public void clear() {
		currentPosition = -1;
		clear();
	}

	
}



		


