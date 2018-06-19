package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCashBackModel implements Parcelable{

    public int ProductCashBackId;
    public ProductCashback CashBack;

    protected ProductCashBackModel(Parcel in) {
        ProductCashBackId = in.readInt();
        CashBack = in.readParcelable(ProductCashback.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ProductCashBackId);
        dest.writeParcelable(CashBack, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductCashBackModel> CREATOR = new Creator<ProductCashBackModel>() {
        @Override
        public ProductCashBackModel createFromParcel(Parcel in) {
            return new ProductCashBackModel(in);
        }

        @Override
        public ProductCashBackModel[] newArray(int size) {
            return new ProductCashBackModel[size];
        }
    };
}
