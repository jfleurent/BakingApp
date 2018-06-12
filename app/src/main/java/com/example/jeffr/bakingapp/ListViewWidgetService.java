package com.example.jeffr.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.jeffr.bakingapp.data.RecipeDBContract;

import timber.log.Timber;

public class ListViewWidgetService extends RemoteViewsService {
    static String[] recipes = {"Nutella Pie","Brownies","Yellow Cake","Cheesecake"};
    static int recipesIndex = 0;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext());
    }
    public class ListViewRemoteViewsFactory implements  RemoteViewsService.RemoteViewsFactory{

        Context mContext;
        Cursor cursor;

        public ListViewRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Timber.d("Start onDataSetChanged");
            Uri INGREDIENT_URI = RecipeDBContract.IngredientEntry.INGREDIENT_CONTENT_URI;
            if (cursor != null) cursor.close();

            String[] projection = {RecipeDBContract.RecipeEntry.COLUMN_NAME,
                    RecipeDBContract.IngredientEntry.COLUMN_NAME,
                    RecipeDBContract.IngredientEntry.COLUMN_QUANTITY,
                    RecipeDBContract.IngredientEntry.COLUMN_MEASURE};

            String selection = RecipeDBContract.RecipeEntry.TABLE_NAME+"."+RecipeDBContract.RecipeEntry.COLUMN_ID
                    + " = " + RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID + " AND "+
                    RecipeDBContract.RecipeEntry.TABLE_NAME+"."+RecipeDBContract.RecipeEntry.COLUMN_NAME
                    +" = '" + recipes[recipesIndex%4]+"'";
            cursor = mContext.getContentResolver().query(
                    INGREDIENT_URI,
                    projection,
                    selection,
                    null,
                    null);
            Timber.d("Start onDataSetChanged");
        }

        @Override
        public void onDestroy() {
            cursor.close();
        }

        @Override
        public int getCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Timber.d("Start getViewAt");
            if (cursor == null || cursor.getCount() == 0) return null;
            cursor.moveToPosition(i);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.ingredeint_list_item);
                String name = cursor
                        .getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_NAME));
                String measure = cursor
                        .getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_MEASURE));
                String quantity = cursor
                        .getString(cursor.getColumnIndex(RecipeDBContract.IngredientEntry.COLUMN_QUANTITY));

                remoteViews.setTextViewText(R.id.ingredient_name, name);
                remoteViews.setTextViewText(R.id.ingredient_measure,measure);
                remoteViews.setTextViewText(R.id.ingredient_quantity,quantity);
            Intent intent = new Intent();

            Timber.d("End getViewAt");
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
