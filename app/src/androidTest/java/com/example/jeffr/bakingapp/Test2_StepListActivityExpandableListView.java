package com.example.jeffr.bakingapp;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.jeffr.bakingapp.fragments.StepsListFragment;

import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
public class Test2_StepListActivityExpandableListView {
    @Rule
    public ActivityTestRule<StepListActivity> stepListActivityActivityTestRule = new ActivityTestRule<>(StepListActivity.class);

    @Test
    public void expandableListViewShowsTest(){

        StepsListFragment stepsListFragment = (StepsListFragment) stepListActivityActivityTestRule.getActivity().getSupportFragmentManager().getFragments().get(0);
        Espresso.registerIdlingResources(stepsListFragment.getCountingIdilingResource());
        Espresso.onView(ViewMatchers.withId(R.id.ingredients_expandable_listview)).perform(click()).check(ViewAssertions.matches(isDisplayed()));
        }

}
