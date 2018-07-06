package com.enrich.salonapp.data.model.ServiceList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubCategoryModel implements Parcelable {

    public int Id;
    @SerializedName("CategoryId")
    public String SubCategoryId;
    @SerializedName("CategoryOrganizationId")
    public String SubCategoryOrganizationId;
    public String ImageUrl;
    public String Name;
    public String ParentCategoryId;
    public String Description;
    public int SortOrder;
    public String Code;
    public int Gender;

    protected SubCategoryModel(Parcel in) {
        Id = in.readInt();
        SubCategoryId = in.readString();
        SubCategoryOrganizationId = in.readString();
        ImageUrl = in.readString();
        Name = in.readString();
        ParentCategoryId = in.readString();
        Description = in.readString();
        SortOrder = in.readInt();
        Code = in.readString();
        Gender = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(SubCategoryId);
        dest.writeString(SubCategoryOrganizationId);
        dest.writeString(ImageUrl);
        dest.writeString(Name);
        dest.writeString(ParentCategoryId);
        dest.writeString(Description);
        dest.writeInt(SortOrder);
        dest.writeString(Code);
        dest.writeInt(Gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryModel> CREATOR = new Creator<SubCategoryModel>() {
        @Override
        public SubCategoryModel createFromParcel(Parcel in) {
            return new SubCategoryModel(in);
        }

        @Override
        public SubCategoryModel[] newArray(int size) {
            return new SubCategoryModel[size];
        }
    };
}
