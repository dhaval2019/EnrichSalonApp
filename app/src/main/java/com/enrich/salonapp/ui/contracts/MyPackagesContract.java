package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Package.MyPackageResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface MyPackagesContract {

    interface View extends IBaseView {
        void showMyPackages(MyPackageResponseModel model);

        void noPackagesBought();
    }

    interface Presenter extends IBasePresenter<MyPackagesContract.View> {
        void getMyPackages(Context context, Map<String, String> map);
    }
}
