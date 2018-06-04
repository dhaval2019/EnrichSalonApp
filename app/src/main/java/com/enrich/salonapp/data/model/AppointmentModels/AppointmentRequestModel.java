package com.enrich.salonapp.data.model.AppointmentModels;

import java.util.ArrayList;

public class AppointmentRequestModel {

    public int RequiredSlotsCount;
    public String CenterId;
    public String CenterDate;
    public ArrayList<AppointmentSlotBookingsModel> SlotBookings;
}
