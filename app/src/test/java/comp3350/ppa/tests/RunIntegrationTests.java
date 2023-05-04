package comp3350.ppa.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import comp3350.ppa.tests.integration.IntegrationTests;

public class RunIntegrationTests extends TestCase {
    public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Integration tests");
        suite.addTest(IntegrationTests.suite());
        return suite;
    }
}
