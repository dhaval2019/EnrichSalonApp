package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpinPriceModel implements Parcelable {

    public String GuestId;
    public String CenterId;
    public String SpinDate;
    public int PriceWon;
    public ErrorModel Error;

    protected SpinPriceModel(Parcel in) {
        GuestId = in.readString();
        CenterId = in.readString();
        SpinDate = in.readString();
        PriceWon = in.readInt();
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GuestId);
        dest.writeString(CenterId);
        dest.writeString(SpinDate);
        dest.writeInt(PriceWon);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpinPriceModel> CREATOR = new Creator<SpinPriceModel>() {
        @Override
        public SpinPriceModel createFromParcel(Parcel in) {
            return new SpinPriceModel(in);
        }

        @Override
        public SpinPriceModel[] newArray(int size) {
            return new SpinPriceModel[size];
        }
    };
}
