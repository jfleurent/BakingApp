package com.example.jeffr.bakingapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeffr.bakingapp.R;
import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.example.jeffr.bakingapp.dataobjects.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecyclerViewHolder> {
    RecyclerViewOnClick recyclerViewOnClick;
    Cursor recipes;

    public void setRecyclerViewOnClick(RecyclerViewOnClick recyclerViewOnClick){
        Timber.d("Start setRecyclerViewOnClick");
        this.recyclerViewOnClick = recyclerViewOnClick;
        Timber.d("End setRecyclerViewOnClick");
    }

    public void setRecipes(Cursor recipes){
        Timber.d("Start setRecipes");
        this.recipes = recipes;
        Timber.d("End setRecipes");
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.d("Start onCreateViewHolder");
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        Timber.d("End onCreateViewHolder");
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.RecyclerViewHolder holder,final int position) {
        Timber.d("Start onBindViewHolder");
        recipes.moveToPosition(position);
        holder.recipeTitle.setText(recipes.getString(recipes.getColumnIndex(RecipeDBContract.RecipeEntry.COLUMN_NAME)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   recyclerViewOnClick.rowSelected(position);
                                               }
                                           });
        Timber.d("End onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        int size;
        return size = recipes == null ? 0 : recipes.getCount();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_title_textview)
        TextView recipeTitle;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            Timber.d("Start RecyclerViewHolder");
            ButterKnife.bind(this,itemView);
            Timber.d("End RecyclerViewHolder");
        }
    }
}
