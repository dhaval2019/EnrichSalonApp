package com.enrich.salonapp.data.model.SignIn;

import android.os.Parcel;
import android.os.Parcelable;

public class SignInGuestModel implements Parcelable {

    public String Id;
    public String FirstName;
    public String LastName;
    public String MobileNumber;
    public String Email;
    public int Gender;
    public String UserName;
    public String CreationDate;
    public String CenterId;
    public String Password;

    public SignInGuestModel() {
    }

    protected SignInGuestModel(Parcel in) {
        Id = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        MobileNumber = in.readString();
        Email = in.readString();
        Gender = in.readInt();
        UserName = in.readString();
        CreationDate = in.readString();
        CenterId = in.readString();
        Password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(MobileNumber);
        dest.writeString(Email);
        dest.writeInt(Gender);
        dest.writeString(UserName);
        dest.writeString(CreationDate);
        dest.writeString(CenterId);
        dest.writeString(Password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignInGuestModel> CREATOR = new Creator<SignInGuestModel>() {
        @Override
        public SignInGuestModel createFromParcel(Parcel in) {
            return new SignInGuestModel(in);
        }

        @Override
        public SignInGuestModel[] newArray(int size) {
            return new SignInGuestModel[size];
        }
    };
}
