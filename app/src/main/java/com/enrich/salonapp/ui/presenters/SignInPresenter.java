package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.SignIn.IsUserRegisteredResponseModel;
import com.enrich.salonapp.ui.contracts.SignInContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    private DataRepository dataRepository;

    public SignInPresenter(SignInContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void isUserRegistered(Context context, String phoneNumber) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.isUserRegistered(context, phoneNumber, new DataSource.GetIsUserRegisteredCallBack() {
            @Override
            public void onSuccess(IsUserRegisteredResponseModel model) {
                if (view != null) {
                    view.showPasswordField(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
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
