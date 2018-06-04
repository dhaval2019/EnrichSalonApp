package com.enrich.salonapp.data.model;

import java.util.ArrayList;

public class ConfirmReservationResponseModel {

    public boolean IsConfirmed;
    public String ConfirmationId;
    public String ReservationId;
    public ArrayList<SlotBookingsModel> ConfirmedBookings;
    public ErrorModel Error;
}
