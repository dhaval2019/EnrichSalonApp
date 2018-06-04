package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NewAndPopularResponseModel implements Parcelable{

    public CategoryModel Category;
    public ArrayList<ServiceViewModel> Services;

    protected NewAndPopularResponseModel(Parcel in) {
        Category = in.readParcelable(CategoryModel.class.getClassLoader());
        Services = in.createTypedArrayList(ServiceViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Category, flags);
        dest.writeTypedList(Services);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewAndPopularResponseModel> CREATOR = new Creator<NewAndPopularResponseModel>() {
        @Override
        public NewAndPopularResponseModel createFromParcel(Parcel in) {
            return new NewAndPopularResponseModel(in);
        }

        @Override
        public NewAndPopularResponseModel[] newArray(int size) {
            return new NewAndPopularResponseModel[size];
        }
    };
}
