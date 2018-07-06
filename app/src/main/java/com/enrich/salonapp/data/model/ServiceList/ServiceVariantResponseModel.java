package com.enrich.salonapp.data.model.ServiceList;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.ServiceViewModel;

import java.util.ArrayList;

public class ServiceVariantResponseModel implements Parcelable {

    public ArrayList<ServiceViewModel> ServiceVariants;

    protected ServiceVariantResponseModel(Parcel in) {
        ServiceVariants = in.createTypedArrayList(ServiceViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ServiceVariants);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceVariantResponseModel> CREATOR = new Creator<ServiceVariantResponseModel>() {
        @Override
        public ServiceVariantResponseModel createFromParcel(Parcel in) {
            return new ServiceVariantResponseModel(in);
        }

        @Override
        public ServiceVariantResponseModel[] newArray(int size) {
            return new ServiceVariantResponseModel[size];
        }
    };
}
