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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeStepRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepRecyclerViewAdapter.RecyclerViewHolder> {
    RecyclerViewOnClick recyclerViewOnClick;
    Cursor cursor;
    List<String> shortDescriptions = new ArrayList<>();

    public void setRecyclerViewOnClick(RecyclerViewOnClick recyclerViewOnClick) {
        Timber.d("Start setRecyclerViewOnClick");
        this.recyclerViewOnClick = recyclerViewOnClick;
        Timber.d("End setRecyclerViewOnClick");
    }

    public void setCursor(Cursor cursor) {
        Timber.d("Start setRecipes");
        this.cursor = cursor;
        Timber.d("End setRecipes");
    }

    @Override
    public RecipeStepRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.d("Start onCreateViewHolder");
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        Timber.d("End onCreateViewHolder");
        return new RecipeStepRecyclerViewAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        Timber.d("Start onBindViewHolder");
            holder.stepName.setText(shortDescriptions.get(position));
            holder.stepNumber.setText("Step " + position + ": ");
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
        setLists();
        return shortDescriptions.size();
    }

    private void setLists() {
        cursor.moveToPosition(0);
        while (cursor.move(1)) {
            String shortDescription = cursor.getString(
                    cursor.getColumnIndex(RecipeDBContract.StepEntry.COLUMN_SHORT_DESCRIPTION));
            if (!shortDescriptions.contains(shortDescription)){
                shortDescriptions.add(shortDescription);
            }
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.step_number_textview)
        TextView stepNumber;

        @BindView(R.id.step_name_textview)
        TextView stepName;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            Timber.d("Start RecyclerViewHolder");
            ButterKnife.bind(this, itemView);
            Timber.d("End RecyclerViewHolder");
        }
    }
}