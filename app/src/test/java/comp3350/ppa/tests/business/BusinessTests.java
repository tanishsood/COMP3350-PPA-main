package comp3350.ppa.tests.business;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BusinessTests {
    public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Business tests");
        suite.addTestSuite(AccessProjectsTest.class);
        suite.addTestSuite(AccessTasksTest.class);
        suite.addTestSuite(StatusCalculationsTest.class);
        suite.addTestSuite(GlobalTimerTest.class);
        return suite;
    }
}
