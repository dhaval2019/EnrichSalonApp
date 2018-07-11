package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface ParentsAndNormalServiceListContract {

    interface View extends IBaseView {
        void showParentAndNormalServiceList(ParentAndNormalServiceListResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getParentAndNormalServiceList(Context context, Map<String, String> map);
    }
}
