package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CenterDetailModel implements Parcelable {

    public String Id;
    public String Name;
    public String Address;
    public String Phone;
    public int TimeZoneId;
    public int CurrencyId;
    public String OrganizationName;
    public String OrganizationLogoUrl;
    public String Email;

    public CenterDetailModel() {
    }

    protected CenterDetailModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Address = in.readString();
        Phone = in.readString();
        TimeZoneId = in.readInt();
        CurrencyId = in.readInt();
        OrganizationName = in.readString();
        OrganizationLogoUrl = in.readString();
        Email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Address);
        dest.writeString(Phone);
        dest.writeInt(TimeZoneId);
        dest.writeInt(CurrencyId);
        dest.writeString(OrganizationName);
        dest.writeString(OrganizationLogoUrl);
        dest.writeString(Email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CenterDetailModel> CREATOR = new Creator<CenterDetailModel>() {
        @Override
        public CenterDetailModel createFromParcel(Parcel in) {
            return new CenterDetailModel(in);
        }

        @Override
        public CenterDetailModel[] newArray(int size) {
            return new CenterDetailModel[size];
        }
    };
}
