package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface AuthenticationTokenContract {

    interface View extends IBaseView {
        void saveAuthenticationToken(AuthenticationModel model);
    }

    interface Presenter extends IBasePresenter<AuthenticationTokenContract.View> {
        void getAuthenticationToken(Context context, AuthenticationRequestModel model, boolean showProgress);
    }
}
