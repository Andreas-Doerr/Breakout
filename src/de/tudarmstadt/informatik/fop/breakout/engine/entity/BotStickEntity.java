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

	private Entity indicatorOut;

	public BotStickEntity(float pos_x) {
		super(Constants.BOT_STICK_ID);

		// starting position
		setPosition(new Vector2f(pos_x, Variables.STICK_Y));

		LoopEvent stickBotLoop = new LoopEvent();
		addComponent(stickBotLoop);

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

		// movement and loosing the ball
		stickBotLoop.addAction(new Action(){
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
					float angle = ball.getMovementAngleRAD();
					float offsetY = (hitY - ballY) / (float) Math.tan(angle);

					float newX = ballX - offsetY;

					setPosition(new Vector2f(newX, Variables.STICK_Y));

					float ballDirectionDEG = ball.getMovementAngleDEG();

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
				} else if (indicatorOut.isVisible()) {
					indicatorOut.setVisible(false);
				}
			}
		});
	}

	public void readdIndicators() {
		StateBasedEntityManager.getInstance().removeEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
		StateBasedEntityManager.getInstance().addEntity(Breakout.GAMEPLAY_STATE, indicatorOut);
	}
}
