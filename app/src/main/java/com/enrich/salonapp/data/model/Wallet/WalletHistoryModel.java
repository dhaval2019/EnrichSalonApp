package com.enrich.salonapp.data.model.Wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletHistoryModel implements Parcelable {

    public String GuestId;
    public String CashbackType;
    public double AppliedAmount;
    public String OrderDate;
    public String OrderTitle;
    public WalletCenterModel Center;

    protected WalletHistoryModel(Parcel in) {
        GuestId = in.readString();
        CashbackType = in.readString();
        AppliedAmount = in.readDouble();
        OrderDate = in.readString();
        OrderTitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GuestId);
        dest.writeString(CashbackType);
        dest.writeDouble(AppliedAmount);
        dest.writeString(OrderDate);
        dest.writeString(OrderTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletHistoryModel> CREATOR = new Creator<WalletHistoryModel>() {
        @Override
        public WalletHistoryModel createFromParcel(Parcel in) {
            return new WalletHistoryModel(in);
        }

        @Override
        public WalletHistoryModel[] newArray(int size) {
            return new WalletHistoryModel[size];
        }
    };
}
