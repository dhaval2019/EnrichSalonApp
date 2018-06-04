package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordResponseModel;
import com.enrich.salonapp.ui.contracts.SettingsContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements SettingsContract.Presenter {

    private DataRepository dataRepository;

    public SettingsPresenter(SettingsContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void changePassword(Context context, ChangePasswordRequestModel model) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.changePassword(context, model, new DataSource.ChangePasswordCallBack() {
            @Override
            public void onSuccess(ChangePasswordResponseModel model) {
                if (view != null) {
                    view.passwordChanged(model);
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
