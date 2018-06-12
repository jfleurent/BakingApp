package com.example.jeffr.bakingapp;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int STEP_LOADER = 167;
    private static final String TAG = StepActivity.class.getSimpleName();

    @BindView(R.id.next_layout)
    LinearLayout nextButton;

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
        ButterKnife.bind(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
