package com.enrich.salonapp.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReferFriendModel implements Parcelable {


    public ArrayList<GuestMobileNo> GuestReferredMobileNos;
    public String GuestId;
    public ReferFriendModel() {
    }

    public ReferFriendModel(String guestId,ArrayList<GuestMobileNo> GuestReferredMobileNoS) {

        this.GuestId=guestId;
        this.GuestReferredMobileNos=GuestReferredMobileNoS;
    }

    public String getGuestId() {
        return GuestId;
    }

    public void setGuestId(String GuestId1) {
        this.GuestId = GuestId1;
    }

    public ArrayList<GuestMobileNo> getGuestReferredMobileNos() {
        return GuestReferredMobileNos;
    }

    public void setGuestReferredMobileNos(ArrayList<GuestMobileNo> GuestReferredMobileNoS) {
        this.GuestReferredMobileNos = GuestReferredMobileNoS;
    }


    protected ReferFriendModel(Parcel in) {
        GuestReferredMobileNos = in.readArrayList(GuestMobileNo.class.getClassLoader());
        GuestId=in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(GuestReferredMobileNos);
        dest.writeString(GuestId);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReferFriendModel> CREATOR = new Creator<ReferFriendModel>() {
        @Override
        public ReferFriendModel createFromParcel(Parcel in) {
            return new ReferFriendModel(in);
        }

        @Override
        public ReferFriendModel[] newArray(int size) {
            return new ReferFriendModel[size];
        }
    };
}
