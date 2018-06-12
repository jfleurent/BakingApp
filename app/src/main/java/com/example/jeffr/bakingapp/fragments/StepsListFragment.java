package com.example.jeffr.bakingapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jeffr.bakingapp.R;
import com.example.jeffr.bakingapp.StepActivity;
import com.example.jeffr.bakingapp.adapters.RecipeStepRecyclerViewAdapter;
import com.example.jeffr.bakingapp.adapters.RecyclerViewOnClick;
import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.example.jeffr.bakingapp.data.RecipeDBHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepsListFragment extends Fragment implements RecyclerViewOnClick, LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int STEP_LIST_LOADER = 595;
    @BindView(R.id.indgredeint_list_spinner)
    Spinner ingredientList;

    @BindView(R.id.step_list_imageview)
    ImageView recipeImage;

    @BindView(R.id.step_list_recyclerview)
    RecyclerView stepListRecyclerView;

    @BindView(R.id.step_list_recipe_textview)
    TextView recipeTitle;

    RecipeStepRecyclerViewAdapter adapter;
    ArrayAdapter<String> adapter2;
    Cursor cursor;

    public StepsListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Start onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
        ButterKnife.bind(this,rootView);
        getActivity().getSupportLoaderManager().initLoader(STEP_LIST_LOADER,null,this);
        recipeTitle.setText(getActivity().getIntent().getExtras().getString("Recipe"));
        recipeImage.setImageResource(getDrawableResource(getActivity().getIntent().getExtras().getString("Recipe")));
        adapter = new RecipeStepRecyclerViewAdapter();
        adapter.setRecyclerViewOnClick(this);
        stepListRecyclerView.setAdapter(adapter);
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Timber.d("End onCreateView");
        return rootView;
    }

    private List<String> getIngredientStrings(){
        Timber.d("Start getIngredientStrings");
        cursor.moveToPosition(0);
        List<String> ingredients = new ArrayList<>();
        while(cursor.move(1)){
            String name = cursor.getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_NAME));
            String quantity = cursor.getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_MEASURE));
            String ingredient = null;
            if(name != null && quantity != null && measure != null){
                ingredient = name + "\t" + quantity +" " + measure;
            }
            if(ingredient != null && !ingredients.contains(ingredient)){
                ingredients.add(ingredient);
            }
        }
        Timber.d("End getIngredientStrings");
        return ingredients;
    }

    @Override
    public void rowSelected(int row) {
        Cursor cursor = getStepsCursor();
        cursor.moveToPosition(row);
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putExtra("Step",cursor.getInt(0));
        getActivity().getIntent().putExtra("Recipe", getActivity().getIntent().getExtras().getString("Recipe"));
        startActivity(intent);
    }

    private Cursor getStepsCursor(){
        RecipeDBHelper recipeDBHelper = new RecipeDBHelper(getActivity());
        String[] steps = {RecipeDBContract.StepEntry.TABLE_NAME+"."+RecipeDBContract.StepEntry.COLUMN_ID};
       return recipeDBHelper.getReadableDatabase().query(
                RecipeDBContract.StepEntry.TABLE_NAME+" , "+ RecipeDBContract.RecipeEntry.TABLE_NAME,
                steps,
               RecipeDBContract.RecipeEntry.TABLE_NAME+"."+ RecipeDBContract.RecipeEntry.COLUMN_NAME +" = '"
               + getActivity().getIntent().getExtras().getString("Recipe") + "' AND "+ RecipeDBContract.RecipeEntry.TABLE_NAME
               + "."+ RecipeDBContract.RecipeEntry.COLUMN_ID + " = " + RecipeDBContract.StepEntry.COLUMN_RECIPE_ID,
                null,
                null,
                null,
                null
                );
    }

    private int getDrawableResource(String recipe) {
        Timber.d("Start getDrawableResource");
        switch (recipe) {
            case "Nutella Pie":
                Timber.d("End getDrawableResource");
                return R.drawable.nutella_pie;
            case "Brownies":
                Timber.d("End getDrawableResource");
                return R.drawable.brownies;
            case "Yellow Cake":
                Timber.d("End getDrawableResource");
                return R.drawable.yellow_cake;
            case "Cheesecake":
                Timber.d("End getDrawableResource");
                return R.drawable.cheese_cake;
            default:
                return 0;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("Start onCreateLoader");
        switch (id) {
            case STEP_LIST_LOADER:
                Uri stepQueryUri = RecipeDBContract.StepEntry.STEP_CONTENT_URI;
                Timber.d("Queried: "+stepQueryUri.toString());

                String[] projection = {RecipeDBContract.StepEntry.COLUMN_DESCRIPTION,
                        RecipeDBContract.StepEntry.COLUMN_SHORT_DESCRIPTION,
                        RecipeDBContract.IngredientEntry.COLUMN_NAME,
                        RecipeDBContract.IngredientEntry.COLUMN_QUANTITY,
                        RecipeDBContract.IngredientEntry.COLUMN_MEASURE};

                String selection = RecipeDBContract.RecipeEntry.TABLE_NAME+"."+RecipeDBContract.RecipeEntry.COLUMN_ID +
                        " = " +RecipeDBContract.IngredientEntry.TABLE_NAME+ "."+RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID
                        + " AND " + RecipeDBContract.RecipeEntry.TABLE_NAME+"."+RecipeDBContract.RecipeEntry.COLUMN_ID +
                        " = " +RecipeDBContract.StepEntry.TABLE_NAME+ "."+ RecipeDBContract.StepEntry.COLUMN_RECIPE_ID +
                        " AND "+ RecipeDBContract.RecipeEntry.TABLE_NAME+"."+RecipeDBContract.RecipeEntry.COLUMN_NAME +
                        " = '" + getActivity().getIntent().getExtras().getString("Recipe")+"'";

                return new android.support.v4.content.CursorLoader(getActivity(),
                        stepQueryUri,
                        projection,
                        selection,
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
        cursor = data;
        adapter.setCursor(data);
        Timber.d("Cursor size:" + cursor.getCount());
        stepListRecyclerView.setAdapter(adapter);
        adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,getIngredientStrings());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientList.setAdapter(adapter2);
        Timber.d("End onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
