package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegistrationResponseModel implements Parcelable {

    public String LoginToken;
    public boolean Success;
    public boolean IsNewGuest;
    public ErrorModel Error;

    protected RegistrationResponseModel(Parcel in) {
        LoginToken = in.readString();
        Success = in.readByte() != 0;
        IsNewGuest = in.readByte() != 0;
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LoginToken);
        dest.writeByte((byte) (Success ? 1 : 0));
        dest.writeByte((byte) (IsNewGuest ? 1 : 0));
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RegistrationResponseModel> CREATOR = new Parcelable.Creator<RegistrationResponseModel>() {
        @Override
        public RegistrationResponseModel createFromParcel(Parcel in) {
            return new RegistrationResponseModel(in);
        }

        @Override
        public RegistrationResponseModel[] newArray(int size) {
            return new RegistrationResponseModel[size];
        }
    };
}
