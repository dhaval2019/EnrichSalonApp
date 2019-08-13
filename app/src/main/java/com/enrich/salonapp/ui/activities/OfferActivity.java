package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.ui.adapters.OfferAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferActivity extends AppCompatActivity {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.offer_recycler_view)
    RecyclerView offersRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<OfferModel> offerList;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Offers List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("OFFERS");

        offerList = getIntent().getParcelableArrayListExtra("OfferList");

        setOfferAdapter(offerList);
    }

    private void setOfferAdapter(ArrayList<OfferModel> list) {
        if(list.isEmpty())
        {

        }else {
            OfferAdapter lookHomeAdapter = new OfferAdapter(this, list);
            offersRecyclerView.setAdapter(lookHomeAdapter);
            offersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }
}
