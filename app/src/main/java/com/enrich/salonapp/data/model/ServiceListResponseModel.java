package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ServiceListResponseModel implements Parcelable {

    public CategoryModel Category;
    public ArrayList<ParentServiceViewModel> Services;
    public int Total;
    public ErrorModel Error;

    protected ServiceListResponseModel(Parcel in) {
        Category = in.readParcelable(CategoryModel.class.getClassLoader());
        Services = in.createTypedArrayList(ParentServiceViewModel.CREATOR);
        Total = in.readInt();
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Category, flags);
        dest.writeTypedList(Services);
        dest.writeInt(Total);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceListResponseModel> CREATOR = new Creator<ServiceListResponseModel>() {
        @Override
        public ServiceListResponseModel createFromParcel(Parcel in) {
            return new ServiceListResponseModel(in);
        }

        @Override
        public ServiceListResponseModel[] newArray(int size) {
            return new ServiceListResponseModel[size];
        }
    };
}

