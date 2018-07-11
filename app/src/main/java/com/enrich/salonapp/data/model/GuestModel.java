package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GuestModel implements Parcelable {

    @SerializedName("id")
    public String Id;

    @SerializedName("GuestFName")
    public String FirstName;

    @SerializedName("GuestLName")
    public String LastName;

    @SerializedName("Email")
    public String Email;

    @SerializedName("GuestPhone")
    public String MobileNumber;

    @SerializedName("Gender")
    public String Gender;

    @SerializedName("CreationDate")
    public String CreationDate;

    @SerializedName("CenterId")
    public String CenterId;

    @SerializedName("DOB")
    public String DateOfBirth;

    @SerializedName("City")
    public String City;

    @SerializedName("StateName")
    public String State;

    @SerializedName("CountryName")
    public String Country;

    @SerializedName("IsMember")
    public int IsMember;

    public String UserName;
    public String Password;

    public GuestModel() {
    }

    protected GuestModel(Parcel in) {
        Id = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Email = in.readString();
        MobileNumber = in.readString();
        Gender = in.readString();
        CreationDate = in.readString();
        CenterId = in.readString();
        DateOfBirth = in.readString();
        City = in.readString();
        State = in.readString();
        Country = in.readString();
        IsMember = in.readInt();
        UserName = in.readString();
        Password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Email);
        dest.writeString(MobileNumber);
        dest.writeString(Gender);
        dest.writeString(CreationDate);
        dest.writeString(CenterId);
        dest.writeString(DateOfBirth);
        dest.writeString(City);
        dest.writeString(State);
        dest.writeString(Country);
        dest.writeInt(IsMember);
        dest.writeString(UserName);
        dest.writeString(Password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestModel> CREATOR = new Creator<GuestModel>() {
        @Override
        public GuestModel createFromParcel(Parcel in) {
            return new GuestModel(in);
        }

        @Override
        public GuestModel[] newArray(int size) {
            return new GuestModel[size];
        }
    };
}
