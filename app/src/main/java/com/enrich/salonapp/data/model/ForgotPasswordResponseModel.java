package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForgotPasswordResponseModel implements Parcelable {

    public boolean Success;
    public ErrorModel Error;

    protected ForgotPasswordResponseModel(Parcel in) {
        Success = in.readByte() != 0;
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (Success ? 1 : 0));
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ForgotPasswordResponseModel> CREATOR = new Creator<ForgotPasswordResponseModel>() {
        @Override
        public ForgotPasswordResponseModel createFromParcel(Parcel in) {
            return new ForgotPasswordResponseModel(in);
        }

        @Override
        public ForgotPasswordResponseModel[] newArray(int size) {
            return new ForgotPasswordResponseModel[size];
        }
    };
}
