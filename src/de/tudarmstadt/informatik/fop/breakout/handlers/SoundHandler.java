package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * Created by PC - Andreas on 20.03.2017.
 *
 * @author Andreas Dörr
 */
public class SoundHandler {

	// Volume
	private static float mainVolume = 1f;
	// TODO set to half it's value when paused
	// Sounds
	private static Sound hitStick;
	private static Sound hitBorder;
	private static Sound destroyBall;
	private static Sound hitBlock;
	private static Sound createItem;
	private static Sound pickupItem;
	private static Sound victory;
	private static Sound gameover;
	private static Sound buttonPush;
	private static Sound notAcceptable;
	// Music
	private static Sound menuMusic;
	private static Sound gameplayMusic;
	private static Sound highscoreMusic;


	// sound init
	public static void init() {
		try {
			hitStick = new Sound(Constants.SOUND_HIT_STICK);
			hitBorder = new Sound(Constants.SOUND_HIT_BORDER);
			destroyBall = new Sound(Constants.SOUND_DESTROY_BALL);
			hitBlock = new Sound(Constants.SOUND_HIT_BLOCK);
			createItem = new Sound(Constants.SOUND_CREATE_ITEM);
			pickupItem = new Sound(Constants.SOUND_PICKUP_ITEM);
			victory = new Sound(Constants.SOUND_VICTORY);
			gameover = new Sound(Constants.SOUND_GAMEOVER);
			buttonPush = new Sound(Constants.SOUND_BUTTON_PRESS);
			notAcceptable = new Sound(Constants.SOUND_NOT_ACCEPTABLE);  // was BUTTON_PRESS

			menuMusic = new Sound(Constants.MUSIC_MENU);
			gameplayMusic = new Sound(Constants.MUSIC_GAMEPLAY);
			highscoreMusic = new Sound(Constants.MUSIC_HIGHSCORE);
		} catch (SlickException e) {
			System.err.println("Could not initialize sounds. Check if they exist at referenced location.");
			e.printStackTrace();
		}
	}

	// mainVolume
	public static float getMainVolume() {
		return mainVolume;
	}

	public static void setMainVolume(float new_main_volume) {
		mainVolume = new_main_volume;
	}

	// play sounds
	public static void playHitStick(float pitch) {
		try {
			hitStick.play(pitch, mainVolume * 0.3f);
		} catch (NullPointerException e) {
			System.err.println("The sound hitStick is not initialized!");
		}
	}

	public static void playHitBorder() {
		try {
			hitBorder.play(1f, mainVolume * 0.3f);
		} catch (NullPointerException e) {
			System.err.println("The sound hitBorder is not initialized!");
		}
	}

	public static void playHitBlock() {
		try {
			hitBlock.play(1f, mainVolume * 0.3f);
		} catch (NullPointerException e) {
			System.err.println("The sound hitBlock is not initialized!");
		}
	}

	public static void playDestroyBall() {
		try {
			destroyBall.play(1f, mainVolume * 0.4f);
		} catch (NullPointerException e) {
			System.err.println("The sound destroyBall is not initialized!");
		}
	}

	public static void playCreateItem() {
		try {
			createItem.play(1f, mainVolume * 0.4f);
		} catch (NullPointerException e) {
			System.err.println("The sound createItem is not initialized!");
		}
	}

	public static void playPickupItem() {
		try {
			pickupItem.play(1f, mainVolume * 0.4f);
		} catch (NullPointerException e) {
			System.err.println("The sound pickupItem is not initialized!");
		}
	}

	public static void playVictory() {
		try {
			stopMusic();
			victory.play(1f, mainVolume * 0.3f);
		} catch (NullPointerException e) {
			System.err.println("The sound victory is not initialized!");
		}
	}

	public static void playGameover() {
		try {
			stopMusic();
			gameover.play(1f, mainVolume * 0.3f);
		} catch (NullPointerException e) {
			System.err.println("The sound gameover is not initialized!");
		}
	}

	public static void playButtonPress() {
		try {
			buttonPush.play(1f, mainVolume * 0.5f);
		} catch (NullPointerException e) {
			System.err.println("The sound buttonPush is not initialized!");
		}
	}

	public static void playNotAcceptable() {
		try {
			buttonPush.stop();
			notAcceptable.play(1f, mainVolume);
		} catch (NullPointerException e) {
			System.err.println("The sound notAcceptable is not initialized!");
		}
	}

	// is specific sound playing
	public static boolean isVictoryPlaying() {
		try {
			return victory.playing();
		} catch (NullPointerException e) {
			System.err.println("The sound victory is not initialized!");
			return false;
		}
	}

	public static boolean isGameoverPlaying() {
		try {
			return gameover.playing();
		} catch (NullPointerException e) {
			System.err.println("The sound gameover is not initialized!");
			return false;
		}
	}

	// stop sounds
	public static void stopVictoryAndGameover() {
		try {
			victory.stop();
			gameover.stop();
		} catch (NullPointerException e) {
			System.err.println("The sound victory or gameover is not initialized!");
		}
	}

	// loop music
	public static void startMenuMusic() {
		try {
			stopMusic();
			menuMusic.loop(1f, mainVolume * 0.5f);
		} catch (NullPointerException e) {
			System.err.println("The sound menuMusic is not initialized!");
		}
	}

	public static void startGameplayMusic() {
		try {
			stopMusic();
			gameplayMusic.loop(1f, mainVolume * 0.75f);
		} catch (NullPointerException e) {
			System.err.println("The sound gameplayMusic is not initialized!");
		}
	}

	public static void startHighscoreMusic() {
		try {
			stopMusic();
			highscoreMusic.loop(1f, mainVolume * 0.5f);
		} catch (NullPointerException e) {
			System.err.println("The sound highscoreMusic is not initialized!");
		}
	}

	// stop music
	public static void stopMusic() {
		try {
			menuMusic.stop();
			gameplayMusic.stop();
			highscoreMusic.stop();
		} catch (NullPointerException e) {
			System.err.println("The sound menuMusic or gameplayMusic is not initialized!");
		}
	}

	// is specific music playing
	public static boolean isMenuMusicPlaying() {
		try {
			return menuMusic.playing();
		} catch (NullPointerException e) {
			System.err.println("The sound menuMusic is not initialized!");
			return false;
		}
	}

	public static boolean isGameplayMusicPlaying() {
		try {
			return gameplayMusic.playing();
		} catch (NullPointerException e) {
			System.err.println("The sound gameplayMusic is not initialized!");
			return false;
		}
	}

	public static boolean isHighscoreMusicPlaying() {
		try {
			return highscoreMusic.playing();
		} catch (NullPointerException e) {
			System.err.println("The sound highscoreMusic is not initialized!");
			return false;
		}
	}

}
