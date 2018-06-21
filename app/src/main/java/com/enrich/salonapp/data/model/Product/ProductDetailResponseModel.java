package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetailResponseModel implements Parcelable{

    public ProductModel Product;

    protected ProductDetailResponseModel(Parcel in) {
        Product = in.readParcelable(ProductModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Product, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDetailResponseModel> CREATOR = new Creator<ProductDetailResponseModel>() {
        @Override
        public ProductDetailResponseModel createFromParcel(Parcel in) {
            return new ProductDetailResponseModel(in);
        }

        @Override
        public ProductDetailResponseModel[] newArray(int size) {
            return new ProductDetailResponseModel[size];
        }
    };
}
