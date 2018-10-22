package com.enrich.salonapp.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.CategoryModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Package.PackageModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.AppointmentsActivity;
import com.enrich.salonapp.ui.activities.CategoryActivity;
import com.enrich.salonapp.ui.activities.OfferActivity;
import com.enrich.salonapp.ui.activities.PackagesActivity;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.enrich.salonapp.ui.activities.ProductHomePageActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.adapters.AppointmentHomeAdapter;
import com.enrich.salonapp.ui.adapters.CategoriesHomeAdapter;
import com.enrich.salonapp.ui.adapters.OfferHomeAdapter;
import com.enrich.salonapp.ui.adapters.PackagesHomeAdapter;
import com.enrich.salonapp.ui.adapters.ProductCategoryHomeAdapter;
import com.enrich.salonapp.ui.adapters.ProductHomeAdapter;
import com.enrich.salonapp.ui.adapters.ProductSubCategoryHomeAdapter;
import com.enrich.salonapp.ui.adapters.RecommendedServicesAdapter;
import com.enrich.salonapp.ui.contracts.CategoryContract;
import com.enrich.salonapp.ui.contracts.HomePageContract;
import com.enrich.salonapp.ui.contracts.PackageContract;
import com.enrich.salonapp.ui.contracts.ProductContract;
import com.enrich.salonapp.ui.contracts.ProductFilterContract;
import com.enrich.salonapp.ui.presenters.CategoryPresenter;
import com.enrich.salonapp.ui.presenters.HomePagePresenter;
import com.enrich.salonapp.ui.presenters.PackagePresenter;
import com.enrich.salonapp.ui.presenters.ProductFilterPresenter;
import com.enrich.salonapp.ui.presenters.ProductPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.OfferComparator;
import com.enrich.salonapp.util.mvp.BaseFragment;
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
import io.supercharge.shimmerlayout.ShimmerLayout;

public class HomeFragment extends BaseFragment implements HomePageContract.View, ProductContract.View, PackageContract.View, CategoryContract.View, ProductFilterContract.View {

    static DrawerLayout mDrawer;

    @BindView(R.id.offer_recycler_view)
    RecyclerView offerRecyclerView;

    @BindView(R.id.looks_recycler_view)
    RecyclerView looksRecyclerView;

    @BindView(R.id.products_recycler_view)
    RecyclerView productsRecyclerView;

    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    @BindView(R.id.appointments_recycler_view)
    RecyclerView appointmentsRecyclerView;

    @BindView(R.id.recommended_services_recycler_view)
    RecyclerView recommendedServicesRecyclerView;

    @BindView(R.id.packages_recycler_view)
    RecyclerView packagesRecyclerView;

    @BindView(R.id.offer_more)
    ImageView offerMore;

    @BindView(R.id.looks_more)
    ImageView looksMore;

    @BindView(R.id.products_more)
    ImageView productsMore;

    @BindView(R.id.categories_more)
    ImageView categoriesMore;

    @BindView(R.id.appointments_more)
    ImageView appointmentsMore;

    @BindView(R.id.packages_more)
    ImageView packagesMore;

    @BindView(R.id.loading_screen)
    ShimmerLayout loadingScreen;

    @BindView(R.id.main_content)
    LinearLayout mainContent;

    @BindView(R.id.no_appointment_container)
    LinearLayout noAppointmentContainer;

    @BindView(R.id.package_container)
    LinearLayout packageContainer;

    @BindView(R.id.appointments_label)
    TextView appointmentsLabel;

    @BindView(R.id.new_popular_service_container)
    FrameLayout newPopularServiceContainer;

    @BindView(R.id.appointment_container)
    LinearLayout appointmentContainer;

    @BindView(R.id.product_container)
    LinearLayout productContainer;

    @BindView(R.id.no_appointment_book_now)
    TextView noAppointmentBookNow;

    @BindView(R.id.salon_label)
    TextView salonLabel;

    @BindView(R.id.salon_indicator)
    View salonIndicator;

    @BindView(R.id.salon_container)
    RelativeLayout salonContainer;

    @BindView(R.id.home_label)
    TextView homeLabel;

    @BindView(R.id.home_indicator)
    View homeIndicator;

    @BindView(R.id.home_container)
    RelativeLayout homeContainer;

    @BindView(R.id.salon_components_container)
    LinearLayout salonComponentsContainer;

    @BindView(R.id.home_components_container)
    LinearLayout homeComponentsContainer;

    @BindView(R.id.home_offer_more)
    ImageView homeOfferMore;

    @BindView(R.id.home_offer_recycler_view)
    RecyclerView homeOfferRecyclerView;

    @BindView(R.id.home_categories_more)
    ImageView homeCategoriesMore;

