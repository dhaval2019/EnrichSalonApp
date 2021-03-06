package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.ui.contracts.CategoryContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class CategoryPresenter extends BasePresenter<CategoryContract.View> implements CategoryContract.Presenter {

    private DataRepository dataRepository;

    public CategoryPresenter(CategoryContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getCategoriesList(Context context, Map<String, String> map, final boolean showProgressBar) {
        if (view == null)
            return;

        if (showProgressBar) {
            view.setProgressBar(true);
        }

        dataRepository.getCategories(context, map, new DataSource.GetCategoryListCallBack() {
            @Override
            public void onSuccess(CategoryResponseModel model) {
                if (view != null) {
                    view.showCategoryList(model);
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
