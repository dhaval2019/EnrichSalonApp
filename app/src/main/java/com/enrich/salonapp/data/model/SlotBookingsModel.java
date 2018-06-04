package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SlotBookingsModel implements Parcelable {

    public String GuestId;
    public String AppointmentGroupId;
    public ArrayList<ServiceAppointmentModel> Services;
    public String TherapistId;
    public String RoomId;
    public int Quantity;
    public String BookingNotes;
    public PriceModel Price;
    public String VirtualGuest;
    public String[] CartItemIds;
    public String ReducedTimeGroupId;

    protected SlotBookingsModel(Parcel in) {
        GuestId = in.readString();
        AppointmentGroupId = in.readString();
        Services = in.createTypedArrayList(ServiceAppointmentModel.CREATOR);
        TherapistId = in.readString();
        RoomId = in.readString();
        Quantity = in.readInt();
        BookingNotes = in.readString();
        Price = in.readParcelable(PriceModel.class.getClassLoader());
        VirtualGuest = in.readString();
        CartItemIds = in.createStringArray();
        ReducedTimeGroupId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GuestId);
        dest.writeString(AppointmentGroupId);
        dest.writeTypedList(Services);
        dest.writeString(TherapistId);
        dest.writeString(RoomId);
        dest.writeInt(Quantity);
        dest.writeString(BookingNotes);
        dest.writeParcelable(Price, flags);
        dest.writeString(VirtualGuest);
        dest.writeStringArray(CartItemIds);
        dest.writeString(ReducedTimeGroupId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SlotBookingsModel> CREATOR = new Creator<SlotBookingsModel>() {
        @Override
        public SlotBookingsModel createFromParcel(Parcel in) {
            return new SlotBookingsModel(in);
        }

        @Override
        public SlotBookingsModel[] newArray(int size) {
            return new SlotBookingsModel[size];
        }
    };
}
