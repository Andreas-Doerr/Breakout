package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;

/**
 * Created by PC - Andreas on 23.03.2017.
 *
 * @author Andreas Dörr
 */
public class ThemeHandler implements GameParameters {

	// backgrounds
	public static String MENU_BACKGROUND;
	public static String MENU_BLANK_BACKGROUND;
	public static String GAME_BACKGROUND;

	// blocks
	public static String BLOCK_1;
	public static String BLOCK_2;
	public static String BLOCK_3;
	public static String BLOCK_4;
	public static String BLOCK_5;

	// balls
	public static String STANDARDBALL;
	public static String WATERBALL;
	public static String FIREBALL;
	public static String DESTROYED_BALL;

	// sticks
	public static String STICK;
	public static String STICK_SLIM;
	public static String STICK_WIDE;

	// items
	public static String SMALLER;
	public static String BIGGER;
	public static String SLOWER;
	public static String FASTER;
	public static String DUP;
	public static String DESTROY_BALL;

	// buttons
	public static String BUTTON;
	public static String SELECTED_BUTTON;
	public static String BUTTON_WIDE;
	public static String SELECTED_BUTTON_WIDE;
	public static String MENU_BUTTON;
	public static String SELECTED_MENU_BUTTON;

	// miscelanious
	public static String PAUSE;


	public static void initTheme() {
		String imagesFolder = IMAGES_FOLDER;
		String themeFolder;
		int selectedTheme = OptionsHandler.getThemeSelector();
		if (selectedTheme == 0) {
			themeFolder = THEME_0_FOLDER;
		} else {
			themeFolder = THEME_1_FOLDER;
		}
		String folder = imagesFolder + themeFolder;

		MENU_BACKGROUND = folder + GameParameters.MENU_BACKGROUND + IMAGE_ENDING;
		MENU_BLANK_BACKGROUND = folder + GameParameters.MENU_BLANK_BACKGROUND + IMAGE_ENDING;
		GAME_BACKGROUND = folder + GameParameters.GAME_BACKGROUND + IMAGE_ENDING;

		// blocks
		BLOCK_1 = folder + GameParameters.BLOCK_1 + IMAGE_ENDING;
		BLOCK_2 = folder + GameParameters.BLOCK_2 + IMAGE_ENDING;
		BLOCK_3 = folder + GameParameters.BLOCK_3 + IMAGE_ENDING;
		BLOCK_4 = folder + GameParameters.BLOCK_4 + IMAGE_ENDING;
		BLOCK_5 = folder + GameParameters.BLOCK_5 + IMAGE_ENDING;

		// balls
		STANDARDBALL = folder + GameParameters.STANDARDBALL + IMAGE_ENDING;
		WATERBALL = folder + GameParameters.WATERBALL + IMAGE_ENDING;
		FIREBALL = folder + GameParameters.FIREBALL + IMAGE_ENDING;
		DESTROYED_BALL = folder + GameParameters.DESTROYED_BALL + IMAGE_ENDING;

		// sticks
		STICK = folder + GameParameters.STICK + IMAGE_ENDING;
		STICK_SLIM = folder + GameParameters.STICK_SLIM + IMAGE_ENDING;
		STICK_WIDE = folder + GameParameters.STICK_WIDE + IMAGE_ENDING;

		// items
		SMALLER = folder + GameParameters.SMALLER + IMAGE_ENDING;
		BIGGER = folder + GameParameters.BIGGER + IMAGE_ENDING;
		SLOWER = folder + GameParameters.SLOWER + IMAGE_ENDING;
		FASTER = folder + GameParameters.FASTER + IMAGE_ENDING;
		DUP = folder + GameParameters.DUP + IMAGE_ENDING;
		DESTROY_BALL = folder + GameParameters.DESTROY_BALL + IMAGE_ENDING;

		// buttons
		BUTTON = folder + GameParameters.BUTTON + IMAGE_ENDING;
		SELECTED_BUTTON = folder + GameParameters.SELECTED_BUTTON + IMAGE_ENDING;
		BUTTON_WIDE = folder + GameParameters.BUTTON_WIDE + IMAGE_ENDING;
		SELECTED_BUTTON_WIDE = folder + GameParameters.SELECTED_BUTTON_WIDE + IMAGE_ENDING;
		MENU_BUTTON = folder + GameParameters.MENU_BUTTON + IMAGE_ENDING;
		SELECTED_MENU_BUTTON = folder + GameParameters.SELECTED_MENU_BUTTON + IMAGE_ENDING;

		// miscelanious
		PAUSE = folder + GameParameters.PAUSE + IMAGE_ENDING;

	}
}
