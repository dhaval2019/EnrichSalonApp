package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateOTPRequestModel implements Parcelable {

    public PhoneModel Phone;
    public String CenterId;

    public CreateOTPRequestModel() {
    }

    protected CreateOTPRequestModel(Parcel in) {
        Phone = in.readParcelable(PhoneModel.class.getClassLoader());
        CenterId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Phone, flags);
        dest.writeString(CenterId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateOTPRequestModel> CREATOR = new Creator<CreateOTPRequestModel>() {
        @Override
        public CreateOTPRequestModel createFromParcel(Parcel in) {
            return new CreateOTPRequestModel(in);
        }

        @Override
        public CreateOTPRequestModel[] newArray(int size) {
            return new CreateOTPRequestModel[size];
        }
    };
}

