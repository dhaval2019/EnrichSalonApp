package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestUpdateModel implements Parcelable{

    public String Email;
    public String Gender;
    public String GuestFName;
    public String GuestLName;
    public String GuestPhone;
    public String id;

    public GuestUpdateModel(){}

    protected GuestUpdateModel(Parcel in) {
        Email = in.readString();
        Gender = in.readString();
        GuestFName = in.readString();
        GuestLName = in.readString();
        GuestPhone = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Email);
        dest.writeString(Gender);
        dest.writeString(GuestFName);
        dest.writeString(GuestLName);
        dest.writeString(GuestPhone);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestUpdateModel> CREATOR = new Creator<GuestUpdateModel>() {
        @Override
        public GuestUpdateModel createFromParcel(Parcel in) {
            return new GuestUpdateModel(in);
        }

        @Override
        public GuestUpdateModel[] newArray(int size) {
            return new GuestUpdateModel[size];
        }
    };
}
