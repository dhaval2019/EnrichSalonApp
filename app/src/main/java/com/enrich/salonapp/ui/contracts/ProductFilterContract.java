package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface ProductFilterContract {

    interface View extends IBaseView {
        void showBrands(BrandResponseModel model);

        void showProductCategories(ProductCategoryResponseModel model);

        void showProductSubCategories(ProductSubCategoryResponseModel model);
    }

    interface Presenter extends IBasePresenter<ProductFilterContract.View> {
        void getBrandsList(Context context);

        void getProductCategoriesList(Context context);

        void getProductSubCategoriesList(Context context);
    }
}
