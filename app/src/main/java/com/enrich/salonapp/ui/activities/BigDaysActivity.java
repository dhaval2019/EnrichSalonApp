package com.enrich.salonapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class BigDaysActivity extends AppCompatActivity {
    Tracker mTracker;
    EnrichApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_days);
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Beauty and Bling winning screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
    }
}
