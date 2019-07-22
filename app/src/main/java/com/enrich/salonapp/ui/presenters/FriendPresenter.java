package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.FriendResponseModel;
import com.enrich.salonapp.data.model.ReferFriendModel;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.ui.contracts.AddressContract;
import com.enrich.salonapp.ui.contracts.FriendContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.ArrayList;

public class FriendPresenter extends BasePresenter<FriendContract.View> implements FriendContract.Presenter {

    private DataRepository dataRepository;

    public FriendPresenter(FriendContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void referFriend(Context context, ReferFriendModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.referFriend(context, model, new DataSource.ReferFriendCallback() {
            @Override
            public void onSuccess(FriendResponseModel model) {
                if (view != null) {
                    view.friendReferred(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }
}
