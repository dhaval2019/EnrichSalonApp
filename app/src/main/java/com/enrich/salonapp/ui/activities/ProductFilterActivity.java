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
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.BrandAdapter;
import com.enrich.salonapp.ui.adapters.ProductCategoryAdapter;
import com.enrich.salonapp.ui.adapters.ProductSubCategoryAdapter;
import com.enrich.salonapp.ui.contracts.ProductFilterContract;
import com.enrich.salonapp.ui.presenters.ProductFilterPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductFilterActivity extends BaseActivity implements ProductFilterContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.filter_recycler_view)
    RecyclerView filterRecyclerView;

    @BindView(R.id.brand_button)
    TextView brandButton;

    @BindView(R.id.product_category_button)
    TextView productCategoryButton;

    @BindView(R.id.product_sub_category_button)
    TextView productSubCategoryButton;

    @BindView(R.id.cancel_filter)
    Button cancelFilter;

    @BindView(R.id.apply_filter)
    Button applyFilter;

    @BindView(R.id.clear_filter)
    TextView clearFilter;

    DataRepository dataRepository;
    ProductFilterPresenter productFilterPresenter;

    ProductRequestModel productRequestModel;

    BrandAdapter brandAdapter;
    ProductCategoryAdapter productCategoryAdapter;
    ProductSubCategoryAdapter productSubCategoryAdapter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Product Filter Screen");
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
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("Filter");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        productFilterPresenter = new ProductFilterPresenter(this, dataRepository);

        productRequestModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ProductRequestModelSend"), ProductRequestModel.class);

        brandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor(0);
                productFilterPresenter.getBrandsList(ProductFilterActivity.this);
            }
        });

        productCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor(1);
                productFilterPresenter.getProductCategoriesList(ProductFilterActivity.this);
            }
        });

        productSubCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor(2);
                productFilterPresenter.getProductSubCategoriesList(ProductFilterActivity.this);
            }
        });

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("ProductRequestModelReceive", EnrichUtils.newGson().toJson(productRequestModel));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRequestModel = new ProductRequestModel();

                if (brandAdapter != null) {
                    brandAdapter.notifyDataSetChanged();
                }

                if (productCategoryAdapter != null) {
                    productCategoryAdapter.notifyDataSetChanged();
                }

                if (productSubCategoryAdapter != null) {
                    productSubCategoryAdapter.notifyDataSetChanged();
                }
            }
        });

        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateSubCategory();
        changeButtonColor(0);
        productFilterPresenter.getBrandsList(this);
    }

    public ProductRequestModel getProductRequestModel() {
        return productRequestModel;
    }

    public void updateSubCategory() {
        if (productRequestModel.ProductCategoryIds.isEmpty()) {
            productRequestModel.ProductSubCategoryIds = new ArrayList<>();
            productSubCategoryButton.setVisibility(View.GONE);
        } else {
            productSubCategoryButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void changeButtonColor(int isSelected) {
        switch (isSelected) {
            case 0:
                brandButton.setTextColor(Color.parseColor("#d69e5c"));
                productCategoryButton.setTextColor(Color.parseColor("#838485"));
                productSubCategoryButton.setTextColor(Color.parseColor("#838485"));
                break;
            case 1:
                brandButton.setTextColor(Color.parseColor("#838485"));
                productCategoryButton.setTextColor(Color.parseColor("#d69e5c"));
                productSubCategoryButton.setTextColor(Color.parseColor("#838485"));
                break;
            case 2:
                brandButton.setTextColor(Color.parseColor("#838485"));
                productCategoryButton.setTextColor(Color.parseColor("#838485"));
                productSubCategoryButton.setTextColor(Color.parseColor("#d69e5c"));
                break;
        }
    }

    @Override
    public void showBrands(BrandResponseModel model) {
        if (!model.Brands.isEmpty()) {
            brandAdapter = new BrandAdapter(model.Brands, this);
            filterRecyclerView.setAdapter(brandAdapter);
            filterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void showProductCategories(ProductCategoryResponseModel model) {
        if (!model.ProductCategory.isEmpty()) {
            productCategoryAdapter = new ProductCategoryAdapter(this, model.ProductCategory);
            filterRecyclerView.setAdapter(productCategoryAdapter);
            filterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void showProductSubCategories(ProductSubCategoryResponseModel model) {
        if (!model.ProductSubCategory.isEmpty()) {
            productSubCategoryAdapter = new ProductSubCategoryAdapter(this, model.ProductSubCategory);
            filterRecyclerView.setAdapter(productSubCategoryAdapter);
            filterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void showProductOffers(OfferResponseModel model) {

    }

    @Override
    public void noProductOffers() {

    }
}
