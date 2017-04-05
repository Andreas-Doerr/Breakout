package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.ButtonEntity;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by PC - Andreas on 06.03.2017.
 *
 * @author Andreas Dörr
 */
public class AboutState extends BasicGameState {

	private int stateID; 							// identifier of this BasicGameState
	private StateBasedEntityManager entityManager; 	// related entityManager

	AboutState( int sid ) {
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

		// back-entity
		ButtonEntity back = new ButtonEntity("back", stateID, Constants.ButtonType.NORMAL, Variables.BUTTON_1_X, Variables.BUTTON_8_Y);
		back.addAction(new ChangeStateAction(Breakout.MAINMENU_STATE));
		back.addAction((gc, sb, delta, event) -> SoundHandler.playButtonPress());


	// listener entity
		Entity listener = new Entity("listener");
		entityManager.addEntity(stateID, listener);
		// Loop event for various uses (controller input)
		LoopEvent listenerLoop = new LoopEvent();
		listener.addComponent(listenerLoop);

		// controller "listener" (Button 2)
		listenerLoop.addAction((gc, sb, delta, event) -> {
			if (ControllerHandler.isButtonPressed(1)) {
				// if the button 3 was not pressed before but is pressed now

				// going to MainMenuState
				sb.enterState(Breakout.MAINMENU_STATE);
				if(gc.isPaused()) {
					gc.resume();
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

		OptionsHandler.updateWindow(gc, sb, stateID);
	}

	/**#####
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game,
					   Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);

		// scaling texts
		g.scale(Variables.BACKGROUND_SCALE, Variables.BACKGROUND_SCALE);

		g.drawString(LanguageHandler.BUTTON_BACK, (Variables.BUTTON_1_X_WIDE / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_X), (Variables.BUTTON_8_Y / Variables.BACKGROUND_SCALE + Constants.TEXT_OFFSET_Y));

		// About the program documentation
		String aboutDoc = "HISTORY:\n" +
				"  Breakout was developed by Atari, inc. in 1976.\n" +
				"  In Breakout the player has to destroy blocks on the screen\n" +
				"  by bouncing a ball off a stick. The ball and stick\n" +
				"  behaviour is changed by dropped items which improve\n" +
				"  or reduce the players chance to win. The game ends when\n" +
				"  all lives are lost or all levels are finished.\n" +
				"  Do you have what it takes to be the best?";
		g.drawString(aboutDoc, (Variables.WINDOW_WIDTH * 0.205f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.4f) / Variables.BACKGROUND_SCALE);

		String aboutDoc2 = "\n" +
				"This Breakout project:\n" +
				"  Was finished in ~30 days;\n" +
				"  Has approximately 5352 lines of code.\n" +
				"  Code by Andreas Dörr \n" +
				"  Textures by Daniel Friesen, Rúben Costa\n" +
				"  Maps by Jens Abels, Rúben Costa\n" +
				"  Marketing by Jens Abels\n" +
				"  Tested by David Volz\n\n" +
				"Current development:\n" +
				"  Time: since 22.02.2017\n" +
				"  Lines: 5502";
		g.drawString(aboutDoc2, (Variables.WINDOW_WIDTH * 0.56f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.4f) / Variables.BACKGROUND_SCALE);

		g.drawString("Problems or suggestions ?\nmail us at: breakout.darujean@gmail.com", (Variables.WINDOW_WIDTH * 0.26f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.6f) / Variables.BACKGROUND_SCALE);

		String soundInterpret = "Sounds:\n" +
				"All rights reserved by the authors of the sounds\n" +
				"All sounds commissioned by OpenGameart.org\n" +
				"Highscore–Boss Theme -by remaxim\n" +
				"Tiny Button Push -by  SoundBible.com-513260752_Mike Koenig\n" +
				"GameOver–No Hope -by Cleyton R. Xavier/Kauffman – httpopengameart_orgusersdoppelganger\n" +
				"Bottle Brak–spookymodem –by Joseph Krebs\n" +
				"Sky Game Menu -by Eric Matyas Soundimage.org\n" +
				"Gameplay - Start_Music - from a NaaLaa breakout example game\n" +
				"Lively Meadow Victory Fanfare -by Matthew Pablo\n" +
				"\n" +
				"All images created  and all rights reserved by DaRuJeAn";
		g.drawString(soundInterpret, (Variables.WINDOW_WIDTH * 0.34f) / Variables.BACKGROUND_SCALE, (Variables.WINDOW_HEIGHT * 0.66f) / Variables.BACKGROUND_SCALE);

	}

	@Override
	public int getID() {
		return stateID;
	}

}