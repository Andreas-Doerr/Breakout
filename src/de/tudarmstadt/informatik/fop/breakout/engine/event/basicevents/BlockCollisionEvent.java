package de.tudarmstadt.informatik.fop.breakout.engine.event.basicevents;

import de.tudarmstadt.informatik.fop.breakout.engine.event.IteratedCollisionEvent;
import de.tudarmstadt.informatik.fop.breakout.handlers.EntityHandler;

/**
 * Created by PC - Andreas on 13.04.2017.
 *
 * @author Andreas Dörr
 */
public class BlockCollisionEvent extends IteratedCollisionEvent {

	@Override
	protected int checkCollisionFor(int startAt) {
		return checkCollision(EntityHandler.getBlockArray(), startAt);
	}
}
