package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.enrich.salonapp.data.model.AppointmentModels.AppointmentSlotBookingsModel;

import java.util.ArrayList;

public class ReserveSlotRequestModel implements Parcelable {

    public boolean CreateInvoice;
    public boolean ForceApplyAutomaticMembership;
    public String CenterId;
    public String CenterTime;
    public ArrayList<AppointmentSlotBookingsModel> SlotBookings;

    public ReserveSlotRequestModel() {
    }

    protected ReserveSlotRequestModel(Parcel in) {
        CreateInvoice = in.readByte() != 0;
        ForceApplyAutomaticMembership = in.readByte() != 0;
        CenterId = in.readString();
        CenterTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (CreateInvoice ? 1 : 0));
        dest.writeByte((byte) (ForceApplyAutomaticMembership ? 1 : 0));
        dest.writeString(CenterId);
        dest.writeString(CenterTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReserveSlotRequestModel> CREATOR = new Creator<ReserveSlotRequestModel>() {
        @Override
        public ReserveSlotRequestModel createFromParcel(Parcel in) {
            return new ReserveSlotRequestModel(in);
        }

        @Override
        public ReserveSlotRequestModel[] newArray(int size) {
            return new ReserveSlotRequestModel[size];
        }
    };
}
