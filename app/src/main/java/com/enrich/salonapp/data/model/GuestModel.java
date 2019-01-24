package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GuestModel implements Parcelable {

    public String Id;
    public String Code;
    public String FirstName;
    public String LastName;
    public String Email;
    public String MobileNumber;
    public PhoneModel MobilePhoneModel;
    public String HomePhone;
    public PhoneModel HomePhoneModel;
    public String WorkPhone;
    public PhoneModel WorkPhoneModel;
    public int Gender;
    public String DateOfBirth;
    public String AnniversaryDate;
    public String Address1;
    public String Address2;
    public String City;
    public String PostalCode;
    public String State;
    public String Country;
    public String Nationality;
    public String ReferralSource;
    public String ReferredGuestId;
    public boolean ReceiveTransactionalSms;
    public String ReceiveMarketingEmail;
    public String ReceiveMarketingSms;
    public String CreationDate;
    public String LastUpdated;
    public String MergeIntoCode;
    public String MergeIntoGuestId;
    public String CenterId;
    public String CenterCode;
    public String CenterName;
    public String GuestIndicator;
    public int IsMember;
    public String DOB_IncompleteYear;
    public String Password;
    public String UserName;
    public String FacebookUserId;
    public String OldPassword;
    public ArrayList<AddressModel> GuestAddress;
    public ArrayList<GuestMembershipModel> MembershipModel;

    public GuestModel() {
    }

    protected GuestModel(Parcel in) {
        Id = in.readString();
        Code = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Email = in.readString();
        MobileNumber = in.readString();
        MobilePhoneModel = in.readParcelable(PhoneModel.class.getClassLoader());
        HomePhone = in.readString();
        HomePhoneModel = in.readParcelable(PhoneModel.class.getClassLoader());
        WorkPhone = in.readString();
        WorkPhoneModel = in.readParcelable(PhoneModel.class.getClassLoader());
        Gender = in.readInt();
        DateOfBirth = in.readString();
        AnniversaryDate = in.readString();
        Address1 = in.readString();
        Address2 = in.readString();
        City = in.readString();
        PostalCode = in.readString();
        State = in.readString();
        Country = in.readString();
        Nationality = in.readString();
        ReferralSource = in.readString();
        ReferredGuestId = in.readString();
        ReceiveTransactionalSms = in.readByte() != 0;
        ReceiveMarketingEmail = in.readString();
        ReceiveMarketingSms = in.readString();
        CreationDate = in.readString();
        LastUpdated = in.readString();
        MergeIntoCode = in.readString();
        MergeIntoGuestId = in.readString();
        CenterId = in.readString();
        CenterCode = in.readString();
        CenterName = in.readString();
        GuestIndicator = in.readString();
        IsMember = in.readInt();
        DOB_IncompleteYear = in.readString();
        Password = in.readString();
        UserName = in.readString();
        FacebookUserId = in.readString();
        OldPassword = in.readString();
        GuestAddress = in.createTypedArrayList(AddressModel.CREATOR);
        MembershipModel = in.createTypedArrayList(GuestMembershipModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Code);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Email);
        dest.writeString(MobileNumber);
        dest.writeParcelable(MobilePhoneModel, flags);
        dest.writeString(HomePhone);
        dest.writeParcelable(HomePhoneModel, flags);
        dest.writeString(WorkPhone);
        dest.writeParcelable(WorkPhoneModel, flags);
        dest.writeInt(Gender);
        dest.writeString(DateOfBirth);
        dest.writeString(AnniversaryDate);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(PostalCode);
        dest.writeString(State);
        dest.writeString(Country);
        dest.writeString(Nationality);
        dest.writeString(ReferralSource);
        dest.writeString(ReferredGuestId);
        dest.writeByte((byte) (ReceiveTransactionalSms ? 1 : 0));
        dest.writeString(ReceiveMarketingEmail);
        dest.writeString(ReceiveMarketingSms);
        dest.writeString(CreationDate);
        dest.writeString(LastUpdated);
        dest.writeString(MergeIntoCode);
        dest.writeString(MergeIntoGuestId);
        dest.writeString(CenterId);
        dest.writeString(CenterCode);
        dest.writeString(CenterName);
        dest.writeString(GuestIndicator);
        dest.writeInt(IsMember);
        dest.writeString(DOB_IncompleteYear);
        dest.writeString(Password);
        dest.writeString(UserName);
        dest.writeString(FacebookUserId);
        dest.writeString(OldPassword);
        dest.writeTypedList(GuestAddress);
        dest.writeTypedList(MembershipModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestModel> CREATOR = new Creator<GuestModel>() {
        @Override
        public GuestModel createFromParcel(Parcel in) {
            return new GuestModel(in);
        }

        @Override
        public GuestModel[] newArray(int size) {
            return new GuestModel[size];
        }
    };
}
