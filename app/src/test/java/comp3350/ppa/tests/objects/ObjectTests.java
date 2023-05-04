package comp3350.ppa.tests.objects;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ObjectTests {
    public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Object Suite");
        suite.addTestSuite(ProjectTest.class);
        suite.addTestSuite(TaskTest.class);
        return suite;
    }
}
