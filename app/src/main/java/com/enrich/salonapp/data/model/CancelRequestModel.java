package com.enrich.salonapp.data.model;

public class CancelRequestModel {
    private int ReasonId;
    private String Comments;

    public int getReasonId() {
        return ReasonId;
    }

    public void setReasonId(int reasonId) {
        ReasonId = reasonId;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
