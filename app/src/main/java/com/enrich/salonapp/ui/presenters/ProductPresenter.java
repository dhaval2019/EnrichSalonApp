package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.ui.contracts.ProductContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class ProductPresenter extends BasePresenter<ProductContract.View> implements ProductContract.Presenter {

    private DataRepository dataRepository;

    public ProductPresenter(ProductContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getProducts(Context context, ProductRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getProducts(context, model, new DataSource.GetProductListCallback() {
            @Override
            public void onSuccess(ProductResponseModel model) {
                if (view != null) {
                    view.showProducts(model);
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
