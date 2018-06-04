package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AppointmentModel implements Parcelable {

    public String AppointmentGroupId;
    public String InvoiceId;
    public int InvoiceStatus;
    public boolean IsRebooking;
    public String Notes;
    public GuestModel Guest;
    public boolean IsNoShowChargeToBeApplied;
    public boolean IsCancellationChargeToBeApplied;
    public boolean IsPaymentOrRedemptionApplied;
    public ArrayList<ServiceAppointmentModel> AppointmentServices;
    public CenterDetailModel Center;
    public PriceModel Price;

    protected AppointmentModel(Parcel in) {
        AppointmentGroupId = in.readString();
        InvoiceId = in.readString();
        InvoiceStatus = in.readInt();
        IsRebooking = in.readByte() != 0;
        Notes = in.readString();
        Guest = in.readParcelable(GuestModel.class.getClassLoader());
        IsNoShowChargeToBeApplied = in.readByte() != 0;
        IsCancellationChargeToBeApplied = in.readByte() != 0;
        IsPaymentOrRedemptionApplied = in.readByte() != 0;
        AppointmentServices = in.createTypedArrayList(ServiceAppointmentModel.CREATOR);
        Center = in.readParcelable(CenterDetailModel.class.getClassLoader());
        Price = in.readParcelable(PriceModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AppointmentGroupId);
        dest.writeString(InvoiceId);
        dest.writeInt(InvoiceStatus);
        dest.writeByte((byte) (IsRebooking ? 1 : 0));
        dest.writeString(Notes);
        dest.writeParcelable(Guest, flags);
        dest.writeByte((byte) (IsNoShowChargeToBeApplied ? 1 : 0));
        dest.writeByte((byte) (IsCancellationChargeToBeApplied ? 1 : 0));
        dest.writeByte((byte) (IsPaymentOrRedemptionApplied ? 1 : 0));
        dest.writeTypedList(AppointmentServices);
        dest.writeParcelable(Center, flags);
        dest.writeParcelable(Price, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentModel> CREATOR = new Creator<AppointmentModel>() {
        @Override
        public AppointmentModel createFromParcel(Parcel in) {
            return new AppointmentModel(in);
        }

        @Override
        public AppointmentModel[] newArray(int size) {
            return new AppointmentModel[size];
        }
    };
}
