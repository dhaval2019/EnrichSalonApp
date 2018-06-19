package com.enrich.salonapp.data.model.CreateOrder;

public class CreateOrderProductModel {

    private int ProductId;
    private int Quantity;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
