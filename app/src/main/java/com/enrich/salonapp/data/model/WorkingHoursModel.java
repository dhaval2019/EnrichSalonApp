package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WorkingHoursModel implements Parcelable {

    @SerializedName("day_of_week")
    public String DayOfWeek;

    @SerializedName("start_time")
    public String StartTime;

    @SerializedName("end_time")
    public String EndTime;

    protected WorkingHoursModel(Parcel in) {
        DayOfWeek = in.readString();
        StartTime = in.readString();
        EndTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DayOfWeek);
        dest.writeString(StartTime);
        dest.writeString(EndTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkingHoursModel> CREATOR = new Creator<WorkingHoursModel>() {
        @Override
        public WorkingHoursModel createFromParcel(Parcel in) {
            return new WorkingHoursModel(in);
        }

        @Override
        public WorkingHoursModel[] newArray(int size) {
            return new WorkingHoursModel[size];
        }
    };
}
