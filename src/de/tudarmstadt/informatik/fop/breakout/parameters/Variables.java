package de.tudarmstadt.informatik.fop.breakout.parameters;

import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;

import static de.tudarmstadt.informatik.fop.breakout.parameters.Constants.*;

/**
 * Created by PC - Andreas on 25.03.2017.
 *
 * @author Andreas Dörr
 */
public class Variables {

	// Window Sizes (Aspect ratio = 4:3)
	public static int WINDOW_WIDTH = OptionsHandler.getWindow_x();
	public static int WINDOW_HEIGHT = OptionsHandler.getWindow_y();
	// image
	public static int BACKGROUND_WIDTH = 1640; // the image width; error if = 0
	public static float BACKGROUND_SCALE = (float) WINDOW_WIDTH  / (BACKGROUND_WIDTH - 40);

	// Block
	public static float BLOCK_SCALE = (float) ((WINDOW_WIDTH) / 18.0 / Constants.BLOCK_IMAGE_X);

	// Ball
	public static float INITIAL_BALL_SPEED_UP = 0.2f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
	public static float INITIAL_BALL_SPEED_RIGHT = 0.075f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
	public static float INITIAL_TOTAL_SPEED = (float) Math.sqrt((INITIAL_BALL_SPEED_UP * INITIAL_BALL_SPEED_UP) + (INITIAL_BALL_SPEED_RIGHT * INITIAL_BALL_SPEED_RIGHT));
	public static float SPEEDUP_VALUE = (INITIAL_TOTAL_SPEED * (MAX_SPEED_MULTIPLIER - 1f)) / HITS_TO_MAX_SPEED;

	// Stick
	public static int STICK_Y = (int)(WINDOW_HEIGHT * 0.9f);
	public static int STICK_Y_TOP = (int)(WINDOW_HEIGHT * 0.1f);
	public static float STICK_SPEED = 0.5f * BACKGROUND_SCALE * SPEED_MULTIPLIER;

	// Buttons
	public static float MENU_ENTRY_SCALE = BACKGROUND_SCALE;
	public static float Entity_SCALE = 0.55f * BACKGROUND_SCALE;

	// Button positions
	// MainMenu
	public static int MAIN_MENU_BUTTON_1_X = (int) (WINDOW_WIDTH * 0.38f);
	public static int MAIN_MENU_BUTTON_2_X = (int) (WINDOW_WIDTH * 0.64f);

