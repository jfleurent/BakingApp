package com.example.jeffr.bakingapp;

import android.app.Dialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.jeffr.bakingapp.data.RecipeDBContract;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.jeffr.bakingapp.fragments.StepsListFragment.recipeName;

public class StepActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STEP_LOADER = 167;
    private static final String TAG = StepActivity.class.getSimpleName();
    private static int stepNumber;
    Cursor cursor;
    SimpleExoPlayer player;
    boolean mExoPlayerFullscreen;
    Dialog mFullScreenDialog;

    @BindView(R.id.next_layout)
    LinearLayout nextButton;

    @BindView(R.id.step_linear_layout)
    LinearLayout setLayout;

    @BindView(R.id.previous_layout)
    LinearLayout previousButton;

    @BindView(R.id.instruction_video_player)
    SimpleExoPlayerView instructionVideo;

    @BindView(R.id.step_description_textview)
    TextView stepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_activiity);
        Timber.d("Start onCreate");
        stepNumber = getIntent().getExtras().getInt("Step Number");
        initalizeLayout();
        Timber.d("End onCreate");
    }

    private void initalizeLayout(){
        Timber.d("Start initalizeLayout");
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
                setPlayer();
            }
        });
        Timber.d("End initalizeLayout");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("Start onCreateLoader");
        switch (id) {
            case STEP_LOADER:
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
            player.setPlayWhenReady(true);
            Timber.d("End setPlayer");
        }

    }

    private void releasePlayer(){
        Timber.d("Start releasePlayer");
        player.stop();
        player.release();
        Timber.d("End releasePlayer");
    }

    private void closeFullscreenDialog() {
        setContentView(R.layout.activity_step_activiity);
        initalizeLayout();
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("Start onPause");
        releasePlayer();
        Timber.d("Start onPause");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.d("Start onConfigurationChanged");
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                public void onBackPressed() {
                    if (mExoPlayerFullscreen)
                        closeFullscreenDialog();
                    super.onBackPressed();
                }
            };
            ((ViewGroup) instructionVideo.getParent()).removeView(instructionVideo);
            mFullScreenDialog.addContentView(instructionVideo,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenDialog.show();
        }
        else {
            closeFullscreenDialog();
        }
        Timber.d("End onConfigurationChanged");
    }
}
