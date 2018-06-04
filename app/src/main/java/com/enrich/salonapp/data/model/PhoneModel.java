package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneModel implements Parcelable {

    public int CountryId;
    public String Number;
    public String DisplayNumber;

    public PhoneModel() {
    }

    protected PhoneModel(Parcel in) {
        CountryId = in.readInt();
        Number = in.readString();
        DisplayNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CountryId);
        dest.writeString(Number);
        dest.writeString(DisplayNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneModel> CREATOR = new Creator<PhoneModel>() {
        @Override
        public PhoneModel createFromParcel(Parcel in) {
            return new PhoneModel(in);
        }

        @Override
        public PhoneModel[] newArray(int size) {
            return new PhoneModel[size];
        }
    };
}

