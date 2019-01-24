package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BeautyAndBlingResponseModel implements Parcelable {

    public ArrayList<BeautyAndBlingModel> BeautyAndBling;
    public ErrorModel Error;

    protected BeautyAndBlingResponseModel(Parcel in) {
        BeautyAndBling = in.createTypedArrayList(BeautyAndBlingModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(BeautyAndBling);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeautyAndBlingResponseModel> CREATOR = new Creator<BeautyAndBlingResponseModel>() {
        @Override
        public BeautyAndBlingResponseModel createFromParcel(Parcel in) {
            return new BeautyAndBlingResponseModel(in);
        }

        @Override
        public BeautyAndBlingResponseModel[] newArray(int size) {
            return new BeautyAndBlingResponseModel[size];
        }
    };
}
