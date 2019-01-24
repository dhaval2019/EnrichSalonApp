package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CenterViewModel implements Parcelable {

    public String Id;
    public CountryModel Country;
    public StateModel State;
    public String Name;
    public double GeoLatitude;
    public double GeoLongitude;
    public double Distance;
    public String Address1;
    public String Address2;
    public String City;
    public String ZipCode;
    public PhoneModel Phone1;
    public PhoneModel Phone2;
    public String Email;
    public String CenterInfo;
    public String ServiceTaxNo;
    public String TIN;
    public String VAT;
    public String CST;
    public String CanBook;
    public String CenterCode;
    public int CenterType;
    public ArrayList<WorkingHoursModel> working_hours;

    protected CenterViewModel(Parcel in) {
        Id = in.readString();
        Country = in.readParcelable(CountryModel.class.getClassLoader());
        State = in.readParcelable(StateModel.class.getClassLoader());
        Name = in.readString();
        GeoLatitude = in.readDouble();
        GeoLongitude = in.readDouble();
        Distance = in.readDouble();
        Address1 = in.readString();
        Address2 = in.readString();
        City = in.readString();
        ZipCode = in.readString();
        Phone1 = in.readParcelable(PhoneModel.class.getClassLoader());
        Phone2 = in.readParcelable(PhoneModel.class.getClassLoader());
        Email = in.readString();
        CenterInfo = in.readString();
        ServiceTaxNo = in.readString();
        TIN = in.readString();
        VAT = in.readString();
        CST = in.readString();
        CanBook = in.readString();
        CenterCode = in.readString();
        CenterType = in.readInt();
        working_hours = in.createTypedArrayList(WorkingHoursModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeParcelable(Country, flags);
        dest.writeParcelable(State, flags);
        dest.writeString(Name);
        dest.writeDouble(GeoLatitude);
        dest.writeDouble(GeoLongitude);
        dest.writeDouble(Distance);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(ZipCode);
        dest.writeParcelable(Phone1, flags);
        dest.writeParcelable(Phone2, flags);
        dest.writeString(Email);
        dest.writeString(CenterInfo);
        dest.writeString(ServiceTaxNo);
        dest.writeString(TIN);
        dest.writeString(VAT);
        dest.writeString(CST);
        dest.writeString(CanBook);
        dest.writeString(CenterCode);
        dest.writeInt(CenterType);
        dest.writeTypedList(working_hours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CenterViewModel> CREATOR = new Creator<CenterViewModel>() {
        @Override
        public CenterViewModel createFromParcel(Parcel in) {
            return new CenterViewModel(in);
        }

        @Override
        public CenterViewModel[] newArray(int size) {
            return new CenterViewModel[size];
        }
    };
}
