package testcases;
import static org.junit.Assert.assertTrue;

import de.tudarmstadt.informatik.fop.breakout.parameters.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import adapter.Adapter;

public class TestKeyBoardInput {
		
		Adapter adapter;
		
		@Before
		public void init() {
			adapter = new Adapter();
		}
		
		@After
		public void finish() {
			adapter.stopGame();
		}
		
		@Test
		public void testNewGame() {
			adapter.initializeGame();
			assertTrue(adapter.getStateBasedGame().getCurrentStateID()== Constants.MAINMENU_STATE);
			adapter.changeToGameplayState();
			assertTrue(adapter.getStateBasedGame().getCurrentStateID()== Constants.GAMEPLAY_STATE);
		}
		
		@Test
		public void testSitckMoveLeft() {
			adapter.initializeGame();
			adapter.changeToGameplayState();
			adapter. handleKeyDownLeftArrow();
			assertTrue("Your stick should move left when pressing left arrow", adapter.getStickPosition().getX() < 200);
		}
		
		@Test
		public void testSitckMoveRight() {
			adapter.initializeGame();
			adapter.changeToGameplayState();
			adapter. handleKeyDownRightArrow();
			assertTrue("Your stick should move right when pressing right arrow", adapter.getStickPosition().getX() > 600);
		}
		
}
