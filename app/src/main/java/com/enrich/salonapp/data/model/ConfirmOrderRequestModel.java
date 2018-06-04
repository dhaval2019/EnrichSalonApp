package com.enrich.salonapp.data.model;

public class ConfirmOrderRequestModel {

    private int orderId;
    private ConfirmOrderModel ConfirmOrder;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ConfirmOrderModel getConfirmOrder() {
        return ConfirmOrder;
    }

    public void setConfirmOrder(ConfirmOrderModel confirmOrder) {
        ConfirmOrder = confirmOrder;
    }
}
