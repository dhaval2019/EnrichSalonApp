package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBundleServiceCenter implements Parcelable{

    public int PackageBundleStoreId;
    public int Id;
    public String CenterId;
    public String Name;
    public int CountryId;
    public String CountryCode;
    public String CountryName;
    public int CountryPhoneCode;
    public String CountryNationality;
    public int StateId;
    public String StateCode;
    public String StateName;
    public double GeoLatitude;
    public double GeoLongitude;
    public String Address1;
    public String Address2;
    public String City;
    public String ZipCode;

    protected PackageBundleServiceCenter(Parcel in) {
        PackageBundleStoreId = in.readInt();
        Id = in.readInt();
        CenterId = in.readString();
        Name = in.readString();
        CountryId = in.readInt();
        CountryCode = in.readString();
        CountryName = in.readString();
        CountryPhoneCode = in.readInt();
        CountryNationality = in.readString();
        StateId = in.readInt();
        StateCode = in.readString();
        StateName = in.readString();
        GeoLatitude = in.readDouble();
        GeoLongitude = in.readDouble();
        Address1 = in.readString();
        Address2 = in.readString();
        City = in.readString();
        ZipCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageBundleStoreId);
        dest.writeInt(Id);
        dest.writeString(CenterId);
        dest.writeString(Name);
        dest.writeInt(CountryId);
        dest.writeString(CountryCode);
        dest.writeString(CountryName);
        dest.writeInt(CountryPhoneCode);
        dest.writeString(CountryNationality);
        dest.writeInt(StateId);
        dest.writeString(StateCode);
        dest.writeString(StateName);
        dest.writeDouble(GeoLatitude);
        dest.writeDouble(GeoLongitude);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(ZipCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleServiceCenter> CREATOR = new Creator<PackageBundleServiceCenter>() {
        @Override
        public PackageBundleServiceCenter createFromParcel(Parcel in) {
            return new PackageBundleServiceCenter(in);
        }

        @Override
        public PackageBundleServiceCenter[] newArray(int size) {
            return new PackageBundleServiceCenter[size];
        }
    };
}
