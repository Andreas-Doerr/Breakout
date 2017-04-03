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

	private ANDEvent buttonEvents;

	public ButtonEntity(String entityID, int stateID, Constants.ButtonType type, int x, int y) {
		super(entityID + "Button");

		// position
		setPosition(new Vector2f(x, y));

		// scale
		if (type == Constants.ButtonType.MAINMENU) {
			setScale(Variables.MENU_ENTRY_SCALE);
		} else setScale(Variables.ENTRY_SCALE);

		// image
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			String imageRef = ThemeHandler.BUTTON;
			if (type == Constants.ButtonType.MAINMENU) {
				imageRef = ThemeHandler.MENU_BUTTON;
			} else if (type == Constants.ButtonType.WIDE) {
				imageRef = ThemeHandler.BUTTON_WIDE;
			}

			try {
				addComponent(new ImageRenderComponent(new Image(imageRef)));
			} catch (SlickException e) {
				System.err.println("ERROR: could not load button image: " + imageRef);
				e.printStackTrace();
			}
		}

		// adding to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(stateID, this);

		// buttonEvents
		buttonEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		addComponent(buttonEvents);

	}

	public void addAction(Action action) {
		// adding an action to the buttonEvents
		buttonEvents.addAction(action);
	}





}
