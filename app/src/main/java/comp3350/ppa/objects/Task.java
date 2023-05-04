package comp3350.ppa.objects;

import java.util.Calendar;
import java.util.Objects;

public class Task {
    private int taskId;
    private int projectId;
    private String title;
    private String description;
    private Calendar dueDate;
    private int estimatedTime;
    private TaskStatusEnum status;

    public Task(int taskId){
        this.taskId = taskId;
    }

    public Task(int taskId, int projectId, String title, String body, Calendar dueDate, int estimatedTime) {
        if (title == null || body == null || dueDate == null)
            throw new NullPointerException();
        this.taskId = taskId;
        this.projectId = projectId;
        this.title = title;
        this.description = body;
        this.dueDate = dueDate;
        this.estimatedTime = estimatedTime;
        this.status = TaskStatusEnum.TODO;
    }

    public Task(int taskId, int projectId, String title, String body, Calendar dueDate, int estimatedTime, TaskStatusEnum status) {
        this(taskId, projectId, title, body, dueDate, estimatedTime);
        this.status = status;
    }

    public int getId() {
        return taskId;
    }

    public void setId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null)
            throw new NullPointerException();
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new NullPointerException();
        this.description = description;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        if  (dueDate == null)
            throw new NullPointerException();
        this.dueDate = dueDate;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        if (estimatedTime < 0)
            throw new IllegalArgumentException("Estimated time cannot be negative.");
        this.estimatedTime = estimatedTime;
    }

    public TaskStatusEnum getStatus() {
        if (status == null)
            throw new NullPointerException();
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        if  (status == null)
            throw new NullPointerException();
        this.status = status;
    }

    public String getEstimatedTimeAsString() {
        // 1 h 40 min format
        long time = this.estimatedTime;

        if (time < 0)
            return "";
        if (time > 60 * 24)
            return "24 h +";

        String hh;
        String mm = String.valueOf(time);

        String val = mm + " min";
        if (time >= 60) {
            hh = String.valueOf(time / 60);
            long m = time % 60;
            mm = ((m < 10) ? "0" : "") + m;
            val = hh + " h " + mm + " min";
        }
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return taskId == task.taskId && projectId == task.projectId
                && estimatedTime == task.estimatedTime && Objects.equals(title, task.title)
                && Objects.equals(description, task.description) && Objects.equals(dueDate, task.dueDate)
                && status == task.status;
    }

}

