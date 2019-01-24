package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GuestSpinCountResponseModel implements Parcelable {

    public String GuestId;
    public String UserName;
    public ArrayList<GuestInvoiceAndSpinCountModel> GuestSpinWheel;
    public int SpinsWithoutInvoices;
    public int WonPrice;
    public String ValidityDate;
    public ErrorModel Error;

    protected GuestSpinCountResponseModel(Parcel in) {
        GuestId = in.readString();
        UserName = in.readString();
        GuestSpinWheel = in.createTypedArrayList(GuestInvoiceAndSpinCountModel.CREATOR);
        SpinsWithoutInvoices = in.readInt();
        WonPrice = in.readInt();
        ValidityDate = in.readString();
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GuestId);
        dest.writeString(UserName);
        dest.writeTypedList(GuestSpinWheel);
        dest.writeInt(SpinsWithoutInvoices);
        dest.writeInt(WonPrice);
        dest.writeString(ValidityDate);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestSpinCountResponseModel> CREATOR = new Creator<GuestSpinCountResponseModel>() {
        @Override
        public GuestSpinCountResponseModel createFromParcel(Parcel in) {
            return new GuestSpinCountResponseModel(in);
        }

        @Override
        public GuestSpinCountResponseModel[] newArray(int size) {
            return new GuestSpinCountResponseModel[size];
        }
    };
}
