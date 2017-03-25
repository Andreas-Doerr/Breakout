package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
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

	private Constants.StickShape widthModifier = Constants.StickShape.NORMAL;

	public StickEntity(String entityID) {
		super(entityID);

		LevelHandler.addStick(this);

		// picture
		updateImage();

		// setting to not be passable
		setPassable(false);

		// scale
		setScale(Variables.BLOCK_SCALE * 4);

		// adding the Stick to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, this);
	}



	public Constants.StickShape getWidthModifier() {
		return widthModifier;
	}
	public void setWidthModifier(Constants.StickShape newWidthModifier) {
		widthModifier = newWidthModifier;
	}

	void moveLeft() {
		setPosition(new Vector2f(getPosition().x - Variables.STICK_SPEED * (float) Constants.FRAME_RATE / (float) Breakout.getApp().getFPS(), getPosition().y));
	}
	void moveRight() {
		setPosition(new Vector2f (getPosition().x + Variables.STICK_SPEED * (float) Constants.FRAME_RATE / (float) Breakout.getApp().getFPS(), getPosition().y));
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
		// delete the stick
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);
		// remove this stick from the list which is keeping track of every stick
		LevelHandler.removeStick(this);
	}
}
