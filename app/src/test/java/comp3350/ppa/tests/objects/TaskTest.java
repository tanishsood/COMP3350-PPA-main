package comp3350.ppa.tests.objects;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Calendar;

import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;

public class TaskTest extends TestCase {

    public TaskTest(String arg0) {
        super(arg0);
    }

    @Test
    public void testTypical() {
        System.out.println("\nStarting TaskTest: typical cases");

        int newTaskId = 3;
        int newProjectId = 5;
        String newTaskTitle = "Task Title";
        String newTaskBody = "Task Body";
        Calendar newDueDate = Calendar.getInstance();
        int newEstimatedTime = 20;

        Task task = new Task(newTaskId, newProjectId, newTaskTitle, newTaskBody, newDueDate, newEstimatedTime);

        task.setStatus(TaskStatusEnum.TODO);

        // Asserts checking proper construction and getters
        assertEquals(newTaskId, task.getId());
        assertEquals(newProjectId, task.getProjectId());
        assertEquals(newTaskTitle, task.getTitle());
        assertEquals(newTaskBody, task.getDescription());
        assertEquals(newDueDate, task.getDueDate());
        assertEquals(newEstimatedTime, task.getEstimatedTime());
        assertEquals(TaskStatusEnum.TODO, task.getStatus());

        // Asserts proper setters
        task.setId(9);
        task.setProjectId(77);
        task.setTitle("Dop Titular");
        task.setDescription("Boldsy");
        Calendar dueDate = Calendar.getInstance();
        task.setDueDate(dueDate);

        Calendar focusMinutes = Calendar.getInstance();
        focusMinutes.setTimeInMillis(40*60*1000L);

        task.setEstimatedTime(30);
        task.setStatus(TaskStatusEnum.DONE);

        assertEquals(9,task.getId());
        assertEquals(77,task.getProjectId());
        assertEquals("Dop Titular",task.getTitle());
        assertEquals("Boldsy",task.getDescription());
        assertEquals(dueDate,task.getDueDate());
        assertEquals(30,task.getEstimatedTime());
        assertEquals(TaskStatusEnum.DONE, task.getStatus());

        // test time estimates
        assertEquals("",getEstimatedTimeAsString(-1));
        assertEquals("0 min",getEstimatedTimeAsString(0));
        assertEquals( "1 min",getEstimatedTimeAsString(1));
        assertEquals( "59 min",getEstimatedTimeAsString(59));
        assertEquals( "1 h 00 min",getEstimatedTimeAsString(60));
        assertEquals( "1 h 01 min",getEstimatedTimeAsString(61));
        assertEquals( "23 h 59 min",getEstimatedTimeAsString(23*60 + 59));
        assertEquals( "24 h 00 min",getEstimatedTimeAsString(24 * 60));
        assertEquals( "24 h +",getEstimatedTimeAsString(24 * 60 + 1));


        System.out.println("Finished TaskTest: typical cases");
    }

    @Test
    private String getEstimatedTimeAsString(int minutes) {
        Task t = new Task(30,2,"test task x","dummy task description", Calendar.getInstance(),
                minutes);
        return (t.getEstimatedTimeAsString());
    }

    @Test
    public void testInvalid() {
        System.out.println("\nStarting TaskTest: null cases");

        Task task;

        try {

            new Task(-1, -1, null, null, null, 0);
            fail("Expected NullPointerException with null task");
        }
        catch (NullPointerException ignored) {}

        task = new Task(5, 45, "Title", "Body", Calendar.getInstance(), 0);

        try {
            task.setTitle(null); fail("Setting Title to null should throw NullPointerException");
        }
        catch (NullPointerException ignored) {
        }
        try {
            task.setDescription(null); fail("Setting Body to null should throw NullPointerException");
        }
        catch (NullPointerException ignored) {
        }
        try {
            task.setDueDate(null); fail("Setting DueDate to null should throw NullPointerException");
        }
        catch (NullPointerException ignored) {
        }
        try {
            task.setEstimatedTime(-1); fail("Setting EstimatedTime to negative value should throw IllegalArgumentException");
        }
        catch (IllegalArgumentException ignored) {
        }
        try {
            task.setStatus(null); fail("Setting Status to null should throw NullPointerException");
        }
        catch (NullPointerException ignored) {
        }

        System.out.println("Finished TaskTest: null cases");
    }
}
