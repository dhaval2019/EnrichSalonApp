package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AvailableTimeResponseModel implements Parcelable {

    public String ReservationId;
//    public SlotBookingsModel[] SlotBookings;
    public ArrayList<SlotModel> OpenSlots = new ArrayList<>();
    public ArrayList<SlotModel> ClosedSlots;
    public ErrorModel Error;

    public AvailableTimeResponseModel() {
    }

    protected AvailableTimeResponseModel(Parcel in) {
        ReservationId = in.readString();
        OpenSlots = in.createTypedArrayList(SlotModel.CREATOR);
        ClosedSlots = in.createTypedArrayList(SlotModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ReservationId);
        dest.writeTypedList(OpenSlots);
        dest.writeTypedList(ClosedSlots);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AvailableTimeResponseModel> CREATOR = new Creator<AvailableTimeResponseModel>() {
        @Override
        public AvailableTimeResponseModel createFromParcel(Parcel in) {
            return new AvailableTimeResponseModel(in);
        }

        @Override
        public AvailableTimeResponseModel[] newArray(int size) {
            return new AvailableTimeResponseModel[size];
        }
    };
}
