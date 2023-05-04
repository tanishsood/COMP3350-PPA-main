package comp3350.ppa.presentation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import comp3350.ppa.R;

public class TaskCard extends LinearLayout {
    public TaskCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Obtain custom attributes for card data
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TaskCard);
        String taskTitle = attributes.getString(R.styleable.TaskCard_taskTitle);
        String timeLimit = attributes.getString(R.styleable.TaskCard_timeLimit);
        String description = attributes.getString(R.styleable.TaskCard_description);
        String dueDate = attributes.getString(R.styleable.TaskCard_taskDueDate);
        attributes.recycle();

        // Inflate the widget_task_card layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_task_card, this, true);

        // Set text in card
        setTitle(taskTitle);
        setTimeLimit(timeLimit);
        setDescription(description);
        setDueDate(dueDate);
    }

    public TaskCard(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        setText(R.id.taskTitle, title);
    }

    public void setTimeLimit(String timeLimit) {
        setText(R.id.timeLimit, timeLimit);
    }

    public void setDescription(String description) {
        setText(R.id.description, description);
    }

    public void setDueDate(String date) {
        setText(R.id.dueDate, date);
    }

    private void setText(int viewId, String text) {
        TextView view = findViewById(viewId);
        view.setText(text);
    }
}