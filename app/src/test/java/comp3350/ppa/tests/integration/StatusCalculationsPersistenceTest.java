package comp3350.ppa.tests.integration;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.application.Services;
import comp3350.ppa.application.Main;

import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.business.StatusCalculations;
import comp3350.ppa.objects.Project;

public class StatusCalculationsPersistenceTest extends TestCase
{
    public StatusCalculationsPersistenceTest(String arg0)
    {
        super(arg0);
    }

    public void setUp() {
        Services.closeDataAccess();

        try{
            IntegrationTests.copyDbToTestFolder();
        } catch (IOException e){
            fail("Could not copy database PPA.script file");
        }

        Services.createDataAccess(Main.dbName);
    }

    public void testProjectWeekly() {
        AccessProjects accessProjects = new AccessProjects();
        Project project;
        ArrayList<Project> projectList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        System.out.println("\nStarting Integration test of StatusCalculations to persistence");

        accessProjects.getProjectById(0, projectList);
        project = projectList.get(0);

        calendar.set(2022, 7, 16);
        assertEquals(80, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));
        calendar.set(2022, 7, 12);
        assertEquals(0, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));
        calendar.set(2022, 7, 25);
        assertEquals(40, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));

        accessProjects.getProjectById(1, projectList);
        project = projectList.get(0);

        calendar.set(2022, 7, 30);
        assertEquals(40, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));
        calendar.set(2022, 7, 2);
        assertEquals(80, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));
        calendar.set(2022, 7, 12);
        assertEquals(40, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));

        calendar.set(2023, 1, 1);
        assertEquals(0, StatusCalculations.projectWeeklyEstimate(calendar, project, Main.dbName));

        assertEquals(0, StatusCalculations.projectWeeklyEstimate(new Project(-1)));
        assertEquals(0, StatusCalculations.projectWeeklyEstimate(new Project(200)));

        System.out.println("\nFinished Integration test of StatusCalculations to persistence");
    }
}
