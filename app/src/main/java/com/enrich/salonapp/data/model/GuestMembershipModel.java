package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestMembershipModel implements Parcelable {

    public int Id;
    public String MembershipName;
    public String GuestMobileNo;
    public int MembershipPrice;
    public String ValidFrom;
    public String ExpiryDate;
    public String MembershipSoldBy;
    public String MembershipPaymentMethod;
    public String ExpirationDays;
    public String status;
    public String MembershipId;
    public String GuestId;
    public String CreatedDate;
    public String UpdatedDate;

    protected GuestMembershipModel(Parcel in) {
        Id = in.readInt();
        MembershipName = in.readString();
        GuestMobileNo = in.readString();
        MembershipPrice = in.readInt();
        ValidFrom = in.readString();
        ExpiryDate = in.readString();
        MembershipSoldBy = in.readString();
        MembershipPaymentMethod = in.readString();
        ExpirationDays = in.readString();
        status = in.readString();
        MembershipId = in.readString();
        GuestId = in.readString();
        CreatedDate = in.readString();
        UpdatedDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(MembershipName);
        dest.writeString(GuestMobileNo);
        dest.writeInt(MembershipPrice);
        dest.writeString(ValidFrom);
        dest.writeString(ExpiryDate);
        dest.writeString(MembershipSoldBy);
        dest.writeString(MembershipPaymentMethod);
        dest.writeString(ExpirationDays);
        dest.writeString(status);
        dest.writeString(MembershipId);
        dest.writeString(GuestId);
        dest.writeString(CreatedDate);
        dest.writeString(UpdatedDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestMembershipModel> CREATOR = new Creator<GuestMembershipModel>() {
        @Override
        public GuestMembershipModel createFromParcel(Parcel in) {
            return new GuestMembershipModel(in);
        }

        @Override
        public GuestMembershipModel[] newArray(int size) {
            return new GuestMembershipModel[size];
        }
    };
}