	public static int MAIN_MENU_BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.53f);
	public static int MAIN_MENU_BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.638f);
	public static int MAIN_MENU_BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.746f);

	// Options
	public static int BUTTON_1_X_WIDE = (int) (WINDOW_WIDTH * 0.308f);
	public static int BUTTON_1_X = (int) (WINDOW_WIDTH * 0.27f);
	public static int BUTTON_2_X = (int) (WINDOW_WIDTH * 0.508f);
	public static int BUTTON_3_X = (int) (WINDOW_WIDTH * 0.708f);

	public static int BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.44f);
	public static int BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.49f);
	public static int BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.54f);
	public static int BUTTON_4_Y = (int) (WINDOW_HEIGHT * 0.59f);
	public static int BUTTON_5_Y = (int) (WINDOW_HEIGHT * 0.64f);
	public static int BUTTON_6_Y = (int) (WINDOW_HEIGHT * 0.69f);
	public static int BUTTON_7_Y = (int) (WINDOW_HEIGHT * 0.74f);
	public static int BUTTON_8_Y = (int) (WINDOW_HEIGHT * 0.84f);

	// GUI
	//Lives
	public static int LIVES_POS_X = (int) (10 / BACKGROUND_SCALE);
	// Blocks
	public static int ACTIVE_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
	public static int DESTROYED_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
	// active balls
	public static int ACTIVE_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
	public static int ACTIVE_DESTROYED_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
	// points
	public static int POINTS_POS_X = (int) (10 / BACKGROUND_SCALE);
	// timer
	public static int TIMER_X = (int) ((WINDOW_WIDTH / 2 - 20) / BACKGROUND_SCALE);


	public static void recalculate() {

		// Window Sizes (Aspect ratio = 4:3)
		WINDOW_WIDTH = OptionsHandler.getWindow_x();
		WINDOW_HEIGHT = OptionsHandler.getWindow_y();
		// image
		BACKGROUND_WIDTH = 1640; // the image width; error if = 0
		BACKGROUND_SCALE = (float) WINDOW_WIDTH  / (BACKGROUND_WIDTH - 40);

		// Block
		BLOCK_SCALE = (float) ((WINDOW_WIDTH) / 18.0 / Constants.BLOCK_IMAGE_X);

		// Ball
		INITIAL_BALL_SPEED_UP = 0.2f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
		INITIAL_BALL_SPEED_RIGHT = 0.075f * BACKGROUND_SCALE * SPEED_MULTIPLIER;
		INITIAL_TOTAL_SPEED = (float) Math.sqrt((INITIAL_BALL_SPEED_UP * INITIAL_BALL_SPEED_UP) + (INITIAL_BALL_SPEED_RIGHT * INITIAL_BALL_SPEED_RIGHT));
		SPEEDUP_VALUE = (INITIAL_TOTAL_SPEED * (MAX_SPEED_MULTIPLIER - 1f)) / HITS_TO_MAX_SPEED;

		// Stick
		STICK_Y = (int)(WINDOW_HEIGHT * 0.9f);
		STICK_Y_TOP = (int)(WINDOW_HEIGHT * 0.1f);
		STICK_SPEED = 0.5f * BACKGROUND_SCALE * SPEED_MULTIPLIER;

		// Buttons
		MENU_ENTRY_SCALE = BACKGROUND_SCALE;
		Entity_SCALE = 0.55f * BACKGROUND_SCALE;

// Button positions
		// MainMenu
		MAIN_MENU_BUTTON_1_X = (int) (WINDOW_WIDTH * 0.38f);
		MAIN_MENU_BUTTON_2_X = (int) (WINDOW_WIDTH * 0.64f);

		MAIN_MENU_BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.53f);
		MAIN_MENU_BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.638f);
		MAIN_MENU_BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.746f);

		// Options
		BUTTON_1_X_WIDE = (int) (WINDOW_WIDTH * 0.308f);
		BUTTON_1_X = (int) (WINDOW_WIDTH * 0.27f);
		BUTTON_2_X = (int) (WINDOW_WIDTH * 0.508f);
		BUTTON_3_X = (int) (WINDOW_WIDTH * 0.708f);

		BUTTON_1_Y = (int) (WINDOW_HEIGHT * 0.44f);
		BUTTON_2_Y = (int) (WINDOW_HEIGHT * 0.49f);
		BUTTON_3_Y = (int) (WINDOW_HEIGHT * 0.54f);
		BUTTON_4_Y = (int) (WINDOW_HEIGHT * 0.59f);
		BUTTON_5_Y = (int) (WINDOW_HEIGHT * 0.64f);
		BUTTON_6_Y = (int) (WINDOW_HEIGHT * 0.69f);
		BUTTON_7_Y = (int) (WINDOW_HEIGHT * 0.74f);
		BUTTON_8_Y = (int) (WINDOW_HEIGHT * 0.84f);

		// GUI
		//Lives
		LIVES_POS_X = (int) (10 / BACKGROUND_SCALE);
		// Blocks
		ACTIVE_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
		DESTROYED_BLOCKS_POS_X = (int) (10 / BACKGROUND_SCALE);
		// active balls
		ACTIVE_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
		ACTIVE_DESTROYED_BALLS_POS_X = (int) (10 / BACKGROUND_SCALE);
		// pos
		POINTS_POS_X = (int) (10 / BACKGROUND_SCALE);
		// timer
		TIMER_X = (int) ((WINDOW_WIDTH / 2 - 20) / BACKGROUND_SCALE);
	}
}
