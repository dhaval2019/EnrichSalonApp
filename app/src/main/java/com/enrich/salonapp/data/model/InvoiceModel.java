package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class InvoiceModel implements Parcelable {

    public String AppointmentGroupId;
    public String InvoiceId;
    public boolean IsRebooking;
    public GuestModel Guest;
    public boolean IsNoShowChargeToBeApplied;
    public boolean IsCancellationChargeToBeApplied;
    public boolean IsPaymentOrRedemptionApplied;
    public ArrayList<ServiceAppointmentModel> AppointmentServices;
    public PriceModel Price;

    protected InvoiceModel(Parcel in) {
        AppointmentGroupId = in.readString();
        InvoiceId = in.readString();
        IsRebooking = in.readByte() != 0;
        Guest = in.readParcelable(GuestModel.class.getClassLoader());
        IsNoShowChargeToBeApplied = in.readByte() != 0;
        IsCancellationChargeToBeApplied = in.readByte() != 0;
        IsPaymentOrRedemptionApplied = in.readByte() != 0;
        AppointmentServices = in.createTypedArrayList(ServiceAppointmentModel.CREATOR);
        Price = in.readParcelable(PriceModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AppointmentGroupId);
        dest.writeString(InvoiceId);
        dest.writeByte((byte) (IsRebooking ? 1 : 0));
        dest.writeParcelable(Guest, flags);
        dest.writeByte((byte) (IsNoShowChargeToBeApplied ? 1 : 0));
        dest.writeByte((byte) (IsCancellationChargeToBeApplied ? 1 : 0));
        dest.writeByte((byte) (IsPaymentOrRedemptionApplied ? 1 : 0));
        dest.writeTypedList(AppointmentServices);
        dest.writeParcelable(Price, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };
}
