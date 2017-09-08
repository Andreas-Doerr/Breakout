package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.engine.event.basicevents.BlockCollisionEvent;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.action.basicactions.MoveUpAction;
import eea.engine.action.basicactions.RotateLeftAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * @author Andreas Dörr
 */
public class BallEntity extends Entity {
	private float SpeedUp;
	private float SpeedRight;
	private Entity lastCollidedWith;

	public BallEntity(float pos_x, float pos_y) {
		super(Constants.BALL_ID);
		this.SpeedUp = Variables.INITIAL_BALL_SPEED_UP;
		this.SpeedRight = Variables.INITIAL_BALL_SPEED_RIGHT;
		this.lastCollidedWith = this;

		// add 1 to the counter of active balls
		LevelHandler.addActiveBalls(1);
		// add this ball to the list which is keeping track of every ball
		EntityHandler.addBall(this);

		// setting all balls to be passable
		setPassable(true);

		// ball scaling
		setScale(Variables.BLOCK_SCALE * 4 * 0.7f);

		// image
		updateImage();

		// set position
		setPosition(new Vector2f(pos_x, pos_y + getSize().y / 2));

		// MOVEMENT
		LoopEvent ballLoop = new LoopEvent();
		ballLoop.addAction((gc, sb, delta, event) -> {
			if (!gc.isPaused()) {
				setPosition(new Vector2f(getPosition().x + getSpeedRight() * Constants.FRAME_RATE / (float) gc.getFPS(), getPosition().y - getSpeedUp() * Constants.FRAME_RATE / (float) gc.getFPS()));
			}
		});
		addComponent(ballLoop);

		// COLLISION with a Block
		BlockCollisionEvent blockCE = new BlockCollisionEvent();
		addComponent(blockCE);
		blockCE.addAction((gc, sb, delta, event) -> {
			BlockEntity block = (BlockEntity) blockCE.getCollidedEntity();
			if (!getLastCollidedWith().equals(block)) {

				// BLOCK
				// if the ball collided with a block (except if the last collision was with exactly this entity)
				boolean didCollide = true;

				// calculating position of the ball relative to the block it collided with
				Vector2f ballpos = getPosition();
				Vector2f blockpos = block.getPosition();
				Vector2f ballpos_rel = new Vector2f(ballpos.x - blockpos.x, blockpos.y - ballpos.y);

				// calculate new speeds for this ball
				if ((ballpos_rel.y < (-Math.abs(ballpos_rel.x) + 10)) && getSpeedUp() > 0) {
					// if hit from below
					setSpeedUp(-getSpeedUp());
				} else if ((ballpos_rel.y > (Math.abs(ballpos_rel.x) - 10)) && getSpeedUp() < 0) {
					// if hit from above
					setSpeedUp(-getSpeedUp());
				} else if ((ballpos_rel.x < 0) && getSpeedRight() > 0) {
					// if hit from the left
					setSpeedRight(-getSpeedRight());
				} else if ((ballpos_rel.x > 0) && getSpeedRight() < 0) {
					// if hit from the right
					setSpeedRight(-getSpeedRight());
				} else {
					// did not hit
					didCollide = false;
				}

				if (didCollide) {
					// play sound
					SoundHandler.playHitBlock();
					// set this balls lastCollidedWith to this block entity
					lastCollidedWith = block;
					// destroying / replacing the block
					block.hit();
				}
			}
		});

		// always:
		//	if the ball is too far down: destroy the ball and everything associated with it
		ballLoop.addAction((gc, sb, delta, event) -> {
			// if the ball is too far down destroy it and everything associated with it
			if (getPosition().y > Variables.WINDOW_HEIGHT * 0.92f) {
				// if the ball is too far down
				destroyBall();
			} else if (OptionsHandler.getGameMode() == 1 && getPosition().y < Variables.WINDOW_HEIGHT * 0.08f) {
				destroyBall();
			}
		});

		LeavingScreenEvent lse = new LeavingScreenEvent();
		addComponent(lse);

		// catching the ball if it sneaks through a border
		// damn you ball >:(
		lse.addAction((gc, sb, delta, event) -> {
			if (getPosition().y < 0 && OptionsHandler.getGameMode() != 1) {
				// went past the top border
				System.err.println("The ball sneaked through the top border (trying to bring it back)");
				setSpeedUp(-Math.abs(getSpeedUp()));
			}
			if (getPosition().x < 0) {
				// went past the left border
				System.err.println("The ball sneaked through the left border (trying to bring it back)");
				setSpeedRight(Math.abs(getSpeedRight()));
			}
			if (getPosition().x > Variables.WINDOW_WIDTH) {
				// went past the right border
				System.err.println("The ball sneaked through the right border (trying to bring it back)");
				setSpeedRight(-Math.abs(getSpeedRight()));
			}
		});


		// adding the ball to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, this);
	}


