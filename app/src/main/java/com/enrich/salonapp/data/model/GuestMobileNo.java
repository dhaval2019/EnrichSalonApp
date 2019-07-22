package com.enrich.salonapp.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class GuestMobileNo implements Parcelable {
    public GuestMobileNo()
    {

    }
    public String MobileNo;
    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo1) {
        this.MobileNo = MobileNo1;
    }
    public GuestMobileNo(String MobileNo1)
    {
        this.MobileNo = MobileNo1;
    }
    protected GuestMobileNo(Parcel in) {

        MobileNo = in.readString();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(MobileNo);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestMobileNo> CREATOR = new Creator<GuestMobileNo>() {
        @Override
        public GuestMobileNo createFromParcel(Parcel in) {
            return new GuestMobileNo(in);
        }

        @Override
        public GuestMobileNo[] newArray(int size) {
            return new GuestMobileNo[size];
        }
    };
}
