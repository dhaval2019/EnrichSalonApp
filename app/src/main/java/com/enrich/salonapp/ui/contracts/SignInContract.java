package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.UserExistsResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface SignInContract {

    interface View extends IBaseView {
        void showPasswordField(int responseCode);

        void saveUserDetails(GuestModel model);

        void passwordSent(ForgotPasswordResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void isUserRegistered(Context context, String phoneNumber);

        void getUserData(Context context, String guestId);

        void forgotPassword(Context context, ForgotPasswordRequestModel model);
    }
}
