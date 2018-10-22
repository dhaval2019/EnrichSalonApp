package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppUpdateResponseModel;
import com.enrich.salonapp.ui.contracts.AppUpdateContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class AppUpdatePresenter extends BasePresenter<AppUpdateContract.View> implements AppUpdateContract.Presenter {

    private DataRepository dataRepository;

    public AppUpdatePresenter(AppUpdateContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getAppUpdate(Context context, Map<String, String> map) {
        if (view == null) return;

//        view.setProgressBar(false);

        dataRepository.getAppUpdate(context, map, new DataSource.GetAppUpdateCallback() {
            @Override
            public void onSuccess(AppUpdateResponseModel model) {
                if (view != null) {
                    view.showAppUpdate(model);
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
                }
            }
        });
    }
}
