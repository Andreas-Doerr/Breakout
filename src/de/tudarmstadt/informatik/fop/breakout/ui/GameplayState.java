package de.tudarmstadt.informatik.fop.breakout.ui;


import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.BotStickEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.PlayerStickEntity;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.BallEntity;
import de.tudarmstadt.informatik.fop.breakout.factories.BorderFactory;
import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.basicactions.*;
import eea.engine.event.basicevents.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

import static de.tudarmstadt.informatik.fop.breakout.parameters.Constants.BorderType.*;

/**
 * @author Timo Bähr, Andreas Dörr
 *
 * this class represents the game window
 */
public class GameplayState extends BasicGameState {

	static long startPauseTime;
	static long pauseTime;
	static boolean currentlyRunning = false;
	private static long startTime;
	private static long playtime;
	private static Color timeColor = Color.white;
	private int stateID; 							// identifier for this BasicGameState
	private StateBasedEntityManager entityManager; 	// related entityManager
	GameplayState( int sid ) {
	   stateID = sid;
	   entityManager = StateBasedEntityManager.getInstance();
	}

	public static void setCurrentlyRunning(boolean newCurrentlyRunning) {
		currentlyRunning = newCurrentlyRunning;
	}

	/**
	  executed before the (first) start of this state
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		// resetting starting parameters
		pauseTime = 0;
		PlayerHandler.reset();
		// keeping the stick from the previous level would be done by:
			// not resetting it here
			// nor creating the PlayerStick here
			// instead create both upon pressing New_Game Button (don't forget controller)

	// Timer init
		startTime = gc.getTime();

	// background
		// creating background-entity
		Entity background = new Entity(Constants.BACKGROUND_ID);	// entity
		background.setPosition(new Vector2f((Variables.WINDOW_WIDTH / 2),(Variables.WINDOW_HEIGHT / 2)));	// starting position
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.GAME_BACKGROUND))); // picture
		}
		background.setScale(Variables.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

	// Level
		// Level as read from the Map (can be started with a different map as read from PlayerHandler)
		if (!Breakout.getDebug()) {
			LevelHandler.initMapLevel();
		}

	// borders
		new BorderFactory(LEFT).createEntity();
		new BorderFactory(RIGHT).createEntity();

		if (OptionsHandler.getGameMode() == 0) {
			new BorderFactory(TOP).createEntity();
		} else if (OptionsHandler.getGameMode() == 1){
			new PlayerStickEntity(Constants.STICK_ID_TOP, Input.KEY_A, Input.KEY_D, new Vector2f((Variables.WINDOW_WIDTH / 2), Variables.STICK_Y_TOP));
		}

	// Stick
		PlayerStickEntity stick = new PlayerStickEntity(Constants.STICK_ID, Input.KEY_LEFT, Input.KEY_RIGHT , new Vector2f((Variables.WINDOW_WIDTH / 2), Variables.STICK_Y));

	// pause
		// creating pause-entity
		Entity pause = new Entity(Constants.PAUSE_ID);	// entity
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			pause.addComponent(new ImageRenderComponent(new Image(ThemeHandler.PAUSE))); // picture
		}
		pause.setPosition(new Vector2f(- pause.getSize().x, 0));	// starting position
		pause.setScale(Variables.BLOCK_SCALE * 4);
		pause.setVisible(false);
		// giving StateBasedEntityManager the pause-entity
		entityManager.addEntity(stateID, pause);

	// listener entity
		Entity listener = new Entity("listener");
		entityManager.addEntity(stateID, listener);
		// Loop event for various uses (controller input)
		LoopEvent listenerLoop = new LoopEvent();
		listener.addComponent(listenerLoop);

	// p event
		KeyPressedEvent p_pressed = new KeyPressedEvent(Input.KEY_P);
		// pause upon pressing p
		p_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				if(gc.isPaused()) {
					pauseTime += gc.getTime() - startPauseTime;
					timeColor = Color.white;
					pause.setPosition(new Vector2f(- pause.getSize().x, 0));
					pause.setVisible(false);
					gc.resume();
				} else {
					gc.pause();
					startPauseTime = gc.getTime();
					timeColor = Color.red;
					pause.setPosition(new Vector2f(Variables.WINDOW_WIDTH / 2, Variables.WINDOW_HEIGHT / 2));
					pause.setVisible(true);
				}
			}
		});
		// giving the listener-entity the p_pressed-event
		listener.addComponent(p_pressed);

	// controller "listener" (Button 3)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(2)) {
					// if the button 3 was not pressed before but is pressed now

					if(gc.isPaused()) {
						pauseTime += gc.getTime() - startPauseTime;
						timeColor = Color.white;
						pause.setPosition(new Vector2f(- pause.getSize().x, 0));
						pause.setVisible(false);
						gc.setMouseGrabbed(true);
						gc.resume();
					} else {
						gc.pause();
						startPauseTime = gc.getTime();
						timeColor = Color.red;
						pause.setPosition(new Vector2f(Variables.WINDOW_WIDTH / 2, Variables.WINDOW_HEIGHT / 2));
						pause.setVisible(true);
						gc.setMouseGrabbed(false);
					}
				}
			}
		});

	// esc event
		KeyPressedEvent esc_pressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		// upon pressing esc note the pause time if necessary and change to Main Menu
		esc_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				SoundHandler.playButtonPress();
				if (!gc.isPaused()) {
					// if the game is paused the action bound to the p_Listener already saved a startPauseTime
					// so: if it was not paused before save the current time as startPauseTime
					startPauseTime = gc.getTime();
				}
				// free the mouse (was grabbed in GameplayState)
				gc.setMouseGrabbed(false);

				// set everything back to prepare for resuming the Game
				timeColor = Color.white;
				pause.setPosition(new Vector2f(- pause.getSize().x, 0));
				pause.setVisible(false);
			}
		});
		esc_pressed.addAction(new ChangeStateAction(Breakout.MAINMENU_STATE));
		// giving the listener-entity the esc_pressed-event
		listener.addComponent(esc_pressed);

	//  controller "listener" (Button 2)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(1)) {
					// if the button 3 was not pressed before but is pressed now

					if (!gc.isPaused()) {
						// if the game is paused the action bound to the p_Listener already saved a startPauseTime
						// so: if it was not paused before save the current time as startPauseTime
						startPauseTime = gc.getTime();
					}
					// free the mouse (was grabbed in GameplayState)
					gc.setMouseGrabbed(false);
					// set everything back to prepare for resuming the Game
					timeColor = Color.white;
					pause.setPosition(new Vector2f(- pause.getSize().x, 0));
					pause.setVisible(false);

					// go to the MainMenuState
					sb.enterState(Breakout.MAINMENU_STATE);
					if(gc.isPaused()) {
						gc.resume();
					}
				}
			}
		});

	// space_pressed event
		KeyPressedEvent space_pressed = new KeyPressedEvent(Input.KEY_SPACE);
	// ball: creation, collision, destruction, animation of destruction
		space_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// only a new ball if no other ball is currently existing
				if (OptionsHandler.getControlMode() == 0 && LevelHandler.getActiveBallCount() <= 0 && LevelHandler.getActiveDestroyedBallCount() <= 0 && PlayerHandler.getLives() > 0 && !gc.isPaused() && ItemHandler.getItemsActive() == 0) {
					// activate the ability to resume the Game
					if (LevelHandler.ballListHasSpace()) {
						SoundHandler.playButtonPress();
						currentlyRunning = true;

						// creating a ball
						BallEntity ball = new BallEntity(new Vector2f(stick.getPosition().x, (stick.getPosition().y)));
						// ball at stick position
						ball.setPosition(new Vector2f(stick.getPosition().x, (stick.getPosition().y - ((stick.getSize().y / 2) + (ball.getSize().y / 2)))));
					} else {
						SoundHandler.playNotAcceptable();
						System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
					}
				} else {
					SoundHandler.playNotAcceptable();
				}
			}
		});
		// giving the listener-entity the space_pressed-event
		listener.addComponent(space_pressed);

		MouseClickedEvent mouse_klicked = new MouseClickedEvent();
		mouse_klicked.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				// only a new ball if no other ball is currently existing
				if (OptionsHandler.getControlMode() == 1 && LevelHandler.getActiveBallCount() <= 0 && LevelHandler.getActiveDestroyedBallCount() <= 0 && PlayerHandler.getLives() > 0 && !gc.isPaused() && ItemHandler.getItemsActive() == 0) {
					// activate the ability to resume the Game
					if (LevelHandler.ballListHasSpace()) {
						SoundHandler.playButtonPress();
						currentlyRunning = true;

						// creating a ball
						BallEntity ball = new BallEntity(new Vector2f(stick.getPosition().x, (stick.getPosition().y)));
						// ball at stick position
						ball.setPosition(new Vector2f(stick.getPosition().x, (stick.getPosition().y - ((stick.getSize().y / 2) + (ball.getSize().y / 2)))));
					} else {
						SoundHandler.playNotAcceptable();
						System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
					}
				} else {
					SoundHandler.playNotAcceptable();
				}
			}
		});
		// giving the listener-entity the mouse_klicked-event
		listener.addComponent(mouse_klicked);

	//  controller "listener" (Button 4)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.getControlMode() == 2 && ControllerHandler.isButtonPressed(3) && !gc.isPaused()) {
					// if the button 3 was not pressed before but is pressed now

					// only a new ball if no other ball is currently existing
					if (LevelHandler.getActiveBallCount() <= 0 && LevelHandler.getActiveDestroyedBallCount() <= 0 && PlayerHandler.getLives() > 0 && ItemHandler.getItemsActive() == 0) {
						// activate the ability to resume the Game
						if (LevelHandler.ballListHasSpace()) {
							currentlyRunning = true;

							// creating a ball
							BallEntity ball = new BallEntity(new Vector2f(stick.getPosition().x, (stick.getPosition().y)));
							// ball at stick position
							ball.setPosition(new Vector2f(stick.getPosition().x, (stick.getPosition().y - ((stick.getSize().y / 2) + (ball.getSize().y / 2)))));
						} else {
							SoundHandler.playNotAcceptable();
							System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
						}
					} else {
						SoundHandler.playNotAcceptable();
					}
				}
			}
		});

// Bot
	// b_pressed event
		KeyPressedEvent b_pressed = new KeyPressedEvent(Input.KEY_B);
		// BotStick creation and its ball
		b_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (!gc.isPaused() && OptionsHandler.isCheatModeActive()) {
					if (LevelHandler.stickListHasSpace()) {
						if (LevelHandler.ballListHasSpace()) {
							SoundHandler.playButtonPress();
							// activate the ability to resume the Game
							currentlyRunning = true;

							// creating a ball
							BallEntity ball = new BallEntity(new Vector2f(stick.getPosition().x, (stick.getPosition().y)));
							// ball at stick position
							ball.setPosition(new Vector2f(stick.getPosition().x, (stick.getPosition().y - ((stick.getSize().y / 2) + (ball.getSize().y / 2)))));

							// adding the StickBot and giving it the ball entity to follow
							new BotStickEntity(Constants.STICK_ID, stick.getPosition().x, ball);

							// setting the Player's stick a bit lower than the BotSticks
							stick.setPosition(new Vector2f(stick.getPosition().x, Variables.STICK_Y + 5));
						} else {
							SoundHandler.playNotAcceptable();
							System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
						}
					} else {
						SoundHandler.playNotAcceptable();
						System.err.println("The maximum supported amount of sticks active at one time has been surpassed!");
					}
				}
			}
		});
		// giving the listener-entity the b_pressed-event
		listener.addComponent(b_pressed);

	// // controller "listener" (Button 1)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive() && ControllerHandler.isButtonPressed(0) && !gc.isPaused()) {
					if (LevelHandler.stickListHasSpace()) {
						if (LevelHandler.ballListHasSpace()) {
							// if the button 2 was not pressed before but is pressed now
							// activate the ability to resume the Game
							currentlyRunning = true;

							// creating a ball
							BallEntity ball = new BallEntity(new Vector2f(stick.getPosition().x, (stick.getPosition().y)));

							// ball at stick position
							ball.setPosition(new Vector2f(stick.getPosition().x, (stick.getPosition().y - ((stick.getSize().y / 2) + (ball.getSize().y / 2)))));

							// adding the StickBot and giving it the ball entity to follow
							new BotStickEntity(Constants.STICK_ID, stick.getPosition().x, ball);

							// setting the Player's stick a bit lower than the BotSticks
							stick.setPosition(new Vector2f(stick.getPosition().x, Variables.STICK_Y + 5));
						} else {
							SoundHandler.playNotAcceptable();
							System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
						}
					} else {
						SoundHandler.playNotAcceptable();
						System.err.println("The maximum supported amount of sticks active at one time has been surpassed!");
					}
				}
			}
		});

	// n_pressed event
		KeyPressedEvent n_pressed = new KeyPressedEvent(Input.KEY_N);
		// BotStick removal (all of them)
		n_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive() && !gc.isPaused()) {
					LevelHandler.destroyAllSticksExceptFirst();
				}
			}
		});
		// giving the listener-entity the n_pressed-event
		listener.addComponent(n_pressed);

// Cheats
	// f_pressed event
		KeyPressedEvent f_pressed = new KeyPressedEvent(Input.KEY_F);
		// set the speed of all balls to max-speed
		f_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive()) {
					LevelHandler.max_speedAllBalls();
				}
			}
		});
		// giving the listener-entity the f_pressed-event
		listener.addComponent(f_pressed);

	// s_pressed event
		KeyPressedEvent s_pressed = new KeyPressedEvent(Input.KEY_S);
		// set the speed of all balls to min-speed
		s_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive()) {
					LevelHandler.min_speedAllBalls();
				}
			}
		});
		// giving the listener-entity the s_pressed-event
		listener.addComponent(s_pressed);

	// m_pressed event
		KeyPressedEvent m_pressed = new KeyPressedEvent(Input.KEY_M);
		// duplicate all balls
		m_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive()) {
					LevelHandler.duplicateAllBalls();
				}
			}
		});
		// giving the listener-entity the m_pressed-event
		listener.addComponent(m_pressed);

	// k_pressed event
		KeyPressedEvent k_pressed = new KeyPressedEvent(Input.KEY_K);
		// kill a ball
		k_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.isCheatModeActive()) {
					ItemHandler.destroyAllItems();
					LevelHandler.destroyAllBlocks();
				}
			}
		});
		// giving the listener-entity the k_pressed-event
		listener.addComponent(k_pressed);

	// l_pressed event
		KeyPressedEvent l_pressed = new KeyPressedEvent(Input.KEY_L);
		// add 100 Lives
		l_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				PlayerHandler.addLives(100);
			}
		});
		// giving the listener-entity the s_pressed-event
		listener.addComponent(l_pressed);

	}

	/**
	 * executed before every frame
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		// StatedBasedEntityManager updates all entities
		entityManager.updateEntities(gc, sb, delta);

		if (Breakout.getApp().isResizable()) {
			Breakout.getApp().setResizable(false);
		}

		// Timer time calculation
		if (!gc.isPaused()) {
			playtime = (gc.getTime() - startTime - pauseTime) ;
		}

		if (!Breakout.getDebug()) {
			// music
			if (!SoundHandler.isGameplayMusicPlaying()) {
				SoundHandler.startGameplayMusic();
			}


			// no lives left
			if (LevelHandler.getActiveBallCount() <= 0 && LevelHandler.getActiveDestroyedBallCount() <= 0 && PlayerHandler.getLives() <= 0 && ItemHandler.getItemsActive() == 0) {
				// deactivate the ability to resume the Game
				currentlyRunning = false;
				// if this was the last destroyedBall in play and no other ball is in play and you have no lives left
				HighscoreState.newTime = playtime;
				HighscoreState.newBlocksDestroyed = LevelHandler.getDestroyedBlocks();

				// go to the HighscoreState with information about this run
				sb.enterState(Breakout.HIGHSCORE_STATE);
				// free the mouse (was grabbed in GameplayState)
				gc.setMouseGrabbed(false);
				HighscoreState.newEntry = true;
				HighscoreState.askName = true;
				HighscoreState.newIsVictory = false;
				HighscoreState.newPoints = PlayerHandler.getPoints();
				SoundHandler.playGameover();
				// resume the game if it is currently paused
				if (gc.isPaused()) {
					gc.resume();
				}
			}

			// level complete
			if (LevelHandler.getActiveBlocks() == 0 && ItemHandler.getItemsActive() == 0) {
				System.out.print("INFO: Level complete (all blocks destroyed). ");
				// adding the time and amount of blocks destroyed of this level to the stats for the Highscore
				HighscoreState.newTime += playtime;
				HighscoreState.newBlocksDestroyed += LevelHandler.getDestroyedBlocks();

				LevelHandler.allBallsLevelComplete();

				LevelHandler.switchMap();

				if (OptionsHandler.getSelectedMap() != 0) {
					System.out.println(OptionsHandler.getSelectedMap() + " < " + (Constants.MAX_MAPS));
					System.out.println("Loading next level... (" + OptionsHandler.getSelectedMapName() + ")");
					LevelHandler.initMapLevel();
				} else {
					OptionsHandler.setSelectedMap(0);

					HighscoreState.newTime = playtime;
					HighscoreState.newBlocksDestroyed = LevelHandler.getDestroyedBlocks();

					System.out.println("Loading Highscore...");
					// deactivate the ability to resume the Game
					currentlyRunning = false;
					// go to the HighscoreState with information about this run
					sb.enterState(Breakout.HIGHSCORE_STATE);
					// free the mouse (was grabbed in GameplayState)
					gc.setMouseGrabbed(false);
					HighscoreState.newEntry = true;
					HighscoreState.askName = true;
					HighscoreState.newIsVictory = true;
					HighscoreState.newPoints = PlayerHandler.getPoints();
					SoundHandler.playVictory();
					// resume the game if it is currently paused
					if (gc.isPaused()) {
						gc.resume();
					}
				}
			}
		}

	}

	/**
	 * executed with every frame
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		// StatedBasedEntityManager renders all entities
		entityManager.renderEntities(gc, sb, g);

		// scaling texts
		g.scale(Variables.BACKGROUND_SCALE, Variables.BACKGROUND_SCALE);

		// TODO this following new
		int lives_pos_y;
		int active_blocks_pos_y;
		int destroyed_blocks_pos_y;
		int active_balls_pos_y;
		int destroyed_balls_pos_y;
		int points_pos_y;
		int showFPS_y = 0;
		if (gc.isShowingFPS()) {
			showFPS_y = (int) (30 / Variables.BACKGROUND_SCALE);
		}
		lives_pos_y = Constants.LIVES_POS_Y + showFPS_y;
		active_blocks_pos_y = Constants.ACTIVE_BLOCKS_POS_Y + showFPS_y;
		destroyed_blocks_pos_y = Constants.DESTROYED_BLOCKS_POS_Y + showFPS_y;
		active_balls_pos_y = Constants.ACTIVE_BALLS_POS_Y + showFPS_y;
		destroyed_balls_pos_y = Constants.ACTIVE_DESTROYED_BALLS_POS_Y + showFPS_y;
		points_pos_y = Constants.POINTS_POS_Y + showFPS_y;


		// show lives left
		g.drawString(LanguageHandler.LIVES_LEFT + ": " + (Integer.toString(PlayerHandler.getLives())), (Variables.LIVES_POS_X), lives_pos_y);
		// show blocks left
		g.drawString(LanguageHandler.ACTIVE_BLOCKS + ": " + (Integer.toString(LevelHandler.getActiveBlocks())), (Variables.ACTIVE_BLOCKS_POS_X), active_blocks_pos_y);
		// show blocks destroyed
		g.drawString(LanguageHandler.DESTROYED_BLOCKS + ": " + (Integer.toString(LevelHandler.getDestroyedBlocks())), (Variables.DESTROYED_BLOCKS_POS_X), destroyed_blocks_pos_y);
		// show amount of active balls (DEBUG)
		g.drawString(LanguageHandler.BALLS_ACTIVE + ": " + (Integer.toString(LevelHandler.getActiveBallCount())), (Variables.ACTIVE_BALLS_POS_X), active_balls_pos_y);
		// show amount of active destroyed balls (DEBUG)
		g.drawString(LanguageHandler.DESTROYED_BALLS_ACTIVE + ": " + (Integer.toString(LevelHandler.getActiveDestroyedBallCount())), (Variables.ACTIVE_DESTROYED_BALLS_POS_X), destroyed_balls_pos_y);
		// show points
		g.drawString(LanguageHandler.POINTS + ": " + Integer.toString(PlayerHandler.getPoints()), (Variables.POINTS_POS_X), points_pos_y);
		// show elapsed Time
		g.setColor(timeColor);
		g.drawString(LanguageHandler.TIMER + ": " + Float.toString(playtime / 10 /100f), (Variables.TIMER_X), (Constants.TIMER_Y));
		g.setColor(Color.white);

	}

	@Override
	public int getID() {
		return stateID;
	}
}