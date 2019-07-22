package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.FriendResponseModel;
import com.enrich.salonapp.data.model.ReferFriendModel;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.List;

public interface FriendContract {
    interface View extends IBaseView {
        void friendReferred(FriendResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void referFriend(Context context, ReferFriendModel modelList);
    }
}




