package com.enrich.salonapp.data.model;

import java.util.ArrayList;

public class FBEventModel {
    public FBEventModel()
    {

    }
    ArrayList<Referal> referalArrayList = new ArrayList<Referal>();
    String storeName;

    public ArrayList<Referal> getReferalArrayList() {
        return referalArrayList;
    }

    public void setReferalArrayList(ArrayList<Referal> referalArrayList) {
        this.referalArrayList = referalArrayList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    String userName;
    String userPhoneNo;
}
