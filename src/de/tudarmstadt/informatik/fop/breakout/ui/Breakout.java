package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Breakout extends StateBasedGame implements Constants {

    private static BasicGameState[] gameStates = new BasicGameState[Constants.NUMBER_OF_STATES];

	// Remember if the game runs in debug mode
	private static boolean debug = false;

	private static AppGameContainer app;

	/**
	 * Creates a new Breakout instance
	 *
	 * @param debug if true, runs in debug mode
	 */
	public Breakout(boolean debug) {
		super("Breakout");
		Breakout.debug = debug;

        gameStates[Constants.MAINMENU_STATE] = new MainMenuState(MAINMENU_STATE);
        gameStates[Constants.GAMEPLAY_STATE] = new GameplayState(Constants.GAMEPLAY_STATE);
        gameStates[Constants.HIGHSCORE_STATE] = new HighscoreState(HIGHSCORE_STATE);
        gameStates[Constants.OPTIONS_STATE] = new OptionsState(OPTIONS_STATE);
        gameStates[Constants.ABOUT_STATE] = new AboutState(ABOUT_STATE);
    }

	public static AppGameContainer getApp() {
		return app;
	}

	@Deprecated
	public static void setApp(AppGameContainer newApp) {
		app = newApp;
	}

	public static boolean getDebug() {
		return debug;
	}

	public static void main(String[] args) throws SlickException {
		// Set the library path depending on the operating system
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/"
							+ System.getProperty("os.name").toLowerCase());
		}

		// Add this StateBasedGame to an AppGameContainer
		app = new AppGameContainer(new Breakout(false));

		// read option-file
		OptionsHandler.readOptions();

		// reading highscore file
		HighscoreHandler.readHighscores();

		SoundHandler.init();

		// initialize the controller
		if (!debug) {
			ControllerHandler.init();
		}

		// reading the language file
		LanguageHandler.readLang();

		// creating the paths to the images
		ThemeHandler.initTheme();

		// Set the display mode and frame rate
		app.setDisplayMode(OptionsHandler.getWindow_x(), OptionsHandler.getWindow_y(), false);

		app.setResizable(true);

		app.setTargetFrameRate(FRAME_RATE);

		// render even if not in focus
		app.setAlwaysRender(true);

		// set the window-icon
		app.setIcon(Constants.WINDOW_ICON);
		// set if FPS is to be shown or not
		app.setShowFPS(OptionsHandler.isShowingFPS());

        Variables.recalculate();

		// now start the game!
		app.start();
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {

        // Add the game gameStates (the first added state will be started initially)
        for (int i = 0; i < Constants.NUMBER_OF_STATES; i++) {
            addState(gameStates[i]);
        }

        // Add the gameStates to the StateBasedEntityManager
        StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
        entityManager.addState(MAINMENU_STATE);
        entityManager.addState(GAMEPLAY_STATE);
        entityManager.addState(HIGHSCORE_STATE);
        entityManager.addState(OPTIONS_STATE);
        entityManager.addState(ABOUT_STATE);

	}

	public static void reinitStates(GameContainer gc, StateBasedGame sb, int stateID) {
        // forcing init for all gameStates
        StateBasedEntityManager.getInstance().clearEntitiesFromState(stateID);
		try {
			gc.getInput().clearKeyPressedRecord();
			gc.getInput().clearControlPressedRecord();
			gc.getInput().clearMousePressedRecord();
			sb.init(gc);
		} catch (SlickException var6) {
			var6.printStackTrace();
		}
		if (gc.isPaused()) {
			gc.resume();
		}
	}

    public static BasicGameState getGameState(int id) {
        return gameStates[id];
    }
}