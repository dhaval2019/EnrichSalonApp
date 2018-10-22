package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface GuestsContact {

    interface View extends IBaseView {
        void saveUserDetails(GuestModel model);

        void dataNotFound();
    }

    interface Presenter extends IBasePresenter<GuestsContact.View> {
        void getUserData(Context context, String guestId, boolean showProgress);
    }
}
