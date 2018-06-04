package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AppointmentResponseModel implements Parcelable {

    public ArrayList<AppointmentModel> Appointments;
    public ErrorModel Error;

    protected AppointmentResponseModel(Parcel in) {
        Appointments = in.createTypedArrayList(AppointmentModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Appointments);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentResponseModel> CREATOR = new Creator<AppointmentResponseModel>() {
        @Override
        public AppointmentResponseModel createFromParcel(Parcel in) {
            return new AppointmentResponseModel(in);
        }

        @Override
        public AppointmentResponseModel[] newArray(int size) {
            return new AppointmentResponseModel[size];
        }
    };
}
