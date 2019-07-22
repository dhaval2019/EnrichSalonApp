package com.enrich.salonapp.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FriendResponseModel implements Parcelable{

    public ArrayList<GuestMobileNo> ValidReferrals;
    public ArrayList<GuestMobileNo> ExistingGuests;
    public ArrayList<GuestMobileNo> ExistingReferrals;
    public ErrorModel Error;

    protected FriendResponseModel(Parcel in) {
        ValidReferrals = in.createTypedArrayList(GuestMobileNo.CREATOR);
        ExistingGuests = in.createTypedArrayList(GuestMobileNo.CREATOR);
        ExistingReferrals = in.createTypedArrayList(GuestMobileNo.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ValidReferrals);
        dest.writeTypedList(ExistingGuests);
        dest.writeTypedList(ExistingReferrals);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendResponseModel> CREATOR = new Creator<FriendResponseModel>() {
        @Override
        public FriendResponseModel createFromParcel(Parcel in) {
            return new FriendResponseModel(in);
        }

        @Override
        public FriendResponseModel[] newArray(int size) {
            return new FriendResponseModel[size];
        }
    };
}

