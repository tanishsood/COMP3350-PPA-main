package comp3350.ppa.tests.persistence;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;
import comp3350.ppa.persistence.DataAccess;

public class DataAccessTest extends TestCase {
    private DataAccess dataAccess;

    public DataAccessTest(String arg0) {
        super(arg0);
    }

    public void setUp() {
        System.out.println("\nStarting Persistence test DataAccess (using stub)");

        // user the following statements to run with the stub database:
        dataAccess = new DataAccessStub();
        dataAccess.open("Stub");
        // or switch to the real database:
        // dataAccess = new DataAccessObject(Main.dbName);
        // dataAccess.open(Main.getDBPathName());
        // Note the increase in test execution time.
    }

    public void tearDown() {
        System.out.println("Finished Persistence test DataAccess (using stub)");
    }


    // this code will run the tests on the given DAO
    public static void dataAccessTest(DataAccess dataAccess) {
        DataAccessTest dataAccessTest = new DataAccessTest("");
        dataAccessTest.dataAccess = dataAccess;
        dataAccessTest.testTasks();
        dataAccessTest.testProjects();
    }

    public void testTasks() {
        System.out.println("Testing Tasks Create, Read, Update, Delete");
        ArrayList<Task> tasks;
        String result;
        Task task;

        // test get all tasks
        tasks = new ArrayList<>();
        result = dataAccess.getTaskSequential(tasks);
        assertNull(result);
        assertEquals(9, tasks.size());
        task = tasks.get(0);
        assertEquals(0, task.getId());
        assertEquals("Download Android Studio", task.getTitle());
        assertEquals("visit the website and get it on your machine", task.getDescription());
        assertEquals("2022-08-12 0:00:00", fromCal(task.getDueDate()));
        assertEquals(40, task.getEstimatedTime());
        assertEquals(TaskStatusEnum.DONE, task.getStatus());

        // test get task by id
        Task taskRandom;
        ArrayList<Task> taskRandomList;
        taskRandomList = dataAccess.getTaskById(task.getId());
        assertEquals(1, taskRandomList.size());
        taskRandom = taskRandomList.get(0);
        assertEquals(task, taskRandom);

        //
        // test get tasks by type

        // to-do tasks
        Project project = new Project(0);
        tasks = new ArrayList<>();
        result = dataAccess.getTodoTasks(tasks, project);
        assertNull(result);
        assertEquals(2,tasks.size());
        task = tasks.get(0);
        assertEquals(TaskStatusEnum.TODO, task.getStatus());

        // completed tasks
        tasks = new ArrayList<>();
        result = dataAccess.getCompletedTasks(tasks, project);
        assertNull(result);
        assertEquals(1,tasks.size());
        task = tasks.get(0);
        assertEquals(TaskStatusEnum.DONE, task.getStatus());

        // in progress tasks
        tasks = new ArrayList<>();
        result = dataAccess.getInProgressTasks(tasks, project);
        assertNull(result);
        assertEquals(1,tasks.size());
        task = tasks.get(0);
        assertEquals(TaskStatusEnum.IN_PROGRESS,task.getStatus());

        // incomplete tasks
        tasks = new ArrayList<>();
        result = dataAccess.getIncompleteTasks(tasks,project);
        assertNull(result);
        assertEquals(3,tasks.size());
        task = tasks.get(0);
        assertEquals(TaskStatusEnum.IN_PROGRESS, task.getStatus());

        // test insert new task
        task.setId(9);
        result = dataAccess.insertTask(task);
        assertNull(result);
        tasks = new ArrayList<>();
        assertNull(dataAccess.getTaskSequential(tasks));
        assertEquals(10, tasks.size());
        Task test = tasks.get(tasks.size()-1);

        // test update task
        task.setTitle("Test 1");
        task.setStatus(TaskStatusEnum.IN_PROGRESS);
        result = dataAccess.updateTask(task);
        assertNull(result);
        tasks = dataAccess.getTaskById(task.getId());
        task = tasks.get(0);
        assertEquals("Test 1", task.getTitle());
        assertEquals(TaskStatusEnum.IN_PROGRESS,task.getStatus());

        // test delete task
        result = dataAccess.deleteTask(task);
        assertNull(result);
        tasks = new ArrayList<>();
        assertNull(dataAccess.getTaskSequential(tasks));
        assertEquals(9, tasks.size());

    }

    public void testProjects() {
        ArrayList<Project> projects;
        String result;
        Project project;

        // test get all projects
        projects = new ArrayList<>();
        result = dataAccess.getProjectSequential(projects);
        assertNull(result);
        assertEquals(2, projects.size());
        project = projects.get(0);
        assertEquals(0, project.getId());
        assertEquals("Make a cool app", project.getTitle());
        assertEquals("Use Android Studio to make a cool app.", project.getDescription());
        assertEquals("2022-08-02 0:00:00", fromCal(project.getDueDate()));
        assertEquals("Grandma probably can help", project.getNotes());
        assertEquals(30, project.getFocusMinutes());

        // test get project by id
        Project projectRandom;
        ArrayList<Project> projectRandomList;
        projectRandomList = dataAccess.getProjectById(project.getId());
        assertEquals(1, projectRandomList.size());
        projectRandom = projectRandomList.get(0);
        assertEquals(project, projectRandom);

        // test insert new project
        project.setId(2);
        result = dataAccess.insertProject(project);
        assertNull(result);
        projects = new ArrayList<>();
        assertNull(dataAccess.getProjectSequential(projects));
        assertEquals(3,projects.size());

        // test update project
        project.setFocusMinutes(30);
        result = dataAccess.updateProject(project);
        assertNull(result);
        projects = dataAccess.getProjectById(project.getId());
        project = projects.get(0);
        assertEquals(30, project.getFocusMinutes());

        // test delete project
        result = dataAccess.deleteProject(project);
        assertNull(result);
        projects = new ArrayList<>();
        assertNull(dataAccess.getProjectSequential(projects));
        assertEquals(2,projects.size());
    }

    private String fromCal(Calendar cal){
        // comparing Calendar objects is impossible because the getInstance is called at different
        // instants with millisecond precision. We can compare strings.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        return sdf.format(cal.getTime());
    }
}
