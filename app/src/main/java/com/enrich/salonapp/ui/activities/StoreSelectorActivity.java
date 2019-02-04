package com.enrich.salonapp.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CenterResponseModel;
import com.enrich.salonapp.data.model.CenterViewModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.StoreSelectorAdapter;
import com.enrich.salonapp.ui.contracts.CenterListContract;
import com.enrich.salonapp.ui.presenters.CenterListPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.DistanceComparator;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.GpsTracker;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreSelectorActivity extends BaseActivity implements CenterListContract.View {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.store_recycler_view)
    RecyclerView storeRecyclerView;

    @BindView(R.id.expand_cities_list)
    ImageView expandCitiesList;

    @BindView(R.id.selected_city_name)
    TextView selectedCityName;

    @BindView(R.id.salon_filter)
    EditText salonFilter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.store_progress)
    ProgressBar storeProgress;

    CenterListPresenter centerListPresenter;
    DataRepository dataRepository;

    StoreSelectorAdapter storeSelectorAdapter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selector);

        application = (EnrichApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Store Selector Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//
//        EnrichUtils.changeStatusBarColor(getWindow());

        setSupportActionBar(toolbar);

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
//        getSupportActionBar().setTitle("Store Selector");

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarWhite);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#000000"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#000000"));

        collapsingToolbarLayout.setTitle("STORES LIST");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        centerListPresenter = new CenterListPresenter(this, dataRepository);

        storeProgress.setVisibility(View.VISIBLE);
        salonFilter.setEnabled(false);
//        getCentreList();


        double latitude = SharedPreferenceStore.getValue(StoreSelectorActivity.this, Constants.CURRENT_LATITUDE, 0.0);
        double longitude = SharedPreferenceStore.getValue(StoreSelectorActivity.this, Constants.CURRENT_LONGITUDE, 0.0);
        if (latitude == 0.0 && longitude == 0.0) {
            getCurrentPlace();
        } else {
            getCentreList(latitude, longitude);
        }

        salonFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                storeSelectorAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getCurrentPlace() {
        PlaceDetectionClient mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(new PlaceFilter());
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                try {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        EnrichUtils.log(String.format("Place '%s' has likelihood: %g", placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood()));
                        if (placeLikelihood.getLikelihood() <= 0.0) {
                            getCentreList(placeLikelihood.getPlace().getLatLng().latitude, placeLikelihood.getPlace().getLatLng().longitude);
                            return;
                        }
                    }
                    likelyPlaces.release();
                } catch (Exception e) {
                    double latitude = SharedPreferenceStore.getValue(StoreSelectorActivity.this, Constants.CURRENT_LATITUDE, 0.0);
                    double longitude = SharedPreferenceStore.getValue(StoreSelectorActivity.this, Constants.CURRENT_LONGITUDE, 0.0);
                    getCentreList(latitude, longitude);
                    Log.e("ERROR TAG: ", e.getLocalizedMessage());
                }
            }
        });
    }

    private void getCentreList(double latitude, double longitude) {
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LATITUDE, latitude);
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LONGITUDE, longitude);

        Map<String, String> map = new HashMap<>();
        map.put("ServiceId", "");
        map.put("latitude", "" + latitude);
        map.put("longitude", "" + longitude);
        centerListPresenter.getCenterList(this, map);
    }

    @Override
    public void showCenterList(CenterResponseModel model) {
        ArrayList<CenterViewModel> list = model.Centers;
        Collections.sort(list, new DistanceComparator());
        setCategoryAdapter(list);
        storeProgress.setVisibility(View.GONE);
        salonFilter.setEnabled(true);
    }

    @Override
    public void showPlaceHolder() {

    }

    private void setCategoryAdapter(ArrayList<CenterViewModel> list) {
        storeSelectorAdapter = new StoreSelectorAdapter(this, list, this);
        storeRecyclerView.setAdapter(storeSelectorAdapter);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        storeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }
}
