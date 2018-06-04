package com.enrich.salonapp.data.model.AppointmentModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AppointmentSlotBookingsModel implements Parcelable {
    public ArrayList<AppointmentServicesModel> Services;
    public int Quantity;
    public String TherapistId;
    public String GuestId;
    public String StartTime;

    public AppointmentSlotBookingsModel() {
    }

    protected AppointmentSlotBookingsModel(Parcel in) {
        Quantity = in.readInt();
        TherapistId = in.readString();
        GuestId = in.readString();
        StartTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Quantity);
        dest.writeString(TherapistId);
        dest.writeString(GuestId);
        dest.writeString(StartTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentSlotBookingsModel> CREATOR = new Creator<AppointmentSlotBookingsModel>() {
        @Override
        public AppointmentSlotBookingsModel createFromParcel(Parcel in) {
            return new AppointmentSlotBookingsModel(in);
        }

        @Override
        public AppointmentSlotBookingsModel[] newArray(int size) {
            return new AppointmentSlotBookingsModel[size];
        }
    };
}
