package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestInvoiceAndSpinCountModel implements Parcelable {

    public String CenterId;
    public String InvoiceId;
    public int InvoiceAmount;
    public String PurchaseType;
    public int NoOfSpin;
    public int RemainingSpins;

    protected GuestInvoiceAndSpinCountModel(Parcel in) {
        CenterId = in.readString();
        InvoiceId = in.readString();
        InvoiceAmount = in.readInt();
        PurchaseType = in.readString();
        NoOfSpin = in.readInt();
        RemainingSpins = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CenterId);
        dest.writeString(InvoiceId);
        dest.writeInt(InvoiceAmount);
        dest.writeString(PurchaseType);
        dest.writeInt(NoOfSpin);
        dest.writeInt(RemainingSpins);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestInvoiceAndSpinCountModel> CREATOR = new Creator<GuestInvoiceAndSpinCountModel>() {
        @Override
        public GuestInvoiceAndSpinCountModel createFromParcel(Parcel in) {
            return new GuestInvoiceAndSpinCountModel(in);
        }

        @Override
        public GuestInvoiceAndSpinCountModel[] newArray(int size) {
            return new GuestInvoiceAndSpinCountModel[size];
        }
    };
}
