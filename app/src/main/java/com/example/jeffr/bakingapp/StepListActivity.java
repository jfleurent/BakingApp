package com.example.jeffr.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepListActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "com.example.jeffr.bakingapp.extra.PLANT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

    }
}
