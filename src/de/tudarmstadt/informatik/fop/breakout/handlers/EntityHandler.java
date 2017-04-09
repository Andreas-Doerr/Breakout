package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.BallEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.BlockEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.StickEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import eea.engine.entity.Entity;

/**
 * Created by PC - Andreas on 07.04.2017.
 *
 * @author Andreas Dörr
 */
public class EntityHandler {

// Arrays
	private static BlockEntity[] blockArray = new BlockEntity[Constants.MAX_AMOUNT_OF_BLOCKS];
	private static BallEntity[] ballArray = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS];
	private static StickEntity[] stickArray = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS];
	private static Entity[] borderArray = new Entity[Constants.MAX_AMOUNT_OF_BORDERS];
	private static Entity[] collidablesArray = new Entity[Constants.MAX_AMOUNT_OF_BLOCKS + Constants.MAX_AMOUNT_OF_STICKS + Constants.MAX_AMOUNT_OF_BORDERS];

	// resetter
	public static void resetEntityArrays() {
		blockArray = new BlockEntity[Constants.MAX_AMOUNT_OF_BLOCKS];
		ballArray = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS];
		stickArray = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS];
		borderArray = new Entity[Constants.MAX_AMOUNT_OF_BORDERS];
		collidablesArray = new Entity[Constants.MAX_AMOUNT_OF_BLOCKS + Constants.MAX_AMOUNT_OF_STICKS + Constants.MAX_AMOUNT_OF_BORDERS];
	}

