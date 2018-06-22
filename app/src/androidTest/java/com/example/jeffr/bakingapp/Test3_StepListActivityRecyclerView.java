package com.example.jeffr.bakingapp;

import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

@RunWith(AndroidJUnit4.class)
public class Test3_StepListActivityRecyclerView {
    @Rule
    public IntentsTestRule<StepListActivity> stepListActivityIntentsTestRule =
            new IntentsTestRule<>(StepListActivity.class);

    @Test
    public void recyclerViewOpensNewActivity(){
        Espresso.onView(ViewMatchers.withId(R.id.step_list_recyclerview)).perform(actionOnItemAtPosition(0, click()));
        Intents.intended(IntentMatchers.hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(),StepActivity.class)));
    }
}
