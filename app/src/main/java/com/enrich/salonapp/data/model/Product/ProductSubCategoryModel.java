package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductSubCategoryModel implements Parcelable{
    public int Id;
    public String Name;
    public ProductCategoryModel ProductCategory;

    protected ProductSubCategoryModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        ProductCategory = in.readParcelable(ProductCategoryModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeParcelable(ProductCategory, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductSubCategoryModel> CREATOR = new Creator<ProductSubCategoryModel>() {
        @Override
        public ProductSubCategoryModel createFromParcel(Parcel in) {
            return new ProductSubCategoryModel(in);
        }

        @Override
        public ProductSubCategoryModel[] newArray(int size) {
            return new ProductSubCategoryModel[size];
        }
    };
}
