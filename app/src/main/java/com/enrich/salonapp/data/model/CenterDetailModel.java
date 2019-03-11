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
    public int CenterType;
    public CountryModel Country;
    public StateModel State;
    public String City;
    public String ZipCode;

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
        CenterType = in.readInt();
        Country = in.readParcelable(CountryModel.class.getClassLoader());
        State = in.readParcelable(StateModel.class.getClassLoader());
        City = in.readString();
        ZipCode = in.readString();
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
        dest.writeInt(CenterType);
        dest.writeParcelable(Country, flags);
        dest.writeParcelable(State, flags);
        dest.writeString(City);
        dest.writeString(ZipCode);
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
