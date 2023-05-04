package comp3350.ppa.tests.objects;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Calendar;

import comp3350.ppa.objects.Project;

public class ProjectTest extends TestCase {

    public ProjectTest(String arg0) {
        super(arg0);
    }

    @Test
    public void testTypical(){
        System.out.println("\nStarting ProjectTest: typical case");
        int newProjectId = 5;
        String newProjectTitle = "New Title";
        String newProjectNote = "New note";
        String newProjectDescription = "New Description";
        Calendar newDueDate = Calendar.getInstance();
        int newFocusMinutes = 50;
        Project project = new Project(newProjectId, newProjectTitle, newProjectDescription, newProjectNote, newDueDate, newFocusMinutes);

        // Asserts checking proper construction and getters
        assertNotNull(project);
        assertEquals(newProjectId, project.getId());
        assertEquals(newProjectTitle, project.getTitle());
        assertEquals(newProjectDescription, project.getDescription());
        assertEquals(newProjectNote,project.getNotes());
        assertEquals(newDueDate, project.getDueDate());
        assertEquals(newFocusMinutes, project.getFocusMinutes());

        // Asserts checking equals
        Calendar altDueDate = Calendar.getInstance();
        Project otherProject = new Project(5, "My Name", "Tall and wide","My note", altDueDate, 50);
        Project altProjectSame = new Project(5, "My Name", "Tall and wide","My note", altDueDate, 50);
        assertEquals(otherProject, altProjectSame);
        Project altProjectDiff = new Project(23, "Name", "Desc","My notes", Calendar.getInstance(), 99);
        assertNotSame(otherProject, altProjectDiff);

        // Asserts checking sets
        project.setId(3);
        project.setNotes("asdf");
        project.setFocusMinutes(32);
        assertEquals(3, project.getId());
        assertEquals(32, project.getFocusMinutes());
        assertEquals("asdf", project.getNotes());

        // Test toString
        assertEquals("Project: " + project.getId() + " New Title New Description", project.toString());
        assertEquals("Project: " + otherProject.getId() + " My Name Tall and wide", otherProject.toString());
        assertEquals("Project: " + altProjectDiff.getId() + " Name Desc", altProjectDiff.toString());

        System.out.println("Finished ProjectTest: typical case");
    }

    @Test
    public void testNull() {
        System.out.println("\nStarting ProjectTest: null cases");

        Project project;
        Project nullProject = null;

        try {
            project = new Project(-1, null, null,null, null, 0);
            fail("Expected NullPointerException with null arguments");
        }
        catch (NullPointerException ignored) {}

        try {
            project = new Project(-1, "Nme", "Desc", "Note", Calendar.getInstance(), -99);
            fail("Expected IllegalArgumentException with negative focus minutes");
        }
        catch (IllegalArgumentException ignored) {}

        System.out.println("Finished ProjectTest: null cases");
    }

    @Test
    public void testBadFocusMinutes(){
        System.out.println("\nStarting ProjectTest: bad focus minutes");
        Project project;
        // try setting negative focus minutes
        project = new Project(-1, "Nme", "Desc", "Note", Calendar.getInstance(), 99);
        try {
            project.setFocusMinutes(-45);
            fail("Expected IllegalArgumentException with negative focus minutes");
        }
        catch (IllegalArgumentException ignored) {}
        System.out.println("Finished ProjectTest: bad focus minutes");
    }

    @Test
    public void testShortName(){
        System.out.println("\nStarting ProjectTest: short name");
        Project project;
        try{project = new Project(-1,"","desc", "Note", Calendar.getInstance(),22);
            fail("Expected empty string name to fail.");
        } catch(IllegalArgumentException ignored) {}
        System.out.println("Finished ProjectTest: short name");
    }

    @Test
    public void testLongName(){
        System.out.println("\nStarting ProjectTest: long name");
        // a name that is too long
        final int maxNameLength = 255;
        String longString = "111111111122222222223333333333444444444455555555556666666666777777777778888888888889999999999000000000000111111111122222222223333333333444444444455555555556666666666777777777778888888888889999999999000000000000111111111122222222223333333333444444444455555555556666666666777777777778888888888889999999999000000000000";
        Project project;
        try{project = new Project(-1,longString,"desc","Note", Calendar.getInstance(),22);
            fail("Expected huge name to fail");
        } catch(IllegalArgumentException ignored) {}
        System.out.println("Finished ProjectTest: long name");
    }
}
