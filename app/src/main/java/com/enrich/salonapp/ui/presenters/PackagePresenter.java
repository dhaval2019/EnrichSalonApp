package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.ui.contracts.PackageContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class PackagePresenter extends BasePresenter<PackageContract.View> implements PackageContract.Presenter {

    private DataRepository dataRepository;

    public PackagePresenter(PackageContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getAllPackages(Context context) {
        if (view == null)
            return;

        dataRepository.getAllPackages(context, new DataSource.PackageListCallBack() {
            @Override
            public void onSuccess(PackageResponseModel model) {
                if (view != null) {
                    view.showPackage(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.noPackageAvailable();
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
