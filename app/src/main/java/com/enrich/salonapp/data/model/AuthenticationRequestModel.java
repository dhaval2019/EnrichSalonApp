package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthenticationRequestModel implements Parcelable {

    public String grant_type = "password";
    public String username = "";
    public String password = "";
    public String clientid = "enrich";

    public AuthenticationRequestModel() {
    }

    protected AuthenticationRequestModel(Parcel in) {
        grant_type = in.readString();
        username = in.readString();
        password = in.readString();
        clientid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(grant_type);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(clientid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticationRequestModel> CREATOR = new Creator<AuthenticationRequestModel>() {
        @Override
        public AuthenticationRequestModel createFromParcel(Parcel in) {
            return new AuthenticationRequestModel(in);
        }

        @Override
        public AuthenticationRequestModel[] newArray(int size) {
            return new AuthenticationRequestModel[size];
        }
    };
}
