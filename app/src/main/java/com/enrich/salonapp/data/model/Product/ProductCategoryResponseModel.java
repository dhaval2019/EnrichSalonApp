package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProductCategoryResponseModel implements Parcelable{

    public ArrayList<ProductCategoryModel> ProductCategory;

    public ProductCategoryResponseModel() {
    }

    protected ProductCategoryResponseModel(Parcel in) {
        ProductCategory = in.createTypedArrayList(ProductCategoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ProductCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductCategoryResponseModel> CREATOR = new Creator<ProductCategoryResponseModel>() {
        @Override
        public ProductCategoryResponseModel createFromParcel(Parcel in) {
            return new ProductCategoryResponseModel(in);
        }

        @Override
        public ProductCategoryResponseModel[] newArray(int size) {
            return new ProductCategoryResponseModel[size];
        }
    };
}
