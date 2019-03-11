package com.enrich.salonapp.data.model;

import android.os.Parcelable;

import com.enrich.salonapp.data.model.Package.BaseChildModel;

import java.util.List;

public abstract class CartItem implements Parcelable {
    public static final int CART_TYPE_LOOKS = 0;
    public static final int CART_TYPE_SUB_PACKAGE = 1;
    public static final int CART_TYPE_SERVICES = 2;
    public static final int CART_TYPE_PRODUCTS = 3;
    public static final int CART_TYPE_PACKAGE = 4;

    protected int quantity = 1;
    protected boolean addedToCart;

    public abstract int getId();

    public abstract String getServiceId();

    public boolean isAdded() {
        return addedToCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void added() {
        addedToCart = true;
    }

    public void removed() {
        addedToCart = false;
        setQuantity(0);
    }

    public abstract String getName();

    public abstract String getCategoryName();

    public abstract String getSubCategoryName();

    public abstract double getPrice();

    public abstract double getMembershipPrice();

    public abstract int getCartItemType();

    public abstract int getStoreId();

    public abstract double getLatitude();

    public abstract double getLongitude();

//    public abstract NearByStoresModel getNearByStoresModel();

    public abstract String getDeliveryPeriod();

    public abstract String getDeliveryInformation();

    public abstract boolean isMyPackage();

    public abstract int getPackageBundleId();

    public abstract int getPaymentMode();

    public abstract TherapistModel getTherapistModel();

    public abstract String getSlotTime();

    public abstract int getPackageBundleItemCount();

    public abstract int getPackageBundleItemType();
}
