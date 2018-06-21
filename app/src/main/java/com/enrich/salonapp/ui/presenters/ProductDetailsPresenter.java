package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.ui.contracts.ProductDetailsContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

import java.util.Map;

public class ProductDetailsPresenter extends BasePresenter<ProductDetailsContract.View> implements ProductDetailsContract.Presenter {

    private DataRepository dataRepository;

    public ProductDetailsPresenter(ProductDetailsContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getProducts(Context context, Map<String, String> map) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getProductDetails(context, map, new DataSource.GetProductDetailsCallback() {
            @Override
            public void onSuccess(ProductDetailResponseModel model) {
                if (view != null) {
                    view.showProductDetails(model);
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
