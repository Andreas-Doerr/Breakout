package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.BlockEntity;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by PC - Andreas on 03.03.2017.
 *
 * @author Andreas Dörr
 */
public class LevelHandler {
	// COUNTER
	private static int activeBlocks = 0;
	private static int destroyedBlocks = 0;
	private static int activeBallCount = 0;
	private static int activeDestroyedBallCount = 0;

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

	// adding / subtracting
	public static void addOneDestroyedBlock() {
		destroyedBlocks++;
	}

	public static void addActiveBalls(int addedBalls) {
		activeBallCount += addedBalls;
	}

    public static void increaseActiveDestroyedBall() {
        activeDestroyedBallCount++;
    }

    public static void decreaceActiveDestroyedBallCount() {
        activeDestroyedBallCount--;
    }

	public static void addActiveBlocks(int addedBlocks) {
		activeBlocks += addedBlocks;
	}

	// resetter
	static void resetCounter() {
		activeBlocks = 0;
		destroyedBlocks = 0;
		activeBallCount = 0;
		activeDestroyedBallCount = 0;
		EntityHandler.resetEntityArrays();
	}

	// MAP
	public static void switchMap() {
		if (OptionsHandler.getSelectedMap() < OptionsHandler.getAmountOfMaps() - 1) {
			OptionsHandler.setSelectedMap(OptionsHandler.getSelectedMap() + 1);
		} else {
			OptionsHandler.setSelectedMap(0);
		}
	}

	private static String[][] readMap(String ref) {
		// returns a 2D Array with the content of the referenced map (map[y][x])
		String[][] map = new String[16][10];
		try {
			String[] lines = FileHandler.read(ref);

			for (int y = 0; y < 10; y++) {
				map[y] = lines[y].split(",");
			}

			return map;
		} catch (FileNotFoundException fnfE) {
			System.err.println("ERROR: Could not find map-file: " + ref);
			initNextMap();
		} catch (ArrayIndexOutOfBoundsException aioobE) {
			System.err.println("ERROR: Corrupted map-file: " + ref + " Wrong amount of lines ");
			aioobE.printStackTrace();
		} catch (IOException ioE) {
			System.err.println("ERROR: Could not read options file.");
			ioE.printStackTrace();
		}
		return null;
	}

	public static void initMapLevel() {
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
	}

	private static void initNextMap() {
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
						float pos_x = (((x + 1) * Constants.BLOCK_IMAGE_X + Constants.BLOCK_IMAGE_X / 2) * Variables.BLOCK_SCALE);
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
