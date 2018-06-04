package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageModel implements Parcelable {

    public int PackageId;
    public String PackageTitle;
    public String PackageDescription;
    public String ValidTill;
    public Double PackageAmount;
    public String PackageImage;
    public String PackageImageWide;
    public String Gender;
    public String PackageImageURL;
    public String PackageImageWideURL;

    protected PackageModel(Parcel in) {
        PackageId = in.readInt();
        PackageTitle = in.readString();
        PackageDescription = in.readString();
        ValidTill = in.readString();
        if (in.readByte() == 0) {
            PackageAmount = null;
        } else {
            PackageAmount = in.readDouble();
        }
        PackageImage = in.readString();
        PackageImageWide = in.readString();
        Gender = in.readString();
        PackageImageURL = in.readString();
        PackageImageWideURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageId);
        dest.writeString(PackageTitle);
        dest.writeString(PackageDescription);
        dest.writeString(ValidTill);
        if (PackageAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(PackageAmount);
        }
        dest.writeString(PackageImage);
        dest.writeString(PackageImageWide);
        dest.writeString(Gender);
        dest.writeString(PackageImageURL);
        dest.writeString(PackageImageWideURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageModel> CREATOR = new Creator<PackageModel>() {
        @Override
        public PackageModel createFromParcel(Parcel in) {
            return new PackageModel(in);
        }

        @Override
        public PackageModel[] newArray(int size) {
            return new PackageModel[size];
        }
    };
}
