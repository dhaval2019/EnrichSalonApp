package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.CancelRequestModel;
import com.enrich.salonapp.data.model.CancelResponseModel;
import com.enrich.salonapp.ui.contracts.CancelAppointmentContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class CancelAppointmentsPresenter extends BasePresenter<CancelAppointmentContract.View> implements CancelAppointmentContract.Presenter {

    private DataRepository dataRepository;

    public CancelAppointmentsPresenter(CancelAppointmentContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void cancelAppointment(Context context, String url, CancelRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.cancelAppointment(context, url, model, new DataSource.CancelAppointmentCallBack() {
            @Override
            public void onSuccess(CancelResponseModel model) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.appointmentCancelled(model);
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
