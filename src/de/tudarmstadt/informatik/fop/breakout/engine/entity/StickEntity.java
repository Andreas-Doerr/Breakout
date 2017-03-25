package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 14.03.2017.
 *
 * @author Andreas Dörr
 */
public class StickEntity extends Entity {

	private GameParameters.StickShape widthModifier = GameParameters.StickShape.NORMAL;

	StickEntity(String entityID) {
		super(entityID);

		LevelHandler.addStick(this);

		// picture
		updateImage();

		// setting to not be passable
		setPassable(false);

		// scale
		setScale(LevelHandler.getScale() * 4);

		// adding the Stick to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(GameParameters.GAMEPLAY_STATE, this);
	}



	public GameParameters.StickShape getWidthModifier() {
		return widthModifier;
	}
	public void setWidthModifier(GameParameters.StickShape newWidthModifier) {
		widthModifier = newWidthModifier;
	}

	void moveLeft() {
		setPosition(new Vector2f(getPosition().x - GameParameters.STICK_SPEED, getPosition().y));
	}
	void moveRight() {
		setPosition(new Vector2f (getPosition().x + GameParameters.STICK_SPEED, getPosition().y));
	}

	public void slimmer() {
		// decrease this sticks width
		if (widthModifier == GameParameters.StickShape.NORMAL) {
			// stick: NORMAL -> SLIM
			widthModifier = (GameParameters.StickShape.SLIM);
			updateImage();
		} else if (widthModifier == GameParameters.StickShape.WIDE) {
			// stick: WIDE -> NORMAL
			widthModifier = (GameParameters.StickShape.NORMAL);
			updateImage();
		}
	}
	public void wider() {
		// increase this sticks width
		if (widthModifier == GameParameters.StickShape.SLIM) {
			// stick: SLIM -> NORMAL
			widthModifier = (GameParameters.StickShape.NORMAL);
			updateImage();
		} else if (widthModifier == GameParameters.StickShape.NORMAL) {
			// stick: NORMAL -> WIDE
			widthModifier = (GameParameters.StickShape.WIDE);
			updateImage();
		}
	}
	private void updateImage() {
		if (!Breakout.getDebug()) {
			String newStickImage = null;
			// select the new image using the WidthModifier
			if (widthModifier == GameParameters.StickShape.SLIM) {
				// if SLIM
				newStickImage = ThemeHandler.STICK_SLIM;
			} else if (widthModifier == GameParameters.StickShape.NORMAL) {
				// if NORMAL
				newStickImage = ThemeHandler.STICK;
			} else if (widthModifier == GameParameters.StickShape.WIDE) {
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
		// delete the stick
		StateBasedEntityManager.getInstance().removeEntity(GameParameters.GAMEPLAY_STATE, this);
		// remove this stick from the list which is keeping track of every stick
		LevelHandler.removeStick(this);
	}
}
