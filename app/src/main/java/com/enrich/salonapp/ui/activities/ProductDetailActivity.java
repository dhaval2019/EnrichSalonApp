package com.enrich.salonapp.ui.activities;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.data.model.Product.ProductModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.ProductHomeAdapter;
import com.enrich.salonapp.ui.contracts.ProductDetailsContract;
import com.enrich.salonapp.ui.presenters.ProductDetailsPresenter;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends BaseActivity implements ProductDetailsContract.View {

    @BindView(R.id.product_name)
    TextView productName;

    @BindView(R.id.product_price)
    TextView productPrice;

    @BindView(R.id.product_old_price)
    TextView productOldPrice;

    @BindView(R.id.product_savings)
    TextView productSavings;

    @BindView(R.id.product_description)
    TextView productDescription;

    @BindView(R.id.product_image)
    ImageView productImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.related_products)
    RecyclerView relatedProducts;

    @BindView(R.id.similar_products_container)
    LinearLayout similarProductsContainer;

    @BindView(R.id.cart_container)
    RelativeLayout cartContainer;

    @BindView(R.id.cart_total_items)
    TextView cartTotalItems;

    @BindView(R.id.cart_total_price)
    TextView cartTotalPrice;

    @BindView(R.id.product_remove)
    ImageView productRemove;

    @BindView(R.id.product_add)
    ImageView productAdd;

    @BindView(R.id.product_count)
    TextView productCount;

    @BindView(R.id.cart_next)
    TextView cartNext;

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.cashback_value)
    TextView cashbackValue;

    @BindView(R.id.cashback_container)
    LinearLayout cashbackContainer;

    ProductModel productModel;
//    ArrayList<ProductModel> list;

    EnrichApplication application;

    int productId;

    DataRepository dataRepository;
    ProductDetailsPresenter productDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ButterKnife.bind(this);

        application = (EnrichApplication) getApplication();

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

        collapsingToolbarLayout.setTitle(" ");

        productId = getIntent().getIntExtra("ProductId", 0);

//        list = getIntent().getParcelableArrayListExtra("ProductList");
//        setSimilarProductsAdapter(productModel, list);
//        relatedProducts.setFocusable(false);

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        productDetailsPresenter = new ProductDetailsPresenter(this, dataRepository);

        Map<String, String> map = new HashMap<>();
        map.put("ProductID", "" + productId);
        productDetailsPresenter.getProducts(this, map);

        productRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.decreaseQuantity(productModel);
                updateCart();
                productCount.setText("" + application.getItemQuantity(productModel));
            }
        });

        productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.increaseQuantity(productModel);
                updateCart();
                productCount.setText("" + application.getItemQuantity(productModel));
            }
        });

        cartNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void setSimilarProductsAdapter(ProductModel model, ArrayList<ProductModel> list) {
//        if (!list.isEmpty()) {
//            Iterator<ProductModel> itemIterator = list.iterator();
//            while (itemIterator.hasNext()) {
//                ProductModel productModel = itemIterator.next();
//                if (productModel.ProductId == model.ProductId) {
//                    itemIterator.remove();
//                }
//            }
//
//            ProductHomeAdapter adapter = new ProductHomeAdapter(this, list);
//            relatedProducts.setAdapter(adapter);
//            relatedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        } else {
//            similarProductsContainer.setVisibility(View.GONE);
//        }
//    }

    @SuppressLint("SetTextI18n")
    public void updateCart() {
        EnrichApplication application = (EnrichApplication) getApplication();
        if (application.getCartItems().size() == 0) {
            cartTotalPrice.setText("");
            cartTotalItems.setText(" ");
            cartNext.setEnabled(false);
            cartNext.setBackground(getResources().getDrawable(R.drawable.grey_gradient_curve_bg));
        } else {
            productModel.setQuantity(application.getItemQuantity(productModel));
            cartTotalPrice.setText(getResources().getString(R.string.Rs) + " " + application.getTotalPrice());
            cartTotalItems.setText("" + application.getCartItems().size());
            productCount.setText("" + application.getItemQuantity(productModel));
            cartNext.setEnabled(true);
            cartNext.setBackground(getResources().getDrawable(R.drawable.gold_bg_gradient_curved));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showProductDetails(ProductDetailResponseModel model) {
        if (model.Product != null) {
            productModel = model.Product;

            productName.setText(productModel.ProductTitle);
            productDescription.setText(productModel.ProductDescription);
            productOldPrice.setText(getResources().getString(R.string.rs_symbol) + " " + productModel.OriginalPrice);
            productPrice.setText(getResources().getString(R.string.rs_symbol) + " " + productModel.ProductAmount);
            productSavings.setText("");

            Picasso.get().load(productModel.ImageURL).into(productImage);

            if (productModel.ProductCashBacks != null) {
                cashbackContainer.setVisibility(View.VISIBLE);
                cashbackValue.setText(getResources().getString(R.string.rs_symbol) + " " + productModel.ProductCashBacks.Cost);
            } else {
                cashbackContainer.setVisibility(View.GONE);
            }

            application.setMaxQuantityAllowed(model.Product.SaleLimit);
            updateCart();
        }
    }
}
