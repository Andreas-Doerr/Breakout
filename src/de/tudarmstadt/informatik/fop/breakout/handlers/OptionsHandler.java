package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.*;

/**
 * Created by PC - Andreas on 08.03.2017.
 *
 * @author Andreas Dörr
 */	//TODO commenting
public class OptionsHandler {

	private static int window_x = 1200;
	private static int window_y = 1200 / 4 * 3;
	private static boolean fullscreen = false;
	private static boolean showFPS = false;
	private static String[] availableLanguages = new String[Constants.MAX_LANGUAGES];
	private static int langSelector = 0;
	private static int controlMode = 0; // 0=keyboard, 1=mouse, 2=controller
	private static int selectedController = 0;
	private static int gameMode = 0;
	private static boolean cheatMode = false;
	private static String[] availableMaps = new String[Constants.MAX_MAPS];
	private static int selectedMap = 0;
	private static int themeSelector = 0;

	// read / write Options
	public static void readOptions() {
		try {
			String[] optionsContent = FileHandler.read(Constants.OPTIONS_FILE, 12);

			try {
				window_x = Integer.valueOf(optionsContent[0]);
				window_y = window_x / 4 * 3;
				fullscreen = Boolean.valueOf(optionsContent[1]);
				availableLanguages = optionsContent[2].split(",");
				langSelector = Integer.valueOf(optionsContent[3]);
				showFPS = Boolean.valueOf(optionsContent[4]);
				controlMode = Integer.valueOf(optionsContent[5]);
				selectedController = Integer.valueOf(optionsContent[6]);
				gameMode = Integer.valueOf(optionsContent[7]);
				cheatMode = Boolean.valueOf(optionsContent[8]);
				availableMaps = optionsContent[9].split(",");
				selectedMap = Integer.valueOf(optionsContent[10]);
				themeSelector = Integer.valueOf(optionsContent[11]);

			} catch (NumberFormatException nfE) {
				String[] test = nfE.getLocalizedMessage().split(": ");
				System.err.println("ERROR: Corrupted options-file! The entry: " + test[1] + " is not a number, but should be one!");
			}
		} catch (FileNotFoundException fnfE) {
			System.err.println("Could not find options-file at: " + Constants.OPTIONS_FILE);
			System.out.println("INFO: Creating new options.config-file based on default parameters.");
			saveOptions();
		} catch (IOException ioE) {
			System.err.println("ERROR: Could not read options file.");
			ioE.printStackTrace();
		}
	}

	public static void saveOptions() {
		String availableLanguagesAsString = "";
		if (availableLanguages[0] != null) {
			for (String thisAvailableLanguage : availableLanguages) {
				availableLanguagesAsString += thisAvailableLanguage + ",";
			}
		} else {
			availableLanguagesAsString = "en,de,";
			int i = 2;
			while (i < Constants.MAX_LANGUAGES && availableLanguages[i] == null) {
				availableLanguagesAsString += "placeholder,";
				i++;
			}
			availableLanguages = availableLanguagesAsString.split(",");
		}

		String availableMapsAsString = "";
		if (availableMaps[0] != null) {
			for (String thisAvailableMap : availableMaps) {
				availableMapsAsString += thisAvailableMap + ",";
			}
		} else {
			availableMapsAsString = "Blume,FOP-Logo,Herz,Parallelen,Pyramide,Rennauto,Schildkroete,level_a,level_b,level_c,level_d,level_e,Explosions,";
			int i = 12;
			while (i < Constants.MAX_MAPS && availableMaps[i] == null) {
				availableMapsAsString += "placeholder,";
				i++;
			}
			availableMaps = availableMapsAsString.split(",");
		}

		String toWrite = "### Options for Breakout ###\n#" +
				"\n###### GENERAL ######\n#" +
				"\n### RESOLUTION" +
				"\n# The Aspect ratio is always 4:3, so Resolution y is always x / 4 * 3" +
				"\n# Resolution x:\n" + window_x +
				"\n# Fullscreen:\n" + fullscreen +
				"\n###### UI ######\n#" +
				"\n### LANGUAGE" +
				"\n# available languages separated by \",\"\n" + availableLanguagesAsString +
				"\n# selected language (0 would be the first of the available Languages specified above,1 would be the second...)\n" + langSelector +
				"\n### FPS ###" +
				"\n# Show FPS:\n" + showFPS +
				"\n###### Controls ######\n#" +
				"\n### General" +
				"\n# controlMode (0=keyboard, 1=mouse, 2=controller\n" + controlMode +
				"\n### Controller" +
				"\n# selected controller\n" + selectedController +
				"\n###### Game Mode ######\n#" +
				"\n# GameMode =0 means standard, =1means coop-mode\n" + gameMode +
				"\n# cheatMode =0 nocheats allowed\n" + cheatMode +
				"\n###### Level ######\n#" +
				"\n### Maps" +
				"\n# available Maps separated by \",\"\n" + availableMapsAsString +
				"\n# selected Map:\n" + selectedMap +
				"\n# themeSelector\n" + themeSelector;

		FileHandler.write(Constants.OPTIONS_FILE, toWrite);
	}

