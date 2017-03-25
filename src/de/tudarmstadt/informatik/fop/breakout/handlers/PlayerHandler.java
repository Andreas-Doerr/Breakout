package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;

/**
 * Created by PC - Andreas on 14.03.2017.
 *
 * @author Andreas Dörr
 */	//TODO commenting
public class PlayerHandler {
	// parameter init
	private static int lives = GameParameters.INITIAL_LIVES;
	private static int points = 0;

	// getter
	public static int getLives() {
		return lives;
	}
	public static int getPoints() {
		return points;
	}

	// setter
	public static void setLives(int newLives) {
		lives = newLives;
	}

	// adding / subtracting
	public static void addLives(int value) {
		lives += value;
	}
	public static void subtractOneLife() {
		lives--;
	}
	public static void addPoints(int added) {
		points += added;
	}
	// resetter
	public static void reset() {
		lives = GameParameters.INITIAL_LIVES;
		points = 0;
		ItemHandler.setItemsActive(0);
		LevelHandler.resetCounter();
	}

}
