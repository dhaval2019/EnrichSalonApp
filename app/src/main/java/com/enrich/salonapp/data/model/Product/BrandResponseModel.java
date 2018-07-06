package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BrandResponseModel implements Parcelable {

    public ArrayList<BrandModel> Brands;

    protected BrandResponseModel(Parcel in) {
        Brands = in.createTypedArrayList(BrandModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Brands);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandResponseModel> CREATOR = new Creator<BrandResponseModel>() {
        @Override
        public BrandResponseModel createFromParcel(Parcel in) {
            return new BrandResponseModel(in);
        }

        @Override
        public BrandResponseModel[] newArray(int size) {
            return new BrandResponseModel[size];
        }
    };
}
