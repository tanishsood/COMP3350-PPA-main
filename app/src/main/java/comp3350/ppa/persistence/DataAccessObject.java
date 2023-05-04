package comp3350.ppa.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;

public class DataAccessObject implements DataAccess {
    private Statement st1;
    private Connection c1;
    private ResultSet rs2;
    private ResultSet rs3;

    private final String dbName;
    private String dbType;

    private ArrayList<Project> projects;
    private ArrayList<Task> tasks;

    private String cmdString;
    private int updateCount;
    private String result;

    public DataAccessObject(String dbName) {
        this.dbName = dbName;
    }

    public void open(String dbPath) {
        String url;
        try {
            // Setup for HSQL
            dbType = "HSQL";
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            url = "jdbc:hsqldb:file:" + dbPath; // stored on disk mode
            c1 = DriverManager.getConnection(url, "ADMIN", "");
            st1 = c1.createStatement();
        }
        catch (Exception e) {
            processSQLError(e);
        }
        System.out.println("Opened " +dbType +" database " +dbPath);
    }
    public void close() {
        try {	// commit all changes to the database
            cmdString = "shutdown compact";
            rs2 = st1.executeQuery(cmdString);
            c1.close();
        }
        catch (Exception e) {
            processSQLError(e);
        }
        System.out.println("Closed " +dbType +" database " +dbName);
    }

    public String getProjectSequential(List<Project> projectResult) {
        Project project;
        result = null;

        try {
            cmdString = "SELECT * from Projects";
            rs2 = st1.executeQuery(cmdString);
        }
        catch (Exception e) {
            processSQLError(e);
        }

        try {
            while (rs2.next()) {
                project = getProject(rs2);
                projectResult.add(project);
            }
            rs2.close();
        }
        catch (SQLException e) {
            result = processSQLError(e);
        }
        catch (ParseException e) {
            result = e.getMessage();
        }

        return result;
    }

    public ArrayList<Project> getProjectById(int projectId) {
        Project project;

        projects = new ArrayList<>();
        try {
            cmdString = "SELECT * from Projects where ProjectID=" + projectId;
            rs3 = st1.executeQuery(cmdString);
            while (rs3.next()) {
                project = getProject(rs3);
                projects.add(project);
            }
            rs3.close();
        } catch (SQLException e) {
            processSQLError(e);
        } catch (ParseException e) {
            return null;
        }
        return projects;
    }

