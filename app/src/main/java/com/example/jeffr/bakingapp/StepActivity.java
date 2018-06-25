package com.example.jeffr.bakingapp;

import android.app.Dialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.jeffr.bakingapp.fragments.StepsListFragment.recipeName;

public class StepActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STEP_LOADER = 167;
    private static int stepNumber;
    Cursor cursor;
    SimpleExoPlayer player;

    @BindView(R.id.next_layout)
    LinearLayout nextButton;

    @BindView(R.id.previous_layout)
    LinearLayout previousButton;

    @BindView(R.id.instruction_video_player)
    SimpleExoPlayerView instructionVideo;

    @BindView(R.id.step_description_textview)
    TextView stepDescription;

    @BindView(R.id.thumbnail_imageview)
    ImageView thumbnail;

    boolean isPlaying = false;

    long playerPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Timber.d("Start onCreate");
        if(savedInstanceState != null){
            playerPosition = savedInstanceState.getLong("Position");
            isPlaying = savedInstanceState.getBoolean("IsPlaying");
            stepNumber = savedInstanceState.getInt("Step Number");
        }
        stepNumber = getIntent().getExtras() == null ? 1 : getIntent().getExtras().getInt("Step Number");
        initializeLayout();;
        Timber.d("End onCreate");
    }

    private void initializeLayout(){
        Timber.d("Start initializeLayout");
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(recipeName);
        getSupportLoaderManager().initLoader(STEP_LOADER, null, this);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cursor.moveToNext()){
                    stepNumber = 0;
                    cursor.moveToFirst();
                }
                else{
                    stepNumber++;
                }
                stepDescription.setText(cursor.getString(0));
                releasePlayer();
                isPlaying = false;
                playerPosition = 0;
                setPlayer();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cursor.moveToPrevious()){
                    stepNumber = cursor.getCount()-1;
                    cursor.moveToLast();
                }
                else{
                    stepNumber--;
                }
                stepDescription.setText(cursor.getString(0));
                isPlaying = false;
                playerPosition = 0;
                releasePlayer();
                setPlayer();
            }
        });
        Timber.d("End initializeLayout");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("Start onCreateLoader");
        switch (id) {
            case STEP_LOADER:
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

    private void setPlayer(){
        Timber.d("Start setPlayer");
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(this,trackSelector,loadControl);
        if(cursor != null){
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(cursor.getString(1)), new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(videoSource);
            instructionVideo.setPlayer(player);
            player.setPlayWhenReady(isPlaying);
            player.seekTo(playerPosition);
            Picasso.with(this)
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

    private void releasePlayer(){
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
        isPlaying = false;
        player.setPlayWhenReady(isPlaying);
        playerPosition = player.getCurrentPosition();
        releasePlayer();
        Timber.d("Start onStop");
    }

    @Override
    protected void onRestart() {

        player.seekTo(playerPosition);
        super.onRestart();
    }
}
