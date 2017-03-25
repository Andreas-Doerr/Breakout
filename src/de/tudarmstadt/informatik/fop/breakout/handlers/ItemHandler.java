package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.factories.ItemFactory;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 14.03.2017.
 *
 * @author Andreas Dörr
 */
public class ItemHandler {

	private static int itemsActive = 0;
	private static int itemsToBeDestroyed = 0;

	public static void createItem(Vector2f pos, float chance) {
		// given the position and the chance for spawning an item
		if (Math.random() <= chance) {
			// if you are lucky
			// determining the type of item spawned
			// 1 <= type >= 6
			int type = (int) (Math.random() * 6 + 1);
			// create an item of the previously determined type at the given position
			new ItemFactory(type, pos).createEntity();
		}
	}

	public static int getItemsActive() {
		return itemsActive;
	}
	public static void setItemsActive(int new_itemsActive) {
		itemsActive = new_itemsActive;
	}
	public static void addItemsActive(int toAdd) {
		itemsActive += toAdd;
	}
	public static int getItemsToBeDestroyed() {
		return itemsToBeDestroyed;
	}
	public static void destroyAllItems() {
		itemsToBeDestroyed = itemsActive;
	}
}
