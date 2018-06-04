package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TherapistResponseModel implements Parcelable{

    public ArrayList<TherapistModel> Therapists;
    public ErrorModel Error;

    protected TherapistResponseModel(Parcel in) {
        Therapists = in.createTypedArrayList(TherapistModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Therapists);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TherapistResponseModel> CREATOR = new Creator<TherapistResponseModel>() {
        @Override
        public TherapistResponseModel createFromParcel(Parcel in) {
            return new TherapistResponseModel(in);
        }

        @Override
        public TherapistResponseModel[] newArray(int size) {
            return new TherapistResponseModel[size];
        }
    };
}
