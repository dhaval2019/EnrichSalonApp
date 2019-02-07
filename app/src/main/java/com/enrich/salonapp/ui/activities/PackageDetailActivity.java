package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.Package.PackageModel;
import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.data.model.Product.ProductModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.ExpandablePackageBundleAdapter;
import com.enrich.salonapp.ui.contracts.PackageDetailsContract;
import com.enrich.salonapp.ui.presenters.PackageDetailsPresenter;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

public class PackageDetailActivity extends BaseActivity implements PackageDetailsContract.View {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.package_detail_image)
    ImageView packageDetailImage;

    @BindView(R.id.package_detail_name)
    TextView packageDetailName;

    @BindView(R.id.package_detail_price)
    TextView packageDetailPrice;

    @BindView(R.id.package_detail_description)
    TextView packageDetailDescription;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.package_bundle_recycler_view)
    RecyclerView packageBundleRecyclerView;

    @BindView(R.id.package_cart_container)
    LinearLayout packageCartContainer;

    @BindView(R.id.package_total_items)
    TextView packageTotalItems;

    @BindView(R.id.package_total_price)
    TextView packageTotalPrice;

    @BindView(R.id.package_next)
    TextView packageNext;

    @BindView(R.id.cart_container)
    RelativeLayout cartContainer;

    @BindView(R.id.no_details_found)
    LinearLayout noDetailsFound;

    @BindView(R.id.package_detail_container)
    NestedScrollView packageDetailContainer;

    @BindView(R.id.package_end_day_count_down)
    CountdownView packageEndDayCountDown;

    @BindView(R.id.package_count_down_container)
    LinearLayout packageCountDownContainer;

    int packageId;

    DataRepository dataRepository;
    PackageDetailsPresenter packageDetailsPresenter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        ButterKnife.bind(this);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Package Detail Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

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

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarWhite);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("Package Details");
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#000000"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#000000"));

        packageId = getIntent().getIntExtra("CreateOrderPackageBundleModel", 0);

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        packageDetailsPresenter = new PackageDetailsPresenter(this, dataRepository);

        Map<String, String> map = new HashMap<>();
        map.put("packageID", "" + packageId);
        packageDetailsPresenter.getPackageDetails(this, map);

        packageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PackageDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        updateCart();
    }

    public void updateCart() {
        EnrichApplication application = (EnrichApplication) getApplication();
        if (application.getCartItems().size() == 0) {
            packageCartContainer.setVisibility(View.GONE);
        } else {
            packageCartContainer.setVisibility(View.VISIBLE);
            packageTotalPrice.setText(getResources().getString(R.string.Rs) + " " + application.getTotalPrice());
            packageTotalItems.setText("" + application.getCartItems().size());
        }
    }

    @Override
    public void showPackageDetails(PackageDetailsResponseModel model) {
        if (model.Package != null) {

            noDetailsFound.setVisibility(View.GONE);
            packageDetailContainer.setVisibility(View.VISIBLE);

            PackageModel packageModel = model.Package;
            logFirebaseViewItem(packageModel);

            if (packageModel.IsTimerEnable) {
                packageCountDownContainer.setVisibility(View.VISIBLE);

                try {
                    String dateStr = packageModel.PackageValidityDate.substring(0, 10) + " " + packageModel.PackageValidityDate.substring(11);
                    SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date endDate = stringToDate.parse(dateStr);
                    Date startDate = new Date();

                    long numberOfDaysInMilliseconds = (endDate.getTime() - startDate.getTime());

                    packageEndDayCountDown.start(numberOfDaysInMilliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                    packageEndDayCountDown.setVisibility(View.GONE);
                }
            } else {
                packageCountDownContainer.setVisibility(View.GONE);
            }

            packageDetailName.setText(packageModel.PackageTitle);
            packageDetailDescription.setText(packageModel.PackageDescription);
            packageDetailPrice.setText(String.format("%s %d", getResources().getString(R.string.Rs), (int) packageModel.StartingPrice));

            Picasso.get().load(packageModel.PackageImageWideURL).placeholder(R.drawable.placeholder_ext).into(packageDetailImage);

            ExpandablePackageBundleAdapter packageBundleAdapter = new ExpandablePackageBundleAdapter(this, packageModel.packageBundle);
            packageBundleRecyclerView.setAdapter(packageBundleAdapter);
            packageBundleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            packageBundleRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else {
            noDetailsFound.setVisibility(View.VISIBLE);
            packageDetailContainer.setVisibility(View.GONE);
        }
    }

    private void logFirebaseViewItem(PackageModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(model.PackageId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.PackageTitle);
        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
