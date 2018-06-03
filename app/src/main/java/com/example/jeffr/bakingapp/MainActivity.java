package com.example.jeffr.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jeffr.bakingapp.adapters.RecipeRecyclerViewAdapter;
import com.example.jeffr.bakingapp.adapters.RecyclerViewOnClick;
import com.example.jeffr.bakingapp.dataobjects.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClick {
    static RecyclerView recyclerView;
    static RecipeRecyclerViewAdapter adapter;
   static List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("Start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recipe_list_recyclerview);
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewOnClick(this);
        new FetchRecipes().execute();
        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_LARGE){
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
            recyclerView.setAdapter(adapter);
        }
        else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
        Timber.d("End onCreate");
    }

    public static void setAdapterList(List<Recipe> recipes){
        Timber.d("Start setAdapterList");
        adapter.setRecipes(recipes);
        recyclerView.setAdapter(adapter);
        Timber.d("End setAdapterList");
    }

    @Override
    public void rowSelected(int row) {
        Timber.d("Start rowSelected");
        Intent intent = new Intent(this,StepListActivity.class);
        startActivity(intent);
        Timber.d("End rowSelected");
    }
}
