package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface PackageDetailsContract {

    interface View extends IBaseView {
        void showPackageDetails(PackageDetailsResponseModel model);
    }

    interface Presenter extends IBasePresenter<PackageDetailsContract.View> {
        void getPackageDetails(Context context, Map<String, String> map);
    }
}
