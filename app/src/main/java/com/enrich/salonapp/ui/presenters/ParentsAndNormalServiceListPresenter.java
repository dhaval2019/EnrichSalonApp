package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.ui.contracts.ParentsAndNormalServiceListContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class ParentsAndNormalServiceListPresenter extends BasePresenter<ParentsAndNormalServiceListContract.View> implements ParentsAndNormalServiceListContract.Presenter {

    private DataRepository dataRepository;

    public ParentsAndNormalServiceListPresenter(ParentsAndNormalServiceListContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getParentAndNormalServiceList(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getParentAndNormalServiceList(context, map, new DataSource.GetParentAndNormalServiceListCallback() {
            @Override
            public void onSuccess(ParentAndNormalServiceListResponseModel model) {
                if (view != null) {
                    view.showParentAndNormalServiceList(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if(view!=null){
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view!=null){
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }
}
