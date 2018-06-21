package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface CategoryContract {

    interface View extends IBaseView {
        void showCategoryList(CategoryResponseModel model);
    }

    interface Presenter extends IBasePresenter<CategoryContract.View> {
        void getCategoriesList(Context context, Map<String, String> map, boolean showProgressBar);
    }
}
