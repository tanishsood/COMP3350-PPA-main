package comp3350.ppa.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import comp3350.ppa.R;
import comp3350.ppa.application.Main;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.business.StatusCalculations;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;

public class ProjectsListActivity extends AppCompatActivity {
    private AccessProjects accessProjects;
    private AccessTasks accessTasks;
    private ArrayList<Project> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        copyDatabaseToDevice();

        Main.startUp();

        setContentView(R.layout.activity_projects_list);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onResume(){
        super.onResume();
        accessProjects = new AccessProjects();
        accessTasks = new AccessTasks();
        loadProjects();
        GlobalTimerWidget timer = findViewById(R.id.listGlobalTimerWidget);
        timer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Main.shutDown();
    }

    private void loadProjects(){
        // Get project list from business layer
        projectList = new ArrayList<>();
        String result = accessProjects.getProjects(projectList);
        if (result != null) {
            Messages.fatalError(this, result);
        } else {
            // Generates visual list of courses
            ArrayAdapter<Project> projectArrayAdapter = new ArrayAdapter<Project>(this, R.layout.widget_project_card, R.id.projectTitle, projectList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ProjectCard card = new ProjectCard(ProjectsListActivity.this);
                    Project proj = projectList.get(position);
                    List<Task> incompleteTasks;
                    List<Task> completedTasks;
                    CharSequence dueDate = DateUtils.getRelativeTimeSpanString(
                            proj.getDueDate().getTimeInMillis(),
                            Calendar.getInstance().getTimeInMillis(),
                            DateUtils.MINUTE_IN_MILLIS
                    );
                    int totalTasks;

                    // Set text on card
                    card.setTitle(proj.getTitle());
                    card.setDueDate(dueDate.toString());
                    card.setWeeklyEstimate(StatusCalculations.projectWeeklyEstimate(proj));

                    // Get list of incomplete tasks for the current project
                    incompleteTasks = new ArrayList<>();
                    String allTasksResult = accessTasks.getIncompleteTasks(incompleteTasks, proj);
                    if (allTasksResult != null)
                        Messages.fatalError(ProjectsListActivity.this, allTasksResult);

                    // Get list of completed tasks
                    completedTasks = new ArrayList<>();
                    String completedTasksResult = accessTasks.getCompletedTasks(completedTasks, proj);
                    if (completedTasksResult != null)
                        Messages.fatalError(ProjectsListActivity.this, completedTasksResult);

                    if (allTasksResult == null && completedTasksResult == null) {
                        // Set task list
                        String taskOne = incompleteTasks.size() > 0 ? incompleteTasks.get(0).getTitle() : null;
                        String taskTwo = incompleteTasks.size() > 1 ? incompleteTasks.get(1).getTitle() : null;
                        String taskThree = incompleteTasks.size() > 2 ? incompleteTasks.get(2).getTitle() : null;
                        card.setTasks(taskOne, taskTwo, taskThree);

                        // Set task count
                        totalTasks = completedTasks.size() + incompleteTasks.size();
                        card.setTaskCount(completedTasks.size(), totalTasks);
                    }

                    return card;
                }
            };

            final ListView listView = findViewById(R.id.projectsList);
            listView.setAdapter(projectArrayAdapter); // This is when list is generated

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(ProjectsListActivity.this, ProjectViewActivity.class);
                Bundle newBundle = new Bundle();
                Project project = projectList.get(position);
                newBundle.putInt("projectId", project.getId());
                intent.putExtras(newBundle);
                ProjectsListActivity.this.startActivityForResult(intent, 1);
            });
        }
    }

    public void buttonAddActivityClick(View view){
        Intent projectIntent= new Intent(ProjectsListActivity.this, ProjectAddActivity.class);
        ProjectsListActivity.this.startActivityForResult(projectIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null)
            Messages.success(this, data.getStringExtra("alert"));
    }

    // Database methods
    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Main.setDBPathName(dataDirectory.toString() + "/" + Main.dbName);

        } catch (IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
        System.out.println("Successfully copied asset files to device");
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];
            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
