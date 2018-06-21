package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface ProductDetailsContract {

    interface View extends IBaseView {
        void showProductDetails(ProductDetailResponseModel model);
    }

    interface Presenter extends IBasePresenter<ProductDetailsContract.View> {
        void getProducts(Context context, Map<String, String> map);
    }
}
