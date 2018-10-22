package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.Package.PackageModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.PackageAdapter;
import com.enrich.salonapp.ui.contracts.PackageContract;
import com.enrich.salonapp.ui.contracts.ProductContract;
import com.enrich.salonapp.ui.presenters.PackagePresenter;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackagesActivity extends BaseActivity implements PackageContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.package_recycler_view)
    RecyclerView packageRecyclerView;

    @BindView(R.id.drawer_toolbar)
    Toolbar toolbar;

    PackagePresenter packagePresenter;

    DataRepository dataRepository;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Package List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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

        collapsingToolbarLayout.setTitle("PACKAGES");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);

        packagePresenter = new PackagePresenter(this, dataRepository);

        packagePresenter.getAllPackages(this);
    }


    @Override
    public void showPackage(PackageResponseModel model) {
        if(!model.Package.isEmpty()){
            PackageAdapter adapter = new PackageAdapter(this, model.Package);
            packageRecyclerView.setAdapter(adapter);
            packageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void noPackageAvailable() {

    }
}
