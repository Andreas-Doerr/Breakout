package testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testcases.TestHighscore;
import testcases.TestMapParser;


@RunWith(Suite.class)
@SuiteClasses({ TestMapParser.class, TestHighscore.class })
public class ExtendedTests {

}
