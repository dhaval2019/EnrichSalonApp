package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AvailableTimeResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface TimeSlotContract {

    interface View extends IBaseView {
        void setTimeSlot(AvailableTimeResponseModel model);

        void noTimeSlot();
    }

    interface Presenter extends IBasePresenter<TimeSlotContract.View> {
        void getTimeSlots(Context context, String url, AppointmentRequestModel model);
    }
}
