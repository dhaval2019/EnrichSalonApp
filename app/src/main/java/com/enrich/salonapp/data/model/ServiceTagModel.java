package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceTagModel implements Parcelable{

    public int ServiceTagId;
    public String SearchTag;


    protected ServiceTagModel(Parcel in) {
        ServiceTagId = in.readInt();
        SearchTag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ServiceTagId);
        dest.writeString(SearchTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceTagModel> CREATOR = new Creator<ServiceTagModel>() {
        @Override
        public ServiceTagModel createFromParcel(Parcel in) {
            return new ServiceTagModel(in);
        }

        @Override
        public ServiceTagModel[] newArray(int size) {
            return new ServiceTagModel[size];
        }
    };
}
