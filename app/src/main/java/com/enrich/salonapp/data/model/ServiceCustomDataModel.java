package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceCustomDataModel implements Parcelable {

    public ServiceCustomDataPermissionModel Permission;

    protected ServiceCustomDataModel(Parcel in) {
        Permission = in.readParcelable(ServiceCustomDataPermissionModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Permission, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceCustomDataModel> CREATOR = new Creator<ServiceCustomDataModel>() {
        @Override
        public ServiceCustomDataModel createFromParcel(Parcel in) {
            return new ServiceCustomDataModel(in);
        }

        @Override
        public ServiceCustomDataModel[] newArray(int size) {
            return new ServiceCustomDataModel[size];
        }
    };
}
