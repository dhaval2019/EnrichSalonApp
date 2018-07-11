package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {

    public int Id;
    public String CategoryId;
    public String CategoryOrganizationId;
    public String Name;
    public String ParentCategoryId;
    public String Description;
    public int SortOrder;
    public String Code;
    public ImagePathsModel ImageUrl;

    protected CategoryModel(Parcel in) {
        Id = in.readInt();
        CategoryId = in.readString();
        CategoryOrganizationId = in.readString();
        Name = in.readString();
        ParentCategoryId = in.readString();
        Description = in.readString();
        SortOrder = in.readInt();
        Code = in.readString();
        ImageUrl = in.readParcelable(ImagePathsModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(CategoryId);
        dest.writeString(CategoryOrganizationId);
        dest.writeString(Name);
        dest.writeString(ParentCategoryId);
        dest.writeString(Description);
        dest.writeInt(SortOrder);
        dest.writeString(Code);
        dest.writeParcelable(ImageUrl, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    @Override
    public String toString() {
        return Name;
    }
}
