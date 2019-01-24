package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.GuestSpinCountResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface BeautyAndBlingEligibilityContract {

    interface View extends IBaseView {
        void showSpinCount(GuestSpinCountResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getSpinCount(Context context, Map<String, String> map);
    }
}
