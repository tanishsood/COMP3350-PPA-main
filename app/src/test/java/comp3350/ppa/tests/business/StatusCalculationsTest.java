package comp3350.ppa.tests.business;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.application.Services;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.business.StatusCalculations;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;
import comp3350.ppa.tests.persistence.DataAccessStub;

public class StatusCalculationsTest extends TestCase {
    private static final String dbName = "StatusCalculationsTest";
    private AccessTasks accessTasks;

    private final Calendar date = Calendar.getInstance();
    private final Calendar rangeStart = Calendar.getInstance();
    private final Calendar rangeEnd = Calendar.getInstance();

    private Project project;
    private Task inRange1;
    private Task inRange2;
    private Task inRangeComplete;
    private Task outOfRangeBefore;
    private Task outOfRangeAfter;

    private ArrayList<Task> taskList;

    public void setUp() {
        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));
        accessTasks = new AccessTasks(dbName);

        taskList = new ArrayList<>();

        date.clear();
        date.set(2022, 5, 15);
        rangeStart.clear(); // Clear time, we only want date
        rangeStart.set(2022,5 , 12);
        rangeEnd.clear();
        rangeEnd.set(2022, 5, 19);

        project = new Project(100);

        inRange1 = new Task(1, project.getId(), "InRange1", "InRange1", Calendar.getInstance(), 30);
        inRange1.getDueDate().clear();
        inRange1.getDueDate().set(2022, 5, 14);

        inRange2 = new Task(2, project.getId(), "InRange2", "InRange2", Calendar.getInstance(), 15);
        inRange2.getDueDate().clear();
        inRange2.getDueDate().set(2022, 5, 17);

        inRangeComplete = new Task(3, project.getId(), "InRangeComplete", "InRangeComplete", Calendar.getInstance(), 25);
        inRangeComplete.getDueDate().clear();
        inRangeComplete.getDueDate().set(2022, 5, 15);
        inRangeComplete.setStatus(TaskStatusEnum.DONE);

        outOfRangeBefore = new Task(4, project.getId(), "OutOfRangeBefore", "OutOfRangeBefore", Calendar.getInstance(), 25);
        outOfRangeBefore.getDueDate().clear();
        outOfRangeBefore.getDueDate().set(2022, 5, 11);
        outOfRangeBefore.setStatus(TaskStatusEnum.DONE);

        outOfRangeAfter = new Task(4, project.getId(), "OutOfRangeAfter", "OutOfRangeAfter", Calendar.getInstance(), 25);
        outOfRangeAfter.getDueDate().clear();
        outOfRangeAfter.getDueDate().set(2022, 5, 20);
        outOfRangeAfter.setStatus(TaskStatusEnum.DONE);
    }

    public void tearDown() {
        Services.closeDataAccess();
    }

    public void testTypicalTasksMinutesLeft() {
        System.out.println("\nStarting TasksMinutesLeftTest: typical cases");

        // Should return 30 with inRange1
        taskList.add(inRange1);
        assertEquals(30, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // Should return 30 + 15 = 45 with inRange1 and inRange2
        taskList.add(inRange2);
        assertEquals(45, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // The rest should not affect output
        taskList.add(inRangeComplete);
        assertEquals(45, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));
        taskList.add(outOfRangeBefore);
        assertEquals(45, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));
        taskList.add(outOfRangeAfter);
        assertEquals(45, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // Only inRange2 should return 15
        taskList.clear();
        taskList.add(inRange2);
        assertEquals(15, StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        System.out.println("Finished TasksMinutesLeftTest: typical cases");
    }

    public void testZeroCasesTasksMinutesLeft() {
        System.out.println("\nStarting TasksMinutesLeftTest: zero cases");

        // Empty list should return 0
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // In range but complete should return 0
        taskList.add(inRangeComplete);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // Out of range should return 0
        taskList.clear();
        taskList.add(outOfRangeBefore);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));
        taskList.clear();
        taskList.add(outOfRangeAfter);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        // A combination should return 0
        taskList.add(inRangeComplete);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        taskList.add(outOfRangeBefore);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        taskList.remove(inRangeComplete);
        assertEquals(0 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        System.out.println("Finished TasksMinutesLeftTest: zero cases");
    }

    public void testEdgeCasesTasksMinutesLeft() {
        System.out.println("\nStarting TasksMinutesLeftTest: edge cases");

        // Test bounds of range
        Task beginningBound = new Task(10, 1, "BeginningBound", "BeginningBound", rangeStart, 60);
        Task endBound = new Task(10, 1, "EndBound", "EndBound", rangeEnd, 75);

        taskList.add(beginningBound);
        assertEquals(60 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        taskList.clear();
        taskList.add(endBound);
        assertEquals(75 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        taskList.add(beginningBound);
        assertEquals(135 ,StatusCalculations.tasksMinutesLeft(taskList, rangeStart, rangeEnd));

        System.out.println("Finished TasksMinutesLeftTest: edge cases");
    }

    public void testNullTasksMinutesLeft() {
        System.out.println("\nStarting TasksMinutesLeftTest: null cases");

        try {
            StatusCalculations.tasksMinutesLeft(null, rangeStart, rangeEnd);
            fail("Expected NullPointerException with null task list");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.tasksMinutesLeft(taskList, null, rangeEnd);
            fail("Expected NullPointerException with null start");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.tasksMinutesLeft(taskList, rangeStart, null);
            fail("Expected NullPointerException with null end");
        }
        catch (NullPointerException ignored) {}

        System.out.println("Finished TasksMinutesLeftTest: null cases");
    }

    public void testTypicalWeekRange() {
        System.out.println("\nStarting WeekRangeTest: typical cases");

        Calendar startDate1 = Calendar.getInstance();
        startDate1.clear();
        startDate1.set(2022, 5, 9); // June 9, 2022 (Thursday)
        Calendar expectedBeginning1 = Calendar.getInstance();
        expectedBeginning1.clear();
        expectedBeginning1.set(2022, 5, 5);
        Calendar expectedEnd1 = Calendar.getInstance();
        expectedEnd1.clear();
        expectedEnd1.set(2022, 5, 12);

        Calendar startDate2 = Calendar.getInstance();
        startDate2.clear();
        startDate2.set(2022, 8, 19); // September 19, 2022 (Monday)
        Calendar expectedBeginning2 = Calendar.getInstance();
        expectedBeginning2.clear();
        expectedBeginning2.set(2022, 8, 18);
        Calendar expectedEnd2 = Calendar.getInstance();
        expectedEnd2.clear();
        expectedEnd2.set(2022, 8, 25);

        Calendar currBegin = Calendar.getInstance();
        Calendar currEnd = Calendar.getInstance();

        StatusCalculations.getWeekRange(startDate1, currBegin, currEnd);
        assertEquals(expectedBeginning1.getTimeInMillis(), currBegin.getTimeInMillis());
        assertEquals(expectedEnd1.getTimeInMillis(), currEnd.getTimeInMillis());

        StatusCalculations.getWeekRange(startDate2, currBegin, currEnd);
        assertEquals(expectedBeginning2.getTimeInMillis(), currBegin.getTimeInMillis());
        assertEquals(expectedEnd2.getTimeInMillis(), currEnd.getTimeInMillis());

        System.out.println("Finished WeekRangeTest: typical cases");
    }

    public void testEdgeCasesWeekRange() {
        System.out.println("\nStarted WeekRangeTest: edge cases");

        // Month boundary
        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(2022, 5, 30); // June 30, 2022 (Thursday)
        Calendar expectedBeginning1 = Calendar.getInstance();
        expectedBeginning1.clear();
        expectedBeginning1.set(2022, 5, 26);
        Calendar expectedEnd1 = Calendar.getInstance();
        expectedEnd1.clear();
        expectedEnd1.set(2022, 6, 3);

        // Sunday input and month boundary
        Calendar startDate2 = Calendar.getInstance();
        startDate2.set(2022, 4, 1); // May 1, 2022 (Sunday)
        Calendar expectedBeginning2 = Calendar.getInstance();
        expectedBeginning2.clear();
        expectedBeginning2.set(2022, 4, 1);
        Calendar expectedEnd2 = Calendar.getInstance();
        expectedEnd2.clear();
        expectedEnd2.set(2022, 4, 8);

        // Sunday input
        Calendar startDate3 = Calendar.getInstance();
        startDate3.set(2022, 5, 12); // June 12, 2022 (Sunday)
        Calendar expectedBeginning3 = Calendar.getInstance();
        expectedBeginning3.clear();
        expectedBeginning3.set(2022, 5, 12);
        Calendar expectedEnd3 = Calendar.getInstance();
        expectedEnd3.clear();
        expectedEnd3.set(2022, 5, 19);

        Calendar currBegin = Calendar.getInstance();
        Calendar currEnd = Calendar.getInstance();

        StatusCalculations.getWeekRange(startDate1, currBegin, currEnd);
        assertEquals(expectedBeginning1.getTimeInMillis(), currBegin.getTimeInMillis());
        assertEquals(expectedEnd1.getTimeInMillis(), currEnd.getTimeInMillis());

        StatusCalculations.getWeekRange(startDate2, currBegin, currEnd);
        assertEquals(expectedBeginning2.getTimeInMillis(), currBegin.getTimeInMillis());
        assertEquals(expectedEnd2.getTimeInMillis(), currEnd.getTimeInMillis());

        StatusCalculations.getWeekRange(startDate3, currBegin, currEnd);
        assertEquals(expectedBeginning3.getTimeInMillis(), currBegin.getTimeInMillis());
        assertEquals(expectedEnd3.getTimeInMillis(), currEnd.getTimeInMillis());

        System.out.println("Finished WeekRangeTest: edge cases");
    }

    public void testNullWeekRange() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            StatusCalculations.getWeekRange(null, start, end);
            fail("Expected a NullPointerException with null currDate");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.getWeekRange(Calendar.getInstance(), null, end);
            fail("Expected a NullPointerException with null outStart");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.getWeekRange(Calendar.getInstance(), start, null);
            fail("Expected a NullPointerException with null outEnd");
        }
        catch (NullPointerException ignored) {}
    }

    public void testTypicalProjectWeekly() {
        System.out.println("\nStarting ProjectWeeklyEstimateTest: typical cases");

        // Should return 30 with inRange1
        accessTasks.insertTask(inRange1);
        assertEquals(30, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // Only inRange2 should return 15
        accessTasks.deleteTask(inRange1);
        accessTasks.insertTask(inRange2);
        assertEquals(15, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // Should return 30 + 15 = 45 with inRange1 and inRange2
        accessTasks.insertTask(inRange1);
        assertEquals(45, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // The rest should not affect output
        accessTasks.insertTask(inRangeComplete);
        assertEquals(45, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.insertTask(outOfRangeBefore);
        assertEquals(45, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.insertTask(outOfRangeAfter);
        assertEquals(45, StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        System.out.println("Finished ProjectWeeklyEstimateTest: typical cases");
    }

    public void testZeroCasesProjectWeekly() {
        System.out.println("\nStarting ProjectWeeklyEstimateTest: zero cases");

        // Empty list should return 0
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // In range but complete should return 0
        accessTasks.insertTask(inRangeComplete);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // Out of range should return 0
        accessTasks.deleteTask(inRangeComplete);
        accessTasks.insertTask(outOfRangeBefore);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));
        accessTasks.deleteTask(outOfRangeBefore);
        accessTasks.insertTask(outOfRangeAfter);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        // A combination should return 0
        accessTasks.insertTask(inRangeComplete);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.insertTask(outOfRangeBefore);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.insertTask(inRangeComplete);
        assertEquals(0 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        System.out.println("Finished ProjectWeeklyEstimateTest: zero cases");
    }

    public void testEdgeCasesProjectWeekly() {
        System.out.println("\nStarting ProjectWeeklyEstimateTest: edge cases");

        // Test bounds of range
        Task beginningBound = new Task(10, project.getId(), "BeginningBound", "BeginningBound", rangeStart, 60);
        Task endBound = new Task(10, project.getId(), "EndBound", "EndBound", rangeEnd, 75);

        accessTasks.insertTask(beginningBound);
        assertEquals(60 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.deleteTask(beginningBound);
        accessTasks.insertTask(endBound);
        assertEquals(75 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        accessTasks.insertTask(beginningBound);
        assertEquals(135 ,StatusCalculations.projectWeeklyEstimate(date, project, dbName));

        System.out.println("Finished ProjectWeeklyEstimateTest: edge cases");
    }

    public void testNullProjectWeekly() {
        System.out.println("\nStarting ProjectWeeklyEstimateTest: null cases");

        try {
            StatusCalculations.projectWeeklyEstimate(null, project, dbName);
            fail("Expected NullPointerException with null date");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.projectWeeklyEstimate(date, null, dbName);
            fail("Expected NullPointerException with null project");
        }
        catch (NullPointerException ignored) {}

        try {
            StatusCalculations.projectWeeklyEstimate(date, project, null);
            fail("Expected NullPointerException with null dbName");
        }
        catch (NullPointerException ignored) {}

        System.out.println("Finished ProjectWeeklyEstimateTest: null cases");
    }
}
