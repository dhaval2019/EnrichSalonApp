package com.enrich.salonapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.enrich.salonapp.data.model.PackageModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.AppointmentsActivity;
import com.enrich.salonapp.ui.activities.CategoryActivity;
import com.enrich.salonapp.ui.activities.OfferActivity;
import com.enrich.salonapp.ui.adapters.AppointmentHomeAdapter;
import com.enrich.salonapp.ui.adapters.CategoriesHomeAdapter;
import com.enrich.salonapp.ui.adapters.OfferHomeAdapter;
import com.enrich.salonapp.ui.adapters.RecommendedServicesAdapter;
import com.enrich.salonapp.ui.contracts.HomePageContract;
import com.enrich.salonapp.ui.presenters.HomePagePresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseFragment;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class HomeFragment extends BaseFragment implements HomePageContract.View {

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

    ArrayList<PackageModel> packageList;
    ArrayList<OfferModel> offerList;
    ArrayList<CategoryModel> categoryList;

    HomePagePresenter homePagePresenter;

    DataRepository dataRepository;

    EnrichApplication application;

    public static HomeFragment getInstance(DrawerLayout drawer) {
        HomeFragment fragment = new HomeFragment();
        mDrawer = drawer;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (EnrichApplication) getActivity().getApplicationContext();
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

        packagesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (packageList != null) {
//                    Intent intent = new Intent(HomeFragment.this.getActivity(), PackageActivity.class);
//                    intent.putExtra("PackageList", packageList);
//                    startActivity(intent);
//                }
            }
        });

        appointmentsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), AppointmentsActivity.class);
                startActivity(intent);
            }
        });

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this.getActivity(), mainUiThread, threadExecutor, null);

        homePagePresenter = new HomePagePresenter(this, dataRepository);

        // GET OFFERS
        homePagePresenter.getOffersList(this.getActivity());

        // GET CATEGORIES
        getCategories();

        // GET APPOINTMENTS
        homePagePresenter.getAppointment(this.getActivity(), RemoteDataSource.HOST + RemoteDataSource.GET_UPCOMING_APPOINTMENT);

        //GET NEW AND POPULAR
        getNewAndPopularServices();

        return rootView;
    }

    private void getCategories() {
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("CenterId", EnrichUtils.getHomeStore(this.getActivity()).Id);
        categoryMap.put("parentCategoryId", Constants.PARENT_CATEGORY_ID);
        homePagePresenter.getCategoriesList(this.getActivity(), categoryMap);
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
        offerList = model.Offers;
        loadingScreen.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        OfferHomeAdapter adapter = new OfferHomeAdapter(HomeFragment.this.getActivity(), model.Offers);
        offerRecyclerView.setAdapter(adapter);
        offerRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void showCategoryList(CategoryResponseModel model) {
        categoryList = model.Categories;
        CategoriesHomeAdapter categoriesHomeAdapter = new CategoriesHomeAdapter(HomeFragment.this.getActivity(), model.Categories);
        categoriesRecyclerView.setAdapter(categoriesHomeAdapter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void showAppointments(AppointmentResponseModel model) {
        if (model.Appointments.size() != 0) {
            appointmentsRecyclerView.setVisibility(View.VISIBLE);
            noAppointmentContainer.setVisibility(View.GONE);
            AppointmentHomeAdapter appointmentHomeAdapter = new AppointmentHomeAdapter(HomeFragment.this.getActivity(), model.Appointments);
            appointmentsRecyclerView.setAdapter(appointmentHomeAdapter);
            appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            appointmentsRecyclerView.setVisibility(View.GONE);
            noAppointmentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNewAndPopularServices(NewAndPopularResponseModel model) {
        RecommendedServicesAdapter recommendedServicesAdapter = new RecommendedServicesAdapter(this.getActivity(), model.Services);
        recommendedServicesRecyclerView.setAdapter(recommendedServicesAdapter);
        recommendedServicesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}
