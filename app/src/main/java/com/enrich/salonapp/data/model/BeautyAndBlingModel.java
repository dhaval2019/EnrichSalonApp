package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BeautyAndBlingModel implements Parcelable {

    public int BeautyAndBlingId;
    public String Name;
    public String FromDate;
    public String ToDate;
    public int PurchaseAmount;
    public int ServiceAmount;
    public int ProductAmount;
    public boolean IsActive;

    protected BeautyAndBlingModel(Parcel in) {
        BeautyAndBlingId = in.readInt();
        Name = in.readString();
        FromDate = in.readString();
        ToDate = in.readString();
        PurchaseAmount = in.readInt();
        ServiceAmount = in.readInt();
        ProductAmount = in.readInt();
        IsActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BeautyAndBlingId);
        dest.writeString(Name);
        dest.writeString(FromDate);
        dest.writeString(ToDate);
        dest.writeInt(PurchaseAmount);
        dest.writeInt(ServiceAmount);
        dest.writeInt(ProductAmount);
        dest.writeByte((byte) (IsActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeautyAndBlingModel> CREATOR = new Creator<BeautyAndBlingModel>() {
        @Override
        public BeautyAndBlingModel createFromParcel(Parcel in) {
            return new BeautyAndBlingModel(in);
        }

        @Override
        public BeautyAndBlingModel[] newArray(int size) {
            return new BeautyAndBlingModel[size];
        }
    };
}
