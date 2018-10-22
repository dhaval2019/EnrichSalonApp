package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface HomePageContract {

    interface View extends IBaseView {
        void showOfferList(OfferResponseModel model);

        void showAppointments(AppointmentResponseModel model);

        void showNewAndPopularServices(NewAndPopularResponseModel model);
    }

    interface Presenter extends IBasePresenter<HomePageContract.View> {
        void getOffersList(Context context, Map<String, String> map);

        void getAppointment(Context context, String url);

        void getNewAndPopularServices(Context context, Map<String, String> map);
    }
}
