package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface PackageContract {

    interface View extends IBaseView {
        void showPackage(PackageResponseModel model);
    }

    interface Presenter extends IBasePresenter<PackageContract.View> {
        void getAllPackages(Context context);
    }
}
