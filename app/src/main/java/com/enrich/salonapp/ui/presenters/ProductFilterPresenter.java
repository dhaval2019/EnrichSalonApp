package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.ui.contracts.ProductFilterContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class ProductFilterPresenter extends BasePresenter<ProductFilterContract.View> implements ProductFilterContract.Presenter {

    private DataRepository dataRepository;

    public ProductFilterPresenter(ProductFilterContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getBrandsList(Context context) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getBrands(context, new DataSource.GetBrandsListCallback() {
            @Override
            public void onSuccess(BrandResponseModel model) {
                if (view != null) {
                    view.showBrands(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(true);
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

    @Override
    public void getProductCategoriesList(Context context) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getProductsCategoryList(context, new DataSource.GetProductsCategoryListCallback() {
            @Override
            public void onSuccess(ProductCategoryResponseModel model) {
                if (view != null) {
                    view.showProductCategories(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(true);
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

    @Override
    public void getProductSubCategoriesList(Context context) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.getProductsSubCategoryList(context, new DataSource.GetProductsSubCategoryListCallback() {
            @Override
            public void onSuccess(ProductSubCategoryResponseModel model) {
                if (view != null) {
                    view.showProductSubCategories(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(true);
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
