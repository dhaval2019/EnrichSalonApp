package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CenterResponseModel implements Parcelable{

    public ArrayList<CenterViewModel> Centers;
    public ErrorModel Error;

    protected CenterResponseModel(Parcel in) {
        Centers = in.createTypedArrayList(CenterViewModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Centers);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CenterResponseModel> CREATOR = new Creator<CenterResponseModel>() {
        @Override
        public CenterResponseModel createFromParcel(Parcel in) {
            return new CenterResponseModel(in);
        }

        @Override
        public CenterResponseModel[] newArray(int size) {
            return new CenterResponseModel[size];
        }
    };
}
