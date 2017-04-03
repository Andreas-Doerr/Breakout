package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.ButtonEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.LoopEvent;
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
public class HighscoreState extends BasicGameState {

	private int stateID; 							// identifier of this BasicGameState
	private StateBasedEntityManager entityManager; 	// related entityManager

	static boolean newEntry = false;
	static boolean askName = false;
	static boolean newIsVictory = false;
	private static String newName = "";
	static long newTime = 0;
	static int newBlocksDestroyed = 0;
	static int newPoints = 0;
	private static boolean isSaved = false;
	private static int thisHighscore = -1;
	// getName
	int[] keysListenedFor = new int[36];

	HighscoreState( int sid ) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses State's ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		//newName
		// letters
		keysListenedFor[0] = Input.KEY_A;
		keysListenedFor[1] = Input.KEY_B;
		keysListenedFor[2] = Input.KEY_C;
		keysListenedFor[3] = Input.KEY_D;
		keysListenedFor[4] = Input.KEY_E;
		keysListenedFor[5] = Input.KEY_F;
		keysListenedFor[6] = Input.KEY_G;
		keysListenedFor[7] = Input.KEY_H;
		keysListenedFor[8] = Input.KEY_I;
		keysListenedFor[9] = Input.KEY_J;
		keysListenedFor[10] = Input.KEY_K;
		keysListenedFor[11] = Input.KEY_L;
		keysListenedFor[12] = Input.KEY_M;
		keysListenedFor[13] = Input.KEY_N;
		keysListenedFor[14] = Input.KEY_O;
		keysListenedFor[15] = Input.KEY_P;
		keysListenedFor[16] = Input.KEY_Q;
		keysListenedFor[17] = Input.KEY_R;
		keysListenedFor[18] = Input.KEY_S;
		keysListenedFor[19] = Input.KEY_T;
		keysListenedFor[20] = Input.KEY_U;
		keysListenedFor[21] = Input.KEY_V;
		keysListenedFor[22] = Input.KEY_W;
		keysListenedFor[23] = Input.KEY_X;
		keysListenedFor[24] = Input.KEY_Y;
		keysListenedFor[25] = Input.KEY_Z;
		// numbers
		keysListenedFor[26] = Input.KEY_1;
		keysListenedFor[27] = Input.KEY_2;
		keysListenedFor[28] = Input.KEY_3;
		keysListenedFor[29] = Input.KEY_4;
		keysListenedFor[30] = Input.KEY_5;
		keysListenedFor[31] = Input.KEY_6;
		keysListenedFor[32] = Input.KEY_7;
		keysListenedFor[33] = Input.KEY_8;
		keysListenedFor[34] = Input.KEY_9;
		keysListenedFor[35] = Input.KEY_0;

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

	// listener entity
		Entity listener = new Entity("listener");
		entityManager.addEntity(stateID, listener);
		// Loop event for various uses (controller input)
		LoopEvent listenerLoop = new LoopEvent();
		listener.addComponent(listenerLoop);

