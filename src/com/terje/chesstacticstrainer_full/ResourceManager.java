package com.terje.chesstacticstrainer_full;


import java.io.IOException;
import java.util.UUID;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.LevelDescriptor;
import com.terje.chesstacticstrainer_full.LevelDescriptorFactory.StageDescriptorFactory.StageDescriptor.Type;
import com.terje.chesstacticstrainer_full.Tools.PersistenceHelper;
import com.terje.chesstacticstrainer_full.Types.TacticResult;
import com.terje.chesstacticstrainer_full.WebService.WsCallback;


public class ResourceManager {

	static ResourceManager INSTANCE;

	//The root folder for the SD card is in the global Environment.
	private final static String path = Environment.getExternalStorageDirectory().getPath();


	public static final String GAME_ROOT_DIR = path+"/chesstactics/";



	private static final String KEY_PLAYER_NAME = "PLAYER_NAME";
	private static final String KEY_SCORE_LEVEL = "PLAYER_SCORE_LEVEL_";
	private static final String KEY_HIGHEST_LEVEL = "HIGHEST_LEVEL";
	private static final String KEY_PLAYER_SCENE = "PLAYER_SCENE";
	private static final String KEY_MY_ID = "MY_ID";
	private static final String KEY_VISIT_COUNTER = "VISIT_COUNTER";
	private static final String KEY_ASK_TO_RATE = "ASK_TO_RATE";
	private static final String KEY_TRICKIEST = "TRICKIEST";
	private static final String KEY_WOOS = "WOOS";
	private static final String KEY_LONGEST_GREEN = "LONGEST_GREEN";
	private static final String KEY_TOTAL_SOLVED = "TOTAL_SOLVED";
	private static final String KEY_MY_RATING = "M_R";
	private static final String KEY_MY_COUNTRY = "M_C";
	private static final String KEY_MY_RD = "M_RD";
	private static final String KEY_HAS_BEEN_ASKED_NAME = "NAME_ASKED";
	private static final String KEY_HAS_BEEN_REGISTERED = "HAS_BEEN_REGISTERED";
	private static final String KEY_TRAINING_LOCKED = "TRAINING_IS_LOCKED";
	



	private static final String KEY_KING_HAS_FALLEN = "KING_FELL_LVL_";


	private static final String KEY_FIRST_TIME_IN_GAME_ENTRY = "FIRST_GE";
	private static final String KEY_FIRST_TIME_PUZZLE = "FIRST_P";
	private static final String KEY_FIRST_TIME_GAME = "FIRST_G";
	private static final String KEY_FIRST_TIME_TACTICS = "FIRST_T";
	





	//RGB(255,127,39)
	public static final Color mBackgroundColor = new Color(1f,.498f,.152f); 
	public static final Color silver = new Color(.89f,.89f,.90f);// (RGB: 227, 228, 229)
	public static final int intBgColor = 0xFD20;//16337664; //F94B00



	public static ChessBoard chessboard;



	public Text first_time_header;
	public Text first_time_bread;
	public Text first_time_countrySelectHeader;


	public Rectangle first_time_bg_rect;

	public MySQLiteHelper db = null;
	public Engine engine;
	public ActivityGame activity;
	public ZoomCamera camera;
	public VertexBufferObjectManager vbom;
	public PersistenceHelper ph;

	public Font myFont;
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	public ITextureRegion playB_region;
	public ITextureRegion practiceB_region;

	public ITextureRegion profileB_region;
	public ITextureRegion score_per_levelB_region;
	public ITextureRegion rating_listB_region;


	public ITextureRegion chesstactics_region;

	public ITextureRegion challenge_region;

	public ITextureRegion background_region;
	public ITextureRegion tactics_background_region;

	public ITextureRegion inputfield_region;

	public ITextureRegion game_background_region;
	public ITextureRegion castle_region;
	public ITextureRegion game_piece_region;
	public ITextureRegion game_intro_popup_region;
	public ITextureRegion chessboard_region;

