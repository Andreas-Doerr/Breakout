package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author Andreas Dörr
 */
public class BallEntity extends Entity {
	private float SpeedUp;
	private float SpeedRight;
	private Entity lastCollidedWith;

	public BallEntity(Vector2f original_pos) {
		super(Constants.BALL_ID);
		this.SpeedUp = Variables.INITIAL_BALL_SPEED_UP;
		this.SpeedRight = Variables.INITIAL_BALL_SPEED_RIGHT;
		this.lastCollidedWith = this;

		// add 1 to the counter of active balls
		LevelHandler.addActiveBalls(1);
		// add this ball to the list which is keeping track of every ball
		LevelHandler.addBall(this);

		// setting all balls to be passable
		setPassable(true);

		// ball scaling
		setScale(Variables.BLOCK_SCALE * 4 * 0.7f);

		// set position
		setPosition(original_pos);

		updateImage();

		// ball movement
		LoopEvent ballLoop = new LoopEvent();
		ballLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (!gc.isPaused()) {
					setPosition(new Vector2f(getPosition().x + getSpeedRight(), getPosition().y -  getSpeedUp()));
				}
			}
		});
		addComponent(ballLoop);

		// when the ball collides with something ...
		CollisionEvent ce = new CollisionEvent();
		addComponent(ce);

		// TODO maybe: reflect by relative position (maybe also speed) like the ball-block collision

		// ... reflecting if collided with a border, stick, block ...
		ce.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				Entity collidedEntity = ce.getCollidedEntity();
				if (!collidedEntity.isPassable()) {
					boolean didCollide = true;

					float ball_centerToRight = getSize().x / 2;
					float ball_centerToTop = getSize().y / 2;

					float other_centerToRight = collidedEntity.getSize().x / 2;
					float other_centerToTop = collidedEntity.getSize().y / 2;

					float x_rel = Math.abs(collidedEntity.getPosition().x - getPosition().x);
					float y_rel = Math.abs(collidedEntity.getPosition().y - getPosition().y);

					// only corner of balls hitbox collided?
					if (x_rel > (ball_centerToRight + other_centerToRight)) {
						System.out.println("Too far apart horizontally");
						didCollide = false;
					} else if (y_rel > (ball_centerToTop + other_centerToTop)) {
						System.out.println("Too far apart vertically");
						didCollide = false;
					}

					// if the entity the ball collided with is not passable

					if (collidedEntity.getID().contains("Border")
							&& !getLastCollidedWith().equals(collidedEntity)) {

						// BORDER
						// if the ball collided with a border (except if the last collision was with exactly this entity)

						// play the sound
						SoundHandler.playHitBorder();
						// set this balls lastCollidedWith to this collidedEntity-entity
						setLastCollidedWith(collidedEntity);
						// calculate new speeds for this ball
						if (collidedEntity.getID().equals(Constants.TOP_BORDER_ID)) {
							// if it was the top Border
							setSpeedUp(-getSpeedUp());
						} else {
							// if it was any other border (left or right)
							setSpeedRight(-getSpeedRight());
						}
					} else if (	(collidedEntity.getID().equals(Constants.STICK_ID)
							&& getPosition().y < Variables.STICK_Y
							&& getSpeedUp() < 0
							|| collidedEntity.getID().equals(Constants.STICK_ID_TOP)
							&& getPosition().y > Variables.STICK_Y_TOP
							&& getSpeedUp() > 0)

							&& !getLastCollidedWith().equals(collidedEntity)) {

						// STICK
						// if the ball collided with the collidedWith (except if the last collision was with exactly this entity, the ball is too low or is not moving downwards)

						// set this balls lastCollidedWith to this collidedWith-entity
						setLastCollidedWith(collidedEntity);
						// calculate new speeds for this ball
						// not intended to support gravity since the ball won't bounce off the collidedWith twice without hitting something else inbetween
						// angle_change < 0 -> ball goes further left than normal
						// angle_change > 0 -> ball goes further right than normal
						// calculating angle_change
						float stick_x = collidedEntity.getPosition().x;
						float stick_side_length = collidedEntity.getSize().x / 2;
						float ball_x = getPosition().x;

						float offset = ball_x - stick_x;
						float angle_change = offset / stick_side_length;

						if (getSpeedRight() > 0) {
							angle_change = -angle_change;
						}
						// angle_change is to be >= 0, <= 1
						if (angle_change > 1f) {
							angle_change = 1f;
						} else if (angle_change < -1f) {
							angle_change = -1f;
						}
						angle_change = (angle_change + 1) / 2;

						// calculating reflected speeds
						float angle = getMovementAngleRAD();
						float angle_new = angle * (angle_change + 0.5f);
						//float v_ges = (float) ((getSpeedUp() / Math.sin(angle)));

						// calculate current speed (not absolute )
						//float v_ges = (float) ((getSpeedUp() / Math.sin(angle)));
						float v_ges = getTotalSpeed();
						// adding speedup to it
						float v_ges_new = v_ges + Variables.SPEEDUP_VALUE;

						// capping to max_speed
						if (v_ges_new > Variables.INITIAL_TOTAL_SPEED * Constants.MAX_SPEED_MULTIPLIER) {
							v_ges_new = Variables.INITIAL_TOTAL_SPEED * Constants.MAX_SPEED_MULTIPLIER;
						}
						// calculating new speeds based on old ones the new angle and the new total speed
						float new_up;
						float new_right;
						if (getSpeedUp() > 0) {
							new_up = - (float) Math.abs(Math.sin(angle_new) * v_ges_new);
						} else {
							new_up = (float) Math.abs(Math.sin(angle_new) * v_ges_new);
						}
						if (getSpeedRight() > 0) {
							new_right = (float) (Math.cos(angle_new) * v_ges_new);
						} else {
							new_right = -(float) ((Math.cos(angle_new) * v_ges_new));
						}

						// eliminate problems caused by one speed of the ball being 0 by never allowing it
						if (new_up == 0f) {
							setSpeedUp(0.0001f);
							System.out.println("You are really fine tuned! (Vertical speed of the ball reached 0)");
						} else {
							setSpeedUp(new_up);
						}
						if (new_right == 0f) {
							setSpeedRight(0.0001f);
							System.out.println("You are really fine tuned! (Horizontal speed of the ball reached 0)");
						} else {
							setSpeedRight(new_right);
						}

						updateImage();

						// calculate the pitch for the sound based on the speed of the ball before hitting the stick
						// approximately: 1f <= pitch >= Constants.MAX_SPEED_MULTIPLIER
						float pitch = Math.abs(v_ges) / Variables.INITIAL_TOTAL_SPEED;
						// play the sound
						SoundHandler.playHitStick(pitch);
					} else if (collidedEntity.getID().contains("block") && !getLastCollidedWith().equals(collidedEntity)) {

						// BLOCK
						// if the ball collided with a block (except if the last collision was with exactly this entity)

						BlockEntity block = (BlockEntity) collidedEntity;

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
							setLastCollidedWith(block);
							// destroying / replacing the block
							block.hit();
						}
					}
				}
			}
		});


		// always:
		//	if the ball is too far down: destroy the ball and everything associated with it
		ballLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {
				// if the ball is too far down destroy it and everything associated with it
				if (getPosition().y > Variables.WINDOW_HEIGHT * 0.92f) {
					// if the ball is too far down
					destroyBall();
				} else if (OptionsHandler.getGameMode() == 1 && getPosition().y < Variables.WINDOW_HEIGHT * 0.08f) {
					destroyBall();
				}
			}
		});

		LeavingScreenEvent lse =  new LeavingScreenEvent();
		addComponent(lse);

		// catching the ball if it sneaks through a border
		// damn you ball >:(
		// and setting its rotation to the MovementAngleDEG()
		lse.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (getPosition().y < 0 && OptionsHandler.getGameMode() != 1) {
					// went past the top border
					System.err.println("The ball sneaked through the top border (trying to bring it back)");
					setSpeedUp( - Math.abs(getSpeedUp()));
				}
				if (getPosition().x < 0) {
					// went past the left border
					System.err.println("The ball sneaked through the left border (trying to bring it back)");
					setSpeedRight(Math.abs(getSpeedRight()));
				}
				if (getPosition().x > Variables.WINDOW_WIDTH) {
					// went past the right border
					System.err.println("The ball sneaked through the right border (trying to bring it back)");
					setSpeedRight( - Math.abs(getSpeedRight()));
				}
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
	public float getMovementAngleRAD() {
		return (float) Math.atan(getSpeedUp() / getSpeedRight());
	}
	public float getMovementAngleDEG() {
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
	public void setLastCollidedWith(Entity newLastCollidedWithStick) {
		this.lastCollidedWith = newLastCollidedWithStick;
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

	// item actions
	public void duplicateBall() {
		if (LevelHandler.ballListHasSpace()) {
			// creating a new ball
			BallEntity ball2 = new BallEntity(getPosition());
			// giving the new ball movement in the opposite direction of the existing ball

			// calculating the angle this ball is moving in ad its speed
			float angle = (float )Math.atan(getSpeedUp() / getSpeedRight());
			float v_ges = (float) ((getSpeedUp() / Math.sin(angle)));

			// define the new angles for the two balls (this one and the one newly created)
			float angle_this_new = angle + 0.5f;
			float angle_dup_new = angle - 0.5f;

			// calculating new speeds for:
			// the newly created ball
			float this_new_up = (float) (Math.sin(angle_this_new) * v_ges);
			float this_new_right = (float)(Math.cos(angle_this_new) * v_ges);
			// ball2
			float dup_new_up = (float) (Math.sin(angle_dup_new) * v_ges);
			float dup_new_right = (float)(Math.cos(angle_dup_new) * v_ges);

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
		LevelHandler.animateDestruction(getPosition());
		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);
		// remove this ball from the list which is keeping track of every ball
		LevelHandler.removeBall(this);
	}
	public void levelComplete() {
		// remove the ball from the StateBasedEntityManager
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);
		// reduce the counter for the amount of balls in play by one
		LevelHandler.addActiveBalls(-1);
		// remove this ball from the list which is keeping track of every ball
		LevelHandler.removeBall(this);
	}

	public void updateImage() {
		String newImage = ThemeHandler.STANDARDBALL;
		if (getTotalSpeed() == Variables.INITIAL_TOTAL_SPEED) {
			newImage = ThemeHandler.WATERBALL;
		} else if (getTotalSpeed() == Variables.INITIAL_TOTAL_SPEED * Constants.MAX_SPEED_MULTIPLIER) {
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