package de.tudarmstadt.informatik.fop.breakout.factories;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.BallEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.event.basicevents.BallCollisionEvent;
import de.tudarmstadt.informatik.fop.breakout.handlers.EntityHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.SoundHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import eea.engine.interfaces.IEntityFactory;

/**
 * Factory for creating Borders of the field. Borders are not visible and not
 * passable entities for holding the ball in the field.
 * 
 * @author Tobias Otterbein, Benedikt Wartusch
 */
public class BorderFactory implements IEntityFactory, Constants {

	private BorderType type;

	/**
	 * Factory Constructor
	 * 
	 * @param type
	 *            determines the type of a created border (TOP, LEFT, RIGHT or BOTTOM)
	 */
	public BorderFactory(BorderType type) {
		this.type = type;
	}

	@Override
	public Entity createEntity() {

		Entity border;
		Vector2f size;
		Vector2f position;

		// Collision with a ball
		BallCollisionEvent ballCE = new BallCollisionEvent();


		switch (type) {

		case TOP:
			border = new Entity(TOP_BORDER_ID);
			position = new Vector2f(Variables.WINDOW_WIDTH / 2, - (BORDER_WIDTH / 2));
			size = new Vector2f(Variables.WINDOW_WIDTH, BORDER_WIDTH);
			ballCE.addAction((gc, sb, delta, event) -> {
				BallEntity ball = (BallEntity) ballCE.getCollidedEntity();
				if (!ball.getLastCollidedWith().equals(border)) {
					// play the sound
					SoundHandler.playHitBorder();
					// set this balls lastCollidedWith to this collidedEntity-entity
					ball.setLastCollidedWith(border);
					// calculate new speeds for this ball
						ball.setSpeedUp(-ball.getSpeedUp());
				}
			});
			break;
		case LEFT:
			border = new Entity(LEFT_BORDER_ID);
			position = new Vector2f(- (BORDER_WIDTH / 2),Variables. WINDOW_HEIGHT / 2);
			size = new Vector2f(BORDER_WIDTH, Variables.WINDOW_HEIGHT);
			ballCE.addAction((gc, sb, delta, event) -> {
				BallEntity ball = (BallEntity) ballCE.getCollidedEntity();
				if (!ball.getLastCollidedWith().equals(border)) {
					// play the sound
					SoundHandler.playHitBorder();
					// set this balls lastCollidedWith to this collidedEntity-entity
					ball.setLastCollidedWith(border);
					// calculate new speeds for this ball
					ball.setSpeedRight(Math.abs(ball.getSpeedRight()));
				}
			});
			break;
		case RIGHT:
			border = new Entity(RIGHT_BORDER_ID);
			position = new Vector2f(Variables.WINDOW_WIDTH + (BORDER_WIDTH / 2), Variables.WINDOW_HEIGHT / 2);
			size = new Vector2f(BORDER_WIDTH, Variables.WINDOW_HEIGHT);
			ballCE.addAction((gc, sb, delta, event) -> {
				BallEntity ball = (BallEntity) ballCE.getCollidedEntity();
				if (!ball.getLastCollidedWith().equals(border)) {
					// play the sound
					SoundHandler.playHitBorder();
					// set this balls lastCollidedWith to this collidedEntity-entity
					ball.setLastCollidedWith(border);
					// calculate new speeds for this ball
					ball.setSpeedRight( - Math.abs(ball.getSpeedRight()));
				}
			});
			break;
		default:
			return null;
		}

		border.addComponent(ballCE);

		border.setPosition(position);
		border.setSize(size);
		border.setVisible(false);
		border.setPassable(false);

		EntityHandler.addBorder(border);

		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, border);

		return border;
	}

}
