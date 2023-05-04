package comp3350.ppa.business;

import java.util.ArrayList;
import java.util.List;

import comp3350.ppa.application.Main;
import comp3350.ppa.application.Services;
import comp3350.ppa.objects.Project;
import comp3350.ppa.persistence.DataAccess;

public class AccessProjects {
    private final DataAccess dataAccess;

    // Default constructor uses Main.dbName
    public AccessProjects() {
        this(Main.dbName);
    }

    // Constructor to allow tests to use a different db
    public AccessProjects(String dbName) {
        if (dbName == null)
            throw new NullPointerException();
        dataAccess = Services.getDataAccess(dbName);
    }

    public String getProjects(List<Project> projects) {
        projects.clear();
        return dataAccess.getProjectSequential(projects);
    }

    public String insertProject(Project newProject) {
        if (newProject == null)
            throw new NullPointerException();
        try {
            DataValidator.tryValidateProject(newProject);
        }
        catch (ValidationException e) {
            return e.getMessage();
        }
        return dataAccess.insertProject(newProject);
    }

    public String deleteProject(Project project) {
        if (project == null)
            throw new NullPointerException();
        return dataAccess.deleteProject(project);
    }

    public String getProjectById(int projectId, ArrayList<Project> projects) {
        Project project;
        ArrayList<Project> newProjects = dataAccess.getProjectById(projectId);

        if (projects == null)
            throw new NullPointerException();

        if (newProjects.size()==1) {
            project = newProjects.get(0);
        } else if (newProjects.size() == 0) {
            return "No project found with the id: " + projectId;
        } else {
            return "Project error. Multiple projects with the same ID: " + projectId;
        }
        projects.clear();
        projects.add(project);
        return null;
    }

    public String updateProject(Project newProject) {
        if (newProject == null)
            throw new NullPointerException();
        try {
            DataValidator.tryValidateProject(newProject);
        }
        catch (ValidationException e) {
            return e.getMessage();
        }
        return dataAccess.updateProject(newProject);
    }
}
