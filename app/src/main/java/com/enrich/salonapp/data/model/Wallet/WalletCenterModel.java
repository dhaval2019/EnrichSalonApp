package com.enrich.salonapp.data.model.Wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletCenterModel implements Parcelable {

    public int CenterId;
    public String Id;
    public String Name;
    public double GeoLatitude;
    public double GeoLongitude;
    public String Address1;
    public String Address2;
    public String City;
    public String ZipCode;

    protected WalletCenterModel(Parcel in) {
        CenterId = in.readInt();
        Id = in.readString();
        Name = in.readString();
        GeoLatitude = in.readDouble();
        GeoLongitude = in.readDouble();
        Address1 = in.readString();
        Address2 = in.readString();
        City = in.readString();
        ZipCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CenterId);
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeDouble(GeoLatitude);
        dest.writeDouble(GeoLongitude);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(ZipCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletCenterModel> CREATOR = new Creator<WalletCenterModel>() {
        @Override
        public WalletCenterModel createFromParcel(Parcel in) {
            return new WalletCenterModel(in);
        }

        @Override
        public WalletCenterModel[] newArray(int size) {
            return new WalletCenterModel[size];
        }
    };
}
