package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForgotPasswordRequestModel implements Parcelable{
    public String UserName;

    public ForgotPasswordRequestModel() {
    }

    protected ForgotPasswordRequestModel(Parcel in) {
        UserName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ForgotPasswordRequestModel> CREATOR = new Parcelable.Creator<ForgotPasswordRequestModel>() {
        @Override
        public ForgotPasswordRequestModel createFromParcel(Parcel in) {
            return new ForgotPasswordRequestModel(in);
        }

        @Override
        public ForgotPasswordRequestModel[] newArray(int size) {
            return new ForgotPasswordRequestModel[size];
        }
    };
}
