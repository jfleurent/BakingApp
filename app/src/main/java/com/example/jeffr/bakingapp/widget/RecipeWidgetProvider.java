package com.example.jeffr.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.example.jeffr.bakingapp.R;
import com.example.jeffr.bakingapp.StepListActivity;
import com.example.jeffr.bakingapp.widget.ListViewWidgetService;
import com.example.jeffr.bakingapp.fragments.StepsListFragment;

import timber.log.Timber;

public class RecipeWidgetProvider extends AppWidgetProvider {


    private static final String PREVIOUS_BUTTON = "previous_button";
    private static final String NEXT_BUTTON = "next_button";
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Start onReceive");
        switch (intent.getAction()){
            case PREVIOUS_BUTTON:
                Timber.d("Clicked Previous Button");
                ListViewWidgetService.recipesIndex--;
                onUpdate(context, AppWidgetManager.getInstance(context),intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID));
                break;
            case NEXT_BUTTON:
                Timber.d("Clicked Next Button");
                ListViewWidgetService.recipesIndex++;
                onUpdate(context, AppWidgetManager.getInstance(context),intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID));
                break;
            default:
                super.onReceive(context,intent);
                Timber.d("Unidentified Button Press");
        }
        Timber.d("End onReceive");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("Start onUpdate");
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            Intent serviceIntent = new Intent(context, ListViewWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setRemoteAdapter(R.id.ingredient_list_view,serviceIntent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.ingredient_list_view);
            remoteViews.setEmptyView(R.id.ingredient_list_view, R.id.empty_layout);
            remoteViews.setViewVisibility(R.id.widget_current_recipe_layout, View.VISIBLE);
            remoteViews.setImageViewResource(R.id.widget_recipe_imageview,getDrawableResource(ListViewWidgetService.recipesIndex%4));
            remoteViews.setTextViewText(R.id.widget_recipe_name_textview, ListViewWidgetService.recipes[ListViewWidgetService.recipesIndex%4]);
            remoteViews.setOnClickPendingIntent(R.id.widget_previous_button, getPendingSelfIntent(context, PREVIOUS_BUTTON,appWidgetIds));
            remoteViews.setOnClickPendingIntent(R.id.widget_next_button, getPendingSelfIntent(context, NEXT_BUTTON,appWidgetIds));

            Intent viewIntent = new Intent(context, StepListActivity.class);
            StepsListFragment.recipeName = ListViewWidgetService.recipes[ListViewWidgetService.recipesIndex%4];

            PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_recipe_imageview, viewPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        Timber.d("End onUpdate");
    }


    protected PendingIntent getPendingSelfIntent(Context context, String action,int[] appWidgetIds) {
        Timber.d("Start getPendingSelfIntent");
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        Timber.d("End getPendingSelfIntent");
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private int getDrawableResource(int recipeIndex){
        Timber.d("Start getDrawableResource");
        switch (recipeIndex){
            case  0:
                Timber.d("End getDrawableResource");
                return R.drawable.nutella_pie;
            case  1:
                Timber.d("End getDrawableResource");
                return R.drawable.brownies;
            case  2:
                Timber.d("End getDrawableResource");
                return R.drawable.yellow_cake;
            case  3:
                Timber.d("End getDrawableResource");
                return R.drawable.cheese_cake;
            default:
                Timber.d("No Resource Found");
                return 0;
        }
    }
}
