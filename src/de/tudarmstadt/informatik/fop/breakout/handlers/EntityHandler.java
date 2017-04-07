package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.BallEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.BlockEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.StickEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;

/**
 * Created by PC - Andreas on 07.04.2017.
 *
 * @author Andreas Dörr
 */
public class EntityHandler {

	// LISTS
	private static BlockEntity[] blockList = new BlockEntity[160 + 1];	// meaning max is 160 since the last entry stays null
	private static BallEntity[] ballList = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS + 1];	// meaning max is 100 since the last entry stays null
	private static StickEntity[] stickList = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS + 1];	// meaning max is 10 since the last entry stays null

	// resetter
	public static void resetEntityLists() {
		blockList = new BlockEntity[160 + 1];	// meaning max is 160 since the last entry stays null
		ballList = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS + 1];	// meaning max is 100 since the last entry stays null
		stickList = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS + 1];	// meaning max is 10 since the last entry stays null
	}

	// ENTITY LISTS
// BLOCK
	public static boolean blockListHasSpace() {
		return blockList[blockList.length - 1] == null;
	}
	public static void addBlock(BlockEntity block) {
		if (blockListHasSpace()) {
			for (int i = 0; i < blockList.length; i++) {
				if (blockList[i] == null) {
					blockList[i] = block;
					break;
				}
			}
		} else {
			System.err.println("Tried to add a block to the blockList even though the maximum supported amount of blocks active at one time has been surpassed!");
		}
	}
	public static void removeBlock(BlockEntity block) {
		int i;
		for (i = 0; i < blockList.length; i++) {
			if (blockList[i] == block) {
				// searching for the entry which is to be removed
				blockList[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		if (i < blockList.length - 1) {

			for (; i < blockList.length - 1; i++) {
				// from the entry which got removed to the second to last one (if you look at i as their index)
				// all entries below the removed one are moved up one
				blockList[i] = blockList[i + 1];
			}
			// the last entry is set to null
			blockList[blockList.length - 1] = null;
		}
	}
	// getter
	public static BlockEntity getMostLeftLowestBlock() {
		BlockEntity toReturn = null;
		if (blockList[0] != null) {
			toReturn = blockList[0];
			for (BlockEntity eachBlockList : blockList) {
				if (eachBlockList != null) {
					if (eachBlockList.getPosition().y > toReturn.getPosition().y) {
						toReturn = eachBlockList;
					} else if (eachBlockList.getPosition().y >= toReturn.getPosition().y && eachBlockList.getPosition().x < toReturn.getPosition().x) {
						toReturn = eachBlockList;
					} else break;
				}
			}
		}
		return toReturn;
	}
	// actions
	public static void destroyRandomBlock() {
		int amountOfBlocks = 0;
		for (BlockEntity eachBlockList : blockList) {
			if (eachBlockList != null) {
				amountOfBlocks++;
			} else {
				break;
			}
		}
		if (amountOfBlocks > 0) {
			int blockToDestroy = (int) (Math.random() * amountOfBlocks);
			blockList[blockToDestroy].destroyBlock();
		}
	}
	public static void destroyAllBlocks() {
		while (blockList[0] != null) {
			blockList[0].destroyBlock();
		}
	}
	public static int getHitsLeft(String block_ID) throws NullPointerException {
		for (BlockEntity eachBlockList : blockList) {
			if (eachBlockList != null) {
				if (eachBlockList.getID().equals(block_ID)) {
					// searching for the entry which is to be asked for its hitsLeft
					return eachBlockList.getHitsLeft();
				}
			} else {
				// went through all entries (after null there will never be another entry due to the way removeBlock works)
				// since it moves the rest up after removing an entry
				break;
			}
		}
		// Test has to get a NullPointerException so I have to create one by referencing the last entry in blockList
		// since it will always be null
		return blockList[160].getHitsLeft();
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
			for (BlockEntity eachBlockList : blockList) {
				if (eachBlockList != null && eachBlockList.getID().equals(eachBlockToHit)) {
					// searching for the entry which is to be hit
					eachBlockList.hit();
					// stops looking for if it found it
					break;
				}
			}
		}


	}

	// BALL
	public static boolean ballListHasSpace() {
		return ballList[ballList.length - 1] == null;
	}
	public static void addBall(BallEntity ball) {
		if (ballListHasSpace()) {
			for (int i = 0; i < ballList.length; i++) {
				if (ballList[i] == null) {
					ballList[i] = ball;
					break;
				}
			}
		} else {
			System.err.println("Tried to add a ball to the ballList even though the maximum supported amount of balls active at one time has been surpassed!");
		}
	}
	public static void removeBall(BallEntity ball) {
		int i;
		for (i = 0; i < ballList.length; i++) {
			if (ballList[i] == ball) {
				// searching for the entry which is to be removed
				ballList[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		if (i < ballList.length - 1) {

			for (; i < ballList.length - 1; i++) {
				// from the entry which got removed to the second to last one (if you look at i as their index)
				// all entries below the removed one are moved up one
				ballList[i] = ballList[i + 1];
			}
			// the last entry is set to null
			ballList[ballList.length - 1] = null;
		}
	}
	// getter
	public static BallEntity[] getBallList() {
		return ballList;
	}
	public static boolean isBallListEmpty() {
		return ballList[0] == null;
	}
	public static BallEntity getLowestDownMovingBall() {
		BallEntity toReturn = null;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				if (toReturn == null && eachBallList.getSpeedUp() < 0) {
					toReturn = eachBallList;
				} else if (toReturn != null && eachBallList.getSpeedUp() < 0 && toReturn.getPosition().y < eachBallList.getPosition().y) {
					toReturn = eachBallList;
				}
			} else break;
		}
		return toReturn;
	}
	public static BallEntity getHighestUpMovingBall() {
		BallEntity toReturn = null;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				if (toReturn == null && eachBallList.getSpeedUp() > 0) {
					toReturn = eachBallList;
				} else if (toReturn != null && eachBallList.getSpeedUp() > 0 && toReturn.getPosition().y > eachBallList.getPosition().y) {
					toReturn = eachBallList;
				}
			} else break;
		}

		return toReturn;
	}
	// actions
	public static void allBallsLevelComplete() {
		while (ballList[0] != null) {
			ballList[0].levelComplete();
		}
	}
	public static void destroyAllBalls() {
		while (ballList[0] != null) {
			ballList[0].destroyBall();
		}
	}
	public static void destroyFirstBall() {
		if (ballList[0] != null) {
			ballList[0].destroyBall();
		}
	}
	public static void destroyRandomBall() {
		int amountOfBalls = 0;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		if (amountOfBalls > 0) {
			int ballToDestroy = (int) (Math.random() * amountOfBalls);
			ballList[ballToDestroy].destroyBall();
		}
	}
	public static void duplicateAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballList[i].duplicateBall();
		}
	}
	public static void max_speedAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballList[i].max_speedBall();
		}
	}
	public static void min_speedAllBalls() {
		int amountOfBalls = 0;
		for (BallEntity eachBallList : ballList) {
			if (eachBallList != null) {
				amountOfBalls++;
			} else {
				break;
			}
		}
		for (int i = 0; i < amountOfBalls; i++) {
			ballList[i].min_speedBall();
		}
	}

	// STICK
	public static boolean stickListHasSpace() {
		return stickList[stickList.length - 1] == null;
	}
	public static void addStick(StickEntity stick) {
		if (stickListHasSpace()) {
			for (int i = 0; i < stickList.length; i++) {
				if (stickList[i] == null) {
					stickList[i] = stick;
					break;
				}
			}
		} else {
			System.err.println("Tried to add a stick to the stickList even though the maximum supported amount of sticks active at one time has been surpassed!");
		}
	}
	public static void removeStick(StickEntity stick) {
		int i;
		for (i = 0; i < stickList.length; i++) {
			if (stickList[i] == stick) {
				// searching for the entry which is to be removed
				stickList[i] = null;
				// stops looking for if it found it
				break;
			}
		}
		if (i < stickList.length - 1) {

			for (; i < stickList.length - 1; i++) {
				// from the entry which got removed to the second to last one (if you look at i as their index)
				// all entries below the removed one are moved up one
				stickList[i] = stickList[i + 1];
			}
			// the last entry is set to null
			stickList[stickList.length - 1] = null;
		}
	}
	// actions
	public static void destroyAllSticks() {
		while (stickList[0] != null) {
			stickList[0].destroyStick();
		}
	}
	public static void destroyBotSticks() {
		for (int i = 0; i < stickList.length; i++) {
			if (stickList[i] != null) {
				if (stickList[i].getID().equals("botStick")) {
					// searching for a botStick
					stickList[i].destroyStick();
					// destroying a stick removes it from the list too. therefore check the same Index again
					i--;
				}
			} else {
				break;
			}
		}
	}
	public static void readdIndicators() {
		for (StickEntity eachStickList : stickList) {
			if (eachStickList != null) {
				eachStickList.readdIndicators();
			} else {
				break;
			}
		}
	}
	public static void activateAllBots() {
		for (StickEntity eachStickList : stickList) {
			if (eachStickList != null) {
				eachStickList.activateBot();
			} else {
				break;
			}
		}
	}
	public static void deactivateAllBots() {
		for (StickEntity eachStickList : stickList) {
			if (eachStickList != null) {
				eachStickList.deactivateBot();
			} else {
				break;
			}
		}
	}
	public static void switchAllBots() {
		for (StickEntity eachStickList : stickList) {
			if (eachStickList != null) {
				eachStickList.switchBot();
			} else {
				break;
			}
		}
	}
}
