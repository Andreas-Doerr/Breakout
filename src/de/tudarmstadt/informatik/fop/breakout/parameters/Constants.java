package de.tudarmstadt.informatik.fop.breakout.parameters;


public interface Constants {
	int FRAME_RATE = 500;
	String WINDOW_ICON = "/images/icon.png";

	// Game States
	int MAINMENU_STATE = 0;
	int GAMEPLAY_STATE = 1;
	int HIGHSCORE_STATE = 2;
	int OPTIONS_STATE = 3;
	int ABOUT_STATE = 4;

	// Options
	String OPTIONS_FILE = "options.config";

	// Player
	// Lives
	int INITIAL_LIVES = 3;

	// Background
	// in Game
	String BACKGROUND_ID = "background";
	// in Menu
	String MENU_ID = "menu";

	// Borders
	enum BorderType {
		TOP, LEFT, RIGHT
	}
	int BORDER_WIDTH = 10;
	String TOP_BORDER_ID = "topBorder";
	String LEFT_BORDER_ID = "leftBorder";
	String RIGHT_BORDER_ID = "rightBorder";

	// Blocks
	int BLOCK_IMAGE_X = 200; // error if = 0
	int BLOCK_IMAGE_Y = 117;

	// Items
	float ITEM_DROPCHANCE = 0.15f;
	String ITEM_1_ID = "item bigger";
	String ITEM_2_ID = "item smaller";
	String ITEM_3_ID = "item loose_ball";
	String ITEM_4_ID = "item duplicate_ball";
	String ITEM_5_ID = "item max_speed_balls";
	String ITEM_6_ID = "item min_speed_balls";

	// Maps
	String TEMPLATE_LEVEL = "maps/level1.map";
	int MAX_MAPS = 20;

	// Language
	int MAX_LANGUAGES = 10;

	// speeds
	int SPEED_MULTIPLIER = 3;

	// Ball
	String BALL_ID = "ball";
	int HITS_TO_MAX_SPEED = 25;
	float MAX_SPEED_MULTIPLIER = 3f;
	String DESTROYED_BALL_ID = "destroyed ball";
	int MAX_AMOUNT_OF_BALLS = 100;

	// Stick
	String PLAYER_STICK_ID = "playerStick";
	String PLAYER_STICK_ID_TOP = "playerStickTop";
	String BOT_STICK_ID = "botStick";
	enum StickShape {
		SLIM, NORMAL, WIDE
	}
	int MAX_AMOUNT_OF_STICKS = 10;

	// Buttons
	enum ButtonType {
		NORMAL, WIDE, MAINMENU
	}
	int TEXT_OFFSET_X = -60;
	int TEXT_OFFSET_X_WIDE = -120;
	int TEXT_OFFSET_Y = -9;

	// Pause
	String PAUSE_ID = "pause";

	// Highscore
	String HIGHSCORE_FOLDER = "highscores/";
	String HIGHSCORE_FILE = "highscore";
	String HIGHSCORE_FILE_ENDING = ".hsc";
	int MAX_HIGHSCORES = 100;

	// GUI
	int Y_TO_NEXT = 20;

	// Timer
	int TIMER_Y = 10;

	// Sounds
	// actions
	String SOUND_HIT_STICK = "/sounds/hitStick.wav";
	String SOUND_HIT_BORDER = "/sounds/hitBlock.wav";
	String SOUND_HIT_BLOCK = "/sounds/hitBlock.wav";
	String SOUND_CREATE_ITEM = "/sounds/starcoin.wav";
	String SOUND_PICKUP_ITEM = "/sounds/pickUP_Item.wav";
	String SOUND_DESTROY_BALL = "/sounds/Bottle Break-spookymodem.wav";
	String SOUND_VICTORY = "/sounds/Lively Meadow Victory Fanfare-Matthew Pablo.wav";
	String SOUND_GAMEOVER = "/sounds/GameOver_Cleyton Kauffman - httpopengameart_orgusersdoppelganger.wav";
	String SOUND_BUTTON_PRESS = "/sounds/Tiny Button Push-SoundBible.com-513260752_Mike Koenig.wav";
	String SOUND_NOT_ACCEPTABLE = "/sounds/error sound - Commissioned by OpenGameArt.wav";
	// music
	String MUSIC_MENU = "/sounds/Sky Game Menu by Eric Matyas Soundimage.wav"; // by Soundimage.org
	String MUSIC_GAMEPLAY = "/sounds/Start__Music from a NaaLaa breakout example game.wav";
	String MUSIC_HIGHSCORE = "/sounds/Highscore.wav";

	// Themes
	int MAX_THEMES = 2;
	String THEME_0_FOLDER = "theme0/";
	String THEME_1_FOLDER = "theme1/";

	// Images (all are: imageFolder + themeFolder + "NAME" + IMAGE_ENDING)
		String IMAGES_FOLDER = "images/";
		String IMAGE_ENDING = ".png";

		// backgrounds
		String MENU_BACKGROUND = "menuBackground";
		String MENU_BLANK_BACKGROUND = "menuBlank";
		String GAME_BACKGROUND = "background";

		// blocks
		String BLOCK_1 = "block_1";
		String BLOCK_2 = "block_2";
		String BLOCK_3 = "block_3";
		String BLOCK_4 = "block_4";
		String BLOCK_5 = "block_5";

		// balls
		String STANDARDBALL = "standardball";
		String WATERBALL = "waterball";
		String FIREBALL = "fireball";
		String DESTROYED_BALL = "destroyedBall";

		// sticks
		String STICK = "stick";
		String STICK_SLIM = "stick_slim";
		String STICK_WIDE = "stick_wide";

		// items
		String SMALLER = "smaller";
		String BIGGER = "bigger";
		String SLOWER = "slower";
		String FASTER = "faster";
		String DUP = "dup";
		String DESTROY_BALL = "destroyedBall";

		// buttons
		String BUTTON = "button";
		String SELECTED_BUTTON = "selectedButton";
		String BUTTON_WIDE = "buttonWide";
		String SELECTED_BUTTON_WIDE = "selectedButtonWide";
		String MENU_BUTTON = "menuButton";
		String SELECTED_MENU_BUTTON = "selectedMenuButton";

		// miscelanious
		String PAUSE = "pause";
}