    public String insertProject(Project currentProject) {
        String values;

        result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String projectDueDateString = sdf.format(currentProject.getDueDate().getTime());

            values = "'" + currentProject.getTitle().replace("'","''")
                +"', '" + currentProject.getDescription().replace("'","''")
                +"', '" + projectDueDateString
                +"', " + currentProject.getFocusMinutes()
                +", '" + currentProject.getNotes().replace("'","''")
                +"'";
            cmdString = "INSERT into Projects " +" Values(null, " +values +")";

            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String updateProject(Project currentProject) {
        String values;
        String where;

        result = null;
        try {
            // Should check for empty values and not update them
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String projectDueDateString = sdf.format(currentProject.getDueDate().getTime());

            values = "Name='" + currentProject.getTitle().replace("'","''")
                    + "', Description='" + currentProject.getDescription().replace("'","''")
                    + "', DueDate='" + projectDueDateString
                    + "', FocusMinutes=" + currentProject.getFocusMinutes()
                    + ", Notes='" + currentProject.getNotes().replace("'","''")
                    +"'";
            where = "ProjectID=" + currentProject.getId();
            cmdString = "UPDATE Projects SET " +values +" WHERE " +where;

            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String deleteProject(Project currentProject) {
        String values;

        result = null;
        try {
            values = "" + currentProject.getId();
            cmdString = "DELETE FROM Projects where ProjectID=" +values;
            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String getTaskSequential(List<Task> taskResult) {
        Task task;
        result = null;

        try {
            cmdString = "SELECT * from tasks";
            rs2 = st1.executeQuery(cmdString);
        }
        catch (Exception e) {
            processSQLError(e);
        }

        try {
            while (rs2.next()) {
                task = getTask(rs2);
                taskResult.add(task);
            }
            rs2.close();
        }
        catch (SQLException e) {
            result = processSQLError(e);
        }
        catch (ParseException e) {
            result = e.getMessage();
        }

        return result;
    }

    private String getTaskByStatus(List<Task> taskResult, Project project, TaskStatusEnum[] currentTaskStatuses) {
        Task task;
        StringBuilder in = new StringBuilder();

        for (TaskStatusEnum status : currentTaskStatuses)
            in.append("'").append(status).append("',");
        in = new StringBuilder(in.substring(0, in.length() - 1)); // lop off final comma

        result = null;

        try {
            cmdString = "SELECT * FROM tasks WHERE ProjectID=" + project.getId() + " AND Status IN (" + in + ")";
            rs2 = st1.executeQuery(cmdString);
        } catch (Exception e) {
            processSQLError(e);
        }

        try {
            while (rs2.next()) {
                task = getTask(rs2);
                taskResult.add(task);
            }
            rs2.close();
        } catch (SQLException e) {
            result = processSQLError(e);
        } catch (ParseException e) {
            result = e.getMessage();
        }


        return result;
    }

    public String getIncompleteTasks(List<Task> taskResult, Project project) {
        return getTaskByStatus(taskResult, project,
                new TaskStatusEnum[]{TaskStatusEnum.TODO,TaskStatusEnum.IN_PROGRESS});
    }

    public String getTodoTasks(List<Task> taskResult, Project project) {
        return getTaskByStatus(taskResult, project,
                new TaskStatusEnum[]{TaskStatusEnum.TODO});
    }

    public String getInProgressTasks(List<Task> taskResult, Project project) {
        return getTaskByStatus(taskResult, project,
                new TaskStatusEnum[]{TaskStatusEnum.IN_PROGRESS});
    }

    public String getCompletedTasks(List<Task> taskResult, Project project) {
        return getTaskByStatus(taskResult, project,
                new TaskStatusEnum[]{TaskStatusEnum.DONE});    }

    public ArrayList<Task> getTaskById(int taskId) {
        Task task;

        tasks = new ArrayList<>();
        try {
            cmdString = "SELECT * from Tasks where TaskID=" + taskId;
            rs3 = st1.executeQuery(cmdString);
            while (rs3.next()) {
                task = getTask(rs3);
                tasks.add(task);
            }
            rs3.close();
        } catch (SQLException e) {
            processSQLError(e);
        } catch (ParseException e) {
            return null;
        }
        return tasks;
    }

    public String insertTask(Task currentTask) {
        String values;

        result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String taskDueDateString = sdf.format(currentTask.getDueDate().getTime());

            values = "'" + currentTask.getTitle().replace("'", "''")
                +"', '" + currentTask.getDescription().replace("'","''")
                +"', '" + taskDueDateString
                +"', '" + currentTask.getStatus()
                +"', " + currentTask.getEstimatedTime()
                +", " + currentTask.getProjectId()
                +"";

            cmdString = "INSERT into Tasks " +" Values(null," +values +")";
            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String updateTask(Task currentTask) {
        String values;
        String where;

        result = null;
        try {
            // Should check for empty values and not update them
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String taskDueDateString = sdf.format(currentTask.getDueDate().getTime());
            int estimatedTime = currentTask.getEstimatedTime();

            values = "Title='" + currentTask.getTitle().replace("'","''")
                    + "', Description='" + currentTask.getDescription().replace("'","''")
                    + "', DueDate='" + taskDueDateString
                    + "', Status='" + currentTask.getStatus()
                    + "', EstimatedTime=" + estimatedTime
                    + ", ProjectID=" + currentTask.getProjectId()
                    + "";
            where = "TaskID=" + currentTask.getId();

            cmdString = "UPDATE Tasks " +" SET " +values +" WHERE " +where;

            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String deleteTask(Task currentTask) {
        String values;

        result = null;
        try {
            values = "" + currentTask.getId();
            cmdString = "DELETE FROM Tasks where TaskID=" +values;
            updateCount = st1.executeUpdate(cmdString);
            result = checkWarning(st1, updateCount);

            cmdString = "COMMIT";
            updateCount = st1.executeUpdate(cmdString);
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        return result;
    }

    public String checkWarning(Statement st, int updateCount) {
        String result;

        result = null;
        try {
            SQLWarning warning = st.getWarnings();
            if (warning != null)
                result = warning.getMessage();
        }
        catch (Exception e) {
            result = processSQLError(e);
        }
        if (updateCount != 1)
            result = "Tuple not inserted correctly.";
        return result;
    }


    public String processSQLError(Exception e) {
        String result = "*** SQL Error: " + e.getMessage();

        // Remember, this will NOT be seen by the user!
        e.printStackTrace();

        return result;
    }

    private Project getProject(ResultSet rs) throws SQLException, ParseException {
        Project project;
        int projectId = rs.getInt("ProjectId");
        String projectName = rs.getString("Name");
        String projectDescription = rs.getString("Description");
        String projectNotes = rs.getString("Notes");
        String projectDueDateString = rs.getString("DueDate");
        int projectFocusMinutes;

        Calendar projectDueDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date parsedDate = sdf.parse(projectDueDateString);
        if (parsedDate == null)
            throw new ParseException("Error parsing project due date", 0);
        projectDueDate.setTime(parsedDate);
        projectFocusMinutes = rs.getInt("FocusMinutes");

        project = new Project(projectId,projectName,projectDescription,projectNotes,projectDueDate,projectFocusMinutes);
        return project;
    }

    private Task getTask(ResultSet rs) throws SQLException, ParseException {
        Task task;
        int taskId = rs.getInt("TaskID");
        int taskProjectId = rs.getInt("ProjectID");
        String taskTitle = rs.getString("Title");
        String taskBody = rs.getString("Description");
        String taskDueDateString = rs.getString("DueDate");
        int taskEstimatedTime = rs.getInt("EstimatedTime");
        TaskStatusEnum taskStatus = TaskStatusEnum.valueOf(rs.getString("Status"));

        Calendar taskDueDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date parsedDate = sdf.parse(taskDueDateString);
        if (parsedDate == null)
            throw new ParseException("Error parsing task due date", 0);
        taskDueDate.setTime(parsedDate);
        task = new Task(taskId, taskProjectId, taskTitle, taskBody, taskDueDate, taskEstimatedTime);
        task.setStatus(taskStatus);
        return task;
    }
}
