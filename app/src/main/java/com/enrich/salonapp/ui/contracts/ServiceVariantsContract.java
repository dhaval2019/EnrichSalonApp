package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ServiceList.ServiceVariantResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface ServiceVariantsContract {

    interface View extends IBaseView {
        void showServiceVariants(ServiceVariantResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getServiceVariants(Context context, Map<String, String> map);
    }
}
