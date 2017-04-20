package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.Action;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 03.04.2017.
 *
 * @author Andreas Dörr
 */
public class ButtonEntity extends Entity {

	private ImageRenderComponent image;
	private ANDEvent buttonEvents;
	private Constants.ButtonType type;
	private boolean isSelected = false;

	public ButtonEntity(String entityID, int stateID, Constants.ButtonType type, int x, int y) {
		super(entityID + "Button");
		this.type = type;

		// position
		setPosition(new Vector2f(x, y));

		// scale
		if (this.type == Constants.ButtonType.MAINMENU) {
			setScale(Variables.MENU_ENTRY_SCALE);
		} else setScale(Variables.ENTRY_SCALE);

		// image
		updateImage();

		// adding to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(stateID, this);

		// buttonEvents
		buttonEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		addComponent(buttonEvents);

		// selection
		LoopEvent loop = new LoopEvent();
		addComponent(loop);
		loop.addAction((gc, sb, delta, event) -> {
			Vector2f mousePosition = new Vector2f((float) gc.getInput().getMouseX(), (float) gc.getInput().getMouseY());
			if (getShape().contains(mousePosition.x, mousePosition.y)) {
				if (!isSelected) {
					isSelected = true;
					updateImage();
				}
			} else {
				if (isSelected) {
					isSelected = false;
					updateImage();
				}
			}
		});

	}

	private void updateImage() {
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			// standard Button
			String imageRef = ThemeHandler.BUTTON;
			if (type == Constants.ButtonType.NORMAL && isSelected) {
				// if the standard button is selected
				imageRef = ThemeHandler.SELECTED_BUTTON;
			} else if (type == Constants.ButtonType.MAINMENU) {
				// MainMenu Button
				if (isSelected) {
					// if it is selected
					imageRef = ThemeHandler.SELECTED_MENU_BUTTON;
				} else {
					// if it is not selected
					imageRef = ThemeHandler.MENU_BUTTON;
				}
			} else if (type == Constants.ButtonType.WIDE) {
				// Wide Button
				if (isSelected) {
					// if it is selected
					imageRef = ThemeHandler.SELECTED_BUTTON_WIDE;
				} else {
					// if it is not selected
					imageRef = ThemeHandler.BUTTON_WIDE;
				}
			}

			try {
				removeComponent(image);
				image = new ImageRenderComponent(new Image(imageRef));
				addComponent(image);
			} catch (SlickException e) {
				System.err.println("ERROR: could not load button image: " + imageRef);
				e.printStackTrace();
			}
		}
	}

	public void addAction(Action action) {
		// adding an action to the buttonEvents
		buttonEvents.addAction(action);
	}


}
