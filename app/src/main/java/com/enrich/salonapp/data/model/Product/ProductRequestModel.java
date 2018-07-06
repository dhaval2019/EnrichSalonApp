package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProductRequestModel implements Parcelable {

    public int Index;
    public ArrayList<Integer> ProductCategoryIds = new ArrayList<>();
    public ArrayList<Integer> BrandIds = new ArrayList<>();
    public int Sort;
    public double MinAmount;
    public double MaxAmount;
    public int Gender;
    public ArrayList<Integer> ProductSubCategoryIds = new ArrayList<>();

    public ProductRequestModel() {
    }

    protected ProductRequestModel(Parcel in) {
        Index = in.readInt();
        Sort = in.readInt();
        MinAmount = in.readDouble();
        MaxAmount = in.readDouble();
        Gender = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Index);
        dest.writeInt(Sort);
        dest.writeDouble(MinAmount);
        dest.writeDouble(MaxAmount);
        dest.writeInt(Gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductRequestModel> CREATOR = new Creator<ProductRequestModel>() {
        @Override
        public ProductRequestModel createFromParcel(Parcel in) {
            return new ProductRequestModel(in);
        }

        @Override
        public ProductRequestModel[] newArray(int size) {
            return new ProductRequestModel[size];
        }
    };

    public boolean isFilterApplied() {
        if (!ProductCategoryIds.isEmpty() || !BrandIds.isEmpty() || !ProductSubCategoryIds.isEmpty()) {
            return true;
        }
        return false;
    }
}
