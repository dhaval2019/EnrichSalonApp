package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.ui.contracts.GuestsContact;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class GuestPresenter extends BasePresenter<GuestsContact.View> implements GuestsContact.Presenter {

    private DataRepository dataRepository;

    public GuestPresenter(GuestsContact.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getUserData(Context context, String guestId, boolean showProgress) {
        if (view == null)
            return;

        view.setProgressBar(showProgress);

        dataRepository.getUserData(context, guestId, new DataSource.GetGuestDataCallBack() {
            @Override
            public void onSuccess(GuestModel model) {
                if (view != null) {
                    view.saveUserDetails(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.dataNotFound();
                    view.showToastMessage("User doesn't exist. Please Sign Up.");
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
