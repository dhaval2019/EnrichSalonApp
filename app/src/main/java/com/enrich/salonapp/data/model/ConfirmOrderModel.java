package com.enrich.salonapp.data.model;

public class ConfirmOrderModel {
    private String TransactionId;
    private String PaymentId;
    private String FirstName;
    private String LastName;
    private String Phone;
    private String EmailAddress;
    private int ModeOfPayment;
    private Double Amount;
    private int PaymentStatus;
    private String ReservationId;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public int getModeOfPayment() {
        return ModeOfPayment;
    }

    public void setModeOfPayment(int modeOfPayment) {
        ModeOfPayment = modeOfPayment;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }
}
