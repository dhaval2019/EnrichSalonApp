package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.CheckUserNameResponseModel;
import com.enrich.salonapp.data.model.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOTPResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface RegisterContract {

    interface View extends IBaseView {
        void otpSent(CreateOTPResponseModel model);

        void isValidUserName(CheckUserNameResponseModel model);
    }

    interface Presenter extends IBasePresenter<RegisterContract.View> {
        void createOtp(Context context, CreateOTPRequestModel model);

        void checkUsername(Context context, String username);
    }
}
