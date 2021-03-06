package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.ProductSubCategoryAdapter;
import com.enrich.salonapp.ui.contracts.ProductFilterContract;
import com.enrich.salonapp.ui.presenters.ProductFilterPresenter;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSubCategoryActivity extends BaseActivity implements ProductFilterContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.product_sub_category_recycler_view)
    RecyclerView productSubCategoryRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    DataRepository dataRepository;
    ProductFilterPresenter productFilterPresenter;

    ProductCategoryModel productCategoryModel;

    ProductSubCategoryAdapter productSubCategoryAdapter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sub_category);

        productCategoryModel = getIntent().getParcelableExtra("ProductRequestModel");

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Product SubCategories List Screen");
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

        collapsingToolbarLayout.setTitle(productCategoryModel.Name);

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        productFilterPresenter = new ProductFilterPresenter(this, dataRepository);

        productFilterPresenter.getProductSubCategoriesList(this);
    }

    @Override
    public void showBrands(BrandResponseModel model) {

    }

    @Override
    public void showProductCategories(ProductCategoryResponseModel model) {

    }

    @Override
    public void showProductSubCategories(ProductSubCategoryResponseModel model) {
        if (!model.ProductSubCategory.isEmpty()) {
            ArrayList<ProductSubCategoryModel> list = new ArrayList<>();

            for (int i = 0; i <model.ProductSubCategory.size(); i++) {
                if (model.ProductSubCategory.get(i).ProductCategory.Id == productCategoryModel.Id) {
                    list.add(model.ProductSubCategory.get(i));
                }
            }

            productSubCategoryAdapter = new ProductSubCategoryAdapter(this, list);
            productSubCategoryRecyclerView.setAdapter(productSubCategoryAdapter);
            productSubCategoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    @Override
    public void showProductOffers(OfferResponseModel model) {

    }

    @Override
    public void noProductOffers() {

    }
}
