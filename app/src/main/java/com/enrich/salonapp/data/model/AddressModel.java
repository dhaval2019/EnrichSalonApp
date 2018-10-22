package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Parcelable {

    public int GuestAddressId;
    public String Location;
    public String HouseNameFlatNo;
    public String Landmark;
    public double Latitude;
    public double Longitude;
    public String AddressType;
    public String GuestId;

    public AddressModel() {
    }

    protected AddressModel(Parcel in) {
        GuestAddressId = in.readInt();
        Location = in.readString();
        HouseNameFlatNo = in.readString();
        Landmark = in.readString();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        AddressType = in.readString();
        GuestId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(GuestAddressId);
        dest.writeString(Location);
        dest.writeString(HouseNameFlatNo);
        dest.writeString(Landmark);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeString(AddressType);
        dest.writeString(GuestId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
}
