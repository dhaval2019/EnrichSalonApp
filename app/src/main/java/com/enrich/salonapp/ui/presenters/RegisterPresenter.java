package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.CheckUserNameResponseModel;
import com.enrich.salonapp.data.model.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.ui.contracts.RegisterContract;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private DataRepository dataRepository;

    public RegisterPresenter(RegisterContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void checkUsername(Context context, String username) {
        dataRepository.checkUserName(context, username, new DataSource.CheckUserNameCallBack() {
            @Override
            public void onSuccess(CheckUserNameResponseModel model) {
                if (view != null) {
                    view.isValidUserName(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void createOtp(Context context, CreateOTPRequestModel model) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.createOTP(context, model, new DataSource.CreateOTPCallBack() {
            @Override
            public void onSuccess(CreateOTPResponseModel model) {
                if (view != null) {
                    view.otpSent(model);
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