		// back-entity
		ButtonEntity back = new ButtonEntity("back", stateID, Constants.ButtonType.NORMAL, Variables.BUTTON_1_X, Variables.BUTTON_8_Y);
		// going to MainMenuState
		back.addAction(new ChangeStateAction(Breakout.MAINMENU_STATE));
		// resetting the scores
		back.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				// playing sound
				SoundHandler.playButtonPress();
				isSaved = false;
				thisHighscore = -1;
			}
		});

		// controller "listener" (Button 2)
		listenerLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (ControllerHandler.isButtonPressed(1)) {
					// if the button 3 was not pressed before but is pressed now

					isSaved = false;
					thisHighscore = -1;

					// going to MainMenuState
					sb.enterState(Breakout.MAINMENU_STATE);
					if(gc.isPaused()) {
						gc.resume();
					}
				}
			}
		});
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

		// music
		if (!SoundHandler.isHighscoreMusicPlaying() && !SoundHandler.isVictoryPlaying() && !SoundHandler.isGameoverPlaying()) {
			SoundHandler.startHighscoreMusic();
		}
		if (askName) {

			// adding chars to the String newName if it is not too long already (maximum length = 20)
			if (newName.length() < 30) {
				for (int eachKeysListenedFor : keysListenedFor) {
					if (gc.getInput().isKeyPressed(eachKeysListenedFor)) {
						newName += Input.getKeyName(eachKeysListenedFor);
					}
				}
				if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
					newName += " ";
				}
				if (gc.getInput().isKeyPressed(Input.KEY_MINUS)) {
					newName += "-";
				}
			}
			// remove the last char in the String
			if (gc.getInput().isKeyPressed(Input.KEY_BACK)) {
				if (newName.length() > 0) {
					newName = newName.substring(0, newName.length()-1);
				}
			}

			// stop allowing to add and remove chars and add this score
			if (gc.getInput().isKeyPressed(Input.KEY_ENTER) && newName.length() > 0) {
				askName = false;
				thisHighscore = HighscoreHandler.addHighscore(newName, newBlocksDestroyed, newTime, newPoints);
				isSaved =true;
				newEntry = false;
				newIsVictory = false;
				newTime = 0;
				newBlocksDestroyed = 0;
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

		g.drawString(LanguageHandler.BUTTON_BACK, (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.BUTTON_8_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));
		if (newEntry) {
			if (newIsVictory) {
				g.drawString(LanguageHandler.VICTORY, (Variables.WINDOW_WIDTH * 0.47f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.4f) / Variables.BACKGROUND_SCALE);
			} else {
				g.drawString(LanguageHandler.GAME_OVER, (Variables.WINDOW_WIDTH * 0.47f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.4f) / Variables.BACKGROUND_SCALE);
			}
			g.drawString(LanguageHandler.IT_TOOK_YOU + " " + newTime / 1000f + " " + LanguageHandler.X_SECONDS, (Variables.WINDOW_WIDTH * 0.22f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.415f) / Variables.BACKGROUND_SCALE);
			g.drawString(LanguageHandler.YOU_DESTROYED + " " + newBlocksDestroyed + " " + LanguageHandler.X_BLOCKS, (Variables.WINDOW_WIDTH * 0.22f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.43f) / Variables.BACKGROUND_SCALE);
			g.drawString(LanguageHandler.YOU_SCORED + " " + newPoints + " " + LanguageHandler.X_POINTS, (Variables.WINDOW_WIDTH * 0.22f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.445f) / Variables.BACKGROUND_SCALE);
			if (isSaved) {
				g.setColor(Color.green);
			}
			g.drawString(LanguageHandler.NAMES.substring(0, LanguageHandler.NAMES.length()-1) + ": " + newName, (Variables.WINDOW_WIDTH * 0.45f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.43f) / Variables.BACKGROUND_SCALE);
			g.setColor(Color.white);
		}

		// Current Highscores
		float highscoreList_x_number = (Variables.WINDOW_WIDTH * 0.22f) / Variables.BACKGROUND_SCALE;
		float highscoreList_x_name = (Variables.WINDOW_WIDTH * 0.242f) / Variables.BACKGROUND_SCALE;
		float highscoreList_x_desBlocks = (Variables.WINDOW_WIDTH * 0.5f) / Variables.BACKGROUND_SCALE;
		float highscoreList_x_time = (Variables.WINDOW_WIDTH * 0.6f) / Variables.BACKGROUND_SCALE;
		float highscoreList_x_points = (Variables.WINDOW_WIDTH * 0.7f) / Variables.BACKGROUND_SCALE;
		float highscoreList_y = (Variables.WINDOW_HEIGHT * 0.6f) / Variables.BACKGROUND_SCALE;


		g.drawString(LanguageHandler.NAMES.toUpperCase(),(Variables.WINDOW_WIDTH * 0.248f) / Variables.BACKGROUND_SCALE, highscoreList_y - 35);
		g.drawString(LanguageHandler.DESTROYED_BLOCKS.toUpperCase(), (Variables.WINDOW_WIDTH * 0.46f) / Variables.BACKGROUND_SCALE, highscoreList_y - 35);
		g.drawString(LanguageHandler.TIMER.toUpperCase(), (Variables.WINDOW_WIDTH * 0.6f) / Variables.BACKGROUND_SCALE, highscoreList_y - 35);
		g.drawString(LanguageHandler.POINTS.toUpperCase(), (Variables.WINDOW_WIDTH * 0.687f) / Variables.BACKGROUND_SCALE, highscoreList_y - 35);

		boolean noMore = false;
		for (int i = 0; i < 9; i++) {
			if (HighscoreHandler.getNameAtHighscorePosition(i) != null) {
				g.drawString((i + 1) + ".:  " + HighscoreHandler.getNameAtHighscorePosition(i), highscoreList_x_number, highscoreList_y + 25 * i);
				g.drawString(String.valueOf(HighscoreHandler.getDesBlocksAtHighscorePosition(i)), highscoreList_x_desBlocks, highscoreList_y + 25 * i);
				g.drawString(String.valueOf(HighscoreHandler.getTimeElapsedAtHighscorePosition(i)), highscoreList_x_time, highscoreList_y + 25 * i);
				g.drawString(String.valueOf(HighscoreHandler.getPointsAtHighscorePosition(i)), highscoreList_x_points, highscoreList_y + 25 * i);
			} else {
				g.drawString("No more entries", highscoreList_x_name, highscoreList_y + 25 * i);
				noMore = true;
				break;
			}
		}
		if (thisHighscore >= 9) {
			g.drawString(thisHighscore + 1 + ".: " + HighscoreHandler.getNameAtHighscorePosition(thisHighscore),highscoreList_x_number, highscoreList_y + 25 * 9);
			g.drawString(String.valueOf(HighscoreHandler.getDesBlocksAtHighscorePosition(thisHighscore)), highscoreList_x_desBlocks, highscoreList_y + 25 * 9);
			g.drawString(String.valueOf(HighscoreHandler.getTimeElapsedAtHighscorePosition(thisHighscore)), highscoreList_x_time, highscoreList_y + 25 * 9);
			g.drawString(String.valueOf(HighscoreHandler.getPointsAtHighscorePosition(thisHighscore)), highscoreList_x_points, highscoreList_y + 25 * 9);
		} else {
			if (!noMore) {
				if (HighscoreHandler.getNameAtHighscorePosition(9) != null) {
					g.drawString("10.: " + HighscoreHandler.getNameAtHighscorePosition(9), highscoreList_x_number, highscoreList_y + 25 * 9);
					g.drawString(String.valueOf(HighscoreHandler.getDesBlocksAtHighscorePosition(9)), highscoreList_x_desBlocks, highscoreList_y + 25 * 9);
					g.drawString(String.valueOf(HighscoreHandler.getTimeElapsedAtHighscorePosition(9)), highscoreList_x_time, highscoreList_y + 25 * 9);
					g.drawString(String.valueOf(HighscoreHandler.getPointsAtHighscorePosition(9)), highscoreList_x_points, highscoreList_y + 25 * 9);
				} else {
					g.drawString("No more entries", highscoreList_x_name, highscoreList_y + 25 * 9);
				}
			}
		}
	}

	@Override
	public int getID() {
		return stateID;
	}

}