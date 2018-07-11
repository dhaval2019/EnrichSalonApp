package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.Package.MyPackageResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.MyPackageAdapter;
import com.enrich.salonapp.ui.contracts.MyPackagesContract;
import com.enrich.salonapp.ui.presenters.MyPackagesPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPackageActivity extends BaseActivity implements MyPackagesContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.my_packages_recycler_view)
    RecyclerView myPackagesRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.no_details_found)
    LinearLayout noDetailsFound;

    @BindView(R.id.see_packages)
    Button seePackages;

    DataRepository dataRepository;
    MyPackagesPresenter myPackagesPresenter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_package);

        ButterKnife.bind(this);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("My Packages List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("MY PACKAGES");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        myPackagesPresenter = new MyPackagesPresenter(this, dataRepository);

        Map<String, String> map = new HashMap<>();
        map.put("guestid", EnrichUtils.getUserData(this).Id);

        myPackagesPresenter.getMyPackages(this, map);

        seePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPackageActivity.this, PackagesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showMyPackages(MyPackageResponseModel model) {
        if (!model.GuestPackage.isEmpty()) {
            noDetailsFound.setVisibility(View.GONE);
            myPackagesRecyclerView.setVisibility(View.VISIBLE);

            MyPackageAdapter adapter = new MyPackageAdapter(this, model.GuestPackage);
            myPackagesRecyclerView.setAdapter(adapter);
            myPackagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void noPackagesBought() {
        noDetailsFound.setVisibility(View.VISIBLE);
        myPackagesRecyclerView.setVisibility(View.GONE);
    }
}
