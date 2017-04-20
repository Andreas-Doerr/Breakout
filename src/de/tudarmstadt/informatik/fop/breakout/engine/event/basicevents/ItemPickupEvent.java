package de.tudarmstadt.informatik.fop.breakout.engine.event.basicevents;

import de.tudarmstadt.informatik.fop.breakout.engine.event.IteratedCollisionEvent;
import de.tudarmstadt.informatik.fop.breakout.handlers.EntityHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ItemHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.SoundHandler;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import eea.engine.action.Action;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Iterator;

/**
 * Created by PC - Andreas on 13.04.2017.
 *
 * @author Andreas Dörr
 */
public class ItemPickupEvent extends IteratedCollisionEvent {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		int startAt = this.checkCollisionFor(0) + 1;
		while (startAt >= 0) {
			Iterator var5 = this.actions.iterator();
			while (var5.hasNext()) {
				Action action = (Action) var5.next();

				// set the counter down one
				ItemHandler.addItemsActive(-1);
				// remove the item
				StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this.getOwnerEntity());
				// play sound
				SoundHandler.playPickupItem();

				// perform the actions which have been added to this event
				action.update(gc, sb, delta, this);
			}

			startAt = this.checkCollisionFor(startAt) + 1;
		}

	}

	@Override
	protected int checkCollisionFor(int startAt) {
		return checkCollision(EntityHandler.getStickArray(), startAt);
	}

}
