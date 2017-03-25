package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
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
		Entity background = new Entity(GameParameters.MENU_ID);
		background.setPosition(new Vector2f((GameParameters.WINDOW_WIDTH / 2),(GameParameters.WINDOW_HEIGHT / 2)));
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BLANK_BACKGROUND)));
		}
		background.setScale(GameParameters.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

	// langSelect-entity
		Entity langSelect_Entity = new Entity("Language Selector");
		langSelect_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_1_Y));
		langSelect_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			langSelect_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the langSelect-entity
		entityManager.addEntity(stateID, langSelect_Entity);

		// creating trigger event and its actions
		ANDEvent langSelect_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		langSelect_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				LanguageHandler.switchLang();
			}
		});
		langSelect_Entity.addComponent(langSelect_Events);

	// resolution-entity
		Entity resolutionSelect_Entity = new Entity("Resolution Selector");
		resolutionSelect_Entity.setPosition(new Vector2f(GameParameters.BUTTON_2_X, GameParameters.BUTTON_1_Y));
		resolutionSelect_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			resolutionSelect_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the resolutionSelect-entity
		entityManager.addEntity(stateID, resolutionSelect_Entity);

		// creating trigger event and its actions
		ANDEvent resolutionSelect_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		resolutionSelect_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.getResolution_x() == 800) {
					OptionsHandler.setResolution_x(1280);
				} else if (OptionsHandler.getResolution_x() == 1280) {
					OptionsHandler.setResolution_x(1600);
				}else {
					OptionsHandler.setResolution_x(800);
				}
				OptionsHandler.saveOptions();
			}
		});
		resolutionSelect_Entity.addComponent(resolutionSelect_Events);

	// controllerSelect-entity
		Entity controllerSelect_Entity = new Entity("Controller Selector");
		controllerSelect_Entity.setPosition(new Vector2f(GameParameters.BUTTON_3_X, GameParameters.BUTTON_1_Y));
		controllerSelect_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			controllerSelect_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the controllerSelect-entity
		entityManager.addEntity(stateID, controllerSelect_Entity);

		// creating trigger event and its actions
		ANDEvent controllerSelect_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		controllerSelect_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				ControllerHandler.switchController();
			}
		});
		controllerSelect_Entity.addComponent(controllerSelect_Events);

	// show_fps-entity
		Entity show_fps_Entity = new Entity("Show FPS");
		show_fps_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_2_Y));
		show_fps_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			show_fps_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the show_fps-entity
		entityManager.addEntity(stateID, show_fps_Entity);

		// creating trigger event and its actions
		ANDEvent show_fps_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		show_fps_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				if (gc.isShowingFPS()) {
					gc.setShowFPS(false);
					OptionsHandler.setShowFPS(false);
				} else {
					gc.setShowFPS(true);
					OptionsHandler.setShowFPS(true);
				}
				OptionsHandler.saveOptions();
			}
		});
		show_fps_Entity.addComponent(show_fps_Events);


	// controlModeSelector_Entity
		Entity controlModeSelector_Entity = new Entity("controlModeSelector_Entity");
		controlModeSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_3_Y));
		controlModeSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			controlModeSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, controlModeSelector_Entity);

		// creating trigger event and its actions
		ANDEvent controlModeSelector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		controlModeSelector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.getControlMode() == 0) {
					OptionsHandler.setControlMode(1);
				} else if (OptionsHandler.getControlMode() == 1) {
					OptionsHandler.setControlMode(2);
				} else {
					OptionsHandler.setControlMode(0);
				}
				OptionsHandler.saveOptions();
			}
		});
		controlModeSelector_Entity.addComponent(controlModeSelector_Events);

	// fullscreenModeSelector_Entity
		Entity fullscreenSelector_Entity = new Entity("fullscreenModeSelector_Entity");
		fullscreenSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_2_X, GameParameters.BUTTON_2_Y));
		fullscreenSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			fullscreenSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, fullscreenSelector_Entity);

		// creating trigger event and its actions
		ANDEvent fullscreenSelector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		fullscreenSelector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.isFullscreen()) {
					OptionsHandler.setFullscreen(false);
				} else {
					OptionsHandler.setFullscreen(true);
				}
				OptionsHandler.saveOptions();
			}
		});
		fullscreenSelector_Entity.addComponent(fullscreenSelector_Events);

	// gameModeSelector_Entity
		Entity gameModeSelector_Entity = new Entity("gameModeSelector_Entity");
		gameModeSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_4_Y));
		gameModeSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			gameModeSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, gameModeSelector_Entity);

		// creating trigger event and its actions
		ANDEvent gameModeSelector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		gameModeSelector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.getGameMode() == 0) {
					OptionsHandler.setGameMode(1);
				} else {
					OptionsHandler.setGameMode(0);
				}
				OptionsHandler.saveOptions();
			}
		});
		gameModeSelector_Entity.addComponent(gameModeSelector_Events);


	// cheatModeSelector_Entity
		Entity cheatModeSelector_Entity = new Entity("cheatModeSelector_Entity");
		cheatModeSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_5_Y));
		cheatModeSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			cheatModeSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, cheatModeSelector_Entity);

		// creating trigger event and its actions
		ANDEvent cheatModeSelector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		cheatModeSelector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
				if (OptionsHandler.isCheatModeActive()) {
					OptionsHandler.setCheatMode(false);
				} else {
					OptionsHandler.setCheatMode(true);
				}
				OptionsHandler.saveOptions();
			}
		});
		cheatModeSelector_Entity.addComponent(cheatModeSelector_Events);


	// mapSelector_Entity
		Entity mapSelector_Entity = new Entity("mapSelector_Entity");
		mapSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_6_Y));
		mapSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			mapSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, mapSelector_Entity);

		// creating trigger event and its actions
		ANDEvent map_selector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		map_selector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
				LevelHandler.switchMap();
			}
		});
		mapSelector_Entity.addComponent(map_selector_Events);


	// themeSelector_Entity
		Entity themeSelector_Entity = new Entity("themeSelector_Entity");
		themeSelector_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_7_Y));
		themeSelector_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			themeSelector_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON_WIDE)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, themeSelector_Entity);

		// creating trigger event and its actions
		ANDEvent themeSelector_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		themeSelector_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				if (!GameplayState.currentlyRunning) {
					SoundHandler.playButtonPress();
					if (OptionsHandler.getThemeSelector() < GameParameters.MAX_THEMES - 1) {
						OptionsHandler.setThemeSelector(OptionsHandler.getThemeSelector() + 1);
					} else {
						OptionsHandler.setThemeSelector(0);
					}
					ThemeHandler.initTheme();

					OptionsHandler.saveOptions();

					// try to init the States thereby reloading textures
					try {
						stateBasedGame.init(gameContainer);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				} else {
					SoundHandler.playNotAcceptable();
				}
			}
		});
		themeSelector_Entity.addComponent(themeSelector_Events);


	// back-entity
		Entity back_Entity = new Entity("Back");
		back_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_8_Y));
		back_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			back_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(stateID, back_Entity);

		// creating trigger event and its actions
		ANDEvent back_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		back_Events.addAction(new ChangeStateAction(Breakout.MAINMENU_STATE));
		back_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
			}
		});
		back_Entity.addComponent(back_Events);

	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		entityManager.updateEntities(container, game, delta);
	}


	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game,
					   Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);

		// scaling texts
		g.scale(GameParameters.BACKGROUND_SCALE,GameParameters.BACKGROUND_SCALE);

		// Buttons
		// back
		g.drawString(LanguageHandler.BUTTON_BACK, (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.BUTTON_8_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		// switch language
		g.drawString(LanguageHandler.BUTTON_SWITCH_LANGUAGE + ": " + OptionsHandler.getSelectedLangName(), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		// switch resolution
		g.drawString(LanguageHandler.BUTTON_SWITCH_RESOLUTION + ": " + OptionsHandler.getResolution_x() + "x" + (int)(OptionsHandler.getResolution_x() / 4f * 3f), (GameParameters.BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		if (OptionsHandler.getResolution_x() != GameParameters.WINDOW_WIDTH){
			g.setColor(Color.red);
			g.drawString(LanguageHandler.REQUIRES_RESTART, (GameParameters.BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y + 15));
			g.setColor(Color.white);
		}
		// switch controller
		g.drawString(LanguageHandler.BUTTON_SWITCH_CONTROLLER, (GameParameters.BUTTON_3_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		// show selected controller
		g.drawString(OptionsHandler.getSelectedController() + ") " + ControllerHandler.getControllerName(), (GameParameters.BUTTON_3_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y + 35));
		// show acceptable?
		String missingForAcceptableController = ControllerHandler.missingForAcceptableController();
		if (!missingForAcceptableController.equals(LanguageHandler.CONTROLLER_OK)) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.green);
		}
		g.drawString(missingForAcceptableController, (GameParameters.BUTTON_3_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X - 25), (GameParameters.BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y + 50));
		g.setColor(Color.white);

		// switch showFPS
		g.drawString(LanguageHandler.BUTTON_SHOW_FPS + ": " + LanguageHandler.yesOrNo(OptionsHandler.isShowingFPS()), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_2_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));

		g.drawString("Fullscreen: " + LanguageHandler.yesOrNo(OptionsHandler.isFullscreen()), (GameParameters.BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_2_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		g.drawString("Controll Mode: " + OptionsHandler.getControlModeName(), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_3_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		g.drawString("GameMode: " + (OptionsHandler.getGameMode()), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_4_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		g.drawString("CheatMode: " + LanguageHandler.yesOrNo(OptionsHandler.isCheatModeActive()), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_5_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		g.drawString(LanguageHandler.SELECTED_MAP + ": " + OptionsHandler.getSelectedMapName(), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_6_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		g.drawString("Theme: " + (OptionsHandler.getThemeSelector() + 1), (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_7_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		if (GameplayState.currentlyRunning) {
			g.setColor(Color.red);
			g.drawString("Not allowed while game is still running!", (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X_WIDE), (GameParameters.BUTTON_7_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y + 15));
			g.setColor(Color.white);
		}


		// Controller binding info
		String controllerInfo = "Controller Keybinding:\n" +
				"  In the Mainmenu:\n" +
				"    Button 4: New Game\n" +
				"    Button 3: Resume Game\n" +
				"  While Playing:\n" +
				"    Right Stick X: Stick movement\n" +
				"    Button 1: StickBot\n" +
				"    Button 2: Main Menu\n" +
				"    Button 3: Pause\n" +
				"    Button 4: New Ball\n" +
				"  In Highscore:\n" +
				"    Button 2: Back";
		// 4.5 0.
		g.drawString(controllerInfo, (GameParameters.WINDOW_WIDTH * 0.62f) / GameParameters.BACKGROUND_SCALE,(GameParameters.WINDOW_HEIGHT * 0.6f) / GameParameters.BACKGROUND_SCALE);

	}

	@Override
	public int getID() {
		return stateID;
	}

}