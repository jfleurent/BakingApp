package com.example.jeffr.bakingapp;

import android.database.Cursor;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class Test5_StepActivityPreviousButton {
    @Rule
    public ActivityTestRule<StepActivity> stepActivityActivityTestRule =
            new ActivityTestRule<>(StepActivity.class);

    @Test
    public void previousButtonTest(){
        Cursor cursor = stepActivityActivityTestRule.getActivity().cursor;
        cursor.moveToLast();
        String stepString = cursor.getString(0);
        cursor.moveToFirst();
        Espresso.onView(ViewMatchers.withId(R.id.previous_layout)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.step_description_textview)).check(ViewAssertions.matches(ViewMatchers.withText(stepString)));
    }
}
