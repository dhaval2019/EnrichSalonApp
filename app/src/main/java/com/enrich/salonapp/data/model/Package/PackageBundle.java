package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.TherapistModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// PARENT CLASS
public class PackageBundle extends CartItem implements Parcelable, Parent<BaseChildModel> {

    public static final int BUNDLE_ITEM_TYPE_SERVICE = 1;
    public static final int BUNDLE_ITEM_TYPE_PRODUCT = 2;
    public static final int BUNDLE_ITEM_TYPE_BRAND = 3;
    public static final int BUNDLE_ITEM_TYPE_CASHBACK = 4;

    public int Id;
    public String Name;
    public String SubTitle;
    public double Price;
    public int PackageId;
    public String BundleDependency;
    public int CityId;
    public int Quantity;
    public String ValidityBasedOn;
    public String ValidTillDate;
    public int ValidityDays;
    public List<PackageBundleCashback> Cashbacks;

    @SerializedName("PackageBundleService")
    public List<PackageBundleService> packageBundleService;

    @SerializedName("PackageBundleStore")
    public List<PackageBundleStore> packageBundleStore;

    @SerializedName("PackageBundleTag")
    public List<PackageBundleTag> packageBundleTag;

    @SerializedName("PackageBundleBrand")
    public List<PackageBundleBrand> packageBundleBrand;

    @SerializedName("PackageBundleProduct")
    public List<PackageBundleProduct> packageBundleProduct;

    @SerializedName("PackageBundleCity")
    public List<PackageBundleCity> packageBundleCity;

    public int Count;

    protected PackageBundle(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        SubTitle = in.readString();
        Price = in.readDouble();
        PackageId = in.readInt();
        BundleDependency = in.readString();
        CityId = in.readInt();
        Quantity = in.readInt();
        ValidityBasedOn = in.readString();
        ValidTillDate = in.readString();
        ValidityDays = in.readInt();
        Cashbacks = in.createTypedArrayList(PackageBundleCashback.CREATOR);
        packageBundleService = in.createTypedArrayList(PackageBundleService.CREATOR);
        packageBundleStore = in.createTypedArrayList(PackageBundleStore.CREATOR);
        packageBundleTag = in.createTypedArrayList(PackageBundleTag.CREATOR);
        packageBundleBrand = in.createTypedArrayList(PackageBundleBrand.CREATOR);
        packageBundleProduct = in.createTypedArrayList(PackageBundleProduct.CREATOR);
        packageBundleCity = in.createTypedArrayList(PackageBundleCity.CREATOR);
        Count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(SubTitle);
        dest.writeDouble(Price);
        dest.writeInt(PackageId);
        dest.writeString(BundleDependency);
        dest.writeInt(CityId);
        dest.writeInt(Quantity);
        dest.writeString(ValidityBasedOn);
        dest.writeString(ValidTillDate);
        dest.writeInt(ValidityDays);
        dest.writeTypedList(Cashbacks);
        dest.writeTypedList(packageBundleService);
        dest.writeTypedList(packageBundleStore);
        dest.writeTypedList(packageBundleTag);
        dest.writeTypedList(packageBundleBrand);
        dest.writeTypedList(packageBundleProduct);
        dest.writeTypedList(packageBundleCity);
        dest.writeInt(Count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundle> CREATOR = new Creator<PackageBundle>() {
        @Override
        public PackageBundle createFromParcel(Parcel in) {
            return new PackageBundle(in);
        }

        @Override
        public PackageBundle[] newArray(int size) {
            return new PackageBundle[size];
        }
    };

    @Override
    public List<BaseChildModel> getChildList() {
        if (!Cashbacks.isEmpty()) {
            ArrayList<BaseChildModel> baseChildModels = new ArrayList<>();
            baseChildModels.addAll(Cashbacks);
            return baseChildModels;
        } else if (!packageBundleService.isEmpty()) {
            ArrayList<BaseChildModel> baseChildModels = new ArrayList<>();
            baseChildModels.addAll(packageBundleService);
            return baseChildModels;
        } else if (!packageBundleProduct.isEmpty()) {
            ArrayList<BaseChildModel> baseChildModels = new ArrayList<>();
            baseChildModels.addAll(packageBundleProduct);
            return baseChildModels;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public double getPrice() {
        return Price;
    }

    @Override
    public int getCartItemType() {
        return CartItem.CART_TYPE_SUB_PACKAGE;
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
        return Id;
    }

    @Override
    public int getPaymentMode() {
        return 0;
    }

    @Override
    public TherapistModel getTherapistModel() {
        return null;
    }

    @Override
    public String getSlotTime() {
        return null;
    }

    @Override
    public int getPackageBundleItemCount() {
        if (!Cashbacks.isEmpty()) {
            return Cashbacks.size();
        } else if (!packageBundleService.isEmpty()) {
            return packageBundleService.size();
        } else if (!packageBundleProduct.isEmpty()) {
            return packageBundleProduct.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getPackageBundleItemType() {
        if (!Cashbacks.isEmpty()) {
            return BUNDLE_ITEM_TYPE_CASHBACK;
        } else if (!packageBundleService.isEmpty()) {
            return BUNDLE_ITEM_TYPE_SERVICE;
        } else if (!packageBundleProduct.isEmpty()) {
            return BUNDLE_ITEM_TYPE_PRODUCT;
        } else {
            return 0;
        }
    }
}
