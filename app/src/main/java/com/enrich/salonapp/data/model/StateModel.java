package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StateModel implements Parcelable {

    public int Id;
    public String Code;
    public String Name;
    public String ShortName;

    protected StateModel(Parcel in) {
        Id = in.readInt();
        Code = in.readString();
        Name = in.readString();
        ShortName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Code);
        dest.writeString(Name);
        dest.writeString(ShortName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StateModel> CREATOR = new Creator<StateModel>() {
        @Override
        public StateModel createFromParcel(Parcel in) {
            return new StateModel(in);
        }

        @Override
        public StateModel[] newArray(int size) {
            return new StateModel[size];
        }
    };
}
