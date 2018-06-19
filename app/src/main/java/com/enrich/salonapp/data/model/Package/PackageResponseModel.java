package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PackageResponseModel implements Parcelable {

    public ArrayList<PackageModel> Package;


    protected PackageResponseModel(Parcel in) {
        Package = in.createTypedArrayList(PackageModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Package);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageResponseModel> CREATOR = new Creator<PackageResponseModel>() {
        @Override
        public PackageResponseModel createFromParcel(Parcel in) {
            return new PackageResponseModel(in);
        }

        @Override
        public PackageResponseModel[] newArray(int size) {
            return new PackageResponseModel[size];
        }
    };
}
