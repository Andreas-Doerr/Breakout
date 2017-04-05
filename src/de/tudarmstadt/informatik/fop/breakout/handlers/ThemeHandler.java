package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;

/**
 * Created by PC - Andreas on 23.03.2017.
 *
 * @author Andreas Dörr
 */
public class ThemeHandler implements Constants {

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
		String themeFolder;
		int selectedTheme = OptionsHandler.getThemeSelector();
		if (selectedTheme == 0) {
			themeFolder = THEME_0_FOLDER;
		} else {
			themeFolder = THEME_1_FOLDER;
		}
		String folder = IMAGES_FOLDER + themeFolder;

		MENU_BACKGROUND = folder + Constants.MENU_BACKGROUND + IMAGE_ENDING;
		MENU_BLANK_BACKGROUND = folder + Constants.MENU_BLANK_BACKGROUND + IMAGE_ENDING;
		GAME_BACKGROUND = folder + Constants.GAME_BACKGROUND + IMAGE_ENDING;

		// blocks
		BLOCK_1 = folder + Constants.BLOCK_1 + IMAGE_ENDING;
		BLOCK_2 = folder + Constants.BLOCK_2 + IMAGE_ENDING;
		BLOCK_3 = folder + Constants.BLOCK_3 + IMAGE_ENDING;
		BLOCK_4 = folder + Constants.BLOCK_4 + IMAGE_ENDING;
		BLOCK_5 = folder + Constants.BLOCK_5 + IMAGE_ENDING;

		// balls
		STANDARDBALL = folder + Constants.STANDARDBALL + IMAGE_ENDING;
		WATERBALL = folder + Constants.WATERBALL + IMAGE_ENDING;
		FIREBALL = folder + Constants.FIREBALL + IMAGE_ENDING;
		DESTROYED_BALL = folder + Constants.DESTROYED_BALL + IMAGE_ENDING;

		// sticks
		STICK = folder + Constants.STICK + IMAGE_ENDING;
		STICK_SLIM = folder + Constants.STICK_SLIM + IMAGE_ENDING;
		STICK_WIDE = folder + Constants.STICK_WIDE + IMAGE_ENDING;

		// items
		SMALLER = folder + Constants.SMALLER + IMAGE_ENDING;
		BIGGER = folder + Constants.BIGGER + IMAGE_ENDING;
		SLOWER = folder + Constants.SLOWER + IMAGE_ENDING;
		FASTER = folder + Constants.FASTER + IMAGE_ENDING;
		DUP = folder + Constants.DUP + IMAGE_ENDING;
		DESTROY_BALL = folder + Constants.DESTROY_BALL + IMAGE_ENDING;

		// buttons
		BUTTON = folder + Constants.BUTTON + IMAGE_ENDING;
		SELECTED_BUTTON = folder + Constants.SELECTED_BUTTON + IMAGE_ENDING;
		BUTTON_WIDE = folder + Constants.BUTTON_WIDE + IMAGE_ENDING;
		SELECTED_BUTTON_WIDE = folder + Constants.SELECTED_BUTTON_WIDE + IMAGE_ENDING;
		MENU_BUTTON = folder + Constants.MENU_BUTTON + IMAGE_ENDING;
		SELECTED_MENU_BUTTON = folder + Constants.SELECTED_MENU_BUTTON + IMAGE_ENDING;

		// miscellaneous
		PAUSE = folder + Constants.PAUSE + IMAGE_ENDING;

	}
}
