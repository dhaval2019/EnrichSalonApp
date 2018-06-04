package com.enrich.salonapp.data.model;

public class UserExistsModel {

    private Long StatusCode;
    private String Message;
    private String InternalMessage;

    public Long getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(Long statusCode) {
        StatusCode = statusCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getInternalMessage() {
        return InternalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        InternalMessage = internalMessage;
    }
}
