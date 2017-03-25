package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by PC - Andreas on 18.03.2017.
 *
 * @author Andreas Dörr
 */
public class BotStickEntity extends StickEntity {

	private boolean first = true;
	private float oldBall_x = 0;

	public BotStickEntity(String entityID, float pos_x, BallEntity ball) {
		super(entityID);

		// starting position
		setPosition(new Vector2f(pos_x, Variables.STICK_Y));

		LoopEvent stickBotLoop = new LoopEvent();
		addComponent(stickBotLoop);

		// movement and loosing the ball
		stickBotLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// TODO maybe if mall moves down calculate where it will hit the stick and move there
				// movement
				if (ball.getPosition().x > (getSize().x / 2) && ball.getPosition().x < (Variables.WINDOW_WIDTH - (getSize().x / 2))) {
					setPosition(new Vector2f(ball.getPosition().x, Variables.STICK_Y));
				}

				// detects if the ball is lost and "removes" the stick if so
				if (first) {
					// only tests every second execution of this action (this stick updates twice as often as the balls speed (don't ask me why))
					if (oldBall_x == ball.getPosition().x && !gc.isPaused()) {
						// if the old position of the ball is the same as the new one (meaning the ball did not move)
						// report it to the console (DEBUG)
						System.out.println(BotStickEntity.this + "  lost its ball");
						destroyStick();
					}
				}
				// allow it to test on the next execution
				first = !first;
				// save the current position of the ball to later be used for detecting if the ball moved
				oldBall_x = ball.getPosition().x;
			}
		});
	}
}
