package com.example.jeffr.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jeffr.bakingapp.dataobjects.Recipe;
import com.example.jeffr.bakingapp.fragments.StepsListFragment;

public class StepListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
    }
}
