package com.terje.chesstacticstrainer_full;

import java.util.Random;

import android.util.Log;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.StageDescriptorFactory.StageDescriptor;
import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.StageDescriptorFactory.StageDescriptor.Type;
import com.terje.chesstacticstrainer_full.Types.MatePuzzle;
import com.terje.chesstacticstrainer_full.Types.TacticProblem;

public class LevelDescriptorFactory {


	public static final int TRAINING_LD = 9999;
	public static final int TEST_LD = 9998;

	public static LevelDescriptor createLevelDescriptor(int lvl) {
		switch (lvl) {
		case TRAINING_LD: 
			return new LevelDescriptor(TRAINING_LD,LevelDescriptor.INFINITY,15,0,0,0,"",true);
		case TEST_LD: 
			return new LevelDescriptor(TEST_LD,LevelDescriptor.INFINITY,15,0,0,0,"",true);
		case 1:
			return new LevelDescriptor(lvl,LevelDescriptor.INFINITY,5,475,350,200,"Easier than easy!",false);
		case 2:
			return new LevelDescriptor(lvl,LevelDescriptor.INFINITY,5,475,350,200,"Queen Home Run",false);
		case 3:
			return new LevelDescriptor(lvl,3,8,375,250,150,"Rook Roller!",false);
		case 4:
			return new LevelDescriptor(lvl,LevelDescriptor.INFINITY,8,475,350,200,"Pawnfully easy",false);
		case 5:			
			return new LevelDescriptor(lvl,3,8,475,250,150,"Pure Tactics",false);
		case 6:			
			return new LevelDescriptor(lvl,3,10,675,350,200,"Promote & Conquer",false);
		case 7:
			return new LevelDescriptor(lvl,3,10,875,550,200,"Rookie Mountain",false);
		case 8:
			return new LevelDescriptor(lvl,3,10,900,850,500,"Move it, rookie!",false);
		case 9:
			return new LevelDescriptor(lvl,3,10,900,750,500,"The fork is with you",false);
		case 10:
			return new LevelDescriptor(lvl,3,8,900,650,500,"PuzzleMania",false);
		case 11:			
			return new LevelDescriptor(lvl,LevelDescriptor.INFINITY,1,800,400,300,"Black King Challenge",false);




		case 12:			
			return new LevelDescriptor(lvl,1,5,500,350,150,"A brave new world",false);
		case 13:			
			return new LevelDescriptor(lvl,2,8,550,400,250,"Cohorte forward!",false);
		case 14:			
			return new LevelDescriptor(lvl,3,8,550,300,250,"Pawnfully hard",false);
		case 15:			
			return new LevelDescriptor(lvl,3,8,800,500,250,"Philidors revenge",false);
		case 16:			
			return new LevelDescriptor(lvl,3,15,500,250,150,"Pure Tactics",false);		
		case 17:			
			return new LevelDescriptor(lvl,3,6,500,250,150,"Puzzles on time",false);
		case 18:			
			return new LevelDescriptor(lvl,0,7,500,250,150,"Perfect or death",false);
		case 19:			
			return new LevelDescriptor(lvl,3,15,500,250,150,"Pure Tactics",false);		
		case 20:			
			return new LevelDescriptor(lvl,2,6,500,250,150,"Bishops and a Knight",false);
		case 21: 
			return new LevelDescriptor(lvl,3,10,500,250,150,"Calm before the storm",false);
		case 22:			
			return new LevelDescriptor(lvl,3,6,500,250,150,"Puzzles on time",false);
		case 23: 
			return new LevelDescriptor(lvl,3,8,500,250,150,"Knight fight",false);
		case 24:			
			return new LevelDescriptor(lvl,1,9,700,400,150,"One fault allowed",false);
		case 25:			
			return new LevelDescriptor(lvl,0,1,700,400,100,"Two Bishops Mate",false);
		case 26:			
			return new LevelDescriptor(lvl,3,12,800,250,150,"Difficult Tactics",false);		
		case 27:			
			return new LevelDescriptor(lvl,3,20,1000,750,500,"Ultimate Challenge",false);
		case 28:			
			return new LevelDescriptor(lvl,0,1,500,250,150,"Catch the King!",false);


		}
		return null;

	}
	//Abstract marker root class
	public static class LevelDescriptor {
		public static final int INFINITY = 1000000;
		private int excellentS,goodS,poorS,noOfProblems,level,noOfFailsAllowed;
		private int timeG,timeY,timeR;
		public boolean isRated=false;
		String levelDescription;

