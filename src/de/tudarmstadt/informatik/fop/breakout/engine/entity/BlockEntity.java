package de.tudarmstadt.informatik.fop.breakout.engine.entity;


import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.handlers.ItemHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.LevelHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.PlayerHandler;
import de.tudarmstadt.informatik.fop.breakout.handlers.ThemeHandler;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by PC - Andreas on 22.03.2017.
 *
 * @author Andreas Dörr
 */
public class BlockEntity extends TestBlockEntity {

	public BlockEntity(String entityID, int newHitsLeft, float newPos_x, float newPos_y) {
		super(entityID, newHitsLeft, newPos_x, newPos_y);

		updateImage();

		// add the block to the StateBasedEntityManager (this is why it has to overwrite)
		// (TestBlockEntity would cause tests to fail if it tried to add itself to the StateBasedEntityManager)
		StateBasedEntityManager.getInstance().addEntity(GameParameters.GAMEPLAY_STATE, this);

	}

	@Override
	public void hit() {
		addHitsLeft(-1);
		if (!hasHitsLeft()) {
			destroyBlock();
		} else {
			if (getHitsLeft() == 4) {
				LevelHandler.blockExplosion(getID());
			}
			// adding points
			PlayerHandler.addPoints(1);

			updateImage();
		}
	}

	@Override
	public void destroyBlock() {
		// remove the block from the StateBasedEntityManager (this is why it has to overwrite)
		// (TestBlockEntity would cause tests to fail if it tried to remove itself from the StateBasedEntityManager)
		StateBasedEntityManager.getInstance().removeEntity(GameParameters.GAMEPLAY_STATE, this);
		// adding points
		PlayerHandler.addPoints(10);
		// increase the counter for the amount of blocks destroyed
		LevelHandler.addOneDestroyedBlock();
		// reduce the counter for the amount of blocks in play by one
		LevelHandler.addActiveBlocks(-1);
		// remove this block from the list which is keeping track of every block
		LevelHandler.removeBlock(this);
		// create an item
		ItemHandler.createItem(getPosition(), GameParameters.ITEM_DROPCHANCE);
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
				imageRef =ThemeHandler.BLOCK_5;
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






