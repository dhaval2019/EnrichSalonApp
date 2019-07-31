package com.enrich.salonapp.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SelectFriendModel implements Parcelable {
    private Uri photo;
    private String name;
    private String mobileNo;
    private boolean cbSelect;

    public SelectFriendModel() {
    }

    public SelectFriendModel(Uri userImage,String userName, String userMobileNo, boolean cbIsSelect) {
        this.photo = userImage;
        this.name = userName;
        this.mobileNo = userMobileNo;
        this.cbSelect=cbIsSelect;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri userImage) {
        this.photo = userImage;
    }

    public String getMobNo() {
        return mobileNo;
    }

    public void setMobileNo(String userMobileNo) {
        this.mobileNo = userMobileNo;
    }
    public boolean getIsSelect() {
        return cbSelect;
    }

    public void setIsSelect(boolean cbIsSelect) {
        this.cbSelect = cbIsSelect;
    }
    protected SelectFriendModel(Parcel in) {
        photo = (Uri) in.readValue(null);
        name = in.readString();
        mobileNo = in.readString();
        cbSelect = (boolean) in.readValue(null);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(photo);
        dest.writeString(name);
        dest.writeString(mobileNo);
        dest.writeValue(cbSelect);


    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectFriendModel myObject = (SelectFriendModel) o;

        if (!(myObject.getMobNo().replaceAll("\\s+", "")).equalsIgnoreCase(getMobNo().replaceAll("\\s+", ""))) return false;
       // if (!myObject.getName().equalsIgnoreCase(getName())) return false;

        return true;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectFriendModel> CREATOR = new Creator<SelectFriendModel>() {
        @Override
        public SelectFriendModel createFromParcel(Parcel in) {
            return new SelectFriendModel(in);
        }

        @Override
        public SelectFriendModel[] newArray(int size) {
            return new SelectFriendModel[size];
        }
    };
}
