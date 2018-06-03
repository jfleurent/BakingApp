package com.example.jeffr.bakingapp;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.example.jeffr.bakingapp.dataobjects.Ingredient;
import com.example.jeffr.bakingapp.dataobjects.Recipe;
import com.example.jeffr.bakingapp.dataobjects.Step;
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
        for(Recipe recipe : recipeList){
            ContentValues cv = new ContentValues();
            cv.put(RecipeDBContract.RecipeEntry.COLUMN_ID,recipe.getId());
            cv.put(RecipeDBContract.RecipeEntry.COLUMN_NAME,recipe.getName());
            cv.put(RecipeDBContract.RecipeEntry.COLUMN_SERVINGS,recipe.getServings());
            MainActivity.recipeDb.getWritableDatabase().insert(RecipeDBContract.RecipeEntry.TABLE_NAME,null,cv);
            int i = 0;
            for(Ingredient ingredient : recipe.getIngredients()){
                cv.clear();
                cv.put(RecipeDBContract.IngredientEntry.COLUMN_ID,recipe.getId()*50+i++);
                cv.put(RecipeDBContract.IngredientEntry.COLUMN_MEASURE,ingredient.getMeasure());
                cv.put(RecipeDBContract.IngredientEntry.COLUMN_NAME,ingredient.getIngredient());
                cv.put(RecipeDBContract.IngredientEntry.COLUMN_QUANTITY,ingredient.getQuantity());
                cv.put(RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID,recipe.getId());
                MainActivity.recipeDb.getWritableDatabase().insert(RecipeDBContract.IngredientEntry.TABLE_NAME,null,cv);
            }
            for(Step step : recipe.getSteps()){
                cv.clear();
                cv.put(RecipeDBContract.StepEntry.COLUMN_DESCRIPTION,step.getDescription());
                cv.put(RecipeDBContract.StepEntry.COLUMN_ID,recipe.getId()*50+step.getId());
                cv.put(RecipeDBContract.StepEntry.COLUMN_RECIPE_ID,recipe.getId());
                cv.put(RecipeDBContract.StepEntry.COLUMN_SHORT_DESCRIPTION,step.getShortDescription());
                cv.put(RecipeDBContract.StepEntry.COLUMN_THUMBNAIL_URL,step.getThumbnialURL());
                cv.put(RecipeDBContract.StepEntry.COLUMN_VIDEO_URL,step.getVideoURL());
                MainActivity.recipeDb.getWritableDatabase().insert(RecipeDBContract.StepEntry.TABLE_NAME,null,cv);
            }

        }

        Timber.d("End doInBackground");
        return recipeList;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        Timber.d("Start onPostExecute");
        MainActivity.recipeList = recipes;
        MainActivity.setAdapterList();
        Timber.d("End onPostExecute");
    }
}
