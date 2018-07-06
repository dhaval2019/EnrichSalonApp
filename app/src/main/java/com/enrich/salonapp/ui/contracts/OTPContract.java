package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface OTPContract {

    interface RegisterView extends IBaseView {
        void userRegistered(RegistrationResponseModel model);

        void registerFailed();
    }

    interface RegisterPresenter extends IBasePresenter<OTPContract.RegisterView> {
        void registerUser(Context context, RegistrationRequestModel model);
    }
}
