package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.Product.OfferProductBrandTagsModel;
import com.enrich.salonapp.data.model.Product.OfferProductCategoryTagsModel;
import com.enrich.salonapp.data.model.Product.OfferProductSubCategoryTagsModel;

import java.util.ArrayList;

public class OfferModel implements Parcelable {

    public ArrayList<OfferProductCategoryTagsModel> OfferProductCategoryTags;
    public ArrayList<OfferProductSubCategoryTagsModel> OfferProductSubCategoryTags;
    public ArrayList<OfferProductBrandTagsModel> OfferProductBrandTags;
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
    public int CallToAction;
    public int Position;
    public int Orders;
    public int PackageId;
    public int ProductId;
    public String CenterId;
    public boolean IsActive;
    public String ToastMessage;
    public String ToastImage;
    public boolean IsToastOffer;
    public boolean IsTimerEnable;
    public boolean IsHome;
    public String BlogRedirectURL;
    public String OfferImageURL;
    public String OfferBannerURL;
    public String ToastImageURL;
    public int ServiceCategory;
    public String ServiceCategoryName;
    public int Gender;

    public OfferModel() {
    }

    protected OfferModel(Parcel in) {
        OfferProductCategoryTags = in.createTypedArrayList(OfferProductCategoryTagsModel.CREATOR);
        OfferProductSubCategoryTags = in.createTypedArrayList(OfferProductSubCategoryTagsModel.CREATOR);
        OfferProductBrandTags = in.createTypedArrayList(OfferProductBrandTagsModel.CREATOR);
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
        CallToAction = in.readInt();
        Position = in.readInt();
        Orders = in.readInt();
        PackageId = in.readInt();
        ProductId = in.readInt();
        CenterId = in.readString();
        IsActive = in.readByte() != 0;
        ToastMessage = in.readString();
        ToastImage = in.readString();
        IsToastOffer = in.readByte() != 0;
        IsTimerEnable = in.readByte() != 0;
        IsHome = in.readByte() != 0;
        BlogRedirectURL = in.readString();
        OfferImageURL = in.readString();
        OfferBannerURL = in.readString();
        ToastImageURL = in.readString();
        ServiceCategory = in.readInt();
        ServiceCategoryName = in.readString();
        Gender = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(OfferProductCategoryTags);
        dest.writeTypedList(OfferProductSubCategoryTags);
        dest.writeTypedList(OfferProductBrandTags);
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
        dest.writeInt(CallToAction);
        dest.writeInt(Position);
        dest.writeInt(Orders);
        dest.writeInt(PackageId);
        dest.writeInt(ProductId);
        dest.writeString(CenterId);
        dest.writeByte((byte) (IsActive ? 1 : 0));
        dest.writeString(ToastMessage);
        dest.writeString(ToastImage);
        dest.writeByte((byte) (IsToastOffer ? 1 : 0));
        dest.writeByte((byte) (IsTimerEnable ? 1 : 0));
        dest.writeByte((byte) (IsHome ? 1 : 0));
        dest.writeString(BlogRedirectURL);
        dest.writeString(OfferImageURL);
        dest.writeString(OfferBannerURL);
        dest.writeString(ToastImageURL);


        dest.writeInt(ServiceCategory);
        dest.writeString(ServiceCategoryName);
        dest.writeInt(Gender);
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
