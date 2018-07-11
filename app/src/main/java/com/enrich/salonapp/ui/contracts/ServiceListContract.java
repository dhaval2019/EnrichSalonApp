package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface ServiceListContract {

    interface View extends IBaseView {
        void showServiceList(ServiceListResponseModel model);

        void showSubCategories(SubCategoryResponseModel model);
    }

    interface Presenter extends IBasePresenter<ServiceListContract.View> {
        void getServiceList(Context context, Map<String, String> map);

        void getSubCategories(Context context, Map<String, String> map);
    }
}
