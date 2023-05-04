package comp3350.ppa.tests.business;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.application.Services;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;
import comp3350.ppa.tests.persistence.DataAccessStub;

public class AccessTasksTest extends TestCase {
    private static final String dbName = "AccessTasksTest";
    private AccessTasks accessTasks;
    Project project1;
    Project project2;
    ArrayList<Task> taskList;

    public void setUp() {
        AccessProjects accessProjects;
        ArrayList<Project> projectList = new ArrayList<>();

        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));

        // Setup dummy projects
        accessProjects = new AccessProjects(dbName);
        project1 = new Project(1, "TestProject1", "TestProject1","Dummy note", Calendar.getInstance(), 30);
        project2 = new Project(2, "TestProject2", "TestProject2","Dummy note", Calendar.getInstance(), 30);

        // Ensure Projects only contains our two projects
        accessProjects.getProjects(projectList);
        for (Project project: projectList) {
            accessProjects.deleteProject(project);
        }
        accessProjects.insertProject(project1);
        accessProjects.insertProject(project2);
        projectList.clear();

        // Setup Tasks
        accessTasks = new AccessTasks(dbName);
        taskList = new ArrayList<>();

        // Ensure Tasks is empty
        accessTasks.getTasks(taskList);
        for (Task task: taskList) {
            accessTasks.deleteTask(task);
        }
        taskList.clear();
    }

    public void tearDown() {
        Services.closeDataAccess();
    }

    public void testTypical() {
        System.out.println("\nStarting AccessTasksTest: typical cases");

        Task doneTaskProj1 = new Task(1, project1.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.DONE);
        Task inProgressTaskProj1 = new Task(2, project1.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.IN_PROGRESS);
        Task todoTaskProj1 = new Task(3, project1.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.TODO);
        Task doneTaskProj2 = new Task(4, project2.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.DONE);
        Task inProgressTaskProj2 = new Task(5, project2.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.IN_PROGRESS);
        Task todoTaskProj2 = new Task(6, project2.getId(), "TestTask", "TestTask", Calendar.getInstance(), 30, TaskStatusEnum.TODO);

        // Insert all tasks
        assertNull(accessTasks.insertTask(doneTaskProj1));
        assertNull(accessTasks.insertTask(doneTaskProj2));
        assertNull(accessTasks.insertTask(inProgressTaskProj1));
        assertNull(accessTasks.insertTask(inProgressTaskProj2));
        assertNull(accessTasks.insertTask(todoTaskProj1));
        assertNull(accessTasks.insertTask(todoTaskProj2));

        // Test getTasks gets all tasks
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(6, taskList.size());

        // Test that getIncompleteTasks gets all incomplete tasks for a project
        assertNull(accessTasks.getIncompleteTasks(taskList, project1));
        assertEquals(2, taskList.size());
        assertTrue(taskList.contains(inProgressTaskProj1));
        assertTrue(taskList.contains(todoTaskProj1));

        assertNull(accessTasks.getIncompleteTasks(taskList, project2));
        assertEquals(2, taskList.size());
        assertTrue(taskList.contains(inProgressTaskProj2));
        assertTrue(taskList.contains(todoTaskProj2));

        // Test that getTodoTasks gets all todo tasks for a project
        assertNull(accessTasks.getTodoTasks(taskList, project1));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(todoTaskProj1));

        assertNull(accessTasks.getTodoTasks(taskList, project2));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(todoTaskProj2));

        // Test that getCompletedTasks gets all complete tasks for a project
        assertNull(accessTasks.getCompletedTasks(taskList, project1));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(doneTaskProj1));

        assertNull(accessTasks.getCompletedTasks(taskList, project2));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(doneTaskProj2));

        // Test that getInProgressTasks gets all in progress tasks for a project
        assertNull(accessTasks.getInProgressTasks(taskList, project1));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(inProgressTaskProj1));

        assertNull(accessTasks.getInProgressTasks(taskList, project2));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(inProgressTaskProj2));

        // Remove some tasks
        assertNull(accessTasks.deleteTask(doneTaskProj1));
        assertNull(accessTasks.deleteTask(todoTaskProj2));

        assertNull(accessTasks.getTasks(taskList));
        assertEquals(4, taskList.size());
        assertFalse(taskList.contains(doneTaskProj1));
        assertFalse(taskList.contains(todoTaskProj2));

        // Test getting task by id
        assertNull(accessTasks.getTaskById(4, taskList));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(doneTaskProj2));

        // Test updating task
        assertNull(accessTasks.getTaskById(4, taskList));
        Task toUpdate = taskList.get(0);
        toUpdate.setDescription("UpdatedBody");
        assertNull(accessTasks.updateTask(toUpdate));
        assertNull(accessTasks.getTaskById(4, taskList));
        assertTrue(taskList.contains(toUpdate));
        Task updated = taskList.get(0);
        assertEquals("UpdatedBody", updated.getDescription());

        System.out.println("Finished AccessTasksTest: typical cases");
    }

    public void testEmpty() {
        System.out.println("\nStarting AccessTasksTest: empty cases");

        Task dummyTask = new Task(1);

        // Test that getTasks clears the list when no tasks
        taskList.add(dummyTask);
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(0, taskList.size());

        // Test that getIncompleteTasks clears the list when no tasks
        taskList.add(dummyTask);
        assertNull(accessTasks.getIncompleteTasks(taskList, project1));
        assertEquals(0, taskList.size());

        // Test that getCompletedTasks clears the list when no tasks
        taskList.add(dummyTask);
        assertNull(accessTasks.getCompletedTasks(taskList, project1));
        assertEquals(0, taskList.size());

        // Test that getTodoTasks clears
        taskList.add(dummyTask);
        assertNull(accessTasks.getTodoTasks(taskList, project1));
        assertEquals(0, taskList.size());

        // Test that getInProgressTasks clears
        taskList.add(dummyTask);
        assertNull(accessTasks.getInProgressTasks(taskList, project1));
        assertEquals(0, taskList.size());

        // Test that getTaskById returns error on not found and does not clear
        taskList.add(dummyTask);
        assertEquals("No task found with the id: 2", accessTasks.getTaskById(2, taskList));
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(dummyTask));

        System.out.println("Finished AccessTasksTest: empty cases");
    }

    public void testDeleteNonexistentItem() {
        System.out.println("\nStarting AccessTasksTest: delete non-existent item");

        Task dummyTask = new Task(1,1,"Dummy task","Dummy", Calendar.getInstance(),20);

        // Delete on an empty list should do nothing
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(0, taskList.size());
        assertNull(accessTasks.deleteTask(dummyTask));
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(0, taskList.size());

        // Deleting the same item twice should do nothing
        assertNull(accessTasks.insertTask(dummyTask));
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(1, taskList.size());

        assertNull(accessTasks.deleteTask(dummyTask));
        assertNull(accessTasks.deleteTask(dummyTask));
        assertNull(accessTasks.getTasks(taskList));
        assertEquals(0, taskList.size());
        assertFalse(taskList.contains(dummyTask));

        System.out.println("Finished AccessTasksTest: delete non-existent item");
    }

    public void testNull() {
        System.out.println("\nStarting AccessTasksTest: null cases");

        try {
            new AccessTasks(null); // Don't need to keep this since we expect it to fail
            fail("Expected NullPointerException with null dbName");
        }
        catch (NullPointerException ignored) {
        }

        try {
            accessTasks.getTasks(null);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getIncompleteTasks(null, project1);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getIncompleteTasks(taskList, null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getCompletedTasks(null, project1);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getCompletedTasks(taskList, null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getTodoTasks(null, project1);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getTodoTasks(taskList, null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getInProgressTasks(null, project1);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getInProgressTasks(taskList, null);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.insertTask(null);
            fail("Expected NullPointerException with null task");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.deleteTask(null);
            fail("Expected NullPointerException with null task");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.getTaskById(1, null);
            fail("Expected NullPointerException with null tasks list");
        }
        catch (NullPointerException ignored) {}

        try {
            accessTasks.updateTask(null);
            fail("Expected NullPointerException with null task");
        }
        catch (NullPointerException ignored) {}

        System.out.println("Finished AccessTasksTest: null cases");
    }
}
