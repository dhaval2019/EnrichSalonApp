package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.ui.contracts.WalletContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class WalletPresenter extends BasePresenter<WalletContract.View> implements WalletContract.Presenter {

    private DataRepository dataRepository;

    public WalletPresenter(WalletContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getWallet(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getWallet(context, map, new DataSource.GetWalletCallback() {
            @Override
            public void onSuccess(WalletModel model) {
                if (view != null) {
                    view.showWallet(model);
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

    @Override
    public void getWalletHistory(Context context, Map<String, String> map) {
        if(view==null)
            return;

        view.setProgressBar(true);

    }
}
