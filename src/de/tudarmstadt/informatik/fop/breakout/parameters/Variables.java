package de.tudarmstadt.informatik.fop.breakout.parameters;

import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;

import static de.tudarmstadt.informatik.fop.breakout.parameters.Constants.HITS_TO_MAX_SPEED;
import static de.tudarmstadt.informatik.fop.breakout.parameters.Constants.MAX_SPEED_MULTIPLIER;

/**
 * Created by PC - Andreas on 25.03.2017.
 *
 * @author Andreas Dörr
 */
public class Variables {

    // Speed Modifier
    public static int SPEED_MODIFIER = 1;


	// Window Sizes (Aspect ratio = 4:3)
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    // image
    public static int BACKGROUND_WIDTH;
    public static float BACKGROUND_SCALE;

	// Block
    public static float BLOCK_SCALE;

	// Ball
    public static float INITIAL_BALL_SPEED_UP;
    public static float INITIAL_BALL_SPEED_RIGHT;
    public static float INITIAL_TOTAL_SPEED;
    public static float SPEEDUP_VALUE;

	// Stick
    public static int STICK_Y;
    public static int STICK_Y_TOP;
    public static float STICK_SPEED;
    public static float COLLISION_DISTANCE;

	// Buttons
    public static float MENU_ENTRY_SCALE;
    public static float ENTRY_SCALE;

	// Button positions
	// MainMenu
    public static int MAIN_MENU_BUTTON_1_X;
    public static int MAIN_MENU_BUTTON_2_X;

    public static int MAIN_MENU_BUTTON_1_Y;
    public static int MAIN_MENU_BUTTON_2_Y;
    public static int MAIN_MENU_BUTTON_3_Y;

	// Options
    public static int BUTTON_1_X_WIDE;
    public static int BUTTON_1_X;
    public static int BUTTON_2_X;
    public static int BUTTON_3_X;

    public static int BUTTON_1_Y;
    public static int BUTTON_2_Y;
    public static int BUTTON_3_Y;
    public static int BUTTON_4_Y;
    public static int BUTTON_5_Y;
    public static int BUTTON_6_Y;
    public static int BUTTON_7_Y;
    public static int BUTTON_8_Y;

	// GUI
    public static int LEFT_X;
    // timer
    public static int TIMER_X;


	public static void recalculate() {

		// Window Sizes (Aspect ratio = 4:3)
		WINDOW_WIDTH = OptionsHandler.getWindow_x();
		WINDOW_HEIGHT = OptionsHandler.getWindow_y();
		// image
		BACKGROUND_WIDTH = 1640; // the image width; error if = 0
		BACKGROUND_SCALE = (float) WINDOW_WIDTH / (BACKGROUND_WIDTH - 40);

		// Block
		BLOCK_SCALE = (float) ((WINDOW_WIDTH) / 18.0 / Constants.BLOCK_IMAGE_X);

		// Ball
        INITIAL_BALL_SPEED_UP = Constants.BALL_SPEED_UP_BASE * BACKGROUND_SCALE;
        INITIAL_BALL_SPEED_RIGHT = Constants.BALL_SPEED_RIGHT_BASE * BACKGROUND_SCALE;
        INITIAL_TOTAL_SPEED = (float) Math.sqrt((INITIAL_BALL_SPEED_UP * INITIAL_BALL_SPEED_UP) + (INITIAL_BALL_SPEED_RIGHT * INITIAL_BALL_SPEED_RIGHT));
		SPEEDUP_VALUE = (INITIAL_TOTAL_SPEED * (MAX_SPEED_MULTIPLIER - 1f)) / HITS_TO_MAX_SPEED;

		// Stick
		STICK_Y = (int) (WINDOW_HEIGHT * 0.9f);
		STICK_Y_TOP = (int) (WINDOW_HEIGHT * 0.1f);
        STICK_SPEED = Constants.STICK_BASE_SPEED * BACKGROUND_SCALE / Constants.FRAME_RATE * 250;

		// Buttons
		MENU_ENTRY_SCALE = BACKGROUND_SCALE;
		ENTRY_SCALE = 0.55f * BACKGROUND_SCALE;

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
		LEFT_X = (int) (10 / BACKGROUND_SCALE);
		// timer
		TIMER_X = (int) ((WINDOW_WIDTH / 2 - 20) / BACKGROUND_SCALE);
	}

    public static void increaseSpeed() {
        if (SPEED_MODIFIER < 100)
            SPEED_MODIFIER += 1;
    }

    public static void decreaseSpeed() {
        if (SPEED_MODIFIER > 1) {
            SPEED_MODIFIER -= 1;
        }
    }
}
