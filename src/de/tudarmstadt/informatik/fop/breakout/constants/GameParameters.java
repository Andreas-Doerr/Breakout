package de.tudarmstadt.informatik.fop.breakout.constants;

import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;

/**
 * Class for holding the game parameters and constants e.g. entity IDs or image
 * paths
 * 
 * @author Tobias Otterbein, Benedikt Wartusch
 * 
 */
public interface GameParameters {
	// Window Settings (Aspect ratio = 4:3)
	int WINDOW_WIDTH = OptionsHandler.getResolution_x();
	int WINDOW_HEIGHT = (int)(WINDOW_WIDTH / 4f * 3f);
	int FRAME_RATE = 60;
	String WINDOW_ICON = "/images/icon.png";

	// Game States
	int MAINMENU_STATE = 0;
	int GAMEPLAY_STATE = 1;
	int HIGHSCORE_STATE = 2;
	int OPTIONS_STATE = 3;
	int ABOUT_STATE = 4;

	// Options
	String OPTIONS_FILE = "options.config";

	// Background
	// general
	int BACKGROUND_WIDTH = 1640; // the image width; error if = 0
	float BACKGROUND_SCALE = (float) WINDOW_WIDTH  / (BACKGROUND_WIDTH - 40);
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
	float ITEM_DROPCHANCE = 0.1f;
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
	int SPEED_MULTIPLIER = 30;

	// Ball
	String BALL_ID = "ball";
	float INITIAL_BALL_SPEED_UP = 0.2f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
	float INITIAL_BALL_SPEED_RIGHT = 0.075f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
	float INITIAL_TOTAL_SPEED = (float) Math.sqrt((INITIAL_BALL_SPEED_UP * INITIAL_BALL_SPEED_UP) + (INITIAL_BALL_SPEED_RIGHT * INITIAL_BALL_SPEED_RIGHT));
	int HITS_TO_MAX_SPEED = 20;
	float MAX_SPEED_MULTIPLIER = 2f;
	float SPEEDUP_VALUE = (INITIAL_TOTAL_SPEED * (MAX_SPEED_MULTIPLIER - 1f)) / HITS_TO_MAX_SPEED;
	String DESTROYED_BALL_ID = "destroyed ball";
	int MAX_AMOUNT_OF_BALLS = 100;

	// Stick
	String STICK_ID = "stick";
	String STICK_ID_TOP = "stickTop";
	int STICK_Y = (int)(WINDOW_HEIGHT * 0.9f);
	int STICK_Y_TOP = (int)(WINDOW_HEIGHT * 0.1f);
	float STICK_SPEED = 0.5f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
	enum StickShape {
		SLIM, NORMAL, WIDE
	}
	int MAX_AMOUNT_OF_STICKS = 10;

	// Buttons
	float MENU_ENTRY_SCALE = BACKGROUND_SCALE;
	float Entity_SCALE = 0.55f * BACKGROUND_SCALE;
	int TEXT_OFFSET_X = -60;
	int TEXT_OFFSET_X_WIDE = -120;
	int TEXT_OFFSET_Y = -9;

	// Button positions
	// MainMenu
	int MAIN_MENU_BUTTON_1_X = (int) (WINDOW_WIDTH * 0.38f);
	int MAIN_MENU_BUTTON_2_X = (int) (WINDOW_WIDTH * 0.64f);

	int MAIN_MENU_BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.53f);
	int MAIN_MENU_BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.638f);
	int MAIN_MENU_BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.746f);

	// Options
	int BUTTON_1_X_WIDE = (int) (WINDOW_WIDTH * 0.308f);
	int BUTTON_1_X = (int) (WINDOW_WIDTH * 0.27f);
	int BUTTON_2_X = (int) (WINDOW_WIDTH * 0.508f);
	int BUTTON_3_X = (int) (WINDOW_WIDTH * 0.708f);

	int BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.44f);
	int BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.49f);
	int BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.54f);
	int BUTTON_4_Y = (int) (WINDOW_HEIGHT * 0.59f);
	int BUTTON_5_Y = (int) (WINDOW_HEIGHT * 0.64f);
	int BUTTON_6_Y = (int) (WINDOW_HEIGHT * 0.69f);
	int BUTTON_7_Y = (int) (WINDOW_HEIGHT * 0.74f);
	int BUTTON_8_Y = (int) (WINDOW_HEIGHT * 0.84f);

	// Pause
	String PAUSE_ID = "pause";

	// Highscore
	String HIGHSCORE_FILE_0 = "highscores/highscore.hsc";
	String HIGHSCORE_FILE_1 = "highscores/highscoreCoop.hsc";
	int MAX_HIGHSCORES = 100;

	// GUI
	int y_to_next = 20;
	// Lives
	int INITIAL_LIVES = 3;
	int LIVES_POS_X = (int) (10 / BACKGROUND_SCALE);
	int LIVES_POS_Y = 0;

	// Blocks
	int ACTIVE_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
	int ACTIVE_BLOCKS_POS_Y = y_to_next;
	int DESTROYED_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
	int DESTROYED_BLOCKS_POS_Y = y_to_next * 2;

	// active balls
	int ACTIVE_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
	int ACTIVE_BALLS_POS_Y = y_to_next * 3;
	int ACTIVE_DESTROYED_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
	int ACTIVE_DESTROYED_BALLS_POS_Y = y_to_next * 4;

	// points
	int POINTS_POS_X = (int) (10 / BACKGROUND_SCALE);
	int POINTS_POS_Y = y_to_next * 5;

	// Timer
	int TIMER_X = (int) ((WINDOW_WIDTH / 2) / BACKGROUND_SCALE) - (int) (20 / BACKGROUND_SCALE);
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
