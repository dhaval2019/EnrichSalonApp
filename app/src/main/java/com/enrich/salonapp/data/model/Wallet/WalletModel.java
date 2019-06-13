package com.enrich.salonapp.data.model.Wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletModel implements Parcelable,Comparable<WalletModel>{

    public int Id;
    public double Amount;
    public String GuestId;
    public String WalletValidityDate;
    public String Walletfor;

    protected WalletModel(Parcel in) {
        Id = in.readInt();
        Amount = in.readDouble();
        GuestId = in.readString();
        WalletValidityDate = in.readString();
        Walletfor = in.readString();
    }
    @Override
    public int compareTo(WalletModel o) {
        return WalletValidityDate.compareTo(o.WalletValidityDate);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeDouble(Amount);
        dest.writeString(GuestId);
        dest.writeString(WalletValidityDate);
        dest.writeString(Walletfor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletModel> CREATOR = new Creator<WalletModel>() {
        @Override
        public WalletModel createFromParcel(Parcel in) {
            return new WalletModel(in);
        }

        @Override
        public WalletModel[] newArray(int size) {
            return new WalletModel[size];
        }
    };
}
