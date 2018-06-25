package com.example.jeffr.bakingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import timber.log.Timber;

import static com.example.jeffr.bakingapp.fragments.StepsListFragment.recipeName;

public class StepListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int STEP_LIST_LOADER = 990;
    public static boolean stepActivityTwoPane;
    public static  boolean isPlaying = false;
    public static  long playerPosition = 0;
    public static Cursor cursor;
    public static SimpleExoPlayer player;
    public static SimpleExoPlayerView simpleExoPlayerView;
    public static TextView stepDescription;
    public static Context context;
    public static ImageView thumbnail;
    public static int stepNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("Start onCreate");
        setContentView(R.layout.activity_step_list);
        if(savedInstanceState != null){
            playerPosition = savedInstanceState.getLong("Position");
            isPlaying = savedInstanceState.getBoolean("IsPlaying");
            stepNumber = savedInstanceState.getInt("Step Number");
        }
        context= this;
        getSupportActionBar().setTitle(recipeName);
        simpleExoPlayerView = findViewById(R.id.instruction_video_player);
        stepDescription = findViewById(R.id.step_description_textview);
        thumbnail = findViewById(R.id.thumbnail_imageview);
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
                String[] projection = {RecipeDBContract.StepEntry.COLUMN_DESCRIPTION, RecipeDBContract.StepEntry.COLUMN_VIDEO_URL, RecipeDBContract.StepEntry.COLUMN_THUMBNAIL_URL};
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
        cursor.moveToPosition(stepNumber);
        stepDescription.setText(cursor.getString(0));
        setPlayer();
        Timber.d("End onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public static void setPlayer(){
        Timber.d("Start setPlayer");
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);
        if(cursor != null){
            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(cursor.getString(1)), new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(videoSource);
            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(isPlaying);
            player.seekTo(playerPosition);
            Picasso.with(context)
                    .load(Uri.parse(cursor.getString(2) == null ? "" : cursor.getString(2) ))
                    .error(R.drawable.baking_icon)
                    .into(thumbnail);
            if(!cursor.getString(1).equals("")){
                thumbnail.setVisibility(View.INVISIBLE);
            }
            else {
                thumbnail.setVisibility(View.VISIBLE);
            }
            Timber.d("End setPlayer");
        }

    }

    public static void releasePlayer(){
        Timber.d("Start releasePlayer");
        if(player != null){
            player.stop();
            player.release();
        }
        Timber.d("End releasePlayer");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("IsPlaying", player.getPlayWhenReady());
        outState.putLong("Position", player.getCurrentPosition());
        outState.putInt("Step Number", cursor.getPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("Start onStop");
        if (simpleExoPlayerView != null && stepDescription != null) {
            isPlaying = player.getPlayWhenReady();
            playerPosition = player.getCurrentPosition();
            releasePlayer();
        }
        Timber.d("End onStop");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stepNumber = 0;
                onBackPressed();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onRestart() {
        isPlaying = false;
        player.setPlayWhenReady(isPlaying);
        player.seekTo(playerPosition);
        super.onRestart();
    }
}
