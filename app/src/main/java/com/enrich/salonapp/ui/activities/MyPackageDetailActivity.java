package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.MyPackage.MyPackageModel;
import com.enrich.salonapp.ui.adapters.ExpandableMyPackageBundleAdapter;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPackageDetailActivity extends AppCompatActivity {

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

    MyPackageModel packageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_package_detail);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        EnrichUtils.changeStatusBarColor(getWindow());

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
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#000000"));

        packageModel = getIntent().getParcelableExtra("MyPackageDetail");

        packageDetailName.setText(packageModel.PackageTitle);
        packageDetailDescription.setText(packageModel.PackageDescription);
        packageDetailPrice.setText(getResources().getString(R.string.Rs) + " " + packageModel.StartingPrice);
        Picasso.get().load(packageModel.PackageImageURL).into(packageDetailImage);

        ExpandableMyPackageBundleAdapter packageBundleAdapter = new ExpandableMyPackageBundleAdapter(this, packageModel.PackageBundle);
        packageBundleRecyclerView.setAdapter(packageBundleAdapter);
        packageBundleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        packageBundleRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        packageBundleRecyclerView.setFocusable(false);
        packageBundleRecyclerView.setNestedScrollingEnabled(true);
    }
}
