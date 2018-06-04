package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GenericCartModel implements Serializable, Parcelable {

    public static final int CART_TYPE_LOOKS = 0;
    public static final int CART_TYPE_SUB_PACKAGE = 1;
    public static final int CART_TYPE_SERVICES = 2;
    public static final int CART_TYPE_PRODUCTS = 3;
    public static final int CART_TYPE_PACKAGE = 4;

    public String Id;
    public String ServiceId;
    public int Quantity;
    public String Name;
    public double Price;
    public int CartItemType;
    public int StoreId;
    public double Latitude;
    public double Longitude;
    public int RequestId;
    public int ResponseId;
    public int PaymentResponseId;
    public String DeliveryInformation;
    public String DeliveryPeriod;
    public String CustomerAddress;
    public TherapistModel therapistModel;
    public String SlotTime;

    protected boolean addedToCart;

    public boolean isMyPackage;
    public int packageBundleId;

    public int PaymentMode;

    public GenericCartModel() {
    }

    protected GenericCartModel(Parcel in) {
        Id = in.readString();
        ServiceId = in.readString();
        Quantity = in.readInt();
        Name = in.readString();
        Price = in.readDouble();
        CartItemType = in.readInt();
        StoreId = in.readInt();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        RequestId = in.readInt();
        ResponseId = in.readInt();
        packageBundleId = in.readInt();
        CustomerAddress = in.readString();
        therapistModel = in.readParcelable(TherapistModel.class.getClassLoader());
        SlotTime = in.readString();
    }

    public static final Creator<GenericCartModel> CREATOR = new Creator<GenericCartModel>() {
        @Override
        public GenericCartModel createFromParcel(Parcel in) {
            return new GenericCartModel(in);
        }

        @Override
        public GenericCartModel[] newArray(int size) {
            return new GenericCartModel[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getCartItemType() {
        return CartItemType;
    }

    public void setCartItemType(int cartItemType) {
        this.CartItemType = cartItemType;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public int getResponseId() {
        return ResponseId;
    }

    public void setResponseId(int responseId) {
        ResponseId = responseId;
    }

    public int getPaymentResponseId() {
        return PaymentResponseId;
    }

    public void setPaymentResponseId(int paymentResponseId) {
        PaymentResponseId = paymentResponseId;
    }

    public String getDeliveryInformation() {
        return DeliveryInformation;
    }

    public void setDeliveryInformation(String deliveryInformation) {
        DeliveryInformation = deliveryInformation;
    }

    public String getDeliveryPeriod() {
        return DeliveryPeriod;
    }

    public void setDeliveryPeriod(String deliveryPeriod) {
        DeliveryPeriod = deliveryPeriod;
    }

    public boolean isMyPackage() {
        return isMyPackage;
    }

    public void setMyPackage(boolean myPackage) {
        isMyPackage = myPackage;
    }

    public int getPackageBundleId() {
        return packageBundleId;
    }

    public void setPackageBundleId(int packageBundleId) {
        this.packageBundleId = packageBundleId;
    }

    public TherapistModel getTherapistModel() {
        return therapistModel;
    }

    public void setTherapistModel(TherapistModel therapistModel) {
        this.therapistModel = therapistModel;
    }

    public String getSlotTime() {
        return SlotTime;
    }

    public void setSlotTime(String slotTime) {
        SlotTime = slotTime;
    }

    public void removed() {
        addedToCart = false;
        setQuantity(0);
    }

    public void added() {
        addedToCart = true;
    }

    public int getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        PaymentMode = paymentMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeInt(Quantity);
        dest.writeString(Name);
        dest.writeDouble(Price);
        dest.writeInt(CartItemType);
        dest.writeInt(StoreId);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeInt(RequestId);
        dest.writeInt(ResponseId);
        dest.writeInt(packageBundleId);
        dest.writeString(CustomerAddress);
        dest.writeParcelable(therapistModel, flags);
        dest.writeString(SlotTime);
    }
}

