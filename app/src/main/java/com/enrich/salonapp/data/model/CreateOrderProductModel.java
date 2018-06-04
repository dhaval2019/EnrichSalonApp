package com.enrich.salonapp.data.model;

public class CreateOrderProductModel {

    private Long ProductId;
    private int Quantity;

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
