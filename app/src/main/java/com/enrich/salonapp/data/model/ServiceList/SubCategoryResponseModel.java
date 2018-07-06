package com.enrich.salonapp.data.model.ServiceList;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.CategoryModel;

import java.util.ArrayList;

public class SubCategoryResponseModel implements Parcelable {

    public CategoryModel Category;
    public ArrayList<SubCategoryModel> SubCategories;

    protected SubCategoryResponseModel(Parcel in) {
        Category = in.readParcelable(CategoryModel.class.getClassLoader());
        SubCategories = in.createTypedArrayList(SubCategoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Category, flags);
        dest.writeTypedList(SubCategories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryResponseModel> CREATOR = new Creator<SubCategoryResponseModel>() {
        @Override
        public SubCategoryResponseModel createFromParcel(Parcel in) {
            return new SubCategoryResponseModel(in);
        }

        @Override
        public SubCategoryResponseModel[] newArray(int size) {
            return new SubCategoryResponseModel[size];
        }
    };
}
