package comp3350.ppa.tests.integration;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;

import comp3350.ppa.application.Services;
import comp3350.ppa.application.Main;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;

public class BusinessPersistenceSeamTest extends TestCase {
    public BusinessPersistenceSeamTest(String arg0) {
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

    public void testAccessProjectsTypical() {
        AccessProjects ap = new AccessProjects();
        Project project;
        ArrayList<Project> projectList = new ArrayList<>();

        System.out.println("\nStarting Integration test of AccessProjects to persistence: Typical cases");

        assertNull(ap.getProjectById(0, projectList));
        project = projectList.get(0);
        assertEquals(0, project.getId());

        assertNull(ap.getProjects(projectList));
        assertEquals(2, projectList.size());
        assertTrue(projectList.contains(project));

        assertNull(ap.deleteProject(project));
        assertEquals("No project found with the id: 0", ap.getProjectById(0, projectList));

        assertNull(ap.getProjects(projectList));
        assertEquals(1, projectList.size());
        assertFalse(projectList.contains(project));

        assertNull(ap.insertProject(project));
        assertNull(ap.getProjectById(2, projectList));
        project = projectList.get(0);
        assertEquals(2, project.getId());

        assertEquals(30, project.getFocusMinutes());
        project.setFocusMinutes(75);
        assertNull(ap.updateProject(project));
        assertNull(ap.getProjectById(2, projectList));
        project = projectList.get(0);
        assertEquals(75, project.getFocusMinutes());

        project.setFocusMinutes(30);
        assertNull(ap.updateProject(project));
        assertNull(ap.getProjectById(2, projectList));
        project = projectList.get(0);
        assertEquals(30, project.getFocusMinutes());

        assertNull(ap.deleteProject(project));
        assertNull(ap.getProjectById(1, projectList));
        project = projectList.get(0);
        assertNull(ap.deleteProject(project));

        assertNull(ap.getProjects(projectList));
        assertEquals(0, projectList.size());

        assertEquals("No project found with the id: 200", ap.getProjectById(200, projectList));

        System.out.println("Finished Integration test of AccessProjects to persistence: Typical cases");
    }

    public void testAccessProjectsDeleteNonexistent() {
        AccessProjects ap = new AccessProjects();
        ArrayList<Project> projectList = new ArrayList<>();

        System.out.println("\nStarting Integration test of AccessProjects to persistence: Delete non-existent");

        assertEquals("Tuple not inserted correctly.", ap.deleteProject(new Project(200)));
        assertEquals("Tuple not inserted correctly.", ap.deleteProject(new Project(-1)));

        assertNull(ap.getProjects(projectList));
        assertEquals(2, projectList.size());

        System.out.println("Finished Integration test of AccessProjects to persistence: Delete non-existent");
    }

    public void testAccessProjectsInsertDuplicate() {
        AccessProjects ap = new AccessProjects();
        ArrayList<Project> projectList = new ArrayList<>();
        Project project;

        System.out.println("\nStarting Integration test of AccessProjects to persistence: Insert duplicate");

        assertNull(ap.getProjects(projectList));
        assertEquals(2, projectList.size());

        project = projectList.get(0);
        assertNull(ap.insertProject(project));
        assertNull(ap.getProjects(projectList));
        assertEquals(3, projectList.size());
        project = projectList.get(2);
        // ID auto increments
        assertEquals(2, project.getId());

        Services.closeDataAccess();

        System.out.println("Finished Integration test of AccessProjects to persistence: Insert duplicate");
    }

    public void testAccessTasksTypical() {
        AccessTasks at = new AccessTasks();
        Task task;
        ArrayList<Task> taskList = new ArrayList<>();

        Project project0 = new Project(0);
        Project project1 = new Project(1);

        System.out.println("\nStarting Integration test of AccessTasks to persistence: Typical cases");

        assertNull(at.getTaskById(0, taskList));
        task = taskList.get(0);
        assertEquals(0, task.getId());

        assertNull(at.getTasks(taskList));
        assertEquals(9, taskList.size());
        assertTrue(taskList.contains(task));

        assertNull(at.getCompletedTasks(taskList, project0));
        assertEquals(1, taskList.size());
        assertNull(at.getInProgressTasks(taskList, project0));
        assertEquals(1, taskList.size());
        assertNull(at.getTodoTasks(taskList, project0));
        assertEquals(2, taskList.size());
        assertNull(at.getIncompleteTasks(taskList, project0));
        assertEquals(3, taskList.size());

        assertNull(at.getCompletedTasks(taskList, project1));
        assertEquals(0, taskList.size());
        assertNull(at.getInProgressTasks(taskList, project1));
        assertEquals(0, taskList.size());
        assertNull(at.getTodoTasks(taskList, project1));
        assertEquals(5, taskList.size());
        assertNull(at.getIncompleteTasks(taskList, project1));
        assertEquals(5, taskList.size());

        assertNull(at.deleteTask(task));
        assertEquals("No task found with the id: 0", at.getTaskById(0, taskList));

        assertNull(at.getTasks(taskList));
        assertEquals(8, taskList.size());
        assertFalse(taskList.contains(task));

        assertNull(at.insertTask(task));
        assertNull(at.getTaskById(9, taskList));
        task = taskList.get(0);
        assertEquals(9, task.getId());

        assertEquals("Download Android Studio", task.getTitle());
        task.setTitle("Download IntelliJ");
        assertNull(at.updateTask(task));
        assertNull(at.getTaskById(9, taskList));
        task = taskList.get(0);
        assertEquals("Download IntelliJ", task.getTitle());

        task.setTitle("Download Android Studio");
        assertNull(at.updateTask(task));
        assertNull(at.getTaskById(9, taskList));
        task = taskList.get(0);
        assertEquals("Download Android Studio", task.getTitle());

        assertEquals("No task found with the id: 200", at.getTaskById(200, taskList));

        // Delete all tasks
        assertNull(at.getTasks(taskList));
        assertEquals(9, taskList.size());
        assertNull(at.deleteTask(taskList.get(0)));
        assertNull(at.deleteTask(taskList.get(1)));
        assertNull(at.deleteTask(taskList.get(2)));
        assertNull(at.deleteTask(taskList.get(3)));
        assertNull(at.deleteTask(taskList.get(4)));
        assertNull(at.deleteTask(taskList.get(5)));
        assertNull(at.deleteTask(taskList.get(6)));
        assertNull(at.deleteTask(taskList.get(7)));
        assertNull(at.deleteTask(taskList.get(8)));

        // Ensure all lists are empty
        assertNull(at.getTasks(taskList));
        assertEquals(0, taskList.size());

        assertNull(at.getCompletedTasks(taskList, project0));
        assertEquals(0, taskList.size());
        assertNull(at.getInProgressTasks(taskList, project0));
        assertEquals(0, taskList.size());
        assertNull(at.getTodoTasks(taskList, project0));
        assertEquals(0, taskList.size());
        assertNull(at.getIncompleteTasks(taskList, project0));
        assertEquals(0, taskList.size());

        assertNull(at.getCompletedTasks(taskList, project1));
        assertEquals(0, taskList.size());
        assertNull(at.getInProgressTasks(taskList, project1));
        assertEquals(0, taskList.size());
        assertNull(at.getTodoTasks(taskList, project1));
        assertEquals(0, taskList.size());
        assertNull(at.getIncompleteTasks(taskList, project1));
        assertEquals(0, taskList.size());

        Services.closeDataAccess();

        System.out.println("Finished Integration test of AccessTasks to persistence: Typical cases");
    }

    public void testAccessTasksDeleteNonexistent() {
        AccessTasks at = new AccessTasks();
        ArrayList<Task> taskList = new ArrayList<>();

        System.out.println("\nStarting Integration test of AccessTasks to persistence: Delete non-existent");

        assertNull(at.getTasks(taskList));
        assertEquals(9, taskList.size());

        assertEquals("Tuple not inserted correctly.", at.deleteTask(new Task(200)));
        assertEquals("Tuple not inserted correctly.", at.deleteTask(new Task(-1)));

        assertNull(at.getTasks(taskList));
        assertEquals(9, taskList.size());

        System.out.println("Finished Integration test of AccessTasks to persistence: Delete non-existent");
    }

    public void testAccessTasksInsertDuplicate() {
        AccessTasks at = new AccessTasks();
        ArrayList<Task> taskList = new ArrayList<>();
        Task task;

        System.out.println("\nStarting Integration test of AccessTasks to persistence: Insert duplicate");

        assertNull(at.getTasks(taskList));
        assertEquals(9, taskList.size());
        task = taskList.get(0);

        assertNull(at.insertTask(task));

        assertNull(at.getTasks(taskList));
        assertEquals(10, taskList.size());
        task = taskList.get(9);
        assertEquals(9, task.getId());

        System.out.println("Finished Integration test of AccessTasks to persistence: Insert duplicate");
    }
}