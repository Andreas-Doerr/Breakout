package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.PlayerHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author Andreas Dörr
 */
public class BallEntity extends TestBallEntity {

	public BallEntity(String entityID) {
		super(entityID);

		updateImage();

		// adding the ball to the StateBasedEntityManager (this is why it has to overwrite)
		// (TestBallEntity would cause tests to fail if it tried to add itself to the StateBasedEntityManager)
		StateBasedEntityManager.getInstance().addEntity(GameParameters.GAMEPLAY_STATE, this);
	}

	@Override
	public void destroyBall() {
		if (LevelHandler.getActiveBallCount() <= 1) {
			// if this wa the last ball: subtract a life
			PlayerHandler.subtractOneLife();
		}
		// remove the ball from the StateBasedEntityManager (this is why it has to overwrite)
		// (TestBallEntity would cause tests to fail if it tried to remove itself from the StateBasedEntityManager)
		StateBasedEntityManager.getInstance().removeEntity(GameParameters.GAMEPLAY_STATE, this);
		// animate the destruction
		LevelHandler.animateDestruction(getPosition());
		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);
		// remove this ball from the list which is keeping track of every ball
		LevelHandler.removeBall(this);
	}

	@Override
	public void levelComplete() {
		// remove the ball from the StateBasedEntityManager (this is why it has to overwrite)
		// (TestBallEntity would cause tests to fail if it tried to remove itself from the StateBasedEntityManager)
		StateBasedEntityManager.getInstance().removeEntity(GameParameters.GAMEPLAY_STATE, this);
		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);
		// remove this ball from the list which is keeping track of every ball
		LevelHandler.removeBall(this);
	}

	@Override
	public void updateImage() {
		String newImage = ThemeHandler.STANDARDBALL;
		if (getTotalSpeed() == GameParameters.INITIAL_TOTAL_SPEED) {
			newImage = ThemeHandler.WATERBALL;
		} else if (getTotalSpeed() == GameParameters.INITIAL_TOTAL_SPEED * GameParameters.MAX_SPEED_MULTIPLIER) {
			newImage = ThemeHandler.FIREBALL;
		}
		if (!Breakout.getDebug()) {
			try {
				addComponent(new ImageRenderComponent(new Image(newImage)));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
}