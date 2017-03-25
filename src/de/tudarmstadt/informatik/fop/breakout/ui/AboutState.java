package de.tudarmstadt.informatik.fop.breakout.ui;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.LanguageHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.SoundHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
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
		Entity background = new Entity(GameParameters.MENU_ID);
		background.setPosition(new Vector2f((GameParameters.WINDOW_WIDTH / 2),(GameParameters.WINDOW_HEIGHT / 2)));
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			background.addComponent(new ImageRenderComponent(new Image(ThemeHandler.MENU_BLANK_BACKGROUND)));
		}
		background.setScale(GameParameters.BACKGROUND_SCALE); // scaling
		// giving StateBasedEntityManager the background-entity
		entityManager.addEntity(stateID, background);

		// back-entity
		Entity back_Entity = new Entity("Back");
		back_Entity.setPosition(new Vector2f(GameParameters.BUTTON_1_X, GameParameters.BUTTON_8_Y));
		back_Entity.setScale(GameParameters.Entity_SCALE);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			back_Entity.addComponent(new ImageRenderComponent(new Image(ThemeHandler.BUTTON)));
		}
		// giving StateBasedEntityManager the back-entity
		entityManager.addEntity(this.stateID, back_Entity);

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

	/**#####
	 * Wird mit dem Frame ausgefuehrt
	 */
	// TODO add mail adreas
	@Override
	public void render(GameContainer container, StateBasedGame game,
					   Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);

		// scaling texts
		g.scale(GameParameters.BACKGROUND_SCALE,GameParameters.BACKGROUND_SCALE);

		g.drawString(LanguageHandler.BUTTON_BACK, (GameParameters.BUTTON_1_X / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_X), (GameParameters.BUTTON_8_Y / GameParameters.BACKGROUND_SCALE + GameParameters.TEXT_OFFSET_Y));

		// About the program documentation
		String aboutDoc = "\n" +
				"   Breakout was developed by Atari, inc. in 1976.\n" +
				"   In Breakout the player has to destroy blocks on the screen\n" +
				"   by bouncing a ball off a stick. The ball and stick\n" +
				"   behaviour is changed by dropped items which improve\n" +
				"   or reduce the players chance to win. The game ends when\n" +
				"   all lives are lost or all levels are finished.\n" +
				"   Do you have what it takes to be the best?";
		g.drawString(aboutDoc, (GameParameters.WINDOW_WIDTH * 0.2f) / GameParameters.BACKGROUND_SCALE, (GameParameters.WINDOW_HEIGHT * 0.4f) / GameParameters.BACKGROUND_SCALE);

		String aboutDoc2 = "\n" +
				"   This Breakout project:\n " +
				"   Was accomplished by ...;\n" +
				"    Was finished in ... days;\n" +
				"    Has approximately ... lines of code.";
		g.drawString(aboutDoc2, (GameParameters.WINDOW_WIDTH * 0.55f) / GameParameters.BACKGROUND_SCALE, (GameParameters.WINDOW_HEIGHT * 0.4f) / GameParameters.BACKGROUND_SCALE);

		g.drawString("Problems or suggestions ?\nmail us at: breakout.darujean@gmail.com", (GameParameters.WINDOW_WIDTH * 0.26f) / GameParameters.BACKGROUND_SCALE, (GameParameters.WINDOW_HEIGHT * 0.6f) / GameParameters.BACKGROUND_SCALE);

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
		g.drawString(soundInterpret, (GameParameters.WINDOW_WIDTH * 0.34f) / GameParameters.BACKGROUND_SCALE, (GameParameters.WINDOW_HEIGHT * 0.66f) / GameParameters.BACKGROUND_SCALE);

	}

	@Override
	public int getID() {
		return stateID;
	}

}