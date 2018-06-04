package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class TherapistPresenter extends BasePresenter<TherapistContract.View> implements TherapistContract.Presenter {

    private DataRepository dataRepository;

    public TherapistPresenter(TherapistContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getTherapist(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getTherapist(context, map, new DataSource.GetTherapistCallBack() {
            @Override
            public void onSuccess(TherapistResponseModel model) {
                if (view != null) {
                    view.showTherapist(model);
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
}
