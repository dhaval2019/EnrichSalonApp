package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface RegisterFCMContract {

    interface View extends IBaseView {
        void FCMRegistered(RegisterFCMResponseModel model);
    }

    interface Presenter extends IBasePresenter<RegisterFCMContract.View> {
        void registerFCM(Context context, RegisterFCMRequestModel model);
    }
}