		public LevelDescriptor(int level,
				int noOfFailsAllowed,int noOfProblems, 
				int excellentS, int goodS, int poorS,String levelDescription,boolean rated) {
			this.level = level;
			this.noOfFailsAllowed = noOfFailsAllowed;
			this.noOfProblems = noOfProblems;
			this.excellentS = excellentS;
			this.goodS = goodS;
			this.poorS = poorS;
			this.levelDescription = levelDescription;
			this.isRated=rated;

			//problems = ResourceManager.getInstance().db.getTacticProblems(noOfProblems,minProbLvl,maxProbLvl);
		}		
		public StageDescriptor getStageDescriptor(int stage) {
			return StageDescriptorFactory.getStageDescriptor(this,stage);
		}

		public int getExcellentS() {
			return excellentS;
		}

		public int getGoodS() {
			return goodS;
		}

		public int getPoorS() {
			return poorS;
		}

		public int getLevel() {
			return level;
		}

		public int getNoOfFailsAllowed() {
			return noOfFailsAllowed;
		}


		public int getNoOfProblems() {
			return noOfProblems;
		}
		public String getLevelDescription() {
			return levelDescription;
		}

	}

	public static class StageDescriptorFactory {

		private static int NO_MOVE_LIMIT = 100000;
		public static class StageDescriptor {
			public enum Type {
				Tactics,FindBestMove,Game,MatePuzzle, 
			}

			Type mType;
			MoveList ml;
			boolean playerStarts = false;
			String pStatement="";
			private int timeG,timeY,timeR,moveLimit=NO_MOVE_LIMIT,problemDifficulty;
			private int[] specialBonuses=null;
			private boolean isTimeBonus = true;
			private int moveG;
			private int moveR;
			private int moveY;
			private static Random rand = new Random();


			public StageDescriptor(Type t,MoveList ml,int timeG, int timeY, int timeR,boolean plF,String pStatement, int moveLimit, int problemDifficulty) {
				this.ml = ml;
				mType = t;
				playerStarts = plF;
				this.pStatement=pStatement;
				this.timeG=timeG;
				this.timeY= timeY;
				this.timeR=timeR;
				if(moveLimit>0) {
					this.moveLimit=moveLimit;
				}
				this.problemDifficulty=problemDifficulty;
			}

			public void setSpecialScore(int ...bonuses) {
				specialBonuses = bonuses;			
			}

			public void setMoveLimits(int moveG,int moveY, int moveR) {
				isTimeBonus = false;
				this.moveG=moveG;
				this.moveY=moveY;
				this.moveR=moveR;
			}

			public void setProblemStatement(String special) {
				pStatement = special;
			}

			public MoveList getMoveList() {
				return ml;
			}

			public Type getType(){
				return mType;
			}
			public String getProblemStatement() {
				return pStatement;
			}

			public int getMoveLimit() {
				return moveLimit;
			}

			public int getProblemDifficulty() {
				return problemDifficulty;
			}

			public int getTimeG() {
				return timeG;
			}

			public int getTimeY() {
				return timeY;
			}

			public int getTimeR() {
				return timeR;
			}

			public boolean isTimed() {
				return (timeG+timeY+timeR)!=0;
			}

			public boolean hasMoveLimit() {
				return moveLimit!=NO_MOVE_LIMIT;
			}

			public int bonus(int bonusFactor) {
				int bonus = 0;
				if (specialBonuses!=null) {	
					if (isTimeBonus) {
						Log.d("schack","Time was "+bonusFactor);
						if (bonusFactor <= timeG)
							bonus+=specialBonuses[0];
						else if (bonusFactor <= timeY)
							bonus += specialBonuses[1];
						else if (bonusFactor <= timeR)
							bonus += specialBonuses[2];
					} else
					{
						if (bonusFactor >=moveG) 
							bonus += specialBonuses[0];
						else if (bonusFactor >=moveY)
							bonus +=specialBonuses[1];
						else if (bonusFactor >=moveR)
							bonus +=specialBonuses[2];

					}
				}
				Log.d("schack","....gives bonus "+bonus);
				return bonus;	
			}
		}


