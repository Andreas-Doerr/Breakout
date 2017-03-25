package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * @author Timo Bähr
 *
 * Diese Klasse repraesentiert das Menuefenster, indem ein neues
 * Spiel gestartet werden kann und das gesamte Spiel beendet 
 * werden kann.
 */
public class MainMenuState extends BasicGameState {

	private int stateID; 							// identifier of this BasicGameState
	private StateBasedEntityManager entityManager; 	// related entityManager
	MainMenuState( int sid ) {
	   stateID = sid;
	   entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses State's ausgefuehrt
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
	// background
		// creating background-entity
		Entity background = new Entity(GameParameters.MENU_ID);
		background.setPosition(new Vector2f((GameParameters.WINDOW_WIDTH / 2),(GameParameters.WINDOW_HEIGHT / 2)));
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BACKGROUND)));
		}
		background.setScale(GameParameters.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

	// listener entity
		Entity listener = new Entity("listener");
		entityManager.addEntity(stateID, listener);
		// Loop event for various uses (controller input)
		LoopEvent listenerLoop = new LoopEvent();
		listener.addComponent(listenerLoop);

	// new_Game-entity
		String new_Game = "New Game";
		Entity new_Game_Entity = new Entity(new_Game);
		new_Game_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_1_X, GameParameters.MAIN_MENU_BUTTON_1_Y));
		new_Game_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			new_Game_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the new_Game-entity
		entityManager.addEntity(this.stateID, new_Game_Entity);

		// creating trigger event and its actions
		ANDEvent new_Game_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		new_Game_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				PlayerHandler.reset();
			}
		});
		new_Game_Events.addAction(new ChangeStateInitAction(Breakout.GAMEPLAY_STATE));
		new_Game_Entity.addComponent(new_Game_Events);

		// controller "listener" (Button 4)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(3)) {
					// if the button 3 was not pressed before but is pressed now

					// resetting the players progress / stats
					PlayerHandler.reset();
					// going into GameplayState (like changeStateInitAction)
					sb.enterState(GameParameters.GAMEPLAY_STATE);

					// forcing init for all states
					StateBasedEntityManager.getInstance().clearEntitiesFromState(GameParameters.GAMEPLAY_STATE);
					try {
						gc.getInput().clearKeyPressedRecord();
						gc.getInput().clearControlPressedRecord();
						gc.getInput().clearMousePressedRecord();
						sb.init(gc);
					} catch (SlickException var6) {
						var6.printStackTrace();
					}
					if(gc.isPaused()) {
						gc.resume();
					}
				}
			}
		});

	// resume_Game-entity
		String resume_Game = "Resume Game";
		Entity resume_Game_Entity = new Entity(resume_Game);
		resume_Game_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_2_X, GameParameters.MAIN_MENU_BUTTON_1_Y));
		resume_Game_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			resume_Game_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the resume_Game-entity
		entityManager.addEntity(this.stateID, resume_Game_Entity);

		// creating trigger event and its actions
		ANDEvent resume_Game_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		resume_Game_Events.addAction(new Action() {
				@Override
				public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
					if (GameplayState.currentlyRunning) {
						// play sound for acceptable button press
						SoundHandler.playButtonPress();
						// keep track of the time (pause time ended now)
						GameplayState.pauseTime += gc.getTime() - GameplayState.startPauseTime;
						sb.enterState(Breakout.GAMEPLAY_STATE);
						if(gc.isPaused()) {
							gc.resume();
						}
					} else {
						SoundHandler.playNotAcceptable();
					}
				}
			});
		resume_Game_Entity.addComponent(resume_Game_Events);

	// controller "listener" (Button 3)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(2)) {
					// if the button 3 was not pressed before but is pressed now

					if (GameplayState.currentlyRunning) {
						GameplayState.pauseTime += gc.getTime() - GameplayState.startPauseTime;
						sb.enterState(Breakout.GAMEPLAY_STATE);
						if(gc.isPaused()) {
							gc.resume();
						}
					} else {
						SoundHandler.playNotAcceptable();
					}
				}
			}
		});

	// highscore-entity
		String highscore = "Highscore";
		Entity highscore_Entity = new Entity(highscore);
		highscore_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_2_X, GameParameters.MAIN_MENU_BUTTON_2_Y));
		highscore_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			highscore_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the highscore-entity
		entityManager.addEntity(this.stateID, highscore_Entity);

		// creating trigger event and its actions
		ANDEvent highscore_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		highscore_Events.addAction(new ChangeStateAction(Breakout.HIGHSCORE_STATE));
		highscore_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
			}
		});
		highscore_Entity.addComponent(highscore_Events);

	// options-entity
		String options = "Options";
		Entity options_Entity = new Entity(options);
		options_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_1_X, GameParameters.MAIN_MENU_BUTTON_2_Y));
		options_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			options_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the options-entity
		entityManager.addEntity(this.stateID, options_Entity);

		// creating trigger event and its actions
		ANDEvent options_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		options_Events.addAction(new ChangeStateAction(Breakout.OPTIONS_STATE));
		options_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
			}
		});
		options_Entity.addComponent(options_Events);

	// quit-entity
		Entity quit_Entity = new Entity("Quit");
		quit_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_1_X, GameParameters.MAIN_MENU_BUTTON_3_Y));
		quit_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			quit_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the quit-entity
		entityManager.addEntity(this.stateID, quit_Entity);

		// creating trigger event and its actions
		ANDEvent quit_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		quit_Events.addAction(new QuitAction());
		quit_Entity.addComponent(quit_Events);

		// controller "listener" (Button 2)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(1)) {
					// if the button 3 was not pressed before but is pressed now
					// exit the game
					gc.exit();
				}
			}
		});

	// about-entity
		String about = "About";
		Entity about_Entity = new Entity(about);
		about_Entity.setPosition(new Vector2f(GameParameters.MAIN_MENU_BUTTON_2_X, GameParameters.MAIN_MENU_BUTTON_3_Y));
		about_Entity.setScale(GameParameters.MENU_ENTRY_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			about_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BUTTON)));
		}
		// giving StateBasedEntityManager the about-entity
		entityManager.addEntity(this.stateID, about_Entity);

		// creating trigger event and its actions
		ANDEvent about_Events = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		about_Events.addAction(new ChangeStateAction(Breakout.ABOUT_STATE));
		about_Events.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				SoundHandler.playButtonPress();
			}
		});
		about_Entity.addComponent(about_Events);

	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		entityManager.updateEntities(container, game, delta);

		if (container.isMouseGrabbed()) {
			container.setMouseGrabbed(false);
		}

		if (!Breakout.getDebug()) {
			// music
			if (!SoundHandler.isMenuMusicPlaying()) {
				SoundHandler.stopVictoryAndGameover();
				SoundHandler.startMenuMusic();
			}
		}
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
		// Normal Game
		g.drawString(LanguageHandler.BUTTON_NEW_GAME, (GameParameters.MAIN_MENU_BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		if (GameplayState.currentlyRunning) {
			g.drawString(LanguageHandler.BUTTON_RESUME_GAME, (GameParameters.MAIN_MENU_BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		} else {
			g.setColor(Color.red);
			g.drawString(LanguageHandler.BUTTON_INACTIVE_RESUME_GAME, (GameParameters.MAIN_MENU_BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_1_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
			g.setColor(Color.white);
		}
		// Highscore
		g.drawString(LanguageHandler.BUTTON_HIGHSCORE, (GameParameters.MAIN_MENU_BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_2_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		// Options
		g.drawString(LanguageHandler.BUTTON_OPTIONS, (GameParameters.MAIN_MENU_BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_2_Y  / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));
		// Quit
		g.drawString(LanguageHandler.BUTTON_QUIT, (GameParameters.MAIN_MENU_BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_3_Y / GameParameters.BACKGROUND_SCALE+ GameParameters.TEXT_OFFSET_Y));
		// About
		g.drawString(LanguageHandler.BUTTON_ABOUT, (GameParameters.MAIN_MENU_BUTTON_2_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.MAIN_MENU_BUTTON_3_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));

	}

	@Override
	public int getID() {
		return stateID;
	}
	
}
