package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBundleProduct extends BaseChildModel implements Parcelable {

    public int ProductId;
    public String ProductTitle;
    public int Gender;
    public double ProductAmount;
    public int ProductCategoryId;
    public String ProductCategoryName;

    protected PackageBundleProduct(Parcel in) {
        ProductId = in.readInt();
        ProductTitle = in.readString();
        Gender = in.readInt();
        ProductAmount = in.readDouble();
        ProductCategoryId = in.readInt();
        ProductCategoryName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ProductId);
        dest.writeString(ProductTitle);
        dest.writeInt(Gender);
        dest.writeDouble(ProductAmount);
        dest.writeInt(ProductCategoryId);
        dest.writeString(ProductCategoryName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleProduct> CREATOR = new Creator<PackageBundleProduct>() {
        @Override
        public PackageBundleProduct createFromParcel(Parcel in) {
            return new PackageBundleProduct(in);
        }

        @Override
        public PackageBundleProduct[] newArray(int size) {
            return new PackageBundleProduct[size];
        }
    };

    @Override
    public String getName() {
        return ProductTitle;
    }

    @Override
    public String getPrice() {
        return "" + ProductAmount;
    }
}
