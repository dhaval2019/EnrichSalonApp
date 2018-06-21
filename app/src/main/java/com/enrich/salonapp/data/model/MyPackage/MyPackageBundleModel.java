package com.enrich.salonapp.data.model.MyPackage;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.enrich.salonapp.data.model.Package.BaseChildModel;

import java.util.ArrayList;
import java.util.List;

public class MyPackageBundleModel implements Parcelable, Parent<BaseChildModel> {

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
    public String ValidTillDate;
    public int IntialQuantity;
    public int RemainingQuantity;
    public String PurchaseDate;
    public List<com.enrich.salonapp.data.model.Package.PackageBundleService> PackageBundleService;
    public List<com.enrich.salonapp.data.model.Package.PackageBundleProduct> PackageBundleProduct;

    protected MyPackageBundleModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        SubTitle = in.readString();
        Price = in.readDouble();
        PackageId = in.readInt();
        BundleDependency = in.readString();
        CityId = in.readInt();
        Quantity = in.readInt();
        ValidTillDate = in.readString();
        IntialQuantity = in.readInt();
        RemainingQuantity = in.readInt();
        PurchaseDate = in.readString();
        PackageBundleService = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleService.CREATOR);
        PackageBundleProduct = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleProduct.CREATOR);
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
        dest.writeString(ValidTillDate);
        dest.writeInt(IntialQuantity);
        dest.writeInt(RemainingQuantity);
        dest.writeString(PurchaseDate);
        dest.writeTypedList(PackageBundleService);
        dest.writeTypedList(PackageBundleProduct);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyPackageBundleModel> CREATOR = new Creator<MyPackageBundleModel>() {
        @Override
        public MyPackageBundleModel createFromParcel(Parcel in) {
            return new MyPackageBundleModel(in);
        }

        @Override
        public MyPackageBundleModel[] newArray(int size) {
            return new MyPackageBundleModel[size];
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
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}