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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeffr.bakingapp.R;
import com.example.jeffr.bakingapp.StepActivity;
import com.example.jeffr.bakingapp.adapters.ExpandableListAdapter;
import com.example.jeffr.bakingapp.adapters.RecipeStepRecyclerViewAdapter;
import com.example.jeffr.bakingapp.adapters.RecyclerViewOnClick;
import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.example.jeffr.bakingapp.data.RecipeDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepsListFragment extends Fragment implements RecyclerViewOnClick, LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int STEP_LIST_LOADER = 595;
    private static final int INGREDIENT_LIST_LOADER = 345;
    public static String recipeName;
    private List<String> headers;
    private HashMap<String,List<String>> stringListHashMap;
    Cursor stepCurosr;

    @BindView(R.id.ingredients_expandable_listview)
    ExpandableListView ingredientList;

    @BindView(R.id.step_list_imageview)
    ImageView recipeImage;

    @BindView(R.id.step_list_recyclerview)
    RecyclerView stepListRecyclerView;

    @BindView(R.id.step_list_recipe_textview)
    TextView recipeTitle;

    RecipeStepRecyclerViewAdapter adapter;
    ArrayAdapter<String> adapter2;

    public StepsListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Start onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
        ButterKnife.bind(this,rootView);
        getActivity().getSupportLoaderManager().initLoader(STEP_LIST_LOADER,null,this);
        getActivity().getSupportLoaderManager().initLoader(INGREDIENT_LIST_LOADER,null,this);
        recipeTitle.setText(recipeName);
        recipeImage.setImageResource(getDrawableResource(recipeName));
        adapter = new RecipeStepRecyclerViewAdapter();
        adapter.setRecyclerViewOnClick(this);
        stepListRecyclerView.setAdapter(adapter);
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Timber.d("End onCreateView");
        return rootView;
    }

    private List<String> getIngredientStrings(Cursor data){
        Timber.d("Start getIngredientStrings");
        data.moveToPosition(0);
        List<String> ingredients = new ArrayList<>();
        while(data.move(1)){
            String name = data.getString(data.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_NAME));
            String quantity = data.getString(data.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_QUANTITY));
            String measure = data.getString(data.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_MEASURE));
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
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putExtra("Step Number", row);
        startActivity(intent);
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

                String selection = RecipeDBContract.RecipeEntry.TABLE_NAME+"."+ RecipeDBContract.RecipeEntry.COLUMN_ID
                        + " = " + RecipeDBContract.StepEntry.COLUMN_RECIPE_ID + " AND " + RecipeDBContract.RecipeEntry.TABLE_NAME
                        + "."+RecipeDBContract.RecipeEntry.COLUMN_NAME + " = '" + recipeName+"'";

                return new android.support.v4.content.CursorLoader(getActivity(),
                        stepQueryUri,
                        null,
                        selection,
                        null,
                        null);
            case INGREDIENT_LIST_LOADER:
                Uri ingredientQueryUri = RecipeDBContract.IngredientEntry.INGREDIENT_CONTENT_URI;
                Timber.d("Queried: "+ingredientQueryUri.toString());

                String selection2 = RecipeDBContract.RecipeEntry.TABLE_NAME+"."+ RecipeDBContract.RecipeEntry.COLUMN_ID
                        + " = " + RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID + " AND " + RecipeDBContract.RecipeEntry.TABLE_NAME
                        + "."+RecipeDBContract.RecipeEntry.COLUMN_NAME + " = '" + recipeName+"'";

                return new android.support.v4.content.CursorLoader(getActivity(),
                        ingredientQueryUri,
                        null,
                        selection2,
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
        switch (loader.getId()){
            case STEP_LIST_LOADER:
                stepCurosr = data;
                adapter.setSteps(data);
                stepListRecyclerView.setAdapter(adapter);
                break;
            case INGREDIENT_LIST_LOADER:
                initData(data);
                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(getActivity(),headers,stringListHashMap);
                ingredientList.setAdapter(expandableListAdapter);
                break;
            default:
                Timber.d("Loader Not Implemented: " + loader.getId());
        }
        Timber.d("End onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void initData(Cursor data){
        Timber.d("Start initData");
        headers = new ArrayList<>();
        stringListHashMap = new HashMap<>();
        headers.add("Ingredients");
        stringListHashMap.put(headers.get(0),getIngredientStrings(data));
        Timber.d("End initData");
    }
}
