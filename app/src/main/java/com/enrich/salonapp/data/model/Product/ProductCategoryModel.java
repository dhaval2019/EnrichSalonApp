package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCategoryModel implements Parcelable{
    public int Id;
    public String Name;

    protected ProductCategoryModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductCategoryModel> CREATOR = new Creator<ProductCategoryModel>() {
        @Override
        public ProductCategoryModel createFromParcel(Parcel in) {
            return new ProductCategoryModel(in);
        }

        @Override
        public ProductCategoryModel[] newArray(int size) {
            return new ProductCategoryModel[size];
        }
    };
}
