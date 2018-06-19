package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface ProductContract {

    interface View extends IBaseView {
        void showProducts(ProductResponseModel model);
    }

    interface Presenter extends IBasePresenter<ProductContract.View> {
        void getProducts(Context context, ProductRequestModel model);
    }
}
