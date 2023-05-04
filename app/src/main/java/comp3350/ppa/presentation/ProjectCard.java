package comp3350.ppa.presentation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import comp3350.ppa.R;

public class ProjectCard extends LinearLayout {
    public ProjectCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Obtain custom attributes for card data
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ProjectCard);
        String projectTitle = attributes.getString(R.styleable.ProjectCard_projectTitle);
        int completedTasks = attributes.getInt(R.styleable.ProjectCard_completedTasks, 0);
        int totalTasks = attributes.getInt(R.styleable.ProjectCard_totalTasks, 0);
        String nextTask1 = attributes.getString(R.styleable.ProjectCard_nextTask1);
        String nextTask2 = attributes.getString(R.styleable.ProjectCard_nextTask2);
        String nextTask3 = attributes.getString(R.styleable.ProjectCard_nextTask3);
        String dueDate = attributes.getString(R.styleable.ProjectCard_dueDate);
        attributes.recycle();

        // Inflate the widget_project_card layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_project_card, this, true);

        // Set text in card
        setTitle(projectTitle);
        setTaskCount(completedTasks, totalTasks);
        setTaskText(R.id.nextTask1, nextTask1);
        setTaskText(R.id.nextTask2, nextTask2);
        setTaskText(R.id.nextTask3, nextTask3);
        setDueDate(dueDate);
    }

    public ProjectCard(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        setText(R.id.projectTitle, title);
    }

    public void setTaskCount(int completedTasks, int totalTasks) {
        setText(R.id.numTasks, String.format(Locale.ENGLISH, "%d/%d", completedTasks, totalTasks));
    }

    public void setTasks(String task1, String task2, String task3) {
        setTaskText(R.id.nextTask1, task1);
        setTaskText(R.id.nextTask2, task2);
        setTaskText(R.id.nextTask3, task3);
    }

    public void setTaskText(int viewId, String task) {
        if (task == null) {
            hideText(viewId);
        } else {
            setText(viewId, task);
            unhideText(viewId);
        }
    }

    public void setDueDate(String date) {
        setText(R.id.dueDate, date);
    }

    public void setWeeklyEstimate(int minutes) {
        setText(R.id.weeklyEstimate, Integer.toString(minutes));
    }

    private void setText(int viewId, String text) {
        TextView view = findViewById(viewId);
        view.setText(text);
    }

    private void hideText(int viewId) {
        TextView view = findViewById(viewId);
        view.setVisibility(GONE);
    }

    private void unhideText(int viewId) {
        TextView view = findViewById(viewId);
        view.setVisibility(VISIBLE);
    }
}
