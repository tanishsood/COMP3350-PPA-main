package comp3350.ppa.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import comp3350.ppa.tests.business.BusinessTests;
import comp3350.ppa.tests.objects.ObjectTests;
import comp3350.ppa.tests.persistence.PersistenceTests;

public class RunUnitTests extends TestCase {
    public static TestSuite suite;

    public static Test suite() {
        System.out.println("Launching Unit Test Suite.");
        suite = new TestSuite("All unit tests");
        suite.addTest(ObjectTests.suite());
        suite.addTest(BusinessTests.suite());
        suite.addTest(PersistenceTests.suite());
        return suite;
    }
}
