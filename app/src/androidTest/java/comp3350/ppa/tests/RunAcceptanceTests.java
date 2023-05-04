package comp3350.ppa.tests;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

import comp3350.ppa.tests.acceptance.CreateProjectsCreateTasksTests;
import comp3350.ppa.tests.acceptance.EditProjectsEditTasksTests;
import comp3350.ppa.tests.acceptance.StopWorkTimerTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({CreateProjectsCreateTasksTests.class, EditProjectsEditTasksTests.class, StopWorkTimerTests.class})
public class RunAcceptanceTests
{
    public RunAcceptanceTests()
    {
        System.out.println("PPA Acceptance tests");
    }
}
