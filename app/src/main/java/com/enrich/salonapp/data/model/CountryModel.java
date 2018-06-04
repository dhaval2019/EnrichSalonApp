package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryModel implements Parcelable {

    public int Id;
    public String Code;
    public String Name;
    public int PhoneCode;
    public String Nationality;

    protected CountryModel(Parcel in) {
        Id = in.readInt();
        Code = in.readString();
        Name = in.readString();
        PhoneCode = in.readInt();
        Nationality = in.readString();
    }

    public static final Creator<CountryModel> CREATOR = new Creator<CountryModel>() {
        @Override
        public CountryModel createFromParcel(Parcel in) {
            return new CountryModel(in);
        }

        @Override
        public CountryModel[] newArray(int size) {
            return new CountryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Code);
        dest.writeString(Name);
        dest.writeInt(PhoneCode);
        dest.writeString(Nationality);
    }
}

