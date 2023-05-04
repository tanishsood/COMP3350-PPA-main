package comp3350.ppa.persistence;

import java.util.ArrayList;
import java.util.List;

import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;

public interface DataAccess {

    void open(String dbName);
    void close();
    String getProjectSequential(List<Project> projectResult);
    ArrayList<Project> getProjectById(int  projectId);
    String insertProject(Project currentProject);
    String updateProject(Project currentProject);
    String deleteProject(Project currentProject);
    String getTaskSequential(List<Task> taskResult);
    String getIncompleteTasks(List<Task> taskResult, Project project);
    String getTodoTasks(List<Task> taskResult, Project project);
    String getInProgressTasks(List<Task> taskResult, Project project);
    String getCompletedTasks(List<Task> taskResult, Project project);
    ArrayList<Task> getTaskById(int taskId);
    String insertTask(Task currentTask);
    String updateTask(Task currentTask);
    String deleteTask(Task currentTask);
}
