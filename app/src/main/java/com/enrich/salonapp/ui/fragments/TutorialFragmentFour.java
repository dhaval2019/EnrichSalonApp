package com.enrich.salonapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enrich.salonapp.R;

public class TutorialFragmentFour extends Fragment {

    public static TutorialFragmentFour getInstance(){
        return new TutorialFragmentFour();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_four, container, false);
        return view;
    }
}
