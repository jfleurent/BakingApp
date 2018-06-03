package com.example.jeffr.bakingapp;

import android.os.AsyncTask;

import com.example.jeffr.bakingapp.dataobjects.Recipe;
import com.example.jeffr.bakingapp.utilities.JsonUtils;
import com.example.jeffr.bakingapp.utilities.NetworkUtils;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class FetchRecipes extends AsyncTask<Integer, Void, List<Recipe>> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Recipe> doInBackground(Integer... integers) {
        Timber.d("Start doInBackground");
        List<Recipe> recipeList = null;
        try {
            recipeList = JsonUtils.getRecipeList(NetworkUtils.getResponseFromHttpUrl());
        } catch (IOException e) {
            Timber.e(e,"IOException in doInBackground");
        }
        Timber.d("End doInBackground");
        return recipeList;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        Timber.d("Start onPostExecute");
        MainActivity.setAdapterList(recipes);
        Timber.d("End onPostExecute");
    }
}
