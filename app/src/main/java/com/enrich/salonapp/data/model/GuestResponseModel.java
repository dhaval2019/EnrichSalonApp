package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GuestResponseModel implements Parcelable{

    @SerializedName("Guest")
    public GuestModel Guest;

    @SerializedName("Error")
    public ErrorModel Error;

    protected GuestResponseModel(Parcel in) {
        Guest = in.readParcelable(GuestModel.class.getClassLoader());
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Guest, flags);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestResponseModel> CREATOR = new Creator<GuestResponseModel>() {
        @Override
        public GuestResponseModel createFromParcel(Parcel in) {
            return new GuestResponseModel(in);
        }

        @Override
        public GuestResponseModel[] newArray(int size) {
            return new GuestResponseModel[size];
        }
    };
}
