package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.TherapistModel;

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
    public List<PackageBundleService> PackageBundleService;
    public ArrayList<PackageBundleStore> PackageBundleStore;
    public ArrayList<PackageBundleTag> PackageBundleTag;
    public ArrayList<PackageBundleBrand> PackageBundleBrand;
    public ArrayList<PackageBundleProduct> PackageBundleProduct;
    public ArrayList<PackageBundleCity> PackageBundleCity;
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
        PackageBundleService = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleService.CREATOR);
        PackageBundleStore = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleStore.CREATOR);
        PackageBundleTag = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleTag.CREATOR);
        PackageBundleBrand = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleBrand.CREATOR);
        PackageBundleProduct = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleProduct.CREATOR);
        PackageBundleCity = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleCity.CREATOR);
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
        dest.writeTypedList(PackageBundleService);
        dest.writeTypedList(PackageBundleStore);
        dest.writeTypedList(PackageBundleTag);
        dest.writeTypedList(PackageBundleBrand);
        dest.writeTypedList(PackageBundleProduct);
        dest.writeTypedList(PackageBundleCity);
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
        if (!PackageBundleService.isEmpty()) {
            List<BaseChildModel> baseChildModels = new ArrayList<>();
            baseChildModels.addAll(PackageBundleService);
            return baseChildModels;
        } else if (!PackageBundleProduct.isEmpty()) {
            List<BaseChildModel> baseChildModels = new ArrayList<>();
            baseChildModels.addAll(PackageBundleProduct);
            return baseChildModels;
        } else {
            return null;
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
        if (!PackageBundleService.isEmpty()) {
            return PackageBundleService.size();
        } else if (!PackageBundleProduct.isEmpty()) {
            return PackageBundleProduct.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getPackageBundleItemType() {
        if (!PackageBundleService.isEmpty()) {
            return BUNDLE_ITEM_TYPE_SERVICE;
        } else if (!PackageBundleProduct.isEmpty()) {
            return BUNDLE_ITEM_TYPE_PRODUCT;
        } else {
            return 0;
        }
    }
}
