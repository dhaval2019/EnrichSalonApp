package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.UserExistsResponseModel;
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
            public void onSuccess(int responseCode) {
                if (view != null) {
                    view.showPasswordField(responseCode);
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

    @Override
    public void getUserData(Context context, String guestId) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.getUserData(context, guestId, new DataSource.GetGuestDataCallBack() {
            @Override
            public void onSuccess(GuestModel model) {
                if (view != null) {
                    view.saveUserDetails(model);
//                    view.setProgressBar(false);
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
