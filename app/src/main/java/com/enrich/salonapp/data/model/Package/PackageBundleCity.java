package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBundleCity implements Parcelable{

    public int PackageBundleCityId;
    public int CityId;

    protected PackageBundleCity(Parcel in) {
        PackageBundleCityId = in.readInt();
        CityId = in.readInt();
    }

    public static final Creator<PackageBundleCity> CREATOR = new Creator<PackageBundleCity>() {
        @Override
        public PackageBundleCity createFromParcel(Parcel in) {
            return new PackageBundleCity(in);
        }

        @Override
        public PackageBundleCity[] newArray(int size) {
            return new PackageBundleCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(PackageBundleCityId);
        parcel.writeInt(CityId);
    }
}
