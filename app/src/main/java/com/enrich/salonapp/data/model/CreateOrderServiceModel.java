package com.enrich.salonapp.data.model;

public class CreateOrderServiceModel {

    private String ServiceId;
    private String StylistId;

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getStylistId() {
        return StylistId;
    }

    public void setStylistId(String stylistId) {
        StylistId = stylistId;
    }
}
