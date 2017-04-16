package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.ButtonEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

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
		Entity background = new Entity(Constants.MENU_ID);
		background.setPosition(new Vector2f((OptionsHandler.getWindow_x() / 2),(OptionsHandler.getWindow_y() / 2)));
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BACKGROUND)));
		}
		background.setScale(Variables.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

	// listener entity
		Entity listener = new Entity("listener");
		entityManager.addEntity(stateID, listener);
		// Loop event for various uses (controller input)
		LoopEvent listenerLoop = new LoopEvent();
		listener.addComponent(listenerLoop);

	// new_Game-entity

		ButtonEntity newGame = new ButtonEntity("newGame", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_1_X, Variables.MAIN_MENU_BUTTON_1_Y);
		newGame.addAction((gc1, sb1, delta, event) -> {
			SoundHandler.playButtonPress();
			PlayerHandler.reset();
			// grab the mouse
			gc1.setMouseGrabbed(true);
		});
		newGame.addAction(new ChangeStateInitAction(Breakout.GAMEPLAY_STATE));

		// controller "listener" (Button 4)
		listenerLoop.addAction((gc1, sb1, delta, event) -> {
			if (ControllerHandler.isButtonPressed(3)) {
				// if the button 3 was not pressed before but is pressed now

				// resetting the players progress / stats
				PlayerHandler.reset();

				// grab the mouse
				gc1.setMouseGrabbed(true);

				// going into GameplayState (like changeStateInitAction)
				sb1.enterState(Constants.GAMEPLAY_STATE);

				// forcing init for all states
				Breakout.reinitStates(gc1, sb1, Constants.GAMEPLAY_STATE);
			}
		});

	// resume_Game-entity
		ButtonEntity resumeGame = new ButtonEntity("resumeGame", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_2_X, Variables.MAIN_MENU_BUTTON_1_Y);
		resumeGame.addAction((gc1, sb1, delta, event) -> {
			if (GameplayState.currentlyRunning) {
				// play sound for acceptable button press
				SoundHandler.playButtonPress();
				// grab the mouse
				gc1.setMouseGrabbed(true);
				// keep track of the time (pause time ended now)
				GameplayState.pauseTime += gc1.getTime() - GameplayState.startPauseTime;
				sb1.enterState(Breakout.GAMEPLAY_STATE);
				if(gc1.isPaused()) {
					gc1.resume();
				}
			} else {
				SoundHandler.playNotAcceptable();
			}
		});

	// controller "listener" (Button 3)
		listenerLoop.addAction((gc1, sb1, delta, event) -> {
			if (ControllerHandler.isButtonPressed(2)) {
				// if the button 3 was not pressed before but is pressed now

				if (GameplayState.currentlyRunning) {
					GameplayState.pauseTime += gc1.getTime() - GameplayState.startPauseTime;
					// grab the mouse
					gc1.setMouseGrabbed(true);
					sb1.enterState(Breakout.GAMEPLAY_STATE);
					if(gc1.isPaused()) {
						gc1.resume();
					}
				} else {
					SoundHandler.playNotAcceptable();
				}
			}
		});

	// options-entity
		ButtonEntity options = new ButtonEntity("options", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_1_X, Variables.MAIN_MENU_BUTTON_2_Y);
		options.addAction(new ChangeStateAction(Breakout.OPTIONS_STATE));
		options.addAction((gc1, sb1, delta, event) -> SoundHandler.playButtonPress());

	// highscore-entity
		ButtonEntity highscore = new ButtonEntity("highscore", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_2_X, Variables.MAIN_MENU_BUTTON_2_Y);
		highscore.addAction(new ChangeStateAction(Breakout.HIGHSCORE_STATE));
		highscore.addAction((gc1, sb1, delta, event) -> SoundHandler.playButtonPress());


	// quit-entity
		ButtonEntity quit = new ButtonEntity("quit", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_1_X, Variables.MAIN_MENU_BUTTON_3_Y);
		quit.addAction(new QuitAction());

		// controller "listener" (Button 2)
		listenerLoop.addAction((gc1, sb1, delta, event) -> {
			if (ControllerHandler.isButtonPressed(1)) {
				// if the button 3 was not pressed before but is pressed now
				// exit the game
				gc1.exit();
			}
		});

	// about-entity
		ButtonEntity about = new ButtonEntity("about", stateID, Constants.ButtonType.MAINMENU, Variables.MAIN_MENU_BUTTON_2_X, Variables.MAIN_MENU_BUTTON_3_Y);
		about.addAction(new ChangeStateAction(Breakout.ABOUT_STATE));
		about.addAction((gc1, sb1, delta, event) -> SoundHandler.playButtonPress());

	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		entityManager.updateEntities(gc, sb, delta);

		if (!Breakout.getApp().isResizable()) {
			Breakout.getApp().setResizable(true);
		}

		OptionsHandler.updateWindow(gc, sb, stateID);


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
		g.scale(Variables.BACKGROUND_SCALE, Variables.BACKGROUND_SCALE);

		// Buttons
		// Normal Game
		g.drawString(LanguageHandler.BUTTON_NEW_GAME, (Variables.MAIN_MENU_BUTTON_1_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		if (GameplayState.currentlyRunning) {
			g.drawString(LanguageHandler.BUTTON_RESUME_GAME, (Variables.MAIN_MENU_BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		} else {
			g.setColor(Color.red);
			g.drawString(LanguageHandler.BUTTON_INACTIVE_RESUME_GAME, (Variables.MAIN_MENU_BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_1_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
			g.setColor(Color.white);
		}
		// Highscore
		g.drawString(LanguageHandler.BUTTON_HIGHSCORE, (Variables.MAIN_MENU_BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_2_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		// Options
		g.drawString(LanguageHandler.BUTTON_OPTIONS, (Variables.MAIN_MENU_BUTTON_1_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_2_Y  / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		// Quit
		g.drawString(LanguageHandler.BUTTON_QUIT, (Variables.MAIN_MENU_BUTTON_1_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_3_Y / Variables.BACKGROUND_SCALE+ Constants.TEXT_OFFSET_Y));
		// About
		g.drawString(LanguageHandler.BUTTON_ABOUT, (Variables.MAIN_MENU_BUTTON_2_X / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.MAIN_MENU_BUTTON_3_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));

	}

	@Override
	public int getID() {
		return stateID;
	}
	
}
