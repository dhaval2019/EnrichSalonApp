package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.Package.PackageModel;

import java.util.ArrayList;

public class PackageDetailsResponseModel implements Parcelable {

    public PackageModel Package;

    protected PackageDetailsResponseModel(Parcel in) {
        Package = in.readParcelable(PackageModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Package, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageDetailsResponseModel> CREATOR = new Creator<PackageDetailsResponseModel>() {
        @Override
        public PackageDetailsResponseModel createFromParcel(Parcel in) {
            return new PackageDetailsResponseModel(in);
        }

        @Override
        public PackageDetailsResponseModel[] newArray(int size) {
            return new PackageDetailsResponseModel[size];
        }
    };
}
