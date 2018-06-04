package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ParentServiceViewModel implements Parent<ServiceViewModel>, Parcelable {

    @SerializedName("Id")
    public String id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Description")
    public String Description;

    @SerializedName("CategoryId")
    public String CategoryId;

    @SerializedName("ServiceType")
    public String ServiceType;

    @SerializedName("ChildServices")
    public ArrayList<ServiceViewModel> ChildServices;

    public ParentServiceViewModel() {
    }

    public ParentServiceViewModel(String id, String name, String description, String categoryId, String serviceType, ArrayList<ServiceViewModel> childServices) {
        this.id = id;
        this.name = name;
        Description = description;
        CategoryId = categoryId;
        ServiceType = serviceType;
        ChildServices = childServices;
    }

    protected ParentServiceViewModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        Description = in.readString();
        CategoryId = in.readString();
        ServiceType = in.readString();
        ChildServices = in.createTypedArrayList(ServiceViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(Description);
        dest.writeString(CategoryId);
        dest.writeString(ServiceType);
        dest.writeTypedList(ChildServices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParentServiceViewModel> CREATOR = new Creator<ParentServiceViewModel>() {
        @Override
        public ParentServiceViewModel createFromParcel(Parcel in) {
            return new ParentServiceViewModel(in);
        }

        @Override
        public ParentServiceViewModel[] newArray(int size) {
            return new ParentServiceViewModel[size];
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

