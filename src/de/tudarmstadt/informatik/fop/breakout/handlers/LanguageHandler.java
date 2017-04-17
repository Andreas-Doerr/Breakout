package de.tudarmstadt.informatik.fop.breakout.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Andreas Dörr
 */	//TODO commenting
public class LanguageHandler {

	public static String BUTTON_NEW_GAME;
	public static String BUTTON_RESUME_GAME;
	public static String BUTTON_INACTIVE_RESUME_GAME;
	public static String BUTTON_HIGHSCORE;
	public static String BUTTON_OPTIONS;
	public static String BUTTON_QUIT;
	public static String BUTTON_ABOUT;
	public static String BUTTON_BACK;
	public static String BUTTON_SWITCH_LANGUAGE;
	public static String BUTTON_SWITCH_RESOLUTION;
	public static String BUTTON_SWITCH_CONTROLLER;
	public static String BUTTON_SHOW_FPS;
	public static String LIVES_LEFT;
	public static String ACTIVE_BLOCKS;
	public static String DESTROYED_BLOCKS;
	public static String BALLS_ACTIVE;
	public static String DESTROYED_BALLS_ACTIVE;
	public static String TIMER;
	public static String REQUIRES_RESTART;
	public static String YES;
	public static String NO;
	public static String NO_AXIS;
	public static String NO_BUTTON;
	public static String POINTS;
	public static String VICTORY;
	public static String GAME_OVER;
	public static String IT_TOOK_YOU;
	public static String YOU_DESTROYED;
	public static String YOU_SCORED;
	public static String X_SECONDS;
	public static String X_BLOCKS;
	public static String X_POINTS;
	public static String CONTROLLER_OK;
	public static String NAMES;
	public static String SELECTED_MAP;

	public static void readLang() {

		String selectedLang = OptionsHandler.getAvailableLanguage(OptionsHandler.getLangSelector());

		try {
			String[] langContent = FileHandler.read("lang/" + selectedLang + ".lang", 35);

			BUTTON_NEW_GAME = langContent[0];
			BUTTON_RESUME_GAME = langContent[1];
			BUTTON_INACTIVE_RESUME_GAME = langContent[2];
			BUTTON_HIGHSCORE = langContent[3];
			BUTTON_OPTIONS = langContent[4];
			BUTTON_QUIT = langContent[5];
			BUTTON_ABOUT = langContent[6];
			BUTTON_BACK = langContent[7];
			BUTTON_SWITCH_LANGUAGE = langContent[8];
			BUTTON_SWITCH_RESOLUTION = langContent[9];
			BUTTON_SWITCH_CONTROLLER = langContent[10];
			BUTTON_SHOW_FPS = langContent[11];
			LIVES_LEFT = langContent[12];
			ACTIVE_BLOCKS = langContent[13];
			DESTROYED_BLOCKS = langContent[14];
			BALLS_ACTIVE = langContent[15];
			DESTROYED_BALLS_ACTIVE = langContent[16];
			TIMER = langContent[17];
			REQUIRES_RESTART = langContent[18];
			YES = langContent[19];
			NO = langContent[20];
			NO_AXIS = langContent[21];
			NO_BUTTON = langContent[22];
			POINTS = langContent[23];
			VICTORY = langContent[24];
			GAME_OVER = langContent[25];
			IT_TOOK_YOU = langContent[26];
			YOU_DESTROYED = langContent[27];
			YOU_SCORED = langContent[28];
			X_SECONDS = langContent[29];
			X_BLOCKS = langContent[30];
			X_POINTS = langContent[31];
			CONTROLLER_OK = langContent[32];
			NAMES = langContent[33];
			SELECTED_MAP = langContent[34];
		} catch (FileNotFoundException e) {
			System.out.println("WARNING: Skipping language \"" + selectedLang + "\" since it's .lang file can not be found.");
			switchLang();
		} catch (IOException ioE) {
			System.err.println("ERROR: Could not read lang file.");
			ioE.printStackTrace();
		}

	}

	public static void switchLang() {
		if (OptionsHandler.getLangSelector() < OptionsHandler.getMaxLanguages() - 1) {
			OptionsHandler.setLangSelector(OptionsHandler.getLangSelector() + 1);
			OptionsHandler.saveOptions();
		} else {
			OptionsHandler.setLangSelector(0);
			OptionsHandler.saveOptions();
		}
		readLang();
	}

	public static String yesOrNo(boolean yes) {
		if (yes) {
			return YES;
		} else {
			return NO;
		}
	}

}
