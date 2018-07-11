package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface ForgotPasswordContract {

    interface View extends IBaseView {
        void passwordSent(ForgotPasswordResponseModel model);
    }

    interface Presenter extends IBasePresenter<ForgotPasswordContract.View> {
        void forgotPassword(Context context, ForgotPasswordRequestModel model);
    }
}
