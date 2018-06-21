package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.ui.contracts.PackageDetailsContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class PackageDetailsPresenter extends BasePresenter<PackageDetailsContract.View> implements PackageDetailsContract.Presenter {

    private DataRepository dataRepository;

    public PackageDetailsPresenter(PackageDetailsContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getPackageDetails(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getPackageDetails(context, map, new DataSource.GetPackageDetailsCallback() {

            @Override
            public void onSuccess(PackageDetailsResponseModel model) {
                if (view != null) {
                    view.showPackageDetails(model);
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
