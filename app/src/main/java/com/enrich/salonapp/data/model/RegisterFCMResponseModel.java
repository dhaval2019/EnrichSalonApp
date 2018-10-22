package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterFCMResponseModel implements Parcelable{

    public ErrorModel Error;



    protected RegisterFCMResponseModel(Parcel in) {
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegisterFCMResponseModel> CREATOR = new Creator<RegisterFCMResponseModel>() {
        @Override
        public RegisterFCMResponseModel createFromParcel(Parcel in) {
            return new RegisterFCMResponseModel(in);
        }

        @Override
        public RegisterFCMResponseModel[] newArray(int size) {
            return new RegisterFCMResponseModel[size];
        }
    };
}
