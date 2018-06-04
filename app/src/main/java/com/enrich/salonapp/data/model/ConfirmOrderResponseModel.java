package com.enrich.salonapp.data.model;

public class ConfirmOrderResponseModel {

    private ConfirmOrderRequestModel ConfirmOrder;
    private ErrorModel Error;

    public ConfirmOrderRequestModel getConfirmOrder() {
        return ConfirmOrder;
    }

    public void setConfirmOrder(ConfirmOrderRequestModel confirmOrder) {
        ConfirmOrder = confirmOrder;
    }

    public ErrorModel getError() {
        return Error;
    }

    public void setError(ErrorModel error) {
        Error = error;
    }
}
