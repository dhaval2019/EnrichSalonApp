package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppUpdateModel implements Parcelable {

    public int Platform;
    public String LiveAppVersion;
    public String CurrentAppVersion;
    public boolean ShouldUpdate;
    public boolean ForceUpdate;
    public String ValidTill;

    protected AppUpdateModel(Parcel in) {
        Platform = in.readInt();
        LiveAppVersion = in.readString();
        CurrentAppVersion = in.readString();
        ShouldUpdate = in.readByte() != 0;
        ForceUpdate = in.readByte() != 0;
        ValidTill = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Platform);
        dest.writeString(LiveAppVersion);
        dest.writeString(CurrentAppVersion);
        dest.writeByte((byte) (ShouldUpdate ? 1 : 0));
        dest.writeByte((byte) (ForceUpdate ? 1 : 0));
        dest.writeString(ValidTill);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppUpdateModel> CREATOR = new Creator<AppUpdateModel>() {
        @Override
        public AppUpdateModel createFromParcel(Parcel in) {
            return new AppUpdateModel(in);
        }

        @Override
        public AppUpdateModel[] newArray(int size) {
            return new AppUpdateModel[size];
        }
    };
}
