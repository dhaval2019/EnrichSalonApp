package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class AuthenticationTokenPresenter extends BasePresenter<AuthenticationTokenContract.View> implements AuthenticationTokenContract.Presenter {

    private DataRepository dataRepository;

    public AuthenticationTokenPresenter(AuthenticationTokenContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getAuthenticationToken(Context context, AuthenticationRequestModel model, boolean showProgress) {
        if (view == null) {
            return;
        }

        if (showProgress)
            view.setProgressBar(true);

        dataRepository.getAuthentication(context, model, new DataSource.GetAuthenticationCallBack() {
            @Override
            public void onSuccess(AuthenticationModel model) {
                if (view != null) {
                    view.saveAuthenticationToken(model);
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
