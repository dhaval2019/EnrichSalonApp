package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.ui.contracts.RegisterFCMContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class RegisterFCMPresenter extends BasePresenter<RegisterFCMContract.View> implements RegisterFCMContract.Presenter {

    private DataRepository dataRepository;

    public RegisterFCMPresenter(RegisterFCMContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void registerFCM(Context context, RegisterFCMRequestModel model) {
        if (view == null) return;

        dataRepository.registerFCM(context, model, new DataSource.RegisterFCMCallback() {
            @Override
            public void onSuccess(RegisterFCMResponseModel model) {
                if (view != null) {
                    view.FCMRegistered(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {

                }
            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }
}
