package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppUpdateResponseModel implements Parcelable {

    public AppUpdateModel AppUpdate;
    public ErrorModel Error;

    protected AppUpdateResponseModel(Parcel in) {
        AppUpdate = in.readParcelable(AppUpdateModel.class.getClassLoader());
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(AppUpdate, flags);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppUpdateResponseModel> CREATOR = new Creator<AppUpdateResponseModel>() {
        @Override
        public AppUpdateResponseModel createFromParcel(Parcel in) {
            return new AppUpdateResponseModel(in);
        }

        @Override
        public AppUpdateResponseModel[] newArray(int size) {
            return new AppUpdateResponseModel[size];
        }
    };
}
