package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.TherapistModel;

public class ProductModel extends CartItem implements Parcelable {

    public int ProductId;
    public String ProductTitle;
    public String SubTitle;
    public String ProductDescription;
    public int ProductAmount;
    public int SaleLimit;
    public ProductCashback ProductCashBacks;
    public int PaymentMode;
    public int Gender;
    public String ProductCategoryId;
    public double OriginalPrice;
    public String DeliveryInformation;
    public int DeliveryPeriod;
    public String ImageURL;
    public int Sold;
    public BrandModel Brand;
    public ProductSubCategoryModel ProductSubCategory;
    public int count;

    protected ProductModel(Parcel in) {
        ProductId = in.readInt();
        ProductTitle = in.readString();
        SubTitle = in.readString();
        ProductDescription = in.readString();
        ProductAmount = in.readInt();
        SaleLimit = in.readInt();
        ProductCashBacks = in.readParcelable(ProductCashback.class.getClassLoader());
        PaymentMode = in.readInt();
        Gender = in.readInt();
        ProductCategoryId = in.readString();
        OriginalPrice = in.readDouble();
        DeliveryInformation = in.readString();
        DeliveryPeriod = in.readInt();
        ImageURL = in.readString();
        Sold = in.readInt();
        Brand = in.readParcelable(BrandModel.class.getClassLoader());
        ProductSubCategory = in.readParcelable(ProductSubCategoryModel.class.getClassLoader());
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ProductId);
        dest.writeString(ProductTitle);
        dest.writeString(SubTitle);
        dest.writeString(ProductDescription);
        dest.writeInt(ProductAmount);
        dest.writeInt(SaleLimit);
        dest.writeParcelable(ProductCashBacks, flags);
        dest.writeInt(PaymentMode);
        dest.writeInt(Gender);
        dest.writeString(ProductCategoryId);
        dest.writeDouble(OriginalPrice);
        dest.writeString(DeliveryInformation);
        dest.writeInt(DeliveryPeriod);
        dest.writeString(ImageURL);
        dest.writeInt(Sold);
        dest.writeParcelable(Brand, flags);
        dest.writeParcelable(ProductSubCategory, flags);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int getId() {
        return ProductId;
    }

    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public String getName() {
        return ProductTitle;
    }

    @Override
    public double getPrice() {
        return (double) ProductAmount;
    }

    @Override
    public int getCartItemType() {
        return CartItem.CART_TYPE_PRODUCTS;
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
        return "" + DeliveryPeriod;
    }

    @Override
    public String getDeliveryInformation() {
        return DeliveryInformation;
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
        return PaymentMode;
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
        return 0;
    }

    @Override
    public int getPackageBundleItemType() {
        return 0;
    }
}