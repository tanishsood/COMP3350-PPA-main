package comp3350.ppa.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import comp3350.ppa.application.Main;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;

public class StatusCalculations {
    /*
     * Calculates minutes of tasks left to do in a time range
     * @param tasks The list of tasks to calculate over
     * @param from The start time for due dates
     * @param to The end time for due dates
     * @return Integer representing minutes left
     */
    public static int tasksMinutesLeft(List<Task> tasks, Calendar from, Calendar to) {
        int remainingMinutes = 0;

        // Ensure we don't have null dates
        if (from == null || to == null)
            throw new NullPointerException();

        // For each task, if it is not done and in range, add estimated time
        for (Task task: tasks) {
            Calendar dueDate = task.getDueDate();
            if (task.getStatus() != TaskStatusEnum.DONE && !dueDate.after(to) && !dueDate.before(from))
                remainingMinutes += task.getEstimatedTime();
        }

        return remainingMinutes;
    }

    /*
     * Gets the estimate of tasks that are due between previous Sunday and next Sunday
     * @param project The project to get the weekly estimate for
     * @return Integer representing minutes of estimate
     */
    public static int projectWeeklyEstimate(Project project) {
        return projectWeeklyEstimate(Calendar.getInstance(), project, Main.dbName);
    }

    /*
     * Gets the estimate of tasks that are due in the week for the given date
     * @param date The date to get the weekly estimate for
     * @param project The project to get the weekly estimate for
     * @param dbName The name of the DB to access
     * @return Integer representing minutes of estimate
     */
    public static int projectWeeklyEstimate(Calendar date, Project project, String dbName) {
        int minutesRemaining = -1;
        Calendar weekStart = Calendar.getInstance();
        Calendar weekEnd = Calendar.getInstance();
        ArrayList<Task> tasks = new ArrayList<>();
        AccessTasks accessTasks = new AccessTasks(dbName);

        // Get week range
        getWeekRange(date, weekStart, weekEnd);

        // Get incomplete tasks for project
        String result = accessTasks.getIncompleteTasks(tasks, project);
        if (result == null)
            minutesRemaining = tasksMinutesLeft(tasks, weekStart, weekEnd);
        return minutesRemaining;
    }

    /*
     * Gets the week range for input currDate, from Sunday to Sunday
     * @param currDate The current date to get the week range for
     * @param outStart A Calendar instance to output the start of range
     * @param outEnd A Calendar instance to output the end of range
     */
    public static void getWeekRange(Calendar currDate, Calendar outStart, Calendar outEnd) {
        // Set start to midnight on prior Sunday
        outStart.setTimeInMillis(currDate.getTimeInMillis());
        outStart.set(Calendar.DAY_OF_WEEK, 1);
        outStart.set(Calendar.HOUR_OF_DAY, 0);
        outStart.set(Calendar.MINUTE, 0);
        outStart.set(Calendar.SECOND, 0);
        outStart.set(Calendar.MILLISECOND, 0);
        // Set end to midnight on next Sunday
        outEnd.setTimeInMillis(outStart.getTimeInMillis());
        outEnd.set(Calendar.DATE, outEnd.get(Calendar.DATE) + 7);
    }
}
