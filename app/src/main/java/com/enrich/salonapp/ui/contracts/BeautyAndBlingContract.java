package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.BeautyAndBlingResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface BeautyAndBlingContract {

    interface View extends IBaseView {
        void checkBeautyAndBling(BeautyAndBlingResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getBeautyAndBling(Context context);
    }
}
