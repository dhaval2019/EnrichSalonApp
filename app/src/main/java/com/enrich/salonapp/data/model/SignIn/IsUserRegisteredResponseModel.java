package com.enrich.salonapp.data.model.SignIn;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.ErrorModel;

public class IsUserRegisteredResponseModel implements Parcelable {

    public SignInGuestModel Guest;
    public ErrorModel Error;

    public IsUserRegisteredResponseModel() {
    }

    protected IsUserRegisteredResponseModel(Parcel in) {
        Guest = in.readParcelable(SignInGuestModel.class.getClassLoader());
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

    public static final Creator<IsUserRegisteredResponseModel> CREATOR = new Creator<IsUserRegisteredResponseModel>() {
        @Override
        public IsUserRegisteredResponseModel createFromParcel(Parcel in) {
            return new IsUserRegisteredResponseModel(in);
        }

        @Override
        public IsUserRegisteredResponseModel[] newArray(int size) {
            return new IsUserRegisteredResponseModel[size];
        }
    };
}
