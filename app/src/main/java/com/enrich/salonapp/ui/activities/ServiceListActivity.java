package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v4.widget.NestedScrollView;
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

import com.crashlytics.android.Crashlytics;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CategoryModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.ParentServiceViewModel;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.CategorySpinnerAdapter;
import com.enrich.salonapp.ui.adapters.HomeParentAndNormalServiceAdapter;
import com.enrich.salonapp.ui.adapters.HomeSubCategorySpinnerAdapter;
import com.enrich.salonapp.ui.adapters.NewServiceListAdapter;
import com.enrich.salonapp.ui.adapters.SampleHomeParentAndNormalServiceAdapter;
import com.enrich.salonapp.ui.contracts.CategoryContract;
import com.enrich.salonapp.ui.contracts.ParentsAndNormalServiceListContract;
import com.enrich.salonapp.ui.contracts.ServiceListContract;
import com.enrich.salonapp.ui.fragments.LoginBottomSheetFragment;
import com.enrich.salonapp.ui.presenters.CategoryPresenter;
import com.enrich.salonapp.ui.presenters.ParentsAndNormalServiceListPresenter;
import com.enrich.salonapp.ui.presenters.ServiceListPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.LoginListener;
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

public class ServiceListActivity extends BaseActivity implements ServiceListContract.View, CategoryContract.View, ParentsAndNormalServiceListContract.View, LoginListener {

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

    @BindView(R.id.recycler_view_container)
    NestedScrollView recyclerViewContainer;

    @BindView(R.id.member_text)
    TextView memberText;

    ArrayList<ParentServiceViewModel> serviceList;

    EnrichApplication application;

    int position;

    private DataRepository dataRepository;
    private ServiceListPresenter serviceListPresenter;
    private CategoryPresenter categoryPresenter;
    private ParentsAndNormalServiceListPresenter parentsAndNormalServiceListPresenter;

    CategoryModel categoryModel;
    SubCategoryModel subCategoryModel;
    CenterDetailModel centerDetailModel;

    NewServiceListAdapter adapter;
    HomeParentAndNormalServiceAdapter parentAndNormalServiceAdapter;

    Tracker mTracker;

    int gender;
    String genderStr;

    boolean isHomeSelected;
    boolean isRebook;

    BottomSheetDialog dialog;

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
        getSupportActionBar().setTitle("");

        position = getIntent().getIntExtra("CategoryListPosition", 0);
        isHomeSelected = getIntent().getBooleanExtra("isHomeSelected", false);
        isRebook = getIntent().getBooleanExtra("isRebook", false);
        gender = getIntent().getIntExtra("Gender", 0);

        if (isRebook) {
            centerDetailModel = EnrichUtils.getHomeStoreForRebook(this);
        } else {
            centerDetailModel = EnrichUtils.getHomeStore(this);
        }

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        serviceListPresenter = new ServiceListPresenter(this, dataRepository);
        categoryPresenter = new CategoryPresenter(this, dataRepository);
        parentsAndNormalServiceListPresenter = new ParentsAndNormalServiceListPresenter(this, dataRepository);

        if (isHomeSelected) {
            maleContainer.setVisibility(View.GONE);
            genderStr = "Female";
            changeGenderIcons(true);
            getServiceList(Constants.HOME_CATEGORY_ID, Constants.FEMALE);
        } else {
            Map<String, String> categoryMap = new HashMap<>();
            categoryMap.put("CenterId", centerDetailModel.Id);
            categoryMap.put("parentCategoryId", Constants.PARENT_CATEGORY_ID);
            if (isHomeSelected) {
                categoryMap.put("HomeCategory", "home");
            }
            categoryPresenter.getCategoriesList(this, categoryMap, true);
        }

        categoryDropdownContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNameSpinner.performClick();
            }
        });

        categoryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isHomeSelected) {
                    subCategoryModel = (SubCategoryModel) parent.getAdapter().getItem(position);
                    getServiceListForHome();
                } else {
                    categoryModel = (CategoryModel) parent.getAdapter().getItem(position);

                    if (gender == 0) {
                        if (isHomeSelected) {
                            gender = Constants.FEMALE;
                            genderStr = "Female";
                        } else {
                            if (EnrichUtils.getUserData(ServiceListActivity.this) != null) {
                                gender = EnrichUtils.getUserData(ServiceListActivity.this).Gender;
                                genderStr = EnrichUtils.getUserData(ServiceListActivity.this).Gender == 1 ? "Male" : "Female";
                            } else {
                                gender = Constants.FEMALE;
                                genderStr = "Female";
                            }
                        }
                    }

                    changeGenderIcons(gender != Constants.MALE);
                    getServiceList(categoryModel.CategoryId, gender);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        serviceNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnrichUtils.getUserData(ServiceListActivity.this) != null) {
                    if (!application.isCartEmpty()) {
                        Intent intent = new Intent(ServiceListActivity.this, DateSelectorActivity.class);
                        intent.putExtra("isHomeSelected", isHomeSelected);
                        startActivity(intent);
                    }
                } else {
                    // SHOW LOGIN DIALOG
                    showLoginDialog();
                }
            }
        });

        serviceFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Crashlytics.setString("ServiceFilterString", s.toString());
                if (isHomeSelected) {
//                    parentAndNormalServiceAdapter.getFilter().filter(s);
                } else {
                    if (adapter != null)
                        adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGenderIcons(false);
                getServiceList(categoryModel.CategoryId, Constants.MALE);
            }
        });

        femaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGenderIcons(true);
                if (!isHomeSelected) {
                    getServiceList(categoryModel.CategoryId, Constants.FEMALE);
                } else {
                    showToastMessage("Home Services are currently only available for Females.");
                }
            }
        });
    }

    private void showLoginDialog() {
        LoginBottomSheetFragment.getInstance(this).show(getSupportFragmentManager(), "login_bottomsheet_fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
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
            recyclerViewContainer.setVisibility(View.VISIBLE);
//            setExpandableServiceListAdapter(list);
        } else {
            if (model.Error != null) {
                EnrichUtils.showMessage(ServiceListActivity.this, "" + model.Error.Message);
            }
            noServiceAvailable.setVisibility(View.VISIBLE);
            recyclerViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSubCategories(SubCategoryResponseModel model) {

        if (EnrichUtils.getUserData(this) != null) {
            if (EnrichUtils.getUserData(this).IsMember == Constants.IS_MEMBER) { //is a member
                memberText.setVisibility(View.VISIBLE);
            } else {
                memberText.setVisibility(View.GONE);
            }
        } else {
            memberText.setVisibility(View.GONE);
        }

        if (isHomeSelected) {
            setHomeSubCategorySpinner(model.SubCategories);
        } else {
            if (!model.SubCategories.isEmpty()) {
                noServiceAvailable.setVisibility(View.GONE);
                recyclerViewContainer.setVisibility(View.VISIBLE);

                for (int i = 0; i < model.SubCategories.size(); i++) {
                    model.SubCategories.get(i).ChildServices = new ArrayList<>();
                }

                Collections.sort(model.SubCategories, new SubCategoryComparator());

                adapter = new NewServiceListAdapter(this, model.SubCategories, genderStr, isHomeSelected, centerDetailModel);
                serviceRecyclerView.setAdapter(adapter);
                serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            } else {
                if (model.Category.Name.equalsIgnoreCase("BODY")) {
                    noServiceAvailable.setText("This category is limited to women only.");
                }
                noServiceAvailable.setVisibility(View.VISIBLE);
                recyclerViewContainer.setVisibility(View.GONE);
            }
        }

        updateCart();
    }

    @Override
    public void noSubCategories() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        application.clearCart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isHomeSelected)
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

    private void changeGenderIcons(boolean isFemale) {
        if (isFemale) {
            genderStr = "Female";
            ImageViewCompat.setImageTintList(femaleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
            femaleText.setTextColor(getResources().getColor(R.color.colorAccent));

            ImageViewCompat.setImageTintList(maleIcon, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
            maleText.setTextColor(getResources().getColor(R.color.grey));
        } else {
            genderStr = "Male";
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

    private void setHomeSubCategorySpinner(ArrayList<SubCategoryModel> list) {
        HomeSubCategorySpinnerAdapter adapter = new HomeSubCategorySpinnerAdapter(this, list);
        categoryNameSpinner.setAdapter(adapter);
        categoryNameSpinner.setSelection(position);
    }

    private void getServiceList(String categoryId, int gender) {
        this.gender = gender;
        Map<String, String> map = new HashMap<>();
        map.put("CategoryId", categoryId);
        map.put("CenterId", centerDetailModel.Id);

        if (gender == Constants.MALE) {
            map.put("gender", "1");
        } else if (gender == Constants.FEMALE) {
            map.put("gender", "2");
        } else {
            map.put("gender", "0");
        }
        serviceListPresenter.getSubCategories(this, map);
    }

    private void getServiceListForHome() {
        Map<String, String> map = new HashMap<>();
        map.put("CenterId", centerDetailModel.Id);
        map.put("SubCategoryId", subCategoryModel.SubCategoryId);
        if (EnrichUtils.getUserData(this) != null)
            map.put("GuestId", EnrichUtils.getUserData(this).Id);
        map.put("Tag", genderStr);

        parentsAndNormalServiceListPresenter.getParentAndNormalServiceList(this, map);
    }

    public void updateCart() {
        EnrichApplication application = (EnrichApplication) getApplication();
        if (application.getCartItems().size() == 0) {
            serviceCartContainer.setVisibility(View.GONE);
        } else {
            serviceCartContainer.setVisibility(View.VISIBLE);
            serviceTotalPrice.setText(String.format("%s %d", getResources().getString(R.string.Rs),  (int) Math.round( application.getTotalPrice())));
            serviceTotalItems.setText(String.format("%d", application.getCartItems().size()));
        }
    }

    @Override
    public void showParentAndNormalServiceList(ParentAndNormalServiceListResponseModel model) {
        if (!model.ParentAndNormalServiceList.isEmpty()) {

            noServiceAvailable.setVisibility(View.GONE);
            recyclerViewContainer.setVisibility(View.VISIBLE);
            serviceRecyclerView.setVisibility(View.VISIBLE);

            SampleHomeParentAndNormalServiceAdapter adapter = new SampleHomeParentAndNormalServiceAdapter(this, model.ParentAndNormalServiceList, genderStr, subCategoryModel, isHomeSelected);
            serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            serviceRecyclerView.setHasFixedSize(true);
            serviceRecyclerView.setAdapter(adapter);

            //            parentAndNormalServiceAdapter.setData(model.ParentAndNormalServiceList);

//            parentAndNormalServiceAdapter = new HomeParentAndNormalServiceAdapter(this, model.ParentAndNormalServiceList, gender, subCategoryModel);
//            serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            serviceRecyclerView.setHasFixedSize(true);
//            serviceRecyclerView.setAdapter(adapter);
        }

        updateCart();
    }

    @Override
    public void onLoginSuccessful() {
        if (EnrichUtils.getUserData(ServiceListActivity.this) != null) {
            if (EnrichUtils.getUserData(this).IsMember == Constants.IS_MEMBER) {
                if (isHomeSelected) {
                    maleContainer.setVisibility(View.GONE);
                    genderStr = "Female";
                    changeGenderIcons(true);
                    getServiceList(Constants.HOME_CATEGORY_ID, Constants.FEMALE);
                } else {
                    Map<String, String> categoryMap = new HashMap<>();
                    categoryMap.put("CenterId", centerDetailModel.Id);
                    categoryMap.put("parentCategoryId", Constants.PARENT_CATEGORY_ID);
                    if (isHomeSelected) {
                        categoryMap.put("HomeCategory", "home");
                    }
                    categoryPresenter.getCategoriesList(this, categoryMap, true);
                }
            }
        }
    }
}
