package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PackageModel implements Parcelable {

    public int PackageId;
    public String PackageTitle;
    public String PackageSubTitle;
    public String PackageDescription;
    public double StartingPrice;
    public String PackageImage;
    public String PackageImageWide;
    public String Gender;
    public String PackageFor;
    public boolean IsActive;
    public int PackageCategoryId;
    public boolean IsTimerEnable;
    public String PackageValidityDate;
    public String PackageImageURL;
    public String PackageImageWideURL;

    @SerializedName("PackageBundle")
    public List<PackageBundle> packageBundle;

    protected PackageModel(Parcel in) {
        PackageId = in.readInt();
        PackageTitle = in.readString();
        PackageSubTitle = in.readString();
        PackageDescription = in.readString();
        StartingPrice = in.readDouble();
        PackageImage = in.readString();
        PackageImageWide = in.readString();
        Gender = in.readString();
        PackageFor = in.readString();
        IsActive = in.readByte() != 0;
        PackageCategoryId = in.readInt();
        IsTimerEnable = in.readByte() != 0;
        PackageValidityDate = in.readString();
        PackageImageURL = in.readString();
        PackageImageWideURL = in.readString();
        packageBundle = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundle.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageId);
        dest.writeString(PackageTitle);
        dest.writeString(PackageSubTitle);
        dest.writeString(PackageDescription);
        dest.writeDouble(StartingPrice);
        dest.writeString(PackageImage);
        dest.writeString(PackageImageWide);
        dest.writeString(Gender);
        dest.writeString(PackageFor);
        dest.writeByte((byte) (IsActive ? 1 : 0));
        dest.writeInt(PackageCategoryId);
        dest.writeByte((byte) (IsTimerEnable ? 1 : 0));
        dest.writeString(PackageValidityDate);
        dest.writeString(PackageImageURL);
        dest.writeString(PackageImageWideURL);
        dest.writeTypedList(packageBundle);
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
