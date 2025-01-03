package com.futsch1.medtimer;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.futsch1.medtimer.AndroidTestHelper.onViewWithTimeout;
import static com.futsch1.medtimer.AndroidTestHelper.onViewWithTimeoutClickable;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

import java.time.LocalTime;

@LargeTest
public class DeleteLinkedReminderTest extends BaseHelper {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void deleteLinkedReminderTest() {
        AndroidTestHelper.createMedicine("Test med");
        AndroidTestHelper.createReminder("1", LocalTime.of(0, 0));

        onViewWithTimeoutClickable(withId(R.id.open_advanced_settings)).perform(click());

        onView(withId(R.id.addLinkedReminder)).perform(click());
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(scrollTo(), click());
        AndroidTestHelper.setTime(0, 1);

        onViewWithTimeoutClickable(new RecyclerViewMatcher(R.id.reminderList).atPositionOnView(1, R.id.open_advanced_settings)).perform(scrollTo(), click());

        onView(withId(R.id.addLinkedReminder)).perform(click());
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(scrollTo(), click());
        AndroidTestHelper.setTime(0, 2);

        onViewWithTimeoutClickable(new RecyclerViewMatcher(R.id.reminderList).atPositionOnView(0, R.id.open_advanced_settings)).perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.delete)).perform(click());
        onView(allOf(withId(android.R.id.button1), withText(R.string.yes))).perform(scrollTo(), click());

        // Check that the reminder list is empty
        onViewWithTimeout(new RecyclerViewMatcher(R.id.reminderList).sizeMatcher(0)).check(matches(isDisplayed()));

    }

}
