package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AvailableTimeResponseModel;
import com.enrich.salonapp.ui.contracts.TimeSlotContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class TimeSlotPresenter extends BasePresenter<TimeSlotContract.View> implements TimeSlotContract.Presenter {

    private DataRepository dataRepository;

    public TimeSlotPresenter(TimeSlotContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getTimeSlots(Context context, String url, AppointmentRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getTimeSlot(context, url, model, new DataSource.GetTimeSlotsCallBack() {
            @Override
            public void onSuccess(AvailableTimeResponseModel model) {
                if (view != null) {
                    view.setTimeSlot(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.noTimeSlot();
                    view.setProgressBar(false);
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
