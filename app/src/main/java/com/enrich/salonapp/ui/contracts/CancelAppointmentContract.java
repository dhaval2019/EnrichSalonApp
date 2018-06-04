package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.CancelRequestModel;
import com.enrich.salonapp.data.model.CancelResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface CancelAppointmentContract {

    interface View extends IBaseView {
        void appointmentCancelled(CancelResponseModel model);
    }

    interface Presenter extends IBasePresenter<CancelAppointmentContract.View> {
        void cancelAppointment(Context context, String url, CancelRequestModel model);
    }
}
