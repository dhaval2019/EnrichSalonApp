package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface AppointmentContracts {

    interface View extends IBaseView {
        void showAppointments(AppointmentResponseModel model);
    }

    interface Presenter extends IBasePresenter<AppointmentContracts.View> {
        void getAppointments(Context context, String url, Map<String, String> map);
    }
}
