package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.ButtonEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Andreas on 06.03.2017.
 * @author Andreas Dörr
 */
public class OptionsState extends BasicGameState {

	private int stateID; 							// identifier of this BasicGameState
	private StateBasedEntityManager entityManager; 	// related entityManager

	OptionsState( int sid ) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses State's ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// background
		// creating background-entity
		Entity background = new Entity(Constants.MENU_ID);
		background.setPosition(new Vector2f((Variables.WINDOW_WIDTH / 2),(Variables.WINDOW_HEIGHT / 2)));
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BLANK_BACKGROUND)));
		}
		background.setScale(Variables.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

	// langSelect-entity
		ButtonEntity langSelect = new ButtonEntity("langSelect", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_1_Y);
		langSelect.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			LanguageHandler.switchLang();
		});

	// resolution-entity
		ButtonEntity resolutionSelect = new ButtonEntity("resolutionSelect", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_2_X, Variables.BUTTON_1_Y);
		resolutionSelect.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (OptionsHandler.getWindow_x() == 800) {
				OptionsHandler.updateWindow(gc, sb, stateID, 1200, 900);
			} else if (OptionsHandler.getWindow_x() == 1200) {
				OptionsHandler.updateWindow(gc, sb, stateID, 1600, 1200);
			}else {
				OptionsHandler.updateWindow(gc, sb, stateID, 800, 600);
			}
			OptionsHandler.saveOptions();
		});

	// controllerSelect-entity
		ButtonEntity controllerSelect = new ButtonEntity("controllerSelect", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_3_X, Variables.BUTTON_1_Y);
		controllerSelect.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			ControllerHandler.switchController();
		});

	// show_fps-entity
		ButtonEntity showFps = new ButtonEntity("showFps", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_2_Y);
		showFps.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (gc.isShowingFPS()) {
				gc.setShowFPS(false);
				OptionsHandler.setShowFPS(false);
			} else {
				gc.setShowFPS(true);
				OptionsHandler.setShowFPS(true);
			}
			OptionsHandler.saveOptions();
		});


	// controlModeSelector_Entity
		ButtonEntity controlModeSelector = new ButtonEntity("controlModeSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_3_Y);
		controlModeSelector.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (OptionsHandler.getControlMode() == 0) {
				OptionsHandler.setControlMode(1);
			} else if (OptionsHandler.getControlMode() == 1) {
				OptionsHandler.setControlMode(2);
			} else {
				OptionsHandler.setControlMode(0);
			}
			OptionsHandler.saveOptions();
		});

	// fullscreenModeSelector_Entity
		ButtonEntity fullscreenSelector = new ButtonEntity("fullscreenSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_2_X, Variables.BUTTON_2_Y);
		fullscreenSelector.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (OptionsHandler.isFullscreen()) {
				OptionsHandler.setFullscreen(false);
			} else {
				OptionsHandler.setFullscreen(true);
			}
			OptionsHandler.saveOptions();
		});

	// gameModeSelector_Entity
		ButtonEntity gameModeSelector = new ButtonEntity("gameModeSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_4_Y);
		gameModeSelector.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (OptionsHandler.getGameMode() == 0) {
				OptionsHandler.setGameMode(1);
			} else {
				OptionsHandler.setGameMode(0);
			}
			OptionsHandler.saveOptions();
		});

	// cheatModeSelector_Entity
		ButtonEntity cheatModeSelector = new ButtonEntity("cheatModeSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_5_Y);
		cheatModeSelector.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			if (OptionsHandler.isCheatModeActive()) {
				OptionsHandler.setCheatMode(false);
			} else {
				OptionsHandler.setCheatMode(true);
			}
			OptionsHandler.saveOptions();
		});

	// mapSelector_Entity
		ButtonEntity mapSelector = new ButtonEntity("mapSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_6_Y);
		mapSelector.addAction((gc, sb, delta, event) -> {
			SoundHandler.playButtonPress();
			LevelHandler.switchMap();
		});

	// themeSelector_Entity
		ButtonEntity themeSelector = new ButtonEntity("themeSelector", stateID, Constants.ButtonType.WIDE, Variables.BUTTON_1_X_WIDE, Variables.BUTTON_7_Y);
		themeSelector.addAction((gc, sb, delta, event) -> {
			if (!GameplayState.currentlyRunning) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.getThemeSelector() < Constants.MAX_THEMES - 1) {
					OptionsHandler.setThemeSelector(OptionsHandler.getThemeSelector() + 1);
				} else {
					OptionsHandler.setThemeSelector(0);
				}
				ThemeHandler.initTheme();

				OptionsHandler.saveOptions();

				// try to init the States thereby reloading textures
				try {
					sb.init(gc);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} else {
				SoundHandler.playNotAcceptable();
			}
		});

	// back-entity
		ButtonEntity back = new ButtonEntity("back", stateID, Constants.ButtonType.NORMAL, Variables.BUTTON_1_X, Variables.BUTTON_8_Y);
		back.addAction(new ChangeStateAction(Breakout.MAINMENU_STATE));
		back.addAction((gc, sb, delta, event) -> SoundHandler.playButtonPress());
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		entityManager.updateEntities(gc, sb, delta);

		OptionsHandler.updateWindow(gc, sb, stateID);
	}


	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game,
					   Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);

		// scaling texts
		g.scale(Variables.BACKGROUND_SCALE, Variables.BACKGROUND_SCALE);

		// Buttons
		// back
		g.drawString(LanguageHandler.BUTTON_BACK, (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.BUTTON_8_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		// switch language
		g.drawString(LanguageHandler.BUTTON_SWITCH_LANGUAGE + ": " + OptionsHandler.getSelectedLangName(), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		// switch resolution
		g.drawString(LanguageHandler.BUTTON_SWITCH_RESOLUTION + ": " + OptionsHandler.getWindow_x() + "x" + OptionsHandler.getWindow_y(), (Variables.BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		if (OptionsHandler.getWindow_x() != Variables.WINDOW_WIDTH){
			g.setColor(Color.red);
			g.drawString(LanguageHandler.REQUIRES_RESTART, (Variables.BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y + 15));
			g.setColor(Color.white);
		}
		// switch controller
		g.drawString(LanguageHandler.BUTTON_SWITCH_CONTROLLER, (Variables.BUTTON_3_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		// show selected controller
		g.drawString(OptionsHandler.getSelectedController() + ") " + ControllerHandler.getControllerName(), (Variables.BUTTON_3_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y + 35));
		// show acceptable?
		String missingForAcceptableController = ControllerHandler.missingForAcceptableController();
		if (!missingForAcceptableController.equals(LanguageHandler.CONTROLLER_OK)) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.green);
		}
		g.drawString(missingForAcceptableController, (Variables.BUTTON_3_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X - 25), (Variables.BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y + 50));
		g.setColor(Color.white);

		// switch showFPS
		g.drawString(LanguageHandler.BUTTON_SHOW_FPS + ": " + LanguageHandler.yesOrNo(OptionsHandler.isShowingFPS()), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_2_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));

		g.drawString("Fullscreen: " + LanguageHandler.yesOrNo(OptionsHandler.isFullscreen()), (Variables.BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_2_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		g.drawString("Controll Mode: " + OptionsHandler.getControlModeName(), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_3_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		g.drawString("GameMode: " + (OptionsHandler.getGameMode()), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_4_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		g.drawString("CheatMode: " + LanguageHandler.yesOrNo(OptionsHandler.isCheatModeActive()), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_5_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		g.drawString(LanguageHandler.SELECTED_MAP + ": " + OptionsHandler.getSelectedMapName(), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_6_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		g.drawString("Theme: " + (OptionsHandler.getThemeSelector() + 1), (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_7_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		if (GameplayState.currentlyRunning) {
			g.setColor(Color.red);
			g.drawString("Not allowed while game is still running!", (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X_WIDE), (Variables.BUTTON_7_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y + 15));
			g.setColor(Color.white);
		}

		// lang and map info
		String fileInfo = "\n" +
				"Expand the Game:\n" +
				"To add your own maps:\n" +
				"  create a .map file,\n" +
				"  put it in the maps folder,\n" +
				"  reference it in the options file\n" +
				"To add another language:\n" +
				"  create a .lang file,\n" +
				"  put it in the lang folder,\n" +
				"  reference it in the options file\n";
		g.drawString(fileInfo, (Variables.WINDOW_WIDTH * 0.41f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.53f) / Variables.BACKGROUND_SCALE);


		// Controller binding info
		String controllerInfo = "Controller Keybinding:\n" +
				"In the Mainmenu:\n" +
				"  Button 4: New Game\n" +
				"  Button 3: Resume Game\n" +
				"While Playing:\n" +
				"  Right Stick X: Bottom-Stick\n" +
				"  Left Stick X: Top-Stick\n" +
				"  Button 1: StickBot\n" +
				"  Button 2: Main Menu\n" +
				"  Button 3: Pause\n" +
				"  Button 4: New Ball\n" +
				"In Highscore:\n" +
				"  Button 2: Back";
		// 4.5 0.
		g.drawString(controllerInfo, (Variables.WINDOW_WIDTH * 0.64f) / Variables.BACKGROUND_SCALE,(Variables.WINDOW_HEIGHT * 0.6f) / Variables.BACKGROUND_SCALE);
	}

	@Override
	public int getID() {
		return stateID;
	}

}