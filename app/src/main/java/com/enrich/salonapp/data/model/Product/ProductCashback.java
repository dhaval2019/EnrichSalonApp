package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCashback implements Parcelable{

    public int CashBackId;
    public String Title;
    public double Cost;
    public int CashBackMode;
    public String ValidTill;
    public int MembershipApplicable;

    protected ProductCashback(Parcel in) {
        CashBackId = in.readInt();
        Title = in.readString();
        Cost = in.readDouble();
        CashBackMode = in.readInt();
        ValidTill = in.readString();
        MembershipApplicable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CashBackId);
        dest.writeString(Title);
        dest.writeDouble(Cost);
        dest.writeInt(CashBackMode);
        dest.writeString(ValidTill);
        dest.writeInt(MembershipApplicable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductCashback> CREATOR = new Creator<ProductCashback>() {
        @Override
        public ProductCashback createFromParcel(Parcel in) {
            return new ProductCashback(in);
        }

        @Override
        public ProductCashback[] newArray(int size) {
            return new ProductCashback[size];
        }
    };
}
