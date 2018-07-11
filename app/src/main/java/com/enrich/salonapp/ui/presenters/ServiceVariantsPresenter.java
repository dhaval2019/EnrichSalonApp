package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ServiceList.ServiceVariantResponseModel;
import com.enrich.salonapp.ui.contracts.ServiceListContract;
import com.enrich.salonapp.ui.contracts.ServiceVariantsContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class ServiceVariantsPresenter extends BasePresenter<ServiceVariantsContract.View> implements ServiceVariantsContract.Presenter {

    private DataRepository dataRepository;

    public ServiceVariantsPresenter(ServiceVariantsContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getServiceVariants(Context context, Map<String, String> map) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.getServiceVariants(context, map, new DataSource.GetServiceVariantsCallback() {
            @Override
            public void onSuccess(ServiceVariantResponseModel model) {
                if (view != null) {
                    view.showServiceVariants(model);
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
