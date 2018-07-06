package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandModel implements Parcelable {
    public int BrandId;
    public String Name;
    public String Image;
    public String ImageUrl;

    protected BrandModel(Parcel in) {
        BrandId = in.readInt();
        Name = in.readString();
        Image = in.readString();
        ImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BrandId);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(ImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandModel> CREATOR = new Creator<BrandModel>() {
        @Override
        public BrandModel createFromParcel(Parcel in) {
            return new BrandModel(in);
        }

        @Override
        public BrandModel[] newArray(int size) {
            return new BrandModel[size];
        }
    };
}
