package com.enrich.salonapp.data.model.Wallet;

public class WalletCenterModel {

    public int CenterId;
    public String Id;
    public String Name;
    public long GeoLatitude;
    public long GeoLongitude;
    public String Address1;
    public String Address2;
    public String City;
    public String ZipCode;

    public WalletCenterModel(String name) {
        Name = name;
    }
}
