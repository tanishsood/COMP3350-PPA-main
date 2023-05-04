package comp3350.ppa.presentation;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import comp3350.ppa.R;
import comp3350.ppa.application.Services;
import comp3350.ppa.business.GlobalTimerAccess;
import comp3350.ppa.objects.Project;

public class GlobalTimerWidget extends LinearLayout {
    private GlobalTimerAccess globalTimer;
    private CountDownTimer countDownTimer;
    private Project project;
    private boolean hideOnStopped;

    public GlobalTimerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Inflate the widget_global_timer layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_global_timer, this, true);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.GlobalTimerWidget);
        hideOnStopped = attributes.getBoolean(R.styleable.GlobalTimerWidget_hideOnStopped, false);

        setTimer(30);
        resume();

        // Register button handler
        Button button = findViewById(R.id.btn_toggle_timer);
        button.setOnClickListener(v -> toggleTimer());
    }

    private void setVisible(boolean visible) {
        if (!hideOnStopped) return;
        this.setVisibility(
            (visible) ? View.VISIBLE : View.GONE
        );
    }

    public GlobalTimerWidget(Context context) {
        this(context, null);
    }

    public void resume() {
        if (globalTimer.isRunning()) {
            startCountDownTimer();
            enableStopButton();
            setVisible(true);
        } else if (project != null) {
            setTimer(project.getFocusMinutes() * 60);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void setProject(Project newProject) {
        project = newProject;
        setTimer(project.getFocusMinutes() * 60);
    }

    private void setTimer(int seconds) {
        globalTimer = Services.createGlobalTimerAccess();
        globalTimer.setSeconds(seconds);
        updateTimeRemaining();

        Button btn = findViewById(R.id.btn_toggle_timer);
        btn.setEnabled(seconds != 0);
    }

    private void toggleTimer() {
        if (globalTimer.isRunning()) {
            globalTimer.stop();
            countDownTimer.cancel();
            updateTimeRemaining();
            enableStartButton();
        }
        else {
            globalTimer.start();
            startCountDownTimer();
            enableStopButton();
        }
    }

    private void startCountDownTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(globalTimer.getSecondsRemaining() * 1000L, 500) {
            @Override
            public void onTick(long l) {
                updateTimeRemaining();
            }

            @Override
            public void onFinish() {
                timerFinish();
            }
        };

        countDownTimer.start();
    }

    private void updateTimeRemaining() {
        setTimerText(globalTimer.getRelativeTimeString());
    }

    private void timerFinish() {
        globalTimer.stop();

        TextView msg = findViewById(R.id.global_time_left);
        msg.setText("Time to stop work!");
        enableStartButton();
    }

    private void setTimerText(String text) {
        TextView timeLeft = findViewById(R.id.global_time_left);
        timeLeft.setText(text);
    }

    private void enableStopButton() {
        Button btn = findViewById(R.id.btn_toggle_timer);
        btn.setText( "STOP");
    }

    private void enableStartButton() {
        Button btn = findViewById(R.id.btn_toggle_timer);
        btn.setText("START");
    }
}
