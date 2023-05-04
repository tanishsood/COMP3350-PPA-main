package comp3350.ppa.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.R;
import comp3350.ppa.business.AccessProjects;
import comp3350.ppa.objects.Project;

public class ProjectAddActivity extends AppCompatActivity {
    private AccessProjects accessProjects;
    private Project project;
    boolean isEditScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_add_edit);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // access the database
        accessProjects = new AccessProjects();

        // determine if this is an edit or add screen
        Bundle bundle = getIntent().getExtras();
        isEditScreen = bundle != null && bundle.containsKey("projectId");
        if (isEditScreen) {
            int projectId = bundle.getInt("projectId");
            loadProject(projectId);
        }

        // Get a support ActionBar corresponding to this toolbar
        // https://developer.android.com/training/appbar/up-action
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        // initialize date picker

        // cause date picker to pop up on focus
        EditText dueDate = findViewById(R.id.datDueDate);
        if (!isEditScreen)
            dueDate.setText(CustomDatePicker.getTodayAsString());
        dueDate.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus)
                return;

            EditText datDueDate = findViewById(R.id.datDueDate);
            CustomDatePicker.getDate(this,datDueDate);
        });
    }

    private void loadProject(int projectId) {
        ArrayList<Project> projectList = new ArrayList<>();
        String result = accessProjects.getProjectById(projectId, projectList);
        if (result == null) {
            // successfully fetched from database
            project = projectList.get(0);
            setTitle("PPA: Edit Project");
            tryPopulateFields();
        } else {
            Messages.warning(this,result);
        }
    }

    private void tryPopulateFields() {
        if (project == null) {
            Messages.fatalError(this,"There is no project to populate. Please go back and try again.");
            return;
        }

        String name = project.getTitle();
        String description = project.getDescription();
        String focusMinutes = String.valueOf(project.getFocusMinutes());
        Calendar dueDate = project.getDueDate();
        String dateString = CustomDatePicker.makeDateString(
                dueDate.get(Calendar.DAY_OF_MONTH), dueDate.get(Calendar.MONTH) + 1, dueDate.get(Calendar.YEAR));

        ((EditText)findViewById(R.id.txtTitle)).setText(name);
        ((EditText)findViewById(R.id.txtDescription)).setText(description);
        ((EditText)findViewById(R.id.datDueDate)).setText(dateString);
        ((EditText)findViewById(R.id.numFocusMinutes)).setText(focusMinutes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int action_submit = R.id.action_submit;
        // https://developer.android.com/training/appbar/actions
        // User chose the submit item, show the app settings UI...

        switch (item.getItemId()) {
            case action_submit:
                ActionSubmitClicked();
                return(true);

            case android.R.id.home:
                finish();
                return (true);
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public void ActionSubmitClicked(){
        String result = tryFetchProject();
        if (result != null) {
            Messages.warning(this,result);
            return;
        }

        result = (isEditScreen)
                ? accessProjects.updateProject(project)
                : accessProjects.insertProject(project);

        if (result != null) {
            Messages.warning(this,result);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("alert","Project '" + project.getTitle() + "' successfully " + (isEditScreen ? "updated" : "created") + ".");
        setResult(RESULT_OK,intent);
        finish();
    }

    private  String tryFetchProject() {
        try {
            EditText txtTitle = findViewById(R.id.txtTitle);
            String title = txtTitle.getText().toString();

            EditText txtDescription = findViewById(R.id.txtDescription);
            String description = txtDescription.getText().toString();
            String notes = (isEditScreen) ? project.getNotes() : "";
            EditText txtMinutes = findViewById(R.id.numFocusMinutes);
            int minutes  = Integer.parseInt(txtMinutes.getText().toString());

            EditText datDueDate= findViewById(R.id.datDueDate);
            Calendar date = CustomDatePicker.makeCalendar(datDueDate.getText().toString());

            int id = (isEditScreen) ? project.getId() : 0;

            project = new Project(id,title, description, notes, date, minutes);
            return null;
        } catch (ParseException e) {
            return "Error in date input. Please input the date again.";
        } catch(NumberFormatException e) {
            return "Error in number input. Please only input numbers.";
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }


}
