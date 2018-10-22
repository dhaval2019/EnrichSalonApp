package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AddressResponseModel implements Parcelable{

    public ArrayList<AddressModel> GuestAddress;
    public ErrorModel Error;

    protected AddressResponseModel(Parcel in) {
        GuestAddress = in.createTypedArrayList(AddressModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(GuestAddress);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressResponseModel> CREATOR = new Creator<AddressResponseModel>() {
        @Override
        public AddressResponseModel createFromParcel(Parcel in) {
            return new AddressResponseModel(in);
        }

        @Override
        public AddressResponseModel[] newArray(int size) {
            return new AddressResponseModel[size];
        }
    };
}
