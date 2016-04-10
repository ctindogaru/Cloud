package poo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ catTest.class, cdTest.class, echoTest.class, loginTest.class, logoutTest.class, lsTest.class,
		mkdirTest.class, newuserTest.class, pwdTest.class, rmTest.class, touchTest.class, userinfoTest.class })
public class AllTests {

}
