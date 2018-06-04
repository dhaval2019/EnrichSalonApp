package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface SettingsContract {

    interface View extends IBaseView {
        void passwordChanged(ChangePasswordResponseModel model);
    }

    interface Presenter extends IBasePresenter<SettingsContract.View> {
        void changePassword(Context context, ChangePasswordRequestModel model);
    }
}
