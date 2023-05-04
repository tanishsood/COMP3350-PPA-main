package comp3350.ppa.business;

import java.util.ArrayList;
import java.util.List;

import comp3350.ppa.application.Main;
import comp3350.ppa.application.Services;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.persistence.DataAccess;

public class AccessTasks {
    private final DataAccess dataAccess;

    // Default constructor uses Main.dbName
    public AccessTasks() {
        this(Main.dbName);
    }

    // Constructor to allow tests to use a different db
    public AccessTasks(String dbName) {
        if (dbName == null)
            throw new NullPointerException();
        dataAccess = Services.getDataAccess(dbName);
    }

    public String getTasks(List<Task> tasks) {
        tasks.clear();
        return dataAccess.getTaskSequential(tasks);
    }

    public String getIncompleteTasks(List<Task> tasks, Project project) {
        if (project == null)
            throw new NullPointerException();
        tasks.clear();
        return dataAccess.getIncompleteTasks(tasks, project);
    }

    public String getTodoTasks(List<Task> tasks, Project project) {
        if (project == null)
            throw new NullPointerException();
        tasks.clear();
        return dataAccess.getTodoTasks(tasks, project);
    }

    public String getCompletedTasks(List<Task> tasks, Project project) {
        if (project == null)
            throw new NullPointerException();
        tasks.clear();
        return dataAccess.getCompletedTasks(tasks, project);
    }


    public String getInProgressTasks(List<Task> tasks, Project project) {
        if (project == null)
            throw new NullPointerException();
        tasks.clear();
        return dataAccess.getInProgressTasks(tasks, project);
    }

    public String insertTask(Task task) {
        if (task == null)
            throw new NullPointerException();
        try {
            DataValidator.tryValidateTask(task);
        }
        catch (ValidationException e) {
            return e.getMessage();
        }
        return dataAccess.insertTask(task);
    }

    public String deleteTask(Task task) {
        if (task == null)
            throw new NullPointerException();
        return dataAccess.deleteTask(task);
    }

    public String getTaskById(int taskId, ArrayList<Task> tasks) {
        Task task;
        ArrayList<Task> newTasks = dataAccess.getTaskById(taskId);

        if (tasks == null)
            throw new NullPointerException();

        if (newTasks.size()==1) {
            task = newTasks.get(0);
        } else if (newTasks.size() == 0) {
            return "No task found with the id: " + taskId;
        } else {
            return "Task error. Multiple tasks with the same ID: " + taskId;
        }
        tasks.clear();
        tasks.add(task);
        return null;
    }

    public String updateTask(Task task) {
        if (task == null)
            throw new NullPointerException("Task cannot be null.");
        try {
            DataValidator.tryValidateTask(task);
        }
        catch (ValidationException e) {
            return e.getMessage();
        }
        return dataAccess.updateTask(task);
    }
}
