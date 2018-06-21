package com.enrich.salonapp.data.model.MyPackage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MyPackageModel implements Parcelable {

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
    public String PackageImageURL;
    public String PackageImageWideURL;
    public ArrayList<MyPackageBundleModel> PackageBundle;

    protected MyPackageModel(Parcel in) {
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
        PackageImageURL = in.readString();
        PackageImageWideURL = in.readString();
        PackageBundle = in.createTypedArrayList(MyPackageBundleModel.CREATOR);
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
        dest.writeString(PackageImageURL);
        dest.writeString(PackageImageWideURL);
        dest.writeTypedList(PackageBundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyPackageModel> CREATOR = new Creator<MyPackageModel>() {
        @Override
        public MyPackageModel createFromParcel(Parcel in) {
            return new MyPackageModel(in);
        }

        @Override
        public MyPackageModel[] newArray(int size) {
            return new MyPackageModel[size];
        }
    };
}
