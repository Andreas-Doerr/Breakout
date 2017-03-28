package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.ControllerHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by PC - Andreas on 18.03.2017.
 *
 * @author Andreas Dörr
 */
public class PlayerStickEntity extends StickEntity {

	private Entity indicator;
	private Entity indicatorOut;

	public PlayerStickEntity(String stickID, Vector2f pos, int left_button, int right_button, boolean mouseInput, int conrtrollerAxis) {
		super(stickID);

		// starting position
		setPosition(pos);

	// stick movement
		// left button listener
		KeyDownEvent left_pressed = new KeyDownEvent(left_button);
		// stick movement
		left_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// stick can't move out of screen or when game is paused or when the controller is trying to move it
				if (OptionsHandler.getControlMode() == 0 && getPosition().x > (getSize().x / 2) && !(gc.isPaused())) {
					moveLeft();
				}
			}
		});
		addComponent(left_pressed);

		// right button listener
		KeyDownEvent right_pressed = new KeyDownEvent(right_button);
		// stick movement
		right_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// stick can't move out of screen or when game is paused or when the controller is trying to move it
				if (OptionsHandler.getControlMode() == 0 && getPosition().x < (Variables.WINDOW_WIDTH - (getSize().x / 2)) && !(gc.isPaused())) {
					moveRight();
				}
			}
		});
		addComponent(right_pressed);

		// Controller Input and mouse Input
		LoopEvent stickLoop = new LoopEvent();
		addComponent(stickLoop);
		stickLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (OptionsHandler.getControlMode() == 2 &&!gc.isPaused() &&
						((ControllerHandler.getStick(conrtrollerAxis) > 0 && getPosition().x < (Variables.WINDOW_WIDTH - (getSize().x / 2)))
						|| (ControllerHandler.getStick(conrtrollerAxis) < 0 && getPosition().x > (getSize().x / 2)))) {
					setPosition(new Vector2f(getPosition().x + (Variables.STICK_SPEED * ControllerHandler.getStick(conrtrollerAxis)) * (float) Constants.FRAME_RATE / (float) gc.getFPS(), getPosition().y));
				} else if (mouseInput && OptionsHandler.getControlMode() == 1) {
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
			}
		});

		// indicators

		indicator = new Entity("indicator");
		indicator.setPassable(true);
		try {
			indicator.addComponent(new ImageRenderComponent(new Image("images/indicator.png")));
		} catch (SlickException e) {
			System.err.println("ERROR: Could not load image: images/indicator.png");
			e.printStackTrace();
		}
		indicator.setVisible(false);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicator);

		indicatorOut = new Entity("indicatorOut");
		indicatorOut.setPassable(true);
		try {
			indicatorOut.addComponent(new ImageRenderComponent(new Image("images/indicatorOut.png")));
		} catch (SlickException e) {
			System.err.println("ERROR: Could not load image: images/indicatorOut.png");
			e.printStackTrace();
		}
		indicatorOut.setVisible(false);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicatorOut);

		LevelHandler.readdIndicators();

		stickLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i, Component component) {

				BallEntity ball = LevelHandler.getLowestDownMovingBall();
				if (ball != null) {
					// calculate the x-pos for the ball when reaching y == hitY with its current direction
					float hitY = Variables.STICK_Y - Variables.COLLISION_DISTANCE;
					float ballX = ball.getPosition().x;
					float ballY = ball.getPosition().y;
					float angle = ball.getMovementAngleRAD();
					float offsetY = (hitY - ballY) / (float) Math.tan(angle);

					float newX = ballX - offsetY;

					float ballDirectionDEG = ball.getMovementAngleDEG();

					if (ballDirectionDEG > 0) {
						indicator.setRotation(90 - ballDirectionDEG);
					} else {
						indicator.setRotation(270 - ballDirectionDEG);
					}

					indicator.setVisible(true);
					indicator.setPosition(new Vector2f(newX, Variables.STICK_Y + -getSize().y * 0.75f));

					// due to prior stick movement calculations
					float offsetX = newX - getPosition().x;

					if (offsetX < (getSize().x * 0.6f) && offsetX > -(getSize().x * 0.6f)) {

						float angle_change = offsetX / (getSize().x / 2);

						if (ball.getSpeedRight() > 0) {
							angle_change = -angle_change;
						}

						// angle_change is to be >= 0, <= 1
						if (angle_change > 1f) {
							angle_change = 1f;
						} else if (angle_change < -1f) {
							angle_change = -1f;
						}

						angle_change = (angle_change + 1) / 2;

						float newDirection = -(angle * (angle_change + 0.5f));

						float newDirectionDEG = (float) (newDirection / Math.PI * 180);

						if (newDirectionDEG > 0) {
							indicatorOut.setRotation(90 - newDirectionDEG);
						} else {
							indicatorOut.setRotation(270 - newDirectionDEG);
						}


						indicatorOut.setVisible(true);
						indicatorOut.setPosition(new Vector2f(newX, Variables.STICK_Y - getSize().y * 0.75f));

					} else if (indicatorOut.isVisible()) {
						indicatorOut.setVisible(false);
					}
				} else if (indicator.isVisible()) {
					indicator.setVisible(false);
					indicatorOut.setVisible(false);
				}
			}
		});
	}

	public void readdIndicators() {
		StateBasedEntityManager.getInstance().removeEntity(Breakout.GAMEPLAY_STATE, indicator);
		StateBasedEntityManager.getInstance().removeEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicator);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
	}


}
