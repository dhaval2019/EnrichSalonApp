package com.enrich.salonapp.data.model.ServiceList;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.ServiceViewModel;

import java.util.ArrayList;

public class ParentAndNormalServiceListResponseModel implements Parcelable{

    public ArrayList<ServiceViewModel> ParentAndNormalServiceList;

    protected ParentAndNormalServiceListResponseModel(Parcel in) {
        ParentAndNormalServiceList = in.createTypedArrayList(ServiceViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ParentAndNormalServiceList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParentAndNormalServiceListResponseModel> CREATOR = new Creator<ParentAndNormalServiceListResponseModel>() {
        @Override
        public ParentAndNormalServiceListResponseModel createFromParcel(Parcel in) {
            return new ParentAndNormalServiceListResponseModel(in);
        }

        @Override
        public ParentAndNormalServiceListResponseModel[] newArray(int size) {
            return new ParentAndNormalServiceListResponseModel[size];
        }
    };
}
