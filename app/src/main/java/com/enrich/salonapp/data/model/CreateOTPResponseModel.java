package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateOTPResponseModel implements Parcelable {

    public String VerificationId;
    public ErrorModel Error;

    protected CreateOTPResponseModel(Parcel in) {
        VerificationId = in.readString();
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VerificationId);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateOTPResponseModel> CREATOR = new Creator<CreateOTPResponseModel>() {
        @Override
        public CreateOTPResponseModel createFromParcel(Parcel in) {
            return new CreateOTPResponseModel(in);
        }

        @Override
        public CreateOTPResponseModel[] newArray(int size) {
            return new CreateOTPResponseModel[size];
        }
    };
}