	// getter
	public float getSpeedUp() {
		return this.SpeedUp;
	}

	public float getSpeedRight() {
		return this.SpeedRight;
	}

	public float getTotalSpeed() {
		return (float) Math.sqrt(getSpeedUp() * getSpeedUp() + getSpeedRight() * getSpeedRight());
	}

	float getMovementAngleRAD() {
		return (float) Math.atan(getSpeedUp() / getSpeedRight());
	}

	float getMovementAngleDEG() {
		return (float) (getMovementAngleRAD() / Math.PI * 180);
	}

	public Entity getLastCollidedWith() {
		return lastCollidedWith;
	}

	// setter
	public void setSpeedUp(float newSpeedUp) {
		this.SpeedUp = newSpeedUp;
	}

	public void setSpeedRight(float newSpeedRight) {
		this.SpeedRight = newSpeedRight;
	}

	public void setTotalSpeed(float newTotalSpeed) {
		if (newTotalSpeed != 0f) {
			// get current speed (to get the direction)
			float current_speed_up = getSpeedUp();
			float current_speed_right = getSpeedRight();

			// get the direction the ball is moving in
			float angle = (float) Math.atan(getSpeedUp() / getSpeedRight());
			// calculating new speeds based on the new angle and the new total speed
			float new_speed_up = (float) Math.abs(Math.sin(angle) * newTotalSpeed);
			float new_speed_right = (float) Math.abs(Math.cos(angle) * newTotalSpeed);
			// eliminate problems caused by one speed of the ball being 0 by never allowing it
			if (current_speed_up > 0) {
				// if the ball is moving up
				setSpeedUp(new_speed_up);
			} else {
				// if the ball is moving down (or not at all)
				setSpeedUp(-new_speed_up);
			}
			if (current_speed_right > 0) {
				// if the ball is moving right
				setSpeedRight(new_speed_right);
			} else {
				// if the ball is moving left (or not at all)
				setSpeedRight(-new_speed_right);
			}
			updateImage();
		} else {
			System.err.println("ERROR: Not allowed to set a balls total speed to 0");
		}
	}

	public void setLastCollidedWith(Entity newLastCollidedWith) {
		this.lastCollidedWith = newLastCollidedWith;
	}

	// item actions
	public void duplicateBall() {
		if (EntityHandler.ballArrayHasSpace()) {
			// creating a new ball
			BallEntity ball2 = new BallEntity(getPosition().x, getPosition().y);
			// giving the new ball movement in the opposite direction of the existing ball

			// calculating the angle this ball is moving in ad its speed
			float angle = (float) Math.atan(getSpeedUp() / getSpeedRight());
			float v_ges = (float) ((getSpeedUp() / Math.sin(angle)));

			// define the new angles for the two balls (this one and the one newly created)
			float angle_this_new = angle + 0.5f;
			float angle_dup_new = angle - 0.5f;

			// calculating new speeds for:
			// the newly created ball
			float this_new_up = (float) (Math.sin(angle_this_new) * v_ges);
			float this_new_right = (float) (Math.cos(angle_this_new) * v_ges);
			// ball2
			float dup_new_up = (float) (Math.sin(angle_dup_new) * v_ges);
			float dup_new_right = (float) (Math.cos(angle_dup_new) * v_ges);

			// not allowing speeds to be set to 0 to eliminate problems
			// for this ball
			if (this_new_up == 0f) {
				setSpeedUp(0.0001f);
			} else {
				setSpeedUp(this_new_up);
			}
			if (this_new_right == 0f) {
				setSpeedRight(0.0001f);
			} else {
				setSpeedRight(this_new_right);
			}

			// for the newly created ball
			if (dup_new_up == 0f) {
				ball2.setSpeedUp(0.0001f);
			} else {
				ball2.setSpeedUp(dup_new_up);
			}
			if (dup_new_right == 0f) {
				ball2.setSpeedRight(0.0001f);
			} else {
				ball2.setSpeedRight(dup_new_right);
			}
			ball2.updateImage();
		} else {
			System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
		}
	}