    @BindView(R.id.home_categories_recycler_view)
    RecyclerView homeCategoriesRecyclerView;

    ArrayList<OfferModel> offerList;
    ArrayList<CategoryModel> categoryList;
    ArrayList<PackageModel> packageList;

    HomePagePresenter homePagePresenter;
    ProductPresenter productPresenter;
    ProductFilterPresenter productFilterPresenter;
    PackagePresenter packagePresenter;
    CategoryPresenter categoryPresenter;

    DataRepository dataRepository;

    EnrichApplication application;
    Tracker mTracker;

    private String isSalonStr = "0";

    public static HomeFragment getInstance(DrawerLayout drawer) {
        HomeFragment fragment = new HomeFragment();
        mDrawer = drawer;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SEND ANALYTICS
        application = (EnrichApplication) getActivity().getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Home Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

        offerMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), OfferActivity.class);
                if (!offerList.isEmpty())
                    intent.putExtra("OfferList", offerList);
                startActivity(intent);
            }
        });

        homeOfferMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), OfferActivity.class);
                if (!offerList.isEmpty())
                    intent.putExtra("OfferList", offerList);
                startActivity(intent);
            }
        });

        appointmentsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), AppointmentsActivity.class);
                startActivity(intent);
            }
        });

        categoriesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), CategoryActivity.class);
                intent.putExtra("CategoryList", categoryList);
                startActivity(intent);
            }
        });

        homeCategoriesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), CategoryActivity.class);
                intent.putExtra("CategoryList", categoryList);
                startActivity(intent);
            }
        });

        packagesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packageList != null) {
                    Intent intent = new Intent(HomeFragment.this.getActivity(), PackagesActivity.class);
                    intent.putExtra("PackageList", packageList);
                    startActivity(intent);
                }
            }
        });

        appointmentsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), AppointmentsActivity.class);
                startActivity(intent);
            }
        });

        productsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), ProductHomePageActivity.class);
                startActivity(intent);
            }
        });

        noAppointmentBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), ServiceListActivity.class);
                startActivity(intent);
            }
        });

        salonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSalonType(true);
            }
        });

        homeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSalonType(false);
            }
        });

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this.getActivity(), mainUiThread, threadExecutor, null);

        homePagePresenter = new HomePagePresenter(this, dataRepository);
        productPresenter = new ProductPresenter(this, dataRepository);
        productFilterPresenter = new ProductFilterPresenter(this, dataRepository);
        packagePresenter = new PackagePresenter(this, dataRepository);
        categoryPresenter = new CategoryPresenter(this, dataRepository);

        // GET OFFERS
        changeSalonType(true);

        // GET CATEGORIES
        getCategories();

        // GET APPOINTMENTS
        homePagePresenter.getAppointment(this.getActivity(), RemoteDataSource.HOST + RemoteDataSource.GET_UPCOMING_APPOINTMENT);

        // GET NEW AND POPULAR
        getNewAndPopularServices();

        // GET PACKAGES
        packagePresenter.getAllPackages(this.getActivity());

        // GET PRODUCTS
        productFilterPresenter.getProductCategoriesList(this.getActivity());
