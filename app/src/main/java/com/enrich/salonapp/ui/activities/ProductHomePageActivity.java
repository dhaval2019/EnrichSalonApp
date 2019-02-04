package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.ProductHomePage.ProductHomePageBrandAdapter;
import com.enrich.salonapp.ui.adapters.ProductHomePage.ProductHomePageCategoryAdapter;
import com.enrich.salonapp.ui.adapters.ProductHomePage.ProductOfferAdapter;
import com.enrich.salonapp.ui.adapters.ProductHomePageSubCategoryAdapter;
import com.enrich.salonapp.ui.adapters.ProductsAdapter;
import com.enrich.salonapp.ui.contracts.HomePageContract;
import com.enrich.salonapp.ui.contracts.ProductContract;
import com.enrich.salonapp.ui.contracts.ProductFilterContract;
import com.enrich.salonapp.ui.presenters.HomePagePresenter;
import com.enrich.salonapp.ui.presenters.ProductFilterPresenter;
import com.enrich.salonapp.ui.presenters.ProductPresenter;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductHomePageActivity extends BaseActivity implements ProductFilterContract.View, ProductContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.product_category_list)
    RecyclerView productCategoryList;

    @BindView(R.id.product_brand_list)
    RecyclerView productBrandList;

    @BindView(R.id.product_list)
    RecyclerView productList;

    @BindView(R.id.view_all_products)
    Button viewAllProducts;

    @BindView(R.id.product_offer_view_pager)
    ViewPager productOfferViewPager;

    @BindView(R.id.product_offer_container)
    LinearLayout productOfferContainer;

    @BindView(R.id.product_offer_page_indicator)
    PageIndicatorView productOfferPageIndicator;

    EnrichApplication application;
    Tracker mTracker;

    DataRepository dataRepository;
    ProductFilterPresenter productFilterPresenter;
    ProductPresenter productPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new);

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

        collapsingToolbarLayout.setTitle("PRODUCTS");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        productFilterPresenter = new ProductFilterPresenter(this, dataRepository);
        productPresenter = new ProductPresenter(this, dataRepository);

//        productFilterPresenter.getProductCategoriesList(this);
        productFilterPresenter.getProductSubCategoriesList(this);
        productFilterPresenter.getBrandsList(this);
        productFilterPresenter.getProductOffers(this);
        productPresenter.getProducts(this, new ProductRequestModel());

        viewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductHomePageActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        productOfferViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                productOfferPageIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showBrands(BrandResponseModel model) {
        if (!model.Brands.isEmpty()) {
            ProductHomePageBrandAdapter adapter = new ProductHomePageBrandAdapter(this, model.Brands);
            productBrandList.setAdapter(adapter);
            productBrandList.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void showProductCategories(ProductCategoryResponseModel model) {
//        if (!model.ProductCategory.isEmpty()) {
//            ProductHomePageCategoryAdapter adapter = new ProductHomePageCategoryAdapter(this, model.ProductCategory);
//            productCategoryList.setAdapter(adapter);
//            productCategoryList.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
//        }
    }

    @Override
    public void showProductSubCategories(ProductSubCategoryResponseModel model) {
        if (!model.ProductSubCategory.isEmpty()) {
            ProductHomePageSubCategoryAdapter adapter = new ProductHomePageSubCategoryAdapter(this, model.ProductSubCategory);
            productCategoryList.setAdapter(adapter);
            productCategoryList.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        }
    }

    @Override
    public void showProductOffers(OfferResponseModel model) {
        if (!model.Offers.isEmpty()) {
            productOfferContainer.setVisibility(View.VISIBLE);
            ProductOfferAdapter adapter = new ProductOfferAdapter(this, model.Offers);
            productOfferViewPager.setAdapter(adapter);
            productOfferPageIndicator.setCount(model.Offers.size());
        } else {
            productOfferContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void noProductOffers() {
        productOfferContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProducts(ProductResponseModel model) {
        if (!model.Product.isEmpty()) {
            ArrayList<ProductModel> list = model.Product.size() <= 4 ? model.Product : new ArrayList<>(model.Product.subList(0, 4));

            ProductsAdapter adapter = new ProductsAdapter(this, list, ProductsAdapter.COMPACT);
            productList.setAdapter(adapter);
            productList.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }
}
