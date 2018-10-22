package com.enrich.salonapp.data.model.CreateOrder;

import com.enrich.salonapp.data.model.AddressModel;

import java.util.ArrayList;

public class CreateOrderRequestModel {

    private Boolean ApplyCredits;
    private String PromoCode;
    private ArrayList<CreateOrderPackageBundleModel> PackageIds;
    private ArrayList<CreateOrderServiceModel> ServiceIds;
    private ArrayList<CreateOrderProductModel> ProductIds;
    private Boolean IsCOD;
    private String GuestId;
    private String GuestName;
    private int platform;
    private String SlotBookdate;
    private AddressModel GuestAddress;

    public Boolean getApplyCredits() {
        return ApplyCredits;
    }

    public void setApplyCredits(Boolean applyCredits) {
        ApplyCredits = applyCredits;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    public ArrayList<CreateOrderPackageBundleModel> getPackageIds() {
        return PackageIds;
    }

    public void setPackageIds(ArrayList<CreateOrderPackageBundleModel> packageIds) {
        PackageIds = packageIds;
    }

    public ArrayList<CreateOrderServiceModel> getServiceIds() {
        return ServiceIds;
    }

    public void setServiceIds(ArrayList<CreateOrderServiceModel> serviceIds) {
        ServiceIds = serviceIds;
    }

    public ArrayList<CreateOrderProductModel> getProductIds() {
        return ProductIds;
    }

    public void setProductIds(ArrayList<CreateOrderProductModel> productIds) {
        ProductIds = productIds;
    }

    public Boolean getCOD() {
        return IsCOD;
    }

    public void setCOD(Boolean COD) {
        IsCOD = COD;
    }

    public String getGuestId() {
        return GuestId;
    }

    public void setGuestId(String guestId) {
        GuestId = guestId;
    }

    public String getGuestName() {
        return GuestName;
    }

    public void setGuestName(String guestName) {
        GuestName = guestName;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getSlotBookdate() {
        return SlotBookdate;
    }

    public void setSlotBookdate(String slotBookdate) {
        SlotBookdate = slotBookdate;
    }

    public AddressModel getGuestAddress() {
        return GuestAddress;
    }

    public void setGuestAddress(AddressModel guestAddress) {
        GuestAddress = guestAddress;
    }
}
