package com.enrich.salonapp.data.model.Wallet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WalletHistoryResponseModel implements Parcelable {

    public ArrayList<WalletHistoryModel> WalletHistory;

    protected WalletHistoryResponseModel(Parcel in) {
        WalletHistory = in.createTypedArrayList(WalletHistoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(WalletHistory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletHistoryResponseModel> CREATOR = new Creator<WalletHistoryResponseModel>() {
        @Override
        public WalletHistoryResponseModel createFromParcel(Parcel in) {
            return new WalletHistoryResponseModel(in);
        }

        @Override
        public WalletHistoryResponseModel[] newArray(int size) {
            return new WalletHistoryResponseModel[size];
        }
    };
}
