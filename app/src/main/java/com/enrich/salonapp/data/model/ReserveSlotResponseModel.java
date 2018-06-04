package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReserveSlotResponseModel implements Parcelable {

    public String CenterId;
    public String CenterTime;
    public boolean IsReserved;
    public String ReservationId;
    public String InvoiceId;
    public String ExpiryTime;
    public int BlockingTime;
    public ArrayList<SlotBookingsModel> SlotBookings;
    public ErrorModel Error;

    protected ReserveSlotResponseModel(Parcel in) {
        CenterId = in.readString();
        CenterTime = in.readString();
        IsReserved = in.readByte() != 0;
        ReservationId = in.readString();
        InvoiceId = in.readString();
        ExpiryTime = in.readString();
        BlockingTime = in.readInt();
        SlotBookings = in.createTypedArrayList(SlotBookingsModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CenterId);
        dest.writeString(CenterTime);
        dest.writeByte((byte) (IsReserved ? 1 : 0));
        dest.writeString(ReservationId);
        dest.writeString(InvoiceId);
        dest.writeString(ExpiryTime);
        dest.writeInt(BlockingTime);
        dest.writeTypedList(SlotBookings);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReserveSlotResponseModel> CREATOR = new Creator<ReserveSlotResponseModel>() {
        @Override
        public ReserveSlotResponseModel createFromParcel(Parcel in) {
            return new ReserveSlotResponseModel(in);
        }

        @Override
        public ReserveSlotResponseModel[] newArray(int size) {
            return new ReserveSlotResponseModel[size];
        }
    };
}
