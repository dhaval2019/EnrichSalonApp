package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.MyPackage.MyPackageModel;

import java.util.ArrayList;

public class MyPackageResponseModel implements Parcelable{

    public ArrayList<MyPackageModel> GuestPackage;

    protected MyPackageResponseModel(Parcel in) {
        GuestPackage = in.createTypedArrayList(MyPackageModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(GuestPackage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyPackageResponseModel> CREATOR = new Creator<MyPackageResponseModel>() {
        @Override
        public MyPackageResponseModel createFromParcel(Parcel in) {
            return new MyPackageResponseModel(in);
        }

        @Override
        public MyPackageResponseModel[] newArray(int size) {
            return new MyPackageResponseModel[size];
        }
    };
}
