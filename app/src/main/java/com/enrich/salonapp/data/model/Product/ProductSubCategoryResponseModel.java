package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProductSubCategoryResponseModel implements Parcelable{

    public ArrayList<ProductSubCategoryModel> ProductSubCategory;


    protected ProductSubCategoryResponseModel(Parcel in) {
        ProductSubCategory = in.createTypedArrayList(ProductSubCategoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ProductSubCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductSubCategoryResponseModel> CREATOR = new Creator<ProductSubCategoryResponseModel>() {
        @Override
        public ProductSubCategoryResponseModel createFromParcel(Parcel in) {
            return new ProductSubCategoryResponseModel(in);
        }

        @Override
        public ProductSubCategoryResponseModel[] newArray(int size) {
            return new ProductSubCategoryResponseModel[size];
        }
    };
}
