package de.tudarmstadt.informatik.fop.breakout.engine.entity;

import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.PlayerHandler;
import de.tudarmstadt.informatik.fop.breakout.interfaces.IHitable;
import eea.engine.entity.Entity;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 22.03.2017.
 *
 * @author Andreas Dörr
 */
public class TestBlockEntity extends Entity implements IHitable{

	private int hitsLeft;

	public TestBlockEntity(String entityID, int originalHitsLeft, float originalPos_x, float originalPos_y) {
		super(entityID);

		this.hitsLeft = originalHitsLeft;
		this.setPosition(new Vector2f(originalPos_x, originalPos_y));

		// add 1 to the counter of active blocks
		LevelHandler.addActiveBlocks(1);
		// add this ball to the list which is keeping track of every ball
		LevelHandler.addBlock(this);


		// setting to not be passable
		setPassable(false);

		// block scaling
		setScale(LevelHandler.getScale());

	}

	@Override
	public int getHitsLeft() {
		return hitsLeft;
	}
	@Override
	public void addHitsLeft(int toAdd) {
		hitsLeft += toAdd;
	}
	@Override
	public void setHitsLeft(int newHitsLeft) {
		hitsLeft = newHitsLeft;
	}
	@Override
	public boolean hasHitsLeft() {
		return hitsLeft > 0;
	}

	public void hit() {
		hitsLeft--;
		if (!hasHitsLeft()) {
			destroyBlock();
		} else {
			if (hitsLeft == 4) {
				LevelHandler.blockExplosion(getID());
			}
			// adding points
			PlayerHandler.addPoints(1);
		}
	}

	public void destroyBlock() {
		// adding points
		PlayerHandler.addPoints(10);
		// increase the counter for the amount of blocks destroyed
		LevelHandler.addOneDestroyedBlock();
		// reduce the counter for the amount of blocks in play by one
		LevelHandler.addActiveBlocks(-1);
		// remove this block from the list which is keeping track of every block
		LevelHandler.removeBlock(this);
	}
}
