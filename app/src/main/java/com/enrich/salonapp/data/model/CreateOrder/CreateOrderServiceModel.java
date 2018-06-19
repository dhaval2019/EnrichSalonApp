package com.enrich.salonapp.data.model.CreateOrder;

public class CreateOrderServiceModel {

    private int ServiceId;
    private String StylistId;

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public String getStylistId() {
        return StylistId;
    }

    public void setStylistId(String stylistId) {
        StylistId = stylistId;
    }
}
