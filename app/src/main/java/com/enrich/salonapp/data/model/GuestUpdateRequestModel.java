package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestUpdateRequestModel implements Parcelable {

    public String CenterId;
    public GuestModel Guest;

    public GuestUpdateRequestModel() {
    }

    protected GuestUpdateRequestModel(Parcel in) {
        CenterId = in.readString();
        Guest = in.readParcelable(GuestModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CenterId);
        dest.writeParcelable(Guest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestUpdateRequestModel> CREATOR = new Creator<GuestUpdateRequestModel>() {
        @Override
        public GuestUpdateRequestModel createFromParcel(Parcel in) {
            return new GuestUpdateRequestModel(in);
        }

        @Override
        public GuestUpdateRequestModel[] newArray(int size) {
            return new GuestUpdateRequestModel[size];
        }
    };
}
