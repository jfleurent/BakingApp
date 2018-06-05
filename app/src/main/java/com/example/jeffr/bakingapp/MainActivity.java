package com.example.jeffr.bakingapp;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jeffr.bakingapp.adapters.RecipeRecyclerViewAdapter;
import com.example.jeffr.bakingapp.adapters.RecyclerViewOnClick;
import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.example.jeffr.bakingapp.data.RecipeDBHelper;
import com.example.jeffr.bakingapp.dataobjects.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClick, LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerView;
    RecipeRecyclerViewAdapter adapter;

   static LoaderManager loaderManager;
   static LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
   static Context context;

    public static final int RECIPE_LOADER = 505;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("Start onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recipe_list_recyclerview);
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewOnClick(this);
        loaderManager = getSupportLoaderManager();
        loaderCallbacks = this;
        context = this;
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

//    public static void setAdapterList(){
//        Timber.d("Start setAdapterList");
//        adapter.setRecipes(getCurosr());
//        recyclerView.setAdapter(adapter);
//        Timber.d("End setAdapterList");
//    }


    @Override
    public void rowSelected(int row) {
        Timber.d("Start rowSelected");
        Intent intent = new Intent(this,StepListActivity.class);
        startActivity(intent);
        Timber.d("End rowSelected");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("Start onCreateLoader");
        switch (id) {
            case RECIPE_LOADER:
                Uri recipeQueryUri = RecipeDBContract.RecipeEntry.RECIPE_CONTENT_URI;
                Timber.d("Queried: "+recipeQueryUri.toString());
                return new android.support.v4.content.CursorLoader(this,
                        recipeQueryUri,
                        null,
                        null,
                        null,
                         null);
            default:
                Timber.d("Loader Not Implemented: " + id);
        }
        Timber.d("End onCreateLoader");
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Timber.d("Start onLoadFinished");
        adapter.setRecipes(data);
        recyclerView.setAdapter(adapter);
        Timber.d("End onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
