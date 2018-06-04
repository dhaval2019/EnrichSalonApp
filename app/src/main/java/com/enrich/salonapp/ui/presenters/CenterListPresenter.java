package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.CenterResponseModel;
import com.enrich.salonapp.ui.contracts.CenterListContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class CenterListPresenter extends BasePresenter<CenterListContract.View> implements CenterListContract.Presenter {

    private DataRepository dataRepository;

    public CenterListPresenter(CenterListContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getCenterList(Context context, Map<String, String> map) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.getCenterList(context, map, new DataSource.GetCenterListCallBack() {
            @Override
            public void onSuccess(CenterResponseModel model) {
                if (view != null) {
                    view.showCenterList(model);
                    view.setProgressBar(false);
                    view.showPlaceHolder();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                    view.showPlaceHolder();
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                    view.showPlaceHolder();
                }
            }
        });
    }
}
