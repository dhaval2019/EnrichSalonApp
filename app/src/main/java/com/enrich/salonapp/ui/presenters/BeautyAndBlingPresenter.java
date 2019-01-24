package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.BeautyAndBlingResponseModel;
import com.enrich.salonapp.ui.contracts.BeautyAndBlingContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class BeautyAndBlingPresenter extends BasePresenter<BeautyAndBlingContract.View> implements BeautyAndBlingContract.Presenter {

    DataRepository dataRepository;

    public BeautyAndBlingPresenter(BeautyAndBlingContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getBeautyAndBling(Context context) {
        if (view == null)
            return;

        dataRepository.getBeautyAndBling(context, new DataSource.GetBeautyAndBlingCallback() {
            @Override
            public void onSuccess(BeautyAndBlingResponseModel model) {
                if (view != null) {
                    view.checkBeautyAndBling(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {

                }
            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }
}
