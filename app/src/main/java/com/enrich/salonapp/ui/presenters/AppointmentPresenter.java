package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.ui.contracts.AppointmentContracts;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class AppointmentPresenter extends BasePresenter<AppointmentContracts.View> implements AppointmentContracts.Presenter {

    private DataRepository dataRepository;

    public AppointmentPresenter(AppointmentContracts.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getAppointments(Context context, String url, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getAppointment(context, url, map, new DataSource.GetAppointmentsCallBack() {
            @Override
            public void onSuccess(AppointmentResponseModel model) {
                if (view != null) {
                    view.showAppointments(model);
                    view.setProgressBar(false);
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