	public ITextureRegion black_king_region;

	public ITextureRegion black_knight_region;

	public ITextureRegion black_bishop_region;

	public ITextureRegion black_queen_region;

	public ITextureRegion black_rook_region;

	public ITextureRegion white_king_region;

	public ITextureRegion white_knight_region;

	public ITextureRegion white_bishop_region;

	public ITextureRegion white_queen_region;

	public ITextureRegion white_rook_region;

	public ITextureRegion black_pawn_region;

	public ITextureRegion white_pawn_region;

	public ITextureRegion happyface;

	public ITextureRegion unhappyface;

	public ITextureRegion yellowface;
	public ITextureRegion orangeface;

	public ITextureRegion ok_region;
	public ITextureRegion tactics_termoPause;
	public ITextureRegion tactics_thinking;
	public ITextureRegion paused_region;


	public ITextureRegion d_highlight_square;
	public ITextureRegion l_highlight_square;

	public ITextureRegion horseHead_region;
	public ITextureRegion empty_horseHead_region;
	public ITextureRegion empty_horseHead_region2;
	public ITextureRegion fail_region;

	public ITextureRegion statsB_region;


	public Text hud;

	public Text game_intro_text;

	private BuildableBitmapTextureAtlas firstTimeTextureAtlas=null;
	private BuildableBitmapTextureAtlas menuTextureAtlas=null;
	private BuildableBitmapTextureAtlas gameTextureAtlas=null;
	private BuildableBitmapTextureAtlas tacticsTextureAtlas=null;
	private BuildableBitmapTextureAtlas tacticsexitTextureAtlas=null;
	private BuildableBitmapTextureAtlas commonTextureAtlas=null;
	private BuildableBitmapTextureAtlas statsTextureAtlas=null;




	//Music
	public Music mMusic,endMusic;

	//Sound
	public Sound movePieceSound,correctMove,wowSound,booSound,wooshSound,bravoSound,heartSound,awwSound;//,slagSound;

	public Sound gallop,grr,grr2,wind,falling;

	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	//Resources that stay loaded the full game
	public void loadStaticResources() {
		loadStaticGraphics();
		loadGameFonts();
	}

	public void loadMenuResources()
	{
		loadMenuAudio();
		loadMenuGraphics();
	}

	public void loadStatsResources() 
	{
		loadStatsGraphics();
	}

	public void unloadMenuResources()
	{
		unloadMenuGraphics();
		//unloadMenuFonts();
		unloadMenuAudio();
	}

	public void unloadStatsResources() 
	{
		if (statsTextureAtlas!=null)
			statsTextureAtlas.unload();
	}

