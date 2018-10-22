package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterFCMRequestModel implements Parcelable {

    public String GuestId;
    public int Platform;
    public String Token;

    public RegisterFCMRequestModel() {
    }

    protected RegisterFCMRequestModel(Parcel in) {
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

    public static final Creator<RegisterFCMRequestModel> CREATOR = new Creator<RegisterFCMRequestModel>() {
        @Override
        public RegisterFCMRequestModel createFromParcel(Parcel in) {
            return new RegisterFCMRequestModel(in);
        }

        @Override
        public RegisterFCMRequestModel[] newArray(int size) {
            return new RegisterFCMRequestModel[size];
        }
    };
}
