package com.enrich.salonapp.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.ui.contracts.RegisterContract;
import com.enrich.salonapp.ui.contracts.ServiceListContract;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class ServiceListPresenter extends BasePresenter<ServiceListContract.View> implements ServiceListContract.Presenter {

    private DataRepository dataRepository;

    public ServiceListPresenter(ServiceListContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getServiceList(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getServiceList(context, map, new DataSource.GetServiceListCallBack() {
            @Override
            public void onSuccess(ServiceListResponseModel model) {
                if (view != null) {
                    view.showServiceList(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.showToastMessage("Something went wrong. Please try again later.");
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.showToastMessage("No Network. Please try again later.");
                    view.setProgressBar(false);
                }
            }
        });
    }

    @Override
    public void getSubCategories(final Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getSubCategoryList(context, map, new DataSource.GetServiceSubCategoryCallback() {
            @Override
            public void onSuccess(SubCategoryResponseModel model) {
                if (view != null) {
                    view.showSubCategories(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.noSubCategories();
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.showToastMessage("No Network. Please try again later.");
                    view.setProgressBar(false);
                }
            }
        });
    }
}
