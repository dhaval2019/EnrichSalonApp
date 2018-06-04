package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferModel implements Parcelable{

    public int OfferId;
    public String OfferTitle;
    public String OfferDescription;
    public String OfferImage;
    public String OfferBanner;
    public String CreatedOn;
    public String ValidTill;
    public String OfferType;
    public int OfferValue;
    public double OfferPercentage;
    public String ApplicableFor;
    public String StoreId;
    public boolean IsActive;
    public String Message;
    public String OfferImageURL;
    public String OfferBannerURL;

    protected OfferModel(Parcel in) {
        OfferId = in.readInt();
        OfferTitle = in.readString();
        OfferDescription = in.readString();
        OfferImage = in.readString();
        OfferBanner = in.readString();
        CreatedOn = in.readString();
        ValidTill = in.readString();
        OfferType = in.readString();
        OfferValue = in.readInt();
        OfferPercentage = in.readDouble();
        ApplicableFor = in.readString();
        StoreId = in.readString();
        IsActive = in.readByte() != 0;
        Message = in.readString();
        OfferImageURL = in.readString();
        OfferBannerURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OfferId);
        dest.writeString(OfferTitle);
        dest.writeString(OfferDescription);
        dest.writeString(OfferImage);
        dest.writeString(OfferBanner);
        dest.writeString(CreatedOn);
        dest.writeString(ValidTill);
        dest.writeString(OfferType);
        dest.writeInt(OfferValue);
        dest.writeDouble(OfferPercentage);
        dest.writeString(ApplicableFor);
        dest.writeString(StoreId);
        dest.writeByte((byte) (IsActive ? 1 : 0));
        dest.writeString(Message);
        dest.writeString(OfferImageURL);
        dest.writeString(OfferBannerURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferModel> CREATOR = new Creator<OfferModel>() {
        @Override
        public OfferModel createFromParcel(Parcel in) {
            return new OfferModel(in);
        }

        @Override
        public OfferModel[] newArray(int size) {
            return new OfferModel[size];
        }
    };
}
