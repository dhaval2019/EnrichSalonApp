package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServiceViewModel extends CartItem implements Parcelable {

    @SerializedName("Id")
    public String id;

    @SerializedName("ServiceId")
    public int ServiceId;

    @SerializedName("Name")
    public String name;

    @SerializedName("Description")
    public String description;

    @SerializedName("HTMLDescription")
    public String hTMLDescription;

    @SerializedName("Duration")
    public int duration;

    @SerializedName("PriceText")
    public String priceText;

    @SerializedName("IncludesTax")
    public boolean includesTax;

    @SerializedName("ImagePaths")
    public ImagePathsModel imagePaths;

    @SerializedName("HasVariant")
    public boolean hasVariant;

    @SerializedName("IsVariant")
    public boolean isVariant;

    @SerializedName("VideoUrl")
    public String videoUrl;

    @SerializedName("CanBook")
    public boolean canBook;

    @SerializedName("ShowPrice")
    public boolean showPrice;

    @SerializedName("SortOrder")
    public int sortOrder;

    @SerializedName("RecoveryTime")
    public int recoveryTime;

    @SerializedName("Code")
    public String code;

    @SerializedName("Price")
    public PriceModel price;

    @SerializedName("ServiceType")
    public String ServiceType;

    public boolean IsAdded;

    public String SlotTime;

    @SerializedName("Therapist")
    public TherapistModel therapist;

    @SerializedName("ParentServiceId")
    public String ParentServiceId;

    @SerializedName("CategoryId")
    public String CategoryId;

    @SerializedName("CategoryName")
    public String CategoryName;

    @SerializedName("ServiceTag")
    public ArrayList<ServiceTagModel> ServiceTag;

    protected ServiceViewModel(Parcel in) {
//        super(in);
        id = in.readString();
        ServiceId = in.readInt();
        name = in.readString();
        description = in.readString();
        hTMLDescription = in.readString();
        duration = in.readInt();
        priceText = in.readString();
        includesTax = in.readByte() != 0;
        imagePaths = in.readParcelable(ImagePathsModel.class.getClassLoader());
        hasVariant = in.readByte() != 0;
        isVariant = in.readByte() != 0;
        videoUrl = in.readString();
        canBook = in.readByte() != 0;
        showPrice = in.readByte() != 0;
        sortOrder = in.readInt();
        recoveryTime = in.readInt();
        code = in.readString();
        price = in.readParcelable(PriceModel.class.getClassLoader());
        ServiceType = in.readString();
        IsAdded = in.readByte() != 0;
        SlotTime = in.readString();
        therapist = in.readParcelable(TherapistModel.class.getClassLoader());
        ParentServiceId = in.readString();
        CategoryId = in.readString();
        CategoryName = in.readString();
        ServiceTag = in.createTypedArrayList(ServiceTagModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeInt(ServiceId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(hTMLDescription);
        dest.writeInt(duration);
        dest.writeString(priceText);
        dest.writeByte((byte) (includesTax ? 1 : 0));
        dest.writeParcelable(imagePaths, flags);
        dest.writeByte((byte) (hasVariant ? 1 : 0));
        dest.writeByte((byte) (isVariant ? 1 : 0));
        dest.writeString(videoUrl);
        dest.writeByte((byte) (canBook ? 1 : 0));
        dest.writeByte((byte) (showPrice ? 1 : 0));
        dest.writeInt(sortOrder);
        dest.writeInt(recoveryTime);
        dest.writeString(code);
        dest.writeParcelable(price, flags);
        dest.writeString(ServiceType);
        dest.writeByte((byte) (IsAdded ? 1 : 0));
        dest.writeString(SlotTime);
        dest.writeParcelable(therapist, flags);
        dest.writeString(ParentServiceId);
        dest.writeString(CategoryId);
        dest.writeString(CategoryName);
        dest.writeTypedList(ServiceTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceViewModel> CREATOR = new Creator<ServiceViewModel>() {
        @Override
        public ServiceViewModel createFromParcel(Parcel in) {
            return new ServiceViewModel(in);
        }

        @Override
        public ServiceViewModel[] newArray(int size) {
            return new ServiceViewModel[size];
        }
    };

    @Override
    public int getId() {
        return ServiceId;
    }

    @Override
    public String getServiceId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price.sales;
    }

    @Override
    public int getCartItemType() {
        return CART_TYPE_SERVICES;
    }

    @Override
    public int getStoreId() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

//    @Override
//    public NearByStoresModel getNearByStoresModel() {
//        return null;
//    }

    @Override
    public String getDeliveryPeriod() {
        return null;
    }

    @Override
    public String getDeliveryInformation() {
        return null;
    }

    @Override
    public boolean isMyPackage() {
        return false;
    }

    @Override
    public int getPackageBundleId() {
        return 0;
    }

    @Override
    public int getPaymentMode() {
        return 0;
    }

    @Override
    public TherapistModel getTherapistModel() {
        return therapist;
    }

    @Override
    public String getSlotTime() {
        return SlotTime;
    }

    @Override
    public int getPackageBundleItemCount() {
        return 0;
    }

    @Override
    public int getPackageBundleItemType() {
        return 0;
    }
}

