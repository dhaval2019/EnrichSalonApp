package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryResponseModel;
import com.enrich.salonapp.ui.contracts.WalletHistoryContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class WalletHistoryPresenter extends BasePresenter<WalletHistoryContract.View> implements WalletHistoryContract.Presenter {

    private DataRepository dataRepository;

    public WalletHistoryPresenter(WalletHistoryContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getWalletHistory(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getWalletHistory(context, map, new DataSource.GetWalletHistoryCallback() {
            @Override
            public void onSuccess(WalletHistoryResponseModel model) {
                if (view != null) {
                    view.showWalletHistory(model);
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
