package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestedTherapistModel implements Parcelable {

    public String Id;
    public String ShortName;
    public String NickName;
    public String FullName;
    public String Email;
    public String PhoneNumber;
    public PhoneModel MobilePhoneModel;
    public int Gender;
    public String FirstName;
    public String LastName;
    public boolean IsAvailable;
    public String VanityImageUrl;
    public int ScalingFactor;
    public int ScaledPrice;
    public int ServiceTime;
    public String DisplayName;

    protected RequestedTherapistModel(Parcel in) {
        Id = in.readString();
        ShortName = in.readString();
        NickName = in.readString();
        FullName = in.readString();
        Email = in.readString();
        PhoneNumber = in.readString();
        MobilePhoneModel = in.readParcelable(PhoneModel.class.getClassLoader());
        Gender = in.readInt();
        FirstName = in.readString();
        LastName = in.readString();
        IsAvailable = in.readByte() != 0;
        VanityImageUrl = in.readString();
        ScalingFactor = in.readInt();
        ScaledPrice = in.readInt();
        ServiceTime = in.readInt();
        DisplayName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(ShortName);
        dest.writeString(NickName);
        dest.writeString(FullName);
        dest.writeString(Email);
        dest.writeString(PhoneNumber);
        dest.writeParcelable(MobilePhoneModel, flags);
        dest.writeInt(Gender);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeByte((byte) (IsAvailable ? 1 : 0));
        dest.writeString(VanityImageUrl);
        dest.writeInt(ScalingFactor);
        dest.writeInt(ScaledPrice);
        dest.writeInt(ServiceTime);
        dest.writeString(DisplayName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestedTherapistModel> CREATOR = new Creator<RequestedTherapistModel>() {
        @Override
        public RequestedTherapistModel createFromParcel(Parcel in) {
            return new RequestedTherapistModel(in);
        }

        @Override
        public RequestedTherapistModel[] newArray(int size) {
            return new RequestedTherapistModel[size];
        }
    };
}
