package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testcases.TestBall;
import testcases.TestBlock;
import testcases.TestKeyBoardInput;
import testcases.TestPlayer;



@RunWith(Suite.class)
@SuiteClasses({ TestBall.class, TestBlock.class, TestPlayer.class, TestKeyBoardInput.class})
public class BasicTests {

}
