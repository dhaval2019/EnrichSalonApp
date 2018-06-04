package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface ServiceListContract {

    interface View extends IBaseView {
        void showServiceList(ServiceListResponseModel model);
    }

    interface Presenter extends IBasePresenter<ServiceListContract.View> {
        void getServiceList(Context context, Map<String, String> map);
    }
}
