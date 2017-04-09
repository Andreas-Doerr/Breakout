package adapter;

import de.tudarmstadt.informatik.fop.breakout.engine.entity.BallEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.BlockEntity;
import de.tudarmstadt.informatik.fop.breakout.engine.entity.StickEntity;
import de.tudarmstadt.informatik.fop.breakout.handlers.*;
import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.test.TestAppGameContainer;

public class Adapter implements Constants {
  /*
   * the instance of our game, extends StateBasedGame
   */
	Breakout breakout;
	
	/**
	 * The TestAppGameContainer for running the tests
	 */
	TestAppGameContainer app;
	BallEntity ball;
	StickEntity stick;

	/**
	 * Use this constructor to initialize everything you need.
	 */
	public Adapter() {
		breakout = null;
	}

	/* ***************************************************
	 * ********* initialize, run, stop the game **********
	 * ***************************************************
	 * 
	 * You can normally leave this code as it is.
	 */

	public StateBasedGame getStateBasedGame() {
		return breakout;
	}

	/**
	 * Diese Methode initialisiert das Spiel im Debug-Modus, d.h. es wird ein
	 * AppGameContainer gestartet, der keine Fenster erzeugt und aktualisiert.
	 * 
	 * Sie müssen diese Methode erweitern
	 */
	public void initializeGame() {

    // Set the library path depending on the operating system
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
      System.setProperty("org.lwjgl.librarypath",
          System.getProperty("user.dir") + "/native/windows");
    } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
      System.setProperty("org.lwjgl.librarypath",
          System.getProperty("user.dir") + "/native/macosx");
    } else {
      System.setProperty("org.lwjgl.librarypath",
          System.getProperty("user.dir") + "/native/"
              + System.getProperty("os.name").toLowerCase());
    }

    // Initialize the game in debug mode (no GUI output)
		breakout = new Breakout(true);

		try {
			app = new TestAppGameContainer(breakout);
			Breakout.setApp(app);
			app.start(0);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		ball = new BallEntity(0,0);
		stick = new StickEntity(Constants.PLAYER_STICK_ID, 0, 0, Input.KEY_LEFT, Input.KEY_RIGHT, false, 0);

		PlayerHandler.reset();
		OptionsHandler.readOptions();

		LevelHandler.initTemplateLevel();
	}

	/**
	 * Stop a running game
	 */
	public void stopGame() {
		if (app != null) {
			app.exit();
			app.destroy();
		}
		StateBasedEntityManager.getInstance().clearAllStates();
		breakout = null;
	}

	public void changeToGameplayState() {
		this.getStateBasedGame().enterState(GAMEPLAY_STATE);
		try {
			app.updateGame(1);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void changeToHighScoreState() {
		this.getStateBasedGame().enterState(HIGHSCORE_STATE);
		try {
			app.updateGame(1);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/* ***************************************************
	 * ********************** Ball **********************
	 * ***************************************************
	 */
	
	/**
	 * Returns a new Entity that represents a ball with ID ballID.
	 * It was added for tests, as we do not know what class/package will represent
	 * your "ball" entity.
	 * 
	 * @param ballID the ID for the new ball instance
	 * @return an entity representing a ball with the ID passed in as ballID
	 */
	public Entity createBallInstance(String ballID) {
		BallEntity ball = null;
		if (EntityHandler.ballArrayHasSpace()) {
			ball = new BallEntity(0,0);
		} else {
			System.err.println("The maximum supported amount of balls active at one time has been surpassed!");
		}

		return ball;
	}

	/**
	 * Returns an instance of the IHitable interface that represents a block
	 * with the ID as passed in and the requested number of hits left (1 = next
	 * hit causes the block to vanish, 2 = it takes two hits, ...)
	 * 
	 * @param blockID the ID the returns block entity should have
	 * @param hitsUntilDestroyed the number of hits (> 0) the block should have left
	 * before it vanishes (1 = vanishes with next touch by ball)
	 * @return an entity representing a block with the given ID and hits left
	 */
	public BlockEntity createBlockInstance(String blockID, int hitsUntilDestroyed) {
	  return new BlockEntity(blockID, hitsUntilDestroyed, 0, 0);
	}

	/**
	 * sets the ball's orientation (angle). 
	 * Note: the name of the method is somewhat unfortunate, but is taken from EEA's entity.
	 * 
	 * @param i the new orientation angle for the ball (0...360)
	 */
	public void setRotation(int i) {
		ball.setRotation(i);
	}

  /**
   * returns the ball's orientation (angle). 
   * Note: the name of the method is somewhat unfortunate, but is taken from EEA's entity.
   * 
   * @return the orientation angle for the ball (0...360)
   */
	public float getRotation() {
		return ball.getRotation();
	}

	/**
	 * Sets the ball's position to the coordinate provide
	 * 
	 * @param vector2f the target position for the ball
	 */
	public void setPosition(Vector2f vector2f) {
		ball.setPosition(vector2f);
	}

  /**
   * returns a definition of the ball's size. Typically, the size of the ball will
   * be constant, but programmers may introduce bonus items that shrink or enlarge the ball.
   * 
   * @return the size of the ball
   */
	public Vector2f getSize() {
		return ball.getSize();
	}

	/**
	 * returns the current speed of the ball's movement
	 * 
	 * @return the ball's speed
	 */
	public float getSpeed() {
		return ball.getTotalSpeed();
	}

	/**
	 * sets the current speed of the ball to the given value
	 * 
	 * @param speed the new speed of the ball
	 */
	public void setSpeed(float speed) {
		ball.setTotalSpeed(speed);
	}

	/**
	 * provide a proper code mapping to a check if your ball entity collides with
	 * 'otherEntity'. You will have to access your ball instance for this purpose.
	 * 
	 * @param otherEntity another entity that the ball may (or may not) collide with
	 * 
	 * @return true if the two entities have collided. Note: your ball should by default
	 * not collide with itself (or other balls, if there are any), null, the background,
	 * or "passable" entities (e.g. other image you have placed on the screen). It should only
	 * collide with the stick if the orientation is correct (>90 but <270 degrees, else it would
	 * "collide with the underside of the stick") but should be "gone" then already).
	 * It should also collide with the borders if the orientation is correct for this, e.g.,
	 * only collide with the top border if the orientation is fitting).
	 */
	public boolean collides(Entity otherEntity) {
		boolean doesCollide = false;
		if (otherEntity != null) {
			System.out.println("Test for ball collision with: " + otherEntity.getID());
		}
		if (otherEntity != null && !otherEntity.isPassable()) {
			doesCollide = true;
			System.out.println("ballpos: " + ball.getPosition());
			System.out.println("otherpos: " + otherEntity.getPosition());
			float ball_centerToRight = ball.getSize().x / 2;
			float ball_centerToTop = ball.getSize().y / 2;
			System.out.println("ball: centerToRight: " + ball_centerToRight + " ; centerToTop: " + ball_centerToTop);

			float other_centerToRight = otherEntity.getSize().x / 2;
			float other_centerToTop = otherEntity.getSize().y / 2;
			if (otherEntity.getID().equals(Constants.PLAYER_STICK_ID)) {
				other_centerToRight = 10;
			}
			System.out.println("other: centerToRight: " + other_centerToRight + " ; centerToTop: " + other_centerToTop);

			float x_rel = Math.abs(otherEntity.getPosition().x - ball.getPosition().x);
			float y_rel = Math.abs(otherEntity.getPosition().y - ball.getPosition().y);
			System.out.println("relative position: x = " + x_rel + " y = " + y_rel);

			System.out.println("ball angle: " + ball.getRotation());

			// only corner of balls hitbox collided?
			if (x_rel > (ball_centerToRight + other_centerToRight)) {
				System.out.println("Too far apart horizontally");
				doesCollide = false;
			} else if (y_rel > (ball_centerToTop + other_centerToTop)) {
				System.out.println("Too far apart vertically");
				doesCollide = false;
			}

			// specific entities
			if (otherEntity.getID().equals(Constants.TOP_BORDER_ID) && ball.getRotation() >= 90 && ball.getRotation() <= 270) {
				// top_border
				// not collided when 90 <= angle <= 270
				doesCollide = false;
			} else if (otherEntity.getID().equals(Constants.RIGHT_BORDER_ID) && (ball.getRotation() >= 180 || ball.getRotation() <= 0)) {
				// right_border
				// not collided when 180 <= angle <= 0 / 360
				doesCollide = false;
			} else if (otherEntity.getID().equals(Constants.LEFT_BORDER_ID) && (ball.getRotation() <= 180 || ball.getRotation() >= 360)) {
				// left_border
				// not collided when 0 / 360 <= angle <= 180
				doesCollide = false;
			} else if (otherEntity.getID().equals(Constants.PLAYER_STICK_ID) && (ball.getRotation() >= 270 || ball.getRotation() <= 90)) {
				doesCollide = false;
			}

		}
		System.out.println("Answer: " + doesCollide + "\n");
	  	return doesCollide;
	}

	/* ***************************************************
	 * ********************** Player *********************
	 * ***************************************************
	 */
	
	/**
	 * ensures that the player has "value" additional lives (=additional balls left).
	 * 
	 * @param value the number of additional balls/lives to be added.
   */
	public void addLives(int value) {
		PlayerHandler.addLives(value);
	}

	/**
	 * ensures that the player has exactly "playerLives" balls/lives left.
	 * 
	 * @param playerLives the number of lives/balls the player shall have left
	 */
	public void setLives(int playerLives) {
		PlayerHandler.setLives(playerLives);
	}

	/**
	 * queries your classes for the number of lives/balls the player has left
	 * 
	 * @return the number of lives/balls left
	 */
	public int getLivesLeft() {
	  	return PlayerHandler.getLives();
	}

	/**
	 * checks if the player still has at least one live/ball left
	 * 
	 * @return true if the player still has at least one live/ball left, else false.
	 */
	public boolean hasLivesLeft() {
		return PlayerHandler.getLives() > 0;
	}

	/* ***************************************************
	 * ********************** Block **********************
	 * ***************************************************
	 */

	/**
	 * Returns the number of necessary hits for degrading this block
	 * 
	 * @param blockID
	 *            ID of the chosen block
	 * @return number of hits
	 */
	public int getHitsLeft(String blockID) {
	  return EntityHandler.getHitsLeft(blockID);
	}

	/**
	 * Returns whether the block has hits left
	 * 
	 * @param blockID
	 *            blockID ID of the chosen block
	 * @return true, if block has hits left, else false
	 */
	public boolean hasHitsLeft(String blockID) {
	  return EntityHandler.hasHitsLeft(blockID);
	}

	/* ***************************************************
	 * ********************** Stick **********************
	 * ***************************************************
	 */
	
	/**
	 * returns the current position of the stick
	 * 
	 * @return the current position of the stick
	 */
	public Vector2f getStickPosition() {
	  return stick.getPosition(); // these are arbitrary values(!)
	}

	/* ***************************************************
	 * ********************** Input **********************
	 * ***************************************************
	 */

	/**
	 * This Method should emulate the key down event.
	 * 
	 * @param updatetime
	 *            : Zeitdauer bis update-Aufruf
	 * @param input
	 *            : z.B. Input.KEY_K, Input.KEY_L
	 */
	public void handleKeyDown(int updatetime, Integer input) {
		for (int i = 0; i < updatetime; i++) {
			if (input == Input.KEY_LEFT) {
				System.out.println("left");
				stick.moveStick(-1);
			}
			if (input == Input.KEY_RIGHT) {
				System.out.println("right");
				stick.moveStick(1);
			}
			try {
				app.updateGame(1);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This Method should emulate the pressing of the right arrow key.
	 */
	public void handleKeyDownRightArrow() {
		handleKeyDown(1000, Input.KEY_RIGHT);
	}

	/**
	 * This Method should emulate the pressing of the left arrow key.
	 */
	public void handleKeyDownLeftArrow() {
		handleKeyDown(1000, Input.KEY_LEFT);
	}
}
