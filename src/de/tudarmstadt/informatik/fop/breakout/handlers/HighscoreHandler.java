package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by PC - Andreas on 21.03.2017.
 *
 * @author Andreas Dörr
 */
public class HighscoreHandler {

	public static int maxHighscoreCount = Constants.MAX_HIGHSCORES;

	private static int highscoreCount0 = 0;
	private static int highscoreCount1 = 0;
	private static String[][] names =  new String[2][maxHighscoreCount];
	private static int[][] desBlocks =  new int[2][maxHighscoreCount];
	private static Long[][] time =  new Long[2][maxHighscoreCount];
	private static int[][] points = new int[2][maxHighscoreCount];

	public static int getHighscoreCount() {
		if (OptionsHandler.getGameMode() == 0) {
			return highscoreCount0;
		} else if (OptionsHandler.getGameMode() == 1) {
			return highscoreCount1;
		}
		return 0;
	}

	public static void reset() {
		highscoreCount0 = 0;
		highscoreCount1 = 0;
		names =  new String[2][maxHighscoreCount];
		desBlocks =  new int[2][maxHighscoreCount];
		time =  new Long[2][maxHighscoreCount];
		points = new int[2][maxHighscoreCount];
	}

	public static void readHighscore() {
		String[] highscoreContent = new String[2];
		for (int gm = 0; gm < 2; gm++) {
			int i = 0;
			try {
				highscoreContent = FileHandler.read(Constants.HIGHSCORE_FOLDER + Constants.HIGHSCORE_FILE + gm + Constants.HIGHSCORE_FILE_ENDING, maxHighscoreCount);
				if (gm == 0) {
					highscoreCount0 = Integer.valueOf(highscoreContent[0]);
				} else if (gm == 1) {
					highscoreCount1 = Integer.valueOf(highscoreContent[0]);
				}
				i++;
				for (; i < maxHighscoreCount && highscoreContent[i] != null; i++) {
					String[] entryContent = highscoreContent[i].split(",");
					names[gm][i - 1] = entryContent[0];
					desBlocks[gm][i - 1] = Integer.valueOf(entryContent[1]);
					time[gm][i - 1] = Long.valueOf(entryContent[2]);
					points[gm][i - 1] = Integer.valueOf(entryContent[3]);
				}
			} catch (FileNotFoundException fnfE) {
				System.err.println("ERROR: Could not find highscore-file at: " + Constants.HIGHSCORE_FOLDER + Constants.HIGHSCORE_FILE + gm + Constants.HIGHSCORE_FILE_ENDING);
				System.out.println("INFO: Saving empty highscore.hsc-file to: " + Constants.HIGHSCORE_FOLDER + Constants.HIGHSCORE_FILE + gm + Constants.HIGHSCORE_FILE_ENDING);
				saveHighscore();
			} catch (IOException ioE) {
				System.err.println("ERROR: Could not read highscore file.");
				ioE.printStackTrace();
			} catch (NumberFormatException nfE) {
				System.err.println("ERROR: There is a NumberFormatException in the Line: " + highscoreContent[i]);
			}
		}
	}

	public static void saveHighscore() {
		String entriesToWrite = "";

		int gameMode = OptionsHandler.getGameMode();

		String highscoreFile = Constants.HIGHSCORE_FOLDER + Constants.HIGHSCORE_FILE + OptionsHandler.getGameMode() + Constants.HIGHSCORE_FILE_ENDING;

		for (int i = 0; i < getHighscoreCount(); i++) {
			entriesToWrite += "# ENTRY " + i + "\n" +
					names[gameMode][i] + "," + desBlocks[gameMode][i] + "," + time[gameMode][i] + "," + points[gameMode][i] + "\n";
		}
		String toWrite = "### This is the highscore List ###\n#" +
				"\n## all the highscores are saved here ##\n#" +
				"# highscoreCount:\n" + getHighscoreCount() +
				"\n#\n### LIST ### \n" + entriesToWrite;

		FileHandler.write(highscoreFile, toWrite);
	}

	public static int addHighscore(String newName, int newDesBlocks, long newTime, int newPoints) {
		int gameMode = OptionsHandler.getGameMode();
		int iteration = 0;
		while (iteration <= getHighscoreCount() && newPoints < points[gameMode][iteration]) {
			iteration++;
		}
		while (iteration <= getHighscoreCount() && newPoints == points[gameMode][iteration] && newDesBlocks < desBlocks[gameMode][iteration]) {
			iteration++;
		}
		while (newPoints == points[gameMode][iteration] && newDesBlocks == desBlocks[gameMode][iteration] && time[gameMode][iteration] != null && newTime > time[gameMode][iteration]) {
			iteration++;
		}

		moveDown(iteration);

		names[gameMode][iteration] = newName;
		desBlocks[gameMode][iteration] = newDesBlocks;
		time[gameMode][iteration] = newTime;
		points[gameMode][iteration] = newPoints;

		if (getHighscoreCount() < maxHighscoreCount) {
			if (OptionsHandler.getGameMode() == 0) {
				highscoreCount0++;
			} else if (OptionsHandler.getGameMode() == 1) {
				highscoreCount1++;
			}
		}

		saveHighscore();

		return iteration;
	}

	private static void moveDown(int startValue) {
		// moves the content of names, desBlocks, time and points up one in the index
		// example: the content of names[4] is now the content of names[5] (if startValue >= 4)
		// thereby deleting the one at the bottom of the list
		// keeping the one at startValue and all with indexes lower than that the same (the one at index startValue will be overwritten in addHighscore() )
		int gameMode = OptionsHandler.getGameMode();
		for (int i = maxHighscoreCount - 1; i > startValue; i--) {
			names[gameMode][i] = names[gameMode][i - 1];
			desBlocks[gameMode][i] = desBlocks[gameMode][i - 1];
			time[gameMode][i] = time[gameMode][i - 1];
			points[gameMode][i] = points[gameMode][i - 1];
		}
	}

	public static String getNameAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < getHighscoreCount()) {
			return names[OptionsHandler.getGameMode()][pos];
		} else {
			return null;
		}
	}
	public static int getDesBlocksAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < getHighscoreCount()) {
			return desBlocks[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}
	public static long getTimeElapsedAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < getHighscoreCount()) {
			return time[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}
	public static int getPointsAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < getHighscoreCount()) {
			return points[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}

}
