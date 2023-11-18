package com.terje.chesstacticstrainer_full;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.terje.chesstacticstrainer_full.Types.Cord;

public class SimpleChessEngine {


	private static final int INFINITY = 1000;
	private static int DEPTH = 3;
	private boolean stop = false;

	
	public SimpleChessEngine(int depth) {
		DEPTH = depth;
	}
	public GameState findBestMove(GameState gs) {
		Pair max = alphaBetaMax(gs,-INFINITY*2,INFINITY*2,DEPTH);
		if (max != null) {
			//Log.d("schack","score: "+max.score);
			Log.d("schack","Best move: "+max.gs.getMove().getLongNotation());
			return max.gs;
		}
		//Log.d("schack","Found no moves");
		return null;

	}
	
	private class Pair {
		GameState gs;
		float score;
		
		public Pair(GameState gs, float score) {
			this.gs = gs;
			this.score = score;
		}
		
	}
	
	private Pair alphaBetaMax(GameState gs, float alpha, float beta, int depthleft ) {
		List<Move> allMoves;
		GameState nextState;
		
		if ( depthleft == 0 || stop) {
			float max = evaluate(gs);
			//Log.d("schack","max: "+max);
			return new Pair(gs,max);
		}
		allMoves = generateMoves(gs);
//		ArrayList<Pair> result = new ArrayList<Pair>();
		Pair alphaPair = null;
		boolean found = false;
		for (Move m:allMoves) {
			//gs.classifyMove(m);
			nextState = gs.makeMove(m);
			if(nextState != null) {
				found = true;
				//Log.d("schack","D: "+depthleft+"-> M: "+m.getShortNoFancy());
				Pair score = alphaBetaMin(nextState, alpha, beta, depthleft - 1 );					
				if(score ==null || score.score >= beta ) {
					//Log.d("schack","D: "+depthleft+" Hard beta cutoff for move "+m.getShortNoFancy()+" score: "+score+" beta: "+beta);
					return new Pair(nextState,beta);   // fail hard beta-cutoff
				}
//				result.add(score);
				if(score.score > alpha ) {
					alphaPair = new Pair(nextState,score.score); // alpha acts like max in MiniMax
					alpha = score.score;
				}
			}
		}
		if (!found) {
			float max = evaluate(gs);
			if (max==INFINITY)
				max+=depthleft;
			return new Pair(gs,max);
		}
		//Log.d("schack","MAX D: "+depthleft+" Wtm: "+gs.whiteToMove+" Moves found: "+allMoves.size()+" A: "+alpha+" B: "+beta);
		//for(Pair p:result) {
		//	Log.d("schack","Move: "+p.gs.getMove().getShortNoFancy()+" Score: "+p.score);
		//}

		//if (result.size()==0)
		//	Log.d("schack","had "+allMoves.size()+" moves but none works");
		return alphaPair;
	}

	private Pair alphaBetaMin(GameState gs, float alpha, float beta, int depthleft ) {
		List<Move> allMoves;
		GameState nextState;
	
		if ( depthleft == 0 || stop) {
			float min = -evaluate(gs);
			//Log.d("schack","min: "+min);
			return new Pair(gs,min);
		}
		allMoves = generateMoves(gs);
//		ArrayList<Pair> result = new ArrayList<Pair>();
		Pair betaPair = null;
		boolean found = false;
		for (Move m:allMoves) {
			//gs.classifyMove(m);
			nextState = gs.makeMove(m);
			if(nextState != null) {
				found = true;
				//Log.d("schack","D: "+depthleft+"-> M: "+m.getShortNoFancy());
				Pair score = alphaBetaMax(nextState, alpha, beta, depthleft - 1 );
				//Log.d("schack","Min: "+ nextState.getMove().getShortNoFancy()+" S: "+score+" D: "+depthleft);
				if(score ==null || score.score <= alpha ) {
					//Log.d("schack","D: "+depthleft+"Fail hard alpha cutoff for move "+m.getShortNoFancy()+" score: "+score+" alpha: "+alpha);
					return new Pair(nextState,alpha); // fail hard alpha-cutoff
				}
//				result.add(score);
				if( score.score < beta ) {
					betaPair = new Pair(nextState,score.score); // beta acts like min in MiniMax
					beta = score.score;
				}
			}
		}
		//no valid moves found. Should evaluate...either mate or stalemate.
		if (!found) {
			float min = -evaluate(gs);
			if (min==INFINITY)
				min-=depthleft;
			return new Pair(gs,min);
		}
		
		
			
			
		//Log.d("schack","MIN D: "+depthleft+" Wtm: "+gs.whiteToMove+" Moves found: "+allMoves.size()+" A: "+alpha+" B: "+beta);
		//for(Pair p:result) {
		//	Log.d("schack","Move: "+p.gs.getMove().getShortNoFancy()+" Score: "+p.score);
		//}
		//if (result.size()==0)
		//	Log.d("schack","had "+allMoves.size()+" moves but none works");
		
		return betaPair;

	}