		public static StageDescriptor getStageDescriptor(LevelDescriptor ld, int stage) {
			switch (ld.getLevel()) {
			case 1:		
				switch(stage) {
				case 5:
					return generateGame(ChessConstants.DOUBLE_QUEEN_MATE_BOARD,"Win before time runs out!",30,30,30,false,false,0);
				default:
					return generateTactic(ld.getLevel());
				}
			case 2:
				switch(stage) {
				case 1:
					return generatePuzzle(2);
				case 5:
					return generateGame(ChessConstants.QUEEN_MATE_BOARD,"Win before time runs out!",30,30,30,false,false,0);
				default:
					return generateTactic(ld.getLevel());
				}
			case 3:
				switch(stage) {
				case 1:
					return generatePuzzle(2);	
				case 2:
					return generatePuzzle(2,30,15,15);	
				case 5:
					return generateGame(ChessConstants.TWO_ROOK_MATE_BOARD,"Roll'em down!",30,15,10,false,false,0);					
				default:
					return generateTactic(ld.getLevel());
				}
			case 4:
				switch(stage) {
				case 1:
					return generateGame(ChessConstants.PAWN_WALL,"Win before time runs out!",30,30,30,false,false,0);					
				case 8:
					return generatePuzzle(3,30,15,15);	
				default:
					return generateTactic(ld.getLevel());

				}
			case 5:
				switch(stage) {
				case 5:
					return generateGame(ChessConstants.ONE_ROOK_MATE_BOARD,"Win before time runs out!",30,15,10,false,false,0);					
				default:
					return generateTactic(ld.getLevel());
				}
			case 6:
				switch(stage) {
				case 2:
					return generatePuzzle(2);	
				case 5:
					return generatePuzzle(2);	
				case 7:
					return generateGame(ChessConstants.TWO_PAWN_BOARD,"Promote and win. Good Luck!",30,30,30,false,false,0);		
				case 10: 
					return generateTactic(1000,"Find best move.\nBe careful, this one is tricky.");
				default:
					return generateTactic(ld.getLevel());
				}			
			case 7:
				switch(stage) {
				case 10:
					return generateTactic(1000,"Find best move.\nBe careful, this one is tricky.");

				default:
					return generateTactic(ld.getLevel());
				}
			case 8:
				switch(stage) {
				case 9: 
					return generatePuzzle(2);
				case 10:
					return generateGame(ChessConstants.PAWN_PROMOTE_BOARD,"Win before time runs out!",30,30,30,false,false,0);					
				default:
					return generateTactic(ld.getLevel());
				}			
			case 9:
				switch(stage) {
				case 10:
					StageDescriptor sd = generateGame(ChessConstants.ROOK_PAWN_PAWNS,"Win game within 23 moves!",0,0,0,true,false,23);
					sd.setSpecialScore(250,150,100);
					sd.setMoveLimits(20,21,23);
					return sd;
				default:
					return generateTactic(ld.getLevel());
				}			

			case 10:
				switch(stage) {
				case 6:
					return generatePuzzle(3);	
				case 7:
					StageDescriptor sd = generatePuzzle(3,60,60,30);	
					sd.setSpecialScore(500,250,150);
					sd.setProblemStatement("Warning - this is on time!\n"+((sd.getMoveList().getFirst().whiteToMove)?"White":"Black")+" mates in 3");
					return sd;
				case 8:
					sd = generatePuzzle(3,60,60,30);	
					sd.setSpecialScore(500,250,150);
					sd.setProblemStatement("Warning - this is on time!\n"+((sd.getMoveList().getFirst().whiteToMove)?"White":"Black")+" mates in 3");
					return sd;
				default:
					return generatePuzzle(2);
				}		

			case 11:				
				StageDescriptor sd = generateGame(ChessConstants.KING_CHALLENGE_1,"You are facing the black king.\nDon't let him get away!",20,30,30,true,false,18);
				sd.setSpecialScore(800,400,300);
				sd.setMoveLimits(3,2,1);
				return sd;


				//////////////////////////////////SCENE 2 LEVELS

			case 12:
				switch(stage) {
				case 5:
					sd = generatePuzzle(3,45,45,45);	
					sd.setSpecialScore(350,200,100);
					sd.setProblemStatement("Solve for high bonus!\n"+((sd.getMoveList().getFirst().whiteToMove)?"White":"Black")+" mates in 3");
					return sd;
				default:
					return generateTactic(ld.getLevel());
				}
			case 13:
				switch(stage) {
				case 1:
					sd = generateGame(ChessConstants.COHORT_BOARD,"Forward troops!",45,45,30,true,true,23);
					sd.setSpecialScore(150,100,50);
					return sd;

				case 8:
					sd = generatePuzzle(3);
					sd.setSpecialScore(150,100,50);
					return sd;
				default:
					return generateTactic(ld.getLevel());
				}

			case 14:
				switch(stage) {
				case 1:
					sd = generateGame(ChessConstants.FULL_PAWN_BOARD,"P*wned!",0,0,0,true,true,50);
					sd.setMoveLimits(15, 5, 0);
					sd.setSpecialScore(350,300,150);
					return sd;
				default:
					return generateTactic(ld.getLevel());
				}
			case 15:
				switch(stage) {

				case 1:
					sd = generateGame(ChessConstants.PHILIDOR_BOARD,"Solve the Philidor",45,30,30,true,true,50);
					sd.setSpecialScore(250,150,100);
					sd.setMoveLimits(22, 20, 0);
					return sd;
				case 2:
				case 5:
					return generatePuzzle(2);
				default:
					return generateTactic(ld.getLevel());
				}
			case 17:
				Random r = new Random();
				if (r.nextBoolean()) 
					sd = generatePuzzle(2,15,20,15);
				else
					sd = generatePuzzle(3,60,60,30);
				return sd;
			case 20:
				switch(stage) {

				case 5:
					return generatePuzzle(3);
				case 6:
					sd = generateGame(ChessConstants.TWO_BISHOPS_AND_KNIGHT_MATE,"Mate the black king",0,0,0,true,true,50);
					sd.setSpecialScore(800,500,250);
					sd.setMoveLimits(20, 15, 0);
					return sd;	

				default:
					return generateTactic(ld.getLevel()); 
				}


			case 22:
				r = new Random();
				if (r.nextBoolean()) 
					sd = generatePuzzle(2,15,15,15);
				else
					sd = generatePuzzle(3,60,45,30);
				return sd;
			case 23:
				switch(stage) {

				case 5:
				case 6:
				case 7:
					return generatePuzzle(3);
				case 8:
					sd = generateGame(ChessConstants.THREE_KNIGHT_MATE,"Three gringos",0,0,0,true,true,35);
					sd.setSpecialScore(800,500,250);
					sd.setMoveLimits(10, 5, 0);
					return sd;						
				default:
					return generateTactic(ld.getLevel()); 
				}

			case 25:
				sd = generateGame(ChessConstants.TWO_BISHOP_MATE,"Mate the black king",0,0,0,true,true,50);
				sd.setSpecialScore(800,500,250);
				sd.setMoveLimits(29, 15, 0);
				return sd;

			case 27:
				r = new Random();
				if (r.nextBoolean()) {
					if (r.nextBoolean())
						sd = generatePuzzle(2,15,15,15);
					else
						sd = generatePuzzle(3,30,30,30);
					return sd;
				} else
					return generateTactic(ld.getLevel());


			case 28:
				sd = generateGame(ChessConstants.KING_CHALLENGE_2,"Catch the King!",0,0,0,true,true,50);
				sd.setSpecialScore(800,500,250);
				sd.setMoveLimits(30, 15, 0);
				return sd;

			
			default:
				return generateTactic(ld.getLevel());

			}
		}

