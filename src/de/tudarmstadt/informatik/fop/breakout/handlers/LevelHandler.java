package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Variables;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.action.basicactions.MoveUpAction;
import eea.engine.action.basicactions.RotateLeftAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import java.io.*;

/**
 * Created by PC - Andreas on 03.03.2017.
 *
 * @author Andreas Dörr
 */	//TODO commenting
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
	private static void addActiveDestroyedBall(int addedDestroyedBalls) {
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
		EntityHandler.resetEntityLists();
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
		destroyedLoop.addAction((gc, sb, delta, event) -> {
			if (destroyedBall.getPosition().y > Variables.WINDOW_HEIGHT + destroyedBall.getSize().y
					|| destroyedBall.getPosition().y < (- destroyedBall.getSize().y)
					|| destroyedBall.getPosition().x > (Variables.WINDOW_WIDTH + destroyedBall.getSize().x)
					|| destroyedBall.getPosition().x < (- destroyedBall.getSize().x)) {
				StateBasedEntityManager.getInstance().removeEntity(Constants.GAMEPLAY_STATE, destroyedBall);
				LevelHandler.addActiveDestroyedBall(-1);
			}
		});
		destroyedBall.addComponent(destroyedLoop);
	}

// MAP
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
		} catch (FileNotFoundException fnfE) {
			System.err.println("ERROR: Could not find map-file: " + ref);
			nextMap();
			return null;
		} catch (IOException ioE) {
			System.err.println("ERROR: Could not read options file.");
			ioE.printStackTrace();
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
