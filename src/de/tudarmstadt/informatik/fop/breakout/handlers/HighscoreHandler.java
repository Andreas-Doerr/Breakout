package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;

import java.io.FileNotFoundException;

/**
 * Created by PC - Andreas on 21.03.2017.
 *
 * @author Andreas Dörr
 */
public class HighscoreHandler {

	public static int maxHighscoreCount = Constants.MAX_HIGHSCORES;

	private static int highscoreCount = 0;
	private static String[][] names =  new String[2][maxHighscoreCount];
	private static int[][] desBlocks =  new int[2][maxHighscoreCount];
	private static Long[][] time =  new Long[2][maxHighscoreCount];
	private static int[][] points = new int[2][maxHighscoreCount];

	public static int getHighscoreCount() {
		return highscoreCount;
	}

	public static void reset() {
		highscoreCount = 0;
		names =  new String[2][maxHighscoreCount];
		desBlocks =  new int[2][maxHighscoreCount];
		time =  new Long[2][maxHighscoreCount];
		points = new int[2][maxHighscoreCount];
	}

	public static void readHighscore() {
		String[] highscoreContent = new String[2];
		int i = 0;
		try {
			highscoreContent = FileHandler.read(Constants.HIGHSCORE_FILE_0, maxHighscoreCount);
			highscoreCount = Integer.valueOf(highscoreContent[0]);
			int gameMode = OptionsHandler.getGameMode();
			i++;
			for (;i < maxHighscoreCount && highscoreContent[i] != null; i++) {
				String[] entryContent = highscoreContent[i].split(",");
				names[gameMode][i - 1] = entryContent[0];
				desBlocks[gameMode][i - 1] = Integer.valueOf(entryContent[1]);
				time[gameMode][i - 1] = Long.valueOf(entryContent[2]);
				points[gameMode][i - 1] = Integer.valueOf(entryContent[3]);
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find highscore-file at: " + Constants.HIGHSCORE_FILE_0);
			System.out.println("INFO: Saving empty highscore.hsc-file to: " + Constants.HIGHSCORE_FILE_0);
			saveHighscore();
		} catch (NumberFormatException a) {
			System.err.println("ERROR: There is a NumberFormatException in the Line: " + highscoreContent[i]);
		}
	}

	public static void saveHighscore() {
		String entriesToWrite = "";

		int gameMode = OptionsHandler.getGameMode();

		String highscoreFile = Constants.HIGHSCORE_FILE_0;
		if (OptionsHandler.getGameMode() == 1) {
			highscoreFile = Constants.HIGHSCORE_FILE_1;
		}

		for (int i = 0; i < highscoreCount; i++) {
			entriesToWrite += "# ENTRY " + i + "\n" +
					names[gameMode][i] + "," + desBlocks[gameMode][i] + "," + time[gameMode][i] + "," + points[gameMode][i] + "\n";
		}
		String toWrite = "### This is the highscore List ###\n#" +
				"\n## all the highscores are saved here ##\n#" +
				"# highscoreCount:\n" + highscoreCount +
				"\n#\n### LIST ### \n" + entriesToWrite;

		try {
			FileHandler.write(highscoreFile, toWrite);
		} catch (FileNotFoundException e) {
			System.err.println("Could not find options-file");
		}
	}

	public static int addHighscore(String newName, int newDesBlocks, long newTime, int newPoints) {
		System.out.println("\nAdding player: " + newName);
		int gameMode = OptionsHandler.getGameMode();
		int iteration = 0;
		while (iteration <= highscoreCount && newPoints < points[gameMode][iteration]) {
			iteration++;
		}
		while (iteration <= highscoreCount && newPoints == points[gameMode][iteration] && newDesBlocks < desBlocks[gameMode][iteration]) {
			iteration++;
		}
		while (newPoints == points[gameMode][iteration] && newDesBlocks == desBlocks[gameMode][iteration] && time[gameMode][iteration] != null && newTime > time[gameMode][iteration]) {
			System.out.println("newDesBlocks: " + newDesBlocks + " == desBlocks[gameMode][iteration]: " + desBlocks[gameMode][iteration] + " && newTime: " + newTime + " > time[gameMode][iteration]: " + time[gameMode][iteration]);
			iteration++;
		}

		System.out.println("Ended with: newDesBlocks: " + newDesBlocks + " != desBlocks[gameMode][iteration]: " + desBlocks[gameMode][iteration] + " || newTime: " + newTime + " !> time[gameMode][iteration]: " + time[gameMode][iteration]);

		moveDown(iteration);

		names[gameMode][iteration] = newName;
		desBlocks[gameMode][iteration] = newDesBlocks;
		time[gameMode][iteration] = newTime;
		points[gameMode][iteration] = newPoints;

		if (highscoreCount < maxHighscoreCount) {
			highscoreCount++;
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
		if (pos >= 0 && pos < highscoreCount) {
			return names[OptionsHandler.getGameMode()][pos];
		} else {
			return null;
		}
	}
	public static int getDesBlocksAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < highscoreCount) {
			return desBlocks[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}
	public static long getTimeElapsedAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < highscoreCount) {
			return time[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}
	public static int getPointsAtHighscorePosition(int pos) {
		if (pos >= 0 && pos < highscoreCount) {
			return points[OptionsHandler.getGameMode()][pos];
		} else {
			return -1;
		}
	}

}
