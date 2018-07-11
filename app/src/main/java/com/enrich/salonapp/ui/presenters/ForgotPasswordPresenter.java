package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.ui.contracts.ForgotPasswordContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordContract.View> implements ForgotPasswordContract.Presenter {

    private DataRepository dataRepository;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void forgotPassword(Context context, ForgotPasswordRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.forgotPassword(context, model, new DataSource.ForgotPasswordCallBack() {
            @Override
            public void onSuccess(ForgotPasswordResponseModel model) {
                if (view != null) {
                    view.passwordSent(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("User doesn't exist. Please Sign Up.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }
}
