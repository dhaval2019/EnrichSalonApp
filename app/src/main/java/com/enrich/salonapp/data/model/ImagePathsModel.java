package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ImagePathsModel implements Parcelable {

    @SerializedName("px64")
    public String px64;
    @SerializedName("px100")
    public String px100;
    @SerializedName("px200")
    public String px200;
    @SerializedName("px400")
    public String px400;
    @SerializedName("px800")
    public String px800;

    protected ImagePathsModel(Parcel in) {
        px64 = in.readString();
        px100 = in.readString();
        px200 = in.readString();
        px400 = in.readString();
        px800 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(px64);
        dest.writeString(px100);
        dest.writeString(px200);
        dest.writeString(px400);
        dest.writeString(px800);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImagePathsModel> CREATOR = new Creator<ImagePathsModel>() {
        @Override
        public ImagePathsModel createFromParcel(Parcel in) {
            return new ImagePathsModel(in);
        }

        @Override
        public ImagePathsModel[] newArray(int size) {
            return new ImagePathsModel[size];
        }
    };
}
