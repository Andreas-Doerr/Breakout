package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 14.03.2017.
 *
 * @author Andreas Dörr
 */
public class StickEntity extends Entity {

	private boolean bot = false;
	private Entity indicator;
	private Entity indicatorOut;
	private Constants.StickShape widthModifier = Constants.StickShape.NORMAL;

	public StickEntity(String entityID, int pos_x, int pos_y, int left_button, int right_button, boolean mouseInput, int controllerAxis) {
		super(entityID);

		// starting position
		setPosition(new Vector2f(pos_x, pos_y));

		EntityHandler.addStick(this);

		// picture
		updateImage();

		// setting to not be passable
		setPassable(false);

		// scale
		setScale(Variables.BLOCK_SCALE * 4);

		// stick movement
		// left button listener
		KeyDownEvent left_down = new KeyDownEvent(left_button);
		// stick movement
		left_down.addAction((gc, sb, delta, event) -> {
			// stick can't move out of screen or when game is paused or when the controller is trying to move it
			if (OptionsHandler.getControlMode() == 0) {
				moveStick(-1f);
			}
		});
		addComponent(left_down);

		// right button listener
		KeyDownEvent right_down = new KeyDownEvent(right_button);
		// stick movement
		right_down.addAction((gc, sb, delta, event) -> {
			// stick can't move out of screen or when game is paused or when the controller is trying to move it
			if (OptionsHandler.getControlMode() == 0) {
				moveStick(1f);
			}
		});
		addComponent(right_down);

		// LoopEvent
		LoopEvent stickLoop = new LoopEvent();
		addComponent(stickLoop);

		// Controller Input and mouse Input
		stickLoop.addAction((gc, sb, delta, event) -> {
			if (OptionsHandler.getControlMode() == 2) {
				moveStick(ControllerHandler.getStick(controllerAxis));
			} else if (mouseInput && OptionsHandler.getControlMode() == 1 && !(gc.isPaused())) {
				if (gc.getInput().getMouseX() < (getSize().x / 2)) {
					// too far left
					setPosition(new Vector2f(getSize().x / 2, getPosition().y));
				} else if (gc.getInput().getMouseX() > (Variables.WINDOW_WIDTH - (getSize().x / 2))) {
					// too far right
					setPosition(new Vector2f(Variables.WINDOW_WIDTH - (getSize().x / 2), getPosition().y));
				} else {
					setPosition(new Vector2f(gc.getInput().getMouseX(), getPosition().y));
				}
			}
		});

		// indicators
		indicator = new Entity("indicator");
		indicator.setPassable(true);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			try {
				indicator.addComponent(new ImageRenderComponent(new Image("images/indicator.png")));
			} catch (SlickException e) {
				System.err.println("ERROR: Could not load image: images/indicator.png");
				e.printStackTrace();
			}
		}
		indicator.setVisible(false);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicator);

		indicatorOut = new Entity("indicatorOut");
		indicatorOut.setPassable(true);
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			try {
				indicatorOut.addComponent(new ImageRenderComponent(new Image("images/indicatorOut.png")));
			} catch (SlickException e) {
				System.err.println("ERROR: Could not load image: images/indicatorOut.png");
				e.printStackTrace();
			}
		}
		indicatorOut.setVisible(false);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicatorOut);

		// indicator positioning
		stickLoop.addAction((gc, sb, delta, event) -> {
			boolean bottom = true;
			BallEntity ball;
			if (getPosition().y > (Variables.WINDOW_HEIGHT / 2)) {
				ball = EntityHandler.getLowestDownMovingBall();
			} else {
				ball = EntityHandler.getHighestUpMovingBall();
				bottom = false;
			}

			if (ball != null) {
				// calculate the x-pos for the ball when reaching y == hitY with its current direction
				float ballX = ball.getPosition().x;
				float ballY = ball.getPosition().y;
				float angle = ball.getMovementAngleRAD();
				float offsetY = ((getPosition().y - Variables.COLLISION_DISTANCE) - ballY) / (float) Math.tan(angle);
				float newX = ballX - offsetY;
				if (!bottom) {
					angle = -angle;
					offsetY = ((getPosition().y + Variables.COLLISION_DISTANCE) - ballY) / (float) Math.tan(angle);
					newX = ballX + offsetY;
				}

				float ballDirectionDEG = ball.getMovementAngleDEG();

				float indicator_DEG;
				float indicator_Y = getPosition().y;

				if (bottom) {
					if (ballDirectionDEG > 0) {
						indicator_DEG = 90 - ballDirectionDEG;
					} else {
						indicator_DEG = 270 - ballDirectionDEG;
					}
					indicator_Y -= getSize().y * 0.75f;

				} else {
					if (ballDirectionDEG > 0) {
						indicator_DEG = 270 - ballDirectionDEG;
					} else {
						indicator_DEG = 90 - ballDirectionDEG;
					}
					indicator_Y += getSize().y * 0.75f;

				}
				indicator.setRotation(indicator_DEG);
				indicator.setPosition(new Vector2f(newX, indicator_Y));
				indicator.setVisible(true);

				// due to prior stick movement calculations
				float offsetX = newX - getPosition().x;

				float newDirection = 0f;
				if (offsetX < (getSize().x * 0.6f) && offsetX > -(getSize().x * 0.6f)) {

					float angle_change = offsetX / (getSize().x / 2);

					if (ball.getSpeedRight() > 0) {
						angle_change = -angle_change;
					}

					// (-1) <= (angle_change) <= (1)
					if (angle_change > 1f) {
						angle_change = 1f;
					} else if (angle_change < -1f) {
						angle_change = -1f;
					}

					// (0) <= (angle_change) <= (1)
					angle_change = (angle_change + 1) / 2;

					float newDirectionDEG;
					if (bottom) {
						newDirection = -(angle * (angle_change + 0.5f));
						newDirectionDEG = (float) (newDirection / Math.PI * 180);
						if (newDirectionDEG > 0) {
							newDirectionDEG = 90 - newDirectionDEG;
						} else {
							newDirectionDEG = 270 - newDirectionDEG;
						}

					} else {
						newDirection = (angle * (angle_change + 0.5f));
						newDirectionDEG = (float) (newDirection / Math.PI * 180);
						if (newDirectionDEG > 0) {
							newDirectionDEG = 270 - newDirectionDEG;
						} else {
							newDirectionDEG = 90 - newDirectionDEG;
						}
					}
					indicatorOut.setRotation(newDirectionDEG);
					indicatorOut.setPosition(new Vector2f(newX, indicator_Y));
					indicatorOut.setVisible(true);
				} else if (indicatorOut.isVisible()) {
					indicatorOut.setVisible(false);
				}

				// BOT
				if (bot) {
					BlockEntity target = EntityHandler.getMostLeftLowestBlock();
					if (target != null) {
						// calculating the desired offset to hit the targeted block
						float targetX = target.getPosition().x;
						float targetY = target.getPosition().y;
						float ballHitX = newX;
						float ballHitY = getPosition().y - Variables.COLLISION_DISTANCE;

						// calculating newAngle needed to reach target from ballHit = 0f;
						float dY = ballHitY - targetY;
						float dX = targetX - ballHitX;
						float m = dY / dX;
						float neededAngle = (float) Math.atan(m);

						// calculating desiredOffset from neededAngle
						float desiredOffset;

						float neededAngleChange = (-neededAngle / angle);

						// (-1) <= (angle_change) <= (1)
						if (neededAngleChange > 1f) {
							neededAngleChange = 1f;
						} else if (neededAngleChange < -1f) {
							neededAngleChange = -1f;
						}

						desiredOffset = neededAngleChange * (getSize().x / 2);

						if (bottom) {
							if (neededAngle < 0) {
								if (neededAngle < newDirection) {
									desiredOffset = Math.abs(desiredOffset);
								} else desiredOffset = -Math.abs(desiredOffset);
								if (newDirection > 0) desiredOffset = -desiredOffset;
							} else {
								if (neededAngle < newDirection) {
									desiredOffset = Math.abs(desiredOffset);
								} else desiredOffset = -Math.abs(desiredOffset);
								if (newDirection < 0) desiredOffset = -desiredOffset;
							}
						} else {
							if (neededAngle < 0) {
								if (neededAngle < newDirection) {
									desiredOffset = Math.abs(desiredOffset);
								} else desiredOffset = - Math.abs(desiredOffset);
								if (newDirection < 0) desiredOffset = -desiredOffset;
							} else {
								if (neededAngle < newDirection) {
									desiredOffset = Math.abs(desiredOffset);
								} else desiredOffset = - Math.abs(desiredOffset);
								if (newDirection > 0) desiredOffset = -desiredOffset;
							}
						}

						// capping the desiredOffset to the stick's width
						if (desiredOffset < -getSize().x / 2) {
							desiredOffset = -getSize().x / 2;
						} else if (desiredOffset > getSize().x / 2) {
							desiredOffset = getSize().x / 2;
						}
						// adding the desiredOffset to the ballHitX
						newX = ballHitX - desiredOffset;
					}

					// making the stick move to its destination (faster the further it is away from it)
					float speed = (newX - getPosition().x) / 25;
					// capping the speed to 1 / -1
					if (speed < -1f) {
						speed = -1f;
					} else if (speed > 1f) {
						speed = 1f;
					}

					moveStick(speed);
				}

			} else if (indicator.isVisible()) {
				indicator.setVisible(false);
				indicatorOut.setVisible(false);
			}
		});

		// adding the Stick to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, this);

		// moving the stick above the indicator in the entity-list
		EntityHandler.readdIndicators();
	}

	private void moveStick(float speed) {
		if (!Breakout.getApp().isPaused() && (speed < -0.001f && (getPosition().x > getSize().x / 2)) || (speed > 0.001f && getPosition().x < (Variables.WINDOW_WIDTH - (getSize().x / 2)))) {
			setPosition(new Vector2f(getPosition().x + Variables.STICK_SPEED * speed * (float) Constants.FRAME_RATE / (float) Breakout.getApp().getFPS(), getPosition().y));
		}
	}

	public void activateBot() {
		bot = true;
	}
	public void deactivateBot() {
		bot = false;
	}
	public void switchBot() {
		bot = !bot;
	}

	public void slimmer() {
		// decrease this sticks width
		if (widthModifier == Constants.StickShape.NORMAL) {
			// stick: NORMAL -> SLIM
			widthModifier = (Constants.StickShape.SLIM);
			updateImage();
		} else if (widthModifier == Constants.StickShape.WIDE) {
			// stick: WIDE -> NORMAL
			widthModifier = (Constants.StickShape.NORMAL);
			updateImage();
		}
	}
	public void wider() {
		// increase this sticks width
		if (widthModifier == Constants.StickShape.SLIM) {
			// stick: SLIM -> NORMAL
			widthModifier = (Constants.StickShape.NORMAL);
			updateImage();
		} else if (widthModifier == Constants.StickShape.NORMAL) {
			// stick: NORMAL -> WIDE
			widthModifier = (Constants.StickShape.WIDE);
			updateImage();
		}
	}
	private void updateImage() {
		if (!Breakout.getDebug()) {
			String newStickImage = null;
			// select the new image using the WidthModifier
			if (widthModifier == Constants.StickShape.SLIM) {
				// if SLIM
				newStickImage = ThemeHandler.STICK_SLIM;
			} else if (widthModifier == Constants.StickShape.NORMAL) {
				// if NORMAL
				newStickImage = ThemeHandler.STICK;
			} else if (widthModifier == Constants.StickShape.WIDE) {
				// if WIDE
				newStickImage = ThemeHandler.STICK_WIDE;
			}
			// activate the new image
			try {
				addComponent(new ImageRenderComponent(new Image(newStickImage)));
			} catch (SlickException e) {
				System.err.println("ERROR: Could not load image: " + newStickImage);
				e.printStackTrace();
			}
		}
	}

	public void destroyStick() {
		// remove this stick from the list which is keeping track of every stick
		EntityHandler.removeStick(this);
		// delete the stick
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);

	}

	public void readdIndicators() {
		if (indicator != null) {
			StateBasedEntityManager.getInstance().removeEntity(Breakout.GAMEPLAY_STATE, indicator);
			StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicator);
		}
		if (indicatorOut != null) {
			StateBasedEntityManager.getInstance().removeEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
			StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
		}
	}
}
