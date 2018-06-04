package com.enrich.salonapp.data.model;

public class OrderResponseModel {

    private String Message;
    private int orderId;
    private Double orderAmount;
    private Double orderTax;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(Double orderTax) {
        this.orderTax = orderTax;
    }
}
