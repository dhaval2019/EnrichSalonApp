package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Wallet.WalletHistoryResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface WalletHistoryContract {

    interface View extends IBaseView {
        void showWalletHistory(WalletHistoryResponseModel model);
    }

    interface Presenter extends IBasePresenter<WalletHistoryContract.View> {
        void getWalletHistory(Context context, Map<String, String> map);
    }
}