	public void max_speedBall() {
		setTotalSpeed(Variables.INITIAL_TOTAL_SPEED * Constants.MAX_SPEED_MULTIPLIER);
	}

	public void min_speedBall() {
		setTotalSpeed(Variables.INITIAL_TOTAL_SPEED);
	}

	public void destroyBall() {
		if (LevelHandler.getActiveBallCount() <= 1) {
			// if this wa the last ball: subtract a life
			PlayerHandler.subtractOneLife();
		}

		// remove the ball from the StateBasedEntityManager
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);

		// animate the destruction
		animateDestruction();

		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);

		// remove this ball from the list which is keeping track of every ball
		EntityHandler.removeBall(this);
	}


	// animation
	private void animateDestruction() {
		// animate the destruction

		LevelHandler.increaseActiveDestroyedBall();

		SoundHandler.playDestroyBall();

		// create the destroyed ball
		Entity destroyedBall = new Entity(Constants.DESTROYED_BALL_ID);    // entity
		destroyedBall.setPosition(getPosition());    // starting position
		destroyedBall.setScale(Variables.BLOCK_SCALE * 4 * 0.7f);
		destroyedBall.setRotation((float) (Math.random() * 360));    // starting rotation
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			try {
				// add the image of the destroyed ball
				destroyedBall.addComponent(new ImageRenderComponent(new Image(ThemeHandler.DESTROYED_BALL)));
			} catch (SlickException e) {
				System.err.println("Cannot find file " + ThemeHandler.DESTROYED_BALL);
				e.printStackTrace();
			}
		}

		// giving StateBasedEntityManager the destroyedBall-entity
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, destroyedBall);

		// movement for the destroyed ball
		LoopEvent destroyedLoop = new LoopEvent();
		if (OptionsHandler.getGameMode() == 0) {
			destroyedLoop.addAction(new MoveDownAction(Variables.INITIAL_BALL_SPEED_UP / 2));
		} else if (OptionsHandler.getGameMode() == 1) {
			if (getPosition().y > Variables.WINDOW_HEIGHT / 2) {
				destroyedLoop.addAction(new MoveDownAction(Variables.INITIAL_BALL_SPEED_UP / 2));
			} else {
				destroyedLoop.addAction(new MoveUpAction(Variables.INITIAL_BALL_SPEED_UP / 2));
			}
		}

		destroyedLoop.addAction(new RotateLeftAction(0.3f));
		// remove the destroyedBall after it left the screen and reduce the counter for destroyedBalls in play
		destroyedLoop.addAction((gc, sb, delta, event) -> {
			if (destroyedBall.getPosition().y > Variables.WINDOW_HEIGHT + destroyedBall.getSize().y
					|| destroyedBall.getPosition().y < (-destroyedBall.getSize().y)
					|| destroyedBall.getPosition().x > (Variables.WINDOW_WIDTH + destroyedBall.getSize().x)
					|| destroyedBall.getPosition().x < (-destroyedBall.getSize().x)) {
				StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, destroyedBall);
				LevelHandler.decreaceActiveDestroyedBallCount();
			}
		});
		destroyedBall.addComponent(destroyedLoop);
	}

	public void levelComplete() {
		// remove the ball from the StateBasedEntityManager
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);
		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);
		// remove this ball from the list which is keeping track of every ball
		EntityHandler.removeBall(this);
	}

	void updateImage() {
		String newImage = ThemeHandler.STANDARDBALL;
		if (getTotalSpeed() < Variables.INITIAL_TOTAL_SPEED * 1.05f) {
			newImage = ThemeHandler.WATERBALL;
		} else if (getTotalSpeed() > Variables.INITIAL_TOTAL_SPEED * Constants.MAX_SPEED_MULTIPLIER * 0.95f) {
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