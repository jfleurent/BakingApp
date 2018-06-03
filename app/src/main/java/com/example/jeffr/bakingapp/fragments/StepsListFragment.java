package com.example.jeffr.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffr.bakingapp.R;

import timber.log.Timber;

public class StepsListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Start onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);
        Timber.d("End onCreateView");
        return rootView;
    }
}
