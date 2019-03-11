package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.Product.ProductModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.ProductsAdapter;
import com.enrich.salonapp.ui.contracts.ProductContract;
import com.enrich.salonapp.ui.presenters.ProductPresenter;
import com.enrich.salonapp.util.EndlessRecyclerOnScrollListener;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends BaseActivity implements ProductContract.View {

    private static final int FILTER = 17691;

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.product_recycler_view)
    RecyclerView productsRecyclerView;

    @BindView(R.id.drawer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.no_products_available)
    TextView noProductsAvailable;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    ProductPresenter productPresenter;

    DataRepository dataRepository;

    EnrichApplication application;
    Tracker mTracker;

    ProductRequestModel productRequestModel;
    Menu menu;

    ProductsAdapter productsAdapter;

    private int pageSize = 0;
    boolean isFilterApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Product List Screen");
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
//                Intent intent = new Intent(ProductActivity.this, ProductHomePageActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("PRODUCTS");

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        productPresenter = new ProductPresenter(this, dataRepository);

        productRequestModel = getIntent().getParcelableExtra("ProductRequestModel");
        if (productRequestModel == null) {
            productRequestModel = new ProductRequestModel();
        }

        productsAdapter = new ProductsAdapter(this, new ArrayList<ProductModel>(), ProductsAdapter.FULL);
        productsRecyclerView.setAdapter(productsAdapter);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (!(pageSize < 15)) {
                    productRequestModel.Index++;
                    productPresenter.getProducts(ProductActivity.this, productRequestModel);
                }
            }
        });

        productPresenter.getProducts(this, productRequestModel);
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER) {
            if (resultCode == RESULT_OK) {
                isFilterApplied = true;
                String tempProductRequestModel = data.getStringExtra("ProductRequestModelReceive");
                productRequestModel = EnrichUtils.newGson().fromJson(tempProductRequestModel, ProductRequestModel.class);
                EnrichUtils.log(EnrichUtils.newGson().toJson(productRequestModel));
                productPresenter.getProducts(this, productRequestModel);
            }
        }
    }

    private void addBadge(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.action_filter);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        EnrichUtils.setBadgeCount(this, icon, "");
    }

    private void removeBadge(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.action_filter);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        EnrichUtils.removeBadge(this, icon);
    }

    @Override
    public void showProducts(final ProductResponseModel model) {
        pageSize = model.Product.size();
        if (model.Product.isEmpty()) {
            noProductsAvailable.setVisibility(View.VISIBLE);
            productsRecyclerView.setVisibility(View.GONE);
        } else {
            noProductsAvailable.setVisibility(View.GONE);
            productsRecyclerView.setVisibility(View.VISIBLE);
            if (isFilterApplied) {
                productsAdapter.setData(model.Product);
                isFilterApplied = false;
            } else {
                productsAdapter.updateList(model.Product);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            Intent intent = new Intent(ProductActivity.this, ProductFilterActivity.class);
            intent.putExtra("ProductRequestModelSend", EnrichUtils.newGson().toJson(productRequestModel));
            startActivityForResult(intent, FILTER);
        }
        return true;
    }
}
