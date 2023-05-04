package comp3350.ppa.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp3350.ppa.R;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.objects.Project;
import comp3350.ppa.objects.Task;

public class ProjectViewActivity extends AppCompatActivity {
    public Intent intent;
    int projectId;
    private Project project;
    private AccessProjects accessProjects;
    private AccessTasks accessTasks;
    private ArrayList<Task> inProgressList;
    private ArrayList<Task> toDoList;
    private ArrayList<Task> completedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task_list);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        accessProjects = new AccessProjects();
        accessTasks = new AccessTasks();

        // load project from database
        Bundle bundle = getIntent().getExtras();
        projectId = bundle.getInt("projectId");

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProject();
    }


    private void loadProject() {
        ArrayList<Project> projectsList = new ArrayList<>();
        String result = accessProjects.getProjectById(projectId, projectsList);

        if (result == null) {
            project = projectsList.get(0);
        } else {
            Messages.fatalError(this,result);
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(project.getTitle());

        // set up multiline note text box
        String notes = project.getNotes();
        EditText txtNotes = findViewById(R.id.txtNotes);
        txtNotes.setText(notes);
        txtNotes.setMaxLines(5);
        txtNotes.setSelection(notes.length(),notes.length());

        txtNotes.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                saveNotes();
        });


        // Update timer widget
        GlobalTimerWidget timerWidget = findViewById(R.id.globalTimerWidget);
        timerWidget.setProject(project);

        // Get project from business layer
        inProgressList = new ArrayList<>();
        toDoList = new ArrayList<>();
        completedList = new ArrayList<>();

        String result1 = accessTasks.getCompletedTasks(completedList, project);
        String result2 = accessTasks.getTodoTasks(toDoList, project);
        String result3 = accessTasks.getInProgressTasks(inProgressList, project);
        if (result1 != null) {
            Messages.fatalError(this, result1);
        } else if (result2 != null) {
            Messages.fatalError(this, result2);
        } else if (result3 != null) {
            Messages.fatalError(this, result3);
        } else {
            LinearLayout inProgressLayout = findViewById(R.id.inProgressList);
            inProgressLayout.removeAllViews();
            for (int i = 0; i < inProgressList.size(); i++) {
                inProgressLayout.addView(createTaskCard(i, inProgressList));
            }
            LinearLayout todoLayout = findViewById(R.id.toDoList);
            todoLayout.removeAllViews();
            for (int i = 0; i < toDoList.size(); i++) {
                todoLayout.addView(createTaskCard(i, toDoList));
            }
            LinearLayout completedLayout = findViewById(R.id.completedList);
            completedLayout.removeAllViews();
            for (int i = 0; i < completedList.size(); i++) {
                completedLayout.addView(createTaskCard(i, completedList));
            }
        }
    }
    private String saveNotes(){
        EditText txtNotes = findViewById(R.id.txtNotes);
        String notes = txtNotes.getText().toString();
        project.setNotes(notes);
        return accessProjects.updateProject(project);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String result = saveNotes();
        if (intent != null)
            intent.putExtra("alert","O NO");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int editId = R.id.edit;
        final int deleteId = R.id.delete;

        int id = item.getItemId();
        switch (id){
            case editId:
                navigateToEdit();
                break;

            case deleteId:
                requestDelete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private TaskCard createTaskCard(int position, List<Task> list) {
        TaskCard card = new TaskCard(ProjectViewActivity.this);
        Task task = list.get(position);

        CharSequence dueDate = DateUtils.getRelativeTimeSpanString(
                task.getDueDate().getTimeInMillis(),
                new Date().getTime(),
                DateUtils.MINUTE_IN_MILLIS
        );

        // Set card text
        card.setTitle(task.getTitle());
        card.setDescription(task.getDescription());
        card.setDueDate(dueDate.toString());
        card.setTimeLimit(task.getEstimatedTimeAsString());

        card.setOnClickListener(view -> {
            ProjectViewActivity me = ProjectViewActivity.this;
            me.intent = new Intent(me, TaskAddEditActivity.class);
            Bundle newBundle = new Bundle();
            newBundle.putInt("projectId", projectId);
            newBundle.putInt("taskId", task.getId());
            me.intent.putExtras(newBundle);
            ProjectViewActivity.this.startActivityForResult(me.intent,1);
        });

        return card;
    }

    private void navigateToEdit(){
        intent = new Intent(ProjectViewActivity.this, ProjectAddActivity.class);
        Bundle newBundle = new Bundle();
        newBundle.putInt("projectId",project.getId());
        intent.putExtras(newBundle);
        this.startActivityForResult(intent,1);
    }

    private void requestDelete(){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    delete();
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure to delete '" + project.getTitle() + "'?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void delete(){
        // delete the project
        String result = accessProjects.deleteProject(project);

        // return to activities page
        if (result != null){
            Messages.fatalError(this,"Error in deleting:" + result);
            return;
        }

        intent = new Intent();
        intent.putExtra("alert","Project '" + project.getTitle() + "' successfully deleted.");
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Messages.success(this, data.getStringExtra("alert"));
        }
    }

    public void buttonAddActivityClick(View view){
        intent= new Intent(ProjectViewActivity.this, TaskAddEditActivity.class);
        Bundle newBundle = new Bundle();
        newBundle.putInt("projectId", projectId);
        intent.putExtras(newBundle);
        ProjectViewActivity.this.startActivityForResult(intent,1);
    }
}
