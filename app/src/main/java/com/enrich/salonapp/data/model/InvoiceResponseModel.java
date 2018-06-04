package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InvoiceResponseModel implements Parcelable {

    public InvoiceModel AppointmentGroup;
    public ErrorModel Error;

    protected InvoiceResponseModel(Parcel in) {
        AppointmentGroup = in.readParcelable(InvoiceModel.class.getClassLoader());
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(AppointmentGroup, flags);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvoiceResponseModel> CREATOR = new Creator<InvoiceResponseModel>() {
        @Override
        public InvoiceResponseModel createFromParcel(Parcel in) {
            return new InvoiceResponseModel(in);
        }

        @Override
        public InvoiceResponseModel[] newArray(int size) {
            return new InvoiceResponseModel[size];
        }
    };
}
