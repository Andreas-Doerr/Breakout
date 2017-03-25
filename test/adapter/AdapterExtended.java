package adapter;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.fop.breakout.handlers.HighscoreHandler;
import de.tudarmstadt.informatik.fop.breakout.interfaces.IHighscoreEntry;

public class AdapterExtended extends Adapter {

  //TODO you probably should declare a highscore object of the "proper type" here...
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtended() {
		super();
	}
	
	/* *************************************************** 
	 * ********************** Highscore ******************
	 * *************************************************** */	
	
	/**
	 * adds a new highscore entry for the player. Note: only the 10 best entries are kept.
	 * 
	 * @param playerName name of the player
	 * @param numberOfDesBlocks number of destroyed blocks
	 * @param elapsedTime time since starting the level/level
	 */
	public void addHighscore(String playerName, int numberOfDesBlocks, long elapsedTime) {
		HighscoreHandler.addHighscore(playerName, numberOfDesBlocks, elapsedTime, 0);
	}	
	
	/**
	 * resets (clears) the highscore list
	 */
	public void resetHighscore() {
		HighscoreHandler.reset();
		HighscoreHandler.maxHighscoreCount = 10;
	}		

	/**
	 * returns all highscore entries as a list
	 * 
	 * @return the list of all current highscore entries
	 */
	public List<? extends IHighscoreEntry> getHighscores() {
    // TODO provide the proper mapping to your code here...
	  return new LinkedList<IHighscoreEntry>();
	}
	
	/**
	 * returns the number of entries in the highscore list
	 * @return returns the number of highscore entries - between 0 and 10
	 */
	public int getHighscoreCount() {
	  return HighscoreHandler.getHighscoreCount();
	}
	
	/**
	 * returns the name of the player at the given position of the highscore table
	 * 
	 * @param position the position in the list, should be inside 
	 * the interval [0, max(9, getHighscoreCount() - 1)]
	 * @return the name of the player at the given position or null if the index is invalid
	 * (negative, greater than 9 and/or greater than or equal to the entry count)
	 */
	public String getNameAtHighscorePosition(int position) {
	  return HighscoreHandler.getNameAtHighscorePosition(position);
	}
	
	/**
	 * return the time since starting this level for the highscore entry at the given position
	 * 
   * @param position the position in the list, should be inside 
   * the interval [0, max(9, getHighscoreCount() - 1)]
	 * @return the time elapsed for the given highscore entry if this exists; otherwise -1
	 */
	public int getTimeElapsedAtHighscorePosition(int position) {
		return (int) HighscoreHandler.getTimeElapsedAtHighscorePosition(position);
	}
	
  /**
   * return the number of blocks destroyed by highscore entry at the given position
   * 
   * @param position the position in the list, should be inside 
   * the interval [0, max(9, getHighscoreCount() - 1)]
   * @return the number of blocks destroyed for the given highscore entry if this exists; otherwise -1
   */
	public int getNumberOfDesBlocksAtHighscorePosition(int position) {
		return HighscoreHandler.getDesBlocksAtHighscorePosition(position);
	}
	
}