	// getter
	public static int getWindow_x() {
		return window_x;
	}
	public static int getWindow_y() {
		return window_y;
	}
	public static boolean isFullscreen() {
		return fullscreen;}
	public static boolean isShowingFPS() {
		return showFPS;
	}
	public static String getAvailableLanguage(int index) {
		return availableLanguages[index];
	}
	public static int getMaxLanguages() {
		return availableLanguages.length;
	}
	public static int getLangSelector() {
		return langSelector;
	}
	public static String getSelectedLangName() {
		return availableLanguages[langSelector];
	}
	public static int getSelectedController() {
		return selectedController;
	}
	public static int getGameMode() {
		return gameMode;
	}
	public static boolean isCheatModeActive() {return cheatMode;}
	public static int getControlMode() {
		return controlMode;
	}
	public static String getControlModeName() {
		String controlModeName = "ERROR";
		if (controlMode == 0) {
			controlModeName = "keyboard";
		} else if (controlMode == 1) {
			controlModeName = "mouse";
		} else if (controlMode == 2) {
			controlModeName = "controller";
		}
		return controlModeName;
	}
	public static int getSelectedMap() {
		return selectedMap;
	}
	public static String getSelectedMapName() {
		try {
			return availableMaps[selectedMap];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("ERROR: SelectedMap is higher than the amount of maps available");
			selectedMap = 0;
			return null;
		}
	}
	public static int getThemeSelector() {
		return themeSelector;
	}

	// setter
	public static void setFullscreen(boolean newFullcreen) {
		fullscreen = newFullcreen;
		resetWindow();
	}
	public static void setShowFPS(boolean newShowFPS) {
		showFPS = newShowFPS;
	}
	public static void setLangSelector(int newLang) {
		langSelector = newLang;
	}
	public static void setSelectedController(int newSelectedController) {
		selectedController = newSelectedController;
	}
	public static void setCheatMode(boolean new_cheatMode) {
		cheatMode = new_cheatMode;
	}
	public static void setGameMode(int new_gameMode) {
		gameMode = new_gameMode;
	}
	public static void setControlMode(int new_controlMode) {
		controlMode = new_controlMode;
	}
	public static void setSelectedMap(int new_selectedMap) {
		selectedMap = new_selectedMap;
	}
	public static void setThemeSelector(int new_themeSelector) {
		themeSelector = new_themeSelector;
	}

	// window
	public static boolean setWindowSize(int new_window_x, int new_window_y) {
		if (new_window_x != window_x || new_window_y != window_y) {
			if (new_window_x < Breakout.getApp().getScreenWidth() && (new_window_x / 4 * 3) <= Breakout.getApp().getScreenHeight() - 25) {
				if (new_window_x != window_x) {
					window_x = new_window_x;
					window_y = new_window_x / 4 * 3;
				} else {
					window_x = new_window_y / 3 * 4;
					window_y = new_window_y;
				}
			} else if (new_window_y != Breakout.getApp().getScreenHeight()){
				window_y = Breakout.getApp().getScreenHeight() - 25;
				window_x = window_y / 3 * 4;
			}
			// recalculate variables
			Variables.recalculate();
			resetWindow();
			return true;
		}
		return false;
	}
	public static void resetWindow() {
		try {
			Breakout.getApp().setDisplayMode(window_x,window_y, fullscreen);
			Breakout.getApp().setResizable(false);
			Breakout.getApp().setResizable(true);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public static void updateWindow(GameContainer gc, StateBasedGame sb, int stateID) {
		if (OptionsHandler.setWindowSize(gc.getWidth(), gc.getHeight())) {
			// init states (like changeStateInitAction just without changing states)
			StateBasedEntityManager.getInstance().clearEntitiesFromState(stateID);

			try {
				gc.getInput().clearKeyPressedRecord();
				gc.getInput().clearControlPressedRecord();
				gc.getInput().clearMousePressedRecord();
				sb.init(gc);
			} catch (SlickException var6) {
				var6.printStackTrace();
			}
		}
	}
	public static void updateWindow(GameContainer gc, StateBasedGame sb, int stateID, int newX, int newY) {
		if (OptionsHandler.setWindowSize(newX, newY)) {
			// init states (like changeStateInitAction just without changing states)
			StateBasedEntityManager.getInstance().clearEntitiesFromState(stateID);

			try {
				gc.getInput().clearKeyPressedRecord();
				gc.getInput().clearControlPressedRecord();
				gc.getInput().clearMousePressedRecord();
				sb.init(gc);
			} catch (SlickException var6) {
				var6.printStackTrace();
			}
		}
	}
}
