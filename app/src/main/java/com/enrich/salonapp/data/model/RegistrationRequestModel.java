package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.SignIn.SignInGuestModel;

public class RegistrationRequestModel implements Parcelable {

    public String VerificationId;
    public String OTP;
    public String CenterId;
    public SignInGuestModel Guest;

    public RegistrationRequestModel() {
    }

    protected RegistrationRequestModel(Parcel in) {
        VerificationId = in.readString();
        OTP = in.readString();
        CenterId = in.readString();
        Guest = in.readParcelable(SignInGuestModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VerificationId);
        dest.writeString(OTP);
        dest.writeString(CenterId);
        dest.writeParcelable(Guest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegistrationRequestModel> CREATOR = new Creator<RegistrationRequestModel>() {
        @Override
        public RegistrationRequestModel createFromParcel(Parcel in) {
            return new RegistrationRequestModel(in);
        }

        @Override
        public RegistrationRequestModel[] newArray(int size) {
            return new RegistrationRequestModel[size];
        }
    };
}
