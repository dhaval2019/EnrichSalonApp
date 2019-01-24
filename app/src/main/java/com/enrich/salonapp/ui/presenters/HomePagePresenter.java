package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.ui.contracts.HomePageContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class HomePagePresenter extends BasePresenter<HomePageContract.View> implements HomePageContract.Presenter {

    private DataRepository dataRepository;

    public HomePagePresenter(HomePageContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getOffersList(Context context, Map<String, String> map) {
        if (view == null)
            return;

        dataRepository.getOffersList(context, map, new DataSource.GetOfferListCallBack() {
            @Override
            public void onSuccess(OfferResponseModel model) {
                if (view != null) {
                    view.showOfferList(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void getAppointment(Context context, String url, Map<String, String> map) {
        if (view == null)
            return;

        dataRepository.getAppointment(context, url, map, new DataSource.GetAppointmentsCallBack() {
            @Override
            public void onSuccess(AppointmentResponseModel model) {
                if (view != null) {
                    view.showAppointments(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void getNewAndPopularServices(Context context, Map<String, String> map) {
        if (view == null)
            return;

        dataRepository.getNewAndPopularServices(context, map, new DataSource.NewAndPopularServicesCallBack() {
            @Override
            public void onSuccess(NewAndPopularResponseModel model) {
                if (view != null) {
                    view.showNewAndPopularServices(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }
}
