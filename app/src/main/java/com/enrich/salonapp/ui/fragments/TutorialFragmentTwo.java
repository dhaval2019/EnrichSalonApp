package com.enrich.salonapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enrich.salonapp.R;

public class TutorialFragmentTwo extends Fragment {

    public static TutorialFragmentTwo getInstance() {
        return new TutorialFragmentTwo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_two, container, false);
        return view;
    }
}
