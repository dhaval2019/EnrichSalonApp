package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceAppointmentModel implements Parcelable {

    public String AppointmentId;
    public String InvoiceItemId;
    public String CartItemId;
    public String AppointmentSegmentId;
    public String PackageId;
    public String Package;
    public ServiceViewModel Service;
    public String ReducedTimeGroupId;
    public String RequestedTherapistGender;
    public String StartTime;
    public String EndTime;
    public int NonReducedDuration;
    public String StartTimeInCenter;
    public String EndTimeInCenter;
    public String Room;
    public boolean Lock;
    public String Equipment;
    public int Status;
    public RequestedTherapistModel RequestedTherapist;
    public int Quantity;
    public PriceModel Prices;
    public String SCD;
    public String ActualStartTime;
    public String CompletedTime;
    public int Progress;
    public String ParentAppointmentId;
    public ServiceCustomDataModel ServiceCustomData;
    public String ItemActions;
    public int EquivalenceFactor;
    public String EquivalentName;
    public boolean IsMembershipApplied;
    public boolean IsAddOn;
    public String AddonAppointmentId;
    public boolean IsPackageApplied;
    public boolean IsDiscountApplied;
    public boolean CanModifyTherapist;


    protected ServiceAppointmentModel(Parcel in) {
        AppointmentId = in.readString();
        InvoiceItemId = in.readString();
        CartItemId = in.readString();
        AppointmentSegmentId = in.readString();
        PackageId = in.readString();
        Package = in.readString();
        Service = in.readParcelable(ServiceViewModel.class.getClassLoader());
        ReducedTimeGroupId = in.readString();
        RequestedTherapistGender = in.readString();
        StartTime = in.readString();
        EndTime = in.readString();
        NonReducedDuration = in.readInt();
        StartTimeInCenter = in.readString();
        EndTimeInCenter = in.readString();
        Room = in.readString();
        Lock = in.readByte() != 0;
        Equipment = in.readString();
        Status = in.readInt();
        RequestedTherapist = in.readParcelable(RequestedTherapistModel.class.getClassLoader());
        Quantity = in.readInt();
        Prices = in.readParcelable(PriceModel.class.getClassLoader());
        SCD = in.readString();
        ActualStartTime = in.readString();
        CompletedTime = in.readString();
        Progress = in.readInt();
        ParentAppointmentId = in.readString();
        ServiceCustomData = in.readParcelable(ServiceCustomDataModel.class.getClassLoader());
        ItemActions = in.readString();
        EquivalenceFactor = in.readInt();
        EquivalentName = in.readString();
        IsMembershipApplied = in.readByte() != 0;
        IsAddOn = in.readByte() != 0;
        AddonAppointmentId = in.readString();
        IsPackageApplied = in.readByte() != 0;
        IsDiscountApplied = in.readByte() != 0;
        CanModifyTherapist = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AppointmentId);
        dest.writeString(InvoiceItemId);
        dest.writeString(CartItemId);
        dest.writeString(AppointmentSegmentId);
        dest.writeString(PackageId);
        dest.writeString(Package);
        dest.writeParcelable(Service, flags);
        dest.writeString(ReducedTimeGroupId);
        dest.writeString(RequestedTherapistGender);
        dest.writeString(StartTime);
        dest.writeString(EndTime);
        dest.writeInt(NonReducedDuration);
        dest.writeString(StartTimeInCenter);
        dest.writeString(EndTimeInCenter);
        dest.writeString(Room);
        dest.writeByte((byte) (Lock ? 1 : 0));
        dest.writeString(Equipment);
        dest.writeInt(Status);
        dest.writeParcelable(RequestedTherapist, flags);
        dest.writeInt(Quantity);
        dest.writeParcelable(Prices, flags);
        dest.writeString(SCD);
        dest.writeString(ActualStartTime);
        dest.writeString(CompletedTime);
        dest.writeInt(Progress);
        dest.writeString(ParentAppointmentId);
        dest.writeParcelable(ServiceCustomData, flags);
        dest.writeString(ItemActions);
        dest.writeInt(EquivalenceFactor);
        dest.writeString(EquivalentName);
        dest.writeByte((byte) (IsMembershipApplied ? 1 : 0));
        dest.writeByte((byte) (IsAddOn ? 1 : 0));
        dest.writeString(AddonAppointmentId);
        dest.writeByte((byte) (IsPackageApplied ? 1 : 0));
        dest.writeByte((byte) (IsDiscountApplied ? 1 : 0));
        dest.writeByte((byte) (CanModifyTherapist ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceAppointmentModel> CREATOR = new Creator<ServiceAppointmentModel>() {
        @Override
        public ServiceAppointmentModel createFromParcel(Parcel in) {
            return new ServiceAppointmentModel(in);
        }

        @Override
        public ServiceAppointmentModel[] newArray(int size) {
            return new ServiceAppointmentModel[size];
        }
    };
}
