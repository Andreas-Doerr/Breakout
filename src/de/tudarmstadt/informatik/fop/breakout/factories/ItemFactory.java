package de.tudarmstadt.informatik.fop.breakout.factories;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.StickEntity;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IEntityFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 14.03.2017.
 *
 * @author Andreas Dörr
 */
public class ItemFactory implements IEntityFactory, Constants {

	private int type;
	private Vector2f position;


	/**
	 * Factory Constructor
	 *
	 * @param type
	 * determines the type of Item to create
	 */
	public ItemFactory(int type, Vector2f position) {
		// given the type of item and the position where it should spawn
		this.type = type;
		this.position = position;
	}

	@Override
	public Entity createEntity() {

		ItemHandler.addItemsActive(1);

		Entity item;
		String image;


		// create the collisionEvent for colliding with a stick
		CollisionEvent ce = new CollisionEvent();

		// ID and image for the given type
		switch (type) {

			case 1:
				// type: 1 (stick wider)
				item = new Entity(ITEM_1_ID);
				image = ThemeHandler.BIGGER;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// if the item hits the stick
						StickEntity stick = (StickEntity) ce.getCollidedEntity();
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						stick.wider();
						PlayerHandler.addPoints(50);
					}
				});
				break;
			case 2:
				// type: 2 (stick slimmer)
				item =  new Entity(ITEM_2_ID);
				image = ThemeHandler.SMALLER;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// if the item hits the stick
						StickEntity stick = (StickEntity) ce.getCollidedEntity();
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						stick.slimmer();
						PlayerHandler.addPoints(25);
					}
				});
				break;
			case 3:
				// type: 3 (loose ball)
				item =  new Entity(ITEM_3_ID);
				image = ThemeHandler.DESTROY_BALL;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						LevelHandler.destroyRandomBall();
						PlayerHandler.addPoints(-50);
					}
				});
				break;
			case 4:
				// type: 4 (duplicate ball)
				item =  new Entity(ITEM_4_ID);
				image = ThemeHandler.DUP;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						LevelHandler.duplicateAllBalls();
						PlayerHandler.addPoints(75);
					}
				});
				break;
			case 5:
				// type: 5 (max speed for all balls)
				item =  new Entity(ITEM_5_ID);
				image = ThemeHandler.FASTER;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						LevelHandler.max_speedAllBalls();
						PlayerHandler.addPoints(10);
					}
				});
				break;
			case 6:
				// type: 6 (min speed for all balls)
				item =  new Entity(ITEM_6_ID);
				image = ThemeHandler.SLOWER;
				// pickup-action
				ce.addAction((gc, sb, delta, event) -> {
					if (ce.getCollidedEntity().getID().equals(Constants.PLAYER_STICK_ID)) {
						// set the counter down one if it got collected
						ItemHandler.addItemsActive(-1);
						// remove the item
						StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
						// play sound
						SoundHandler.playPickupItem();
						// do ...
						LevelHandler.min_speedAllBalls();
						PlayerHandler.addPoints(15);
					}
				});
				break;
			default:
				// type is not implemented
				System.err.println("ItemType " + type + " is not implemented in ItemFactory");
				return null;
		}

		// add the collisionEvent for colliding with a stick
		item.addComponent(ce);

		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			try {
				item.addComponent(new ImageRenderComponent(new Image(image)));
			} catch (SlickException e) {
				System.err.println("Cannot find item-image " + image);
				e.printStackTrace();
			}
		}

		// setting the position to the given value
		item.setPosition(position);
		// setting item to be visible
		item.setVisible(true);
		// setting item to be passable
		item.setPassable(true);
		// setting the item scale (scaling with resolution changes)
		item.setScale(Variables.BLOCK_SCALE * 4);

		// item movement
		LoopEvent moveLoop = new LoopEvent();
		if (OptionsHandler.getGameMode() == 1 && Math.random() < 0.5f) {
			moveLoop.addAction(new MoveDownAction( - Variables.INITIAL_BALL_SPEED_UP / 4));
		} else {
			moveLoop.addAction(new MoveDownAction(Variables.INITIAL_BALL_SPEED_UP / 4));
		}
		// item deletion upon exiting visible area
		moveLoop.addAction((gc, sb, delta, event) -> {
			if (item.getPosition().y > Variables.WINDOW_HEIGHT + item.getSize().y || item.getPosition().y < -item.getSize().y) {
				// if the item fully left the screen: delete it
				// set the counter down one if it left the screen
				ItemHandler.addItemsActive(-1);
				// (item is half an item-height below the window)
				StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
			}
		});
		//for desrtoy all Items
		moveLoop.addAction((gc, sb, delta, event) -> {
			if (ItemHandler.getItemsToBeDestroyed() > 0) {
				// if the item is to be destroyed by a cheat
				// set the counter down one if it got destroyed
				ItemHandler.addItemsActive(-1);
				ItemHandler.oneItemLessToDestroy();
				// (item is half an item-height below the window)
				StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, item);
			}
		});
		item.addComponent(moveLoop);

		// item-creation sound
		SoundHandler.playCreateItem();

		// adding the item to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, item);

		return item;
	}

}