	public void loadGameResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		loadGameGraphics();	
		loadGameSceneGraphics();
		loadGameAudio();
	}

	public void unloadGameResources() {

		unloadGameGraphics();
		unloadGameSceneGraphics();
		unloadGameAudio();
	}

	public void loadTacticsResources() 
	{
		loadTacticsGraphics();
		loadTacticsAudio();
	}

	public void unloadTacticsResources() {
		unloadTacticsAudio();
		unloadTacticsGraphics();
	}

	public void unloadTacticsEntryResources() {
	}

	public void loadChallengeExitResources() 
	{
		loadChallengeExitGraphics();
	}
	public void unloadChallengeExitResources() {
		tacticsexitTextureAtlas.unload();
	}
	public void loadFirstTimeResources() {
		loadFirstTimeGraphics();

	}

	public void unloadFirstTimeResources() {
		firstTimeTextureAtlas.unload();

	}
	
	public void loadEndResources() {
		loadEndAudio();
	}

	public void unloadEndResources() {
		if (endMusic!=null&&endMusic.isPlaying())
			endMusic.stop();
		endMusic = null;
	}	
	
	public String[] countries = {"br","ca","cn","de","fr","nl","ru","se","ua","uk","us","bt","eu","in","kg","no","fi","it","ie","tr"};
	public ITextureRegion[] countryTextures = new ITextureRegion[countries.length];


	public TiledTextureRegion tiledfaces;


	private void loadStaticGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/common/");
		commonTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),400, 900, TextureOptions.BILINEAR);
		ok_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(commonTextureAtlas, activity, "ok_button.png");
		game_intro_popup_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(commonTextureAtlas, activity, "popup_bg.png");

		try 
		{
			this.commonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.commonTextureAtlas.load();

		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}  
	}



	BitmapTextureAtlas bgTexture;
	BitmapTextureAtlas castleTexture;

	public  ITextureRegion cloud_region,pf,pf_1,pf_2,pf_3,glowBall,levelUp;
	public ITiledTextureRegion king;

	public int[][] ladderStepLocations;

	public int castleOffsetY=0,castleOffsetX=0,firstSceneId;
	public boolean mirrorPiece = false;
	
	private static final int HIGHEST_SCENE = 2;
	
	private void loadGameSceneGraphics() {
		int scene = getPlayerScene();
		switch (scene) {
		case 1:
			castleTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1200, 1078, TextureOptions.BILINEAR);
			castle_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(castleTexture,activity, "castle2.png", 0, 0);
			ladderStepLocations = new int[][]{{-970,-550},{-870,-555},{-700,-550},{-510,-555},
					{-330,-520}/*,{-255,-510}*/,/*{-225,-460}*/
					{-500,-423},/*{-390,-415},*/{-300,-390},
					{-475,-305}/*,{-400,-295}*/,{-310,-275},
					{-470,-195},/*{-400,-185}*/{-325,-165},
					{-400,-105},{-400,60}};
			castleOffsetY = -300;
			castleOffsetX = -470;
			firstSceneId = 1;
			break;
		case HIGHEST_SCENE:
			mirrorPiece = true;
			castleTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1280, 1691, TextureOptions.BILINEAR);
			castle_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(castleTexture,activity, "castle3.png", 0, 0);
			ladderStepLocations = new int[][]{{-400,-585},{-500,-700},{-610,-750},
					{-740,-818},{-900,-800},{-1050,-760},
					{-835,-680},{-955,-655}, {-1075,-605},
					{-819,-520},/*{-948,-510},*/{-1060,-465},
					{-825,-385},/*{-960,-370},*/{-1055,-330},
					{-837,-260},{-1000,-225},
					{-830,-140},{-1010,-100},
					{-930,-25},
					{-927,190}};
			castleOffsetY = -400;
			castleOffsetX = -470;
			firstSceneId = 12;
			break;
		
		}	

		if (castleTexture != null)
			castleTexture.load();
		else
			Log.e("schack","scene was "+scene+" in loadGameGrapghics");
	}


	private void loadGameGraphics() {
		Log.d("schack","loadgamegraphics called here");
		if (gameTextureAtlas == null) {
			bgTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1000,1000, TextureOptions.BILINEAR);
			//bgTexture = new BitmapTextureAtlas(activity.getTextureManager(), 2000,2000, TextureOptions.BILINEAR);
			Log.d("schack","does it reach here?");
			gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1500, 1500, TextureOptions.BILINEAR);
			game_piece_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "knight.png");
			pf = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pf.png");
			pf_1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pf_1.png");
			pf_2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pf_2.png");
			pf_3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pf_3.png");
			glowBall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "glow.png");
			levelUp = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelup.png");
			king = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "black_king_seq.png", 5, 1);

			//king = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "black_king.png");
			cloud_region =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity, "cloud.png");

			game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture,activity, "bgs.png", 0, 0);
			//BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fantasytower.jpg");

			try 
			{
				this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));

			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}  
		}

		gameTextureAtlas.load();
		bgTexture.load();


	}



	private void loadFirstTimeGraphics() {
		if (firstTimeTextureAtlas==null) {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/firsttime/");
			firstTimeTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1500, 1500, TextureOptions.BILINEAR);
			inputfield_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(firstTimeTextureAtlas, activity, "input_bg.png");
			for (int i=0;i<countries.length;i++) 
				countryTextures[i] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(firstTimeTextureAtlas, activity, countries[i]+".png");			
			try 
			{
				this.firstTimeTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.firstTimeTextureAtlas.load();
			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}
		} else
			firstTimeTextureAtlas.load();
		//first_time_name_exists_already = new Text(0,0,smallRedFont,"Ups! That name already exists!",vbom);
		first_time_bg_rect = new Rectangle(ActivityGame.CAMERA_WIDTH/2, ActivityGame.CAMERA_HEIGHT/2,ActivityGame.CAMERA_WIDTH-40, ActivityGame.CAMERA_HEIGHT-20, vbom);


	}


	// 
	private void loadMenuGraphics()
	{
		if (menuTextureAtlas == null) {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
			menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
			chesstactics_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "chesstactics.png");
			challenge_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "challenge.png");
			background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.png");
			playB_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
			practiceB_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "practice.png");
			statsB_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "stats.png");

			try 
			{
				this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.menuTextureAtlas.load();
			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}	  
		} else
			menuTextureAtlas.load();

	}


	private void loadStatsGraphics() {
		if (statsTextureAtlas == null) {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/stats/");
			statsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
			rating_listB_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(statsTextureAtlas, activity, "rating_list.png");
			score_per_levelB_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(statsTextureAtlas, activity, "score_per_level.png");
			profileB_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(statsTextureAtlas, activity, "your_details.png");

			try 
			{
				this.statsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.statsTextureAtlas.load();
			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}	  
		} else
			statsTextureAtlas.load();

	}


	private void loadTacticsGraphics() {
		if (tacticsTextureAtlas == null) {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tactics/");
			tacticsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
			tacticsTextureAtlas.addEmptyTextureAtlasSource(0,0,2048,1024);
			chessboard_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "board.png");
			black_king_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_king.gif");
			black_knight_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_knight.gif");
			black_bishop_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_bishop.gif");
			black_queen_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_queen.gif");
			black_rook_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_rook.gif");
			black_pawn_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "b_pawn.gif");
			white_king_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_king.gif");
			white_knight_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_knight.gif");
			white_bishop_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_bishop.gif");
			white_queen_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_queen.gif");
			white_rook_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_rook.gif");
			white_pawn_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "w_pawn.gif");

			d_highlight_square = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "d_hilight_square.png");
			l_highlight_square = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "l_hilight_square.png");

			happyface = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "smiley_green.png");
			unhappyface = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "smiley_red.png");
			yellowface = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "smiley_yellow.png");
			orangeface = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "smiley_orange.png");

			tiledfaces = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tacticsTextureAtlas, activity, "tiledsmileys.png", 4, 1);
			tactics_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "bg_pic.jpg");
			tactics_termoPause = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "termopause.png");
			tactics_thinking = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "thinking.png");

			empty_horseHead_region2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "empty_knight.png");

			paused_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsTextureAtlas, activity, "paused.png");

			try 
			{
				this.tacticsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.tacticsTextureAtlas.load();
			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}	
		} else
			tacticsTextureAtlas.load();


	}

	public void loadChallengeExitGraphics() {
		if (tacticsexitTextureAtlas == null) {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tactics/");
			tacticsexitTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
			empty_horseHead_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsexitTextureAtlas, activity, "empty_knight.png");
			fail_region =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(tacticsexitTextureAtlas, activity, "fail.png");
			try 
			{
				this.tacticsexitTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.tacticsexitTextureAtlas.load();
			} 
			catch (final TextureAtlasBuilderException e)
			{
				Debug.e(e);
			}	  
		} else
			tacticsexitTextureAtlas.load();

	}


	private void loadMenuAudio()
	{
		if (mMusic == null) {
			MusicFactory.setAssetBasePath("sfx/");
			try {
				Log.d("Schack","Loading music");
				mMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music.mp3");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loadGameAudio() 
	{
		SoundFactory.setAssetBasePath("sfx/");
		try {
			grr = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "grr.mp3");
			grr2 = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "grr2.mp3");
			wind = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "wind.mp3");
			gallop = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "gallop.mp3");
			falling = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "falling.mp3");
		} catch (final IOException e) {
			Debug.e(e);
		}	
	}

	private void loadTacticsAudio() {
		SoundFactory.setAssetBasePath("sfx/");
		try {
			movePieceSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "move.mp3");
			correctMove = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "correct.mp3");
			wowSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "whoo.mp3");
			booSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "boo.mp3");
			wooshSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "woosh.mp3");
			bravoSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "applause.mp3");
			heartSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "heart.mp3");
			awwSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "aww.mp3");
			

		} catch (final IOException e) {
			Debug.e(e);
		}

	}

	private void loadEndAudio() {
		
			if (endMusic == null) {
				MusicFactory.setAssetBasePath("sfx/");
				try {
					Log.d("Schack","Loading music");
					endMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "endmusic.mp3");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	public Font font,mediumFont,mediumWhiteFont,tacticPopupFont;
	public Font smallWhiteFont;
	public Font smallRedFont;

	private LevelDescriptor currentLevel;








	//Fonts used in all game.

	private void loadGameFonts()
	{
		FontFactory.setAssetBasePath("fonts/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture tacticPopupFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture mediumFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture smallWhiteFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture mediumWhiteFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture smallRedFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Aller_Rg.ttf", 50f, true,android.graphics.Color.WHITE, 2f,android.graphics.Color.WHITE);
		font.load();
		tacticPopupFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), tacticPopupFontTexture, activity.getAssets(), "Aller_Rg.ttf", 30f, true,android.graphics.Color.WHITE, 2f,android.graphics.Color.WHITE);
		tacticPopupFont.load();
		mediumFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mediumFontTexture, activity.getAssets(), "Aller_Rg.ttf", 40f, true,android.graphics.Color.WHITE, 2f,android.graphics.Color.WHITE);
		mediumFont.load();
		mediumWhiteFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mediumWhiteFontTexture, activity.getAssets(), "Aller_Rg.ttf", 40f, true, android.graphics.Color.WHITE, 2f,android.graphics.Color.WHITE);
		mediumWhiteFont.load();
		smallWhiteFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), smallWhiteFontTexture, activity.getAssets(), "Aller_Rg.ttf", 30f, true, android.graphics.Color.WHITE, 2f,android.graphics.Color.WHITE);
		smallWhiteFont.load();
		smallRedFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), smallRedFontTexture, activity.getAssets(), "Aller_Rg.ttf", 25f, true, android.graphics.Color.RED, 2f,android.graphics.Color.BLACK);
		smallRedFont.load();
	}




	public void loadSplashScreenResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 338, 218, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "shield_logo.png", 0, 0);
		splashTextureAtlas.load();
	}



	/***************************************
	 * UNLOAD
	 */



	private void unloadMenuAudio() {
		if (mMusic !=null) {
			if (mMusic.isPlaying())
				mMusic.stop();
			mMusic = null;
		}
	}


	private void unloadGameAudio() {
		grr = null;
		grr2 = null;
		wind = null;
		gallop = null;
		falling = null;
	}


	private void unloadTacticsAudio() {
		movePieceSound = null;
		correctMove = null;
		wowSound = null;
		booSound = null;
		wooshSound = null;
	}

	private void unloadMenuGraphics()
	{
		menuTextureAtlas.unload();
	}

	private void unloadGameSceneGraphics() {
		castleTexture.unload();		
	}
	private void unloadGameGraphics() {
		gameTextureAtlas.unload();
		bgTexture.unload();
	}

	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}


	private void unloadMenuFonts() {
		smallWhiteFont.unload();
		font.unload();
		smallRedFont.unload();
	}

	private void unloadTacticsGraphics()
	{
		tacticsTextureAtlas.unload();
	}


	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void prepareManager(Engine engine, ActivityGame activity, ZoomCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().ph = new PersistenceHelper(activity);
		getInstance().db = createDB(activity);

	}

	private static MySQLiteHelper createDB(ActivityGame c) {
		MySQLiteHelper myDBH = new MySQLiteHelper(c);
		try {
			myDBH.createDataBase();
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
		try {
			myDBH.openDataBase();
		} catch (SQLException e) {
			throw e;
		}
		return myDBH;
	}
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static ResourceManager getInstance()
	{
		if (INSTANCE == null) {
			INSTANCE=new ResourceManager();
		}
		return INSTANCE;
	}

	public int calculateCenterX(ITextureRegion r) {
		return (int)(ActivityGame.CAMERA_WIDTH/2+r.getWidth()/2);
	}

	public String getPlayerName() {

		return ph.getString(KEY_PLAYER_NAME);
	}

	public int getPlayerScore(int lvl) {
		return ph.getInt(KEY_SCORE_LEVEL+lvl);

	}

	public int getPlayerScene() {
		return ph.getInt(KEY_PLAYER_SCENE)+1;
	}

	public void setPlayerScene(int scene) {
		ph.put(KEY_PLAYER_SCENE,scene);
	}
	public void setPlayerName(String name) {
		ph.put(KEY_PLAYER_NAME,name);	
	}

	public void increasePlayerScene() {
		ph.put(KEY_PLAYER_SCENE,ph.getInt(KEY_PLAYER_SCENE)+1);
	}

	public void setCleared(int lvl) {
		if (ph.getInt(KEY_HIGHEST_LEVEL)<lvl)
			ph.put(KEY_HIGHEST_LEVEL, lvl);		
	}

	public void setPersonalBest(int lvl, int score) {
		ph.put(KEY_SCORE_LEVEL+lvl,score);
	}

	public int getHighestLevelCleared() {
		//return 10;
		return ph.getInt(KEY_HIGHEST_LEVEL);
	}

	public int getTotalScore() {
		int level = 1;
		int score = 0,total = 0;

		do {
			score = getPlayerScore(level++);
			total+=score;
		} while (score>0);
		return total;
	}


	public void setCurrentSelectedLevel(LevelDescriptor lvl) {
		currentLevel = lvl;
	}
	public LevelDescriptor getCurrentSelectedLevel() {
		return currentLevel;
	}
	private TacticResult currentTacticRes=null;


	public void setTacticResult(TacticResult res) {
		currentTacticRes = res;
	}

	public TacticResult getTacticResult() {
		return currentTacticRes;
	}



	public void incrementUseCount() {
		ph.put(KEY_VISIT_COUNTER, ph.getInt(KEY_VISIT_COUNTER)+1);
	}

	public String getMyId() {
		return ph.getString(KEY_MY_ID);
	}

	private String generateUniqueId() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}

	private boolean isFirstTime() {
		if (getMyId().equals(PersistenceHelper.UNDEFINED)) 
			return true;
		else
			return false;
	}

	private boolean isRegistered() {
		//return false;
		return ! (ph.getString(KEY_HAS_BEEN_REGISTERED).equals(PersistenceHelper.UNDEFINED));

	}
	


	public void registerUser() {

	if (isFirstTime()) {
			Log.d("schack","Creating new user...setting ratings");
			ph.put(KEY_MY_ID, generateUniqueId());
			this.setRating(1500);
			this.setRD(275);
		}
		if (isNetworkAvailable()) {
			final WsCallback userLoginCb = new WsCallback() {			
				@Override
				public void doSomething(String res) {
					if (res !=null) {
						Log.d("schack","Got "+res+" back from login call");
					} else Log.e("schack","GOT NULL back from login call");
				} 
			};
			Log.d("schack","Gets to registeruser");
			if (!isRegistered()) {
				final WsCallback createUserCb = new WsCallback() {			
					@Override
					public void doSomething(String res) {
						if (res !=null) {
							Log.d("schack","Got "+res+" back from createuser call");
							if (res.equals("0"))
								Log.d("schack","Zero...no can do");
							else	
								ph.put(KEY_HAS_BEEN_REGISTERED, "REGISTRADO");
							//now the user exists in the server. Then we can log the login.
							WebService rt = new WebService(userLoginCb);
							rt.execute("loginuser=&uuid="+getMyId());

						} else Log.e("schack","GOT NULL back from createuser callzzz");
					} 
				};



				String countryCode = getCountry();
				if (countryCode.equals(PersistenceHelper.UNDEFINED))
					Tools.getCountry();			
				WebService rt = new WebService(createUserCb);
				Log.d("schack","createuser="+this.getPlayerName()+"&country="+countryCode+"&uuid="+this.getMyId());
				rt.execute("createuser="+this.getPlayerName()+"&country="+countryCode+"&uuid="+this.getMyId());

			} else {
				Log.d("schack","user already registered..?");
				//log the user
				WebService rt = new WebService(userLoginCb);
				rt.execute("loginuser=&uuid="+this.getMyId());
			}

		}
	}

	public int getVisitCount() {
		return ph.getInt(KEY_VISIT_COUNTER);
	}

	public boolean hasNotBeenAskedToRate() {
		if(ph.getInt(KEY_ASK_TO_RATE)==0)
			return true;
		return false;
	}

	public void userHasBeenAskedToRate() {
		ph.put(KEY_ASK_TO_RATE,1);
	}


	public boolean isFirstTimeGameEntry() {
		boolean f = ph.getString(KEY_FIRST_TIME_IN_GAME_ENTRY).equals(PersistenceHelper.UNDEFINED);
		ph.put(KEY_FIRST_TIME_IN_GAME_ENTRY,"FOO");
		return f;
		//return true;
	}

	public String getInitialMessage(Type type) {
		switch (type) {
		case Tactics:
			boolean f = ph.getString(KEY_FIRST_TIME_TACTICS).equals(PersistenceHelper.UNDEFINED);
			ph.put(KEY_FIRST_TIME_TACTICS,"FOO");
			if(f) 
				return "TACTICS CHALLENGE\n\nIn a tactics challenge, the\n"
				+ "computer moves first.\nYour challenge is to find the\n"
				+ "best move on time. If time\nruns out you will lose!\nIf you are quick, you get\nbonuses! The level\n"
				+ "difficulty will gradually\nget harder and harder,\nbut you will also get\nbetter and better :)\nGood luck!";
			break;

		case Game:
			f = ph.getString(KEY_FIRST_TIME_GAME).equals(PersistenceHelper.UNDEFINED);
			ph.put(KEY_FIRST_TIME_GAME,"FOO");
			if(f) 
				return "GAME CHALLENGE\n\nIn a game challenge you\n"
				+ "have to defeat the\ncomputer opponent either\n"
				+ "within a move or time limit.\nLucky for you, the computer\n"
				+ "opponent isn't very smart!\nGood luck!";
			break;

		case MatePuzzle:
			f = ph.getString(KEY_FIRST_TIME_PUZZLE).equals(PersistenceHelper.UNDEFINED);
			ph.put(KEY_FIRST_TIME_PUZZLE,"FOO");
			if(f) 
				return "PUZZLE CHALLENGE\n\nIn a puzzle challenge,\n"
				+ "you move first.\nYou have to find the best\n"
				+ "(shortest number of moves)\nthat check mates your\n"
				+ "opponent. The puzzle can have\nboth time and move limits.\n"

						+ "Some of these problems can\nbe really difficult so be\npatient and take your\ntime."
						+ "Best of luck!";
			break;
		}

		return null;
	}


	public void registerSolved(int prob) {
		int highest = ph.getInt(KEY_TRICKIEST);
		if (highest < prob)
			ph.put(KEY_TRICKIEST, prob);
		ph.put(KEY_TOTAL_SOLVED,ph.getInt(KEY_TOTAL_SOLVED)+1);
	}

	public void registerWoos(int count) {
		ph.put(KEY_WOOS, ph.getInt(KEY_WOOS)+count);
	}

	public void registerLongestGreen(int greenSequence) {
		int highest = ph.getInt(KEY_LONGEST_GREEN);
		if (highest < greenSequence)
			ph.put(KEY_LONGEST_GREEN, greenSequence);

	}

	public void setRating(float rating) {
		ph.put(KEY_MY_RATING,rating);
	}

	public void setCountry(String country) {
		ph.put(KEY_MY_COUNTRY,country);
	}
	public void setRD(float rd) {
		ph.put(KEY_MY_RD,rd);
	}

	public int getTrickiestSolved() {
		return ph.getInt(KEY_TRICKIEST);
	}

	public int getTotalWoos() {	
		return ph.getInt(KEY_WOOS);
	}

	public int getLongestGreen() {	
		return ph.getInt(KEY_LONGEST_GREEN);
	}	

	public int getTotalSolved() {
		return ph.getInt(KEY_TOTAL_SOLVED);
	}

	public float getRating() {
		return ph.getFloat(KEY_MY_RATING);
	}

	public String getCountry() {
		return ph.getString(KEY_MY_COUNTRY);
	}
	public float getRD() {
		return ph.getFloat(KEY_MY_RD);
	}

	float oldRating = 0;

	public boolean kingHasFallen;

	public float getOldRating() {
		return oldRating;
	}

	public void setOldRating() {
		oldRating = getRating();
	}


	public boolean hasNotBeenAskedName() {
		return ph.getString(KEY_HAS_BEEN_ASKED_NAME).equals(PersistenceHelper.UNDEFINED);
	}

	public void userHasBeenAskedForName() {
		ph.put(KEY_HAS_BEEN_ASKED_NAME, "YEPP");
	}

	public boolean isTrainingLocked() {
		return ph.getString(KEY_TRAINING_LOCKED).equals(PersistenceHelper.UNDEFINED);
	}

	public void unlockTraining() {
		ph.put(KEY_TRAINING_LOCKED, "NOPE");		
	}
	
	public boolean isPaid() {
		return true;
	}

	//int dollC = 0;
	public boolean kingHasFallen() {
	//	if (dollC++<3)
	//		return false;
	//	else
	//		return true;
		return !ph.getString(KEY_KING_HAS_FALLEN+getPlayerScene()).equals(PersistenceHelper.UNDEFINED);
	}

	public void setKingHasFallen() {
		ph.put(KEY_KING_HAS_FALLEN+getPlayerScene(),"YEPP");
	}

	public boolean firstTimeKiller() {
		
		boolean ret = true;
		String firstTime = ph.getString("A_Key");
		if (!firstTime.equals(PersistenceHelper.UNDEFINED))
			ret = false;
		else
			ph.put("A_Key", "Defined");
		return ret;
		
	}

	public boolean noMoreLevels() {
		return getPlayerScene() > HIGHEST_SCENE;
	}

	
	public boolean isMaster() {
		return !ph.getString("MASTER").equals(PersistenceHelper.UNDEFINED);
	}
	public void goMaster() {
		ph.put(KEY_HIGHEST_LEVEL, 0);
		ph.put("MASTER","true");
	}
}
