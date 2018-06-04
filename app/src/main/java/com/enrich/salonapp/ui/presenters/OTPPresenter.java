package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.ui.contracts.OTPContract;
import com.enrich.salonapp.ui.contracts.RegisterContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class OTPPresenter extends BasePresenter<OTPContract.RegisterView> implements OTPContract.RegisterPresenter {

    private DataRepository dataRepository;

    public OTPPresenter(OTPContract.RegisterView view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void registerUser(Context context, RegistrationRequestModel model) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.registerUser(context, model, new DataSource.RegisterUserCallBack() {
            @Override
            public void onSuccess(RegistrationResponseModel model) {
                if (view != null) {
                    view.userRegistered(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
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
