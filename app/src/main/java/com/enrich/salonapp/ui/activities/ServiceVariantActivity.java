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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ServiceList.ServiceVariantResponseModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.VariantRecyclerViewAdapter;
import com.enrich.salonapp.ui.contracts.ServiceVariantsContract;
import com.enrich.salonapp.ui.fragments.LoginBottomSheetFragment;
import com.enrich.salonapp.ui.presenters.ServiceVariantsPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.ParentAndNormalServiceComparator;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceVariantActivity extends BaseActivity implements ServiceVariantsContract.View {

//    @BindView(R.id.drawer_collapse_toolbar)
//    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.service_variant_recycler_view)
    RecyclerView variantRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.parent_service_name)
    TextView parentServiceName;

    @BindView(R.id.parent_service_description)
    TextView parentServiceDescription;

    @BindView(R.id.service_cart_container)
    LinearLayout serviceCartContainer;

    @BindView(R.id.service_total_items)
    TextView serviceTotalItems;

    @BindView(R.id.service_total_price)
    TextView serviceTotalPrice;

    @BindView(R.id.service_next)
    TextView serviceNext;

    @BindView(R.id.no_service_available)
    TextView noServiceAvailable;

    @BindView(R.id.parent_service_logo)
    ImageView parentServiceLogo;

    ServiceViewModel serviceViewModel;
    String subCategoryId;

    DataRepository dataRepository;
    ServiceVariantsPresenter serviceVariantsPresenter;

    EnrichApplication application;
    Tracker mTracker;

    String gender;

    boolean isHomeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_variant);

        ButterKnife.bind(this);

        isHomeSelected = getIntent().getBooleanExtra("isHomeSelected", false);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Service Variant List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        serviceViewModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ServiceViewModel"), ServiceViewModel.class);
        subCategoryId = getIntent().getStringExtra("SubCategoryId");
        gender = getIntent().getStringExtra("Gender");

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Variants");
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        parentServiceName.setText(serviceViewModel.name);
        parentServiceDescription.setText(serviceViewModel.description);
        Picasso.with(this).load(serviceViewModel.imagePaths.px100).placeholder(R.drawable.placeholder).into(parentServiceLogo);

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        serviceVariantsPresenter = new ServiceVariantsPresenter(this, dataRepository);

        Map<String, String> map = new HashMap<>();
        map.put("CenterId", EnrichUtils.getHomeStore(this).Id);
        map.put("SubCategoryId", subCategoryId);
        if (EnrichUtils.getUserData(this) != null)
            map.put("GuestId", EnrichUtils.getUserData(this).Id);
        map.put("ParentServiceId", serviceViewModel.id);
        map.put("Tag", gender);
        serviceVariantsPresenter.getServiceVariants(this, map);

        serviceNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnrichUtils.getUserData(ServiceVariantActivity.this) != null) {
                    if (!application.isCartEmpty()) {
                        Intent intent = new Intent(ServiceVariantActivity.this, DateSelectorActivity.class);
                        intent.putExtra("isHomeSelected", isHomeSelected);
                        startActivity(intent);
                    }
                } else {
                    LoginBottomSheetFragment.getInstance().show(getSupportFragmentManager(), "login_bottomsheet_fragment");
                }
            }
        });

        updateCart();
    }

    @Override
    public void showServiceVariants(ServiceVariantResponseModel model) {
        if (!model.ServiceVariants.isEmpty()) {
            noServiceAvailable.setVisibility(View.GONE);
            variantRecyclerView.setVisibility(View.VISIBLE);

            Collections.sort(model.ServiceVariants, new ParentAndNormalServiceComparator());

            VariantRecyclerViewAdapter variantRecyclerViewAdapter = new VariantRecyclerViewAdapter(this, model.ServiceVariants, isHomeSelected);
            variantRecyclerView.setAdapter(variantRecyclerViewAdapter);
            variantRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            variantRecyclerView.setNestedScrollingEnabled(false);
        } else {
            noServiceAvailable.setVisibility(View.VISIBLE);
            variantRecyclerView.setVisibility(View.GONE);
        }
    }

    public void updateCart() {
        EnrichApplication application = (EnrichApplication) getApplication();
        if (application.getCartItems().size() == 0) {
            serviceCartContainer.setVisibility(View.GONE);
        } else {
            serviceCartContainer.setVisibility(View.VISIBLE);
            serviceTotalPrice.setText(getResources().getString(R.string.Rs) + " " + (int) application.getTotalPrice());
            serviceTotalItems.setText("" + application.getCartItems().size());
        }
    }
}
