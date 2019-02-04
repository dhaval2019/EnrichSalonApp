package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterDeviceRequestModel implements Parcelable {

    public String GuestId;
    public int Platform;
    public String Token;

    public RegisterDeviceRequestModel() {
    }

    protected RegisterDeviceRequestModel(Parcel in) {
        GuestId = in.readString();
        Platform = in.readInt();
        Token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GuestId);
        dest.writeInt(Platform);
        dest.writeString(Token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegisterDeviceRequestModel> CREATOR = new Creator<RegisterDeviceRequestModel>() {
        @Override
        public RegisterDeviceRequestModel createFromParcel(Parcel in) {
            return new RegisterDeviceRequestModel(in);
        }

        @Override
        public RegisterDeviceRequestModel[] newArray(int size) {
            return new RegisterDeviceRequestModel[size];
        }
    };
}
