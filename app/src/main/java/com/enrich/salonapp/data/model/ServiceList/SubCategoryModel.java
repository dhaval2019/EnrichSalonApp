package com.enrich.salonapp.data.model.ServiceList;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryModel implements Parcelable, Parent<ServiceViewModel> {

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
    public ArrayList<ServiceViewModel> ChildServices = new ArrayList<>();

    public SubCategoryModel(int id, String subCategoryId, String subCategoryOrganizationId, String imageUrl, String name, String parentCategoryId, String description, int sortOrder, String code, int gender, ArrayList<ServiceViewModel> childServices) {
        Id = id;
        SubCategoryId = subCategoryId;
        SubCategoryOrganizationId = subCategoryOrganizationId;
        ImageUrl = imageUrl;
        Name = name;
        ParentCategoryId = parentCategoryId;
        Description = description;
        SortOrder = sortOrder;
        Code = code;
        Gender = gender;
        ChildServices = childServices;
    }

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
        ChildServices = in.createTypedArrayList(ServiceViewModel.CREATOR);
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
        dest.writeTypedList(ChildServices);
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

    @Override
    public List<ServiceViewModel> getChildList() {
        return ChildServices;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