	final static float[] kPosV = {1f,.6f,.25f,0,0,.25f,.6f,1f};
	final static float A_LOT = 100;
	
	private float evaluate(GameState gs) {
	
		int r = gs.checkForEndConditions();
		float material = calcMaterial(gs);
		//Log.d("schack","material: "+material);
		if (r!=GameResult.NORMAL) {
			
			if (r==GameResult.MATE) {
			Log.d("schack","gamestate is mate in evaluate "+gs.getMove().getShortNoFancy());
			return -INFINITY;	
			}	
			
			if (r==GameResult.STALEMATE) {
				if (material > 1)
					return -A_LOT;				
			}
	
		}
		//if opponents king has fewer degrees of movement, add some credit.
		//float oppKingMoves=gs.getNoOfKingEscapeRoutes(!gs.whiteToMove);
		float kingMoveFactor = 0;
		float kingOppFactor=0;
		float bal = 1;
		//if (material<ChessConstants.pieceValue[ChessConstants.B_BISHOP]) {
		Position kOp = gs.getKingPosition(!gs.whiteToMove);
		kingMoveFactor = ((kPosV[kOp.x]+kPosV[kOp.y]/2)+(Math.abs(kPosV[kOp.x]-kPosV[kOp.y])/6)*bal);
//		Log.d("schack","adding kfactor: "+kingMoveFactor);
		
		if (material<ChessConstants.B_QUEEN) {
			//Factor for king opposition.
			Position kMe = gs.getKingPosition(gs.whiteToMove);
			float dx = Math.abs(kOp.x - kMe.x);
			float dy = Math.abs(kOp.y - kMe.y);
//			if ((dx == 0 && dy==1)||(dx==1 && dy == 0))
//				kingOppFactor = 0;
			 
		kingOppFactor = (((dx+dy)/6)+((kPosV[kMe.x]+kPosV[kMe.y])/4));
		}	
		
		float kingCheckFactor = (gs.getMove().isInCheck())?-.5f:0;
		//}
		
		
		//}
		return material + kingMoveFactor + kingOppFactor+kingCheckFactor;
	}
	
	private float calcMaterial(GameState gs) {
		float w=0,b=0,material=0;
		for(int row=0; row<8;row++) {
			for(int column=0;column<8;column++) {
				int pieceT = gs.getPosition().get(column, row);
				if (ChessConstants.isWhite(pieceT))
					w+=ChessConstants.pieceValue[pieceT];
				else
					b+=ChessConstants.pieceValue[pieceT];					
			}
		}

		if (!gs.whiteToMove) {
			//If black, return -value if white in advantage.
			material = b-w;
		}
		else {
			material = w-b;
		}
		return material;
	}

	private List<Move>generateMoves(GameState gs) {
		boolean wtm = gs.whiteToMove;
//		Log.d("schack","WTM "+wtm);
		List<Move>moves = new ArrayList<Move>();
		for(int row=0; row<8;row++) {
			for(int column=0;column<8;column++) {
				int pieceT = gs.getPosition().get(column, row);

				if (!ChessConstants.isEmpty(pieceT)&&wtm==ChessConstants.isWhite(pieceT)) {
					gs.addMoves(moves,column,row,BitBoardStore.bb.getMoves(column, row, pieceT));
				}
			}
		}
		//Log.d("schack","Found these moves: ");
		for(Move m:moves) {
			gs.classifyMove(m);
			//for now, only consider Queen promotes.
			if (m.moveType==Move.pawnPromoteF) {
//				Log.d("schack","Setting da promote");
				m.promotePiece=ChessConstants.getQueen(wtm);
			}
		//Log.d("schack","M: "+m.getShortNoFancy());
		}
		return moves;
	}

public void cancel() {
		this.stop = true;
	}

}