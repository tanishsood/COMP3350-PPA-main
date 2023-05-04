package comp3350.ppa.tests.acceptance;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.ppa.R;
import comp3350.ppa.presentation.ProjectsListActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditProjectsEditTasksTests {
    @Rule
    public ActivityTestRule<ProjectsListActivity> homeActivity = new ActivityTestRule<>(ProjectsListActivity.class);

    @Test
    public void testCreateAndEditTasks() {

        //Test adding the Project
        onView(withId(R.id.btnAddProject)).perform(click());
        onView(withText("PPA: Add Project")).check(matches(isDisplayed()));

        // test adding project name
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Project 1"))
                .check(matches(withText("New Test Project 1")));

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

        // test focus duration
        onView(withId(R.id.numFocusMinutes)).check(matches(withText("30")))
                .perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Project 1' successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //Validating that project exists
        onView(withText("New Test Project 1")).check(matches(isDisplayed()));

        //Click on the project
        onView(withText("New Test Project 1")).perform(click());

        Espresso.closeSoftKeyboard();

        //Test adding the Task
       onView(withId(R.id.btnAddTask)).perform(click());

        // test adding task title
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Task 1"))
                .check(matches(withText("New Test Task 1")));


        // test adding description
        onView(withId(R.id.txtDescription)).check(matches(withText("")))
                .perform(clearText(), typeText("This is a test task"))
                .check(matches(withText("This is a test task")));

        // test due date
        onView(withId(R.id.datDueDate)).perform(click());
        onView(instanceOf(DatePicker.class)).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2022, 8, 24));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));

        // test estimated time
        onView(withId(R.id.numEstimatedTime)).check(matches(withText("30")))
                .perform(clearText(), typeText("40"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Task successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        Espresso.closeSoftKeyboard();

        //Validating that task exists
        onView(withText("New Test Task 1")).check(matches(isDisplayed()));

        //Test adding the Task
        onView(withId(R.id.btnAddTask)).perform(click());

        // test adding task title
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Task 2"))
                .check(matches(withText("New Test Task 2")));


        // test adding description
        onView(withId(R.id.txtDescription)).check(matches(withText("")))
                .perform(clearText(), typeText("This is a second test task"))
                .check(matches(withText("This is a second test task")));

        // test due date
        onView(withId(R.id.datDueDate)).perform(click());
        onView(instanceOf(DatePicker.class)).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2022, 8, 24));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));

        // test estimated time
        onView(withId(R.id.numEstimatedTime)).check(matches(withText("30")))
                .perform(clearText(), typeText("40"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Task successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        Espresso.closeSoftKeyboard();

        //Validating that task exists
        onView(withText("New Test Task 2")).check(matches(isDisplayed()));

        //Test adding the Task
        onView(withId(R.id.btnAddTask)).perform(click());

        // test adding task title
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Task 3"))
                .check(matches(withText("New Test Task 3")));


        // test adding description
        onView(withId(R.id.txtDescription)).check(matches(withText("")))
                .perform(clearText(), typeText("This is a third test task"))
                .check(matches(withText("This is a third test task")));

        // test due date
        onView(withId(R.id.datDueDate)).perform(click());
        onView(instanceOf(DatePicker.class)).check(matches(isDisplayed()))
                .perform(PickerActions.setDate(2022, 8, 24));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.datDueDate)).check(matches(withText("2022-08-24")));

        // test estimated time
        onView(withId(R.id.numEstimatedTime)).check(matches(withText("30")))
                .perform(clearText(), typeText("40"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Task successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        Espresso.closeSoftKeyboard();

        //Validating that task exists
        onView(withText("New Test Task 3")).check(matches(isDisplayed()));

        //Click on the task to edit
        onView(withText("New Test Task 2")).perform(click());

        // test editing project name
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("New Test Edited Task 2"));

        // test editing description
        onView(withId(R.id.txtDescription)).perform(clearText(), typeText("This is a edited test task"));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Task successfully updated.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //Validating that edited task exists
        onView(withText("New Test Edited Task 2")).check(matches(isDisplayed()));

        //Click on the edited task
        onView(withText("New Test Edited Task 2")).perform(click());

        // test edited project name
        onView(withId(R.id.txtTitle)).check(matches(withText("New Test Edited Task 2")));

        // test edited description
        onView(withId(R.id.txtDescription)).check(matches(withText("This is a edited test task")));

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());

        //Testing editing the fields but don't save it
        onView(withText("New Test Task 1")).perform(click());

        // test editing task title
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("abcd"))
                .check(matches(withText("abcd")));

        // test editing description
        onView(withId(R.id.txtDescription)).perform(clearText(), typeText("abcde"))
                .check(matches(withText("abcde")));

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());

        //Validating that task exists
        onView(withText("abcd")).check(doesNotExist());

        //Check if fields changed
        onView(withText("New Test Task 1")).perform(click());

        // check the task title
        onView(withId(R.id.txtTitle)).check(matches(withText("New Test Task 1")));

        // check the description
        onView(withId(R.id.txtDescription)).check(matches(withText("This is a test task")));

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
    }

    @Test
    public void testCreateAndEditProjects() {

        //Test adding new project
        onView(withId(R.id.btnAddProject)).perform(click());
        onView(withText("PPA: Add Project")).check(matches(isDisplayed()));

        // test adding project name
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Project 2"))
                .check(matches(withText("New Test Project 2")));

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

        // test focus duration
        onView(withId(R.id.numFocusMinutes)).check(matches(withText("30")))
                .perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Project 2' successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //Validating that project exists
        onView(withText("New Test Project 2")).check(matches(isDisplayed()));

        //Test adding new project
        onView(withId(R.id.btnAddProject)).perform(click());
        onView(withText("PPA: Add Project")).check(matches(isDisplayed()));

        // test adding project name
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Project 3"))
                .check(matches(withText("New Test Project 3")));

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

        // test focus duration
        onView(withId(R.id.numFocusMinutes)).check(matches(withText("30")))
                .perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Project 3' successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //Validating that project exists
        onView(withText("New Test Project 3")).check(matches(isDisplayed()));

        //Test adding new project
        onView(withId(R.id.btnAddProject)).perform(click());
        onView(withText("PPA: Add Project")).check(matches(isDisplayed()));

        // test adding project name
        onView(withId(R.id.txtTitle)).check(matches(withText("")))
                .perform(clearText(), typeText("New Test Project 4"))
                .check(matches(withText("New Test Project 4")));

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

        // test focus duration
        onView(withId(R.id.numFocusMinutes)).check(matches(withText("30")))
                .perform(clearText(), typeText("40 this should not get typed"))
                .check(matches(withText("40")));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Project 4' successfully created.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //Validating that project exists
        onView(withText("New Test Project 4")).check(matches(isDisplayed()));

        //Click on the project to edit
        onView(withText("New Test Project 2")).perform(click());
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Edit")).perform(click());

        // test editing project name
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("New Test Edited Project 2"));

        // test editing description
        onView(withId(R.id.txtDescription)).perform(clearText(), typeText("This is a edited test project"));

        // test saving
        // To finish, click the green check mark to save
        onView(withId(R.id.action_submit)).perform(click());

        // Should have a dialog to clear
        onView(withText("Project 'New Test Edited Project 2' successfully updated.")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());

        //Validating that project exists
        onView(withText("New Test Edited Project 2")).check(matches(isDisplayed()));

        //Click on the edited project
        onView(withText("New Test Edited Project 2")).perform(click());
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Edit")).perform(click());

        // test edited project name
        onView(withId(R.id.txtTitle)).check(matches(withText("New Test Edited Project 2")));

        // test edited description
        onView(withId(R.id.txtDescription)).check(matches(withText("This is a edited test project")));

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());

        //Click on the project to edit but not edit
        onView(withText("New Test Project 3")).perform(click());
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Edit")).perform(click());

        // test editing project name
        onView(withId(R.id.txtTitle)).perform(clearText(), typeText("abcd"));

        // test editing description
        onView(withId(R.id.txtDescription)).perform(clearText(), typeText("abcde"));

        //click back button
        onView(withContentDescription("Navigate up")).perform(click());

        // Should be back on main screen, with new project removed
        onView(withText("abcd")).check(doesNotExist());
    }
}
