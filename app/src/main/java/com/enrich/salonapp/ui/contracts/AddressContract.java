package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface AddressContract {

    interface View extends IBaseView {
        void addressAdded(AddressResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void addAddress(Context context, AddressModel model);
    }
}
