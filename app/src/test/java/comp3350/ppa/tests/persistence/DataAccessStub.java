package comp3350.ppa.tests.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import comp3350.ppa.application.Main;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;
import comp3350.ppa.persistence.DataAccess;


public class DataAccessStub implements DataAccess {
    private final String dbType = "stub";
    private final String dbName;
    private int increment;
    ArrayList<Project> projects;
    ArrayList<Task> tasks;

    public DataAccessStub(String dbName) {
        this.dbName = dbName;
        increment = 0;
    }

    public DataAccessStub() {
        this(Main.dbName);
    }

    public void open(String dbName) {

        projects = new ArrayList<>();
        tasks = new ArrayList<>();

        insertProject(new Project(0,"Make a cool app",
                "Use Android Studio to make a cool app.",
                "Grandma probably can help",
                toCal("2022-08-02"),30));

        insertProject(new Project(1,"Build a garden box",
                "Call John for screws\nDon't use treated wood!",
                "Make a easy-to-access garden box for grandma. It needs to have proper irrigation and it cannot have any kind of treated wood.",
                toCal("2022-08-40"), 60));

        tasks.add(new Task(0, 0,
                "Download Android Studio",
                "visit the website and get it on your machine",
                toCal("2022-08-12"),
                40));
        tasks.get(0).setStatus(TaskStatusEnum.DONE);

        tasks.add(new Task(1, 0,"Code sensei moment","code like crazy, but in a disciplined way.",
                toCal("2022-08-16"), 40));
        tasks.get(1).setStatus(TaskStatusEnum.IN_PROGRESS);
        tasks.add(new Task(2, 0,"Find customers","Advertise in the coffee news. Reach out to Brian.",
                toCal("2022-08-20"), 40));
        tasks.add(new Task(3, 0,"Integrate PayPal","There is an API guide if you search for it on Google.",
                toCal("2022-08-25"), 40));

        tasks.add(new Task(4, 1,"Profit","Make sure you get your rake ready for all the money that will be coming in.",
                toCal("2022-08-30"), 40));

        tasks.add(new Task(5, 1,"Get wood from EG Penners","Pine, Spruce or Fir is fine. NOT treated wood!",
                toCal("2022-08-03"), 40));
        tasks.add(new Task(6, 1,"Purchase handsaw","Check Kijiji and VarageSale. Mark had one for sale also.",
                toCal("2022-08-04"), 40));
        tasks.add(new Task(7, 1,"Cut boards","Use the guide from BoardAtHome magazine (pg 4)",
                toCal("2022-08-12"), 40));
        tasks.add(new Task(8, 1,"Fill with dirt","Call Arnald's Aggregates at 1-204-123-4567 and ask for peat moss and garden soil.",
                toCal("2022-08-18"), 40));
    }

    public void close() {
        System.out.println("Closed " + dbType + " database " + dbName);
    }

    private Calendar toCal(String dateString){
        return toCal(dateString, "00:00:00");
    }

    private Calendar toCal(String dateString, String minuteString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();


        try {
            Date d = sdf.parse(dateString + " " + minuteString + ".000");
            cal.setTime(d);
        } catch (ParseException pe) {System.out.println("bad test data: " + pe.getMessage());}
        return cal;
    }

    public String getProjectSequential(List<Project> projectResult) {
        projectResult.addAll(projects);
        return null;
    }

    public ArrayList<Project> getProjectById(int projectId) {
        ArrayList<Project> result = new ArrayList<>();
        for(Project project : projects) {
            if(project.getId() == projectId) {
                result.add(project);
            }
        }
        return result;
    }

    public String insertProject(Project currentProject) {
        // modify the ID
        currentProject.setId(increment);
        increment++;

        // don't bother checking for duplicates
        projects.add(currentProject);
        return null;
    }

    public String updateProject(Project currentProject) {
        int index;

        index = projects.indexOf(currentProject);
        if (index >= 0)
            projects.set(index, currentProject);
        return null;
    }

    public String deleteProject(Project currentProject) {
        int index;

        index = projects.indexOf(currentProject);
        if (index >= 0)
            projects.remove(index);
        return null;
    }

    public String getTaskSequential(List<Task> taskResult) {
        taskResult.addAll(tasks);
        return null;
    }

    public String getIncompleteTasks(List<Task> taskResult, Project project) {
        for (Task task: tasks) {
            if (task.getProjectId() == project.getId() && task.getStatus() != TaskStatusEnum.DONE)
                taskResult.add(task);
        }
        return null;
    }

    public String getTodoTasks(List<Task> taskResult, Project project) {
        for (Task task: tasks) {
            if (task.getProjectId() == project.getId() && task.getStatus() == TaskStatusEnum.TODO)
                taskResult.add(task);
        }
        return null;
    }

    public String getInProgressTasks(List<Task> taskResult, Project project) {
        for (Task task: tasks) {
            if (task.getProjectId() == project.getId() && task.getStatus() == TaskStatusEnum.IN_PROGRESS)
                taskResult.add(task);
        }
        return null;
    }

    public String getCompletedTasks(List<Task> taskResult, Project project)
    {
        for (Task task: tasks) {
            if (task.getProjectId() == project.getId() && task.getStatus() == TaskStatusEnum.DONE)
                taskResult.add(task);
        }
        return null;
    }



    public ArrayList<Task> getTaskById(int taskId)
    {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getId() == taskId)
                result.add(task);
        }
        return result;
    }

    public String insertTask(Task currentTask)
    {
        // don't bother checking for duplicates
        tasks.add(currentTask);
        return null;
    }

    public String updateTask(Task currentTask)
    {
        int index;

        index = tasks.indexOf(currentTask);
        if (index >= 0)
            tasks.set(index, currentTask);
        return null;
    }

    public String deleteTask(Task currentTask)
    {
        int index;

        index = tasks.indexOf(currentTask);
        if (index >= 0)
            tasks.remove(index);
        return null;
    }
}