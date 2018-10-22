package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.ui.contracts.AddressContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class AddressPresenter extends BasePresenter<AddressContract.View> implements AddressContract.Presenter {

    private DataRepository dataRepository;

    public AddressPresenter(AddressContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void addAddress(Context context, AddressModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.addAddress(context, model, new DataSource.AddAddressCallback() {
            @Override
            public void onSuccess(AddressResponseModel model) {
                if (view != null) {
                    view.addressAdded(model);
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
