package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface UpdateGuestContract {

    interface View extends IBaseView {
        void guestUpdated(GuestUpdateResponseModel model);

        void updateFailed();
    }

    interface Presenter extends IBasePresenter<UpdateGuestContract.View> {
        void updateGuest(Context context, GuestUpdateRequestModel model);
    }
}
