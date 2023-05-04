package comp3350.ppa.tests.business;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.application.Services;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.objects.Project;
import comp3350.ppa.tests.persistence.DataAccessStub;

public class AccessProjectsTest extends TestCase {
    private static final String dbName = "AccessProjectsTest";
    private AccessProjects accessProjects;
    ArrayList<Project> projectList;

    public void setUp() {
        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));
        accessProjects = new AccessProjects(dbName);
        projectList = new ArrayList<>();

        // Ensure Projects is empty
        accessProjects.getProjects(projectList);
        for (Project project: projectList) {
            accessProjects.deleteProject(project);
        }
        projectList.clear();
    }

    public void tearDown() {
        Services.closeDataAccess();
    }

    public void testTypical() {
        System.out.println("\nStarting AccessProjectsTest: typical cases");

        Project project1 = new Project(1, "TestProject1", "TestProject1","TestNote", Calendar.getInstance(), 30);
        Project project2 = new Project(2, "TestProject2", "TestProject2","TestNote", Calendar.getInstance(), 30);
        Project project3 = new Project(3, "TestProject3", "TestProject3","TestNote", Calendar.getInstance(), 30);

        // Test that getProjects returns projects added, in the order they were added
        assertNull(accessProjects.insertProject(project1));
        assertNull(accessProjects.insertProject(project3));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(2, projectList.size());
        assertTrue(projectList.contains(project1));
        assertTrue(projectList.contains(project3));
        assertFalse(projectList.contains(project2));
        assertEquals(project1, projectList.get(0));
        assertEquals(project3, projectList.get(1));

        // Remove project3 and expect getProjects to no longer have it
        assertNull(accessProjects.deleteProject(project3));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(1, projectList.size());
        assertTrue(projectList.contains(project1));
        assertFalse(projectList.contains(project2));
        assertFalse(projectList.contains(project3));
        assertEquals(project1, projectList.get(0));

        // Add project2 then project3 and expect order to match
        assertNull(accessProjects.insertProject(project2));
        assertNull(accessProjects.insertProject(project3));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(3, projectList.size());
        assertTrue(projectList.contains(project1));
        assertTrue(projectList.contains(project2));
        assertTrue(projectList.contains(project3));
        assertEquals(project1, projectList.get(0));
        assertEquals(project2, projectList.get(1));
        assertEquals(project3, projectList.get(2));

        // Test that getProjectById only returns projects that exist
        assertNull(accessProjects.getProjectById(project1.getId(), projectList));
        assertEquals(1, projectList.size());
        assertTrue(projectList.contains(project1));

        assertNull(accessProjects.deleteProject(project2));
        assertEquals("No project found with the id: " + project2.getId(), accessProjects.getProjectById(project2.getId(), projectList));

        // Test that projects are updated
        assertNull(accessProjects.getProjectById(project1.getId(), projectList));
        Project toUpdate = projectList.get(0);
        toUpdate.setFocusMinutes(75);
        assertNull(accessProjects.updateProject(toUpdate));
        assertNull(accessProjects.getProjectById(project1.getId(), projectList));
        Project updated = projectList.get(0);
        assertEquals(75, updated.getFocusMinutes());

        System.out.println("Finished AccessProjectsTest: typical cases");
    }

    public void testEmpty() {
        System.out.println("\nStarting AccessProjectsTest: empty cases");

        Project dummyProject = new Project(1);

        // Test that getProjects clears the list when no projects
        projectList.add(dummyProject);
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(0, projectList.size());

        // Test that getProjectById doesn't clear
        projectList.add(dummyProject);
        assertEquals("No project found with the id: 2", accessProjects.getProjectById(2, projectList));
        assertEquals(1, projectList.size());
        assertTrue(projectList.contains(dummyProject));

        System.out.println("Finished AccessProjectsTest: empty cases");
    }

    public void testDeleteNonexistentItem() {
        System.out.println("\nStarting AccessProjectsTest: delete non-existent item");

        Project dummyProject = new Project(1,"Dummy project","Dummy","NewNote",Calendar.getInstance(),30);

        // Delete on an empty list should do nothing
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(0, projectList.size());
        assertNull(accessProjects.deleteProject(dummyProject));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(0, projectList.size());

        // Deleting the same item twice should do nothing
        assertNull(accessProjects.insertProject(dummyProject));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(1, projectList.size());
        assertTrue(projectList.contains(dummyProject));

        assertNull(accessProjects.deleteProject(dummyProject));
        assertNull(accessProjects.deleteProject(dummyProject));
        assertNull(accessProjects.getProjects(projectList));
        assertEquals(0, projectList.size());

        System.out.println("Finished AccessProjectsTest: delete non-existent item");
    }

    public void testNull() {
        System.out.println("\nStarting AccessProjectsTest: null cases");

        try {
            new AccessProjects(null); // Don't need to keep this since we expect it to fail
            fail("Expected NullPointerException with null dbName");
        }
        catch (NullPointerException ignored) {
        }

        try {
            accessProjects.getProjects(null);
            fail("Expected NullPointerException with null projects list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessProjects.insertProject(null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessProjects.deleteProject(null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessProjects.getProjectById(1, null);
            fail("Expected NullPointerException with null project list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessProjects.updateProject(null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        System.out.println("Finished AccessProjectsTest: null cases");
    }
}
