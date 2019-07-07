package com.enrich.salonapp.data.model;

import android.net.Uri;

public class SelectFriendModel {
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
}
