package com.enrich.salonapp.data.model.Wallet;

public class WalletHistoryModel {

    public String GuestId;
    public String CashbackType;
    public double AppliedAmount;
    public String OrderDate;
    public String OrderTitle;
    public WalletCenterModel Center;

    public WalletHistoryModel() {
    }

    public WalletHistoryModel(double appliedAmount, String orderDate, String orderTitle, WalletCenterModel center) {
        AppliedAmount = appliedAmount;
        OrderDate = orderDate;
        OrderTitle = orderTitle;
        Center = center;
    }
}