// ENTITY Arrays
// BLOCKS
	public static boolean blockArrayHasSpace() {
		return blockArray[blockArray.length - 1] == null;
	}
	public static void addBlock(BlockEntity block) {
		if (blockArrayHasSpace()) {
			for (int i = 0; i < blockArray.length; i++) {
				if (blockArray[i] == null) {
					blockArray[i] = block;
					break;
				}
			}
			addCollidable(block);
		} else {
			System.err.println("Tried to add a block to the blockArray even though the maximum supported amount of blocks active at one time has been surpassed!");
		}
	}
	public static void removeBlock(BlockEntity block) {
		int i;
		for (i = 0; i < blockArray.length; i++) {
			if (blockArray[i] == block) {
				// searching for the entry which is to be removed
				blockArray[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		for (; i < blockArray.length - 1; i++) {
			// entries below the removed one are moved up one until reaching a null entry
			if (blockArray[i+1] != null) {
				blockArray[i] = blockArray[i + 1];
			} else {
				blockArray[i] = null;
				break;
			}
		}
		removeCollidable(block);
	}
	// getter
	public static BlockEntity[] getBlockArray() {
		return blockArray;
	}
	public static BlockEntity getMostLeftLowestBlock() {
		BlockEntity toReturn = null;
		if (blockArray[0] != null) {
			toReturn = blockArray[0];
			for (BlockEntity eachBlockArray : blockArray) {
				if (eachBlockArray != null) {
					if (eachBlockArray.getPosition().y > toReturn.getPosition().y) {
						toReturn = eachBlockArray;
					} else if (eachBlockArray.getPosition().y >= toReturn.getPosition().y && eachBlockArray.getPosition().x < toReturn.getPosition().x) {
						toReturn = eachBlockArray;
					} else break;
				}
			}
		}
		return toReturn;
	}
	// actions
	public static void destroyRandomBlock() {
		int amountOfBlocks = 0;
		for (BlockEntity eachBlockArray : blockArray) {
			if (eachBlockArray != null) {
				amountOfBlocks++;
			} else {
				break;
			}
		}
		if (amountOfBlocks > 0) {
			int blockToDestroy = (int) (Math.random() * amountOfBlocks);
			blockArray[blockToDestroy].destroyBlock();
		}
	}
	public static void destroyAllBlocks() {
		while (blockArray[0] != null) {
			blockArray[0].destroyBlock();
		}
	}
	public static int getHitsLeft(String block_ID) throws NullPointerException {
		for (BlockEntity eachBlockArray : blockArray) {
			if (eachBlockArray != null) {
				if (eachBlockArray.getID().equals(block_ID)) {
					// searching for the entry which is to be asked for its hitsLeft
					return eachBlockArray.getHitsLeft();
				}
			} else {
				// went through all entries (after null there will never be another entry due to the way removeBlock works)
				// since it moves the rest up after removing an entry
				break;
			}
		}
		// Test has to get a NullPointerException so I have to create one by referencing the last entry in blockArray
		// since it will always be null
		return blockArray[160].getHitsLeft();
	}
	public static boolean hasHitsLeft(String block_ID) {
		return getHitsLeft(block_ID) > 0;
	}
	public static void blockExplosion(String block_ID) {
		String[] x_y = block_ID.split("block");
		String[] coordinates = x_y[1].split("_");
		int x = Integer.valueOf(coordinates[0]);
		int y = Integer.valueOf(coordinates[1]);
		String[] blockToHit = new String[4];
		blockToHit[0] = "block" + x + "_" + (y + 1);
		blockToHit[1] = "block" + (x - 1)  + "_" + y;
		blockToHit[2] = "block" + (x + 1) + "_" + y;
		blockToHit[3] = "block" + x + "_" + (y - 1);

		for (String eachBlockToHit : blockToHit) {
			for (BlockEntity eachBlockArray : blockArray) {
				if (eachBlockArray != null && eachBlockArray.getID().equals(eachBlockToHit)) {
					// searching for the entry which is to be hit
					eachBlockArray.hit();
					// stops looking for if it found it
					break;
				}
			}
		}


	}

	// BALLS
	public static boolean ballArrayHasSpace() {
		return ballArray[ballArray.length - 1] == null;
	}
	public static void addBall(BallEntity ball) {
		if (ballArrayHasSpace()) {
			for (int i = 0; i < ballArray.length; i++) {
				if (ballArray[i] == null) {
					ballArray[i] = ball;
					break;
				}
			}
		} else {
			System.err.println("Tried to add a ball to the ballArray even though the maximum supported amount of balls active at one time has been surpassed!");
		}
	}
	public static void removeBall(BallEntity ball) {
		int i;
		for (i = 0; i < ballArray.length; i++) {
			if (ballArray[i] == ball) {
				// searching for the entry which is to be removed
				ballArray[i] = null;
				// stops looking for if it found it
				break;
			}
		}

		for (; i < ballArray.length - 1; i++) {
			// entries below the removed one are moved up one until reaching a null entry
			if (ballArray[i+1] != null) {
				ballArray[i] = ballArray[i + 1];
			} else {
				ballArray[i] = null;
				break;
			}
		}
	}
	// getter
	public static boolean isBallArrayEmpty() {
		return ballArray[0] == null;
	}
	public static BallEntity getLowestDownMovingBall() {
		BallEntity toReturn = null;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				if (toReturn == null && eachBallArray.getSpeedUp() < 0) {
					toReturn = eachBallArray;
				} else if (toReturn != null && eachBallArray.getSpeedUp() < 0 && toReturn.getPosition().y < eachBallArray.getPosition().y) {
					toReturn = eachBallArray;
				}
			} else break;
		}
		return toReturn;
	}
	public static BallEntity getHighestUpMovingBall() {
		BallEntity toReturn = null;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				if (toReturn == null && eachBallArray.getSpeedUp() > 0) {
					toReturn = eachBallArray;
				} else if (toReturn != null && eachBallArray.getSpeedUp() > 0 && toReturn.getPosition().y > eachBallArray.getPosition().y) {
					toReturn = eachBallArray;
				}
			} else break;
		}

		return toReturn;
	}
	// actions
	public static void allBallsLevelComplete() {
		while (ballArray[0] != null) {
			ballArray[0].levelComplete();
		}
	}
	public static void destroyAllBalls() {
		while (ballArray[0] != null) {
			ballArray[0].destroyBall();
		}
	}
	public static void destroyFirstBall() {
		if (ballArray[0] != null) {
			ballArray[0].destroyBall();
		}
	}
	public static void destroyRandomBall() {
		int amountOfBalls = 0;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		if (amountOfBalls > 0) {
			int ballToDestroy = (int) (Math.random() * amountOfBalls);
			ballArray[ballToDestroy].destroyBall();
		}
	}
	public static void duplicateAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballArray[i].duplicateBall();
		}
	}
	public static void max_speedAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballArray[i].max_speedBall();
		}
	}
	public static void min_speedAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallArray : ballArray) {
			if (eachBallArray != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballArray[i].min_speedBall();
		}
	}

	// STICKS
	public static boolean stickArrayHasSpace() {
		return stickArray[stickArray.length - 1] == null;
	}
	public static void addStick(StickEntity stick) {
		if (stickArrayHasSpace()) {
			for (int i = 0; i < stickArray.length; i++) {
				if (stickArray[i] == null) {
					stickArray[i] = stick;
					break;
				}
			}
			addCollidable(stick);
		} else {
			System.err.println("Tried to add a stick to the stickArray even though the maximum supported amount of sticks active at one time has been surpassed!");
		}
	}
	public static void removeStick(StickEntity stick) {
		int i;
		for (i = 0; i < stickArray.length; i++) {
			if (stickArray[i] == stick) {
				// searching for the entry which is to be removed
				stickArray[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		for (; i < stickArray.length - 1; i++) {
			// entries below the removed one are moved up one until reaching a null entry
			if (stickArray[i+1] != null) {
				stickArray[i] = stickArray[i + 1];
			} else {
				stickArray[i] = null;
				break;
			}
		}
		removeCollidable(stick);
	}
	// getter
	public static StickEntity[] getStickArray() {
		return stickArray;
	}
	// actions
	public static void destroyAllSticks() {
		while (stickArray[0] != null) {
			stickArray[0].destroyStick();
		}
	}
	public static void destroyBotSticks() {
		for (int i = 0; i < stickArray.length; i++) {
			if (stickArray[i] != null) {
				if (stickArray[i].getID().equals("botStick")) {
					// searching for a botStick
					stickArray[i].destroyStick();
					// destroying a stick removes it from the Array too. therefore check the same Index again
					i--;
				}
			} else {
				break;
			}
		}
	}
	public static void readdIndicators() {
		for (StickEntity eachStickArray : stickArray) {
			if (eachStickArray != null) {
				eachStickArray.readdIndicators();
			} else {
				break;
			}
		}
	}
	public static void activateAllBots() {
		for (StickEntity eachStickArray : stickArray) {
			if (eachStickArray != null) {
				eachStickArray.activateBot();
			} else {
				break;
			}
		}
	}
	public static void deactivateAllBots() {
		for (StickEntity eachStickArray : stickArray) {
			if (eachStickArray != null) {
				eachStickArray.deactivateBot();
			} else {
				break;
			}
		}
	}
	public static void switchAllBots() {
		for (StickEntity eachStickArray : stickArray) {
			if (eachStickArray != null) {
				eachStickArray.switchBot();
			} else {
				break;
			}
		}
	}

	// BORDERS
	public static boolean borderArrayHasSpace() {
		return borderArray[borderArray.length - 1] == null;
	}
	public static void addBorder(Entity border) {
		if (stickArrayHasSpace()) {
			for (int i = 0; i < borderArray.length; i++) {
				if (borderArray[i] == null) {
					borderArray[i] = border;
					break;
				}
			}
			addCollidable(border);
		} else {
			System.err.println("Tried to add a border to the borderArray even though the maximum supported amount of borders active at one time has been surpassed!");
		}
	}
	public static void removeBorder(Entity border) {
		int i;
		for (i = 0; i < borderArray.length; i++) {
			if (borderArray[i] == border) {
				// searching for the entry which is to be removed
				borderArray[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		for (; i < borderArray.length - 1; i++) {
			// entries below the removed one are moved up one until reaching a null entry
			if (borderArray[i+1] != null) {
				borderArray[i] = borderArray[i + 1];
			} else {
				borderArray[i] = null;
				break;
			}
		}
		removeCollidable(border);
	}
	// getter
	public static Entity[] getBorderArray() {
		return borderArray;
	}

	// COLLIDABLES
	public static void addCollidable(Entity collidable) {
		if (stickArrayHasSpace()) {
			for (int i = 0; i < collidablesArray.length; i++) {
				if (collidablesArray[i] == null) {
					collidablesArray[i] = collidable;
					break;
				}
			}
		} else {
			System.err.println("Tried to add a collidable to the collidablesArray even though the maximum supported amount of collidables active at one time has been surpassed!");
		}
	}
	public static void removeCollidable(Entity collidable) {
		int i;
		for (i = 0; i < collidablesArray.length; i++) {
			if (collidablesArray[i] == collidable) {
				// searching for the entry which is to be removed
				collidablesArray[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		for (; i < collidablesArray.length - 1; i++) {
			// entries below the removed one are moved up one until reaching a null entry
			if (collidablesArray[i+1] != null) {
				collidablesArray[i] = collidablesArray[i + 1];
			} else {
				collidablesArray[i] = null;
				break;
			}
		}
	}
	// getter
	public static Entity[] getCollidablesArray() {
		return collidablesArray;
	}

}
