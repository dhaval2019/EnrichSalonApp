package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Wallet.WalletHistoryModel;
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.data.model.Wallet.WalletResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface WalletContract {

    interface View extends IBaseView {
        void showWallet(WalletResponseModel model);
    }

    interface Presenter extends IBasePresenter<WalletContract.View> {
        void getWallet(Context context, Map<String, String> map);
    }
}
