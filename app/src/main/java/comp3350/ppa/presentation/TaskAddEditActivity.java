package comp3350.ppa.presentation;

import static comp3350.ppa.R.id.rabDone;
import static comp3350.ppa.R.id.rabInProgress;
import static comp3350.ppa.R.id.rabTodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import comp3350.ppa.R;
import comp3350.ppa.business.AccessTasks;
import comp3350.ppa.objects.Task;
import comp3350.ppa.objects.TaskStatusEnum;

public class TaskAddEditActivity extends AppCompatActivity {
    int projectId;
    private AccessTasks accessTasks;
    private Task task;
    boolean isEditScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_add_edit);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // access the database
        accessTasks = new AccessTasks();

        // determine if this is an edit or add screen
        Bundle bundle = getIntent().getExtras();
        projectId = bundle.getInt("projectId");
        isEditScreen = bundle.containsKey("taskId");
        if (isEditScreen) {
            int taskId = bundle.getInt("taskId");
            loadTask(taskId);
        }

        // Get a support ActionBar corresponding to this toolbar
        // https://developer.android.com/training/appbar/up-action
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        // cause date picker to pop up on focus
        EditText dueDate = findViewById(R.id.datDueDate);
        if (!isEditScreen) {
            dueDate.setText(CustomDatePicker.getTodayAsString());
            ((RadioButton)findViewById(rabTodo)).setChecked(true);
        }
        dueDate.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus)
                return;
            CustomDatePicker.getDate(this,dueDate);
        });
    }

    private void loadTask(int taskId){
        ArrayList<Task> taskList = new ArrayList<>();
        String result = accessTasks.getTaskById(taskId, taskList);
        if (result == null) {
            // successfully fetched from database
            task = taskList.get(0);
            setTitle("PPA: Edit Task");
            tryPopulateFields();

        }  else {
            Messages.warning(this,result);
        }
    }

    private void tryPopulateFields() {
        if (task == null) {
            Messages.fatalError(this,"There is no task to populate. Please go back and try again.");
            return;
        }

        String name = task.getTitle();
        String description = task.getDescription();
        Calendar dueDate = task.getDueDate();
        String dueDateString = CustomDatePicker.makeDateString(
                dueDate.get(Calendar.DAY_OF_MONTH), dueDate.get(Calendar.MONTH) + 1, dueDate.get(Calendar.YEAR));
        String estimatedTime = String.valueOf(task.getEstimatedTime());

        ((EditText)findViewById(R.id.txtTitle)).setText(name);

        switch (task.getStatus()){
            case DONE:
                ((RadioButton)findViewById(rabDone)).setChecked(true);
                break;
            case IN_PROGRESS:
                ((RadioButton)findViewById(rabInProgress)).setChecked(true);
                break;
            case TODO:
                ((RadioButton)findViewById(rabTodo)).setChecked(true);
                break;
        }

        ((EditText)findViewById(R.id.txtDescription)).setText(description);
        ((EditText)findViewById(R.id.datDueDate)).setText(dueDateString);
        ((EditText)findViewById(R.id.numEstimatedTime)).setText(estimatedTime);
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
        String result = tryFetchTask();
        if (result != null) {
            Messages.warning(this,result);
            return;
        }

        result = (isEditScreen)
            ? accessTasks.updateTask(task)
            : accessTasks.insertTask(task);

        if (result != null) {
            Messages.warning(this,result);
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("alert","Task successfully " + (isEditScreen ? "updated" : "created") + ".");
        setResult(RESULT_OK,intent);
        finish();
    }

    private  String tryFetchTask() {
        try {
            EditText txtTitle = findViewById(R.id.txtTitle);
            String title = txtTitle.getText().toString();

            EditText txtDescription = findViewById(R.id.txtDescription);
            String description = txtDescription.getText().toString();

            EditText txtMinutes = findViewById(R.id.numEstimatedTime);
            int minutes  = Integer.parseInt(txtMinutes.getText().toString());

            EditText datDueDate= findViewById(R.id.datDueDate);
            Calendar date = CustomDatePicker.makeCalendar(datDueDate.getText().toString());

            TaskStatusEnum status;
            RadioGroup radio = findViewById(R.id.rabStatus);
            switch (radio.getCheckedRadioButtonId()) {
                case rabTodo:
                    status = TaskStatusEnum.TODO;
                    break;
                case rabInProgress:
                    status = TaskStatusEnum.IN_PROGRESS;
                    break;
                case rabDone:
                    status = TaskStatusEnum.DONE;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + radio.getCheckedRadioButtonId());
            }

            int id = (isEditScreen) ? task.getId() : 0;

            task = new Task(id,projectId, title,description, date, minutes, status);
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
