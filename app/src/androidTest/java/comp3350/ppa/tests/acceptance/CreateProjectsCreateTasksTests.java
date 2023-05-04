package comp3350.ppa.tests.acceptance;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;

import android.widget.DatePicker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.ppa.R;
import comp3350.ppa.presentation.ProjectsListActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateProjectsCreateTasksTests
{

    @Rule
    public ActivityTestRule<ProjectsListActivity> homeActivity = new ActivityTestRule<>(ProjectsListActivity.class);

    @Test
    public void testProjectListScreen() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed())).check(matches(hasDescendant(withText("My Projects"))));
        onView(withId(R.id.btnAddProject)).check(matches(isDisplayed())).check(matches(isEnabled()));
        onView(withText("Make a cool app")).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateAndDeleteProject() {
        onView(withId(R.id.btnAddProject)).perform(click());
        onView(withText("PPA: Add Project")).check(matches(isDisplayed()));

        // test adding project name
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Project 5"))
                .check(matches(withText("New Test Project 5")));

        // test adding description
        onView(withId(R.id.txtDescription)).check(matches(withText("")))
                .perform(clearText(), typeText("This is a test project"))
                .check(matches(withText("This is a test project")));

        // test due date
        onView(withId(R.id.datDueDate)).perform(click());
        onView(instanceOf(DatePicker.class)).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2022, 8, 24));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));
        // ensure cancel doesn't change the date TODO: This currently fails
        onView(withId(R.id.datDueDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2023, 9, 3));
        onView(withText("CANCEL")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));

        // test focus duration
        onView(withId(R.id.numFocusMinutes)).check(matches(withText("30")))
                .perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Project 5' successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        // Should be back on main screen, with new project added
        onView(withText("New Test Project 5")).check(matches(isDisplayed()));

        // Delete the project
        onView(withText("New Test Project 5")).perform(click());
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Delete")).perform(click());

        onView(withText("Are you sure to delete 'New Test Project 5'?")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());

        // Should be back on main screen, with new project removed
        onView(withText("New Test Project 5")).check(doesNotExist());
    }
    private void accessTaskEditScreen(){
        onView(withText("Make a cool app")).perform(click());
        onView(withText("Code sensei moment")).perform(click());
    }

    private void accessTaskAddScreen(){
        onView(withText("Make a cool app")).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnAddTask)).perform(click());
    }

    private void modifyTaskFields(int id){

        // test adding project name
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("New Test Task " + id))
                .check(matches(withText("New Test Task " + id)));

        onView(withId(R.id.rabTodo)).check(matches(withText("Todo")))
                .perform(click())
                .check(matches(isChecked()));

        onView(withId(R.id.rabInProgress)).check(matches(withText("In-Progress")))
                .perform(click())
                .check(matches(isChecked()));

        // test adding description
        onView(withId(R.id.txtDescription)).perform(clearText(), typeText("This is a test task"))
                .check(matches(withText("This is a test task")));

        // test due date
        onView(withId(R.id.datDueDate)).perform(click());
        onView(instanceOf(DatePicker.class)).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2022, 8, 24));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));
        // ensure cancel doesn't change the date TODO: This currently fails
        onView(withId(R.id.datDueDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2023, 9, 3));
        onView(withText("CANCEL")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));

        // test task duration
        onView(withId(R.id.numEstimatedTime)).perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));


    }

    @Test
    public void testEditingTask() {
        // test changing some fields then backing out of the screen
        accessTaskEditScreen();
        modifyTaskFields(1);
        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());
        // Should have a dialog to clear
        onView(withText("Task successfully updated.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        Espresso.pressBack();
        onView(withText("New Test Task 1")).check(matches(isDisplayed()));
        onView(withText("Code sensei moment")).check(doesNotExist());

        // Change task back
        onView(withText("New Test Task 1")).perform(click());
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("Code sensei moment"))
                .check(matches(withText("Code sensei moment")));
        onView(withId(R.id.action_submit)).perform(click());
        onView(withText("Task successfully updated.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        // Ensure task was changed back
        onView(withText("New Test Task 1")).check(doesNotExist());
        onView(withText("Code sensei moment")).check(matches(isDisplayed()));
    }

    @Test
    public void testCancelEditingTask() {
        // test changing some fields then backing out of the screen
        accessTaskEditScreen();
        modifyTaskFields(1);

        Espresso.closeSoftKeyboard();
        Espresso.pressBack();

        // Ensure edits were not made
        onView(withText("New Test Task 1")).check(doesNotExist());
        onView(withText("Code sensei moment")).check(matches(isDisplayed()));
    }

    @Test
    public void testSavingAddTask() {
        // fill out fields (title, status, description, due date, estimated time
        accessTaskAddScreen();
        modifyTaskFields(2);
        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());
        // Should have a dialog to clear
        onView(withText("Task successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withText("New Test Task 2")).check(matches(isDisplayed()));
    }

    @Test
    public void testCancellingAddTask() {
        // test changing some fields then backing out of the screen
        accessTaskAddScreen();
        modifyTaskFields(3);
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();   // back out without finishing

        // ensure that the task is not there!
        onView(withText("New Test Task 3"))
            .check(doesNotExist());
    }
}
