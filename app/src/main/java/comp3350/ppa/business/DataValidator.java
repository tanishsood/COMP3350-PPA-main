package comp3350.ppa.business;

import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;

public class DataValidator {
    static public void tryValidateProject(Project project) throws ValidationException{
        if (project.getId() < 0)
            throw new ValidationException("Project ID must be larger than 0");
        if (project.getTitle().equals(""))
            throw new ValidationException("Please enter a title");
        if (project.getFocusMinutes() == 0)
            throw new ValidationException("Project Focus Minutes must be larger than 0");
        if (project.getTitle().length() > 255)
            throw new ValidationException("Project name must be less than 255 characters");
        if (project.getDescription().length() > 1024)
            throw new ValidationException("Project description must be less than 1024 characters");
    }

    public static void tryValidateTask(Task currentTask) throws ValidationException {
        if (currentTask.getId() < 0)
            throw new ValidationException("Task ID must be larger than 0");
        if (currentTask.getTitle().equals(""))
            throw new ValidationException("Please enter a title");
        if (currentTask.getTitle().length() > 255)
            throw new ValidationException("Task title must be less than 255 characters");
        if (currentTask.getDescription().length() > 1024)
            throw new ValidationException("Task description must be less than 1024 characters");
        if (currentTask.getEstimatedTime() <= 0)
            throw new ValidationException("Task must have an estimated time greater than 0");
    }
}
