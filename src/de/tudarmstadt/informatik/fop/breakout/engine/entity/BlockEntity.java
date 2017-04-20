package de.tudarmstadt.informatik.fop.breakout.engine.entity;


import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by PC - Andreas on 22.03.2017.
 *
 * @author Andreas Dörr
 */
public class BlockEntity extends Entity {

	private int hitsLeft;

	public BlockEntity(String entityID, int originalHitsLeft, float originalPos_x, float originalPos_y) {
		super(entityID);

		this.hitsLeft = originalHitsLeft;
		this.setPosition(new Vector2f(originalPos_x, originalPos_y));

		// add 1 to the counter of active blocks
		LevelHandler.addActiveBlocks(1);
		// add this ball to the list which is keeping track of every ball
		EntityHandler.addBlock(this);


		// setting to not be passable
		setPassable(false);

		// block scaling
		setScale(Variables.BLOCK_SCALE);

		updateImage();

		// add the block to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, this);
	}


	public int getHitsLeft() {
		return hitsLeft;
	}

	public void addHitsLeft(int toAdd) {
		hitsLeft += toAdd;
	}

	public void setHitsLeft(int newHitsLeft) {
		hitsLeft = newHitsLeft;
	}

	public boolean hasHitsLeft() {
		return hitsLeft > 0;
	}

	public void hit() {
		addHitsLeft(-1);
		if (!hasHitsLeft()) {
			destroyBlock();
		} else {
			if (getHitsLeft() == 4) {
				EntityHandler.blockExplosion(getID());
			}
			// adding points
			PlayerHandler.addPoints(1);

			updateImage();
		}
	}

	public void destroyBlock() {
		// remove the block from the StateBasedEntityManager
		StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, this);
		// adding points
		PlayerHandler.addPoints(10);
		// increase the counter for the amount of blocks destroyed
		LevelHandler.addOneDestroyedBlock();
		// reduce the counter for the amount of blocks in play by one
		LevelHandler.addActiveBlocks(-1);
		// remove this block from the list which is keeping track of every block
		EntityHandler.removeBlock(this);
		// create an item
		ItemHandler.createItem(getPosition(), Constants.ITEM_DROPCHANCE);
	}

	private void updateImage() {
		if (!Breakout.getDebug()) {
			String imageRef = ThemeHandler.BLOCK_1;
			if (getHitsLeft() == 2) {
				imageRef = ThemeHandler.BLOCK_2;
			} else if (getHitsLeft() == 3) {
				imageRef = ThemeHandler.BLOCK_3;
			} else if (getHitsLeft() == 4) {
				imageRef = ThemeHandler.BLOCK_4;
			} else if (getHitsLeft() == 5) {
				imageRef = ThemeHandler.BLOCK_5;
			}
			try {
				// loading and assigning picture
				addComponent(new ImageRenderComponent(new Image(imageRef)));
			} catch (SlickException e) {
				System.err.println("Cannot find file " + imageRef);
				e.printStackTrace();
			}
		}
	}

}