package com.example.jeffr.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

import static com.example.jeffr.bakingapp.fragments.StepsListFragment.recipeName;

public class StepListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int STEP_LIST_LOADER = 990;
    public static boolean stepActivityTwoPane;
    public static Cursor cursor;
    public static SimpleExoPlayer player;
    public static SimpleExoPlayerView simpleExoPlayerView;
    public static TextView stepDescription;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("Start onCreate");
        setContentView(R.layout.activity_step_list);
        context= this;
        getSupportActionBar().setTitle(recipeName);
        simpleExoPlayerView = findViewById(R.id.instruction_video_player);
        stepDescription = findViewById(R.id.step_description_textview);
        if (simpleExoPlayerView != null && stepDescription != null) {
            getSupportLoaderManager().initLoader(STEP_LIST_LOADER, null, this);
            stepActivityTwoPane = true;
        }
        else{
            stepActivityTwoPane = false;
        }
        Timber.d("End onCreate");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("Start onCreateLoader");
        switch (id) {
            case STEP_LIST_LOADER:
                Uri stepQueryUri = RecipeDBContract.StepEntry.STEP_CONTENT_URI;
                String[] projection = {RecipeDBContract.StepEntry.COLUMN_DESCRIPTION, RecipeDBContract.StepEntry.COLUMN_VIDEO_URL};
                String selection = RecipeDBContract.RecipeEntry.TABLE_NAME + "." + RecipeDBContract.RecipeEntry.COLUMN_ID
                        + " = " + RecipeDBContract.StepEntry.COLUMN_RECIPE_ID + " AND " + RecipeDBContract.RecipeEntry.TABLE_NAME
                        + "." + RecipeDBContract.RecipeEntry.COLUMN_NAME + " = '" + recipeName + "'";
                Timber.d("End onCreateLoader");
                return new android.support.v4.content.CursorLoader(this,
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
        cursor.moveToPosition(0);
        stepDescription.setText(cursor.getString(0));
        setPlayer();
        Timber.d("End onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public static  void setPlayer() {
        Timber.d("Start setPlayer");
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        if (cursor != null) {
            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(cursor.getString(1)), new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(videoSource);
            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(true);
            Timber.d("End setPlayer");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("Start onPause");
        if(player != null){
            player.stop();
            player.release();
        }
        Timber.d("End onPause");
    }
}
