package comp3350.ppa.tests.acceptance;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.ppa.R;
import comp3350.ppa.application.Services;
import comp3350.ppa.business.GlobalTimerAccess;
import comp3350.ppa.presentation.ProjectsListActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StopWorkTimerTests
{
    @Rule
    public ActivityTestRule<ProjectsListActivity> homeActivity = new ActivityTestRule<>(ProjectsListActivity.class);

    @Test
    public void testUseTimerOnProjectScreen() {
        // Enter project view and ensure timer is set properly, and start it
        onView(withText("Make a cool app")).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(withText("30:00")));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("START")))
                .perform(click())
                .check(matches(withText("STOP")));

        // Ensure timer is running
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(not(withText("01:00"))));

        // Go back to the project list and ensure timer is running
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(not(withText("00:30"))));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("STOP")));

        // Go to different project view and ensure timer is running
        onView(withText("Build a garden box")).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(not(withText("60:00"))));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("STOP")));

        // Go to created project view and ensure timer is running
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        onView(withText("Make a cool app")).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(not(withText("30:00"))));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("STOP")));

        // Force timer to end
        GlobalTimerAccess timer = Services.getGlobalTimerAccess();
        timer.setSeconds(2);
        timer.start();
        Espresso.closeSoftKeyboard();
        Espresso.pressBack(); // Change view to force timer to update

        // Ensure text has changed
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(withText("Time to stop work!")));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("START")));
    }

    @Test
    public void testCancelTimerOnProjectView() {
        // Start the timer
        onView(withText("Make a cool app")).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(withText("30:00")));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("START")))
                .perform(click())
                .check(matches(withText("STOP")));

        // Ensure timer is running
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(not(withText("30:00"))));

        // Cancel the timer
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("STOP")))
                .perform(click())
                .check(matches(withText("START")));

        // Ensure timer is not running
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(withText("30:00")));

        // Go back to the project list and ensure timer is not running
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();
        onView(withId(R.id.global_time_left)).check(matches(not(isDisplayed())));

        // Go back to the project view and ensure timer is not running
        onView(withText("Make a cool app")).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.global_time_left)).check(matches(isDisplayed()))
                .check(matches(withText("30:00")));
        onView(withId(R.id.btn_toggle_timer)).check(matches(isDisplayed()))
                .check(matches(withText("START")));

        // Force timer to end
        GlobalTimerAccess timer = Services.getGlobalTimerAccess();
        timer.setSeconds(2);
        Espresso.closeSoftKeyboard();
        Espresso.pressBack(); // Change view to force timer to update

        // Ensure text has not changed
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.global_time_left)).check(matches(not(isDisplayed())));
    }

}
