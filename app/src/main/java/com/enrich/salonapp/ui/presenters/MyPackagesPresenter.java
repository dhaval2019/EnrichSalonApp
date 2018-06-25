package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Package.MyPackageResponseModel;
import com.enrich.salonapp.ui.contracts.MyPackagesContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class MyPackagesPresenter extends BasePresenter<MyPackagesContract.View> implements MyPackagesContract.Presenter {

    private DataRepository dataRepository;

    public MyPackagesPresenter(MyPackagesContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getMyPackages(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getMyPackages(context, map, new DataSource.GetMyPackagesCallback() {
            @Override
            public void onSuccess(MyPackageResponseModel model) {
                if (view != null) {
                    view.showMyPackages(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.noPackagesBought();
                    view.setProgressBar(false);
//                    view.showToastMessage("Something went wrong. Please try again later.");
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
