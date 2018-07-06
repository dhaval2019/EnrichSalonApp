package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by varunbarve on 23/10/17.
 */

public class ErrorModel implements Parcelable {

    public int StatusCode;
    public String Message;
    public String InternalMessage;

    public ErrorModel() {
    }

    protected ErrorModel(Parcel in) {
        StatusCode = in.readInt();
        Message = in.readString();
        InternalMessage = in.readString();
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(StatusCode);
        dest.writeString(Message);
        dest.writeString(InternalMessage);
    }

    @Override
    public int describeContents () {
        return 0;
    }

    public static final Creator<ErrorModel> CREATOR = new Creator<ErrorModel>() {
        @Override
        public ErrorModel createFromParcel (Parcel in) {
            return new ErrorModel(in);
        }

        @Override
        public ErrorModel[] newArray (int size) {
            return new ErrorModel[size];
        }
    };
}
