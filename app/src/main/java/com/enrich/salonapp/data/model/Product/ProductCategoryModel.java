package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCategoryModel implements Parcelable {
    public int Id;
    public String Name;
    public int Order;
    public String ProductCategoryImage;
    public String ProductCategoryImageUrl;
    public ProductCategoryModel ProductCategory;

    protected ProductCategoryModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Order = in.readInt();
        ProductCategoryImage = in.readString();
        ProductCategoryImageUrl = in.readString();
        ProductCategory = in.readParcelable(ProductCategoryModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeInt(Order);
        dest.writeString(ProductCategoryImage);
        dest.writeString(ProductCategoryImageUrl);
        dest.writeParcelable(ProductCategory, flags);
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
