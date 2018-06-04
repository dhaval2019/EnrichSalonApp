package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceCustomDataPermissionModel implements Parcelable {

    public boolean CanView;
    public boolean CanEdit;

    protected ServiceCustomDataPermissionModel(Parcel in) {
        CanView = in.readByte() != 0;
        CanEdit = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (CanView ? 1 : 0));
        dest.writeByte((byte) (CanEdit ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceCustomDataPermissionModel> CREATOR = new Creator<ServiceCustomDataPermissionModel>() {
        @Override
        public ServiceCustomDataPermissionModel createFromParcel(Parcel in) {
            return new ServiceCustomDataPermissionModel(in);
        }

        @Override
        public ServiceCustomDataPermissionModel[] newArray(int size) {
            return new ServiceCustomDataPermissionModel[size];
        }
    };
}
