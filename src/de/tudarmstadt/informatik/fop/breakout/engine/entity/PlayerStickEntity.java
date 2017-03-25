package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.ControllerHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.OptionsHandler;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by PC - Andreas on 18.03.2017.
 *
 * @author Andreas Dörr
 */
public class PlayerStickEntity extends StickEntity {

	public PlayerStickEntity(String entityID, int left_button, int right_button, Vector2f pos) {
		super(entityID);

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
				if (OptionsHandler.getControlMode() == 0 && getPosition().x < (GameParameters.WINDOW_WIDTH - (getSize().x / 2)) && !(gc.isPaused())) {
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
				if (OptionsHandler.getControlMode() == 2 &&!(gc.isPaused() &&
						(ControllerHandler.getRightStick_x() > 0 && getPosition().x < (GameParameters.WINDOW_WIDTH - (getSize().x / 2)) && !(gc.isPaused())
						|| ControllerHandler.getRightStick_x() < 0 && getPosition().x > (getSize().x / 2)) )) {
					setPosition(new Vector2f(getPosition().x + (GameParameters.STICK_SPEED * ControllerHandler.getRightStick_x()), getPosition().y));
				} else if (OptionsHandler.getControlMode() == 1
						&& gc.getInput().getMouseX() > (getSize().x / 2)
						&& gc.getInput().getMouseX() < (GameParameters.WINDOW_WIDTH - (getSize().x / 2))) {
					setPosition(new Vector2f(gc.getInput().getMouseX(), getPosition().y));
				}
			}
		});



	}
}
