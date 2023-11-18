package com.terje.chesstacticstrainer_full;

import java.util.HashMap;

public final class ChessConstants {

	public static final String FEN_STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq";

	public static final String[] pieceNames = {"empty","white king", "white queen", "white bishop", "white knight","white rook","white pawn","black king","black queen","black bishop","black knight","black rook","black pawn"};
	public static final String[] pieceCharNames = {"x","K","Q","B","N","R","P","k","q","b","n","r","p"};
	public static final int BOARD_PIXEL_SIZE = 400;
    
	public static final int B_EMPTY = 0;
    public static final int W_KING = 1;
    public static final int W_QUEEN = 2;
    public static final int W_BISHOP = 3;
    public static final int W_KNIGHT = 4;
    public static final int W_ROOK = 5;
    public static final int W_PAWN = 6;
    public static final int B_KING = 7;
    public static final int B_QUEEN = 8;
    public static final int B_BISHOP = 9;
    public static final int B_KNIGHT = 10;
    public static final int B_ROOK = 11;
    public static final int B_PAWN = 12;

    public static final int[] pieceValue = {0,0,10,3,3,5,1,0,10,3,3,5,1};
    
    public static final int[] T1 = {B_EMPTY,B_PAWN,B_PAWN,B_PAWN,W_PAWN,B_EMPTY,B_ROOK,B_EMPTY};
    public static final int[] T2 = {B_KING,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] T3 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] T4 = {B_ROOK,B_EMPTY,B_EMPTY,W_BISHOP,B_EMPTY,B_EMPTY,B_ROOK,B_EMPTY};
    public static final int[] T5 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] T6 = {B_EMPTY,B_EMPTY,W_KNIGHT,W_BISHOP,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] T7 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] T8 = {W_ROOK,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_KNIGHT,W_KING};

    
  
    public static final int[] Z1 = {B_KING, B_EMPTY, B_EMPTY,B_ROOK,B_BISHOP, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z2 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z3 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z4 = {B_BISHOP, B_EMPTY, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z5 = {B_KNIGHT, B_EMPTY, W_PAWN, W_PAWN, W_PAWN, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z6 = {B_EMPTY, B_EMPTY, W_PAWN,W_KING, W_PAWN, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z7 = {B_EMPTY, B_EMPTY, W_PAWN, W_PAWN, W_PAWN, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] Z8 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[][] COHORT_BOARD = {Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8};
    
    
    public static final int[] R1 = {B_ROOK, B_PAWN, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, W_PAWN, W_ROOK};
    public static final int[] R2 = {B_KNIGHT, B_PAWN, B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, W_PAWN, W_KNIGHT};
    public static final int[] R3 = {B_BISHOP,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_PAWN,W_BISHOP};
    public static final int[] R4 = {B_QUEEN,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_PAWN,W_QUEEN};
    public static final int[] R5 = {B_KING,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_PAWN,W_KING};   
    public static final int[] M1 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] M2 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_QUEEN, B_KING};
    public static final int[] M3 = {B_EMPTY,B_EMPTY,B_EMPTY,W_KING,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] M4 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_ROOK};
    public static final int[] M6 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_KING};
    public static final int[] M5 = {B_EMPTY, W_KING, B_EMPTY, B_EMPTY,B_EMPTY, B_KING, B_PAWN, B_EMPTY};   
    public static final int[] M7 = {B_EMPTY, B_QUEEN, B_EMPTY, B_EMPTY,B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY};
    public static final int[] M8 = {B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_EMPTY, B_QUEEN, B_QUEEN};
    public static final int[] M9 = {B_EMPTY,B_EMPTY,B_EMPTY,B_KING,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] M10 ={B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_ROOK};
    public static final int[] M11 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] M12 = {B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] M13 = {B_KING,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_PAWN,W_PAWN,W_KING};
    public static final int[] M14 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_ROOK};

 
    public static final int[] S1 = {B_EMPTY,W_ROOK,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S2 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S4 = {B_EMPTY,B_EMPTY,B_EMPTY,W_KING,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S5 = {B_KING,B_EMPTY,B_EMPTY,W_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S6 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_ROOK,B_EMPTY,B_EMPTY,B_EMPTY};

    public static final int[] S7 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_BISHOP};
    public static final int[] S8 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_KING,B_EMPTY,B_EMPTY,W_KING};
    public static final int[] S9 = {B_KING,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_KING};
    public static final int[] S10 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_KNIGHT};
    public static final int[] S11 = {B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,W_PAWN,B_EMPTY,B_EMPTY,B_EMPTY};

    public static final int[] S12 = {B_EMPTY,B_PAWN,B_PAWN,B_PAWN,W_PAWN,W_PAWN,W_PAWN,B_EMPTY};
    public static final int[] S13 = {B_KING,B_PAWN,B_PAWN,B_PAWN,W_PAWN,W_PAWN,W_PAWN,W_KING};
    public static final int[] S14 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,W_KNIGHT};

    public static final int[] S15 = {B_EMPTY,W_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,W_KING,B_EMPTY,B_EMPTY};
    public static final int[] S16 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S17 = {B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S18 = {B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S19 = {B_EMPTY,B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S20 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY,B_EMPTY};
    public static final int[] S21 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_PAWN,B_EMPTY,B_EMPTY};
    public static final int[] S22 = {B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_EMPTY,B_KING,B_PAWN,B_EMPTY};
     
    public static final int[] S23 = {B_KING,B_EMPTY,B_EMPTY,B_EMPTY,W_KING,B_EMPTY,B_EMPTY,B_EMPTY};
    
    /*
	 * xxxxxxxx
	 * xxxxxxxx
	 * xxxxKxxp
	 * PxxxxQxk
	 * xxxxxxxp
	 * xxxPxPxp
	 * xxxxPxxP
	 * xxxxxxxx
	 */
    
    public static final int[] P_PROM = {B_EMPTY, B_EMPTY, B_EMPTY,B_PAWN,B_KING,B_EMPTY,W_KING,B_EMPTY};
    public static final int[][] INITIAL_BOARD = {R1,R2,R3,R4,R5,R3,R2,R1};
    public static final int[][] QUEEN_MATE_BOARD = {M1,M2,M3,M1,M1,M1,M1,M1};
    public static final int[][] DOUBLE_QUEEN_MATE_BOARD = {M1,M2,M3,M1,M1,M1,M1,M8};
    public static final int[][] TWO_ROOK_MATE_BOARD = {M4,M4,M6,M1,M1,M3,M1,M1};   
    public static final int[][] ONE_ROOK_MATE_BOARD = {M4,M1,M6,M3,M1,M1,M1,M1};   
    public static final int[][] PAWN_PROMOTE_BOARD = {M1,M1,M1,P_PROM,M1,M1,M1,M1};
    public static final int[][] KING_CHALLENGE_1 =  {M3,M1,M1,M9,M1,M1,M1,M10};
    public static final int[][] TWO_PAWN_BOARD = {M3,M1,M1,M9,M11,M1,M1,M10};
    public static final int[][] ROOK_PAWN_PAWNS = {M14,M1,M1,M1,M1,M12,M13,M12};
    public static final int[][] PHILIDOR_BOARD = {S1,S2,S2,S4,S5,S6,S2,S2};
    public static final int[][] TWO_BISHOP_MATE = {S2,S2,S7,S8,S2,S7,S2,S2};
    public static final int[][] TWO_BISHOPS_AND_KNIGHT_MATE = {S2,S2,S7,S8,S14,S7,S2,S2};
    public static final int[][] THREE_KNIGHT_MATE = {S2,S2,S10,S9,S10,S10,S2,S2};
    public static final int[][] PAWN_WALL = {S2,S2,S2,S2,S23,S11,S11,S11};
    public static final int[][] FULL_PAWN_BOARD = {S12,S12,S12,S13,S12,S12,S12,S12};
    public static final int[][] KING_CHALLENGE_2 = {S15,S16,S17,S18,S19,S20,S21,S22};
    
    
        
    
    public static final int[] PIECE_BOX = {B_ROOK,B_ROOK,B_KNIGHT,B_KNIGHT,B_BISHOP,B_BISHOP,B_QUEEN,B_KING,B_PAWN,B_PAWN,B_PAWN,B_PAWN,B_PAWN,B_PAWN,B_PAWN,B_PAWN,
    	W_ROOK,W_ROOK,W_KNIGHT,W_KNIGHT,W_BISHOP,W_BISHOP,W_QUEEN,W_KING,W_PAWN,W_PAWN,W_PAWN,W_PAWN,W_PAWN,W_PAWN,W_PAWN,W_PAWN};
    public static final String[] pieceShortName = {"","K","Q","B","N","R","","K","Q","B","N","R",""};
    public static final String[] pieceNotationPicName = {"","wk.gif","wq.gif","wb.gif","wn.gif","wr.gif","","bk.gif","bq.gif","bb.gif","bn.gif","br.gif"};
    public static final String[] pieceImgNames = {"dummy", "w_king","w_queen","w_bishop","w_knight","w_rook","w_pawn","b_king","b_queen","b_bishop","b_knight","b_rook","b_pawn"};
    public static final String[] pieceStyle = {"normal","fancy"};
    public final static boolean isWhite(int type) {
    	return ((type < B_KING) && (type > B_EMPTY));
    }
    
	public final static boolean sameColor(int p1, int p2) {
		return (p1>ChessConstants.W_PAWN)?(p2>ChessConstants.W_PAWN):(p2<=ChessConstants.W_PAWN);
	}
	
    public final static boolean isKnight(int type) {
    	return (type == B_KNIGHT || type == W_KNIGHT);
    }
    public final static boolean isBishop(int type) {
    	return (type == B_BISHOP || type == W_BISHOP);
    }
    public final static boolean isQueen(int type) {
    	return (type == B_QUEEN || type == W_QUEEN);
    }
	public final static boolean isRook(int type) {
	   	return (type == B_ROOK || type == W_ROOK);
	}
	public final static boolean isPawn(int type) {
	   	return (type == B_PAWN || type == W_PAWN);
	}
	public final static boolean isKing(int type) {
	   	return (type == B_KING || type == W_KING);
	}
	public final static boolean isEmpty(int type) {
		return (type==B_EMPTY);
	}
	public final static int getQueen(boolean color) {
		return color?W_QUEEN:B_QUEEN;
	}
	public final static int getBishop(boolean color) {
		return color?W_BISHOP:B_BISHOP;
	}
	public final static int getRook(boolean color) {
		return color?W_ROOK:B_ROOK;
	}
	public final static int getKnight(boolean color) {
		return color?W_KNIGHT:B_KNIGHT;
	}
	public final static int getPawn(boolean color) {
		return color?W_PAWN:B_PAWN;
	}
	public final static int getKing(boolean color) {
		return color?W_KING:B_KING;
	}
	
	public static int convertToInt (char ch) {
		int intType=ChessConstants.B_EMPTY;
		switch (ch) {
		case 'p':
			intType = ChessConstants.B_PAWN;
			break;
		case 'P':
			intType = ChessConstants.W_PAWN;
			break;
		case 'k':
			intType = ChessConstants.B_KING;
			break;	
		case 'K':
			intType = ChessConstants.W_KING;
			break;
		case 'q':
			intType = ChessConstants.B_QUEEN;
			break;
		case 'Q':
			intType = ChessConstants.W_QUEEN;
			break;
		case 'b':
			intType = ChessConstants.B_BISHOP;
			break;
		case 'B':
			intType = ChessConstants.W_BISHOP;
			break;
		case 'n':
			intType = ChessConstants.B_KNIGHT;
			break;
		case 'N':
			intType = ChessConstants.W_KNIGHT;
			break;
		case 'r':
			intType = ChessConstants.B_ROOK;
			break;
		case 'R':
			intType = ChessConstants.W_ROOK;
			break;
		}
		return intType;
	}

	
	public static enum Color {
		white,
		black,
		anyColor;
	}
	
	
	public final static String[][] countries = {{"Afghanistan", "AF"}, {"Albania", "AL"}, {"Algeria", "DZ"}, {"Andorra", "AD"}, {"Angola", "AO"}, {"Antigua Barbuda", "AG"}, {"Argentina","AR"},{"Armenia","AM"},{"Aruba","AW"},{"Australia","AU"},{"Austria","AT"},{"Azerbaijan","AZ"},
		{"Bahamas","BS"},{"Bahrain","BH"}, {"Bangladesh","BD"},{"Barbados","BB"},{"Belarus","BY"},{"Belgium","BE"},{"Benin", "BJ"},{"Bhutan","BT"},{"Bolivia","BO"},{"Bosnia Herzegovina","BA"},{"Botswana","BW"},{"Brazil","BR"},{"Bulgaria","BG"},{"Burkina Faso","BF"},{"Burundi","BI"},
		{"Cambodia","KH"},{"Cameroon","CM"},{"Canada","CA"},{"Chad","TD"},{"Chile","CL"},{"China","CN"},{"Colombia","CO"},{"Comoros","KM"},{"Congo","CG"},{"CostaRica","CR"},{"Croatia","HR"},{"Cuba","CU"},{"Cyprus","CY"},{"CzechRepublic","CZ"}, 
		{"Denmark","DK"},{"Djibouti","DJ"},{"Dominica","DM"},{"Dominican Republic","DO"},
		{"Ecuador","EC"},{"Egypt","EG"},{"El Salvador","SV"},{"Equatorial Guinea","GQ"},{"Eritrea","ER"},{"Estonia","EE"},{"Ethiopia","ET"},
		{"Faeroe Islands","FO"},{"Finland","FI"},{"France","FR"},{"French Polynesia","PF"},{"Gabon","GA"},{"Georgia","GE"},{"Germany","GE"},{"Ghana","GH"},{"Gibraltar","GI"},{"Greece","GR"},{"Greenland","GL"},{"Grenada","GD"},{"Guam","GU"},{"Guatemala","GT"},{"Guinea","GN"},
		{"Haiti","HT"},{"Honduras","HN"},{"Hong Kong","HK"},{"Hungary","HU"},{"Iceland","IS"},{"India","IN"},{"Indonesia","ID"},{"Iran","IR"},{"Iraq","IQ"},{"Ireland","IE"},{"Israel","IL"},{"Italy","IT"},{"IvoryCoast","CI"},
		{"Jamaica","JM"},{"Japan","JP"},{"Jordan","JO"},{"Kazakhstan","KZ"},{"Kenya","KE"},{"Kiribati","KI"},{"Kuwait","KW"},{"Kyrgyzstan","KG"},
		{"Laos","LA"},{"Latvia","LV"},{"Lebanon","LB"},{"Liberia","LR"},{"Libya","LY"},{"Lithuania","LT"},{"Luxembourg","LU"},
		{"Macedonia","MK"},{"Madagascar","MG"},{"Malawi","MW"},{"Malaysia","MY"},{"Maldives","MV"},{"Mali","ML"},{"Malta","MT"},{"Mauritania","MR"},{"Mauritius","MU"},{"Mexico","MX"},{"Micronesia","FM"},{"Moldova","MD"},{"Mongolia","MN"},{"Montenegro","ME"},{"Morocco","MA"},{"Mozambique","MZ"},
		{"Nepal","NP"},{"Netherlands","NL"},{"NewZealand","NZ"},{"Nicaragua","NI"},{"Niger","NE"},{"Nigeria","NG"},{"North Korea","KP"},{"Norway","NO"},{"Oman","OM"},
		{"Pakistan","PK"},{"Palau","PW"},{"Panama","PA"},{"Paraguay","PY"},{"Peru","PE"},{"Philippines","PH"},{"Poland","PL"},{"Portugal","PT"},{"Puerto Rico","PR"},
		{"Qatar","QA"},{"Romania","RO"},{"Russia","RU"},{"Rwanda","RW"},
		{"Saint Lucia","LC"},{"Saudi Arabia","SA"},{"Senegal","SN"},{"Serbia","RS"},{"Sierra Leone","SL"},{"Singapore","SG"},{"Slovakia","SK"},{"Slovenia","SI"},{"Somalia","SO"},{"South Africa","ZA"},{"South Korea","KR"},{"Spain","ES"},{"Sri Lanka","LK"},{"Swaziland","SZ"},{"Sweden","SE"},{"Switzerland","CH"},{"Syria","SY"},
		{"Taiwan","TW"},{"Tajikistan","TJ"},{"Thailand","TH"},{"Togo","TG"},{"Tonga","TO"},{"Trinidad Tobago","TT"},{"Tunisia","TN"},{"Turkey","TR"},{"Turkmenistan","TM"},
		{"Uganda","UG"},{"Ukraine","UA"},{"United Kingdom","GB"},{"United Nations","UN"},{"United States","US"},{"Uruguay","UY"},{"Uzbekistan","UZ"},{"Venezuela","VE"},{"Viet Nam","VN"},{"Yemen","YE"},{"Zambia","ZM"},{"Zimbabwe","ZW"}};

	
	
	//references into array 
	public final static int Luckiest_dude_alive = 0;
	public final static int Endgame_champ = 1;
	public final static int Tactician = 2;
	public final static int Opening_champ = 3;
	public final static int Strategist = 4;
	public final static int Speedy_gonzales = 5;
	public final static int Trickster = 6;
	public final static int Midgame_champ = 7;
	public final static int Death_before_dishonour = 8;
	public final static int Defender = 9;
	
	public final static int NO_OF_AWARD_TYPES = 10;
	public final static String[] Awards = {"Luckiest dude alive","Endgame champ","Tactician","Opening champ",
		"Strategist","Speedy Gonzales","Trickster","Midgame champ","Klingon Lord","Arrrgh!","No Comment"};
			
	public final static String[] gameComments = {"Beautiful game","Stupid mistake decided","Boring, boring...","Time stress decided",
		"This won't go to history", "I was lucky to win", "Combat victory", "Nice combination won!", "Game decided in opening", 
		"Game decided in midgame", "Game decided in endgame", "No Comment"};
	public final static int NO_COMMENT = 11;
	
	
	public final static String[] Advice = {"Need more endgame training","Doubtful opening play","Should be more careful",
			"Too impatient","Too little initiative","Doubtful pawn structure","Unwise exchanges","Should study tactics","You must have a plan!",
			"Strong play, but many mistakes","Strategy is important!","No Comment"};

	
	
	public final static HashMap<String, Pair> countryHash = new HashMap<String, Pair>();

	private final static class Pair {
		String  country;
		int index;
		Pair(String co, int ind) {
			country=co;
			index = ind;
		}
	}
	public final static HashMap<String, Pair>getCountryHash() {
		if (countryHash.isEmpty()) {
		    for (int x = 0; x < countries.length; x++) {
		        String key = countries[x][1];
		        Pair value = new Pair(countries[x][0],x);
		        countryHash.put(key, value);
		      }
		}
		return countryHash;
	}
	

	public final static String getCountryFromCode(String code) {
		Pair p = getCountryHash().get(code);
		if (p!=null) 
			return p.country.replace(" ", "").toLowerCase();
		
		else {
			System.out.println("didnt find country code: "+code);
			return "sweden";
		}
	}
	
	public final static int getCountryIndex(String code) {
		Pair p = getCountryHash().get(code);
		if (p!=null) 
			return p.index;
			
		else
			return 0;
	}
	
	public final static String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	

	
}