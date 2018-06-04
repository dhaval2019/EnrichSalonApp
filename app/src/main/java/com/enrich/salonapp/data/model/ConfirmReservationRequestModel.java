package com.enrich.salonapp.data.model;

import java.util.ArrayList;

public class ConfirmReservationRequestModel {

    public String ReservationNotes;
    public String CenterId;
    public String ReservationId;
    public int BookingSource;
    public ArrayList<SlotBookingsModel> SlotBookings;
}
