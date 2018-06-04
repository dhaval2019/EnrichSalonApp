package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.ui.contracts.UpdateGuestContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class UpdateGuestPresenter extends BasePresenter<UpdateGuestContract.View> implements UpdateGuestContract.Presenter {

    private DataRepository dataRepository;

    public UpdateGuestPresenter(UpdateGuestContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void updateGuest(Context context, GuestUpdateRequestModel model) {
        if (view == null) {
            return;
        }

        view.setProgressBar(true);

        dataRepository.updateUser(context, model, new DataSource.UpdateUserCallBack() {
            @Override
            public void onSuccess(GuestUpdateResponseModel model) {
                if (view != null) {
                    view.guestUpdated(model);
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
