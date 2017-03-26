package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
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
public class BotStickEntity extends StickEntity {

	private boolean first = true;
	private float oldBall_x = 0;

	public BotStickEntity(float pos_x, BallEntity givenBall) {
		super(Constants.BOT_STICK_ID);

		// starting position
		setPosition(new Vector2f(pos_x, Variables.STICK_Y));

		LoopEvent stickBotLoop = new LoopEvent();
		addComponent(stickBotLoop);

		Entity indicator = new Entity("indicator");
		indicator.setPassable(true);
		try {
			indicator.addComponent(new ImageRenderComponent(new Image("images/faster.png")));
		} catch (SlickException e) {
			System.err.println("ERROR: Could not load image: images/faster.png");
			e.printStackTrace();
		}
		indicator.setScale(2);
		indicator.setPosition(new Vector2f(200,200));
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicator);

		// movement and loosing the ball
		stickBotLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// movement
				/*
				if (ball.getPosition().x > (getSize().x / 2) && ball.getPosition().x < (Variables.WINDOW_WIDTH - (getSize().x / 2))) {
					setPosition(new Vector2f(ball.getPosition().x, Variables.STICK_Y));
				}*/
				BallEntity ball = LevelHandler.getLowestDownMovingBall();
				if (ball != null) {
					// calculate the x-pos for the ball when reaching y == hitY with its current direction
					float hitY = Variables.STICK_Y - Variables.COLLISION_DISTANCE;
					float ballX = ball.getPosition().x;
					float ballY = ball.getPosition().y;
					float ballDirection = ball.getMovementAngleRAD();
					float offset = (hitY - ballY) /(float) Math.tan(ballDirection);

					float newX = ballX - offset;
					// TODO indicator for how the ball will be reflected
					float direction = ball.getMovementAngleDEG();

					if (direction > 0) {
						indicator.setRotation(90-direction);
					} else {
						indicator.setRotation(270-direction);
					}


					// TODO use blockList from LevelHandler to aim for the blocks
					if (newX < (getSize().x / 2)) {
						newX = getSize().x / 2;
					} else if (newX > (Variables.WINDOW_WIDTH - (getSize().x / 2))) {
						newX = Variables.WINDOW_WIDTH - (getSize().x / 2);
					}
					setPosition(new Vector2f(newX, Variables.STICK_Y));

					indicator.setPosition(new Vector2f(newX, Variables.STICK_Y + - getSize().y/2));
				}
			}
		});
	}
}
