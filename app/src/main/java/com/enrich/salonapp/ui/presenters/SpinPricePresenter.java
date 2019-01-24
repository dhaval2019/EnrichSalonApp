package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.CampaignRewardResponseModel;
import com.enrich.salonapp.data.model.SpinPriceModel;
import com.enrich.salonapp.ui.contracts.SpinPriceContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class SpinPricePresenter extends BasePresenter<SpinPriceContract.View> implements SpinPriceContract.Presenter {

    private DataRepository dataRepository;

    public SpinPricePresenter(SpinPriceContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getSpinPrice(Context context, Map<String, String> map) {
        if (view == null)
            return;

        dataRepository.getSpinPrice(context, map, new DataSource.GetSpinPriceCallback() {
            @Override
            public void onSuccess(SpinPriceModel model) {
                if (view != null) {
                    view.setSpinPrice(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {

                }
            }

            @Override
            public void onNetworkFailure() {
                if (view == null) {

                }
            }
        });
    }

    @Override
    public void getCampaignRewards(Context context, Map<String, String> map) {
        if (view == null)
            return;

        dataRepository.getCampaignRewards(context, map, new DataSource.GetCampaignRewards() {
            @Override
            public void onSuccess(CampaignRewardResponseModel model) {
                if (view != null) {
                    view.setCampaignRewards(model);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {

                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {

                }
            }
        });
    }
}