		/*@Override
		public MoveList get(int current) {
		}
		 */

		private static StageDescriptor generateGame(int[][] board,String pStatement,int timeG, int timeY, 
				int timeR, boolean playerIsWhite,boolean playerStarts,int moveLimit) {
			MoveList ml = new MoveList();
			ml.add(new GameState(board,playerIsWhite==playerStarts));
			return new StageDescriptor(Type.Game,ml,timeG,timeY,timeR,playerStarts,pStatement,moveLimit,0);
		}

		private static StageDescriptor generateTactic(int difficultyLevel, String specialDescription) {
			StageDescriptor standard = generateTactic(difficultyLevel);
			standard.setSpecialScore(200,100,50);
			standard.setProblemStatement(specialDescription);
			return standard;
		}

		private static StageDescriptor generateTactic(int difficultyLevel) {
			int min,max;
			int g,y,r;
			switch(difficultyLevel) {
			case 1:
				min=0;
				max=1000;
				g=y=r=20;
				break;
			case 2:
				min=1000;
				max=1100;
				g=y=r=20;
				break;
			case 3:
				min=1000;
				max=1200;
				g=y=r=15;
				break;
			case 4:
				min=1000;
				max=1400;
				g=y=r=15;
				break;
			case 5:
				min=1000;
				max=1400;
				g=10;y=r=12;
				break;
			case 6:
			case 7:
			case 8:
				min=1100;
				max=1300;
				g=12;y=r=12;
				break;
			case 9:
			case 10:
				min=1100;
				max=1400;
				g=y=r=12;
				break;
			case 12:
				min=1200;
				max=1400;
				g=10;y=10;r=15;
				break;	
			case 13:
				min=1300;
				max=1500;
				g=10;y=10;r=15;
				break;	
			case 14:
				min=1000;
				max=1500;
				g=5;y=15;r=10;
				break;	
			case 16:
			case 18:
				min=1350;
				max=1850;
				g=5;y=15;r=10;
				break;	
			case 19:
				min=1400;
				max=1950;
				g=5;y=15;r=10;
				break;

			case 27:
				min=1600;
				max=3000;
				g=5;y=15;r=10;
				break;

			case 1000:
				min=1500;
				max=1700;
				g=y=r=15;
				break;

			case TRAINING_LD:
				int playerRating = (int)ResourceManager.getInstance().getRating();
				min = playerRating - 100;
				max = playerRating + 100;
				g=y=r=10;
				Log.d("schack","min max"+min+" "+max);
				break;
			default:
				min=0000;
				max=3000;
				g=y=r=12;
			}
			//harder problems if master.
			if (ResourceManager.getInstance().isMaster()) {
				min = min + 150;
				max = max + 150;
			}
			TacticProblem problem = ResourceManager.getInstance().db.getTacticProblem(min,max);
			int[][] pos = Tools.translateBoard(problem.board);
			MoveList ml = new MoveList();
			ml.add(new GameState(pos,problem.whiteToMove));
			Tools.addMoves(problem.moves,ml);

			if (ml.size() < 2)
				System.err.println("too few moves!");	
			ml.resetMovePointer();		
			return new StageDescriptor(Type.Tactics,ml,g,y,r,false,"Find the best move\nTime: "+(g+y+r)+"s, Level:"+
					(problem.rating<1000?" Novice":
						(problem.rating<1250?" Easy":
							(problem.rating<1400?" Normal":
								(problem.rating<1500?" Hard":
									(problem.rating<1600?" Advanced":
										(problem.rating<1700?" Expert":
											(problem.rating<1900?" Master":" Grand Master"))))))),0,problem.rating);
		}

		private static StageDescriptor generatePuzzle(int moves, int ...t) {
			if (t==null||t.length<3) 
				t = new int[3];

			MoveList ml = null;
			do {
				MatePuzzle mp = ResourceManager.getInstance().db.getMatePuzzle(moves);
				ml = GameAnalyzer.createGameFromFen(mp.fen, mp.moves);
			} while (ml==null);
			StageDescriptor sd = new StageDescriptor(Type.MatePuzzle,ml,t[0],t[1],t[2],true,((ml.getFirst().whiteToMove)?"White":"Black")+" mates in "+moves+"!",moves,0);
			sd.setSpecialScore(175,120,100,75);
			return sd;
		}
	}





}





