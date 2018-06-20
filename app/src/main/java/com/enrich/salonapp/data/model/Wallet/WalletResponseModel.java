package com.enrich.salonapp.data.model.Wallet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WalletResponseModel implements Parcelable{

    public ArrayList<WalletModel> Wallets;

    protected WalletResponseModel(Parcel in) {
        Wallets = in.createTypedArrayList(WalletModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Wallets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletResponseModel> CREATOR = new Creator<WalletResponseModel>() {
        @Override
        public WalletResponseModel createFromParcel(Parcel in) {
            return new WalletResponseModel(in);
        }

        @Override
        public WalletResponseModel[] newArray(int size) {
            return new WalletResponseModel[size];
        }
    };
}
