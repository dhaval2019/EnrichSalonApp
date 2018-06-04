package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SlotModel implements Parcelable {

    public String Time;
    public String Warnings;
    public int Priority;
    public boolean Available;

    protected SlotModel(Parcel in) {
        Time = in.readString();
        Warnings = in.readString();
        Priority = in.readInt();
        Available = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Time);
        dest.writeString(Warnings);
        dest.writeInt(Priority);
        dest.writeByte((byte) (Available ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SlotModel> CREATOR = new Creator<SlotModel>() {
        @Override
        public SlotModel createFromParcel(Parcel in) {
            return new SlotModel(in);
        }

        @Override
        public SlotModel[] newArray(int size) {
            return new SlotModel[size];
        }
    };
}
