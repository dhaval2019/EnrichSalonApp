package com.enrich.salonapp.data.model;

public class CreateOrderPackageModel {

    private Long PackageId;
    private int Count;

    public Long getPackageId() {
        return PackageId;
    }

    public void setPackageId(Long packageId) {
        PackageId = packageId;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
