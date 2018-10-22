package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AppUpdateResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface AppUpdateContract {

    interface View extends IBaseView {
        void showAppUpdate(AppUpdateResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getAppUpdate(Context context, Map<String, String> map);
    }
}
