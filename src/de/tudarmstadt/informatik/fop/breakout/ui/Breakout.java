package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.entity.StateBasedEntityManager;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;

public class Breakout extends StateBasedGame implements Constants {

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
		HighscoreHandler.readHighscore();

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
		// set the window-icon
		app.setIcon(Constants.WINDOW_ICON);
		// set if FPS is to be shown or not
		app.setShowFPS(OptionsHandler.isShowingFPS());

		// now start the game!
		app.start();
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {

		// Add the game states (the first added state will be started initially)
	  // This may look as follows, assuming you use the associated class names and parameters:

		addState(new MainMenuState(MAINMENU_STATE));
		addState(new GameplayState(GAMEPLAY_STATE));
		addState(new HighscoreState(HIGHSCORE_STATE));
		addState(new OptionsState(OPTIONS_STATE));
		addState(new AboutState(ABOUT_STATE));

		// Add the states to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
		StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);
		StateBasedEntityManager.getInstance().addState(HIGHSCORE_STATE);
		StateBasedEntityManager.getInstance().addState(OPTIONS_STATE);
		StateBasedEntityManager.getInstance().addState(ABOUT_STATE);

	}
}