//        productFilterPresenter.getProductSubCategoriesList(this.getActivity());
//        productPresenter.getProducts(this.getActivity(), new ProductRequestModel());

        return rootView;
    }

    private void changeSalonType(boolean isSalon) {
        if (isSalon) {
            isSalonStr = "0";

            Map<String, String> map = new HashMap<>();
            map.put("IsHomeOffer", isSalonStr);
            homePagePresenter.getOffersList(this.getActivity(), map);

            salonComponentsContainer.setVisibility(View.VISIBLE);
            salonLabel.setTextColor(Color.parseColor("#d69e5c"));
            salonIndicator.setBackgroundColor(Color.parseColor("#d69e5c"));

            homeComponentsContainer.setVisibility(View.GONE);
            homeLabel.setTextColor(Color.parseColor("#9E9E9E"));
            homeIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            isSalonStr = "1";

            Map<String, String> map = new HashMap<>();
            map.put("IsHomeOffer", isSalonStr);
            homePagePresenter.getOffersList(this.getActivity(), map);

            homeComponentsContainer.setVisibility(View.VISIBLE);
            homeLabel.setTextColor(Color.parseColor("#d69e5c"));
            homeIndicator.setBackgroundColor(Color.parseColor("#d69e5c"));

            salonComponentsContainer.setVisibility(View.GONE);
            salonLabel.setTextColor(Color.parseColor("#9E9E9E"));
            salonIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void getCategories() {
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("CenterId", EnrichUtils.getHomeStore(this.getActivity()).Id);
        categoryMap.put("parentCategoryId", Constants.PARENT_CATEGORY_ID);
        categoryPresenter.getCategoriesList(this.getActivity(), categoryMap, false);
    }

    private void getNewAndPopularServices() {
        Map<String, String> map = new HashMap<>();
        map.put("CenterId", EnrichUtils.getHomeStore(this.getActivity()).Id);
        map.put("CategoryId", "");
        map.put("GuestId", EnrichUtils.getUserData(this.getActivity()).Id);
        map.put("size", "500");
        map.put("length", "500");
        map.put("start", "0");
        map.put("Tag", "popular, new");
        homePagePresenter.getNewAndPopularServices(this.getActivity(), map);
    }

    @Override
    public void onResume() {
        super.onResume();
        application.clearCart();
        if (categoriesRecyclerView != null) {
            getCategories();
            getNewAndPopularServices();
        }
    }

    @Override
    public void showOfferList(OfferResponseModel model) {
        if (!model.Offers.isEmpty()) {
            offerList = model.Offers;

            Collections.sort(offerList, new OfferComparator());

            OfferHomeAdapter adapter = new OfferHomeAdapter(HomeFragment.this.getActivity(), offerList);
            if (isSalonStr.equalsIgnoreCase("0")) {
                offerRecyclerView.setAdapter(adapter);
                offerRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            } else {
                homeOfferRecyclerView.setAdapter(adapter);
                homeOfferRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }

    @Override
    public void showCategoryList(CategoryResponseModel model) {
        categoryList = model.Categories;
        CategoriesHomeAdapter categoriesHomeAdapter = new CategoriesHomeAdapter(HomeFragment.this.getActivity(), model.Categories);
        categoriesRecyclerView.setAdapter(categoriesHomeAdapter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        homeCategoriesRecyclerView.setAdapter(categoriesHomeAdapter);
        homeCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadingScreen.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAppointments(AppointmentResponseModel model) {
        if (model.Appointments.size() != 0) {
            appointmentContainer.setVisibility(View.VISIBLE);
            appointmentsRecyclerView.setVisibility(View.VISIBLE);
            noAppointmentContainer.setVisibility(View.GONE);

            AppointmentHomeAdapter appointmentHomeAdapter = new AppointmentHomeAdapter(HomeFragment.this.getActivity(), model.Appointments);
            appointmentsRecyclerView.setAdapter(appointmentHomeAdapter);
            appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            appointmentContainer.setVisibility(View.VISIBLE);
            appointmentsRecyclerView.setVisibility(View.GONE);
            noAppointmentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNewAndPopularServices(NewAndPopularResponseModel model) {
        if (!model.Services.isEmpty()) {
            newPopularServiceContainer.setVisibility(View.VISIBLE);
            RecommendedServicesAdapter recommendedServicesAdapter = new RecommendedServicesAdapter(this.getActivity(), model.Services, this);
            recommendedServicesRecyclerView.setAdapter(recommendedServicesAdapter);
            recommendedServicesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            newPopularServiceContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void showPackage(PackageResponseModel model) {
        if (!model.Package.isEmpty()) {
            packageContainer.setVisibility(View.VISIBLE);
            packageList = model.Package;
            PackagesHomeAdapter adapter = new PackagesHomeAdapter(this.getActivity(), model.Package);
            packagesRecyclerView.setAdapter(adapter);
            packagesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            packageContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void noPackageAvailable() {
        packageContainer.setVisibility(View.GONE);
    }

    public ArrayList<CategoryModel> getCategoryList() {
        return categoryList;
    }

    @Override
    public void showProducts(ProductResponseModel model) {
        if (!model.Product.isEmpty()) {
            productContainer.setVisibility(View.VISIBLE);
            ProductHomeAdapter adapter = new ProductHomeAdapter(this.getActivity(), model.Product);
            productsRecyclerView.setAdapter(adapter);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            productContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBrands(BrandResponseModel model) {

    }

    @Override
    public void showProductCategories(ProductCategoryResponseModel model) {
        if (!model.ProductCategory.isEmpty()) {
            productContainer.setVisibility(View.VISIBLE);
            ProductCategoryHomeAdapter adapter = new ProductCategoryHomeAdapter(this.getActivity(), model.ProductCategory);
            productsRecyclerView.setAdapter(adapter);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            productContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProductSubCategories(ProductSubCategoryResponseModel model) {
        if (!model.ProductSubCategory.isEmpty()) {
            productContainer.setVisibility(View.VISIBLE);
            ProductSubCategoryHomeAdapter adapter = new ProductSubCategoryHomeAdapter(this.getActivity(), model.ProductSubCategory);
            productsRecyclerView.setAdapter(adapter);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            productContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProductOffers(OfferResponseModel model) {

    }
}
