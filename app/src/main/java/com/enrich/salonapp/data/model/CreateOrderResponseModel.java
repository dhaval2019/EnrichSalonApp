package com.enrich.salonapp.data.model;

public class CreateOrderResponseModel {

    private OrderResponseModel Order;
    private PaymentSummaryModel PaymentSummary;

    public OrderResponseModel getOrder() {
        return Order;
    }

    public void setOrder(OrderResponseModel order) {
        Order = order;
    }

    public PaymentSummaryModel getPaymentSummary() {
        return PaymentSummary;
    }

    public void setPaymentSummary(PaymentSummaryModel paymentSummary) {
        PaymentSummary = paymentSummary;
    }
}
