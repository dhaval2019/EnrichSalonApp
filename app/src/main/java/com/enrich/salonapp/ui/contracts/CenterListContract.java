package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.CenterResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface CenterListContract {

    interface View extends IBaseView {
        void showCenterList(CenterResponseModel model);

        void showPlaceHolder();
    }

    interface Presenter extends IBasePresenter<CenterListContract.View> {
        void getCenterList(Context context, Map<String, String> map);
    }
}
