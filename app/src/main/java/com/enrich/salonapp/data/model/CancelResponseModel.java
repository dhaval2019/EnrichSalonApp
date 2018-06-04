package com.enrich.salonapp.data.model;

public class CancelResponseModel {

    private boolean IsAppointmentStatusSet;
    private int Status;
    private boolean IsCancelChargeApplied;
    private ErrorModel Error;
    private String Message;

    public boolean isAppointmentStatusSet() {
        return IsAppointmentStatusSet;
    }

    public void setAppointmentStatusSet(boolean appointmentStatusSet) {
        IsAppointmentStatusSet = appointmentStatusSet;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isCancelChargeApplied() {
        return IsCancelChargeApplied;
    }

    public void setCancelChargeApplied(boolean cancelChargeApplied) {
        IsCancelChargeApplied = cancelChargeApplied;
    }

    public ErrorModel getError() {
        return Error;
    }

    public void setError(ErrorModel error) {
        Error = error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
