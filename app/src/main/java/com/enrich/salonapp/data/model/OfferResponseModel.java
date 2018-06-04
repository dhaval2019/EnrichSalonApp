package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OfferResponseModel implements Parcelable {

    public ArrayList<OfferModel> Offers;

    protected OfferResponseModel(Parcel in) {
        Offers = in.createTypedArrayList(OfferModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Offers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferResponseModel> CREATOR = new Creator<OfferResponseModel>() {
        @Override
        public OfferResponseModel createFromParcel(Parcel in) {
            return new OfferResponseModel(in);
        }

        @Override
        public OfferResponseModel[] newArray(int size) {
            return new OfferResponseModel[size];
        }
    };
}
