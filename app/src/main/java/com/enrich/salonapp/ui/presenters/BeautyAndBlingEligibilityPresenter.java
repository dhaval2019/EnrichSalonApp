package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.GuestSpinCountResponseModel;
import com.enrich.salonapp.ui.contracts.BeautyAndBlingEligibilityContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class BeautyAndBlingEligibilityPresenter extends BasePresenter<BeautyAndBlingEligibilityContract.View> implements BeautyAndBlingEligibilityContract.Presenter {

    private DataRepository dataRepository;

    public BeautyAndBlingEligibilityPresenter(BeautyAndBlingEligibilityContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getSpinCount(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getGuestSpinCount(context, map, new DataSource.GetGuestSpinCountCallback() {
            @Override
            public void onSuccess(GuestSpinCountResponseModel model) {
                if (view != null) {
                    view.showSpinCount(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.showToastMessage("Something went wrong while getting the count. Please try again");
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.showToastMessage("There seem's to be no internet connection.");
                    view.setProgressBar(false);
                }
            }
        });
    }
}
