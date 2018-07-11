package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CategoryModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.ParentServiceViewModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.CategorySpinnerAdapter;
import com.enrich.salonapp.ui.adapters.NewServiceListAdapter;
import com.enrich.salonapp.ui.contracts.CategoryContract;
import com.enrich.salonapp.ui.contracts.ServiceListContract;
import com.enrich.salonapp.ui.presenters.CategoryPresenter;
import com.enrich.salonapp.ui.presenters.ServiceListPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.SubCategoryComparator;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceListActivity extends BaseActivity implements ServiceListContract.View, CategoryContract.View {

    @BindView(R.id.category_name_spinner)
    Spinner categoryNameSpinner;

    @BindView(R.id.service_recycler_view)
    RecyclerView serviceRecyclerView;

    @BindView(R.id.category_dropdown_container)
    LinearLayout categoryDropdownContainer;

    @BindView(R.id.service_cart_container)
    LinearLayout serviceCartContainer;

    @BindView(R.id.service_total_items)
    TextView serviceTotalItems;

    @BindView(R.id.service_total_price)
    TextView serviceTotalPrice;

    @BindView(R.id.service_next)
    TextView serviceNext;

    @BindView(R.id.service_filter)
    EditText serviceFilter;

    @BindView(R.id.service_filter_container)
    RelativeLayout serviceListFilterContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.male_container)
    LinearLayout maleContainer;

    @BindView(R.id.female_container)
    LinearLayout femaleContainer;

    @BindView(R.id.female_icon)
    ImageView femaleIcon;

    @BindView(R.id.female_text)
    TextView femaleText;

    @BindView(R.id.male_icon)
    ImageView maleIcon;

    @BindView(R.id.male_text)
    TextView maleText;

    @BindView(R.id.no_service_available)
    TextView noServiceAvailable;

    ArrayList<ParentServiceViewModel> serviceList;

    EnrichApplication application;

    int position;

    DataRepository dataRepository;
    ServiceListPresenter serviceListPresenter;
    CategoryPresenter categoryPresenter;
    CategoryModel categoryModel;

    NewServiceListAdapter adapter;

    Tracker mTracker;

    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list_new);

        ButterKnife.bind(this);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Service List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
        getSupportActionBar().setTitle("");

        position = getIntent().getIntExtra("CategoryListPosition", 0);

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        serviceListPresenter = new ServiceListPresenter(this, dataRepository);
        categoryPresenter = new CategoryPresenter(this, dataRepository);

        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("CenterId", EnrichUtils.getHomeStore(this).Id);
        categoryMap.put("parentCategoryId", Constants.PARENT_CATEGORY_ID);
        categoryPresenter.getCategoriesList(this, categoryMap, true);

        categoryDropdownContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNameSpinner.performClick();
            }
        });

        categoryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryModel = (CategoryModel) parent.getAdapter().getItem(position);

                if (gender == null) {
                    gender = EnrichUtils.getUserData(ServiceListActivity.this).Gender.toLowerCase();
                }

                changeGenderIcons(!gender.equalsIgnoreCase("male"));
                getServiceList(categoryModel.CategoryId, gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        serviceNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!application.isCartEmpty()) {
                    Intent intent = new Intent(ServiceListActivity.this, DateSelectorActivity.class);
                    startActivity(intent);
                }
            }
        });

        serviceFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGenderIcons(false);
                getServiceList(categoryModel.CategoryId, "male");
            }
        });

        femaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGenderIcons(true);
                getServiceList(categoryModel.CategoryId, "female");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

    private void changeGenderIcons(boolean isFemale) {
        if (isFemale) {
            ImageViewCompat.setImageTintList(femaleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
            femaleText.setTextColor(getResources().getColor(R.color.colorAccent));

            ImageViewCompat.setImageTintList(maleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
            maleText.setTextColor(getResources().getColor(R.color.grey));
        } else {

            ImageViewCompat.setImageTintList(maleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
            maleText.setTextColor(getResources().getColor(R.color.colorAccent));

            ImageViewCompat.setImageTintList(femaleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
            femaleText.setTextColor(getResources().getColor(R.color.grey));
        }
    }

    private void setCategorySpinner(ArrayList<CategoryModel> categoryList) {
        CategorySpinnerAdapter categorySpinnerAdapter = new CategorySpinnerAdapter(this, categoryList);
        categoryNameSpinner.setAdapter(categorySpinnerAdapter);
        categoryNameSpinner.setSelection(position);
    }

    private void getServiceList(String categoryId, String gender) {
        this.gender = gender;
        Map<String, String> map = new HashMap<>();
        map.put("CategoryId", categoryId);
        map.put("CenterId", EnrichUtils.getHomeStore(this).Id);

        if (gender.equalsIgnoreCase("male")) {
            map.put("gender", "1");
        } else if (gender.equalsIgnoreCase("female")) {
            map.put("gender", "2");
        } else {
            map.put("gender", "0");
        }

//        serviceListPresenter.getServiceList(this, map);
        serviceListPresenter.getSubCategories(this, map);
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

    @Override
    public void showServiceList(ServiceListResponseModel model) {
        if (model.Services.size() != 0) {
            ArrayList<ParentServiceViewModel> list = new ArrayList<>();
            for (int i = 0; i < model.Services.size(); i++) {
                if (!model.Services.get(i).ChildServices.isEmpty()) {
                    list.add(model.Services.get(i));
                }
            }

            serviceList = list;
            noServiceAvailable.setVisibility(View.GONE);
            serviceRecyclerView.setVisibility(View.VISIBLE);
//            setExpandableServiceListAdapter(list);
        } else {
            if (model.Error != null) {
                EnrichUtils.showMessage(ServiceListActivity.this, "" + model.Error.Message);
            }
            noServiceAvailable.setVisibility(View.VISIBLE);
            serviceRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSubCategories(SubCategoryResponseModel model) {
        if (!model.SubCategories.isEmpty()) {
            noServiceAvailable.setVisibility(View.GONE);
            serviceRecyclerView.setVisibility(View.VISIBLE);

            for (int i = 0; i < model.SubCategories.size(); i++) {
                model.SubCategories.get(i).ChildServices = new ArrayList<>();
            }

            Collections.sort(model.SubCategories, new SubCategoryComparator());

            adapter = new NewServiceListAdapter(this, model.SubCategories, gender);
            serviceRecyclerView.setAdapter(adapter);
            serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            noServiceAvailable.setVisibility(View.VISIBLE);
            serviceRecyclerView.setVisibility(View.GONE);
        }
    }

//    private void setExpandableServiceListAdapter(ArrayList<ParentServiceViewModel> list) {
//        adapter = new ServiceListAdapter(list, this);
//        serviceRecyclerView.setAdapter(adapter);
//        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        application.clearCart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.service_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.service_list_filter) {
            if (serviceListFilterContainer.getVisibility() == View.VISIBLE) {
                serviceFilter.setText("");
                serviceListFilterContainer.setVisibility(View.GONE);
            } else {
                serviceListFilterContainer.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCategoryList(CategoryResponseModel model) {
        if (!model.Categories.isEmpty()) {
            setCategorySpinner(model.Categories);
        }
    }
}
