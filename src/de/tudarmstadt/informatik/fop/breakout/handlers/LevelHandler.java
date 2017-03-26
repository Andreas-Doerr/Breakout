package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.Action;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.action.basicactions.MoveUpAction;
import eea.engine.action.basicactions.RotateLeftAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.io.*;

/**
 * Created by PC - Andreas on 03.03.2017.
 *
 * @author Andreas Dörr
 */	//TODO commenting
public class LevelHandler {

	private static int activeBlocks = 0;
	private static int destroyedBlocks = 0;
	private static int activeBallCount = 0;
	private static int activeDestroyedBallCount = 0;

	private static BlockEntity[] blockList = new BlockEntity[160 + 1];	// meaning max is 160 since the last entry stays null
	private static BallEntity[] ballList = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS + 1];	// meaning max is 100 since the last entry stays null
	private static StickEntity[] stickList = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS + 1];	// meaning max is 10 since the last entry stays null

	// getter
	public static int getActiveBlocks() {
		return activeBlocks;
	}
	public static int getDestroyedBlocks() {
		return destroyedBlocks;
	}
	public static int getActiveBallCount() {
		return activeBallCount;
	}
	public static int getActiveDestroyedBallCount() {
		return activeDestroyedBallCount;
	}

	// setter
	public static void setActiveBlocks(int newLevelInitBlocks) {
		activeBlocks = newLevelInitBlocks;
	}

	// adding / subtracting
	public static void addOneDestroyedBlock() {
		destroyedBlocks++;
	}
	public static void addActiveBalls(int addedBalls) {
		activeBallCount += addedBalls;
	}
	public static void addActiveDestroyedBall(int addedDestroyedBalls) {
		activeDestroyedBallCount += addedDestroyedBalls;
	}
	public static void addActiveBlocks(int addedBlocks) {
		activeBlocks += addedBlocks;
	}

	// resetter
	public static void resetCounter() {
		activeBlocks = 0;
		destroyedBlocks = 0;
		activeBallCount = 0;
		activeDestroyedBallCount = 0;
		resetEntityLists();
	}
	public static void resetEntityLists() {
		blockList = new BlockEntity[160 + 1];	// meaning max is 160 since the last entry stays null
		ballList = new BallEntity[Constants.MAX_AMOUNT_OF_BALLS + 1];	// meaning max is 100 since the last entry stays null
		stickList = new StickEntity[Constants.MAX_AMOUNT_OF_STICKS + 1];	// meaning max is 10 since the last entry stays null
	}


	// entity lists

	// block entity list
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
		int toReturn = -1;
		for (BlockEntity eachBlockList : blockList) {
			if (eachBlockList != null) {
				if (eachBlockList.getID().equals(block_ID)) {
					// searching for the entry which is to be asked for its hitsLeft
					toReturn = eachBlockList.getHitsLeft();
					// stops looking for if it found it
					break;
				}
			} else {
				// went through all entries (after null there will never be another entry due to the way removeBlock works)
				// since it moves the rest up after removing an entry
				break;
			}
		}
		if (toReturn != -1) {
			return toReturn;
		} else {
			// Test has to get a NullPointerException so I have to create one by referencing the last entry in blockList
			// since it will always be null
			return blockList[160].getHitsLeft();
		}

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

	// ball entity list
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
	// animation
	public static void animateDestruction(Vector2f pos) {
		// animate the destruction

		LevelHandler.addActiveDestroyedBall(1);

		SoundHandler.playDestroyBall();

		// create the destroyed ball
		Entity destroyedBall = new Entity(Constants.DESTROYED_BALL_ID);    // entity
		destroyedBall.setPosition(pos);    // starting position
		destroyedBall.setScale(Variables.BLOCK_SCALE * 4 * 0.7f);
		destroyedBall.setRotation((float) (Math.random() * 360));    // starting rotation
		if (!Breakout.getDebug()) {
			// only if not in debug-mode
			try {
				// add the image of the destroyed ball
				destroyedBall.addComponent(new ImageRenderComponent(new Image(ThemeHandler.DESTROYED_BALL)));
			} catch (SlickException e) {
				System.err.println("Cannot find file " + ThemeHandler.DESTROYED_BALL);
				e.printStackTrace();
			}
		}

		// giving StateBasedEntityManager the destroyedBall-entity
		StateBasedEntityManager.getInstance().addEntity(Constants.GAMEPLAY_STATE, destroyedBall);

		// movement for the destroyed ball
		LoopEvent destroyedLoop = new LoopEvent();
		if (OptionsHandler.getGameMode() == 0) {
			destroyedLoop.addAction(new MoveDownAction(Variables.INITIAL_BALL_SPEED_UP / 2));
		} else if (OptionsHandler.getGameMode() == 1) {
			if (pos.y > Variables.WINDOW_HEIGHT / 2) {
				destroyedLoop.addAction(new MoveDownAction(Variables.INITIAL_BALL_SPEED_UP / 2));
			} else {
				destroyedLoop.addAction(new MoveUpAction(Variables.INITIAL_BALL_SPEED_UP / 2));
			}
		}

		destroyedLoop.addAction(new RotateLeftAction(0.3f));
		// remove the destroyedBall after it left the screen and reduce the counter for destroyedBalls in play
		destroyedLoop.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (destroyedBall.getPosition().y > Variables.WINDOW_HEIGHT + destroyedBall.getSize().y
						|| destroyedBall.getPosition().y < (- destroyedBall.getSize().y)
						|| destroyedBall.getPosition().x > (Variables.WINDOW_WIDTH + destroyedBall.getSize().x)
						|| destroyedBall.getPosition().x < (- destroyedBall.getSize().x)) {
					StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, destroyedBall);
					LevelHandler.addActiveDestroyedBall(-1);
				}
			}
		});
		destroyedBall.addComponent(destroyedLoop);
	}

	// stick entity list
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
	public static void destroyAllSticksExceptFirst() {
		while (stickList[1] != null) {
			stickList[1].destroyStick();
		}
	}
	public static void destroyFirstStick() {
		if (stickList[0] != null) {
			stickList[0].destroyStick();
		}
	}
	// TODO destroy all Bot-Sticks
	public static void destroyBotSticks() {
		for (int i = 0; i < stickList.length; i++) {
			if (stickList[i] != null) {
				if (stickList[i].getID().equals("botStick")) {
					// searching for a botStick
					stickList[i].destroyStick();
					// destroying a stick removes it from the list too. therefore check the same Index again
					i--;
				}
			}
		}
	}


	// maps
	public static void switchMap() {
		if (OptionsHandler.getSelectedMap() < Constants.MAX_MAPS - 1) {
			OptionsHandler.setSelectedMap(OptionsHandler.getSelectedMap() + 1);
			if (OptionsHandler.getSelectedMapName().equals("placeholder")) {
				switchMap();
			}
		} else {
			OptionsHandler.setSelectedMap(0);
		}
	}
	public static String[][] readMap(String ref) {
		// returns a 2D Array with the content of the referenced map (map[y][x])
		String[][] map = new String[16][10];
		try {
			String[] lines = FileHandler.read(ref, 10);

			for (int y = 0; y < 10; y++) {
				map[y] = lines[y].split(",");
			}

			return map;
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find map-file: " + ref);
			nextMap();
			return null;
		}
	}
	public static void initMapLevel() {
		if (!OptionsHandler.getSelectedMapName().equals("placeholder")) {
			String[][] map = readMap("maps/" + OptionsHandler.getSelectedMapName() + ".map");
			if (map != null) {
				// setting map the middle of the screen for coop-mode
				float y_offset = Constants.BLOCK_IMAGE_Y / 2;
				if (OptionsHandler.getGameMode() == 1) {
					y_offset = (Variables.WINDOW_HEIGHT / 2) + Constants.BLOCK_IMAGE_Y * 2;
				}

				for (int y = 0; y < 10; y++) {
					for (int x = 0; x < 16; x++) {
						// now x and y are the coordinates of the block
						// and parts[x] is the value in the map
						int hitsLeft = Integer.valueOf(map[y][x]);
						if (hitsLeft > 0) {
							String ID = "block" + x + "_" + y;
							float pos_x = (((x + 1) * Constants.BLOCK_IMAGE_X + Constants.BLOCK_IMAGE_X / 2) * Variables.BLOCK_SCALE);
							float pos_y = (((y + 1) * Constants.BLOCK_IMAGE_Y + y_offset) * Variables.BLOCK_SCALE);

							new BlockEntity(ID, hitsLeft, pos_x, pos_y);
						}
					}
				}
			}
		} else {
			switchMap();
		}
	}
	public static void nextMap() {
		switchMap();
		initMapLevel();
	}
	public static void initTemplateLevel() {
		String[][] map = readMap(Constants.TEMPLATE_LEVEL);
		if (map != null) {

			for (int y = 0; y < 10; y++) {
				for (int x = 0; x < 16; x++) {
					// now x and y are the coordinates of the block
					// and parts[x] is the value in the map
					int hitsLeft = Integer.valueOf(map[y][x]);
					if (hitsLeft > 0) {
						String ID = "block" + x + "_" + y;
						float pos_x = (((x + 1)  * Constants.BLOCK_IMAGE_X + Constants.BLOCK_IMAGE_X / 2) * Variables.BLOCK_SCALE);
						float pos_y = (((y + 1) * Constants.BLOCK_IMAGE_Y + Constants.BLOCK_IMAGE_Y / 2) * Variables.BLOCK_SCALE);

						new BlockEntity(ID, hitsLeft, pos_x, pos_y);
					}
				}
			}
		} else {
			System.err.println("ERROR: Could not create blocks since template-map-file could not be found");
		}
	}

}
